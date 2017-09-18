package serverSide;

import AuxDataStructs.Room;
import AuxDataStructs.VectorTimestamp;
import static Constants.Constants.*;
import static Constants.LoggerConstants.*;
import interfaces.LoggerInterface;
//import Interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import interfaces.RegisterInterface;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfig;

public class Museum implements MuseumInterface {

    /**
     * TNumber of Rooms in the Museum
     *
     * @serialField nRooms
     */
    private final int nRooms;
    //private LoggerInterface logger;
    /**
     * Rooms Aux Data Structure to manage all rooms info
     *
     * @serialField rooms
     */
    Room[] rooms;

    private final VectorTimestamp clocks;
    private LoggerInterface logger;
    private String reghostname ;

    /**
     * Museum Instantiation
     *
     * @param logger Logger interface
     * @param reghostname Host name registry 
     * @throws RemoteException Remote exception
     */
    public Museum(LoggerInterface logger, String reghostname) throws RemoteException {
        this.clocks = new VectorTimestamp(VECTORTIMESTAMP_SIZE, 0);
        this.logger = logger;
        this.nRooms = ROOMS_NUMBER;
        this.rooms = new Room[ROOMS_NUMBER];
        this.reghostname = reghostname;
        //this.logger = logger;
        for (int i = 0; i < nRooms; i++) {
            Room room = new Room();
            this.rooms[i] = room;
            //this.MuseumLog(DT, i, room.getDistance());
            //this.MuseumLog(NP, i, room.getNumberOfPaintings());
            logger.MuseumLog(DT, i, room.getDistance(), this.clocks.clone());
            logger.MuseumLog(NP, i, room.getNumberOfPaintings(), this.clocks.clone());
        }
    }

    /**
     * Gets the number of paintings of the given room
     *
     * @param roomId Room identification
     * @param clock Vectorial timestamp clock
     * @return Number of paintings of the given room
     */
    @Override
    public VectorTimestamp getPaintingNumbers(int roomId, VectorTimestamp clock) {
        this.clocks.update(clock);
        int ret = rooms[roomId].getNumberOfPaintings();
        this.clocks.setArg_integer(ret);
        return this.clocks.clone();
    }

    /**
     * Gets the distance to the given room
     *
     * @param roomId Room identification
     * @param clock Vectorial timestamp clock
     * @return Distance to the room
     */
    @Override
    public VectorTimestamp getRoomDistance(int roomId, VectorTimestamp clock) {
        this.clocks.update(clock);
        int ret = rooms[roomId].getDistance();
        this.clocks.setArg_integer(ret);
        return this.clocks.clone();
    }

    /**
     * Rolls a canvas on the given room
     *
     * @param roomID Room identification
     * @param clock Vectorial timestamp clock
     * @return Boolean flag: <b>true</b> if canvas rolled, <b>false</b> if not
     * @throws java.rmi.RemoteException Remote Exception
     */
    @Override
    public synchronized VectorTimestamp rollACanvas(int roomID, VectorTimestamp clock) throws RemoteException {
        this.clocks.update(clock);
        boolean ret = rooms[roomID].decrement();
        this.clocks.setArg_bool(ret);
        //this.MuseumLog(NP, roomID, this.rooms[roomID].getNumberOfPaintings());
        this.logger.MuseumLog(NP, roomID, this.rooms[roomID].getNumberOfPaintings(), this.clocks.clone());
        return this.clocks.clone();
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
        String nameEntryObject = RegistryConfig.REGISTRY_MUSEUM_NAME;
        
        
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
        System.out.println("Museum shutdown.");
    }
    /*public synchronized void MuseumLog(int arg_1, int arg_2, int arg_3){
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.configs[0].get("LoggerServer"), Integer.parseInt(this.configs[1].get("LoggerServer")));
        if (!con.open()) {
            System.exit(1);
        }
        
        
        outMessage = new Message(MUSEUM_LOG, arg_1, arg_2, arg_3);        
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getMessageType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }*/
}
