package printOut;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class PrintElementSetup {

	private Font printFont;
	
	@Attribute
	private String fontFamily;
	@Attribute
	private Integer fontStyle;
	@Attribute
	private Integer fontSize;
	@Element
	private Point anchorPoint;

	private Color printColor;
	@Attribute Integer printColorCode;
	
	public PrintElementSetup (Font defaultFont, Point anchorPoint, Color printColor) {
		printFont = defaultFont;
		fontFamily = printFont.getFamily();
		fontStyle = printFont.getStyle();
		fontSize = printFont.getSize();
		this.anchorPoint = anchorPoint;
		this.printColor = printColor;
		printColorCode = printColor.getRGB();
	}

	public PrintElementSetup () {
		printFont = null;
		printColor = null;
		anchorPoint = null;
	}

	/**
	 * @return the printFont
	 */
	public Font getPrintFont() {
		
		if (printFont == null) {
			printFont = new Font (fontFamily, fontStyle, fontSize);
		}
		return printFont;
	}

	/**
	 * @param printFont the printFont to set
	 */
	public void setPrintFont(Font printFont) {
		this.printFont = printFont;
		fontFamily = printFont.getFamily();
		fontStyle = printFont.getStyle();
		fontSize = printFont.getSize();
	}

	/**
	 * @return the anchorPoint
	 */
	public Point getAnchorPoint() {
		return anchorPoint;
	}

	/**
	 * @param anchorPoint the anchorPoint to set
	 */
	public void setAnchorPoint(Point anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	/**
	 * @return the printColor
	 */
	public Color getPrintColor() {
		
		if (printColor == null)
			printColor = new Color (printColorCode);
	
		return printColor;
	}

	/**
	 * @param printColor the printColor to set
	 */
	public void setPrintColor(Color printColor) {
		this.printColor = printColor;
		printColorCode = printColor.getRGB();
	}
	
	
}
