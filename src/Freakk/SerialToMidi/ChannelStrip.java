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
import java.util.HashMap;
import java.util.Map;

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
	private  FreakkKnob gateKnob;
	private  int gain = 0;
	private  int gateThreshold = 0;
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
	
	// Public Fields
	// -----------------------------------
	public JCheckBox muteCheckBox;
	public JSlider slider;
	public JButton playBtn;
	public static int height = 400;
	public static int width = 100; 
	
	public FreakkLedMeter meter;
	
	private final String [] notes = {
				"C-1","C#-1","D-1","D#-1","E-1","F-1","F#-1","G-1","G#-1","A-1","A#-1","B-1",
				"C0","C#0","D0","D#0","E0","F0","F#0","G0","G#0","A0","A#0","B0",
				"C1","C#1","D1","D#1","E1","F1","F#1","G1","G#1","A1","A#1","B1",
				"C2","C#2","D2","D#2","E2","F2","F#2","G2","G#2","A2","A#2","B2",
				"C3","C#3","D3","D#3","E3","F3","F#3","G3","G#3","A3","A#3","B3",
				"C4","C#4","D4","D#4","E4","F4","F#4","G4","G#4","A4","A#4","B4"
				};
	private final static int lowNote = 12; // C-1
	private Map<String, Integer> noteValues;
	
	// Constructors
	// -----------------------------------
	public ChannelStrip(String name, int note, int w, int h) throws InterruptedException{
		
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
		
		// MIDI Note remap
		initNoteValues();
		comboBox = new JComboBox(notes);
		comboBox.setSelectedIndex(note - lowNote);
		
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
		this(name, lowNote, width, height);
	}

	public ChannelStrip() throws InterruptedException{
		this("CH "+ (++channelNumber));
		setBorder(new EmptyBorder(0, 10, 0, 10));
	}
	
	// Private Methods
	// -----------------------------------
	
	public void initNoteValues(){
		noteValues = new HashMap<String, Integer>(); 
		int note = lowNote; // C-1

		for(int i=-1 ; i<=4 ; ++i){
		 noteValues.put("C"+i,note++);
		 noteValues.put("C#"+i,note++);
		 noteValues.put("D"+i,note++);
		 noteValues.put("D#"+i,note++);
		 noteValues.put("E"+i,note++);
		 noteValues.put("F"+i,note++);
		 noteValues.put("F#"+i,note++);
		 noteValues.put("G"+i,note++);
		 noteValues.put("G#"+i,note++);
		 noteValues.put("A"+i,note++);
		 noteValues.put("A#"+i,note++);
		 noteValues.put("B"+i,note++);
		}
	}
	
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
    
    public int getNote(){
    	return noteValues.get(comboBox.getSelectedItem());
    }
    
}
