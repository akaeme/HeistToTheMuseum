package serverSide;

import AuxDataStructs.Logging.*;
import AuxDataStructs.VectorTimestamp;
import Constants.Constants;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import interfaces.LoggerInterface;
import interfaces.RegisterInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfig;

public class Logger implements LoggerInterface {
    /**
   *  State
   *
   *    @serialField state
   */
    private String state = "";
    
    /**
   *  Rooms array
   *
   *    @serialField rooms
   */
    private RoomLogging[] rooms;
    
    /**
   *  Ordinary Thieves array
   *
   *    @serialField ordinaryThieves
   */
    private OrdinaryThiefLogging[] ordinaryThieves;
    
    /**
   *  Master thief
   *
   *    @serialField masterThief
   */
    private MasterThiefLogging masterThief = new MasterThiefLogging();
    
    /**
   *  AssaultParties array
   *
   *    @serialField assaultParties
   */
    private AssaultPartyLogging[] assaultParties;
    
    /**
   *  Constants
   *
   *    @serialField constants
   */
    private Constants constants;
    
    /**
   *  File name to log
   *
   *    @serialField FILENAME
   */
    private static final String FILENAME = "log.txt";
    
    private final VectorTimestamp clocks;
    private String reghostname;
     /**
     * Logger Instantiation
     * @param reghostname Host name registry 
     */
    public Logger(String reghostname) {
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 0);
        this.rooms = new RoomLogging[ROOMS_NUMBER];
        this.ordinaryThieves = new OrdinaryThiefLogging[ORDINARY_THIEVES];
        this.assaultParties = new AssaultPartyLogging[ASSAULT_PARTIES_NUMBER];
        this.reghostname = reghostname;
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

    /**
     * Subtitle.
     */
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
        output+=String.format("VCk  0       - local clock of the master thief\n");
        output+=String.format("VCk  1       - local clock of the ordinary thief 1\n");
        output+=String.format("VCk  2       - local clock of the ordinary thief 2\n");
        output+=String.format("VCk  3       - local clock of the ordinary thief 3\n");
        output+=String.format("VCk  4       - local clock of the ordinary thief 4\n");
        output+=String.format("VCk  5       - local clock of the ordinary thief 5\n");
        output+=String.format("VCk  6       - local clock of the ordinary thief 6\n");
        try {
            Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Log Header.
     */
    private void LogHeader() {
        String output;
        output="                             Heist to the Museum - Description of the internal state\n";
        output+="MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6                VCk\n";
        output+="Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    0   1   2   3   4   5   6 \n";
        output+="                   Assault party 1                       Assault party 2                       Museum\n";
        output+="           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5\n";
        output+="    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT\n";
        
        try {
            Files.write(Paths.get(FILENAME), output.getBytes());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Log Results.
     * @param total Total paintings
     */
    private void LogResults(int total) {
        String output = String.format("My friends, tonight's effort produced %2d priceless paintings!\n\n", total);
        try {
            Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Get Element index
     * @param assaultPartyIndex AssaultParty Identification
     * @param id Identification
     * @return Element Id or -1
     */
    private int getElemIndex(int assaultPartyIndex, int id) {
        for (int i = 0; i < this.assaultParties[assaultPartyIndex].getElemsInParty().size(); i++) {
            if (this.assaultParties[assaultPartyIndex].getElemsInParty().get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    //@Override
    /**
     * AssaultParty Log.
     * @param kind Room Id, Member Id, Position, Carrying a Canvas
     * @param APid AssaultParty Identification
     * @param Eid Element Id
     * @param val Value
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp AssaultPartyLog(int kind, int APid, int Eid, int val,VectorTimestamp clock) {
        this.clocks.update(clock);
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
        return this.clocks.clone();
    }
    /**
     * Ordinary Thief log.
     * @param kind State, Situation or Maximum Displacement
     * @param Tid Thief Identification
     * @param val Value
     * @param clock Vectorial timestamp clock
     * @return  Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp OrdinaryThiefLog(int kind, int Tid, int val, VectorTimestamp clock) {
         this.clocks.update(clock);
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
        return this.clocks.clone();
    }
    /**
     * MasterThief Log.
     * @param val Value
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp MasterThiefLog(int val, VectorTimestamp clock) {
        this.clocks.update(clock);
        this.masterThief.setStat(val);
        this.ThiefLogged();
        return this.clocks.clone();
    }
    
    /**
     * Museum Log.
     * @param kind Distance or Paintings Number
     * @param Rid Room identification
     * @param val Value
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    //@Override
    @Override
    public synchronized VectorTimestamp MuseumLog(int kind, int Rid, int val, VectorTimestamp clock) {
        this.clocks.update(clock);
        switch (kind) {
            case NP:
                this.rooms[Rid].setNP(val);
                break;
            case DT:
                this.rooms[Rid].setDT(val);
                break;
        }
        return this.clocks.clone();
    }
    
    /**
     * Final Report.
     * @param total_paint total paintings
     * @param clock Vectorial timestamp clock
     * @return Vectorial timestamp clock
     */
    @Override
    public synchronized VectorTimestamp FinalReport(int total_paint, VectorTimestamp clock) {
        this.clocks.update(clock);
        this.LogHeader();
        this.LogState();
        this.LogResults(total_paint);
        this.LogSubtitle();
        return this.clocks.clone();
    }

    /**
     * Log State.
     */
    private synchronized void LogState() {
        try {
            Files.write(Paths.get(FILENAME), this.state.getBytes(), APPEND);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Log AssaultParties.
     */
    private synchronized void LogAssaultParties() {
        if (this.checkAllAssaultPartiesLogged()) {
            this.state += this.getAsaultParties() + "   " + this.getRooms() + "\n";
        }

    }
    /**
     * Get AssaultParties.
     * @return AssaultParties logged
     */
    private String getAsaultParties() {
        String toRet = "";
        for (AssaultPartyLogging aps : this.assaultParties) {
            toRet += aps.toString() + "         ";
        }
        return toRet;
    }
    /**
     * Get Rooms.
     * @return Rooms logged
     */
    private String getRooms() {
        String toRet = "";
        for (RoomLogging r : this.rooms) {
            toRet += r.toString() + "   ";
        }
        return toRet;
    }
    
    /**
     * Check if all AssaultParties Logged.
     * @return @return <b>true</b> if all AssaultParties logged, <b>false</b> if not
     */
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

    /**
     * Thief Logged.
     */
    private synchronized void ThiefLogged() {

        if (this.masterThief.getStat() != -1 && this.checkAllThievesLogged()) {
            this.state += this.masterThief + this.getOrdinaryThieves() + "\n";
        }
    }

    /**
     * Get Ordinary Thieves.
     * @return Ordinary Thieves to String
     */
    private String getOrdinaryThieves() {
        String toRet = "";
        for (OrdinaryThiefLogging ot : this.ordinaryThieves) {
            toRet += ot.toString() + "    ";
        }
        return toRet;
    }
    
    /**
     * Check if all Thieves Logged.
     * @return <b>true</b> if all thieves logged, <b>false</b> if not
     */
    private boolean checkAllThievesLogged() {
        for (OrdinaryThiefLogging ot : this.ordinaryThieves) {
            if (ot.getMD() == -1 || ot.getS() == -1 || ot.getStat() == -1) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void shutdown() throws RemoteException {
        Registry registry = null;
        RegisterInterface reg = null;
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_LOGGER_NAME;
        
        
        try {
            reg = (RegisterInterface) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Logger registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Logger not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Logger shutdown.");
    }

}
