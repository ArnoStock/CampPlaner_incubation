package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import riverDB.RiverDB;
import riverDB.RiverDBPanel;
import rosterDB.RosterDB;
import rosterDB.RosterDBPanel;
import tripDB.TripDB;
import tripDB.TripDBPanel;

public class MainWindow extends JFrame {

	public static final String versionString = "V0.3";
	public static RiverDB riverDB;
	public static RosterDB rosterDB;
	public static TripDB tripDB;
	
	private EventOptionsDialog eventOptionsDialog;
	private ComputerOptionsDialog computerOptionsDialog;
	public static SetupData setupData;
	private TripDBPanel tripDBPanel;
	private RiverDBPanel riverDBPanel;
	private RosterDBPanel rosterDBPanel;
	
	private About aboutDialog;
	
	JFileChooser fc = new JFileChooser();
	
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
        setTitle("Camp Planer " + versionString);
        setSize(800, 600);
	    setMinimumSize(new Dimension(500, 300));

        // import setup data
        try {
			System.out.println ("Trying to read setup file " + SETUP_FILE_NAME + ".");
        	setupData = SetupData.read (SETUP_FILE_NAME);
		} catch (Exception e) {
			System.out.println ("Could not read setup file. Using default setup.");
			System.out.println (e);
	        setupData = new SetupData ();
		}
        eventOptionsDialog = new EventOptionsDialog (this);
        computerOptionsDialog = new ComputerOptionsDialog(this);
        createMenuBar ();

        // Create JTabbedPane object
        JTabbedPane tabpane = new JTabbedPane
           (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
        // create panel to edit trip information
        tripDBPanel = new TripDBPanel();
        tripDBPanel.setBackground(Color.LIGHT_GRAY);
        tabpane.addTab("Fahrten", tripDBPanel);
        // create panel to edit river information
        riverDBPanel = new RiverDBPanel();
        tabpane.addTab("Fl\u00FCsse", riverDBPanel);
        // create panel to edit roster information
        rosterDBPanel = new RosterDBPanel();
        rosterDBPanel.setBackground(Color.GREEN);
		setupData.addPropertyChangeListener(rosterDBPanel);
        tabpane.addTab("Fahrtenleiter", rosterDBPanel);
        // add tabs to window
        add(tabpane);    
        
        aboutDialog = new About (this);

        readFromFile(setupData.getDataFileName());

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
        addMenuItemWithAccelerator (fileMenu, "Neu", "New", KeyEvent.VK_N);
        
        fileMenu.addSeparator();
        JMenu importMenu = new JMenu ("Import");
        importMenu.setMnemonic(KeyEvent.VK_I);
        addMenuItemWithAccelerator (importMenu, "Fl\u00FCsse", "ImportRivers", KeyEvent.VK_F);
        addMenuItemWithAccelerator (importMenu, "Fahrtenleiter", "ImportRosters", KeyEvent.VK_A);
        fileMenu.add(importMenu);
        
        JMenu exportMenu = new JMenu ("Export");
        exportMenu.setMnemonic(KeyEvent.VK_E);
        addMenuItemWithAccelerator (exportMenu, "Fl\u00FCsse", "ExportRivers", KeyEvent.VK_F);
        addMenuItemWithAccelerator (exportMenu, "Fahrtenleiter", "ExportRosters", KeyEvent.VK_A);
        fileMenu.add(exportMenu);

        fileMenu.addSeparator();
        addMenuItemWithAccelerator (fileMenu, "Beenden", "Quit", KeyEvent.VK_B);
        menuBar.add( fileMenu );

//        JMenu linkMenu = new JMenu( "Links" );
//        menuBar.add( linkMenu );

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

			// save backup file
	  		saveBackupFile (setupData.getDataFileName());
	  		// save file
	  		saveToFile(setupData.getDataFileName());

	  	}
	  	
