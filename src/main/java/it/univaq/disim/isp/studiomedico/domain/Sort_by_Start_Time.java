package it.univaq.disim.isp.studiomedico.domain;

import java.util.Comparator;

public class Sort_by_Start_Time implements Comparator<Prenotazione> {

	@Override
	public int compare(Prenotazione p1, Prenotazione p2) {
		if (p1.getOrainizio().isBefore(p2.getOrainizio())) return -1;
		if (p1.getOrainizio().isAfter(p2.getOrainizio())) return 1;
		return 0;
	}




}
