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
public interface ControlCollectionSiteInterface {
    //MasterThief
    int appraiseSit(boolean over);
    void startOperations();
    void sendAssaultParty();
    void takeARest();
    boolean[] collectCanvas();
    int sumUpResults();
    //OrdinaryThief
    void handACanvas(boolean hasPainting, int thiefID);
    int prepareExcursion(int thiefID);
}
