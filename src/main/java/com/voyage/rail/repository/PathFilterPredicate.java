package com.voyage.rail.repository;

import java.util.Iterator;

import org.apache.commons.collections.Predicate;
import org.joda.time.LocalTime;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

public class PathFilterPredicate implements Predicate {

	@Override
	public boolean evaluate(Object arg0) {
		Path p = (Path) arg0;
		Iterable<Relationship> r = p.relationships();
		Iterator<Relationship> iterator = r.iterator();
		do {
			Relationship arrive = iterator.next();
			if (iterator.hasNext()) {
				Relationship depart = iterator.next();
				String arriveTrainNumber = (String) arrive.getProperty("trainNumber");
				String departTrainNumber = (String) depart.getProperty("trainNumber");
				// train change
				if (!arriveTrainNumber.equals(departTrainNumber)) {
					LocalTime arriveTime = (LocalTime) arrive.getProperty("arrivalTime");
					LocalTime departureTime = (LocalTime) arrive.getProperty("departureTime");
					if (departureTime.isAfter(arriveTime)) {
						return true;
					} else {
						if (arriveTime.isAfter(LocalTime.parse("20:00"))
								&& departureTime.isBefore(LocalTime.parse("02:00"))) {
							return true;
						}

					}
				}
			}

		} while (iterator.hasNext());
		return false;
	}

}
