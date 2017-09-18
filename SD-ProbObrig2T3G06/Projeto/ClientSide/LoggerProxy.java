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
import Interfaces.LoggerInterface;
import java.util.HashMap;


public class LoggerProxy implements LoggerInterface {
  /**
   *  Server Host name
   *
   *    @serialField serverHostName
   */
    private final String serverHostName;
  /**
   *  Server Port Id
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
     * LoggerProxy Instantiation
     * @param constants Constants
     * @param configs Configurations
     */
    public LoggerProxy(Constants constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
        this.serverHostName = this.configs[0].get("LoggerServer");
        this.serverHostPort = Integer.parseInt(this.configs[1].get("LoggerServer"));
    }

    
    /**
     * Ordinary Thief log
     * @param kind State, Situation or Maximum Displacement
     * @param Tid Thief Identification
     * @param val Value
     */
    @Override
    public void OrdinaryThiefLog(int kind, int Tid, int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("ORDINARY THIEF LOG");
        outMessage = new Message(ORDINARY_THIEF_LOG, kind, Tid, val);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     * Master Theif Log
     * @param val value
     */
    @Override
    public void MasterThiefLog(int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("MASTER_THIEF_LOG LOG");
        outMessage = new Message(MASTER_THIEF_LOG, val);       
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     * FinalReport
     * @param total_paint total paintings
     */
    @Override
    public void FinalReport(int total_paint) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        System.out.println("FINAL_REPORT LOG");
        
        outMessage = new Message(FINAL_REPORT, total_paint);       
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
        /**
     * AssaultParty Log
     * @param kind Room Id, Member Id, Position, Carrying a Canvas
     * @param APid AssaultParty Identification
     * @param Eid Element Id
     * @param val Value
     */
    @Override
    public void AssaultPartyLog(int kind, int APid, int Eid, int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("ASSAULT_PARTY_LOG");
        outMessage = new Message(ASSAULT_PARTY_LOG, kind, APid, Eid, val); 
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
