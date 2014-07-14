package Freakk.SerialToMidi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 * FreakkLedMeter.java - 
 *   A Led Meter GUI
 * @author Francesco Iesu
 * @version 13/07/2014
 */

public class FreakkLedMeter extends JPanel{
	
	private final int numBars = 12;
	private final int numRedBars = 3;
	private final int numYellowBars = 3;
	private final int numGreenBars = numBars - numRedBars - numYellowBars;
	private Dimension size;
	private JPanel [] bars = new JPanel[numBars];
	private int value = 0;
	private int tempValue = 0;
	private boolean topReached = false;
	private Thread reFreshThread;
    /**
     * Create a new FreakkLedMeter passing WIDTH and HEIGHT values
     * @param int values for WIDTH and HEIGHT
     */
	public FreakkLedMeter(int width, int height) throws InterruptedException{
		size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setSize(size);
		this.setBackground(Color.BLACK);
		this.setLayout(new GridLayout(numBars, 0, 10, 2));
		for(int i=0; i<numBars; ++i) bars[i] = new JPanel();
//		for(int i=0; i<numRedBars; ++i) { bars[i] = new JPanel(); bars[i].setBackground(Color.RED); bars[i].hide();}
//		for(int i=0; i<numYellowBars; ++i){ bars[numRedBars+i] = new JPanel();  bars[numRedBars+i].setBackground(Color.YELLOW); bars[i].hide(); }
//		for(int i=0; i<numGreenBars; ++i) { bars[numRedBars+numYellowBars+i] = new JPanel(); bars[numRedBars+numYellowBars+i].setBackground(Color.GREEN); bars[i].hide();}
		setSkin("rgb");
		for(int i=0; i<numBars; ++i) add(bars[i]);
		//Start();
		reFreshThread = new Thread(new Runnable() {           
            public void run() { 
            	while(true){
        			if(value>tempValue && topReached==false){
        				tempValue++;
        				try {
							Thread.sleep(6);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        			else if(value==tempValue){ 
        				topReached = true; 
        				value=0; try {
						Thread.sleep(10);
        				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
        				}
        			}
        			else if(tempValue>0){
        				tempValue--;
        				try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        			Refresh();
        		}
            } 
        });
		reFreshThread.start();
	}

	public void setValueN(int n){
		if(n<0) n=0;
		if(n>numBars) n=numBars;
		value = n;
		topReached=false;
		//Refresh();
	}
	
	public void setValuePercent(int n){
		value = (int)( (double)n/100 * numBars);
		topReached=false;
		//Refresh();
	}
	
	public void Refresh(){
		if(tempValue>=0){
			for(int i=(numBars-1); i>=0; --i) {
				if(i>=(numBars-tempValue)) bars[i].show();
				else bars[i].hide();
			}
		}
	}
	
	public void Start() throws InterruptedException{
		
	}
	
    /**
     * Set the color of the leds passing a string: "rgb"(Default), "blue", "light-blue", "red", "orange", "purple"
     * @param string values
     */
	public void setSkin( String str){
		switch(str){
		case "rgb":
			for(int i=0; i<numRedBars; ++i) { bars[i].setBackground(Color.RED); bars[i].hide();}
			for(int i=0; i<numYellowBars; ++i){ bars[numRedBars+i].setBackground(Color.YELLOW); bars[i].hide(); }
			for(int i=0; i<numGreenBars; ++i) { bars[numRedBars+numYellowBars+i].setBackground(Color.GREEN); bars[i].hide();}
			break;
		case "blue":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(56,86,255));
			break;
		case "light-blue":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(145,200,255));
			break;
		case "red":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(247,10,10));
			break;
		case "orange":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(255,187,69));
			break;
		case "green":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(8,199,11));
			break;
		case "purple":
			for(int i=0; i<numBars; ++i) bars[i].setBackground(new Color(228,25,255));
			break;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {

			JFrame myFrame = new JFrame("FreakkLedMeter Demo");
			
			Container thePane = myFrame.getContentPane();
			thePane.setLayout(new BorderLayout());
			// Add a FreakkLedMeter to the pane.
			FreakkLedMeter meter = new FreakkLedMeter(20,150);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
			meter.setValueN(0);
			thePane.add(meter, BorderLayout.WEST);
//			myFrame.setUndecorated(true) ;
			myFrame.pack();
			myFrame.show();
			String []skins = {"rgb", "blue", "light-blue", "red", "orange", "green", "purple"};
			int pause = 600;
			while(true){
				for(int j=0;j<skins.length; ++j){
					meter.setSkin(skins[j]);
					for(int i=0; i<=12; i+=3){
						meter.setValueN(i);
						Thread.sleep(pause);
					}
				}
			}

    }

}
