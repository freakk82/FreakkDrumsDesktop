package Freakk.SerialToMidi;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.Box;

public class MainWindow extends JFrame { // Your class name

	// Private Fields
	// -----------------------------------
	private static final int numChannels = 8;
	private static final int C1 = 36;
	private static final ChannelStrip channels[] = new ChannelStrip[numChannels];
	private static final String channelNames[] = { "BD", "SN", "HH", "CR1",
			"RD", "TT1", "TT2", "FT2" };
	private static final String ImmutableMap = null;
	private static final Map<String, Integer> noteValues = createMap();
	private static MidiOut MidiOut;
	private MidiDevice.Info[] info;
	private static List<MidiDevice> devices;
	private static List<String> serialPorts;

	// Public Fields
	// -----------------------------------

	// Constructors
	// -----------------------------------
	public MainWindow() throws MidiUnavailableException {
		super("FreakkDrums");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 500);

		// Center Window on Screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		getContentPane().setLayout(new BorderLayout());

		// Create Sub Panels
		// TOP PANEL: Select Serial Port and MIDI Out
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension(800, 100));
		top.setBackground(Color.DARK_GRAY);
		getContentPane().add(top, BorderLayout.PAGE_START);
		top.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// SERIAL PORT

		/**
         * Lists available serial ports.
         * @return
         */
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
		serialPortCombo.setPreferredSize(new Dimension(180, 20));
		
		// MIDI Out Selection (MIDI loopback driver)
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
		JComboBox midiOutCombo = new JComboBox(midiPortNamesList.toArray());
		midiOutCombo.setPreferredSize(new Dimension(180, 20));
		midiOutCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //
                // Get the source of the component, which is our combo
                // box.
                //
                JComboBox comboBox = (JComboBox) event.getSource();

                MidiDevice selected = devices.get(comboBox.getSelectedIndex());
                try {
					if(MidiOut.SetDevice(selected)) System.out.println("MIDI Out Port set to \""+comboBox.getItemAt(comboBox.getSelectedIndex())+"\"");
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        });
		//midiOutCombo.setSelectedItem(midiOutCombo.getItemAt(0));
		JLabel serialPortLabel = new JLabel("SerialPort");
		serialPortLabel.setForeground(Color.WHITE);
		top.add(serialPortLabel);
		top.add(serialPortCombo);

		Component horizontalGlue = Box.createHorizontalGlue();
		top.add(horizontalGlue);

		JLabel MidiOutLabel = new JLabel("MIDI Out Port");
		MidiOutLabel.setForeground(Color.WHITE);
		top.add(MidiOutLabel);
		top.add(midiOutCombo);

		

		JPanel bottom = new JPanel();
		bottom.setSize(800, 400);
		getContentPane().add(bottom, BorderLayout.CENTER);
		bottom.setLayout(new GridLayout(0, 8, 0, 0));

		// Implement Serial To Midi

		// Serial read to do
		MidiOut = new MidiOut();

		// Add Channel Strips
		for (int i = 0; i < numChannels; i++) {
			channels[i] = new ChannelStrip(channelNames[i],
					(int) bottom.getWidth() / numChannels, bottom.getHeight());
			channels[i].playBtn.addActionListener(new playBtnClick(noteValues
					.get(channelNames[i])));
			bottom.add(channels[i]);

		}

		setVisible(true);

	}

	// Private Methods
	// -----------------------------------
	private static Map<String, Integer> createMap() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("BD", C1);
		result.put("SN", C1 + 2);
		result.put("HH", C1 + 6);
		result.put("CR1", C1 + 13);
		result.put("RD", C1 + 15);
		result.put("TT1", C1 + 12);
		result.put("TT2", C1 + 11);
		result.put("FT2", C1 + 5);
		return Collections.unmodifiableMap(result);
	}

	// Public Methods
	// -----------------------------------
	public static void main(String[] args) throws Exception {
		MainWindow w = new MainWindow();
		// serialToMidi.StartSerialToMidi();

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
				MidiOut.SendNoteOn(command);
			} catch (InvalidMidiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//System.out.println("MIDI Out error!");
			}
		}
	}
}