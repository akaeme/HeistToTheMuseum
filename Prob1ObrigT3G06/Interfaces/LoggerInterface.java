/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public interface LoggerInterface {
    void OrdinaryThiefLog(int kind, int Tid, int val);
    void MasterThiefLog(int val);
    void MuseumLog(int kind, int Rid, int val);
    void AssaultPartyLog(int kind, int APid, int Eid , int val);
    void FinalReport(int total_paint);
}
