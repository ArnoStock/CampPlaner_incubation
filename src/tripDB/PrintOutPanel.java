package tripDB;

import gui.SetupData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.Calendar;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSize.ISO;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import printOut.TripsPreviewFrame;
import printOut.PrintPanel;

@SuppressWarnings("serial")
public class PrintOutPanel extends JPanel implements ActionListener {

	private TripDB tripDB;
	private Calendar targetDay;
	
	private PrinterJob printerJob = PrinterJob.getPrinterJob();
	private HashPrintRequestAttributeSet printAttributes = 
			new HashPrintRequestAttributeSet();
	
	private JButton previewTripsButton;
	private JButton previewSummaryButton;
	private JTextArea checkResultText;
	private JTextField errorsField;
	private JTextField warningsField;
	
	private TripsPreviewFrame preview;
	
	private ImageIcon formSheet;
	
	private SetupData setupData;
	
	public PrintOutPanel(TripDB tripDB, Calendar targetDay, SetupData setupData) {

		super (new BorderLayout ());
		
		this.tripDB = tripDB;
		this.targetDay = targetDay;
		this.setupData = setupData;
		printAttributes.add(MediaSizeName.ISO_A4);

		JPanel bottomPanel = new JPanel (new GridLayout (2,1));
		JPanel statusPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		errorsField = new JTextField ("Fehler: 0");
		errorsField.setEditable(false);
		warningsField = new JTextField ("Warnungen: 0");
		warningsField.setEditable(false);
		statusPanel.add (errorsField);
		statusPanel.add (warningsField);
		
		JPanel bp = new JPanel (new GridLayout (1,2));
		
		previewTripsButton = new JButton ("Fahrtenzettel", new ImageIcon ("toolbarButtonGraphics/general/PrintPreview24.gif"));
		previewTripsButton.setActionCommand("PreviewTrips");
		previewTripsButton.addActionListener(this);
		bp.add (previewTripsButton);
	
		previewSummaryButton = new JButton ("Zusammenfassung", new ImageIcon ("toolbarButtonGraphics/general/PrintPreview24.gif"));
		previewSummaryButton.setActionCommand("PreviewSummary");
		previewSummaryButton.addActionListener(this);
		bp.add (previewSummaryButton);
		
		bottomPanel.add (statusPanel);
		bottomPanel.add(bp);
		add(bottomPanel, BorderLayout.SOUTH);
		
		checkResultText = new JTextArea();
		JScrollPane ts = new JScrollPane (checkResultText);
		add (ts, BorderLayout.CENTER);
		
		formSheet = new ImageIcon ("/home/arno/Dokumente/Dokumente/JEM/FAHRTENZETTEL11.jpg", "Fahrtenzettel");
//		formSheet = new ImageIcon ("/home/arno/Dokumente/Dokumente/JEM/FAHRTENZETTEL_hires.jpg", "Fahrtenzettel");

	}
	
	public void updateTripList (TripDB tripDB) {
		this.tripDB = tripDB;
		checkTrips();
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	
	@Override
	public void actionPerformed(ActionEvent evt) {
	
		if (evt.getActionCommand().equals("PreviewTrips")) {
			
			PrintPanel previewPanel = new PrintPanel (formSheet, tripDB, targetDay.getTime(), setupData.getPrintElementSetupList());
			
//			PreviewFrame preview = new PreviewFrame ((Frame) SwingUtilities.getRoot(this), testLabel.getPrintable(new MessageFormat("Capitals"), new MessageFormat("{0}")), printerJob.getPageFormat(printAttributes));
			PageFormat pf = printerJob.getPageFormat(printAttributes); 
			Paper p = pf.getPaper();
			p.setImageableArea(0, 0, p.getWidth(), p.getHeight());
			pf.setPaper(p);
			TripsPreviewFrame preview = new TripsPreviewFrame ((Frame) SwingUtilities.getRoot(this), previewPanel, pf ,setupData);
			
		}

		if (evt.getActionCommand().equals("PreviewSummary")) {
			printerJob.pageDialog(printAttributes);
//			printerJob.printDialog(printAttributes);

		}
		
	}

	public void checkTrips() {
		
		tripDB.checkDay (targetDay.getTime(), checkResultText);
		int e = tripDB.getCheckErrors();
		int w = tripDB.getCheckWarnings();
		if (e > 0) {
			errorsField.setFont(errorsField.getFont().deriveFont(Font.BOLD));
			errorsField.setForeground(Color.RED);
		}
		else {
			errorsField.setFont(errorsField.getFont().deriveFont(Font.PLAIN));
			errorsField.setForeground(new Color (0, 100, 0));
		}
		errorsField.setText("Fehler: " + e);
		if (w > 0) {
			warningsField.setFont(errorsField.getFont().deriveFont(Font.BOLD));
			warningsField.setForeground(new Color (100, 100, 0));
		}
		else {
			warningsField.setFont(errorsField.getFont().deriveFont(Font.PLAIN));
			warningsField.setForeground(new Color (0, 100, 0));
		}
		warningsField.setText("Warnungen: " + w);
		
	}

}
