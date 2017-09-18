/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import AuxDataStructs.RoomManager;
import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.*;
import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterThief extends Thread {

    /**
     * Interface ControlCollectionSite
     *
     * @serialField controlCollectionSite
     */
    private final ControlCollectionSiteInterface controlCollectionSite;
    /**
     * Interface ConcentrationSite
     *
     * @serialField concentrationSite
     */
    private final ConcentrationSiteInterface concentrationSite;
    /**
     * Interface Museum
     *
     * @serialField museum
     */
    private final MuseumInterface museum;
    /**
     * Interface AssaultParty
     *
     * @serialField assaultParties
     */
    private final AssaultPartyInterface[] assaultParties;
    /**
     * Interface Logger
     *
     * @serialField logger
     */
    private final LoggerInterface logger;
    /**
     * Aux Data Structure that manages the rooms
     *
     * @serialField manager
     */
    private final RoomManager[] manager;
    /**
     * Array that indicates which rooms were assigned
     *
     * @serialField assignedRooms
     */
    private final int[] assignedRooms;
    /**
     * Master Thief state
     *
     * @serialField state
     */
    private int state;
    /**
     * Number of total Paintings stolen
     *
     * @serialField numberOfPaintings
     */
    private int numberOfPaintings;
    /**
     * Flag that indicates if the heist is or not over
     *
     * @serialField heistOver
     */
    private boolean heistOver = false;

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
    private final VectorTimestamp clocks;
    private VectorTimestamp clocksReceived;

    /**
     * MasterThief Instantiation
     *
     * @param museum Interface Museum
     * @param controlCollectionSite Interface ControlCollectionSite
     * @param concentrationSite Interface ConcentrationSite
     * @param assaultParties Interface AssaultParties
     * @param logger Interface logger
     */
    public MasterThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, LoggerInterface logger) {
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.museum = museum;
        this.assaultParties = assaultParties;
        this.logger = logger;
        this.manager = new RoomManager[ROOMS_NUMBER];
        this.assignedRooms = new int[ASSAULT_PARTIES_NUMBER];
        this.numberOfPaintings = 0;
        this.state = PLANING_THE_HEIST;
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.manager[i] = new RoomManager();
        }
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 6);
    }

    /**
     * getRoomToBeStolen
     *
     * @return gets the room id to be stolen
     */
    public int getRoomToBeStolen() {
        for (int i = 0; i < this.manager.length; i++) {
            if (!this.manager[i].isBeingStolen() && !this.manager[i].isEmptyRoom()) {
                return i;
            }
        }
        return this.manager.length - 1;
    }

    /**
     * checkAllEmpty
     *
     * @return boolean flag, true if all rooms are empty, false if not
     */
    public boolean checkAllEmpty() {
        for (RoomManager rm : manager) {
            if (!rm.isEmptyRoom()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        System.out.println("Running....................");
        while (!this.heistOver) {
            switch (this.state) {
                case PLANING_THE_HEIST: {
                    try {
                        System.out.println("Planning the heist");
                        this.clocks.increment();
                        this.clocksReceived = this.logger.MasterThiefLog(this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                        this.clocks.increment();
                        this.clocksReceived = this.controlCollectionSite.startOperations(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.state = DECIDING_WHAT_TO_DO;
                break;
                case DECIDING_WHAT_TO_DO: {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.MasterThiefLog(this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                int action = 0;
                try {
                    this.clocks.increment();
                    this.clocksReceived = this.controlCollectionSite.appraiseSit(this.checkAllEmpty(), this.clocks.clone());
                    action = this.clocksReceived.getArg_integer();
                    this.clocks.update(this.clocksReceived);
                } catch (RemoteException ex) {
                    Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                }
                switch (action) {
                    case TO_ASSEMBLE_A_GROUP: {
                        try {
                            this.clocks.increment();
                            this.clocksReceived = this.concentrationSite.prepareAssaultParty(this.clocks.clone());
                            this.clocks.update(this.clocksReceived);
                        } catch (RemoteException ex) {
                            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    this.state = ASSEMBLING_A_GROUP;
                    break;
                    case TO_WAIT_FOR_GROUP: {
                        try {
                            this.clocks.increment();
                            this.clocksReceived = this.controlCollectionSite.takeARest(this.clocks.clone());
                            this.clocks.update(this.clocksReceived);
                        } catch (RemoteException ex) {
                            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    this.state = WAITNG_FOR_GROUP_ARRIVAL;
                    break;
                    case TO_PRESENTING_THE_REPORT:
                        this.state = PRESENTING_THE_REPORT;
                        break;
                    default:
                        break;
                }
                break;
                case ASSEMBLING_A_GROUP: {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.MasterThiefLog(this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for (int j = 0; j < ASSAULT_PARTIES_NUMBER; j++) {
                    int roomToSteal = this.getRoomToBeStolen();
                    int distance = 0;
                    try {
                        this.clocks.increment();
                        this.clocksReceived = museum.getRoomDistance(roomToSteal, this.clocks.clone());
                        distance = this.clocksReceived.getArg_integer();
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.assaultParties[j].resetAndSet(roomToSteal, distance, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.assignedRooms[j] = roomToSteal;
                    this.manager[roomToSteal].setBeingStolen(true);
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.controlCollectionSite.sendAssaultParty(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.state = DECIDING_WHAT_TO_DO;
                break;
                case PRESENTING_THE_REPORT: {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.controlCollectionSite.sumUpResults(this.clocks.clone());
                        this.numberOfPaintings = this.clocksReceived.getArg_integer();
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.concentrationSite.heistOver(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.MasterThiefLog(this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.FinalReport(this.numberOfPaintings, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.heistOver = true;
                System.out.println("OVER");
                break;
                case WAITNG_FOR_GROUP_ARRIVAL: {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.MasterThiefLog(this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                boolean[] empty = null;
                try {
                    System.out.println("Collect");
                    this.clocks.increment();
                    this.clocksReceived = this.controlCollectionSite.collectCanvas(this.clocks.clone());
                    empty = this.clocksReceived.getArg_bool_array();
                    this.clocks.update(this.clocksReceived);
                } catch (RemoteException ex) {
                    Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < ROOMS_NUMBER; i++) {
                    manager[i].setBeingStolen(false);
                }
                for (int i = 0; i < ASSAULT_PARTIES_NUMBER; i++) {
                    if (empty[i]) {
                        this.manager[this.assignedRooms[i]].setEmptyRoom(true);
                    }
                }
                this.state = DECIDING_WHAT_TO_DO;
                break;
            }
        }
    }

}
