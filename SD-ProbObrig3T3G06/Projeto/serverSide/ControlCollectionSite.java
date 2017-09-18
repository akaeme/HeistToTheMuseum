/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import AuxDataStructs.AssaultPartyManager;
import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.ASSAULT_PARTIES_NUMBER;
import static Constants.Constants.*;
import interfaces.ControlCollectionSiteInterface;
import interfaces.RegisterInterface;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfig;

public class ControlCollectionSite implements ControlCollectionSiteInterface {
  /**
   *  Sum of Paintings
   *
   *    @serialField sum
   */
    private int sum;
    
  /**
   *  Assault Party Manager Aux Structure
   *
   *    @serialField manager
   */
    private final AssaultPartyManager[] manager;
  /**
   *  Number of paintings returned from each thief
   *
   *    @serialField numberOfPaintings
   */
    int[] numberOfPaintings;
    
  /**
   *  Signal how many thieves already came from heist
   *
   *    @serialField backFromHeist
   */
    private int backFromHeist;
    
    private final VectorTimestamp clocks;
    
    private String reghostname ;
    
    public ControlCollectionSite(String reghostname){
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 0);
        this.backFromHeist = 0;
        this.sum = 0;
        this.manager = new AssaultPartyManager[ASSAULT_PARTIES_NUMBER];
        this.numberOfPaintings = new int[ASSAULT_PARTIES_NUMBER];
        this.reghostname = reghostname;
    }
    
    
    /**
     * Appraises the Situation 
     * @param over <b>boolean flag</b> if heist is over or not
     * @param clock Vectorial timestamp clock
     * @return TO_PRESENTING_THE_REPORT, TO_ASSEMBLE_A_GROUP or TO_WAIT_FOR_GROUP 
     */
    @Override
    public synchronized VectorTimestamp appraiseSit(boolean over, VectorTimestamp clock) {
        this.clocks.update(clock);
        if(over){
            this.clocks.setArg_integer(TO_PRESENTING_THE_REPORT);
            return this.clocks.clone();
        }
        if(!onAction()){
            this.clocks.setArg_integer(TO_ASSEMBLE_A_GROUP);
            return this.clocks.clone();
        }
        else if(onAction()){
            this.clocks.setArg_integer(TO_WAIT_FOR_GROUP);
            return this.clocks.clone();
        }
        else {
             this.clocks.setArg_integer(0);
            return this.clocks.clone();
        }
    }
    /**
     * Starts the operations
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp startOperations(VectorTimestamp clock) {
        this.clocks.update(clock);
        for(int i=0;i<ASSAULT_PARTIES_NUMBER;i++){
            manager[i] = new AssaultPartyManager();
        }
        return this.clocks.clone();
    }
    /**
     * Sends the AssaultParty to a new Heist
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp sendAssaultParty(VectorTimestamp clock) {
        this.clocks.update(clock);
        while (this.groupsFull()!=-1) {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
        backFromHeist = 0;
        numberOfPaintings = new int[ASSAULT_PARTIES_NUMBER];
        for(AssaultPartyManager apm : manager){
            apm.setInAction(true);
            
        }
        notifyAll();
        return this.clocks.clone();
    }
    /**
     * Master thief goes to sleep
     */
    @Override
    public synchronized VectorTimestamp takeARest(VectorTimestamp clock) {
         this.clocks.update(clock);
        while(this.backFromHeist !=ORDINARY_THIEVES){
            try{
                wait();
            }catch(InterruptedException e){
                
            }
        }
        return this.clocks.clone();
    }
    /**
     * Master Thief collects the canvas of each thief back from Heist
     * 
     * @param clock Vectorial timestamp clock
     * @return boolean data structure signaling which thieves had a canvas
     */
    @Override
    public synchronized VectorTimestamp collectCanvas(VectorTimestamp clock) {
        this.clocks.update(clock);
        boolean signaling[]=new boolean[ASSAULT_PARTIES_NUMBER];
        for(int i=0;i<ASSAULT_PARTIES_NUMBER;i++){
            this.setSum(this.getSum()+this.numberOfPaintings[i]);
            if(this.numberOfPaintings[i] != MAX_PARTY_THIEVES){
                signaling[i] = true;
            }
        }
        for(int i =0 ;i<this.manager.length;i++){
            this.manager[i] = new AssaultPartyManager();
        }
        this.clocks.setArg_bool_array(signaling);
        return this.clocks.clone();
    }
    /**
     * Sums up all the paintings
     * @param clock Vectorial timestamp clock
     * @return total sum of paintings
     */
    @Override
    public synchronized VectorTimestamp sumUpResults(VectorTimestamp clock) {
         this.clocks.update(clock);
        this.clocks.setArg_integer(this.getSum());
        return this.clocks.clone();
    }
    /**
     * Each thief back from heist hands the canvas to Master Thief
     * @param busyHands <b>true</b> if thief has a canvas to deliver, <b>false</b> if not
     * @param thiefID thief id
     * @param clock Vectorial timestamp clock
     * @return  Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp handACanvas(boolean busyHands, int thiefID,VectorTimestamp clock) {
        this.clocks.update(clock);
        this.setArrived(this.backFromHeist+1);
        if(busyHands){
            int index = this.getPartyIndex(thiefID);
            this.numberOfPaintings[index]++;
        }
        if(this.getThievesBackFromHeist()==6)
           notifyAll();
       return this.clocks.clone();
    }
    /**
     * Prepares an Excursion
     * @param thiefID thief id
     * @param clock Vectorial timestamp clock
     * @return party that thief joined/belongs
     */
    @Override
    public synchronized VectorTimestamp prepareExcursion(int thiefID, VectorTimestamp clock) {
        this.clocks.update(clock);
        int toRet = joinParty(thiefID);
        notifyAll();
        while (!this.manager[toRet].isInAction()) {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
        this.clocks.setArg_integer(toRet);
        return this.clocks.clone();
    }
    /**
     * Gets the party identification of the given thief
     * @param thiefID thief identification
     * @return Party identification where given thief belongs
     */
    public int getPartyIndex(int thiefID){
        for(int i = 0 ; i<manager.length;i++){
            if(this.manager[i].amIHere(thiefID))
                return i;
        }
        return -1;
    }
    /**
     * Thief joins a party
     * @param thiefID thief identification
     * @return The index of the party joined if group found (groupsFull)
     */
    public synchronized int joinParty(int thiefID){
        int index = groupsFull();
        if(index == -1)
            return -1;
        manager[index].setThief(thiefID, manager[index].getThievesNumber());
        manager[index].setThievesNumber(manager[index].getThievesNumber()+1);
        return index;
    }
    /**
     * Checks the groups that are full or not
     * @return First party not full, or -1
     */
    public int groupsFull(){
        for(int i = 0; i< this.manager.length ; i++){
            if(this.manager[i].getThievesNumber() != MAX_PARTY_THIEVES)
                return i;
        }
        return -1;
    }
    /**
     * AssaultParty on action
     * @return <b>true</b> if party in action, <b>false</b> if not
     */
    public synchronized boolean onAction(){
        for(AssaultPartyManager apm : manager){
           if(apm.isInAction())
               return true;
        }
        return false;
    }
    /**
     * Gets the total sum of paintings
     * @return Total paintings number
     */   
    public int getSum() {
        return sum;
    }
    /**
     * Sets a new total number of paintings
     * @param sum total sum of paintings
     */
    public void setSum(int sum) {
        this.sum = sum;
    }
    /**
     * Gets the Thieves back from Heist
     * @return Number of thieves back from Heist
     */
    public int getThievesBackFromHeist() {
        return backFromHeist;
    }
    /**
     * Set as Arrived
     * @param arrived <b>true</b> if arrived, <b>false</b> if not
     */
    public void setArrived(int arrived) {
        this.backFromHeist = arrived;
    }
    @Override
    public void shutdown() throws RemoteException {
        Registry registry = null;
        RegisterInterface reg = null;
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_COLLECTION_SITE_NAME;
        
        
        try {
            reg = (RegisterInterface) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Logger registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Logger not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ControLCollection shutdown.");
    }
}
