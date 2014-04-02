package tripDB;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TripTimeIndicatorComponent extends JPanel implements ChangeListener, MouseMotionListener, MouseListener {

	private Trip trip;
	private JSpinner startTimeSpinner;
	private int startPointX;
	private JPanel slider;
	
	private final int START_TIME_H = 8;
	private final int END_TIME_H = 13;
	private final int INDICATOR_WIDTH = 600;
	private double scale;
	private Calendar cal;

	public TripTimeIndicatorComponent (Trip trip) {
		super ();
		
		this.trip = trip;
		this.setLayout(null);
		cal = Calendar.getInstance();
		
        //Add the start time spinner.
		slider = new JPanel();
	    SpinnerModel dateModel = new SpinnerDateModel(trip.getTripStartTime(),
                                     null,
                                     null,
                                     Calendar.MINUTE);//ignored for user input
        startTimeSpinner = new JSpinner (dateModel);
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "HH:mm"));
        startTimeSpinner.addChangeListener(this);
        slider.add (startTimeSpinner);
//		Border empty = BorderFactory.createEmptyBorder(2,2,2,2);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
//		Border compound = BorderFactory.createCompoundBorder(raisedbevel, empty);
		slider.setBorder(raisedbevel);

        add (slider);
        slider.addMouseMotionListener(this);
        slider.addMouseListener(this);
        
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
//		compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		setBorder(loweredbevel);
		
//		setPreferredSize(new Dimension (400, getPreferredSize().height));
		setPreferredSize(new Dimension (INDICATOR_WIDTH, slider.getPreferredSize().height+6));
//System.out.println ("loc: "+slider.getLocation());

		scale = (INDICATOR_WIDTH-6-slider.getWidth()) / ((END_TIME_H - START_TIME_H)*60);

		slider.setSize(slider.getPreferredSize());
		slider.setLocation(timeToX((Date) startTimeSpinner.getValue()), 3);
		slider.invalidate();
		slider.repaint();

	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {

		Date d = (Date) startTimeSpinner.getValue();
		trip.setTripStartTime(d);
		slider.setLocation(timeToX(d), slider.getLocation().y);
		
	}
	
	private int truncX (int x) {
		
		if (x < 3) {
			x = 3;
		}
		if ((x + slider.getWidth() + 3) > INDICATOR_WIDTH) {
			x = INDICATOR_WIDTH - slider.getWidth() - 3;
		}
		
//System.out.println (this.getWidth() + " - "+ x + " " + slider.getWidth());
		return x;
	}
	
	private int timeToX (Date d) {

		cal.setTime(d);
		int t = (cal.get(Calendar.HOUR_OF_DAY)-START_TIME_H) * 60 + cal.get(Calendar.MINUTE);
		int x = (int) Math.round(t*scale);
		return truncX (x);
	}
	
	private Date xToDate (int x) {
		int tm = (int) Math.round( (x-3) / scale );
		int m = tm % 60;
		int h = tm / 60 + START_TIME_H;
		
		cal.set(Calendar.HOUR_OF_DAY,h);
		cal.set(Calendar.MINUTE,m);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		return cal.getTime();
	}

	@Override
	public void mouseDragged(MouseEvent evt) {

		Point p;
		if (evt.getSource().equals(slider)) {
			p = new Point (truncX(slider.getLocation().x + (evt.getX() - startPointX)), 3);
			slider.setLocation(p);
			Date d = xToDate ((int)p.getX());
			trip.setTripStartTime(d);
			startTimeSpinner.setValue(d);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		
		if (evt.getSource().equals(slider)) {
			if (evt.getButton() == MouseEvent.BUTTON1) {
				startPointX = (int) (evt.getX());
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
}
