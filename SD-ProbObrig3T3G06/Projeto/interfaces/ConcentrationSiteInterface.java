/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import AuxDataStructs.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ConcentrationSiteInterface extends Remote {

    /*public  void prepareAssaultParty() throws RemoteException;
    public  int amINeeded(int thiefID) throws RemoteException;
    public void heistOver() throws RemoteException;*/
    
     /**
     * Prepares AssaultParty\
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    public VectorTimestamp prepareAssaultParty(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Thief amINeeded
     * 
     * @param thiefID thief id
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    public VectorTimestamp amINeeded(int thiefID, VectorTimestamp clock) throws RemoteException;

    
     /**
     * Heist Over
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    public VectorTimestamp heistOver(VectorTimestamp clock) throws RemoteException;
    
    public void shutdown() throws RemoteException;
}
