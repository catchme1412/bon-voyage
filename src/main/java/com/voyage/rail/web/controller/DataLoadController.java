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

//		// DatabaseManager objA = new DatabaseManager();
//		RailwayStation railwayStation = new RailwayStation();
//		railwayStation.setStationCode("SBC");
//		Coordinate c = new Coordinate(17, 18);
//		railwayStation.setCoordinate(c);
//		databaseManager.addRailwayStation(railwayStation);
//		RailwayStation railwayStation2 = new RailwayStation();
//		railwayStation2.setStationCode("SA");
//		Coordinate c2 = new Coordinate(17, 18);
//		railwayStation2.setCoordinate(c2);
//		databaseManager.addRailwayStation(railwayStation2);
//
//		RouteLeg leg = new RouteLeg();
//		leg.setFrom(railwayStation);
//		leg.setTo(railwayStation2);
//		databaseManager.addRouteLeg(leg);
//		databaseManager.getAllPathsFrom(railwayStation);
//
//		RailwayStation retrievedMovie = databaseManager.getRailwayStation("SBC");
//		System.out.println(retrievedMovie.getStationCode());
		try {
//			loadIntialData(databaseManager);
		    new DataPopulateService(databaseManager).loadIntialData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hello";

	}
}
