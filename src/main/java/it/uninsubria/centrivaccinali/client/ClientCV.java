package it.uninsubria.centrivaccinali.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {
    protected ClientCV() throws RemoteException {
    }
}
