package com.voyage.rail.core.domain;

import org.joda.time.LocalTime;
import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.vividsolutions.jts.geom.Coordinate;

@NodeEntity
public class RailwayStation {

	@GraphId
	private Long id;

	@Indexed(unique = true)
	private String stationCode;

	private String stationName;

	private Coordinate coordinate;

	// @RelatedToVia
	// private Collection<RouteLeg> routes;

	public RailwayStation() {
		// routes = new HashSet<RouteLeg>();
//		stationCode = "NIL";
//		stationName = "NIL";
//		coordinate = new Coordinate();
	}

	public RailwayStation(Node node) {
		id = node.getId();
		stationCode = (String) node.getProperty("stationCode", "NIL");
		stationName = (String) node.getProperty("stationName", "NIL");
		coordinate = (Coordinate) node.getProperty("coordinate", new Coordinate());
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	@Override
	public String toString() {
		return String.format("[%s]", getStationCode());
	}

	@Override
	public boolean equals(Object obj) {
		RailwayStation other = (RailwayStation) obj;

		return this.id.intValue() == other.getId().intValue() || (stationCode.equals(other.getStationCode()));
	}

	@Override
	public int hashCode() {
		return stationCode.hashCode();
	}

	// public Iterable<RouteLeg> getRoutes() {
	// return routes;
	// }
	//
	public RouteLeg addRoute(RouteLeg route, String roleName) {
		// routes.add(route);
		return route;
	}

	public RouteLeg addRoute(RailwayStation to, String trainNumber, LocalTime arrivalTime, LocalTime departureTime,
			double distance) {
		RouteLeg r = new RouteLeg();
		r.setFrom(this);
		r.setTo(to);
		r.setTrainNumber(trainNumber);
		r.setArrivalTime(arrivalTime);
		r.setDepartureTime(departureTime);
		r.setDistance(distance);
		return addRoute(r, "ROUTE");
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
