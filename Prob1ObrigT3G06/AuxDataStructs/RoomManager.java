/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class RoomManager {

    private boolean emptyRoom;
    private boolean beingStolen;
    
    public RoomManager(){
        this.beingStolen = false;
        this.emptyRoom = false;
    }
    public boolean isEmptyRoom() {
        return emptyRoom;
    }

    public void setEmptyRoom(boolean emptyRoom) {
        this.emptyRoom = emptyRoom;
    }

    public boolean isBeingStolen() {
        return beingStolen;
    }

    public void setBeingStolen(boolean beingStolen) {
        this.beingStolen = beingStolen;
    }
    
    
}
