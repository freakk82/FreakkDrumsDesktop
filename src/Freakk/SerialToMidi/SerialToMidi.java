package Freakk.SerialToMidi;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
 
public class SerialToMidi implements Runnable{
    static InputStream input;
    static OutputStream output;
    static String portName;
    static String serialMsg;
    static BufferedReader reader;
    static SerialPort port;
    static CommPortIdentifier portId;
    static boolean running;
    static MidiOut midiout;
    static int MIDI_CHANNEL = 10;
    static final int NOTE_ON_CMD = 13;
    static final int NOTE_OFF_CMD = 12;
    
    public SerialToMidi(String prt, MidiOut midi) throws Exception
    {
    	portName = prt;
    	midiout = midi;
        System.out.println("Using port: " + portName);
        portId = CommPortIdentifier.getPortIdentifier(portName);
        port = (SerialPort)portId.open("FreakkDrums", 4000);
        input = port.getInputStream();
        output = port.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        port.disableReceiveTimeout();
        port.enableReceiveThreshold(3);
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
		            	    byte buf[] = new byte[3];
							int numChars = input.read(buf);
//							System.out.println("signed: "+Integer.toBinaryString((int)buf[0]));
//							System.out.println("unsigned: "+Integer.toBinaryString((int)(buf[0] & 0xff)));
							int msg= buf[0] & 0xFF;
							int cmd = (int)(msg >> 4); // second Nibble
							int chan = (int)(msg & 0xF); // first Nibble
							int key = buf[1] & 0xFF;
							int vel = buf[2] & 0xFF;
//							System.out.println("received "+numChars);
//							System.out.println(Integer.toBinaryString(msg)+" "+Integer.toBinaryString(chan)+" "+key+" "+vel);
//						    System.out.println(Integer.toBinaryString(msg & 0xFF));
//							System.out.println(buf[0]+" "+buf[1]+" "+buf[2]);
							System.out.println((cmd << 4)+" "+chan+" "+key+" "+vel);
							/*
						    int[] m = {MIDI_CHANNEL,key,vel};
							if(cmd == NOTE_ON_CMD) midiout.SendNoteOn(m);
							else if(cmd == NOTE_OFF_CMD) midiout.SendNoteOff(m);
							else System.err.println("error");
							*/
							ShortMessage smsg = new ShortMessage();
							smsg.setMessage(cmd<<4, chan, key, vel);
							midiout.SendShortMessage(smsg);
							
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidMidiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		        }
    }
    
    public boolean isRunning(){
    	return running;
    }


}