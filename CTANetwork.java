/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * CTANetwork class represents an entire network of CTA lines and stops
 * We only have one instance of CTANetwork in our program.
 * CTANetwork is a subclass of CTALine because the entire network has an ArrayList of stops, just like lines.
 * CTANetwork also has an ArrayList of CTALine.
 * Reason for class: to provide organization and hierarchy to code and program.
 */
package project;
import java.io.*;
import java.util.*;

public class CTANetwork extends CTALine {
	
	private ArrayList<CTALine> lines; //lines is an arraylist of cta lines
	//Each cta stop exists both in the array of stops of the network and the array of stops in each line the stop exists
	
	
	public CTANetwork() { //default constructor
		//name and index are irrelevant for the network, since we only have once instance
		super();
		lines = defaultLines();
	}
	
	
	
	public CTANetwork(ArrayList<CTALine> lines, ArrayList<CTAStop> stops) { //non-default constructor
		this();
		setLines(lines);
		setStops(stops);
	}
	
	
	
	public CTANetwork(ArrayList<CTAStop> stops) { //2nd non-default constructor
		this();
		lines= defaultLines();
		for (CTAStop stop : stops ) { //the reason we use addStop loop instead of setStops is that setStops sets the array of stops
			//we want to add the stops to their correspondent lines as well
			addStop(stop);
		}
	}
	
	
	
	//returns an arraylist of default cta lines.
	public ArrayList<CTALine> defaultLines(){ //default lines, called by constructors in case we don't read from file
		ArrayList<CTALine> instance = new ArrayList<CTALine> (7);
		instance.add(new CTALine("Red",0));
		instance.add(new CTALine("Green",1));
		instance.add(new CTALine("Blue",2));
		instance.add(new CTALine("Brown",3));
		instance.add(new CTALine("Purple",4));
		instance.add(new CTALine("Pink",5));
		instance.add(new CTALine("Orange",6));
		return instance;
	}
	
	
	
	//accessors
	public ArrayList<CTALine> getLines(){
		return lines;
	}
	
	
	
	//returns an arraylist of stops that are transfer stations (exist on more than 1 line)
	public ArrayList<CTAStop> getTransferStops() {
        ArrayList<CTAStop> transferStops = new ArrayList<>();
        for (int i =0 ; i < getStops().size();i++) {
            int onStations = 0;
            for (int j = 0; j < getLines().size(); j++) {
                if (getStops().get(i).getLineIndex(j) != -1) {
                    onStations++;
                }
            }
            if (onStations > 1) {
                transferStops.add(getStops().get(i));
            }
        }
        return transferStops;
    }
	
	
	
	//mutators
	public void setLines(ArrayList<CTALine> lines) {
		this.lines = lines; //no need for validation
	}
	
	
	
	@Override
	//returns string representing the number of lines and stops in the cta network
	public String toString() {
		return "The CTA Network: " + lines.size() +" line(s) and " + getStops().size() + " stop(s).";
	}

	
	
	//returns true if 2 networks are identical in lines and stops
	public boolean equals(CTANetwork n) {
		if (this.lines.size() == n.lines.size()) {
			for (int i = 0 ; i < lines.size();i++) {
				if (!this.lines.get(i).equals(n.lines.get(i))) return false;
			}
			return true && super.equals(n);
		}
		return false;
	}
	
	
	
