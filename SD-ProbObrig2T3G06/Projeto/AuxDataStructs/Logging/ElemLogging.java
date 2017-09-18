/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;


public class ElemLogging {
   /**
   *  Element id, position, carrying canvas
   *
   *    @serialField Id
   *    @serialField Pos
   *    @serialField Cv
   */
    private int Id, Pos, Cv;
    /**
     * Element Logging Instantiation
     */
    public ElemLogging() {
        Id = -1;
        Pos = -1;
        Cv = -1;
    }
    /**
     * Get Elem id
     * @return Identification
     */
    public int getId() {
        return Id;
    }
    /**
     * Set Elem id
     * @param Id Identification
     */
    public void setId(int Id) {
        this.Id = Id;
    }
    /**
     * Get Elem Position
     * @return Position
     */
    public int getPos() {
        return Pos;
    }
    /**
     * Set Elem Position
     * @param Pos Position
     */
    public void setPos(int Pos) {
        this.Pos = Pos;
    }
    /**
     * Get Elem Carrying Canvas
     * @return Carrying Canvas
     */
    public int getCv() {
        return Cv;
    }
    /**
     * Set Element Carrying Canvas
     * @param Cv Carrying Canvas
     */
    public void setCv(int Cv) {
        this.Cv = Cv;
    }
    
    @Override
    public String toString() {
        return String.format("%1d %2d  %1d ", Id, Pos, Cv);
    }
}
