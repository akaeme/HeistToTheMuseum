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
import ServerSide.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class ILogger implements GenericInterface {

    /**
     * Logger (represents the provided service)
     *
     * @serialField logger
     */
    private final Logger logger;

    /**
     * Logger interface instantiation
     *
     * @param logger logger
     */
    public ILogger(Logger logger) {
        this.logger = logger;
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
        
         //processing 
        try {
        // Load logger
        Class<?> cls = Class.forName("ServerSide.Logger");
        Object obj = this.logger;
        Method method = null;
            switch (inMessage.getMessageType()) {
                case ORDINARY_THIEF_LOG:
                    method = cls.getDeclaredMethod("OrdinaryThiefLog", int.class, int.class, int.class);
                    method.invoke(obj, inMessage.getMessageArg_1(), inMessage.getMessageArg_2(), inMessage.getMessageArg_3());
                    outMessage = new Message(ACK);
                    break;
                case MASTER_THIEF_LOG:
                    method = cls.getDeclaredMethod("MasterThiefLog", int.class);
                    method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK);
                    break;
                case MUSEUM_LOG:
                    method = cls.getDeclaredMethod("MuseumLog", int.class, int.class, int.class);
                    method.invoke(obj, inMessage.getMessageArg_1(), inMessage.getMessageArg_2(), inMessage.getMessageArg_3());
                    outMessage = new Message(ACK);
                    break;
                case ASSAULT_PARTY_LOG:
                    method = cls.getDeclaredMethod("AssaultPartyLog", int.class, int.class, int.class, int.class);
                    method.invoke(obj, inMessage.getMessageArg_1(), inMessage.getMessageArg_2(), inMessage.getMessageArg_3(), inMessage.getMessageArg_4());
                    outMessage = new Message(ACK);
                    break;
                case FINAL_REPORT:
                    method = cls.getDeclaredMethod("FinalReport", int.class);
                    method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK);
                    break;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(ILogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outMessage;
    }

}
