package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;

public class Utente {

	private Integer id;
	private String nome,cognome,cf,telefono,luogoDiNascita,email,password;
	private Date dataDiNascita;

	public Utente(){}

	public Utente(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita,
			String email, String password, Date dataDiNascita) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.cf = cf;
		this.telefono = telefono;
		this.luogoDiNascita = luogoDiNascita;
		this.email = email;
		this.password = password;
		this.dataDiNascita = dataDiNascita;
	}

	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getCf() {
		return cf;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getLuogoDiNascita() {
		return luogoDiNascita;
	}
	public void setLuogoDiNascita(String luogoDiNascita) {
		this.luogoDiNascita = luogoDiNascita;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getDataDiNascita() {
		return dataDiNascita;
	}
	public void setDataDiNascita(Date dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	
}
