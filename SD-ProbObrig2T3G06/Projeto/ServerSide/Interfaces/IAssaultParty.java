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
import ServerSide.AssaultParty;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IAssaultParty implements GenericInterface {

    /**
     * AssaultParty (represents the provided service)
     *
     * @serialField assaultParty
     */
    private AssaultParty assaultParty;

    /**
     * AssaultParty interface instantiation
     *
     * @param ap assault party
     */
    public IAssaultParty(AssaultParty ap) {
        this.assaultParty = ap;
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
        Message outMessage = null;                           // mensagem de resposta
        /* processing */
        try {
        // Load AssaultParty
        Class<?> cls = Class.forName("ServerSide.AssaultParty");
        Object obj = assaultParty;
        Object value;
            switch (inMessage.getMessageType()) {
                case JOIN_PARTY:
                    Method method = cls.getDeclaredMethod("joinParty", int.class, int.class);
                    method.invoke(obj, inMessage.getMessageArg_1(), inMessage.getMessageArg_2());
                    outMessage = new Message(ACK);
                    break;
                case CRAWL_IN:
                    method = cls.getDeclaredMethod("crawlIn", int.class);
                    value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (int) value);
                    break;
                case CRAWL_OUT:
                    method = cls.getDeclaredMethod("crawlOut", int.class);
                    value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (int) value);
                    break;
                case WAIT_ALL_ELEMS:
                    method = cls.getDeclaredMethod("waitAllElems");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
                case REVERSE_DIRECTION:
                    method = cls.getDeclaredMethod("reverseDirection", int.class);
                    method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK);
                    break;
                case RESET_AND_SET:
                    method = cls.getDeclaredMethod("resetAndSet", int.class, int.class);
                    method.invoke(obj, inMessage.getMessageArg_1(), inMessage.getMessageArg_2());
                    outMessage = new Message(ACK);
                    break;
                case GET_ROOM_ID:
                    method = cls.getDeclaredMethod("getRoomID");
                    value = method.invoke(obj);
                    outMessage = new Message(ACK, (int) value);
                    break;
                case GET_PARTY_ID:
                    method = cls.getDeclaredMethod("getPartyID");
                    value = method.invoke(obj);
                    outMessage = new Message(ACK, (int) value);
                    break;
            }
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        return outMessage;
    }
}
