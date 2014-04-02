package rosterDB;

import java.awt.Color;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="Availability")
public class RosterAvailability extends Object {


	public RosterAvailability() {
		// TODO Auto-generated constructor stub
	}
	
	public RosterAvailability (Date availabilityDate) {
		
		this.availabilityDate = availabilityDate;
	}

	public final static String getAvailabilityString (int availabilityCode) {
			switch (availabilityCode) {
			case ROSTER_ABSENT: return "-";
			case ROSTER_PARTICIPATES: return "T"; // Teilnehmer
			case ROSTER_VACATION: return "U"; // Urlaub
			case ROSTER_AVAILABLE: return "A"; // Arbeitet
		}
		return "-";
	}

	public final static Color getRosterAvailabilityForeGroundColor (int availabilityCode, boolean isSelected) {
		
		if (isSelected) {
			switch (availabilityCode) {
				case ROSTER_ABSENT: return Color.decode("0x202020");
				case ROSTER_PARTICIPATES: return Color.decode("0x202020");
				case ROSTER_VACATION: return Color.decode("0xE0E0E0");
				case ROSTER_AVAILABLE: return Color.decode("0x202020");
			}
		}
		
		switch (availabilityCode) {
			case ROSTER_ABSENT: return Color.BLACK;
			case ROSTER_PARTICIPATES: return Color.BLACK;
			case ROSTER_VACATION: return Color.WHITE;
			case ROSTER_AVAILABLE: return Color.BLACK;
		}
		return Color.WHITE;
	}

	
	public final static Color getRosterAvailabilityBackGroundColor (int availabilityCode, boolean isSelected) {
		
		if (isSelected) {
				switch (availabilityCode) {
				case ROSTER_ABSENT: return Color.decode("0xE0E0E0");
				case ROSTER_PARTICIPATES: return Color.decode("0xE0E080");
				case ROSTER_VACATION: return Color.decode("0x0080E0");
				case ROSTER_AVAILABLE: return Color.decode("0x00E080");
			}
		}
		
		switch (availabilityCode) {
			case ROSTER_ABSENT: return Color.decode("0xFFFFFF");
			case ROSTER_PARTICIPATES: return Color.decode("0xFFFF00");
			case ROSTER_VACATION: return Color.decode("0x0000FF");
			case ROSTER_AVAILABLE: return Color.decode("0x00FF00");
		}
		return Color.WHITE;
	}

	
	public RosterAvailability(long date, int availabilityCode) {
		
		availabilityDate = new Date (date);
		this.availabilityCode = availabilityCode;
	}
	
	public final static int ROSTER_ABSENT = 0;
	public final static int ROSTER_PARTICIPATES = 1;
	public final static int ROSTER_VACATION = 2;
	public final static int ROSTER_AVAILABLE = 3;
	
	/*
	 * Availability at this date:
	 * 0: absent
	 * 1: participant
	 * 2: vacation
	 * 3: working
	 * 
	 * */
	@Attribute
	private Integer availabilityCode;

	@Attribute
	private Date availabilityDate;
	
	public Integer getAvailabilityCode () {
		return availabilityCode;
	}
	
	public Color getRosterAvailabilityBackGroundColor (boolean isSelected) {
		return getRosterAvailabilityBackGroundColor (availabilityCode, isSelected);
	}

	public Color getRosterAvailabilityForeGroundColor (boolean isSelected) {
		return getRosterAvailabilityForeGroundColor (availabilityCode, isSelected);
	}
	
	@Override
	public String toString () {
		return getAvailabilityString (availabilityCode);
	}

	public void setAvailabilityCode(int availabilityCode) {
		this.availabilityCode = availabilityCode;
	}

	public Date getDate() {
		
		return availabilityDate;
	}

}
