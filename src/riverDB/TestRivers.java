package riverDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

public class TestRivers {

	private static RiverDB riverDB;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		riverDB = readCSV ("/home/arno/workspace/CampPlaner/data/river.csv");
		System.out.println (riverDB);
		
		try {
			System.out.println ("saving...");
			riverDB.write ("riverdb.xml");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Serializer serializer = new Persister();
		File file = new File("riverdb.xml");
		try {
			System.out.println ("loading...");
			riverDB = serializer.read(RiverDB.class, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println (riverDB);
		
	}
	
	
	public static RiverDB readCSV (String fName) {
		
		riverDB = new RiverDB ();
	
		
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
					        "Parse Fluss: " + lcsvp.getValueByLabel("Fluss") +
					        " Start: " + lcsvp.getValueByLabel("Einstieg") +
					        " Ziel: " + lcsvp.getValueByLabel("Ausstieg")
					    );

					
					riverDB.add (lcsvp.getValueByLabel("Fluss"), lcsvp.getValueByLabel("Einstieg"), lcsvp.getValueByLabel("Ausstieg"), 
							lcsvp.getValueByLabel("WW"), Integer.parseInt(lcsvp.getValueByLabel("WW Stufe")), 
							Integer.parseInt(lcsvp.getValueByLabel("Km")), Integer.parseInt(lcsvp.getValueByLabel("Entfernung")),
							lcsvp.getValueByLabel("Land"), lcsvp.getValueByLabel("Info"),
							Integer.parseInt(lcsvp.getValueByLabel("Min")), Integer.parseInt(lcsvp.getValueByLabel("Max")), lcsvp.getValueByLabel("Einheit"), 
							Integer.parseInt(lcsvp.getValueByLabel("Teilnehmer")));

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
			 
		return riverDB;
	}

}
