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
import Interfaces.ControlCollectionSiteInterface;
import java.util.HashMap;

public class ControlCollectionSiteProxy implements ControlCollectionSiteInterface {
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
     * ControlCollectionSiteProxy Instantiation
     * @param constants Constants
     * @param configs Configurations
     */
    public ControlCollectionSiteProxy(Constants constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
        this.serverHostName = this.configs[0].get("ControlCollectionSiteServer");
        this.serverHostPort = Integer.parseInt(this.configs[1].get("ControlCollectionSiteServer"));
    }

    /**
     * Appraises the Situation 
     * @param over <b>boolean flag</b> if heist is over or not
     * @return TO_PRESENTING_THE_REPORT, TO_ASSEMBLE_A_GROUP or TO_WAIT_FOR_GROUP 
     */
    @Override
    public int appraiseSit(boolean over) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("APPRAISE SIT CLIENT");
        outMessage = new Message(APPRAISE_SIT, over);
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
     * Starts the operations
     */
    @Override
    public void startOperations() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("STARTS OPS SEND");
        outMessage = new Message(START_OPS); 
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
    }
    /**
     * Sends the AssaultParty to a new Heist
     */
    @Override
    public void sendAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        System.out.println("SEND ASSAULT PARTY");
        outMessage = new Message(SEND_AP);       
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Master thief goes to sleep
     */
    @Override
    public void takeARest() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("TAKE A REST");
        outMessage = new Message(TAKE_REST); 
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Master Thief collects the canvas of each thief back from Heist
     * 
     * @return boolean data structure signaling which thieves had a canvas
     */
    @Override
    public boolean[] collectCanvas() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("COLLECT A CANVAS");
        outMessage = new Message(COLLECT_CANVAS);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return inMessage.getBooleanArray();
    }
    /**
     * Sums up all the paintings
     * @return total sum of paintings
     */
    @Override
    public int sumUpResults() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("SUM UP RES");
        outMessage = new Message(SUM_UP_RESULTS); 
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
     * Each thief back from heist hands the canvas to Master Thief
     * @param hasPainting <b>true</b> if thief has a canvas to deliver, <b>false</b> if not
     * @param thiefID thief id
     */
    @Override
    public void handACanvas(boolean hasPainting, int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("HAND A CANVAS");
        outMessage = new Message(HAND_A_CANVAS, hasPainting, thiefID);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Prepares an Excursion
     * @param thiefID thief id
     * @return party that thief joined/belongs
     */
    @Override
    public int prepareExcursion(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("PREPARE EXCURSION");
        outMessage = new Message(PREPARE_EXCURSION, thiefID);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return inMessage.getMessageArg_1();
    }

}
