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
 
public class SerialReader2 implements Runnable{
    static InputStream input;
    static OutputStream output;
    static String portName;
    static String serialMsg;
    static BufferedReader reader;
    static SerialPort port;
    static boolean running;
    
    public SerialReader2(String prt) throws Exception
    {
    	portName = prt;
        System.out.println("Using port: " + portName);
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
        port = (SerialPort)portId.open("FreakkDrums", 4000);
        input = port.getInputStream();
        output = port.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        port.disableReceiveTimeout();
        port.enableReceiveThreshold(1);
        port.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
    } 
    
    public void Terminate() {
        running = false;
    }
    
    public void run() {
				// TODO Auto-generated method stub
				running = true;
				while(running){
		            try {
							/*
							 * serialMsg = reader.readLine();
							 * String[] data = serialMsg.split(" ");
							 * int channel = Integer.parseInt(data[2]);
						     * int key = Integer.parseInt(data[4]);
						     * int vel = Integer.parseInt(data[6]);
						     * int[] msg = {channel, key, vel};
						     * String inputLine=reader.readLine();
						     * System.out.println(inputLine);
							 */
							
							char []charBuf = {' ',' ',' '};
							reader.read(charBuf);//read the first byte
						    int channel = (int)(charBuf[0]);
						    int key = (int)(charBuf[1]);
						    int vel = (int)(charBuf[2]);

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