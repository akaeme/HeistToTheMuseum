/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

public interface ControlCollectionSiteInterface {
    //MasterThief
        /**
     * Appraises the Situation 
     * @param over <b>boolean flag</b> if heist is over or not
     * @return TO_PRESENTING_THE_REPORT, TO_ASSEMBLE_A_GROUP or TO_WAIT_FOR_GROUP 
     */
    int appraiseSit(boolean over);
    /**
     * Starts the operations
     */
    void startOperations();
    
    /**
     * Sends the AssaultParty to a new Heist
     */
    void sendAssaultParty();
    
    /**
     * Master thief goes to sleep
     */
    void takeARest();
    
    /**
     * Master Thief collects the canvas of each thief back from Heist
     * 
     * @return boolean data structure signaling which thieves had a canvas
     */
    boolean[] collectCanvas();
    
    /**
     * Sums up all the paintings
     * @return total sum of paintings
     */
    int sumUpResults();
    //OrdinaryThief
    
    /**
     * Each thief back from heist hands the canvas to Master Thief
     * @param hasPainting <b>true</b> if thief has a canvas to deliver, <b>false</b> if not
     * @param thiefID thief id
     */
    void handACanvas(boolean hasPainting, int thiefID);
    
    /**
     * Prepares an Excursion
     * @param thiefID thief id
     * @return party that thief joined/belongs
     */
    int prepareExcursion(int thiefID);
}
