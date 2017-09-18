/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;


public class RoomLogging {
    /**
   *  Room Distance and Number of Paintings
   *
   *    @serialField DT
   *    @serialField NP
   */
    private int DT, NP;
    /**
     * Get Room Distance
     * @return Distance
     */
    public int getDT() {
        return DT;
    }
    /**
     * Set Room Distance
     * @param DT Distance
     */
    public void setDT(int DT) {
        this.DT = DT;
    }
    /**
     * Get Room Number of Paintings
     * @return Number of Paintings in room
     */
    public int getNP() {
        return NP;
    }
    /**
     * Set Room Number of Paintings
     * @param NP Number of Paintings
     */
    public void setNP(int NP) {
        this.NP = NP;
    }
    
    
    
    @Override
    public String toString() {
        return String.format("%2d %2d", NP, DT);
    }
}
