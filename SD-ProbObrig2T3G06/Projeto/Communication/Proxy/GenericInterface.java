/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication.Proxy;

import ComInf.Message;
import ComInf.MessageException;

public interface GenericInterface {
    public Message processAndReply (Message inMessage) throws MessageException;
}
