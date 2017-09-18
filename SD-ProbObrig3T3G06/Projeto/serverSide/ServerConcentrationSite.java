/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import interfaces.ConcentrationSiteInterface;
import genclass.GenericIO;
import interfaces.RegisterInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import registry.RegistryConfig;
import static registry.RegistryConfig.RMI_REGISTRY_PORT;


public class ServerConcentrationSite {

    /**
     * @param args the command line arguments
     * @throws java.rmi.NotBoundException Not bound
     * @throws java.rmi.RemoteException Remote expection
     * @throws java.rmi.AlreadyBoundException Already bound
     */
    public static void main(String[] args) throws NotBoundException, RemoteException, AlreadyBoundException {
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = RMI_REGISTRY_PORT;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");

        /* instantiate a remote object that runs mobile code and generate a stub for it */
        ConcentrationSite engine = new ConcentrationSite(rmiRegHostName);
        ConcentrationSiteInterface engineStub = null;
        int listeningPort = RegistryConfig.REGISTRY_CONCENTRATION_SITE_PORT;
        /* it should be set accordingly in each case */

        try {
            engineStub = (ConcentrationSiteInterface) UnicastRemoteObject.exportObject((Remote) engine, listeningPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("ComputeEngine stub generation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_CONCENTRATION_SITE_NAME;
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
        GenericIO.writelnString("ConcentrationSite object was registered!");
    }

}
