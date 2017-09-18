/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import ComInf.Message;
import static ComInf.Message.*;
import Communication.ClientCom;
import Constants.Constants;
import ServerSide.Centralized;
import java.io.IOException;
import java.util.HashMap;


public class CentralizedProxy {

    private final String serverHostName;
    private final int serverHostPort;
    private Centralized centralizedSv;

    public CentralizedProxy(String SERVER_HOST, int SERVER_PORT) {
        this.serverHostName = SERVER_HOST;
        this.serverHostPort = SERVER_PORT;
        this.centralizedSv = new Centralized();
    }

    public Constants getConstants() throws IOException, ClassNotFoundException {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        outMessage = new Message(GET_CONSTANTS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return this.centralizedSv.DeSerialize(inMessage.getS_arg_1());
    }

    public HashMap<String, String>[] getConfigs() throws IOException, ClassNotFoundException {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.serverHostName, this.serverHostPort);
        if (!con.open()) {
            System.exit(1);
        }
        
        outMessage = new Message(GET_CONFIGS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getConfigs();
    }
}
