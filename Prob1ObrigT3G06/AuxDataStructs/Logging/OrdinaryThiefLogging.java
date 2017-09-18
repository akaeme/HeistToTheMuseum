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
public class OrdinaryThiefLogging {

    private int Stat, S, MD;

    public OrdinaryThiefLogging() {
        Stat = -1;
        S = -1;
        MD = -1;
    }

    public int getStat() {
        return Stat;
    }

    public void setStat(int Stat) {
        this.Stat = Stat;
    }

    public int getS() {
        return S;
    }

    public void setS(int S) {
        this.S = S;
    }

    public int getMD() {
        return MD;
    }

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
