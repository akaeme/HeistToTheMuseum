/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import AuxDataStructs.RoomManager;
import Constants.Constants;
import static Constants.States.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;


public class MasterThief extends Thread {
  /**
   *  Interface ControlCollectionSite
   *
   *    @serialField controlCollectionSite
   */
    private final ControlCollectionSiteInterface controlCollectionSite;
  /**
   *  Interface ConcentrationSite
   *
   *    @serialField concentrationSite
   */
    private final ConcentrationSiteInterface concentrationSite;
  /**
   *  Interface Museum
   *
   *    @serialField museum
   */
    private final MuseumInterface museum;
  /**
   *  Interface AssaultParty
   *
   *    @serialField assaultParties
   */
    private final AssaultPartyInterface[] assaultParties;
  /**
   *  Interface Logger
   *
   *    @serialField logger
   */
    private final LoggerInterface logger;
  /**
   *  Aux Data Structure that manages the rooms
   *
   *    @serialField manager
   */
    private final RoomManager[] manager;
  /**
   *  Array that indicates which rooms were assigned
   *
   *    @serialField assignedRooms
   */ 
    private final int[] assignedRooms;
  /**
   *  Master Thief state
   *
   *    @serialField state
   */
    private int state;
  /**
   *  Number of total Paintings stolen
   *
   *    @serialField numberOfPaintings
   */   
    private int numberOfPaintings;
   /**
   *  Flag that indicates if the heist is or not over
   *
   *    @serialField heistOver
   */  
    private boolean heistOver = false;

    private final Constants constants;
    
    /**
     * getNumberOfPaintings
     * 
     * @return gets the number of paintings
     */
    public int getNumberOfPaintings() {
        return numberOfPaintings;
    }
    /**
     * setNumberOfPaintings
     * 
     * @param numberOfPaintings number of paintings
     */
    public void setNumberOfPaintings(int numberOfPaintings) {
        this.numberOfPaintings = numberOfPaintings;
    }
  /**
   *  MasterThief Instantiation
   *
   *    @param museum Interface Museum
   *    @param controlCollectionSite Interface ControlCollectionSite
   *    @param concentrationSite Interface ConcentrationSite
   *    @param assaultParties Interface AssaultParties
   *    @param logger Interface logger
     * @param constants Constants
   */
    public MasterThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, LoggerInterface logger, Constants constants){
        this.constants = constants;
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.museum = museum;
        this.assaultParties = assaultParties;
        this.logger = logger;
        this.manager =  new RoomManager[this.constants.ROOMS_NUMBER];
        this.assignedRooms = new int[this.constants.ASSAULT_PARTIES_NUMBER];
        this.numberOfPaintings = 0;
        this.state = PLANING_THE_HEIST;
        for(int i=0;i<this.constants.ROOMS_NUMBER;i++){
            this.manager[i] = new RoomManager();
        }
    }
    
    /**
     * getRoomToBeStolen
     * 
     * @return gets the room id to be stolen
     */
    public int getRoomToBeStolen(){
        for(int i=0; i<this.manager.length;i++){
            if(!this.manager[i].isBeingStolen() && !this.manager[i].isEmptyRoom())
            {
                return i;
            }
        }
        return this.manager.length-1;
    }
    /**
     * checkAllEmpty
     * 
     * @return boolean flag, true if all rooms are empty, false if not
     */
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
                        for(int j =0; j<this.constants.ASSAULT_PARTIES_NUMBER;j++){
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
                        for(int i=0;i<this.constants.ROOMS_NUMBER;i++){
                            manager[i].setBeingStolen(false);
                        }
                        for(int i=0; i<this.constants.ASSAULT_PARTIES_NUMBER;i++){
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
