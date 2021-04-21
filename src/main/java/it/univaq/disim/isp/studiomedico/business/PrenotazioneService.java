package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.exceptions.DatabaseException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Prenotazione;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Visita;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public interface PrenotazioneService {

    int getIdSpecializzazioneByVisita(String visita);


    List<Medico> getMediciByIdSpecializzazione(int id_specializzazione);

    // query che dato un Medico restituisce la lista dei turni di quel Medico
    //List<Turno> getTurniByMedico(Medico medico);

    // query che dato un Medico restituisce la lista dei turni di quel Medico
    List<Turno> getTurniByMedico(String nome, String cognome);

    // query che dato un Turno di un Medico restituisce la lista delle Prenotazioni effettuate in quel Turno
    List<Prenotazione> getPrenotazioniByIdTurno(int id_turno);

    List<Medico> effettuaRicercaPrenotazione(String visita);

    List<Prenotazione> getPrenotazioniByIdPaziente(int id);

    Visita getVisita(String visita);

    List<String> getVisiteByIdTurno(Integer id);

    void annullaPrenotazione(Integer id);

    void prenotaVisita(Prenotazione prenotazionestore);
}
