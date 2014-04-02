package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class About extends JDialog implements ActionListener {

	
	public About (JFrame parent) {
		
		super(parent,"\u00DCber das Programm");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(new BorderLayout());
		JPanel p = new JPanel (new FlowLayout ());
		
		p = new JPanel (new FlowLayout ());
		p.add (new JLabel ("Camp Planer V0.1"));
		add (p, BorderLayout.NORTH);

		p = new JPanel (new FlowLayout ());
		p.add (new JLabel ("(c) 2014 Arno Stock"));
		add (p, BorderLayout.CENTER);
		
		JButton btnOk = new JButton ("Ok");
		btnOk.addActionListener(this);
		p = new JPanel (new FlowLayout ());
		p.add (btnOk);
		add (p, BorderLayout.SOUTH);
		
		setMinimumSize(new Dimension (230, 200));
		setLocationByPlatform(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

        setVisible(false);
		
	}
	
	
}
