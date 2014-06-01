package printOut;

import gui.MainWindow;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;

import tripDB.Trip;

@SuppressWarnings("serial")
public class PrintPanelSummary extends JPanel implements Printable {
	
	private Date dayToPrint;
	private List<Trip> tripsOfDay;
	
	private static final Integer TRIP_DISTANCE = 20;
	
	public PrintPanelSummary (Date dayToPrint) {
		super (null);
		this.dayToPrint = dayToPrint;
		tripsOfDay = MainWindow.tripDB.getAllTrips(dayToPrint);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		if (tripsOfDay.size() == 0)
			return NO_SUCH_PAGE;

		Font oldFont = g.getFont();
        g.setFont(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (PrintElementSetupList.FONT_SUMMARY_SHEET).getPrintFont());
        g.setColor(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (PrintElementSetupList.FONT_SUMMARY_SHEET).getPrintColor());
        Graphics2D g2d = (Graphics2D) g;

        Integer page = 1;
        // X/Y = border size
        Point a = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (PrintElementSetupList.FONT_SUMMARY_SHEET).getAnchorPoint();
        int y;
        if (pageIndex == 0)
            y = putHeader (g2d, a.x, a.y, page);
        else 
        	y = getHeaderHeight (g2d, a.y);
        
        for (Integer t = 0; t < tripsOfDay.size(); t++) {
        	
        	String s = getTripText (t);
        	Integer textHeight = getTextBlockHeight (g2d, s);
        	
           	if (!g2d.hitClip(a.x, y + textHeight + TRIP_DISTANCE, 1, 1)) {
        		page += 1;
        		if (page == pageIndex+1)
        			y = putHeader (g2d, a.x, a.y, page);
        		else
        			y = getHeaderHeight (g2d, a.y);
        	}
   
        	if (page == pageIndex+1)
        		y = putTextBlock (g2d, s, a.x, y) + TRIP_DISTANCE;
        	else 
        		y += textHeight + TRIP_DISTANCE;
        }
		
        g.setFont(oldFont);
        
		if (pageIndex > page-1)
			return NO_SUCH_PAGE;
		return PAGE_EXISTS;
	}
	
	private Integer putHeader (Graphics2D g2d, int x, int y, int page) {

		y = putTextBlock (g2d, "Fahrtenprogramm f\u00fcr " + new SimpleDateFormat("EEEE").format(dayToPrint)
        						+ " den " + new SimpleDateFormat("dd.MM").format(dayToPrint) + "  Seite " + page, x, y);
        return y + 15;

	}

	private Integer getHeaderHeight (Graphics2D g2d, int y) {

		y = getTextBlockHeight (g2d, "Fahrtenprogramm f\u00fcr " + new SimpleDateFormat("EEEE").format(dayToPrint)
        						+ " den " + new SimpleDateFormat("dd.MM").format(dayToPrint) + "  Seite 1");
        return y + 15;
	}
	
	private int getTextBlockHeight (Graphics2D g2d, String t) {

		if ((t == null) || (t.equals(""))) {
			return 0;
		}
        TextLayout tl = new TextLayout(t, 
        		MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (PrintElementSetupList.FONT_SUMMARY_SHEET).getPrintFont(), 
        		g2d.getFontRenderContext());
        String[] outputs = t.split("\n");
        int y = 0;
        for(int l=0; l<outputs.length; l++){
            y += tl.getBounds().getHeight()*1.3;
        }
        return y;
	}

	
	private String getTripText (Integer trip) {
		
        String s = "Gruppe: " + tripsOfDay.get(trip).getGroupNumber().toString();
        s += "  Fluss: " + tripsOfDay.get(trip).getRiver().getRiverName();
        s += "  WW" + tripsOfDay.get(trip).getRiver().getWwLevel() + "\n";
        s += "Start: " + tripsOfDay.get(trip).getRiver().getTripFrom();
        s += " / Ziel: " + tripsOfDay.get(trip).getRiver().getTripTo();
        s += "  (" + tripsOfDay.get(trip).getRiver().getTripLength() + "km)\n";
        if (tripsOfDay.get(trip).getIsEducation())
        	s += "Schulungsfahrt\n";
        else 
        	s += "F\u00fchrungsdsfahrt\n";
        s += "Abfahrt: " + new SimpleDateFormat("hh.mm").format(tripsOfDay.get(trip).getTripStartTime());
        s += "  Gruppenst\u00e4rke: " + tripsOfDay.get(trip).getTotalGroupSize().toString() + "\n";
        s+= "Fahrtenleiter:\n";
        for (int r = 0; r < tripsOfDay.get(trip).getRosterCount(); r++) {
        	s += "   " + tripsOfDay.get(trip).getRosterName(r) + " (" + tripsOfDay.get(trip).getRosterPhoneNumber (r) + ")\n";
        }
        s+= "Bemerkung: " + tripsOfDay.get(trip).getComment();
        
        return s;

	}
	
	private int putTextBlock (Graphics2D g2d, String t, int x, int startY) {

		int y = startY;
		if ((t == null) || (t.equals(""))) {
			return y;
		}
        TextLayout tl = new TextLayout(t, 
        		MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (PrintElementSetupList.FONT_SUMMARY_SHEET).getPrintFont(), 
        		g2d.getFontRenderContext());
        String[] outputs = t.split("\n");
        for(int l=0; l<outputs.length; l++){
            g2d.drawString(outputs[l], x, y);
            y += tl.getBounds().getHeight()*1.3;
        }
        return y;
	}

	
}
