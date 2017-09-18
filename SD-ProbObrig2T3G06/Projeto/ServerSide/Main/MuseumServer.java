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
import ServerSide.Interfaces.IMuseum;
import ServerSide.Museum;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class MuseumServer {
    /**
     * @param args the command line arguments
     * @throws java.net.SocketException Socket Exception
     * @throws java.net.SocketTimeoutException Socket Timeout
     * @throws java.io.IOException IO invalid
     * @throws java.lang.ClassNotFoundException Class not found
     */
    public static void main(String[] args) throws SocketException, SocketTimeoutException, IOException, ClassNotFoundException {
        String hostName = args[0];
        int hostPort = Integer.parseInt(args[1]);
        HashMap<String, String>[] configs;
        Constants constants;

        CentralizedProxy centralizedProxy = new CentralizedProxy(hostName, hostPort);
        constants = centralizedProxy.getConstants();
        configs = centralizedProxy.getConfigs();

        int serverPort = Integer.parseInt(configs[1].get("MuseumServer"));

        Museum museum;
        IMuseum museumI;

        //communication channels
        ServerCom channel_1, channel_2;

        ClientProxy clientProxyMuseum;

        //listening channel
        channel_1 = new ServerCom(serverPort);
        channel_1.start();

        museum = new Museum(constants, configs);
        museumI = new IMuseum(museum);

        System.out.println("Museum service has started!\n Server is listening.");

        while (true) {
            //listen
            channel_2 = channel_1.accept();

            clientProxyMuseum = new ClientProxy(channel_2, museumI);
            clientProxyMuseum.start();
        }

    }

}
