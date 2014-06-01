package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import printOut.PrintElementPanel;
import printOut.PrintElementSetupList;


@SuppressWarnings("serial")
public class PrintElementSetupPanel extends JPanel implements ActionListener {
	
	JPanel componentPanel;
	
	public PrintElementSetupPanel () {
		
		super (new BorderLayout());
		
		add (new JLabel ("Formularelemente"), BorderLayout.NORTH);
		
		componentPanel = new JPanel (new GridLayout (0, 1));
		
		for (int i = 0; i < PrintElementSetupList.PARA_COUNT; i++) {
			componentPanel.add(new PrintElementPanel(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup(i), 
					MainWindow.setupData.getPrintElementSetupList().getElementName(i), i));
		}

		JScrollPane sp = new JScrollPane(componentPanel);
		add (sp, BorderLayout.CENTER);
		
		JPanel bp = new JPanel (new BorderLayout ());
		JButton sb = new JButton ("Standard");
		sb.setActionCommand("SetDefault");
		sb.addActionListener(this);
		bp.add(sb, BorderLayout.WEST);
		add (bp, BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("SetDefault")) {
			MainWindow.setupData.getPrintElementSetupList().setDefaultPrintElementConfiguration ((Font) UIManager.getDefaults().get("TextField.font"));
			 for (Component c: componentPanel.getComponents()) {
				 if (c instanceof PrintElementPanel) {
					 ((PrintElementPanel) c).updateSetupData(MainWindow.setupData.getPrintElementSetupList().getPrintElementSetup(((PrintElementPanel) c).getIndex()));
				 }
			 }
			 invalidate ();
			 repaint ();
		}
		
	}
	

}
