/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import static Constants.Constants.ASSAULT_PARTIES_NUMBER;
import genclass.GenericIO;
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


public class MasterThiefClient {

    /**
     * @param args the command line arguments
     * @throws RemoteException Remote Exception
     */
    public static void main(String[] args) throws RemoteException {
        /* get location of the generic registry service */

        // nome do sistema onde está localizado o serviço de registos RMI
        String rmiRegHostName;
        // port de escuta do serviço
        int rmiRegPortNumb;
       rmiRegHostName = args[0];
        rmiRegPortNumb = RMI_REGISTRY_PORT;

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
            System.out.println("ControlCollectionsite is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        String nameEntry;
        for (int i = 0; i < ASSAULT_PARTIES_NUMBER; i++) {
            if (i == 0) {
                nameEntry = REGISTRY_ASSAULT_PARTY0_NAME;
            } else {
                nameEntry = REGISTRY_ASSAULT_PARTY1_NAME;
            }

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
        MasterThief mthief = new MasterThief((MuseumInterface) museum_interface, (ControlCollectionSiteInterface) ccs_interface, (ConcentrationSiteInterface) cs_interface, (AssaultPartyInterface[]) ap_interface, (LoggerInterface) logger_interface);

        mthief.start();

        while (mthief.isAlive()) {
            Thread.yield();
        }
        try {
            mthief.join();
        } catch (InterruptedException e) {
        }
        museum_interface.shutdown();
        cs_interface.shutdown();
        ccs_interface.shutdown();
        ap_interface[0].shutdown();
        ap_interface[1].shutdown();
        logger_interface.shutdown();
        System.out.println("Done!");
    }

}
