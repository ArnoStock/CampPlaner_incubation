package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class FilesSetupPanel extends JPanel implements ActionListener {
	
	JFileChooser fc = new JFileChooser();
	JLabel tripFormFileNameLabel = new JLabel();
	JButton choosetripFormFileNameButton = new JButton ("Ändern");
	SetupData setupData;
	
	
	
	public FilesSetupPanel (SetupData setupData) {
		
		super (new BorderLayout());
		
		this.setupData= setupData;
		
		add (new JLabel ("Dateinamen und Pfade"), BorderLayout.NORTH);
		
		JPanel cp = new JPanel (new GridLayout (0, 1));

		JPanel fnp = new JPanel (new FlowLayout (FlowLayout.LEADING));
		fnp.add (tripFormFileNameLabel);
		tripFormFileNameLabel.setText(setupData.getFileName());
		fnp.add (choosetripFormFileNameButton);
		choosetripFormFileNameButton.setActionCommand("editTripFormFileName");
		choosetripFormFileNameButton.addActionListener (this);
		
		cp.add(fnp);
		JScrollPane sp = new JScrollPane(cp);
		add (sp, BorderLayout.CENTER);
		
	}



	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if (evt.getActionCommand().equals("editTripFormFileName")) {
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
			    fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            fc.getSelectedFile().getName());
			       setupData.setFileName (fc.getSelectedFile().getAbsolutePath());
			    }
			
		}

	}
	

}
