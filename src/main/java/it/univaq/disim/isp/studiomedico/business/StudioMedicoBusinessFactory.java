package it.univaq.disim.isp.studiomedico.business;

import it.univaq.disim.isp.studiomedico.business.impl.BusinessFactoryDB;


public abstract class StudioMedicoBusinessFactory {


    private static final StudioMedicoBusinessFactory factory = new BusinessFactoryDB();

    public static StudioMedicoBusinessFactory getInstance() {
        return factory;
    }

    public abstract UtenteService getUtenteService();
}
