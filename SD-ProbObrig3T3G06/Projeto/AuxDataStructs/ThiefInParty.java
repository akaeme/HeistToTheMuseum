/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;


public class ThiefInParty{
    /**
   *  Thief displacement
   *
   *    @serialField displacement
   */
    private final int displacement;
    
    /**
   *  Thief position
   *
   *    @serialField position
   */
    private int position;
    
     /**
   *  Thief Identification
   *
   *    @serialField id
   */
    private final int id;
    
    /**
   *  Boolean flag signaling if Thief is ready to go
   *
   *    @serialField readyToGo
   */
    private boolean readyToGo;

    /**
     * Thief Instantiation
     * @param displacement Thief displacement
     * @param position Thief position
     * @param thiefID  Thief Identification
     */
    public ThiefInParty(int displacement, int position, int thiefID) {
        this.displacement = displacement;
        this.id = thiefID;
        this.position = position;
        this.readyToGo = false;
    }

    /**
     * Gets Thief position
     * @return Thief position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets new thief position
     * @param position New thief position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the value of boolean flag readyToGo
     * @return <b>true</b> if ready, <b>false</b> if not
     */
    public boolean isReadyToGo() {
        return readyToGo;
    }

    /**
     * Sets new boolean value to flag readyToGo
     * @param readyToGo New boolean value
     */
    public void setReadyToGo(boolean readyToGo) {
        this.readyToGo = readyToGo;
    }

    /**
     * Gets the thief identification
     * @return Thief Identification
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the thief displacement
     * @return Thief displacement
     */
    public int getDisplacement() {
        return displacement;
    }
}
