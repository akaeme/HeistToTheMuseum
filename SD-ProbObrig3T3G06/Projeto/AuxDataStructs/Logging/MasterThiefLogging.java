/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;

public class MasterThiefLogging {
    /**
   *  State of Master Thief
   *
   *    @serialField Stat
   */
    private int Stat;

    /**
     * Master Thief Logging
     */
    public MasterThiefLogging() {
        Stat = -1;
    }
    /**
     * Get Master Thief State
     * @return State
     */
    public int getStat() {
        return Stat;
    }

    /**
     * Set Master Thief State
     * @param Stat State
     */
    public void setStat(int Stat) {
        this.Stat = Stat;
    }
    
    @Override
    public String toString() {
        return String.format("%4d  ", Stat);
    }
}
