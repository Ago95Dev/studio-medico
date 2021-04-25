package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Prenotazione;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;

import java.time.LocalDate;
import java.util.List;

public interface SegretariaService {
    Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo, String specializzazione, String contratto, String turno, String oraInizio, String oraFine) throws BusinessException;

    void inserisciTurnoProvvisorio(int id_medico, String turno, String oraInizio, String oraFine);

    List<Turno> getTurniProposti();

    void accettaTurnoProposto(Turno turno);

    void rifiutaTurnoProposto(Turno turnostore);

    List<Prenotazione> getPrenotazioniByNow(LocalDate now);

    void updateCheckin(Prenotazione prenotazione);

    void updateInCorsoTurno(Turno turno);

    void updatePresenzeMedico(Medico medico);

    void updatePrestazioniMedico(Medico medico);

    void updateCheckout(Prenotazione prenotazione);
}
