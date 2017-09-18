/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

public interface ConcentrationSiteInterface {
    /**
     * Prepares AssaultParty.
     */
    public  void prepareAssaultParty();
    /**
     * Thief amINeeded.
     * 
     * @param thiefID thief id
     * @return state of the thief
     */
    public  int amINeeded(int thiefID);
    /**
     * Heist Over.
     */
    public void heistOver();
}
