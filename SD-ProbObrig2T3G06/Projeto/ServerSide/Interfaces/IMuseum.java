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
import ServerSide.Museum;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IMuseum implements GenericInterface {

    /**
     * Museum (represents the provided service)
     *
     * @serialField museum
     */
    private final Museum museum;

    /**
     * Museum interface instantiation
     *
     * @param museum museum
     */
    public IMuseum(Museum museum) {
        this.museum = museum;
    }
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
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta
        
        /* processing */
        try {
        // Load Museum
        Class<?> cls = Class.forName("ServerSide.Museum");
        Object obj = this.museum;
        Object value;
            switch (inMessage.getMessageType()) {
                case ROLL_A_CANVAS:
                    Method method = cls.getDeclaredMethod("rollACanvas", int.class);
                    value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (boolean) value);
                    break;
                case GET_ROOM_DISTANCE:
                    method = cls.getDeclaredMethod("getRoomDistance", int.class);
                    value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (int) value);
                    break;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(IMuseum.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outMessage;
    }

}
