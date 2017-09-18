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
public class ElemLogging {

    private int Id, Pos, Cv;

    public ElemLogging() {
        Id = -1;
        Pos = -1;
        Cv = -1;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getPos() {
        return Pos;
    }

    public void setPos(int Pos) {
        this.Pos = Pos;
    }

    public int getCv() {
        return Cv;
    }

    public void setCv(int Cv) {
        this.Cv = Cv;
    }
    
    @Override
    public String toString() {
        return String.format("%1d %2d  %1d ", Id, Pos, Cv);
    }
}
