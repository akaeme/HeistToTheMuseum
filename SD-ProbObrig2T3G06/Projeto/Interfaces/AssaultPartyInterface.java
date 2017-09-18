/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;


public interface AssaultPartyInterface {
      /**
     * Thief joins a party
     * 
     * @param thiefID Thief identification
     * @param displacement Thief displacement
     */
    void joinParty(int thiefID, int displacement);
    
    /**
    * Crawling in
    * 
    * @param thiefID Thief identification
    * @return Distance left to reach the room 
    * @throws Exception thief index error
    */
    int crawlIn(int thiefID) throws Exception;
    
    /**
    * Crawling out
    * 
    * @param thiefID Thief identification
    * @return Distance left to reach the outside 
    * @throws Exception thief index error
    */
    int crawlOut(int thiefID) throws Exception;
    
    /**
    * 
    * Wait all Elements
    */
    void waitAllElems();
    
    /**
     * Reverse direction
     * @param thiefID Thief identification
     */
    void reverseDirection(int thiefID);
    
    /**
     * resetAndSet
     * 
     * @param roomID Room identification
     * @param distanceToRoom Distance to the room
     */    
    void resetAndSet(int roomID, int distanceToRoom);
    
    /**
     * Gets the Party Identification
     * 
     * @return Party identification
     */
    int getPartyID();
    
    /**
     * Gets the Room identification
     * 
     * @return Room identification
     */
    int getRoomID();
}
