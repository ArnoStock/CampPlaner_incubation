package printOut;

import gui.SetupData;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TripsPreviewFrame extends JDialog implements ActionListener, ItemListener {

	private class Resolution extends JComponent {
		
		private Integer resolution;
		
		public Resolution (Integer resolution) {
			this.resolution = resolution;
		}
		
		public double getResolution () {
			double r = resolution;
			return r/100;
		}
		
		@Override
		public String toString () {
			return resolution + "%";
		}
		
	}
	
	private final int[] RES = { 25, 50, 75, 100, 125, 150, 175, 200, 250, 300, 350, 400, 450, 500 };
	
	private JButton print = new JButton("Alles", new ImageIcon ("toolbarButtonGraphics/general/Print16.gif"));
	private JButton printThisPage = new JButton("Seite", new ImageIcon ("toolbarButtonGraphics/general/Print16.gif"));
	private JButton cancel = new JButton("Zurück");
	private JCheckBox printOntoFormSheet = new JCheckBox ("Formular");
	private Pageable pg = null;
	private double scale = 1.0;
	private Page page[] = null;
	private JComboBox<Integer> jcb = new JComboBox<Integer>();
	private JComboBox<Resolution> rcb = new JComboBox<Resolution>();
	private CardLayout cl = new CardLayout();
	private JPanel p = new JPanel(cl);
	private JButton back = new JButton(new ImageIcon ("toolbarButtonGraphics/navigation/Back16.gif")),
				forward = new JButton(new ImageIcon ("toolbarButtonGraphics/navigation/Forward16.gif"));
	private SetupData setupData;
	
/*
	public PreviewFrame(Frame parentWindow, Pageable pg, SetupData setupData) {
		super(parentWindow, "Print Preview");
		setModal(true);

		this.setupData = setupData;
		this.pg = pg;
		createPreview();
	}
*/
	public TripsPreviewFrame(Frame parentWindow, final Printable pr, final PageFormat p, SetupData setupData) {
		super(parentWindow, "Print Preview");
		setModal(true);

		this.pg = new Pageable() {
			@Override
			public int getNumberOfPages() {
				Graphics g = new java.awt.image.BufferedImage(2,2,java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
				int n=0;
				try { while(pr.print(g, p, n) == Printable.PAGE_EXISTS) n++; }
				catch(Exception ex) {ex.printStackTrace();}
				g.dispose();
				return n;
			}
			@Override public PageFormat getPageFormat(int x) { return p; }
			@Override public Printable getPrintable(int x) { return pr; }
		};
		createPreview();
	}

	private void createPreview() {
		page = new Page[pg.getNumberOfPages()];
//		FlowLayout fl = new FlowLayout();
		PageFormat pf = pg.getPageFormat(0);
		Dimension size = new Dimension((int)pf.getPaper().getWidth(), (int)pf.getPaper().getHeight());
		if(pf.getOrientation() != PageFormat.PORTRAIT)
		size = new Dimension(size.height, size.width);
//		JPanel temp = null;
		for(int i=0; i<page.length; i++) {
			jcb.addItem(i+1);
			page[i] = new Page(i, size);
			p.add(""+(i+1), new JScrollPane(page[i]));
		}
		for (int r: RES) {
			rcb.addItem(new Resolution (r));
		}
		rcb.setSelectedIndex(3);
		setTopPanel();
		this.getContentPane().add(p, "Center");
//		Dimension d = this.getToolkit().getScreenSize();
		this.setSize(size.width+20,size.height+40);
//		this.setSize(d.width,d.height-60);
//		slider.setSize(this.getWidth()/2, slider.getPreferredSize().height);
		this.setVisible(true);
		page[jcb.getSelectedIndex()].refreshScale();
	}
	
	private void setTopPanel() {
		FlowLayout fl = new FlowLayout();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(); 
		JPanel topPanel = new JPanel(gbl), temp = new JPanel(fl); //slider.setBorder(new TitledBorder("Percentage Zoom"));
		rcb.addItemListener(this);
		back.addActionListener(this);
		forward.addActionListener(this);
		back.setEnabled(false);
		forward.setEnabled(page.length > 1);
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		topPanel.add(rcb); 
		temp.add(back);
		temp.add(jcb);
		temp.add(forward);
		temp.add(cancel);
		temp.add (printOntoFormSheet);
		temp.add(print);
		temp.add(printThisPage);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbl.setConstraints(temp, gbc);
		topPanel.add(temp);
		print.addActionListener(this);
		printThisPage.addActionListener(this);
		cancel.addActionListener(this);
		jcb.addItemListener(this);
		print.setMnemonic('A');
		cancel.setMnemonic('Z');
		printThisPage.setMnemonic('S');
		printOntoFormSheet.setToolTipText("Soll ein Formblatt bedruckt werden?");
		this.getContentPane().add(topPanel, "North");
	}
	
	public void itemStateChanged(ItemEvent ie) {

		if (ie.getSource().equals(jcb)) {
			cl.show(p, jcb.getSelectedItem().toString());
			page[jcb.getSelectedIndex()].refreshScale();
			back.setEnabled(jcb.getSelectedIndex() == 0 ? false: true);
			forward.setEnabled(jcb.getSelectedIndex() == jcb.getItemCount()-1 ? false:true);
			this.validate();
		}
		
		if (ie.getSource().equals(rcb)) {
			scale = ((Resolution) rcb.getSelectedItem()).getResolution();
			page[jcb.getSelectedIndex()].refreshScale();
			this.validate();
		}

	} 
	
	public void actionPerformed(ActionEvent ae) {
		Object o = ae.getSource();
		if(o == print) {
			try {
				PrinterJob pj = PrinterJob.getPrinterJob();
				PageFormat pf = pg.getPageFormat(0); 
				Paper p = pf.getPaper();
				p.setImageableArea(0, 0, p.getWidth(), p.getHeight());
				pf.setPaper(p);
				pj.defaultPage(pf);
				pj.setPageable(pg);
				if(pj.printDialog())
				pj.print();
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null,ex.toString(), "Error in Printing",1);
			}
		}
		else if(o == printThisPage)
			printCurrentPage();
		else if(o == back) {
			jcb.setSelectedIndex(jcb.getSelectedIndex() == 0 ? 0:jcb.getSelectedIndex()-1);
			if(jcb.getSelectedIndex() == 0)
				back.setEnabled(false);
		}
		else if(o == forward) {
			jcb.setSelectedIndex(jcb.getSelectedIndex() == jcb.getItemCount()-1 ? 0:jcb.getSelectedIndex()+1);
			if(jcb.getSelectedIndex() == jcb.getItemCount()-1)
				forward.setEnabled(false);
			}
			else if(o == cancel) this.dispose();
	} 
	
	public void printCurrentPage() {
		try {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.defaultPage(pg.getPageFormat(0));
		pj.setPrintable(new PsuedoPrintable());
		javax.print.attribute.HashPrintRequestAttributeSet pra = 
				new javax.print.attribute.HashPrintRequestAttributeSet();
		if(pj.printDialog(pra))
		pj.print(pra);
		}
		catch(Exception ex) {
		JOptionPane.showMessageDialog(null,ex.toString(), "Error in Printing", 1);
		}
	} 

	class Page extends JLabel {
		final int n;
		PageFormat pf;
		java.awt.image.BufferedImage bi = null;
		Dimension size = null;
		
		public Page(int x, Dimension size) {
			this.size = size;
			bi = new java.awt.image.BufferedImage(size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_RGB);
			n = x;
			pf = pg.getPageFormat(n);
			Graphics g = bi.getGraphics();
			Color c = g.getColor();
			g.setColor(Color.white);
			g.fillRect(0, 0, (int)pf.getWidth(), (int)pf.getHeight());
			g.setColor(c);
			try {
				g.clipRect(0, 0, (int)pf.getWidth(), (int)pf.getHeight());
				pg.getPrintable(n).print(g, pf, n);
			}
				catch(Exception ex) { }
				this.setIcon(new ImageIcon(bi));
		} 

		public void refreshScale() { 
			if(scale != 1.0)
				this.setIcon(new ImageIcon(bi.getScaledInstance((int)(size.width*scale), (int)(size.height*scale), Image.SCALE_SMOOTH)));
			else 
				this.setIcon(new ImageIcon(bi));
			this.validate();
		}
	}
	
	class PsuedoPrintable implements Printable {
		public int print(Graphics g, PageFormat fmt, int index) {
		if(index > 0) return Printable.NO_SUCH_PAGE;
		int n = jcb.getSelectedIndex();
		try { return pg.getPrintable(n).print(g, fmt, n); } 
		catch(Exception ex) {}
		return Printable.PAGE_EXISTS;
		}
	}
}
