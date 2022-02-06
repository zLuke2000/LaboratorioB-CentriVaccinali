//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.Window;

import java.rmi.RemoteException;

public class CentriVaccinali  {

    /**
     * Riferimento al client per essere inviato ai controller.
     * @see ClientCV
     */
    public static ClientCV client;

    /**
     * Metodo main per avvio ClientCV su Thread separato per non bloccare l'interfaccia durante le operazioni
     * @param args Argomenti da linea di comando.
     */
    public static void main(String[] args) {
        // Avvio Thread separato per ClientCV
        try {
            client = new ClientCV();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Avvio interfaccia grafica
        Window.open();
    }
}