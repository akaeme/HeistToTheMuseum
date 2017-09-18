/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

public interface MuseumInterface {
    /**
     * Gets the number of paintings of the given room
     * @param roomId Room identification
     * @return Number of paintings of the given room
     */
    public int getPaintingNumbers(int roomId);
    
    /**
     * Gets the distance to the given room
     * @param roomId Room identification
     * @return Distance to the room
     */
    public int getRoomDistance(int roomId);
    
    /** 
     * Rolls a canvas on the given room
     * @param roomID Room identification
     * @return Boolean flag: <b>true</b> if canvas rolled, <b>false</b> if not
     */
    public boolean rollACanvas(int roomID);
}
