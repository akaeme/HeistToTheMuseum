/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxDataStructs;

import java.util.HashMap;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;


public class JsonParser {
    /**
     * 
     * @param json Json
     * @return Hashmap
     * @throws JSONException JSON Exception
     */
    public static HashMap[] getHashMaps(JSONArray json) throws JSONException {
        HashMap<String, String> hostName = new HashMap<>();
        HashMap<String, String> hostPort = new HashMap<>();

        for (int i = 0; i < json.size(); i++) {
            JSONObject jo = (JSONObject) json.get(i);
            Set<String> set = jo.keySet();

            set.forEach((key) -> {
                JSONObject value = (JSONObject) jo.get(key);
                Set<String> new_keys = value.keySet();
                new_keys.stream().map((new_key2) -> {
                    return new_key2;
                }).forEachOrdered((new_key2) -> {
                    String my_value = (String) value.get(new_key2); //value
                    if (new_key2.equals("hostName")) {
                        hostName.put(key, my_value);
                    } else {
                        hostPort.put(key, my_value);
                    }
                });
            });
        }

        HashMap<String, String>[] responseArray = new HashMap[]{hostName, hostPort};

        return responseArray;
    }

}
