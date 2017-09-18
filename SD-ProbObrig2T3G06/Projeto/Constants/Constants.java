package Constants;

import java.io.Serializable;

public class Constants implements Serializable {

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

}
