/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuxDataStructs;

/**
 * 
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class ThiefInParty{

    private final int displacement;
    private int position;
    private final int id;
    private boolean readyToGo;

    public ThiefInParty(int displacement, int position, int thiefID) {
        this.displacement = displacement;
        this.id = thiefID;
        this.position = position;
        this.readyToGo = false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isReadyToGo() {
        return readyToGo;
    }

    public void setReadyToGo(boolean readyToGo) {
        this.readyToGo = readyToGo;
    }

    public int getId() {
        return id;
    }

    public int getDisplacement() {
        return displacement;
    }
}
