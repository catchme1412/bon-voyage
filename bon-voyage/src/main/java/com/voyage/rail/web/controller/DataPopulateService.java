package com.voyage.rail.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.Traversal;

import com.vividsolutions.jts.geom.Coordinate;
import com.voyage.rail.core.domain.RailwayStation;
import com.voyage.rail.core.domain.RelationshipTypes;
import com.voyage.rail.core.domain.RouteLeg;
import com.voyage.rail.repository.DatabaseManager;
import com.voyage.service.google.map.GoogleMapQueryResult;
import com.voyage.service.google.map.GoogleMapServiceProvider;
import com.voyage.util.FileUtils;

public class DataPopulateService {

    private DatabaseManager databaseManager;

    public DataPopulateService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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
                System.out.println("Arrive:" + arriveTime);
                if ("Source".equalsIgnoreCase(arriveTime)) {
                    prevDepartureTime = a[5].trim();
                    prevStationCode = fromStationCode = a[1].trim();
                    prevStationName = fromStationName = a[1].trim();
                } else if ("Destination".equalsIgnoreCase(arriveTime)) {
                    break;
                } else {
                    toStationCode = a[1].trim();
                    toStationName = a [2].trim();
                    distance = Double.parseDouble(a[7].trim());

                    double distanceBetween = distance - prevDistance;
                    System.out.println(prevStationCode + ">" + toStationCode + ":" + prevDepartureTime + ":"
                            + arriveTime + ":::" + distanceBetween);
                    prevDepartureTime = a[5].trim();
                    prevStationCode = toStationCode;
                    prevDistance = distance;

                    RailwayStation origin = new RailwayStation();
                    origin.setStationCode(prevStationCode);
                    origin.setCoordinate(getCoordinate(prevStationName));
                    RailwayStation destination = new RailwayStation();
                    destination.setStationCode(prevStationCode);
                    destination.setCoordinate(getCoordinate(toStationName));

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
                    Expander relExpander = Traversal
                            .expanderForTypes(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);
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

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private Coordinate getCoordinate(String locationName) {
        double lat;
        double lng;
        GoogleMapQueryResult r = new GoogleMapServiceProvider().getGeocode(locationName);
        lat = r.getResults().get(0).getGeometry().getLocation().getLat();
        lng = r.getResults().get(0).getGeometry().getLocation().getLng();
        return new Coordinate(lat, lng);
    }
}
