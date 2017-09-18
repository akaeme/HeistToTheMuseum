/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;


public class States {

    public static final int NEEDED = 2, NOT_NEEDED = 1, IN_ACTION = 0;
    public static final int OUTSIDE = 1000;
    public static final int CRAWLING_INWARDS = 2000;
    public static final int CRAWLING_OUTWARDS = 4000;
    public static final int AT_ROOM = 3000;
    public static final int PLANING_THE_HEIST = 1000;
    public static final int ASSEMBLING_A_GROUP = 3000;
    public static final int DECIDING_WHAT_TO_DO = 2000;
    public static final int PRESENTING_THE_REPORT = 5000;

    public static final int WAITNG_FOR_GROUP_ARRIVAL = 4000;

    public static final int TO_PRESENTING_THE_REPORT = 1,
            TO_ASSEMBLE_A_GROUP = 2,
            TO_WAIT_FOR_GROUP = 3;
}
