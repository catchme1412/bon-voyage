//package com.voyage.rail.repository;
//
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.log4j.spi.Configurator;
//import org.neo4j.kernel.GraphDatabaseAPI;
//import org.neo4j.kernel.InternalAbstractGraphDatabase.Configuration;
//
//
//public class Neo4jServerConfigurator implements Configurator {
//	private final EmbeddedServerConfigurator configurator;
//
//	public Neo4jServerConfigurator(GraphDatabaseAPI db, Map<String, Object> initialProperties) {
//		configurator = new EmbeddedServerConfigurator(db);
//		for (Map.Entry<String, Object> entry : initialProperties.entrySet()) {
//			configurator.configuration().addProperty(entry.getKey(), entry.getValue());
//		}
//	}
//
//	public Object getProperty(String property) {
//		return configurator.configuration().getProperty(property);
//	}
//
//	public void setProperty(String property, Object value) {
//		configurator.configuration().setProperty(property, value);
//	}
//
//	public void addProperty(String property, Object value) {
//		configurator.configuration().addProperty(property, value);
//	}
//
//	@Override
//	public Configuration configuration() {
//		return configurator.configuration();
//	}
//
//	@Override
//	public Map<String, String> getDatabaseTuningProperties() {
//		return configurator.getDatabaseTuningProperties();
//	}
//
//	@Override
//	public Set<ThirdPartyJaxRsPackage> getThirdpartyJaxRsClasses() {
//		return configurator.getThirdpartyJaxRsClasses();
//	}
//}