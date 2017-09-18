/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import Constants.Constants;
import static Constants.LoggerConstants.*;
import static Constants.States.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OrdinaryThief extends Thread {
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
   *  Id AssaultParty
   *
   *    @serialField myParty
   */
    private AssaultPartyInterface myParty;
    
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
   *  Interface Logger
   *
   *    @serialField logger
   */ 
    private final LoggerInterface logger;
  /**
   *  Thief id
   *
   *    @serialField id
   */ 
    private final int id;
  /**
   *  Thief displacement
   *
   *    @serialField displacement
   */   
    private final int displacement;
  /**
   *  Thief state
   *
   *    @serialField state
   */  
    private int state;
    
  /**
   *  Thief flag that indicates if he has a canvas
   *
   *    @serialField busyHands
   */    
    private boolean busyHands;
  /**
   *  Thief flag that indicates that he has returned from the heist
   *
   *    @serialField backFromheist
   */ 
    private boolean backFromheist;
  /**
   *  Distance to the room, from the Outside
   *
   *    @serialField distanceToRoom
   */
    private int distanceToRoom = 0;
    
  /**
   *  Flag that indicates if the heist is or not over
   *
   *    @serialField heistOver
   */    
    private boolean heistOver = false;

    private final Constants constants;
  /**
   *  Party Setter
   *
   *    @param index assaultParty identification
   */
    public void setMyParty(int index) {
        this.myParty = this.assaultParties[index];
    }
  /**
   *  OrdinaryThief Instantiation
   *
   *    @param museum Interface Museum
   *    @param controlCollectionSite Interface ControlCollectionSite
   *    @param concentrationSite Interface ConcentrationSite
   *    @param assaultParties Interface AssaultParties
   *    @param id Thief id
   *    @param logger Interface logger
   *    @param constants Constants
   */
    public OrdinaryThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, int id, LoggerInterface logger, Constants constants) {
        this.constants = constants;
        this.museum = museum;
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.assaultParties = assaultParties;
        this.logger = logger;

        this.id = id;
        this.displacement = (int) (Math.random() * (this.constants.MAX_THIEVES_DISPLACEMENT + 1 - this.constants.MIN_THIEVES_DISPLACEMENT)) + this.constants.MIN_THIEVES_DISPLACEMENT;
        this.state = OUTSIDE;
        this.logger.OrdinaryThiefLog(MD, this.id, this.displacement);
    }

    @Override
    public void run() {
        while (!this.heistOver) {
            switch (this.state) {
                case OUTSIDE:
                    this.logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    this.logger.OrdinaryThiefLog(S, this.id, 0);
                    if (this.backFromheist) {
                        this.backFromheist = false;
                        this.controlCollectionSite.handACanvas(this.busyHands, this.id);
                        this.busyHands = false;
                    }
                    int signal = this.concentrationSite.amINeeded(this.id);
                    if (signal == 1) {
                        this.heistOver = true;
                        break;
                    }
                    this.setMyParty(this.controlCollectionSite.prepareExcursion(this.id));
                    this.myParty.joinParty(this.id, this.displacement);
                    this.state = CRAWLING_INWARDS;
                    break;
                case CRAWLING_INWARDS: {
                    this.logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    try {
                        this.distanceToRoom = this.myParty.crawlIn(this.id);
                    } catch (Exception ex) {
                        Logger.getLogger(OrdinaryThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.distanceToRoom == 0) {
                    this.myParty.waitAllElems();
                    this.state = AT_ROOM;
                }
                break;
                case AT_ROOM:
                    this.logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    this.busyHands = museum.rollACanvas(this.myParty.getRoomID());
                    if (this.busyHands) {
                        this.logger.AssaultPartyLog(Cv, this.myParty.getPartyID(), this.id, 1);
                    } else {
                        this.logger.AssaultPartyLog(Cv, this.myParty.getPartyID(), this.id, 0);
                    }
                    this.myParty.reverseDirection(this.id);
                    this.state = CRAWLING_OUTWARDS;
                    break;
                case CRAWLING_OUTWARDS: {
                    this.logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    try {
                        this.distanceToRoom = this.myParty.crawlOut(this.id);
                    } catch (Exception ex) {
                        Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (this.distanceToRoom == 0) {
                    this.myParty.waitAllElems();
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
