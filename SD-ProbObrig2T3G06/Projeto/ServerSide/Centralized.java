/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Constants.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;


public class Centralized implements Serializable {
    /**
     * Serialize
     * @param c Constants
     * @return Serialized string
     * @throws IOException invalid IO
     */
    public String Serialize(Constants c) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjectOut = new ObjectOutputStream(byteArray)) {
            ObjectOut.writeObject(c);
        }
        return Base64.getEncoder().encodeToString(byteArray.toByteArray());
    }
    
    /**
      * DeSerialize
      * @param s Serialized string
      * @return DeSerialized
      * @throws IOException invalid IO
      * @throws ClassNotFoundException Class not found
      */
    public Constants DeSerialize(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ObjectIn = new ObjectInputStream(new ByteArrayInputStream(data));
        Object obj = ObjectIn.readObject();
        Constants toRet = (Constants) obj;
        return toRet;
    }

}
