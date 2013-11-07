package com.voyage.rail.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vividsolutions.jts.geom.Coordinate;
import com.voyage.rail.core.domain.RailwayStation;
import com.voyage.rail.core.domain.RelationshipTypes;
import com.voyage.rail.core.domain.RouteLeg;
import com.voyage.rail.repository.DatabaseManager;
import com.voyage.service.google.map.GoogleMapQueryResult;
import com.voyage.service.google.map.GoogleMapServiceProvider;
import com.voyage.util.FileUtils;

//import com.voyage.service.google.map.GoogleMapQueryResult;
//import com.voyage.service.google.map.GoogleMapServiceProvider;
//import com.voyage.util.FileUtils;

@Controller
@RequestMapping("/init")
public class DataLoadController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String printWelcome(ModelMap model, HttpServletRequest req) {
		DatabaseManager databaseManager = null;
		//
		ServletContext application = req.getServletContext();
		if (application.getAttribute("databaseManager") == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			databaseManager = (DatabaseManager) context.getBean("databaseManager");
			application.setAttribute("databaseManager", databaseManager);
		} else {
			databaseManager = (DatabaseManager) application.getAttribute("databaseManager");
		}

		// DatabaseManager objA = new DatabaseManager();
		RailwayStation railwayStation = new RailwayStation();
		railwayStation.setStationCode("SBC");
		Coordinate c = new Coordinate(17, 18);
		railwayStation.setCoordinate(c);
		databaseManager.addRailwayStation(railwayStation);
		RailwayStation railwayStation2 = new RailwayStation();
		railwayStation2.setStationCode("SA");
		Coordinate c2 = new Coordinate(17, 18);
		railwayStation2.setCoordinate(c2);
		databaseManager.addRailwayStation(railwayStation2);

		RouteLeg leg = new RouteLeg();
		leg.setFrom(railwayStation);
		leg.setTo(railwayStation2);
		databaseManager.addRouteLeg(leg);
		databaseManager.getAllPathsFrom(railwayStation);

		RailwayStation retrievedMovie = databaseManager.getRailwayStation("SBC");
		System.out.println(retrievedMovie.getStationCode());
		try {
			loadIntialData(databaseManager);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hello";

	}

