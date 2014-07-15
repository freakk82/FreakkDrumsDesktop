package Freakk.SerialToMidi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;


public class ChannelStrip extends JPanel{
	
	// Private Fields
	// ----------------------------------- 
	private static int channelNumber = 0;
	private  String channelName;
	final  boolean shouldFill = true;
	final  boolean shouldWeightX = true;
	final  boolean RIGHT_TO_LEFT = false;
	public static int height = 400;
	public static int width = 100; 
	private  FreakkKnob gateKnob;
	private  int gain = 0;
	private  int gateThreshold = 0;
	
	// Public Fields
	// -----------------------------------
	public JCheckBox muteCheckBox;
	public JSlider slider;
	public JButton playBtn;
	private JTextField gainValue;
	private Component rigidArea;
	private JTextField gateThresValue;
	private JLabel lblGate;
	private JLabel lblGain;
	private JLabel lblMute;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private Component rigidArea_1;
	private Component rigidArea_2;
	private JSeparator separator_3;
	private Component rigidArea_3;
	private JComboBox comboBox;
	private Component rigidArea_4;
	private Component rigidArea_5;
	private Component rigidArea_6;
	
	public FreakkLedMeter meter;
	
	// Constructors
	// -----------------------------------
	public ChannelStrip(String name, int w, int h) throws InterruptedException{
		
		channelName = name;
		width = w;
		height = h;
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(width, height) );
		setSize(width, height);
		// Layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(10, 10)));
		lblMute = new JLabel("NOTE");
		lblMute.setToolTipText("Remap Output Note");
		lblMute.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMute.setForeground(Color.GRAY);
		add(lblMute);
		
		comboBox = new JComboBox();
		add(comboBox);
		
		rigidArea_4 = Box.createRigidArea(new Dimension(10, 10));
		add(rigidArea_4);
		
		separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		separator.setBackground(Color.BLACK);
		add(separator);
		add(Box.createRigidArea(new Dimension(10, 10)));
		
		gateKnob = new FreakkKnob();
		gateKnob.setValue(0);
		gateKnob.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int val = (int)( (double)(gateKnob.getValue()/100.0f)*127 );
				gateThresValue.setText(""+val);
				gateThreshold = val;
			}
		});
		
		JPanel gateAndMeterPanel = new JPanel();
		gateAndMeterPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
		gateAndMeterPanel.setBackground(Color.BLACK);
