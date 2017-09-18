/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Monitors;

import static Constants.Constants.*;
import Interfaces.ConcentrationSiteInterface;

/**
 * 
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class ConcentrationSite implements ConcentrationSiteInterface{
private int nThieves = 0;
    
    private int thiefState[] = new int[ORDINARY_THIEVES];
    
    public ConcentrationSite(){
        for(int i=0; i<ORDINARY_THIEVES ; i++){
            thiefState[i] = IN_ACTION;
        }
    }
    @Override
    public synchronized void prepareAssaultParty() {
        while (this.nThieves != ORDINARY_THIEVES) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        for(int i =0; i<ORDINARY_THIEVES;i++)
            this.thiefState[i] = NEEDED;
        notifyAll();
    }
    
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
    
@Override
    public synchronized void heistOver() {
        for(int i =0; i<ORDINARY_THIEVES;i++){
            this.thiefState[i] = NOT_NEEDED;
        }
        notifyAll();
    }
}
