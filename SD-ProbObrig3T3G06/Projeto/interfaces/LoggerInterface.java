/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import AuxDataStructs.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LoggerInterface extends Remote {

    /*void OrdinaryThiefLog(int kind, int Tid, int val) throws RemoteException;
    void MasterThiefLog(int val) throws RemoteException;
    void MuseumLog(int kind, int Rid, int val) throws RemoteException;
    void AssaultPartyLog(int kind, int APid, int Eid , int val) throws RemoteException;
    void FinalReport(int total_paint) throws RemoteException;*/
    
    
    /**
     * Ordinary Thief log
     * @param kind State, Situation or Maximum Displacement
     * @param Tid Thief Identification
     * @param val Value
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp OrdinaryThiefLog(int kind, int Tid, int val, VectorTimestamp clock) throws RemoteException;

    
    /**
     * MasterThief Log
     * @param val Value
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp MasterThiefLog(int val, VectorTimestamp clock) throws RemoteException;

    
    /**
     * Museum  log
     * @param kind State, Situation or Maximum Displacement
     * @param Rid Room Identification
     * @param val Value
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp MuseumLog(int kind, int Rid, int val, VectorTimestamp clock) throws RemoteException;

    
    /** AssaultParty Log
     * @param kind kind
     * @param APid AssaultParty Identification
     * @param Eid Element Id
     * @param val Value
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp AssaultPartyLog(int kind, int APid, int Eid, int val, VectorTimestamp clock) throws RemoteException;

    
     /**
     * Final Report
     * @param total_paint total paintings
     * @param clock Vectorial Timestamp clock
     * @throws RemoteException Remote exception
     * @return Vectorial Timestamp
     */
    VectorTimestamp FinalReport(int total_paint, VectorTimestamp clock) throws RemoteException;
    
    public void shutdown() throws RemoteException;
}
