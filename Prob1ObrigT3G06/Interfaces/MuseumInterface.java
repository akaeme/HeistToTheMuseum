/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public interface MuseumInterface {
    public int getPaintingNumbers(int roomId);
    public int getRoomDistance(int roomId);
    public boolean rollACanvas(int roomID);
}
