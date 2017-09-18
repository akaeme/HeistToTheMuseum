/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import ComInf.Message;
import static ComInf.Message.*;
import Communication.ClientCom;
import Constants.Constants;
import Interfaces.ConcentrationSiteInterface;
import java.util.HashMap;


public class ConcentrationSiteProxy implements ConcentrationSiteInterface {
  /**
   *  Server Host Name
   *
   *    @serialField serverHostName
   */
    private final String serverHostName;
    
  /**
   *  Server Host Port
   *
   *    @serialField serverHostPort
   */
    private final int serverHostPort;

    
  /**
   *  Constants
   *
   *    @serialField constants
   */
    private final Constants constants;
    
    
  /**
   *  Configurations
   *
   *    @serialField configs
   */
    private final HashMap<String, String>[] configs;

    /**
     * ConcentrationSiteProxy Instantiation
     * @param constants Constants
     * @param configs Configurations
     */
    public ConcentrationSiteProxy(Constants constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
        this.serverHostName = this.configs[0].get("ConcentrationSiteServer");
        this.serverHostPort = Integer.parseInt(this.configs[1].get("ConcentrationSiteServer"));
    }
    /**
     * Prepares AssaultParty
     */
    @Override
    public void prepareAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("PREPARE ASSAULT PARTY");
        outMessage = new Message(PREPARE_AP);       
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Thief amINeeded
     * 
     * @param thiefID thief id
     * @return state of the thief
     */
    @Override
    public int amINeeded(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("AM I NEEDED");
        outMessage = new Message(AM_I_NEEDED, thiefID); 
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return inMessage.getMessageArg_1();
    }
    /**
     * Heist Over
     */
    @Override
    public void heistOver() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("HEIST OVER");
        outMessage = new Message(HEIST_OVER);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

}
