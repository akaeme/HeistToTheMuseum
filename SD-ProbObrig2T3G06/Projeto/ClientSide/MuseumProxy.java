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
import Interfaces.MuseumInterface;
import java.util.HashMap;


public class MuseumProxy implements MuseumInterface {
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
    * Configurations
    *   @serialField configs
    */
    private final HashMap<String, String>[] configs;
/**
 * MuseumProxy Instantiation
 * 
     * @param constants Constants
     * @param configs Configurations
 */
    public MuseumProxy(Constants constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
        this.serverHostName = this.configs[0].get("MuseumServer");
        this.serverHostPort = Integer.parseInt(this.configs[1].get("MuseumServer"));
    }
/**
 * getPaintingNumbers
 * 
 * @param roomId Room Id
 * @return Number of paintings on that room
 */
    @Override
    public int getPaintingNumbers(int roomId) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("GET PAINTING NUMBERS");
        outMessage = new Message(GET_NUMBER_PAINTINGS, roomId);        
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
     * getRoomDistance
     * 
     * @param roomId Room id
     * @return distance to that room from Outside
     */
    @Override
    public int getRoomDistance(int roomId) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("GET ROOM DISTANCE");
        outMessage = new Message(GET_ROOM_DISTANCE, roomId);        
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
 * rollACanvas
 * 
 * @param roomID Room Id
 * @return Boolean flag, true if rollACanvas succeeded
 */
    @Override
    public boolean rollACanvas(int roomID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        System.out.println("ROLL A CANVAS");
        outMessage = new Message(ROLL_A_CANVAS, roomID);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        
        con.close();
        
        return inMessage.getFlag();
    }

}
