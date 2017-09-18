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
import Interfaces.AssaultPartyInterface;
import java.util.HashMap;


public class AssaultPartyProxy implements AssaultPartyInterface {
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
     * AssaultPartyProxy Instantiation
     * @param id AssaultParty Identification
     * @param constants Constants
     * @param configs Configurations
     */
    public AssaultPartyProxy(int id, Constants constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
        String command = "AssaultPartyServer_"+id;
        this.serverHostName = this.configs[0].get(command);
        this.serverHostPort = Integer.parseInt(this.configs[1].get(command));
    }

    /**
     * Join party
     * @param thiefID Thief identification
     * @param displacement Thief displacement
     */
    @Override
    public void joinParty(int thiefID, int displacement) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("JOIN PARTY");
        outMessage = new Message(JOIN_PARTY, thiefID, displacement);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     * Crawling in
     * @param thiefID Thief Identification
     * @return Distance left to reach the room
     * @throws Exception Exception thief index error
     */
    @Override
    public int crawlIn(int thiefID) throws Exception {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("CRAWL IN");
        outMessage = new Message(CRAWL_IN, thiefID);        
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
    * Crawling out
    * 
    * @param thiefID Thief identification
    * @return Distance left to reach the outside 
    * @throws Exception thief index error
    */
    @Override
    public int crawlOut(int thiefID) throws Exception {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("CRAWL OUT");
        outMessage = new Message(CRAWL_OUT, thiefID);       
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
     * 
     * Wait all Elements
     */
    @Override
    public void waitAllElems() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("WAIT ALL ELEMS");
        outMessage = new Message(WAIT_ALL_ELEMS); 
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Reverse direction
     * @param thiefID Thief identification
     */
    @Override
    public void reverseDirection(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("REVERSE DIR");
        outMessage = new Message(REVERSE_DIRECTION, thiefID);       
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * resetAndSet
     * 
     * @param roomID Room identification
     * @param distanceToRoom Distance to the room
     */
    @Override
    public void resetAndSet(int roomID, int distanceToRoom) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("R AND S");
        outMessage = new Message(RESET_AND_SET, roomID, distanceToRoom);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    /**
     * Gets the Party Identification
     * 
     * @return Party identification
     */
    @Override
    public int getPartyID() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("GET PARTY ID");
        outMessage = new Message(GET_PARTY_ID);        
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
     * Gets the Room identification
     * 
     * @return Room identification
     */
    @Override
    public int getRoomID() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("GET ROOM ID");
        outMessage = new Message(GET_ROOM_ID);       
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
