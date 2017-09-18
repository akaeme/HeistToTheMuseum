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
import ServerSide.ControlCollectionSite;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IControlCollectionSite implements GenericInterface {

    /**
     * Control Collection Site (represents the provided service).
     *
     * @serialField controlCollectionSite
     */
    private final ControlCollectionSite controlCollectionSite;

    /**
     * Control Collection Site interface Instantiation.
     *
     * @param ccs controlCollectionSite site
     */
    public IControlCollectionSite(ControlCollectionSite ccs) {
        this.controlCollectionSite = ccs;
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
            // Load ControlCollectionSite
            Class<?> cls = Class.forName("ServerSide.ControlCollectionSite");
            Object obj = this.controlCollectionSite;
            Object value;
            Method method = null;
            switch (inMessage.getMessageType()) {
                case APPRAISE_SIT:
                    method = cls.getDeclaredMethod("appraiseSit", boolean.class);
                    value = method.invoke(obj, inMessage.getFlag());
                    outMessage = new Message(ACK, (int) value);
                    break;
                case START_OPS:
                    method = cls.getDeclaredMethod("startOperations");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
                case SEND_AP:
                    method = cls.getDeclaredMethod("sendAssaultParty");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
                case TAKE_REST:
                    method = cls.getDeclaredMethod("takeARest");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
                case COLLECT_CANVAS:
                    method = cls.getDeclaredMethod("collectCanvas");
                    value = method.invoke(obj);
                    outMessage = new Message(ACK, (boolean[]) value);
                    break;
                case SUM_UP_RESULTS:
                    method = cls.getDeclaredMethod("sumUpResults");
                    value = method.invoke(obj);
                    outMessage = new Message(ACK, (int) value);
                    break;
                case HAND_A_CANVAS:
                    method = cls.getDeclaredMethod("handACanvas", boolean.class, int.class);
                    method.invoke(obj, inMessage.getFlag(), inMessage.getMessageArg_1());
                    outMessage = new Message(ACK);
                    break;
                case PREPARE_EXCURSION:
                    method = cls.getDeclaredMethod("prepareExcursion", int.class);
                    value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (int) value);
                    break;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(IControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outMessage;
    }

}
