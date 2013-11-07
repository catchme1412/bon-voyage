package com.voyage.rail.core.domain;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.voyage.rail.repository.RailwayStationRepository;

/**
 * @author mh
 * @since 04.03.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/rail-test-context.xml"})
@Transactional
public class RailDomainTests {


    @Autowired Neo4jOperations template;
    
    @Autowired
    RailwayStationRepository railwayStationRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        RailwayStation from = new RailwayStation();
        from.setStationCode("SBC");
        template.save(from);
        RailwayStation result = railwayStationRepository.findByStationCode("SBC");
        System.out.println(result.getStationCode());
    }
    
   
    
    @Test
    public void test2() {
        RailwayStation from = new RailwayStation();
        from.setStationCode("ABC");
        template.save(from);
        RailwayStation to = new RailwayStation();
        to.setStationCode("DEF");
        template.save(to);
        
        LocalTime departureTime = null;
        LocalTime arrivalTime = null;
        double distance = 0;
        RouteLeg route = from.addRoute(to, "T1", arrivalTime, departureTime, distance);
        template.save(route);
        
        RouteLeg rr = railwayStationRepository.getRelationshipBetween(from, to, RouteLeg.class, "ROUTE");
        System.out.println(rr.getTrainNumber());
        
        
    }
}
