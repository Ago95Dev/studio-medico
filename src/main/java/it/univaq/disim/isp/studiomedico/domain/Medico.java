package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;
import java.util.Set;

public class Medico extends Paziente {

	private Specializzazione specializzazione;
	private Integer numeropresenze, numerovisite;
	private Contratto contratto;
	private Set<Turno> listaTurni;

	public Medico(Integer id, String nome, String cognome, String cf, String telefono, String luogoDiNascita, String email, String password, Date dataDiNascita, Specializzazione specializzazione) {
		super(id, nome, cognome, cf, telefono, luogoDiNascita, email, password, dataDiNascita);
		this.specializzazione=specializzazione;
	}
    public Medico() {
        super();
        Specializzazione s;
    }

    // public Medico(String nome, String cognome, String mail, String password) {
	//	super(nome, cognome, mail, password);

	public Specializzazione getSpecializzazione() {
		return specializzazione;
	}

	public void setSpecializzazione(Specializzazione specializzazione) {
		this.specializzazione = specializzazione;
	}

	public Integer getNumeropresenze() {
		return numeropresenze;
	}

	public void setNumeropresenze(Integer numeropresenze) {
		this.numeropresenze = numeropresenze;
	}

	public Integer getNumerovisite() {
		return numerovisite;
	}

	public void setNumerovisite(Integer numerovisite) {
		this.numerovisite = numerovisite;
	}

	public Contratto getContratto() {
		return contratto;
	}

	public void setContratto(Contratto contratto) {
		this.contratto = contratto;
	}

	public Set<Turno> getListaTurni() {
		return listaTurni;
	}

	public void setListaTurni(Set<Turno> listaTurni) {
		this.listaTurni = listaTurni;
	}

	public String getStringfromContratto(TipologiaContratto contratto){
		String c = null;
		switch (contratto) {
			case FORFETTARIO:
				c = "Forfettario";
				break;
			case PRESENZE:
				c = "Presenze";
				break;
			case PRESTAZIONI:
				c = "Prestazioni";
				break;
		}
		return c;
	}

	public String getStringfromSpecializzazione(Specializzazione specializzazione){
		String c = null;
		switch (specializzazione) {
			case FISIOTERAPIA:
				c = "Fisioterapia";
				break;
			case NUTRIZIONISTA:
				c = "Nutrizionista";
				break;
			case CARDIOLOGIA:
				c = "Cardiologia";
				break;
			case SENOLOGIA:
				c = "Senologia";
				break;
			case OTORINOLARINGOIATRIA:
				c = "Otorinolaringoiatria";
				break;
			case ORTOPEDIA:
				c = "Ortopedia";
				break;
			case UROLOGIA:
				c = "Urologia";
				break;
			case NEUROLOGIA:
				c = "Neurologia";
				break;
			case GASTROENTEROLOGIA:
				c = "Gastroenterologia";
				break;
			case ONCOLOGIA:
				c = "Oncologia";
				break;
			case NEUROCHIRURGIA:
				c = "Neurochirurgia";
				break;
			case MEDICINAINTERNA:
				c = "Medicina Interna";
				break;
			case GINECOLOGIA:
				c = "Ginecologia";
				break;
			case PSICOLOGIA:
				c = "Psicologia";
				break;
			case CHIRURGIAVASCOLARE:
				c = "Chirurgia Vascolare";
				break;
			case OSTETRICIA:
				c = "Ostetricia";
				break;
			case ANDROLOGIA:
				c = "Andrologia";
				break;
			case TRAUMATOLOGIA:
				c = "Traumatologia";
				break;
		}
		return c;
	}
}