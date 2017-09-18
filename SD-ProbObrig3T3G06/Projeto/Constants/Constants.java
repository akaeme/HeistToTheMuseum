
package Constants;
/**
 * Constants Configuration
 * 
 */
public class Constants {

    public static final int ROOMS_NUMBER = 5;
    public static final int MAX_PAINTINGS = 16;

    public static final int MIN_PAITINGS = 8;
    public static final int MAX_ROOM_DISTANCE = 30;

    public static final int MIN_ROOM_DISTANCE = 15;
    public static final int MAX_THIEVES_DISTANCE = 3;
    public static final int MAX_THIEVES_DISPLACEMENT = 6;

    public static final int MIN_THIEVES_DISPLACEMENT = 2;
    public static final int ASSAULT_PARTIES_NUMBER = 2;
    public static final int MAX_PARTY_THIEVES = 3;
    public static final int ORDINARY_THIEVES = ASSAULT_PARTIES_NUMBER * MAX_PARTY_THIEVES;

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
    // Number of entities that we have, 6 +1
    public static final int VECTORTIMESTAMP_SIZE = ORDINARY_THIEVES + 1;
    
}
