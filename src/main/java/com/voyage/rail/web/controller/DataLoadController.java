package com.voyage.rail.web.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.voyage.rail.repository.DatabaseManager;

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
