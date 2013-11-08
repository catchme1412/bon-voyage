package com.voyage.rail.core.domain;

import com.raj.util.rating.Rating;

public class RailwayStationRating {

	private Rating amenetiesRating;
	
	private Rating reachabilityRating;
	
	private Rating securityRating;
	
	private int numberOfTrainsPassingThrough;

	public Rating getAmenetiesRating() {
		return amenetiesRating;
	}

	public void setAmenetiesRating(Rating amenetiesRating) {
		this.amenetiesRating = amenetiesRating;
	}

	public Rating getReachabilityRating() {
		return reachabilityRating;
	}

	public void setReachabilityRating(Rating reachabilityRating) {
		this.reachabilityRating = reachabilityRating;
	}

	public Rating getSecurityRating() {
		return securityRating;
	}

	public void setSecurityRating(Rating securityRating) {
		this.securityRating = securityRating;
	}

	public int getNumberOfTrainsPassingThrough() {
		return numberOfTrainsPassingThrough;
	}

	public void setNumberOfTrainsPassingThrough(int numberOfTrainsPassingThrough) {
		this.numberOfTrainsPassingThrough = numberOfTrainsPassingThrough;
	}
	
}
