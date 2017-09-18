/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;


public class OrdinaryThiefLogging {
    /**
   *  Ordinary Thief State, Situation, Maximum Displacement
   *
   *    @serialField museum
   */
    private int Stat, S, MD;
    /**
     * Ordinary Thief Logging
     */
    public OrdinaryThiefLogging() {
        Stat = -1;
        S = -1;
        MD = -1;
    }
    /**
     * Get Ordinary Thief State
     * @return State
     */
    public int getStat() {
        return Stat;
    }
       /**
     * Set Ordinary Thief State
     * @param Stat State
     */ 
    public void setStat(int Stat) {
        this.Stat = Stat;
    }
    /**
     * Get Ordinary Thief Situation
     * @return Situation
     */
    public int getS() {
        return S;
    }
    /**
     * Set Ordinary Thief Situation
     * @param S Situation
     */
    public void setS(int S) {
        this.S = S;
    }
    /**
     * Get Ordinary Thief Maximum Displacement
     * @return Maximum Displacement
     */
    public int getMD() {
        return MD;
    }
    /**
     * Set Ordinary Thief Maximum Displacement
     * @param MD Maximum Displacement
     */
    public void setMD(int MD) {
        this.MD = MD;
    }

    @Override
    public String toString() {
        String str = "";
        if (S == 0) {
            str = "W";
        } else {
            str = "P";
        }
        return String.format("%4d %1s %2d", Stat, str, MD);
    }
}
