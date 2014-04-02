package printOut;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import tripDB.Trip;
import tripDB.TripDB;

@SuppressWarnings("serial")
public class PrintPanel extends JPanel implements Printable {
	
	private ImageIcon formSheet;
//	private JLabel pictureLabel;
//	private JLabel groupNumberLabel;
	private TripDB trips;
	private Date dayToPrint;
	private List<Trip> tripsOfDay;
	private PrintElementSetupList printSetup;
	
	public PrintPanel (ImageIcon formSheet, TripDB trips, Date dayToPrint, PrintElementSetupList printSetup) {
		
		super (null);
		
		this.formSheet = formSheet;
		this.trips = trips;
		this.dayToPrint = dayToPrint;
		tripsOfDay = trips.getAllTrips(dayToPrint);
		this.printSetup = printSetup;
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		if (pageIndex >= tripsOfDay.size())
			return NO_SUCH_PAGE;
		
		Font oldFont = g.getFont();
        Graphics2D g2d = (Graphics2D) g;
		double scaleX = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();
		double scaleY = g2d.getDeviceConfiguration().getDefaultTransform().getScaleY();
   
		if ((scaleX == 1.0) && (scaleY == 1.0))
			g2d.drawImage(formSheet.getImage(), 0, 0, (int)pageFormat.getWidth(), (int)pageFormat.getHeight(), 0, 0, formSheet.getIconWidth(), formSheet.getIconHeight(), null);

        putText (g2d, tripsOfDay.get(pageIndex).getGroupNumber().toString(), PrintElementSetupList.FONT_GROUP_NUMBER);
        putText (g2d, tripsOfDay.get(pageIndex).getTotalGroupSize().toString(), PrintElementSetupList.FONT_GROUP_SIZE);
        putText (g2d, tripsOfDay.get(pageIndex).getRiver().getRiverName(), PrintElementSetupList.FONT_RIVERNAME);
        putText (g2d, new SimpleDateFormat("EEEE").format(dayToPrint) , PrintElementSetupList.FONT_GROUP_DAY);
        putText (g2d, new SimpleDateFormat("dd.MM").format(dayToPrint) , PrintElementSetupList.FONT_GROUP_DATE);
        putText (g2d, tripsOfDay.get(pageIndex).getRiver().getWwLevel(), PrintElementSetupList.FONT_WW_LEVEL);
        if (tripsOfDay.get(pageIndex).getIsEducation())
        	putText (g2d, "X", PrintElementSetupList.FONT_EDUCATION);
        else 
        	putText (g2d, "X", PrintElementSetupList.FONT_TOUR);
        
        putText (g2d, tripsOfDay.get(pageIndex).getRiver().getTripFrom(), PrintElementSetupList.FONT_FROM);
        putText (g2d, tripsOfDay.get(pageIndex).getRiver().getTripTo(), PrintElementSetupList.FONT_TO);
        putText (g2d, "" + tripsOfDay.get(pageIndex).getRiver().getTripLength(), PrintElementSetupList.FONT_TRIP_LENGTH);

        putText (g2d, new SimpleDateFormat("hh.mm").format(tripsOfDay.get(pageIndex).getTripStartTime()) , PrintElementSetupList.FONT_TIME);
        putText (g2d, "" + tripsOfDay.get(pageIndex).getRiver().getDistanceToStart(), PrintElementSetupList.FONT_DISTANCE);

        putText (g2d, "" + tripsOfDay.get(pageIndex).getComment(), PrintElementSetupList.FONT_COMMENT);
        
        int d = tripsOfDay.get(pageIndex).getDriverCount();
        if (d < 1) {
        	xout_car (g2d, PrintElementSetupList.PARA_XOUT_CAR_1);
        }
        if (d < 2) {
        	xout_car (g2d, PrintElementSetupList.PARA_XOUT_CAR_2);
        }
        if (d < 3) {
        	xout_car (g2d, PrintElementSetupList.PARA_XOUT_CAR_3);
        }
        
        xout_participants (g2d, PrintElementSetupList.PARA_XOUT_PARTICIPANTS_1, 
        						PrintElementSetupList.PARA_XOUT_PARTICIPANTS_2, 
        						tripsOfDay.get(pageIndex).getGroupSize()- tripsOfDay.get(pageIndex).getDriverCount());

        g.setFont(oldFont);
		return PAGE_EXISTS;
	}
	
	private void putText (Graphics2D g2d, String t, int i) {

		g2d.setFont(printSetup.getPrintElementSetup (i).getPrintFont());
		g2d.setColor(printSetup.getPrintElementSetup (i).getPrintColor());
        Point a = printSetup.getPrintElementSetup (i).getAnchorPoint();
        g2d.drawString(t, a.x, a.y);

	}

	private void xout_participants (Graphics2D g2d, int i, int j, int n) {
		
		if (n==13)
			return;
		g2d.setColor(printSetup.getPrintElementSetup (i).getPrintColor());
        Point a = printSetup.getPrintElementSetup (i).getAnchorPoint();
        Point b = printSetup.getPrintElementSetup (j).getAnchorPoint();
        g2d.setStroke(new BasicStroke(5));
        int y1 = a.y + ((b.y -a.y) / 13) * n;
		g2d.drawRect(a.x, y1, b.x - a.x, b.y - y1);
		g2d.drawLine(a.x, y1, b.x, b.y);
		g2d.drawLine(b.x, y1, a.x, b.y);
		
	}
	
	private void xout_car (Graphics2D g2d, int i) {
		
		g2d.setColor(printSetup.getPrintElementSetup (i).getPrintColor());
        Point a = printSetup.getPrintElementSetup (i).getAnchorPoint();
        g2d.setStroke(new BasicStroke(5));
		g2d.drawRect(a.x, a.y, 220, 100);
		g2d.drawLine(a.x, a.y, a.x+220, a.y+100);
		g2d.drawLine(a.x+220, a.y, a.x, a.y+100);
		
	}
	
}
