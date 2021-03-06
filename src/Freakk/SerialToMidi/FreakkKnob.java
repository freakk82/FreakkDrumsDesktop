package Freakk.SerialToMidi;

// Imports for the GUI classes.
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * FreakkKnob.java - 
 *   A knob component evolved from Grant William Braught's FreakkKnob
 * @author Francesco Iesu
 * @version 13/07/2014
 */

class FreakkKnob 
    extends JComponent
    implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int radius = 20;
    private static final int spotRadius = 4;
    private static final int barWidth = 1;
    private static final double minTheta = Math.toRadians(-120);
    private static final double maxTheta = Math.toRadians(120);
    
    private double theta;
    private Color knobColor;
    private Color spotColor;
    private static int previousY;
    private boolean pressedOnSpot;

    /**
     * No-Arg constructor that initializes the position
     * of the knob to 0 radians (Up).
     */
    public FreakkKnob() {
	this(0);
    }

    /**
     * Constructor that initializes the position
     * of the knob to the specified angle in radians.
     *
     * @param initAngle the initial angle of the knob.
     */
    public FreakkKnob(double initTheta) {
	this(initTheta, Color.darkGray, Color.white);
    }
    
    /**
     * Constructor that initializes the position of the
     * knob to the specified position and also allows the
     * colors of the knob and spot to be specified.
     *
     * @param initAngle the initial angle of the knob.
     * @param initColor the color of the knob.
     * @param initSpotColor the color of the spot.
     */
    public FreakkKnob(double initTheta, Color initKnobColor, 
		 Color initSpotColor) {
		theta = initTheta;
		pressedOnSpot = false;
		knobColor = initKnobColor;
		spotColor = initSpotColor;
		this.addMouseListener(this);	
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.setMaximumSize(new Dimension(2*radius,2*radius));
    }

    /**
     * Paint the FreakkKnob on the graphics context given.  The knob
     * is a filled circle with a small filled circle offset 
     * within it to show the current angular position of the 
     * knob.
     *
     * @param g The graphics context on which to paint the knob.
     */
    public void paint(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);
	// Draw the knob.
	g.setColor(knobColor);
	g.fillOval(0,0,2*radius,2*radius);

	// Find the center of the spot.
	Point pt = getSpotCenter();
	int xc = (int)pt.getX();
	int yc = (int)pt.getY();

	// Draw the spot.
	g.setColor(spotColor);
	//g.fillRect(xc-barWidth, yc-barWidth, 2*barWidth, 4*radius/5);
	//g.setColor(knobColor); //set the spot invisible
	g.fillOval(xc-spotRadius, yc-spotRadius, 2*spotRadius, 2*spotRadius);
	
	//System.out.println("val: "+getValue() + " ; deg:" + Math.toDegrees(theta));
    }

    /**
     * Return the ideal size that the knob would like to be.
     *
     * @return the preferred size of the FreakkKnob.
     */
    public Dimension getPreferredSize() {
    	return new Dimension(2*radius,2*radius);
    }

    /**
     * Return the minimum size that the knob would like to be.
     * This is the same size as the preferred size so the
     * knob will be of a fixed size.
     *
     * @return the minimum size of the FreakkKnob.
     */
    public Dimension getMinimumSize() {
    	return new Dimension(2*radius,2*radius);
    }

    /**
     * Get the current anglular position of the knob.
     *
     * @return the current anglular position of the knob.
     */
    public double getAngleRad() {
    	return theta;
    }
    public double getAngleDeg() {
    	return Math.toDegrees(theta);
    }
    
    /**
     * Get the current value of the knob as an int from 0 to 100.
     *
     * @return the current value of the knob as an int from 0 to 100.
     */
    public int getValue(){ 
    	return (int)( (getAngleDeg()+120.0)/2.4 );
    }
    /**
     * Set the current value of the knob as an int from 0 to 100.
     *
     */
    public void setValue(int val){
    	double deg = -120+240*(double)val/100.0f;
    	theta = Math.toRadians(deg);
    	repaint();
    }
    /** 
     * Calculate the x, y coordinates of the center of the spot.
     *
     * @return a Point containing the x,y position of the center
     *         of the spot.
     */ 
    private Point getSpotCenter() {

	// Calculate the center point of the spot RELATIVE to the
	// center of the of the circle.

	int r = radius - spotRadius;

	int xcp = (int)(r * Math.sin(theta));
	int ycp = (int)(r * Math.cos(theta));

	// Adjust the center point of the spot so that it is offset
	// from the center of the circle.  This is necessary becasue
	// 0,0 is not actually the center of the circle, it is  the 
        // upper left corner of the component!
	int xc = radius + xcp;
	int yc = radius - ycp;

	// Create a new Point to return since we can't  
	// return 2 values!
	return new Point(xc,yc);
    }

    /**
     * Determine if the mouse click was on the spot or
     * not.  If it was return true, otherwise return 
     * false.
     *
     * @return true if x,y is on the spot and false if not.
     */
    private boolean isOnSpot(Point pt) {
	return (pt.distance(getSpotCenter()) < spotRadius);
    }

    // Methods from the MouseListener interface.

    /**
     * Empy method because nothing happens on a click.
     *
     * @param e reference to a MouseEvent object describing 
     *          the mouse click.
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Empty method because nothing happens when the mouse
     * enters the Knob.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse entry.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Empty method because nothing happens when the mouse
     * exits the knob.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse exit.
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * When the mouse button is pressed, the dragging of the
     * spot will be enabled if the button was pressed over
     * the spot.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse press.
     */
    public void mousePressed(MouseEvent e) {
	Point mouseLoc = e.getPoint();
	pressedOnSpot = isOnSpot(mouseLoc);
    }

    /**
     * When the button is released, the dragging of the spot
     * is disabled.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse release.
     */
    public void mouseReleased(MouseEvent e) {
	pressedOnSpot = false;
    }
    
    // Methods from the MouseMotionListener interface.

    /**
     * Empty method because nothing happens when the mouse
     * is moved if it is not being dragged.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse move.
     */
    public void mouseMoved(MouseEvent e) {}

    /**
     * Compute the new angle for the spot and repaint the 
     * knob.  The new angle is computed based on the new
     * mouse position.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse drag.
     */
    public void mouseDragged(MouseEvent e) {
		if (pressedOnSpot) {  // Grab The Knob Spot
	
		    int mx = e.getX();
		    int my = e.getY();
	
		    // Compute the x, y position of the mouse RELATIVE
		    // to the center of the knob.
		    int mxp = mx - radius;
		    int myp = radius - my;
	
		    // Compute the new angle of the knob from the
		    // new x and y position of the mouse.  
		    // Math.atan2(...) computes the angle at which
		    // x,y lies from the positive y axis with cw rotations
		    // being positive and ccw being negative.
		    theta = Math.atan2(mxp, myp);
		}
		else{ // Drag Up and Down
	        int y = e.getY();
	        if (y < previousY) {
	            theta+= 0.2;
	        } else if (y > previousY) {
	            theta-= 0.2;
	        }
	        previousY = y;
		}
		if(theta < minTheta) theta = minTheta;
		else if(theta > maxTheta) theta = maxTheta;
		repaint();
    }

    /**
     * Here main is used simply as a test method.  If this file
     * is executed "java FreakkKnob" then this main() method will be
     * run.  However, if another file uses a FreakkKnob as a component
     * and that file is run then this main is ignored.
     */
    public static void main(String[] args) {

	JFrame myFrame = new JFrame("FreakkKnob Test method");
	
	Container thePane = myFrame.getContentPane();
	
	// Add a FreakkKnob to the pane.
	FreakkKnob knob = new FreakkKnob();
	thePane.add(knob);
	thePane.setPreferredSize(knob.getPreferredSize());
	thePane.setMaximumSize(knob.getPreferredSize());
	myFrame.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 System.exit(0);
             }
         });

	myFrame.pack();
	myFrame.show();
    }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
			 int notches = e.getWheelRotation();
			 int val = getValue();
		       if (notches > 0 && val > 0) {
		           setValue(val-1);
		       } else if(notches < 0 && val < 100){
		    	   setValue(val+1);
		       }
	}
}

