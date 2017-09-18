/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import AuxDataStructs.AssaultPartyManager;
import Constants.Constants;
import static Constants.States.*;
import Interfaces.ControlCollectionSiteInterface;

public class ControlCollectionSite implements ControlCollectionSiteInterface {
  /**
   *  Sum of Paintings
   *
   *    @serialField sum
   */
    private int sum;
    
  /**
   *  Assault Party Manager Aux Structure
   *
   *    @serialField manager
   */
    private final AssaultPartyManager[] manager;
  /**
   *  Number of paintings returned from each thief
   *
   *    @serialField numberOfPaintings
   */
    int[] numberOfPaintings;
    
  /**
   *  Signal how many thieves already came from heist
   *
   *    @serialField backFromHeist
   */
    private int backFromHeist;
    
    private Constants constants;
    
    public ControlCollectionSite(Constants constants){
        this.constants = constants;
        this.backFromHeist = 0;
        this.sum = 0;
        this.manager = new AssaultPartyManager[this.constants.ASSAULT_PARTIES_NUMBER];
        this.numberOfPaintings = new int[this.constants.ASSAULT_PARTIES_NUMBER];
    }
    
    
    /**
     * Appraises the Situation 
     * @param over <b>boolean flag</b> if heist is over or not
     * @return TO_PRESENTING_THE_REPORT, TO_ASSEMBLE_A_GROUP or TO_WAIT_FOR_GROUP 
     */
    @Override
    public synchronized int appraiseSit(boolean over) {
        if(over)
            return TO_PRESENTING_THE_REPORT;
        if(!onAction())
            return TO_ASSEMBLE_A_GROUP;
        else if(onAction()){
            return TO_WAIT_FOR_GROUP;
        }
        else return 0;
    }
    /**
     * Starts the operations
     */
    @Override
    public synchronized void startOperations() {
        
        for(int i=0;i<this.constants.ASSAULT_PARTIES_NUMBER;i++){
            manager[i] = new AssaultPartyManager();
        }
    }
    /**
     * Sends the AssaultParty to a new Heist
     */
    @Override
    public synchronized void sendAssaultParty() {
        while (this.groupsFull()!=-1) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        backFromHeist = 0;
        numberOfPaintings = new int[this.constants.ASSAULT_PARTIES_NUMBER];
        for(AssaultPartyManager apm : manager){
            apm.setInAction(true);
            
        }
        notifyAll();
    }
    /**
     * Master thief goes to sleep
     */
    @Override
    public synchronized void takeARest() {
        while(this.backFromHeist !=this.constants.ORDINARY_THIEVES){
            try{
                wait();
            }catch(InterruptedException e){
                
            }
        }
    }
    /**
     * Master Thief collects the canvas of each thief back from Heist
     * 
     * @return boolean data structure signaling which thieves had a canvas
     */
    @Override
    public synchronized boolean[] collectCanvas() {
        boolean signaling[]=new boolean[this.constants.ASSAULT_PARTIES_NUMBER];
        for(int i=0;i<this.constants.ASSAULT_PARTIES_NUMBER;i++){
            this.setSum(this.getSum()+this.numberOfPaintings[i]);
            if(this.numberOfPaintings[i] != this.constants.MAX_PARTY_THIEVES){
                signaling[i] = true;
            }
        }
        for(int i =0 ;i<this.manager.length;i++){
            this.manager[i] = new AssaultPartyManager();
        }
        return signaling;
    }
    /**
     * Sums up all the paintings
     * @return total sum of paintings
     */
    @Override
    public synchronized int sumUpResults() {
        return this.getSum();
    }
    /**
     * Each thief back from heist hands the canvas to Master Thief
     * @param busyHands <b>true</b> if thief has a canvas to deliver, <b>false</b> if not
     * @param thiefID thief id
     */
    @Override
    public synchronized void handACanvas(boolean busyHands, int thiefID) {
        this.setArrived(this.backFromHeist+1);
        if(busyHands){
            int index = this.getPartyIndex(thiefID);
            this.numberOfPaintings[index]++;
        }
        if(this.getThievesBackFromHeist()==6)
           notifyAll();
    }
    /**
     * Prepares an Excursion
     * @param thiefID thief id
     * @return party that thief joined/belongs
     */
    @Override
    public synchronized int prepareExcursion(int thiefID) {
        int toRet = joinParty(thiefID);
        notifyAll();
        while (!this.manager[toRet].isInAction()) {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
        return toRet;
    }
    /**
     * Gets the party identification of the given thief
     * @param thiefID thief identification
     * @return Party identification where given thief belongs
     */
    public int getPartyIndex(int thiefID){
        for(int i = 0 ; i<manager.length;i++){
            if(this.manager[i].amIHere(thiefID))
                return i;
        }
        return -1;
    }
    /**
     * Thief joins a party
     * @param thiefID thief identification
     * @return The index of the party joined if group found (groupsFull)
     */
    public synchronized int joinParty(int thiefID){
        int index = groupsFull();
        if(index == -1)
            return -1;
        manager[index].setThief(thiefID, manager[index].getThievesNumber());
        manager[index].setThievesNumber(manager[index].getThievesNumber()+1);
        return index;
    }
    /**
     * Checks the groups that are full or not
     * @return First party not full, or -1
     */
    public int groupsFull(){
        for(int i = 0; i< this.manager.length ; i++){
            if(this.manager[i].getThievesNumber() != this.constants.MAX_PARTY_THIEVES)
                return i;
        }
        return -1;
    }
    /**
     * AssaultParty on action
     * @return <b>true</b> if party in action, <b>false</b> if not
     */
    public synchronized boolean onAction(){
        for(AssaultPartyManager apm : manager){
           if(apm.isInAction())
               return true;
        }
        return false;
    }
    /**
     * Gets the total sum of paintings
     * @return Total paintings number
     */   
    public int getSum() {
        return sum;
    }
    /**
     * Sets a new total number of paintings
     * @param sum total sum of paintings
     */
    public void setSum(int sum) {
        this.sum = sum;
    }
    /**
     * Gets the Thieves back from Heist
     * @return Number of thieves back from Heist
     */
    public int getThievesBackFromHeist() {
        return backFromHeist;
    }
    /**
     * Set as Arrived
     * @param arrived <b>true</b> if arrived, <b>false</b> if not
     */
    public void setArrived(int arrived) {
        this.backFromHeist = arrived;
    }
}
