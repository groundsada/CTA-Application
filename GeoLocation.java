/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * GeoLocation class represents geographical locations with coordinates
 * This is an implementation of the GeoLocation class we created in lab 4
 */
package project;

public class GeoLocation {
	private double lat; //latitude of geolocation
	private double lng; //longitude of geolocation
	
	public GeoLocation () { //default constructor
		lat = 0;
		lng = 0;
	}
	
	public GeoLocation(double lat, double lng) { //non-default constructor
		this();
		setLat(lat);
		setLng(lng);
	}
	
	public double getLat() { //accessor method for lat
		return lat;
	}
	
	public double getLng() { //accessor method for lng 
		return lng;
	}
	
	public void setLat(double lat) { //mutator method for lat
		if (validLat(lat)) { //calls the bool .validLat method, which checks if the latitude is correct
			this.lat=lat;
		}
	}
	
	public void setLng(double lng) { //mutator method for lng
		if (validLng(lng)) { //calls the bool .validLng method, which checks if the longitude is correct
			this.lng=lng;
		}
	}
	
	public boolean validLat(double lat) { //validLat checks if geolocation has valid latitude.
		return (lat >= -90 && lat <= 90 ) ? true : false;
	}
	public boolean validLng(double lng) { //validLng checks if geolocation has valid longitude.
		return (lng >= -180 && lng <= 180 ) ? true : false; //ternary operation
	}
	
	public String toString() { //toString method returns descriptive string
		return "(" + Double.toString(lat) + ", " + Double.toString(lng) +")";
	}
	
	public boolean equals(GeoLocation g) { //checks if two geolocations are equal
		return lat==g.lat && lng == g.lng;
	}
	
	//calculates the distance between 2 geolocations using the Haversine formula
	public double calcDistance (GeoLocation g) {
        double distance = Math.pow(Math.sin((Math.toRadians(lat-g.lat)) / 2), 2) +  Math.pow(Math.sin((Math.toRadians(lng-g.lng)) / 2), 2) *  
        		Math.cos(Math.toRadians(lat)) *  Math.cos(Math.toRadians(g.lat));  
        distance = 2 * Math.asin(Math.sqrt(distance));
        distance *= 3961; //radius of earth in miles
        return distance;
        
        //We couldn't have used dist = sqrt(Math.pow(lat-g.lat, 2) + Math.pow(lng-g.lng, 2)); which calculates the distance between two point on a regular plane
        //The results would have been off. Even though we don't care about the actual distance in miles, we only need a value to compare distances, the results would have been off.
        //The reason it doesn't work it that the distance between 2 lat lines isn't the same as 2 lng lines.
	}
	
}
