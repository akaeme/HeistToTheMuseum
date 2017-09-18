/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerSide;


import Constants.Constants;
import static Constants.States.*;
import Interfaces.ConcentrationSiteInterface;

public class ConcentrationSite implements ConcentrationSiteInterface{
  /**
   *  Thieves Number in ConcentrationSite
   *
   *    @serialField nThieves
   */
    private int nThieves = 0;
    
  /**
   *  Thieves states
   *
   *    @serialField thiefState
   */
    private int thiefState[];
    
    private Constants constants;
    /**
     * ConcentrationSite Instantiation
     * @param constants Constants
     */
    public ConcentrationSite(Constants constants){
        this.constants = constants;
        this.thiefState = new int[this.constants.ORDINARY_THIEVES];
        for(int i=0; i<this.constants.ORDINARY_THIEVES ; i++){
            thiefState[i] = IN_ACTION;
        }
    }
    
    /**
     * Prepares AssaultParty
     */
    @Override
    public synchronized void prepareAssaultParty() {
        while (this.nThieves != this.constants.ORDINARY_THIEVES) {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
        for(int i =0; i<this.constants.ORDINARY_THIEVES;i++)
            this.thiefState[i] = NEEDED;
        notifyAll();
    }
    
    /**
     * Thief amINeeded
     * 
     * @param thiefID thief id
     * @return state of the thief
     */
    @Override
    public synchronized int amINeeded(int thiefID) {
        if(this.thiefState[thiefID] == IN_ACTION){
            this.nThieves++;
        }
        notifyAll();
        while (this.thiefState[thiefID]!= NOT_NEEDED && this.thiefState[thiefID]!= NEEDED) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        if(this.thiefState[thiefID] == NEEDED){
            this.nThieves--;
            this.thiefState[thiefID] = IN_ACTION;        
        }
        return this.thiefState[thiefID];
    }
    
    /**
     * Heist Over
     */
    @Override
    public synchronized void heistOver() {
        for(int i =0; i<this.constants.ORDINARY_THIEVES;i++){
            this.thiefState[i] = NOT_NEEDED;
        }
        notifyAll();
    }
}