	//reads a file with the name fileName. If it fails to find it, it prompts the user to input another file name to read.
	//reading means that it adds the stops in the file to the network, both to the stops list, and to the corresponding lines the stop exists on
	//it does so by parsing csv files
	public void readFile(String fileName, Scanner input) throws IOException {
		File theFile =new File ("");
		Scanner finput = input;//finput has to be initialized to something outside of the try/catch scope. We initalize it to input becuase it is a valid Scanner, but it will be definitely corrected once the loop runs
		boolean repeat = true;
		do {
			try {
				theFile = new File("src/project/" + fileName);
				finput = new Scanner(theFile);
				repeat = false;
			} catch (FileNotFoundException e) {
				System.out.println("Error! File not found!\nAre you sure your file is here? " + theFile.getAbsolutePath() + "\nPlease enter the correct file name: ");
				fileName = input.nextLine();
			}
		} while(repeat);
		String nextLine = finput.nextLine(); //takes in first line of file
		for (int i = 0 ; i < 7 ; i++) {
			getLines().get(i).setName(nextLine.split(",")[i+5].split(":")[0] );
		}
		int lineCounter = 1;
		while (finput.hasNextLine()) {
			lineCounter++;
			nextLine = finput.nextLine();
			String[] nextLineSplits = nextLine.split(",");
			//code to add station
			double stopLat = 0;
			double stopLng = 0;
			boolean stopWheelchair = false;
			int[] stopLineIndex = {-1,-1,-1,-1,-1,-1,-1};
			try {
				stopLat = Double.parseDouble(nextLineSplits[1]);
				stopLng = Double.parseDouble(nextLineSplits[2]);
				stopWheelchair = Boolean.parseBoolean(nextLineSplits[4]);
				for (int i = 0 ; i < 7 ; i++) {
					stopLineIndex[i] = Integer.parseInt(nextLineSplits[i+5]);
				}
				CTAStop stopFromLine = new CTAStop(nextLineSplits[0], stopLat, stopLng, nextLineSplits[3], stopWheelchair, stopLineIndex);
				addStop(stopFromLine);
			} catch (Exception e) {
				System.out.println("Error processing stop: \"" + nextLineSplits[0]  + "\" at line (" + lineCounter+ ") in \""  + theFile.getName()+ "\". This stop has been ignored.\nYou can manually add it or fix file and retry.");
			}
		}
		finput.close();
	}
	
	
	
	@Override
	//Adds a stop to the cta network. It adds the stop to the arraylist of stops for the network,
	//and the arraylist of stops for each line the stop is on, while fixing the indexing of stops on lines after adding the stop. 
	public void addStop(CTAStop stopToAdd) {
		//add stop to list of stops
		getStops().add(stopToAdd);
		//loop through lines, add stop to everyone it exists in
		for (int i = 0 ; i < getLines().size();i++) { //loop through every line
			getLines().get(i).addStop(stopToAdd); //add to line
			//reorderOtherLines(i);
		}
	}
	
	
	
	//removes a stop from the cta network. It both removes the stop from the arraylist of network stops and the arraylist of stops for every line the stop is on.
	public void removeStop(CTAStop stop) {
		// remove from list of stops
		for (int i = 0 ; i < getStops().size();i++) {
			if (getStops().get(i).equals(stop)) {
				getStops().remove(i);
			}
		}
		// loop through lines and delete it
		for (int i = 0 ; i < getLines().size();i++) {
			for (int j = 0 ; j< getLines().get(i).getStops().size();j++) {
				if (getLines().get(i).getStops().get(j).equals(stop)) {
					getLines().get(i).getStops().remove(j);
				}
			}
		}
		//fix numbering issues that occur after removing
		for (CTALine line : getLines()) {
			int i = 0 ;
			for (CTAStop numStop : line.getStops()) {
				numStop.setLineIndex(line.getIndex(),i );
				i++;
			}
        }
	}
	
	
	
	//prompts the user to enter a file name
	//then writes data to file in csv format consistent with the CTAStops.csv format
	//We are able to readFile the output of writeToFile (output is consistent with csv format used for input)
	public void writeToFile(Scanner input) throws IOException {
		System.out.print("Please enter a name for the file: ");
		String fileName = input.nextLine();
		File outputFile = new File ("src/project/" + fileName);
		FileWriter output = new FileWriter(outputFile);
		output.write("Name,Latitude,Longitude,Description,Wheelchair,"
				+ "Red:" +getLines().get(0).getStops().size()+ ",Green:" +getLines().get(1).getStops().size()
				+",Blue:" +getLines().get(2).getStops().size()+ ",Brown:" +getLines().get(3).getStops().size()
				+ ",Purple:" +getLines().get(4).getStops().size()+ ",Pink:" +getLines().get(5).getStops().size()
				+ ",Orange:" +getLines().get(6).getStops().size()+ "\n");
		for (int i = 0 ; i < getStops().size();i++) {
			//ignore placeholders from being written to file
			if (!getStops().get(i).getName().equalsIgnoreCase("Placeholder")) {
				output.write(getStops().get(i).toCSVString()+"\n");
			}
		}
		System.out.println("File was written to: " + outputFile.getAbsolutePath());
		output.close();
	}
	
	
	
