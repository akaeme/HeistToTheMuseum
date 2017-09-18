/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import AuxDataStructs.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MuseumInterface extends Remote{
    /*public int getPaintingNumbers(int roomId) throws RemoteException;
    public int getRoomDistance(int roomId) throws RemoteException;
    public boolean rollACanvas(int roomID) throws RemoteException;*/
    
    
    /**
     * Gets the number of paintings of the given room
     * @param roomId Room identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Number of paintings of the given room
     */
    public VectorTimestamp getPaintingNumbers(int roomId, VectorTimestamp clock) throws RemoteException;
    
    
    /**
     * Gets the distance to the given room
     * @param roomId Room identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Distance to the room
     */
    public VectorTimestamp getRoomDistance(int roomId, VectorTimestamp clock) throws RemoteException;
    
    
    /** 
     * Rolls a canvas on the given room
     * @param roomID Room identification
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Boolean flag: <b>true</b> if canvas rolled, <b>false</b> if not
     */
    public VectorTimestamp rollACanvas(int roomID, VectorTimestamp clock) throws RemoteException;
    public void shutdown() throws RemoteException;
}
