package it.univaq.disim.isp.studiomedico.domain;

import java.time.Duration;

public class Visita {

	private int id;
	private String nome;
	private float prezzo;
	private Duration durata;

	public Visita(String nome, float prezzo, Duration durata) {
		this.nome = nome;
		this.prezzo = prezzo;
		this.durata = durata;
	}

	public Visita(){}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
		this.durata = durata;
	}

	public void setId(int id) {
	}

	public String stampaDurata (){
		if (this.durata.equals(Duration.ofMinutes(30))) {
			return this.durata.toMinutes() + "m";
		}
		else {
			return this.durata.toHours() + "h";
		}
	}

	public String stampaPrezzo() {
		return this.prezzo + "€";
	}
}
