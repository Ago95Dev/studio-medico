package it.univaq.disim.isp.studiomedico.business.Implementations.db;

public class BusinessFactoryDB extends StudioMedicoBusinessFactory {

        private UtenteService utenteService;

        public DBBusinessFactory() {

            try {
                utenteService = new UtenteServiziDB();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public UtenteService getUtenteService() {
            return utenteService;
        }



    }





}
