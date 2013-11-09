package com.voyage.rail.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalTime;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import com.vividsolutions.jts.geom.Coordinate;
import com.voyage.rail.core.domain.RailwayStation;
import com.voyage.rail.core.domain.RouteLeg;
import com.voyage.rail.repository.DatabaseManager;
import com.voyage.service.google.map.GoogleMapQueryResult;
import com.voyage.service.google.map.GoogleMapServiceProvider;
import com.voyage.util.FileUtils;

public class DataPopulateService {

	private DatabaseManager databaseManager;
	private Map<String, Coordinate> coordinateMap;

	public DataPopulateService(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
		coordinateMap = new HashMap<String, Coordinate>();
	}

	public void loadIntialData() throws IOException {

		InputStream files = FileUtils.loadAsResource("trains.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(files, "UTF-8"));
		String strLine;
		// skip the first line
		br.readLine();
		while ((strLine = br.readLine()) != null) {
			System.out.println(strLine);
			String[] a = strLine.split("\t");
			String trainNumber = a[0].trim();
			processRouteFile(trainNumber);
			GoogleMapQueryResult r = new GoogleMapServiceProvider().getGeocode(a[2].trim());
		}
	}

	private void processRouteFile(String trainNumber) {

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
			String prevStationName = null;
			String prevDepartureTime = null;
			double prevDistance = 0;

			while ((strLine = br.readLine()) != null) {
				String a[] = strLine.split("\t");
				String arriveTime = a[4].trim();
				if ("Source".equalsIgnoreCase(arriveTime)) {
					prevDepartureTime = a[5].trim();
					prevStationCode = fromStationCode = a[1].trim();
					prevStationName = fromStationName = a[2].trim();
				} else if ("Destination".equalsIgnoreCase(arriveTime)) {
					break;
				} else {
					toStationCode = a[1].trim();
					toStationName = a[2].trim();
					distance = Double.parseDouble(a[7].trim());

					double distanceBetween = distance - prevDistance;
					System.out.println(prevStationCode + ">" + toStationCode + ":" + prevDepartureTime + "-"
							+ arriveTime + ":distance:" + distanceBetween);
					
					RailwayStation origin = databaseManager.getRailwayStation(prevStationCode);
					if (origin == null) {
						origin = new RailwayStation();
						origin.setStationCode(prevStationCode);
						origin.setStationName(prevStationName);
						origin.setCoordinate(getCoordinate(prevStationName));
						databaseManager.addRailwayStation(origin);
					}

					RailwayStation destination = databaseManager.getRailwayStation(toStationCode);
					if (destination != null) {
						destination = new RailwayStation();
						destination.setStationCode(toStationCode);
						destination.setStationName(toStationName);
						destination.setCoordinate(getCoordinate(toStationName));
						databaseManager.addRailwayStation(destination);
					}
					// databaseManager.addRailwayStation(origin);
					RouteLeg leg = new RouteLeg();
					leg.setFrom(origin);
					leg.setTo(destination);
					leg.setTrainNumber(trainNumber);
					leg.setDepartureTime(LocalTime.parse(prevDepartureTime));
					leg.setArrivalTime(LocalTime.parse(arriveTime));
					leg.setDistance(distanceBetween);
					leg.setTrainType("EXP");
					databaseManager.addRouteLeg(leg);

					// origin.getRoutes().add(leg);
					// destination.getRoutes().add(leg);
					// origin.persist();
					RailwayStation t = databaseManager.getRailwayStation(prevStationCode);
					System.out.println(t.getStationCode());
					// leg.setDay(a[])

					// t = databaseManager.getRailwayStation(prevStationCode);
					// System.out.println(t.getStationCode());
					Iterable<Path> mm = databaseManager.getAllSimplePath(origin, destination);
					for (Path m : mm) {
						StringBuffer proposedPath = new StringBuffer();
						for (Relationship re : m.relationships()) {
							System.out.println(re.getProperty("trainNumber"));
						}
					}
					prevDepartureTime = a[5].trim();
					prevStationCode = toStationCode;
					prevDistance = distance;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Coordinate getCoordinate(String locationName) {
		double lat = 10;
		double lng = 10;
		Coordinate c = databaseManager.getCoordinate(locationName);
		if (c != null) {
			System.out.println("From cache:" + locationName);
			return c;
		} else {
			c = coordinateMap.get(locationName);
			if (c == null) {
				locationName = locationName.replaceAll(" JN$", "");
				GoogleMapQueryResult r = new GoogleMapServiceProvider().getGeocode(locationName);
				lat = r.getResults().get(0).getGeometry().getLocation().getLat();
				lng = r.getResults().get(0).getGeometry().getLocation().getLng();
				c = new Coordinate(lat, lng);
			}
			return c;
		}
	}
}
