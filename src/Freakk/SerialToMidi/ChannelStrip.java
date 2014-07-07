package Freakk.SerialToMidi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JButton;


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
	// Public Fields
	// -----------------------------------
	public JButton playBtn;
	
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
		JCheckBox muteCheckBox = new JCheckBox("");
		muteCheckBox.setBackground(Color.DARK_GRAY);
		muteCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(muteCheckBox);
		add(Box.createRigidArea(new Dimension(10, 10)));
		
		// Volume Slider
		JSlider slider = new JSlider();
		slider.setForeground(Color.WHITE);
		slider.setPaintLabels(true);
		slider.setBorder(null);
		slider.setBackground(Color.DARK_GRAY);
		slider.setOrientation(SwingConstants.VERTICAL);
		add(slider);
		add(Box.createRigidArea(new Dimension(10, 10)));
		
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
}
