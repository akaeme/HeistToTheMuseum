/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import AuxDataStructs.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface AssaultPartyInterface extends Remote{
    /*void joinParty(int thiefID, int displacement) throws RemoteException;
    int crawlIn(int thiefID) throws Exception, RemoteException;
    int crawlOut(int thiefID) throws Exception, RemoteException;
    void waitAllElems() throws RemoteException;
    void reverseDirection(int thiefID) throws RemoteException;
    int getDistanceToRoom() throws RemoteException;
    void resetAndSet(int roomID, int distanceToRoom) throws RemoteException;
    int getPartyID() throws RemoteException;
    int getRoomID() throws RemoteException;*/
    
    /**
     * Thief joins a party
     * 
     * @param thiefID Thief identification
     * @param displacement Thief displacement
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp joinParty(int thiefID, int displacement, VectorTimestamp clock) throws RemoteException;
    
   /**
    * Crawling in
    * 
    * @param thiefID Thief identification
    * @return Distance left to reach the room 
    * @throws Exception thief index error
    * @param clock Vectorial Timestamp clock
    * @throws RemoteException Remote exception
    */
    VectorTimestamp crawlIn(int thiefID, VectorTimestamp clock) throws Exception, RemoteException;
    
    /**
    * Crawling out
    * 
    * @param thiefID Thief identification
    * @return Distance left to reach the outside
    * @param clock Vectorial Timestamp clock
    * @throws RemoteException Remote exception
    * @throws Exception thief index error
    */
    VectorTimestamp crawlOut(int thiefID, VectorTimestamp clock) throws Exception, RemoteException;
    
    /**
    * 
    * Wait all Elements
    * @param clock Vectorial Timestamp clock
    * @throws RemoteException Remote exception
    * @return Vectorial Timestamp
    */
    VectorTimestamp waitAllElems(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Reverse direction
     * @param thiefID Thief identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp reverseDirection(int thiefID,VectorTimestamp clock) throws RemoteException;
    
    /**
     * Gets the Distance to Room
     * 
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Distance to room
     */
    VectorTimestamp getDistanceToRoom(VectorTimestamp clock) throws RemoteException;
    
    /**
     * resetAndSet
     * 
     * @param roomID Room identification
     * @param distanceToRoom Distance to the room
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */ 
    VectorTimestamp resetAndSet(int roomID, int distanceToRoom, VectorTimestamp clock) throws RemoteException;
    
    /**
     * Gets the Party identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Party identification
     */
    VectorTimestamp getPartyID(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Gets the Room identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Room identification
     */
    VectorTimestamp getRoomID(VectorTimestamp clock) throws RemoteException;
    public void shutdown() throws RemoteException;
}
