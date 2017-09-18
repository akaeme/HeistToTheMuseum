/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;
import static Constants.Constants.*;
/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class AssaultPartyManager {

    private int[] thieves = new int[MAX_PARTY_THIEVES];
    private int thievesNumber;
    private int roomTarget;
    private boolean inAction;

    public AssaultPartyManager() {
        this.thievesNumber = 0;
        this.roomTarget = -1;
        this.inAction = false;
    }

    public int getThievesNumber() {
        return thievesNumber;
    }

    public void setThievesNumber(int thievesNumber) {
        this.thievesNumber = thievesNumber;
    }

    public int getRoomTarget() {
        return roomTarget;
    }

    public void setRoomTarget(int roomTarget) {
        this.roomTarget = roomTarget;
    }

    public boolean isInAction() {
        return inAction;
    }

    public void setInAction(boolean inAction) {
        this.inAction = inAction;
    }

    public boolean getThief(int thiefID) {
        for (int i = 0; i < this.thieves.length; i++) {
            if (this.thieves[i] == thiefID) {
                return true;
            }
        }
        return false;
    }

    public void setThief(int thiefID, int index) {
        this.thieves[index] = thiefID;
    }
    public boolean amIHere(int thiefID){
        for(int i=0;i<MAX_PARTY_THIEVES;i++){
            if(this.thieves[i]==thiefID)
                return true;
        }
        return false;
    }
}
