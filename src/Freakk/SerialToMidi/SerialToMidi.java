package Freakk.SerialToMidi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JOptionPane;

import Freakk.SerialToMidi.MainWindow;
import gnu.io.*;

public class SerialToMidi {

        private static Logger logger = Logger.getLogger(SerialToMidi.class.getName());
        int midioutN;
        CommPortIdentifier portId;
        CommPort port;
        String portName;
        private static MidiOut midiout;
        boolean keepreading;
        boolean started;

        /**
         * Standard constructor.
         * @param port the name of the serial port, e.g. COM2, TTY1
         * @param midiout, the midi device where to send messages
         * @throws Exception
         */
        public SerialToMidi() throws Exception {
        	started = false;
        	/*
                //Extract native libs
                if(NativeUtils.detectOS() == OS.Windows){
                        if(NativeUtils.detectArchtiecture() == 32){
                                NativeUtils.extractLibraryFromJar("/natives/win32/rxtxSerial.dll");
                        }
                        else{
                                NativeUtils.extractLibraryFromJar("/natives/win64/rxtxSerial.dll");
                        }
                }
               */ 
        }


        /**
         * Starts gathering data from the serial port and sending it to the midi out
         * @throws Exception
         */
        public void start(String portN, MidiOut out) throws Exception {

        		portId =CommPortIdentifier.getPortIdentifier(portN);
                if(portId == null) {
                        throw new Exception("Cannot open serial port "+portN);
                }

                this.port = (SerialPort) portId.open(
                                "FreakkDrums", // Name of the application asking for the port 
                                10000   // Wait max. 10 sec. to acquire port
                );

                InputStream is= this.port.getInputStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                keepreading = true;
                started = true;
                new Thread(new Runnable() {

                        @Override
                        public void run() {
                                while(keepreading)
                                {
                                        String line = null;
                                        
                                        try {
                                                line = reader.readLine();
                                                logger.info("reading");
                                                String[] data = line.split(" ");
                                                //cc ch 10 ctrl 12 val 130
                                                if(data[0].equalsIgnoreCase("cc"))
                                                {
                                                        /*
                                                        int channel = Integer.parseInt(data[2]);
                                                        int ctrl = Integer.parseInt(data[4]);
                                                        int val = Integer.parseInt(data[6]);
                                                        int [] msg = {channel, ctrl, val};
                                                        midiout.SendCC(msg);
                                                        */
                                                }
                                                else if(data[0].equalsIgnoreCase("non"))
                                                {
                                                        int channel = Integer.parseInt(data[2]);
                                                        int key = Integer.parseInt(data[4]);
                                                        int vel = Integer.parseInt(data[6]);
                                                        int[] msg = {channel, key, vel};
                                                        midiout.SendNoteOn(msg);
                                                        logger.info("note "+key+" , vel "+vel);
                                                }
                                        } catch (IOException e) {

                                        }catch(InvalidMidiDataException ie)
                                        {
                                                System.out.println("error");
                                        }
                                }
                        }
                }).start();
                System.out.println("started");
        }

        /**
         * Stops acquiring data from the serial port and closes port and midi
         */
        public void stop()
        {
                if(started){
                	keepreading = false;
                	port.close();
                	started = false;
                }
        }

        /**
         * Starts an interactive and graphical way of using this utility
         * @param args ignored
         */
        /*
        public static void main(String[] args) {
        */
        public void StartSerialToMidi(){
                logger.info("Starting SerialToMidi");
                try{
                		//MainWindow w = new MainWindow();
                        FileHandler fh = new FileHandler("serialToMidi.log");
                        fh.setFormatter(new SimpleFormatter());
                        logger.addHandler(fh);
                        logger.setLevel(Level.ALL);

                        logger.info("Chosen serial port "+portName);
                        //logger.info("Chosen midi port "+midiout);
                        
                        //SerialToMidiDialog dialog = new SerialToMidiDialog(this, chosenPort, midiout);
                        this.start(portName, midiout);
                }
                catch (Exception e) {
                        logger.severe("Error: "+e.getMessage());
                        System.exit(-1);
                }
        }
        
        
        // SETTERS
        // -----------------------------------------------------
        public void setSerialPort(String str) {
        	portName = str;
        }
        
        public void setMidiPort(MidiOut m){
        	midiout = m;
        }

}
