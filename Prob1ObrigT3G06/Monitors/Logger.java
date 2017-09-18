package Monitors;

import AuxDataStructs.Logging.*;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import Interfaces.LoggerInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import java.util.logging.Level;

/**
 *
 * @author Fábio Silva <alexandre.fabio@ua.pt>
 */
public class Logger implements LoggerInterface {
    private String state = "";
    private RoomLogging[] rooms = new RoomLogging[ROOMS_NUMBER];
    private OrdinaryThiefLogging[] ordinaryThieves = new OrdinaryThiefLogging[ORDINARY_THIEVES];
    private MasterThiefLogging masterThief = new MasterThiefLogging();
    private AssaultPartyLogging[] assaultParties = new AssaultPartyLogging[ASSAULT_PARTIES_NUMBER];
    private static final String FILENAME = "log.txt";

    public Logger() {
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = new RoomLogging();
        }
        for (int i = 0; i < this.ordinaryThieves.length; i++) {
            this.ordinaryThieves[i] = new OrdinaryThiefLogging();
        }
        for (int i = 0; i < this.assaultParties.length; i++) {
            assaultParties[i] = new AssaultPartyLogging();
        }
    }

    public void LogSubtitle() {
        String output;
        output = "Legend: \n";
        output += "MstT Stat    – state of the master thief \n";
        output += String.format("Thief # Stat - state of the ordinary thief # (# - 1 .. %d) \n", ORDINARY_THIEVES);
        output += String.format("Thief # S    – situation of the ordinary thief # (# - 1 .. %d) either 'W' (waiting to join a party) or 'P' (in party) \n", ORDINARY_THIEVES);
        output += String.format("Thief # MD   – maximum displacement of the ordinary thief # (# - 1 .. %d) a random number between %d and %d \n", ORDINARY_THIEVES, MIN_THIEVES_DISPLACEMENT, MAX_THIEVES_DISPLACEMENT);
        output += String.format("Assault party # RId        – assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5) \n");
        output += String.format("Assault party # Elem # Id  – assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6) \n");
        output += String.format("Assault party # Elem # Pos – assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)\n");
        output += String.format("Assault party # Elem # Cv  – assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1) \n");
        output += String.format("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls \n");
        output += String.format("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30\n");

        try {
            Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void LogHeader() {
        String output;
        output = "                             Heist to the Museum - Description of the internal state\n";
        output += "MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6\n";
        output += "Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD\n";
        output += "                   Assault party 1                       Assault party 2                       Museum\n";
        output += "           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5\n";
        output += "    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT\n";

        try {
            Files.write(Paths.get(FILENAME), output.getBytes());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LogResults(int total) {
        String output = String.format("My friends, tonight's effort produced %2d priceless paintings!\n\n", total);
        try {
            Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getElemIndex(int assaultPartyIndex, int id) {
        for (int i = 0; i < this.assaultParties[assaultPartyIndex].getElemsInParty().size(); i++) {
            if (this.assaultParties[assaultPartyIndex].getElemsInParty().get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public synchronized void AssaultPartyLog(int kind, int APid, int Eid, int val) {
        switch (kind) {
            case RId:
                this.assaultParties[APid] = new AssaultPartyLogging();
                this.assaultParties[APid].setRid(val);
                this.assaultParties[APid].clearElems();
                break;
            case Id:
                ElemLogging elem = new ElemLogging();
                elem.setId(val);
                this.assaultParties[APid].addElem(elem);
                break;
            case Pos:
                this.assaultParties[APid].getElemsInParty().get(getElemIndex(APid, Eid)).setPos(val);
                break;
            case Cv:
                this.assaultParties[APid].getElemsInParty().get(getElemIndex(APid, Eid)).setCv(val);
                break;
        }
        this.LogAssaultParties();

    }

    @Override
    public synchronized void OrdinaryThiefLog(int kind, int Tid, int val) {
        switch (kind) {
            case Stat:
                this.ordinaryThieves[Tid].setStat(val);
                break;
            case S:
                this.ordinaryThieves[Tid].setS(val);
                break;
            case MD:
                this.ordinaryThieves[Tid].setMD(val);
                break;
        }
        this.ThiefLogged();
    }

    @Override
    public synchronized void MasterThiefLog(int val) {
        this.masterThief.setStat(val);
        this.ThiefLogged();
    }

    @Override
    public synchronized void MuseumLog(int kind, int Rid, int val) {
        switch (kind) {
            case NP:
                this.rooms[Rid].setNP(val);
                break;
            case DT:
                this.rooms[Rid].setDT(val);
                break;
        }
    }

    @Override
    public synchronized void FinalReport(int total_paint) {
        this.LogHeader();
        this.LogState();
        this.LogResults(total_paint);
        this.LogSubtitle();
    }

    private synchronized void LogState() {
        try {
            Files.write(Paths.get(FILENAME), this.state.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void LogAssaultParties() {
        if (this.checkAllAssaultPartiesLogged()) {
            this.state += this.getAsaultParties() + "   " + this.getRooms() + "\n";
        }

    }

    private String getAsaultParties() {
        String toRet = "";
        for (AssaultPartyLogging aps : this.assaultParties) {
            toRet += aps.toString() + "         ";
        }
        return toRet;
    }

    private String getRooms() {
        String toRet = "";
        for (RoomLogging r : this.rooms) {
            toRet += r.toString() + "   ";
        }
        return toRet;
    }

    private boolean checkAllAssaultPartiesLogged() {
        int counter = 0;
        for (AssaultPartyLogging aps : this.assaultParties) {
            if (aps.getRid() == -1) {
                return false;
            }
            for (ElemLogging e : aps.getElemsInParty()) {
                if (e.getCv() != -1 && e.getId() != -1 && e.getPos() != -1) {
                    counter++;
                }
            }
            if (counter < 3) {
                return false;
            }
        }
        return true;
    }

    private synchronized void ThiefLogged() {

        if (this.masterThief.getStat() != -1 && this.checkAllThievesLogged()) {
            this.state += this.masterThief + this.getOrdinaryThieves() + "\n";
        }
    }

    private String getOrdinaryThieves() {
        String toRet = "";
        for (OrdinaryThiefLogging ot : this.ordinaryThieves) {
            toRet += ot.toString() + "    ";
        }
        return toRet;
    }

    private boolean checkAllThievesLogged() {
        for (OrdinaryThiefLogging ot : this.ordinaryThieves) {
            if (ot.getMD() == -1 || ot.getS() == -1 || ot.getStat() == -1) {
                return false;
            }
        }
        return true;
    }

}
