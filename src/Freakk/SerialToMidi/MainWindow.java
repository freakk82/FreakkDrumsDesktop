package Freakk.SerialToMidi;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.JFrame;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame { // Your class name

	// Private Fields
	// -----------------------------------
	private static final int numChannels = 8;
	private static final int C1 = 36;
	private static final Dimension frameSize = new Dimension(800,500);
	private static final Dimension topSize = new Dimension(800,80);
	private static final Dimension bottomSize = new Dimension(800,420);
	private static final ChannelStrip channels[] = new ChannelStrip[numChannels];
	private static final List<Integer> channelNoteValues= Arrays.asList(
			C1,
			C1 + 2,
			C1 + 6,
			C1 + 13,
			C1 + 15,
			C1 + 12,
			C1 + 11,
			C1 + 5);
	private static final List<String> channelNames = Arrays.asList(
			"BD", "SN", "HH", "CR1","RD", "TT1", "TT2", "FT2" );
	private static final String ImmutableMap = null;
	private static final Map<String, Integer> noteValues = createMap();
	private static MidiOut midiout;
	private MidiDevice.Info[] info;
	private static List<MidiDevice> devices;
	private static MidiDevice midiDeviceActive;
	private static List<String> serialPorts;
	private static String serialPortActive;
	private Thread  stmThread	;
    private SerialToMidi STM = null;
    
    // Additional Controls

	// Public Fields
	// -----------------------------------

	// Constructors
	// -----------------------------------
	public MainWindow() throws MidiUnavailableException, InterruptedException {
		super("FreakkDrums");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(frameSize);
		
		// ------------------------------------------------ 
		// 	GUI 
		// ------------------------------------------------ 
		
		// Center Window on Screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);

		// Create Sub Panels
		// TOP PANEL: Select Serial Port and MIDI Out
		JPanel top = new JPanel();
		top.setBorder(new EmptyBorder(10, 20, 0, 20));
		top.setForeground(Color.GRAY);
		top.setPreferredSize(topSize);
		top.setMinimumSize(topSize);
		top.setMaximumSize(topSize);
		top.setSize(topSize);
		top.setBackground(Color.BLACK);
		getContentPane().add(top, BorderLayout.PAGE_START);
//		top.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		top.setLayout(new GridLayout(0,3,20,0));
		
		// ------------------------------------------------ 
		// 	SERIAL MIDI IN READER 
		// ------------------------------------------------
		JPanel portPanel = new JPanel();
		portPanel.setBackground(Color.BLACK);
		portPanel.setLayout(
			    new BoxLayout(portPanel, BoxLayout.PAGE_AXIS)
				);
		
        List<String> serialPorts = new ArrayList<String>();
        @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
        while(ports.hasMoreElements()) {
                CommPortIdentifier port = ports.nextElement();
                if(port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                	serialPorts.add(port.getName());
                }
        }
        JComboBox serialPortCombo = new JComboBox(serialPorts.toArray());
        serialPortCombo.setMaximumRowCount(1);
		serialPortCombo.setPreferredSize(new Dimension(180, 10));
		serialPortCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //
                // Get the source of the component, which is our combo
                // box.
                //
                JComboBox comboBox = (JComboBox) event.getSource();
                serialPortActive = comboBox.getSelectedItem().toString();
                try {
                	STM = new SerialToMidi(MainWindow.this,serialPortActive);
			        stmThread = new Thread(STM);
			        stmThread.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

		// ------------------------------------------------ 
		// 	MIDI OUT 
		// ------------------------------------------------
		JPanel midiPanel = new JPanel();
		midiPanel.setBackground(Color.BLACK);
		midiPanel.setLayout(
			    new BoxLayout(midiPanel, BoxLayout.PAGE_AXIS)
				);
		info = MidiSystem.getMidiDeviceInfo();
		devices = new ArrayList<MidiDevice>();
		List<String> midiPortNamesList = new ArrayList<String>();
		
		for (int i = 0; i < info.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(info[i]);
			if ( ! (dev instanceof Sequencer) && ! (dev instanceof Synthesizer) && ! (dev instanceof Transmitter) && (dev.getMaxTransmitters()>=0)) {
				   // we're now sure that device represents a MIDI port
				// debug
				//System.out.println(info[i].getName()+" RX: "+dev.getMaxReceivers()+" TX: "+dev.getMaxTransmitters());
			devices.add(dev);
			midiPortNamesList.add(info[i].getName());
			}
		}
		
		midiout = new MidiOut();
		JComboBox midiOutCombo = new JComboBox(midiPortNamesList.toArray());
		midiOutCombo.setPreferredSize(new Dimension(180, 20));
		midiOutCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //
                // Get the source of the component, which is our combo
                // box.
                //
                JComboBox comboBox = (JComboBox) event.getSource();
                midiDeviceActive = devices.get(comboBox.getSelectedIndex());
				try {
					if(midiout.SetDevice(midiDeviceActive)) System.out.println("MIDI Out Port set to \""+comboBox.getItemAt(comboBox.getSelectedIndex())+"\"");
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StartSerialToMidi();
            }
        });

		JLabel serialPortLabel = new JLabel("Input Port");
		serialPortLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		serialPortLabel.setForeground(Color.LIGHT_GRAY);
		portPanel.add(serialPortLabel);
		portPanel.add(serialPortCombo);
		top.add(portPanel);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 30));
		portPanel.add(rigidArea);

		JLabel MidiOutLabel = new JLabel("MIDI Out Port");
		MidiOutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		MidiOutLabel.setForeground(Color.LIGHT_GRAY);
		midiPanel.add(MidiOutLabel);
		midiPanel.add(midiOutCombo);
		top.add(midiPanel);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 30));
		midiPanel.add(rigidArea_1);
		
		JLabel FreakkLogoLabel = new JLabel("");
		FreakkLogoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		FreakkLogoLabel.setIcon(new ImageIcon(MainWindow.class.getResource("/images/FreakkDrumsLogo.png")));
		FreakkLogoLabel.setBackground(Color.RED);
		top.add(FreakkLogoLabel);
		

		JPanel bottom = new JPanel();
		bottom.setBorder(new EmptyBorder(0, 10, 0, 10));
		bottom.setForeground(Color.GRAY);
		bottom.setBackground(Color.BLACK);
		bottom.setPreferredSize(bottomSize);
		bottom.setSize(bottomSize);
		bottom.setMaximumSize(bottomSize);
		bottom.setMinimumSize(bottomSize);
		getContentPane().add(bottom, BorderLayout.CENTER);
		bottom.setLayout(new GridLayout(0, 8, 10, 0));

		// Add Channel Strips
		for (int i = 0; i < numChannels; i++) {
			channels[i] = new ChannelStrip(channelNames.get(i), channelNoteValues.get(i),
					(int) (bottom.getWidth()) / numChannels, bottom.getHeight());
//			channels[i].playBtn.addActionListener(new playBtnClick(noteValues
//					.get(channelNames.get(i))));

			channels[i].playBtn.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					int y = arg0.getY();
					int height = (int)((JButton) arg0.getSource()).getSize().getHeight();
					int vel = (int)(-127 * ((double)(y-height)/height));
					//System.out.println("vel: "+vel+" - h: "+ height+" - y: "+y);
					ShortMessage smgs;
					try {
						smgs = new ShortMessage(ShortMessage.NOTE_ON, noteValues.get(((JButton) arg0.getSource()).getLabel()), vel);
						SendMidiShortMessage(smgs);
					} catch (InvalidMidiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			bottom.add(channels[i]);
		}
		
		setVisible(true);

	}

	// Private Methods
	// -----------------------------------
	private static Map<String,Integer> createMap() {
		Map<String,Integer> result = new HashMap<String,Integer>();
		result.put("BD",C1);
		result.put("SN",C1 + 2);
		result.put("HH",C1 + 6);
		result.put("CR1",C1 + 13);
		result.put("RD",C1 + 15);
		result.put("TT1",C1 + 12);
		result.put("TT2",C1 + 11);
		result.put("FT2",C1 + 5);
		return Collections.unmodifiableMap(result);
	}

	// Public Methods
	// -----------------------------------
	public static void main(String[] args) throws Exception {
		
		MainWindow w = new MainWindow();
	}

	private class playBtnClick implements ActionListener {

		int key;
		int chan;
		int vel;

		public playBtnClick(int n) {
			key = n;
			chan = 10;
			vel = 127;
		}

		public void actionPerformed(ActionEvent e) {
			int[] command = { chan, key, vel };
			try {
				midiout.SendNoteOn(command);
			} catch (InvalidMidiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//System.out.println("MIDI Out error!");
			}
		}
	}
	
	public void StartSerialToMidi(){
		
		try {
			//STM.Start(serialPortActive);
			//STM = new SerialReader2(portName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SendMidiShortMessage(ShortMessage smsg) throws InvalidMidiDataException{
		int key = smsg.getData1();
		int vel = smsg.getData2();
		int channelIndex = channelNoteValues.indexOf(key);
		key = channels[channelIndex].getNote();
		//Apply Gain boost/cut
		float gain = 1+(float)channels[channelIndex].getGain()/10.0f; // apply velocity multiplier from 0 to 3
		vel = (int)(vel*gain);
		if(vel>127) vel = 127;
		if(!channels[channelIndex].isMute() && vel > channels[channelIndex].getGateThreshold()){ 
			midiout.SendShortMessage(new ShortMessage(smsg.getCommand(), key, vel));
//			System.out.println(smsg.getCommand()+" "+key+" "+vel); // debug
			channels[channelIndex].meter.setValuePercent((int)(100*(double) vel/127) );
		}
	}
	
}