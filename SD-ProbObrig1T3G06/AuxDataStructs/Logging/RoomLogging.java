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
public class RoomLogging {

    private int DT, NP;

    public int getDT() {
        return DT;
    }

    public void setDT(int DT) {
        this.DT = DT;
    }

    public int getNP() {
        return NP;
    }

    public void setNP(int NP) {
        this.NP = NP;
    }
    
    
    
    @Override
    public String toString() {
        return String.format("%2d %2d", NP, DT);
    }
}
