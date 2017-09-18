/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Main;

import ClientSide.CentralizedProxy;
import Communication.Proxy.ClientProxy;
import Communication.ServerCom;
import Constants.Constants;
import ServerSide.AssaultParty;
import ServerSide.Interfaces.IAssaultParty;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class AssaultPartyServer_0 {

/**
 * 
 * @param args The command line arguments
 * @throws SocketException SocketException
 * @throws SocketTimeoutException SocketTimeoutException
 * @throws IOException IOException
 * @throws ClassNotFoundException ClassNotFoundException
 */
    public static void main(String[] args) throws SocketException, SocketTimeoutException, IOException, ClassNotFoundException {
        String hostName = args[0];
        int hostPort = Integer.parseInt(args[1]);
        int partyId = Integer.parseInt(args[2]);
        HashMap<String, String>[] configs;
        Constants constants;

        CentralizedProxy centralizedProxy = new CentralizedProxy(hostName, hostPort);
        constants = centralizedProxy.getConstants();
        configs = centralizedProxy.getConfigs();
        String command = "AssaultPartyServer_" + partyId;
        int serverPort = Integer.parseInt(configs[1].get(command));

        AssaultParty assaultParty;
        IAssaultParty assaultPartyI;

        //communication channels
        ServerCom channel_1, channel_2;

        ClientProxy clientProxyAssaultParty;

        //listening channel
        channel_1 = new ServerCom(serverPort);
        channel_1.start();

        assaultParty = new AssaultParty(partyId, constants, configs);
        assaultPartyI = new IAssaultParty(assaultParty);
        System.out.println("AssaultParty_" + partyId + " service has started!\n Server is listening.");

        while (true) {
            // listen
            channel_2 = channel_1.accept();

            clientProxyAssaultParty = new ClientProxy(channel_2, assaultPartyI);
            clientProxyAssaultParty.start();
        }
    }

}
