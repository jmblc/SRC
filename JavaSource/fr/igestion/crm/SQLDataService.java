package fr.igestion.crm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts.action.DynaActionForm;

import common.Logger;
import fr.igestion.annuaire.bean.Personne;
import fr.igestion.annuaire.bean.Utilisateur;
import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.Appelant;
import fr.igestion.crm.bean.Application;
import fr.igestion.crm.bean.ComptageSalaries;
import fr.igestion.crm.bean.DetailObjet;
import fr.igestion.crm.bean.GarantieRecherche;
import fr.igestion.crm.bean.InfosBDD;
import fr.igestion.crm.bean.InfosDeSession;
import fr.igestion.crm.bean.LibelleCode;
import fr.igestion.crm.bean.LigneDVS;
import fr.igestion.crm.bean.LigneExcel;
import fr.igestion.crm.bean.Limite;
import fr.igestion.crm.bean.Message;
import fr.igestion.crm.bean.ModeleEdition;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.ObjetAppelant;
import fr.igestion.crm.bean.ObjetRecherche;
import fr.igestion.crm.bean.PostItBeneficiaire;
import fr.igestion.crm.bean.PostItEtablissement;
import fr.igestion.crm.bean.Salarie;
import fr.igestion.crm.bean.StreamFile;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.Transfert;
import fr.igestion.crm.bean.VersionGarantie;
import fr.igestion.crm.bean.contrat.AbonnementService;
import fr.igestion.crm.bean.contrat.Acte;
import fr.igestion.crm.bean.contrat.Adresse;
import fr.igestion.crm.bean.contrat.AyantDroit;
import fr.igestion.crm.bean.contrat.Beneficiaire;
import fr.igestion.crm.bean.contrat.Camp_EntiteGestion;
import fr.igestion.crm.bean.contrat.ContratBeneficiaire;
import fr.igestion.crm.bean.contrat.ContratEtablissement;
import fr.igestion.crm.bean.contrat.Couverture;
import fr.igestion.crm.bean.contrat.Decompte;
import fr.igestion.crm.bean.contrat.DetailContratBeneficiaire;
import fr.igestion.crm.bean.contrat.DetailContratEtablissement;
import fr.igestion.crm.bean.contrat.EntiteGestion;
import fr.igestion.crm.bean.contrat.Etablissement;
import fr.igestion.crm.bean.contrat.InfosRO;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.contrat.Prestation;
import fr.igestion.crm.bean.contrat.RIB;
import fr.igestion.crm.bean.contrat.SiteWeb;
import fr.igestion.crm.bean.document.Document;
import fr.igestion.crm.bean.evenement.ComplementsInfosEvenement;
import fr.igestion.crm.bean.evenement.Evenement;
import fr.igestion.crm.bean.evenement.Historique;
import fr.igestion.crm.bean.pec.BasePEC;
import fr.igestion.crm.bean.pec.DemandePec;
import fr.igestion.crm.bean.pec.InfoPec;
import fr.igestion.crm.bean.pec.ModelePEC;
import fr.igestion.crm.bean.pec.Pec;
import fr.igestion.crm.bean.scenario.AdresseGestion;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.InfosScenario;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.ReferenceStatistique;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;
import oracle.jdbc.OracleTypes;

public class SQLDataService {

    private static final Logger LOGGER = Logger.getLogger(SQLDataService.class);
    private static final String _AnoLiberationConn = "Libération connexion impossible";
    private static final String _AnoRollBack = "rollback impossible";

    private static SQLDataService _instance;
    private static Properties _prop; // on sait pas à quoi ça sert mais dans le cas d'une usine à injection de merde, on garde au cas où
    private static DateFormat _fmtddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static final String _UNION = " UNION ";

    public static int _max_nbr_fiches_recherchees = 3000;
    public static int _Max_Rows = 100;

    public static String _evenement_pec_id = "5";
    public static String _evenement_sous_motif_piece_manquante_id = "98";

    public static String _entite_SansObjet = "122";

    public static String _TYPE_ASSURE_HB = "148";
    public static String _TYPE_PS = "152";

    public static String _SERVEUR_SMTP = null;
    
    public static String _hermes_login = "IGESTION";
    
    private static final String _ON = "on";
    private static final String _EMPTY = "-1";
    private static final String _VRAI = "1";
    private static final String _FAUX = "0";
    
    private static String _SORTANT;
    private static String _ENTRANT;
    
    private static String _MediaCourriel;
    private static String _MediaAppel;
    private static String _MediaFax;
    
    private static String _AppelMotif; 
    private static String _MotDePasseSousMotif;
    private static String _PECSousMotif;
    public static final String _ssMotifPECHospit = "1001";
    
    private static String _CourrielMotif;
    private static String _FaxMotif;
    private static String _ProcedureMailSousMotif;
    private static String _DemandePECMotif;
    
    private static String _DemandePECGEDCode;
    private static String _prioriteDemandePEC;
    
    private static String _userROBOT;
    private static String _siteMARSEILLE;
    private static String _TCOHContacts;
    
    private static String _StatutATraiter;
    private static String _StatutEnvoye;
    private static String _StatutAttente;
    private static String _StatutAEmettre;
    private static String _StatutAnnule;
    
    private static String _PecV;
    private static String _PecDemande;
    private static String _EvtMotDePasseWeb;
    
    private static String _CodeFicheTransfert;
    
    private static String _EvenementAppel;
    
    private static String _TypeDossierHContact;
    
    private SQLDataService() {
    }
    
    public static SQLDataService getInstance() throws NamingException {

        if (_instance == null) {

            _instance = new SQLDataService();

            Context initContext = new InitialContext();
            
            String hermes_login = null; 
            try {
            	hermes_login = (String) initContext.lookup("java:comp/env/hcontact_hermes_login");
            	if (hermes_login != null) {
            		_hermes_login = hermes_login;
            	}
            } catch (NamingException e) {
            	e.printStackTrace();
            }

            _max_nbr_fiches_recherchees = ((Integer) initContext
                    .lookup("java:comp/env/hcontact_max_nbr_fiches_recherchees"))
                    .intValue();
                                           
            _Max_Rows = ((Integer) initContext
                    .lookup("java:comp/env/hcontact_Max_Rows")).intValue();

            _entite_SansObjet = (String) initContext
                    .lookup("java:comp/env/hcontact_entite_SansObjet");

            _TYPE_ASSURE_HB = (String) initContext
                    .lookup("java:comp/env/hcontact_TYPE_ASSURE_HB");
            _TYPE_PS = (String) initContext
                    .lookup("java:comp/env/hcontact_TYPE_PS");

            _SERVEUR_SMTP = (String) initContext
                    .lookup("java:comp/env/hcontact_serveur_smtp");

            _evenement_sous_motif_piece_manquante_id = (String) initContext
                    .lookup("java:comp/env/evenement_sous_motif_piece_manquante_id");
            
            // Configuration H.Courriers
            _MotDePasseSousMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_MotDePasseSousMotif");
            
            _SORTANT=(String) initContext
                    .lookup("java:comp/env/hcourrier_SORTANT");
            _ENTRANT=(String) initContext
                    .lookup("java:comp/env/hcourrier_ENTRANT");
            
            _MediaCourriel=(String) initContext
                    .lookup("java:comp/env/hcourrier_MediaCourriel");
            _MediaAppel=(String) initContext
                    .lookup("java:comp/env/hcourrier_MediaAppel");
            _MediaFax=(String) initContext
                    .lookup("java:comp/env/hcourrier_MediaFax");
            
            _AppelMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_AppelMotif"); 
            _MotDePasseSousMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_MotDePasseSousMotif");
            _PECSousMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_PECSousMotif");
            _CourrielMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_CourrielMotif");
            _FaxMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_FaxMotif");
            _ProcedureMailSousMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_ProcedureMailSousMotif");
            _DemandePECMotif=(String) initContext
                    .lookup("java:comp/env/hcourrier_DemandePECMotif");
            
            _DemandePECGEDCode=(String) initContext
                    .lookup("java:comp/env/hcourrier_DemandePECGEDCode");
            _prioriteDemandePEC=(String) initContext
                    .lookup("java:comp/env/hcourrier_prioriteDemandePEC");
            
            _userROBOT = (String) initContext
                    .lookup("java:comp/env/hcourrier_userROBOT");
            _siteMARSEILLE = (String) initContext
                    .lookup("java:comp/env/hcourrier_siteMARSEILLE");
            _TCOHContacts = (String) initContext
                    .lookup("java:comp/env/hcourrier_TCOHContacts");
            
            _StatutATraiter= (String) initContext
                    .lookup("java:comp/env/hcourrier_StatutATraiter");
            _StatutEnvoye= (String) initContext
                    .lookup("java:comp/env/hcourrier_StatutEnvoye");
            _StatutAttente= (String) initContext
                    .lookup("java:comp/env/hcourrier_StatutAttente");
            _StatutAEmettre=(String) initContext
                    .lookup("java:comp/env/hcourrier_StatutAEmettre");
            
            _StatutAnnule=(String) initContext
                    .lookup("java:comp/env/hcourrier_StatutAnnule");
            
            _PecV=(String) initContext
                    .lookup("java:comp/env/hcourrier_PecV");
            _PecDemande=(String) initContext
                    .lookup("java:comp/env/hcourrier_PecDemande");
            _EvtMotDePasseWeb= (String) initContext
                    .lookup("java:comp/env/hcourrier_EvtMotDePasseWeb");
            _CodeFicheTransfert=(String) initContext
                    .lookup("java:comp/env/hcourrier_CodeFicheTransfert");
            _EvenementAppel=(String) initContext
                    .lookup("java:comp/env/hcourrier_EvenementAppel");
            _TypeDossierHContact=(String) initContext
                    .lookup("java:comp/env/hcourrier_TypeDossierHContact");
            
        }
        return _instance;
    }

    private static void closeRsStmtConn(ResultSet rs, Statement stmt, Connection conn){
        try {
            if( rs != null ){
                rs.close();
            }
            if( stmt != null ){
                stmt.close();
            }
            if( conn != null ){
                conn.close();
            }    
        } catch (Exception e) {
            LOGGER.error(_AnoLiberationConn, e);
        }
    }
    
    private static void closeStmtConn( Statement stmt, Connection conn){
        try {
            if( stmt != null ){
                stmt.close();
            }
            if( conn != null ){
                conn.close();
            }    
        } catch (Exception e) {
            LOGGER.error(_AnoLiberationConn, e);
        }
    }
    
    public static String get_SERVEUR_SMTP() {
        return _SERVEUR_SMTP;
    }
    
    private static final String isOn(HttpServletRequest request, String param){
        return (_ON.equals(request.getParameter(param)))?_VRAI:_FAUX;
    }
    
    public static Connection getConnexion() {

        Connection conn = null;
        
        try{
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/hcontacts");
            conn = ds.getConnection(); 
        } catch(Exception e){
            throw new IContactsException("getConnexion",e);
        }
        return conn;

    }

    public static Connection getConnexionOracle() {

       
        Connection connection = null;

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            
            Context context = new InitialContext();
            String user = (String) context
                    .lookup("java:comp/env/hcontact_user");
            String password = (String) context
                    .lookup("java:comp/env/hcontact_password");
            String url = (String) context.lookup("java:comp/env/hcontact_url");

            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new IContactsException(e);
        }
        return connection;
    }

    public static Connection getConnexionBaseHisto() {

        Connection conn = null;

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/ged_histo");
            conn = ds.getConnection();
        } catch (Exception e) {
            throw new IContactsException("getConnexionBaseHisto",e);
        }

        return conn;

    }

    public static boolean positionnerModule(String nom_module) {

        Connection conn = null;
        CallableStatement stmt = null;

        try {
            String requete = "{ call DBMS_APPLICATION_INFO.SET_MODULE (?,null) }";

            conn = getConnexion();
            stmt = conn.prepareCall(requete);
            stmt.setString(1, nom_module);
            stmt.execute();

            stmt.clearParameters();
            return true;
        } catch (Exception e) {
            throw new IContactsException("positionnerModule",e);
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String getValeurParametrage(String uneCle)
            throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";
        try {

            String requete = "SELECT p.VALEUR from HOTLINE.PARAMETRAGE p WHERE  p.CLE = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, uneCle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            return res;
        } catch (Exception e) {
            throw new IContactsException("getValeurParametrage",e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Utilisateur getUtilisateurByLogin(String loginUtilisateur) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Utilisateur unDisplay = null;

        try {
            String requete = "SELECT UTL_ID, UTL_PRS_ID, UTL_LOGIN, UTL_PASSWORD FROM H_ANNUAIRE.T_UTILISATEURS_UTL where UTL_LOGIN = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, loginUtilisateur);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Utilisateur();
                unDisplay.setUTL_ID((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setUTL_PRS_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setUTL_LOGIN((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setUTL_PASSWORD((rs.getString(4) != null) ? rs
                        .getString(4) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            throw new IContactsException("getUtilisateurByLogin",e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Personne getPersonne(String idPersonne) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Personne unDisplay = null;

        try {
            String requete = "SELECT prs.PRS_ID, prs.PRS_NOM, prs.PRS_PRENOM, prs.PRS_NUMSIRET, prs.PRS_CODNAF, prs.PRS_DTENAISS, "
                    + "prs.PRS_CVL_ID, prs.PRS_SEXE, prs.PRS_CTG_ID, prs.PRS_ATV_ID, prs.PRS_PCJ_ID, prs.PRS_TYPE, prs.PRS_PRIVE, prs.PRS_ACTIF, "
                    + "prs.PRS_VIP, prs.PRS_PRS_ID_SUPERIEUR, prs.PRS_PRS_ID_MAISONMERE, prs.PRS_PRS_ID_DIRIGEANT, prs.PRS_PRS_ID_CREATEUR, "
                    + "prs.PRS_MATRICULE, prs_mor.prs_nom "
                    + "FROM H_ANNUAIRE.T_PERSONNES_PRS prs, H_ANNUAIRE.T_PERSONNESEMPLOIS_PEM pem, H_ANNUAIRE.T_PERSONNES_PRS prs_mor "
                    + "WHERE prs.PRS_ID = ? AND prs_mor.prs_id(+) = pem.pem_prs_id_employeur AND prs.prs_id = pem.pem_prs_id_employe(+) ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPersonne);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Personne();

                unDisplay.setPRS_ID((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setPRS_NOM((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setPRS_PRENOM((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setPRS_NUMSIRET((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setPRS_CODNAF((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setPRS_DTENAISS(rs.getTimestamp(6));
                unDisplay.setPRS_CVL_ID((rs.getString(7) != null) ? rs
                        .getString(7) : "");

                unDisplay.setPRS_SEXE((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setPRS_CTG_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setPRS_ATV_ID((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setPRS_PCJ_ID((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setPRS_TYPE((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setPRS_PRIVE((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setPRS_ACTIF((rs.getString(14) != null) ? rs
                        .getString(14) : "");

                unDisplay.setPRS_VIP((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay
                        .setPRS_PRS_ID_SUPERIEUR((rs.getString(16) != null) ? rs
                                .getString(16) : "");
                unDisplay
                        .setPRS_PRS_ID_MAISONMERE((rs.getString(17) != null) ? rs
                                .getString(17) : "");
                unDisplay
                        .setPRS_PRS_ID_DIRIGEANT((rs.getString(18) != null) ? rs
                                .getString(18) : "");
                unDisplay
                        .setPRS_PRS_ID_CREATEUR((rs.getString(19) != null) ? rs
                                .getString(19) : "");
                unDisplay.setPRS_MATRICULE((rs.getString(20) != null) ? rs
                        .getString(20) : "");

                unDisplay.setEntreprise((rs.getString(21) != null) ? rs
                        .getString(21) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            throw new IContactsException("getPersonne", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Application getApplication(String aliasApplication) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Application unDisplay = null;
        
        try {
            
            String requete = "SELECT APP_ID, APP_LIB, APP_ALIAS, APP_DESCRIPTION, APP_CHEMINEXE, APP_LIBREACCES, APP_ACTIF "
                    + "FROM H_ANNUAIRE.T_APPLICATIONS_APP WHERE APP_ALIAS = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, aliasApplication);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Application();

                unDisplay.setAPP_ID((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setAPP_LIB((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setAPP_ALIAS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setAPP_DESCRIPTION((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setAPP_CHEMINEXE((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setAPP_LIBREACCES((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setAPP_ACTIF((rs.getString(7) != null) ? rs
                        .getString(7) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            throw new IContactsException("getApplication", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static TeleActeur getTeleActeurByLogin(String loginTeleActeur) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            TeleActeur unDisplay = null;
            String requete = "SELECT t.PASSWORD, t.LOGIN, t.LOGINTEL, t.CIVILITE_CODE, t.NOM, t.PRENOM, "
                    + "t.EMAIL, t.ROLE, t.POSTE, t.ACDNAME, t.ACTIF, t.SITE, c.LIBELLE, t.ID, t.UTL_ID, t.ONGLETSFICHES "
                    + "FROM HOTLINE.TELEACTEUR t, HOTLINE.CODES c "
                    + "WHERE t.LOGIN = ? and t.CIVILITE_CODE = c.CODE(+)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, loginTeleActeur);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new TeleActeur();
                unDisplay.setPassword((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setLogin((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setExtension((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setCodeCivilite((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNom((rs.getString(5) != null) ? rs.getString(5)
                        : "");
                unDisplay.setPrenom((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setEmail((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setRole((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setPoste((rs.getString(9) != null) ? rs.getString(9)
                        : "");
                unDisplay.setAcdName((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setActif((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setSite((rs.getString(12) != null) ? rs.getString(12)
                        : "");
                unDisplay.setLibelleCivilite((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setId(rs.getString(14));
                unDisplay.setUtl_Id((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setOngletsFiches(rs.getString(16));
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            throw new IContactsException("getTeleActeurByLogin", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static TeleActeur getTeleActeurById(String idTeleActeur) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            TeleActeur unDisplay = null;
            String requete = "SELECT t.PASSWORD, t.LOGIN, t.LOGINTEL, t.CIVILITE_CODE, t.NOM, t.PRENOM, "
                    + "t.EMAIL, t.ROLE, t.POSTE, t.ACDNAME, t.ACTIF, t.SITE, c.LIBELLE, t.ID, t.UTL_ID, t.ONGLETSFICHES "
                    + "FROM HOTLINE.TELEACTEUR t, HOTLINE.CODES c  "
                    + "WHERE t.ID = ? and t.CIVILITE_CODE = c.CODE(+) ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idTeleActeur);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new TeleActeur();
                unDisplay.setPassword((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setLogin((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setExtension((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setCodeCivilite((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNom((rs.getString(5) != null) ? rs.getString(5)
                        : "");
                unDisplay.setPrenom((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setEmail((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setRole((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setPoste((rs.getString(9) != null) ? rs.getString(9)
                        : "");
                unDisplay.setAcdName((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setActif((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setSite((rs.getString(12) != null) ? rs.getString(12)
                        : "");
                unDisplay.setLibelleCivilite((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setId(rs.getString(14));
                unDisplay.setUtl_Id((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setOngletsFiches(rs.getString(16));
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            throw new IContactsException("getTeleActeurById", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<TeleActeur> getTeleActeursHabilitesSurEntiteGestionSensible(
            String entite_gestion_sensible_id, String ne_pas_prendre_les_exclus) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<TeleActeur> res = new ArrayList<TeleActeur>();

        try {
            conn = getConnexion();
            TeleActeur unDisplay = null;
            String requete = "SELECT distinct t.ID, c.LIBELLE, t.NOM, t.PRENOM, "
                    + "prs_mor.prs_nom as societe, srv.srv_lib service, pol.pol_lib pole, pem.PEM_POSTE, t.ACTIF  "
                    + "FROM HOTLINE.TELEACTEUR t, HOTLINE.CODES c, "
                    + "h_annuaire.t_utilisateurs_utl utl, H_ANNUAIRE.t_personnes_prs prs, "
                    + "H_ANNUAIRE.t_personnes_prs prs_mor, H_ANNUAIRE.t_personnesemplois_pem pem, H_ANNUAIRE.t_personnesservicespoles_psp psp, "
                    + "H_ANNUAIRE.t_services_srv srv, H_ANNUAIRE.t_poles_pol pol, HOTLINE.TELEACTEURENTITEGESTIONBL egbl "
                    + "WHERE t.CIVILITE_CODE = c.CODE(+) and t.UTL_ID = utl.UTL_ID(+) and utl.UTL_PRS_ID = prs.PRS_ID(+) "
                    + "AND prs_mor.prs_id(+) = pem.pem_prs_id_employeur AND prs.prs_id = pem.PEM_PRS_ID_EMPLOYE(+) "
                    + "and prs.prs_id = psp.psp_prs_id(+) AND psp.psp_srv_id = srv.srv_id(+) AND psp.psp_pol_id = pol.pol_id(+) "
                    + "and t.ID = egbl.TELEACTEUR_ID AND egbl.ENTITEGESTION_ID = ? ";
            if (_VRAI.equals(ne_pas_prendre_les_exclus)) {
                requete += " AND t.EXCLU_MESSAGE_CONFIDENTIALITE = 0 ";
            }
            requete += " ORDER BY 3 ASC, 4 ASC ";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, entite_gestion_sensible_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new TeleActeur();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelleCivilite((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setNom((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setPrenom((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay.setSociete((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setService((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setPole((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setPoste((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setActif((rs.getString(9) != null) ? rs.getString(9)
                        : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTeleActeursHabilitesSurEntiteGestionSensible", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<TeleActeur> getTeleActeurs(String actif_ou_pas,
            String lettre_demandee) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<TeleActeur> res = new ArrayList<TeleActeur>();
        try {
            conn = getConnexion();
            TeleActeur unDisplay = null;
            String requete = "SELECT distinct t.ID, c.LIBELLE, t.NOM, t.PRENOM, t.IDHERMES, t.ONGLETSFICHES, t.ACTIF, t.UTL_ID, "
                    + "prs_mor.prs_nom as societe, srv.srv_lib service, pol.pol_lib pole, hch.HCH_ADMINISTRATION, hch.HCH_STATISTIQUES, t.EXCLU_MESSAGE_CONFIDENTIALITE "
                    + "FROM HOTLINE.TELEACTEUR t, HOTLINE.CODES c, hotline.T_HCONTACTS_HABILITATIONS_HCH hch, "
                    + "h_annuaire.t_utilisateurs_utl utl, H_ANNUAIRE.t_personnes_prs prs, "
                    + "H_ANNUAIRE.t_personnes_prs prs_mor, H_ANNUAIRE.t_personnesemplois_pem pem, H_ANNUAIRE.t_personnesservicespoles_psp psp, "
                    + "H_ANNUAIRE.t_services_srv srv, H_ANNUAIRE.t_poles_pol pol "
                    + "WHERE t.CIVILITE_CODE = c.CODE(+) and t.UTL_ID = utl.UTL_ID(+) and utl.UTL_PRS_ID = prs.PRS_ID(+) "
                    + "AND prs_mor.prs_id(+) = pem.pem_prs_id_employeur AND prs.prs_id = pem.PEM_PRS_ID_EMPLOYE(+) "
                    + "and prs.prs_id = psp.psp_prs_id(+) AND psp.psp_srv_id = srv.srv_id(+) AND psp.psp_pol_id = pol.pol_id(+) "
                    + "and t.UTL_ID = hch.HCH_UTL_ID(+)";

            if (_VRAI.equals(actif_ou_pas)) {
                requete += " AND t.ACTIF = 1 ";
            }

            if (!"".equals(lettre_demandee)) {
                requete += " AND t.NOM like ? ";
            }

            requete += " ORDER BY 3 ASC, 4 ASC";

            stmt = conn.prepareStatement(requete);
            if (!"".equals(lettre_demandee)) {
                stmt.setString(1, lettre_demandee + "%");
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new TeleActeur();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelleCivilite((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setNom((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setPrenom((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay.setIdHermes((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setOngletsFiches((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setActif((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setUtl_Id((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setSociete((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setService((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setPole((rs.getString(11) != null) ? rs.getString(11)
                        : "");
                unDisplay.setHCH_ADMINISTRATION((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setHCH_STATISTIQUES((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay
                        .setEXCLU_MESSAGE_CONFIDENTIALITE((rs.getString(14) != null) ? rs
                                .getString(14) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTeleActeurs", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<EntiteGestion> getEntitesGestion() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<EntiteGestion> res = new ArrayList<EntiteGestion>();
        try {
            conn = getConnexion();
            EntiteGestion unDisplay = null;
            String requete = "select w.*, decode(egbl.ENTITEGESTION_ID, null, '0', '1') "
                    + "FROM (select distinct eg.ID, eg.CODE, eg.LIBELLE, mut.ID as mutuelle_id, mut.LIBELLE as mutuelle_libelle  "
                    + "FROM application.ENTITE_GESTION eg, application.mutuelle mut "
                    + "WHERE eg.MUTUELLE_ID = mut.id and eg.type = 'E') w "
                    + "left outer join hotline.ENTITEGESTIONBLACKLISTEE egbl on w.ID = egbl.ENTITEGESTION_ID "
                    + "ORDER by 5, 2, 3 asc ";
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMutuelle_id((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setMutuelle((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setSensible((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getEntitesGestion", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<EntiteGestion> getEntitesGestionSensibles() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<EntiteGestion> res = new ArrayList<EntiteGestion>();
        try {
            conn = getConnexion();
            EntiteGestion unDisplay = null;
            String requete = "SELECT eg.ID, eg.CODE, eg.LIBELLE, eg.MUTUELLE_ID, mut.LIBELLE, count(t.ID) "
                    + "FROM HOTLINE.ENTITEGESTIONBLACKLISTEE egbl "
                    + "INNER JOIN APPLICATION.ENTITE_GESTION eg on (egbl.ENTITEGESTION_ID = eg.ID and eg.type = 'E') "
                    + "INNER JOIN APPLICATION.MUTUELLE mut on eg.MUTUELLE_ID = mut.ID "
                    + "LEFT OUTER JOIN HOTLINE.TELEACTEURENTITEGESTIONBL tegbl on tegbl.ENTITEGESTION_ID = eg.id "
                    + "LEFT OUTER JOIN HOTLINE.TELEACTEUR t on TEGBL.TELEACTEUR_ID = t.ID AND t.ACTIF = 1 "
                    + "GROUP BY eg.ID, eg.CODE, eg.LIBELLE, eg.MUTUELLE_ID, mut.LIBELLE "
                    + "ORDER BY 5 ASC, 2 ASC, 3 ASC   ";
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMutuelle_id((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setMutuelle((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay
                        .setNbrTeleacteursActifsHabilites((rs.getString(6) != null) ? rs
                                .getString(6) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getEntitesGestionSensibles", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<EntiteGestion> getEntitesGestionSensiblesPourTeleActeur(
            String teleacteur_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<EntiteGestion> res = new ArrayList<EntiteGestion>();

        try {
            conn = getConnexion();
            EntiteGestion unDisplay = null;
            String requete = "SELECT eg.ID, eg.CODE, eg.LIBELLE "
                    + "FROM HOTLINE.TELEACTEURENTITEGESTIONBL egbl, APPLICATION.ENTITE_GESTION eg "
                    + "WHERE egbl.ENTITEGESTION_ID = eg.ID AND egbl.TELEACTEUR_ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getEntitesGestionSensiblesPourTeleActeur", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Message> getMessages() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Message> res = new ArrayList<Message>();
        try {
            conn = getConnexion();
            Message unDisplay = null;
            String requete = "SELECT c.libelle, m.ID, m.titre,"
                    + "CASE WHEN (LENGTH (m.titre) < 40) THEN m.titre WHEN m.titre IS NULL THEN '' ELSE SUBSTR (m.titre, 1, 40) || '...' END debut_titre, "
                    + "m.contenu, CASE WHEN (LENGTH (m.contenu) < 40) THEN m.contenu WHEN m.contenu IS NULL THEN '' ELSE SUBSTR (m.contenu, 1, 40) || '...' END debut_contenu, "
                    + "m.datedebut, m.datefin, m.campagne_id, case when m.DATEFIN is null and trunc(m.DATEDEBUT) <= trunc(sysdate) then '1' when trunc(m.datefin) >= trunc(sysdate) and trunc(m.datedebut) <= trunc(sysdate) then '1' else '0' end "
                    + "FROM hotline.MESSAGE m, hotline.campagne c where c.id = m.CAMPAGNE_ID  order by 10 desc, 1 asc";

            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Message();
                unDisplay.setCampagne(rs.getString(1));
                unDisplay.setID(rs.getString(2));
                unDisplay.setTITRE((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setDebutTitre((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCONTENU((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setDebutContenu((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setDATEDEBUT((rs.getTimestamp(7) != null) ? rs
                        .getTimestamp(7) : null);
                unDisplay.setDATEFIN((rs.getTimestamp(8) != null) ? rs
                        .getTimestamp(8) : null);
                unDisplay.setCAMPAGNE_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setActif((rs.getString(10) != null) ? rs
                        .getString(10) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getMessages", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Transfert> getTransferts() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Transfert> res = new ArrayList<Transfert>();
        try {
            conn = getConnexion();
            Transfert unDisplay = null;
            String requete = "SELECT T.TRA_ID, T.TRA_LIBELLE, T.TRA_EMAIL "
                    + "FROM HOTLINE.T_TRANSFERTS_TRA T order by 2 asc";

            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Transfert();
                unDisplay.setTRA_ID(rs.getString(1));
                unDisplay.setTRA_LIBELLE((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setTRA_EMAIL((rs.getString(3) != null) ? rs
                        .getString(3) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTransferts", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getDroitUtilisationApplication(
            String aliasApplication, String idUtilisateur) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";
        try {

            String requete = "SELECT COUNT (*) FROM h_annuaire.T_UTILISATEURSAPPLICATIONS_UAP uap, h_annuaire.T_APPLICATIONS_APP app WHERE app.app_alias = ?  AND uap.uap_utl_id = ? AND app.app_id = uap.uap_app_id";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, aliasApplication);
            stmt.setString(2, idUtilisateur);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }

            stmt.clearParameters();

            return res;
        } catch (Exception e) {
            throw new IContactsException("getDroitUtilisationApplication", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Map<String, String> getHabilitationsUser(String idUtilisateur) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, String> res = new HashMap<String, String>();
        try {

            String requete = "SELECT * "
                    + " FROM HOTLINE.T_HCONTACTS_HABILITATIONS_HCH hch WHERE hch.HCH_UTL_ID = ? ";
            conn = getConnexion();

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idUtilisateur);

            rs = stmt.executeQuery();
            int nbr_colonnes = rs.getMetaData().getColumnCount();
            if (rs.next()) {
                for (int i = 0; i < nbr_colonnes; i++) {
                    res.put(rs.getMetaData().getColumnName(i + 1),
                            rs.getString(i + 1));
                }
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getHabilitationsUser", e); 
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getSatisfactions() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();
        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE FROM HOTLINE.CODES s WHERE s.jdoclass='hosta.crm.impl.codes.Satisfaction' and s.ALIAS in( 'NEUTRE', 'SATISFAIT', 'INSATISFAIT', 'DANGER') order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getSatisfactions", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getTypesAppelantsAutre() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();
        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE FROM HOTLINE.CODES s WHERE s.jdoclass='hosta.crm.impl.codes.TypeAppelant' "
                    + "and s.ALIAS in( 'HB', 'ASSUREUR', 'COURTIER', 'DELEGUE', 'PROFSANTE', 'PROSPECT', 'AUTRE') order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTypesAppelantsAutre", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getTypesAppelantsRecherche() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE "
                    + "FROM HOTLINE.CODES s WHERE s.jdoclass='hosta.crm.impl.codes.TypeAppelant' "
                    + "and s.ALIAS in( 'ASSURE', 'ENTREPRISE', 'HB', 'ASSUREUR', 'COURTIER', 'DELEGUE', 'PROFSANTE', 'PROSPECT', 'AUTRE') order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTypesAppelantsRecherche", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getCodesClotures() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<LibelleCode> res = new ArrayList<LibelleCode>();
        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ALIAS, s.ORDRE FROM HOTLINE.CODES s "
                    + "WHERE s.JDOCLASS = 'hosta.crm.impl.codes.Cloture' "
                    + "and s.ALIAS in(  'CLOTURE', 'ATRAITER',  'APPELSORTANT', 'HORSCIBLE', 'AUTRECAMP' ) order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setAlias((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getCodesClotures", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getCodesCloturesRecherche() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ALIAS, s.ORDRE FROM HOTLINE.CODES s "
                    + "WHERE s.JDOCLASS = 'hosta.crm.impl.codes.Cloture' "
                    + "and s.ALIAS in(  'CLOTURE', 'ATRAITER',  'APPELSORTANT', 'HORSCIBLE', 'ACQUITTEMENT') order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setAlias((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getCodesCloturesRecherche", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getCodeClotureByAlias(String alias) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";
        try {
            String requete = "SELECT s.CODE FROM HOTLINE.CODES s WHERE s.JDOCLASS = 'hosta.crm.impl.codes.Cloture' and s.ALIAS =  ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, alias);

            rs = stmt.executeQuery();
            while (rs.next()) {
                res = (rs.getString(1) != null) ? rs.getString(1) : "";
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getCodeClotureByAlias", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getTypesAppelants() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE FROM HOTLINE.CODES s WHERE s.JDOCLASS = 'hosta.crm.impl.codes.TypeAppelant' and s.ALIAS in(  'ENTREPRISE', 'ASSURE',  'AUTRE' ) order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getTypesAppelants", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getSousStatuts() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT ID, LIBELLE FROM EVENEMENT.SOUS_STATUT ss where ss.ACTIF = 1  order by LIBELLE ASC";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            throw new IContactsException("getSousStatuts", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getSousStatutsForInputSelect() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT ID, LIBELLE FROM EVENEMENT.SOUS_STATUT ss where ss.ACTIF = 1  order by LIBELLE ASC";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            sb.append("<select class='swing_11' id='sous_statut_id' name='sous_statut_id'>");

            sb.append("<option selected='selected' value='-1'>Choisir une raison</option>");

            while (rs.next()) {
                sb.append("<option value=\"" + rs.getString(1) + "\">"
                        + rs.getString(2) + "</option>");
            }

            sb.append("</select>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            throw new IContactsException("getSousStatutsForInputSelect", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getTypesAppelantsForInputSelect(String mode) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE FROM HOTLINE.CODES s WHERE s.jdoclass='hosta.crm.impl.codes.TypeAppelant' "
                    + "and s.ALIAS in( 'HB', 'ASSUREUR', 'COURTIER', 'DELEGUE', 'PROFSANTE', 'PROSPECT', 'AUTRE') order by 2 asc";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            if ("CREATION".equals(mode)) {
                sb.append("<select class='swing_11' id='creation_type_appelant_id' name='creation_type_appelant_id' onchange='Javascript:creationAppelantSetFlags()'>");
            } else {
                sb.append("<select class='swing_11' id='modification_type_appelant_id' name='modification_type_appelant_id' onchange='Javascript:modificationAppelantSetFlags()'>");
            }
            sb.append("<option selected='selected' value='-1'>Choisir un type d'appelant</option>");

            while (rs.next()) {
                sb.append("<option value=\"" + rs.getString(1) + "\">"
                        + rs.getString(2) + "</option>");
            }

            sb.append("</select>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            throw new IContactsException("getTypesAppelantsForInputSelect", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getCivilitesForInputSelect(String mode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT civ.CODE, civ.LIBELLE FROM APPLICATION.CODES civ "
                    + "WHERE UPPER(civ.LIBELLE) in ('MONSIEUR', 'MADEMOISELLE', 'MADAME') and civ.MUTUELLE_ID = 1 order by 2 asc";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            if ("CREATION".equals(mode)) {
                sb.append("<select class='swing_11' id='creation_appelant_code_civilite' name='creation_appelant_code_civilite'>");
            } else {
                sb.append("<select class='swing_11' id='modification_appelant_code_civilite' name='modification_appelant_code_civilite'>");
            }
            sb.append("<option selected='selected' value='-1'>&nbsp;</option>");

            while (rs.next()) {
                sb.append("<option value=\"" + rs.getString(1) + "\">"
                        + rs.getString(2) + "</option>");
            }

            sb.append("</select>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            throw new IContactsException("getCivilitesForInputSelect", e);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getPeriodesRappel() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT s.CODE, s.LIBELLE, s.ORDRE FROM HOTLINE.CODES s WHERE s.JDOCLASS = 'hosta.crm.impl.codes.PeriodRappel' order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getPeriodesRappel", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getRegimes() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            LibelleCode unDisplay = null;
            String requete = "SELECT distinct r.code, r.LIBELLE FROM HOTLINE.CODES r WHERE r.JDOCLASS = 'hosta.crm.impl.codes.Regime' and rownum <=3";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getRegimes", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getOuiNon() {
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        LibelleCode l1 = new LibelleCode();
        l1.setCode(_VRAI);
        l1.setLibelle("Oui");
        res.add(l1);

        LibelleCode l2 = new LibelleCode();
        l2.setCode(_FAUX);
        l2.setLibelle("Non");
        res.add(l2);

        return res;
    }

    public static Collection<Campagne> getCampagnesPourCreationEtRechercheFiches(
            String teleacteur_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Campagne> res = new ArrayList<Campagne>();
        try {

            Campagne unDisplay = null;
            String requete = "SELECT distinct c.ID, c.LIBELLE, c.ACTIF,c.FLAG_ENTITE_GESTION "
                    + "FROM hotline.campagne c, hotline.teleacteur t, hotline.teleacteurcampagne telecamp "
                    + "WHERE telecamp.CAMPAGNE_ID = c.ID "
                    + "and telecamp.TELEACTEUR_ID = ? "
                    + "and t.ID = telecamp.TELEACTEUR_ID "
                    + "order by c.actif desc, c.LIBELLE asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Campagne();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setActif(rs.getString(3));
                unDisplay.setFLAG_ENTITE_GESTION(rs.getInt(4));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getCampagnesPourCreationEtRechercheFiches", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Scenario getScenarioPourCampagne(String libCampagne, String codeMutuelle, String codeEntite) {
    	Scenario result = null;
    	

    	
    	return result;
    }

    public static Campagne getCampagneById(String idCampagne) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Camp_EntiteGestion> res = new ArrayList<Camp_EntiteGestion>();
        try {
            Campagne unDisplay = null;
            String requete = "SELECT ID, CODEFT, LIBELLE, TEL, ACTIF,FLAG_ENTITE_GESTION FROM HOTLINE.CAMPAGNE where ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idCampagne);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Campagne();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCodeFT((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setTel((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay.setActif((rs.getString(5) != null) ? rs.getString(5)	
                        : "");
                unDisplay.setFLAG_ENTITE_GESTION((rs.getInt(6) != 0) ? rs.getInt(6):0);
            }
            stmt.clearParameters();
            if (unDisplay.getFLAG_ENTITE_GESTION()==1)
            {
            	Camp_EntiteGestion unDisplay_Camp = null;
            	requete="SELECT ce.campagne_id,ce.entite_gestion_id,eg.LIBELLE,ce.scenario_id from HOTLINE.CAMPENTITEGESTION ce,APPLICATION.ENTITE_GESTION eg WHERE CAMPAGNE_ID=? and eg.ID = ce.ENTITE_GESTION_ID";
            	closeRsStmtConn(rs,stmt,conn);
            	conn = getConnexion();
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, idCampagne);
                rs = stmt.executeQuery();
                while (rs.next()) {
                	unDisplay_Camp = new Camp_EntiteGestion();
                	unDisplay_Camp.setCampagne_id(rs.getString(1));
                	unDisplay_Camp.setEntite_gestion_id(rs.getString(2));
                	unDisplay_Camp.setLibelle(rs.getString(3));
                	unDisplay_Camp.setid_secenario(rs.getString(4));
                    res.add(unDisplay_Camp);
                }
                unDisplay.setCamp_EntiteGestions(res);
                stmt.clearParameters();
            }
            
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getCampagneById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    //
    // recherche idscenario 
    //
    
    public static String getscenario_by_campentitegestion(String campagne_id,String entitegestion_id) {
    	String res="0";
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
    	String requete = "SELECT SCENARIO_ID from HOTLINE.CAMPENTITEGESTION where CAMPAGNE_ID=? AND ENTITE_GESTION_ID=? ";
        
        conn = getConnexion();
        stmt = conn.prepareStatement(requete);
        stmt.setString(1, campagne_id);
        stmt.setString(2, entitegestion_id);
        rs = stmt.executeQuery();
        while (rs.next()) {
        	res= rs.getString(1);
        	
        }
        return res;
        } catch (Exception e) {
            LOGGER.error("getCampagneMutuelles", e);
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
        
    	
    	
    }
    //
    //
    public static Collection<Mutuelle> getCampagneMutuelles(String campagne_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Mutuelle> res = new ArrayList<Mutuelle>();

        try {
            Mutuelle unDisplay = null;
            String requete = "SELECT m.ID, m.LIBELLE, m.ACTIF FROM APPLICATION.MUTUELLE m, HOTLINE.CAMPMUT cm "
                    + "WHERE cm.CAMPAGNE_ID = ? and cm.MUTUELLE_ID = m.ID order by LIBELLE ASC";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, campagne_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Mutuelle();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setActif(rs.getString(3));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getCampagneMutuelles", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Message> getCampagneMessage(String campagne_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Message> res = new ArrayList<Message>();

        try {
            Message unDisplay = null;
            String requete = "SELECT m.ID, m.TITRE FROM HOTLINE.MESSAGE m "
                    + "WHERE m.CAMPAGNE_ID = ?  "
                    + "and trunc(m.DATEDEBUT)<= trunc(sysdate) "
                    + "and ( trunc(m.DATEFIN) >= trunc(sysdate) or m.datefin is null )";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, campagne_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Message();
                unDisplay.setID(rs.getString(1));
                unDisplay.setTITRE((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getCampagneMessage", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Message getMessageById(String message_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Message unDisplay = null;
            String requete = "SELECT m.ID, m.TITRE, m.CONTENU, m.DATEDEBUT, m.DATEFIN, m.CAMPAGNE_ID, c.LIBELLE "
                    + "FROM HOTLINE.MESSAGE m, HOTLINE.CAMPAGNE c "
                    + "WHERE m.CAMPAGNE_ID = c.ID AND m.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, message_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Message();
                unDisplay.setID(rs.getString(1));
                unDisplay.setTITRE((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setCONTENU((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDATEDEBUT(rs.getTimestamp(4));
                unDisplay.setDATEFIN(rs.getTimestamp(5));
                unDisplay.setCAMPAGNE_ID(rs.getString(6));
                unDisplay.setCampagne((rs.getString(7) != null) ? rs
                        .getString(7) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getMessageById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Transfert getTransfertById(String transfert_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Transfert unDisplay = null;
            String requete = "SELECT t.TRA_ID, t.TRA_LIBELLE, t.TRA_EMAIL FROM HOTLINE.t_TRANSFERTS_TRA t "
                    + "WHERE t.TRA_ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, transfert_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Transfert();
                unDisplay.setTRA_ID(rs.getString(1));
                unDisplay.setTRA_LIBELLE((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setTRA_EMAIL((rs.getString(3) != null) ? rs
                        .getString(3) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getTransfertById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Scenario getScenarioByCampagneMutuelle(String campagne_id,
            String mutuelle_id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Scenario unDisplay = null;
            String requete = "SELECT DISTINCT sc.ID, sc.CONSIGNES, sc.DISCOURS, se.SCENARIO_ID "
                    + "FROM HOTLINE.SCENARIO sc "
                    + "LEFT OUTER JOIN HOTLINE.SCENARIO_EVENEMENT se ON sc.ID = se.SCENARIO_ID AND se.S_MOTIF_EVENEMENT_ID=? "
                    + "WHERE sc.CAMPAGNE_ID = ? AND sc.MUTUELLE_ID = ? ";
                    
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, _evenement_pec_id);
            stmt.setString(2, campagne_id);
            stmt.setString(3, mutuelle_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                
                unDisplay = new Scenario();
                unDisplay.setID(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                
                unDisplay.setModelesPEC(new ArrayList<ModelePEC>());
                
                if( rs.getString(4) != null ){
               
                    stmt.clearParameters();
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.SCENARIO_EVENEMENT se, "
                            + "     HOTLINE.MODELE_PEC mp "
                            + "WHERE se.SCENARIO_ID = ? "
                            + "     AND se.S_MOTIF_EVENEMENT_ID=?"
                            + "     AND mp.ID = se.MODELEPEC_ID";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, unDisplay.getID());
                    stmt.setString(2, _evenement_pec_id); 
                    rs = stmt.executeQuery();
                    
                    ModelePEC unModelePEC = null;
                    while (rs.next()) {
                        
                        unModelePEC = new ModelePEC();
                        unModelePEC.setId(rs.getString(1));
                        unModelePEC.setLibelle(rs.getString(2));
                        unModelePEC.setOperateur(rs.getString(3));
                        unModelePEC.setOrganisme(rs.getString(4));
                        unModelePEC.setEmissionFax(rs.getBoolean(5));
                        unModelePEC.setFax(rs.getString(6));
                        unModelePEC.setEmissionCourriel(rs.getBoolean(7));
                        unModelePEC.setCourriel(rs.getString(8));
                        unModelePEC.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        unModelePEC.setAppelantFournisseurPermis(rs.getBoolean(10));
                        
                        unDisplay.getModelesPEC().add(unModelePEC);
                    }
                }
            }    
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getScenarioByCampagneMutuelle", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Motif> getMotifsByScenarioId(String scenario_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Motif> res = new ArrayList<Motif>();

        try {
            Motif unDisplay = null;
            String requete = "SELECT m.ID, m.LIBELLE FROM HOTLINE.MOTIFAPPEL m "
                    + "WHERE m.SCENARIO_ID = ? and m.ACTIF = 1 order by 2 asc ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, scenario_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Motif();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getMotifsByScenarioId", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<SousMotif> getSousMotifsByMotifId(String motif_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<SousMotif> res = new ArrayList<SousMotif>();

        try {
            SousMotif unDisplay = null;
            String requete = "SELECT sm.ID, sm.LIBELLE FROM HOTLINE.SMOTIFAPPEL sm "
                    + "WHERE sm.MOTIF_ID = ? and sm.ACTIF = 1 order by 2 asc ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, motif_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new SousMotif();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getSousMotifsByMotifId", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Point> getPointsBySousMotifId(String sous_motif_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Point> res = new ArrayList<Point>();
        try {
            Point unDisplay = null;
            String requete = "SELECT p.ID, p.LIBELLE FROM HOTLINE.Point p WHERE p.S_MOTIF_ID = ? and p.ACTIF = 1 order by 2 asc ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, sous_motif_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Point();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getPointsBySousMotifId", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<SousPoint> getSousPointsByPointId(String point_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<SousPoint> res = new ArrayList<SousPoint>();

        try {
            SousPoint unDisplay = null;
            String requete = "SELECT sp.ID, sp.LIBELLE FROM HOTLINE.SPoint sp WHERE sp.POINT_ID = ? and sp.ACTIF = 1 order by 2 asc ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, point_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new SousPoint();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getSousPointsByPointId", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Motif getMotifById(String motif_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Motif unDisplay = null;
            String requete = "SELECT m.ID, m.CONSIGNES, m.DISCOURS, m.libelle, p.motif_id "
                    + "FROM HOTLINE.MOTIFAPPEL m "
                    + "LEFT OUTER JOIN HOTLINE.RATTACHEMENT_PEC p ON p.motif_id=m.id "
                    + "WHERE m.ID = ?  ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, motif_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                unDisplay = new Motif();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                
                if( rs.getString(5) != null ){
                    stmt.clearParameters();
                    
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.MODELE_PEC mp,"
                            + "     HOTLINE.RATTACHEMENT_PEC rp "
                            + "WHERE rp.motif_id = ?  "
                            + "    AND rp.modelepec_id=mp.id ";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, motif_id);
                    rs = stmt.executeQuery();
                    
                    ModelePEC result = null;
                    if (rs.next()) {
                        result = new ModelePEC();
                        result.setId(rs.getString(1));
                        result.setLibelle(rs.getString(2));
                        result.setOperateur(rs.getString(3));
                        result.setOrganisme(rs.getString(4));
                        result.setEmissionFax(rs.getBoolean(5));
                        result.setFax(rs.getString(6));
                        result.setEmissionCourriel(rs.getBoolean(7));
                        result.setCourriel(rs.getString(8));
                        result.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        result.setAppelantFournisseurPermis(rs.getBoolean(10));
                    }
                    unDisplay.setPec(result);
                }
                
                stmt.clearParameters();
                requete = "SELECT mp.ID, mp.LIBELLE, mp.TYPE, mp.MAIL_OBJET, mp.MAIL_INVITE, mp.MAIL_CORPS, mp.MAIL_SIGNATURE, mp.RECAP_ADH, mp.RECAP_CENTREGESTION "
                        + "FROM HOTLINE.RATTACHEMENT_PROCEDURE prc, "
                        + "     HOTLINE.MODELE_PROCEDURE mp "
                        + "WHERE prc.motif_id = ?  "
                        + "    AND mp.id=prc.modprocedure_id ";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, motif_id);
                rs = stmt.executeQuery();
                
                Collection<ModeleProcedureMail> mp = new ArrayList<ModeleProcedureMail>();
                
                while( rs.next() ) {
                    ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
                    leModeleProcedureMail.setId(rs.getString(1));
                    leModeleProcedureMail.setLibelle(rs.getString(2));
                    leModeleProcedureMail.setType(rs.getString(3));
                    leModeleProcedureMail.setMailObjet(rs.getString(4));
                    leModeleProcedureMail.setMailInvite(rs.getString(5));
                    leModeleProcedureMail.setMailCorps(rs.getString(6));
                    leModeleProcedureMail.setMailSignature(rs.getString(7));
                    leModeleProcedureMail.setRecapAdherent(rs.getBoolean(8));
                    leModeleProcedureMail.setRecapCentreGestion(rs.getBoolean(9));
                    mp.add(leModeleProcedureMail);
                }
                unDisplay.setProceduresMail(mp);
            }
           
            
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getMotifById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Motif getMotifBySousMotif(SousMotif leSousMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Motif unDisplay = null;
            String requete = "SELECT m.ID, m.CONSIGNES, m.DISCOURS, m.libelle "
                    + "FROM HOTLINE.MOTIFAPPEL m, "
                    + "     HOTLINE.SMOTIFAPPEL sm " + "WHERE sm.ID = ? "
                    + "	 AND m.ID = sm.motif_id";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, leSousMotif.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Motif();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getMotifBySousMotif", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static SousMotif getSousMotifByPoint(Point lePoint) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            SousMotif unDisplay = null;
            String requete = "SELECT sm.ID, sm.CONSIGNES, sm.DISCOURS, sm.libelle "
                    + "FROM HOTLINE.SMOTIFAPPEL sm, "
                    + "     HOTLINE.POINT p "
                    + "WHERE p.ID = ? " + "	 AND sm.ID = p.s_motif_id";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, lePoint.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new SousMotif();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getSousMotifByPoint", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static SousMotif getSousMotifById(String sous_motif_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            SousMotif unDisplay = null;
            String requete = "SELECT sm.ID, sm.CONSIGNES, sm.DISCOURS, ref.RST_ID, ref.RST_LIBELLE, sm.flux_transfert_client, sm.mail_resiliation, sm.libelle, p.s_motif_id "
                    + "FROM HOTLINE.SMOTIFAPPEL sm "
                    + "LEFT OUTER JOIN EVENEMENT.T_REFS_STATS_RST ref ON sm.REFERENCE_ID = ref.RST_ID "
                    + "LEFT OUTER JOIN HOTLINE.RATTACHEMENT_PEC p ON p.s_motif_id=sm.id "
                    + "WHERE sm.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, sous_motif_id);
            rs = stmt.executeQuery();
            if(rs.next()) {

                unDisplay = new SousMotif();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");

                unDisplay.setIdReferenceExterne((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay
                        .setLibelleReferenceExterne((rs.getString(5) != null) ? rs
                                .getString(5) : "");
                unDisplay.setFluxTransfertClient((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setMailResiliation((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setLibelle((rs.getString(8) != null) ? rs
                        .getString(8) : "");

                
                if( rs.getString(9) != null ){
                    stmt.clearParameters();
                    
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.MODELE_PEC mp,"
                            + "     HOTLINE.RATTACHEMENT_PEC rp "
                            + "WHERE rp.s_motif_id = ?  "
                            + "    AND rp.modelepec_id=mp.id ";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, sous_motif_id);
                    rs = stmt.executeQuery();
                    
                    ModelePEC result = null;
                    if (rs.next()) {
                        result = new ModelePEC();
                        result.setId(rs.getString(1));
                        result.setLibelle(rs.getString(2));
                        result.setOperateur(rs.getString(3));
                        result.setOrganisme(rs.getString(4));
                        result.setEmissionFax(rs.getBoolean(5));
                        result.setFax(rs.getString(6));
                        result.setEmissionCourriel(rs.getBoolean(7));
                        result.setCourriel(rs.getString(8));
                        result.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        result.setAppelantFournisseurPermis(rs.getBoolean(10));
                    }
                    unDisplay.setPec(result);
                }
                
                stmt.clearParameters();
                requete = "SELECT mp.ID, mp.LIBELLE, mp.TYPE, mp.MAIL_OBJET, mp.MAIL_INVITE, mp.MAIL_CORPS, mp.MAIL_SIGNATURE, mp.RECAP_ADH, mp.RECAP_CENTREGESTION "
                        + "FROM HOTLINE.RATTACHEMENT_PROCEDURE prc, "
                        + "     HOTLINE.MODELE_PROCEDURE mp "
                        + "WHERE prc.s_motif_id = ?  "
                        + "    AND mp.id=prc.modprocedure_id ";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, sous_motif_id);
                rs = stmt.executeQuery();
                
                Collection<ModeleProcedureMail> mp = new ArrayList<ModeleProcedureMail>();
                
                while( rs.next() ) {
                    ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
                    leModeleProcedureMail.setId(rs.getString(1));
                    leModeleProcedureMail.setLibelle(rs.getString(2));
                    leModeleProcedureMail.setType(rs.getString(3));
                    leModeleProcedureMail.setMailObjet(rs.getString(4));
                    leModeleProcedureMail.setMailInvite(rs.getString(5));
                    leModeleProcedureMail.setMailCorps(rs.getString(6));
                    leModeleProcedureMail.setMailSignature(rs.getString(7));
                    leModeleProcedureMail.setRecapAdherent(rs.getBoolean(8));
                    leModeleProcedureMail.setRecapCentreGestion(rs.getBoolean(9));
                    mp.add(leModeleProcedureMail);
                }
                unDisplay.setProceduresMail(mp);

            }

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getSousMotifById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Point getPointBySousPoint(SousPoint leSousPoint) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Point unDisplay = null;

            String requete = "SELECT p.ID, p.CONSIGNES, p.DISCOURS, p.libelle "
                    + "FROM HOTLINE.SOUSPOINT sp, " + "     HOTLINE.POINT p"
                    + "WHERE sp.ID = ? " + "	 AND p.ID = sp.point_id";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, leSousPoint.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Point();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(8) : "");

            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPointBySousPoint", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Point getPointById(String point_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Point unDisplay = null;
            String requete = "SELECT p.ID, p.CONSIGNES, p.DISCOURS, p.libelle, p.mail_resiliation, rp.point_id "
                    + "FROM HOTLINE.POINT p "
                    + "LEFT OUTER JOIN HOTLINE.RATTACHEMENT_PEC rp ON rp.point_id = p.id "
                    + "WHERE p.ID = ?  ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, point_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                unDisplay = new Point();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setMailResiliation((rs.getString(5) != null) ? rs
                        .getString(5) : "");

                if( rs.getString(6) != null ){
                    
                    stmt.clearParameters();
                    
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.MODELE_PEC mp,"
                            + "     HOTLINE.RATTACHEMENT_PEC rp "
                            + "WHERE rp.point_id = ?  "
                            + "    AND rp.modelepec_id=mp.id ";
                    
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, point_id);
                    rs = stmt.executeQuery();
                    
                    ModelePEC result = null;
                    if (rs.next()) {
                        result = new ModelePEC();
                        result.setId(rs.getString(1));
                        result.setLibelle(rs.getString(2));
                        result.setOperateur(rs.getString(3));
                        result.setOrganisme(rs.getString(4));
                        result.setEmissionFax(rs.getBoolean(5));
                        result.setFax(rs.getString(6));
                        result.setEmissionCourriel(rs.getBoolean(7));
                        result.setCourriel(rs.getString(8));
                        result.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        result.setAppelantFournisseurPermis(rs.getBoolean(10));
                    }
                    unDisplay.setPec(result);
                }
                
                stmt.clearParameters();
                requete = "SELECT mp.ID, mp.LIBELLE, mp.TYPE, mp.MAIL_OBJET, mp.MAIL_INVITE, mp.MAIL_CORPS, mp.MAIL_SIGNATURE, mp.RECAP_ADH, mp.RECAP_CENTREGESTION "
                        + "FROM HOTLINE.RATTACHEMENT_PROCEDURE prc, "
                        + "     HOTLINE.MODELE_PROCEDURE mp "
                        + "WHERE prc.point_id = ?  "
                        + "    AND mp.id=prc.modprocedure_id ";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, point_id);
                rs = stmt.executeQuery();
                
                Collection<ModeleProcedureMail> mp = new ArrayList<ModeleProcedureMail>();
                
                while( rs.next() ) {
                    ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
                    leModeleProcedureMail.setId(rs.getString(1));
                    leModeleProcedureMail.setLibelle(rs.getString(2));
                    leModeleProcedureMail.setType(rs.getString(3));
                    leModeleProcedureMail.setMailObjet(rs.getString(4));
                    leModeleProcedureMail.setMailInvite(rs.getString(5));
                    leModeleProcedureMail.setMailCorps(rs.getString(6));
                    leModeleProcedureMail.setMailSignature(rs.getString(7));
                    leModeleProcedureMail.setRecapAdherent(rs.getBoolean(8));
                    leModeleProcedureMail.setRecapCentreGestion(rs.getBoolean(9));
                    mp.add(leModeleProcedureMail);
                }
                unDisplay.setProceduresMail(mp);
                
            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPointById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static SousPoint getSousPointById(String sous_point_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            SousPoint unDisplay = null;
            String requete = "SELECT sp.ID, sp.CONSIGNES, sp.DISCOURS, sp.libelle, sp.mail_resiliation, p.s_point_id "
                    + "FROM HOTLINE.SPOINT sp "
                    + "LEFT OUTER JOIN HOTLINE.RATTACHEMENT_PEC p ON p.s_point_id = sp.id "
                    + "WHERE sp.ID = ?  ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, sous_point_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                unDisplay = new SousPoint();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCONSIGNES((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDISCOURS((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setMailResiliation((rs.getString(5) != null) ? rs
                        .getString(5) : "");

                if( rs.getString(6) != null ){
                    
                    stmt.clearParameters();
                    
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.MODELE_PEC mp,"
                            + "     HOTLINE.RATTACHEMENT_PEC rp "
                            + "WHERE rp.s_point_id = ?  "
                            + "    AND rp.modelepec_id=mp.id ";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, sous_point_id);
                    rs = stmt.executeQuery();
                    
                    ModelePEC result = null;
                    if (rs.next()) {
                        result = new ModelePEC();
                        result.setId(rs.getString(1));
                        result.setLibelle(rs.getString(2));
                        result.setOperateur(rs.getString(3));
                        result.setOrganisme(rs.getString(4));
                        result.setEmissionFax(rs.getBoolean(5));
                        result.setFax(rs.getString(6));
                        result.setEmissionCourriel(rs.getBoolean(7));
                        result.setCourriel(rs.getString(8));
                        result.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        result.setAppelantFournisseurPermis(rs.getBoolean(10));
                    }
                    unDisplay.setPec(result);
                }
               
                stmt.clearParameters();
                requete = "SELECT mp.ID, mp.LIBELLE, mp.TYPE, mp.MAIL_OBJET, mp.MAIL_INVITE, mp.MAIL_CORPS, mp.MAIL_SIGNATURE, mp.RECAP_ADH, mp.RECAP_CENTREGESTION "
                        + "FROM HOTLINE.RATTACHEMENT_PROCEDURE prc, "
                        + "     HOTLINE.MODELE_PROCEDURE mp "
                        + "WHERE prc.s_point_id = ?  "
                        + "    AND mp.id=prc.modprocedure_id ";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, sous_point_id);
                rs = stmt.executeQuery();
                
                Collection<ModeleProcedureMail> mp = new ArrayList<ModeleProcedureMail>();
                
                while( rs.next() ) {
                    ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
                    leModeleProcedureMail.setId(rs.getString(1));
                    leModeleProcedureMail.setLibelle(rs.getString(2));
                    leModeleProcedureMail.setType(rs.getString(3));
                    leModeleProcedureMail.setMailObjet(rs.getString(4));
                    leModeleProcedureMail.setMailInvite(rs.getString(5));
                    leModeleProcedureMail.setMailCorps(rs.getString(6));
                    leModeleProcedureMail.setMailSignature(rs.getString(7));
                    leModeleProcedureMail.setRecapAdherent(rs.getBoolean(8));
                    leModeleProcedureMail.setRecapCentreGestion(rs.getBoolean(9));
                    mp.add(leModeleProcedureMail);
                }
                unDisplay.setProceduresMail(mp);

            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getSousPointById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getCodeAssureTypeAppelant() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {

            String requete = "SELECT c.CODE FROM HOTLINE.CODES c WHERE c.jdoclass='hosta.crm.impl.codes.TypeAppelant' AND c.ALIAS = 'ASSURE'";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getCodeAssureTypeAppelant", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection rechercheAssures(HttpServletRequest request) {

        // HABILITATION : EG
    	 Campagne Mycampagne = (Campagne) request.getSession()
                 .getAttribute("campagne");
    	
        Connection conn = null;
        PreparedStatement stmt = null;
        Collection res = new ArrayList();
        ObjetRecherche unDisplay = null;

        String cle_recherche = (String) request.getParameter("cle_recherche");

        String tous_client = (String) request.getParameter("tous_client");
        String inclure_inactifs = (String) request
                .getParameter("inclure_inactifs");
        String mutuelle_id = (String) request.getParameter("mutuelle_id");
        String teleacteur_id = (String) request.getParameter("teleacteur_id");
        String filtre_inclure_inactifs = (_FAUX.equals(inclure_inactifs)) ? " AND ben.ACTIF = 1 "
                : "";
        String ckb_nom_prenom = (String) request.getParameter("ckb_nom_prenom");
        String ckb_numero_adherent = (String) request
                .getParameter("ckb_numero_adherent");
        String ckb_numero_secu = (String) request
                .getParameter("ckb_numero_secu");

        int compteur_bind = 1;

        ResultSet rs = null;
        try {

            conn = getConnexion();

            StringBuilder requete_globale = new StringBuilder("");
            String comparateur = " = ";
            if (cle_recherche.indexOf('%') != -1) {
                comparateur = " like ";
            }

            // NUM CONTRAT
            if (IContacts._vrai.equals(ckb_numero_adherent)) {
                // a)Recherche d'adhérent par code AVT
                requete_globale
                        .append(" select /*+ index(ben.BENEF_CODE_IDX)*/ mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite, pers.NOM as nom, "
                                + "pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, "
                                + "ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE AND ben.QUALITE_CODE = qualcod.CODE "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID AND ben.CODE  "
                                + comparateur + " ?  ");
                if (_FAUX.equals(tous_client))
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                requete_globale.append(filtre_inclure_inactifs);

                requete_globale.append(_UNION);
                // b)Recherche d'adhérent par num contrat ANETO (contrat actif)
                requete_globale
                        .append(" select /*+ index(aca.X_CONTADHNUMCONTRAT )*/ mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite, pers.NOM as nom, "
                                + "pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, "
                                + "ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg, "
                                + "application.contrat_adherent aca, application.contrat_beneficiaire acb,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE AND ben.QUALITE_CODE = qualcod.CODE "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID AND acb.beneficiaire_id = ben.id and acb.contrat_adherent_id = aca.id AND eg.id = ben.entite_gestion_id "
                                + "AND aca.numcontrat " + comparateur + " ? ");
                if (_FAUX.equals(tous_client)){
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                }    
                requete_globale.append(filtre_inclure_inactifs);
            }

            // NOM - PRENOM
            if (IContacts._vrai.equals(ckb_nom_prenom)) {

                if (!"".equals(requete_globale.toString())) {
                    requete_globale.append(_UNION);
                }

                requete_globale
                        .append(" select /*+ index(pers pers_nom_idx) */ mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite, pers.NOM as nom,"
                                + "pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous,"
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE "
                                + "AND ben.QUALITE_CODE = qualcod.CODE AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "AND replace(pers.NOM, ' ', '') "
                                + comparateur + " replace(?, ' ', '') ");
                if (_FAUX.equals(tous_client))
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                requete_globale.append(filtre_inclure_inactifs);

                requete_globale.append(_UNION);

                requete_globale
                        .append("select  mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite, pers.NOM as nom,"
                                + "pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE "
                                + "AND ben.QUALITE_CODE = qualcod.CODE AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "AND replace(pers.PRENOM, ' ', '') "
                                + comparateur + " replace(?, ' ', '') ");
                if (_FAUX.equals(tous_client))
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                requete_globale.append(filtre_inclure_inactifs);
                requete_globale.append(_UNION);

                requete_globale
                        .append("select /*+ index(pers X_PERSONNE_FCTNOMPRENOM )*/ mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, "
                                + "civcod.LIBELLE as civilite, pers.NOM as nom, pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance,"
                                + "ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE "
                                + "AND ben.QUALITE_CODE = qualcod.CODE AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "and replace(pers.NOM ||  pers.PRENOM, ' ', '') "
                                + comparateur + " replace(?, ' ', '') ");
                if (_FAUX.equals(tous_client)){
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                }    
                requete_globale.append(filtre_inclure_inactifs);
                requete_globale.append(_UNION);

                requete_globale
                        .append("select /*+ index(pers X_PERSONNE_FCTNOMPRENOM)*/ mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite,"
                                + " pers.NOM as nom, pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, "
                                + "ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE "
                                + "AND ben.QUALITE_CODE = qualcod.CODE AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "AND replace(pers.PRENOM ||  pers.NOM, ' ', '') "
                                + comparateur + " replace(?, ' ', '') ");
                if (_FAUX.equals(tous_client)){
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                }    
                requete_globale.append(filtre_inclure_inactifs);
            }

            // NUM SECU
            if (IContacts._vrai.equals(ckb_numero_secu)) {
                if (!"".equals(requete_globale.toString())) {
                    requete_globale.append(_UNION);
                }

                requete_globale
                        .append(" select /*+ index(ben BENEF_NUMSS_IDX)*/  mut.LIBELLE as mutuelle, ben.CODE as numero_adherent, civcod.LIBELLE as civilite, pers.NOM as nom, "
                                + "pers.PRENOM as prenom, qualcod.LIBELLE as qualite, pers.DATENAISSANCE as date_naissance, "
                                + "ben.NUMEROSS || ' ' || ben.CLESS as num_ss, ben.ACTIF as actif, ous.OUS_LIB as outil, ben.ID as ben_id "
                                + ", mut.ID as MUTUELLE_ID,ben.ENTITE_GESTION_ID as ENTITE_GESTION_ID,eg.LIBELLE "
                                + "FROM application.mutuelle mut, application.beneficiaire ben, application.personne pers, application.T_OUTILSSOURCE_OUS ous, "
                                + "application.codes civcod, application.codes qualcod, hotline.teleacteurentitegestion teg,application.entite_gestion eg "
                                + "WHERE ben.PERSONNE_ID = pers.ID and pers.CIVILITE_CODE = civcod.CODE AND ben.QUALITE_CODE = qualcod.CODE "
                                + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? AND eg.id = ben.entite_gestion_id "
                                + "AND ben.MUTUELLE_ID = mut.ID AND ben.BEN_OUS_ID = ous.OUS_ID AND ben.NUMEROSS "
                                + comparateur + " substr(?, 1, 13) ");
                if (_FAUX.equals(tous_client)){
                    requete_globale.append(" and ben.mutuelle_id = ? ");
                }    
                requete_globale.append(filtre_inclure_inactifs);
            }

            requete_globale.append(" ORDER BY 9 DESC, 1 ASC ");

            String requete_finale = "SELECT w.*, decode(m_cl.ID, null, 'NC', 'C') FROM ("
                    + requete_globale.toString()
                    + ") w left outer join "
                    + "application.mutuelle m_cl on w.MUTUELLE_ID = m_cl.ID and m_cl.ID = ? ";

            stmt = conn.prepareStatement(requete_finale,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(_Max_Rows);
            stmt.setMaxRows(_Max_Rows);

            // NUM CONTRAT
            if (IContacts._vrai.equals(ckb_numero_adherent)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            // NOM - PRENOM
            if (IContacts._vrai.equals(ckb_nom_prenom)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            // NUM SECU
            if (IContacts._vrai.equals(ckb_numero_secu)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            // Pour les mutuelles cliquables
            stmt.setString(compteur_bind, mutuelle_id);

            rs = stmt.executeQuery();

            // VERIFIER TAILLE DE LA REQUETE
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); 
            }

            if (rowcount > _max_nbr_fiches_recherchees) {
                Limite limite = new Limite();
                limite.setTaille(rowcount);
                res.add(limite);
            } else {
                while (rs.next()) {
                    unDisplay = new ObjetRecherche();
                    
                    unDisplay.setMutuelle((rs.getString(1) != null) ? rs
                            .getString(1) : "&nbsp;");
                    unDisplay
                            .setCodeAdherentNumeroContrat((rs.getString(2) != null) ? rs
                                    .getString(2) : "&nbsp;");
                    unDisplay.setCivilite((rs.getString(3) != null) ? rs
                            .getString(3) : "&nbsp;");
                    unDisplay.setNom((rs.getString(4) != null) ? rs
                            .getString(4) : "&nbsp;");
                    unDisplay.setPrenom((rs.getString(5) != null) ? rs
                            .getString(5) : "&nbsp;");
                    unDisplay.setQualite((rs.getString(6) != null) ? rs
                            .getString(6) : "&nbsp;");
                    unDisplay
                            .setDateNaissance((rs.getTimestamp(7) != null) ? rs
                                    .getTimestamp(7) : null);
                    unDisplay.setNumeroSecu((rs.getString(8) != null) ? rs
                            .getString(8) : "&nbsp;");
                    unDisplay.setRadie((rs.getString(9) != null) ? rs
                            .getString(9) : "&nbsp;");
                    unDisplay.setOutilGestion((rs.getString(10) != null) ? rs
                            .getString(10) : "&nbsp;");
                    unDisplay.setId((rs.getString(11) != null) ? rs
                            .getString(11) : "&nbsp;");
                    unDisplay.setMutuelleId((rs.getString(12) != null) ? rs
                            .getString(12) : "&nbsp;");
                    unDisplay.setEntite_gestion_id((rs.getString(13) != null) ? rs
                            .getString(13) : "&nbsp;");
                    unDisplay.setLibelle_entite_gestion((rs.getString(14) != null) ? rs
                            .getString(14) : "&nbsp;");
                    
                    unDisplay.setClickable((rs.getString(15) != null) ? rs
                            .getString(15) : "&nbsp;");
                    
                    Collection<Camp_EntiteGestion> Camp_EntiteGestions = new ArrayList<Camp_EntiteGestion>();
                    Camp_EntiteGestions = Mycampagne.getCamp_EntiteGestions();
                    String ok="ok";
                    if (Mycampagne.getFLAG_ENTITE_GESTION()==1)
                    {
                    	ok="";
                    for (int camp=0; camp< Camp_EntiteGestions.size();camp++)
                    {
                    	Camp_EntiteGestion myentite = (Camp_EntiteGestion) Camp_EntiteGestions.toArray()[camp];
                    	if (unDisplay.getEntite_gestion_id().equals(myentite.getEntite_gestion_id()))
                    	{
                    		ok="ok";
                    	}
                    }
                    }
                    if (ok=="ok")
                    {
                    	 unDisplay.setEntite_gestion_id((rs.getString(13) != null) ? rs
                                 .getString(13) : "&nbsp;");
                    }
                    else
                    {
                    	unDisplay.setEntite_gestion_id("-1");
                    }
                    res.add(unDisplay);
                }
            }

            stmt.clearParameters();

            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheAssures", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection rechercheEntreprises(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        Collection res = new ArrayList();
        ObjetRecherche unDisplay = null;

        String cle_recherche = (String) request.getParameter("cle_recherche");
        String tous_client = (String) request.getParameter("tous_client");
        String inclure_inactifs = (String) request
                .getParameter("inclure_inactifs");
        String mutuelle_id = (String) request.getParameter("mutuelle_id");
        String teleacteur_id = (String) request.getParameter("teleacteur_id");
        String filtre_inclure_inactifs = (_FAUX.equals(inclure_inactifs)) ? " AND etab.ACTIF = 1 "
                : "";
        String ckb_numero_contrat_collectif = (String) request
                .getParameter("ckb_numero_contrat_collectif");
        String ckb_libelle = (String) request.getParameter("ckb_libelle");
        String ckb_numero_siret = (String) request
                .getParameter("ckb_numero_siret");

        int compteur_bind = 1;

        ResultSet rs = null;
        try {

            conn = getConnexion();

            StringBuilder requete_globale = new StringBuilder("");
            String comparateur = " = ";
            if (cle_recherche.indexOf('%') != -1) {
                comparateur = " like ";
            }

            /* LIBELLE */
            if (IContacts._vrai.equals(ckb_libelle)) {
                requete_globale
                        .append("select  /*+ index(etab.X_ETABLISSEMENT_FCTLIB)*/  mut.LIBELLE as mutuelle, etab.LIBELLE as libelle_etablissement, etab.SIRET as siret, "
                                + "etab.CODE as code_etablissement, entre.CODE as code_entreprise, eg.code  || '-' || eg.libelle as eg, "
                                + "adr.codepostal as code_postal, adr.LOCALITE as localite, etab.ACTIF as actif, ous.ous_lib as outil,  etab.ID as id "
                                + ", mut.ID as MUTUELLE_ID "
                                + "FROM application.MUTUELLE mut, application.ETABLISSEMENT etab, application.entreprise entre, "
                                + "application.entite_gestion eg, application.T_OUTILSSOURCE_OUS ous, application.adresse adr, hotline.teleacteurentitegestion teg "
                                + "WHERE etab.MUTUELLE_ID = mut.ID and etab.ENTREPRISE_ID = entre.ID and etab.ENTITE_GESTION_ID = eg.id "
                                + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ADRESSE_ID = adr.ID(+) and replace(etab.libelle,' ','') "
                                + comparateur + " replace(?, ' ', '') ");
                if (_FAUX.equals(tous_client)) {
                    requete_globale.append(" and etab.mutuelle_id = ? ");
                }
                requete_globale.append(filtre_inclure_inactifs);
            }

            /* CODE ENTREPRISE AVT et NUMERO CONTRAT ANETO */
            if (IContacts._vrai.equals(ckb_numero_contrat_collectif)) {
                if (!"".equals(requete_globale.toString())) {
                    requete_globale.append(_UNION);
                }

                requete_globale
                        .append("select mut.LIBELLE as mutuelle, etab.LIBELLE as libelle_etablissement, etab.SIRET as siret, "
                                + "etab.CODE as code_etablissement, entre.CODE as code_entreprise, eg.code  || '-' || eg.libelle as eg, "
                                + "adr.codepostal as code_postal, adr.LOCALITE as localite,  etab.ACTIF as actif, ous.ous_lib as outil, etab.ID as id "
                                + ", mut.ID as MUTUELLE_ID "
                                + "FROM application.MUTUELLE mut, application.ETABLISSEMENT etab, application.entreprise entre, "
                                + "application.entite_gestion eg, application.T_OUTILSSOURCE_OUS ous, application.adresse adr, hotline.teleacteurentitegestion teg "
                                + "WHERE etab.MUTUELLE_ID = mut.ID and etab.ENTREPRISE_ID = entre.ID and etab.ENTITE_GESTION_ID = eg.id "
                                + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ADRESSE_ID = adr.ID(+) and entre.code "
                                + comparateur + " ? ");
                if (_FAUX.equals(tous_client)) {
                    requete_globale.append(" and etab.mutuelle_id = ? ");
                }
                requete_globale.append(filtre_inclure_inactifs);

                requete_globale.append(_UNION);

                requete_globale
                        .append("select mut.LIBELLE as mutuelle, etab.LIBELLE as libelle_etablissement, etab.SIRET as siret, "
                                + "etab.CODE as code_etablissement, entre.CODE as code_entreprise, eg.code  || '-' || eg.libelle as eg, "
                                + "adr.codepostal as code_postal, adr.LOCALITE as localite, etab.ACTIF as actif, ous.ous_lib as outil,  etab.ID as id "
                                + ", mut.ID as MUTUELLE_ID "
                                + "FROM application.MUTUELLE mut, application.ETABLISSEMENT etab, application.entreprise entre, "
                                + "application.entite_gestion eg, application.T_OUTILSSOURCE_OUS ous, application.adresse adr, "
                                + "application.contrat_etablissement cte, hotline.teleacteurentitegestion teg "
                                + "WHERE etab.MUTUELLE_ID = mut.ID and etab.ENTREPRISE_ID = entre.ID and etab.ENTITE_GESTION_ID = eg.id "
                                + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ADRESSE_ID = adr.ID(+) AND cte.etablissement_id = etab.ID   "
                                + "AND cte.NUMCONTRAT " + comparateur + " ? ");
                if (_FAUX.equals(tous_client)) {
                    requete_globale.append(" and etab.mutuelle_id = ? ");
                }
                requete_globale.append(filtre_inclure_inactifs);

                // Prise en compte des groupes d'assurés (03/05/2011)
                requete_globale.append(_UNION);

                requete_globale
                        .append("select mut.LIBELLE as mutuelle, etab.LIBELLE as libelle_etablissement, etab.SIRET as siret, "
                                + "etab.CODE as code_etablissement, entre.CODE as code_entreprise, eg.code  || '-' || eg.libelle as eg, "
                                + "adr.codepostal as code_postal, adr.LOCALITE as localite, etab.ACTIF as actif, ous.ous_lib as outil,  etab.ID as id "
                                + ", mut.ID as MUTUELLE_ID "
                                + "FROM application.MUTUELLE mut, application.ETABLISSEMENT etab, application.entreprise entre,"
                                + "APPLICATION.CONTRAT_GROUPEASSURE cga, "
                                + "application.entite_gestion eg, application.T_OUTILSSOURCE_OUS ous, application.adresse adr, "
                                + "application.contrat_etablissement cte, hotline.teleacteurentitegestion teg "
                                + "WHERE etab.MUTUELLE_ID = mut.ID AND cte.id = cga.CONTRAT_ETABLISSEMENT_ID and cga.ETABLISSEMENT_ID = etab.id "
                                + "and etab.ENTITE_GESTION_ID = eg.id and etab.ENTREPRISE_ID = entre.ID "
                                + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ADRESSE_ID = adr.ID(+) "
                                + "AND cte.NUMCONTRAT " + comparateur + " ? ");
                if (_FAUX.equals(tous_client))
                    requete_globale.append(" and etab.mutuelle_id = ? ");
                requete_globale.append(filtre_inclure_inactifs);

            }

            /* NUMERO SIRET */
            if (IContacts._vrai.equals(ckb_numero_siret)) {
                if (!"".equals(requete_globale.toString())) {
                    requete_globale.append(_UNION);
                }

                requete_globale
                        .append(" select mut.LIBELLE as mutuelle, etab.LIBELLE as libelle_etablissement, etab.SIRET as siret, "
                                + "etab.CODE as code_etablissement, entre.CODE as code_entreprise, eg.code  || '-' || eg.libelle as eg, "
                                + "adr.codepostal as code_postal, adr.LOCALITE as localite, etab.ACTIF as actif, ous.ous_lib as outil,  etab.ID as id "
                                + ", mut.ID as MUTUELLE_ID "
                                + "FROM application.MUTUELLE mut, application.ETABLISSEMENT etab, application.entreprise entre, "
                                + "application.entite_gestion eg, application.T_OUTILSSOURCE_OUS ous, application.adresse adr, hotline.teleacteurentitegestion teg "
                                + "WHERE etab.MUTUELLE_ID = mut.ID and etab.ENTREPRISE_ID = entre.ID and etab.ENTITE_GESTION_ID = eg.id "
                                + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                                + "AND etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ADRESSE_ID = adr.ID(+) and  etab.SIRET "
                                + comparateur + " ? ");
                if (_FAUX.equals(tous_client)) {
                    requete_globale.append(" and etab.mutuelle_id = ? ");
                }
                requete_globale.append(filtre_inclure_inactifs);
            }

            requete_globale.append(" ORDER BY 9 DESC, 1 ASC ");

            String requete_finale = "SELECT w.*, decode(m_cl.ID, null, 'NC', 'C') FROM ("
                    + requete_globale.toString()
                    + ") w left outer join "
                    + "application.mutuelle m_cl on w.MUTUELLE_ID = m_cl.ID and m_cl.ID = ? ";

            stmt = conn.prepareStatement(requete_finale,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            // LIBELLE
            if (IContacts._vrai.equals(ckb_libelle)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            /* CODE ENTREPRISE AVT et NUMERO CONTRAT ANETO */
            if (IContacts._vrai.equals(ckb_numero_contrat_collectif)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }

                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            /* NUMERO SIRET */
            if (IContacts._vrai.equals(ckb_numero_siret)) {
                stmt.setString(compteur_bind, teleacteur_id);
                compteur_bind++;
                stmt.setString(compteur_bind, cle_recherche);
                compteur_bind++;
                if (_FAUX.equals(tous_client)) {
                    stmt.setString(compteur_bind, mutuelle_id);
                    compteur_bind++;
                }
            }

            // Pour les mutuelles cliquables
            stmt.setString(compteur_bind, mutuelle_id);

            rs = stmt.executeQuery();

            // VERIFIER TAILLE DE LA REQUETE
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
                // On se remet avant le début (à cause du prochain next)
            }

            if (rowcount > _max_nbr_fiches_recherchees) {
                Limite limite = new Limite();
                limite.setTaille(rowcount);
                res.add(limite);
            } else {
                while (rs.next()) {
                    unDisplay = new ObjetRecherche();
                    unDisplay.setMutuelle((rs.getString(1) != null) ? rs
                            .getString(1) : "&nbsp;");
                    unDisplay.setNom((rs.getString(2) != null) ? rs
                            .getString(2) : "&nbsp;");
                    unDisplay.setNumeroSiret((rs.getString(3) != null) ? rs
                            .getString(3) : "&nbsp;");
                    unDisplay
                            .setCodeEtablissement((rs.getString(4) != null) ? rs
                                    .getString(4) : "&nbsp;");
                    unDisplay.setCodeEntreprise((rs.getString(5) != null) ? rs
                            .getString(5) : "&nbsp;");
                    unDisplay.setEntiteGestion((rs.getString(6) != null) ? rs
                            .getString(6) : "&nbsp;");
                    unDisplay.setCodePostal((rs.getString(7) != null) ? rs
                            .getString(7) : "&nbsp;");
                    unDisplay.setLocalite((rs.getString(8) != null) ? rs
                            .getString(8) : "&nbsp;");
                    unDisplay.setRadie((rs.getString(9) != null) ? rs
                            .getString(9) : "&nbsp;");
                    unDisplay.setOutilGestion((rs.getString(10) != null) ? rs
                            .getString(10) : "&nbsp;");
                    unDisplay.setId((rs.getString(11) != null) ? rs
                            .getString(11) : "&nbsp;");
                    unDisplay.setMutuelleId((rs.getString(12) != null) ? rs
                            .getString(12) : "&nbsp;");
                    unDisplay.setClickable((rs.getString(13) != null) ? rs
                            .getString(13) : "&nbsp;");
                    res.add(unDisplay);
                }
            }

            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheEntreprises", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<?> rechercheAppelants(Map<String, String> map) {

        Connection conn = null;
        PreparedStatement stmt = null;
        Collection res = new ArrayList();
        ObjetRecherche unDisplay = null;

        String cle_recherche = (String) map.get("cle_recherche");
        cle_recherche = cle_recherche.replaceAll(" ", "");
        String type_appelant_id = (String) map.get("type_appelant_id");
        String teleacteur_id = (String) map.get("teleacteur_id");
        String filtre_appelant_id = "";

        int compteur_bind = 1;

        ResultSet rs = null;
        int nb_rows = 0;
        try {

            conn = getConnexion();

            StringBuilder requete_globale = new StringBuilder("");
            String comparateur = " = ";
            if (cle_recherche.indexOf('%') != -1) {
                comparateur = " like ";
            }

            if (!_EMPTY.equals(type_appelant_id)) {
                filtre_appelant_id = " and app.TYPE_CODE = ? ";
            }

            /* NOM */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.nom, ' ', '')) "
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* PRENOM */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.prenom, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* NOM PRENOM */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.NOM ||  app.PRENOM, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* PRENOM NOM */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.PRENOM ||  app.NOM, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* ETABLISSEMENT RS */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.ETABLISSEMENT_RS, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* CODE ADHERENT */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(app.CODEADHERENT)"
                            + comparateur
                            + " upper(?) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* NUMERO DE SECU */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and app.NUMEROSS "
                            + comparateur
                            + " substr(?, 1, 13) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* NUMERO DE FINESS */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.NUMFINESS, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* LOCALITE */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.ADr_LOCALITE, ' ', '')) "
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);
            requete_globale.append(_UNION);

            /* CODEPOSTAL */
            requete_globale
                    .append("select type_cod.LIBELLE, civ.LIBELLE, app.NOM, app.PRENOM, app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.NUMEROSS || ' ' || app.CLESS, app.CODEADHERENT, app.ID "
                            + "from hotline.CODES type_cod, HOTLINE.APPELANT app, hotline.codes civ "
                            + "where app.TYPE_CODE = type_cod.CODE(+) and app.CIVILITE_CODE = civ.CODE(+) "
                            + "and upper(replace(app.ADr_CODEPOSTAL, ' ', '')) "
                            + comparateur + " upper( replace(?, ' ', '') ) ");
            requete_globale.append(filtre_appelant_id);

            requete_globale.append(" ORDER BY 1 ASC ");

            stmt = conn.prepareStatement(requete_globale.toString(),
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;
            if (!_EMPTY.equals(type_appelant_id)) {
                stmt.setString(compteur_bind, type_appelant_id);
                compteur_bind++;
            }

            rs = stmt.executeQuery();

            // VERIFIER TAILLE DE LA REQUETE
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }

            if (rowcount > _max_nbr_fiches_recherchees) {
                Limite limite = new Limite();
                limite.setTaille(rowcount);
                res.add(limite);
            } else {
                while (rs.next()) {
                    unDisplay = new ObjetRecherche();
                    unDisplay.setTypeAppelant((rs.getString(1) != null) ? rs
                            .getString(1) : "&nbsp;");
                    unDisplay.setCivilite((rs.getString(2) != null) ? rs
                            .getString(2) : "&nbsp;");
                    unDisplay.setNom((rs.getString(3) != null) ? rs
                            .getString(3) : "&nbsp;");
                    unDisplay.setPrenom((rs.getString(4) != null) ? rs
                            .getString(4) : "&nbsp;");
                    unDisplay.setEtablissementRS((rs.getString(5) != null) ? rs
                            .getString(5) : "&nbsp;");
                    unDisplay.setNumeroFiness((rs.getString(6) != null) ? rs
                            .getString(6) : "&nbsp;");
                    unDisplay.setNumeroSecu((rs.getString(7) != null) ? rs
                            .getString(7) : "&nbsp;");
                    unDisplay
                            .setCodeAdherentNumeroContrat((rs.getString(8) != null) ? rs
                                    .getString(8) : "&nbsp;");
                    unDisplay.setId((rs.getString(9) != null) ? rs.getString(9)
                            : "&nbsp;");
                    res.add(unDisplay);
                }
            }

            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheAppelants", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection rechercheEtablissementsHospitaliers(
            String cle_recherche) {

        Connection conn = null;
        PreparedStatement stmt = null;
        Collection res = new ArrayList();
        ObjetRecherche unDisplay = null;

        int compteur_bind = 1;

        ResultSet rs = null;
        int nb_rows = 0;
        try {

            conn = getConnexion();

            StringBuilder requete_globale = new StringBuilder("");
            String comparateur = " = ";
            if (cle_recherche.indexOf('%') != -1) {
                comparateur = " like ";
            }

            /* ETABLISSEMENT RS */
            requete_globale
                    .append("select app.ETABLISSEMENT_RS, app.NUMFINESS, "
                            + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE, app.ID "
                            + "FROM hotline.CODES type_cod, HOTLINE.APPELANT app "
                            + "WHERE app.TYPE_CODE = type_cod.CODE(+) "
                            + "AND upper(replace(app.ETABLISSEMENT_RS, ' ', ''))"
                            + comparateur + " upper( replace(?, ' ', '') ) "
                            + "AND type_cod.ALIAS = 'PROFSANTE'");

            requete_globale.append(_UNION);

            /* NUMERO DE FINESS */
            requete_globale
                    .append("select app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE, app.ID "
                            + "FROM hotline.CODES type_cod, HOTLINE.APPELANT app "
                            + "WHERE app.TYPE_CODE = type_cod.CODE(+) "
                            + "and upper(replace(app.NUMFINESS, ' ', '')) "
                            + comparateur + " upper( replace(?, ' ', '') ) "
                            + "AND type_cod.ALIAS = 'PROFSANTE'");

            requete_globale.append(_UNION);

            /* LOCALITE */
            requete_globale
                    .append("select app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE, app.ID "
                            + "FROM hotline.CODES type_cod, HOTLINE.APPELANT app "
                            + "WHERE app.TYPE_CODE = type_cod.CODE(+) "
                            + "and upper(replace(app.ADR_LOCALITE, ' ', '')) "
                            + comparateur + " upper( replace(?, ' ', '') ) "
                            + "AND type_cod.ALIAS = 'PROFSANTE'");

            requete_globale.append(_UNION);

            /* CODEPOSTAL */
            requete_globale
                    .append("select app.ETABLISSEMENT_RS, app.NUMFINESS , "
                            + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE, app.ID "
                            + "FROM hotline.CODES type_cod, HOTLINE.APPELANT app "
                            + "WHERE app.TYPE_CODE = type_cod.CODE(+) "
                            + "and upper(app.ADR_CODEPOSTAL) " + comparateur
                            + " upper(?) " + "AND type_cod.ALIAS = 'PROFSANTE'");

            requete_globale.append(" ORDER BY 1 ASC ");

            stmt = conn.prepareStatement(requete_globale.toString(),
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;

            stmt.setString(compteur_bind, cle_recherche);
            compteur_bind++;

            rs = stmt.executeQuery();

            // VERIFIER TAILLE DE LA REQUETE
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
                // On se remet avant le début (à cause du prochain next)
            }

            if (rowcount > _max_nbr_fiches_recherchees) {
                Limite limite = new Limite();
                limite.setTaille(rowcount);
                res.add(limite);
            } else {
                while (rs.next()) {
                    unDisplay = new ObjetRecherche();
                    unDisplay.setEtablissementRS((rs.getString(1) != null) ? rs
                            .getString(1) : "&nbsp;");
                    unDisplay.setNumeroFiness((rs.getString(2) != null) ? rs
                            .getString(2) : "&nbsp;");
                    unDisplay.setCodePostal((rs.getString(3) != null) ? rs
                            .getString(3) : "&nbsp;");
                    unDisplay.setLocalite((rs.getString(4) != null) ? rs
                            .getString(4) : "&nbsp;");
                    unDisplay.setId((rs.getString(5) != null) ? rs.getString(5)
                            : "&nbsp;");
                    res.add(unDisplay);
                }
            }

            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheEtablissementsHospitaliers", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Beneficiaire getBeneficiaireById(String idBeneficaire) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Beneficiaire unDisplay = null;
            String requete = "select w.*, decode(egb.ENTITEGESTION_ID, null, '0', '1') as SENSIBLE FROM"
                    + " (SELECT ben.ID, ben.MUTUELLE_ID, ben.CODE, ben.QUALITE_CODE, ben.NUMEROSS, ben.CLESS, ben.CAISSERO, ben.CENTRERO, "
                    + "ben.CARTEVITALE, ben.REGIME_CODE, ben.DATEPREMIEREADHESION, ben.ENTITE_GESTION_ID EG, ben.PERSONNE_ID, "
                    + "ben.ETABLISSEMENT_ID, ben.PRELEVEMENT_RIB_ID, ben.VIREMENT_RIB_ID, ben.ACTIF, ben.DATERADIATION, "
                    + "ben.POSNOEMIE_CODE, ben.BEN_OUS_ID, ben.BEN_OFFSHORE, ben.BEN_OFA_ID, ben.DATEADHESION, "
                    + "qual.LIBELLE, ous.OUS_LIB FROM APPLICATION.BENEFICIAIRE ben, APPLICATION.CODES qual, APPLICATION.T_OUTILSSOURCE_OUS ous  "
                    + "WHERE ID = ? and ben.QUALITE_CODE = qual.CODE(+) and ben.BEN_OUS_ID = ous.OUS_ID(+) ) w "
                    + "left outer join hotline.entitegestionblacklistee egb on w.EG = egb.ENTITEGESTION_ID ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idBeneficaire);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Beneficiaire();
                unDisplay.setID(rs.getString(1));
                unDisplay.setMUTUELLE_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCODE((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setQUALITE_CODE((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNUMEROSS((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCLESS((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setCAISSERO((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setCENTRERO((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setCARTEVITALE((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setREGIME_CODE((rs.getString(10) != null) ? rs
                        .getString(10) : "");

                unDisplay
                        .setDATEPREMIEREADHESION((rs.getTimestamp(11) != null) ? rs
                                .getTimestamp(11) : null);
                unDisplay.setENTITE_GESTION_ID((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setPERSONNE_ID((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setETABLISSEMENT_ID((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setPRELEVEMENT_RIB_ID((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setVIREMENT_RIB_ID((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                unDisplay.setACTIF((rs.getString(17) != null) ? rs
                        .getString(17) : "");

                unDisplay.setDATERADIATION((rs.getTimestamp(18) != null) ? rs
                        .getTimestamp(18) : null);
                unDisplay.setPOSNOEMIE_CODE((rs.getString(19) != null) ? rs
                        .getString(19) : "");
                unDisplay.setBEN_OUS_ID((rs.getString(20) != null) ? rs
                        .getString(20) : "");
                unDisplay.setBEN_OFFSHORE((rs.getString(21) != null) ? rs
                        .getString(21) : "");
                unDisplay.setBEN_OFA_ID((rs.getString(22) != null) ? rs
                        .getString(22) : "");
                unDisplay.setDATEADHESION((rs.getTimestamp(23) != null) ? rs
                        .getTimestamp(23) : null);
                unDisplay.setQualite((rs.getString(24) != null) ? rs
                        .getString(24) : null);
                unDisplay.setOutilGestion((rs.getString(25) != null) ? rs
                        .getString(25) : null);
                unDisplay
                        .setEntiteGestionSensible((rs.getString(26) != null) ? rs
                                .getString(26) : null);
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getBeneficiaireById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static fr.igestion.crm.bean.contrat.Personne getPersonneById(
            String idPersonne) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            fr.igestion.crm.bean.contrat.Personne unDisplay = null;

            String requete = "SELECT p.ID, p.CODE, p.CIVILITE_CODE, p.SEXE_CODE, p.NOM, p.PRENOM, p.NOMJEUNEFILLE, p.DATENAISSANCE, p.SITUATIONFAMILIALE_CODE,"
                    + "p.SITUATIONPROFESSIONNELLE_CODE, p.RANGNAISSANCE, p.ADRESSE_ID, p.CSP_CODE, csp.LIBELLE, civ.LIBELLE "
                    + "FROM APPLICATION.PERSONNE p, APPLICATION.CODES csp, APPLICATION.CODES civ "
                    + "WHERE ID = ? and p.CSP_CODE = csp.CODE(+) "
                    + "  AND p.CIVILITE_CODE = civ.CODE(+)";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPersonne);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new fr.igestion.crm.bean.contrat.Personne();

                unDisplay.setID(rs.getString(1));
                unDisplay.setCODE((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setCIVILITE_CODE((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setSEXE_CODE((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNOM((rs.getString(5) != null) ? rs.getString(5)
                        : "");
                unDisplay.setPRENOM((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setNOMJEUNEFILLE((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setDATENAISSANCE((rs.getTimestamp(8) != null) ? rs
                        .getTimestamp(8) : null);
                unDisplay
                        .setSITUATIONFAMILIALE_CODE((rs.getString(9) != null) ? rs
                                .getString(9) : "");

                unDisplay
                        .setSITUATIONPROFESSIONNELLE_CODE((rs.getString(10) != null) ? rs
                                .getString(10) : "");
                unDisplay.setRANGNAISSANCE((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setADRESSE_ID((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setCSP_CODE((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCSP((rs.getString(14) != null) ? rs.getString(14)
                        : "");
                unDisplay.setCivilite((rs.getString(15) != null) ? rs
                        .getString(15) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPersonneById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Etablissement getEtablissementById(String idEtablissement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Etablissement unDisplay = null;

            String requete = "select w.*, decode(egb.ENTITEGESTION_ID, null, '0', '1') as SENSIBLE FROM	"
                    + "(SELECT etab.ID, etab.CODE, etab.LIBELLE, etab.SIRET, etab.APE, etab.MUTUELLE_ID, etab.ENTREPRISE_ID,"
                    + "etab.ADRESSE_ID, etab.ENTITE_GESTION_ID EG,   etab.GRANDCOMPTE_ID, etab.ACTIF, etab.DATERADIATION, etab.ETB_OUS_ID,"
                    + "etab.ETB_OFFSHORE, etab.ETB_OFA_ID , ous.OUS_LIB, eg.CODE || '-' || eg.LIBELLE, cor.ADRESSE_ID as ADR_COR, cor.PERSONNE_ID "
                    + "FROM APPLICATION.ETABLISSEMENT etab, APPLICATION.T_OUTILSSOURCE_OUS ous, APPLICATION.ENTITE_GESTION eg, "
                    + "APPLICATION.CORRESPONDANT cor, APPLICATION.CORRESPONDANT_ETABLISSEMENT cor_etab "
                    + "WHERE etab.ETB_OUS_ID = ous.OUS_ID(+) and etab.ENTITE_GESTION_ID = eg.ID(+)  "
                    + "and cor_etab.ETABLISSEMENT_ID(+) = etab.ID  and cor_etab.CORRESPONDANT_ID = cor.ID(+) and etab.ID = ?) w "
                    + "left outer join hotline.entitegestionblacklistee egb on w.EG = egb.ENTITEGESTION_ID";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEtablissement);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Etablissement();

                unDisplay.setID(rs.getString(1));
                unDisplay.setCODE((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLIBELLE((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setSIRET((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay.setAPE((rs.getString(5) != null) ? rs.getString(5)
                        : "");
                unDisplay.setMUTUELLE_ID((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setENTREPRISE_ID((rs.getString(7) != null) ? rs
                        .getString(7) : "");

                unDisplay.setADRESSE_ID((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setENTITE_GESTION_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setGRANDCOMPTE_ID((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setACTIF((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setDATERADIATION((rs.getTimestamp(12) != null) ? rs
                        .getTimestamp(12) : null);
                unDisplay.setETB_OUS_ID((rs.getString(13) != null) ? rs
                        .getString(13) : "");

                unDisplay.setETB_OFFSHORE((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setETB_OFA_ID((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setOutilGestion((rs.getString(16) != null) ? rs
                        .getString(16) : null);
                unDisplay.setEntiteGestion((rs.getString(17) != null) ? rs
                        .getString(17) : null);
                unDisplay
                        .setCorrespondantAdresseId((rs.getString(18) != null) ? rs
                                .getString(18) : null);
                unDisplay
                        .setCorrespondantPersonneId((rs.getString(19) != null) ? rs
                                .getString(19) : null);

                unDisplay
                        .setEntiteGestionSensible((rs.getString(20) != null) ? rs
                                .getString(20) : null);
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getEtablissementById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static EntiteGestion getEntiteGestionById(String idEntiteGestion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            EntiteGestion unDisplay = null;
            String requete = "SELECT eg.id, eg.code, eg.libelle, mut.libelle "
                    + "FROM APPLICATION.entite_gestion eg, APPLICATION.mutuelle mut "
                    + "WHERE EG.MUTUELLE_ID = MUT.ID and eg.id = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEntiteGestion);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMutuelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getEntiteGestionById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    
    public static Collection<EntiteGestion> getListeSiteWeb() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<EntiteGestion> resultats;
        
        try {
            
            String requete = "SELECT eg.id, eg.code, eg.libelle, mut.libelle, S.SITE, s.URL "
                            + "FROM APPLICATION.entite_gestion eg, "
                            + "     APPLICATION.mutuelle       mut, "
                            + "     HOTLINE.T_SITEWEBENTITE_SWB s "  
                            + "WHERE EG.MUTUELLE_ID = MUT.ID "
                            + "    AND eg.id = s.entite_id "
                            + "ORDER BY 4 ASC";
            
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            resultats = new ArrayList<EntiteGestion>();
            EntiteGestion unDisplay = null;
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2): "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs.getString(3) : "");
                unDisplay.setMutuelle((rs.getString(4) != null) ? rs.getString(4) : "");
                
                if( rs.getString(5)!=null ){
                    SiteWeb descSite = new SiteWeb();
                    descSite.setTypeSite(rs.getString(5));
                    descSite.setUrl(rs.getString(6));
                    unDisplay.setSiteWeb( descSite );
                }
                resultats.add(unDisplay);
            }
            stmt.clearParameters();
            return resultats;
        } catch (Exception e) {
            LOGGER.error("getDetailEntiteGestionById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    
    public static EntiteGestion getDetailEntiteGestionById(String idEntiteGestion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            EntiteGestion unDisplay = null;
            String requete = "SELECT eg.id, eg.code, eg.libelle, mut.libelle, NVL(S.SITE,'SANS'), NVL(S.URL,'')  " 
                +"FROM APPLICATION.entite_gestion eg "
                +"JOIN APPLICATION.mutuelle mut ON EG.MUTUELLE_ID = MUT.ID "
                +"LEFT OUTER JOIN HOTLINE.T_SITEWEBENTITE_SWB s ON eg.id = s.entite_id " 
                +"WHERE eg.id = ?";
            
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEntiteGestion);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMutuelle((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                
                SiteWeb descSiteWeb = new SiteWeb();
                descSiteWeb.setTypeSite(rs.getString(5));
                descSiteWeb.setUrl(rs.getString(6));
                unDisplay.setSiteWeb( descSiteWeb );
                
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getDetailEntiteGestionById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<EntiteGestion> getEntitesGestionsByMutuelleId(String idMutuelle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection results = new ArrayList<EntiteGestion>();;
        
        try {
            EntiteGestion unDisplay = null;
            String requete = "SELECT eg.id, eg.code, eg.libelle "
                    + "FROM APPLICATION.entite_gestion eg "
                    + "WHERE EG.MUTUELLE_ID = ? AND eg.type = 'E'";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idMutuelle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new EntiteGestion();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                results.add(unDisplay);
            }
            stmt.clearParameters();
            return results;
        } catch (Exception e) {
            LOGGER.error("getEntiteGestionByMutuelleId", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Adresse getAdresseById(String idAdresse) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Adresse unDisplay = null;
            String requete = "SELECT ID, LIGNE_1, LIGNE_2, LIGNE_3, CODEPOSTAL, LOCALITE, PAYS, TELEPHONEFIXE, TELEPHONEAUTRE, TELECOPIE, "
                    + "COURRIEL, LIGNE_4, DEBUT_EFFET, FIN_EFFET FROM APPLICATION.ADRESSE  WHERE ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idAdresse);
            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new Adresse();
                unDisplay.setID(rs.getString(1));
                unDisplay.setLIGNE_1((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setLIGNE_2((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLIGNE_3((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCODEPOSTAL((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setLOCALITE((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setPAYS((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setTELEPHONEFIXE((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setTELEPHONEAUTRE((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setTELECOPIE((rs.getString(10) != null) ? rs
                        .getString(10) : "");

                unDisplay.setCOURRIEL((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setLIGNE_4((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setDEBUT_EFFET((rs.getTimestamp(13) != null) ? rs
                        .getTimestamp(13) : null);
                unDisplay.setFIN_EFFET((rs.getTimestamp(14) != null) ? rs
                        .getTimestamp(14) : null);
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getAdresseById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static DetailObjet getDetailBeneficiaireById(String idAdherent) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            DetailObjet unDisplay = null;
            String requete = "SELECT db.beneficiaire_id, db.vip, db.npai, db.code_detailvip, dv.libelle "
                    + "FROM hotline.detailbeneficiaire db, hotline.detailvip dv  "
                    + "WHERE db.beneficiaire_id = ? AND db.code_detailvip = dv.code(+) ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idAdherent);
            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new DetailObjet();
                unDisplay.setIdObjet(rs.getString(1));
                unDisplay.setVip((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setNpai((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setCodeVip((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setLibelleVip((rs.getString(5) != null) ? rs
                        .getString(5) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getDetailBeneficiaireById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static DetailObjet getDetailEtablissementById(String idEtablissement) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DetailObjet unDisplay = null;
            String requete = "SELECT de.etablissement_id, de.vip, de.code_detailvip, dv.libelle "
                    + "FROM hotline.detailetablissement de, hotline.detailvip dv  "
                    + "WHERE de.etablissement_id = ? AND de.code_detailvip = dv.code(+) ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEtablissement);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new DetailObjet();
                unDisplay.setIdObjet(rs.getString(1));
                unDisplay.setVip((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setCodeVip((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLibelleVip((rs.getString(4) != null) ? rs
                        .getString(4) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getDetailEtablissementById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getIdAdherentByIdAssure(String idBeneficiaire) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {

            String requete = "SELECT APPLICATION.fs_getadhIdBybenefid(?) FROM DUAL ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idBeneficiaire);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getIdAdherentByIdAssure", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getLibelleCodeByCode(String schema, String code) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {

            String requete = "";
            if ("APPLICATION".equalsIgnoreCase(schema)) {
                requete = "SELECT c.LIBELLE FROM APPLICATION.CODES c WHERE c.code = ? ";
            } else {
                requete = "SELECT c.LIBELLE FROM HOTLINE.CODES c WHERE c.code = ? ";
            }
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, code);
            rs = stmt.executeQuery();

            while (rs.next()) {
                res = rs.getString(1);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getLibelleCodeByCode", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static PostItBeneficiaire getPostItBeneficiaire(String adherent_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            PostItBeneficiaire unDisplay = null;

            String requete = "SELECT p.BENEFICIAIRE_ID, p.DATE_CREATION, p.CREATEUR_ID, p.DATE_MAJ, p.USERMAJ_ID, p.CONTENU "
                    + "FROM HOTLINE.POSTITBENEFICIAIRE p WHERE p.BENEFICIAIRE_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, adherent_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new PostItBeneficiaire();
                unDisplay.setBENEFICIAIRE_ID(rs.getString(1));
                unDisplay.setDATE_CREATION((rs.getTimestamp(2) != null) ? rs
                        .getTimestamp(2) : null);
                unDisplay.setCREATEUR_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDATE_MAJ((rs.getTimestamp(4) != null) ? rs
                        .getTimestamp(4) : null);
                unDisplay.setUSERMAJ_ID((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCONTENU((rs.getString(6) != null) ? rs
                        .getString(6) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPostItBeneficiaire", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static PostItEtablissement getPostItEtablissement(
            String etablissement_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            PostItEtablissement unDisplay = null;

            String requete = "SELECT p.ETABLISSEMENT_ID, p.DATE_CREATION, p.CREATEUR_ID, p.DATE_MAJ, p.USERMAJ_ID, p.CONTENU "
                    + "FROM HOTLINE.POSTITETABLISSEMENT p WHERE p.ETABLISSEMENT_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, etablissement_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new PostItEtablissement();
                unDisplay.setETABLISSEMENT_ID(rs.getString(1));
                unDisplay.setDATE_CREATION((rs.getTimestamp(2) != null) ? rs
                        .getTimestamp(2) : null);
                unDisplay.setCREATEUR_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDATE_MAJ((rs.getTimestamp(4) != null) ? rs
                        .getTimestamp(4) : null);
                unDisplay.setUSERMAJ_ID((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCONTENU((rs.getString(6) != null) ? rs
                        .getString(6) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPostItEtablissement", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean modifierPostItBeneficiaire(String adherent_id,
            String contenu) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "UPDATE HOTLINE.POSTITBENEFICIAIRE db set db.CONTENU = ? WHERE db.BENEFICIAIRE_ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, contenu);
            stmt.setString(2, adherent_id);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("modifierPostItBeneficiaire", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierPostItEntreprise(String etablissement_id,
            String contenu) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "UPDATE HOTLINE.POSTITETABLISSEMENT pe set pe.CONTENU = ? WHERE pe.ETABLISSEMENT_ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, contenu);
            stmt.setString(2, etablissement_id);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("modifierPostItEntreprise", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerPostItBeneficiaire(String adherent_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "DELETE FROM HOTLINE.POSTITBENEFICIAIRE pb WHERE pb.BENEFICIAIRE_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, adherent_id);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerPostItBeneficiaire", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerPostItEtablissement(String etablissement_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "DELETE FROM HOTLINE.POSTITETABLISSEMENT pe WHERE pe.ETABLISSEMENT_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, etablissement_id);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerPostItEtablissement", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterPostItBeneficiaire(String adherent_id,
            String contenu, String teleacteur_id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "INSERT INTO HOTLINE.POSTITBENEFICIAIRE ( BENEFICIAIRE_ID, DATE_CREATION, CREATEUR_ID, CONTENU) values (?, SYSDATE, ?, ?)";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, adherent_id);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, contenu);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterPostItBeneficiaire", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterPostItEtablissement(String etablissement_id,
            String contenu, String teleacteur_id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "INSERT INTO HOTLINE.POSTITETABLISSEMENT( ETABLISSEMENT_ID, DATE_CREATION, CREATEUR_ID, CONTENU) values (?, SYSDATE, ?, ?)";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, etablissement_id);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, contenu);
            stmt.executeQuery();
            stmt.clearParameters();

            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterPostItEtablissement", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static Collection<AyantDroit> getAyantsDroit(String idBeneficiaire,
            String idMutuelle) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        AyantDroit unDisplay = null;
        Collection<AyantDroit> res = new ArrayList<AyantDroit>();
        try {

            String requete = "select /*+index (b x_getadhidbenefid) */ b.ID, p.ID, c.LIBELLE, b.numeross, b.cless, b.actif, b.dateradiation, "
                    + "b.dateadhesion, ccsp.CODESOURCE || '-' || ccsp.LIBELLE, civ.LIBELLE,  p.nom, p.prenom, p.datenaissance "
                    + "from application.beneficiaire b, application.personne p, application.mutuelle m, "
                    + "application.codes c,  application.codes ccsp, application.codes civ "
                    + "where application.FS_GetAdhIdByBenefId(b.id) = application.FS_GetAdhIdByBenefId(?) "
                    + "and b.MUTUELLE_ID = m.ID and b.PERSONNE_ID = p.ID and p.CSP_CODE =  ccsp.CODE AND b.QUALITE_CODE = c.CODE "
                    + "and p.civilite_code = civ.code "
                    + "and m.id = ? order by 3 asc, 12 asc ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idBeneficiaire);
            stmt.setString(2, idMutuelle);
            rs = stmt.executeQuery();
            while (rs.next()) {

                unDisplay = new AyantDroit();
                unDisplay.setId(rs.getString(1));
                unDisplay.setPersonneId(rs.getString(2));
                unDisplay.setQualite((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setNumeross((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCless((rs.getString(5) != null) ? rs.getString(5)
                        : "");
                unDisplay.setActif((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setDateRadiation(rs.getTimestamp(7));
                unDisplay.setDateAdhesion(rs.getTimestamp(8));
                unDisplay.setCSP((rs.getString(9) != null) ? rs.getString(9)
                        : "");
                unDisplay.setCivilite((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setNom((rs.getString(11) != null) ? rs.getString(11)
                        : "");
                unDisplay.setPrenom((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setDateNaissance(rs.getTimestamp(13));

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getAyantsDroit", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Historique> findHistoriqueAdherent(
            String idAdherent) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Historique> res = new ArrayList<Historique>();

        try {
            conn = getConnexion();

            Historique unDisplay = null;

            StringBuilder sb = new StringBuilder();
            String requeteFiches = "SELECT 'FICHE' AS type_objet, a.ID, a.beneficiaire_qualite AS qualite, c_clot.libelle AS statut, "
                    + "a.dateappel AS date_evenement, moa.libelle AS objet, sma.libelle AS detail, c_satis.libelle AS satisfaction, "
                    + "CASE WHEN (LENGTH (a.commentaire) < 40) THEN a.commentaire WHEN a.commentaire IS NULL THEN '' ELSE SUBSTR (a.commentaire, 1, 40) || '...' END resume_commentaire,"
                    + "c_clot.libelle as entrant_sortant, null as date_dossier_parent, null as courrier_id, null as ALIASRST, a.commentaire as COMMENTAIRE, null as RENVOI "
                    + "FROM hotline.appel a, hotline.codes c_clot, hotline.motifappel moa, hotline.smotifappel sma, hotline.codes c_satis "
                    + "WHERE a.adherent_id = ? AND a.cloture_code = c_clot.code AND a.motif_id = moa.ID(+) "
                    + "AND a.s_motif_id = sma.ID(+) AND a.satisfaction_code = c_satis.code ";

            String requeteEvenements = "SELECT 'EVENEMENT' AS type_objet, e.ID, nvl(c_qual.libelle, 'Adherent') AS qualite, s.libelle AS statut, "
                    + "e.date_creation AS date_evenement, ev_mot.libelle AS objet, ev_smot.libelle AS detail, NULL AS satisfaction, "
                    + "CASE WHEN (LENGTH (e.commentaire) < 40) THEN e.commentaire WHEN e.commentaire IS NULL THEN '' ELSE SUBSTR (e.commentaire, 1, 40) || '...' END resume_commentaire, "
                    + "e.type as entrant_sortant, e.COL14 as date_dossier_parent, e.COURRIER_ID as courrier_id, rst.rst_alias as ALIASRST, e.commentaire as COMMENTAIRE, e.RENVOI as RENVOI "
                    + "FROM evenement.evenement e, evenement.statut s, evenement.evenement_motif ev_mot, evenement.evenement_s_motif ev_smot, "
                    + "application.beneficiaire ben, application.codes c_qual, evenement.T_REFS_STATS_RST rst "
                    + "WHERE e.adherent_id = ? AND e.statut_id = s.ID AND e.motif_id = ev_mot.ID(+) "
                    + "AND e.sousmotif_id = ev_smot.ID(+) AND e.beneficiaire_id = ben.ID(+) "
                    + "AND ben.qualite_code = c_qual.code(+) "
                    + "AND EV_SMOT.RST_ID = RST.RST_ID(+) ";

            String union = " UNION ALL ";

            sb.append(requeteFiches);
            sb.append(union);
            sb.append(requeteEvenements);
            sb.append(" ORDER BY 5 DESC ");

            stmt = conn.prepareStatement(sb.toString());
            stmt.setString(1, idAdherent);
            stmt.setString(2, idAdherent);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Historique();
                unDisplay.setType(rs.getString(1));
                unDisplay.setId(rs.getString(2));
                unDisplay.setQualite(rs.getString(3));
                unDisplay.setStatut(rs.getString(4));
                unDisplay
                        .setDateEvenement((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtDDMMYYYYHHMMSS(rs.getTimestamp(5)) : "");
                unDisplay
                        .setDateEvenementTri((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtUSyyyyMMddHHmmss(rs.getTimestamp(5)) : "");
                unDisplay.setObjet((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setDetail((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setSatisfaction((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setDebutCommentaire((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setEstSortant( (rs.getString(10)!=null && _SORTANT.contains( rs.getString(10) )) ? _VRAI : _FAUX);
                unDisplay
                        .setDateCreationDossierParent((rs.getTimestamp(11) != null) ? UtilDate
                                .formatDDMMYYYY(rs.getTimestamp(11)) : "");
                unDisplay.setCourrier_id((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setAliasRST((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCommentaire((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setEstUnRenvoi((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("findHistoriqueAdherent", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Historique> findHistoriqueEtablissement(
            String idEtablissement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Historique> res = new ArrayList<Historique>();

        try {
            conn = getConnexion();

            Historique unDisplay = null;

            StringBuilder sb = new StringBuilder();
            String requeteFiches = "SELECT 'FICHE' AS type_objet,  a.ID, 'Entreprise'  AS qualite, c_clot.libelle as statut, a.dateappel AS date_evenement,"
                    + "moa.libelle AS objet, sma.libelle AS detail, c_satis.libelle AS satisfaction, "
                    + "CASE WHEN (LENGTH (a.commentaire) < 40) THEN a.commentaire WHEN a.commentaire IS NULL THEN '' ELSE SUBSTR (a.commentaire, 1, 40) || '...' END resume_commentaire,"
                    + "c_clot.libelle AS entrant_sortant,null as date_dossier_parent, null as courrier_id,  null as ALIASRST, a.commentaire as COMMENTAIRE, null as RENVOI "
                    + "FROM hotline.appel a, hotline.codes c_clot, hotline.motifappel moa, hotline.smotifappel sma, hotline.codes c_satis, "
                    + "application.etablissement etab "
                    + "WHERE etab.ID = ? AND a.etablissement_id = etab.ID "
                    + "AND a.cloture_code = c_clot.code AND a.motif_id = moa.ID(+) "
                    + "AND a.s_motif_id = sma.ID(+) "
                    + "AND a.satisfaction_code = c_satis.code ";

            String requeteEvenements = "SELECT 'EVENEMENT' AS type_objet, e.ID, 'Entreprise' AS qualite, s.libelle as statut, "
                    + "e.date_creation as date_evenement, ev_mot.libelle as objet, ev_smot.libelle as detail, null as satisfaction, "
                    + "CASE WHEN (LENGTH (e.commentaire) < 40) THEN e.commentaire WHEN e.commentaire IS NULL THEN '' ELSE SUBSTR (e.commentaire, 1, 40) || '...' END resume_commentaire, "
                    + "e.TYPE AS entrant_sortant, col14 as date_dossier_parent, e.COURRIER_ID as courrier_id, rst.rst_alias as ALIASRST, e.commentaire as COMMENTAIRE, e.RENVOI as RENVOI  "
                    + "FROM evenement.evenement e, evenement.statut s, evenement.evenement_motif ev_mot, "
                    + "evenement.evenement_s_motif ev_smot, evenement.T_REFS_STATS_RST rst "
                    + "WHERE e.statut_id = s.ID AND e.motif_id = ev_mot.ID(+) AND e.sousmotif_id = ev_smot.ID(+) "
                    + "AND e.BENEFICIAIRE_ID IS NULL AND EV_SMOT.RST_ID = RST.RST_ID(+) and e.ETABLT_ID = ?  ";

            String union = " UNION ALL ";

            sb.append(requeteFiches);
            sb.append(union);
            sb.append(requeteEvenements);
            sb.append(" ORDER BY 5 DESC ");

            stmt = conn.prepareStatement(sb.toString());
            stmt.setString(1, idEtablissement);
            stmt.setString(2, idEtablissement);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Historique();
                unDisplay.setType(rs.getString(1));
                unDisplay.setId(rs.getString(2));
                unDisplay.setQualite(rs.getString(3));
                unDisplay.setStatut(rs.getString(4));
                unDisplay
                        .setDateEvenement((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtDDMMYYYYHHMMSS(rs.getTimestamp(5)) : "");
                unDisplay
                        .setDateEvenementTri((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtUSyyyyMMddHHmmss(rs.getTimestamp(5)) : "");
                unDisplay.setObjet((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setDetail((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setSatisfaction((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setDebutCommentaire((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setEstSortant((rs.getString(10) != null && _SORTANT.contains( rs.getString(10))) ? _VRAI : _FAUX);
                unDisplay
                        .setDateCreationDossierParent((rs.getTimestamp(11) != null) ? UtilDate
                                .fmtDDMMYYYYHHMMSS(rs.getTimestamp(11)) : "");
                unDisplay.setCourrier_id((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setAliasRST((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCommentaire((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setEstUnRenvoi((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("findHistoriqueEtablissement", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Historique> findHistoriqueAppelant(
            String idAppelant) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Historique> res = new ArrayList<Historique>();

        try {
            conn = getConnexion();

            Historique unDisplay = null;

            StringBuilder sb = new StringBuilder();

            String requeteFiches = "SELECT 'FICHE' AS type_objet, a.ID, a.beneficiaire_qualite AS qualite, c_clot.libelle AS statut, "
                    + "a.dateappel AS date_evenement, moa.libelle AS objet, sma.libelle AS detail, c_satis.libelle AS satisfaction, "
                    + "CASE WHEN (LENGTH (a.commentaire) < 40) THEN a.commentaire WHEN a.commentaire IS NULL THEN '' ELSE SUBSTR (a.commentaire, 1, 40) || '...' END resume_commentaire, "
                    + "c_clot.libelle as entrant_sortant, null as date_dossier_parent, null AS courrier_id, null AS aliasrst, a.commentaire as commentaire, null as renvoi  "
                    + "FROM hotline.appel a, hotline.codes c_clot, hotline.motifappel moa, hotline.smotifappel sma, "
                    + "hotline.codes c_satis WHERE a.appelant_id = ? AND a.cloture_code = c_clot.code "
                    + "AND a.motif_id = moa.ID(+) AND a.s_motif_id = sma.ID(+) AND a.satisfaction_code = c_satis.code ";

            String requeteEvenements = "SELECT 'EVENEMENT' AS type_objet, e.ID, 'Assure Hors Base' AS qualite, s.libelle AS statut, "
                    + "e.date_creation AS date_evenement, ev_mot.libelle AS objet, ev_smot.libelle AS detail, NULL AS satisfaction,"
                    + "CASE WHEN (LENGTH (e.commentaire) < 40) THEN e.commentaire WHEN e.commentaire IS NULL THEN '' ELSE SUBSTR (e.commentaire, 1, 40) || '...'  END resume_commentaire,"
                    + "e.TYPE AS entrant_sortant, col14 AS date_dossier_parent, e.courrier_id AS courrier_id, rst.rst_alias AS aliasrst, e.commentaire as commentaire, e.RENVOI as RENVOI  "
                    + "FROM evenement.evenement e, evenement.statut s,  evenement.evenement_motif ev_mot, evenement.evenement_s_motif ev_smot,"
                    + "evenement.t_refs_stats_rst rst "
                    + "WHERE e.statut_id = s.ID AND e.motif_id = ev_mot.ID(+) "
                    + "AND e.sousmotif_id = ev_smot.ID(+) AND ev_smot.rst_id = rst.rst_id(+) "
                    + "AND ( e.COL26 = ? OR e.APPELANt_ID = ? ) ";

            String union = " UNION ALL ";

            sb.append(requeteFiches);
            sb.append(union);
            sb.append(requeteEvenements);

            sb.append(" ORDER BY 5 DESC ");

            stmt = conn.prepareStatement(sb.toString());
            stmt.setString(1, idAppelant);
            stmt.setString(2, idAppelant);
            stmt.setString(3, idAppelant);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Historique();
                unDisplay.setType(rs.getString(1));
                unDisplay.setId(rs.getString(2));
                unDisplay.setQualite((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setStatut((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay
                        .setDateEvenement((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtDDMMYYYYHHMMSS(rs.getTimestamp(5)) : "");
                unDisplay
                        .setDateEvenementTri((rs.getTimestamp(5) != null) ? UtilDate
                                .fmtUSyyyyMMddHHmmss(rs.getTimestamp(5)) : "");
                unDisplay.setObjet((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setDetail((rs.getString(7) != null) ? rs.getString(7)
                        : "");
                unDisplay.setSatisfaction((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setDebutCommentaire((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setEstSortant((rs.getString(10) != null && _SORTANT.contains( rs.getString(10))) ? _VRAI : _FAUX);
                unDisplay
                        .setDateCreationDossierParent((rs.getTimestamp(11) != null) ? UtilDate
                                .fmtDDMMYYYYHHMMSS(rs.getTimestamp(11)) : "");
                unDisplay.setCourrier_id((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setAliasRST((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCommentaire((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setEstUnRenvoi((rs.getString(15) != null) ? rs
                        .getString(15) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();

            return res;
        } catch (Exception e) {
            LOGGER.error("findHistoriqueAppelant", e);
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<String> getDatesDecomptes(
            Map<String, String> criteres) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<String> res = new ArrayList<String>();

        try {
            conn = getConnexion();

            String unDisplay = "";
            int compteur = 1;

            StringBuilder requeteDatesDecomptes = new StringBuilder("");
            requeteDatesDecomptes
                    .append("SELECT DISTINCT d.DATE_DECOMPTE "
                            + "FROM application.beneficiaire ben, application.decompte d, application.prestation prest "
                            + "WHERE ben.ID = prest.BENEFICIAIRE_ID and prest.DECOMPTE_ID = d.ID and ben.MUTUELLE_ID = ? ");

            String idMutuelle = (String) criteres.get("IDMUTUELLE");
            String codeBeneficiaire = (String) criteres.get("CODEBENEFICIAIRE");

            // CODEBENEFICIAIRE
            if (codeBeneficiaire != null && !"".equals(codeBeneficiaire)) {
                requeteDatesDecomptes.append(" and ben.CODE = ? ");
            }

            requeteDatesDecomptes.append(" ORDER BY d.DATE_DECOMPTE DESC");

            // BIND VARIABLES

            // IDMUTUELLE n'est jamais null ou vide
            stmt = conn.prepareStatement(requeteDatesDecomptes.toString());

            stmt.setString(1, idMutuelle);

            // CODEBENEFICIAIRE
            if (codeBeneficiaire != null && !"".equals(codeBeneficiaire)) {
                stmt.setString(compteur + 1, codeBeneficiaire);
                compteur++;
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new String(_fmtddMMyyyy.format(rs.getTimestamp(1)));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getDatesDecomptes", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Decompte> getDecomptes(Map<String, String> criteres) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Decompte> res = new ArrayList<Decompte>();

        try {
            conn = getConnexion();

            Decompte unDisplay = null;
            int compteur = 1;

            StringBuilder requeteDecomptes = new StringBuilder("");
            requeteDecomptes
                    .append("SELECT DISTINCT d.ID, d.DATE_DECOMPTE, d.NUMERO_DECOMPTE FROM application.beneficiaire ben, application.decompte d, application.prestation prest, application.ACTE act WHERE ben.ID = prest.BENEFICIAIRE_ID and prest.DECOMPTE_ID = d.ID and prest.ACTE_ID = act.ID and ben.MUTUELLE_ID = ? ");

            String idMutuelle = (String) criteres.get("IDMUTUELLE");
            String codeBeneficiaire = (String) criteres.get("CODEBENEFICIAIRE");
            String idBeneficiaire = (String) criteres.get("IDBENEFICIAIRE");
            String dateDecompte = (String) criteres.get("DATEDECOMPTE");
            String codeActe = (String) criteres.get("CODEACTE");

            // CODEBENEFICIAIRE
            if (codeBeneficiaire != null && !"".equals(codeBeneficiaire)) {
                requeteDecomptes.append(" and ben.CODE = ? ");
            }

            // IDBENEFICIAIRE
            if (idBeneficiaire != null && !"".equals(idBeneficiaire)) {
                requeteDecomptes.append(" AND ben.ID = ? ");
            }

            // DATEDECOMPTE
            if (dateDecompte != null && !"".equals(dateDecompte)) {
                requeteDecomptes
                        .append(" AND d.DATE_DECOMPTE = to_date(?, 'dd/mm/yyyy') ");
            }

            // CODEACTE
            if (codeActe != null && !"".equals(codeActe)) {
                requeteDecomptes.append(" and act.CODE = ? ");
            }

            requeteDecomptes.append(" ORDER BY d.DATE_DECOMPTE DESC");

            // BIND VARIABLES
            // IDMUTUELLE n'est jamais null ou vide
            stmt = conn.prepareStatement(requeteDecomptes.toString());

            stmt.setString(1, idMutuelle);

            // CODEBENEFICIAIRE
            if (codeBeneficiaire != null && !"".equals(codeBeneficiaire)) {
                stmt.setString(compteur + 1, codeBeneficiaire);
                compteur++;
            }

            // IDBENEFICIAIRE
            if (idBeneficiaire != null && !"".equals(idBeneficiaire)) {
                stmt.setString(compteur + 1, idBeneficiaire);
                compteur++;
            }

            // DATEDECOMPTE
            if (dateDecompte != null && !"".equals(dateDecompte)) {
                stmt.setString(compteur + 1, dateDecompte);
                compteur++;
            }

            // CODEACTE
            if (codeActe != null && !"".equals(codeActe)) {
                stmt.setString(compteur + 1, codeActe);
                compteur++;
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Decompte();
                unDisplay.setId(rs.getString(1));
                unDisplay.setDateDecompte(_fmtddMMyyyy.format(rs
                        .getTimestamp(2)));
                unDisplay.setNumeroDecompte(rs.getString(3));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getDecomptes", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Acte> getAllCodesActes(Map<String, String> criteres) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Acte> res = new ArrayList<Acte>();

        try {
            conn = getConnexion();

            Acte unDisplay = null;

            String idMutuelle = (String) criteres.get("IDMUTUELLE");
            String codeBeneficiaire = (String) criteres.get("CODEBENEFICIAIRE");

            String requeteCodesActes = "SELECT DISTINCT act.ID, act.code, act.libelle "
                    + "FROM application.DECOMPTE d, application.PRESTATION prest, application.ACTE act, application.BENEFICIAIRE ben "
                    + "WHERE prest.beneficiaire_id = ben.ID AND prest.decompte_id = d.ID "
                    + "AND prest.acte_id = act.ID AND ben.ID = prest.beneficiaire_id "
                    + "AND ben.code = ? AND ben.mutuelle_id = ? order by code asc";

            stmt = conn.prepareStatement(requeteCodesActes);
            stmt.setString(1, codeBeneficiaire);
            stmt.setString(2, idMutuelle);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Acte();
                unDisplay.setId(rs.getString(1));
                unDisplay.setCode(rs.getString(2));
                unDisplay.setLibelle(rs.getString(3));

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getAllCodesActes", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Prestation> getPrestationsDecompte(
            String idDecompte, String idBeneficiaire, String codeActe) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Prestation unDisplay = null;
        int compteur = 1;

        Collection<Prestation> res = new ArrayList<Prestation>();

        try {

            StringBuilder requetePrestations = new StringBuilder("");
            requetePrestations
                    .append("SELECT pers.PRENOM, pers.nom, prest.libelle, prest.dateprescription, prest.montantdepense, ");
            requetePrestations
                    .append("prest.remboursementro, prest.REMBOURSEMENTMUTUELLE, prest.DATEREMBOURSEMENTMUTUELLE, act.CODE, ");
            requetePrestations
                    .append("decode(prest.DESTPAIEMENT, '000000000', 'Assur&eacute;', 'Tiers') ");
            requetePrestations
                    .append("FROM application.prestation prest, application.decompte DEC, application.beneficiaire ben, ");
            requetePrestations
                    .append("application.personne pers, application.ACTE act WHERE prest.decompte_id = DEC.ID ");
            requetePrestations
                    .append("and prest.BENEFICIAIRE_ID = ben.ID and ben.PERSONNE_ID = pers.id ");
            requetePrestations
                    .append("AND prest.ACTE_ID = act.ID AND DEC.ID = ?");
            conn = getConnexion();

            if (!"".equals(idBeneficiaire)) {
                requetePrestations.append(" AND ben.ID = ?");
            }

            if (!"".equals(codeActe)) {
                requetePrestations.append(" AND act.CODE = ?");
            }

            stmt = conn.prepareStatement(requetePrestations.toString());

            stmt.setString(1, idDecompte);

            if (!"".equals(idBeneficiaire)) {
                stmt.setString(compteur + 1, idBeneficiaire);
                compteur++;
            }

            if (!"".equals(codeActe)) {
                stmt.setString(compteur + 1, codeActe);
                compteur++;
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Prestation();
                unDisplay.setBeneficiaire(rs.getString(1) + " "
                        + rs.getString(2));
                unDisplay.setLibelle(rs.getString(3));
                unDisplay.setDateSoin((Date) rs.getTimestamp(4));
                unDisplay.setDepense(rs.getFloat(5));
                unDisplay.setRemboursementSecu(rs.getFloat(6));
                unDisplay.setRemboursementMutuelle(rs.getFloat(7));
                unDisplay.setDateRemboursement((Date) rs.getTimestamp(8));
                unDisplay.setCodeActe(rs.getString(9));
                unDisplay.setDestPaiement((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getPrestationsDecompte", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static RIB getRibById(String idRib) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            RIB unDisplay = null;
            String requete = "SELECT r.ID, c.LIBELLE, r.CODEINTERBANCAIRE, r.CODEGUICHET, r.NUMEROCOMPTE, r.CLERIB, r.NOMETABLISSEMENT, r.DEBUTEFFET, "
                    + "r.CLE_IBAN, r.CODE_PAYS, r.IDENTIFIANT_NATIONAL_COMPTE, r.CODE_BIC "
                    + " FROM application.RIB r, application.codes c where r.ID = ? and r.CODEINTERBANCAIRE = c.CODE(+) ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idRib);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new RIB();
                unDisplay.setID(idRib);
                unDisplay.setTYPERIB((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCODEINTERBANCAIRE((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setCODEGUICHET((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNUMEROCOMPTE((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCLERIB((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setNOMETABLISSEMENT((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setDEBUTEFFET((rs.getTimestamp(8) != null) ? rs
                        .getTimestamp(8) : null);

                unDisplay.setCLE_IBAN((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setCODE_PAYS((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay
                        .setIDENTIFIANT_NATIONAL_COMPTE((rs.getString(11) != null) ? rs
                                .getString(11) : "");
                unDisplay.setCODE_BIC((rs.getString(12) != null) ? rs
                        .getString(12) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getRibById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static InfosRO getInfosRO(String beneficiaire_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            InfosRO unDisplay = null;

            String requete = "select pos_noe.LIBELLE, reg.LIBELLE, ben.CAISSERO, ben.CENTRERO  "
                    + "FROM application.beneficiaire ben, application.codes pos_noe, application.codes reg "
                    + "where ben.id = ? and ben.POSNOEMIE_CODE = pos_noe.code(+) and ben.REGIME_CODE = reg.code(+)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, beneficiaire_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new InfosRO();
                unDisplay.setLibelle((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setRegime((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setCaisse((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setCentre((rs.getString(4) != null) ? rs.getString(4)
                        : "");
            }
            stmt.clearParameters();

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getInfosRO", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<ContratBeneficiaire> getContratsBeneficiaire(
            String idBeneficiaire, int statut) {
        // Ramène les contrats d'un bénéficiaire
        // Statut :
        // 0 : inactifs
        // 1 : actifs
        // 2 : anticipés
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<ContratBeneficiaire> res = new ArrayList<ContratBeneficiaire>();
        try {
            ContratBeneficiaire unDisplay = null;
            StringBuilder requete = new StringBuilder();
            requete.append("SELECT cod.libelle, ca.produit, cb.datesouscription, cb.dateradiation, cb.datedebut, cb.datefin, ");
            requete.append("cb.contrat_adherent_id, modpaie.libelle, freqapp.libelle,cod.MUTUELLE_ID, ca.NUMCONTRAT, cb.ID, ");
            requete.append("pm.RAISON_SOCIALE, decode(conten_adh.CONTRAT_ADHERENT_ID, NULL, '0', '1') as CONTENTIEUX, ");
            requete.append("conten_adh.RAISON_CONTENTIEUX, conten_adh.DATE_CONTENTIEUX ");
            requete.append("FROM application.contrat_beneficiaire cb, application.contentieux_adherent conten_adh, ");
            requete.append("application.contrat_adherent ca, application.codes cod, application.codes modpaie, application.codes freqapp, ");
            requete.append("application.t_agence_agn agn, application.personnesmorales pm ");
            requete.append("WHERE cb.beneficiaire_id = ? AND cb.contrat_adherent_id = ca.ID ");
            requete.append("AND ca.type_code = cod.code(+) ");
            requete.append("AND ca.modepaiement = modpaie.code(+) AND ca.frequenceappel = freqapp.code(+) ");
            requete.append("AND ca.ID = conten_adh.CONTRAT_ADHERENT_ID(+) ");
            requete.append("AND application.PS_GETETATCONTRATBENEF(cb.ID)=? ");
            requete.append("and ca.DISTRIB_AGENCE_ID = agn.AGN_ID(+) and agn.AGN_PMO_ID = pm.ID(+) ");
            requete.append("order by 1 asc ");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, idBeneficiaire);
            stmt.setInt(2, statut);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ContratBeneficiaire();
                unDisplay.setTypeContrat((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeRT((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setDateSouscription(rs.getTimestamp(3));
                unDisplay.setDateRadiation(rs.getTimestamp(4));
                unDisplay.setDateDedutDroit(rs.getTimestamp(5));
                unDisplay.setDateFinDroit(rs.getTimestamp(6));
                unDisplay.setIdContratAdherent((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setModePaiement((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setFrequenceAppel((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setIdMutuelle((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setNumeroContrat((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay
                        .setIdContratBeneficiaire((rs.getString(12) != null) ? rs
                                .getString(12) : "");
                unDisplay.setCourtier((rs.getString(13) != null) ? rs
                        .getString(13) : "");

                unDisplay.setContentieux((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setContentieuxRaison((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setContentieuxDate(rs.getTimestamp(16));

                unDisplay.setIdBeneficiaire(idBeneficiaire);

                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;

            if (res != null && !res.isEmpty()) {
                requete.delete(0, requete.length());
                requete.append("SELECT g.id, g.LIBELLE_RT, g.DOCUMENT from application.garantie g where g.MUTUELLE_ID = ? and g.CODE_RT = ?");
                ContratBeneficiaire dcbtemp = null;
                for (Iterator<ContratBeneficiaire> i = res.iterator(); i
                        .hasNext();) {
                    dcbtemp = (ContratBeneficiaire) i.next();
                    stmt = conn.prepareStatement(requete.toString());
                    stmt.setString(1, dcbtemp.getIdMutuelle());
                    stmt.setString(2, dcbtemp.getCodeRT());
                    Blob blob = null;
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        dcbtemp.setIdGarantie((rs.getString(1) != null) ? rs
                                .getString(1) : "");
                        dcbtemp.setLibelleRT((rs.getString(2) != null) ? rs
                                .getString(2) : "");
                        blob = rs.getBlob(3);
                        if (blob != null) {
                            dcbtemp.setHasBlob(true);
                        }
                        blob = null;
                    }
                }
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("getContratsBeneficiaire", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<ContratEtablissement> getContratsEtablissement(
            String idEtablissement, int statut) {

        // Ramène les contrats d'un établissement : NIVEAU 1
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<ContratEtablissement> res = new ArrayList<ContratEtablissement>();
        try {

            ContratEtablissement unDisplay = null;
            StringBuilder requete = new StringBuilder();

            requete.append("SELECT cod.libelle, ce.produit, ce.date_souscription, ce.date_effet, ");
            requete.append("ce.date_creation_radiation,  ce.id,  modpaie.libelle, freqapp.libelle, cod.mutuelle_id, ce.NUMCONTRAT,  ");
            requete.append("pm.RAISON_SOCIALE, decode(conten_etab.CONTRAT_ETABLISSEMENT_ID, NULL, '0', '1') as CONTENTIEUX, ");
            requete.append("conten_etab.RAISON_CONTENTIEUX, conten_etab.DATE_CONTENTIEUX ");
            requete.append("FROM application.contrat_etablissement ce, application.contentieux_etablissement conten_etab, ");
            requete.append("application.codes cod, application.codes modpaie, application.codes freqapp, ");
            requete.append("application.t_agence_agn agn, application.personnesmorales pm ");
            requete.append("WHERE ce.etablissement_id = ? AND ce.typecontrat_code = cod.code(+) ");
            requete.append("AND ce.modepaiement = modpaie.code(+) AND ce.frequenceappel = freqapp.code(+) ");
            requete.append("AND ce.ID = conten_etab.CONTRAT_ETABLISSEMENT_ID(+) ");
            requete.append("AND APPLICATION.PS_GETETATCONTRATETAB(ce.id)=?  ");
            requete.append("AND ce.DISTRIB_AGENCE_ID = agn.AGN_ID(+) and agn.AGN_PMO_ID = pm.ID(+) ");

            requete.append(_UNION);

            requete.append("SELECT cod.libelle, ce.produit, ce.date_souscription, ce.date_effet, ");
            requete.append("ce.date_creation_radiation,  ce.id,  modpaie.libelle, freqapp.libelle, cod.mutuelle_id, ce.NUMCONTRAT,  ");
            requete.append("pm.RAISON_SOCIALE, decode(conten_etab.CONTRAT_ETABLISSEMENT_ID, NULL, '0', '1') as CONTENTIEUX, ");
            requete.append("conten_etab.RAISON_CONTENTIEUX, conten_etab.DATE_CONTENTIEUX ");
            requete.append("FROM APPLICATION.CONTRAT_GROUPEASSURE cga, application.contrat_etablissement ce, application.contentieux_etablissement conten_etab, ");
            requete.append("application.codes cod, application.codes modpaie, application.codes freqapp, ");
            requete.append("application.t_agence_agn agn, application.personnesmorales pm ");
            requete.append("WHERE cga.CONTRAT_ETABLISSEMENT_ID = CE.ID AND cga.etablissement_id = ? AND ce.typecontrat_code = cod.code(+) ");
            requete.append("AND ce.modepaiement = modpaie.code(+) AND ce.frequenceappel = freqapp.code(+) ");
            requete.append("AND ce.ID = conten_etab.CONTRAT_ETABLISSEMENT_ID(+) ");
            requete.append("AND APPLICATION.PS_GETETATCONTRATETAB(ce.id)=?  ");
            requete.append("AND ce.DISTRIB_AGENCE_ID = agn.AGN_ID(+) and agn.AGN_PMO_ID = pm.ID(+) ");

            requete.append("order by 1 asc ");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, idEtablissement);
            stmt.setInt(2, statut);

            stmt.setString(3, idEtablissement);
            stmt.setInt(4, statut);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ContratEtablissement();
                unDisplay.setTypeContrat((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeRT((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setDateSouscription(rs.getTimestamp(3));
                unDisplay.setDateDedutDroit(rs.getTimestamp(4));
                unDisplay.setDateRadiation(rs.getTimestamp(5));
                unDisplay
                        .setIdContratEtablissement((rs.getString(6) != null) ? rs
                                .getString(6) : "");
                unDisplay.setModePaiement((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setFrequenceAppel((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setIdMutuelle((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setNumeroContrat((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setCourtier((rs.getString(11) != null) ? rs
                        .getString(11) : "");

                unDisplay.setContentieux((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setContentieuxRaison((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setContentieuxDate(rs.getTimestamp(14));

                unDisplay.setIdEtablissement(idEtablissement);
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;

            if (res != null && !res.isEmpty()) {
                requete.delete(0, requete.length());
                requete.append("SELECT g.id, g.LIBELLE_RT, g.DOCUMENT from application.garantie g where g.MUTUELLE_ID = ? and g.CODE_RT = ?");
                ContratEtablissement dcetemp = null;
                for (Iterator<ContratEtablissement> i = res.iterator(); i
                        .hasNext();) {
                    dcetemp = (ContratEtablissement) i.next();
                    stmt = conn.prepareStatement(requete.toString());
                    stmt.setString(1, dcetemp.getIdMutuelle());
                    stmt.setString(2, dcetemp.getCodeRT());
                    Blob blob = null;
                    rs = stmt.executeQuery();
                    while (rs.next()) {

                        dcetemp.setIdGarantie((rs.getString(1) != null) ? rs
                                .getString(1) : "");
                        dcetemp.setLibelleRT((rs.getString(2) != null) ? rs
                                .getString(2) : "");
                        blob = rs.getBlob(3);
                        if (blob != null) {
                            dcetemp.setHasBlob(true);
                        }
                        blob = null;
                    }
                    stmt.close();
                }
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("getContratsEtablissement", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<DetailContratBeneficiaire> getDetailsContratBeneficiaire(
            String idContratBeneficiaire) {

        // Ramène les risques optionnels d'un contrat de bénéficiaire
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<DetailContratBeneficiaire> res = new ArrayList<DetailContratBeneficiaire>();

        try {

            DetailContratBeneficiaire unDisplay = null;
            StringBuilder requete = new StringBuilder();

            requete.append("SELECT DISTINCT ce.numcontrat AS numero_contrat, NULL AS code_groupe_assures,"
                    + "e.libelle AS libelle_groupe_assures, 0 AS ID,"
                    + "NULL AS date_souscription, NULL AS date_radiation, 1 AS trie,cga.etablissement_id AS etablissement_id "
                    + "FROM application.contrat_groupeassure cga, application.contrat_adherent cadh, application.contrat_etablissement ce, "
                    + "application.etablissement e, application.contrat_beneficiaire cb "
                    + "WHERE cadh.contrat_groupeassure_id = cga.ID "
                    + "AND cga.contrat_etablissement_id = ce.ID "
                    + "AND cga.etablissement_id = e.ID "
                    + "AND cb.ID = ? "
                    + "AND cadh.ID = cb.contrat_adherent_id AND ce.ETABLISSEMENT_ID <> e.ID "
                    + " UNION ALL "
                    + "SELECT DISTINCT ce.numcontrat AS numero_contrat, "
                    + "cga.code AS code_groupe_assures, "
                    + "cga.libelle AS libelle_groupe_assures, cga.ID AS ID, "
                    + "cga.date_effet AS date_souscription, "
                    + "cga.date_fin AS date_radiation, 2 AS trie, "
                    + "e.ID AS etablissement_id "
                    + "FROM application.contrat_groupeassure cga, "
                    + "application.contrat_adherent cadh, "
                    + "application.contrat_etablissement ce, "
                    + "application.etablissement e, "
                    + "application.contrat_beneficiaire cb "
                    + "WHERE cadh.contrat_groupeassure_id = cga.ID "
                    + "AND cga.contrat_etablissement_id = ce.ID "
                    + "AND cb.ID = ? "
                    + "AND cga.etablissement_id = e.ID "
                    + "AND cadh.ID = cb.contrat_adherent_id "
                    + "ORDER BY 1, 8, 7");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, idContratBeneficiaire);
            stmt.setString(2, idContratBeneficiaire);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new DetailContratBeneficiaire();
                unDisplay.setNumeroContrat((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeGroupeAssures((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay
                        .setLibelleGroupeAssures((rs.getString(3) != null) ? rs
                                .getString(3) : "");
                unDisplay.setId(rs.getString(4));
                unDisplay.setDateSouscription((rs.getTimestamp(5) != null) ? rs
                        .getTimestamp(5) : null);
                unDisplay.setDateRadiation((rs.getTimestamp(6) != null) ? rs
                        .getTimestamp(6) : null);
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getDetailsContratBeneficiaire", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static Collection<DetailContratEtablissement> getDetailsContratEtablissement(
            String idContratEtablissement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<DetailContratEtablissement> res = new ArrayList<DetailContratEtablissement>();

        try {

            DetailContratEtablissement unDisplay = null;

            String requete = "select distinct CE.NUMCONTRAT as NUMERO_CONTRAT, null as CODE_GROUPE_ASSURES, e.libelle as LIBELLE_GROUPE_ASSURES,"
                    + "null AS ID, e.DATESOUSCRIPTION as DATE_SOUSCRIPTION, e.DATERADIATION as DATE_RADIATION, 1 AS TRIE, e.id AS ETABLISSEMENT_ID, null as col_tri "
                    + "from application.contrat_etablissement ce, application.contrat_groupeassure cga, application.etablissement e "
                    + "where ce.id = CGA.CONTRAT_ETABLISSEMENT_ID and CE.id = ? and CGA.ETABLISSEMENT_ID = e.id AND ce.etablissement_id <> e.ID "
                    + " UNION ALL "
                    + "select distinct CE.NUMCONTRAT as NUMERO_CONTRAT, CGA.CODE as CODE_GROUPE_ASSURES, cga.libelle as LIBELLE_GROUPE_ASSURES,"
                    + "cga.id AS ID, CGA.DATE_EFFET as DATE_SOUSCRIPTION, CGA.DATE_FIN as DATE_RADIATION, 2 AS TRIE, e.id AS ETABLISSEMENT_ID, util.fs_isNumber(CGA.CODE)  as col_tri  "
                    + "from application.contrat_etablissement ce, application.contrat_groupeassure cga, application.etablissement e "
                    + "where ce.id = CGA.CONTRAT_ETABLISSEMENT_ID and CE.id = ? and CGA.ETABLISSEMENT_ID = e.id "
                    + "ORDER BY 1, 8, 7, 9 DESC, 2";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratEtablissement);
            stmt.setString(2, idContratEtablissement);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new DetailContratEtablissement();
                unDisplay.setNumeroContrat((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeGroupeAssures((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay
                        .setLibelleGroupeAssures((rs.getString(3) != null) ? rs
                                .getString(3) : "");
                unDisplay.setId(rs.getString(4));
                unDisplay.setDateSouscription((rs.getTimestamp(5) != null) ? rs
                        .getTimestamp(5) : null);
                unDisplay.setDateRadiation((rs.getTimestamp(6) != null) ? rs
                        .getTimestamp(6) : null);

                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getDetailsContratEtablissement", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<GarantieRecherche> getIDsGarantiesAssureContrat(
            String contrat_adherent_id, String id_beneficiaire) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<GarantieRecherche> res = new ArrayList<GarantieRecherche>();

        try {

            GarantieRecherche unDisplay = null;
            String requete = "SELECT pla.PLA_ID FROM application.CONTRAT_BENEFICIAIRE contben,  application.CONTRAT_ADHERENT contadh,  application.T_PLAQUETTES_PLA pla "
                    + " WHERE contben.contrat_adherent_id = ? AND contben.beneficiaire_id = ? AND contben.contrat_adherent_id = contadh.id "
                    + "AND contadh.produit_id = pla.PLA_PRODUIT_ID "
                    + _UNION
                    + "SELECT pla.PLA_ID FROM application.CONTRAT_BENEFICIAIRE_OPTION cbo, application.T_PLAQUETTES_PLA pla, application.RISQUE_OPTION ro "
                    + "WHERE cbo.contrat_adherent_id = ? AND cbo.beneficiaire_id = ? AND cbo.risque_option_id = ro.id AND ro.risque_id = pla.PLA_RISQUE_ID "
                    + _UNION
                    + "SELECT pla.PLA_ID FROM application.CONTRAT_BENEFICIAIRE_OPTION cbo, application.T_PLAQUETTES_PLA pla WHERE cbo.contrat_adherent_id = ? "
                    + " AND cbo.beneficiaire_id = ? AND cbo.risque_option_id = pla.PLA_RISQUE_OPTION_ID";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, contrat_adherent_id);
            stmt.setString(2, id_beneficiaire);
            stmt.setString(3, contrat_adherent_id);
            stmt.setString(4, id_beneficiaire);
            stmt.setString(5, contrat_adherent_id);
            stmt.setString(6, id_beneficiaire);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new GarantieRecherche();
                unDisplay.setPLA_ID(rs.getString(1));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getIDsGarantiesAssureContrat", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<GarantieRecherche> getIDsGarantiesEtablissementContrat(
            String contrat_etablissement_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<GarantieRecherche> res = new ArrayList<GarantieRecherche>();

        try {

            GarantieRecherche unDisplay = null;

            String requete = "SELECT pla.pla_id, pla.pla_blocage, pla.pla_utl_blocage, pla.pla_date_blocage, mut.ID, mut.libelle, "
                    + "pla.pla_produit_id, pla.pla_risque_id, pla.pla_risque_option_id, reg.reg_id, reg.reg_lib, pla.pla_createur_id, "
                    + "pla.pla_date_creation, prs.prs_nom || ' ' || prs.prs_prenom "
                    + "FROM application.t_plaquettes_pla pla, application.mutuelle mut, application.t_regimes_reg reg, h_annuaire.t_utilisateurs_utl utl,"
                    + "h_annuaire.t_personnes_prs prs "
                    + "WHERE pla.pla_mutuelle_id = mut.ID AND pla.pla_regime_id = reg.reg_id AND pla.pla_createur_id = utl.utl_id "
                    + "AND utl.utl_prs_id = prs.prs_id AND pla.pla_id IN ( "
                    + "SELECT pla.pla_id FROM application.contrat_etablissement contetab, application.t_plaquettes_pla pla "
                    + "WHERE contetab.ID = ? AND contetab.produit_id = pla.pla_produit_id "
                    + _UNION
                    + "SELECT pla.pla_id "
                    + "FROM application.contrat_etablissement contetab, APPLICATION.CONTRAT_GROUPEASSURE cga, APPLICATION.CONTRAT_GROUPEASSURE_OPTION cgao,"
                    + "application.t_plaquettes_pla pla, application.risque_option ro "
                    + "WHERE contetab.ID = ? AND contetab.id = CGA.CONTRAT_ETABLISSEMENT_ID ANd CGA.ID = cgao.CONTRAT_GROUPEASSURE_ID "
                    + "AND cgao.risque_option_id = ro.ID AND ro.risque_id = pla.pla_risque_id "
                    + _UNION
                    + "SELECT pla.pla_id "
                    + "FROM application.contrat_etablissement contetab, APPLICATION.CONTRAT_GROUPEASSURE cga, APPLICATION.CONTRAT_GROUPEASSURE_OPTION cgao, "
                    + "application.t_plaquettes_pla pla "
                    + "WHERE contetab.ID = ? AND contetab.ID = CGA.CONTRAT_ETABLISSEMENT_ID ANd CGA.ID = cgao.CONTRAT_GROUPEASSURE_ID "
                    + "AND cgao.risque_option_id = pla.pla_risque_option_id)";

            conn = getConnexion();

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, contrat_etablissement_id);
            stmt.setString(2, contrat_etablissement_id);
            stmt.setString(3, contrat_etablissement_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new GarantieRecherche();
                unDisplay.setPLA_ID(rs.getString(1));
                unDisplay.setPLA_BLOCAGE(rs.getString(2));
                unDisplay.setPLA_UTL_BLOCAGE(rs.getString(3));
                unDisplay.setPLA_DATE_BLOCAGE(rs.getTimestamp(4));
                unDisplay.setPLA_MUTUELLE_ID(rs.getString(5));
                unDisplay.setLIBELLE_MUTUELLE((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setPLA_PRODUIT_ID(rs.getString(7));
                unDisplay.setPLA_RISQUE_ID(rs.getString(8));
                unDisplay.setPLA_RISQUE_OPTION_ID(rs.getString(9));
                unDisplay.setPLA_REGIME_ID(rs.getString(10));
                unDisplay.setLIBELLE_REGIME((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setPLA_CREATEUR_ID(rs.getString(12));
                unDisplay.setPLA_DATE_CREATION(rs.getTimestamp(13));

                unDisplay
                        .setNOM_PRENOM_CREATEUR((rs.getString(14) != null) ? rs
                                .getString(14) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getIDsGarantiesEtablissementContrat", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static GarantieRecherche getGarantie(String idVersion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        GarantieRecherche unDisplay = null;
        try {
            String requete = "SELECT pla.pla_id, reg.REG_LIB, p.CODERT || NVL2(p.libelle,  ' (' || p.libelle || ')', ''), '1' as NIVEAU  FROM application.T_PLAQUETTES_PLA pla, "
                    + "application.T_REGIMES_REG reg, application.PRODUIT p WHERE pla.PLA_ID = ? "
                    + "AND pla.PLA_REGIME_ID = reg.REG_ID AND pla.PLA_PRODUIT_ID = p.ID "
                    + _UNION
                    + "SELECT pla.pla_id,  reg.REG_LIB, r.CODE ||  NVL2(r.libelle,  ' (' || r.libelle || ')', '') ,  '2' as NIVEAU FROM application.T_PLAQUETTES_PLA pla, application.T_REGIMES_REG reg, application.RISQUE r "
                    + " WHERE pla.PLA_ID = ? AND pla.PLA_REGIME_ID = reg.REG_ID AND pla.PLA_RISQUE_ID = r.ID "
                    + _UNION
                    + "SELECT pla.pla_id,  reg.REG_LIB, ro.CODE || NVL2(ro.libelle,  ' (' || ro.libelle || ')', '') ,  '3' as NIVEAU  FROM application.T_PLAQUETTES_PLA pla, application.T_REGIMES_REG reg, application.RISQUE_OPTION ro "
                    + "WHERE pla.PLA_ID = ? "
                    + "AND pla.PLA_REGIME_ID = reg.REG_ID "
                    + "AND pla.PLA_RISQUE_OPTION_ID = ro.ID";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idVersion);
            stmt.setString(2, idVersion);
            stmt.setString(3, idVersion);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new GarantieRecherche();
                unDisplay.setPLA_ID(rs.getString(1));
                unDisplay.setLIBELLE_REGIME(rs.getString(2));
                unDisplay.setLIBELLE(rs.getString(3));
                unDisplay.setNIVEAU(rs.getString(4));
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getGarantie", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<VersionGarantie> getGarantieVersions(
            String idGarantie) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<VersionGarantie> res = new ArrayList<VersionGarantie>();

        try {
            conn = getConnexion();
            VersionGarantie unDisplay = null;

            // On ne ramène pas les BLOB (trop lourd)
            String requete = "SELECT plv.PLV_ID, plv.PLV_PLA_ID, plv.PLV_DATE_INSERTION, plv.PLV_DATE_EFFET, plv.PLV_DATE_FIN, "
                    + "plv.PLV_NOM_FICHIER, plv.PLV_COMMENTAIRES, plv.PLV_CREATEUR_ID, prs.PRS_NOM || ' ' || prs.PRS_PRENOM   "
                    + "FROM APPLICATION.T_VERSIONS_PLAQUETTES_PLV plv, H_ANNUAIRE.T_UTILISATEURS_UTL utl, H_ANNUAIRE.T_PERSONNES_PRS prs  "
                    + "WHERE plv.PLV_PLA_ID = ? "
                    + "AND plv.PLV_CREATEUR_ID = utl.UTL_ID "
                    + "AND utl.UTL_PRS_ID = prs.PRS_ID "
                    + "ORDER BY plv.PLV_DATE_EFFET DESC, plv.PLV_DATE_FIN DESC ";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idGarantie);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new VersionGarantie();
                unDisplay.setPLV_ID(rs.getString(1));
                unDisplay.setPLV_PLA_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setPLV_DATE_INSERTION(rs.getTimestamp(3));
                unDisplay.setPLV_DATE_EFFET(rs.getTimestamp(4));
                unDisplay.setPLV_DATE_FIN(rs.getTimestamp(5));
                unDisplay.setPLV_NOM_FICHIER((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setPLV_COMMENTAIRES((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setPLV_CREATEUR_ID((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setNOM_PRENOM_CREATEUR((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getGarantieVersions", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static VersionGarantie getGarantieVersion(String idVersion) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        VersionGarantie unDisplay = null;

        try {

            String requete = "SELECT plv.PLV_ID, plv.PLV_PLA_ID, plv.PLV_DATE_INSERTION, plv.PLV_DATE_EFFET, plv.PLV_DATE_FIN, plv.PLV_BLOB, "
                    + "plv.PLV_NOM_FICHIER, plv.PLV_COMMENTAIRES, plv.PLV_CREATEUR_ID, prs.PRS_NOM || ' ' || prs.PRS_PRENOM  "
                    + "FROM APPLICATION.T_VERSIONS_PLAQUETTES_PLV plv, H_ANNUAIRE.T_UTILISATEURS_UTL utl, H_ANNUAIRE.T_PERSONNES_PRS prs "
                    + "WHERE plv.PLV_ID = ? "
                    + "AND plv.PLV_CREATEUR_ID = utl.UTL_ID "
                    + "AND utl.UTL_PRS_ID = prs.PRS_ID ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idVersion);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new VersionGarantie();

                unDisplay.setPLV_ID(rs.getString(1));
                unDisplay.setPLV_PLA_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setPLV_DATE_INSERTION(rs.getTimestamp(3));
                unDisplay.setPLV_DATE_EFFET(rs.getTimestamp(4));
                unDisplay.setPLV_DATE_FIN(rs.getTimestamp(5));
                unDisplay.setPLV_BLOB(rs.getBytes(6));
                unDisplay.setPLV_NOM_FICHIER((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setPLV_COMMENTAIRES((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setPLV_CREATEUR_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay
                        .setNOM_PRENOM_CREATEUR((rs.getString(10) != null) ? rs
                                .getString(10) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getGarantieVersion", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Salarie> getSalariesEntreprise(
            String idContratEtablissement) {

        Collection<Salarie> res = new ArrayList<Salarie>();
        Salarie unDisplay = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requete = "";

        try {

            requete = "SELECT DISTINCT codciv.libelle, pers.nom, pers.prenom, contadh.numcontrat, "
                    + "codqual.libelle, ben.numeross, ben.cless, pers.datenaissance, "
                    + "APPLICATION.PS_GETETATCONTRATBENEF(contben.id), contadh.dateradiation, ben.ID, cga.code, cga.libelle "
                    + "FROM application.codes codciv, application.beneficiaire ben, application.personne pers, "
                    + "application.etablissement etab, application.codes codqual, application.contrat_adherent contadh, "
                    + "application.contrat_beneficiaire contben, application.contrat_groupeassure cga "
                    + "WHERE pers.civilite_code = codciv.code AND ben.qualite_code = codqual.code AND ben.personne_id = pers.ID "
                    + "AND etab.ID = cga.etablissement_id AND cga.ID = contadh.contrat_groupeassure_id AND contadh.ID = contben.contrat_adherent_id "
                    + "AND contben.beneficiaire_id = ben.ID AND CGA.CONTRAT_ETABLISSEMENT_ID = ? ORDER BY 9 DESC, 2 ASC, 5 ASC, 3 ASC";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratEtablissement);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Salarie();
                unDisplay.setCivilite((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setNom((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setPrenom((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setNumeroAdherent((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setQualite((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setNumeroSecu((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setCleSecu((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setDateNaissance((Date) rs.getTimestamp(8));
                unDisplay.setActif((rs.getString(9) != null) ? rs.getString(9)
                        : "");
                unDisplay.setDateRadiation((Date) rs.getTimestamp(10));
                unDisplay.setIdBeneficiaire(rs.getString(11));
                unDisplay.setCodeGroupeAssures((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay
                        .setLibelleGroupeAssures((rs.getString(13) != null) ? rs
                                .getString(13) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getSalariesEntreprise", e);
            return new ArrayList<Salarie>();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Appel creerAppel(String teleacteur_id, String campagne_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Appel appel = null;

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "INSERT INTO HOTLINE.APPEL(ID, DATEAPPEL, CREATEUR_ID, CAMPAGNE_ID, EDITIONENCOURS, EDITEUR_ID, CLOTURE_CODE ) "
                    + " VALUES (HOTLINE.SEQ_ID_APPEL.nextVal, SYSDATE, ?, ?, ?, ?, (select C.CODE from hotline.codes c where c.alias = 'OUVERTE') )";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, campagne_id);
            stmt.setString(3, _VRAI);
            stmt.setString(4, teleacteur_id);
            stmt.executeQuery();

            // Ramener la currVal
            String requete_id_appelant_cree = "SELECT HOTLINE.SEQ_ID_APPEL.currVal FROM DUAL";
            String id_appel_cree = "";
            stmt = conn.prepareStatement(requete_id_appelant_cree);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id_appel_cree = rs.getString(1);
            }
            stmt.clearParameters();

            // Ramener la sysdate
            String requete_date = "SELECT a.DATEAPPEL FROM HOTLINE.APPEL a WHERE a.id = ? ";
            Timestamp date_appel = null;
            stmt = conn.prepareStatement(requete_date);
            stmt.setString(1, id_appel_cree);
            rs = stmt.executeQuery();
            while (rs.next()) {
                date_appel = rs.getTimestamp(1);
            }

            appel = new Appel();
            appel.setID(id_appel_cree);
            appel.setDATEAPPEL(date_appel);
            appel.setCREATEUR_ID(teleacteur_id);
            appel.setCampagne(campagne_id);

            conn.commit();

            return appel;

        } catch (Exception e) {
            LOGGER.error("creerAppel", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String creerAppelant(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            String creation_type_appelant_id = (String) request
                    .getParameter("creation_type_appelant_id");
            String creation_appelant_code_civilite = (String) request
                    .getParameter("creation_appelant_code_civilite");
            if (_EMPTY.equals(creation_appelant_code_civilite)) {
                creation_appelant_code_civilite = "";
            }
            String creation_appelant_nom = (String) request
                    .getParameter("creation_appelant_nom");
            String creation_appelant_prenom = (String) request
                    .getParameter("creation_appelant_prenom");
            String creation_appelant_date_naissance = (String) request
                    .getParameter("creation_appelant_date_naissance");
            String creation_appelant_numero_secu = (String) request
                    .getParameter("creation_appelant_numero_secu");
            String creation_appelant_cle_secu = (String) request
                    .getParameter("creation_appelant_cle_secu");

            String creation_appelant_etablissement_rs = (String) request
                    .getParameter("creation_appelant_etablissement_rs");
            String creation_appelant_numero_finess = (String) request
                    .getParameter("creation_appelant_numero_finess");
            String creation_appelant_numero_adherent = (String) request
                    .getParameter("creation_appelant_numero_adherent");

            String creation_appelant_telephone_fixe = (String) request
                    .getParameter("creation_appelant_telephone_fixe");
            String creation_appelant_telephone_autre = (String) request
                    .getParameter("creation_appelant_telephone_autre");
            String creation_appelant_fax = (String) request
                    .getParameter("creation_appelant_fax");
            String creation_appelant_adresse_mail = (String) request
                    .getParameter("creation_appelant_adresse_mail");

            String creation_appelant_ligne_1 = (String) request
                    .getParameter("creation_appelant_ligne_1");
            String creation_appelant_ligne_2 = (String) request
                    .getParameter("creation_appelant_ligne_2");
            String creation_appelant_ligne_3 = (String) request
                    .getParameter("creation_appelant_ligne_3");
            String creation_appelant_ligne_4 = (String) request
                    .getParameter("creation_appelant_ligne_4");
            String creation_appelant_code_postal = (String) request
                    .getParameter("creation_appelant_code_postal");
            String creation_appelant_localite = (String) request
                    .getParameter("creation_appelant_localite");

            String requete = "INSERT INTO HOTLINE.APPELANT (ID, TYPE_CODE, CIVILITE_CODE, NOM, PRENOM, DATENAISSANCE, NUMEROSS, CLESS, "
                    + "ETABLISSEMENT_RS, NUMFINESS, CODEADHERENT, "
                    + "ADR_TELEPHONEFIXE, ADR_TELEPHONEAUTRE, ADR_TELECOPIE, ADR_COURRIEL,  ADR_LIGNE_1, ADR_LIGNE_2, ADR_LIGNE_3, ADR_LIGNE_4, ADR_CODEPOSTAL, ADR_LOCALITE )"
                    + " VALUES (HOTLINE.SEQ_ID_APPELANT.nextVal, ?, ?, ?, ?, to_date(?, 'DD/MM/YYYY'), ?, ?, "
                    + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, creation_type_appelant_id);
            stmt.setString(2, creation_appelant_code_civilite);
            stmt.setString(3, creation_appelant_nom);
            stmt.setString(4, creation_appelant_prenom);
            stmt.setString(5, creation_appelant_date_naissance);
            stmt.setString(6, creation_appelant_numero_secu);
            stmt.setString(7, creation_appelant_cle_secu);

            stmt.setString(8, creation_appelant_etablissement_rs);
            stmt.setString(9, creation_appelant_numero_finess);
            stmt.setString(10, creation_appelant_numero_adherent);

            stmt.setString(11, creation_appelant_telephone_fixe);
            stmt.setString(12, creation_appelant_telephone_autre);
            stmt.setString(13, creation_appelant_fax);
            stmt.setString(14, creation_appelant_adresse_mail);

            stmt.setString(15, creation_appelant_ligne_1);
            stmt.setString(16, creation_appelant_ligne_2);
            stmt.setString(17, creation_appelant_ligne_3);
            stmt.setString(18, creation_appelant_ligne_4);
            stmt.setString(19, creation_appelant_code_postal);
            stmt.setString(20, creation_appelant_localite);

            stmt.executeQuery();
            conn.commit();

            String requete_id_appelant_creer = "SELECT HOTLINE.SEQ_ID_APPELANT.currVal FROM DUAL";
            String id_appelant_creer = "";
            stmt = conn.prepareStatement(requete_id_appelant_creer);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id_appelant_creer = rs.getString(1);
            }
            stmt.clearParameters();

            stmt.clearParameters();
            return _VRAI + "|" + id_appelant_creer;
        } catch (Exception e) {
            LOGGER.error("creerAppelant", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String creerEtablissementHospitalier(
            HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            String etablissement_raison_sociale = (String) request
                    .getParameter("etablissement_raison_sociale");
            String etablissement_num_finess = (String) request
                    .getParameter("etablissement_num_finess");
            String etablissement_telephone_fixe = (String) request
                    .getParameter("etablissement_telephone_fixe");
            String etablissement_telephone_autre = (String) request
                    .getParameter("etablissement_telephone_autre");
            String etablissement_fax = (String) request
                    .getParameter("etablissement_fax");

            String etablissement_adresse1 = (String) request
                    .getParameter("etablissement_adresse1");
            String etablissement_adresse2 = (String) request
                    .getParameter("etablissement_adresse2");
            String etablissement_adresse3 = (String) request
                    .getParameter("etablissement_adresse3");
            String etablissement_code_postal = (String) request
                    .getParameter("etablissement_code_postal");
            String etablissement_localite = (String) request
                    .getParameter("etablissement_localite");

            String requete = "INSERT INTO HOTLINE.APPELANT (ID, TYPE_CODE, ETABLISSEMENT_RS, NUMFINESS, "
                    + "ADR_TELEPHONEFIXE, ADR_TELEPHONEAUTRE, ADR_TELECOPIE, ADR_LIGNE_1, ADR_LIGNE_2, ADR_LIGNE_3, ADR_CODEPOSTAL, ADR_LOCALITE )"
                    + " VALUES (HOTLINE.SEQ_ID_APPELANT.nextVal, (SELECT c.CODE FROM HOTLINE.CODES c WHERE c.ALIAS = 'PROFSANTE' ), ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ? )";

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, etablissement_raison_sociale);
            stmt.setString(2, etablissement_num_finess);
            stmt.setString(3, etablissement_telephone_fixe);
            stmt.setString(4, etablissement_telephone_autre);
            stmt.setString(5, etablissement_fax);

            stmt.setString(6, etablissement_adresse1);
            stmt.setString(7, etablissement_adresse2);
            stmt.setString(8, etablissement_adresse3);
            stmt.setString(9, etablissement_code_postal);
            stmt.setString(10, etablissement_localite);

            stmt.executeQuery();
            conn.commit();

            // Ramener la currVal
            String requete_id_appelant_creer = "SELECT HOTLINE.SEQ_ID_APPELANT.currVal FROM DUAL";
            String id_appelant_creer = "";
            stmt = conn.prepareStatement(requete_id_appelant_creer);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id_appelant_creer = rs.getString(1);
            }
            stmt.clearParameters();

            stmt.clearParameters();
            return _VRAI + "|" + id_appelant_creer;
        } catch (Exception e) {
            LOGGER.error("creerEtablissementHospitalier", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String modifierAppelant(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            String id_appelant = (String) request.getParameter("id_appelant");

            String modification_type_appelant_id = (String) request
                    .getParameter("modification_type_appelant_id");
            String modification_appelant_code_civilite = (String) request
                    .getParameter("modification_appelant_code_civilite");
            if (_EMPTY.equals(modification_appelant_code_civilite)) {
                modification_appelant_code_civilite = "";
            }
            String modification_appelant_nom = (String) request
                    .getParameter("modification_appelant_nom");
            String modification_appelant_prenom = (String) request
                    .getParameter("modification_appelant_prenom");
            String modification_appelant_date_naissance = (String) request
                    .getParameter("modification_appelant_date_naissance");
            String modification_appelant_numero_secu = (String) request
                    .getParameter("modification_appelant_numero_secu");
            String modification_appelant_cle_secu = (String) request
                    .getParameter("modification_appelant_cle_secu");

            String modification_appelant_etablissement_rs = (String) request
                    .getParameter("modification_appelant_etablissement_rs");
            String modification_appelant_numero_finess = (String) request
                    .getParameter("modification_appelant_numero_finess");
            String modification_appelant_numero_adherent = (String) request
                    .getParameter("modification_appelant_numero_adherent");

            String modification_appelant_telephone_fixe = (String) request
                    .getParameter("modification_appelant_telephone_fixe");
            String modification_appelant_telephone_autre = (String) request
                    .getParameter("modification_appelant_telephone_autre");
            String modification_appelant_fax = (String) request
                    .getParameter("modification_appelant_fax");
            String modification_appelant_adresse_mail = (String) request
                    .getParameter("modification_appelant_adresse_mail");

            String modification_appelant_ligne_1 = (String) request
                    .getParameter("modification_appelant_ligne_1");
            String modification_appelant_ligne_2 = (String) request
                    .getParameter("modification_appelant_ligne_2");
            String modification_appelant_ligne_3 = (String) request
                    .getParameter("modification_appelant_ligne_3");
            String modification_appelant_ligne_4 = (String) request
                    .getParameter("modification_appelant_ligne_4");
            String modification_appelant_code_postal = (String) request
                    .getParameter("modification_appelant_code_postal");
            String modification_appelant_localite = (String) request
                    .getParameter("modification_appelant_localite");

            String requete = "UPDATE HOTLINE.APPELANT SET TYPE_CODE = ?, CIVILITE_CODE = ?, NOM = ?, PRENOM = ?, "
                    + "DATENAISSANCE = to_date(?, 'DD/MM/YYYY'), NUMEROSS = ?, CLESS = ?, "
                    + "ETABLISSEMENT_RS = ?, NUMFINESS = ?, CODEADHERENT = ?, "
                    + "ADR_TELEPHONEFIXE = ?, ADR_TELEPHONEAUTRE = ?, ADR_TELECOPIE = ?, ADR_COURRIEL = ?,  "
                    + "ADR_LIGNE_1 = ?, ADR_LIGNE_2 = ?, ADR_LIGNE_3 = ?, ADR_LIGNE_4 = ?, ADR_CODEPOSTAL = ?, ADR_LOCALITE = ? "
                    + "WHERE ID = ? ";

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, modification_type_appelant_id);
            stmt.setString(2, modification_appelant_code_civilite);
            stmt.setString(3, modification_appelant_nom);
            stmt.setString(4, modification_appelant_prenom);

            stmt.setString(5, modification_appelant_date_naissance);
            stmt.setString(6, modification_appelant_numero_secu);
            stmt.setString(7, modification_appelant_cle_secu);

            stmt.setString(8, modification_appelant_etablissement_rs);
            stmt.setString(9, modification_appelant_numero_finess);
            stmt.setString(10, modification_appelant_numero_adherent);

            stmt.setString(11, modification_appelant_telephone_fixe);
            stmt.setString(12, modification_appelant_telephone_autre);
            stmt.setString(13, modification_appelant_fax);
            stmt.setString(14, modification_appelant_adresse_mail);

            stmt.setString(15, modification_appelant_ligne_1);
            stmt.setString(16, modification_appelant_ligne_2);
            stmt.setString(17, modification_appelant_ligne_3);
            stmt.setString(18, modification_appelant_ligne_4);
            stmt.setString(19, modification_appelant_code_postal);
            stmt.setString(20, modification_appelant_localite);

            stmt.setString(21, id_appelant);

            stmt.executeQuery();
            conn.commit();

            stmt.clearParameters();
            return _VRAI + "|" + id_appelant;
        } catch (Exception e) {
            LOGGER.error("modifierAppelant", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String modifierEtablissementHospitalier(
            HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String id_etablissement_hospitalier = (String) request
                    .getParameter("id_etablissement_hospitalier");
            String etablissement_raison_sociale = (String) request
                    .getParameter("etablissement_raison_sociale");
            String etablissement_num_finess = (String) request
                    .getParameter("etablissement_num_finess");
            String etablissement_telephone_fixe = (String) request
                    .getParameter("etablissement_telephone_fixe");
            String etablissement_telephone_autre = (String) request
                    .getParameter("etablissement_telephone_autre");
            String etablissement_fax = (String) request
                    .getParameter("etablissement_fax");

            String etablissement_adresse1 = (String) request
                    .getParameter("etablissement_adresse1");
            String etablissement_adresse2 = (String) request
                    .getParameter("etablissement_adresse2");
            String etablissement_adresse3 = (String) request
                    .getParameter("etablissement_adresse3");
            String etablissement_code_postal = (String) request
                    .getParameter("etablissement_code_postal");
            String etablissement_localite = (String) request
                    .getParameter("etablissement_localite");

            String requete = "UPDATE HOTLINE.APPELANT SET ETABLISSEMENT_RS = ?, NUMFINESS = ?,  "
                    + "ADR_TELEPHONEFIXE = ?, ADR_TELEPHONEAUTRE = ?, ADR_TELECOPIE = ?,  "
                    + "ADR_LIGNE_1 = ?, ADR_LIGNE_2 = ?, ADR_LIGNE_3 = ?, ADR_CODEPOSTAL = ?, ADR_LOCALITE = ? "
                    + "WHERE ID = ? ";

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, etablissement_raison_sociale);
            stmt.setString(2, etablissement_num_finess);
            stmt.setString(3, etablissement_telephone_fixe);
            stmt.setString(4, etablissement_telephone_autre);
            stmt.setString(5, etablissement_fax);

            stmt.setString(6, etablissement_adresse1);
            stmt.setString(7, etablissement_adresse2);
            stmt.setString(8, etablissement_adresse3);
            stmt.setString(9, etablissement_code_postal);
            stmt.setString(10, etablissement_localite);

            stmt.setString(11, id_etablissement_hospitalier);

            stmt.executeQuery();
            conn.commit();

            stmt.clearParameters();
            return _VRAI;
        } catch (Exception e) {
            LOGGER.error("modifierEtablissementHospitalier", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String modifierMessage(HttpServletRequest request) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String id_message = (String) request.getParameter("id_message");

            String titre = (String) request.getParameter("titre");
            String contenu = (String) request.getParameter("contenu");
            String dateDebut = (String) request.getParameter("dateDebut");
            String dateFin = (String) request.getParameter("dateFin");

            String campagne_id = (String) request.getParameter("campagne_id");

            String requete = "UPDATE HOTLINE.MESSAGE SET TITRE = ?, CONTENU = ?, "
                    + "DATEDEBUT = to_date(?, 'DD/MM/YYYY'), DATEFIN = to_date(?, 'DD/MM/YYYY'), CAMPAGNE_ID = ?  "
                    + "WHERE ID = ? ";

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, titre);
            stmt.setString(2, contenu);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setString(5, campagne_id);
            stmt.setString(6, id_message);

            stmt.executeQuery();
            conn.commit();

            stmt.clearParameters();
            return _VRAI + "|" + "";
        } catch (Exception e) {
            LOGGER.error("modifierMessage", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierTransfert(String transfert_id,
            String libelle, String email) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE HOTLINE.T_TRANSFERTS_TRA t set t.TRA_LIBELLE = ?, t.TRA_EMAIL = ? "
                    + "WHERE t.TRA_ID = ?";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, libelle);
            stmt.setString(2, email);
            stmt.setString(3, transfert_id);

            stmt.executeQuery();
            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("modifierTransfert", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String modifierTeleActeur(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String teleacteur_id = (String) request
                    .getParameter("teleacteur_id");
            String utl_id = (String) request.getParameter("utl_id");
            String IDHermes = (String) request.getParameter("IDHermes");
            String onglets_fiches = (String) request
                    .getParameter("onglets_fiches");
            String moduleAdministration = (String) request
                    .getParameter("moduleAdministration");
            String moduleStatistiques = (String) request
                    .getParameter("moduleStatistiques");
            String excluTransfertsPossibles = (String) request
                    .getParameter("excluTransfertsPossibles");

            String requete = "UPDATE HOTLINE.TELEACTEUR t SET t.IDHERMES = ?, t.ONGLETSFICHES= ?, t.EXCLU_MESSAGE_CONFIDENTIALITE = ? "
                    + "WHERE t.ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, IDHermes);
            stmt.setString(2, onglets_fiches);
            stmt.setString(3, excluTransfertsPossibles);
            stmt.setString(4, teleacteur_id);
            stmt.executeQuery();

            stmt.clearParameters();
            requete = "SELECT count(*) from HOTLINE.T_HCONTACTS_HABILITATIONS_HCH hch "
                    + "WHERE hch.HCH_UTL_ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, utl_id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                stmt.clearParameters();
                int count = rs.getInt(1);
                if (count > 0) {
                    requete = "UPDATE HOTLINE.T_HCONTACTS_HABILITATIONS_HCH hch  SET hch.HCH_ADMINISTRATION = ?, hch.HCH_STATISTIQUES = ? "
                            + "WHERE hch.HCH_UTL_ID = ?";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, moduleAdministration);
                    stmt.setString(2, moduleStatistiques);
                    stmt.setString(3, utl_id);
                } else {
                    requete = "INSERT INTO HOTLINE.T_HCONTACTS_HABILITATIONS_HCH (HCH_UTL_ID, HCH_ADMINISTRATION, HCH_STATISTIQUES) "
                            + "VALUES (?, ?, ?)";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, utl_id);
                    stmt.setString(2, moduleAdministration);
                    stmt.setString(3, moduleStatistiques);
                }
                stmt.executeQuery();
            }

            conn.commit();

            stmt.clearParameters();
            return _VRAI;
        } catch (Exception e) {
            LOGGER.error("modifierTeleActeur", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeRsStmtConn( rs, stmt, conn);
        }
    }

    public static StringBuilder getAppelantForModification(String id_appelant) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder res = new StringBuilder();

        try {
            conn = getConnexion();

            String requete = "SELECT app.ID, app.TYPE_CODE, civ.LIBELLE, app.NOM, app.PRENOM, to_char(APP.DATENAISSANCE, 'DD/MM/YYYY'), app.NUMEROSS, app.CLESS,"
                    + "app.ETABLISSEMENT_RS, app.NUMFINESS, app.CODEADHERENT, "
                    + "app.ADR_TELEPHONEFIXE, app.ADR_TELEPHONEAUTRE, app.ADR_TELECOPIE, app.ADR_COURRIEL, app.ADR_LIGNE_1, app.ADR_LIGNE_2, app.ADR_LIGNE_3, app.ADR_LIGNE_4, app.ADR_CODEPOSTAL, app.ADR_LOCALITE "
                    + "FROM HOTLINE.APPELANT app, HOTLINE.CODES civ "
                    + "WHERE app.ID = ? AND APP.CIVILITE_CODE = civ.CODE(+)";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, id_appelant);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res.append("<ID>" + rs.getString(1) + "</ID>");
                res.append("<TYPE_CODE>"
                        + ((rs.getString(2) != null) ? rs.getString(2) : "")
                        + "</TYPE_CODE>");
                res.append("<CIVILITE_LIBELLE>"
                        + ((rs.getString(3) != null) ? rs.getString(3) : "")
                        + "</CIVILITE_LIBELLE>");
                res.append("<NOM>"
                        + ((rs.getString(4) != null) ? rs.getString(4) : "")
                        + "</NOM>");
                res.append("<PRENOM>"
                        + ((rs.getString(5) != null) ? rs.getString(5) : "")
                        + "</PRENOM>");
                res.append("<DATENAISSANCE>"
                        + ((rs.getString(6) != null) ? rs.getString(6) : "")
                        + "</DATENAISSANCE>");
                res.append("<NUMEROSS>"
                        + ((rs.getString(7) != null) ? rs.getString(7) : "")
                        + "</NUMEROSS>");
                res.append("<CLESS>"
                        + ((rs.getString(8) != null) ? rs.getString(8) : "")
                        + "</CLESS>");

                res.append("<ETABLISSEMENT_RS>"
                        + ((rs.getString(9) != null) ? rs.getString(9) : "")
                        + "</ETABLISSEMENT_RS>");
                res.append("<NUMFINESS>"
                        + ((rs.getString(10) != null) ? rs.getString(10) : "")
                        + "</NUMFINESS>");
                res.append("<CODEADHERENT>"
                        + ((rs.getString(11) != null) ? rs.getString(11) : "")
                        + "</CODEADHERENT>");

                res.append("<ADR_TELEPHONEFIXE>"
                        + ((rs.getString(12) != null) ? rs.getString(12) : "")
                        + "</ADR_TELEPHONEFIXE>");
                res.append("<ADR_TELEPHONEAUTRE>"
                        + ((rs.getString(13) != null) ? rs.getString(13) : "")
                        + "</ADR_TELEPHONEAUTRE>");
                res.append("<ADR_TELECOPIE>"
                        + ((rs.getString(14) != null) ? rs.getString(14) : "")
                        + "</ADR_TELECOPIE>");
                res.append("<ADR_COURRIEL>"
                        + ((rs.getString(15) != null) ? rs.getString(15) : "")
                        + "</ADR_COURRIEL>");

                res.append("<ADR_LIGNE_1>"
                        + ((rs.getString(16) != null) ? rs.getString(16) : "")
                        + "</ADR_LIGNE_1>");
                res.append("<ADR_LIGNE_2>"
                        + ((rs.getString(17) != null) ? rs.getString(17) : "")
                        + "</ADR_LIGNE_2>");
                res.append("<ADR_LIGNE_3>"
                        + ((rs.getString(18) != null) ? rs.getString(18) : "")
                        + "</ADR_LIGNE_3>");
                res.append("<ADR_LIGNE_4>"
                        + ((rs.getString(19) != null) ? rs.getString(19) : "")
                        + "</ADR_LIGNE_4>");
                res.append("<ADR_CODEPOSTAL>"
                        + ((rs.getString(20) != null) ? rs.getString(20) : "")
                        + "</ADR_CODEPOSTAL>");
                res.append("<ADR_LOCALITE>"
                        + ((rs.getString(21) != null) ? rs.getString(21) : "")
                        + "</ADR_LOCALITE>");

            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getAppelantForModification", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getMessageForModification(String id_message) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder res = new StringBuilder();

        try {
            conn = getConnexion();

            String requete = "SELECT c.ID, m.titre, m.contenu, m.datedebut, m.datefin, m.campagne_id "
                    + "FROM hotline.MESSAGE m, hotline.campagne c where c.id = m.CAMPAGNE_ID and m.ID = ?";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, id_message);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res.append("<ID_CAMPAGNE>" + rs.getString(1) + "</ID_CAMPAGNE>");
                res.append("<TITRE>"
                        + ((rs.getString(2) != null) ? rs.getString(2) : "")
                        + "</TITRE>");
                res.append("<CONTENU>"
                        + ((rs.getString(3) != null) ? rs.getString(3) : "")
                        + "</CONTENU>");
                res.append("<DATEDEBUT>"
                        + ((rs.getString(4) != null) ? UtilDate
                                .formatDDMMYYYY(rs.getTimestamp(4)) : "")
                        + "</DATEDEBUT>");
                res.append("<DATEFIN>"
                        + ((rs.getString(5) != null) ? UtilDate
                                .formatDDMMYYYY(rs.getTimestamp(5)) : "")
                        + "</DATEFIN>");

            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getMessageForModification", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static Collection<AbonnementService> findAbonnementAdherent( String idAdherent ){
        

        Connection conn = null;
        PreparedStatement stmt = null;      
        ResultSet rs = null;
        Collection<AbonnementService> res = new ArrayList<AbonnementService>();     
        try{
            conn = SQLDataService.getConnexion();
            
            AbonnementService unDisplay = null;    
        
            String requeteAbonnements ="select S.SRV_LIBELLE,TO_CHAR(A.SAB_DTE_DEBUT_EFFET,'dd/mm/yyyy') DEBUT,TO_CHAR(A.SAB_DTE_FIN_EFFET,'dd/mm/yyyy') FIN,DECODE(A.SAB_ACTIF,1,'Oui','Non') Actif "+
                                       "from application.t_services_srv s, "+
                                       "     application.t_servicesabonnement_sab a "+
                                       "where A.SAB_SRV_ID = S.SRV_ID " +
                                       "     and A.SAB_BENEF_ID = ?";
            
            stmt = conn.prepareStatement(requeteAbonnements);
            stmt.setString(1 , idAdherent);
           
            rs = stmt.executeQuery();
                          
            while(rs.next()){
                unDisplay = new AbonnementService();
                unDisplay.setLibelle( rs.getString(1) );
                unDisplay.setDebut( rs.getString(2) );
                unDisplay.setFin( rs.getString(3) );
                unDisplay.setActif( rs.getString(4) );
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;               
            return res;
          
            } catch (Exception e) {
                LOGGER.error("findHistoriqueAdherent", e);
                res.clear();
                return res;
            } finally {
                closeRsStmtConn(rs,stmt,conn);
            }
        }
    
    public static Appelant getAppelantById(String idAppelant) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Appelant unDisplay = null;
            String requete = "SELECT app.ID, app.NOM, app.PRENOM, app.CIVILITE_CODE, app.TYPE_CODE, app.ADR_LIGNE_1, app.ADR_LIGNE_2, "
                    + "app.ADR_LIGNE_3, app.ADR_LIGNE_4, "
                    + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE, app.ADR_PAYS, app.ADR_TELEPHONEFIXE, app.ADR_TELEPHONEAUTRE, app.ADR_TELECOPIE, app.ADR_COURRIEL,"
                    + "app.VIP, app.NPAI, app.CODEADHERENT, app.NUMEROSS, app.CLESS, TRUNC(app.DATENAISSANCE), app.ETABLISSEMENT_RS, app.NUMFINESS, civ.LIBELLE, typ.LIBELLE  "
                    + "FROM HOTLINE.APPELANT app, HOTLINE.CODES civ, HOTLINE.CODES typ "
                    + "WHERE app.ID = ? and app.type_code = typ.CODE(+) and app.civilite_code = civ.code(+)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idAppelant);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Appelant();
                unDisplay.setID(rs.getString(1));
                unDisplay.setNOM((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setPRENOM((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                unDisplay.setCIVILITE_CODE((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setTYPE_CODE((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setADR_LIGNE_1((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setADR_LIGNE_2((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setADR_LIGNE_3((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setADR_LIGNE_4((rs.getString(9) != null) ? rs
                        .getString(9) : "");

                unDisplay.setADR_CODEPOSTAL((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setADR_LOCALITE((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setADR_PAYS((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setADR_TELEPHONEFIXE((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setADR_TELEPHONEAUTRE((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setADR_TELECOPIE((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setADR_COURRIEL((rs.getString(16) != null) ? rs
                        .getString(16) : "");

                unDisplay.setVIP((rs.getString(17) != null) ? rs.getString(17)
                        : "");
                unDisplay.setNPAI((rs.getString(18) != null) ? rs.getString(18)
                        : "");
                unDisplay.setCODEADHERENT((rs.getString(19) != null) ? rs
                        .getString(19) : "");
                unDisplay.setNUMEROSS((rs.getString(20) != null) ? rs
                        .getString(20) : "");
                unDisplay.setCLESS((rs.getString(21) != null) ? rs
                        .getString(21) : "");
                unDisplay.setDATENAISSANCE((rs.getTimestamp(22) != null) ? rs
                        .getTimestamp(22) : null);
                unDisplay.setETABLISSEMENT_RS((rs.getString(23) != null) ? rs
                        .getString(23) : "");
                unDisplay.setNUMFINESS((rs.getString(24) != null) ? rs
                        .getString(24) : "");
                unDisplay.setCivilite((rs.getString(25) != null) ? rs
                        .getString(25) : "");
                unDisplay.setType((rs.getString(26) != null) ? rs.getString(26)
                        : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getAppelantById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String modifierMotDePasse(String utl_id,
            String mot_de_passe_actuel, String nouveau_mot_de_passe) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "SELECT COUNT(*) FROM H_ANNUAIRE.T_UTILISATEURS_UTL utl WHERE utl.UTL_ID = ? and utl.UTL_PASSWORD = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, utl_id);
            stmt.setString(2, mot_de_passe_actuel);

            rs = stmt.executeQuery();

            rs.next();
            int nbr_lignes = rs.getInt(1);
            stmt.clearParameters();
            if (nbr_lignes == 0) {
                res = "0|Le mot de passe actuel saisi n'est correct. Attention aux majuscules...";
            } else {
                // J'essaie de changer le mot de passe
                requete = "UPDATE H_ANNUAIRE.T_UTILISATEURS_UTL utl set utl.UTL_PASSWORD = ? WHERE utl.UTL_ID = ?";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, nouveau_mot_de_passe);
                stmt.setString(2, utl_id);
                rs = stmt.executeQuery();
                conn.commit();
                res = _VRAI;
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("modifierMotDePasse", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String mettreEnAttenteAcquittement(String appel_id,
            String teleacteur_id, String sous_statut_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String res = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.CLOTURE_CODE = (select c.CODE from HOTLINE.CODES c WHERE c.ALIAS = 'ACQUITTEMENT'),  a.SOUS_STATUT = ?, a.DATEMODIFICATION = SYSDATE, a.MODIFICATEUR_ID = ? "
                    + "WHERE a.ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, sous_statut_id);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, appel_id);
            stmt.executeQuery();
            conn.commit();
            res = _VRAI;

            return res;
        } catch (Exception e) {
            LOGGER.error("mettreEnAttenteAcquittement", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String modifierCommentairesFicheAppel(String appel_id,
            String teleacteur_id, String commentaires) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String res = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.COMMENTAIRE = ?,  a.DATEMODIFICATION = SYSDATE, a.MODIFICATEUR_ID = ? "
                    + "WHERE a.ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, commentaires);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, appel_id);
            stmt.executeQuery();
            conn.commit();
            res = _VRAI;

            return res;
        } catch (Exception e) {
            LOGGER.error("modifierCommentairesFicheAppel", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static String mettreStatutFicheAppelACloture(String appel_id,
            String teleacteur_id, String commentaires) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String res = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.COMMENTAIRE = ?, a.CLOTURE_CODE = (select clo_cod.CODE FROM HOTLINE.CODES clo_cod WHERE clo_cod.ALIAS = 'CLOTURE'),"
                    + "a.DATEMODIFICATION = SYSDATE, a.DATECLOTURE = SYSDATE, a.CLOTUREUR_ID = ?, a.MODIFICATEUR_ID = ? "
                    + "WHERE a.ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, commentaires);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, teleacteur_id);
            stmt.setString(4, appel_id);
            stmt.executeQuery();
            conn.commit();
            res = _VRAI;

            return res;
        } catch (Exception e) {
            LOGGER.error("mettreStatutFicheAppelACloture", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder rafraichirZoneAppel(String idAppel, String zone) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder res = new StringBuilder("");

        try {
            conn = getConnexion();

            String requete = "";

            if ("MODIFICATEUR".equals(zone)) {
                requete = "SELECT a.DATEMODIFICATION, t.PRENOM || ' ' || t.NOM "
                        + "FROM HOTLINE.APPEL a, HOTLINE.TELEACTEUR t "
                        + "WHERE a.MODIFICATEUR_ID = t.ID AND a.ID = ?";
            }

            else if ("CLOTUREUR".equals(zone)) {
                requete = "SELECT a.DATECLOTURE, t.PRENOM || ' ' || t.NOM "
                        + "FROM HOTLINE.APPEL a, HOTLINE.TELEACTEUR t "
                        + "WHERE a.CLOTUREUR_ID = t.ID AND a.ID = ?";
            }

            else if ("STATUT".equals(zone)) {
                requete = "SELECT cod_clot.LIBELLE, cod_clot.ALIAS, ss.LIBELLE "
                        + "FROM HOTLINE.APPEL a, HOTLINE.CODES cod_clot, EVENEMENT.SOUS_STATUT ss "
                        + "WHERE a.CLOTURE_CODE = cod_clot.CODE "
                        + "AND a.SOUS_STATUT = ss.ID(+) " + "AND a.ID = ? ";
            }

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idAppel);
            rs = stmt.executeQuery();

            rs.next();

            res.append(zone + "|");

            if ("MODIFICATEUR".equals(zone)) {
                res.append("<div id=\"id_modification_fa\">");
                res.append(UtilDate.fmtDDMMYYYYHHMMSS(rs.getTimestamp(1))
                        + " par "
                        + ((rs.getString(2) != null) ? rs.getString(2) : ""));
                res.append("</div>");
            }

            else if ("CLOTUREUR".equals(zone)) {
                res.append("<div id=\"id_cloture_fa\">");
                res.append(UtilDate.fmtDDMMYYYYHHMMSS(rs.getTimestamp(1))
                        + " par "
                        + ((rs.getString(2) != null) ? rs.getString(2) : ""));
                res.append("</div>");
            }

            else if ("STATUT".equals(zone)) {
                res.append("<div id=\"id_statut_fa\">");
                res.append((rs.getString(1) != null) ? rs.getString(1) : "");
                if (("ACQUITTEMENT".equalsIgnoreCase(rs.getString(2)))
                        && rs.getString(3) != null) {
                    res.append(" (" + rs.getString(3) + ")");
                }
                res.append("</div>");
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("rafraichirZoneAppel", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String activerDesactiverTeleActeur(String utl_id,
            String choix_action) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String res = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);
            String requete = "";
            if ("D".equals(choix_action)) {
                requete = "UPDATE H_ANNUAIRE.T_PERSONNES_PRS prs SET prs.PRS_ACTIF = 0 WHERE prs.PRS_ID = (SELECT p.PRS_ID FROM H_ANNUAIRE.T_PERSONNES_PRS p, H_ANNUAIRE.T_UTILISATEURS_UTL utl WHERE utl.UTL_PRS_ID = p.PRS_ID AND utl.UTL_ID = ?)";
            } else {
                requete = "UPDATE H_ANNUAIRE.T_PERSONNES_PRS prs SET prs.PRS_ACTIF = 1 WHERE prs.PRS_ID = (SELECT p.PRS_ID FROM H_ANNUAIRE.T_PERSONNES_PRS p, H_ANNUAIRE.T_UTILISATEURS_UTL utl WHERE utl.UTL_PRS_ID = p.PRS_ID AND utl.UTL_ID = ?)";
            }
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, utl_id);
            stmt.executeQuery();
            conn.commit();
            res = _VRAI;

            return res;
        } catch (Exception e) {
            LOGGER.error("activerDesactiverTeleActeur", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return "0|" + e.getMessage();
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static Appel getAppelById(String idAppel) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Appel unDisplay = null;
            String requete = "SELECT a.ID, a.MODIFICATEUR_ID, a.CAMPAGNE_ID, a.MUTUELLE_ID, a.DATEAPPEL, a.DATECLOTURE, "
                    + "a.MOTIF_ID, a.S_MOTIF_ID, a.POINT_ID, a.S_POINT_ID, a.REGIME_CODE, a.TRAITEMENTURGENT, a.RECLAMATION, a.COMMENTAIRE, "
                    + "a.SATISFACTION_CODE, a.CLOTURE_CODE, a.NUMERORAPPEL, a.DATERAPPEL, a.PERIODERAPPEL_CODE, a.CODEAPPELANT_SELECTIONNE,  "
                    + "a.BENEFICIAIRE_ID, a.ADHERENT_ID, a.ETABLISSEMENT_ID, a.APPELANT_ID, a.ENTITEGESTION_ID, a.EDITIONENCOURS,  "
                    + "a.EDITEUR_ID, a.DATEEDITION, a.CLOTUREUR_ID, a.DATEMODIFICATION, a.PRIORITE, a.SOUS_STATUT,  "
                    + "cod_satisfaction.LIBELLE, cod_statut.LIBELLE, cod_periode_rappel.LIBELLE, "
                    + "c.LIBELLE, mut.LIBELLE, eg.LIBELLE, moa.LIBELLE, smoa.LIBELLE, p.LIBELLE, sp.LIBELLE, "
                    + "code_appelant.LIBELLE, code_appelant.ALIAS, cod_statut.ALIAS,  a.CREATEUR_ID, cod_satisfaction.ALIAS, "
                    + "sous_statut.LIBELLE, a.NOMDOCUMENTGENERE, a.MODELE_EDITION_ID, a.TRANSFERTS, a.RESOLU "
                    + "FROM HOTLINE.APPEL a, HOTLINE.CODES cod_satisfaction, HOTLINE.CODES code_appelant,  HOTLINE.CODES cod_statut, HOTLINE.CODES cod_periode_rappel,  "
                    + "HOTLINE.CAMPAGNE c, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa,  "
                    + "HOTLINE.POINT p, HOTLINE.SPOINT sp, EVENEMENT.SOUS_STATUT sous_statut  "
                    + "WHERE a.SATISFACTION_CODE = cod_satisfaction.CODE(+) AND a.PERIODERAPPEL_CODE = cod_periode_rappel.CODE(+)  "
                    + "AND a.CODEAPPELANT_SELECTIONNE = code_appelant.CODE(+) "
                    + "AND a.CLOTURE_CODE = cod_statut.CODE(+) AND a.CAMPAGNE_ID = c.ID(+) AND a.MUTUELLE_ID = mut.ID(+) AND a.MOTIF_ID = moa.ID(+) "
                    + "AND a.S_MOTIF_ID = smoa.ID(+) and a.POINT_ID = p.ID(+) AND a.S_POINT_ID = sp.ID(+) "
                    + "AND a.campagne_id = c.ID(+) AND a.MUTUELLE_ID = mut.ID(+) AND a.ENTITEGESTION_ID = eg.ID(+)"
                    + "AND a.SOUS_STATUT = sous_statut.ID(+) AND a.ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idAppel);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Appel();

                unDisplay.setID(rs.getString(1));
                unDisplay.setMODIFICATEUR_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCAMPAGNE_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMUTUELLE_ID((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setDATEAPPEL((rs.getTimestamp(5) != null) ? rs
                        .getTimestamp(5) : null);
                unDisplay.setDATECLOTURE((rs.getTimestamp(6) != null) ? rs
                        .getTimestamp(6) : null);

                unDisplay.setMOTIF_ID((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setS_MOTIF_ID((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setPOINT_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setS_POINT_ID((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setREGIME_CODE((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setTRAITEMENTURGENT((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setRECLAMATION((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCOMMENTAIRE((rs.getString(14) != null) ? rs
                        .getString(14) : "");

                unDisplay.setSATISFACTION_CODE((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setCLOTURE_CODE((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                unDisplay.setNUMERORAPPEL((rs.getString(17) != null) ? rs
                        .getString(17) : "");
                unDisplay.setDATERAPPEL((rs.getTimestamp(18) != null) ? rs
                        .getTimestamp(18) : null);
                unDisplay.setPERIODERAPPEL_CODE((rs.getString(19) != null) ? rs
                        .getString(19) : "");
                unDisplay
                        .setCODEAPPELANT_SELECTIONNE((rs.getString(20) != null) ? rs
                                .getString(20) : "");

                unDisplay.setBENEFICIAIRE_ID((rs.getString(21) != null) ? rs
                        .getString(21) : "");
                unDisplay.setADHERENT_ID((rs.getString(22) != null) ? rs
                        .getString(22) : null);
                unDisplay.setETABLISSEMENT_ID((rs.getString(23) != null) ? rs
                        .getString(23) : null);
                unDisplay.setAPPELANT_ID((rs.getString(24) != null) ? rs
                        .getString(24) : null);
                unDisplay.setENTITEGESTION_ID((rs.getString(25) != null) ? rs
                        .getString(25) : null);
                unDisplay.setEDITIONENCOURS((rs.getString(26) != null) ? rs
                        .getString(26) : null);

                unDisplay.setEDITEUR_ID((rs.getString(27) != null) ? rs
                        .getString(27) : "");
                unDisplay.setDATEEDITION((rs.getTimestamp(28) != null) ? rs
                        .getTimestamp(28) : null);
                unDisplay.setCLOTUREUR_ID((rs.getString(29) != null) ? rs
                        .getString(29) : null);
                unDisplay
                        .setDATEMODIFICATION((rs.getTimestamp(30) != null) ? rs
                                .getTimestamp(30) : null);
                unDisplay.setPRIORITE((rs.getString(31) != null) ? rs
                        .getString(31) : null);
                unDisplay.setSOUS_STATUT((rs.getString(32) != null) ? rs
                        .getString(32) : null);

                unDisplay.setSatisfaction((rs.getString(33) != null) ? rs
                        .getString(33) : "");
                unDisplay.setStatut((rs.getString(34) != null) ? rs
                        .getString(34) : "");
                unDisplay.setPeriodeRappel((rs.getString(35) != null) ? rs
                        .getString(35) : "");

                unDisplay.setCampagne((rs.getString(36) != null) ? rs
                        .getString(36) : "");
                unDisplay.setMutuelle((rs.getString(37) != null) ? rs
                        .getString(37) : "");
                unDisplay.setEntiteGestion((rs.getString(38) != null) ? rs
                        .getString(38) : "");
                unDisplay.setMotif((rs.getString(39) != null) ? rs
                        .getString(39) : "");
                unDisplay.setSousMotif((rs.getString(40) != null) ? rs
                        .getString(40) : "");
                unDisplay.setPoint((rs.getString(41) != null) ? rs
                        .getString(41) : "");
                unDisplay.setSousPoint((rs.getString(42) != null) ? rs
                        .getString(42) : "");

                unDisplay.setTypeAppelant((rs.getString(43) != null) ? rs
                        .getString(43) : "");
                unDisplay.setAliasTypeAppelant((rs.getString(44) != null) ? rs
                        .getString(44) : "");
                unDisplay.setAliasStatut((rs.getString(45) != null) ? rs
                        .getString(45) : "");
                unDisplay.setCREATEUR_ID((rs.getString(46) != null) ? rs
                        .getString(46) : "");
                unDisplay.setAliasSatisfaction((rs.getString(47) != null) ? rs
                        .getString(47) : "");
                unDisplay.setLibelleSousStatut((rs.getString(48) != null) ? rs
                        .getString(48) : "");
                unDisplay.setNOMDOCUMENTGENERE((rs.getString(49) != null) ? rs
                        .getString(49) : "");
                unDisplay.setMODELE_EDITION_ID((rs.getString(50) != null) ? rs
                        .getString(50) : "");
                unDisplay.setTRANSFERTS((rs.getString(51) != null) ? rs
                        .getString(51) : "");
                unDisplay.setResolu((rs.getString(52) != null) ? rs
                        .getString(51) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getAppelById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection rechercheFichesAppels(Map<String, String> map) {

        // HABILITATIONS :
        // ASSURES ET ENTREPRISES : sur les EG
        // AUTRES APPELANTS : sur les mutuelles

        Connection conn = null;
        PreparedStatement stmt = null, stmt_count = null;
        ResultSet rs = null;
        Collection res = new ArrayList();
        Appel unDisplay = null;

        String comparateur_fiche_id = " = ";
        String comparateur_mot_cle = " = ";
        String comparateur_motif = " = ";
        String comparateur_sous_motif = " = ";
        String comparateur_point = " = ";
        String comparateur_sous_point = " = ";

        String fiche_id = (String) map.get("fiche_id");
        String mot_cle = (String) map.get("mot_cle");
        String reference_id = (String) map.get("reference_id");
        String campagne_id = (String) map.get("campagne_id");
        String type_appelant = (String) map.get("type_appelant");
        String createur_id = (String) map.get("createur_id");
        String statut_id = (String) map.get("statut_id");
        String reclamation = (String) map.get("reclamation");
        String satisfaction_id = (String) map.get("satisfaction_id");
        String date_debut = (String) map.get("date_debut");
        String date_fin = (String) map.get("date_fin");
        String motif = (String) map.get("motif");
        String sous_motif = (String) map.get("sous_motif");
        String point = (String) map.get("point");
        String sous_point = (String) map.get("sous_point");
        String teleacteur_id = (String) map.get("teleacteur_id");

        String filtre_campagne = " and a.CAMPAGNE_ID = ?  ";
        String filtre_reference = " and smoa.REFERENCE_ID = ? ";
        String filtre_createur = " and a.CREATEUR_ID = ? ";
        String filtre_satisfaction = " and a.SATISFACTION_CODE = ? ";
        String filtre_statut = " and cod_cloture.CODE = ? ";
        String filtre_type_appelant = " and a.CODEAPPELANT_SELECTIONNE = ? ";
        String filtre_date_debut = " and trunc(a.DATEAPPEL) >= to_date(?,'dd/mm/YYYY')  ";
        String filtre_date_fin = " and trunc(a.DATEAPPEL) <= to_date(?,'dd/mm/YYYY') ";
        String filtre_reclamation = " and a.RECLAMATION = ? ";

        int compteur_bind = 1;
        try {
            conn = getConnexion();

            if (fiche_id.indexOf('%') != -1) {
                comparateur_fiche_id = " like ";
            }

            if (mot_cle.indexOf('%') != -1) {
                comparateur_mot_cle = " like ";
            }

            if (motif.indexOf('%') != -1) {
                comparateur_motif = " like ";
            }

            if (sous_motif.indexOf('%') != -1) {
                comparateur_sous_motif = " like ";
            }

            if (point.indexOf('%') != -1) {
                comparateur_point = " like ";
            }

            if (sous_point.indexOf('%') != -1) {
                comparateur_sous_point = " like ";
            }

            StringBuilder requete_globale = new StringBuilder("");

            /* ASSURE : HABILITATIONS SUR EG */
            requete_globale
                    .append("select a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, 'I' AS pole, "
                            + "mut.LIBELLE as MUTUELLE, "
                            + "eg.LIBELLE as EG,  moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point, 'Assuré', ben.CODE as CODE, cod_cloture.LIBELLE, a.commentaire as commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, HOTLINE.CAMPAGNE c, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, APPLICATION.BENEFICIAIRE ben,  application.personne pers, HOTLINE.CODES cod_type_appelant, hotline.teleacteurentitegestion teg "
                            + "WHERE a.CREATEUR_ID = t.ID	AND a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE	AND cod_type_appelant.ALIAS = 'ASSURE' "
                            + "AND a.CAMPAGNE_ID = c.ID AND a.MUTUELLE_ID = mut.ID AND a.ENTITEGESTION_ID = eg.ID(+) AND a.BENEFICIAIRE_ID = ben.ID(+) "
                            + "AND ben.PERSONNE_ID = pers.ID(+) "
                            + "AND a.MOTIF_ID = moa.ID(+)	AND a.S_MOTIF_ID = smoa.ID(+) AND a.POINT_ID = p.ID(+) AND a.S_POINT_ID = sp.ID(+) "
                            + "AND teg.ENTITEGESTION_ID = a.ENTITEGESTION_ID AND teg.TELEACTEUR_ID = ? "
                            + "AND a.CLOTURE_CODE = cod_cloture.CODE ");

            // CHAMPS DE RECHERCHE
            if (!"".equals(fiche_id)) {
                requete_globale.append(" AND a.ID " + comparateur_fiche_id
                        + " ?  ");
            }

            if (!"".equals(mot_cle)) {
                requete_globale.append(" AND upper(a.COMMENTAIRE) "
                        + comparateur_mot_cle + " upper(?) ");
            }

            if (!"".equals(motif)) {
                requete_globale.append(" AND upper(moa.LIBELLE)"
                        + comparateur_motif + " upper(?) ");
            }
            if (!"".equals(sous_motif)) {
                requete_globale.append(" AND upper(smoa.LIBELLE)"
                        + comparateur_sous_motif + " upper(?) ");
            }
            if (!"".equals(point)) {
                requete_globale.append(" AND upper(p.LIBELLE)"
                        + comparateur_point + " upper(?) ");
            }
            if (!"".equals(sous_point)) {
                requete_globale.append(" AND upper(sp.LIBELLE)"
                        + comparateur_sous_point + " upper(?) ");
            }

            // FILTRES
            if (!"".equals(campagne_id)) {
                requete_globale.append(filtre_campagne);
            }

            if (!"".equals(reference_id)) {
                requete_globale.append(filtre_reference);
            }

            if (!"".equals(type_appelant)) {
                requete_globale.append(filtre_type_appelant);
            }
            if (!"".equals(createur_id)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(statut_id)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(reclamation)) {
                requete_globale.append(filtre_reclamation);
            }
            if (!"".equals(satisfaction_id)) {
                requete_globale.append(filtre_satisfaction);
            }
            if (!"".equals(date_debut)) {
                requete_globale.append(filtre_date_debut);
            }
            if (!"".equals(date_fin)) {
                requete_globale.append(filtre_date_fin);
            }

            requete_globale.append(" UNION  ");

            /* ENTREPRISE : HABILITATIONS SUR EG */
            requete_globale
                    .append("select a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, 'C' AS pole,  "
                            + "mut.LIBELLE as MUTUELLE, "
                            + "eg.LIBELLE as EG,  moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point, 'Entreprise', etab.CODE as CODE, cod_cloture.LIBELLE, a.commentaire as commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, HOTLINE.CAMPAGNE c, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, APPLICATION.ETABLISSEMENT etab, HOTLINE.CODES cod_type_appelant, hotline.teleacteurentitegestion teg "
                            + "WHERE a.CREATEUR_ID = t.ID	AND a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE AND cod_type_appelant.ALIAS = 'ENTREPRISE'	"
                            + "AND a.CAMPAGNE_ID = c.ID AND a.MUTUELLE_ID = mut.ID AND a.ENTITEGESTION_ID = eg.ID(+) AND a.ETABLISSEMENT_ID = etab.ID(+) "
                            + "AND a.MOTIF_ID = moa.ID(+)	AND a.S_MOTIF_ID = smoa.ID(+) AND a.POINT_ID = p.ID(+) AND a.S_POINT_ID = sp.ID(+) "
                            + "AND teg.ENTITEGESTION_ID = a.ENTITEGESTION_ID AND teg.TELEACTEUR_ID = ? "
                            + "AND a.CLOTURE_CODE = cod_cloture.CODE	");

            // CHAMPS DE RECHERCHE
            if (!"".equals(fiche_id)) {
                requete_globale.append(" AND a.ID " + comparateur_fiche_id
                        + " ?  ");
            }

            if (!"".equals(mot_cle)) {
                requete_globale.append(" AND upper(a.COMMENTAIRE) "
                        + comparateur_mot_cle + " upper(?) ");
            }

            if (!"".equals(motif)) {
                requete_globale.append(" AND upper(moa.LIBELLE)"
                        + comparateur_motif + " upper(?) ");
            }
            if (!"".equals(sous_motif)) {
                requete_globale.append(" AND upper(smoa.LIBELLE)"
                        + comparateur_sous_motif + " upper(?) ");
            }
            if (!"".equals(point)) {
                requete_globale.append(" AND upper(p.LIBELLE)"
                        + comparateur_point + " upper(?) ");
            }
            if (!"".equals(sous_point)) {
                requete_globale.append(" AND upper(sp.LIBELLE)"
                        + comparateur_sous_point + " upper(?) ");
            }

            // FILTRES
            if (!"".equals(campagne_id)) {
                requete_globale.append(filtre_campagne);
            }

            if (!"".equals(reference_id)) {
                requete_globale.append(filtre_reference);
            }

            if (!"".equals(type_appelant)) {
                requete_globale.append(filtre_type_appelant);
            }
            if (!"".equals(createur_id)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(statut_id)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(reclamation)) {
                requete_globale.append(filtre_reclamation);
            }
            if (!"".equals(satisfaction_id)) {
                requete_globale.append(filtre_satisfaction);
            }
            if (!"".equals(date_debut)) {
                requete_globale.append(filtre_date_debut);
            }
            if (!"".equals(date_fin)) {
                requete_globale.append(filtre_date_fin);
            }

            requete_globale.append(" UNION  ");

            /* APPELANT : HABILITATION SUR LA MUTUELLE */
            requete_globale
                    .append("select a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, '' AS pole, "
                            + "mut.LIBELLE as MUTUELLE, "
                            + "null as EG, moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point,  cod_type_appelant.LIBELLE as Type_appelant, null as CODE, cod_cloture.LIBELLE, a.commentaire as commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, HOTLINE.CAMPAGNE c, APPLICATION.MUTUELLE mut, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, HOTLINE.APPELANT app, HOTLINE.CODES cod_type_appelant, HOTLINE.TELEACTEURCAMPAGNE tc, hotline.CAMPMUT cm "
                            + "WHERE a.CREATEUR_ID = t.ID AND a.CODEAPPELANT_SELECTIONNE IS NOT NULL AND a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE(+) "
                            + "AND a.CAMPAGNE_ID = c.ID AND a.MUTUELLE_ID = mut.ID  "
                            + "AND a.APPELANT_ID = app.ID(+) "
                            + "AND a.MOTIF_ID = moa.ID(+) AND a.S_MOTIF_ID = smoa.ID(+) AND a.POINT_ID = p.ID(+) AND a.S_POINT_ID = sp.ID(+) "
                            + "AND (cod_type_appelant.CODE IS NULL OR cod_type_appelant.ALIAS NOT IN ('ASSURE', 'ENTREPRISE')) "
                            + "AND tc.CAMPAGNE_ID = cm.CAMPAGNE_ID AND cm.MUTUELLE_ID = a.MUTUELLE_ID AND tc.TELEACTEUR_ID = ? "
                            + "AND a.CLOTURE_CODE = cod_cloture.CODE ");

            // CHAMPS DE RECHERCHE
            if (!"".equals(fiche_id)) {
                requete_globale.append(" AND a.ID " + comparateur_fiche_id
                        + " ?  ");
            }

            if (!"".equals(mot_cle)) {
                requete_globale.append(" AND upper(a.COMMENTAIRE) "
                        + comparateur_mot_cle + " upper(?) ");
            }

            if (!"".equals(motif)) {
                requete_globale.append(" AND upper(moa.LIBELLE)"
                        + comparateur_motif + " upper(?) ");
            }
            if (!"".equals(sous_motif)) {
                requete_globale.append(" AND upper(smoa.LIBELLE)"
                        + comparateur_sous_motif + " upper(?) ");
            }
            if (!"".equals(point)) {
                requete_globale.append(" AND upper(p.LIBELLE)"
                        + comparateur_point + " upper(?) ");
            }
            if (!"".equals(sous_point)) {
                requete_globale.append(" AND upper(sp.LIBELLE)"
                        + comparateur_sous_point + " upper(?) ");
            }

            // FILTRES
            if (!"".equals(campagne_id)) {
                requete_globale.append(filtre_campagne);
            }

            if (!"".equals(reference_id)) {
                requete_globale.append(filtre_reference);
            }

            if (!"".equals(type_appelant)) {
                requete_globale.append(filtre_type_appelant);
            }
            if (!"".equals(createur_id)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(statut_id)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(reclamation)) {
                requete_globale.append(filtre_reclamation);
            }
            if (!"".equals(satisfaction_id)) {
                requete_globale.append(filtre_satisfaction);
            }
            if (!"".equals(date_debut)) {
                requete_globale.append(filtre_date_debut);
            }
            if (!"".equals(date_fin)) {
                requete_globale.append(filtre_date_fin);
            }

            // AJOUT DEBUT
            requete_globale.append(" UNION  ");

            /* AUCUN APPELANT : HABILITATION SUR LA MUTUELLE */
            requete_globale
                    .append("select a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, '' AS pole, "
                            + "mut.LIBELLE as MUTUELLE, "
                            + "null as EG, null as motif, null as sous_motif, "
                            + "null as point, null as sous_point,  null as Type_appelant, null as CODE, cod_cloture.LIBELLE, a.commentaire as commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, HOTLINE.CAMPAGNE c, APPLICATION.MUTUELLE mut,  "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, HOTLINE.TELEACTEURCAMPAGNE tc, hotline.CAMPMUT cm "
                            + "WHERE a.CREATEUR_ID = t.ID AND a.CODEAPPELANT_SELECTIONNE is null "
                            + "AND a.MOTIF_ID = moa.ID(+) AND a.S_MOTIF_ID = smoa.ID(+) AND a.POINT_ID = p.ID(+) AND a.S_POINT_ID = sp.ID(+) "
                            + "AND a.CAMPAGNE_ID = c.ID AND a.MUTUELLE_ID = mut.ID "
                            + "AND tc.CAMPAGNE_ID = cm.CAMPAGNE_ID AND cm.MUTUELLE_ID = a.MUTUELLE_ID AND tc.TELEACTEUR_ID = ? "
                            + "AND a.CLOTURE_CODE = cod_cloture.CODE ");

            // CHAMPS DE RECHERCHE :
            if (!"".equals(fiche_id)) {
                requete_globale.append(" AND a.ID " + comparateur_fiche_id
                        + " ?  ");
            }

            if (!"".equals(mot_cle)) {
                requete_globale.append(" AND upper(a.COMMENTAIRE) "
                        + comparateur_mot_cle + " upper(?) ");
            }

            if (!"".equals(motif)) {
                requete_globale.append(" AND upper(moa.LIBELLE)"
                        + comparateur_motif + " upper(?) ");
            }
            if (!"".equals(sous_motif)) {
                requete_globale.append(" AND upper(smoa.LIBELLE)"
                        + comparateur_sous_motif + " upper(?) ");
            }
            if (!"".equals(point)) {
                requete_globale.append(" AND upper(p.LIBELLE)"
                        + comparateur_point + " upper(?) ");
            }
            if (!"".equals(sous_point)) {
                requete_globale.append(" AND upper(sp.LIBELLE)"
                        + comparateur_sous_point + " upper(?) ");
            }

            // FILTRES
            if (!"".equals(campagne_id)) {
                requete_globale.append(filtre_campagne);
            }

            if (!"".equals(reference_id)) {
                requete_globale.append(filtre_reference);
            }

            if (!"".equals(createur_id)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(statut_id)) {
                requete_globale.append(filtre_statut);
            }

            if (!"".equals(date_debut)) {
                requete_globale.append(filtre_date_debut);
            }
            if (!"".equals(date_fin)) {
                requete_globale.append(filtre_date_fin);
            }

            // AJOUT FIN
            requete_globale.append(" ORDER BY 1 DESC ");

            stmt = conn.prepareStatement(requete_globale.toString(),
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            // ASSURES
            stmt.setString(compteur_bind, teleacteur_id);
            compteur_bind++;

            if (!"".equals(fiche_id)) {
                stmt.setString(compteur_bind, fiche_id);
                compteur_bind++;
            }
            if (!"".equals(mot_cle)) {
                stmt.setString(compteur_bind, mot_cle);
                compteur_bind++;
            }
            if (!"".equals(motif)) {
                stmt.setString(compteur_bind, motif);
                compteur_bind++;
            }
            if (!"".equals(sous_motif)) {
                stmt.setString(compteur_bind, sous_motif);
                compteur_bind++;
            }
            if (!"".equals(point)) {
                stmt.setString(compteur_bind, point);
                compteur_bind++;
            }
            if (!"".equals(sous_point)) {
                stmt.setString(compteur_bind, sous_point);
                compteur_bind++;
            }
            if (!"".equals(campagne_id)) {
                stmt.setString(compteur_bind, campagne_id);
                compteur_bind++;
            }
            if (!"".equals(reference_id)) {
                stmt.setString(compteur_bind, reference_id);
                compteur_bind++;
            }
            if (!"".equals(type_appelant)) {
                stmt.setString(compteur_bind, type_appelant);
                compteur_bind++;
            }
            if (!"".equals(createur_id)) {
                stmt.setString(compteur_bind, createur_id);
                compteur_bind++;
            }
            if (!"".equals(statut_id)) {
                stmt.setString(compteur_bind, statut_id);
                compteur_bind++;
            }
            if (!"".equals(reclamation)) {
                stmt.setString(compteur_bind, reclamation);
                compteur_bind++;
            }
            if (!"".equals(satisfaction_id)) {
                stmt.setString(compteur_bind, satisfaction_id);
                compteur_bind++;
            }
            if (!"".equals(date_debut)) {
                stmt.setString(compteur_bind, date_debut);
                compteur_bind++;
            }
            if (!"".equals(date_fin)) {
                stmt.setString(compteur_bind, date_fin);
                compteur_bind++;
            }

            // ENTREPRISES
            stmt.setString(compteur_bind, teleacteur_id);
            compteur_bind++;

            if (!"".equals(fiche_id)) {
                stmt.setString(compteur_bind, fiche_id);
                compteur_bind++;
            }
            if (!"".equals(mot_cle)) {
                stmt.setString(compteur_bind, mot_cle);
                compteur_bind++;
            }
            if (!"".equals(motif)) {
                stmt.setString(compteur_bind, motif);
                compteur_bind++;
            }
            if (!"".equals(sous_motif)) {
                stmt.setString(compteur_bind, sous_motif);
                compteur_bind++;
            }
            if (!"".equals(point)) {
                stmt.setString(compteur_bind, point);
                compteur_bind++;
            }
            if (!"".equals(sous_point)) {
                stmt.setString(compteur_bind, sous_point);
                compteur_bind++;
            }
            if (!"".equals(campagne_id)) {
                stmt.setString(compteur_bind, campagne_id);
                compteur_bind++;
            }
            if (!"".equals(reference_id)) {
                stmt.setString(compteur_bind, reference_id);
                compteur_bind++;
            }
            if (!"".equals(type_appelant)) {
                stmt.setString(compteur_bind, type_appelant);
                compteur_bind++;
            }
            if (!"".equals(createur_id)) {
                stmt.setString(compteur_bind, createur_id);
                compteur_bind++;
            }
            if (!"".equals(statut_id)) {
                stmt.setString(compteur_bind, statut_id);
                compteur_bind++;
            }
            if (!"".equals(reclamation)) {
                stmt.setString(compteur_bind, reclamation);
                compteur_bind++;
            }
            if (!"".equals(satisfaction_id)) {
                stmt.setString(compteur_bind, satisfaction_id);
                compteur_bind++;
            }
            if (!"".equals(date_debut)) {
                stmt.setString(compteur_bind, date_debut);
                compteur_bind++;
            }
            if (!"".equals(date_fin)) {
                stmt.setString(compteur_bind, date_fin);
                compteur_bind++;
            }

            // APPELANTS
            stmt.setString(compteur_bind, teleacteur_id);
            compteur_bind++;
            if (!"".equals(fiche_id)) {
                stmt.setString(compteur_bind, fiche_id);
                compteur_bind++;
            }
            if (!"".equals(mot_cle)) {
                stmt.setString(compteur_bind, mot_cle);
                compteur_bind++;
            }
            if (!"".equals(motif)) {
                stmt.setString(compteur_bind, motif);
                compteur_bind++;
            }
            if (!"".equals(sous_motif)) {
                stmt.setString(compteur_bind, sous_motif);
                compteur_bind++;
            }
            if (!"".equals(point)) {
                stmt.setString(compteur_bind, point);
                compteur_bind++;
            }
            if (!"".equals(sous_point)) {
                stmt.setString(compteur_bind, sous_point);
                compteur_bind++;
            }
            if (!"".equals(campagne_id)) {
                stmt.setString(compteur_bind, campagne_id);
                compteur_bind++;
            }
            if (!"".equals(reference_id)) {
                stmt.setString(compteur_bind, reference_id);
                compteur_bind++;
            }
            if (!"".equals(type_appelant)) {
                stmt.setString(compteur_bind, type_appelant);
                compteur_bind++;
            }
            if (!"".equals(createur_id)) {
                stmt.setString(compteur_bind, createur_id);
                compteur_bind++;
            }
            if (!"".equals(statut_id)) {
                stmt.setString(compteur_bind, statut_id);
                compteur_bind++;
            }
            if (!"".equals(reclamation)) {
                stmt.setString(compteur_bind, reclamation);
                compteur_bind++;
            }
            if (!"".equals(satisfaction_id)) {
                stmt.setString(compteur_bind, satisfaction_id);
                compteur_bind++;
            }
            if (!"".equals(date_debut)) {
                stmt.setString(compteur_bind, date_debut);
                compteur_bind++;
            }
            if (!"".equals(date_fin)) {
                stmt.setString(compteur_bind, date_fin);
                compteur_bind++;
            }

            // AJOUT

            // PAS D'APPELANT
            stmt.setString(compteur_bind, teleacteur_id);
            compteur_bind++;

            if (!"".equals(fiche_id)) {
                stmt.setString(compteur_bind, fiche_id);
                compteur_bind++;
            }
            if (!"".equals(mot_cle)) {
                stmt.setString(compteur_bind, mot_cle);
                compteur_bind++;
            }
            if (!"".equals(motif)) {
                stmt.setString(compteur_bind, motif);
                compteur_bind++;
            }
            if (!"".equals(sous_motif)) {
                stmt.setString(compteur_bind, sous_motif);
                compteur_bind++;
            }
            if (!"".equals(point)) {
                stmt.setString(compteur_bind, point);
                compteur_bind++;
            }
            if (!"".equals(sous_point)) {
                stmt.setString(compteur_bind, sous_point);
                compteur_bind++;
            }

            if (!"".equals(campagne_id)) {
                stmt.setString(compteur_bind, campagne_id);
                compteur_bind++;
            }

            if (!"".equals(reference_id)) {
                stmt.setString(compteur_bind, reference_id);
                compteur_bind++;
            }

            if (!"".equals(createur_id)) {
                stmt.setString(compteur_bind, createur_id);
                compteur_bind++;
            }
            if (!"".equals(statut_id)) {
                stmt.setString(compteur_bind, statut_id);
                compteur_bind++;
            }

            if (!"".equals(date_debut)) {
                stmt.setString(compteur_bind, date_debut);
                compteur_bind++;
            }
            if (!"".equals(date_fin)) {
                stmt.setString(compteur_bind, date_fin);
                compteur_bind++;
            }

            // AJOUT

            rs = stmt.executeQuery();

            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); 
            }

            if (rowcount > _max_nbr_fiches_recherchees) {
                Limite limite = new Limite();
                limite.setTaille(rowcount);
                res.add(limite);
            } else {
                while (rs.next()) {
                    unDisplay = new Appel();
                    unDisplay.setDATEAPPEL((rs.getTimestamp(1) != null) ? rs
                            .getTimestamp(1) : null);
                    unDisplay.setTeleacteur((rs.getString(2) != null) ? rs
                            .getString(2) : "");
                    unDisplay.setID(rs.getString(3));
                    unDisplay.setPole((rs.getString(4) != null) ? rs
                            .getString(4) : "");
                    unDisplay.setMutuelle((rs.getString(5) != null) ? rs
                            .getString(5) : "");
                    unDisplay.setEntiteGestion((rs.getString(6) != null) ? rs
                            .getString(6) : "");
                    unDisplay.setMotif((rs.getString(7) != null) ? rs
                            .getString(7) : "");
                    unDisplay.setSousMotif((rs.getString(8) != null) ? rs
                            .getString(8) : "");
                    unDisplay.setPoint((rs.getString(9) != null) ? rs
                            .getString(9) : "");
                    unDisplay.setSousPoint((rs.getString(10) != null) ? rs
                            .getString(10) : "");
                    unDisplay.setTypeAppelant((rs.getString(11) != null) ? rs
                            .getString(11) : "");
                    unDisplay
                            .setCodeAdherentNumeroContrat((rs.getString(12) != null) ? rs
                                    .getString(12) : "");
                    unDisplay.setStatut((rs.getString(13) != null) ? rs
                            .getString(13) : "");
                    unDisplay.setCOMMENTAIRE((rs.getString(14) != null) ? rs
                            .getString(14) : "");
                    res.add(unDisplay);
                }
            }

            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheFichesAppels", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Appel> rechercheFichesATraiter(
            String teleacteur_id, String sens_tri_fiches_a_traiter,
            String col_de_tri_fiches_a_traiter) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Appel> res = new ArrayList<Appel>();
        Appel unDisplay = null;

        int nb_rows = 0;
        try {
            conn = getConnexion();

            StringBuilder requete_globale = new StringBuilder("");

            /* ASSURE : HABILITATIONS SUR EG */
            requete_globale
                    .append("select DISTINCT a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, 'I' AS pole, "
                            + "eg.LIBELLE as EG, 'Assuré', ben.CODE as CODE, mut.LIBELLE as MUTUELLE, moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point, a.commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, APPLICATION.BENEFICIAIRE ben, HOTLINE.CODES cod_type_appelant, hotline.teleacteurentitegestion teg "
                            + "WHERE a.CREATEUR_ID = t.ID	and a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE	and cod_type_appelant.ALIAS = 'ASSURE' "
                            + "and a.MUTUELLE_ID = mut.ID	and a.ENTITEGESTION_ID = eg.ID(+) and a.BENEFICIAIRE_ID = ben.ID "
                            + "and a.MOTIF_ID = moa.ID(+)	and a.S_MOTIF_ID = smoa.ID(+) and a.POINT_ID = p.ID(+) and a.S_POINT_ID = sp.ID(+) "
                            + "AND teg.ENTITEGESTION_ID = ben.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                            + "and a.CLOTURE_CODE = cod_cloture.CODE	"
                            + "and a.CLOTURE_CODE in (7, 221)"); // 'ATRAITER','ACQUITTEMENT'

            requete_globale.append(" UNION  ");

            /* ENTREPRISE : HABILITATIONS SUR EG */
            requete_globale
                    .append("select DISTINCT a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, 'C' AS pole, "
                            + "eg.LIBELLE as EG,'Entreprise', etab.CODE as CODE, mut.LIBELLE as MUTUELLE, moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point, a.commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, APPLICATION.ETABLISSEMENT etab, HOTLINE.CODES cod_type_appelant, hotline.teleacteurentitegestion teg "
                            + "WHERE a.CREATEUR_ID = t.ID	and a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE and cod_type_appelant.ALIAS = 'ENTREPRISE'	"
                            + "and a.MUTUELLE_ID = mut.ID	and a.ENTITEGESTION_ID = eg.ID(+) and a.ETABLISSEMENT_ID = etab.ID "
                            + "and a.MOTIF_ID = moa.ID(+)	and a.S_MOTIF_ID = smoa.ID(+) and a.POINT_ID = p.ID(+) and a.S_POINT_ID = sp.ID(+) "
                            + "AND teg.ENTITEGESTION_ID = etab.ENTITE_GESTION_ID AND teg.TELEACTEUR_ID = ? "
                            + "and a.CLOTURE_CODE = cod_cloture.CODE	"
                            + "and a.CLOTURE_CODE in (7, 221)");// 'ATRAITER','ACQUITTEMENT'

            requete_globale.append(" UNION  ");

            /* APPELANT : HABILITATION SUR LA MUTUELLE */
            requete_globale
                    .append("select DISTINCT a.DATEAPPEL, t.NOM || ' ' || t.PRENOM as CREATEUR, a.ID, '' AS pole, null as EG, "
                            + "cod_type_appelant.LIBELLE as Type_appelant, null as CODE, mut.LIBELLE as MUTUELLE, moa.LIBELLE as motif, smoa.LIBELLE as sous_motif, "
                            + "p.LIBELLE as point, sp.LIBELLE as sous_point, a.commentaire "
                            + "FROM hotline.APPEL a, HOTLINE.TELEACTEUR t, APPLICATION.MUTUELLE mut, APPLICATION.ENTITE_GESTION eg, "
                            + "HOTLINE.MOTIFAPPEL moa, HOTLINE.SMOTIFAPPEL smoa, HOTLINE.POINT p, HOTLINE.SPOINT sp, "
                            + "HOTLINE.CODES cod_cloture, HOTLINE.CODES cod_type_appelant, HOTLINE.TELEACTEURCAMPAGNE tc, hotline.CAMPMUT cm "
                            + "WHERE a.CREATEUR_ID = t.ID and a.CODEAPPELANT_SELECTIONNE = cod_type_appelant.CODE "
                            + "and a.MUTUELLE_ID = mut.ID	and a.ENTITEGESTION_ID = eg.ID(+) "
                            + "and a.MOTIF_ID = moa.ID(+) and a.S_MOTIF_ID = smoa.ID(+) and a.POINT_ID = p.ID(+) and a.S_POINT_ID = sp.ID(+)"
                            + "and a.CLOTURE_CODE = cod_cloture.CODE and cod_type_appelant.ALIAS NOT IN ('ASSURE', 'ENTREPRISE') "
                            + "and tc.CAMPAGNE_ID = cm.CAMPAGNE_ID and cm.MUTUELLE_ID = a.MUTUELLE_ID and tc.TELEACTEUR_ID = ? "
                            + "and a.CLOTURE_CODE in (7, 221)");// 'ATRAITER','ACQUITTEMENT'

            requete_globale.append(" ORDER BY " + col_de_tri_fiches_a_traiter
                    + " " + sens_tri_fiches_a_traiter);

            stmt = conn.prepareStatement(requete_globale.toString());
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, teleacteur_id);
            stmt.setString(3, teleacteur_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Appel();
                unDisplay.setDATEAPPEL((rs.getTimestamp(1) != null) ? rs
                        .getTimestamp(1) : null);
                unDisplay.setTeleacteur((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setID(rs.getString(3));
                unDisplay.setPole((rs.getString(4) != null) ? rs.getString(4)
                        : "");
                unDisplay.setEntiteGestion((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setTypeAppelant((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay
                        .setCodeAdherentNumeroContrat((rs.getString(7) != null) ? rs
                                .getString(7) : "");
                unDisplay.setMutuelle((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setMotif((rs.getString(9) != null) ? rs.getString(9)
                        : "");
                unDisplay.setSousMotif((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setPoint((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setSousPoint((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setCOMMENTAIRE((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();

            return res;
        } catch (Exception e) {
            LOGGER.error("rechercheFichesATraiter", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getTeleActeurCampagnesForInputSelect(
            String teleacteur_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "select distinct w.*, decode(tc.CAMPAGNE_ID, null, 'NH', 'H') FROM "
                    + "(select c.ID as IDCAMPAGNE, c.LIBELLE, c.ACTIF from hotline.CAMPAGNE c) w "
                    + "left outer join hotline.TELEACTEURCAMPAGNE tc on w.IDCAMPAGNE = tc.CAMPAGNE_ID "
                    + "and tc.TELEACTEUR_ID = ? order by 3 desc, 2 asc";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String classe = "noir11";

                if (_FAUX.equals(rs.getString(3))) {
                    classe = "gris11Radie";
                }

                if ("H".equals(rs.getString(4))) {
                    String fond = "#D9C263"; // #FF8F20
                    sb.append("<tr bgcolor='"
                            + fond
                            + "' onmouseover=\"this.style.background='#95B3DE'\" onmouseout=\"this.style.background='"
                            + fond
                            + "'\"><td class='"
                            + classe
                            + "'>"
                            + rs.getString(2)
                            + "</td><td><input type='checkbox' name='ids_campagnes' value='"
                            + rs.getString(1)
                            + "' checked='checked' /></td></tr>");
                } else {
                    sb.append("<tr bgcolor='#FFFFFF' onmouseover=\"this.style.background='#95B3DE'\" onmouseout=\"this.style.background='#FFFFFF'\"><td class='"
                            + classe
                            + "'>"
                            + rs.getString(2)
                            + "</td><td><input type='checkbox' name='ids_campagnes' value='"
                            + rs.getString(1) + "' /></td></tr>");
                }
            }
            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getTeleActeurCampagnesForInputSelect", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getTeleActeurEntitesGestionForInputSelect(
            String teleacteur_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "select distinct w.*, decode(teg.ENTITEGESTION_ID, null, 'NH', 'H') as habilite_ou_pas from("
                    + "select distinct mut.LIBELLE, eg.CODE || ' ' || eg.LIBELLE as libelle_eg, eg.ID as IDEG "
                    + "from hotline.teleacteurcampagne tc, hotline.campagne c, "
                    + "application.mutuelle mut, hotline.campmut cm, application.entite_gestion eg "
                    + "where tc.TELEACTEUR_ID = ? and tc.CAMPAGNE_ID = c.id and cm.CAMPAGNE_ID = c.id "
                    + "and cm.MUTUELLE_ID = mut.id and eg.MUTUELLE_ID = mut.id and eg.type = 'E' ) w "
                    + "left outer join hotline.TELEACTEURENTITEGESTION teg on w.IDEG = teg.ENTITEGESTION_ID and teg.TELEACTEUR_ID = ? order by 1, 2 asc";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, teleacteur_id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                if ("H".equals(rs.getString(4))) {
                    String fond = "#D9C263"; // #FF8F20
                    sb.append("<tr bgcolor='"
                            + fond
                            + "' onmouseover=\"this.style.background='#95B3DE'\" onmouseout=\"this.style.background='"
                            + fond
                            + "'\"><td class='noir11'>"
                            + rs.getString(1)
                            + "</td><td class='noir11'>"
                            + rs.getString(2)
                            + "</td><td><input type='checkbox' name='ids_eg' value='"
                            + rs.getString(3)
                            + "' checked='checked' /></td></tr>");
                } else {
                    sb.append("<tr bgcolor='#FFFFFF' onmouseover=\"this.style.background='#95B3DE'\" onmouseout=\"this.style.background='#FFFFFF'\"><td class='noir11'>"
                            + rs.getString(1)
                            + "</td><td class='noir11'>"
                            + rs.getString(2)
                            + "</td><td><input type='checkbox' name='ids_eg' value='"
                            + rs.getString(3) + "' /></td></tr>");
                }
            }

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getTeleActeurEntitesGestionForInputSelect", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean affecterTeleActeurCampagnes(String teleacteur_id,
            String[] ids_campagnes) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requeteList = "";

        String requeteDelete = "";
        String requeteInsert = "";

        List<String> old_campagnes = new ArrayList<String>();
        List<String> new_campagnes = new ArrayList<String>();

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // LISTE
            requeteList = "SELECT tc.CAMPAGNE_ID FROM HOTLINE.TELEACTEURCAMPAGNE tc WHERE tc.TELEACTEUR_ID = ?";
            stmt = conn.prepareStatement(requeteList);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();

            for (int i = 0; i < ids_campagnes.length; i++) {
                new_campagnes.add(ids_campagnes[i]);
            }

            String currCampagne = null;
            boolean found = false;
            while (rs.next()) {
                currCampagne = rs.getString(1);
                found = false;
                for (int i = 0; i < ids_campagnes.length; i++) {
                    if (currCampagne.equalsIgnoreCase(ids_campagnes[i])) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    old_campagnes.add(currCampagne);
                } else {
                    new_campagnes.remove(currCampagne);
                }
            }

            // DELETE DANS TELEACTEURCAMPAGNE
            requeteDelete = "DELETE FROM HOTLINE.TELEACTEURCAMPAGNE tc WHERE tc.TELEACTEUR_ID = ? AND tc.CAMPAGNE_ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            for (String campagne_id : old_campagnes) {
                stmt.setString(1, teleacteur_id);
                stmt.setString(2, campagne_id);
                stmt.executeQuery();
                stmt.clearParameters();
            }
            stmt.close();

            // DELETE DANS TELEACTEURENTITEGESTION
            requeteDelete = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTION teg "
                    + "WHERE TEG.TELEACTEUR_ID = ? "
                    + "    AND NOT EXISTS(SELECT eg.id  "
                    + "                   FROM HOTLINE.TELEACTEURCAMPAGNE tc, "
                    + "                        HOTLINE.CAMPMUT            cm, "
                    + "                        HOTLINE.ENTITE_GESTION     eg "
                    + "                   WHERE TC.TELEACTEUR_ID = TEG.TELEACTEUR_ID "
                    + "                        AND TC.CAMPAGNE_ID = CM.CAMPAGNE_ID "
                    + "                        AND EG.MUTUELLE_ID = CM.MUTUELLE_ID "
                    + "                        AND TEG.ENTITEGESTION_ID = eg.id )";

            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, teleacteur_id);
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();

            // DELETE DANS TELEACTEURENTITEGESTIONBL
            requeteDelete = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTIONBL teg "
                    + "WHERE TEG.TELEACTEUR_ID = ? "
                    + "    AND NOT EXISTS(SELECT eg.id  "
                    + "                   FROM HOTLINE.TELEACTEURCAMPAGNE tc, "
                    + "                        HOTLINE.CAMPMUT            cm, "
                    + "                        HOTLINE.ENTITE_GESTION     eg "
                    + "                   WHERE TC.TELEACTEUR_ID = TEG.TELEACTEUR_ID "
                    + "                        AND TC.CAMPAGNE_ID = CM.CAMPAGNE_ID "
                    + "                        AND EG.MUTUELLE_ID = CM.MUTUELLE_ID "
                    + "                        AND TEG.ENTITEGESTION_ID = eg.id )";

            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, teleacteur_id);
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();

            // INSERTION
            requeteInsert = "INSERT INTO HOTLINE.TELEACTEURCAMPAGNE (TELEACTEUR_ID, CAMPAGNE_ID) VALUES(?,?)";
            stmt = conn.prepareStatement(requeteInsert);
            for (String campagne_id : new_campagnes) {
                stmt.setString(1, teleacteur_id);
                stmt.setString(2, campagne_id);
                stmt.executeQuery();
                stmt.clearParameters();
            }
            stmt.close();

            requeteInsert = "INSERT INTO HOTLINE.TELEACTEURENTITEGESTION( TELEACTEUR_ID, ENTITEGESTION_ID ) "
                    + "SELECT DISTINCT TC.TELEACTEUR_ID,eg.id "
                    + "FROM HOTLINE.TELEACTEURCAMPAGNE tc, "
                    + "     HOTLINE.CAMPMUT            cm, "
                    + "     HOTLINE.ENTITE_GESTION     eg, "
                    + "     APPLICATION.ENTITE_GESTION meg "
                    + "WHERE TC.TELEACTEUR_ID = ? "
                    + "    AND TC.CAMPAGNE_ID = CM.CAMPAGNE_ID "
                    + "    AND EG.MUTUELLE_ID = CM.MUTUELLE_ID "
                    + "    AND EG.ID=MEG.ID "
                    + "    AND MEG.TYPE!='T' "
                    + "    AND NOT EXISTS( SELECT EGBL.ENTITEGESTION_ID "
                    + "                    FROM HOTLINE.ENTITEGESTIONBLACKLISTEE egbl "
                    + "                    WHERE EGBL.ENTITEGESTION_ID = EG.ID "
                    + "                        AND EGBL.MUTUELLE_ID = CM.MUTUELLE_ID ) "
                    + "MINUS     "
                    + "SELECT TEG.TELEACTEUR_ID,TEG.ENTITEGESTION_ID "
                    + "FROM HOTLINE.TELEACTEURENTITEGESTION teg "
                    + "WHERE TEG.TELEACTEUR_ID = ? ";
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, teleacteur_id);
            stmt.executeQuery();

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("affecterTeleActeurCampagnes", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean affecterTeleActeurEntitesGestion(
            String teleacteur_id, String[] ids_eg) {
        Connection conn = null;
        PreparedStatement stmt = null;

        String requeteDelete = "";
        String requeteInsert = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // DELETE DANS TELEACTEURENTITEGESTION
            requeteDelete = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTION teg where teg.TELEACTEUR_ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, teleacteur_id);
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();

            // EVENTUELS INSERT
            if (ids_eg != null) {
                int taille_tableau = ids_eg.length;
                requeteInsert = "INSERT INTO HOTLINE.TELEACTEURENTITEGESTION (TELEACTEUR_ID, ENTITEGESTION_ID) VALUES(?,?)";
                for (int i = 0; i < taille_tableau; i++) {
                    stmt = conn.prepareStatement(requeteInsert);
                    stmt.setString(1, teleacteur_id);
                    stmt.setString(2, ids_eg[i]);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("affecterTeleActeurEntitesGestion", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean affecterTeleActeurEntitesGestionSensible(
            String entite_gestion_id, String[] ids_teleacteurs) {
        Connection conn = null;
        PreparedStatement stmt = null;

        String requeteDelete = "";
        String requeteInsert = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // DELETE DANS TELEACTEURENTITEGESTIONBL
            requeteDelete = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTIONBL teg where teg.ENTITEGESTION_ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, entite_gestion_id);
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();

            // EVENTUELS INSERT
            if (ids_teleacteurs != null) {
                int taille_tableau = ids_teleacteurs.length;
                requeteInsert = "INSERT INTO HOTLINE.TELEACTEURENTITEGESTIONBL (TELEACTEUR_ID, ENTITEGESTION_ID) VALUES(?,?)";
                stmt = conn.prepareStatement(requeteInsert);
                for (int i = 0; i < taille_tableau; i++) {
                    stmt.clearParameters();
                    stmt.setString(1, ids_teleacteurs[i]);
                    stmt.setString(2, entite_gestion_id);
                    stmt.executeQuery();
                    stmt.clearParameters();
                }
                stmt.close();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("affecterTeleActeurEntitesGestionSensible", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean appliquerCopieHabilitations(String teleacteur_id,
            String[] ids_teleacteurs) {

        Connection conn = null;
        PreparedStatement stmt = null;

        String requeteDeleteTC = "", requeteDeleteTEG = "";
        String requeteInsertTC = "", requeteInsertTEG = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // DELETE DANS TELEACTEURENTITEGESTION POUR LES TELEACTEURS
            // SELECTIONNES
            requeteDeleteTC = "DELETE FROM HOTLINE.TELEACTEURCAMPAGNE tc where tc.TELEACTEUR_ID = ?";
            requeteDeleteTEG = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTION teg where teg.TELEACTEUR_ID = ?";

            if (ids_teleacteurs != null) {
                int taille_tableau = ids_teleacteurs.length;
                for (int i = 0; i < taille_tableau; i++) {
                    stmt = conn.prepareStatement(requeteDeleteTC);
                    stmt.setString(1, ids_teleacteurs[i]);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();

                    stmt = conn.prepareStatement(requeteDeleteTEG);
                    stmt.setString(1, ids_teleacteurs[i]);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();
                }
            }

            // EVENTUELS INSERT
            requeteInsertTC = "INSERT INTO HOTLINE.TELEACTEURCAMPAGNE (TELEACTEUR_ID, CAMPAGNE_ID) "
                    + "SELECT ?, tc.CAMPAGNE_ID FROM HOTLINE.TELEACTEURCAMPAGNE tc where tc.TELEACTEUR_ID = ? ";

            requeteInsertTEG = "INSERT INTO HOTLINE.TELEACTEURENTITEGESTION (TELEACTEUR_ID, ENTITEGESTION_ID) "
                    + "SELECT ?, teg.ENTITEGESTION_ID FROM HOTLINE.TELEACTEURENTITEGESTION teg where teg.TELEACTEUR_ID = ? ";

            if (ids_teleacteurs != null) {
                int taille_tableau = ids_teleacteurs.length;
                for (int i = 0; i < taille_tableau; i++) {
                    stmt = conn.prepareStatement(requeteInsertTC);
                    stmt.setString(1, ids_teleacteurs[i]);
                    stmt.setString(2, teleacteur_id);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();

                    stmt = conn.prepareStatement(requeteInsertTEG);
                    stmt.setString(1, ids_teleacteurs[i]);
                    stmt.setString(2, teleacteur_id);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("appliquerCopieHabilitations", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder getInfosTeleActeur(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            conn = getConnexion();

            String prenom_nom = (String) request.getParameter("prenom_nom");
            String teleacteur_id = (String) request
                    .getParameter("teleacteur_id");
            String utl_id = (String) request.getParameter("utl_id");

            String requete = "SELECT prs.PRS_PRENOM ||' ' || prs.PRS_NOM as COLLABORATEUR, srv.SRV_LIB, pol.POL_LIB, "
                    + "respon.PRS_PRENOM ||' ' || respon.PRS_NOM as responsable "
                    + "FROM H_ANNUAIRE.T_UTILISATEURS_UTL utl, H_ANNUAIRE.T_PERSONNES_PRS prs, H_ANNUAIRE.T_PERSONNES_PRS respon, "
                    + "H_ANNUAIRE.t_personnesservicespoles_psp psp, H_ANNUAIRE.t_services_srv srv, H_ANNUAIRE.t_poles_pol pol "
                    + "WHERE utl.UTL_ID = ? AND utl.UTL_PRS_ID = prs.PRS_ID AND prs.prs_id = psp.psp_prs_id(+) "
                    + "AND psp.psp_srv_id = srv.srv_id(+) AND psp.psp_pol_id = pol.pol_id(+) "
                    + "AND prs.PRS_PRS_ID_SUPERIEUR = respon.PRS_ID(+)";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, utl_id);
            rs = stmt.executeQuery();

            sb.append("<div class='separateur' style='padding-top:10px'/><img src='../img/puce_bloc.gif'><label class='noir11B'>INFORMATIONS GENERALES </label> </div>");
            sb.append("<div style='padding-top:5px'><table cellpadding='4' cellspacing='4'>");
            if (rs.next()) {
                sb.append("<tr><td class='bleu11'>&nbsp;&nbsp;&nbsp;Collaborateur</td><td class='noir11'>"
                        + prenom_nom.toUpperCase() + "</td></tr>");
                sb.append("<tr><td class='bleu11'>&nbsp;&nbsp;&nbsp;Service</td><td class='noir11'>"
                        + ((rs.getString(2) != null) ? rs.getString(2) : "")
                        + "</td></tr>");
                sb.append("<tr><td class='bleu11'>&nbsp;&nbsp;&nbsp;P&ocirc;le</td><td class='noir11'>"
                        + ((rs.getString(3) != null) ? rs.getString(3) : "")
                        + "</td></tr>");
                sb.append("<tr><td class='bleu11'>&nbsp;&nbsp;&nbsp;Responsable</td><td class='noir11'>"
                        + ((rs.getString(4) != null) ? rs.getString(4) : "")
                        + "</td></tr>");
            } else {
                sb.append("<tr><td class='bordeau11' colspan='2'>&nbsp;&nbsp;&nbsp;Utilisateur non r&eacute;f&eacute;renc&eacute; dans l'annuaire iGestion.</td></tr>");
            }
            sb.append("</table></div>");
            stmt.clearParameters();

            sb.append("<br>");

            requete = "SELECT c.LIBELLE, c.ACTIF FROM HOTLINE.CAMPAGNE c, HOTLINE.TELEACTEURCAMPAGNE tc WHERE tc.TELEACTEUR_ID = ? "
                    + "AND tc.CAMPAGNE_ID = c.ID order by 1 asc";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();

            sb.append("<div class='separateur' style='padding-top:10px'/><img src='../img/puce_bloc.gif'><label class='noir11B'>HABILITE SUR LES CAMPAGNES</label></div>");
            sb.append("<div style='padding-top:5px'><table cellpadding='4' cellspacing='4'>");
            sb.append("<tr><td class='bleu11B'>&nbsp;&nbsp;&nbsp;CAMPAGNE</td></tr>");

            while (rs.next()) {
                String classe = (_VRAI.equals(rs.getString(2))) ? "noir11"
                        : "gris11";

                sb.append("<tr><td class='" + classe
                        + "' colspan='2'>&nbsp;&nbsp;&nbsp;" + rs.getString(1)
                        + "</td></tr>");
            }
            sb.append("</table></div>");
            sb.append("<br>");

            stmt.clearParameters();

            requete = "SELECT mut.LIBELLE, mut.ACTIF,  eg.CODE || ' ' || eg.LIBELLE, eg.ACTIF  "
                    + "FROM APPLICATION.ENTITE_GESTION eg, HOTLINE.TELEACTEURENTITEGESTION teg, APPLICATION.MUTUELLE mut "
                    + "WHERE teg.TELEACTEUR_ID = ? "
                    + "AND teg.ENTITEGESTION_ID = eg.ID "
                    + "AND eg.MUTUELLE_ID = mut.ID " + "ORDER BY 1 asc, 3 asc";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();

            sb.append("<div class='separateur' style='padding-top:10px'/><img src='../img/puce_bloc.gif'><label class='noir11B'>HABILITE SUR LES ENTITES DE GESTION</label></div>");
            sb.append("<div style='padding-top:5px'><table cellpadding='4' cellspacing='4'>");
            sb.append("<tr><td class='bleu11B'>&nbsp;&nbsp;&nbsp;CLIENT</td><td class='bleu11B'>ENTITE DE GESTION</td></tr>");

            while (rs.next()) {
                String classe_mutuelle = (_VRAI.equals(rs.getString(2))) ? "noir11"
                        : "gris11";
                String classe_eg = (_VRAI.equals(rs.getString(4))) ? "noir11"
                        : "gris11";
                sb.append("<tr><td class='"
                        + classe_mutuelle
                        + "' >&nbsp;&nbsp;&nbsp;"
                        + rs.getString(1)
                        + "</td><td class='"
                        + classe_eg
                        + "'>"
                        + ((rs.getString(3) != null) ? rs.getString(3)
                                : "&nbsp;") + "</td></tr>");
            }
            sb.append("</table></div>");
            stmt.clearParameters();

            return sb;
        } catch (Exception e) {
            LOGGER.error("getInfosTeleActeur", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean supprimerMessage(String message_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "DELETE FROM HOTLINE.MESSAGE m WHERE m.ID = ?";
            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, message_id);

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerMessage", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerTransfert(String transfert_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "DELETE FROM HOTLINE.T_TRANSFERTS_TRA t WHERE t.TRA_ID = ?";
            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, transfert_id);

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerTransfert", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerEntiteGestionSensible(
            String entite_gestion_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "DELETE FROM HOTLINE.TELEACTEURENTITEGESTIONBL tegbl WHERE tegbl.ENTITEGESTION_ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, entite_gestion_id);
            stmt.executeQuery();
            stmt.clearParameters();

            requete = "DELETE FROM HOTLINE.ENTITEGESTIONBLACKLISTEE egbl WHERE egbl.ENTITEGESTION_ID = ? ";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, entite_gestion_id);
            stmt.executeQuery();
            stmt.clearParameters();

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerEntiteGestionSensible", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder getCampagnesForInputSelect() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT c.ID, c.LIBELLE FROM HOTLINE.CAMPAGNE c order by 2 asc";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            sb.append("<select class='swing_11' id='campagne_id' name='campagne_id'>");

            sb.append("<option selected='selected' value='-1'>Choisir une campagne</option>");

            while (rs.next()) {
                sb.append("<option value=\"" + rs.getString(1) + "\">"
                        + rs.getString(2) + "</option>");
            }

            sb.append("</select>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getCampagnesForInputSelect", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Campagne> getCampagnes(String actif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Campagne> res = new ArrayList<Campagne>();
        Campagne unDisplay = null;

        try {

            String requete = "SELECT c.ID, c.LIBELLE, c.ACTIF FROM HOTLINE.CAMPAGNE c ";
            if (_VRAI.equals(actif)) {
                requete += " AND c.ACTIF = 1 ";
            }
            requete += "order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new Campagne();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setActif((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getCampagnes", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean modifierMessage(String message_id,
            String campagne_id, String titre, String contenu, String dateDebut,
            String dateFin) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE HOTLINE.MESSAGE m set m.CAMPAGNE_ID = ?, m.TITRE = ?, m.CONTENU = ?, "
                    + "m.DATEDEBUT = to_date(?, 'DD/MM/YYYY'), m.DATEFIN = to_date(?, 'DD/MM/YYYY') "
                    + "WHERE m.ID = ?";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, campagne_id);
            stmt.setString(2, titre);
            stmt.setString(3, contenu);
            stmt.setString(4, dateDebut);
            stmt.setString(5, dateFin);
            stmt.setString(6, message_id);

            stmt.executeQuery();
            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("modifierMessage", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean creerMessage(String campagne_id, String titre,
            String contenu, String dateDebut, String dateFin) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "INSERT INTO HOTLINE.MESSAGE (CAMPAGNE_ID, TITRE, CONTENU, DATEDEBUT, DATEFIN) VALUES (?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, campagne_id);
            stmt.setString(2, titre);
            stmt.setString(3, contenu);
            stmt.setString(4, dateDebut);
            stmt.setString(5, dateFin);

            stmt.executeQuery();
            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("creerMessage", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean creerTransfert(String libelle, String email) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "INSERT INTO HOTLINE.T_TRANSFERTS_TRA (TRA_ID, TRA_LIBELLE, TRA_EMAIL) VALUES ( HOTLINE.SEQ_TRA.nextVal, ?, ? )";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, libelle);
            stmt.setString(2, email);
            stmt.executeQuery();
            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("creerTransfert", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterEntitesGestionsSensibles(
            String[] ids_entites_gestions) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "INSERT INTO HOTLINE.ENTITEGESTIONBLACKLISTEE (ENTITEGESTION_ID, MUTUELLE_ID) "
                    + "(select ?, eg.MUTUELLE_ID FROM APPLICATION.ENTITE_GESTION eg WHERE eg.id = ?)";

            if (ids_entites_gestions != null) {
                for (int i = 0; i < ids_entites_gestions.length; i++) {
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, ids_entites_gestions[i]);
                    stmt.setString(2, ids_entites_gestions[i]);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();
                }
            }

            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterEntitesGestionsSensibles", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder searchAllObjectsConnexions(String col_de_tri,
            String sens) {

        Collection<HttpSession> connexions = HContactsSessionListener
                .getSessionsActives();
        Collection<InfosDeSession> connexions_a_trier = new ArrayList<InfosDeSession>();

        for (int i = 0; i < connexions.size(); i++) {
            HttpSession session = (HttpSession) connexions.toArray()[i];
            InfosDeSession infos_session = new InfosDeSession(session);
            connexions_a_trier.add(infos_session);
        }

        Collection<InfosDeSession> connexions_triees = CrmUtilSession.trierSessions(
                connexions_a_trier, sens, col_de_tri);

        StringBuilder sb = new StringBuilder();
        sb.append("<table class='m_table' cellspacing='0' width='90%'>");
        sb.append("<tr>");
        sb.append("<td class='m_td_entete' height='26px' onClick=\"Javascript:trierSessionsPar('UTILISATEUR')\">Utilisateur <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' height='26px' onClick=\"Javascript:trierSessionsPar('ENTREPRISE')\">Entreprise <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('DATE_CONNEXION')\">Date de connexion <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('DUREE_CONNEXION')\">Dur&eacute;e de connexion <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('DATE_DERNIER_ACCES')\">Date de dernier acc&egrave;s <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('DUREE_INACTIVITE')\">Durée d'inactivit&eacute; <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('IP')\">I.P. <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("<td class='m_td_entete' onClick=\"Javascript:trierSessionsPar('INSTANCE')\">Instance <img src='img/SORT_WHITE.gif' border='0'/></td>");
        sb.append("</tr>");

        for (int i = 0; i < connexions_triees.size(); i++) {
            InfosDeSession infos_session = (InfosDeSession) connexions_triees
                    .toArray()[i];
            String utl_id = infos_session.getUTL_ID();

            sb.append("<tr class=\"m_tr_noir\" onClick=\"Javascript:getInfosPersonneAnnuaire('"
                    + utl_id
                    + "')\" onmouseover=\"this.className='m_tr_selected'\" onmouseout=\"this.className='m_tr_noir'\">");
            sb.append("<td class='m_td'>" + infos_session.getNomUser()
                    + "</td>");
            sb.append("<td class='m_td'>" + infos_session.getEntreprise()
                    + "</td>");
            sb.append("<td class='m_td'>"
                    + infos_session.getDateConnexionFormatee() + "</td>");
            sb.append("<td class='m_td'>"
                    + infos_session.getDureeConnexionFormatee() + "</td>");
            sb.append("<td class='m_td'>"
                    + infos_session.getDateDernierAccesFormatee() + "</td>");
            sb.append("<td class='m_td'>"
                    + infos_session.getDureeInactiviteFormatee() + "</td>");
            sb.append("<td class='m_td'>" + infos_session.getIP() + "</td>");
            sb.append("<td class='m_td' align='center'>"
                    + infos_session.getInstance() + "</td>");

            sb.append("</tr>");
        }

        sb.append("</table>@@@@@@" + connexions_triees.size());

        return sb;
    }

    public static StringBuilder getInfosPersonneAnnuaire(String utl_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");

        try {

            String requete = "SELECT DISTINCT cvl.cvl_lib, prs.prs_nom, prs.prs_prenom, srv.srv_lib, "
                    + "pol.pol_lib, prs_mor.prs_nom, vil.vil_lib, pem.pem_poste, fct.fct_lib, prs.PRS_MATRICULE "
                    + "FROM H_ANNUAIRE.T_PERSONNES_PRS prs, H_ANNUAIRE.T_UTILISATEURS_UTL utl, H_ANNUAIRE.T_PERSONNES_PRS prs_mor, "
                    + "H_ANNUAIRE.T_CIVILITES_CVL cvl, H_ANNUAIRE.T_SERVICES_SRV srv,  H_ANNUAIRE.T_PERSONNESSERVICESPOLES_PSP psp, "
                    + "H_ANNUAIRE.T_POLES_POL pol, H_ANNUAIRE.T_COORDONNEESPOSTALES_CPO cpo,  H_ANNUAIRE.T_PERSONNESCOORDPOST_PCP pcp, "
                    + "H_ANNUAIRE.T_PERSONNESEMPLOIS_PEM pem,  H_ANNUAIRE.T_VILLES_VIL vil, H_ANNUAIRE.T_PERSONNESFONCTIONS_PFC PFC, "
                    + "H_ANNUAIRE.T_FONCTIONS_FCT FCT  WHERE prs.prs_cvl_id = cvl.cvl_id(+)   AND prs.prs_id = psp.psp_prs_id(+) "
                    + "AND psp.psp_srv_id = srv.srv_id(+)  AND psp.psp_pol_id = pol.pol_id(+)  AND prs_mor.prs_id = pcp.pcp_prs_id(+) "
                    + "AND pcp.pcp_cpo_id = cpo.cpo_id(+)  AND cpo.cpo_vil_id = vil.vil_id(+) AND prs.prs_id = pfc.pfc_prs_id(+) "
                    + "AND pfc.PFC_FCT_ID = FCT.FCT_ID(+)  AND prs_mor.prs_id(+) = pem.pem_prs_id_employeur "
                    + "AND prs.prs_id = pem.pem_prs_id_employe(+) AND prs.prs_type = 'PHY'  "
                    + "AND utl.UTL_PRS_ID = prs.PRS_ID AND utl.UTL_ID = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, utl_id);

            sb.append("<table cellpadding='4' cellspacing='2' width='100%'>");
            sb.append("<tr><td class='noir11' colspan='2' align='right'><a href='Javascript:fermerBlockUI()'><img src='./img/icon_close_cross.gif' border='0'></td></tr>");
            rs = stmt.executeQuery();

            while (rs.next()) {

                // NOM_ENTREPRISE
                if (rs.getString(6) != null) {
                    sb.append("<tr><td class='noir11'>Entreprise</td><td class='bleu11'>"
                            + rs.getString(6) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Entreprise</td><td class='bleu11'>NA</td></tr>");
                }

                // CIVILITE
                if (rs.getString(1) != null) {
                    sb.append("<tr><td class='noir11'>Civilit&eacute;</td><td class='bleu11'>"
                            + rs.getString(1) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Civilit&eacute;</td><td class='bleu11'>NA</td></tr>");
                }

                // NOM
                if (rs.getString(2) != null) {
                    sb.append("<tr><td class='noir11'>Nom</td><td class='bleu11'>"
                            + rs.getString(2) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Nom</td><td class='bleu11'>NA</td></tr>");
                }

                // PRENOM
                if (rs.getString(3) != null) {
                    sb.append("<tr><td class='noir11'>Pr&eacute;nom</td><td class='bleu11'>"
                            + rs.getString(3) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Pr&eacute;nom</td><td class='bleu11'>NA</td></tr>");
                }

                // SERVICE
                if (rs.getString(4) != null) {
                    sb.append("<tr><td class='noir11'>Service</td><td class='bleu11'>"
                            + rs.getString(4) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Service</td><td class='bleu11'>NA</td></tr>");
                }

                // POLE
                if (rs.getString(5) != null) {
                    sb.append("<tr><td class='noir11'>P&ocirc;le</td><td class='bleu11'>"
                            + rs.getString(5) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>P&ocirc;le</td><td class='bleu11'>NA</td></tr>");
                }

                // VILLE
                if (rs.getString(7) != null) {
                    sb.append("<tr><td class='noir11'>Ville</td><td class='bleu11'>"
                            + rs.getString(7) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Ville</td><td class='bleu11'>NA</td></tr>");
                }

                // POSTE
                if (rs.getString(8) != null) {
                    sb.append("<tr><td class='noir11'>Poste</td><td class='bleu11'>"
                            + rs.getString(8) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Poste</td><td class='bleu11'>NA</td></tr>");
                }

                // FONCTION
                if (rs.getString(9) != null) {
                    sb.append("<tr><td class='noir11'>Fonction</td><td class='bleu11'>"
                            + rs.getString(9) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Fonction</td><td class='bleu11'>NA</td></tr>");
                }

                // MATRICULE
                if (rs.getString(10) != null) {
                    sb.append("<tr><td class='noir11'>Matricule</td><td class='bleu11'>"
                            + rs.getString(10) + "</td></tr>");
                } else {
                    sb.append("<tr><td class='noir11'>Matricule</td><td class='bleu11'>NA</td></tr>");
                }

            }
            sb.append("</table>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getInfosPersonneAnnuaire", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
 
    public static Collection<ReferenceStatistique> getReferencesStatistiques() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<ReferenceStatistique> res = new ArrayList<ReferenceStatistique>();

        try {
            ReferenceStatistique unDisplay = null;
            String requete = "SELECT rst.RST_ID, rst.RST_LIBELLE, rst.RST_ACTIF "
                    + "FROM EVENEMENT.T_REFS_STATS_RST rst " + "order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ReferenceStatistique();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setActif(rs.getString(3));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getReferencesStatistiques", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<ReferenceStatistique> getListeSousMotifsDisponibles() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<ReferenceStatistique> res = new ArrayList<ReferenceStatistique>();

        try {
            ReferenceStatistique unDisplay = null;
            String requete = "SELECT rst.RST_ID, rst.LIBELLE, rst.ACTIF, rst.ID "
                    + "FROM EVENEMENT.EVENEMENT_S_MOTIF rst WHERE ACTIF = 1 order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ReferenceStatistique();
                unDisplay.setId(rs.getString("ID"));
                unDisplay.setLibelle(rs.getString("LIBELLE"));
                unDisplay.setActif(rs.getString("ACTIF"));
                unDisplay.setRstId(rs.getString("RST_ID"));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getReferencesStatistiques", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Scenario getScenario(String idCampagne, String idMutuelle) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Scenario unDisplay = null;
            String requete = "SELECT ID, MUTUELLE_ID, CAMPAGNE_ID, LIBELLE, DISCOURS, CONSIGNES, REPONSE "
                    + "FROM HOTLINE.SCENARIO WHERE CAMPAGNE_ID = ? and MUTUELLE_ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idCampagne);
            stmt.setString(2, idMutuelle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Scenario();
                unDisplay.setID(rs.getString(1));
                unDisplay.setMUTUELLE_ID(rs.getString(2));
                unDisplay.setCAMPAGNE_ID(rs.getString(3));
                unDisplay.setLIBELLE(rs.getString(4));
                unDisplay.setDISCOURS(rs.getString(5));
                unDisplay.setCONSIGNES(rs.getString(6));
                unDisplay.setREPONSE(rs.getString(7));
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getScenario", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Mutuelle getMutuelleById(String idMutuelle) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Mutuelle unDisplay = null;
            String requete = "SELECT ID, LIBELLE, CODE FROM APPLICATION.MUTUELLE WHERE ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idMutuelle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Mutuelle();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCode((rs.getString(3) != null) ? rs.getString(3)
                        : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getMutuelleById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Mutuelle getDetailMutuelleById(String idMutuelle) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Mutuelle unDisplay = null;
            String requete = "SELECT ID, LIBELLE, CODE "
                    + "FROM APPLICATION.MUTUELLE WHERE ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idMutuelle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Mutuelle();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setLibelle((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCode((rs.getString(3) != null) ? rs.getString(3)
                        : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getMutuelleById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<InfosScenario> getScenariiModeleProcedure(
            ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<InfosScenario> res = new ArrayList<InfosScenario>();

        try {

            conn = getConnexion();

            String requete = "select s.id,C.LIBELLE,M.LIBELLE,S.LIBELLE "
                    + " from HOTLINE.RATTACHEMENT_PROCEDURE rp, "
                    + " 	HOTLINE.SCENARIO   s, " + " 	HOTLINE.CAMPAGNE   c, "
                    + " 	APPLICATION.MUTUELLE m " + "where "
                    + "        rp.MODPROCEDURE_ID = ? "
                    + "    and rp.SCENARIO_ID = s.ID "
                    + "    and s.CAMPAGNE_ID = c.ID "
                    + "    and s.MUTUELLE_ID = m.id ";

            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, Long.parseLong(leModeleProcedureMail.getId()));
            rs = stmt.executeQuery();
            InfosScenario infosScenario = null;
            while (rs.next()) {

                infosScenario = new InfosScenario();
                infosScenario.setId(rs.getString(1));
                infosScenario.setCampagneLib(rs.getString(2));
                infosScenario.setMutuelleLib(rs.getString(3));
                infosScenario.setLibelle(rs.getString(4));
                res.add(infosScenario);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("getScenariiModeleProcedure", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<InfosScenario> getScenariiModelePEC(
            ModelePEC unModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<InfosScenario> res = new ArrayList<InfosScenario>();

        try {

            conn = getConnexion();

            String requete = "select s.id,C.LIBELLE,M.LIBELLE,S.LIBELLE "
                    + "from HOTLINE.SCENARIO_EVENEMENT se, "
                    + "     HOTLINE.SCENARIO   s, "
                    + "     HOTLINE.CAMPAGNE   c, "
                    + "     APPLICATION.MUTUELLE m " + "where "
                    + "        se.MODELEPEC_ID = ? "
                    + "    and se.SCENARIO_ID = s.ID "
                    + "    and s.CAMPAGNE_ID = c.ID "
                    + "    and s.MUTUELLE_ID = m.id   ";

            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, Long.parseLong(unModelePEC.getId()));
            rs = stmt.executeQuery();
            InfosScenario infosScenario = null;
            while (rs.next()) {

                infosScenario = new InfosScenario();
                infosScenario.setId(rs.getString(1));
                infosScenario.setCampagneLib(rs.getString(2));
                infosScenario.setMutuelleLib(rs.getString(3));
                infosScenario.setLibelle(rs.getString(4));
                res.add(infosScenario);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("getScenariiModelePEC", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<ModelePEC> getModelesPEC() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<ModelePEC> res = new ArrayList<ModelePEC>();

        try {

            conn = getConnexion();

            String requete = "SELECT ID, LIBELLE " + "FROM HOTLINE.MODELE_PEC ";
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            ModelePEC leModelePEC = null;
            while (rs.next()) {

                leModelePEC = new ModelePEC();
                leModelePEC.setId(rs.getString(1));
                leModelePEC.setLibelle(rs.getString(2));
                res.add(leModelePEC);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("getModelesPEC", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<ModelePEC> getModelesPECByScenarioId(String idScenario) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            conn = getConnexion();

            String requete = "SELECT mp.id, mp.libelle, mp.OPERATEUR, mp.ORGANISME, "
                            + "      mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " 	 mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.SCENARIO_EVENEMENT se,"
                            + "     HOTLINE.MODELE_PEC mp "
                            + "WHERE se.SCENARIO_ID=? AND se.MODELEPEC_ID=mp.ID";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idScenario);
            rs = stmt.executeQuery();
            
            Collection<ModelePEC> modelesPEC = new ArrayList<ModelePEC>();
            
            ModelePEC leModelePEC = null;
            while (rs.next()) {
                leModelePEC = new ModelePEC();
                leModelePEC.setId(rs.getString(1));
                leModelePEC.setLibelle(rs.getString(2));
                leModelePEC.setOperateur(rs.getString(3));
                leModelePEC.setOrganisme(rs.getString(4));
                leModelePEC.setEmissionFax(rs.getBoolean(5));
                leModelePEC.setFax(rs.getString(6));
                leModelePEC.setEmissionCourriel(rs.getBoolean(7));
                leModelePEC.setCourriel(rs.getString(8));
                leModelePEC.setAppelantBeneficiairePermis(rs.getBoolean(9));
                leModelePEC.setAppelantFournisseurPermis(rs.getBoolean(10));
                
                modelesPEC.add(leModelePEC);
            }
            return modelesPEC;
        } catch (Exception e) {
            LOGGER.error("getModelePECByScenarioId", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean rattacherProcedureMail(Scenario leScenario,
            ModeleProcedureMail leModelProcedureMail, Object item) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            // Identidication du type de mouvement (Ajout ou mise à jour )
            String requete = "SELECT COUNT(*) "
                    + "FROM HOTLINE.RATTACHEMENT_PROCEDURE "
                    + "WHERE SCENARIO_ID  = ? AND MODPROCEDURE_ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(leModelProcedureMail.getId()));

            rs = stmt.executeQuery();

            if (rs.next()) {

                if (rs.getInt(1) == 0) {
                    // Ajout
                    requete = "INSERT INTO HOTLINE.RATTACHEMENT_PROCEDURE( "
                            + "		SCENARIO_ID," + "      MODPROCEDURE_ID,"
                            + "     MOTIF_ID," + "     S_MOTIF_ID,"
                            + "     POINT_ID," + "     S_POINT_ID )"
                            + "VALUES(?,?,?,?,?,?)";
                    stmt.close();
                    stmt = conn.prepareStatement(requete);

                    // Analyse du rattachement
                    stmt.setLong(1, Long.parseLong(leScenario.getID()));
                    stmt.setLong(2,
                            Long.parseLong(leModelProcedureMail.getId()));
                    if (item instanceof Motif) {
                        stmt.setLong(3, Long.parseLong(((Motif) item).getId()));
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof SousMotif) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setLong(4,
                                Long.parseLong(((SousMotif) item).getId()));
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof Point) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setLong(5, Long.parseLong(((Point) item).getId()));
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof SousPoint) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setLong(6,
                                Long.parseLong(((SousPoint) item).getId()));
                    }
                    stmt.executeQuery();
                    conn.commit();
                } else {
                    // Mise à jour
                    // Ajout
                    requete = "UPDATE HOTLINE.RATTACHEMENT_PROCEDURE "
                            + "SET 	MOTIF_ID=?," + "     S_MOTIF_ID=?,"
                            + "     POINT_ID=?," + "     S_POINT_ID=? "
                            + "WHERE SCENARIO_ID=? AND MODPROCEDURE_ID=?";
                    stmt.close();
                    stmt = conn.prepareStatement(requete);

                    // Analyse du rattachement
                    stmt.setLong(5, Long.parseLong(leScenario.getID()));
                    stmt.setLong(6,
                            Long.parseLong(leModelProcedureMail.getId()));
                    if (item instanceof Motif) {
                        stmt.setLong(1, Long.parseLong(((Motif) item).getId()));
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof SousMotif) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setLong(2,
                                Long.parseLong(((SousMotif) item).getId()));
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof Point) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setLong(3, Long.parseLong(((Point) item).getId()));
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof SousPoint) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setLong(4,
                                Long.parseLong(((SousPoint) item).getId()));
                    }
                    stmt.executeQuery();
                    conn.commit();
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("rattacherProcedureMail", e);
            return false;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static boolean supprimerRattachementPEC(Scenario leScenario, ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            // Identidication du type de mouvement (Ajout ou mise à jour )
            String requete = "DELETE FROM HOTLINE.RATTACHEMENT_PEC "
                    + "WHERE SCENARIO_ID  = ? AND MODELEPEC_ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, leScenario.getID());
            stmt.setString(2, leModelePEC.getId());
            stmt.executeQuery();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerRattachementPEC", e);
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean existsScenarioItemRattachementPEC(Scenario leScenario, Object item) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean res = false;
        
        try {
            conn = getConnexion();
            String requete = "";
            
            if (item instanceof Motif) {
                requete = "SELECT count(*) "
                        + "FROM HOTLINE.RATTACHEMENT_PEC "
                        + "WHERE SCENARIO_ID=? AND MOTIF_ID=? ";
                stmt = conn.prepareStatement(requete);
                stmt.setLong(1, Long.parseLong(leScenario.getID()));
                stmt.setLong(2, Long.parseLong(((Motif) item).getId()));
            } else if (item instanceof SousMotif) {
                requete = "SELECT count(*) "
                        + "FROM HOTLINE.RATTACHEMENT_PEC "
                        + "WHERE SCENARIO_ID=? AND S_MOTIF_ID=? ";
                stmt = conn.prepareStatement(requete);
                stmt.setLong(1, Long.parseLong(leScenario.getID()));
                stmt.setLong(2, Long.parseLong(((SousMotif) item).getId()));
            } else if (item instanceof Point) {
                requete = "SELECT count(*) "
                        + "FROM HOTLINE.RATTACHEMENT_PEC "
                        + "WHERE SCENARIO_ID=? AND POINT_ID=? ";
                stmt = conn.prepareStatement(requete);
                stmt.setLong(1, Long.parseLong(leScenario.getID()));
                stmt.setLong(2, Long.parseLong(((Point) item).getId()));
            } else if (item instanceof SousPoint) {
                requete = "SELECT count(*) "
                        + "FROM HOTLINE.RATTACHEMENT_PEC "
                        + "WHERE SCENARIO_ID=? AND S_POINT_ID=? ";
                stmt = conn.prepareStatement(requete);
                stmt.setLong(1, Long.parseLong(leScenario.getID()));
                stmt.setLong(2, Long.parseLong(((SousPoint) item).getId()));
            }
                        
            rs = stmt.executeQuery();
            if( rs.next()){
                int cpt = rs.getInt(1);
                if( cpt>0){
                    res = true;
                }
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("supprimerRattachementPEC", e);
            return false;
        } finally {
            closeRsStmtConn( rs, stmt, conn);
        }
    }
    
    public static boolean rattacherPEC(Scenario leScenario, ModelePEC leModelePEC, Object item) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            // Identidication du type de mouvement (Ajout ou mise à jour )
            String requete = "SELECT COUNT(*) "
                    + "FROM HOTLINE.RATTACHEMENT_PEC "
                    + "WHERE SCENARIO_ID  = ? AND MODELEPEC_ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, leScenario.getID());
            stmt.setString(2, leModelePEC.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {

                if (rs.getInt(1) == 0) {
                    // Ajout
                    requete = "INSERT INTO HOTLINE.RATTACHEMENT_PEC( SCENARIO_ID, MODELEPEC_ID, "
                            + " MOTIF_ID, S_MOTIF_ID, POINT_ID, S_POINT_ID )" 
                            + "VALUES(?,?,?,?,?,?)";
                    stmt.close();
                    stmt = conn.prepareStatement(requete);

                    // Analyse du rattachement
                    stmt.setLong(1, Long.parseLong(leScenario.getID()));
                    stmt.setLong(2, Long.parseLong(leModelePEC.getId()));
                    
                    if (item instanceof Motif) {
                        stmt.setLong(3, Long.parseLong(((Motif) item).getId()));
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof SousMotif) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setLong(4,
                                Long.parseLong(((SousMotif) item).getId()));
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof Point) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setLong(5, Long.parseLong(((Point) item).getId()));
                        stmt.setNull(6, Types.NUMERIC);
                    } else if (item instanceof SousPoint) {
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                        stmt.setNull(5, Types.NUMERIC);
                        stmt.setLong(6,
                                Long.parseLong(((SousPoint) item).getId()));
                    }
                    stmt.executeQuery();
                    conn.commit();
                } else {
                    requete = "UPDATE HOTLINE.RATTACHEMENT_PEC "
                            + "SET 	MOTIF_ID=?," + "     S_MOTIF_ID=?,"
                            + "     POINT_ID=?," + "     S_POINT_ID=? "
                            + "WHERE SCENARIO_ID=? AND MODELEPEC_ID=?";
                    stmt.close();
                    stmt = conn.prepareStatement(requete);

                    // Analyse du rattachement
                    stmt.setLong(5, Long.parseLong(leScenario.getID()));
                    stmt.setLong(6, Long.parseLong(leModelePEC.getId()));
                    if (item instanceof Motif) {
                        stmt.setLong(1, Long.parseLong(((Motif) item).getId()));
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof SousMotif) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setLong(2,
                                Long.parseLong(((SousMotif) item).getId()));
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof Point) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setLong(3, Long.parseLong(((Point) item).getId()));
                        stmt.setNull(4, Types.NUMERIC);
                    } else if (item instanceof SousPoint) {
                        stmt.setNull(1, Types.NUMERIC);
                        stmt.setNull(2, Types.NUMERIC);
                        stmt.setNull(3, Types.NUMERIC);
                        stmt.setLong(4,
                                Long.parseLong(((SousPoint) item).getId()));
                    }
                    stmt.executeQuery();
                    conn.commit();
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("rattacherPEC", e);
            return false;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static Object getRattachementProcedureMail(Scenario leScenario,
            ModeleProcedureMail laProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Object resultat = null;

        try {

            String requete = "SELECT MOTIF_ID, S_MOTIF_ID, POINT_ID, S_POINT_ID "
                    + "FROM HOTLINE.RATTACHEMENT_PROCEDURE "
                    + "WHERE SCENARIO_ID  = ? " + "AND MODPROCEDURE_ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(laProcedureMail.getId()));

            rs = stmt.executeQuery();
            if (rs.next()) {

                if (rs.getString(1) != null) {
                    Motif item = new Motif();
                    item.setId(rs.getString(1));
                    resultat = item;
                } else if (rs.getString(2) != null) {
                    SousMotif item = new SousMotif();
                    item.setId(rs.getString(2));
                    resultat = item;
                } else if (rs.getString(3) != null) {
                    Point item = new Point();
                    item.setId(rs.getString(3));
                    resultat = item;
                } else if (rs.getString(4) != null) {
                    SousPoint item = new SousPoint();
                    item.setId(rs.getString(4));
                    resultat = item;
                } else {
                    resultat = null;
                }
            } else {
                resultat = null;
            }
            stmt.clearParameters();
            return resultat;
        } catch (Exception e) {
            LOGGER.error("getRattachementProcedureMail", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Scenario getRattachementsPEC(Scenario unScenario) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String requete = "SELECT  rp.MODELEPEC_ID, rp.MOTIF_ID, rp.S_MOTIF_ID, rp.POINT_ID, rp.S_POINT_ID "
                    + "FROM HOTLINE.RATTACHEMENT_PEC rp "
                    + "WHERE SCENARIO_ID  = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, unScenario.getID());
            rs = stmt.executeQuery();
            
            ModelePEC unModelePEC = null;
            
            while(rs.next()) {

                unModelePEC = new ModelePEC();
                unModelePEC.setId(rs.getString(1));
                
                if (rs.getString(2) != null) {
                    Motif item = new Motif();
                    item.setId(rs.getString(2));
                    unScenario.addRattachement(unModelePEC, item);
                } else if (rs.getString(3) != null) {
                    SousMotif item = new SousMotif();
                    item.setId(rs.getString(3));
                    unScenario.addRattachement(unModelePEC, item);
                } else if (rs.getString(4) != null) {
                    Point item = new Point();
                    item.setId(rs.getString(4));
                    unScenario.addRattachement(unModelePEC, item);
                } else if (rs.getString(5) != null) {
                    SousPoint item = new SousPoint();
                    item.setId(rs.getString(5));
                    unScenario.addRattachement(unModelePEC, item);
                }
            }
            stmt.clearParameters();
            return unScenario;
        } catch (Exception e) {
            LOGGER.error("getRattachementPEC", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Scenario getScenarioById(String idScenario) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Scenario unDisplay = null;
            String requete = "SELECT DISTINCT ID, MUTUELLE_ID, CAMPAGNE_ID, LIBELLE, DISCOURS, CONSIGNES, REPONSE, se.SCENARIO_ID "
                    + "FROM HOTLINE.SCENARIO s "
                    + "LEFT OUTER JOIN HOTLINE.SCENARIO_EVENEMENT se ON s.ID = SE.SCENARIO_ID AND se.S_MOTIF_EVENEMENT_ID=? "
                    + "WHERE s.ID = ? " ;
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, _evenement_pec_id);
            stmt.setString(2, idScenario);
            rs = stmt.executeQuery();
            if (rs.next()) {
                unDisplay = new Scenario();
                unDisplay.setID((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setMUTUELLE_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCAMPAGNE_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setLIBELLE((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setDISCOURS((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCONSIGNES((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setREPONSE((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                
                unDisplay.setModelesPEC(new ArrayList<ModelePEC>());
                
                if( rs.getString(8) != null ){
                    
                    stmt.clearParameters();
                    requete = "SELECT mp.ID, mp.LIBELLE, mp.OPERATEUR, mp.ORGANISME, "
                            + " mp.ENVOI_FAX, mp.FAX, mp.ENVOIL_COURRIEL, mp.COURRIEL, "
                            + " mp.BENEFICIAIRE_PERMIS, mp.FOURNISSEUR_PERMIS "
                            + "FROM HOTLINE.SCENARIO_EVENEMENT se, "
                            + "     HOTLINE.MODELE_PEC mp "
                            + "WHERE se.SCENARIO_ID = ? "
                            + "     AND se.S_MOTIF_EVENEMENT_ID=?"
                            + "     AND mp.ID = se.MODELEPEC_ID";
                    stmt = conn.prepareStatement(requete);
                    stmt.setString(1, unDisplay.getID());
                    stmt.setString(2, _evenement_pec_id); 
                    rs = stmt.executeQuery();
                    
                    
                    
                    ModelePEC unModelePEC = null;
                    while (rs.next()) {
                        
                        unModelePEC = new ModelePEC();
                        unModelePEC.setId(rs.getString(1));
                        unModelePEC.setLibelle(rs.getString(2));
                        unModelePEC.setOperateur(rs.getString(3));
                        unModelePEC.setOrganisme(rs.getString(4));
                        unModelePEC.setEmissionFax(rs.getBoolean(5));
                        unModelePEC.setFax(rs.getString(6));
                        unModelePEC.setEmissionCourriel(rs.getBoolean(7));
                        unModelePEC.setCourriel(rs.getString(8));
                        unModelePEC.setAppelantBeneficiairePermis(rs.getBoolean(9));
                        unModelePEC.setAppelantFournisseurPermis(rs.getBoolean(10));
                        
                        unDisplay.getModelesPEC().add(unModelePEC);
                    }
                }
            }
            stmt.clearParameters();

            String requeteProcedure = "SELECT m.ID, m.LIBELLE, m.TYPE, "
                    + "m.MAIL_OBJET, m.MAIL_INVITE, m.MAIL_CORPS, m.MAIL_SIGNATURE, m.RECAP_ADH, m.RECAP_CENTREGESTION  "
                    + "FROM HOTLINE.RATTACHEMENT_PROCEDURE r, "
                    + "      HOTLINE.MODELE_PROCEDURE m "
                    + "WHERE r.SCENARIO_ID=? AND r.MODPROCEDURE_ID=m.ID";

            stmt = conn.prepareStatement(requeteProcedure);
            stmt.setString(1, idScenario);
            rs = stmt.executeQuery();

            Collection<ModeleProcedureMail> modeles = new ArrayList<ModeleProcedureMail>();
            ModeleProcedureMail unModeleProcedureMail = null;
            while (rs.next()) {
                unModeleProcedureMail = new ModeleProcedureMail();
                unModeleProcedureMail.setLibelle(rs.getString(1));
                unModeleProcedureMail.setType(rs.getString(2));
                unModeleProcedureMail.setMailObjet(rs.getString(3));
                unModeleProcedureMail.setMailInvite(rs.getString(4));
                unModeleProcedureMail.setMailCorps(rs.getString(5));
                unModeleProcedureMail.setMailSignature(rs.getString(6));
                modeles.add(unModeleProcedureMail);
            }
            unDisplay.setProceduresMail(modeles);

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getScenarioById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Motif> getScenarioMotifs(String idScenario) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Motif> res = new ArrayList<Motif>();

        try {

            Motif unDisplay = null;
            String requete = "SELECT moa.ID, moa.LIBELLE, moa.DISCOURS, moa.CONSIGNES, moa.REGIME_CODE, moa.ACTIF, moa.DECISIONNEL "
                    + "FROM HOTLINE.MOTIFAPPEL moa, HOTLINE.SCENARIO s "
                    + "WHERE s.ID = ? and moa.SCENARIO_ID = s.ID and moa.ACTIF = 1 order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idScenario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Motif();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setDISCOURS(rs.getString(3));
                unDisplay.setCONSIGNES(rs.getString(4));
                unDisplay.setREGIME_CODE(rs.getString(5));
                unDisplay.setACTIF(rs.getString(6));
                unDisplay.setDecisionnel(rs.getString(7));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getScenarioMotifs", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<SousMotif> getMotifSousMotifs(String idMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<SousMotif> res = new ArrayList<SousMotif>();

        try {

            SousMotif unDisplay = null;
            String requete = "SELECT sm.ID, sm.MOTIF_ID, sm.LIBELLE, sm.DISCOURS, sm.CONSIGNES, sm.REPONSE, "
                    + "sm.REGIME_CODE, sm.ACTIF, sm.MAIL_RESILIATION, sm.DATE_MAJ, sm.DECISIONNEL "
                    + "FROM HOTLINE.SMOTIFAPPEL sm "
                    + "WHERE sm.MOTIF_ID = ? and sm.ACTIF = 1 order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idMotif);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new SousMotif();
                unDisplay.setId((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setMOTIF_ID((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDISCOURS((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCONSIGNES((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setREPONSE((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setREGIME_CODE((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setACTIF((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setDecisionnel((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getMotifSousMotifs", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Point> getSousMotifPoints(String idSousMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Point> res = new ArrayList<Point>();

        try {

            Point unDisplay = null;
            String requete = "SELECT p.ID, p.S_MOTIF_ID, p.LIBELLE, p.DISCOURS, p.CONSIGNES, p.REPONSE, p.REGIME_CODE, p.ACTIF, p.DECISIONNEL "
                    + "FROM HOTLINE.POINT p WHERE p.S_MOTIF_ID = ? and p.ACTIF = 1 order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idSousMotif);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Point();
                unDisplay.setId(rs.getString(1));
                unDisplay.setSousMotifId(rs.getString(2));
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDISCOURS((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCONSIGNES((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setREPONSE((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setREGIME_CODE((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setACTIF((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setDecisionnel((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getSousMotifPoints", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<SousPoint> getPointSousPoints(String idPoint) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<SousPoint> res = new ArrayList<SousPoint>();

        try {

            SousPoint unDisplay = null;
            String requete = "SELECT sp.ID, sp.POINT_ID, sp.LIBELLE, sp.DISCOURS, sp.CONSIGNES, sp.REPONSE, sp.REGIME_CODE, sp.ACTIF, sp.DECISIONNEL "
                    + "FROM HOTLINE.SPOINT sp WHERE sp.POINT_ID = ? and sp.ACTIF = 1 order by 3 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPoint);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new SousPoint();
                unDisplay.setId(rs.getString(1));
                unDisplay.setPointId(rs.getString(2));
                unDisplay.setLibelle((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDISCOURS((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setCONSIGNES((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setREPONSE((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setREGIME_CODE((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setACTIF((rs.getString(8) != null) ? rs.getString(8)
                        : "");
                unDisplay.setDecisionnel((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getPointSousPoints", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static void modifierConsignesDiscours(String niveau,
            String idNiveau, String nouvellesConsignes, String nouveauDiscours) {

        Connection conn = null;
        PreparedStatement stmt = null;

        String requeteUpdate = "";
        String table = "";
        try {
            conn = getConnexion();
            if ("SCENARIO".equals(niveau)) {
                table = "HOTLINE.SCENARIO";
            } else if ("MOTIF".equals(niveau)) {
                table = "HOTLINE.MOTIFAPPEL";
            } else if ("SOUSMOTIF".equals(niveau)) {
                table = "HOTLINE.SMOTIFAPPEL";
            } else if ("POINT".equals(niveau)) {
                table = "HOTLINE.POINT";
            } else if ("SOUSPOINT".equals(niveau)) {
                table = "HOTLINE.SPOINT";
            }
            requeteUpdate = "UPDATE " + table
                    + " set CONSIGNES = ?, DISCOURS = ? WHERE ID = ?";

            stmt = conn.prepareStatement(requeteUpdate);
            stmt.setString(1, nouvellesConsignes);
            stmt.setString(2, nouveauDiscours);
            stmt.setString(3, idNiveau);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
        } catch (Exception e) {
            LOGGER.error("modifierConsignesDiscours", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierLibelle(String niveau, String idNiveau,
            String nouveauLibelle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteUpdate = "";
        String table = "";
        try {
            conn = getConnexion();
            if ("MOTIF".equals(niveau)) {
                table = "HOTLINE.MOTIFAPPEL";
            } else if ("SOUSMOTIF".equals(niveau)) {
                table = "HOTLINE.SMOTIFAPPEL";
            } else if ("POINT".equals(niveau)) {
                table = "HOTLINE.POINT";
            } else if ("SOUSPOINT".equals(niveau)) {
                table = "HOTLINE.SPOINT";
            }
            requeteUpdate = "UPDATE " + table + " set LIBELLE = ? WHERE ID = ?";
            stmt = conn.prepareStatement(requeteUpdate);
            stmt.setString(1, nouveauLibelle);
            stmt.setString(2, idNiveau);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("modifierLibelle", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder supprimerMotif(String idMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteDelete = "";
        String requeteUpdate = "";
        StringBuilder sb = new StringBuilder();

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // On tente la suppression directe
            requeteDelete = "DELETE FROM HOTLINE.MOTIFAPPEL ma WHERE ma.ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, idMotif);
            stmt.execute();
            stmt.clearParameters();
            conn.commit();
            sb.append(_VRAI);
            return sb;
        } catch (SQLException sqle) {
            LOGGER.info("supprimerMotif- delete",sqle);
            try {
                // MOTIF
                requeteUpdate = "UPDATE HOTLINE.MOTIFAPPEL ma set ma.ACTIF = 0, ma.LIBELLE = ma.LIBELLE || '#' || ma.ID WHERE ma.ID = ?";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // SOUSMOTIF
                requeteUpdate = "UPDATE HOTLINE.SMOTIFAPPEL sma set sma.ACTIF = 0, sma.LIBELLE = sma.LIBELLE || '#' || sma.ID WHERE sma.ID in "
                        + "( select sma.ID from hotline.smotifappel sma where sma.motif_id = ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // POINT
                requeteUpdate = "UPDATE HOTLINE.POINT p set p.ACTIF = 0, p.LIBELLE = p.LIBELLE || '#' || p.ID WHERE p.ID in "
                        + "( select p.ID from hotline.motifappel ma, hotline.smotifappel sma, hotline.point p "
                        + "  where p.S_MOTIF_ID = sma.id and sma.motif_id = ma.id and ma.id = ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // SOUSPOINT
                requeteUpdate = "UPDATE HOTLINE.SPOINT sp set sp.ACTIF = 0, sp.LIBELLE = sp.LIBELLE || '#' || sp.ID  WHERE sp.ID in "
                        + "( select sp.ID from hotline.motifappel ma, hotline.smotifappel sma, hotline.point p,  hotline.spoint sp "
                        + "  where sp.POINT_ID = p.ID and p.S_MOTIF_ID = sma.id and sma.motif_id = ma.id and ma.id = ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // TOUT OK
                conn.commit();
                sb.append(_VRAI);
                return sb;
            } catch (Exception e) {
                LOGGER.error("supprimerMotif- update", e);
                try {
                    conn.rollback();
                } catch (Exception sqlee) {
                    LOGGER.error(_AnoRollBack, sqlee);
                }
                sb.append("0|");
                sb.append(e.getMessage());
                return sb;
            }
        } catch (Exception e) {
            LOGGER.error("supprimerMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            sb.append("0|");
            sb.append(e.getMessage());
            return sb;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder supprimerSousMotif(String idSousMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteDelete = "";
        String requeteUpdate = "";

        StringBuilder sb = new StringBuilder();

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            // On tente la suppression directe
            requeteDelete = "DELETE FROM HOTLINE.SMOTIFAPPEL sm WHERE sm.ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, idSousMotif);
            stmt.execute();
            stmt.clearParameters();
            conn.commit();
            sb.append(_VRAI);
            return sb;
        } catch (SQLException sqle) {
            LOGGER.info("supprimerSousMotif - Delete",sqle);
            try {
                // SOUSMOTIF
                requeteUpdate = "UPDATE HOTLINE.SMOTIFAPPEL sma set sma.ACTIF = 0, sma.LIBELLE = sma.LIBELLE || '#' || sma.ID WHERE sma.ID = ?";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idSousMotif);
                stmt.execute();
                stmt.clearParameters();
                conn.commit();
                stmt.close();

                // POINT
                requeteUpdate = "UPDATE HOTLINE.POINT p set p.ACTIF = 0, p.LIBELLE = p.LIBELLE || '#' || p.ID WHERE p.ID in "
                        + "( select p.ID from hotline.point p where p.S_MOTIF_ID =  ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idSousMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // SOUSPOINT
                requeteUpdate = "UPDATE HOTLINE.SPOINT sp set sp.ACTIF = 0, sp.LIBELLE = sp.LIBELLE || '#' || sp.ID WHERE sp.ID in "
                        + "( select sp.ID from hotline.smotifappel sma, hotline.point p,  hotline.spoint sp "
                        + "  where sp.POINT_ID = p.ID and p.S_MOTIF_ID = sma.id and sma.id = ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idSousMotif);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // TOUT OK
                conn.commit();
                sb.append(_VRAI);
                return sb;

            } catch (Exception e) {
                LOGGER.error("supprimerSousMotif - update", e);
                try {
                    conn.rollback();
                } catch (Exception sqlee) {
                    LOGGER.error(_AnoRollBack, sqlee);
                }
                sb.append("0|");
                sb.append(e.getMessage());
                return sb;
            }
        } catch (Exception e) {
            LOGGER.error("supprimerSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            sb.append("0|");
            sb.append(e.getMessage());
            return sb;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder supprimerPoint(String idPoint) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteDelete = "";
        String requeteUpdate = "";
        StringBuilder sb = new StringBuilder();

        try {

            conn = getConnexion();
            conn.setAutoCommit(false);

            // On tente la suppression directe
            requeteDelete = "DELETE FROM HOTLINE.POINT p WHERE p.ID = ?";
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, idPoint);
            stmt.execute();
            stmt.clearParameters();
            conn.commit();
            sb.append(_VRAI);
            return sb;
        } catch (SQLException sqle) {
            LOGGER.info("supprimerPoint - Delete ", sqle);
            try {
                // POINT
                requeteUpdate = "UPDATE HOTLINE.POINT p set p.ACTIF = 0, p.LIBELLE = p.LIBELLE || '#' || p.ID WHERE p.ID = ?";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idPoint);
                stmt.execute();
                stmt.clearParameters();

                // SOUSPOINT
                requeteUpdate = "UPDATE HOTLINE.SPOINT sp set sp.ACTIF = 0, sp.LIBELLE = sp.LIBELLE || '#' || sp.ID WHERE sp.ID in "
                        + "( select sp.ID from hotline.spoint sp where sp.point_id = ?  ) ";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idPoint);
                stmt.execute();
                stmt.clearParameters();
                stmt.close();

                // TOUT OK
                conn.commit();
                sb.append(_VRAI);
                return sb;

            } catch (Exception e) {
                LOGGER.error("supprimerPoint - update", e);
                try {
                    conn.rollback();
                } catch (Exception sqlee) {
                    LOGGER.error(_AnoRollBack, sqlee);
                }
                sb.append("0|");
                sb.append(e.getMessage());
                return sb;
            }
        } catch (Exception e) {
            LOGGER.error("supprimerPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            sb.append("0|");
            sb.append(e.getMessage());
            return sb;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder supprimerSousPoint(String idSousPoint) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteDelete = "";
        String requeteUpdate = "";

        StringBuilder sb = new StringBuilder();

        try {
            requeteDelete = "DELETE FROM HOTLINE.SPOINT sp WHERE sp.ID = ?";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requeteDelete);
            stmt.setString(1, idSousPoint);
            stmt.execute();
            stmt.clearParameters();
            conn.commit();
            sb.append(_VRAI);
            return sb;
        } catch (SQLException sqle) {
            LOGGER.info("supprimerSousPoint - Delete",sqle);
            try {
                requeteUpdate = "UPDATE HOTLINE.SPOINT sp set sp.ACTIF = 0, sp.LIBELLE = sp.LIBELLE || '#' || sp.ID WHERE sp.ID = ?";
                stmt = conn.prepareStatement(requeteUpdate);
                stmt.setString(1, idSousPoint);
                stmt.execute();
                stmt.clearParameters();

                conn.commit();
                sb.append(_VRAI);
                return sb;

            } catch (SQLException e) {
                LOGGER.error("supprimerSousPoint - update ", e);
                try {
                    conn.rollback();
                } catch (Exception sqlee) {
                    LOGGER.error(_AnoRollBack, sqlee);
                }
                sb.append("0|");
                sb.append(e.getMessage());
                return sb;
            }
        } catch (Exception e) {
            LOGGER.error("supprimerSousPointsupprimerSousPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            sb.append("0|");
            sb.append(e.getMessage());
            return sb;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static void appliquerMailResiliationSousMotif(String idSousMotif,
            String mailResiliation, boolean propager) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String requete = "";

        try {
            conn = getConnexion();

            requete = "UPDATE HOTLINE.SMOTIFAPPEL sma SET sma.MAIL_RESILIATION = ? WHERE sma.ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, mailResiliation);
            stmt.setString(2, idSousMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();

            if (propager) {
                CallableStatement cs = conn
                        .prepareCall("call hotline.PS_PropageMailResSMotif(?)");
                cs.setString(1, idSousMotif);
                cs.executeUpdate();
                cs.close();
            }
        } catch (Exception e) {
            LOGGER.error("appliquerMailResiliationSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static void appliquerMailResiliationPoint(String idPoint,
            String mailResiliation, boolean propager) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requete = "";

        try {
            conn = getConnexion();

            requete = "UPDATE HOTLINE.POINT p SET p.MAIL_RESILIATION = ? WHERE p.ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, mailResiliation);
            stmt.setString(2, idPoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();

            if (propager) {
                CallableStatement cs = conn
                        .prepareCall("call hotline.PS_PropageMailResPoint(?)");
                cs.setString(1, idPoint);
                cs.executeUpdate();
                cs.close();
            }
        } catch (Exception e) {
            LOGGER.error("appliquerMailResiliationPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static void appliquerMailResiliationSousPoint(String idSousPoint,
            String mailResiliation) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String requete = "";

        try {
            conn = getConnexion();

            requete = "UPDATE HOTLINE.SPOINT sp SET sp.MAIL_RESILIATION = ? WHERE sp.ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, mailResiliation);
            stmt.setString(2, idSousPoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
        } catch (Exception e) {
            LOGGER.error("appliquerMailResiliationSousPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static void appliquerFluxTransfertClient(String idSousMotif,
            String flux_transfert_client) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String requete = "";

        try {
            conn = getConnexion();

            requete = "UPDATE HOTLINE.SMOTIFAPPEL sm SET sm.flux_transfert_client  = ? WHERE sm.ID = ?";
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, flux_transfert_client);
            stmt.setString(2, idSousMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
        } catch (Exception e) {
            LOGGER.error("appliquerFluxTransfertClient", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static void propagerRegimeScenario(String idScenario,
            String codeRegime) {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getConnexion();
            cs = conn.prepareCall("call hotline.PROPAGEREGIMESCENARIO(?,?)");
            cs.setString(1, idScenario);
            if (codeRegime != null) {
                cs.setString(2, codeRegime);
            } else {
                cs.setLong(2, -1);
            }
            cs.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("propagerRegimeScenario", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
        } finally {
            try {
                cs.close();
                conn.close();
            } catch (Exception e) {
                LOGGER.error(_AnoLiberationConn, e);
            }
        }
    }
   
    public static Collection<Document> getDocuments(){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<Document> res = new ArrayList<Document>();

        try {
            String requete = "SELECT DOC_ID, DOC_LIBELLE, DOC_TYPE, DOC_DESC, DOC_FICHIER, DOC_DEBUT, DOC_FIN "
                    + "FROM HOTLINE.T_DOCUMENT_DOC";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Document document = new Document();
                document.setId(rs.getString(1));
                document.setLibelle(rs.getString(2));
                document.setType(rs.getString(3));
                document.setDescription(rs.getString(4));
                document.setDebut(rs.getDate(6));
                document.setFin(rs.getDate(7));
                res.add(document);
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("getDocuments", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Document getDocument(String documentId){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Document leDocument = new Document();

        try {
            String requete = "SELECT DOC_ID, DOC_LIBELLE, DOC_TYPE, DOC_DESC, DOC_FICHIER, DOC_DEBUT, DOC_FIN, DOC_CONTENU "
                    + "FROM HOTLINE.T_DOCUMENT_DOC "
                    + "WHERE DOC_ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, documentId);

            rs = stmt.executeQuery();

            if(rs.next()) {
                leDocument.setId(rs.getString(1));
                leDocument.setLibelle(rs.getString(2));
                leDocument.setType(rs.getString(3));
                leDocument.setDescription(rs.getString(4));
                leDocument.setNomFichier(rs.getString(5));
                leDocument.setDebut(rs.getDate(6));
                leDocument.setFin(rs.getDate(7));
                leDocument.setContenu(rs.getBytes(8));
            }
            else{
                return null;
            }
            return leDocument;
            
        } catch (Exception e) {
            LOGGER.error("getDocument", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<ModeleProcedureMail> getDocumentProcedures(Document document){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<ModeleProcedureMail> results = new ArrayList<ModeleProcedureMail>();
        
        try {
            String requete = "SELECT mp.ID, MP.LIBELLE "
                    + "FROM HOTLINE.T_DOCUMENT_MODELEPROC_DMP dmp, "
                    + "     HOTLINE.MODELE_PROCEDURE mp "
                    + "WHERE dmp.DMP_DOC_ID=? "
                    + "     AND mp.ID=dmp.DMP_MODPROC_ID";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, document.getId());

            rs = stmt.executeQuery();

            while(rs.next()) {
                ModeleProcedureMail leModel = new ModeleProcedureMail();
                leModel.setId(rs.getString(1));
                leModel.setLibelle(rs.getString(2));
                results.add(leModel);
            }
            
            return results;
            
        } catch (Exception e) {
            LOGGER.error("getDocumentProcedures", e);
            results.clear();
            return results;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static boolean ajouterDocument( Document leDocument ) {

        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {

            conn = getConnexion();
            conn.setAutoCommit(false);
            
            String currentId = "";
            Statement stmt_currentId = conn.createStatement();
            ResultSet rs_currId =  stmt_currentId.executeQuery("SELECT HOTLINE.SEQ_ID_DOC.nextVal FROM DUAL");                
            rs_currId.next();
            currentId = rs_currId.getString(1);
            
            String requeteInsert = "INSERT INTO HOTLINE.T_DOCUMENT_DOC( DOC_ID, DOC_LIBELLE, DOC_TYPE, DOC_DESC, DOC_FICHIER, DOC_DEBUT, DOC_FIN, DOC_CONTENU ) VALUES( ?, ?, ?, ?, ?, ?, ?, EMPTY_BLOB() )";
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setString(1, currentId);
            stmt.setString(2, leDocument.getLibelle());
            stmt.setString(3, leDocument.getType());
            stmt.setString(4, leDocument.getDescription());
            stmt.setString(5, leDocument.getFichier().getFileName());
            stmt.setDate(6, new java.sql.Date(leDocument.getDebut().getTime()));
            if( leDocument.getFin()!=null ){
                stmt.setDate(7, new java.sql.Date(leDocument.getFin().getTime()));
            }
            else{
                stmt.setNull(7, java.sql.Types.DATE);
            }
            stmt.execute();
            stmt.close();  
            
            String requeteGetBLOB = "SELECT DOC_CONTENU FROM HOTLINE.T_DOCUMENT_DOC " +
                    "WHERE DOC_ID = ? " ;
            stmt = conn.prepareStatement(requeteGetBLOB);
            stmt.setString(1, currentId);
            ResultSet rs_blob = stmt.executeQuery();
            rs_blob.next();
            Blob blob = rs_blob.getBlob(1);
        
            //Ecriture dans le blob
            InputStream is = leDocument.getFichier().getInputStream();
            ecrireFluxDansBlob(is, blob);

            stmt.close();
            
            stmt = conn.prepareStatement("UPDATE HOTLINE.T_DOCUMENT_DOC SET (DOC_CONTENU) = ?  WHERE DOC_ID =  ? ");
            stmt.setBlob(1, blob);
            stmt.setString(2, currentId);
            
            stmt.executeUpdate();       
            
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterDocument", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    private static void ecrireFluxDansBlob(InputStream in, Blob blob) throws Exception {
    	
        BufferedInputStream bf = new BufferedInputStream(in);        
        OutputStream outstream = blob.setBinaryStream(1L);
        BufferedOutputStream out = new BufferedOutputStream(outstream);
        int data = -1;
        while ((data = bf.read()) != -1) {
            out.write(data);
        }
        bf.close();
        out.close();
    }
    
    public static boolean modifierDocument( Document leDocument ) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteUpdate = "UPDATE HOTLINE.T_DOCUMENT_DOC SET DOC_LIBELLE=?, DOC_TYPE=?, DOC_DESC=?, DOC_FICHIER=?, DOC_DEBUT=?, DOC_FIN=? "
                    + "WHERE DOC_ID=?";

            conn = getConnexion();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(requeteUpdate);

            stmt.setString(1, leDocument.getLibelle());
            stmt.setString(2, leDocument.getType());
            stmt.setString(3, leDocument.getDescription());
            stmt.setString(4, leDocument.getNomFichier());
            stmt.setDate(5, new java.sql.Date(leDocument.getDebut().getTime()));
            if( leDocument.getFin()!=null ){
                stmt.setDate(6, new java.sql.Date(leDocument.getFin().getTime()));
            }
            else{
                stmt.setNull(6, java.sql.Types.DATE);
            }
            stmt.setString(7, leDocument.getId());
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();
            
            String requeteGetBLOB = "SELECT DOC_CONTENU FROM HOTLINE.T_DOCUMENT_DOC " +
                    "WHERE DOC_ID = ? " ;
            stmt = conn.prepareStatement(requeteGetBLOB);
            stmt.setString(1, leDocument.getId());
            ResultSet rs_blob = stmt.executeQuery();
            rs_blob.next();
            Blob blob = rs_blob.getBlob(1);
            stmt.close();
        
            //Ecriture dans le blob
            InputStream is = leDocument.getFichier().getInputStream();       
            ecrireFluxDansBlob(is, blob);
            
            stmt = conn.prepareStatement("UPDATE HOTLINE.T_DOCUMENT_DOC SET (DOC_CONTENU) = ?  WHERE DOC_ID =  ? ");
            stmt.setBlob(1, blob);
            stmt.setString(2, leDocument.getId());
            
            stmt.executeUpdate();       
            
            conn.commit();
            
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierDocument", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static boolean supprimerDocument( String leDocumentId ) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteDelete = "DELETE FROM HOTLINE.T_DOCUMENT_DOC WHERE DOC_ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteDelete);

            stmt.setString(1, leDocumentId );
            
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerDocument", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static AdresseGestion getAdresseGestionScenario(Scenario unScenario ){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        AdresseGestion adresse=null;
        
        try {
            
            String requete = "SELECT ACG_ID, ACG_SCE_ID, ACG_LIBELLE, ACG_ADRESSE, ACG_TEL, ACG_FAX, ACG_MAIL "
                    + "FROM HOTLINE.T_ADRESSECENTREGESTION_ACG "
                    + "WHERE ACG_SCE_ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            
            stmt.setLong(1,Long.parseLong(unScenario.getID()));
            rs = stmt.executeQuery();

            if(rs.next()) {
                adresse = new AdresseGestion();
                adresse.setID(rs.getString(1));
                adresse.setSCENARIO_ID(rs.getString(2));
                adresse.setLIBELLE(rs.getString(3));
                adresse.setADRESSE(rs.getString(4));
                adresse.setTELEPHONE(rs.getString(5));
                adresse.setTELECOPIE(rs.getString(6));
                adresse.setCOURRIEL(rs.getString(7));
            }

            return adresse;
            
        } catch (Exception e) {
            LOGGER.error("getAdresseGestion", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<ModeleProcedureMail> getProceduresAdresseGestionScenario(Scenario unScenario ){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        Collection<ModeleProcedureMail> modelesAdresse = new ArrayList();
        
        try {
            
            String requete = "SELECT DISTINCT MP.ID,MP.LIBELLE " 
                            +"FROM HOTLINE.T_ADRESSECENTREGESTION_ACG A, "
                            +"     HOTLINE.RATTACHEMENT_PROCEDURE     RM, "
                            +"     HOTLINE.MODELE_PROCEDURE           MP "
                            +"WHERE   A.ACG_SCE_ID = ?  "
                            +"    AND A.ACG_SCE_ID = RM.SCENARIO_ID  "
                            +"    AND MP.ID = RM.MODPROCEDURE_ID  "
                            +"    AND MP.RECAP_CENTREGESTION = 1  ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            
            stmt.setLong(1,Long.parseLong(unScenario.getID()));
            rs = stmt.executeQuery();

            while(rs.next()) {
                ModeleProcedureMail unDisplay = new ModeleProcedureMail();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                modelesAdresse.add(unDisplay);
            }

            return modelesAdresse;
            
        } catch (Exception e) {
            LOGGER.error("getProceduresAdresseGestionScenario", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static boolean ajouterAdresseGestion(
            AdresseGestion uneAdresse) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteInsert = "INSERT INTO HOTLINE.T_ADRESSECENTREGESTION_ACG(  ACG_ID, ACG_SCE_ID, ACG_LIBELLE, ACG_ADRESSE, ACG_TEL, ACG_FAX, ACG_MAIL) "
                    + "VALUES( HOTLINE.SEQ_ID_ACG.NEXTVAL,?,?,?,?,?,?)";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setString(1, uneAdresse.getSCENARIO_ID());
            stmt.setString(2, uneAdresse.getLIBELLE());
            stmt.setString(3, uneAdresse.getADRESSE());
            stmt.setString(4, uneAdresse.getTELEPHONE());
            stmt.setString(5, uneAdresse.getTELECOPIE());
            stmt.setString(6, uneAdresse.getCOURRIEL());
            
            stmt.executeQuery();
            conn.commit();
            
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterAdresseGestion", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static boolean modifierAdresseGestion(
            AdresseGestion uneAdresse) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteUpate = "UPDATE HOTLINE.T_ADRESSECENTREGESTION_ACG SET "
                    + "ACG_LIBELLE=?, ACG_ADRESSE=?, ACG_TEL=?, ACG_FAX=?, ACG_MAIL=? "
                    + "WHERE ACG_ID=?";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requeteUpate);

            stmt.setString(1, uneAdresse.getLIBELLE());
            stmt.setString(2, uneAdresse.getADRESSE());
            stmt.setString(3, uneAdresse.getTELEPHONE());
            stmt.setString(4, uneAdresse.getTELECOPIE());
            stmt.setString(5, uneAdresse.getCOURRIEL());
            
            stmt.setString(6, uneAdresse.getID());
            
            stmt.executeQuery();
            conn.commit();
            
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierAdresseGestion", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static boolean supprimerAdresseGestion(
            AdresseGestion uneAdresse) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteDelete = "DELETE FROM HOTLINE.T_ADRESSECENTREGESTION_ACG "
                                 + "WHERE ACG_ID=?";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement( requeteDelete );
            stmt.setString(1, uneAdresse.getID());
            
            stmt.executeQuery();
            conn.commit();
            
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerAdresseGestion", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static Collection<ModeleProcedureMail> getModelesProcedureMail(){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Collection<ModeleProcedureMail> res = new ArrayList<ModeleProcedureMail>();

        try {
            String requete = "SELECT ID, LIBELLE, TYPE, MAIL_OBJET, MAIL_INVITE, MAIL_CORPS, MAIL_INVITE, RECAP_ADH, RECAP_CENTREGESTION "
                    + "FROM HOTLINE.MODELE_PROCEDURE";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            while (rs.next()) {
                ModeleProcedureMail modele = new ModeleProcedureMail();
                modele.setId(rs.getString(1));
                modele.setLibelle(rs.getString(2));
                modele.setType(rs.getString(3));
                modele.setMailObjet(rs.getString(4));
                modele.setMailInvite(rs.getString(5));
                modele.setMailCorps(rs.getString(6));
                modele.setMailSignature(rs.getString(7));
                modele.setRecapAdherent(rs.getBoolean(8));
                modele.setRecapCentreGestion(rs.getBoolean(9));
                res.add(modele);
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("getModelesProcedureMail", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    
    public static boolean ajouterModeleProcedureMail(
            ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteInsert = "INSERT INTO HOTLINE.MODELE_PROCEDURE( LIBELLE, TYPE, MAIL_OBJET, MAIL_INVITE, MAIL_CORPS, MAIL_SIGNATURE, TYPE_DESTINATAIRE, RECAP_ADH, RECAP_CENTREGESTION ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setString(1, leModeleProcedureMail.getLibelle());
            stmt.setString(2, leModeleProcedureMail.getType());
            stmt.setString(3, leModeleProcedureMail.getMailObjet());
            stmt.setString(4, leModeleProcedureMail.getMailInvite());
            stmt.setString(5, leModeleProcedureMail.getMailCorps());
            stmt.setString(6, leModeleProcedureMail.getMailSignature());
            stmt.setString(7, leModeleProcedureMail.getTypeDestinataire());
            stmt.setBoolean(8, leModeleProcedureMail.isRecapAdherent());
            stmt.setBoolean(9, leModeleProcedureMail.isRecapCentreGestion());
            
            stmt.executeQuery();
            stmt.clearParameters();
            stmt.close();
            
            if( leModeleProcedureMail.getDocument()!= null ){
                
                requeteInsert = "INSERT INTO HOTLINE.T_DOCUMENT_MODELEPROC_DMP(DMP_MODPROC_ID, DMP_DOC_ID ) VALUES( hotline.seq_id_modprocedure.CURRVAL, ? )";
                stmt = conn.prepareStatement(requeteInsert);
                stmt.setString(1, leModeleProcedureMail.getDocument().getId());
                stmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterModeleProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierModeleProcedureMail(
            ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteUpdate = "UPDATE HOTLINE.MODELE_PROCEDURE "
                    + "SET LIBELLE=?, TYPE=?, MAIL_OBJET=?, MAIL_INVITE=?, MAIL_CORPS=?, MAIL_SIGNATURE=?, TYPE_DESTINATAIRE=?, RECAP_ADH=?, RECAP_CENTREGESTION=? "
                    + "WHERE ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteUpdate);

            stmt.setString(1, leModeleProcedureMail.getLibelle());
            stmt.setString(2, leModeleProcedureMail.getType());
            stmt.setString(3, leModeleProcedureMail.getMailObjet());
            stmt.setString(4, leModeleProcedureMail.getMailInvite());
            stmt.setString(5, leModeleProcedureMail.getMailCorps());
            stmt.setString(6, leModeleProcedureMail.getMailSignature());
            stmt.setString(7, leModeleProcedureMail.getTypeDestinataire());
            stmt.setBoolean(8, leModeleProcedureMail.isRecapAdherent());
            stmt.setBoolean(9, leModeleProcedureMail.isRecapCentreGestion());        

            stmt.setLong(10, Long.parseLong(leModeleProcedureMail.getId()));

            stmt.executeUpdate();
            stmt.clearParameters();
            stmt.close();
            
            requeteUpdate = "DELETE FROM HOTLINE.T_DOCUMENT_MODELEPROC_DMP WHERE DMP_MODPROC_ID=?";
            stmt = conn.prepareStatement(requeteUpdate);
            stmt.setString(1, leModeleProcedureMail.getId());
            stmt.executeUpdate();
            
            if( leModeleProcedureMail.getDocument()!= null ){
                
                String requeteInsert = "INSERT INTO HOTLINE.T_DOCUMENT_MODELEPROC_DMP(DMP_DOC_ID, DMP_MODPROC_ID) VALUES(?,?)";
                stmt = conn.prepareStatement(requeteInsert);
                stmt.setString(1, leModeleProcedureMail.getDocument().getId());
                stmt.setString(2, leModeleProcedureMail.getId());
                stmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierModeleProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerRattachementProcedureMail(
            Scenario leScenario, ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteDelete = "DELETE FROM HOTLINE.RATTACHEMENT_PROCEDURE "
                    + "WHERE SCENARIO_ID = ? AND MODPROCEDURE_ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteDelete);

            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(leModeleProcedureMail.getId()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerRattachementProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerModeleProcedureMail(
            ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            String requeteDelete = "DELETE FROM HOTLINE.MODELE_PROCEDURE "
                    + "WHERE ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteDelete);

            stmt.setLong(1, Long.parseLong(leModeleProcedureMail.getId()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerModeleProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static ModeleProcedureMail getModeleProcedureMail(
            ModeleProcedureMail leModeleProcedureMail) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ModeleProcedureMail result=null;
        
        try {

            String requete = "SELECT LIBELLE, TYPE, MAIL_OBJET, MAIL_INVITE, MAIL_CORPS, MAIL_SIGNATURE, TYPE_DESTINATAIRE, RECAP_ADH, RECAP_CENTREGESTION "
                    + "FROM HOTLINE.MODELE_PROCEDURE " + "WHERE ID=? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            stmt.setLong(1, Long.parseLong(leModeleProcedureMail.getId()));

            rs = stmt.executeQuery();
            
            if (rs.next()) {
                result = new ModeleProcedureMail();
                result.setId(leModeleProcedureMail.getId());
                result.setLibelle(rs.getString(1));
                result.setType(rs.getString(2));
                result.setMailObjet(rs.getString(3));
                result.setMailInvite(rs.getString(4));
                result.setMailCorps(rs.getString(5));
                result.setMailSignature(rs.getString(6));
                result.setTypeDestinataire(rs.getString(7));
                result.setRecapAdherent(rs.getBoolean(8));
                result.setRecapCentreGestion(rs.getBoolean(9));
                rs.close();
                stmt.close();
                
                requete = "SELECT d.DOC_ID, d.DOC_LIBELLE  "
                        + "FROM HOTLINE.T_DOCUMENT_MODELEPROC_DMP md,"
                        + "     HOTLINE.T_DOCUMENT_DOC d "
                        + "WHERE md.DMP_MODPROC_ID=? "
                        + " AND d.DOC_ID = md.DMP_DOC_ID";
               stmt = conn.prepareStatement(requete);
               stmt.setString(1, leModeleProcedureMail.getId());
               rs = stmt.executeQuery();
               if( rs.next() ){
                   Document leDocument = new Document();
                   leDocument.setId(rs.getString(1));
                   leDocument.setLibelle(rs.getString(2));
                   result.setDocument(leDocument);
               }
                
            } else {
                return null;
            }

            

            return result;
        } catch (Exception e) {
            LOGGER.error("getModeleProcedureMail", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean ajouterScenarioPEC(Scenario leScenario,
            ModelePEC leModelePEC) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.SCENARIO_EVENEMENT( SCENARIO_ID, "
                    + "S_MOTIF_EVENEMENT_ID, "
                    + "ENVOI_FAX, "
                    + "ENVOIL_COURRIEL, "
                    + "APPELANT_FOURNISSEUR_PERMIS, "
                    + "APPELANT_ADHERENT_PERMIS, "
                    + "MODELEPEC_ID ) "
                    + "VALUES( ?, ?, ?, ?, ?, ?, ? )";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(_evenement_pec_id));
            stmt.setBoolean(3, leModelePEC.getEmissionFax());
            stmt.setBoolean(4, leModelePEC.getEmissionCourriel());
            stmt.setBoolean(5, leModelePEC.getAppelantFournisseurPermis());
            stmt.setBoolean(6, leModelePEC.getAppelantBeneficiairePermis());
            stmt.setLong(7, Long.parseLong(leModelePEC.getId()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
            
        } catch (Exception e) {
            LOGGER.error("ajouterScenarioPEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterModelePEC(ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.MODELE_PEC( "
                    + "  OPERATEUR, " + "  ORGANISME, " + "  ENVOI_FAX, "
                    + "  FAX, " + "  ENVOIL_COURRIEL, " + "  COURRIEL, "
                    + "  LIBELLE, " + "  BENEFICIAIRE_PERMIS, "
                    + "  FOURNISSEUR_PERMIS ) "
                    + "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setString(1, leModelePEC.getOperateur());
            stmt.setString(2, leModelePEC.getOrganisme());
            stmt.setBoolean(3, leModelePEC.getEmissionFax());
            stmt.setString(4, leModelePEC.getFax());
            stmt.setBoolean(5, leModelePEC.getEmissionCourriel());
            stmt.setString(6, leModelePEC.getCourriel());
            stmt.setString(7, leModelePEC.getLibelle());
            stmt.setBoolean(8, leModelePEC.getAppelantBeneficiairePermis());
            stmt.setBoolean(9, leModelePEC.getAppelantFournisseurPermis());

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterModelePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    
    public static boolean modifierEntiteGestionSiteWeb(EntiteGestion lEntiteGestion){
    
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            
            String requete = "SELECT count(*) "
                            +"FROM HOTLINE.T_SITEWEBENTITE_SWB s "
                            +"WHERE s.entite_id = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            stmt.setLong(1, Long.parseLong( lEntiteGestion.getId()) );
            rs = stmt.executeQuery();
            boolean present = false;
            if (rs.next()) {
                if( rs.getInt(1)>0){
                    present = true;
                }
            };
            if( present ){
                requete = "UPDATE HOTLINE.T_SITEWEBENTITE_SWB s SET s.site=?, s.url=? WHERE s.entite_id = ?";
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, lEntiteGestion.getSiteWeb().getTypeSite());
                stmt.setString(2, lEntiteGestion.getSiteWeb().getUrl());
                stmt.setLong(3, Long.parseLong( lEntiteGestion.getId()) );
                stmt.executeUpdate();
            }
            else{
                requete = "INSERT INTO HOTLINE.T_SITEWEBENTITE_SWB(ENTITE_ID,SITE,URL) VALUES(?,?,?)";
                stmt = conn.prepareStatement(requete);
                stmt.setLong(1, Long.parseLong( lEntiteGestion.getId()) );
                stmt.setString(2, lEntiteGestion.getSiteWeb().getTypeSite());
                stmt.setString(3, lEntiteGestion.getSiteWeb().getUrl());
                stmt.executeUpdate();  
            }
            conn.commit();
            
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierEntiteGestionSiteWeb", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeRsStmtConn( rs, stmt, conn);
        }
    }
    
    public static boolean modifierModelePEC(ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteUpdate = "UPDATE HOTLINE.MODELE_PEC "
                    + "SET OPERATEUR=?, " + "ORGANISME=?, " + "ENVOI_FAX=?, "
                    + "FAX=?, " + "ENVOIL_COURRIEL=?, " + "COURRIEL=?, "
                    + "LIBELLE=?, " + "BENEFICIAIRE_PERMIS=?, "
                    + "FOURNISSEUR_PERMIS=? " + "WHERE ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteUpdate);

            stmt.setString(1, leModelePEC.getOperateur());
            stmt.setString(2, leModelePEC.getOrganisme());
            stmt.setBoolean(3, leModelePEC.getEmissionFax());
            stmt.setString(4, leModelePEC.getFax());
            stmt.setBoolean(5, leModelePEC.getEmissionCourriel());
            stmt.setString(6, leModelePEC.getCourriel());
            stmt.setString(7, leModelePEC.getLibelle());
            stmt.setBoolean(8, leModelePEC.getAppelantBeneficiairePermis());
            stmt.setBoolean(9, leModelePEC.getAppelantFournisseurPermis());

            stmt.setLong(10, Long.parseLong(leModelePEC.getId()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierModelePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerModelePEC(ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requete = "DELETE FROM HOTLINE.MODELE_PEC WHERE ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            stmt.setLong(1, Long.parseLong(leModelePEC.getId()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerModelePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static ModelePEC getModelePEC(ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ModelePEC result = null;
        
        try {
            String requete = "SELECT " + "LIBELLE, " + "OPERATEUR, "
                    + "ORGANISME, " + "ENVOI_FAX, " + "FAX, "
                    + "ENVOIL_COURRIEL, " + "COURRIEL, "
                    + "BENEFICIAIRE_PERMIS, " + "FOURNISSEUR_PERMIS "
                    + "FROM HOTLINE.MODELE_PEC " + "WHERE ID=?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            stmt.setLong(1, Long.parseLong(leModelePEC.getId()));

            rs = stmt.executeQuery();

            if (rs.next()) {
                result = new ModelePEC();
                result.setId(leModelePEC.getId());
                result.setLibelle(rs.getString(1));
                result.setOperateur(rs.getString(2));
                result.setOrganisme(rs.getString(3));
                result.setEmissionFax(rs.getBoolean(4));
                result.setFax(rs.getString(5));
                result.setEmissionCourriel(rs.getBoolean(6));
                result.setCourriel(rs.getString(7));
                result.setAppelantBeneficiairePermis(rs.getBoolean(8));
                result.setAppelantFournisseurPermis(rs.getBoolean(9));
            }

            stmt.clearParameters();
            return result;
        } catch (Exception e) {
            LOGGER.error("getModelePEC", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean modifierModelePEC(Scenario leScenario,
            ModelePEC leModelePEC) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "UPDATE HOTLINE.SCENARIO_EVENEMENT "
                    + "SET " + "S_MOTIF_EVENEMENT_ID=?, " + "ENVOI_FAX=?, "
                    + "ENVOIL_COURRIEL=?, " + "MODELEPEC_ID=? "
                    + "WHERE SCENARIO_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);

            stmt.setLong(1, Long.parseLong(_evenement_pec_id));
            stmt.setBoolean(2, leModelePEC.getEmissionFax());
            stmt.setBoolean(3, leModelePEC.getEmissionCourriel());
            stmt.setLong(4, Long.parseLong(leModelePEC.getId()));
            stmt.setLong(5, Long.parseLong(leScenario.getID()));

            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierModelePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean supprimerModelePEC(Scenario leScenario,ModelePEC leModelePEC) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteDelete = "DELETE FROM HOTLINE.SCENARIO_EVENEMENT "
                    + "WHERE SCENARIO_ID = ? AND MODELEPEC_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(leModelePEC.getId()));
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();

            requeteDelete = "DELETE FROM HOTLINE.RATTACHEMENT_PEC "
                    + "WHERE SCENARIO_ID = ? AND MODELEPEC_ID = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requeteDelete);
            stmt.setLong(1, Long.parseLong(leScenario.getID()));
            stmt.setLong(2, Long.parseLong(leModelePEC.getId()));
            stmt.executeQuery();
            stmt.clearParameters();

            conn.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("supprimerModelePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterMotif(String idScenario, String libelleMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.MOTIFAPPEL (SCENARIO_ID, LIBELLE) VALUES(?,?)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, idScenario);
            stmt.setString(2, libelleMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterSousMotif(String idMotif,
            String libelleSousMotif, String idReferenceStatistique) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.SMOTIFAPPEL (MOTIF_ID, LIBELLE, REFERENCE_ID) VALUES(?,?,?)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, idMotif);
            stmt.setString(2, libelleSousMotif);
            stmt.setString(3, idReferenceStatistique);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static boolean ajouterSousMotif(String idMotif, String libelle, ReferenceStatistique sousMotifRef) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.SMOTIFAPPEL (MOTIF_ID, LIBELLE, REFERENCE_ID, REGIME_CODE) VALUES(?,?,?,?)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, idMotif);
            stmt.setString(2, libelle.trim());
            stmt.setString(3, sousMotifRef.getRstId());
            stmt.setString(4, sousMotifRef.getId());
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterPoint(String idSousMotif, String libellePoint) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.POINT (S_MOTIF_ID, LIBELLE) VALUES(?,?)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, idSousMotif);
            stmt.setString(2, libellePoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean ajouterSousPoint(String idPoint,
            String libelleSousPoint) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "INSERT INTO HOTLINE.SPOINT (POINT_ID, LIBELLE) VALUES(?,?)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, idPoint);
            stmt.setString(2, libelleSousPoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ajouterSousPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierMotif(String idMotif, String libelleMotif) {

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String requeteInsert = "UPDATE HOTLINE.MOTIFAPPEL moa SET moa.LIBELLE = ? WHERE moa.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, libelleMotif);
            stmt.setString(2, idMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierSousMotif(String idSousMotif,
            String libelleSousMotif, String idReferenceStatistique) {

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String requeteUpdate = "UPDATE HOTLINE.SMOTIFAPPEL smoa SET smoa.LIBELLE = ?, smoa.REFERENCE_ID = ? WHERE smoa.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteUpdate);
            stmt.setString(1, libelleSousMotif);
            stmt.setString(2, idReferenceStatistique);
            stmt.setString(3, idSousMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }
    
    public static boolean modifierSousMotif(String idSousMotif, String libelle, ReferenceStatistique sousMotifRef) {

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String requeteUpdate = "UPDATE HOTLINE.SMOTIFAPPEL smoa SET smoa.LIBELLE = ?, smoa.REFERENCE_ID = ?, smoa.REGIME_CODE = ? WHERE smoa.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteUpdate);
            stmt.setString(1, libelle);
            stmt.setString(2, sousMotifRef.getRstId());
            stmt.setString(3, sousMotifRef.getId());
            stmt.setString(4, idSousMotif);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierSousMotif", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierPoint(String idPoint, String libellePoint) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "UPDATE HOTLINE.POINT p SET p.LIBELLE = ? WHERE p.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, libellePoint);
            stmt.setString(2, idPoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean modifierSousPoint(String idSousPoint,
            String libelleSousPoint) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String requeteInsert = "UPDATE HOTLINE.SPOINT sp SET sp.LIBELLE = ? WHERE sp.ID = ?";
            conn = getConnexion();
            stmt = conn.prepareStatement(requeteInsert);
            stmt.setString(1, libelleSousPoint);
            stmt.setString(2, idSousPoint);
            stmt.executeQuery();
            stmt.clearParameters();
            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("modifierSousPoint", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static Collection<Appel> getFichesLockees() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Appel> res = new ArrayList<Appel>();
        try {

            Appel unDisplay = null;
            String requete = "SELECT a.ID, t.NOM || ' ' ||  t.PRENOM, a.DATEEDITION, c.LIBELLE, mut.LIBELLE, moa.LIBELLE, sma.LIBELLE "
                    + "FROM hotline.APPEL a, hotline.TELEACTEUR t, hotline.CAMPAGNE c, APPLICATION.MUTUELLE mut, hotline.MOTIFAPPEL moa,  hotline.SMOTIFAPPEL sma "
                    + "WHERE a.EDITIONENCOURS = 1 and a.EDITEUR_ID = t.ID(+) "
                    + "and a.CAMPAGNE_ID = c.id "
                    + "and a.MUTUELLE_ID = mut.id "
                    + "and a.MOTIF_ID = moa.ID(+) "
                    + "and a.S_MOTIF_ID = sma.ID(+)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Appel();
                unDisplay.setID(rs.getString(1));
                unDisplay.setEditeur((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDATEEDITION((rs.getTimestamp(3) != null) ? rs
                        .getTimestamp(3) : null);
                unDisplay.setCampagne((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setMutuelle((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setMotif((rs.getString(6) != null) ? rs.getString(6)
                        : "");
                unDisplay.setSousMotif((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getFichesLockees", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean delockerFichesAppels(String[] ids_fiches_a_delocker) {

        Connection conn = null;
        PreparedStatement stmt = null;

        String requeteUpdate = "";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            // UPDATE
            if (ids_fiches_a_delocker != null) {
                int taille_tableau = ids_fiches_a_delocker.length;
                requeteUpdate = "UPDATE HOTLINE.APPEL a SET a.EDITIONENCOURS = 0, a.EDITEUR_ID = null, a.DATEEDITION = null WHERE a.ID = ?";
                for (int i = 0; i < taille_tableau; i++) {
                    stmt = conn.prepareStatement(requeteUpdate);
                    stmt.setString(1, ids_fiches_a_delocker[i]);
                    stmt.executeQuery();
                    stmt.clearParameters();
                    stmt.close();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("delockerFichesAppels", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static boolean lockerFicheAppel(String teleacteur_id,
            String id_appel) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean res = false;
        try {

            // Je bloque que si blocage = 0 'Sinon, y a déjà quelqu'un
            // dessus...)

            String requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.DATEEDITION = SYSDATE, "
                    + "a.EDITEUR_ID = ?, a.EDITIONENCOURS = 1 "
                    + "WHERE a.ID = ? AND a.EDITIONENCOURS = 0 AND a.EDITEUR_ID IS NULL";

            conn = getConnexion();

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, id_appel);
            rs = stmt.executeQuery();

            if (rs.next()) {
                res = true;
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("lockerFicheAppel", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static boolean deLockerFicheAppel(String teleacteur_id,
            String id_appel) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean res = false;

        try {

            // Je déloque que ce que j'ai locké
            String requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.DATEEDITION = null, "
                    + "a.EDITEUR_ID = null, a.EDITIONENCOURS  = 0 "
                    + "WHERE a.ID = ? AND a.EDITEUR_ID = ? AND a.EDITIONENCOURS = 1";

            conn = getConnexion();

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, id_appel);
            stmt.setString(2, teleacteur_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                res = true;
            }

            return res;
        } catch (Exception e) {
            LOGGER.error("deLockerFicheAppel", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return false;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder supprimerProcedureMail(String appel_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        String statut_attente = "6";
        String statut_annule = "8";
        String sous_motif = "1741";

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            String requete = "UPDATE EVENEMENT.EVENEMENT e "
                    + "SET e.STATUT_ID=? "
                    + "WHERE e.STATUT_ID=? AND e.APPEL_ID=? AND SOUSMOTIF_ID=?";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, statut_annule);
            stmt.setString(2, statut_attente);
            stmt.setString(3, appel_id);
            stmt.setString(4, sous_motif);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("supprimerProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder supprimerDemandePEC(String appel_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();
        
        try {
            String requete = "UPDATE EVENEMENT.EVENEMENT e "
                    + "SET e.STATUT_ID=? "
                    + "WHERE e.STATUT_ID=? AND e.APPEL_ID=? AND SOUSMOTIF_ID IN (?, ?)";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, _StatutAnnule);
            stmt.setString(2, _StatutAttente);
            stmt.setString(3, appel_id);
            stmt.setString(4, _PECSousMotif);
            stmt.setString(5, _ssMotifPECHospit);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;
        } catch (Exception e) {
            LOGGER.error("supprimerDemandePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder emettreDemandePEC(String appel_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        String statut_attente = _StatutAttente;
        String statut_envoi_en_attente = _StatutAEmettre;
        String sous_motif = _DemandePECMotif;

        try {
            String requete = "UPDATE EVENEMENT.EVENEMENT e "
                    + "SET e.STATUT_ID=? "
                    + "WHERE e.STATUT_ID=? AND e.APPEL_ID=? AND SOUSMOTIF_ID IN (?, ?)";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, statut_envoi_en_attente);
            stmt.setString(2, statut_attente);
            stmt.setString(3, appel_id);
            stmt.setString(4, sous_motif);
            stmt.setString(5, _ssMotifPECHospit);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("emettreDemandePEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder emettreProcedureMail(String appel_id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        String statut_attente = _StatutAttente;
        String statut_envoi_en_attente = _StatutAEmettre;
        String sous_motif = _ProcedureMailSousMotif;

        try {
            String requete = "UPDATE EVENEMENT.EVENEMENT e "
                    + "SET e.STATUT_ID=? "
                    + "WHERE e.STATUT_ID=? AND e.APPEL_ID=? AND SOUSMOTIF_ID=?";

            conn = getConnexion();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, statut_envoi_en_attente);
            stmt.setString(2, statut_attente);
            stmt.setString(3, appel_id);
            stmt.setString(4, sous_motif);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("emettreProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder cloturerFicheAppelEnHorsCible(String appel_id,
            String code_cloture_souhaite, DynaActionForm daf,
            HttpSession session) {
        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            // CHAMPS DEJA POSITIONNES : ID, CAMPAGNE_ID, CREATEUR_ID, DATEAPPEL

            requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.CLOTURE_CODE = ?, a.DATECLOTURE = SYSDATE, a.CLOTUREUR_ID = ?, a.MUTUELLE_ID = ?, a.EDITIONENCOURS = 0, a.EDITEUR_ID = null, a.RESOLU=1 "
                    + "WHERE a.ID = ?";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, code_cloture_souhaite);
            stmt.setString(2, (String) daf.get(CrmForms._teleacteur_id));
            stmt.setString(3, (String) daf.get(CrmForms._mutuelle_id));
            stmt.setString(4, appel_id);
            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        }

        catch (Exception e) {
            LOGGER.error("cloturerFicheAppelEnHorsCible", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }

    }

    public static StringBuilder cloturerFicheAppelEnClotureOuEnAppelSortant(
            String appel_id, String code_cloture_souhaite, Boolean resolu,
            DynaActionForm daf, HttpSession session) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String code_appelant_preselectionne = "";
            String entite_gestion_id = "";
            String appelant_id = "";
            String beneficiaire_id = "";
            String beneficiaire_qualite = "";
            String adherent_id = "";
            String etablissement_id = "";
            String motif_id = "";
            String sous_motif_id = "";
            String point_id = "";
            String sous_point_id = "";
            String periode_rappel = "";

            if (!_EMPTY.equals(daf.get(CrmForms._motif_id))) {
                motif_id = (String) daf.get("motif_id");
            }
            if (!_EMPTY.equals(daf.get(CrmForms._sous_motif_id))) {
                sous_motif_id = (String) daf.get(CrmForms._sous_motif_id);
            }
            if (!_EMPTY.equals(daf.get(CrmForms._point_id))) {
                point_id = (String) daf.get(CrmForms._point_id);
            }
            if (!_EMPTY.equals(daf.get("sous_point_id"))) {
                sous_point_id = (String) daf.get(CrmForms._sous_point_id);
            }
            if (!_EMPTY.equals(daf.get("periode_rappel"))) {
                periode_rappel = (String) daf.get(CrmForms._periode_rappel);
            }

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    etablissement_id = beneficiaire.getETABLISSEMENT_ID();
                    entite_gestion_id = beneficiaire.getENTITE_GESTION_ID();
                    beneficiaire_id = beneficiaire.getID();
                    adherent_id = beneficiaire.getAdherentId();
                    beneficiaire_qualite = beneficiaire.getQualite();
                    code_appelant_preselectionne = (String) daf
                            .get("appelant_code");
                }
            } else if (objet instanceof Etablissement) {
                Etablissement etablissement = (Etablissement) objet_appelant
                        .getObjet();
                if (etablissement != null) {
                    etablissement_id = etablissement.getID();
                    entite_gestion_id = etablissement.getENTITE_GESTION_ID();
                    code_appelant_preselectionne = (String) daf
                            .get(CrmForms._appelant_code);
                }
            } else {
                Appelant appelant = (Appelant) objet_appelant.getObjet();
                if (appelant != null) {
                    appelant_id = appelant.getID();
                    code_appelant_preselectionne = appelant.getTYPE_CODE();
                }
            }

            requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.CLOTURE_CODE = ?, a.DATECLOTURE = SYSDATE, a.CLOTUREUR_ID = ?, a.MUTUELLE_ID = ?, a.EDITIONENCOURS = 0, a.EDITEUR_ID = null,  "
                    + "a.MOTIF_ID = ?, a.S_MOTIF_ID = ?, a.POINT_ID = ?, a.S_POINT_ID = ?, a.APPELANT_ID = ?, a.BENEFICIAIRE_ID = ?, "
                    + "a.BENEFICIAIRE_QUALITE = ?, a.ADHERENT_ID = ?, a.NUMERORAPPEL = ?, a.DATERAPPEL = ?, a.PERIODERAPPEL_CODE = ?, "
                    + "a.COMMENTAIRE = ?, a.SATISFACTION_CODE = ?, a.TRAITEMENTURGENT = ?, a.ENTITEGESTION_ID = ?, a.ETABLISSEMENT_ID = ?, "
                    + "a.CODEAPPELANT_SELECTIONNE = ?, a.RECLAMATION = ?, a.TRANSFERTS = ?, a.RESOLU=? "
                    + "WHERE a.ID = ?";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, code_cloture_souhaite);
            stmt.setString(2, (String) daf.get(CrmForms._teleacteur_id));
            stmt.setString(3, (String) daf.get(CrmForms._mutuelle_id));
            stmt.setString(4, motif_id);
            stmt.setString(5, sous_motif_id);
            stmt.setString(6, point_id);
            stmt.setString(7, sous_point_id);
            stmt.setString(8, appelant_id);
            stmt.setString(9, beneficiaire_id);
            stmt.setString(10, beneficiaire_qualite);
            stmt.setString(11, adherent_id);
            stmt.setString(12, (String) daf.get(CrmForms._numero_rappel));
            stmt.setString(13, (String) daf.get(CrmForms._date_rappel));
            stmt.setString(14, periode_rappel);
            stmt.setString(15, (String) daf.get(CrmForms._commentaires));
            stmt.setString(16, (String) daf.get(CrmForms._satisfaction_code));
            stmt.setString(17, (String) daf.get(CrmForms._traitement_urgent));
            stmt.setString(18, entite_gestion_id);
            stmt.setString(19, etablissement_id);
            stmt.setString(20, code_appelant_preselectionne);
            stmt.setString(21, (String) daf.get(CrmForms._reclamation));
            stmt.setString(22, (String) daf.get(CrmForms._destinataire_transfert));
            stmt.setBoolean(23, resolu);

            stmt.setString(24, appel_id);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("cloturerFicheAppelEnClotureOuEnAppelSortant", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }
    }

    public static StringBuilder cloturerFicheAppelEnATraiter(String appel_id,
            String code_cloture_souhaite, DynaActionForm daf,
            HttpSession session) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            // CHAMPS DEJA POSITIONNES : ID, CAMPAGNE_ID, CREATEUR_ID, DATEAPPEL

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String code_appelant_preselectionne = "";
            String entite_gestion_id = "";
            String appelant_id = "";
            String beneficiaire_id = "";
            String beneficiaire_qualite = "";
            String adherent_id = "";
            String etablissement_id = "";
            String motif_id = "";
            String sous_motif_id = "";
            String point_id = "";
            String sous_point_id = "";
            String periode_rappel = "";

            if (!_EMPTY.equals(daf.get(CrmForms._motif_id))) {
                motif_id = (String) daf.get(CrmForms._motif_id);
            }
            if (!_EMPTY.equals(daf.get(CrmForms._sous_motif_id))) {
                sous_motif_id = (String) daf.get(CrmForms._sous_motif_id);
            }
            if (!_EMPTY.equals(daf.get(CrmForms._point_id))) {
                point_id = (String) daf.get(CrmForms._point_id);
            }
            if (!_EMPTY.equals(daf.get(CrmForms._sous_point_id))) {
                sous_point_id = (String) daf.get(CrmForms._sous_point_id);
            }
            if (!_EMPTY.equals(daf.get(CrmForms._periode_rappel))) {
                periode_rappel = (String) daf.get(CrmForms._periode_rappel);
            }

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    etablissement_id = beneficiaire.getETABLISSEMENT_ID();
                    entite_gestion_id = beneficiaire.getENTITE_GESTION_ID();
                    beneficiaire_id = beneficiaire.getID();
                    adherent_id = beneficiaire.getAdherentId();
                    beneficiaire_qualite = beneficiaire.getQualite();
                    code_appelant_preselectionne = (String) daf
                            .get(CrmForms._appelant_code);
                }
            } else if (objet instanceof Etablissement) {
                Etablissement etablissement = (Etablissement) objet_appelant
                        .getObjet();
                if (etablissement != null) {
                    etablissement_id = etablissement.getID();
                    entite_gestion_id = etablissement.getENTITE_GESTION_ID();
                    code_appelant_preselectionne = (String) daf
                            .get(CrmForms._appelant_code);
                }
            } else {
                Appelant appelant = (Appelant) objet_appelant.getObjet();
                if (appelant != null) {
                    appelant_id = appelant.getID();
                    code_appelant_preselectionne = appelant.getTYPE_CODE();
                }
            }

            requete = "UPDATE HOTLINE.APPEL a "
                    + "set a.CLOTURE_CODE = ?, a.MUTUELLE_ID = ?, a.EDITIONENCOURS = 0, a.EDITEUR_ID = null,  "
                    + "a.MOTIF_ID = ?, a.S_MOTIF_ID = ?, a.POINT_ID = ?, a.S_POINT_ID = ?, a.APPELANT_ID = ?, a.BENEFICIAIRE_ID = ?, "
                    + "a.BENEFICIAIRE_QUALITE = ?, a.ADHERENT_ID = ?, a.NUMERORAPPEL = ?, a.DATERAPPEL = ?, a.PERIODERAPPEL_CODE = ?, "
                    + "a.COMMENTAIRE = ?, a.SATISFACTION_CODE = ?, a.TRAITEMENTURGENT = ?, a.ENTITEGESTION_ID = ?, a.ETABLISSEMENT_ID = ?, "
                    + "a.CODEAPPELANT_SELECTIONNE = ?, a.RECLAMATION = ? "
                    + "WHERE a.ID = ?";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);

            stmt.setString(1, code_cloture_souhaite);
            stmt.setString(2, (String) daf.get(CrmForms._mutuelle_id));
            stmt.setString(3, motif_id);
            stmt.setString(4, sous_motif_id);
            stmt.setString(5, point_id);
            stmt.setString(6, sous_point_id);
            stmt.setString(7, appelant_id);
            stmt.setString(8, beneficiaire_id);
            stmt.setString(9, beneficiaire_qualite);
            stmt.setString(10, adherent_id);
            stmt.setString(11, (String) daf.get(CrmForms._numero_rappel));
            stmt.setString(12, (String) daf.get(CrmForms._date_rappel));
            stmt.setString(13, periode_rappel);
            stmt.setString(14, (String) daf.get(CrmForms._commentaires));
            stmt.setString(15, (String) daf.get(CrmForms._satisfaction_code));
            stmt.setString(16, (String) daf.get(CrmForms._traitement_urgent));
            stmt.setString(17, entite_gestion_id);
            stmt.setString(18, etablissement_id);
            stmt.setString(19, code_appelant_preselectionne);
            stmt.setString(20, (String) daf.get(CrmForms._reclamation));

            stmt.setString(21, appel_id);

            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("cloturerFicheAppelEnATraiter", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }

    }

    public static StringBuilder creerEvenementsProcedureMail(String appel_id,
            DynaActionForm daf, HttpSession session, ModeleProcedureMail unModeleProcedure ) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        String motif_dossier = _CourrielMotif;
        String s_motif_dossier = _ProcedureMailSousMotif;
        String statut_dossier = _StatutAttente;

        Collection<ModeleProcedureMail> procedures= (Collection<ModeleProcedureMail>)session.getAttribute(FicheAppelAction._var_session_selected_procedureMails);
        if( procedures==null || procedures.isEmpty()){
            return sb.append(_VRAI);
        }
        
        try {

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String mutuelle_id = "";
            String entite_gestion_id = "";
            String etablissement_id = "";
            String adherent_id = "";
            String personne_id = "";
            String adresse_id = "";
            String site = "";

            String procedure_id = "", adresse_mail = "", appelant_id = "";

            mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
            adresse_mail = (String) daf.get(CrmForms._procedure_mail_dest);
            procedure_id = (String) daf.get(CrmForms._procedure_mail_id);
            
            
            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    etablissement_id = beneficiaire.getETABLISSEMENT_ID();
                    entite_gestion_id = beneficiaire.getENTITE_GESTION_ID();
                    adherent_id = beneficiaire.getAdherentId();
                    appelant_id = adherent_id;
                    fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                            .getPersonne();
                    if (personne != null) {
                        personne_id = personne.getID();
                        adresse_id = personne.getADRESSE_ID();
                    }
                }
            } else if (objet instanceof Etablissement) {
                Etablissement etablissement = (Etablissement) objet_appelant
                        .getObjet();
                if (etablissement != null) {
                    etablissement_id = etablissement.getID();
                    appelant_id = etablissement_id;
                    entite_gestion_id = etablissement.getENTITE_GESTION_ID();
                }
            }

            TeleActeur teleActeur = (TeleActeur) session
                    .getAttribute(IContacts._var_session_teleActeur);
            if (teleActeur != null) {
                site = teleActeur.getSite();
            }

            String requete = "INSERT INTO EVENEMENT.EVENEMENT( "
                    + "ID, DATE_CREATION, CREATEUR_ID, SITE, APPEL_ID, "
                    + "MUTUELLE_ID, ENTITE_ID, ETABLT_ID, ADHERENT_ID, PERSONNE_ID, ADRESSE_ID, "
                    + "MOTIF_ID, SOUSMOTIF_ID, STATUT_ID, "
                    + "TYPE, MEDIA, SORTIE_EMAIL, "
                    + "EMAIL, COL15, APPELANT_ID, COMMENTAIRE ) "
                    + "VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, 1, "
                    + "?, ?, ?, ? ) ";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);
            
            stmt.clearParameters();
            stmt.setString(1, (String) daf.get(CrmForms._teleacteur_id));
            stmt.setString(2, site);
            stmt.setString(3, appel_id);

            stmt.setString(4, mutuelle_id);
            stmt.setString(5, entite_gestion_id);
            stmt.setString(6, etablissement_id);
            stmt.setString(7, adherent_id);
            stmt.setString(8, personne_id);
            stmt.setString(9, adresse_id);

            stmt.setString(10, motif_dossier);
            stmt.setString(11, s_motif_dossier);
            stmt.setString(12, statut_dossier);

            stmt.setString(13, _SORTANT);
            stmt.setString(14, _MediaCourriel);

            stmt.setString(15, adresse_mail);
            stmt.setString(16, procedure_id);
            stmt.setString(17, appelant_id);
            
            stmt.setString(18, unModeleProcedure.getLibelle() );

            stmt.execute();
            conn.commit();
    
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("creerEvenementProcedureMail", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }

    }

    public static StringBuilder creerEvenementPourHCourriers(String appel_id,
            DynaActionForm daf, HttpSession session, File fichier) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            // CHAMPS DEJA POSITIONNES : ID, CAMPAGNE_ID, CREATEUR_ID, DATEAPPEL

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();
            
            String entite_gestion_id = "";
            String appelant_id = "";
            String beneficiaire_id = "";
            String personne_id = "";
            String adresse_id = "";
            String adherent_id = "";
            String etablissement_id = "";
            String traitement_urgent = "";
            String periode_rappel = "";
            String site = "";
            String user = "";
            String mutuelle_id = "";
            String numero_rappel = "";
            String date_rappel = "";
            String commentaires = "";
            String flag_division = _FAUX;

            String type_dossier = _TypeDossierHContact;
            String priorite = "2";

            mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
            numero_rappel = (String) daf.get(CrmForms._numero_rappel);
            date_rappel = (String) daf.get(CrmForms._date_rappel);
            commentaires = (String) daf.get(CrmForms._commentaires);
            traitement_urgent = (String) daf.get(CrmForms._traitement_urgent);

            TeleActeur teleActeur = (TeleActeur) session
                    .getAttribute(IContacts._var_session_teleActeur);
            if (teleActeur != null) {
                site = teleActeur.getSite();
                user = teleActeur.getLogin();
            }
            
            if (!_EMPTY.equals(daf.get(CrmForms._periode_rappel))) {
                periode_rappel = (String) daf.get(CrmForms._periode_rappel);
            }

            if (!_EMPTY.equals(daf.get(CrmForms._type_dossier))) {
                type_dossier = (String) daf.get(CrmForms._type_dossier);
            }

            if (_VRAI.equals(traitement_urgent)) {
                priorite = "10";
            }

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    etablissement_id = beneficiaire.getETABLISSEMENT_ID();
                    entite_gestion_id = beneficiaire.getENTITE_GESTION_ID();
                    beneficiaire_id = beneficiaire.getID();
                    adherent_id = beneficiaire.getAdherentId();
                    fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                            .getPersonne();
                    if (personne != null) {
                        personne_id = personne.getID();
                        adresse_id = personne.getADRESSE_ID();
                    }
                }
            } else if (objet instanceof Etablissement) {
                Etablissement etablissement = (Etablissement) objet_appelant
                        .getObjet();
                if (etablissement != null) {
                    etablissement_id = etablissement.getID();
                    entite_gestion_id = etablissement.getENTITE_GESTION_ID();
                    
                }
            } else {
                Appelant appelant = (Appelant) objet_appelant.getObjet();
                if (appelant != null) {
                    appelant_id = appelant.getID();
                }
            }

            // On crée un "événement"
            // ETAPES :
            // 1) INSERT DANS GED.DVS_MASTER_IMAGE
            // 2) INSERT DANS GED.COURRIER
            // 3) INSERT DANS EVENEMENT.EVENEMENT

            // Récupération des séquences
            String requete_sequences = "SELECT GED.SQ_COURRIER_LOT.nextVal, GED.SQ_COURRIER_GROUPE_LOT.nextVal, GED.SQ_COURRIER_N_DOCUMENT_ID.nextVal FROM DUAL ";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete_sequences);
            rs = stmt.executeQuery();
            rs.next();
            String SQ_COURRIER_LOT_NEXTVAL = rs.getString(1);
            String SQ_COURRIER_GROUPE_LOT_NEXTVAL = rs.getString(2);
            String SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL = rs.getString(3);
            stmt.clearParameters();
            stmt.close();
            rs.close();

            String extension = "PDF";

            // Création d'un BLOB vide
            String requete_insert_blob = "INSERT INTO GED.DVS_MASTER_IMAGE(N_DOCUMENT_ID, BL_MASTER_IMAGE, C_MIME) VALUES (?, EMPTY_BLOB(), ?)";
            stmt = conn.prepareStatement(requete_insert_blob);

            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(2, extension);
            stmt.execute();
            stmt.clearParameters();
            stmt.close();

            // Récupération du BLOB vide
            stmt = conn.prepareStatement("SELECT BL_MASTER_IMAGE FROM GED.DVS_MASTER_IMAGE WHERE N_DOCUMENT_ID = ?");
            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            rs = stmt.executeQuery();
            rs.next();
            Blob blob = rs.getBlob(1);
            
            stmt.clearParameters();
            stmt.close();

            // Ecriture dans le BLOB vide
            InputStream is = new FileInputStream(fichier);
            ecrireFluxDansBlob(is, blob);

            // UPADTE du BLOB vide

            stmt = conn.prepareStatement("UPDATE GED.DVS_MASTER_IMAGE SET (BL_MASTER_IMAGE) = ? WHERE N_DOCUMENT_ID = ?");
            stmt.setBlob(1, blob);
            stmt.setString(2, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.executeUpdate();
            stmt.clearParameters();
            stmt.close();

            // INSERTION DANS GED.COURRIER
            stmt = conn
                    .prepareStatement("INSERT INTO GED.COURRIER (DATE_INSERTION, DATE_RECEPTION_POSTE, ID,"
                            + "MUTUELLE_ID, ENTITE_GESTION_ID, ADHERENT_ID, BENEFICIAIRE_ID, ETABLISSEMENT_ID, "
                            + "ADRESSE_ID, PERSONNE_ID, CODE_ID, COMMENTAIRE, LOT, PAGE, PAGE_ORIGINE, EXTENSION, DIRECTION, "
                            + "USERSCAN, USERIDENTIF, MEDIA, N_DOCUMENT_ID, SITE, GROUPE_LOT, TCO_ID, PRIORITE, FLAG_DIVISION, "
                            + "USER_DIVISION, DATE_DIVISION, DATE_IDENTIFICATION ) "
                            +

                            " VALUES (SYSDATE, SYSDATE, GED.SEQ_COURRIER_ID.nextVal, "
                            + "?, ?, ?, ?, ?,  "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, (select tco.ID from GED.T_COURRIERS_ORIGINE_TCO tco where tco.libelle = 'H.Contacts'), ?, ?, "
                            + "?, SYSDATE, SYSDATE)");

            // ID : SEQ_COURRIER_ID_NEXTVAL
            // LOT : SQ_COURRIER_LOT_NEXTVAL
            // N_DOCUMENT_ID : SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL
            // GROUPE_LOT : SQ_COURRIER_GROUPE_LOT_NEXTVAL
            // PAGE : page_du_scan = PAGE_ORIGINE

            stmt.setString(1, mutuelle_id);
            stmt.setString(2, entite_gestion_id);
            stmt.setString(3, adherent_id);
            stmt.setString(4, beneficiaire_id);
            stmt.setString(5, etablissement_id);

            stmt.setString(6, adresse_id);
            stmt.setString(7, personne_id);
            stmt.setString(8, type_dossier); 
            stmt.setString(9, commentaires);
            stmt.setString(10, SQ_COURRIER_LOT_NEXTVAL);
            stmt.setString(11, "1");
            stmt.setString(12, "1");
            stmt.setString(13, extension);
            stmt.setString(14, _ENTRANT); // DIRECTION

            stmt.setString(15, user);
            stmt.setString(16, user);
            stmt.setString(17, _MediaAppel);
            stmt.setString(18, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(19, site);
            stmt.setString(20, SQ_COURRIER_GROUPE_LOT_NEXTVAL);
            stmt.setString(21, priorite); // PRIORITE
            stmt.setString(22, flag_division);
            stmt.setString(23, user);

            stmt.execute();
            stmt.clearParameters();

            requete = "INSERT INTO EVENEMENT.EVENEMENT "
                    + "(ID, DATE_CREATION, CREATEUR_ID, MUTUELLE_ID, ENTITE_ID, ETABLT_ID, ADHERENT_ID, BENEFICIAIRE_ID, "
                    + "PERSONNE_ID, ADRESSE_ID, MOTIF_ID, SOUSMOTIF_ID, NUMERORAPPEL, DATERAPPEL, PERIODERAPPEL, COMMENTAIRE, STATUT_ID, "
                    + "TYPE, MEDIA, URGENT, SITE, APPEL_ID, APPELANT_ID, RECLAMATION, COURRIER_ID) "
                    + "VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, ?, ?, ?, ?,"
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?,"
                    + "?, ?, ?, ?, ?, ?, ?, ? ) ";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, (String) daf.get(CrmForms._teleacteur_id));
            stmt.setString(2, (String) daf.get(CrmForms._mutuelle_id));
            stmt.setString(3, entite_gestion_id);
            stmt.setString(4, etablissement_id);
            stmt.setString(5, adherent_id);
            stmt.setString(6, beneficiaire_id);

            stmt.setString(7, personne_id);
            stmt.setString(8, adresse_id);
            stmt.setString(9, _EvenementAppel);
            stmt.setString(10, type_dossier); 
            stmt.setString(11, numero_rappel);
            stmt.setString(12, date_rappel);
            stmt.setString(13, periode_rappel);
            stmt.setString(14, commentaires);
            stmt.setString(15, _StatutATraiter);

            stmt.setString(16, _ENTRANT);
            stmt.setString(17, _MediaAppel);
            stmt.setString(18, (String) daf.get(CrmForms._traitement_urgent));
            stmt.setString(19, site);
            stmt.setString(20, appel_id);
            stmt.setString(21, appelant_id);
            stmt.setString(22, (String) daf.get(CrmForms._reclamation));
            stmt.setString(23, SQ_COURRIER_LOT_NEXTVAL);
            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("creerEvenementPourHCourriers", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    
    public static StringBuilder insererBlobPEC( DemandePec laDemandePEC,File fichier){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        StringBuilder sb = new StringBuilder();
        
        try {
            
            Evenement evt = getEvenementById( laDemandePEC.getId() );
            
            // Récupération des séquences
            String requete_sequences = "SELECT GED.SQ_COURRIER_LOT.nextVal, GED.SQ_COURRIER_GROUPE_LOT.nextVal, GED.SQ_COURRIER_N_DOCUMENT_ID.nextVal FROM DUAL ";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete_sequences);
            rs = stmt.executeQuery();
            rs.next();
            String SQ_COURRIER_LOT_NEXTVAL = rs.getString(1);
            String SQ_COURRIER_GROUPE_LOT_NEXTVAL = rs.getString(2);
            String SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL = rs.getString(3);
            stmt.clearParameters();
            stmt.close();
            rs.close();

            String extension = "PDF";

            // Création d'un BLOB vide
            String requete_insert_blob = "INSERT INTO GED.DVS_MASTER_IMAGE(N_DOCUMENT_ID, BL_MASTER_IMAGE, C_MIME) VALUES (?, EMPTY_BLOB(), ?)";
            stmt = conn.prepareStatement(requete_insert_blob);

            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(2, extension);
            stmt.execute();
            stmt.clearParameters();
            stmt.close();

            // Récupération du BLOB vide
            stmt = conn
                    .prepareStatement("SELECT BL_MASTER_IMAGE FROM GED.DVS_MASTER_IMAGE WHERE N_DOCUMENT_ID = ?");
            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            rs = stmt.executeQuery();

            rs.next();
            Blob blob = rs.getBlob(1);
             
            stmt.clearParameters();
            stmt.close();

            // Ecriture dans le BLOB vide
            InputStream is = new FileInputStream(fichier);
            ecrireFluxDansBlob(is, blob);

            // UPADTE du BLOB vide

            stmt = conn
                    .prepareStatement("UPDATE GED.DVS_MASTER_IMAGE SET (BL_MASTER_IMAGE) = ? WHERE N_DOCUMENT_ID = ?");
            stmt.setBlob(1, blob);
            stmt.setString(2, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.executeUpdate();
            stmt.clearParameters();
            stmt.close();

            // INSERTION DANS GED.COURRIER
            stmt = conn
                    .prepareStatement("INSERT INTO GED.COURRIER (DATE_INSERTION, DATE_RECEPTION_POSTE, ID,"
                            + "MUTUELLE_ID, ENTITE_GESTION_ID, ADHERENT_ID, BENEFICIAIRE_ID, ETABLISSEMENT_ID, "
                            + "ADRESSE_ID, PERSONNE_ID, CODE_ID, COMMENTAIRE, LOT, PAGE, PAGE_ORIGINE, EXTENSION, DIRECTION, "
                            + "USERSCAN, USERIDENTIF, MEDIA, N_DOCUMENT_ID, SITE, GROUPE_LOT, TCO_ID, PRIORITE, FLAG_DIVISION, "
                            + "USER_DIVISION, DATE_DIVISION, DATE_IDENTIFICATION, ENATTENTE, ENCOURS, TRAITE, DESTINATAIRE, SUJET ) "
                            + " VALUES (SYSDATE, SYSDATE, GED.SEQ_COURRIER_ID.nextVal, "
                            + "?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, SYSDATE, SYSDATE, ?, ?, ?, ?, ? )");

            // ID : SEQ_COURRIER_ID_NEXTVAL
            // LOT : SQ_COURRIER_LOT_NEXTVAL
            // N_DOCUMENT_ID : SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL
            // GROUPE_LOT : SQ_COURRIER_GROUPE_LOT_NEXTVAL
            // PAGE : page_du_scan = PAGE_ORIGINE

            stmt.setString(1, evt.getMUTUELLE_ID() );
            stmt.setString(2, evt.getENTITE_ID() );
            stmt.setString(3, evt.getADHERENT_ID() );
            stmt.setString(4, evt.getBENEFICIAIRE_ID() );
            stmt.setString(5, evt.getETABLT_ID() ) ;

            stmt.setString(6, evt.getADRESSE_ID() );
            stmt.setString(7, evt.getPERSONNE_ID() );
            stmt.setString(8, _DemandePECGEDCode); 
            stmt.setString(9, "" );
            stmt.setString(10, SQ_COURRIER_LOT_NEXTVAL);
            stmt.setString(11, "1");
            stmt.setString(12, "1");
            stmt.setString(13, extension);
            stmt.setString(14, _SORTANT); 

            stmt.setString(15, _userROBOT);
            stmt.setString(16, _userROBOT);
            stmt.setString(17, evt.getMEDIA() );
            stmt.setString(18, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(19, _siteMARSEILLE );
            stmt.setString(20, SQ_COURRIER_GROUPE_LOT_NEXTVAL);
            stmt.setString(21, _TCOHContacts);
            stmt.setString(22, _prioriteDemandePEC); 
            stmt.setString(23, _FAUX);
            
            stmt.setString(24, _userROBOT);
            stmt.setString(25, _VRAI);
            stmt.setString(26, _FAUX);
            stmt.setString(27, _FAUX);
            stmt.setString(28, laDemandePEC.getAdrOperateur() );
            stmt.setString(29, _PecDemande+" "+laDemandePEC.getOperateur());
                                    
            stmt.execute();
            stmt.clearParameters();
            
            stmt = conn.prepareStatement("UPDATE EVENEMENT.EVENEMENT e SET COURRIER_ID = ? WHERE ID=?");
            stmt.setString(1, SQ_COURRIER_LOT_NEXTVAL);
            stmt.setString(2, evt.getID());
            stmt.executeUpdate();
            
            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("insererBlobPEC", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return sb.append(_FAUX);
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    };
    
    
    public static StringBuilder creerEvenementFicheDeTransfert(String appel_id,
            DynaActionForm daf, HttpSession session, File fichier) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String entite_gestion_id = "";
            String appelant_id = "";
            String beneficiaire_id = "";
            String personne_id = "";
            String adresse_id = "";
            String adherent_id = "";
            String etablissement_id = "";
            String periode_rappel = "";
            String site = "";
            String user = "";
            String mutuelle_id = "";
            String numero_rappel = "";
            String date_rappel = "";
            String commentaires = "";
            String flag_division = _FAUX;

            String entrant_sortant = _ENTRANT, destinataire_transfert = "";

            mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
            numero_rappel = (String) daf.get(CrmForms._numero_rappel);
            date_rappel = (String) daf.get(CrmForms._date_rappel);
            commentaires = (String) daf.get(CrmForms._commentaires);
            destinataire_transfert = (String) daf.get(CrmForms._destinataire_transfert);

            TeleActeur teleActeur = (TeleActeur) session
                    .getAttribute(IContacts._var_session_teleActeur);
            if (teleActeur != null) {
                site = teleActeur.getSite();
                user = teleActeur.getLogin();
            }

            if (!"".equals(destinataire_transfert)) {
                if (destinataire_transfert.toLowerCase().indexOf(
                        "@igestion-gd.fr") != -1) {
                    entrant_sortant = _ENTRANT;
                } else {
                    entrant_sortant = _SORTANT;
                }
            }

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    etablissement_id = beneficiaire.getETABLISSEMENT_ID();
                    entite_gestion_id = beneficiaire.getENTITE_GESTION_ID();
                    beneficiaire_id = beneficiaire.getID();
                    adherent_id = beneficiaire.getAdherentId();
                    fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                            .getPersonne();
                    if (personne != null) {
                        personne_id = personne.getID();
                        adresse_id = personne.getADRESSE_ID();
                    }
                }
            } else if (objet instanceof Etablissement) {
                Etablissement etablissement = (Etablissement) objet_appelant
                        .getObjet();
                if (etablissement != null) {
                    etablissement_id = etablissement.getID();
                    entite_gestion_id = etablissement.getENTITE_GESTION_ID();
                }
            } else {
                Appelant appelant = (Appelant) objet_appelant.getObjet();
                if (appelant != null) {
                    appelant_id = appelant.getID();
                }
            }

            // On crée un "événement"
            // ETAPES :
            // 1) INSERT DANS GED.DVS_MASTER_IMAGE
            // 2) INSERT DANS GED.COURRIER
            // 3) INSERT DANS EVENEMENT.EVENEMENT

            // Récupération des séquences
            String requete_sequences = "SELECT GED.SQ_COURRIER_LOT.nextVal, GED.SQ_COURRIER_GROUPE_LOT.nextVal, GED.SQ_COURRIER_N_DOCUMENT_ID.nextVal FROM DUAL ";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete_sequences);
            rs = stmt.executeQuery();
            rs.next();
            String SQ_COURRIER_LOT_NEXTVAL = rs.getString(1);
            String SQ_COURRIER_GROUPE_LOT_NEXTVAL = rs.getString(2);
            String SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL = rs.getString(3);
            stmt.clearParameters();
            stmt.close();
            rs.close();

            String extension = "PDF";

            // Création d'un BLOB vide
            String requete_insert_blob = "INSERT INTO GED.DVS_MASTER_IMAGE(N_DOCUMENT_ID, BL_MASTER_IMAGE, C_MIME) VALUES (?, EMPTY_BLOB(), ?)";
            stmt = conn.prepareStatement(requete_insert_blob);

            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(2, extension);
            stmt.execute();
            stmt.clearParameters();
            stmt.close();

            // Récupération du BLOB vide
            stmt = conn
                    .prepareStatement("SELECT BL_MASTER_IMAGE FROM GED.DVS_MASTER_IMAGE WHERE N_DOCUMENT_ID = ?");
            stmt.setString(1, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            rs = stmt.executeQuery();
            rs.next();
            
            Blob blob = rs.getBlob(1);
            
            stmt.clearParameters();
            stmt.close();

            // Ecriture dans le BLOB vide
            InputStream is = new FileInputStream(fichier);
            ecrireFluxDansBlob(is, blob);

            // UPADTE du BLOB vide

            stmt = conn.prepareStatement("UPDATE GED.DVS_MASTER_IMAGE SET (BL_MASTER_IMAGE) = ? WHERE N_DOCUMENT_ID = ?");
            stmt.setBlob(1, blob);
            stmt.setString(2, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.executeUpdate();
            stmt.clearParameters();
            stmt.close();

            // INSERTION DANS GED.COURRIER
            stmt = conn
                    .prepareStatement("INSERT INTO GED.COURRIER (DATE_INSERTION, DATE_RECEPTION_POSTE, "
                            + "ID, MUTUELLE_ID, ENTITE_GESTION_ID, ADHERENT_ID, BENEFICIAIRE_ID, ETABLISSEMENT_ID, "
                            + "ADRESSE_ID, PERSONNE_ID, CODE_ID, COMMENTAIRE, LOT, PAGE, PAGE_ORIGINE, EXTENSION, DIRECTION, "
                            + "USERSCAN, USERIDENTIF, MEDIA, N_DOCUMENT_ID, SITE, GROUPE_LOT, TCO_ID, FLAG_DIVISION,"
                            + "USER_DIVISION, DATE_DIVISION, DATE_IDENTIFICATION, DATE_TRAITEMENT, TRAITE ) "
                            + " VALUES (SYSDATE, SYSDATE, GED.SEQ_COURRIER_ID.nextVal, ?, ?, ?, ?, ?,  "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, (select tco.ID from GED.T_COURRIERS_ORIGINE_TCO tco where tco.libelle = 'H.Contacts'), ?, "
                            + "?, SYSDATE, SYSDATE, SYSDATE, ? )");

            // ID : SEQ_COURRIER_ID_NEXTVAL
            // LOT : SQ_COURRIER_LOT_NEXTVAL
            // N_DOCUMENT_ID : SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL
            // GROUPE_LOT : SQ_COURRIER_GROUPE_LOT_NEXTVAL
            // PAGE : page_du_scan = PAGE_ORIGINE

            stmt.setString(1, mutuelle_id);
            stmt.setString(2, entite_gestion_id);
            stmt.setString(3, adherent_id);
            stmt.setString(4, beneficiaire_id);
            stmt.setString(5, etablissement_id);

            stmt.setString(6, adresse_id);
            stmt.setString(7, personne_id);
            stmt.setString(8, _CodeFicheTransfert); 
            stmt.setString(9, commentaires);
            stmt.setString(10, SQ_COURRIER_LOT_NEXTVAL);
            stmt.setString(11, "1");
            stmt.setString(12, "1");
            stmt.setString(13, extension);
            stmt.setString(14, entrant_sortant);

            stmt.setString(15, user);
            stmt.setString(16, user);
            stmt.setString(17, _MediaAppel);
            stmt.setString(18, SQ_COURRIER_N_DOCUMENT_ID_NEXTVAL);
            stmt.setString(19, site);
            stmt.setString(20, SQ_COURRIER_GROUPE_LOT_NEXTVAL);
            stmt.setString(21, flag_division);
            stmt.setString(22, user);
            stmt.setString(23, _VRAI);

            stmt.execute();
            stmt.clearParameters();

            requete = "INSERT INTO EVENEMENT.EVENEMENT "
                    + "(ID, DATE_CREATION, CREATEUR_ID, MUTUELLE_ID, ENTITE_ID, ETABLT_ID, ADHERENT_ID, BENEFICIAIRE_ID, "
                    + "PERSONNE_ID, ADRESSE_ID, MOTIF_ID, SOUSMOTIF_ID, NUMERORAPPEL, DATERAPPEL, PERIODERAPPEL, COMMENTAIRE, STATUT_ID, "
                    + "TYPE, MEDIA, URGENT, SITE, APPEL_ID, APPELANT_ID, RECLAMATION, COURRIER_ID) "
                    + "VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, ?, ?, ?, ?,"
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?,"
                    + "?, ?, ?, ?, ?, ?, ?, ? ) ";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, (String) daf.get(CrmForms._teleacteur_id));
            stmt.setString(2, (String) daf.get(CrmForms._mutuelle_id));
            stmt.setString(3, entite_gestion_id);
            stmt.setString(4, etablissement_id);
            stmt.setString(5, adherent_id);
            stmt.setString(6, beneficiaire_id);

            stmt.setString(7, personne_id);
            stmt.setString(8, adresse_id);
            stmt.setString(9, _EvenementAppel); 
            stmt.setString(10, _CodeFicheTransfert);
            stmt.setString(11, numero_rappel);
            stmt.setString(12, date_rappel);
            stmt.setString(13, periode_rappel);
            stmt.setString(14, commentaires);
            stmt.setString(15, _StatutEnvoye); 

            stmt.setString(16, entrant_sortant);
            stmt.setString(17, _MediaAppel);
            stmt.setString(18, (String) daf.get(CrmForms._traitement_urgent));
            stmt.setString(19, site);
            stmt.setString(20, appel_id);
            stmt.setString(21, appelant_id);
            stmt.setString(22, (String) daf.get(CrmForms._reclamation));
            stmt.setString(23, SQ_COURRIER_LOT_NEXTVAL);
            stmt.execute();
            stmt.clearParameters();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("creerEvenementFicheDeTransfert", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static Evenement getEvenementById(String idEvenement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Evenement unDisplay = null;
            String requete = "SELECT e.ID, e.DATE_CREATION, e.CREATEUR_ID, e.MUTUELLE_ID, e.ENTITE_ID, e.ETABLT_ID, e.ADHERENT_ID, e.BENEFICIAIRE_ID,"
                    + "e.PERSONNE_ID, e.ADRESSE_ID, e.MOTIF_ID, e.SOUSMOTIF_ID, e.NUMERORAPPEL, e.DATERAPPEL, e.PERIODERAPPEL, e.COMMENTAIRE, e.STATUT_ID, e.DATE_MAJ,"
                    + "e.USERMAJ_ID, e.DATEEDITION, e.TYPE, e.MEDIA, e.URGENT, e.NPAI, e.COURRIER_ID, e.MANQUANT, e.RENVOI, e.SITE, e.SOUS_STATUT_ID, e.JDOCLASS,"
                    + "e.APPEL_ID, e.APPELANT_ID, e.RECLAMATION, e.CLE_REFEXTERNE, mut.LIBELLE, motif.LIBELLE, sous_motif.LIBELLE, statut.LIBELLE, sous_statut.LIBELLE, "
                    + "e.COL13, e.COL14, e.EMAIL "
                    + "FROM EVENEMENT.EVENEMENT e,  EVENEMENT.EVENEMENT_MOTIF motif,  EVENEMENT.EVENEMENT_S_MOTIF sous_motif, "
                    + "EVENEMENT.STATUT statut, EVENEMENT.SOUS_STATUT sous_statut, APPLICATION.MUTUELLE mut "
                    + "WHERE e.ID = ? AND e.MUTUELLE_ID = mut.ID(+) "
                    + "AND e.MOTIF_ID = motif.ID(+) AND e.SOUSMOTIF_ID = sous_motif.ID(+) "
                    + "AND e.STATUT_ID = statut.ID(+) AND e.SOUS_STATUT_ID = sous_statut.ID(+)  ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEvenement);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Evenement();

                unDisplay.setID(rs.getString(1));
                unDisplay.setDATE_CREATION((rs.getTimestamp(2) != null) ? rs
                        .getTimestamp(2) : null);
                unDisplay.setCREATEUR_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setMUTUELLE_ID((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setENTITE_ID((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setETABLT_ID((rs.getString(6) != null) ? rs
                        .getString(6) : "");
                unDisplay.setADHERENT_ID((rs.getString(7) != null) ? rs
                        .getString(7) : "");
                unDisplay.setBENEFICIAIRE_ID((rs.getString(8) != null) ? rs
                        .getString(8) : "");

                unDisplay.setPERSONNE_ID((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setADRESSE_ID((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setMOTIF_ID((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setSOUSMOTIF_ID((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setNUMERORAPPEL((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setDATERAPPEL((rs.getTimestamp(14) != null) ? rs
                        .getTimestamp(14) : null);
                unDisplay.setPERIODERAPPEL((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setCOMMENTAIRE((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                unDisplay.setSTATUT_ID((rs.getString(17) != null) ? rs
                        .getString(17) : "");
                unDisplay.setDATE_MAJ((rs.getTimestamp(18) != null) ? rs
                        .getTimestamp(18) : null);

                unDisplay.setUSERMAJ_ID((rs.getString(19) != null) ? rs
                        .getString(19) : "");
                unDisplay.setDATEEDITION((rs.getTimestamp(20) != null) ? rs
                        .getTimestamp(20) : null);
                unDisplay.setTYPE((rs.getString(21) != null) ? rs.getString(21)
                        : "");
                unDisplay.setMEDIA((rs.getString(22) != null) ? rs
                        .getString(22) : "");
                unDisplay.setURGENT((rs.getString(23) != null) ? rs
                        .getString(23) : "");
                unDisplay.setNPAI((rs.getString(24) != null) ? rs.getString(24)
                        : "");
                unDisplay.setCOURRIER_ID((rs.getString(25) != null) ? rs
                        .getString(25) : "");
                unDisplay.setMANQUANT((rs.getString(26) != null) ? rs
                        .getString(26) : "");
                unDisplay.setRENVOI((rs.getString(27) != null) ? rs
                        .getString(27) : "");
                unDisplay.setSITE((rs.getString(28) != null) ? rs.getString(28)
                        : "");
                unDisplay.setSOUS_STATUT_ID((rs.getString(29) != null) ? rs
                        .getString(29) : "");
                unDisplay.setJDOCLASS((rs.getString(30) != null) ? rs
                        .getString(30) : "");

                unDisplay.setAPPEL_ID((rs.getString(31) != null) ? rs
                        .getString(31) : "");
                unDisplay.setAPPELANT_ID((rs.getString(32) != null) ? rs
                        .getString(32) : "");
                unDisplay.setRECLAMATION((rs.getString(33) != null) ? rs
                        .getString(33) : "");
                unDisplay.setCLE_REFEXTERNE((rs.getString(34) != null) ? rs
                        .getString(34) : "");

                unDisplay.setMutuelle((rs.getString(35) != null) ? rs
                        .getString(35) : "");
                unDisplay.setMotif((rs.getString(36) != null) ? rs
                        .getString(36) : "");
                unDisplay.setSousMotif((rs.getString(37) != null) ? rs
                        .getString(37) : "");
                unDisplay.setStatut((rs.getString(38) != null) ? rs
                        .getString(38) : "");
                unDisplay.setSousStatut((rs.getString(39) != null) ? rs
                        .getString(39) : "");

                unDisplay.setCOL13((rs.getTimestamp(40) != null) ? rs
                        .getTimestamp(40) : null); 
                unDisplay.setCOL14((rs.getTimestamp(41) != null) ? rs
                        .getTimestamp(41) : null); 

                unDisplay.setEMAIL(rs.getString(42));

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getEvenementById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static ComplementsInfosEvenement getComplementsInfosEvenement(
            String idEvenement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            ComplementsInfosEvenement unDisplay = null;
            String requete = "select cou.LOT, cou.TCO_ID, nvl(cou.FLAG_HISTO,'0'), cou.DATE_INSERTION, cou.DATE_IDENTIFICATION, "
                    + "cou.DATE_TRAITEMENT, cou.DATE_RECEPTION_POSTE, cou.ORIGINAL, cou.USERSCAN, cou.USERIDENTIF, cou.USERTRAIT, "
                    + "cou.EXPEDITEUR, cou.SUJET, cou.CC, COU.DESTINATAIRE, tco.LIBELLE "
                    + "FROM EVENEMENT.EVENEMENT e left outer join GED.COURRIER cou on (e.COURRIER_ID = cou.LOT and cou.page = 1 "
                    + ") "
                    + "left outer join GED.T_COURRIERS_ORIGINE_TCO tco on (COU.TCO_ID = TCO.ID) WHERE e.ID = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idEvenement);
            rs = stmt.executeQuery();
            if(rs.next()) {
                unDisplay = new ComplementsInfosEvenement();

                unDisplay.setLOT((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setTCO_ID((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setFLAG_HISTO((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setDATE_INSERTION((rs.getTimestamp(4) != null) ? rs
                        .getTimestamp(4) : null);
                unDisplay
                        .setDATE_IDENTIFICATION((rs.getTimestamp(5) != null) ? rs
                                .getTimestamp(5) : null);

                unDisplay.setDATE_TRAITEMENT((rs.getTimestamp(6) != null) ? rs
                        .getTimestamp(6) : null);
                unDisplay
                        .setDATE_RECEPTION_POSTE((rs.getTimestamp(7) != null) ? rs
                                .getTimestamp(7) : null);
                unDisplay.setORIGINAL((rs.getString(8) != null) ? rs
                        .getString(8) : "");
                unDisplay.setUSERSCAN((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setUSERIDENTIF((rs.getString(10) != null) ? rs
                        .getString(10) : "");
                unDisplay.setUSERTRAIT((rs.getString(11) != null) ? rs
                        .getString(11) : "");

                unDisplay.setEXPEDITEUR((rs.getString(12) != null) ? rs
                        .getString(12) : "");
                unDisplay.setSUJET((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setCC((rs.getString(14) != null) ? rs.getString(14)
                        : "");
                unDisplay.setDESTINATAIRE((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setCourrierOrigine((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                rs.close();
            }
            else{
                return null;
            }
            stmt.clearParameters();
            stmt.close();
            
            // Blobs associés :
            // - si FLAG_HISTO = 0 alors table = GED.DVS_MASTER_IMAGE dvs
            // - sinon table = GED.DVS_MASTER_IMAGE@HISTO_GED

            if (!_FAUX.equals(unDisplay.getFLAG_HISTO())) {
                // connexion à la base histo
                conn.close();
                conn = SQLDataService.getConnexionBaseHisto();
            }

            requete = "select dvs.N_DOCUMENT_ID, dvs.C_MIME, cou.PAGE, cou.PAGE_PJ "
                    + "FROM GED.COURRIER cou, GED.DVS_MASTER_IMAGE dvs "
                    + "WHERE COU.LOT = ? AND cou.N_DOCUMENT_ID = dvs.N_DOCUMENT_ID "
                    + "ORDER by COU.PAGE ASC";

            
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, unDisplay.getLOT());
            rs = stmt.executeQuery();

            LigneDVS ligne = null;
            Collection<LigneDVS> lignes_images = new ArrayList<LigneDVS>();
            while (rs.next()) {
                ligne = new LigneDVS();
                ligne.setN_DOCUMENT_ID((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                ligne.setC_MIME((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                ligne.setPJ(rs.getString(4));
                lignes_images.add(ligne);
            }

            unDisplay.setInfosCourriers(lignes_images);

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getComplementsInfosEvenement", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StreamFile getFileStream(Map<String, String> criteres) {

        Connection conn = null;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        StreamFile unDisplay = null;

        String numeroBlob = (String) criteres.get("numeroBlob");
        String courrierID = (String) criteres.get("courrierID");
        String imageID = (String) criteres.get("imageID");
        String base = (String) criteres.get("base");

        try {

            if ("C".equals(base)) {
                conn = getConnexion();
            } else {
                conn = SQLDataService.getConnexionBaseHisto();
            }

            if ("2".equals(numeroBlob)) {
                stmt = conn
                        .prepareStatement("SELECT BL_MASTER_IMAGE, C_MIME from GED.DVS_MASTER_IMAGE dmi, GED.COURRIER c where dmi.N_DOCUMENT_ID = c.N_DOCUMENT_ID and c.LOT = ?");
                stmt.setString(1, courrierID);
            } else if ("3".equals(numeroBlob)) {
                stmt = conn
                        .prepareStatement("SELECT BL_MASTER_IMAGE, C_MIME from GED.DVS_MASTER_IMAGE dmi where dmi.N_DOCUMENT_ID = ?");
                stmt.setString(1, imageID);
            } else if ("4".equals(numeroBlob)) {
                stmt = conn
                        .prepareStatement("SELECT BL_MASTER_IMAGE, C_MIME from GED.DVS_MASTER_IMAGE dmi where dmi.N_DOCUMENT_ID = ?");
                stmt.setString(1, imageID);
            }

            if (conn != null) {

                rs = stmt.executeQuery();

                if (rs.next()) {
                    unDisplay = new StreamFile();

                    if ("2".equals(numeroBlob)) {
                        unDisplay.setBlob(rs.getBlob(1));
                        unDisplay.setExtension(rs.getString(2));
                    } else if ("3".equals(numeroBlob)) {
                        unDisplay.setBlob(rs.getBlob(1));
                        unDisplay.setExtension(rs.getString(2));
                    } else if ("4".equals(numeroBlob)) {
                        unDisplay.setBlob(rs.getBlob(1));
                        unDisplay.setExtension(rs.getString(2));
                    }
                }
            }

            return unDisplay;

        } catch (Exception e) {
            LOGGER.error("getFileStream", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static ModeleEdition getModeleEditionById(String idModele) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            ModeleEdition unDisplay = null;
            String requete = "SELECT me.ID, me.LIBELLE, me.FICHIER, me.REPERTOIRE, me.FONCTION "
                    + "FROM HOTLINE.T_MODELE_EDITION me " + "WHERE me.ID = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idModele);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ModeleEdition();
                unDisplay.setID(rs.getString(1));
                unDisplay.setLIBELLE((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setFICHIER((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setREPERTOIRE((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setFONCTION((rs.getString(5) != null) ? rs
                        .getString(5) : "");
            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getModeleEditionById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static InfoPec getInfoPecById(String idPec) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            InfoPec unDisplay = null;

            String requete = "select "
                    + "    a.nom                               			, "
                    + "    s.libelle                            			, "
                    + "    m.libelle                            			, "
                    + "    de.IPC_EVE_ID                            		, "
                    + "    de.IPC_DATEINSERT                        		, "
                    + "    de.IPC_ADHNOM||' '||IPC_ADHPRENOM              , "
                    + "    substr(de.IPC_ADHNUMSS,1,13)                   , "
                    + "    substr(de.IPC_ADHNUMSS,14,2)                   , "
                    + "    de.IPC_BENEFNOM||' '||de.IPC_BENEFPRENOM       , "
                    + "    substr(de.IPC_BENEFNUMSS,1,13)                 , "
                    + "    substr(de.IPC_BENEFNUMSS,14,2)                 , "
                    + "    de.IPC_ETABLISSEMENT                        	, "
                    + "    de.IPC_ETABNUMFINESS                        	, "
                    + "    de.IPC_ETABTYPE                            	, "
                    + "    de.IPC_ETABADRESSE1                        	, "
                    + "    de.IPC_ETABADRESSE2                        	, "
                    + "    de.IPC_ETABADRESSE3                        	, "
                    + "    de.IPC_ETABCP                            		, "
                    + "    de.IPC_ETABVILLE                        		, "
                    + "    de.IPC_ETABTEL                            		, "
                    + "    de.IPC_ETABFAX                            		, "
                    + "    de.IPC_ENTREEDATE                        		, "
                    + "    de.IPC_ENTRENUM                            	, "
                    + "    case  "
                    + "        when de.IPC_ENVOICOURRIER='1' then 'OK' "
                    + "        else 'KO' "
                    + "        end                             			, "
                    + "    case "
                    + "        when de.IPC_ENVOIFAX='1' then 'OK' "
                    + "        else 'KO' "
                    + " 		 end                            			, "
                    + "    case "
                    + "        when de.IPC_ENVOIEMAIL='1' then 'OK' "
                    + "        else 'KO' "
                    + "        end                            			, "
                    + "    de.IPC_COMMENTAIRES                        	, "
                    + "    case  "
                    + "        when de.IPC_TM='1' then 'OK' "
                    + "        else 'KO'  "
                    + "        end                                		, "
                    + "    case"
                    + "		when de.IPC_TYPECODE = '137' then '1'  "
                    + "		when de.IPC_TYPECODE = '223' then '2'  "
                    + "		when de.IPC_TYPECODE = '165' then '3'  "
                    + "		when de.IPC_TYPECODE = '230' then '4'  "
                    + "		when de.IPC_TYPECODE = '168' then '5'  "
                    + "		when de.IPC_TYPECODE = '6'	 then '6'  "
                    + "		end	                          				, "
                    + "    de.IPC_DUREE                            		, "
                    + "    case  "
                    + "        when de.IPC_FJ='1' then 'OK' "
                    + "        else 'KO' "
                    + "        end                            			, "
                    + "    de.IPC_FJLIMITE                            	, "
                    + "    case "
                    + "        when de.IPC_CHP='1' then 'OK' "
                    + "        else 'KO'  "
                    + "        end                                		, "
                    + "    de.IPC_CHPPLAFOND                        		, "
                    + "    de.IPC_CHPLIMITE                        		, "
                    + "    case  "
                    + "        when de.IPC_LITPARENT='1' then 'OK' "
                    + "        else 'KO' "
                    + "		 end                        				, "
                    + "    case  "
                    + "        when trim(de.IPC_AUTRESFRAIS) is not null then 'OK'  "
                    + "        else 'KO' end                         		, "
                    + "    de.IPC_AUTRESFRAIS                        		, "
                    + "    de.IPC_EMAILDESTINATAIRE                    	 "
                    + "from " + "    evenement.evenement         e, "
                    + "    application.mutuelle        m, "
                    + "    evenement.statut            s, "
                    + "    evenement.t_infospec_ipc    de, "
                    + "    hotline.teleacteur          a " + "where							 "
                    + "		e.id = ?				 " + "    and e.id = de.ipc_eve_id   "
                    + "    and e.statut_id = s.id    "
                    + "    and e.mutuelle_id = m.id "
                    + "    and a.id = e.createur_id ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPec);
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Nouvelle version avec table détail
                unDisplay = new InfoPec();
                unDisplay.setCreateur(rs.getString(1));
                unDisplay.setStatut(rs.getString(2));
                unDisplay.setMutuelle(rs.getString(3));
                unDisplay.setId(rs.getString(4));
                unDisplay.setDateCreation(rs.getString(5));
                unDisplay.setNom_prenom_adherent(rs.getString(6));
                unDisplay.setNumSS_adherent(rs.getString(7));
                unDisplay.setCleSS_adherent(rs.getString(8));
                unDisplay.setNom_prenom_beneficiaire(rs.getString(9));
                unDisplay.setNumSS_beneficiaire(rs.getString(10));
                unDisplay.setCleSS_beneficiaire(rs.getString(11));
                unDisplay.setEtablissementRS_appelant(rs.getString(12));
                unDisplay.setNumFiness_appelant(rs.getString(13));
                unDisplay.setTypeEtablissement(rs.getString(14));
                unDisplay.setAdresse1_appelant(rs.getString(15));
                unDisplay.setAdresse2_appelant(rs.getString(16));
                unDisplay.setAdresse3_appelant(rs.getString(17));
                unDisplay.setCodepostal_appelant(rs.getString(18));
                unDisplay.setVille_appelant(rs.getString(19));
                unDisplay.setTel_appelant_fixe(rs.getString(20));
                unDisplay.setFax_appelant(rs.getString(21));
                unDisplay.setStrDateEntree(rs.getString(22));
                unDisplay.setNumEntree(rs.getString(23));
                unDisplay.setEnvoiCourrier(rs.getString(24));
                unDisplay.setEnvoiFax(rs.getString(25));
                unDisplay.setEnvoiMail(rs.getString(26));
                unDisplay.setCommentaire(rs.getString(27));
                unDisplay.setTicketModerateur(rs.getString(28));
                unDisplay.setTypeHospitalisation(rs.getString(29));
                unDisplay.setDureeHospitalisation(rs.getString(30));
                unDisplay.setForfaitJournalier(rs.getString(31));
                unDisplay.setNbrJourLimiteForfaitJournalier(rs.getString(32));
                unDisplay.setChambreParticuliere(rs.getString(33));
                unDisplay.setPlafondChambre(rs.getString(34));
                unDisplay.setLimiteChambre(rs.getString(35));
                unDisplay.setLitParent(rs.getString(36));
                unDisplay.setPrecision(rs.getString(37));
                unDisplay.setPrecisionAutre(rs.getString(38));
                unDisplay.setAdresseEnvoiMail(rs.getString(39));

                stmt.clearParameters();
            } else {
                String requeteBis = "SELECT "
                        + "	A.NOM, "
                        + "	s.LIBELLE, "
                        + "	mut.LIBELLE,  "
                        + "	pec.ID, "
                        + "	pec.date_creation, "
                        + "	case when HB.ID is not null and thb.alias = 'HB' then HB.NOM||' '||HB.PRENOM "
                        + "	     else PADH.NOM||' '||PADH.PRENOM "
                        + "	end, "
                        + "	case when HB.ID is not null and thb.alias = 'HB' then HB.NUMEROSS "
                        + "	     else ADH.NUMEROSS "
                        + "	end, "
                        + "	case when HB.ID is not null and thb.alias = 'HB' then HB.CLESS "
                        + " 	    else ADH.CLESS "
                        + "	end, "
                        + "	case when pec.col27 is not null then pec.col27||' '||pec.col28 "
                        + "	     else PBENEF.NOM||' '||PBENEF.PRENOM "
                        + "	end, "
                        + "	case when pec.col27 is not null then pec.col29 "
                        + "	     else BENEF.NUMEROSS " + "	end, "
                        + "	case when pec.col27 is not null then pec.col30 "
                        + "	     else BENEF.CLESS " + "	end, " + "	pec.col01, "
                        + "	pec.col02, " + "	pec.col17, " + "	pec.col05, "
                        + "	pec.col06, " + "	pec.col07, " + "	pec.col08, "
                        + "	pec.col09, " + "	pec.col03, " + "	pec.col04, "
                        + "	TO_CHAR(pec.col13,'yyyymmdd'), " + "	pec.col10, "
                        + "	case when pec.sortie_courrier = '1' then 'OK' "
                        + "	     else 'KO' " + "	end, "
                        + "	case when pec.sortie_fax = '1' then 'OK' "
                        + "     else 'KO' " + "	end,  "
                        + "	case when pec.sortie_email = '1' then 'OK' "
                        + "	     else 'KO' " + "	end,   "
                        + "	pec.commentaire, "
                        + "	case when pec.col16 = '1' then 'OK' "
                        + "	     else 'KO' " + "	end, " + "	pec.col15, "
                        + "	pec.col18, "
                        + "	case when pec.col33 = '1' then 'OK' "
                        + "	     else 'KO' " + "	end, " + "	pec.col19, "
                        + "	case when pec.col32 = '1' then 'OK' "
                        + "	     else 'KO' " + "	end, " + "	pec.col21, "
                        + "	pec.col20, "
                        + "	case when pec.col24= '1' then 'OK' "
                        + "	     else 'KO' " + "	end, "
                        + "	case when pec.col11 = '1' then 'OK' "
                        + "	     else 'KO' " + "	end, " + "	pec.col11, "
                        + "	pec.email " + "FROM EVENEMENT.PEC pec, "
                        + "     EVENEMENT.STATUT s, "
                        + "     APPLICATION.MUTUELLE mut, "
                        + "     hotline.teleacteur          a, "
                        + "     hotline.appelant            hb, "
                        + "     application.beneficiaire    adh, "
                        + "     APPLICATION.PERSONNE        padh, "
                        + "     application.beneficiaire    benef, "
                        + "     APPLICATION.PERSONNE        pbenef, "
                        + "     hotline.codes				  thb	" + "WHERE pec.ID = ? "
                        + "    AND pec.STATUT_ID = s.ID "
                        + "    AND pec.MUTUELLE_ID = mut.ID "
                        + "    and a.id = pec.CREATEUR_ID "
                        + "    and pec.appelant_id = hb.id(+) "
                        + "    and hb.TYPE_CODE = thb.code "
                        + "    and pec.adherent_id = adh.id(+) "
                        + "    and adh.personne_id = padh.id(+) "
                        + "    and pec.beneficiaire_id = benef.id(+) "
                        + "    and benef.personne_id = pbenef.id(+)";

                stmt = conn.prepareStatement(requeteBis);
                stmt.setString(1, idPec);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    unDisplay = new InfoPec();

                    unDisplay.setCreateur(rs.getString(1));
                    unDisplay.setStatut(rs.getString(2));
                    unDisplay.setMutuelle(rs.getString(3));
                    unDisplay.setId(rs.getString(4));
                    unDisplay.setDateCreation(rs.getString(5));
                    unDisplay.setNom_prenom_adherent(rs.getString(6));
                    unDisplay.setNumSS_adherent(rs.getString(7));
                    unDisplay.setCleSS_adherent(rs.getString(8));
                    unDisplay.setNom_prenom_beneficiaire(rs.getString(9));
                    unDisplay.setNumSS_beneficiaire(rs.getString(10));
                    unDisplay.setCleSS_beneficiaire(rs.getString(11));
                    unDisplay.setEtablissementRS_appelant(rs.getString(12));
                    unDisplay.setNumFiness_appelant(rs.getString(13));
                    unDisplay.setTypeEtablissement(rs.getString(14));
                    unDisplay.setAdresse1_appelant(rs.getString(15));
                    unDisplay.setAdresse2_appelant(rs.getString(16));
                    unDisplay.setAdresse3_appelant(rs.getString(17));
                    unDisplay.setCodepostal_appelant(rs.getString(18));
                    unDisplay.setVille_appelant(rs.getString(19));
                    unDisplay.setTel_appelant_fixe(rs.getString(20));
                    unDisplay.setFax_appelant(rs.getString(21));
                    unDisplay.setStrDateEntree(rs.getString(22));
                    unDisplay.setNumEntree(rs.getString(23));
                    unDisplay.setEnvoiCourrier(rs.getString(24));
                    unDisplay.setEnvoiFax(rs.getString(25));
                    unDisplay.setEnvoiMail(rs.getString(26));
                    unDisplay.setCommentaire(rs.getString(27));
                    unDisplay.setTicketModerateur(rs.getString(28));
                    unDisplay.setTypeHospitalisation(rs.getString(29));
                    unDisplay.setDureeHospitalisation(rs.getString(30));
                    unDisplay.setForfaitJournalier(rs.getString(31));
                    unDisplay.setNbrJourLimiteForfaitJournalier(rs
                            .getString(32));
                    unDisplay.setChambreParticuliere(rs.getString(33));
                    unDisplay.setPlafondChambre(rs.getString(34));
                    unDisplay.setLimiteChambre(rs.getString(35));
                    unDisplay.setLitParent(rs.getString(36));
                    unDisplay.setPrecision(rs.getString(37));
                    unDisplay.setPrecisionAutre(rs.getString(38));
                    unDisplay.setAdresseEnvoiMail(rs.getString(39));
                }
            }

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getInfoPecById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static DemandePec getDemandePecById(String idPec) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DemandePec unDisplay = null;

            String requete = "select " 
                    + "a.nom                                          , "  
                    + "s.libelle                                      , "
                    + "m.libelle                                      , " 
                    + "e.id                                           , "
                    + "E.DATE_CREATION   DATEINSERT                   , "
                    + "padh.NOM||' '||padh.PRENOM                     , "
                    + "ADH.NUMEROSS                                   , "
                    + "ADH.CLESS                                      , "
                    + "PBEN.NOM||' '||pben.PRENOM                     , "
                    + "BEN.NUMEROSS                                   , "
                    + "BEN.CLESS                                      , "
                    + "e.col01  ETABLISSEMENT                         , "
                    + "e.col02  ETABNUMFINESS                         , " 
                    + "e.col05  ETABADRESSE1                          , "
                    + "e.col06  ETABADRESSE2                          , "
                    + "e.col07  ETABADRESSE3                          , "
                    + "e.col08  ETABCP                                , "
                    + "e.col09  ETABVILLE                             , "
                    + "e.col03  ETABTEL                               , "
                    + "e.col04  ETABFAX                               , "
                    + "TO_CHAR(e.col13,'YYYYMMDD')  HOSPITALISATIONDATEENTREE , "
                    + "e.col10  HOSPITALISATIONNUMEROENTREE           , "
                    + "e.col18  TYPEHOSPITALISATION                   , "
                    + "NVL(e.col11,' ')  TYPEDMT                      , "
                    + "e.col15  FRAISSEJOUR                           , "
                    + "e.col16  FRAISFORFAIT18                        , "
                    + "e.col17  FRAISFORFAITJOURNALIER                , "
                    + "e.col23  MODEHOSPITALISATION                   , "
                    + "e.col24  FRAISCHAMBREPARTICULIERE              , "
                    + "e.col32  FRAISLITACCOMPAGNANT                  , "
                    + "e.col33  FRAISHONORAIRE                        , "     
                    + "NVL(e.COMMENTAIRE,' ')                         , " 
                    + "e.SORTIE_FAX                                   , "
                    + "e.SORTIE_EMAIL                                 , "
                    + "e.EMAIL                                        , "
                    + "e.COL12 FAX                                    , "
                    + "e.COL19 OPERATEUR                              , "
                    + "e.COL20 ORGANISME                              , "
                    + "TO_CHAR(padh.DATENAISSANCE,'YYYYMMDD')         , "
                    + "TO_CHAR(pben.DATENAISSANCE,'YYYYMMDD')         , "
                    + "e.COURRIER_ID                                    "
                + "from evenement.evenement   e " 
                + "join application.mutuelle  m on e.mutuelle_id = m.id " 
                + "join evenement.statut      s on e.statut_id = s.id "
                + "join hotline.teleacteur    a on a.id = e.createur_id "
                + "join application.beneficiaire adh on adh.id = e.adherent_id "
                + "join application.personne padh on padh.id = adh.personne_id "  
                + "left outer join application.beneficiaire ben on ben.id = e.beneficiaire_id "
                + "left outer join application.personne pben on pben.id = ben.personne_id  " 
                + "where e.id = ?";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPec);
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Nouvelle version avec table détail
                unDisplay = new DemandePec();
                unDisplay.setCreateur(rs.getString(1));
                unDisplay.setStatut(rs.getString(2));
                unDisplay.setMutuelle(rs.getString(3));
                unDisplay.setId(rs.getString(4));
                unDisplay.setDateCreation(rs.getString(5));
                
                unDisplay.setNom_prenom_adherent(rs.getString(6));
                unDisplay.setNumSS_adherent(rs.getString(7));
                unDisplay.setCleSS_adherent(rs.getString(8));
                unDisplay.setNom_prenom_beneficiaire(rs.getString(9));
                unDisplay.setNumSS_beneficiaire(rs.getString(10));
                unDisplay.setCleSS_beneficiaire(rs.getString(11));
                
                unDisplay.setEtablissementRS_appelant(rs.getString(12));
                unDisplay.setNumFiness_appelant(rs.getString(13));
                unDisplay.setAdresse1_appelant(rs.getString(14));
                unDisplay.setAdresse2_appelant(rs.getString(15));
                unDisplay.setAdresse3_appelant(rs.getString(16));
                unDisplay.setCodepostal_appelant(rs.getString(17));
                unDisplay.setVille_appelant(rs.getString(18));
                unDisplay.setTel_appelant_fixe(rs.getString(19));
                unDisplay.setFax_appelant(rs.getString(20));
                
                unDisplay.setStrDateEntree(rs.getString(21));
                unDisplay.setNumEntree(rs.getString(22));
                unDisplay.setTypeHospitalisation(rs.getString(23));
                unDisplay.setCodeDMT(rs.getString(24));
               
                unDisplay.setFraisSejour(rs.getString(25));
                unDisplay.setForfait18(rs.getString(26));
                unDisplay.setForfaitJournalier(rs.getString(27));
                unDisplay.setModeTraitementHospitalisation(rs.getString(28));
                unDisplay.setChambreParticuliere(rs.getString(29));
                unDisplay.setLitParent(rs.getString(30));
                unDisplay.setHonoraire(rs.getString(31));
                
                unDisplay.setCommentaire(rs.getString(32));
                
                if( _VRAI.equals( rs.getString(33) ) ){
                    unDisplay.setCanal(_MediaFax);
                    unDisplay.setAdrOperateur(rs.getString(36));
                }
                else{
                    unDisplay.setCanal(_MediaCourriel); 
                    unDisplay.setAdrOperateur(rs.getString(35));
                }
                unDisplay.setOperateur(rs.getString(37));
                unDisplay.setOrganisme(rs.getString(38));
             
                unDisplay.setStrDnai_adherent(rs.getString(39));
                unDisplay.setStrDnai_beneficiaire(rs.getString(40));
                unDisplay.setLotGED(rs.getString(41));
            }

            stmt.clearParameters();
            
            
            requete = "select dvs.N_DOCUMENT_ID, dvs.C_MIME, cou.PAGE, cou.PAGE_PJ,DECODE(cou.FLAG_HISTO,1,'H','C') "
                    + "FROM GED.COURRIER cou, GED.DVS_MASTER_IMAGE dvs "
                    + "WHERE COU.LOT = ? AND cou.N_DOCUMENT_ID = dvs.N_DOCUMENT_ID "
                    + "ORDER by COU.PAGE ASC";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, unDisplay.getLotGED() );
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                LigneDVS ligne = new LigneDVS();
                ligne.setN_DOCUMENT_ID((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                ligne.setC_MIME((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                ligne.setPJ(rs.getString(4));
                unDisplay.setDocument(ligne);
                unDisplay.setDocumentHisto(rs.getString(5));
            }
            
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getDemandePecById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<DemandePec> getDemandePecByAppelId(String idAppel) {

        Connection conn = null;
        PreparedStatement stmtEvt = null;
        ResultSet rsEvt = null;
        
        PreparedStatement stmtDoc = null;
        ResultSet rsDoc = null;
        
        ArrayList<DemandePec> results = new ArrayList<DemandePec>();
        
        try {
            DemandePec unDisplay = null;

            conn = getConnexion();
            
            String requeteDoc = "select dvs.N_DOCUMENT_ID, dvs.C_MIME, cou.PAGE, cou.PAGE_PJ,DECODE(cou.FLAG_HISTO,1,'H','C') "
                    + "FROM GED.COURRIER cou, GED.DVS_MASTER_IMAGE dvs "
                    + "WHERE COU.LOT = ? AND cou.N_DOCUMENT_ID = dvs.N_DOCUMENT_ID "
                    + "ORDER by COU.PAGE ASC";
            stmtDoc = conn.prepareStatement(requeteDoc);
            
            String requeteEvt = "select " 
                    + "a.nom                                          , "  
                    + "s.libelle                                      , "
                    + "m.libelle                                      , " 
                    + "e.id                                           , "
                    + "E.DATE_CREATION   DATEINSERT                   , "
                    + "padh.NOM||' '||padh.PRENOM                     , "
                    + "ADH.NUMEROSS                                   , "
                    + "ADH.CLESS                                      , "
                    + "PBEN.NOM||' '||pben.PRENOM                     , "
                    + "BEN.NUMEROSS                                   , "
                    + "BEN.CLESS                                      , "
                    + "e.col01  ETABLISSEMENT                         , "
                    + "e.col02  ETABNUMFINESS                         , " 
                    + "e.col05  ETABADRESSE1                          , "
                    + "e.col06  ETABADRESSE2                          , "
                    + "e.col07  ETABADRESSE3                          , "
                    + "e.col08  ETABCP                                , "
                    + "e.col09  ETABVILLE                             , "
                    + "e.col03  ETABTEL                               , "
                    + "e.col04  ETABFAX                               , "
                    + "TO_CHAR(e.col13,'YYYYMMDD')  HOSPITALISATIONDATEENTREE , "
                    + "e.col10  HOSPITALISATIONNUMEROENTREE           , "
                    + "e.col18  TYPEHOSPITALISATION                   , "
                    + "NVL(e.col11,' ')  TYPEDMT                      , "
                    + "e.col15  FRAISSEJOUR                           , "
                    + "e.col16  FRAISFORFAIT18                        , "
                    + "e.col17  FRAISFORFAITJOURNALIER                , "
                    + "e.col23  MODEHOSPITALISATION                   , "
                    + "e.col24  FRAISCHAMBREPARTICULIERE              , "
                    + "e.col32  FRAISLITACCOMPAGNANT                  , "
                    + "e.col33  FRAISHONORAIRE                        , "     
                    + "e.COMMENTAIRE, " 
                    + "e.SORTIE_FAX, "
                    + "e.SORTIE_EMAIL, "
                    + "e.EMAIL, "
                    + "e.COL12 FAX, "
                    + "e.COL19 OPERATEUR, "
                    + "e.COL20 ORGANISME, "
                    + "TO_CHAR(padh.DATENAISSANCE,'YYYYMMDD'),"
                    + "TO_CHAR(pben.DATENAISSANCE,'YYYYMMDD'), "
                    + "e.COURRIER_ID "
                + "from evenement.evenement   e " 
                + "join application.mutuelle  m on e.mutuelle_id = m.id " 
                + "join evenement.statut      s on e.statut_id = s.id "
                + "join hotline.teleacteur    a on a.id = e.createur_id "
                + "join application.beneficiaire adh on adh.id = e.adherent_id "
                + "join application.personne padh on padh.id = adh.personne_id "  
                + "left outer join application.beneficiaire ben on ben.id = e.beneficiaire_id "
                + "left outer join application.personne pben on pben.id = ben.personne_id  " 
                + "where e.appel_id = ? and e.jdoclass = ?";
            stmtEvt = conn.prepareStatement(requeteEvt);
            
            stmtEvt.setString(1, idAppel);
            stmtEvt.setString(2,_PecDemande);
            rsEvt = stmtEvt.executeQuery();
            while (rsEvt.next()) {
                
                unDisplay = new DemandePec();
                unDisplay.setCreateur(rsEvt.getString(1));
                unDisplay.setStatut(rsEvt.getString(2));
                unDisplay.setMutuelle(rsEvt.getString(3));
                unDisplay.setId(rsEvt.getString(4));
                unDisplay.setDateCreation(rsEvt.getString(5));
                
                unDisplay.setNom_prenom_adherent(rsEvt.getString(6));
                unDisplay.setNumSS_adherent(rsEvt.getString(7));
                unDisplay.setCleSS_adherent(rsEvt.getString(8));
                unDisplay.setNom_prenom_beneficiaire(rsEvt.getString(9));
                unDisplay.setNumSS_beneficiaire(rsEvt.getString(10));
                unDisplay.setCleSS_beneficiaire(rsEvt.getString(11));
                
                unDisplay.setEtablissementRS_appelant(rsEvt.getString(12));
                unDisplay.setNumFiness_appelant(rsEvt.getString(13));
                unDisplay.setAdresse1_appelant(rsEvt.getString(14));
                unDisplay.setAdresse2_appelant(rsEvt.getString(15));
                unDisplay.setAdresse3_appelant(rsEvt.getString(16));
                unDisplay.setCodepostal_appelant(rsEvt.getString(17));
                unDisplay.setVille_appelant(rsEvt.getString(18));
                unDisplay.setTel_appelant_fixe(rsEvt.getString(19));
                unDisplay.setFax_appelant(rsEvt.getString(20));
                
                unDisplay.setStrDateEntree(rsEvt.getString(21));
                unDisplay.setNumEntree(rsEvt.getString(22));
                unDisplay.setTypeHospitalisation(rsEvt.getString(23));
                unDisplay.setCodeDMT(rsEvt.getString(24));
               
                unDisplay.setFraisSejour(rsEvt.getString(25));
                unDisplay.setForfait18(rsEvt.getString(26));
                unDisplay.setForfaitJournalier(rsEvt.getString(27));
                unDisplay.setModeTraitementHospitalisation(rsEvt.getString(28));
                unDisplay.setChambreParticuliere(rsEvt.getString(29));
                unDisplay.setLitParent(rsEvt.getString(30));
                unDisplay.setHonoraire(rsEvt.getString(31));
                
                unDisplay.setCommentaire(rsEvt.getString(32));
                
                if( _VRAI.equals( rsEvt.getString(33) ) ){
                    unDisplay.setCanal(_MediaFax);
                    unDisplay.setAdrOperateur(rsEvt.getString(36));
                }
                else{
                    unDisplay.setCanal(_MediaCourriel); 
                    unDisplay.setAdrOperateur(rsEvt.getString(35));
                }
                unDisplay.setOperateur(rsEvt.getString(37));
                unDisplay.setOrganisme(rsEvt.getString(38));
             
                unDisplay.setStrDnai_adherent(rsEvt.getString(39));
                unDisplay.setStrDnai_beneficiaire(rsEvt.getString(40));
                unDisplay.setLotGED(rsEvt.getString(41));
                
                stmtDoc.clearParameters();
                stmtDoc.setString(1, unDisplay.getLotGED() );
                rsDoc = stmtDoc.executeQuery();
                
                if(rsDoc.next()) {
                    LigneDVS ligne = new LigneDVS();
                    ligne.setN_DOCUMENT_ID((rsDoc.getString(1) != null) ? rsDoc.getString(1) : "");
                    ligne.setC_MIME((rsDoc.getString(2) != null) ? rsDoc.getString(2)
                            : "");
                    ligne.setPJ(rsDoc.getString(4));
                    unDisplay.setDocument(ligne);
                    unDisplay.setDocumentHisto(rsDoc.getString(5));
                }
                
                results.add(unDisplay);
            }
            
            return results;
        } catch (Exception e) {
            LOGGER.error("getDemandePecByAppelId", e);
            return null;
        } finally {
            closeRsStmtConn(rsEvt,stmtEvt,conn);
            closeRsStmtConn(rsDoc,stmtDoc,conn);
        }
    }

    
    
    public static Pec getPecById(String idPec) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Pec unDisplay = null;
            String requete = "SELECT pec.ID, pec.APPELANT_ID, pec.BENEFICIAIRE_ID, pec.ADHERENT_ID, pec.CREATEUR_ID, "
                    + " pec.APPEL_ID, pec.MUTUELLE_ID, pec.COL26, pec.PERSONNE_ID, pec.ADRESSE_ID, pec.ETABLT_ID, pec.ENTITE_ID, "
                    + " pec.COMMENTAIRE, pec.DATE_CREATION, pec.COL17, "
                    + "pec.COL15, "
                    + "pec.COL13, pec.COL10, pec.COL18, pec.SORTIE_FAX, pec.SORTIE_EMAIL, pec.SORTIE_COURRIER, pec.EMAIL, "
                    + " pec.COL01, pec.COL02, pec.COL16, pec.COL24, pec.COL33, pec.COL19, pec.COL32, pec.COL21, pec.COL20, pec.COL11, "
                    + " pec.MEDIA, pec.TYPE, pec.URGENT, pec.STATUT_ID, pec.MOTIF_ID, pec.SOUSMOTIF_ID, pec.COL08, pec.COL05, pec.COL06, "
                    + " pec.COL07, pec.COL09, pec.COL04, pec.COL03,  "
                    + " nvl(pec.COL27,DETAILPEC.IPC_BENEFNOM), nvl(pec.COL28, DETAILPEC.IPC_BENEFPRENOM ), nvl(pec.COL29,substr(DETAILPEC.IPC_BENEFNUMSS,1,13)), nvl(pec.COL30,substr(DETAILPEC.IPC_BENEFNUMSS,14,2) ), pec.COL31, "
                    + " detailpec.IPC_ADHNOM,detailpec.IPC_ADHPRENOM,substr(detailpec.IPC_ADHNUMSS,1,13),substr(detailpec.IPC_ADHNUMSS,14,2), "
                    + " s.LIBELLE, mut.LIBELLE "
                    + "FROM EVENEMENT.PEC pec, EVENEMENT.T_INFOSPEC_IPC detailpec, EVENEMENT.STATUT s, APPLICATION.MUTUELLE mut  "
                    + "WHERE pec.ID = ? "
                    + "AND pec.STATUT_ID = s.ID(+) "
                    + "AND pec.MUTUELLE_ID = mut.ID(+) "
                    + "AND pec.ID = DETAILPEC.IPC_EVE_ID(+)";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idPec);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Pec();
                unDisplay.setId(rs.getString(1));
                unDisplay.setIdProfessionnelSante(rs.getString(2));
                unDisplay.setIdBeneficiaire(rs.getString(3));
                unDisplay.setIdAdherent(rs.getString(4));
                unDisplay.setIdCreateur(rs.getString(5));

                unDisplay.setIdAppel(rs.getString(6));
                unDisplay.setIdMutuelle(rs.getString(7));
                unDisplay.setIdAdherentHorsBase(rs.getString(8));
                unDisplay.setIdPersonne(rs.getString(9));
                unDisplay.setIdAdresse(rs.getString(10));
                unDisplay.setIdEtablissement(rs.getString(11));
                unDisplay.setIdEntiteGestion(rs.getString(12));

                unDisplay.setCommentaire((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                unDisplay.setDateCreation(rs.getTimestamp(14));
                unDisplay.setTypeEtablissement((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setHospitalisation((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                unDisplay.setDateEntreeHospitalisation(rs.getTimestamp(17));
                unDisplay
                        .setNumeroEntreeHospitalisation((rs.getString(18) != null) ? rs
                                .getString(18) : "");
                unDisplay
                        .setDureeHospitalisation((rs.getString(19) != null) ? rs
                                .getString(19) : "");
                unDisplay.setEnvoiFax((rs.getString(20) != null) ? rs
                        .getString(20) : "");
                unDisplay.setEnvoiMail((rs.getString(21) != null) ? rs
                        .getString(21) : "");
                unDisplay.setEnvoiCourrier((rs.getString(22) != null) ? rs
                        .getString(22) : "");
                unDisplay.setAdresseMailEnvoi((rs.getString(23) != null) ? rs
                        .getString(23) : "");

                unDisplay.setRaisonSociale((rs.getString(24) != null) ? rs
                        .getString(24) : "");
                unDisplay.setNumFiness((rs.getString(25) != null) ? rs
                        .getString(25) : "");
                unDisplay.setTicketModerateur((rs.getString(26) != null) ? rs
                        .getString(26) : "");
                unDisplay.setLitParent((rs.getString(27) != null) ? rs
                        .getString(27) : "");
                unDisplay.setForfaitJournalier((rs.getString(28) != null) ? rs
                        .getString(28) : "");
                unDisplay
                        .setForfaitJournalierLimiteJours((rs.getString(29) != null) ? rs
                                .getString(29) : "");
                unDisplay
                        .setChambreParticuliere((rs.getString(30) != null) ? rs
                                .getString(30) : "");
                unDisplay.setChambreParticulierePlafondJournalier((rs
                        .getString(31) != null) ? rs.getString(31) : "");
                unDisplay
                        .setChambreParticuliereLimiteJours((rs.getString(32) != null) ? rs
                                .getString(32) : "");
                unDisplay.setAutreFrais((rs.getString(33) != null) ? rs
                        .getString(33) : "");

                unDisplay.setMedia((rs.getString(34) != null) ? rs
                        .getString(34) : "");
                unDisplay.setType((rs.getString(35) != null) ? rs.getString(35)
                        : "");
                unDisplay.setUrgence((rs.getString(36) != null) ? rs
                        .getString(36) : "");
                unDisplay.setStatut_id(rs.getString(37));
                unDisplay.setMotif_id(rs.getString(38));
                unDisplay.setSous_motif_id(rs.getString(39));

                unDisplay
                        .setCodePostalDestinataire((rs.getString(40) != null) ? rs
                                .getString(40) : "");
                unDisplay
                        .setLigne_1Destinataire((rs.getString(41) != null) ? rs
                                .getString(41) : "");
                unDisplay
                        .setLigne_2Destinataire((rs.getString(42) != null) ? rs
                                .getString(42) : "");
                unDisplay
                        .setLigne_3Destinataire((rs.getString(43) != null) ? rs
                                .getString(43) : "");
                unDisplay
                        .setLocaliteDestinataire((rs.getString(44) != null) ? rs
                                .getString(44) : "");
                unDisplay
                        .setTelecopieDestinataire((rs.getString(45) != null) ? rs
                                .getString(45) : "");
                unDisplay
                        .setTelephone_fixeDestinataire((rs.getString(46) != null) ? rs
                                .getString(46) : "");

                unDisplay
                        .setNomBeneficiaireHorsBase((rs.getString(47) != null) ? rs
                                .getString(47) : "");
                unDisplay
                        .setPrenomBeneficiaireHorsBase((rs.getString(48) != null) ? rs
                                .getString(48) : "");
                unDisplay
                        .setNumSecuBeneficiaireHorsBase((rs.getString(49) != null) ? rs
                                .getString(49) : "");
                unDisplay
                        .setCleSecuBeneficiaireHorsBase((rs.getString(50) != null) ? rs
                                .getString(50) : "");
                unDisplay.setDateNaissance(rs.getTimestamp(51));

                unDisplay
                        .setNomAdherentHorsBase((rs.getString(52) != null) ? rs
                                .getString(52) : "");
                unDisplay
                        .setPrenomAdherentHorsBase((rs.getString(53) != null) ? rs
                                .getString(53) : "");
                unDisplay
                        .setNumSecuAdherentHorsBase((rs.getString(54) != null) ? rs
                                .getString(54) : "");
                unDisplay
                        .setCleSecuAdherentHorsBase((rs.getString(55) != null) ? rs
                                .getString(55) : "");

                unDisplay.setStatut((rs.getString(56) != null) ? rs
                        .getString(56) : "");
                unDisplay.setMutuelle((rs.getString(57) != null) ? rs
                        .getString(57) : "");

            }
            stmt.clearParameters();
            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getPecById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getSites() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();
        try {

            LibelleCode unDisplay = null;
            String requete = "select distinct PRS.PRS_ID,  PRS.PRS_PRENOM "
                    + "FROM H_ANNUAIRE.T_PERSONNES_PRS prs "
                    + "WHERE PRS.PRS_NOM = 'HOSTA' "
                    + "AND prs_prenom not in ('SUPERVISEUR', 'RABAT') "
                    + "AND PRS.PRS_ACTIF = 1 order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getSites", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Mutuelle> getMutuellesHabilitees(
            String teleacteur_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Mutuelle> res = new ArrayList<Mutuelle>();
        try {

            Mutuelle unDisplay = null;
            String requete = "select distinct MUT.ID, upper(MUT.LIBELLE), MUT.ACTIF "
                    + "FROM HOTLINE.teleacteurentitegestion teleeg, APPLICATION.mutuelle mut, "
                    + "APPLICATION.ENTITE_GESTION eg "
                    + "WHERE TELEEG.TELEACTEUR_ID = ? "
                    + "AND TELEEG.ENTITEGESTION_ID = EG.ID "
                    + "AND EG.MUTUELLE_ID = mut.id " + "order by 2 asc";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Mutuelle();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setActif(rs.getString(3));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getMutuellesHabilitees", e);
            return new ArrayList<Mutuelle>();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Mutuelle> getMutuelles() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Mutuelle> res = new ArrayList<Mutuelle>();
        try {

            Mutuelle unDisplay = null;
            String requete = "select distinct MUT.ID, upper(MUT.LIBELLE), MUT.ACTIF "
                    + "FROM application.mutuelle mut "
                    + "ORDER BY 2 ASC";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Mutuelle();
                unDisplay.setId(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                unDisplay.setActif(rs.getString(3));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getMutuelles", e);
            return new ArrayList<Mutuelle>();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static Collection<LigneExcel> getStatistiquesFichesAppels(
            Map<String, String> criteres) {
        // N'afficher que les fiches sur lesquelles le teleacteurs est habilité
        // Exclure RABAT

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LigneExcel> res = new ArrayList<LigneExcel>();
        LigneExcel unDisplay = null;
        try {

            String SITE_ID = (String) criteres.get("SITE_ID");
            String TELEACTEUR_ID = (String) criteres.get("TELEACTEUR_ID");
            String CREATEUR_ID = (String) criteres.get("CREATEUR_ID");
            String MUTUELLE_ID = (String) criteres.get("MUTUELLE_ID");
            String REFERENCE_ID = (String) criteres.get("REFERENCE_ID");
            String STATUT_ID = (String) criteres.get("STATUT_ID");
            String DATEDEBUT = (String) criteres.get("DATEDEBUT");
            String DATEFIN = (String) criteres.get("DATEFIN");
            String RESOLU = (String) criteres.get("RESOLU");
            
            
            String filtre_site = " and TCREAT.SITE = ? ";
            String filtre_client = " and mut.ID = ? ";
            String filtre_reference = " and smoa.REFERENCE_ID = ? ";
            String filtre_createur = " and a.CREATEUR_ID = ? ";
            String filtre_statut = " and a.CLOTURE_CODE = ? ";
            String filtre_date_fin = " and trunc(a.DATEAPPEL) <= trunc(to_date(?,'dd/mm/YYYY')) ";
            String filtre_resolu_oui = " and a.RESOLU = '1' ";
            String filtre_resolu_non = " and a.RESOLU = '0' ";
            
            StringBuilder requete_globale = new StringBuilder();

            /* ASSURE : HABILITATIONS SUR EG */
            requete_globale
                    .append("SELECT TCREAT.SITE as SITE, MUT.LIBELLE as client, rst.rst_libelle AS typefiche, "
                            + " cod_type_appelant.LIBELLE as TYPEAPPELANT, prs.nom || ' ' || prs.prenom AS NOMAPPELANT, ben.code AS CODEAPPELANT, a.dateappel, a.datecloture, "
                            + " tcreat.NOM || ' ' || tcreat.PRENOM AS createur, tclot.NOM || ' ' || tclot.prenom AS clotureur, "
                            + " cod_cloture.libelle AS statut, a.ID, DECODE(a.resolu,1,'oui',0,'non','?') resolu, a.commentaire,moa.libelle as motif,smoa.libelle as smotif "
                            +" ,eg.libelle as lib_entite_gestion "
                            + "FROM hotline.appel a, hotline.teleacteur tcreat, hotline.teleacteur tclot, hotline.codes cod_cloture, "
                            + " application.mutuelle mut, application.beneficiaire ben, application.personne prs, "
                            + " hotline.codes cod_type_appelant, hotline.teleacteurentitegestion teg, "
                           + " application.ENTITE_GESTION eg, "
                            + " hotline.smotifappel smoa, hotline.motifappel moa,evenement.t_refs_stats_rst rst "
                            + "WHERE a.codeappelant_selectionne = cod_type_appelant.code "
                            + " AND cod_type_appelant.alias = 'ASSURE' AND a.beneficiaire_id = ben.ID(+) "
                            + " AND BEN.PERSONNE_ID = prs.id(+) AND a.mutuelle_id = mut.ID AND a.cloture_code = cod_cloture.code "
                            + " AND a.createur_id = tcreat.ID AND A.CLOTUREUR_ID = tclot.ID(+) AND teg.entitegestion_id = a.ENTITEGESTION_ID "
                            + " and a.ENTITEGESTION_ID = eg.id(+) "
                            + " AND teg.teleacteur_id = ? AND a.s_motif_id = smoa.ID(+) AND a.motif_id = moa.ID(+) AND smoa.reference_id = rst.rst_id(+) "
                            + " AND tcreat.SITE <> 'RABAT' AND TRUNC (a.dateappel) >= TRUNC (TO_DATE (?, 'dd/mm/yyyy'))");

            // FILTRES
            if (!"".equals(SITE_ID)) {
                requete_globale.append(filtre_site);
            }
            if (!"".equals(MUTUELLE_ID)) {
                requete_globale.append(filtre_client);
            }
            if (!"".equals(REFERENCE_ID)) {
                requete_globale.append(filtre_reference);
            }
            if (!"".equals(CREATEUR_ID)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(STATUT_ID)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(DATEFIN)) {
                requete_globale.append(filtre_date_fin);
            }
            if ("1".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_oui);
            }
            else if ("0".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_non);
            }
            
            requete_globale.append(" UNION ALL ");

            /* ENTREPRISE : HABILITATIONS SUR EG */
            requete_globale
                    .append("select TCREAT.SITE as SITE, MUT.LIBELLE as client, rst.rst_libelle AS typefiche, "
                            + "cod_type_appelant.LIBELLE as TYPEAPPELANT, ETAB.LIBELLE AS NOMAPPELANT, NULL AS CODEAPPELANT, a.dateappel, a.datecloture, "
                            + "tcreat.NOM || ' ' || tcreat.PRENOM AS createur, tclot.NOM || ' ' || tclot.prenom AS clotureur, "
                            + "cod_cloture.libelle AS statut, a.ID, DECODE(a.resolu,1,'oui',0,'non','?') resolu, a.commentaire,moa.libelle as motif,smoa.libelle as smotif "
                             + ",eg.libelle as lib_entite_gestion "
                            + "FROM hotline.appel a, hotline.teleacteur tcreat, hotline.teleacteur tclot, hotline.codes cod_cloture, "
                            + "application.mutuelle mut, application.etablissement etab, "
                            + "hotline.codes cod_type_appelant, hotline.teleacteurentitegestion teg, "
                            + " application.ENTITE_GESTION eg, "
                            + "hotline.smotifappel smoa, evenement.t_refs_stats_rst rst, "
                            + "hotline.motifappel moa "
                            + "WHERE a.codeappelant_selectionne = cod_type_appelant.code "
                            + "AND cod_type_appelant.alias = 'ENTREPRISE' "
                            + "AND a.ETABLISSEMENT_ID = etab.ID(+) AND a.mutuelle_id = mut.ID "
                            + "AND a.cloture_code = cod_cloture.code "
                            + "AND a.createur_id = tcreat.ID "
                            + "AND A.CLOTUREUR_ID = tclot.ID(+) "
                            + "AND teg.entitegestion_id = a.ENTITEGESTION_ID "
                            + "AND a.ENTITEGESTION_ID = eg.id(+) "
                            + "AND teg.teleacteur_id = ? "
                            + "AND smoa.reference_id = rst.rst_id(+) "
                            + "AND a.s_motif_id = smoa.ID(+) AND a.motif_id = moa.ID(+) "
                            + "AND tcreat.SITE <> 'RABAT' "
                            + "AND TRUNC (a.dateappel) >= TRUNC (TO_DATE (?, 'dd/mm/yyyy'))");

            // FILTRES
            if (!"".equals(SITE_ID)) {
                requete_globale.append(filtre_site);
            }
            if (!"".equals(MUTUELLE_ID)) {
                requete_globale.append(filtre_client);
            }
            if (!"".equals(REFERENCE_ID)) {
                requete_globale.append(filtre_reference);
            }
            if (!"".equals(CREATEUR_ID)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(STATUT_ID)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(DATEFIN)) {
                requete_globale.append(filtre_date_fin);
            }
            if ("1".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_oui);
            }
            else if ("0".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_non);
            }
            
            
            requete_globale.append(" UNION ALL ");

            /* APPELANT : HABILITATION SUR LA MUTUELLE */
            requete_globale
                    .append("select TCREAT.SITE as SITE, MUT.LIBELLE as client, rst.rst_libelle AS typefiche, "
                            + "cod_type_appelant.LIBELLE as TYPEAPPELANT, app.NOM || ' ' || app.PRENOM || ' ' || APP.ETABLISSEMENT_RS AS NOMAPPELANT, "
                            + "APP.CODEADHERENT as CODEAPPELANT, a.dateappel, a.datecloture, tcreat.NOM || ' ' || tcreat.PRENOM AS createur, "
                            + "tclot.NOM || ' ' || tclot.prenom AS clotureur, cod_cloture.libelle AS statut, a.ID, DECODE(a.resolu,1,'oui',0,'non','?') resolu,a.commentaire,moa.libelle as motif,smoa.libelle as smotif  "
                            + ",'AUCUNE' as lib_entite_gestion "
                            + "FROM hotline.appel a, hotline.teleacteur tcreat, hotline.teleacteur tclot, hotline.codes cod_cloture, "
                            + "application.mutuelle mut, hotline.appelant app , "
                            + "hotline.codes cod_type_appelant, HOTLINE.TELEACTEURCAMPAGNE tc, hotline.CAMPMUT cm, "
                            + "hotline.smotifappel smoa, evenement.t_refs_stats_rst rst, "
                            + "hotline.motifappel moa "
                            + "WHERE a.codeappelant_selectionne IS NOT NULL AND a.codeappelant_selectionne = cod_type_appelant.code(+) "
                            + "AND (cod_type_appelant.code IS NULL OR cod_type_appelant.ALIAS NOT IN ('ASSURE', 'ENTREPRISE')) "
                            + "and A.APPELANT_ID = app.ID(+) AND a.cloture_code = cod_cloture.code "
                            + "AND a.createur_id = tcreat.ID AND A.CLOTUREUR_ID = tclot.ID(+) "
                            + "and A.MUTUELLE_ID = mut.id and A.CAMPAGNE_ID = CM.CAMPAGNE_ID "
                            + "AND tc.CAMPAGNE_ID = cm.CAMPAGNE_ID AND cm.MUTUELLE_ID = a.MUTUELLE_ID "
                            + "AND a.s_motif_id = smoa.ID(+) AND a.motif_id = moa.ID(+) "
                            + "AND tc.TELEACTEUR_ID = ? AND smoa.reference_id = rst.rst_id(+) AND tcreat.SITE <> 'RABAT' "
                            + "AND TRUNC (a.dateappel) >= TRUNC (TO_DATE (?, 'dd/mm/yyyy'))");

            // FILTRES
            if (!"".equals(SITE_ID)) {
                requete_globale.append(filtre_site);
            }
            if (!"".equals(MUTUELLE_ID)) {
                requete_globale.append(filtre_client);
            }
            if (!"".equals(REFERENCE_ID)) {
                requete_globale.append(filtre_reference);
            }
            if (!"".equals(CREATEUR_ID)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(STATUT_ID)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(DATEFIN)) {
                requete_globale.append(filtre_date_fin);
            }
            if ("1".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_oui);
            }
            else if ("0".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_non);
            }
            
            // AJOUT
            requete_globale.append(" UNION ALL ");

            /* AUCUN APPELANT : HABILITATION SUR LA MUTUELLE */
            requete_globale
                    .append("select TCREAT.SITE as SITE, MUT.LIBELLE as client, rst.rst_libelle AS typefiche, "
                            + "NULL as TYPEAPPELANT, null AS NOMAPPELANT, "
                            + "null as CODEAPPELANT, a.dateappel, a.datecloture, tcreat.NOM || ' ' || tcreat.PRENOM AS createur, "
                            + "tclot.NOM || ' ' || tclot.prenom AS clotureur, cod_cloture.libelle AS statut, a.ID, DECODE(a.resolu,1,'oui',0,'non','?') resolu, a.commentaire,moa.libelle as motif,smoa.libelle as smotif  "
                            + ",'AUCUNE' as lib_entite_gestion "
                            + "FROM hotline.appel a, hotline.teleacteur tcreat, hotline.teleacteur tclot, hotline.codes cod_cloture, "
                            + "application.mutuelle mut, HOTLINE.TELEACTEURCAMPAGNE tc, hotline.CAMPMUT cm, "
                            + "hotline.smotifappel smoa, evenement.t_refs_stats_rst rst, "
                            + "hotline.motifappel moa "
                            + "WHERE a.codeappelant_selectionne is null "
                            + "AND a.mutuelle_id = mut.ID AND a.cloture_code = cod_cloture.code "
                            + "AND a.createur_id = tcreat.ID AND A.CLOTUREUR_ID = tclot.ID(+) "
                            + "and A.CAMPAGNE_ID = CM.CAMPAGNE_ID "
                            + "AND tc.CAMPAGNE_ID = cm.CAMPAGNE_ID AND cm.MUTUELLE_ID = a.MUTUELLE_ID "
                            + "AND a.s_motif_id = smoa.ID(+) AND a.motif_id = moa.ID(+) "
                            + "AND tc.TELEACTEUR_ID = ? AND smoa.reference_id = rst.rst_id(+) AND tcreat.SITE <> 'RABAT' "
                            + "AND TRUNC (a.dateappel) >= TRUNC (TO_DATE (?, 'dd/mm/yyyy'))");

            // FILTRES
            if (!"".equals(SITE_ID)) {
                requete_globale.append(filtre_site);
            }
            if (!"".equals(MUTUELLE_ID)) {
                requete_globale.append(filtre_client);
            }
            if (!"".equals(REFERENCE_ID)) {
                requete_globale.append(filtre_reference);
            }
            if (!"".equals(CREATEUR_ID)) {
                requete_globale.append(filtre_createur);
            }
            if (!"".equals(STATUT_ID)) {
                requete_globale.append(filtre_statut);
            }
            if (!"".equals(DATEFIN)) {
                requete_globale.append(filtre_date_fin);
            }
            if ("1".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_oui);
            }
            else if ("0".equals(RESOLU)) {
                requete_globale.append(filtre_resolu_non);
            }
            
            // AJOUT
            requete_globale.append(" ORDER BY ID DESC ");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete_globale.toString());

            int compteur_bind = 1;

            for (int j = 0; j < 4; j++) {
                stmt.setString(compteur_bind, TELEACTEUR_ID);
                compteur_bind++;

                stmt.setString(compteur_bind, DATEDEBUT);
                compteur_bind++;

                if (!"".equals(SITE_ID)) {
                    stmt.setString(compteur_bind, SITE_ID);
                    compteur_bind++;
                }

                if (!"".equals(MUTUELLE_ID)) {
                    stmt.setString(compteur_bind, MUTUELLE_ID);
                    compteur_bind++;
                }

                if (!"".equals(REFERENCE_ID)) {
                    stmt.setString(compteur_bind, REFERENCE_ID);
                    compteur_bind++;
                }

                if (!"".equals(CREATEUR_ID)) {
                    stmt.setString(compteur_bind, CREATEUR_ID);
                    compteur_bind++;
                }

                if (!"".equals(STATUT_ID)) {
                    stmt.setString(compteur_bind, STATUT_ID);
                    compteur_bind++;
                }

                if (!"".equals(DATEFIN)) {
                    stmt.setString(compteur_bind, DATEFIN);
                    compteur_bind++;
                }
     
            }

            rs = stmt.executeQuery();
            int nbEnr = 0;
            while (rs.next()) {
            	
            	nbEnr++;
            	
            	if (nbEnr > 100000) {
            		throw new Exception("Nombre de résultats trop important ! Impossible de charger le tableau");
            	}
            	
                unDisplay = new LigneExcel();

                unDisplay.setSITE((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setCLIENT((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setTYPEFICHE((rs.getString(3) != null) ? rs
                        .getString(3) : "");
                unDisplay.setTYPEAPPELANT((rs.getString(4) != null) ? rs
                        .getString(4) : "");
                unDisplay.setNOMAPPELANT((rs.getString(5) != null) ? rs
                        .getString(5) : "");
                unDisplay.setCODE_APPELANT((rs.getString(6) != null) ? rs
                        .getString(6) : "");

                unDisplay.setDATE_APPEL((rs.getTimestamp(7) != null) ? rs
                        .getTimestamp(7) : null);
                unDisplay.setDATE_CLOTURE((rs.getTimestamp(8) != null) ? rs
                        .getTimestamp(8) : null);

                unDisplay.setCREATEUR((rs.getString(9) != null) ? rs
                        .getString(9) : "");
                unDisplay.setCLOTUREUR((rs.getString(10) != null) ? rs
                        .getString(10) : "");

                unDisplay.setSTATUT((rs.getString(11) != null) ? rs
                        .getString(11) : "");
                unDisplay.setIDFICHE((rs.getString(12) != null) ? rs
                        .getString(12) : "");

                unDisplay.setRESOLU((rs.getString(13) != null) ? rs
                        .getString(13) : "");
                
                unDisplay.setCOMMENTAIRE((rs.getString(14) != null) ? rs
                        .getString(14) : "");
                unDisplay.setMotif((rs.getString(15) != null) ? rs
                        .getString(15) : "");
                unDisplay.setSous_motif((rs.getString(16) != null) ? rs
                        .getString(16) : "");
                unDisplay.setLib_entite_gestion((rs.getString(17) != null) ? rs
                        .getString(17) : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getStatistiquesFichesAppels", e);
            return new ArrayList<LigneExcel>();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getTypesDossiers(String mutuelle_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {
            conn = getConnexion();
            LibelleCode unDisplay = null;
            String requete = "SELECT DISTINCT ES.ID AS MOTIF_ID, ES.LIBELLE AS MOTIF_LIBELLE "
                    + "FROM EVENEMENT.EVENEMENT_S_MOTIF ES, EVENEMENT.T_ACTIONSCLIENTSMOTIFS_ACM ACM, "
                    + "EVENEMENT.T_ACTIONS_ACT ACT, APPLICATION.MUTUELLE AM "
                    + "WHERE ACM.ACM_ACTION_ID = ACT.ACT_ID "
                    + "AND ACM.ACM_MOTIF_ID = ES.ID "
                    + "AND ACM.ACM_MUTUELLE_ID = AM.ID(+) "
                    + "AND ACT.ACT_LIB = 'TRAITEMENT' "
                    + "AND AM.ID = ? ORDER BY 2 asc";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, mutuelle_id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setCode(rs.getString(1));
                unDisplay.setLibelle(rs.getString(2));
                res.add(unDisplay);
            }

            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getTypesDossiers", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder creerEvenementChangementMotDePasseWeb(
            String appel_id, DynaActionForm daf, HttpSession session) {

        Connection conn = null;
        PreparedStatement stmt = null;

        StringBuilder sb = new StringBuilder();

        try {
            String requete = "";

            ObjetAppelant objet_appelant = (ObjetAppelant) session
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String beneficiaire_id = "";
            String personne_id = "";
            String adresse_id = "";
            String adherent_id = "";
            String reclamation = "1";
            String mutuelle_id = "";
            String teleacteur_id = "";
            
            String email_confirmation = "";

            mutuelle_id = (String) daf.get(CrmForms._mutuelle_id);
            teleacteur_id = (String) daf.get(CrmForms._teleacteur_id);
            email_confirmation = (String) daf.get(CrmForms._email_confirmation);

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet_appelant
                        .getObjet();
                if (beneficiaire != null) {
                    beneficiaire_id = beneficiaire.getID();
                    adherent_id = beneficiaire.getAdherentId();
                    fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                            .getPersonne();
                    if (personne != null) {
                        personne_id = personne.getID();
                        adresse_id = personne.getADRESSE_ID();
                    }
                }
            }

              requete = "INSERT INTO EVENEMENT.EVENEMENT "
                    + "(ID, DATE_CREATION, CREATEUR_ID, MUTUELLE_ID, ADHERENT_ID, BENEFICIAIRE_ID, "
                    + "PERSONNE_ID, ADRESSE_ID, MOTIF_ID, SOUSMOTIF_ID, STATUT_ID, "
                    + "TYPE, MEDIA, APPEL_ID, RECLAMATION,  EMAIL, JDOCLASS) "
                    + "VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, ?, ?,"
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ? ) ";

            conn = getConnexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, teleacteur_id);
            stmt.setString(2, mutuelle_id);
            stmt.setString(3, adherent_id);
            stmt.setString(4, beneficiaire_id);

            stmt.setString(5, personne_id);
            stmt.setString(6, adresse_id);
            stmt.setString(7, _AppelMotif);
            stmt.setString(8, _MotDePasseSousMotif);
            stmt.setString(9, _StatutATraiter);

            stmt.setString(10, _ENTRANT);
            stmt.setString(11, _MediaAppel);
            stmt.setString(12, appel_id);
            stmt.setString(13, reclamation);
            stmt.setString(14, email_confirmation);
            stmt.setString(15, _EvtMotDePasseWeb);
            
            stmt.execute();

            conn.commit();
            sb.append(_VRAI);

            return sb;

        } catch (Exception e) {
            LOGGER.error("creerEvenementChangementMotDePasseWeb", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeStmtConn( stmt, conn);
        }

    }

    public static InfosBDD getInfosVersionBaseInstance() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        InfosBDD unDisplay = new InfosBDD();

        try {
            conn = getConnexion();

            String requete = "SELECT p.VALEUR as VERSION, SYS_CONTEXT('USERENV','DB_NAME') AS BASE, "
                    + "SYS_CONTEXT('USERENV','INSTANCE') AS INSTANCE "
                    + "FROM HOTLINE.PARAMETRAGE p "
                    + "WHERE  p.CLE = 'NUM_VERSION_HCONTACTS' ";

            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay.setVersion(rs.getString(1));
                unDisplay.setBase(rs.getString(2));
                unDisplay.setInstance(rs.getString(3));
            }

            return unDisplay;
        } catch (Exception e) {
            LOGGER.error("getInfosVersionBaseInstance", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getTransfertsForInputSelect() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT tra.TRA_LIBELLE, tra.TRA_EMAIL "
                    + "FROM HOTLINE.T_TRANSFERTS_TRA tra " + "order by 1 ASC";
	//requete = "SELECT tra.TRA_LIBELLE, tra.TRA_EMAIL FROM HOTLINE.T_TRANSFERTS_TRA tra where tra_id=1021 order by 1 ASC";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            sb.append("<TABLE width='100%'>");

            while (rs.next()) {
                sb.append("<TR>");
                sb.append("<TD width='1px' nowrap='nowrap'><input type='checkbox' name='ckb_transfert' value='"
                        + rs.getString(2) + "'></TD>");
                sb.append("<TD nowrap='nowrap' class='noir11'>"
                        + rs.getString(1) + "</TD>");
                sb.append("<TD nowrap='nowrap' class='noir11'>"
                        + rs.getString(2) + "</TD>");
                sb.append("</TR>");

            }

            sb.append("</TABLE>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getTransfertsForInputSelect", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    public static StringBuilder getTransfertsForInputSelectMSG() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {

            String requete = "SELECT tra.TRA_LIBELLE, tra.TRA_EMAIL "
                    + "FROM HOTLINE.T_TRANSFERTS_TRA tra where tra_id =1021 " + "order by 1 ASC";
	//requete = "SELECT tra.TRA_LIBELLE, tra.TRA_EMAIL FROM HOTLINE.T_TRANSFERTS_TRA tra order by 1 ASC";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);

            rs = stmt.executeQuery();

            sb.append("<table width='100%'>");

            while (rs.next()) {
                sb.append("<tr>");
                sb.append("<td width='1px' nowrap='nowrap'><input type='checkbox' name='ckb_transfert' value='"
                        + rs.getString(2) + "'></td>");
                sb.append("<td nowrap='nowrap' class='noir11'>"
                        + rs.getString(1) + "</td>");
                sb.append("<td nowrap='nowrap' class='noir11'>"
                        + rs.getString(2) + "</td>");
                sb.append("</tr>");

            }

            sb.append("</table>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getTransfertsForInputSelect", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    public static StringBuilder getEtablissementHospitalierById(
            String etablissement_hospitalier_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");

        try {

            String requete = "SELECT app.ETABLISSEMENT_RS, app.NUMFINESS, app.ADR_TELEPHONEFIXE, "
                    + "app.ADR_TELEPHONEAUTRE, app.ADR_TELECOPIE, app.ADR_LIGNE_1, app.ADR_LIGNE_2, app.ADR_LIGNE_3, "
                    + "app.ADR_CODEPOSTAL, app.ADR_LOCALITE  "
                    + "FROM HOTLINE.APPELANT app " + "WHERE app.ID = ? ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, etablissement_hospitalier_id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                sb.append("<ETABLISSEMENTRS>");
                sb.append((rs.getString(1) != null) ? rs.getString(1) : "");
                sb.append("</ETABLISSEMENTRS>");

                sb.append("<NUMFINESS>");
                sb.append((rs.getString(2) != null) ? rs.getString(2) : "");
                sb.append("</NUMFINESS>");

                sb.append("<TELEPHONEFIXE>");
                sb.append((rs.getString(3) != null) ? rs.getString(3) : "");
                sb.append("</TELEPHONEFIXE>");

                sb.append("<TELEPHONEAUTRE>");
                sb.append((rs.getString(4) != null) ? rs.getString(4) : "");
                sb.append("</TELEPHONEAUTRE>");

                sb.append("<TELECOPIE>");
                sb.append((rs.getString(5) != null) ? rs.getString(5) : "");
                sb.append("</TELECOPIE>");

                sb.append("<ADR_LIGNE_1>");
                sb.append((rs.getString(6) != null) ? rs.getString(6) : "");
                sb.append("</ADR_LIGNE_1>");

                sb.append("<ADR_LIGNE_2>");
                sb.append((rs.getString(7) != null) ? rs.getString(7) : "");
                sb.append("</ADR_LIGNE_2>");

                sb.append("<ADR_LIGNE_3>");
                sb.append((rs.getString(8) != null) ? rs.getString(8) : "");
                sb.append("</ADR_LIGNE_3>");

                sb.append("<ADR_CODEPOSTAL>");
                sb.append((rs.getString(9) != null) ? rs.getString(9) : "");
                sb.append("</ADR_CODEPOSTAL>");

                sb.append("<ADR_LOCALITE>");
                sb.append((rs.getString(10) != null) ? rs.getString(10) : "");
                sb.append("</ADR_LOCALITE>");
            }

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getEtablissementHospitalierById", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder creerEvenementDemandePecHContacts(
            HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder res = new StringBuilder("");

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            TeleActeur teleacteur = (TeleActeur) request.getSession()
                    .getAttribute(IContacts._var_session_teleActeur);
            Mutuelle mutuelle = (Mutuelle) request.getSession().getAttribute(FicheAppelAction._var_session_mutuelle);
            
            ModelePEC leModelePEC = (ModelePEC)request.getSession().getAttribute(FicheAppelAction._var_session_pec);
            
            Appel appel = (Appel) request.getSession().getAttribute(FicheAppelAction._var_session_appel);

            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String createur_id = ""; 
            String mutuelle_id = "";
            String entite_id = "";
            String etablissement_id = "";
            String adherent_id = "";
            String beneficiaire_id = "";
            String personne_id = "";
            String adresse_id = "";
            
            String sous_motif_id = _DemandePECMotif;
            String commentaires = "";
            String statut_id = _StatutAttente;
            String type = _SORTANT;
            String media = "";
            String motif_id = "";
            String sortieFax = "0";
            String sortieEmail = "0";
            String email = "";
            String fax = "";
            
            if( leModelePEC.getEmissionCourriel() ){
                media =  _MediaCourriel;
                motif_id = _CourrielMotif;
                sortieEmail = "1";
                email = leModelePEC.getCourriel();
            } else {
                media =  _MediaFax;
                motif_id = _FaxMotif;
                sortieFax="1";
                fax = leModelePEC.getFax();
            }
            
            String operateur = leModelePEC.getOperateur();
            String organisme = leModelePEC.getOrganisme();
                    
            String jdoclass = _PecDemande;
            String appel_id = "";
            String col26 = "";
            String id_etablissement_hospitalier = "";
            String col01 = "", col02 = "", col03 = "", col04 = "", col05 = "", col06 = "", col07 = "", col08 = "", col09 = "", col10 = "";
            String col11 = "", col13 = "", col15 = "", col16 = "", col17 = "", col18 = "", col23 = "", col24 = "", col32 = "", col33 = "";
            
            createur_id = teleacteur.getId();
            mutuelle_id = mutuelle.getId();
            appel_id = appel.getID();

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet;
                adherent_id = beneficiaire.getAdherentId();
                beneficiaire_id = beneficiaire.getID();
                personne_id = beneficiaire.getPERSONNE_ID();
                fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                        .getPersonne();
                if (personne != null) {
                    adresse_id = personne.getADRESSE_ID();
                }

                entite_id = beneficiaire.getENTITE_GESTION_ID();
                etablissement_id = beneficiaire.getETABLISSEMENT_ID();

            } else {
                if (objet instanceof Appelant) {
                    Appelant appelant = (Appelant) objet;
                    if (appelant != null) {
                        col26 = appelant.getID();
                        stmt = conn
                                .prepareStatement("SELECT ID FROM APPLICATION.ENTITE_GESTION WHERE MUTUELLE_ID = ?");
                        stmt.setString(1, mutuelle_id);
                        stmt.execute();
                        rs = stmt.getResultSet();
                        entite_id = _entite_SansObjet;
                        if (rs.next()){
                            entite_id = rs.getString(1);
                        }    
                        if (rs.next()){
                            entite_id = _entite_SansObjet;
                        }    
                    }
                }
            }

            id_etablissement_hospitalier = (String) request.getParameter("id_etablissement_hospitalier");
            
            if (id_etablissement_hospitalier != null && !id_etablissement_hospitalier.trim().isEmpty()) {
            	sous_motif_id = _ssMotifPECHospit;
            }
            commentaires = (String) request.getParameter("commentaires");
            col01 = (String) request.getParameter("etablissement_raison_sociale");
            col02 = (String) request.getParameter("etablissement_num_finess");
            col03 = (String) request.getParameter("etablissement_telephone_fixe");
            col04 = (String) request.getParameter("etablissement_fax");
            col05 = (String) request.getParameter("etablissement_adresse1");
            col06 = (String) request.getParameter("etablissement_adresse2");
            col07 = (String) request.getParameter("etablissement_adresse3");
            col08 = (String) request.getParameter("etablissement_code_postal");
            col09 = (String) request.getParameter("etablissement_localite");
            col10 = (String) request.getParameter("hospitalisation_numero_entree");
            col11 = (String) request.getParameter("type_dmt");
            col13 = (String) request.getParameter("hospitalisation_date_entree");
            col15 = isOn(request,"frais_sejour");
            col16 = isOn(request,"frais_forfait18");
            col17 = isOn(request,"frais_forfait_journalier");
            col18 = (String) request.getParameter("type_hospitalisation");
            col23 = (String) request.getParameter("mode_hospitalisation");
            col24 = isOn(request,"frais_chambre_particuliere");
            col32 = isOn(request,"frais_lit_accompagnant");
            col33 = isOn(request,"frais_honoraire");

            String requete = "INSERT INTO EVENEMENT.EVENEMENT (ID, DATE_CREATION, CREATEUR_ID, MUTUELLE_ID, "
                    + "ENTITE_ID, ETABLT_ID, ADHERENT_ID, BENEFICIAIRE_ID, PERSONNE_ID, ADRESSE_ID, MOTIF_ID, SOUSMOTIF_ID, "
                    + "COMMENTAIRE, STATUT_ID, TYPE, MEDIA, JDOCLASS, APPEL_ID, APPELANT_ID, COL26,"
                    + "COL01, COL02, COL03, COL04, COL05, COL06, COL07, COL08, COL09, COL10, "
                    + "COL11, COL13, COL15, COL16, COL17, COL18, COL23, COL24, COL32, COL33, "
                    + "SORTIE_FAX, SORTIE_EMAIL, EMAIL, COL12, COL19, COL20 ) "
                    + " VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, "
                    + "?, ? , ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?,?,?,?,?,?)";

            stmt = conn.prepareStatement(requete);
            
            stmt.setString(1, createur_id);
            stmt.setString(2, mutuelle_id);

            stmt.setString(3, entite_id);
            stmt.setString(4, etablissement_id);
            stmt.setString(5, adherent_id);
            stmt.setString(6, beneficiaire_id);
            stmt.setString(7, personne_id);
            stmt.setString(8, adresse_id);
            stmt.setString(9, motif_id);
            stmt.setString(10, sous_motif_id);

            stmt.setString(11, commentaires);
            stmt.setString(12, statut_id);
            stmt.setString(13, type);
            stmt.setString(14, media);
            stmt.setString(15, jdoclass);
            stmt.setString(16, appel_id);
            stmt.setString(17, id_etablissement_hospitalier);
            stmt.setString(18, col26);

            stmt.setString(19, col01);
            stmt.setString(20, col02);
            stmt.setString(21, col03);
            stmt.setString(22, col04);
            stmt.setString(23, col05);
            stmt.setString(24, col06);
            stmt.setString(25, col07);
            stmt.setString(26, col08);
            stmt.setString(27, col09);
            stmt.setString(28, col10);
            
            stmt.setString(29, col11);
            stmt.setString(30,col13);
            stmt.setString(31,col15);
            stmt.setString(32,col16);
            stmt.setString(33,col17);
            stmt.setString(34,col18);
            stmt.setString(35,col23);
            stmt.setString(36,col24);
            stmt.setString(37,col32);
            stmt.setString(38,col33);

            stmt.setString(39,sortieFax);
            stmt.setString(40,sortieEmail);        
            stmt.setString(41,email);
            stmt.setString(42,fax);
            stmt.setString(43,operateur);
            stmt.setString(44,organisme);        
            
            stmt.executeUpdate();

            conn.commit();
            res.append(_VRAI);

            return res;
        } catch (Exception e) {
            LOGGER.error("creerEvenementDemandePecHContacts", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder creerPecHContacts(HttpServletRequest request) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder res = new StringBuilder("");

        try {
            conn = getConnexion();
            conn.setAutoCommit(false);

            TeleActeur teleacteur = (TeleActeur) request.getSession()
                    .getAttribute(IContacts._var_session_teleActeur);
            Mutuelle mutuelle = (Mutuelle) request.getSession().getAttribute(
                    "mutuelle");
            Appel appel = (Appel) request.getSession().getAttribute("appel");

            ObjetAppelant objet_appelant = (ObjetAppelant) request.getSession()
                    .getAttribute(FicheAppelAction._var_session_objet_appelant);
            Object objet = objet_appelant.getObjet();

            String createur_id = "", mutuelle_id = "", entite_id = "", etablissement_id = "", adherent_id = "", beneficiaire_id = "";
            String personne_id = "", adresse_id = "", motif_id = _AppelMotif, sous_motif_id = _PECSousMotif;
            String commentaires = "", statut_id = "4", type = _ENTRANT, media = _MediaAppel, urgence = "", jdoclass = _PecV, appel_id = "", col26 = "", id_etablissement_hospitalier = "";
            String col01 = "", col02 = "", col03 = "", col04 = "", col05 = "", col06 = "", col07 = "", col08 = "", col09 = "", col10 = "";
            String col11 = "", col13 = "", col15 = "", ticketModerateur = "", col17 = "", col18 = "", col19 = "", col20 = "", col21 = "", fraisChambreParticuliere = "", fraisForfaitJournalier = "";
            String sortie_fax = "", sortie_email = "", sortie_courrier = "", email = "";

            if (teleacteur != null) {
                createur_id = teleacteur.getId();
            }
            if (mutuelle != null) {
                mutuelle_id = mutuelle.getId();
            }
            if (appel != null) {
                appel_id = appel.getID();
            }

            if (objet instanceof Beneficiaire) {
                Beneficiaire beneficiaire = (Beneficiaire) objet;
                adherent_id = beneficiaire.getAdherentId();
                beneficiaire_id = beneficiaire.getID();
                personne_id = beneficiaire.getPERSONNE_ID();
                fr.igestion.crm.bean.contrat.Personne personne = (fr.igestion.crm.bean.contrat.Personne) beneficiaire
                        .getPersonne();
                if (personne != null) {
                    adresse_id = personne.getADRESSE_ID();
                }

                entite_id = beneficiaire.getENTITE_GESTION_ID();
                etablissement_id = beneficiaire.getETABLISSEMENT_ID();

            } else {
                if (objet instanceof Appelant) {
                    Appelant appelant = (Appelant) objet;
                    if (appelant != null) {
                        col26 = appelant.getID();
                        stmt = conn
                                .prepareStatement("SELECT ID FROM APPLICATION.ENTITE_GESTION WHERE MUTUELLE_ID = ?");
                        stmt.setString(1, mutuelle_id);
                        stmt.execute();
                        rs = stmt.getResultSet();
                        entite_id = _entite_SansObjet;
                        if (rs.next()){
                            entite_id = rs.getString(1);
                        }    
                        if (rs.next()){
                            entite_id = _entite_SansObjet;
                        }    
                    }
                }
            }

            id_etablissement_hospitalier = (String) request
                    .getParameter("id_etablissement_hospitalier");
            commentaires = (String) request.getParameter("commentaires");
            col01 = (String) request
                    .getParameter("etablissement_raison_sociale");
            col02 = (String) request.getParameter("etablissement_num_finess");
            col03 = (String) request.getParameter("etablissement_telephone_fixe");
            col04 = (String) request.getParameter("etablissement_fax");
            col05 = (String) request.getParameter("etablissement_adresse1");
            col06 = (String) request.getParameter("etablissement_adresse2");
            col07 = (String) request.getParameter("etablissement_adresse3");
            col08 = (String) request.getParameter("etablissement_code_postal");
            col09 = (String) request.getParameter("etablissement_localite");
            col10 = (String) request
                    .getParameter("hospitalisation_numero_entree");
            col11 = (String) request.getParameter("frais_precision_autre");
            col13 = (String) request
                    .getParameter("hospitalisation_date_entree");
            col15 = (String) request.getParameter("type_hospitalisation");
            ticketModerateur = isOn(request,"frais_ticket_moderateur");
            col17 = (String) request.getParameter("type_etablissement");
            col18 = (String) request.getParameter("hospitalisation_duree");
            col19 = (String) request
                    .getParameter("frais_forfait_journalier_limite_jours");
            col20 = (String) request
                    .getParameter("frais_chambre_particuliere_limite_jours");
            col21 = (String) request
                    .getParameter("frais_chambre_particuliere_plafond_journalier");
            fraisChambreParticuliere = isOn(request,"frais_chambre_particuliere");
            fraisForfaitJournalier = isOn(request,"frais_forfait_journalier");
            sortie_fax = isOn(request,"envoi_fax");
            sortie_email = isOn(request,"envoi_courriel");
            sortie_courrier = isOn(request,"envoi_courrier");
            email = (String) request.getParameter("envoi_courriel_adresse");
            urgence = (String) request.getParameter("urgence");

            String requete = "INSERT INTO EVENEMENT.EVENEMENT (ID, DATE_CREATION, CREATEUR_ID, MUTUELLE_ID, "
                    + "ENTITE_ID, ETABLT_ID, ADHERENT_ID, BENEFICIAIRE_ID, PERSONNE_ID, ADRESSE_ID, MOTIF_ID, SOUSMOTIF_ID, "
                    + "COMMENTAIRE, STATUT_ID, TYPE, MEDIA, URGENT, JDOCLASS, APPEL_ID, APPELANT_ID, COL26,"
                    + "COL01, COL02, COL03, COL04, COL05, COL06, COL07, COL08, COL09, COL10, "
                    + "COL11, COL13, COL15, COL16, COL17, COL18, COL19, COL20, COL21, COL32, COL33,"
                    + "SORTIE_FAX, SORTIE_EMAIL, SORTIE_COURRIER, EMAIL) "
                    + ""
                    + " VALUES (EVENEMENT.SEQ_EVT_ID.nextVal, SYSDATE, ?, ?, "
                    + "?, ? , ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?)";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, createur_id);
            stmt.setString(2, mutuelle_id);

            stmt.setString(3, entite_id);
            stmt.setString(4, etablissement_id);
            stmt.setString(5, adherent_id);
            stmt.setString(6, beneficiaire_id);
            stmt.setString(7, personne_id);
            stmt.setString(8, adresse_id);
            stmt.setString(9, motif_id);
            stmt.setString(10, sous_motif_id);

            stmt.setString(11, commentaires);
            stmt.setString(12, statut_id);
            stmt.setString(13, type);
            stmt.setString(14, media);
            stmt.setString(15, urgence);
            stmt.setString(16, jdoclass);
            stmt.setString(17, appel_id);
            stmt.setString(18, id_etablissement_hospitalier);
            stmt.setString(19, col26);

            stmt.setString(20, col01);
            stmt.setString(21, col02);
            stmt.setString(22, col03);
            stmt.setString(23, col04);
            stmt.setString(24, col05);
            stmt.setString(25, col06);
            stmt.setString(26, col07);
            stmt.setString(27, col08);
            stmt.setString(28, col09);
            stmt.setString(29, col10);

            if (col11.isEmpty()) {
                stmt.setNull(30, java.sql.Types.NULL);
            } else {
                stmt.setString(30, col11);
            }
            stmt.setString(31, col13);
            stmt.setString(32, col15);
            if (ticketModerateur.isEmpty()) {
                stmt.setNull(33, OracleTypes.NUMERIC);
            } else {
                stmt.setString(33, ticketModerateur);
            }
            if (col17.isEmpty()) {
                stmt.setNull(34, java.sql.Types.NULL);
            } else {
                stmt.setString(34, col17);
            }
            stmt.setString(35, col18);
            if (col19.isEmpty()) {
                stmt.setNull(36, java.sql.Types.NULL);
            } else {
                stmt.setString(36, col19);
            }
            if (col20.isEmpty()) {
                stmt.setNull(37, java.sql.Types.NULL);
            } else {
                stmt.setString(37, col20);
            }
            stmt.setDouble(38, (!"".equals(col21)) ? Double.valueOf(col21)
                    : 0.0);
            if (fraisChambreParticuliere.isEmpty()) {
                stmt.setNull(39, OracleTypes.NUMERIC);
            } else {
                stmt.setString(39, fraisChambreParticuliere);
            }
            if (fraisForfaitJournalier.isEmpty()) {
                stmt.setNull(40, OracleTypes.NUMERIC);
            } else {
                stmt.setString(40, fraisForfaitJournalier);
            }

            stmt.setString(41, sortie_fax);
            stmt.setString(42, sortie_email);
            stmt.setString(43, sortie_courrier);
            stmt.setString(44, email);

            stmt.executeUpdate();

            stmt = conn
                    .prepareStatement("SELECT EVENEMENT.SEQ_EVT_ID.currVal FROM DUAL");
            stmt.execute();
            rs = stmt.getResultSet();
            rs.next();
            long evenementId = rs.getLong(1);

            requete = "INSERT INTO EVENEMENT.T_INFOSPEC_IPC(IPC_ID, IPC_EVE_ID, "
                    + " IPC_ADHNOM, IPC_ADHPRENOM, IPC_ADHNUMSS, "
                    + " IPC_BENEFNOM , IPC_BENEFPRENOM, IPC_BENEFNUMSS, "
                    + " IPC_ETABLISSEMENT, IPC_ETABNUMFINESS, IPC_ETABTYPE,"
                    + " IPC_ETABADRESSE1, IPC_ETABADRESSE2, IPC_ETABADRESSE3, IPC_ETABCP, IPC_ETABVILLE,"
                    + " IPC_ETABTEL, IPC_ETABFAX, IPC_EMAILDESTINATAIRE, "
                    + " IPC_ENTREEDATE, IPC_ENTRENUM, IPC_ENVOICOURRIER , IPC_ENVOIFAX, IPC_ENVOIEMAIL,"
                    + " IPC_COMMENTAIRES, IPC_TM, IPC_TYPECODE, IPC_TYPELIB,  IPC_DUREE,"
                    + " IPC_FJ, IPC_FJLIMITE, IPC_CHP, IPC_CHPPLAFOND, IPC_CHPLIMITE, "
                    + " IPC_AUTRESFRAIS ) "
                    + " VALUES( EVENEMENT.SEQ_IPC_ID.nextVal, ?, "
                    + " ?, ?, ?, "
                    + " ?, ?, ?, "
                    + " ?, ?, ?, "
                    + " ?, ?, ?, ?, ?, "
                    + " ?, ?, ?, "
                    + " ?, ?, ? , ?, ?, "
                    + " ?, ?, ?, ?, ? ," + " ?, ?, ?, ?, ?, " + " ? )";

            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, evenementId);

            stmt.setString(2, (String) request.getParameter("nom_adherent"));
            stmt.setString(3, (String) request.getParameter("prenom_adherent"));
            stmt.setString(4,
                    (String) request.getParameter("numero_ss_adherent"));

            stmt.setString(5, (String) request.getParameter("nom_beneficiaire"));
            stmt.setString(6,
                    (String) request.getParameter("prenom_beneficiaire"));
            stmt.setString(7,
                    (String) request.getParameter("numero_ss_beneficiaire"));

            stmt.setString(8, (String) request
                    .getParameter("etablissement_raison_sociale"));
            stmt.setString(9,
                    (String) request.getParameter("etablissement_num_finess"));

            String type_etablissement = (String) request
                    .getParameter("type_etablissement");
            if ("0".equalsIgnoreCase(type_etablissement))
                type_etablissement = BasePEC._TYPE_ETAB_PUBLIC;
            else if ("1".equalsIgnoreCase(type_etablissement))
                type_etablissement = BasePEC._TYPE_ETAB_PRIVE;

            if (type_etablissement.isEmpty())
                stmt.setNull(10, java.sql.Types.NULL);
            else
                stmt.setString(10, type_etablissement);

            stmt.setString(11,
                    (String) request.getParameter("etablissement_adresse1"));
            stmt.setString(12,
                    (String) request.getParameter("etablissement_adresse2"));
            stmt.setString(13,
                    (String) request.getParameter("etablissement_adresse3"));
            stmt.setString(14,
                    (String) request.getParameter("etablissement_code_postal"));
            stmt.setString(15,
                    (String) request.getParameter("etablissement_localite"));

            stmt.setString(16, (String) request
                    .getParameter("etablissement_telephone_fixe"));
            stmt.setString(17,
                    (String) request.getParameter("etablissement_fax"));
            stmt.setString(18, (String) email);

            String dateEntree = (String) request
                    .getParameter("hospitalisation_date_entree");
            stmt.setString(19,
                    dateEntree.substring(6, 10) + dateEntree.substring(3, 5)
                            + dateEntree.substring(0, 2));
            stmt.setString(20, (String) request
                    .getParameter("hospitalisation_numero_entree"));
            stmt.setString(21, (String) sortie_courrier);
            stmt.setString(22, (String) sortie_fax);
            stmt.setString(23, (String) sortie_email);

            stmt.setString(24, (String) commentaires);
            if (ticketModerateur.isEmpty()){
                stmt.setNull(25, OracleTypes.NULL);
            } else{
                stmt.setString(25, ticketModerateur);
            }    
                
            switch (Integer.valueOf(
                    (String) request.getParameter("type_hospitalisation"))
                    .intValue()) {
            case 1:
                stmt.setString(26, "137");
                stmt.setString(27, "Chirurgie");
                break;
            case 2:
                stmt.setString(26, "223");
                stmt.setString(27, "Médecine");
                break;
            case 3:
                stmt.setString(26, "165");
                stmt.setString(27, "Maternité");
                break;
            case 4:
                stmt.setString(26, "230");
                stmt.setString(27, "Psychiatrie");
                break;
            case 5:
                stmt.setString(26, "168");
                stmt.setString(27, "Maison de repos/Convalescence");
                break;
            case 6:
                stmt.setString(26, "6");
                stmt.setString(27, "Maison d'enfants/Aériums");
                break;
            default:
                stmt.setString(26, "");
                stmt.setString(27, "indéfini");
                break;
            }
            stmt.setString(28,
                    (String) request.getParameter("hospitalisation_duree"));

            if (fraisForfaitJournalier.isEmpty()) {
                stmt.setNull(29, OracleTypes.NULL);
            } else {
                stmt.setString(29, fraisForfaitJournalier);
            }
            if (((String) request
                    .getParameter("frais_forfait_journalier_limite_jours"))
                    .isEmpty()) {
                stmt.setNull(30, OracleTypes.NULL);
            } else {
                stmt.setString(30, (String) request
                        .getParameter("frais_forfait_journalier_limite_jours"));
            }
            if (fraisChambreParticuliere.isEmpty()) {
                stmt.setNull(31, OracleTypes.NULL);
            } else {
                stmt.setString(31, fraisChambreParticuliere);
            }
            stmt.setDouble(32, (!"".equals(col21)) ? Double.valueOf(col21)
                    : 0.0);
            if (((String) request
                    .getParameter("frais_chambre_particuliere_limite_jours"))
                    .isEmpty()) {
                stmt.setNull(33, OracleTypes.NUMERIC);
            } else {
                stmt.setString(
                        33,
                        (String) request
                                .getParameter("frais_chambre_particuliere_limite_jours"));
            }
            if (((String) request.getParameter("frais_precision_autre"))
                    .isEmpty()) {
                stmt.setNull(34, OracleTypes.VARCHAR);
            } else {
                stmt.setString(34,
                        (String) request.getParameter("frais_precision_autre"));
            }

            stmt.executeUpdate();

            conn.commit();
            res.append(_VRAI);

            return res;
        } catch (Exception e) {
            LOGGER.error("creerPecHContacts", e);
            try {
                conn.rollback();
            } catch (Exception sqle) {
                LOGGER.error(_AnoRollBack, sqle);
            }
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Evenement> getEvenementsAssocies(String appe_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Evenement> res = new ArrayList<Evenement>();

        try {
            conn = getConnexion();
            Evenement unDisplay = null;
            String requete = "SELECT e.ID, e.JDOCLASS, e.COURRIER_ID "
                    + "FROM EVENEMENT.EVENEMENT e " + "WHERE e.APPEL_ID = ? ";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, appe_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Evenement();
                unDisplay.setID((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setJDOCLASS((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setCOURRIER_ID((rs.getString(3) != null) ? rs
                        .getString(3) : "");

                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getEvenementsAssocies", e);
            return new ArrayList<Evenement>();
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static StringBuilder getTeleActeursHabilitesSurCampagne(
            String campagne_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");

        try {
            conn = getConnexion();

            String requete = "SELECT distinct t.ID, c.LIBELLE, t.NOM, t.PRENOM, "
                    + "prs_mor.prs_nom as societe, srv.srv_lib service, pol.pol_lib pole, pem.PEM_POSTE, t.ACTIF  "
                    + "FROM HOTLINE.TELEACTEUR t, HOTLINE.TELEACTEURCAMPAGNE tc, HOTLINE.CODES c, "
                    + "h_annuaire.t_utilisateurs_utl utl, H_ANNUAIRE.t_personnes_prs prs, "
                    + "H_ANNUAIRE.t_personnes_prs prs_mor, H_ANNUAIRE.t_personnesemplois_pem pem, H_ANNUAIRE.t_personnesservicespoles_psp psp, "
                    + "H_ANNUAIRE.t_services_srv srv, H_ANNUAIRE.t_poles_pol pol "
                    + "WHERE t.CIVILITE_CODE = c.CODE(+) and t.UTL_ID = utl.UTL_ID(+) and utl.UTL_PRS_ID = prs.PRS_ID(+) "
                    + "AND prs_mor.prs_id(+) = pem.pem_prs_id_employeur AND prs.prs_id = pem.PEM_PRS_ID_EMPLOYE(+) "
                    + "and prs.prs_id = psp.psp_prs_id(+) AND psp.psp_srv_id = srv.srv_id(+) AND psp.psp_pol_id = pol.pol_id(+) "
                    + "and t.ID = tc.TELEACTEUR_ID AND tc.CAMPAGNE_ID = ? ORDER BY 3 ASC, 4 ASC";

            stmt = conn.prepareStatement(requete);
            stmt.setString(1, campagne_id);

            sb.append("<table cellpadding='4' cellspacing='0' class='m_table' width='100%'border='1'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Civilit&eacute;</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Nom</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Pr&eacute;nom</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Soci&eacute;t&eacute;</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Service</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>P&ocirc;le</td>");
            sb.append("<td class='m_td_entete_sans_main' align='center'>Poste</td>");
            sb.append("</tr>");

            rs = stmt.executeQuery();
            String classe = "m_tr_noir";
            while (rs.next()) {
                String actif = (rs.getString(9) != null) ? rs.getString(9) : "";

                if (_FAUX.equals(actif)) {
                    classe = "m_tr_gris";
                } else {
                    classe = "m_tr_noir";
                }

                sb.append("<tr class='" + classe + "'>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(2) != null) ? rs.getString(2) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(3) != null) ? rs.getString(3) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(4) != null) ? rs.getString(4) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(5) != null) ? rs.getString(5) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(6) != null) ? rs.getString(6) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(7) != null) ? rs.getString(7) : "")
                        + "</td>");
                sb.append("<td class='m_td'>"
                        + ((rs.getString(8) != null) ? rs.getString(8) : "")
                        + "</td>");
                sb.append("</tr>");

            }

            sb.append("</table>");

            stmt.clearParameters();
            return sb;
        } catch (Exception e) {
            LOGGER.error("getTeleActeursHabilitesSurCampagne", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<LibelleCode> getAllContratsEtablissement(
            String idEtablissement) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<LibelleCode> res = new ArrayList<LibelleCode>();

        try {

            LibelleCode unDisplay = null;
            StringBuilder requete = new StringBuilder();

            requete.append("SELECT ce.NUMCONTRAT, ce.ID, APPLICATION.PS_GETETATCONTRATETAB(ce.id) ");
            requete.append("FROM application.contrat_etablissement ce ");
            requete.append("WHERE ce.etablissement_id = ? ");

            requete.append(_UNION);

            requete.append("SELECT ce.NUMCONTRAT, ce.ID, APPLICATION.PS_GETETATCONTRATETAB(ce.id) ");
            requete.append("FROM APPLICATION.CONTRAT_GROUPEASSURE cga, application.contrat_etablissement ce ");
            requete.append("WHERE cga.CONTRAT_ETABLISSEMENT_ID = CE.ID AND cga.etablissement_id = ? ");

            requete.append("order by 3 desc, 1 asc ");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, idEtablissement);
            stmt.setString(2, idEtablissement);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new LibelleCode();
                unDisplay.setLibelle((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCode((rs.getString(2) != null) ? rs.getString(2)
                        : "");
                unDisplay.setActif((rs.getString(3) != null) ? rs.getString(3)
                        : "");
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;

            return res;
        } catch (Exception e) {
            LOGGER.error("getAllContratsEtablissement", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<Couverture> getCouverturesGroupeAssures(
            String detail_contrat_etablissement_id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Couverture> res = new ArrayList<Couverture>();
        try {

            Couverture unDisplay = null;
            StringBuilder requete = new StringBuilder();

            requete.append("select R.CODE, RO.CODE, CGAO.DATE_EFFET, CGAO.DATE_FIN "
                    + "from application.contrat_groupeassure_option cgao, application.risque r, application.risque_option ro "
                    + "where CGAO.CONTRAT_GROUPEASSURE_ID =  ? "
                    + "and CGAO.RISQUE_OPTION_ID = RO.ID and RO.RISQUE_ID = R.ID order by 1, 2");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, detail_contrat_etablissement_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Couverture();
                unDisplay.setCodeRisque((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeRisqueOption((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDateSouscription((rs.getTimestamp(3) != null) ? rs
                        .getTimestamp(3) : null);
                unDisplay.setDateRadiation((rs.getTimestamp(4) != null) ? rs
                        .getTimestamp(4) : null);
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getCouverturesGroupeAssures", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }

    }

    public static Collection<Couverture> getCouverturesBeneficiaire(
            String id_contrat_beneficiaire) {

        // Ramène les risques options actifs d'un bénéficiaire
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection<Couverture> res = new ArrayList<Couverture>();

        try {

            Couverture unDisplay = null;
            StringBuilder requete = new StringBuilder();

            requete.append("SELECT r.code, ro.code, cbo.date_souscription, cbo.DATE_CREATION_RADIATION ");
            requete.append("from application.contrat_beneficiaire_option cbo, application.risque_option ro, application.risque r, application.produit p ");
            requete.append("where cbo.contrat_beneficiaire_id = ?  and cbo.risque_option_id = ro.id and ro.risque_id = r.id and r.produit_id = p.id order by 2 asc, 4 asc ");

            conn = getConnexion();
            stmt = conn.prepareStatement(requete.toString());
            stmt.setString(1, id_contrat_beneficiaire);
            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new Couverture();
                unDisplay.setCodeRisque((rs.getString(1) != null) ? rs
                        .getString(1) : "");
                unDisplay.setCodeRisqueOption((rs.getString(2) != null) ? rs
                        .getString(2) : "");
                unDisplay.setDateSouscription((rs.getTimestamp(3) != null) ? rs
                        .getTimestamp(3) : null);
                unDisplay.setDateRadiation((rs.getTimestamp(4) != null) ? rs
                        .getTimestamp(4) : null);
                res.add(unDisplay);
            }
            stmt.clearParameters();
            unDisplay = null;
            return res;
        } catch (Exception e) {
            LOGGER.error("getCouverturesBeneficiaire", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getInfoContratCollectifEtablissement(
            String idContratEtablissement) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {

            String requete = "select e.libelle "
                    + "from application.contrat_etablissement ce, application.etablissement e "
                    + "where ce.etablissement_id = e.id and CE.id = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratEtablissement);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getInfoContratCollectifEtablissement", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static String getInfoContratCollectifAssure(String idContratAdherent) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";

        try {

            String requete = "select CE.NUMCONTRAT || '|' ||  e.libelle "
                    + "from application.contrat_adherent ca, APPLICATION.CONTRAT_GROUPEASSURE cga,"
                    + "APPLICATION.CONTRAT_ETABLISSEMENT ce, application.etablissement e "
                    + "where CA.CONTRAT_GROUPEASSURE_ID = CGA.ID and CGA.CONTRAT_ETABLISSEMENT_ID = CE.ID"
                    + " and CE.ETABLISSEMENT_ID = E.ID and ca.id = ? ";
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratAdherent);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getInfoContratCollectifAssure", e);
            return null;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

    public static Collection<ComptageSalaries> getComptagesSalaries(
            String idContratEtablissement) {

        // Compte les salariés actifs et radiés d'une entreprise
        Collection<ComptageSalaries> res = new ArrayList<ComptageSalaries>();
        ComptageSalaries unDisplay = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requete = "";

        try {

            requete = "select APPLICATION.PS_GETETATCONTRATBENEF(contben.id), count(distinct case when CODQUAL.codesource='A' then contadh.numcontrat||'-'||ben.id else null end) nbAdh,"
                    + "count(distinct case when CODQUAL.codesource <>'A' then contadh.numcontrat||'-'||ben.id else null end) nbBenef, "
                    + "count(distinct contadh.numcontrat||'-'||ben.id) "
                    + "FROM application.codes codciv, application.beneficiaire ben, application.personne pers, application.etablissement etab, "
                    + "application.codes codqual, application.contrat_adherent contadh, application.contrat_beneficiaire contben, "
                    + "application.contrat_groupeassure cga "
                    + "WHERE pers.civilite_code = codciv.code AND ben.qualite_code = codqual.code "
                    + "AND ben.personne_id = pers.ID AND etab.ID = cga.etablissement_id "
                    + "AND cga.ID = contadh.contrat_groupeassure_id AND contadh.ID = contben.contrat_adherent_id "
                    + "AND contben.beneficiaire_id = ben.ID AND CGA.CONTRAT_ETABLISSEMENT_ID = ? "
                    + "group by rollup(APPLICATION.PS_GETETATCONTRATBENEF(contben.id))";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratEtablissement);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ComptageSalaries();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setNbrAdherent(rs.getInt(2));
                unDisplay.setNbrAutres(rs.getInt(3));
                unDisplay.setTotal(rs.getInt(4));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getComptagesSalaries", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    
    public static String designation_entite_gestion(String identitegestion)
    {
    	String res="";
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requete = "";

        try {
        	        	
        	requete="select LIBELLE  from APPLICATION.ENTITE_GESTION where id=?";
        	            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, identitegestion);

            rs = stmt.executeQuery();
            while (rs.next()) {
            	res=rs.getString(1);
            	
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("designation_entite_gestion", e);
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    	//return res;
    }
    public static String hasCNDS(String idContratAdherent)
    {
    	String res="";
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requete = "";

        try {
        	requete = "select *"
            		+" from "
            		+" (SELECT distinct ro.code GT,cbeno.date_effet,cbeno.date_fin,cadh.numcontrat, benef.numeross,ro.flag_cnsd "
            		+" FROM application.beneficiaire benef "
            		+" INNER JOIN APPLICATION.CONTRAT_BENEFICIAIRE_option cbeno ON benef.id =CBENo.BENEFICIAIRE_ID "
            		+" INNER JOIN application.risque_option ro ON cbeno.risque_option_id = ro.id "
            		+" where cadh.numcontrat=? AND page = 1 "  
            		+" AND page = 1 and ((date_effet<=SYSDATE and date_fin is null) OR (date_effet<=SYSDATE and date_fin >=SYSDATE))"
            		+" AND ro.flag_CNSD = 1"
            		+" order by cbeno.date_effet desc";

        	
        	requete="select * "
        	+" from "
        	+"(SELECT distinct ro.code GT,cbeno.date_effet,cbeno.date_fin,cadh.numcontrat, benef.numeross,ro.flag_cnsd "
        	+" FROM application.beneficiaire benef " 
        	+" INNER JOIN APPLICATION.CONTRAT_BENEFICIAIRE_option cbeno ON benef.id =CBENo.BENEFICIAIRE_ID " 
        	+" INNER JOIN application.contrat_adherent cadh ON cbeno.contrat_adherent_id = cadh.id "
        	+" INNER JOIN application.risque_option ro ON cbeno.risque_option_id = ro.id " 
        	+" where cadh.numcontrat=? "
        	+" and date_effet <= sysdate "
        	+" order by date_fin desc nulls first,flag_cnsd desc nulls last "
        	+") where rownum = 1";


            
            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratAdherent);

            rs = stmt.executeQuery();
            while (rs.next()) {
            	if (rs.getInt(6)==1)
            	{
            		res="OK";	
            	}
            	
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("hasCNDS", e);
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }
    	//return res;
    
	
    public static Collection<ComptageSalaries> getComptagesDetailsSalaries(
            String idContratEtablissement, String actif_ou_pas) {

        Collection<ComptageSalaries> res = new ArrayList<ComptageSalaries>();
        ComptageSalaries unDisplay = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String requete = "";

        try {

            requete = "select cga.code, count(distinct case when CODQUAL.codesource='A' then contadh.numcontrat||'-'||ben.id else null end) nbAdh,"
                    + "count(distinct case when CODQUAL.codesource <>'A' then contadh.numcontrat||'-'||ben.id else null end) nbBenef, "
                    + "count(distinct contadh.numcontrat||'-'||ben.id) "
                    + "FROM application.codes codciv, application.beneficiaire ben, application.personne pers, application.etablissement etab,"
                    + "application.codes codqual, application.contrat_adherent contadh,application.contrat_beneficiaire contben, "
                    + "application.contrat_groupeassure cga "
                    + "WHERE pers.civilite_code = codciv.code AND ben.qualite_code = codqual.code "
                    + "AND ben.personne_id = pers.ID AND etab.ID = cga.etablissement_id "
                    + "AND cga.ID = contadh.contrat_groupeassure_id AND contadh.ID = contben.contrat_adherent_id "
                    + "AND contben.beneficiaire_id = ben.ID AND CGA.CONTRAT_ETABLISSEMENT_ID = ? and APPLICATION.PS_GETETATCONTRATBENEF(contben.id) = ? "
                    + "group by cga.code ";

            conn = getConnexion();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, idContratEtablissement);
            stmt.setString(2, actif_ou_pas);

            rs = stmt.executeQuery();
            while (rs.next()) {
                unDisplay = new ComptageSalaries();
                unDisplay.setCode((rs.getString(1) != null) ? rs.getString(1)
                        : "");
                unDisplay.setNbrAdherent(rs.getInt(2));
                unDisplay.setNbrAutres(rs.getInt(3));
                unDisplay.setTotal(rs.getInt(4));
                res.add(unDisplay);
            }
            stmt.clearParameters();
            return res;
        } catch (Exception e) {
            LOGGER.error("getComptagesDetailsSalaries", e);
            res.clear();
            return res;
        } finally {
            closeRsStmtConn(rs,stmt,conn);
        }
    }

}