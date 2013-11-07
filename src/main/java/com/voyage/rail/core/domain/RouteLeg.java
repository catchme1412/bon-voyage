package com.voyage.rail.core.domain;

import org.joda.time.LocalTime;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "ROUTE")
public class RouteLeg {

    @GraphId
    Long id;

    @StartNode
    private RailwayStation from;

    @EndNode
    private RailwayStation to;

    private String trainNumber;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    private double distance;

    private int day;

    private String trainType;

    public RailwayStation getFrom() {
        return from;
    }

    public void setFrom(RailwayStation from) {
        this.from = from;
    }

    public RailwayStation getTo() {
        return to;
    }

    public void setTo(RailwayStation to) {
        this.to = to;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        RouteLeg other = (RouteLeg) obj;
        return from.equals(other.getFrom()) && to.equals(other.getTo()) && arrivalTime.equals(other.getArrivalTime());
    }

    @Override
    public int hashCode() {
        return from.hashCode() + 17 * to.hashCode();
    }

}