	  	if (cmdString == "Load") {
	  		
	    	if( rosterDB.isDataChanged () || tripDB.isDataChanged() || riverDB.isDataChanged() ) {
	    		if ( JOptionPane.showConfirmDialog(null,
					"Ge\u00E4nderte Daten sind noch nicht abgespeichert und gehen verloren!",
					"Datei laden ohne zu speichern?", 
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION ) {
						return;
				}
			}

	  		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "CampPlaner Dateien", "cplan");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this, "CampPlaner Datei \u00F6ffnen");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				readFromFile (fc.getSelectedFile().getAbsolutePath());
			}	  		
	  	}
	
	  	if (cmdString == "New") {
	  		
	    	if( rosterDB.isDataChanged () || tripDB.isDataChanged() || riverDB.isDataChanged() ) {
	    		if ( JOptionPane.showConfirmDialog(null,
					"Ge\u00E4nderte Daten sind noch nicht abgespeichert und gehen verloren!\nDaten verwerfen ohne zu speichern?",
					"Datei verwerfen ohne zu speichern?", 
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION ) {
						return;
				}
			}
	    	
	    	rosterDB = new RosterDB ();
	    	riverDB = new RiverDB ();
	    	tripDB = new TripDB ();
	    	
			riverDB.addPropertyChangeListener(tripDBPanel);
			rosterDB.addPropertyChangeListener(tripDBPanel);
			rosterDBPanel.refreshList();
			riverDBPanel.refreshList();
			tripDBPanel.refreshList();
	  	}
	  	
	  	if (cmdString == "ImportRivers") {
	  		
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "comma separated values", "csv");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this, "Fl\u00FCsse importieren");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				riverDB = RiverDB.readCSV (fc.getSelectedFile().getAbsolutePath());
				riverDB.addPropertyChangeListener(tripDBPanel);
				riverDB.setIsDataChanged (true);
				riverDBPanel.refreshList();
			}
	  	}

	  	if (cmdString == "ExportRivers") {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "comma separated values", "csv");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this, "Fl\u00FCsse exportieren");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				RiverDB.writeCSV (riverDB, fc.getSelectedFile().getAbsolutePath());
			}	  		
	  	}
	  	
	  	if (cmdString == "ImportRosters") {
	  		
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "comma separated values", "csv");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this, "Fahrtenleiter importieren");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				rosterDB.addCSV (fc.getSelectedFile().getAbsolutePath(), RosterDB.READ_MODE_ADD);
				rosterDBPanel.refreshList();
			}
	  		
	  	}

	  	if (cmdString == "ExportRosters") {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "comma separated values", "csv");
			fc.setFileFilter(filter);
			int returnVal = fc.showDialog(this, "Fahrtenleiter exportieren");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				RosterDB.writeCSV (rosterDB, fc.getSelectedFile().getAbsolutePath());
			}	  		
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
//	  		computerOptionsDialog.setSetupData(setupData); // FIXME: set cloned object
	 		computerOptionsDialog.setVisible(true);
	 		if (computerOptionsDialog.getModalResult()) {
//	 			setupData = computerOptionsDialog.getSetupData();
	 		}
	  	}
		
	  	if (cmdString == "About") {
	  		aboutDialog.setVisible(true);
	  	}
	}

    protected void processWindowEvent(WindowEvent e) {
    	if (e.getID() == WindowEvent.WINDOW_CLOSING) {

   	    	if( rosterDB.isDataChanged () || tripDB.isDataChanged() || riverDB.isDataChanged() ) {
   	    		if ( JOptionPane.showConfirmDialog(null,
   					"Sollen die Daten jetzt abgespeichert werden?", 
   	    			"Daten sind noch nicht abgespeichert",
   					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) {
 						saveToFile(setupData.getDataFileName());
   				}
				saveBackupFile(setupData.getDataFileName());
    	    }
    	    
    		try {
				setupData.write(SETUP_FILE_NAME);
				System.out.println ("Setup information written to " + SETUP_FILE_NAME + ".");
			} catch (Exception exception) {
				System.out.println ("Could not save setup information to file " + SETUP_FILE_NAME + ".");
				exception.printStackTrace();
			}
	        dispose();
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
    
    public void readFromFile (String fileName) {

    	ZipFile zipIn = null;
		// open the ZIP file
		try {
			zipIn = new ZipFile (fileName);
		} catch (IOException e1) {
//			e1.printStackTrace();
			System.err.println ("Datei " + fileName + " kann nicht ge\u00f6ffnet werden!");
			riverDB = new RiverDB();
			rosterDB = new RosterDB ();
			tripDB = new TripDB();
		}
    
		if (zipIn != null) {
	        // pull data from zip
			try {
				riverDB = RiverDB.read (zipIn);
			} catch (Exception e1) {
				e1.printStackTrace();
				riverDB = new RiverDB();
			}

			try {
				rosterDB = RosterDB.read (zipIn);
			} catch (Exception e1) {
				e1.printStackTrace();
				rosterDB = new RosterDB ();
			}
			
			try {
				tripDB = TripDB.read (zipIn);
			} catch (Exception e1) {
				e1.printStackTrace();
				tripDB = new TripDB();
			}
			
			try {
				zipIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		riverDB.addPropertyChangeListener(tripDBPanel);
		rosterDB.addPropertyChangeListener(tripDBPanel);
		rosterDBPanel.refreshList();
		riverDBPanel.refreshList();
		tripDBPanel.refreshList();
	}

	protected void saveBackupFile (String fileName) {

  		File backupFile = new File(fileName);
  		String backupFileName;
		try {
			backupFileName = backupFile.getCanonicalPath();
	  		if (backupFileName.contains("."))
	  			backupFileName = backupFileName.substring(0, backupFileName.lastIndexOf('.'));
	  		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
	        backupFileName += "_" + dateFormat.format(new Date()) + ".cplan";
	  		saveToFile(backupFileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void saveToFile (String fileName) {

    	ZipOutputStream zipOut = null;
    	// create new ZIP file
        try {
			zipOut = new ZipOutputStream(new FileOutputStream(fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
        // enable compression
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        // set compression level to max.
        zipOut.setLevel (9);

        // save trips
        try {
			tripDB.write(zipOut);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // save rosters
        try {
			rosterDB.write(zipOut);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // save rivers
        try {
			riverDB.write(zipOut);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
			zipOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public static ImageIcon getImageIcon (String iconName) {
	    if (ClassLoader.getSystemResource(iconName) != null)
			return new ImageIcon (ClassLoader.getSystemResource(iconName));
		else return new ImageIcon (iconName);
    }
}
