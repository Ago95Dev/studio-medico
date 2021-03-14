package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;
import java.util.Set;

public class Turno {
    private Integer id;
    private Date giorno, orainizio, orafine;
    private boolean accettato, incorso;
    private Set<Prenotazione> listaPrenotazioni;

    //public boolean accettaTurno()
    //public booblean rifiutaTurno()
    //public set<Prenotazione> getlistaPrenotazioni();
    //public void setlistaPrenotazioni(Set<Prenotazione> lista);
    //public boolean inCorso();
    //addlistaPrenotazioni(Prenotazione prenotazione);
}
