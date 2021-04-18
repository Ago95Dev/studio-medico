package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;

public class BusinessFactoryDB extends StudioMedicoBusinessFactory {

    private UtenteService utenteService;
    private PrenotazioneService prenotazioneService;

    public BusinessFactoryDB() {

        try {
            utenteService = new UtenteServiceDB();
            prenotazioneService = new PrenotazioneServiceDB();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UtenteService getUtenteService() {
            return utenteService;
        }

    @Override
    public PrenotazioneService getPrenotazioneService() {
        return prenotazioneService;
    }

}