//		gateAndMeterPanel.setLayout(new GridLayout(0, 2, 0, 0));
		gateAndMeterPanel.setLayout(new BorderLayout());
		meter = new FreakkLedMeter(10,gateAndMeterPanel.getHeight());
		meter.setSkin("blue");
		gateAndMeterPanel.add(meter, BorderLayout.WEST);
		
		JPanel gatePanel = new JPanel();
		gatePanel.setBackground(Color.BLACK);
		gatePanel.setLayout(new BoxLayout(gatePanel, BoxLayout.Y_AXIS));
		
		lblGate = new JLabel("GATE");
		lblGate.setForeground(Color.GRAY);
		lblGate.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		gatePanel.add(lblGate);
		gatePanel.add(gateKnob);
	
		gateThresValue = new JTextField();
		gateThresValue.setText("0");
		gateThresValue.setHorizontalAlignment(SwingConstants.CENTER);
		gateThresValue.setForeground(Color.WHITE);
		gateThresValue.setColumns(3);
		gateThresValue.setBorder(null);
		gateThresValue.setBackground(Color.BLACK);
		gateThresValue.addActionListener(
				new ActionListener() 
				{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					 int val = Integer.parseInt(gateThresValue.getText());
					 if (val<0) val = 0;
					 else if(val >127) val = 127;
			    	 gateThreshold = val;
			    	 gateKnob.setValue((int)(100*(double)val/127.0f));
			    	 gateThresValue.setText(""+val);
				}
				});
			gateThresValue.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				 int notches = arg0.getWheelRotation();
				 int val = Integer.parseInt(gateThresValue.getText() ); 
			       if (notches > 0 && val > (0)) {
			           val--;
			           gateThreshold = val;
			           gateThresValue.setText(""+val);
			    	   gateKnob.setValue((int)(100*(double)val/127.0f));
			       } else if(notches < 0 && val < 127){
			    	   val++;
			    	   gateThreshold = val;
			           gateThresValue.setText(""+val);
			    	   gateKnob.setValue((int)(100*(double)val/127.0f));
			       }
			}
		});
			gatePanel.add(gateThresValue);
			gateAndMeterPanel.add(gatePanel, BorderLayout.EAST);
		
		
		add(gateAndMeterPanel);
		
		
		rigidArea_1 = Box.createRigidArea(new Dimension(10, 10));
		add(rigidArea_1);
		
		separator_1 = new JSeparator();
		separator_1.setForeground(Color.GRAY);
		separator_1.setBackground(Color.BLACK);
		add(separator_1);

		rigidArea = Box.createRigidArea(new Dimension(10, 10));
		add(rigidArea);
		
		gainValue = new JTextField();
		// Volume Slider
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int val = slider.getValue();
				gainValue.setText( ""+val);
				gain = val;
			}
		});
		
		lblGain = new JLabel("GAIN");
		lblGain.setForeground(Color.GRAY);
		lblGain.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblGain);
		slider.setValue(0);
		slider.setMaximum(20);
		slider.setMinimum(-10);
		slider.setForeground(Color.LIGHT_GRAY);
		slider.setBorder(null);
		slider.setBackground(Color.BLACK);
		slider.setOrientation(SwingConstants.VERTICAL);
		//slider.setSize(10, 150);
		add(slider);
		
			// Gain controller
			
			gainValue.setHorizontalAlignment(SwingConstants.CENTER);
			gainValue.setText("0");
			gainValue.setBorder(null);
			gainValue.setForeground(Color.WHITE);
			gainValue.setBackground(Color.BLACK);
			add(gainValue);
			gainValue.setColumns(3);
			// Listen for changes in the text
			gainValue.addActionListener(
					new ActionListener() 
					{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						 int val = Integer.parseInt(gainValue.getText());
						  if (val<-10 || val >20){
					       JOptionPane.showMessageDialog(null,
					          "Please enter a Gain value between -10 and +20", "Error Massage",
					          JOptionPane.ERROR_MESSAGE);
					        gainValue.setText(""+gainValue.getText());
					     } else {
					    	 gain = val;
					    	 slider.setValue(val);
					    	 gainValue.setText(""+gainValue.getText());
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
			
			rigidArea_5 = Box.createRigidArea(new Dimension(10, 10));
			add(rigidArea_5);
			
			separator_3 = new JSeparator();
			separator_3.setForeground(Color.DARK_GRAY);
			separator_3.setBackground(Color.BLACK);
			add(separator_3);
			
			rigidArea_3 = Box.createRigidArea(new Dimension(10, 10));
			add(rigidArea_3);
			
			// Mute Checkbox
			muteCheckBox = new JCheckBox("MUTE");
			muteCheckBox.setForeground(Color.GRAY);
			muteCheckBox.setToolTipText("MUTE");
			muteCheckBox.setBackground(Color.BLACK);
			muteCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(muteCheckBox);
			
			rigidArea_2 = Box.createRigidArea(new Dimension(10, 10));
			add(rigidArea_2);
			
			separator_2 = new JSeparator();
			separator_2.setForeground(Color.DARK_GRAY);
			separator_2.setBackground(Color.BLACK);
			add(separator_2);
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
	
	public ChannelStrip(String name) throws InterruptedException{
		this(name, width, height);
	}

	public ChannelStrip() throws InterruptedException{
		this("CH "+ (++channelNumber));
		setBorder(new EmptyBorder(0, 10, 0, 10));
	}
	
	// Private Methods
	// -----------------------------------
		
	// Public Methods
	// -----------------------------------
    public void setWidth(int w){
    	width = w;
    	this.setPreferredSize( new Dimension(w,height) );
    	this.setSize( new Dimension(w,height) );
    }
    public void setHeight(int h){
    	height = h;
    	this.setPreferredSize( new Dimension(width, h) );
    	this.setSize( new Dimension(width, h) );
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
