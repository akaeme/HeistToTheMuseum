/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import static Constants.Constants.*;
import interfaces.AssaultPartyInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ControlCollectionSiteInterface;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static registry.RegistryConfig.*;


public class OrdinaryThiefClient {

    /**
     * @param args the command line arguments
     * @throws RemoteException Remote Exception
     * @throws CloneNotSupportedException Clone not supported
     */
    public static void main(String[] args) throws RemoteException, CloneNotSupportedException {
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = RMI_REGISTRY_PORT;
        
        MuseumInterface museum_interface = null;
        ConcentrationSiteInterface cs_interface = null;
        ControlCollectionSiteInterface ccs_interface = null;
        LoggerInterface logger_interface = null;
        AssaultPartyInterface[] ap_interface = new AssaultPartyInterface[ASSAULT_PARTIES_NUMBER];
        
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            museum_interface = (MuseumInterface) registry.lookup(REGISTRY_MUSEUM_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating referee site: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Museum is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            logger_interface = (LoggerInterface) registry.lookup(REGISTRY_LOGGER_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating log: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Logger is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            cs_interface = (ConcentrationSiteInterface) registry.lookup(REGISTRY_CONCENTRATION_SITE_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating bench: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ConcentrationSite is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            ccs_interface = (ControlCollectionSiteInterface) registry.lookup(REGISTRY_COLLECTION_SITE_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating referee site: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ControlCollectionSite is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }
        String nameEntry;
        for (int i = 0; i < ASSAULT_PARTIES_NUMBER; i++) {
            if(i==0) nameEntry=REGISTRY_ASSAULT_PARTY0_NAME;
            else nameEntry=REGISTRY_ASSAULT_PARTY1_NAME;
            
            try {
                Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
                ap_interface[i] = (AssaultPartyInterface) registry.lookup(nameEntry);
            } catch (RemoteException e) {
                System.out.println("Exception thrown while locating referee site: " + e.getMessage() + "!");
                System.exit(1);
            } catch (NotBoundException e) {
                System.out.println("Assault party is not registered: " + e.getMessage() + "!");
                System.exit(1);
            }
        }

        OrdinaryThief thiefs[] = new OrdinaryThief[ORDINARY_THIEVES];

        for (int i = 0; i < ORDINARY_THIEVES; i++) {

            thiefs[i] = new OrdinaryThief((MuseumInterface) museum_interface, (ControlCollectionSiteInterface) ccs_interface, (ConcentrationSiteInterface) cs_interface, (AssaultPartyInterface[]) ap_interface, i, (LoggerInterface) logger_interface);

        }

        /* Arranque da simulação */
        for (int i = 0; i < ORDINARY_THIEVES; i++) {
            thiefs[i].start();
        }

        /* Aguardar o fim da simulação */
        for (int i = 0; i <ORDINARY_THIEVES; i++) {
            try {
                thiefs[i].join();
            } catch (InterruptedException e) {
            }
            System.out.println("O cliente " + i + " terminou.");
        }
        
        System.out.println("Done!");
    }
    
}
