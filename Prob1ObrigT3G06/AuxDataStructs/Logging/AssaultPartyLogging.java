/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;

import java.util.ArrayList;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class AssaultPartyLogging {

    private ArrayList<ElemLogging> elemsInParty = new ArrayList<ElemLogging>();
    private int Rid;

    public ArrayList<ElemLogging> getElemsInParty() {
        return elemsInParty;
    }

    public int getRid() {
        return Rid;
    }

    public void setRid(int Rid) {
        this.Rid = Rid;
    }

    
    public AssaultPartyLogging() {
        Rid = -1;
    }
    
    public void addElem(ElemLogging e){
        this.elemsInParty.add(e);
    }
    
    public void clearElems(){
        this.elemsInParty.clear();
    }
    
    @Override
    public String toString() {
        String s = "";
        for (ElemLogging e : this.elemsInParty) {
            if (e.getPos() != -1 && e.getCv() != -1 && e.getId() != -1 && elemsInParty.size() == 3 && !e.toString().equals("")) {
                //System.out.println("e=" + s);
                s += e + "   ";
            }
        }
        //System.out.println("s= " + s);
        return String.format("%1d    ", Rid) + s;
    }
}
