/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * CTALine class represents CTA Lines (Red line, blue line...)
 * CTALines have name, index (index of line in network), and an ArrayList of stops.
 */
package project;
import java.util.*;

public class CTALine {
	private String name; //name of line, for example: red line, blue line...
	private ArrayList<CTAStop> stops; //list of stops on that line
	private int index; //index of line i.e. red is 0
	//the index is the corresponding order of lines in the input csv file
	
	public CTALine() { //default constructor
		name = "Placeholder";
		stops = new ArrayList<CTAStop>(0);
		index = 0;
	}
	
	public CTALine(String name, int index) { //non-default constructor
		this();
		setName(name);
		setIndex(index);
	}
	public CTALine(String name, int index, ArrayList<CTAStop> stops) { //2nd non-default constructor
		this();
		setName(name);
		setStops(stops);
		setIndex(index);
	}
	
	//accessors
	public String getName() {
		return name;
	}
	public int getIndex() {
		return index;
	}
	public ArrayList<CTAStop> getStops(){
		return stops;
	}
	
	//mutators
	public void setName(String name) {
		this.name = name;
	}
	public void setIndex(int index) {
		if (index >= 0)  this.index = index;
	}
	public void setStops(ArrayList<CTAStop> stops) {
		this.stops = stops;
	}
	
	//toString method returns string with line name and # of stops.
	//no need to return other information in toString
	public String toString() {
		return "Line " + name + " - has " + stops.size() + " stop(s).";
	}
	
	//if two lines are identical in all properties, including all stops, returns true
	public boolean equals(CTALine line) {
		if (this.name.equals(line.name) && this.stops.size() == line.stops.size() && this.index == line.getIndex()) {
			for ( int i = 0 ; i < this.stops.size() ; i++) {
				if (!this.stops.get(i).equals(line.stops.get(i))) return false;
			}
			return true;
		}
		return false;
	}
	
	//adds a stop to a line
	public void addStop(CTAStop stopToAdd) {
		//in order to add station to line, it must have > -1 index
		if (stopToAdd.getLineIndex(index) > -1 ) {
			//we have 3 scenarios, 1) list of stops on line is shorter than the stop we're adding
			//we bridge the gap with placeholders(default constructor stops)
			if (getStops().size() == 0 && stopToAdd.getLineIndex(index) == 0 ) { //if line has no stops, just add the stop directly
				getStops().add(stopToAdd);
			}
			else if (getStops().size() <= stopToAdd.getLineIndex(index)) {
				for (int i  = getStops().size(); i < stopToAdd.getLineIndex(index) ; i++ ) {
					getStops().add(new CTAStop());
				}
				getStops().add(stopToAdd);
			}
			else { //scenario 2) there is a placeholder in the index we're trying to add the stop to
				// we replace the placeholder with the stop we're adding
				if (getStops().get(stopToAdd.getLineIndex(index)).getName().equalsIgnoreCase("Placeholder") || getStops().get(stopToAdd.getLineIndex(index)).equals(stopToAdd)) {
					getStops().remove(stopToAdd.getLineIndex(index));
					getStops().add(stopToAdd.getLineIndex(index), stopToAdd);
				} else { //scenario 3) there is a real stop in the index we're trying to add the stop to
					// we add the new stop, and shift down the stops after the one we're adding
					getStops().add(stopToAdd.getLineIndex(index), stopToAdd);
					//then we fix index for the shifted down stops
					for (int i = 0 ; i < getStops().size() ; i++) {
						getStops().get(i).setLineIndex(index, i);
					}
				}
			}
		}
	}
	
	//removing a stop from a line is simple, using getStops().remove() and there's no need for a separate method.
}