	//returns an arraylist of cta stops resulting from searching by name by passing the argument searchInput
	public ArrayList<CTAStop> searchByName(String searchInput) {
        ArrayList<CTAStop> results = new ArrayList<CTAStop>();
        for (int i = 0 ; i < getStops().size();i++) {
        	if (getStops().get(i).getName().toLowerCase().contains(searchInput.toLowerCase())){
        		results.add(getStops().get(i));
        	}
        }
        return results;
    }

	
	
	//returns the nearest cta stop to a geolocation
	public CTAStop nearestStation(GeoLocation g) {
		double min = 1000000;
		CTAStop returnStop = null;
		for (int i = 0 ; i < getStops().size();i++) {
			if (getStops().get(i).calcDistance(g) < min ) {
				returnStop = getStops().get(i);
				min = getStops().get(i).calcDistance(g);
			}
		}
		return returnStop;
	}
	
	
	
	//returns a detailed string describing the path between 2 cta stops
	public String createPath(CTAStop begin, CTAStop end) {
		//first, we check if origin and destination share coordinates
		//we check with coordinates instead of equals() method, becuase multiple stops could be at the same location
		if (begin.getLat() == end.getLat() && begin.getLng() == end.getLng()) {
			return "You're already here";
		}
		//if on different coordinates, we check to see if they're on the same line
        int onSameLine = shareLineIndex(begin, end); //check if on same line
        if (onSameLine != -1) {
        	
            return ("Ride " + Math.abs(begin.getLineIndex(onSameLine) - end.getLineIndex(onSameLine))
			+" stops on the " + getLines().get(onSameLine).getName() +" line from \"" + begin.getName()
			+"\" to \"" + end.getName()+"\"." );
        } else { //not on the same line
        	//scenario 1 : only 1 transfer
            for (CTAStop transfer : getTransferStops()) {
                int sharedTransferLineIndex = shareLineIndex(begin, transfer);
                if (shareLineIndex(begin, transfer) != -1 &&
                        !transfer.equals(begin) &&
                        shareLineIndex(end, transfer) != -1) {
                    return ("Ride " + Math.abs(begin.getLineIndex(sharedTransferLineIndex) - transfer.getLineIndex(sharedTransferLineIndex))
        			+" stops on the " + getLines().get(sharedTransferLineIndex).getName() +" line from \"" + begin.getName()
        			+"\" to \"" + transfer.getName()+"\".\n"  + createPath(transfer, end));
                }
            }
            //scenario 2 : more than 1 transfer
            for (CTAStop transfer : getTransferStops()) {
                int sharedTransferLineIndex = shareLineIndex(begin, transfer);
                if (sharedTransferLineIndex != -1 && !transfer.equals(begin) && !transfer.equals(end)) {
                	return ("Ride " + Math.abs(begin.getLineIndex(sharedTransferLineIndex) - transfer.getLineIndex(sharedTransferLineIndex))
        			+" stops on the " + getLines().get(sharedTransferLineIndex).getName() +" line from \"" + begin.getName()
        			+"\" to \"" + transfer.getName()+"\".\n"  + createPath(transfer, end));
                }
            }
        }
        return "Unable to find path from " + begin.getName() + " to " + end.getName()+ ".";
    }
	
	
	
	//returns the index of the line that is shared between stopA and stopB
	//for example, if both are on red line, it returns 0(index of red line)
	//if they don't share a line, it returns -1
	 public int shareLineIndex(CTAStop stopA, CTAStop stopB) {
	        for (int i = 0; i < getLines().size(); i++) {
	            if (stopA.getLineIndex(i) != -1 && stopB.getLineIndex(i) != -1) {
	                return i;
	            }
	        }
	        return -1;
	    }
}
