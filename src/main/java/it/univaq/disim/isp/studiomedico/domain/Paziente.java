package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

public class Paziente extends Utente {

	private LinkedList<Prenotazione> listaPrenotazioni = new LinkedList<>();



	public Paziente(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita, String email, String password, Date dataDiNascita) {
		super(id, nome, cognome, cf, telefono, luogoDiNascita, email, password, dataDiNascita);
	}

    public Paziente() {

    }
}
