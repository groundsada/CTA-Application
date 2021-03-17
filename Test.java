/*
 * Author: Mohammad Firas Sada
 * Date: 11/1/2020
 * Test class tests the entire CTA application.
 * The main method calls methods that are programmed to test each and every idenitifer, method in each and every class of the application.
 * Every thing is tested with valid arguments, and every kind of invalid arguments (all possible scenarios)
 * All output results should be "Success".
 * User-interface menu and menu options were tested separately. I ran the application on loop, and tested every single menu option
 * 		with both valid and invalid option, and compared results with expectations detailed in Phase 1.
 * 		reading from files was tested with valid files, files with errors, and non-existent files
 * 		writing to files was validated by reading from the output files
 * 		creating paths between stations was tested by using nested loops to generate the path between any 2 pairs of stations
 */
package project;
import java.io.IOException;
import java.util.*;
public class Test {
	
	public static void main(String[] args) throws IOException{
		Scanner input = new Scanner(System.in);
		testGeoLocation();
		testCTAStop();
		testCTALine();
		testCTANetwork(input);
		//since application is methods that prompt the user for input, the application along with the writeToFile method of CTANetwork
		//are tested in a different way. I ran the application on loop, and tried every possible user input to see if program works correctly.
		input.close();
	}
	
	//Test for GeoLocation class with all its identifiers, methods and constructors.
	public static void testGeoLocation() {
		boolean success = true;
		System.out.println("*********     TESTING GEOLOCATION     **********");
		GeoLocation a1 = new GeoLocation(); //testing default constructor for GeoLocation
		System.out.println(a1); //testing toString
		if (!a1.toString().equals("(0.0, 0.0)")) success=false;
		System.out.println(a1.toString()); //testing toString as well
		if (!a1.toString().equals("(0.0, 0.0)")) success=false;
		System.out.println(a1.getLng()); //testing accessor 1/2
		if (a1.getLng() != 0.0) success=false;
		System.out.println(a1.getLat()); // testing accessor 2/2
		if (a1.getLat() != 0.0) success=false;
		a1.setLat(200); //testing mutator 1/2
		a1.setLng(200); //testing mutator 2/2
		System.out.println(a1.toString()); //testing results of validation with toString
		if (!a1.toString().equals("(0.0, 0.0)")) success=false;
		
		System.out.println(""+ a1.validLat(90) +  a1.validLat(91) + a1.validLat(-90)+a1.validLat(-91)); //testing validators at endpoints
		System.out.println(""+a1.validLng(180) +  a1.validLat(181)+a1.validLat(-180)+a1.validLat(-181)); //testing validators at endpoints
		
		a1.setLat(100);
		a1.setLng(100);
		System.out.println(a1.toString()); //resting different values for validation
		if (!a1.toString().equals("(0.0, 100.0)")) success=false;
		
		a1.setLat(-50);
		a1.setLng(-50);
		System.out.println(a1.toString()); //testing different values for validation
		if (!a1.toString().equals("(-50.0, -50.0)")) success=false;
		
		GeoLocation a2 = new GeoLocation(-50,-50); //testing non-default constructor 1/1
		System.out.println(a2.equals(a1)); //testing equals for identical values
		if (!a2.equals(a1)) success = false;
		a2.setLat(20);
		System.out.println(a2.equals(a1)); //testing for different lat
		if (a2.equals(a1)) success = false;
		
		GeoLocation a3 = new GeoLocation(20,-50);
		System.out.println(a3.equals(a2)); //testing for different lng	
		if (!a3.equals(a2)) success = false;
		a3.setLng(50);
		System.out.println(a3.equals(a2)); //testing for different lng
		if (a3.equals(a2)) success = false;
		if (success) System.out.println("*********     GEOLOCATION RESULT: SUCCESS (100%)    **********\n");
		else System.out.println("*********     GEOLOCATION RESULT: FAILURE    **********\n");
		/*
		All constructors and methods were tested for all possible scenarios
		Expected output matches resulting output:
		(0.0, 0.0)
		(0.0, 0.0)
		0.0
		0.0
		(0.0, 0.0)
		truefalsetruefalse
		truefalsefalsefalse
		(0.0, 100.0)
		(-50.0, -50.0)
		true
		false
		true
		false
		*/
	}
	
