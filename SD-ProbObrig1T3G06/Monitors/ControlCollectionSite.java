/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxDataStructs.AssaultPartyManager;
import static Constants.Constants.*;
import Interfaces.ControlCollectionSiteInterface;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class ControlCollectionSite implements ControlCollectionSiteInterface {

    private int sum = 0;
    private final AssaultPartyManager[] manager = new AssaultPartyManager[ASSAULT_PARTIES_NUMBER];
    int[] numberOfPaintings = new int[ASSAULT_PARTIES_NUMBER];
    private int backFromHeist = 0;
    
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
    
    @Override
    public synchronized void startOperations() {
        
        for(int i=0;i<ASSAULT_PARTIES_NUMBER;i++){
            manager[i] = new AssaultPartyManager();
        }
    }
    
    @Override
    public synchronized void sendAssaultParty() {
        while (this.groupsFull()!=-1) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        backFromHeist = 0;
        numberOfPaintings = new int[ASSAULT_PARTIES_NUMBER];
        for(AssaultPartyManager apm : manager){
            apm.setInAction(true);
            
        }
        notifyAll();
    }
    
    @Override
    public synchronized void takeARest() {
        while(this.backFromHeist !=ORDINARY_THIEVES){
            try{
                wait();
            }catch(Exception e){
                
            }
        }
    }
    
    @Override
    public synchronized boolean[] collectCanvas() {
        boolean signaling[]=new boolean[ASSAULT_PARTIES_NUMBER];
        for(int i=0;i<ASSAULT_PARTIES_NUMBER;i++){
            this.setSum(this.getSum()+this.numberOfPaintings[i]);
            if(this.numberOfPaintings[i] != MAX_PARTY_THIEVES){
                signaling[i] = true;
            }
        }
        for(int i =0 ;i<this.manager.length;i++){
            this.manager[i] = new AssaultPartyManager();
        }
        return signaling;
    }
    
    @Override
    public synchronized int sumUpResults() {
        return this.getSum();
    }
    
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
    
    @Override
    public synchronized int prepareExcursion(int thiefID) {
        int toRet = joinParty(thiefID);
        notifyAll();
        while (!this.manager[toRet].isInAction()) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        return toRet;
    }
    
    public int getPartyIndex(int thiefID){
        for(int i = 0 ; i<manager.length;i++){
            if(this.manager[i].amIHere(thiefID))
                return i;
        }
        return -1;
    }
    
    public synchronized int joinParty(int thiefID){
        int index = groupsFull();
        if(index == -1)
            return -1;
        manager[index].setThief(thiefID, manager[index].getThievesNumber());
        manager[index].setThievesNumber(manager[index].getThievesNumber()+1);
        return index;
    }
    
    public int groupsFull(){
        for(int i = 0; i< this.manager.length ; i++){
            if(this.manager[i].getThievesNumber() != MAX_PARTY_THIEVES)
                return i;
        }
        return -1;
    }
    
    public synchronized boolean onAction(){
        for(AssaultPartyManager apm : manager){
           if(apm.isInAction())
               return true;
        }
        return false;
    }
    
    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getThievesBackFromHeist() {
        return backFromHeist;
    }

    public void setArrived(int arrived) {
        this.backFromHeist = arrived;
    }
}
