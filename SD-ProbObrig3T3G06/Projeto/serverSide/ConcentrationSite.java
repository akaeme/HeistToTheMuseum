/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverSide;

import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.*;
import interfaces.ConcentrationSiteInterface;
import interfaces.RegisterInterface;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfig;

public class ConcentrationSite implements ConcentrationSiteInterface{
  /**
   *  Thieves Number in ConcentrationSite
   *
   *    @serialField nThieves
   */
    private int nThieves = 0;
    
    private final VectorTimestamp clocks;
  /**
   *  Thieves states
   *
   *    @serialField thiefState
   */
    private final int thiefState[];
    
    private String reghostname ;
    /**
     * ConcentrationSite Instantiation
     * @param reghostname Host name Registry 
     */
    public ConcentrationSite(String reghostname){
        //index 0 para inicializar
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 0);
        this.thiefState = new int[ORDINARY_THIEVES];
        for(int i=0; i<ORDINARY_THIEVES ; i++){
            thiefState[i] = IN_ACTION;
        }
        this.reghostname = reghostname;
    }
    /**
     * Prepares AssaultParty
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp prepareAssaultParty(VectorTimestamp clock) {
        this.clocks.update(clock);
        while (this.nThieves != ORDINARY_THIEVES) {
            try {
                wait(); 
            } catch (InterruptedException e) {
            }
        }
        for(int i =0; i<ORDINARY_THIEVES;i++)
            this.thiefState[i] = NEEDED;
        notifyAll();
        return this.clocks.clone();
    }
    
    /**
     * Thief amINeeded
     * 
     * @param thiefID thief id
     * @param clock Vectorial timestamp clock
     * @return state of the thief
     */
    @Override
    public synchronized VectorTimestamp amINeeded(int thiefID, VectorTimestamp clock) {
        this.clocks.update(clock);
        if(this.thiefState[thiefID] == IN_ACTION){
            this.nThieves++;
        }
        notifyAll();
        while (this.thiefState[thiefID]!= NOT_NEEDED && this.thiefState[thiefID]!= NEEDED) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        if(this.thiefState[thiefID] == NEEDED){
            this.nThieves--;
            this.thiefState[thiefID] = IN_ACTION;        
        }
        this.clocks.setArg_integer(this.thiefState[thiefID]);
        return this.clocks.clone();
    }
    
    /**
     * Heist Over
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp heistOver(VectorTimestamp clock) {
        this.clocks.update(clock);
        for(int i =0; i<ORDINARY_THIEVES;i++){
            this.thiefState[i] = NOT_NEEDED;
        }
        notifyAll();
        return this.clocks.clone();
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
        String nameEntryObject = RegistryConfig.REGISTRY_CONCENTRATION_SITE_NAME;
        
        
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
        System.out.println("ConcentrationSite shutdown.");
    }
}
