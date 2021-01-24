package domain;

import java.util.Date;

public class Visita {
	
	private Medico medico ;
	private Paziente paziente;
	private Date data;

	public Visita(Medico m,Paziente p,Date data) {
		this.medico=m;
		this.paziente=p;
		this.data=data;
	}
	
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	public Paziente getPaziente() {
		return paziente;
	}
	public void setPaziente(Paziente paziente) {
		this.paziente = paziente;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}


	
}
