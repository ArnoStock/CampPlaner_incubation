package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import riverDB.RiverDB;
import riverDB.RiverDBPanel;
import rosterDB.RosterDB;
import rosterDB.RosterDBPanel;
import tripDB.TripDB;
import tripDB.TripDBPanel;
import printOut.PrintElementSetupList;

public class MainWindow extends JFrame {

	private RiverDB riverDB;
	private RosterDB rosterDB;
	private TripDB tripDB;
	
	private EventOptionsDialog eventOptionsDialog;
	private ComputerOptionsDialog computerOptionsDialog;
	private SetupData setupData;
	private TripDBPanel panelTrips;
	
	private About aboutDialog;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SETUP_FILE_NAME = "CampPlanerSetup.xml";

	/**
	 * @param args
	 */
    static public void main (String[] args) {
        new MainWindow().setVisible(true);
    }
    
    public MainWindow () {
        setTitle("Camp Planer V0.2");
        setSize(800, 600);
	    setMinimumSize(new Dimension(500, 300));

        // import setup data
        try {
			System.out.println ("Trying to read setup file" + SETUP_FILE_NAME + ".");
        	setupData = SetupData.read (SETUP_FILE_NAME);
		} catch (Exception e) {
			System.out.println ("Could not read setup file. Using default setup.");
			System.out.println (e);
	        setupData = new SetupData ();
		}
        eventOptionsDialog = new EventOptionsDialog (this, setupData);
        computerOptionsDialog = new ComputerOptionsDialog(this, setupData);
        createMenuBar ();
        
        // import rivers
        try {
			riverDB = RiverDB.read ("riverdb.xml");
			rosterDB = RosterDB.read("rosterdb.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // import trips
        tripDB = new TripDB();
        
        // Create JTabbedPane object
        JTabbedPane tabpane = new JTabbedPane
           (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
        // create panel to edit trip information
        panelTrips = new TripDBPanel(tripDB, rosterDB, riverDB, setupData);
        panelTrips.setBackground(Color.LIGHT_GRAY);
        tabpane.addTab("Fahrten", panelTrips);
        // create panel to edit river information
        JPanel panelRivers = new RiverDBPanel(riverDB);
        tabpane.addTab("Fl\u00FCsse", panelRivers);
        // create panel to edit roster information
        JPanel panelRoster = new RosterDBPanel(rosterDB, setupData);
        panelRoster.setBackground(Color.GREEN);
        tabpane.addTab("Fahrtenleiter", panelRoster);
        // add tabs to window
        add(tabpane);    
        
        aboutDialog = new About (this);
    }
    
	@SuppressWarnings("serial")
	private void addMenuItemWithAccelerator (JMenu menu, String name, final String a, int men) {
        JMenuItem mi;
        mi = menu.add(new AbstractAction(name) { 
              public void actionPerformed(ActionEvent e) {
            	  guiAction (a);
              } 
        });
        mi.setMnemonic(men);
    }

	private void createMenuBar () {
    	
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "Datei" );
        fileMenu.setMnemonic('d');
        addMenuItemWithAccelerator (fileMenu, "Speichern", "Save", KeyEvent.VK_S);
        addMenuItemWithAccelerator (fileMenu, "Laden", "Load", KeyEvent.VK_L);
        fileMenu.addSeparator();
        addMenuItemWithAccelerator (fileMenu, "Beenden", "Quit", KeyEvent.VK_B);
        menuBar.add( fileMenu );

        JMenu linkMenu = new JMenu( "Links" );
        menuBar.add( linkMenu );

        JMenu optionsMenu = new JMenu( "Einstellungen" );
        optionsMenu.setMnemonic('e');
        addMenuItemWithAccelerator (optionsMenu, "Computer", "SetupComputer", KeyEvent.VK_C);
        addMenuItemWithAccelerator (optionsMenu, "Veranstaltung", "SetupEvent", KeyEvent.VK_V);
        menuBar.add( optionsMenu );

        JMenu helpMenu = new JMenu( "Hilfe" );
        menuBar.add( helpMenu );
        helpMenu.setMnemonic('h');
        addMenuItemWithAccelerator (helpMenu, "\u00DCber das Programm", "About", KeyEvent.VK_A);
        
        setJMenuBar( menuBar );

    }
	
    protected void guiAction(String cmdString) {
    	
	  	if (cmdString == "Save") {
	  		
    		String tripDBFileName = "TripDB.cplan";
    		try {
				tripDB.write(tripDBFileName);
			} catch (Exception e1) {
				System.out.println ("Could not save trip information to file " + tripDBFileName + ".");
				e1.printStackTrace();
			}
    		try {
    			riverDB.write("riverdb.xml");
			} catch (Exception e1) {
				System.out.println ("Could not save trip information to file " + "riverdb.xml" + ".");
				e1.printStackTrace();
			}
    		try {
    			rosterDB.write("rosterdb.xml");
			} catch (Exception e1) {
				System.out.println ("Could not save trip information to file " + "rosterdb.xml" + ".");
				e1.printStackTrace();
			}

	  	}
	  	
	  	if (cmdString == "Load") {
	  		
	  		if (isDataChanged ())
	  			return;
	  		String tripDBFileName = "TripDB.cplan";
	  		panelTrips.setTripDB (readTripDB(tripDBFileName));
	  		
	  	}
	  	
	  	if (cmdString == "Quit") {
	  		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	  	}

	  	if (cmdString == "SetupEvent") {
	  		eventOptionsDialog.setVisible(true);
	  		if (eventOptionsDialog.getModalResult()) {
	  		}
	  	}
    	
	  	if (cmdString == "SetupComputer") {
	  		computerOptionsDialog.setSetupData(setupData); // FIXME: set cloned object
	 		computerOptionsDialog.setVisible(true);
	 		if (computerOptionsDialog.getModalResult()) {
	 			setupData = computerOptionsDialog.getSetupData();	// FIXME: inform other windows
	 		}
	  	}
		
	  	if (cmdString == "About") {
	  		aboutDialog.setVisible(true);
	  	}
		
	}

    private boolean isDataChanged () {
    	if( rosterDB.isDataChanged () || tripDB.isDataChanged() || riverDB.isDataChanged() ) {
    		if ( JOptionPane.showConfirmDialog(null,
				"Beenden ohne zu speichern?", 
				"Daten sind noch nicht abgespeichert", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION ) {
					return true;
			}
		}
    	return false;
    }


    protected void processWindowEvent(WindowEvent e) {
    	if (e.getID() == WindowEvent.WINDOW_CLOSING) {

    		if (isDataChanged())
    			return;
    		
    		try {
				setupData.write(SETUP_FILE_NAME);
			} catch (Exception exception) {
				System.out.println ("Could not save setup information to file " + SETUP_FILE_NAME + ".");
				exception.printStackTrace();
			}
	        dispose(); // nicht unbedingt n�tig.
    	    System.exit(0);
    	}
    	super.processWindowEvent(e);
    }

    public TripDB readTripDB (String fn) {
	    // import rivers
	    try {
			tripDB = TripDB.read (fn);
			return tripDB;
		} catch (Exception e) {
			System.out.println ("Could not read trip data file " + fn + ".");
			e.printStackTrace();
		}
	    return null;
    }

	
}
