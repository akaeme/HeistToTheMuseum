/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import AuxDataStructs.ThiefInParty;
import ComInf.Message;
import static ComInf.Message.*;
import Communication.ClientCom;
import Constants.Constants;
import static Constants.LoggerConstants.*;
import Interfaces.AssaultPartyInterface;
//import Interfaces.LoggerInterface;
import java.util.Arrays;
import java.util.HashMap;

public class AssaultParty implements AssaultPartyInterface {
  /**
   *  Thieves Number in AssaultParty
   *
   *    @serialField nThieves
   */
    private int nThieves;
  /**
   *  Index of the thief to move on the crawling process
   *
   *    @serialField indexToMove
   */
    private int indexToMove;
  /**
   *  Room Identification
   *
   *    @serialField roomID
   */
    private int roomID;
  /**
   *  Distance to the room from Outside
   *
   *    @serialField distanteToRoom
   */
    private int distanceToRoom;
  /**
   *  Aux Data Structure indicates the thieves party distribution
   *
   *    @serialField thieves
   */
    private ThiefInParty[] thieves;
    
  /**
   *  Party Id
   * 
   *    @serialField partyID
   */
    private final int partyID;
    //private LoggerInterface logger;
    private Constants constants;
    private HashMap<String, String>[] configs;
    /**
     * AssaultParty Instantiation
     * 
     * @param id Party identification
     * @param constants Constants
     * @param configs Configurations
     */
    public AssaultParty(int id, Constants constants, HashMap<String, String>[] configs) {
        this.configs = configs;
        this.constants = constants;
        this.partyID = id;
        this.roomID = -1;
        this.thieves = new ThiefInParty[this.constants.MAX_PARTY_THIEVES];
        this.indexToMove = 0;
        this.roomID = -1;
        this.distanceToRoom = -1;
        this.nThieves = 0;
        //this.logger = logger;
    }

    /**
     * Thief joins a party
     * 
     * @param thiefID Thief identification
     * @param displacement Thief displacement
     */
    @Override
    public synchronized void joinParty(int thiefID, int displacement) {
        this.thieves[this.nThieves] = new ThiefInParty(displacement, 0, thiefID);
        this.nThieves++;
        if (this.nThieves == this.constants.MAX_PARTY_THIEVES) {
            notifyAll();
        }
        
        
        this.OrdinaryThiefLog(S, partyID, 1);
        this.AssaultPartyLog(Id, partyID, thiefID, thiefID);
        this.AssaultPartyLog(Pos, partyID, thiefID, 0);
        this.AssaultPartyLog(Cv, partyID, thiefID, 0);
    }
/**
 * Crawling in
 * 
 * @param thiefID Thief identification
 * @return Distance left to reach the room 
 * @throws Exception thief index error
 */
    @Override
    public synchronized int crawlIn(int thiefID) throws Exception {
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != this.constants.MAX_PARTY_THIEVES || this.roomID == -1) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        int predictPos[] = null;
        boolean GO = false;
        for (int i = this.thieves[this.indexToMove].getDisplacement(); i > 0; i--) {
            predictPos = this.getArrayPos();
            GO = true;
            predictPos[this.indexToMove] += i;
            if (predictPos[this.indexToMove] >= this.distanceToRoom) {
                predictPos[this.indexToMove] = this.distanceToRoom;
            }
            int[] checkPos = predictPos;
            Arrays.sort(checkPos);
            for (int j = checkPos.length - 1; j >= 1; j--) {
                if (checkPos[j] - checkPos[j - 1] > this.constants.MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
                    GO = false;
                }
            }
            if (GO) {
                break;
            }
        }
        if (GO) {
            this.thieves[this.indexToMove].setPosition(predictPos[this.indexToMove]);
        }
        int ret = this.indexToMove;
        if (this.indexToMove == 0) {
            this.indexToMove = this.constants.MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        this.AssaultPartyLog(Pos, this.partyID, thiefID, this.thieves[indexToMove].getPosition());
        return this.distanceToRoom - this.thieves[ret].getPosition();
    }
/**
 * Crawling out
 * 
 * @param thiefID Thief identification
 * @return Distance left to reach the outside 
 * @throws Exception thief index error
 */
    @Override
    public synchronized int crawlOut(int thiefID) throws Exception {
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != this.constants.MAX_PARTY_THIEVES || this.roomID == -1) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        int predictPos[] = null;
        boolean GO = false;
        for (int i = this.thieves[this.indexToMove].getDisplacement(); i > 0; i--) {
            predictPos = this.getArrayPos();
            GO = true;
            predictPos[this.indexToMove] += i;
            if (predictPos[this.indexToMove] >= this.distanceToRoom) {
                predictPos[this.indexToMove] = this.distanceToRoom;
            }
            int[] checkPos = predictPos;
            Arrays.sort(checkPos);
            for (int j = checkPos.length - 1; j >= 1; j--) {
                if (checkPos[j] - checkPos[j - 1] > this.constants.MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
                    GO = false;
                }
            }
            if (GO) {
                break;
            }
        }
        if (GO) {
            this.thieves[this.indexToMove].setPosition(predictPos[this.indexToMove]);
        }
        int ret = this.indexToMove;
        if (this.indexToMove == 0) {
            this.indexToMove = this.constants.MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        this.AssaultPartyLog(Pos, this.partyID, thiefID, this.distanceToRoom - this.thieves[indexToMove].getPosition());
        return this.distanceToRoom - this.thieves[ret].getPosition();
    }

