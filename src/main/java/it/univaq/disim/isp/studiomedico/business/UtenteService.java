package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Specializzazione;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;

import java.util.List;


public interface UtenteService {

    Utente autenticazione(String email) throws BusinessException;

    Utente registrazione(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo) throws BusinessException;

    Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo, String specializzazione, String Contratto, String turno, String oraInizio, String oraFine) throws BusinessException;

    void inserisciTurnoProvvisorio(int id_medico, String turno, String oraInizio, String oraFine);

    List<Turno> getTurniProposti();

    void accettaTurnoProposto(Turno turno);

    void rifiutaTurnoProposto(Turno turnostore);

    List<Utente> getAllUtenti();
}


