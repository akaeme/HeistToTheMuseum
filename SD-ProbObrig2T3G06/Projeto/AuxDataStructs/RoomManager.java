/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs;


public class RoomManager {
   /**
   *  Boolean flag signaling if room is empty
   *
   *    @serialField emptyRoom
   */
    private boolean emptyRoom;
    
   /**
   *  Boolean flag signaling if room is being stolen
   *
   *    @serialField beingStolen
   */
    private boolean beingStolen;
    
    /**
     * Room Instantiation
     */
    public RoomManager(){
        this.beingStolen = false;
        this.emptyRoom = false;
    }
    
    /**
     * Gets the boolean flag emptyRoom
     * @return <b>true</b> if room is empty, <b>false</b> if not
     */
    public boolean isEmptyRoom() {
        return emptyRoom;
    }

    /**
     * Sets new boolean value to flag emptyRoom
     * @param emptyRoom New boolean value
     */
    public void setEmptyRoom(boolean emptyRoom) {
        this.emptyRoom = emptyRoom;
    }

    /**
     * Gets the boolean flag beingStolen
     * @return <b>true</b> if room is being stolen, <b>false</b> if not
     */
    public boolean isBeingStolen() {
        return beingStolen;
    }
    /**
     * Sets new boolean value to flag beingStolen
     * @param beingStolen New boolean value
     */
    public void setBeingStolen(boolean beingStolen) {
        this.beingStolen = beingStolen;
    }
    
    
}