	public static void testCTAStop() {
		boolean success = true;
		System.out.println("*********     TESTING CTASTOP     **********");
		
		CTAStop a1 = new CTAStop(); //testing default constructor
		System.out.println(a1.toString()); // testing toString
		if (!a1.toString().equals("Placeholder Placeholder 0.0 0.0 false -1 -1 -1 -1 -1 -1 -1")) success = false;
		
		
		//testing mutators
		a1.setDescription("Hello");
		a1.setName("Hi");
		a1.setLat(42); //testing validators with valid values
		a1.setLng(-88.5); //testing validators with valid values
		a1.setLat(45);//testing validators with invalid values
		a1.setLng(-80);//testing validators with invalid values
		a1.setWheelchair(true);
		a1.setLineIndex(0, 10);
		a1.setLineIndex(1, 20);
		a1.setLineIndex(2, 36);
		a1.setLineIndex(4, -2); //trying invalid index
		System.out.println(a1.toString());
		if (!a1.toString().equals("Hi Hello 42.0 -88.5 true 10 20 36 -1 -1 -1 -1")) success = false;
		
		//testing accessors
		System.out.println(a1.getDescription());
		if (!a1.getDescription().equals("Hello")) success = false;
		System.out.println(a1.getLat());
		if (a1.getLat() != 42.0) success = false;
		System.out.println(a1.getLng());
		if (a1.getLng() != -88.5) success = false;
		System.out.println(a1.getName());
		if (!a1.getName().equals("Hi")) success = false;
		System.out.println(a1.hasWheelchair());
		success = a1.hasWheelchair();
		for (int i = 0 ; i < a1.getLineIndex().length;i++) {
			System.out.print(a1.getLineIndex(i) + " ");
		}
		
		//testing non-default constructor 1/2 and lineIndex mutator with invalid values in the array
		int[] aNewIndex = {-3,-2,-1,0,1,2,3,4,5,6,7,8};
		CTAStop a2 = new CTAStop("Another One", 42, -87.5, "DJ Khaled", false, aNewIndex);
		System.out.println("\n"+a2.toString());
		if (!a2.toString().equals("Another One DJ Khaled 42.0 -87.5 false -1 -1 -1 0 1 2 3")) success =false;
		a2 = new CTAStop("Hi", 42, -88.5, "Hello",true, 10, 20, 36, -1,-1,-1,-1); //testing non-default constructor 2/2
		
		//testing eqauls method by changing every value one-by-one
		System.out.println(a1.equals(a2)); //testing equals for identical stops
		a1.setName("HAHA");

		System.out.println(a1.toCSVString());
		if (!a1.toCSVString().equals("HAHA,42.0,-88.5,Hello,TRUE,10,20,36,-1,-1,-1,-1")) success =false;
		System.out.println(a2.toCSVString());
		if (!a2.toCSVString().equals("Hi,42.0,-88.5,Hello,TRUE,10,20,36,-1,-1,-1,-1")) success =false;
		
		
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setName("Hi");
		a1.setDescription("Hehe");
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setDescription("Hello");
		a1.setWheelchair(false);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setWheelchair(true);
		a1.setLineIndex(1, -30);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = a1.equals(a2);
		a1.setLineIndex(1, 30);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setLineIndex(1, 20);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = a1.equals(a2);
		a1.setLat(41.5);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setLat(42);
		a1.setLng(-88.1);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = !a1.equals(a2);
		a1.setLng(-88.5);
		System.out.println(a1.equals(a2)); //testing equals for non-identical stops
		success = a1.equals(a2);
		
		if (success) System.out.println("*********     CTASTOP RESULT: SUCCESS (100%)    **********\n");
		else System.out.println("*********     CTASTOP RESULT: FAILURE    **********\n");
		/*
		All constructors and methods were tested for all possible scenarios
		Expected output matches resulting output:
		Placeholder Placeholder 0.0 0.0 false -1 -1 -1 -1 -1 -1 -1
		Hi Hello 42.0 -88.5 true 10 20 36 -1 -1 -1 -1
		Hello
		42.0
		-88.5
		Hi
		true
		10 20 36 -1 -1 -1 -1 
		Another One DJ Khaled 42.0 -87.5 false -1 -1 -1 0 1 2 3
		true
		HAHA,42.0,-88.5,Hello,TRUE,10,20,36,-1,-1,-1,-1
		Hi,42.0,-88.5,Hello,TRUE,10,20,36,-1,-1,-1,-1
		false
		false
		false
		true
		false
		true
		false
		false
		true
		*/
	}
	
