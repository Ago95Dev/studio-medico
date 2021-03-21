package it.univaq.disim.isp.studiomedico.business.impl.jdbc;

public class ConnessioneDB {
    private static final String DB_NAME = "studio_medico";
    protected static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME + "?noAccessToProcedureBodies=true" + "&serverTimezone=Europe/Rome";
    protected static final String DB_USER = "a2fl";
    protected static final String DB_PASSWORD = "password";
}

