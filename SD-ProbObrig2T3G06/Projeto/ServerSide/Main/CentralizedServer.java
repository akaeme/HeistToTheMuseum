/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Main;

import AuxDataStructs.JsonParser;
import Communication.Proxy.ClientProxy;
import Communication.ServerCom;
import Constants.Constants;
import ServerSide.Centralized;
import ServerSide.Interfaces.ICentralized;
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


public class CentralizedServer {

    /**
     * @param args the command line arguments
     * @throws java.net.SocketException SocketException
     * @throws java.net.SocketTimeoutException SocketTimeoutException
     * @throws java.text.ParseException ParseException
     * @throws org.json.JSONException JSONException
     * @throws org.json.simple.parser.ParseException ParseException
     */
    public static void main(String[] args) throws SocketException, SocketTimeoutException, IOException, ParseException, JSONException, org.json.simple.parser.ParseException {

        int hostPort = Integer.parseInt(args[0]);

        String json_path = "mapping.json";

        HashMap<String, String>[] hashMaps;

        Centralized centralizedSv;

        ICentralized centralizedSv_I;

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(json_path));

        JSONArray json = (JSONArray) obj;

        hashMaps = JsonParser.getHashMaps(json);

        //canais de comunicação
        ServerCom schan, schani;

        // agente que presta o serviço
        ClientProxy clientProxy;

        Constants c = new Constants();
        centralizedSv = new Centralized();
        centralizedSv_I = new ICentralized(centralizedSv.Serialize(c), hashMaps);

        //criacao do canal de escuta
        schan = new ServerCom(hostPort);
        schan.start();

        System.out.println("Centralized service has started!\n Server is listening.");

        while (true) {
            // escuta
            schani = schan.accept();
            // lançamento do agente prestador do serviço
            clientProxy = new ClientProxy(schani, centralizedSv_I);
            clientProxy.start();
        }

    }

}
