package printOut;

import gui.MainWindow;

import java.awt.BasicStroke;
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
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import tripDB.Trip;

@SuppressWarnings("serial")
public class PrintPanel extends JPanel implements Printable {
	
	private ImageIcon formSheet;
	private Date dayToPrint;
	private List<Trip> tripsOfDay;
	
	public PrintPanel (ImageIcon formSheet, Date dayToPrint) {
		
		super (null);
		
		this.formSheet = formSheet;
		this.dayToPrint = dayToPrint;
		tripsOfDay = MainWindow.tripDB.getAllTrips(dayToPrint);
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
       	// this is just to detect, if we print to screen or to a printer output
       	if (((scaleX == 1.0) && (scaleY == 1.0)) || !MainWindow.setupData.getPrintOnFormSheet())
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

        putText (g2d, new SimpleDateFormat("HH:mm").format(tripsOfDay.get(pageIndex).getTripStartTime()) , PrintElementSetupList.FONT_TIME);
        putText (g2d, "" + tripsOfDay.get(pageIndex).getRiver().getDistanceToStart(), PrintElementSetupList.FONT_DISTANCE);

        putComment (g2d, "" + tripsOfDay.get(pageIndex).getComment(), PrintElementSetupList.FONT_COMMENT);
        
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
        
        if (tripsOfDay.get(pageIndex).getIsKidsTour()) {
        	xout_participants (g2d, PrintElementSetupList.PARA_XOUT_PARTICIPANTS_1, 
        					PrintElementSetupList.PARA_XOUT_PARTICIPANTS_2, 
        					tripsOfDay.get(pageIndex).getGroupSize());
        	// FIXME: patch driver label
        }
        else {
            xout_participants (g2d, PrintElementSetupList.PARA_XOUT_PARTICIPANTS_1, 
            				PrintElementSetupList.PARA_XOUT_PARTICIPANTS_2, 
            				tripsOfDay.get(pageIndex).getGroupSize()- tripsOfDay.get(pageIndex).getDriverCount());
        }

        g.setFont(oldFont);
		return PAGE_EXISTS;
	}
	
	private void putText (Graphics2D g2d, String t, int i) {

		Integer fWidth = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getFieldWidth();
		Font font = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintFont();
		Integer fontSize = font.getSize();
		Integer minFontSize = fontSize / 2;
		
		g2d.setFont(font);
		while ((fWidth < g2d.getFontMetrics().getStringBounds(t, g2d).getWidth()) &&
				( fontSize > minFontSize )) {
			fontSize = fontSize -1;
			g2d.setFont(font.deriveFont((float)fontSize));
		}
		
		g2d.setColor(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintColor());
        Point a = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getAnchorPoint();
        g2d.drawString(t, a.x, a.y);
	}
	
	private void putComment (Graphics2D g2d, String t, int i) {

		Integer fWidth = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getFieldWidth();
		Font font = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintFont();
		Integer fontSize = font.getSize();
		Integer minFontSize = fontSize / 2;
		
		g2d.setFont(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintFont());

		if ((t == null) || (t.equals(""))) {
			return;
		}
	
		g2d.setColor(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintColor());
        Point a = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getAnchorPoint();
        TextLayout tl = new TextLayout("AbcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZ", 
        		MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintFont(), 
        		g2d.getFontRenderContext());
        String[] outputs = t.split("\n");
        for(int l=0; l<outputs.length; l++){
        	
    		g2d.setFont(font);
    		while ((fWidth < g2d.getFontMetrics().getStringBounds(outputs[l], g2d).getWidth()) &&
    				( fontSize > minFontSize )) {
    			fontSize = fontSize -1;
    			g2d.setFont(font.deriveFont((float)fontSize));
    		}
        	
            g2d.drawString(outputs[l], a.x,(int) (a.getY()+l*tl.getBounds().getHeight()*1.3));
        }
	}

	private void xout_participants (Graphics2D g2d, int i, int j, int n) {
		
		if (n==13)
			return;
		g2d.setColor(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintColor());
        Point a = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getAnchorPoint();
        Point b = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (j).getAnchorPoint();
        g2d.setStroke(new BasicStroke(5));
        int y1 = a.y + ((b.y -a.y) / 13) * n;
		g2d.drawRect(a.x, y1, b.x - a.x, b.y - y1);
		g2d.drawLine(a.x, y1, b.x, b.y);
		g2d.drawLine(b.x, y1, a.x, b.y);
		
	}
	
	private void xout_car (Graphics2D g2d, int i) {
		
		g2d.setColor(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getPrintColor());
        Point a = MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup (i).getAnchorPoint();
        g2d.setStroke(new BasicStroke(5));
		g2d.drawRect(a.x, a.y, 220, 100);
		g2d.drawLine(a.x, a.y, a.x+220, a.y+100);
		g2d.drawLine(a.x+220, a.y, a.x, a.y+100);
		
	}
	
}
