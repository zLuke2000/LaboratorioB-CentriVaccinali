package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.rmi.Remote;

public interface ServerCVInterface extends Remote{
    void registraVaccinato(Vaccinato cittadinovaccinato);
    //public void registraCentroVaccinale(CentroVaccinale );
}
