package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;

public class Segretaria extends Paziente {


	public Segretaria(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita, String email, String password, Date dataDiNascita) {
		super(id, nome, cognome, cf, telefono, luogoDiNascita, email, password, dataDiNascita);
	}
}