	private void loadIntialData(DatabaseManager databaseManager) throws IOException {

		InputStream files = FileUtils.loadAsResource("trains.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(files, "UTF-8"));
		String strLine;
		// skip the first line
		br.readLine();
		while ((strLine = br.readLine()) != null) {
			System.out.println(strLine);
			String[] a = strLine.split("\t");
			String trainNumber = a[0].trim();
			processRouteFile(trainNumber, databaseManager);
			System.out.println("TrainNo:" + a[0].trim());
			System.out.println("Name:" + a[1].trim());
			System.out.println("Loc:" + a[2].trim());
			GoogleMapQueryResult r = new GoogleMapServiceProvider().getGeocode(a[2].trim());
			System.out.println("LAT:" + r.getResults().get(0).getGeometry().getLocation().getLat());
			System.out.println("LNG:" + r.getResults().get(0).getGeometry().getLocation().getLng());

		}
	}

	private void processRouteFile(String trainNumber, DatabaseManager databaseManager) {

		try {

			InputStream files = FileUtils.loadAsResource("route_" + trainNumber + ".csv");
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(files, "UTF-8"));
			String strLine;
			// skip the first line
			br.readLine();
			// 1 SBC BANGALORE CY JN 1 Source 20:00 0 1
			String fromStationCode;
			String fromStationName;
			String toStationCode;
			String toStationName;

			double distance;
			int daySinceSource;
			String prevStationCode = null;
			String prevStationName;
			String prevDepartureTime = null;
			double prevDistance = 0;

			while ((strLine = br.readLine()) != null) {
				String a[] = strLine.split("\t");
				String arriveTime = a[4].trim();
				System.out.println("Arrive:" + arriveTime);
				if ("Source".equalsIgnoreCase(arriveTime)) {
					prevDepartureTime = a[5].trim();
					prevStationCode = fromStationCode = a[1].trim();
					prevStationName = fromStationName = a[1].trim();
				} else if ("Destination".equalsIgnoreCase(arriveTime)) {
					break;
				} else {
					toStationCode = a[1].trim();
					distance = Double.parseDouble(a[7].trim());

					double distanceBetween = distance - prevDistance;
					System.out.println(prevStationCode + ">" + toStationCode + ":" + prevDepartureTime + ":"
							+ arriveTime + ":::" + distanceBetween);
					prevDepartureTime = a[5].trim();
					prevStationCode = toStationCode;
					prevDistance = distance;

					GoogleMapQueryResult r = new GoogleMapServiceProvider().getGeocode(a[2].trim());
					double lat = r.getResults().get(0).getGeometry().getLocation().getLat();
					System.out.println("LAT:" + lat);
					double lng = r.getResults().get(0).getGeometry().getLocation().getLng();
					System.out.println("LNG:" + lng);

					RailwayStation origin = new RailwayStation();
					origin.setStationCode(prevStationCode);
					origin.setCoordinate(new Coordinate(lat, lng));
					RailwayStation destination = new RailwayStation();
					destination.setStationCode(prevStationCode);
					r = new GoogleMapServiceProvider().getGeocode(prevStationCode);
					lat = r.getResults().get(0).getGeometry().getLocation().getLat();
					System.out.println("DE:LAT:" + lat);
				   lng = r.getResults().get(0).getGeometry().getLocation().getLng();
					System.out.println("D:LNG:" + lng);
					destination.setCoordinate(new Coordinate(lat, lng));
					RouteLeg leg = new RouteLeg();
					leg.setFrom(origin);
					leg.setTo(destination);
					leg.setTrainNumber(trainNumber);
					leg.setDistance(distanceBetween);
					leg.setTrainType("EXP");

					// origin.getRoutes().add(leg);
					// destination.getRoutes().add(leg);
					// origin.persist();
					databaseManager.addRailwayStation(origin);
					databaseManager.addRailwayStation(destination);
					RailwayStation t = databaseManager.getRailwayStation(prevStationCode);
					System.out.println(t.getStationCode());
					// leg.setDay(a[])
					databaseManager.addRouteLeg(leg);

					t = databaseManager.getRailwayStation(prevStationCode);
					System.out.println(t.getStationCode());
					// databaseManager.getTemplate().getGraphDatabase()
					// for(Path
					// path:td.traverse(databaseManager.getNode(origin))) {
					//
					// }
					// GraphAlgoFactory.allSimplePaths(expander, maxDepth)
					Expander relExpander = Traversal.expanderForTypes(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);
					relExpander.add(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);
					PathFinder<Path> p2 = GraphAlgoFactory.allSimplePaths(relExpander, 100);
					Iterable<Path> mm = p2.findAllPaths(databaseManager.getTemplate().getNode(origin.getId()),
							databaseManager.getTemplate().getNode(destination.getId()));
					for (Path m : mm) {
						StringBuffer proposedPath = new StringBuffer();
						for (Relationship re : m.relationships()) {
							System.out.println(re.getProperty("trainNumber"));
						}
					}
					//
					//
					//
					//
					//
					//
					// }
					System.out.println("FFFFFFFFFFFFFFFFFFFDDDDDDDDDDDDDDDDDSSSSSSSS");
					// Iterable<Path> ree = databaseManager.getAllPathsBetween(
					// databaseManager.getTemplate().getNode(origin.getId()),
					// databaseManager.getTemplate()
					// .getNode(destination.getId()));
					// for (Path p : ree) {
					// System.out.println(p.startNode());
					// }\

				}
				// if ("Source".equalsIgnoreCase(arriveTime)) {
				// prevDepartureTime = departureTime = a[5].trim();
				// prevStationCode = fromStationCode = a[1].trim();
				// prevStationName = fromStationName = a[1].trim();
				//
				// continue;
				// } else if ("Destination".equalsIgnoreCase(arriveTime)){
				// break;
				// } else {
				// toStationCode = a[1].trim();
				// System.out.println(prevStationCode +">" + toStationCode + ":"
				// + prevDepartureTime + ":"+ arriveTime);
				// prevStationCode = toStationCode;
				// prevDepartureTime = departureTime = a[4].trim();
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