    /**
     * 
     * Wait all Elements
     */
    @Override
    public synchronized void waitAllElems() {
        while (!this.allInDestination()) {
            try {
                if (!this.allInDestination() && this.thieves[indexToMove].getPosition() == this.distanceToRoom) {
                    if (this.indexToMove == 0) {
                        this.indexToMove = this.constants.MAX_PARTY_THIEVES - 1;
                    } else {
                        this.indexToMove -= 1;
                    }
                    notifyAll();
                }
                wait();
            } catch (InterruptedException e) {
            }
        }
        notifyAll();
    }

    /**
     * Reverse direction
     * @param thiefID Thief identification
     */
    @Override
    public synchronized void reverseDirection(int thiefID) {
        this.setThiefReady(thiefID);

        while (!this.checkAllReady()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        for (ThiefInParty TIP : thieves) {
            if (TIP.getId() == thiefID) {
                TIP.setPosition(0);
            }
        }
        this.indexToMove = this.constants.MAX_PARTY_THIEVES - 1;
        notifyAll();
    }
    /**
     * Gets the distance to Room
     * 
     * @return Distance to the room
     */
    public synchronized int getDistanceToRoom() {
        return this.distanceToRoom;
    }

    /**
     * resetAndSet
     * 
     * @param roomID Room identification
     * @param distanceToRoom Distance to the room
     */
    @Override
    public synchronized void resetAndSet(int roomID, int distanceToRoom) {
        this.nThieves = 0;
        this.roomID = roomID;
        this.distanceToRoom = distanceToRoom;
        this.AssaultPartyLog(RId, this.partyID, -1, this.roomID);
    }

    
    /**
     * Gets the Party Identification
     * 
     * @return Party identification
     */
    @Override
    public synchronized int getPartyID() {
        return this.partyID;
    }

    /**
     * Gets the Room identification
     * 
     * @return Room identification
     */
    @Override
    public synchronized int getRoomID() {
        return this.roomID;
    }

    /**
     * Sets the given Thief as ready
     * 
     * @param thiefID Thief identification
     */
    public synchronized void setThiefReady(int thiefID) {
        for (ThiefInParty TIP : thieves) {
            if (TIP.getId() == thiefID) {
                TIP.setReadyToGo(true);
            }
        }
    }

    /**
     * Check if all thieves in party are ready to go
     * 
     *    @return <b>true</b>, all ready to go, <b>false</b> if not
     */
    public synchronized boolean checkAllReady() {
        for (ThiefInParty TIP : thieves) {
            if (!TIP.isReadyToGo()) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if all thieves are in the same destination
     * 
     *    @return <b>true</b>, all in the same distance to room, <b>false</b> if not
     */
    public synchronized boolean allInDestination() {
        for (ThiefInParty TIP : thieves) {
            if (TIP.getPosition() != this.distanceToRoom) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the Thief position in array
     * 
     * @return Thief position
     */
    public synchronized int[] getArrayPos() {
        int[] pos = new int[this.thieves.length];
        for (int i = 0; i < this.thieves.length; i++) {
            pos[i] = this.thieves[i].getPosition();
        }
        return pos;
    }
    /**
     * Ordinary Thief log
     * @param arg_1 State, Situation or Maximum Displacement
     * @param arg_2 Thief Identification
     * @param arg_3 Value
     */
    public synchronized void OrdinaryThiefLog(int arg_1, int arg_2, int arg_3){
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.configs[0].get("LoggerServer"), Integer.parseInt(this.configs[1].get("LoggerServer")));
        if (!con.open()) {
            System.exit(1);
        }
        
        outMessage = new Message(ORDINARY_THIEF_LOG, arg_1, arg_2, arg_3);        
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
     * @param arg_1 Room Id, Member Id, Position, Carrying a Canvas
     * @param arg_2 AssaultParty Identification
     * @param arg_3 Element Id
     * @param arg_4 Value
     */
    public synchronized void AssaultPartyLog(int arg_1, int arg_2, int arg_3, int arg_4){
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.configs[0].get("LoggerServer"), Integer.parseInt(this.configs[1].get("LoggerServer")));
        if (!con.open()) {
            System.exit(1);
        }
        
        outMessage = new Message(ASSAULT_PARTY_LOG, arg_1, arg_2, arg_3, arg_4);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

}
