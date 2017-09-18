/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;

import static Constants.Constants.*;

public class Room {
  /**
   *  Number of Paintings at room
   *
   *    @serialField numberOfPaintings
   */
    private int numberOfPaintings;
  /**
   *  Room distance
   *
   *    @serialField distance
   */
    private final int distance;
    
   /**
    * Room Instantiation
    */
    public Room() {
        this.numberOfPaintings = (int) (Math.random() * (MAX_PAINTINGS + 1 - MIN_PAITINGS)) + MIN_PAITINGS;
        this.distance = (int) (Math.random() * (MAX_ROOM_DISTANCE + 1 - MIN_ROOM_DISTANCE)) + MIN_ROOM_DISTANCE;

    }
    /**
     * Decrements the number of paintings on room
     * @return <b>true</b> if there were any room to steal, <b>false</b> if not
     */
    public boolean decrement() {
        if (this.numberOfPaintings > 0) {
            this.numberOfPaintings--;
            return true;
        }
        return false;
    }
    /**
     * Number of paintings
     * @return Number of paintings at room
     */
    public int getNumberOfPaintings() {
        return numberOfPaintings;
    }

    /**
     * Gets the room distance
     * @return Distance to room
     */
    public int getDistance() {
        return distance;
    }

}
