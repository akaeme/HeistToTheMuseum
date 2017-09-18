package ServerSide;

import AuxDataStructs.Room;
import ComInf.Message;
import static ComInf.Message.*;
import Communication.ClientCom;
import Constants.Constants;
import static Constants.LoggerConstants.*;
//import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;
import java.util.HashMap;


public class Museum implements MuseumInterface {
  /**
   *  TNumber of Rooms in the Museum
   *
   *    @serialField nRooms
   */
    private final int nRooms;
    //private LoggerInterface logger;
  /**
   *  Rooms Aux Data Structure to manage all rooms info
   *
   *    @serialField rooms
   */
    Room[] rooms;
    
    private final Constants constants;
    private final HashMap<String, String>[] configs;
    /**
     * Museum Instantiation
     * @param constants Constants
     * @param configs Configurations
     */
    public Museum(Constants constants, HashMap<String, String>[] configs){
        this.configs = configs;
        this.constants = constants;
        this.nRooms=this.constants.ROOMS_NUMBER;
        this.rooms = new Room[this.constants.ROOMS_NUMBER];
        //this.logger = logger;
        for(int i=0;i<nRooms;i++){
            Room room = new Room();
            this.rooms[i] = room;
            this.MuseumLog(DT, i, room.getDistance());
            this.MuseumLog(NP, i, room.getNumberOfPaintings());
        }     
    }
    /**
     * Gets the number of paintings of the given room
     * @param roomId Room identification
     * @return Number of paintings of the given room
     */
    @Override
    public int getPaintingNumbers(int roomId){
        return rooms[roomId].getNumberOfPaintings();
    }
    /**
     * Gets the distance to the given room
     * @param roomId Room identification
     * @return Distance to the room
     */
    @Override
    public int getRoomDistance(int roomId){
        return rooms[roomId].getDistance();
    }
    /**
     * Rolls a canvas on the given room
     * @param roomID Room identification
     * @return Boolean flag: <b>true</b> if canvas rolled, <b>false</b> if not
     */
    @Override
    public synchronized boolean rollACanvas(int roomID) {
        boolean ret= rooms[roomID].decrement();
        this.MuseumLog(NP, roomID, this.rooms[roomID].getNumberOfPaintings());
        return ret;
    }

    public synchronized void MuseumLog(int arg_1, int arg_2, int arg_3){
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.configs[0].get("LoggerServer"), Integer.parseInt(this.configs[1].get("LoggerServer")));
        if (!con.open()) {
            System.exit(1);
        }
        
        
        outMessage = new Message(MUSEUM_LOG, arg_1, arg_2, arg_3);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
