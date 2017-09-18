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
public interface AssaultPartyInterface {
    void joinParty(int thiefID, int displacement);
    int crawlIn(int thiefID) throws Exception;
    int crawlOut(int thiefID) throws Exception;
    void waitAllElems();
    void reverseDirection(int thiefID);
    int getDistanceToRoom();
    void resetAndSet(int roomID, int distanceToRoom);
    int getPartyID();
    int getRoomID();
}
