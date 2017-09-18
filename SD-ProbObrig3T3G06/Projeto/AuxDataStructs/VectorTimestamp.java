/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs;

import java.io.Serializable;
import java.util.Arrays;


public class VectorTimestamp implements Cloneable, Serializable {
    /**
   *  Serial ID
   *
   *    @serialField serialVersionUID
   */
    private static final long serialVersionUID = 1001L;
    
    /**
     * Vector of Timestamps
     */
    private int[] timestamps;
    
    /**
     * Index
     */
    private int index;
    
    /**
     * Integer Argument
     */
    private int arg_integer;
    
    /**
     * Boolean Argument
     */
    private boolean arg_bool;
    
    /**
     * Boolean array
     */
    private boolean arg_bool_array[];

    
    /**
     * VectorTimestamp Instantiation
     * @param size Vector Size
     * @param index index
     */
    public VectorTimestamp(int size, int index) {
        this.timestamps = new int[size];
        this.index = index;
        this.arg_integer = 0;
        this.arg_bool = false;
        //this.arg_bool_array = new boolean[];
    }
    /**
     * Increments on timestamps index
     */
    public synchronized void increment() {
        timestamps[index]++;
    }
    
    /**
     * Updates Timestamp Vector
     * @param vt Timestamp Vector
     */
    public synchronized void update(VectorTimestamp vt) {
        for(int i = 0; i < timestamps.length; i++)
            timestamps[i] = Math.max(timestamps[i], vt.timestamps[i]);
    }
    
    /**
     * Gets timestamps integer array
     * @return timestamps
     */
    public int[] toIntArray() {
        return timestamps;
    }
    
    /**
     * Clones the timestamp vector
     * @return copy of timestamp vector
     */
    @Override
    public synchronized VectorTimestamp clone(){
        VectorTimestamp copy = null;        
        try { 
            copy = (VectorTimestamp) super.clone ();
        } catch (CloneNotSupportedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
        copy.index = index;
        copy.timestamps = timestamps.clone();
        return copy;
    }
    
    /**
     * Gets the timestamp vector of index
     * @return timestamp vector of index
     */
    public int getTimestamp(){
        return this.timestamps[this.index];
    }
    
    /**
     * Sets the timestamp vector of index
     * @param value value to set on timestamp index
     */
    public void setTimestamp(int value){
        this.timestamps[this.index] = value;
    }
    
    /**
     * Gets integer argument
     * @return integer argument
     */
    public int getArg_integer() {
        return arg_integer;
    }

    /**
     * Sets integer argument
     * @param arg_integer new integer value to be set
     */
    public void setArg_integer(int arg_integer) {
        this.arg_integer = arg_integer;
    }

    /**
     * Gets the boolean argument
     * @return Boolean argument
     */
    public boolean isArg_bool() {
        return arg_bool;
    }

    /**
     * Sets the boolean argument 
     * @param arg_bool new value to Boolean Argument
     */
    public void setArg_bool(boolean arg_bool) {
        this.arg_bool = arg_bool;
    }

    /**
     * Gets Boolean Array
     * @return Array of booleans
     */
    public boolean[] getArg_bool_array() {
        return arg_bool_array;
    }

    /**
     * Sets new values to Boolean array
     * @param arg_bool_array new Boolean array to be set
     */
    public void setArg_bool_array(boolean[] arg_bool_array) {
        this.arg_bool_array = arg_bool_array;
    }
    
    
}
