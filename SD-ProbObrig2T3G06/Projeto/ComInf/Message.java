package ComInf;

import static Constants.Constants.*;
import java.io.*;
import java.util.HashMap;

public class Message implements Serializable {

    private static final long serialVersionUID = 1001L;
    
    /*MasterThief*/
   public static final int START_OPS = 1;
   public static final int APPRAISE_SIT = 2;
   public static final int PREPARE_AP = 3;
   public static final int TAKE_REST = 4;
   public static final int GET_ROOM_DISTANCE = 5;
   public static final int RESET_AND_SET = 6;
   public static final int SEND_AP = 7;
   public static final int SUM_UP_RESULTS = 8;
   public static final int HEIST_OVER = 9;
   public static final int COLLECT_CANVAS = 10;
   
   /*Ordinary Thief*/
   public static final int HAND_A_CANVAS = 11;
   public static final int AM_I_NEEDED = 12;
   public static final int PREPARE_EXCURSION = 13;
   public static final int JOIN_PARTY = 14;
   public static final int CRAWL_IN = 15;
   public static final int WAIT_ALL_ELEMS = 16;
   public static final int ROLL_A_CANVAS = 17;
   public static final int GET_ROOM_ID = 18;
   public static final int REVERSE_DIRECTION = 19;
   public static final int CRAWL_OUT = 20;
   
   /*ACKS*/
   public static final int ACK = 21;
   
   /*Logger*/
   public static final int GET_NUMBER_PAINTINGS = 22;
   public static final int GET_PARTY_ID = 23;
   public static final int ORDINARY_THIEF_LOG = 24;
   public static final int MASTER_THIEF_LOG = 25;
   public static final int MUSEUM_LOG = 26;
   public static final int ASSAULT_PARTY_LOG = 27;
   public static final int FINAL_REPORT = 28;
   
   /*Centralized Server*/
   public static final int GET_CONSTANTS = 29;
   public static final int GET_CONFIGS = 30;
   
   /*Message Fields*/
   
   private int msgType = -1;   //message type
   private int arg_1 = -1;   //first int arg
   private int arg_2 = -1;  //second int arg
   private int arg_3 = -1;  //third int arg
   private int arg_4 = -1;  //fourth int arg
   private boolean flag = false;
   private boolean[] booleanArray = new boolean[ASSAULT_PARTIES_NUMBER];  //flag
   private String s_arg_1 = "";
   private HashMap<String, String> [] configs;
   /*Constructors*/
    public Message (){
   }
    
   public Message (int type){
      this.msgType = type;
   }

   public Message (int type, int arg_1){
      this.msgType = type;
      this.arg_1= arg_1;
   }
   public Message(int type, boolean flag){
       this.flag = flag;
       this.msgType = type;
   }
   
   public Message(int type, boolean flag, int arg_1){
       this.flag = flag;
       this.arg_1 = arg_1;
       this.msgType = type;
   }
   
   public Message (int type, boolean[] booleanArray){
      this.msgType = type;
      this.booleanArray= booleanArray;
   }
   
   public Message (int type, int arg_1, boolean[] booleanArray){
      this.msgType = type;
      this.arg_1= arg_1;
      this.booleanArray = booleanArray;
   }

   public Message (int type, int arg_1, int arg_2){
      this.msgType = type;
      this.arg_1= arg_1;
      this.arg_2 = arg_2;
   }
   public Message (int type, int arg_1, int arg_2, int arg_3){
      this.msgType = type;
      this.arg_1= arg_1;
      this.arg_2 = arg_2;
      this.arg_3 = arg_3;
   }
   public Message (int type, int arg_1, int arg_2, int arg_3, int arg_4){
      this.msgType = type;
      this.arg_1= arg_1;
      this.arg_2 = arg_2;
      this.arg_3 = arg_3;
      this.arg_4 = arg_4;
   }
   public Message(int type, String s_arg_1){
       this.msgType = type;
       this.s_arg_1 = s_arg_1;
   }
   public Message(int type, HashMap<String, String>[] configs){
       this.msgType = type;
       this.configs = configs;
   }
   
   /*Getters*/
   public int getMessageType (){
      return (msgType);
   }
   
   public boolean getFlag(){
       return flag;
   }
   
   public boolean[] getBooleanArray (){
      return (booleanArray);
   }
   
   public int getMessageArg_1 (){
      return (arg_1);
   }

   public int getMessageArg_2 (){
      return (arg_2);
   }
   
   public int getMessageArg_3 (){
      return (arg_3);
   }
   
   public int getMessageArg_4 (){
      return (arg_4);
   }

    public String getS_arg_1() {
        return s_arg_1;
    }

    public HashMap<String, String>[] getConfigs() {
        return configs;
    }
    

}
