/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;

import static Constants.Constants.*;

/**
 * 
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class Room {
    private int numberOfPaintings;
    private final int distance;
    public Room() {
        this.numberOfPaintings = (int) (Math.random() * (MAX_PAINTINGS + 1 - MIN_PAITINGS)) + MIN_PAITINGS;
        this.distance = (int) (Math.random() * (MAX_ROOM_DISTANCE + 1 - MIN_ROOM_DISTANCE)) + MIN_ROOM_DISTANCE;

    }
    public boolean decrement() {
        if (this.numberOfPaintings > 0) {
            this.numberOfPaintings--;
            return true;
        }
        return false;
    }

    public int getNumberOfPaintings() {
        return numberOfPaintings;
    }

    public int getDistance() {
        return distance;
    }

}
