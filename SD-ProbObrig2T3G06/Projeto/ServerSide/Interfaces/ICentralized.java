/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Interfaces;

import ComInf.Message;
import static ComInf.Message.*;
import ComInf.MessageException;
import Communication.Proxy.GenericInterface;
import java.util.HashMap;


public class ICentralized implements GenericInterface {
    /**
     * Constants
     *
     * @serialField constants
     */
    private final String constants;
    
    /**
     * Configurations
     *
     * @serialField configs
     */
    private final HashMap<String, String>[] configs;

    /**
     * ICentralized Instantiation
     * @param constants Constants
     * @param configs Configurations
     */
    public ICentralized(String constants, HashMap<String, String>[] configs) {
        this.constants = constants;
        this.configs = configs;
    }

    @Override
        /**
     * Message Processing
     * Generating and answer message
     *
     * @param inMessage inbox message
     *
     * @return answer message
     *
     * @throws MessageException if message is invalid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message response = null;
        switch (inMessage.getMessageType()) {
            case GET_CONSTANTS:
                response = new Message(ACK, this.constants);
                break;
            case GET_CONFIGS:
                response = new Message(ACK, this.configs);
                break;
        }
        return response;
    }

}
