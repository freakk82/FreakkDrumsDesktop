package Freakk.SerialToMidi;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.CharBuffer;

import javax.sound.midi.InvalidMidiDataException;
 
public class SerialToMidi implements Runnable{
    static InputStream input;
    static OutputStream output;
    static String portName;
    static String serialMsg;
    static BufferedReader reader;
    static SerialPort port;
    static boolean running;
    static MidiOut midiout;
    static int MIDI_CHANNEL = 10;
    
    public SerialToMidi(String prt, MidiOut midi) throws Exception
    {
    	portName = prt;
    	midiout = midi;
        System.out.println("Using port: " + portName);
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
        port = (SerialPort)portId.open("FreakkDrums", 4000);
        input = port.getInputStream();
        output = port.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        port.disableReceiveTimeout();
        port.enableReceiveThreshold(1);
        port.setSerialPortParams(115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        port.setInputBufferSize(3);
    } 
    
    public SerialToMidi(String prt, MidiOut midi, int channel) throws Exception{
    	this(prt, midi);
    	MIDI_CHANNEL = channel;
    }
    
    public void Terminate() {
        running = false;
    }
    
    public void run() {
				// TODO Auto-generated method stub
				running = true;
				while(running){
		            try {
							
//							 serialMsg = reader.readLine();
//							 String[] data = serialMsg.split(" ");
//							 int channel = Integer.parseInt(data[2]);
//						     int key = Integer.parseInt(data[4]);
//						     int vel = Integer.parseInt(data[6]);
//						     int[] msg = {channel, key, vel};
//						     String inputLine=reader.readLine();
//						     System.out.println(inputLine);
							
							char buf[] = new char[8];
							reader.read(buf);
//							int msg = (int)buf[0] & 0xF; // first Nibble
//							int channel = (int)buf[0] >> 4; // second Nibble
							int key = (int)buf[1] ;
							int vel = (int)buf[2] ;
						    System.out.println(key+" "+vel);
						    int[] m = {MIDI_CHANNEL,key,vel};
						    try {
								midiout.SendNoteOn(m);
							} catch (InvalidMidiDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
    }
    
    public boolean isRunning(){
    	return running;
    }

}