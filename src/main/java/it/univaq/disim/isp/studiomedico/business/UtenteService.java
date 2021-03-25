package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Utente;

public interface UtenteService {

    Utente autenticazione(String email, String password) throws BusinessException;


    Utente registrazione(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo) throws BusinessException;

    Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo,String specializzazione) throws BusinessException;
}


