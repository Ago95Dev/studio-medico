package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PrenotazioneServiceDB extends ConnessioneDB implements PrenotazioneService {

    private static Connection con;

    private UtenteServiceDB utenteservice;

    public PrenotazioneServiceDB() throws BusinessException {

        try {
            con = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new BusinessException("Errore di connessione");
        }
    }

    // query che data una Visita restituisce la Specializzazione associata
    @Override
    public int getIdSpecializzazioneByVisita (String visita){
    	String query = "select specializzazione_di_competenza from tipi_visita where tipo=?";
    	Integer idSpecializzazione = null;
    	try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,visita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    idSpecializzazione = rs.getInt("specializzazione_di_competenza");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
    	return idSpecializzazione;
    }

    // query che data una Specializzazione restituisce la lista dei Medici associati a quella Specializzazione
    @Override
    public List<Medico> getMediciByIdSpecializzazione (int id_specializzazione){
        String query = "select * from utenti where id_specializzazione=?";
        List<Medico> listaMedici = new LinkedList<>();
        Utente utente = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_specializzazione);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    switch (rs.getString("ruolo")) {
                        case "medico":
                            utente = new Medico();
                            break;
                        case "segretaria":
                            utente = new Segretaria();
                            break;
                        case "paziente":
                            utente = new Paziente();
                            break;
                    }
                    Objects.requireNonNull(utente).setId(rs.getInt("id"));
                    utente.setEmail(rs.getString("email"));
                    utente.setCf(rs.getString("codice_fiscale"));
                    utente.setNome(rs.getString("nome"));
                    utente.setCognome(rs.getString("cognome"));
                    utente.setPassword("password");
                    utente.setTelefono(rs.getString("telefono"));
                    utente.setLuogoDiNascita(rs.getString("luogo_di_nascita"));
                    utente.setDataDiNascita(rs.getDate("data_di_nascita"));
                    if (utente instanceof Medico){
                        //((Medico) utente).setSpecializzazione(utenteservice.findSpecializzazionebyId(rs.getInt("id_specializzazione")));
                        //((Medico) utente).setContratto(utenteservice.findContrattobyId(rs.getInt("id_contratto")));
                        //((Medico) utente).setListaTurni("query su turni in base al medico");
                        ((Medico) utente).setNumeropresenze(rs.getInt("numeropresenze"));
                        ((Medico) utente).setNumerovisite(rs.getInt("numeroprestazioni"));
                        listaMedici.add((Medico) utente);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaMedici;
    }

    @Override
    public List<Turno> getTurniByMedico(Medico medico) {
        return null;
    }

    // query che dato un Medico restituisce la lista dei turni di quel Medico
    @Override
    public List<Turno> getTurniByMedico(String nome, String cognome) throws BusinessException{
        String query = "select id from utenti where nome=? and cognome=? and ruolo=?";
        String query2 = "select * from turni where id_medico=? and accettato=1";
        Integer id_medico = null;
        List<Turno> listaTurni = new LinkedList<>();
        try (PreparedStatement s = con.prepareStatement(query)) {
            s.setString(1, nome);
            s.setString(2, cognome);
            s.setString(3, "medico");
            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                    id_medico = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new UtenteNotFoundException("Errore esecuzione query", e);
        }

        try (PreparedStatement s2 = con.prepareStatement(query2)) {
            s2.setInt(1, id_medico);
            try (ResultSet rs = s2.executeQuery()) {
                while (rs.next()) {
                   Turno turno = new Turno();
                   turno.setId(rs.getInt("id"));
                   turno.setData(rs.getDate("data").toLocalDate());
                   turno.setOrainizio(rs.getTime("ora_inizio").toLocalTime());
                   turno.setOrafine(rs.getTime("ora_fine").toLocalTime());
                   turno.setAccettato(rs.getBoolean("accettato"));
                   turno.setIncorso(rs.getBoolean("in_corso"));
                   listaTurni.add(turno);
                }
            }
        } catch (SQLException e) {
            throw new UtenteNotFoundException("Errore esecuzione query", e);
        }
        return listaTurni;
    }

    // query che dato un Turno di un Medico restituisce la lista delle Prenotazioni effettuate in quel Turno
    @Override
    public List<Prenotazione> getPrenotazioniByTurno(Turno turno){

        return null;
    }

    @Override
    public List<Medico> effettuaRicercaPrenotazione (String visita){
    	return getMediciByIdSpecializzazione(getIdSpecializzazioneByVisita(visita));
    }
}