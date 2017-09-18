/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

public interface LoggerInterface {
    /**
     * Ordinary Thief log
     * @param kind State, Situation or Maximum Displacement
     * @param Tid Thief Identification
     * @param val Value
     */
    void OrdinaryThiefLog(int kind, int Tid, int val);
    
    /**
     * MasterThief Log
     * @param val Value
     */
    void MasterThiefLog(int val);
    //void MuseumLog(int kind, int Rid, int val);
    
    /** AssaultParty Log
     * @param kind kind
     * @param APid AssaultParty Identification
     * @param Eid Element Id
     * @param val Value
     */
    void AssaultPartyLog(int kind, int APid, int Eid , int val);
    
     /**
     * Final Report
     * @param total_paint total paintings
     */
    void FinalReport(int total_paint);
}
