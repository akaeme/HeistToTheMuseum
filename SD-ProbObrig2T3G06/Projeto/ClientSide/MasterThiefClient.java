/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import Constants.Constants;
import Interfaces.AssaultPartyInterface;
import Interfaces.ConcentrationSiteInterface;
import Interfaces.ControlCollectionSiteInterface;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;
import java.io.IOException;
import java.util.HashMap;


public class MasterThiefClient {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException IO invalid
     * @throws java.lang.ClassNotFoundException Class not found
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String hostName = args[0];
        int hostPort = Integer.parseInt(args[1]);
        HashMap<String, String>[] configs;
        Constants constants;
  
        CentralizedProxy centralizedProxy = new CentralizedProxy(hostName, hostPort);
        constants = centralizedProxy.getConstants();
        configs = centralizedProxy.getConfigs();
        
        AssaultPartyProxy[] assaultPartyProxy = new AssaultPartyProxy[constants.ASSAULT_PARTIES_NUMBER];
        MuseumProxy museumProxy = new MuseumProxy(constants, configs);
        ConcentrationSiteProxy concentrationSiteProxy = new ConcentrationSiteProxy(constants, configs);
        ControlCollectionSiteProxy controlConcentrationSideProxy = new ControlCollectionSiteProxy(constants, configs);
        LoggerProxy loggerProxy = new LoggerProxy(constants, configs);
        
        for(int i=0; i<assaultPartyProxy.length;i++){
            assaultPartyProxy[i] = new AssaultPartyProxy(i, constants, configs);
        }
        
        MasterThief masterThief = new MasterThief((MuseumInterface) museumProxy, (ControlCollectionSiteInterface) controlConcentrationSideProxy, (ConcentrationSiteInterface) concentrationSiteProxy, (AssaultPartyInterface[]) assaultPartyProxy, (LoggerInterface) loggerProxy, constants);
        
        masterThief.start();
        
        while(masterThief.isAlive()){
            Thread.yield();
        }
        try{
            masterThief.join();
        }catch(InterruptedException e ){
            
        }
        
    }
    
}
