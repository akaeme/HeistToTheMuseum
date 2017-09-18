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
import ServerSide.ConcentrationSite;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IConcentrationSite implements GenericInterface {

    /**
     * Concentration site (represents the provided service)
     *
     * @serialField concentrationSite
     */
    private final ConcentrationSite concentrationSite;

    /**
     * Concentration site interface instantiation
     *
     * @param cs concentration site
     */
    public IConcentrationSite(ConcentrationSite cs) {
        this.concentrationSite = cs;
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
        Message outMessage = null; 

        /* processing */
        try {
        // Load ConcentrationSite
        Class<?> cls = Class.forName("ServerSide.ConcentrationSite");
        Object obj = this.concentrationSite;
            switch (inMessage.getMessageType()) {
                case PREPARE_AP:
                    Method method = cls.getDeclaredMethod("prepareAssaultParty");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
                case AM_I_NEEDED:
                    method = cls.getDeclaredMethod("amINeeded", int.class);
                    Object value = method.invoke(obj, inMessage.getMessageArg_1());
                    outMessage = new Message(ACK, (int) value);
                    break;
                case HEIST_OVER:
                    method = cls.getDeclaredMethod("heistOver");
                    method.invoke(obj);
                    outMessage = new Message(ACK);
                    break;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
        }
        return outMessage;
    }
}
