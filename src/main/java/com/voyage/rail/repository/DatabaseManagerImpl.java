package com.voyage.rail.repository;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.gis.spatial.SimplePointLayer;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.encoders.SimplePointEncoder;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.EstimateEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;
import com.voyage.rail.core.domain.RailwayStation;
import com.voyage.rail.core.domain.RelationshipTypes;
import com.voyage.rail.core.domain.RouteLeg;

@Transactional
public class DatabaseManagerImpl implements InitializingBean, DatabaseManager {

    @Autowired
    private RailwayStationRepository railwayStationRepository;

    @Autowired
    private EmbeddedGraphDatabase graphDatabaseService;

    private SpatialDatabaseService spatialDb;

    private SimplePointLayer stationLayer;

    @Autowired
    Neo4jOperations template;

    @Override
    public void addRailwayStation(RailwayStation station) {
        template.save(station);
        stationLayer.add(station.getCoordinate());
    }

    public void addRouteLeg(RouteLeg leg) {
        template.save(leg);
    }

    @Override
    public RailwayStation getRailwayStation(String stationCode) {
        return railwayStationRepository.findByStationCode(stationCode);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        spatialDb = new SpatialDatabaseService(graphDatabaseService);
        stationLayer = (SimplePointLayer) spatialDb.getOrCreateLayer("stationLayer", SimplePointEncoder.class,
                SimplePointLayer.class, "lon:lat");

    }

    @Override
    public Neo4jOperations getTemplate() {
        return template;
    }

    @Override
    public List<Path> getAllPathsFrom(RailwayStation from) {
        List<Path> legs = new ArrayList<Path>();
        TraversalDescription traversal = Traversal.description().uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);

        Traverser t = traversal.traverse(template.getNode(from.getId()));
        for (Path position : t) {
            legs.add(position);
        }

        return legs;
    }

    @Override
    public List<RouteLeg> getRouteLeg(RailwayStation from, RailwayStation to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Need to be implemented");
    }

    public Node getNode(RailwayStation station) {
        return template.getNode(station.getId());
    }

    @Override
    public Iterable<Path> getAllSimplePath(Node from, Node to) {
        // TODO Auto-generated method stub
        Expander relExpander = Traversal.expanderForTypes(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);

        relExpander.add(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);

        // CostEvaluator<Double> costEval = ;
        // EstimateEvaluator<Double> estimateEval;
        // PathFinder<WeightedPath> shortestPath =
        // GraphAlgoFactory.aStar(relExpander, costEval, estimateEval);
        PathFinder<Path> pf = GraphAlgoFactory.allSimplePaths(relExpander, 200);
        return pf.findAllPaths(from, to);
    }

    @Override
    public Iterable<WeightedPath> getAstar(Node from, Node to) {
        // TODO Auto-generated method stub
        Expander relExpander = Traversal.expanderForTypes(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);

        relExpander.add(RelationshipTypes.RAIL_ROUTE, Direction.OUTGOING);

        CostEvaluator<Double> costEval = null;
        EstimateEvaluator<Double> estimateEval = null;
        PathFinder<WeightedPath> shortestPath = GraphAlgoFactory.aStar(relExpander, costEval, estimateEval);
        return shortestPath.findAllPaths(from, to);
    }

	@Override
	public Coordinate getCoordinate(String stationName) {
		RailwayStation found = railwayStationRepository.findByStationName(stationName);
		
		return found != null ? found.getCoordinate() : null;
	}

}
