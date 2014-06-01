package printOut;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

public class PrintElementSetupList {

	public final static int FONT_GROUP_NUMBER = 0;
	public final static int FONT_GROUP_SIZE = 1;
	public final static int FONT_GROUP_DAY = 2;
	public final static int FONT_GROUP_DATE = 3;
	public final static int FONT_WW_LEVEL = 4;
	public final static int FONT_TOUR = 5;
	public final static int FONT_EDUCATION = 6;
	public final static int FONT_RIVERNAME = 7;
	public final static int FONT_FROM = 8;
	public final static int FONT_TO = 9;
	public final static int FONT_TRIP_LENGTH = 10;
	public final static int FONT_DISTANCE = 11;
	public final static int FONT_TIME = 12;
	public final static int FONT_COMMENT = 13;
	public final static int PARA_XOUT_CAR_1 = 14;
	public final static int PARA_XOUT_CAR_2 = 15;
	public final static int PARA_XOUT_CAR_3 = 16;
	public final static int PARA_XOUT_PARTICIPANTS_1 = 17;
	public final static int PARA_XOUT_PARTICIPANTS_2 = 18;

	public final static int FONT_SUMMARY_SHEET = 19;

	public final static int PARA_COUNT = 20;
	
	private final String PARA_NAMES[] = { "Gruppennummer", "Gruppengr\u00f6\u00dfe", "Wochentag", "Datum", "WW Einstufung", 
			"Kanutour", "Schulung", "Flussname", "Start", "Ziel", "Flussl\u00e4nge", "Anfahrt", "Zeit", "Bemerkung",
			"Auto 1", "Auto 2", "Auto 3", "Teilnehmer Start", "Teilnehmer Ende", "Zusammenfassung" };

	
	@ElementList(name="PrintElementSetup")
	ArrayList <PrintElementSetup> printElements;

	public PrintElementSetupList () {
		printElements = new ArrayList<PrintElementSetup>();
	}
	
	public PrintElementSetupList (Font defaultFont) {
		
		printElements = new ArrayList<PrintElementSetup>();
		
		for (int i = 0; i < PARA_COUNT; i++) {
			printElements.add(new PrintElementSetup (defaultFont, new Point (0,0), Color.black));
		}
		setDefaultPrintElementConfiguration (defaultFont);
	}
	
	public PrintElementSetup getPrintElementSetup (int index) {
		return printElements.get(index);
	}

	public void setDefaultPrintElementConfiguration (Font defaultFont) {
		
		printElements = new ArrayList<PrintElementSetup>();
		
		for (int i = 0; i < PARA_COUNT; i++) {
			printElements.add(new PrintElementSetup (defaultFont, new Point (0,0), Color.black));
		}
		
		Font lFont = printElements.get(FONT_GROUP_NUMBER).getPrintFont().deriveFont(Font.BOLD, 26.0f);
		Font mFont = printElements.get(FONT_GROUP_NUMBER).getPrintFont().deriveFont(Font.BOLD, 20.0f);
		Font sFont = printElements.get(FONT_GROUP_NUMBER).getPrintFont().deriveFont(Font.BOLD, 16.0f);
		
		printElements.get(FONT_GROUP_NUMBER).setPrintFont(lFont);
		printElements.get(FONT_GROUP_NUMBER).setAnchorPoint(new Point (530, 60));
		
		printElements.get(FONT_GROUP_SIZE).setPrintFont(lFont);
		printElements.get(FONT_GROUP_SIZE).setAnchorPoint(new Point (290, 345));

		printElements.get(FONT_RIVERNAME).setPrintFont(sFont);
		printElements.get(FONT_RIVERNAME).setAnchorPoint(new Point (260, 123));

		printElements.get(FONT_GROUP_DAY).setPrintFont(sFont);
		printElements.get(FONT_GROUP_DAY).setAnchorPoint(new Point (335, 97));

		printElements.get(FONT_GROUP_DATE).setPrintFont(sFont);
		printElements.get(FONT_GROUP_DATE).setAnchorPoint(new Point (485, 97));
		
		printElements.get(FONT_WW_LEVEL).setPrintFont(mFont);
		printElements.get(FONT_WW_LEVEL).setAnchorPoint(new Point (133, 190));
		
		printElements.get(FONT_TOUR).setPrintFont(lFont);
		printElements.get(FONT_TOUR).setAnchorPoint(new Point (144, 132));

		printElements.get(FONT_EDUCATION).setPrintFont(lFont);
		printElements.get(FONT_EDUCATION).setAnchorPoint(new Point (144, 158));

		printElements.get(FONT_FROM).setPrintFont(sFont);
		printElements.get(FONT_FROM).setAnchorPoint(new Point (240, 147));

		printElements.get(FONT_TO).setPrintFont(sFont);
		printElements.get(FONT_TO).setAnchorPoint(new Point (430, 147));

		printElements.get(FONT_TRIP_LENGTH).setPrintFont(sFont);
		printElements.get(FONT_TRIP_LENGTH).setAnchorPoint(new Point (510, 123));

		printElements.get(FONT_DISTANCE).setPrintFont(mFont);
		printElements.get(FONT_DISTANCE).setAnchorPoint(new Point (435, 171));

		printElements.get(FONT_TIME).setPrintFont(mFont);
		printElements.get(FONT_TIME).setAnchorPoint(new Point (360, 196));

		printElements.get(FONT_COMMENT).setPrintFont(sFont);
		printElements.get(FONT_COMMENT).setAnchorPoint(new Point (130, 268));
		
		printElements.get(PARA_XOUT_CAR_1).setAnchorPoint(new Point (50, 385));
		printElements.get(PARA_XOUT_CAR_2).setAnchorPoint(new Point (50, 505));
		printElements.get(PARA_XOUT_CAR_3).setAnchorPoint(new Point (50, 625));
		printElements.get(PARA_XOUT_PARTICIPANTS_1).setAnchorPoint(new Point (320, 415));
		printElements.get(PARA_XOUT_PARTICIPANTS_2).setAnchorPoint(new Point (560, 730));
		printElements.get(FONT_SUMMARY_SHEET).setAnchorPoint(new Point (45, 25));
		
	}
	
	public String getElementName (int i) {
		return PARA_NAMES[i];
	}
	
}
