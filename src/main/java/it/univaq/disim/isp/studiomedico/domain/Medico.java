package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;
import java.util.Set;

public class Medico extends Paziente {

	private Specializzazione specializzazione = Specializzazione.CARDIOLOGIA;
	private Integer numeropresenze, numerovisite;
	private Contratto contratto;
	private Set<Turno> listaTurni;

	public Medico(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita, String email, String password, Date dataDiNascita) {
		super(id, nome, cognome, cf, telefono, luogoDiNascita, email, password, dataDiNascita);
	}

	// public Medico(String nome, String cognome, String mail, String password) {
	//	super(nome, cognome, mail, password);
	}
	