	public static void testCTALine() {
		boolean success = true;
		System.out.println("*********     TESTING CTALINE     **********");
		CTALine a1 = new CTALine(); //testing default constructor
		System.out.println(a1); //testing toString
		System.out.println(a1.toString()); //testing toString
		if (!a1.toString().equals("Line Placeholder - has 0 stop(s).")) success = false;
		
		//testing accessors
		System.out.println(a1.getIndex());
		if (a1.getIndex() != 0 ) success = false;
		System.out.println(a1.getName());
		if (!a1.getName().equals("Placeholder")) success=false;
		System.out.println(a1.getStops());
		if (a1.getStops().size() != 0) success=false;
		
		//testing mutators
		a1.setIndex(5);
		a1.setName("Some Line Name");
		ArrayList<CTAStop> a1Stops = new ArrayList<CTAStop>();
		a1Stops.add(new CTAStop("Some stop 1", 42, -88.5, "Some Description 1", true, 1, -1, -1,-1,-1,-1,-1));
		a1Stops.add(new CTAStop("Some stop 2", 41.5, -87.99, "Some Description 2", true, 2, -1, -1,-1,-1,-1,-1));
		a1Stops.add(new CTAStop("Some stop 3", 41.78, -87.19, "Some Description 3", true, 3, -1, -1,-1,3,-1,-1));
		a1.setStops(a1Stops);
		
		//testing accessorss with other values
		System.out.println(a1.getIndex());
		if (a1.getIndex() != 5 ) success = false;
		System.out.println(a1.getName());
		if (!a1.getName().equals("Some Line Name")) success=false;
		System.out.println(a1.getStops());
		if (a1.getStops().size() != 3) success=false;
		
		//trying index validation
		a1.setIndex(-22);
		System.out.println(a1.getIndex());
		if (a1.getIndex() != 5) success=false;
		
		
		CTALine a2 = new CTALine("Some Line Name",5); //non-default constructor 1/2
		a2.setStops(a1Stops);
		
		System.out.println(a2.equals(a1)); //equals testing when true
		
		//equals method testing:
		ArrayList<CTAStop> a2Stops = new ArrayList<CTAStop>();
		a2Stops.add(new CTAStop("Some stop 1", 42, -88.5, "Some Description 1", true, 1, -1, -1,-1,-1,-1,-1));
		a2Stops.add(new CTAStop("Some stop 2", 41.5, -87.99, "Some Description 2", true, 2, -1, -1,-1,-1,-1,-1));
		a2Stops.add(new CTAStop("Some stop 3", 41.78, -87.19, "Some Description 3", true,4, -1, -1,-1,3,-1,-1));
		a2.setStops(a2Stops);
		System.out.println(a2.equals(a1)); //equals testing when false 1/3
		
		a2.setStops(a1Stops);
		a2.setName("A different name");
		System.out.println(a2.equals(a1)); //equals testing when false 2/3
		
		a2.setName("Some Line Name");
		a2.setIndex(20);
		System.out.println(a2.equals(a1)); //equals testing when false 3/3
		
		CTALine a3 = new CTALine(a2.getName(), a2.getIndex(), a2.getStops()); //non-default constructor 3/3
		System.out.println(a2.equals(a3));
		a2.setIndex(0);
		a2.addStop(new CTAStop("ARUBA", 42, -88.55, "elevated", true, 6,-1,1,-1,-1,-1,-1));
		a2.addStop(new CTAStop("JAMAICA", 42, -88.55, "elevated", true, 2,-1,1,-1,-1,-1,-1));
		a2.addStop(new CTAStop("BERMUDA", 42, -88.55, "elevated", true, 4,-1,1,-1,-1,-1,-1));
		for(CTAStop stop : a2.getStops()) {
			System.out.println(stop);
		}
		System.out.println(a2.getStops());
		if(!a2.getStops().toString().equals("[\"Some stop 1\" Some Description 1 42.0 -88.5, \"Some stop 2\" Some Description 2 41.5 -87.99, \"JAMAICA\" elevated 42.0 0.0, \"Some stop 3\" Some Description 3 41.78 -87.19, \"BERMUDA\" elevated 42.0 0.0, \"Placeholder\" Placeholder 0.0 0.0, \"Placeholder\" Placeholder 0.0 0.0, \"ARUBA\" elevated 42.0 0.0]"))
			success =false;
		a2.addStop(new CTAStop("NOT ON LINE", 42, -88.55, "elevated", true, -1,-1,1,-1,-1,-1,-1));
		a2.addStop(new CTAStop("FIRST ELEMENT", 42, -88.55, "elevated", true, 0,-1,1,-1,-1,-1,-1));
		a2.addStop(new CTAStop("SECOND ELEMENT", 42, -88.55, "elevated", true, 1,-1,1,-1,-1,-1,-1));
		System.out.println(a2.getStops());
		if (!a2.getStops().toString().equals("[\"FIRST ELEMENT\" elevated 42.0 0.0, \"SECOND ELEMENT\" elevated 42.0 0.0, \"Some stop 1\" Some Description 1 42.0 -88.5, \"Some stop 2\" Some Description 2 41.5 -87.99, \"JAMAICA\" elevated 42.0 0.0, \"Some stop 3\" Some Description 3 41.78 -87.19, \"BERMUDA\" elevated 42.0 0.0, \"Placeholder\" Placeholder 0.0 0.0, \"Placeholder\" Placeholder 0.0 0.0, \"ARUBA\" elevated 42.0 0.0]"))
			success =false;
		if (success) System.out.println("*********     CTALINE RESULT: SUCCESS (100%)    **********\n");
		else System.out.println("*********     CTALINE RESULT: FAILURE    **********\n");
		/*
		All constructors and methods were tested for all possible scenarios
		Expected output matches resulting output:
		Line Placeholder - has 0 stop(s).
		Line Placeholder - has 0 stop(s).
		0
		Placeholder
		[]
		5
		Some Line Name
		[Some stop 1 Some Description 1 42.0 -88.5 true 1 -1 -1 -1 -1 -1 -1, Some stop 2 Some Description 2 41.5 -87.99 true 2 -1 -1 -1 -1 -1 -1, Some stop 3 Some Description 3 41.78 -87.19 true 3 -1 -1 -1 3 -1 -1]
		5
		true
		false
		false
		false
		true
		Some stop 1 Some Description 1 42.0 -88.5 true 0 -1 -1 -1 -1 -1 -1
		Some stop 2 Some Description 2 41.5 -87.99 true 1 -1 -1 -1 -1 -1 -1
		JAMAICA elevated 42.0 0.0 true 2 -1 1 -1 -1 -1 -1
		Some stop 3 Some Description 3 41.78 -87.19 true 3 -1 -1 -1 3 -1 -1
		BERMUDA elevated 42.0 0.0 true 4 -1 1 -1 -1 -1 -1
		Placeholder Placeholder 0.0 0.0 false 5 -1 -1 -1 -1 -1 -1
		Placeholder Placeholder 0.0 0.0 false 6 -1 -1 -1 -1 -1 -1
		ARUBA elevated 42.0 0.0 true 7 -1 1 -1 -1 -1 -1
		[Some stop 1 Some Description 1 42.0 -88.5 true 0 -1 -1 -1 -1 -1 -1, Some stop 2 Some Description 2 41.5 -87.99 true 1 -1 -1 -1 -1 -1 -1, JAMAICA elevated 42.0 0.0 true 2 -1 1 -1 -1 -1 -1, Some stop 3 Some Description 3 41.78 -87.19 true 3 -1 -1 -1 3 -1 -1, BERMUDA elevated 42.0 0.0 true 4 -1 1 -1 -1 -1 -1, Placeholder Placeholder 0.0 0.0 false 5 -1 -1 -1 -1 -1 -1, Placeholder Placeholder 0.0 0.0 false 6 -1 -1 -1 -1 -1 -1, ARUBA elevated 42.0 0.0 true 7 -1 1 -1 -1 -1 -1]
		[FIRST ELEMENT elevated 42.0 0.0 true 0 -1 1 -1 -1 -1 -1, SECOND ELEMENT elevated 42.0 0.0 true 1 -1 1 -1 -1 -1 -1, Some stop 1 Some Description 1 42.0 -88.5 true 2 -1 -1 -1 -1 -1 -1, Some stop 2 Some Description 2 41.5 -87.99 true 3 -1 -1 -1 -1 -1 -1, JAMAICA elevated 42.0 0.0 true 4 -1 1 -1 -1 -1 -1, Some stop 3 Some Description 3 41.78 -87.19 true 5 -1 -1 -1 3 -1 -1, BERMUDA elevated 42.0 0.0 true 6 -1 1 -1 -1 -1 -1, Placeholder Placeholder 0.0 0.0 false 7 -1 -1 -1 -1 -1 -1, Placeholder Placeholder 0.0 0.0 false 8 -1 -1 -1 -1 -1 -1, ARUBA elevated 42.0 0.0 true 9 -1 1 -1 -1 -1 -1]
		 */
	}
	public static void testCTANetwork(Scanner input) throws IOException {
		boolean success = true;
		System.out.println("*********     TESTING CTANETWORK     **********");
		
		CTANetwork a = new CTANetwork(); //default constructor
		System.out.println(a.toString()); //toString
		if(!a.toString().equals("The CTA Network: 7 line(s) and 0 stop(s).")) success = false;
		
		CTAStop stop1 = new CTAStop("A", 42.55, -88.5, "elevated", true, -1,0,-1,-1,-1,-1,3);
		CTAStop stop2 = new CTAStop("B", 42.55, -88.5, "elevated", true, -1,1,-1,-1,-1,-1,2);
		CTAStop stop3= new CTAStop("C", 42.55, -88.5, "elevated", true, -1,2,-1,-1,2,-1,1);
		CTAStop stop4 = new CTAStop("D", 42.55, -88.5, "elevated", true, -1,3,-1,-1,-1,-1,0);
		
		a.addStop(stop1); //addStop
		a.addStop(stop2); //addStop
		a.addStop(stop3); //addStop
		a.addStop(stop4); //addStop
		
		ArrayList<CTAStop> setOfStops = new ArrayList<CTAStop>();
		setOfStops.add(stop1);
		setOfStops.add(stop2);
		setOfStops.add(stop3);
		setOfStops.add(stop4);
		
		CTANetwork b = new CTANetwork(setOfStops); //non-default constructor 1/2
		System.out.println(b.equals(a)); //equals
		success = success && b.equals(a);
		
		b.removeStop(stop2); //remove
		System.out.println(b.toString());
		if(!b.toString().equals("The CTA Network: 7 line(s) and 3 stop(s).")) success =false;
		System.out.println(b.getStops());		
		System.out.println(b.getLines());		
		
		b.setStops(a.getStops()); //setStops and getStops
		b.setLines(a.getLines()); //setLines and getLines
		System.out.println(b.equals(a));
		success = success && b.equals(a);
		
		ArrayList<CTAStop> searchResults = a.searchByName("C"); //searchByName
		System.out.println(searchResults);
		
		CTANetwork c = new CTANetwork(a.getLines(), a.getStops()); //non-default constructor 3
		c.readFile("CTAStops.csv", input);
		c.removeStop(new CTAStop("Halsted", 41.77954, -87.64468, "elevated", true ,-1, 26, -1, -1, -1, -1, -1));
		for (CTAStop stop : c.getLines().get(1).getStops()) {
			System.out.println(stop.toString());
		}
		//since writeToFile() prompts the user, it is tested with the application class
		if (success) System.out.println("*********     CTANETWORK RESULT: SUCCESS (100%)    **********\n");
		else System.out.println("*********     CTANETWORK RESULT: FAILURE    **********\n");
		/*
		All constructors and methods were tested for all possible scenarios
		Expected output matches resulting output:
		The CTA Network: 7 line(s) and 0 stop(s).
		true
		The CTA Network: 7 line(s) and 3 stop(s).
		[A elevated 0.0 -88.5 true -1 0 -1 -1 -1 -1 2, C elevated 0.0 -88.5 true -1 1 -1 -1 2 -1 1, D elevated 0.0 -88.5 true -1 2 -1 -1 -1 -1 0]
		[Line Red - has 0 stop(s)., Line Green - has 3 stop(s)., Line Blue - has 0 stop(s)., Line Brown - has 0 stop(s)., Line Purple - has 3 stop(s)., Line Pink - has 0 stop(s)., Line Orange - has 3 stop(s).]
		true
		[C elevated 0.0 -88.5 true -1 1 -1 -1 2 -1 1]
		Harlem elevated 41.88706 -87.80486 true -1 0 -1 -1 -1 -1 -1
		Oak Park elevated 41.886784 -87.794324 false -1 1 -1 -1 -1 -1 -1
		Ridgeland elevated 41.886784 -87.784628 false -1 2 -1 -1 -1 -1 -1
		Austin elevated 41.887293 -87.774135 false -1 3 -1 -1 -1 -1 -1
		Central elevated 41.887389 -87.76565 true -1 4 -1 -1 -1 -1 -1
		Laramie elevated 41.887163 -87.754986 true -1 5 -1 -1 -1 -1 -1
		Cicero elevated 41.886519 -87.744698 true -1 6 -1 -1 -1 -1 -1
		Pulaski elevated 41.885412 -87.725404 true -1 7 -1 -1 -1 -1 -1
		Conservatory-Central Park Drive elevated 41.884904 -87.716513 true -1 8 -1 -1 -1 -1 -1
		Kenzie elevated 41.884321 -87.706155 true -1 9 -1 -1 -1 -1 -1
		California elevated 41.88422 -87.696234 true -1 10 -1 -1 -1 -1 -1
		Ashland elevated 41.885268 -87.666969 true -1 11 -1 -1 -1 11 -1
		Morgan elevated 41.8856 -87.6522 true -1 12 -1 -1 -1 12 -1
		Clinton elevated 41.885678 -87.641782 true -1 13 -1 -1 -1 13 -1
		Clark/Lake elevated 41.885767 -87.630885 true -1 14 16 26 25 14 19
		State/Lake elevated 41.88574 -87.627835 false -1 15 -1 25 26 15 20
		Washington/Wabash elevated 41.8829 -87.626205 true -1 16 -1 24 27 16 21
		Adams/Wabash elevated 41.884431 -87.6261 false -1 17 -1 23 28 17 22
		Roosevelt elevated/subway 41.867368 -87.627402 true 23 18 -1 -1 -1 -1 14
		Cermak-McCormick elevated 41.8531548 -87.626423 true -1 19 -1 -1 -1 -1 -1
		35th-Bronzeville-IIT elevated 41.831677 -87.625826 true -1 20 -1 -1 -1 -1 -1
		Indiana elevated 41.821732 -87.621371 true -1 21 -1 -1 -1 -1 -1
		43rd elevated 41.816462 -87.619021 true -1 22 -1 -1 -1 -1 -1
		47th elevated 41.8094 -87.61909 true -1 23 -1 -1 -1 -1 -1
		51st elevated 41.8022 -87.61903 true -1 24 -1 -1 -1 -1 -1
		Garfield elevated 41.79454 -87.61835 true -1 25 -1 -1 -1 -1 -1
		Ashland/63rd elevated 41.77943 -87.66393 true -1 26 -1 -1 -1 -1 -1
		A elevated 0.0 -88.5 true -1 27 -1 -1 -1 -1 10
		B elevated 0.0 -88.5 true -1 28 -1 -1 -1 -1 9
		C elevated 0.0 -88.5 true -1 29 -1 -1 8 -1 8
		D elevated 0.0 -88.5 true -1 30 -1 -1 -1 -1 7
		*/
	}
}

