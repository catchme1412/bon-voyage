package com.raj.util.rating;


/**
 * @see http://en.wikipedia.org/wiki/Elo_rating_system#Mathematical_details
 * 
 * @author rkv
 *
 */
public class Rating  {
	
	private double rating;
	private static final double HIGHEST_RATING = 3000 ;//The highest ever FIDE rating was 2872
	
	public Rating(double initialRating) {
		this.rating = initialRating;
	}

	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public double getRatingAsPercentage() {
		return this.rating/HIGHEST_RATING;
	}

}