package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class About extends JDialog implements ActionListener {

	
	public About (JFrame parent) {
		
		super(parent,"\u00DCber das Programm");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(new BorderLayout());
		JPanel p = new JPanel (new FlowLayout ());
		
		p = new JPanel (new FlowLayout ());
		p.add (new JLabel ("Camp Planer " + MainWindow.versionString));
		add (p, BorderLayout.NORTH);

		String s = "Fehler!";
		try {
			char[] c = new char [2048];
			if (ClassLoader.getSystemResource("text/About.txt") != null) {
				InputStream in = ClassLoader.getSystemResource("text/About.txt").openStream();
				if (in != null) {
					InputStreamReader r = new InputStreamReader (in);
					r.read(c);
					s = new String (c);
				}
			}
			else {
				FileReader r = new FileReader ("text/About.txt");
				r.read(c);
				r.close();
				s = new String (c);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JTextArea t = new JTextArea (s);
		t.setEditable(false);
		add (t, BorderLayout.CENTER);
		
		JButton btnOk = new JButton ("Ok");
		btnOk.addActionListener(this);
		p = new JPanel (new FlowLayout ());
		p.add (btnOk);
		add (p, BorderLayout.SOUTH);
		
		int width = t.getPreferredSize().width + 10;
		if (width > 400)
			width = 400;
		int height = t.getPreferredSize().height + 100;
		setMinimumSize(new Dimension (width, height));
		setLocationByPlatform(true);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

        setVisible(false);
		
	}
	
	
}
