/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import AuxDataStructs.RoomManager;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class MasterThief extends Thread {
    private final ControlCollectionSiteInterface controlCollectionSite;
    private final ConcentrationSiteInterface concentrationSite;
    private final MuseumInterface museum;
    private final AssaultPartyInterface[] assaultParties;
    private final LoggerInterface logger;
    
    private RoomManager[] manager =  new RoomManager[ROOMS_NUMBER];
    private int[] assignedRooms = new int[ASSAULT_PARTIES_NUMBER];
    private int state;
    private int numberOfPaintings;
    private boolean heistOver = false;

    public int getNumberOfPaintings() {
        return numberOfPaintings;
    }

    public void setNumberOfPaintings(int numberOfPaintings) {
        this.numberOfPaintings = numberOfPaintings;
    }
    
    public MasterThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, LoggerInterface logger){
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.museum = museum;
        this.assaultParties = assaultParties;
        this.logger = logger;
        
        this.numberOfPaintings = 0;
        this.state = PLANING_THE_HEIST;
        for(int i=0;i<ROOMS_NUMBER;i++){
            this.manager[i] = new RoomManager();
        }
    }
    
    public int getRoomToBeStolen(){
        for(int i=0; i<this.manager.length;i++){
            if(!this.manager[i].isBeingStolen() && !this.manager[i].isEmptyRoom())
            {
                return i;
            }
        }
        return this.manager.length-1;
    }
    
    public boolean checkAllEmpty(){
        for(RoomManager rm : manager){
            if(!rm.isEmptyRoom())
                return false;
        }
        return true;
    }
    
    @Override
    public void run() {
        while (!this.heistOver) {
                switch(this.state){
                    case PLANING_THE_HEIST:
                        this.logger.MasterThiefLog(this.state);
                        this.controlCollectionSite.startOperations(); 
                        this.state = DECIDING_WHAT_TO_DO; 
                        break;
                    case DECIDING_WHAT_TO_DO:
                        this.logger.MasterThiefLog(this.state);
                        int action = this.controlCollectionSite.appraiseSit(this.checkAllEmpty());
                        switch(action){   
                            case TO_ASSEMBLE_A_GROUP: 
                                this.concentrationSite.prepareAssaultParty(); 
                                this.state = ASSEMBLING_A_GROUP;
                                break;
                            case TO_WAIT_FOR_GROUP: 
                                this.controlCollectionSite.takeARest();
                                this.state = WAITNG_FOR_GROUP_ARRIVAL;
                                break;
                            case TO_PRESENTING_THE_REPORT: 
                                this.state = PRESENTING_THE_REPORT;
                                break;
                            default:
                                break;
                        }
                        break;
                    case ASSEMBLING_A_GROUP: 
                        this.logger.MasterThiefLog(this.state);
                        for(int j =0; j<ASSAULT_PARTIES_NUMBER;j++){
                            int roomToSteal= this.getRoomToBeStolen();
                            int distance = museum.getRoomDistance(roomToSteal);
                            this.assaultParties[j].resetAndSet(roomToSteal, distance);
                            this.assignedRooms[j] = roomToSteal;
                            this.manager[roomToSteal].setBeingStolen(true);
                        }
                        this.controlCollectionSite.sendAssaultParty(); 
                        this.state = DECIDING_WHAT_TO_DO;
                        break;
                    case PRESENTING_THE_REPORT:
                        this.numberOfPaintings=this.controlCollectionSite.sumUpResults();
                        this.concentrationSite.heistOver();
                        this.logger.MasterThiefLog(this.state);
                        this.logger.FinalReport(this.numberOfPaintings);
                        this.heistOver = true;
                        System.out.println("OVER");
                        break;
                    case WAITNG_FOR_GROUP_ARRIVAL:
                        this.logger.MasterThiefLog(this.state);
                        boolean[] empty = this.controlCollectionSite.collectCanvas(); 
                        for(int i=0;i<ROOMS_NUMBER;i++){
                            manager[i].setBeingStolen(false);
                        }
                        for(int i=0; i<ASSAULT_PARTIES_NUMBER;i++){
                            if(empty[i]){
                                this.manager[this.assignedRooms[i]].setEmptyRoom(true);
                            }
                        }
                        this.state = DECIDING_WHAT_TO_DO;
                        break;
                }
        }
    }
    
}
