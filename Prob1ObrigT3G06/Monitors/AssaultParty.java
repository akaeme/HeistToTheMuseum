/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxDataStructs.ThiefInParty;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import Interfaces.AssaultPartyInterface;
import Interfaces.LoggerInterface;
import java.util.Arrays;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class AssaultParty implements AssaultPartyInterface {

    private int nThieves;
    private int indexToMove;
    private int roomID;
    private int distanceToRoom;
    private ThiefInParty[] thieves;
    private final int partyID;
    private LoggerInterface logger;

    public AssaultParty(int id, LoggerInterface logger) {
        this.partyID = id;
        this.roomID = -1;
        this.thieves = new ThiefInParty[MAX_PARTY_THIEVES];
        this.indexToMove = 0;
        this.roomID = -1;
        this.distanceToRoom = -1;
        this.nThieves = 0;
        this.logger = logger;
    }

    @Override
    public synchronized void joinParty(int thiefID, int displacement) {
        this.thieves[this.nThieves] = new ThiefInParty(displacement, 0, thiefID);
        this.nThieves++;
        if (this.nThieves == MAX_PARTY_THIEVES) {
            notifyAll();
        }
        
        this.logger.OrdinaryThiefLog(S, partyID, 1);
        this.logger.AssaultPartyLog(Id, partyID, thiefID, thiefID);
        this.logger.AssaultPartyLog(Pos, partyID, thiefID, 0);
        this.logger.AssaultPartyLog(Cv, partyID, thiefID, 0);
    }

    @Override
    public synchronized int crawlIn(int thiefID) throws Exception {
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != MAX_PARTY_THIEVES || this.roomID == -1) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        int predictPos[] = null;
        boolean GO = false;
        for (int i = this.thieves[this.indexToMove].getDisplacement(); i > 0; i--) {
            predictPos = this.getArrayPos();
            GO = true;
            predictPos[this.indexToMove] += i;
            if (predictPos[this.indexToMove] >= this.distanceToRoom) {
                predictPos[this.indexToMove] = this.distanceToRoom;
            }
            int[] checkPos = predictPos;
            Arrays.sort(checkPos);
            for (int j = checkPos.length - 1; j >= 1; j--) {
                if (checkPos[j] - checkPos[j - 1] > MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
                    GO = false;
                }
            }
            if (GO) {
                break;
            }
        }
        if (GO) {
            this.thieves[this.indexToMove].setPosition(predictPos[this.indexToMove]);
        }
        int ret = this.indexToMove;
        if (this.indexToMove == 0) {
            this.indexToMove = MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        this.logger.AssaultPartyLog(Pos, this.partyID, thiefID, this.thieves[indexToMove].getPosition());
        return this.distanceToRoom - this.thieves[ret].getPosition();
    }

    @Override
    public synchronized int crawlOut(int thiefID) throws Exception {
        while (thiefID != this.thieves[this.indexToMove].getId() || this.nThieves != MAX_PARTY_THIEVES || this.roomID == -1) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        int predictPos[] = null;
        boolean GO = false;
        for (int i = this.thieves[this.indexToMove].getDisplacement(); i > 0; i--) {
            predictPos = this.getArrayPos();
            GO = true;
            predictPos[this.indexToMove] += i;
            if (predictPos[this.indexToMove] >= this.distanceToRoom) {
                predictPos[this.indexToMove] = this.distanceToRoom;
            }
            int[] checkPos = predictPos;
            Arrays.sort(checkPos);
            for (int j = checkPos.length - 1; j >= 1; j--) {
                if (checkPos[j] - checkPos[j - 1] > MAX_THIEVES_DISTANCE || (checkPos[j] == checkPos[j - 1] && checkPos[j] != 0 && checkPos[j] != this.distanceToRoom)) {
                    GO = false;
                }
            }
            if (GO) {
                break;
            }
        }
        if (GO) {
            this.thieves[this.indexToMove].setPosition(predictPos[this.indexToMove]);
        }
        int ret = this.indexToMove;
        if (this.indexToMove == 0) {
            this.indexToMove = MAX_PARTY_THIEVES - 1;
        } else {
            this.indexToMove -= 1;
        }
        notifyAll();
        this.logger.AssaultPartyLog(Pos, this.partyID, thiefID, this.distanceToRoom - this.thieves[indexToMove].getPosition());
        return this.distanceToRoom - this.thieves[ret].getPosition();
    }

    @Override
    public synchronized void waitAllElems() {
        while (!this.allInDestination()) {
            try {
                if (!this.allInDestination() && this.thieves[indexToMove].getPosition() == this.distanceToRoom) {
                    if (this.indexToMove == 0) {
                        this.indexToMove = MAX_PARTY_THIEVES - 1;
                    } else {
                        this.indexToMove -= 1;
                    }
                    notifyAll();
                }
                wait();
            } catch (Exception e) {
            }
        }
        notifyAll();
    }

    @Override
    public synchronized void reverseDirection(int thiefID) {
        this.setThiefReady(thiefID);

        while (!this.checkAllReady()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        for (ThiefInParty TIP : thieves) {
            if (TIP.getId() == thiefID) {
                TIP.setPosition(0);
            }
        }
        this.indexToMove = MAX_PARTY_THIEVES - 1;
        notifyAll();
    }

    @Override
    public synchronized int getDistanceToRoom() {
        return this.distanceToRoom;
    }

    @Override
    public synchronized void resetAndSet(int roomID, int distanceToRoom) {
        this.nThieves = 0;
        this.roomID = roomID;
        this.distanceToRoom = distanceToRoom;
        this.logger.AssaultPartyLog(RId, this.partyID, -1, this.roomID);
    }

    @Override
    public synchronized int getPartyID() {
        return this.partyID;
    }

    @Override
    public synchronized int getRoomID() {
        return this.roomID;
    }

    public synchronized void setThiefReady(int thiefID) {
        for (ThiefInParty TIP : thieves) {
            if (TIP.getId() == thiefID) {
                TIP.setReadyToGo(true);
            }
        }
    }

    public synchronized boolean checkAllReady() {
        for (ThiefInParty TIP : thieves) {
            if (!TIP.isReadyToGo()) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean allInDestination() {
        for (ThiefInParty TIP : thieves) {
            if (TIP.getPosition() != this.distanceToRoom) {
                return false;
            }
        }
        return true;
    }

    public synchronized int[] getArrayPos() {
        int[] pos = new int[this.thieves.length];
        for (int i = 0; i < this.thieves.length; i++) {
            pos[i] = this.thieves[i].getPosition();
        }
        return pos;
    }

}
