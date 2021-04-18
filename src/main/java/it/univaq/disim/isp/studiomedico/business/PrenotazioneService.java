package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Prenotazione;
import it.univaq.disim.isp.studiomedico.domain.Turno;

import java.util.List;

public interface PrenotazioneService {

    int getIdSpecializzazioneByVisita(String visita);


    List<Medico> getMediciByIdSpecializzazione(int id_specializzazione);

    // query che dato un Medico restituisce la lista dei turni di quel Medico
    List<Turno> getTurniByMedico(Medico medico);

    // query che dato un Medico restituisce la lista dei turni di quel Medico
    List<Turno> getTurniByMedico(String nome, String cognome) throws BusinessException;

    // query che dato un Turno di un Medico restituisce la lista delle Prenotazioni effettuate in quel Turno
    List<Prenotazione> getPrenotazioniByTurno(Turno turno);

    List<Medico> effettuaRicercaPrenotazione(String visita);
}
