package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;

public class Medico extends Paziente{

	public Medico(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita, String email, String password, Date dataDiNascita) {
		super(id, nome, cognome, cf, telefono, luogoDiNascita, email, password, dataDiNascita);
	}

	// public Medico(String nome, String cognome, String mail, String password) {
	//	super(nome, cognome, mail, password);
	}
	

