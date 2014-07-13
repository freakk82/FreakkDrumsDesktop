package Freakk.SerialToMidi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;


public class ChannelStrip extends JPanel{
	
	// Private Fields
	// ----------------------------------- 
	private static int channelNumber = 0;
	private String channelName;
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;
	public static final int defaultHeight = 400;
	public static final int defaultWidth = 100;
	
	private int gain = 0;
	private int gateThreshold = 0;
	
	// Public Fields
	// -----------------------------------
	public JCheckBox muteCheckBox;
	public JSlider slider;
	
	public JButton playBtn;
	private JTextField gainValue;
	private Component rigidArea;
	
	// Constructors
	// -----------------------------------
	public ChannelStrip(String name, int width, int height){
		
		channelName = name;
		setBackground(Color.DARK_GRAY);
		setSize(width, height);
		// Layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(10, 10)));
		
		// Mute Checkbox
		muteCheckBox = new JCheckBox("");
		muteCheckBox.setBackground(Color.DARK_GRAY);
		muteCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(muteCheckBox);
		add(Box.createRigidArea(new Dimension(10, 10)));
		
		gainValue = new JTextField();

		gainValue.setHorizontalAlignment(SwingConstants.CENTER);
		gainValue.setText("0");
		gainValue.setBorder(null);
		gainValue.setForeground(Color.WHITE);
		gainValue.setBackground(Color.DARK_GRAY);
		add(gainValue);
		gainValue.setColumns(3);
		// Listen for changes in the text
		gainValue.addActionListener(
				new ActionListener() 
				{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					 int val = Integer.parseInt(gainValue.getText());
					 System.out.println(val);
					  if (val<-10 || val >20){
				       JOptionPane.showMessageDialog(null,
				          "Please enter a Gain value between -10 and +20", "Error Massage",
				          JOptionPane.ERROR_MESSAGE);
				     } else {
				    	 gain = val;
				    	 slider.setValue(val);
				     }
				}
				});
		gainValue.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				 int notches = arg0.getWheelRotation();
				 int val = Integer.parseInt(gainValue.getText() ); 
			       if (notches > 0 && val > (-10)) {
			           val--;
			           gain = val;
			           gainValue.setText(""+val);
			    	   slider.setValue(val);
			       } else if(notches < 0 && val < 20){
			    	   val++;
			    	   gain = val;
			    	   gainValue.setText(""+val);
			    	   slider.setValue(val);
			       }
			}
		});

		rigidArea = Box.createRigidArea(new Dimension(10, 10));
		add(rigidArea);
		
		// Volume Slider
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int val = slider.getValue();
				gainValue.setText( ""+val);
				gain = val;
			}
		});
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setValue(0);
		slider.setMaximum(20);
		slider.setMinimum(-10);
		slider.setForeground(Color.WHITE);
		slider.setPaintLabels(true);
		slider.setBorder(null);
		slider.setBackground(Color.DARK_GRAY);
		slider.setOrientation(SwingConstants.VERTICAL);
		//slider.setSize(10, 150);
		add(slider);
		add(Box.createRigidArea(new Dimension(10, 10)));
		slider.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				 int notches = arg0.getWheelRotation();
				 int val = slider.getValue(); 
			       if (notches > 0 && val > (-10)) {
			           val--;
			    	   gainValue.setText(""+val);
			    	   slider.setValue(val);
			    	   gain = val;
			       } else if(notches < 0 && val < 20){
			    	   val++;
			    	   gainValue.setText(""+val);
			    	   slider.setValue(val);
			    	   gain = val;
			       }
			}
		});
		// Play Button
		playBtn = new JButton(channelName);
		playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		playBtn.setPreferredSize(new Dimension(50, 50));
		playBtn.setSize(new Dimension(50, 50));
		add(playBtn);
		add(Box.createRigidArea(new Dimension(10, 10)));

	}
	
	public ChannelStrip(String name){
		this(name, defaultWidth, defaultHeight);
	}

	public ChannelStrip(){
		this("CH "+ (++channelNumber));
	}
	
	// Private Methods
	// -----------------------------------
		
	// Public Methods
	// -----------------------------------
    public void setWidth(int w){
    	this.setSize(180,400);
    }
    
    // GETTERS
    public int getGain(){
    	return gain;
    }
    public int getGateThreshold(){
    	return gateThreshold;
    }
    public boolean isMute(){
    	return muteCheckBox.isSelected();
    }
}
