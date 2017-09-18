/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heisttothemuseum;

import static Constants.Constants.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;
import Monitors.*;
import Threads.*;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class HeistToTheMuseum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AssaultParty[] assaultParties = new AssaultParty[ASSAULT_PARTIES_NUMBER];
        ControlCollectionSite controlCollectionSite = new ControlCollectionSite();
        ConcentrationSite concentrationSite = new ConcentrationSite();
        Logger logger = new Logger();
        Museum museum = new Museum((LoggerInterface) logger);
        OrdinaryThief ordinaryThieves[] = new OrdinaryThief[ORDINARY_THIEVES];
        
        for(int i=0;i<assaultParties.length;i++){
            assaultParties[i] = new AssaultParty(i, (LoggerInterface) logger);
        }
        MasterThief masterThief = new MasterThief((MuseumInterface) museum, (ControlCollectionSiteInterface) controlCollectionSite, (ConcentrationSiteInterface) concentrationSite, (AssaultPartyInterface[]) assaultParties, (LoggerInterface) logger);
        for(int i=0;i<ordinaryThieves.length;i++){
            ordinaryThieves[i] = new OrdinaryThief((MuseumInterface) museum, (ControlCollectionSiteInterface) controlCollectionSite, (ConcentrationSiteInterface) concentrationSite, (AssaultPartyInterface[]) assaultParties, i, (LoggerInterface) logger);
        }
        
        masterThief.start();
        for(OrdinaryThief ot: ordinaryThieves){
            ot.start();
        }
    }

}
