/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;
import static Constants.Constants.*;

public class AssaultPartyManager {
   /**
   *  Thieves Array
   *
   *    @serialField thieves
   */
    private int[] thieves = new int[MAX_PARTY_THIEVES];
   /**
   *  Thieves Number
   *
   *    @serialField thievesNumber
   */
    private int thievesNumber;
    
  /**
   *  AssaultParty Target room
   *
   *    @serialField roomTarget
   */
    private int roomTarget;
    
  /**
   *  Flag AssaultParty in action
   *
   *    @serialField inAction
   */
    private boolean inAction;

    /**
     * AssaultPartyManager Instantiation
     */
    public AssaultPartyManager() {
        this.thievesNumber = 0;
        this.roomTarget = -1;
        this.inAction = false;
    }

    /**
     * Get thieves number
     * @return thieves number
     */
    public int getThievesNumber() {
        return thievesNumber;
    }

    /**
     * Sets / updates the thieves number
     * @param thievesNumber New number of thieves
     */
    public void setThievesNumber(int thievesNumber) {
        this.thievesNumber = thievesNumber;
    }

    /**
     * Gets the targeted room
     * @return room target
     */
    public int getRoomTarget() {
        return roomTarget;
    }
    
    /**
     * Sets the targeted room
     * @param roomTarget Room target
     */
    public void setRoomTarget(int roomTarget) {
        this.roomTarget = roomTarget;
    }

    /**
     * Checks the Flag inAction
     * @return <b>true</b> if assault party in action, <b>false</b> if not
     */
    public boolean isInAction() {
        return inAction;
    }

    /**
     * Sets a new boolean value to the flag inAction
     * @param inAction new <b>boolean</b> value 
     */
    public void setInAction(boolean inAction) {
        this.inAction = inAction;
    }
    /**
     * Gets the thief
     * @param thiefID thief identification
     * @return <b>true</b> if thief found, <b>false</b> if not
     */
    public boolean getThief(int thiefID) {
        for (int i = 0; i < this.thieves.length; i++) {
            if (this.thieves[i] == thiefID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the thief to this thieves array index
     * @param thiefID thief identification
     * @param index thieves array index
     */
    public void setThief(int thiefID, int index) {
        this.thieves[index] = thiefID;
    }
    
    /**
     * Checks if the thief is on the array, available
     * @param thiefID thief identification
     * @return <b>true</b> if he is there, <b>false</b> if not
     */
    public boolean amIHere(int thiefID){
        for(int i=0;i<MAX_PARTY_THIEVES;i++){
            if(this.thieves[i]==thiefID)
                return true;
        }
        return false;
    }
}
