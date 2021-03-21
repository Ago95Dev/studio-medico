package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Utente;

public interface UtenteService {

    Utente autenticazione(String email, String password) throws BusinessException;

    Utente registrazione(String username, String password, String nomeu, String cognomeu, String codicef, String email, String ruolo) throws BusinessException;

}


