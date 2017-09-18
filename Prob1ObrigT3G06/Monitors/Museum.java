package Monitors;

import AuxDataStructs.Room;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import Interfaces.LoggerInterface;
import Interfaces.MuseumInterface;

/**
 * 
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class Museum implements MuseumInterface {
    private final int nRooms;
    Room[] rooms;
    public Museum(LoggerInterface logger){
        this.nRooms=ROOMS_NUMBER;
        this.rooms = new Room[ROOMS_NUMBER];
        
        for(int i=0;i<nRooms;i++){
            Room room = new Room();
            this.rooms[i] = room;
            logger.MuseumLog(DT, i, room.getDistance());
            logger.MuseumLog(NP, i, room.getNumberOfPaintings());
        }     
    }
    @Override
    public int getPaintingNumbers(int roomId){
        return rooms[roomId].getNumberOfPaintings();
    }
    @Override
    public int getRoomDistance(int roomId){
        return rooms[roomId].getDistance();
    }
    @Override
    public synchronized boolean rollACanvas(int roomID) {
        boolean ret= rooms[roomID].decrement();
        return ret;
    }
}
