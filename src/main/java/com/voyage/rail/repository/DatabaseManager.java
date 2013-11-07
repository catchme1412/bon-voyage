package com.voyage.rail.repository;

import java.util.List;

import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.springframework.data.neo4j.template.Neo4jOperations;

import com.vividsolutions.jts.geom.Coordinate;
import com.voyage.rail.core.domain.RailwayStation;
import com.voyage.rail.core.domain.RouteLeg;

public interface DatabaseManager {

	public void addRailwayStation(RailwayStation station);

	public RailwayStation getRailwayStation(String stationCode);
	
	public Coordinate getCoordinate(String stationName);

	public Neo4jOperations getTemplate();

	public void addRouteLeg(RouteLeg leg);

	public List<RouteLeg> getRouteLeg(RailwayStation from, RailwayStation to);

	List<Path> getAllPathsFrom(RailwayStation from);

	Iterable<Path> getAllSimplePath(Node from, Node to);
	
	public Node getNode(RailwayStation station);

    Iterable<WeightedPath> getAstar(Node from, Node to);
}
