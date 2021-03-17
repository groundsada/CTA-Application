/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * CTAStop class represents a cta station (cta stop)
 * CTAStop is a subclass of GeoLocation because it is a GeoLocation with coordinates
 */
package project;

public class CTAStop extends GeoLocation {
	
	private String name; //name of the cta stop "i.e. Harlem"
	private String description; //description like "elevated" or "underground"
	private boolean wheelchair; //true means stop has wheelchair access
	private int[] lineIndex = new int[7]; //index on each line: red, green, orange, blue, brown, purple, pink
	//lines do not have to be in this specific order. Order is applied from the CTAStops.csv file.
	
	public CTAStop(){ //default constructor
		super();
		name = "Placeholder";
		description = "Placeholder";
		wheelchair = false;
		lineIndex = new int[] {-1,-1,-1,-1,-1,-1,-1}; //red, green, orange, blue, brown, purple, pink
	}
	
	//non-default constructor
	public CTAStop(String name, double lat, double lng, String description, boolean wheelchair, int[] lineIndex) {
		this();
		setName(name); //alternatively, we could just use this.name=name; but since we're creating an mutator anyway why not use it
		setLat(lat);
		setLng(lng);
		setDescription(description);
		setWheelchair(wheelchair);
		setLineIndex(lineIndex);
	}
	
	//second non-default constructor (no array)
	public CTAStop(String name, double lat, double lng, String description, boolean wheelchair, int lineIndexRed,int lineIndexGreen,int lineIndexOrange,int lineIndexBlue,int lineIndexBrown,int lineIndexPurple,int lineIndexPink) { 
		setName(name);
		setLat(lat);
		setLng(lng);
		setDescription(description);
		setWheelchair(wheelchair);
		setLineIndex(0, lineIndexRed);
		setLineIndex(1, lineIndexGreen);
		setLineIndex(2, lineIndexOrange);
		setLineIndex(3, lineIndexBlue);
		setLineIndex(4, lineIndexBrown);
		setLineIndex(5, lineIndexPurple);
		setLineIndex(6, lineIndexPink);
	}
	//accessors
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public boolean hasWheelchair() {
		return wheelchair;
	}
	public int[] getLineIndex() {
		return lineIndex;
	}
	public int getLineIndex(int i) {
		return lineIndex[i];
	}
	
	//mutators
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setWheelchair(boolean wheelchair) {
		this.wheelchair = wheelchair;
	}
	public void setLineIndex(int[] lineIndex) {
		this.lineIndex = lineIndex;
		for (int i = 0 ; i < this.lineIndex.length; i++) { //this is a validator that there are no values less than -1
			if ( this.lineIndex[i] <-1 ) this.lineIndex[i] = -1;
		}
		
	}
	public void setLineIndex(int i, int lineIndex) {
		if (lineIndex >= -1 && i < this.lineIndex.length ) this.lineIndex[i] = lineIndex;
	}
	
	//validators
	@Override
	public boolean validLat(double lat) { //validLat checks if geolocation has a valid Chicago latitude
		//This is an override of the validLat method for GeoLocation. That one checks if lat is any valid earth lat.
		//This one checks if lat is valid Chicagoland lat.
		return (lat >= 41 && lat <= 42.5 ) ? true : false;
	}
	
	@Override
	public boolean validLng(double lng) { //validLng checks if geolocation has valid longitude.
		//This is overridden in the CTAStop class where it checks if longitude is valid in Chicago.
		return (lng >= -88.5 && lng <= -87 ) ? true : false; //ternary operation
	}
	
	@Override
	//toString method returns string that has all of the stop's information
	public String toString() {
		return "\"" +  name + "\" " + description + " " + getLat() + " " + getLng();
	}
	
	//returns a .csv style string that can be used for i/o
	public String toCSVString() {
		//Example: Central,41.887389,-87.76565,elevated,TRUE,-1,4,-1,-1,-1,-1,-1
		return name +","+getLat()+","+getLng()+","+description+","+ Boolean.toString(hasWheelchair()).toUpperCase()
				+ ","+ lineIndex[0]+ ","+ lineIndex[1]+ ","+ lineIndex[2]+ ","+ lineIndex[3]+ ","+ lineIndex[4]
						+ ","+ lineIndex[5]+ ","+ lineIndex[6];
	}
	
	//returns true if two stops are equals in all properties(identical)
	public boolean equals(CTAStop g) {
		for (int i= 0 ; i <7;i++) {
			if ( this.getLineIndex(i) != g.getLineIndex(i)) return false;
		}
		return super.equals(g) && this.getName().equalsIgnoreCase(g.getName())  && this.hasWheelchair() == g.hasWheelchair() && getDescription().equalsIgnoreCase(g.getDescription());
	}
	
}
