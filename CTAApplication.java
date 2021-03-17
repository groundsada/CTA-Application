/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * CTAApplication is the main class that has the main method that runs the program
 * It does not resemble an object. It contains all the user interface and menu methods, along with all the methods that prompt the user for input
 * Test.java is the class that tests the project
 * Inheritance Relationship: CTANetwork is a subclass of CTALine, CTAStop is a subclass of GeoLocation
 * Association Relationship: CTALine objects have an ArrayList of CTAStop/
 * 							 CTANetwork objects have an ArrayList of CTAStop and an ArrayList of CTALine
 */
package project;
import java.io.*;
import java.util.*;
public class CTAApplication {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		CTANetwork cta = new CTANetwork(); //the cta network instance
		cta.readFile("CTAStops.csv", input); //reads CTAStops.csv by default. User has the option to read from other files
		menu(cta,input); //displays the user menu
		input.close();
	}
	
	
	
	//adds a new station to the network by prompting the user for the new station properties and callign cta.addStop
	public static void createStation(CTANetwork cta, Scanner input) {
		CTAStop newStation = new CTAStop();
		setNamePrompt(newStation, input);
		setCoordinatesPrompt(newStation, input);
		setWheelchairPrompt(newStation, input);
		setDescriptionPrompt(newStation, input);
		setLineIndexPrompt(cta, newStation, input);
		cta.addStop(newStation);
		System.out.println("Successfully added " + newStation.getName() + " to the network.");
	}
	
	
	
	//modifies an exisiting station on the cta network by prompting the user to edit the details
	public static void modifyStation(CTANetwork cta,Scanner input) {
		CTAStop toEdit = searchStop(cta, input);
		cta.removeStop(toEdit);
		System.out.println("The station's current name is: " + toEdit.getName());
		setNamePrompt(toEdit, input);
		System.out.println("The station's current coordinates are: " + toEdit.getLat() + ","+toEdit.getLng());
		setCoordinatesPrompt(toEdit, input);
		if (toEdit.hasWheelchair()) {
			System.out.println("The station has wheelchair access.");
		} else {
			System.out.println("The station doesn't have wheelchair access.");
		}
		setWheelchairPrompt(toEdit, input);
		System.out.println("The station's current description is: " + toEdit.getName());
		setDescriptionPrompt(toEdit, input);
		System.out.print("The station is currently on the following lines: ");
		for (int i = 0 ; i< cta.getLines().size();i++) {
			if (toEdit.getLineIndex(i) > -1 ) {
				System.out.print(cta.getLines().get(i).getName() + " ");
			}
		}
		setLineIndexPrompt(cta,toEdit, input);
		cta.addStop(toEdit);
	}
	
	
	//deletes a cta stop from the network, after prompting the user to select the stop to delete
	public static void deleteStop(CTANetwork cta, Scanner input) {
		CTAStop toDelete = searchStop(cta, input);
		cta.removeStop(toDelete);
	}
	
	
	//prompts the user to enter the details of the GeoLocation
	//then returns the nearestStation to said GeoLocation
	public static CTAStop nearestStation(CTANetwork cta,Scanner input) {
		GeoLocation g = new GeoLocation();
		do {
			try {
				System.out.print("Please enter your geographical latitude: ");
				double lat = Double.parseDouble(input.nextLine());
				if (g.validLat(lat)) {
					g.setLat(lat);
					break;
				} else {
					System.out.println("Not a valid latitude. Please enter a value between (-90) and (90).");
					}
			
			} catch (Exception e) {
				System.out.println("Not a valid Chicago latitude. Please enter a value between (-90) and (90).");
			}
		} while(true);
		do {
			try {
				System.out.print("Please enter your geographical longitude: ");
				double lng = Double.parseDouble(input.nextLine());
				if (g.validLng(lng)) {
					g.setLng(lng);
					break;
				} else {
					System.out.println("Not a valid longitude. Please enter a value between (-180) and (180).");
					}
				
			} catch (Exception e) {
				System.out.println("Not a valid latitude. Please enter a value between (-180) and (180).");
			}
		} while (true);
		System.out.println("The nearest station to your location is " + cta.nearestStation(g).getName() + " with a distance of: "+ cta.nearestStation(g).calcDistance(g) + " miles");
		System.out.println(cta.nearestStation(g).getName() + " is located at (" + cta.nearestStation(g).getLat() + "," + cta.nearestStation(g).getLng() +").");
		return cta.nearestStation(g);
	}
	
	
	
	//prompts the user to enter a name to search for the corresponding stop
	//if multiple results found, it prompts the user to select the wanted stop
	public static CTAStop searchStop(CTANetwork cta, Scanner input) {
        do {
            System.out.print("Please enter the name of the station: ");
            String searchInput = input.nextLine();
            ArrayList<CTAStop> results = cta.searchByName(searchInput);
            if (results.size() == 0) {
                System.out.println("There are no matches for: " + searchInput);
            } else if (results.size() == 1) {
            	System.out.print(results.get(0) +"\nOn lines: ");
            	for (int i = 0; i < results.get(0).getLineIndex().length;i++) {
    				if (results.get(0).getLineIndex(i) > -1 ) {
    					System.out.print(cta.getLines().get(i).getName()+ " ");
    				}
    			}
            	System.out.println("\n");
            	return results.get(0);
            } else {
            	System.out.println(results.size() + " matches.");
            	do {
                    for (int i = 0; i <results.size(); i++) {
                        System.out.println((i+1)+". " + results.get(i).toString());
                    }
                    System.out.print("Please pick a station from the results and enter its number: ");
                    int choice = intPrompt(input);
                    try {
                        System.out.print(results.get(choice-1) +"\nOn lines: ");
                        for (int i = 0; i < results.get(choice-1).getLineIndex().length;i++) {
            				if (results.get(choice-1).getLineIndex(i) > -1 ) {
            					System.out.print(cta.getLines().get(i).getName()+ " ");
            				}
            			}
                    	System.out.println("\n");
                        return results.get(choice -1);
                    } catch (Exception e) {
                        System.out.println("Invalid number. Please try again.");
                    }
                } while (true);
            }  	
        } while(true);
    }
	
	
	
	//advanced search for a stop. Prompts the user for details to search based on
	//meaning, it searches through requirements and prompts the user to select a result
	//and returns said result
	public static CTAStop advSearchStop(CTANetwork cta, Scanner input) {
		do {
		ArrayList<CTAStop> results = cta.getStops();
		for (int i = 0 ; i < cta.getLines().size();i++) {
			System.out.println("Is the station you're searching for on the " + cta.getLines().get(i).getName() + " Line? (Y/N): ");
			if (booleanPrompt(input)) {
				for (int j = 0 ; j < results.size();j++) {
					if (results.get(j).getLineIndex(i) == -1 ) {
						results.remove(j);
						j--;
					}
				}
			}
		}
		System.out.println("Does the station you're searching for have wheelchair access? (Y/N): ");
		boolean answer = booleanPrompt(input);
		
		for (int j = 0 ; j < results.size();j++) {
			if (results.get(j).hasWheelchair() != answer ) {
				results.remove(j);
				j--;
			}
		}
		
		if (results.size() == 0) {
            System.out.println("There are no matches for your requirements. Try Again!" );
        } else if (results.size() == 1) {
        	System.out.println(results.get(0));
        	return results.get(0);
        } else {
        	System.out.println(results.size() + " matches.");
        	do {
                for (int i = 0; i <results.size(); i++) {
                    System.out.println((i+1)+". " + results.get(i).toString());
                }
                System.out.println("Please pick a station from the results and enter its number: ");
                int choice = intPrompt(input);
                try {
                	System.out.print(results.get(choice -1));
                	System.out.print(results.get(choice-1) +"\nOn lines: ");
                    for (int i = 0; i < results.get(choice-1).getLineIndex().length;i++) {
        				if (results.get(choice-1).getLineIndex(i) > -1 ) {
        					System.out.print(cta.getLines().get(i).getName()+ " ");
        				}
        			}
                	System.out.println("\n");
                    return results.get(choice -1);
                } catch (Exception e) {
                    System.out.println("Invalid number. Please try again.");
                }
            } while (true);
        }  	
		} while(true);
    }
	
	
	//prompts the user to input a valid boolean (Y/N)
	public static boolean booleanPrompt(Scanner input) {
		do {
			try {
				char bool = input.nextLine().toLowerCase().charAt(0);
                switch (bool) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                	System.out.println("Not a valid boolean. Please type \"Y\" or \"N\":");
                    break;
                }
			}catch (Exception e) {
				System.out.println("Not a valid boolean. Please type \"Y\" or \"N\":");
			}
		}while (true);
	}
	
	
	
	//prompts the user for a valid integer
	public static int intPrompt(Scanner input) {
		do {
			try {
					int a  = Integer.parseInt(input.nextLine());
					return a;
			} catch (Exception e) {
				System.out.println("Not a valid integer. Please try again: ");
			}
		}while (true);
	}
	
	
	
	//prompts the user to input valid Chicago coordinates (lat and lng)
	public static void setCoordinatesPrompt(CTAStop station, Scanner input) {
		do {
			try {
				System.out.print("Please enter " + station.getName() + "'s geographical latitude: ");
				double lat = Double.parseDouble(input.nextLine());
				if (station.validLat(lat)) {
					station.setLat(lat);
					break;
				} else {
					System.out.println("Not a valid Chicago latitude. Please enter a value between (41) and (42.5).");
					}
				
			} catch (Exception e) {
				System.out.println("Not a valid Chicago latitude. Please enter a value between (41) and (42.5).");
			}
		} while (true);
		do {
			try {
				System.out.print("Please enter " + station.getName() + "'s geographical longitude: ");
				double lng = Double.parseDouble(input.nextLine());
				if (station.validLng(lng)) {
					station.setLng(lng);
					break;
				} else {
					System.out.println("Not a valid Chicago longitude. Please enter a value between (-87) and (-88.5).");
					}
				
			} catch (Exception e) {
				System.out.println("Not a valid Chicago latitude. Please enter a value between (-87) and (-88.5).");
			}
		} while (true);
	}
	
	
	
	//prompts the user to set wheelchair access (boolean prompt)
	public static void setWheelchairPrompt(CTAStop station, Scanner input) {
		System.out.print("Is " + station.getName() + " wheel chair accessible? (Y/N): ");
		station.setWheelchair(booleanPrompt(input));
	}
	
	
	
	//prompts the user to enter a name for a stop (used to create a stop)
	public static void setNamePrompt(CTAStop station, Scanner input) {
		System.out.print("Please enter the name of the station you want to create: ");
		station.setName(input.nextLine());
	}
	
	
	//prompts the user to enter the description of a stop (used to create a stop)
	public static void setDescriptionPrompt(CTAStop station, Scanner input) {
		System.out.print("What's " + station.getName() +"'s description? Some examples: \"elevated\", \"surface\", \"undergound\", \"subway\": ");
		station.setDescription(input.nextLine());
	}
	
	
	//prompts the user to set line indexes for a station
	//by asking things like, is it in the red line? (Y/N)
	//used to create a stop
	public static void setLineIndexPrompt(CTANetwork cta, CTAStop station, Scanner input) {
		for (int i = 0 ; i < cta.getLines().size();i++) {
			System.out.println("Is " + station.getName() + " on the " + cta.getLines().get(i).getName() + " Line? (Y/N): ");
			if (booleanPrompt(input)) {
				System.out.println("Where is it on the " + cta.getLines().get(i).getName() + " Line? Do you want to:");
				System.out.println("1. Enter its number on the line (indexing starts at 0)\n2. or enter the name of the station you're adding it before (The station it should replace in order)?");
				do {
					String choice = input.nextLine();
					if (choice.contains("1")) {
						System.out.println("Please enter its postition on the line: ");
						do {
							try {
								int pos = Integer.parseInt(input.nextLine());
								if ( pos > cta.getLines().get(i).getStops().size()+1) {
									System.out.println("The " + cta.getLines().get(i).getName() + " Line has only " + cta.getLines().get(i).getStops().size()
											+" stops. You're trying to add a station numbered " + pos + ".");
									System.out.println("Please add stations in order so that there won't be gaps.\nPlease try again: ");
								} else {
									station.setLineIndex(i, pos);
									break;
								}
							} catch (Exception e) {
								System.out.println("Please enter a valid number: ");
							}
						} while (true);
						break;
					} else if (choice.contains("2")) {
						do {
						CTAStop before = searchStop(cta, input);
						int pos = before.getLineIndex(i);
						if (pos == -1) {
							System.out.println("Selected stop not on the line. Please try again!");
						} else if ( pos > cta.getLines().get(i).getStops().size()+1) {
							System.out.println("The " + cta.getLines().get(i).getName() + " Line has only " + cta.getLines().get(i).getStops().size()
									+" stops. You're trying to add a station numbered " + pos + ".");
							System.out.println("Please add stations in order so that there won't be gaps.\nPlease try again: ");
						} else {
							station.setLineIndex(i, pos);
							break;
						}
						}while(true);
						break;
					} else {
						System.out.println("Not a valid choice. Please enter 1 or 2: ");
					}
				}while(true);
			}
		}
	}
	
	
	
	//prints the path between two stop (for example, ride the red line from xx to yy)
	//uses searching to find origin and destination, then uses cta.createPath to generate the path string
	public static void findPath(CTANetwork cta, Scanner input) throws IOException {
		System.out.println("What station are you starting from? (origin): ");
		CTAStop begin = searchStop(cta, input);		
		System.out.println("What station are you going to? (desitnation): ");
		CTAStop end = searchStop(cta, input);
		
		System.out.println(cta.createPath(begin,end));
	}
	
	
	//prints the path from a GeoLoation to a cta stop
	public static void findPathFromGeoLocation(CTANetwork cta,Scanner input){
		CTAStop nearestStop = nearestStation(cta, input);
		System.out.println("What station are you going to? (desitnation): ");
		CTAStop end = searchStop(cta, input);
		System.out.println("Get from your location to " + nearestStop.getName());
		System.out.println(cta.createPath(nearestStop,end));
	}
	
	
	
	//prints the path between 2 GeoLocations
	public static void findPathBetweenGeoLocations(CTANetwork cta,Scanner input){
		System.out.println("Starting point: ");
		CTAStop begin = nearestStation(cta, input);
		
		System.out.println("Destination point: ");
		CTAStop end = nearestStation(cta, input);
		System.out.print("\n");
		if (begin.equals(end)) {
			System.out.println("There's no cta line connecting you to your destination");
		} else {
		System.out.println("Get from your location to \"" + begin.getName() +"\"");
		System.out.println(cta.createPath(begin,end));
		System.out.println("Get from the CTA station to your destination.");
		}
	}
	
	
	
	//prints all the stops of the cta network
	public static void displayAll(CTANetwork cta) {
		for (CTAStop stop : cta.getStops()) {
			System.out.print("\"" +stop.getName() + "\", on line(s): ");
			for (int i = 0; i < stop.getLineIndex().length;i++) {
				if (stop.getLineIndex(i) > -1 ) {
					System.out.print(cta.getLines().get(i).getName()+ " ");
				}
			}
			System.out.print("\n");
		}
	}
	
	
	//prints all the stops on a specific line on the network, by prompting the user to specify a line
	public static void displayLineAll(CTANetwork cta,Scanner input) {
		for(int i = 0 ; i < cta.getLines().size();i++) {
			System.out.println((i+1) + ". " +cta.getLines().get(i).getName());
		}
		System.out.print("Please enter the number of the line: ");
		do {
			try {
			CTALine choiceLine = cta.getLines().get(Integer.parseInt(input.nextLine())-1);
			for (CTAStop stop : choiceLine.getStops()) {
				System.out.print("\"" +stop.getName() + "\", on line(s): ");
				for (int i = 0; i < stop.getLineIndex().length;i++) {
					if (stop.getLineIndex(i) > -1 ) {
						System.out.print(cta.getLines().get(i).getName()+ " ");
					}
				}
				System.out.print("\n");
			}
			break;
		} catch (Exception e ) {
			System.out.println("Invalid choice. Please try again.");
		}
		}while(true);
	}
	
	
	
	//prompts the user for file to read
	public static void readFile(CTANetwork cta,Scanner input) throws IOException {
		System.out.println("Please enter input file name: ");
		String fileName = input.nextLine();
		cta.readFile(fileName, input);
		System.out.println("Successfully imported stops!");
	}
	
	
	
	//calls the menu items and remains on loop until user exits the program
	public static void menu(CTANetwork cta, Scanner input) {
		System.out.println("Welcome to the Chicago Transit Authority Program.");
		boolean repeat = true;
		do {
			System.out.println("\nPlease select an option:\n1. Find the nearest station to you\n2. Get directions from one station to another");
			System.out.println("3. Get directions from a geographical location to another\n4. Get directions from a geographical location to a CTA station");
			System.out.println("5. Search for a station by name\n6. Search for a station by requirements\n7. Add a station to the CTA\n8. Edit a CTA station\n9. Delete a station from the CTA");
			System.out.println("10. View all CTA stations\n11. View a specific line's stations\n12. Import stations from a file\n13. Export stations to a file\n14. Exit the program");
			System.out.print(">> ");
			String userInput = input.nextLine();
			try {
				int choice  = Integer.parseInt(userInput);
				switch (choice) {
				case 1:
					System.out.println("Finding the nearest station to you:");
					nearestStation(cta, input);
					break;
				case 2:
					System.out.println("Getting directions from one station to another:");
					findPath(cta, input);
					break;
				case 3:
					System.out.println("Getting directions from a geographical location to another:");
					findPathBetweenGeoLocations(cta, input);
					break;
				case 4:
					System.out.println("Getting directions from a geographical location to a CTA station:");
					findPathFromGeoLocation(cta, input);
					break;
				case 5:
					System.out.println("Searching for a station by name:");
					searchStop(cta, input);
					break;
				case 6:
					System.out.println("Searching for a station by requirements:");
					advSearchStop(cta, input);
					break;
				case 7:
					System.out.println("Adding a station to the CTA:");
					createStation(cta, input);
					break;
				case 8:
					System.out.println("Editing a CTA station:");
					modifyStation(cta, input);
					break;
				case 9:
					System.out.println("Deleting a station from the CTA:");
					deleteStop(cta, input);
					break;
				case 10:
					System.out.println("Viewing all CTA stations:");
					displayAll(cta);
					break;
				case 11:
					System.out.println("Viewing a specific line's stations:");
					displayLineAll(cta, input);
					break;
				case 12:
					System.out.println("Importing stations from a file:");
					readFile(cta, input);
					break;
				case 13:
					System.out.println("Exporting stations to a file:");
					cta.writeToFile(input);
					break;
				case 14:
					repeat = false;
					System.out.println("Program terminated.");
					break;
				default:
					System.out.println("Not a valid choice. Please try again.");
					break;
				}
			} catch(Exception e) {
				System.out.println("Not a valid choice. Please try again.");
			}
		} while(repeat);
	}
}
