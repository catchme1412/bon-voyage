package com.voyage.rail.core.domain;

import java.util.List;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.voyage.rail.repository.DatabaseManager;
import com.voyage.rail.repository.RailwayStationRepository;

/**
 * @author mh
 * @since 04.03.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/rail-test-context.xml" })
@Transactional
public class RailDomainTests {

	@Autowired
	Neo4jOperations template;

	@Autowired
	DatabaseManager databaseManager;

	@Autowired
	RailwayStationRepository railwayStationRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		RailwayStation from = new RailwayStation();
		from.setStationCode("SBC");
		databaseManager.addRailwayStation(from);
		RailwayStation result = railwayStationRepository.findByStationCode("SBC");
		System.out.println(result.getStationCode());
	}

	@Test
	public void testfindByStationName() {
		RailwayStation from = new RailwayStation();
		from.setStationCode("SBC");
		from.setStationName("Bangalore");
		databaseManager.addRailwayStation(from);
		RailwayStation result = railwayStationRepository.findByStationName("Bangalore");
		Assert.notNull(result);
		System.out.println(result.getStationCode());
	}

	@Test
	public void test2() {
		RailwayStation from = new RailwayStation();
		from.setStationCode("ABC");
		databaseManager.addRailwayStation(from);
		RailwayStation to = new RailwayStation();
		to.setStationCode("DEF");
		databaseManager.addRailwayStation(to);

		LocalTime departureTime = null;
		LocalTime arrivalTime = null;
		double distance = 0;
		RouteLeg route = from.addRoute(to, "T1", arrivalTime, departureTime, distance);
		databaseManager.addRouteLeg(route);

		RouteLeg rr = railwayStationRepository.getRelationshipBetween(from, to, RouteLeg.class, "ROUTE");
		System.out.println(rr.getTrainNumber());
		
		Iterable<Path> r = databaseManager.getAllSimplePath(from, to);
		for (Path  p : r) {
			System.out.println(p.startNode());
			System.out.println(p.endNode());
		}

	}
}
