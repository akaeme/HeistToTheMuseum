/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class MasterThiefLogging {

    private int Stat;

    public MasterThiefLogging() {
        Stat = -1;
    }

    public int getStat() {
        return Stat;
    }

    public void setStat(int Stat) {
        this.Stat = Stat;
    }
    
    @Override
    public String toString() {
        return String.format("%4d  ", Stat);
    }
}
