/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import genclass.GenericIO;
import interfaces.AssaultPartyInterface;
import interfaces.LoggerInterface;
import interfaces.RegisterInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import registry.RegistryConfig;
import static registry.RegistryConfig.REGISTRY_LOGGER_NAME;
import static registry.RegistryConfig.RMI_REGISTRY_PORT;


public class ServerAssaultParty_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String rmiRegHostName = args[0];
     int rmiRegPortNumb = RMI_REGISTRY_PORT;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");
        
        LoggerInterface logger_interface = null;
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
        /* instantiate a remote object that runs mobile code and generate a stub for it */
        AssaultParty engine = new AssaultParty(1, (LoggerInterface) logger_interface, rmiRegHostName);
        AssaultPartyInterface engineStub = null;
        int listeningPort = RegistryConfig.REGISTRY_ASSAULT_PARTY_PORT_1;
        /* it should be set accordingly in each case */

        try {
            engineStub = (AssaultPartyInterface) UnicastRemoteObject.exportObject((Remote) engine, listeningPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("ComputeEngine stub generation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_ASSAULT_PARTY1_NAME;
        Registry registry = null;
        RegisterInterface reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        try {
            reg = (RegisterInterface) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, engineStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("ComputeEngine registration exception: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("ComputeEngine already bound exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("AP1 object was registered!");
    }

}
