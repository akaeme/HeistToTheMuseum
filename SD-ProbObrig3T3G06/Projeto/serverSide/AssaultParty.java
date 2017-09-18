/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import AuxDataStructs.ThiefInParty;
import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import interfaces.AssaultPartyInterface;
import interfaces.LoggerInterface;
import interfaces.RegisterInterface;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
//import Interfaces.LoggerInterface;
import java.util.Arrays;
import java.util.logging.Level;
import registry.RegistryConfig;

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
    LoggerInterface logger;
    private final VectorTimestamp clocks;
    
    private String reghostname ;
    //private LoggerInterface logger;
    /**
     * AssaultParty Instantiation
     * 
     * @param id Party identification
     * @param logger Logger Interface 
     * @param reghostname Host name Registry 
     */
    public AssaultParty(int id, LoggerInterface logger, String reghostname) {
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 0);
        this.partyID = id;
        this.roomID = -1;
        this.thieves = new ThiefInParty[MAX_PARTY_THIEVES];
        this.indexToMove = 0;
        this.roomID = -1;
        this.distanceToRoom = -1;
        this.nThieves = 0;
        this.logger = logger;
        this.reghostname = reghostname;
    }

    /**
     * Thief joins a party
     * 
     * @param thiefID Thief identification
     * @param displacement Thief displacement
     * @param clock Vectorial timestamp clock
     * @return  Vectorial timestamp clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    @Override
    public synchronized VectorTimestamp joinParty(int thiefID, int displacement, VectorTimestamp clock) throws RemoteException {
        this.clocks.update(clock);
        this.thieves[this.nThieves] = new ThiefInParty(displacement, 0, thiefID);
        this.nThieves++;
        if (this.nThieves == MAX_PARTY_THIEVES) {
            notifyAll();
        }
       this.logger.OrdinaryThiefLog(S, partyID, 1, this.clocks.clone());
       this.logger.AssaultPartyLog(Id, partyID, thiefID, thiefID,this.clocks.clone());
       this.logger.AssaultPartyLog(Pos, partyID, thiefID, 0,this.clocks.clone());
       this.logger.AssaultPartyLog(Cv, partyID, thiefID, 0,this.clocks.clone());
       return this.clocks.clone();
    }
/**
 * Crawling in
 * 
 * @param thiefID Thief identification
 * @param clock Vectorial timestamp clock
 * @return Distance left to reach the room 
 * @throws Exception thief index error
 */
    @Override
    public synchronized VectorTimestamp crawlIn(int thiefID, VectorTimestamp clock) throws Exception {
          this.clocks.update(clock);
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != MAX_PARTY_THIEVES || this.roomID == -1) {
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
                if (checkPos[j] - checkPos[j - 1] >MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
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
            this.indexToMove = MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        //this.AssaultPartyLog(Pos, this.partyID, thiefID, this.thieves[indexToMove].getPosition());
        this.logger.AssaultPartyLog(Pos, this.partyID, thiefID, this.thieves[indexToMove].getPosition(), this.clocks.clone());
        this.clocks.setArg_integer(this.distanceToRoom - this.thieves[ret].getPosition());
        return this.clocks.clone();
    }
/**
 * Crawling out
 * 
 * @param thiefID Thief identification
 * @param clock Vectorial timestamp clock
 * @return Distance left to reach the outside 
 * @throws Exception thief index error
 */
    @Override
    public synchronized VectorTimestamp crawlOut(int thiefID, VectorTimestamp clock) throws Exception {
         this.clocks.update(clock);
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != MAX_PARTY_THIEVES || this.roomID == -1) {
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
                if (checkPos[j] - checkPos[j - 1] > MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
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
            this.indexToMove = MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        //this.AssaultPartyLog(Pos, this.partyID, thiefID, this.distanceToRoom - this.thieves[indexToMove].getPosition());
        this.logger.AssaultPartyLog(Pos, this.partyID, thiefID, this.distanceToRoom - this.thieves[indexToMove].getPosition(), this.clocks.clone());

        this.clocks.setArg_integer(this.distanceToRoom - this.thieves[ret].getPosition());
        return this.clocks.clone();
    }

    /**
     * 
     * Wait all Elements
     * @param clock Vectorial timestamp clock
     * @return  Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp waitAllElems(VectorTimestamp clock) {
        this.clocks.update(clock);
        while (!this.allInDestination()) {
            try {
                if (!this.allInDestination() && this.thieves[indexToMove].getPosition() == this.distanceToRoom) {
                    if (this.indexToMove == 0) {
                        this.indexToMove = MAX_PARTY_THIEVES - 1;
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
        return this.clocks.clone();
    }

    /**
     * Reverse direction
     * @param thiefID Thief identification
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp reverseDirection(int thiefID, VectorTimestamp clock) {
        this.clocks.update(clock);
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
        this.indexToMove = MAX_PARTY_THIEVES - 1;
        notifyAll();
        return this.clocks.clone();
    }
    /**
     * Gets the distance to Room
     * 
     * @return Distance to the room
     */
    @Override
    public synchronized VectorTimestamp getDistanceToRoom(VectorTimestamp clock) {
        this.clocks.update(clock);
        this.clocks.setArg_integer(this.distanceToRoom);
        return this.clocks.clone();
    }

    /**
     * resetAndSet
     * 
     * @param roomID Room identification
     * @param distanceToRoom Distance to the room
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    @Override
    public synchronized VectorTimestamp resetAndSet(int roomID, int distanceToRoom, VectorTimestamp clock) throws RemoteException {
        this.clocks.update(clock);
        this.nThieves = 0;
        this.roomID = roomID;
        this.distanceToRoom = distanceToRoom;
       this.logger.AssaultPartyLog(RId, this.partyID, -1, this.roomID, this.clocks.clone());
       return this.clocks.clone();
    }

    
    /**
     * Gets the Party Identification
     * 
     * @param clock Vectorial timestamp clock
     * @return Party identification
     */
    @Override
    public synchronized VectorTimestamp getPartyID(VectorTimestamp clock) {
        this.clocks.update(clock);
         this.clocks.setArg_integer( this.partyID);
        return this.clocks.clone();
    }

    /**
     * Gets the Room identification
     * 
     * @param clock Vectorial timestamp clock
     * @return Room identification
     */
    @Override
    public synchronized VectorTimestamp getRoomID(VectorTimestamp clock) {
        this.clocks.update(clock);
         this.clocks.setArg_integer( this.roomID);
        return this.clocks.clone();
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
    
    @Override
    public void shutdown() throws RemoteException {
        Registry registry = null;
        RegisterInterface reg = null;
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject =null;
        if(this.partyID == 0){
            nameEntryObject = RegistryConfig.REGISTRY_ASSAULT_PARTY0_NAME;
        }else{
            nameEntryObject = RegistryConfig.REGISTRY_ASSAULT_PARTY1_NAME;
        }
        
        try {
            reg = (RegisterInterface) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Logger registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Logger not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Assaultparty shutdown.");
    }
    /**
     * Ordinary Thief log
     * @param arg_1 State, Situation or Maximum Displacement
     * @param arg_2 Thief Identification
     * @param arg_3 Value
     */
    /*public synchronized void OrdinaryThiefLog(int arg_1, int arg_2, int arg_3){
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
    }*/
    /**
     * AssaultParty Log
     * @param arg_1 Room Id, Member Id, Position, Carrying a Canvas
     * @param arg_2 AssaultParty Identification
     * @param arg_3 Element Id
     * @param arg_4 Value
     */
    /*public synchronized void AssaultPartyLog(int arg_1, int arg_2, int arg_3, int arg_4){
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
    }*/

}
