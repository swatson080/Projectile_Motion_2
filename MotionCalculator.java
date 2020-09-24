/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectilemotion;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

/**
 *
 * @author sbeli
 */
public class MotionCalculator {
    /**
     * the number of planets
     */
    private final int NUM_PLANETS = 8;
    /**
     * array of gravity constants in metric
     */
    private final double[] GRAVITY_METRIC = { -3.70, -8.87, -9.80665, -3.71, -24.79, -10.44, -8.69, -11.15 };
    /**
     * array of gravity constants in imperial
     */
    private final double[] GRAVITY_IMPERIAL = { -12.1391, -29.10, -32.174, -12.1719, -81.332, -34.252, -28.5105, -36.5814 };
    /**
     * object's initial velocity
     */
    private double initialVelocity;
    /**
     * object's launch angle
     */
    private double launchAngle;
    /**
     * gravitational constant used in the calculations
     */
    private double gravity;
    /**
     * indicates the current planet. Acceptable values are 0-7
     */
    private int currentPlanet;
    /**
     * indicates whether metric or imperial units are being used
     */
    private boolean metric;
    /**
     * indicates whether radians or degrees are being used
     */
    private boolean radians;
    
    /**
     * No-arg constructor.
     * Sets gravity to earth's gravity in metric. Sets metric and radians to
     * true. Sets all other values to 0.
     */
    public MotionCalculator() {
        currentPlanet = 2;
        initialVelocity = 0;
        launchAngle = 0;
        gravity = GRAVITY_METRIC[currentPlanet];
        
        metric = true;
        radians = true;
    }
    
    /**
     * sets the initial velocity
     * @param v0 the initial velocity entered by user
     */
    public void setInitialVelocity(double v0) {
        initialVelocity = v0;
    }
    
    /**
     * sets the launch angle
     * @param angle the launch angle entered by user
     */
    public void setLaunchAngle(double angle) {
        launchAngle = angle;
        // If user passed degrees, convert it to radians
        if(!radians) {
            convertToRads();
        }
    }
    
    /**
     * sets the current planet
     * @param planet indicates planet. acceptable values are 0-7.
     */
    public void setCurrentPlanet(int planet) {
        currentPlanet = planet;
        setGravity();
    }
    
    /**
     * sets the value of gravity based on the currenty planet 
     * and whether metric or imperial units are selected.
     */
    private void setGravity() {
        if(metric) {
            gravity = GRAVITY_METRIC[currentPlanet];
        }
        else {
            gravity = GRAVITY_IMPERIAL[currentPlanet];
        }
    }
    
    /**
     * sets units to metric or imperial.
     * @param metric a boolean value. if true, units are set to metric.
     * if false, units are set to imperial.
     */
    public void setMetric(boolean metric) {
        this.metric = metric;
    }
    
    /**
     * sets angle measures to radians or degrees.
     * @param radians a boolean value. if true, angles are set to radians.
     * if false, angles are set to imperial
     */
    public void setRadians(boolean radians) {
        this.radians = radians;
    }
    
    /**
     * gets the current planet
     * @return an int representing what the current planet is
     */
    public int getCurrentPlanet() {
        return currentPlanet;
    }
    
    /**
     * calculates the x component of velocity
     * @return x component of velocity as a double
     */
    private double getXvelocity() {
        return initialVelocity * Math.cos(launchAngle);
    }
    
    /**
     * calculates the y component of velocity
     * @return y component of velocity as a double
     */
    private double getYvelocity() {
        return initialVelocity * Math.sin(launchAngle);
    }
    
    /**
     * converts an angle in degrees to an angle in radians
     * @return angle in radians
     */
    private void convertToRads() {
        launchAngle *= (Math.PI / 180.0);
    }
    
    /**
     * calculates the flight time
     */
    public double calculateTime() {
        return -(getYvelocity() * 2.0) / gravity;
    }
    
    /**
     * calculates the maximum height of the projectile
     * @return the maximum height of the projectile
     */
    public double calculateMaxHeight() {
        double time = calculateTime() / 2.0;
        return getYvelocity() * (time) + ((gravity / 2.0) * Math.pow(time,2.0)); 
    }
    
    /**
     * calculates object's horizontal displacement
     */
    public double calculateDisplacement() {
        return getXvelocity() * calculateTime();
    }
    
}
