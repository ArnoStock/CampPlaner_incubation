package rosterDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;



public class TestRosters {
	
	private static RosterDB rosterDB;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		rosterDB = readCSV ("/home/arno/workspace/CampPlaner/data/roster.csv");
		System.out.println (rosterDB);
		
		try {
			System.out.println ("saving...");
			rosterDB.write ("rosterdb.xml");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Serializer serializer = new Persister();
		File file = new File("rosterdb.xml");
		try {
			System.out.println ("loading...");
			rosterDB = serializer.read(RosterDB.class, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println (rosterDB);
		
	}
	
	
	public static RosterDB readCSV (String fName) {
		
		rosterDB = new RosterDB ();
	
		
		LabeledCSVParser lcsvp = null;
		try {
			lcsvp = new LabeledCSVParser(
				    new CSVParser(
				    		new FileReader( fName )		            
				    )
				);
			
			try {
				while(lcsvp.getLine() != null){

					System.out.println(
					        "Parse Nachname: " + lcsvp.getValueByLabel("Name") +
					        " Vorname: " + lcsvp.getValueByLabel("Vorname") +
					        " Handy: " + lcsvp.getValueByLabel("Handy")
					    );

					
					rosterDB.add (	lcsvp.getValueByLabel("Name"), lcsvp.getValueByLabel("Vorname"), 
									lcsvp.getValueByLabel("Handy"), Boolean.getBoolean(lcsvp.getValueByLabel("Aspirant")));

				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
		return rosterDB;
	}
	
}

