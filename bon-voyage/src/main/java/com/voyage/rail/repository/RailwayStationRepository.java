package com.voyage.rail.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import com.voyage.rail.core.domain.RailwayStation;

public interface RailwayStationRepository extends GraphRepository<RailwayStation>,
        RelationshipOperationsRepository<RailwayStation> {
    RailwayStation findByStationCode(String stationCode);
}