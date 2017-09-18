/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdinaryThief extends Thread {

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
     * Id AssaultParty
     *
     * @serialField myParty
     */
    private AssaultPartyInterface myParty;

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
     * Interface Logger
     *
     * @serialField logger
     */
    private final LoggerInterface logger;
    /**
     * Thief id
     *
     * @serialField id
     */
    private final int id;
    /**
     * Thief displacement
     *
     * @serialField displacement
     */
    private final int displacement;
    /**
     * Thief state
     *
     * @serialField state
     */
    private int state;

    /**
     * Thief flag that indicates if he has a canvas
     *
     * @serialField busyHands
     */
    private boolean busyHands;
    /**
     * Thief flag that indicates that he has returned from the heist
     *
     * @serialField backFromheist
     */
    private boolean backFromheist;
    /**
     * Distance to the room, from the Outside
     *
     * @serialField distanceToRoom
     */
    private int distanceToRoom = 0;

    /**
     * Flag that indicates if the heist is or not over
     *
     * @serialField heistOver
     */
    private boolean heistOver = false;

    /**
     * Party Setter
     *
     * @param index assaultParty identification
     */
    public void setMyParty(int index) {
        this.myParty = this.assaultParties[index];
    }

    private final VectorTimestamp clocks;
    private VectorTimestamp clocksReceived;

    /**
     * OrdinaryThief Instantiation
     *
     * @param museum Interface Museum
     * @param controlCollectionSite Interface ControlCollectionSite
     * @param concentrationSite Interface ConcentrationSite
     * @param assaultParties Interface AssaultParties
     * @param id Thief id
     * @param logger Interface logger
     * @throws RemoteException Remote Exception
     * @throws CloneNotSupportedException Clone not supported
     */
    public OrdinaryThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, int id, LoggerInterface logger) throws RemoteException, CloneNotSupportedException {
        this.museum = museum;
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.assaultParties = assaultParties;
        this.logger = logger;

        this.id = id;
        this.displacement = (int) (Math.random() * (MAX_THIEVES_DISPLACEMENT + 1 - MIN_THIEVES_DISPLACEMENT)) + MIN_THIEVES_DISPLACEMENT;
        this.state = OUTSIDE;
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, this.id);
        this.clocksReceived = this.logger.OrdinaryThiefLog(MD, this.id, this.displacement, this.clocks.clone());
        this.clocks.update(this.clocksReceived);
    }

    @Override
    public void run() {
        System.out.println("Running OT....................");
        while (!this.heistOver) {
            switch (this.state) {
                case OUTSIDE: {
                    try {
                        System.out.println("OUTSIDEEEEEEEEEEEEEEEEEEEEEEEE");
                        this.clocks.increment();
                        this.clocksReceived = this.logger.OrdinaryThiefLog(Stat, this.id, this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.logger.OrdinaryThiefLog(S, this.id, 0, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.backFromheist) {
                    this.backFromheist = false;
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.controlCollectionSite.handACanvas(this.busyHands, this.id, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.busyHands = false;
                }
                int signal = 0;
                try {
                    this.clocks.increment();
                    this.clocksReceived = this.concentrationSite.amINeeded(this.id, this.clocks.clone());
                    signal = this.clocksReceived.getArg_integer();
                    this.clocks.update(this.clocksReceived);
                } catch (RemoteException ex) {
                    Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (signal == 1) {
                    this.heistOver = true;
                    break;
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.controlCollectionSite.prepareExcursion(this.id, this.clocks.clone());
                        this.setMyParty(this.clocksReceived.getArg_integer());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.joinParty(this.id, this.displacement, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.state = CRAWLING_INWARDS;
                break;
                case CRAWLING_INWARDS: {
                    try {
                        System.out.println("CRAWLINNNNNNNNNNNNN");
                        this.clocks.increment();
                        this.clocksReceived = this.logger.OrdinaryThiefLog(Stat, this.id, this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.crawlIn(this.id, this.clocks.clone());
                        this.distanceToRoom = this.clocksReceived.getArg_integer();
                        this.clocks.update(this.clocksReceived);
                    } catch (Exception ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.distanceToRoom == 0) {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.waitAllElems(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.state = AT_ROOM;
                }
                break;
                case AT_ROOM: {
                    try {
                        System.out.println("AT ROOOOMMMM");
                        this.clocks.increment();
                        this.clocksReceived = logger.OrdinaryThiefLog(Stat, this.id, this.state, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.getRoomID(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                        this.clocks.increment();
                        this.clocksReceived = museum.rollACanvas(this.clocksReceived.getArg_integer(), this.clocks.clone());
                        this.busyHands = this.clocksReceived.isArg_bool();
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.busyHands) {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.getPartyID(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                        this.clocks.increment();
                        this.clocksReceived = this.logger.AssaultPartyLog(Cv, this.clocksReceived.getArg_integer(), this.id, 1, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.getPartyID(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                        this.clocks.increment();
                        this.clocksReceived = this.logger.AssaultPartyLog(Cv, this.clocksReceived.getArg_integer(), this.id, 0, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                 {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.reverseDirection(this.id, this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.state = CRAWLING_OUTWARDS;
                break;
                case CRAWLING_OUTWARDS: {
                    try {
                        System.out.println("OUTTTTTTTTTTTTTTTTTTTTTTTTTT");
                        this.clocks.increment();
                         this.clocksReceived = logger.OrdinaryThiefLog(Stat, this.id, this.state, this.clocks.clone());
                          this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.crawlOut(this.id, this.clocks.clone());
                        this.distanceToRoom = this.clocksReceived.getArg_integer();
                         this.clocks.update(this.clocksReceived);
                    } catch (Exception ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.distanceToRoom == 0) {
                    try {
                        this.clocks.increment();
                        this.clocksReceived = this.myParty.waitAllElems(this.clocks.clone());
                        this.clocks.update(this.clocksReceived);
                    } catch (RemoteException ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.backFromheist = true;
                    this.state = OUTSIDE;
                }
                break;
                default:
                    System.out.println("END");
            }
        }
    }
}
