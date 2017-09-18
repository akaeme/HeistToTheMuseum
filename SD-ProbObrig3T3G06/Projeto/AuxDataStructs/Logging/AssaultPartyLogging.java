/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs.Logging;

import java.util.ArrayList;


public class AssaultPartyLogging {

    /**
   *  Elements in party
   *
   *    @serialField elemsInParty
   */
    private ArrayList<ElemLogging> elemsInParty = new ArrayList<ElemLogging>();
    
    /**
   *  Room id
   *
   *    @serialField Rid
   */
    private int Rid;

    /**
     * Get Elements in party
     * @return Elements in party
     */
    public ArrayList<ElemLogging> getElemsInParty() {
        return elemsInParty;
    }
    /**
     * Get room id
     * @return room id
     */
    public int getRid() {
        return Rid;
    }
    /**
     * Set room id
     * @param Rid room id
     */
    public void setRid(int Rid) {
        this.Rid = Rid;
    }

    /**
     * Assault Party Logging
     */
    public AssaultPartyLogging() {
        Rid = -1;
    }
    
    /**
     * Adding Element 
     * @param e element
     */
    public void addElem(ElemLogging e){
        this.elemsInParty.add(e);
    }
    
    /**
     * Clear elements
     */
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
