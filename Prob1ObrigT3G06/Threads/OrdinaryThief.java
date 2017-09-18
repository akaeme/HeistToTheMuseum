/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import static Constants.Constants.AT_ROOM;
import static Constants.Constants.CRAWLING_INWARDS;
import static Constants.Constants.CRAWLING_OUTWARDS;
import static Constants.Constants.MAX_THIEVES_DISPLACEMENT;
import static Constants.Constants.MIN_THIEVES_DISPLACEMENT;
import static Constants.Constants.OUTSIDE;
import static Constants.LoggerConstants.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class OrdinaryThief extends Thread {

    private final MuseumInterface museum;
    private AssaultPartyInterface[] assaultParties;
    private AssaultPartyInterface myParty;
    private final ControlCollectionSiteInterface controlCollectionSite;
    private final ConcentrationSiteInterface concentrationSite;
    private final LoggerInterface logger;
    private final int id;
    private final int displacement;
    private int state;
    private boolean busyHands;
    private boolean backFromheist;
    private int distanceToRoom = 0;
    private boolean heistOver = false;

    public void setMyParty(int index) {
        this.myParty = this.assaultParties[index];
    }

    public OrdinaryThief(MuseumInterface museum, ControlCollectionSiteInterface controlCollectionSite, ConcentrationSiteInterface concentrationSite, AssaultPartyInterface[] assaultParties, int id, LoggerInterface logger) {
        this.museum = museum;
        this.controlCollectionSite = controlCollectionSite;
        this.concentrationSite = concentrationSite;
        this.assaultParties = assaultParties;
        this.logger = logger;

        this.id = id;
        this.displacement = (int) (Math.random() * (MAX_THIEVES_DISPLACEMENT + 1 - MIN_THIEVES_DISPLACEMENT)) + MIN_THIEVES_DISPLACEMENT;
        this.state = OUTSIDE;
        logger.OrdinaryThiefLog(MD, this.id, this.displacement);
    }

    @Override
    public void run() {
        while (!this.heistOver) {
            switch (this.state) {
                case OUTSIDE:
                    logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    logger.OrdinaryThiefLog(S, this.id, 0);
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
                    logger.OrdinaryThiefLog(Stat, this.id, this.state);
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
                    logger.OrdinaryThiefLog(Stat, this.id, this.state);
                    this.busyHands = museum.rollACanvas(this.myParty.getRoomID());
                    if (this.busyHands) {
                        logger.AssaultPartyLog(Cv, this.myParty.getPartyID(), this.id, 1);
                    } else {
                        logger.AssaultPartyLog(Cv, this.myParty.getPartyID(), this.id, 0);
                    }
                    this.myParty.reverseDirection(this.id);
                    this.state = CRAWLING_OUTWARDS;
                    break;
                case CRAWLING_OUTWARDS: {
                    logger.OrdinaryThiefLog(Stat, this.id, this.state);
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
