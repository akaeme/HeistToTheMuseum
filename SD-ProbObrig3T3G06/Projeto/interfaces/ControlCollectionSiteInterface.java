/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import AuxDataStructs.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ControlCollectionSiteInterface extends Remote{
/*    
//MasterThief
    int appraiseSit(boolean over) throws RemoteException;
    void startOperations() throws RemoteException;
    void sendAssaultParty() throws RemoteException;
    void takeARest() throws RemoteException;
    boolean[] collectCanvas() throws RemoteException;
    int sumUpResults() throws RemoteException;
    //OrdinaryThief
    void handACanvas(boolean hasPainting, int thiefID) throws RemoteException;
    int prepareExcursion(int thiefID) throws RemoteException;*/
 
    //MasterThief
    
     /**
     * Appraises the Situation 
     * @param over <b>boolean flag</b> if heist is over or not
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return TO_PRESENTING_THE_REPORT, TO_ASSEMBLE_A_GROUP or TO_WAIT_FOR_GROUP 
     */
    VectorTimestamp appraiseSit(boolean over, VectorTimestamp clock) throws RemoteException;
    
    /**
     * Starts the operations
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
    */
    VectorTimestamp startOperations(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Sends the AssaultParty to a new Heist
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp sendAssaultParty(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Master thief goes to sleep
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp takeARest(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Master Thief collects the canvas of each thief back from Heist
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return boolean data structure signaling which thieves had a canvas
     */
    VectorTimestamp collectCanvas(VectorTimestamp clock) throws RemoteException;
    
    /**
     * Sums up all the paintings
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return total sum of paintings
     */
    VectorTimestamp sumUpResults(VectorTimestamp clock) throws RemoteException;
    //OrdinaryThief
    /**
     * Each thief back from heist hands the canvas to Master Thief
     * @param hasPainting <b>true</b> if thief has a canvas to deliver, <b>false</b> if not
     * @param thiefID thief id
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp handACanvas(boolean hasPainting, int thiefID, VectorTimestamp clock) throws RemoteException;
    
    /**
     * Prepares an Excursion
     * @param thiefID thief id
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return party that thief joined/belongs
     */
    VectorTimestamp prepareExcursion(int thiefID, VectorTimestamp clock) throws RemoteException;
    public void shutdown() throws RemoteException;
}
