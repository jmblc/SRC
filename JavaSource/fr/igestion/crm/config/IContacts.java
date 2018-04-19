package fr.igestion.crm.config;

import java.io.File;

import javax.naming.InitialContext;

public class IContacts {

    public static final String _version = "V4.2";
    public static final String _HCONTACTS="HCONTACTS";
    
    public static final String _AccueilForm="AccueilForm";
    public static final String _FicheAppelForm="FicheAppelForm";
    
    public static final String _expirationSession = "expirationSession";
    public static final String _fichesLockees = "fichesLockees";
    public static final String _accueilAdministration = "accueilAdministration";
    public static final String _modificationDeclenchementMailResiliation = "modificationDeclenchementMailResiliation";
    public static final String _modificationFluxTransfertClient="modificationFluxTransfertClient";
    public static final String _pec = "pec";
    public static final String _modele_pec = "modele_pec";
    public static final String _message = "message";
    public static final String _entitesGestionSensibles="entitesGestionSensibles";
    public static final String _procedure_mail="procedure_mail";
    public static final String _sessionsActives="sessionsActives";
    public static final String _teleActeurs="teleActeurs";
    public static final String _modele_procedure_mail="modele_procedure_mail";
    public static final String _siteWeb="siteWeb";
    public static final String _document="document";
    public static final String _scenarios="scenarios";
    public static final String _transferts="transferts";
    public static final String _teleActeursEntiteGestionSensible="teleActeursEntiteGestionSensible";
    public static final String _ajouterEntiteGestionSensible="ajouterEntiteGestionSensible";
    public static final String _copierHabilitationsTeleActeur="copierHabilitationsTeleActeur";
    public static final String _habilitationTeleActeurCampagnes="habilitationTeleActeurCampagnes";
    public static final String _habilitationTeleActeurEntitesGestion="habilitationTeleActeurEntitesGestion";
    public static final String _adresseGestion = "adresseGestion";
    
    
    public static final String _ficheAppel="ficheAppel";
    public static final String _accueil="accueil";
    public static final String _erreurConnexion="erreurConnexion";
    public static final String _ouvrir_fiche_from_externe="ouvrir_fiche_from_externe";
    public static final String _fichesATraiter="fichesATraiter";
    
    public static final String _struts_method = "method";

    public static final String _ASC="ASC";
    public static final String _DESC="DESC";
    
    public static final String _blankSelect = "-1";
    public static final String _faux = "false";
    public static final String _vrai = "true";
    public static final String _oui = "Oui";
    public static final String _nom = "Non";
    
    public static final String _var_context_param_app="param_app";
    
    public static final String _var_session_teleActeur="teleActeur";
    public static final String _var_session_teleacteurs_recherche="teleacteurs_recherche";
    public static final String _var_session_mutuelles_habilitees="mutuelles_habilitees";
    public static final String _var_session_satisfactions="satisfactions";
    public static final String _var_session_codes_clotures="codes_clotures";
    public static final String _var_session_codes_clotures_recherche="codes_clotures_recherche";
    public static final String _var_session_types_appelants="types_appelants";
    public static final String _var_session_types_appelants_autre="types_appelants_autre";
    public static final String _var_session_types_appelants_recherche="types_appelants_recherche";
    public static final String _var_session_sous_statuts="sous_statuts";
    public static final String _var_session_periodes_rappel="periodes_rappel";
    public static final String _var_session_regimes="regimes";
    public static final String _var_session_oui_non="oui_non";
    public static final String _var_session_sites="sites";
    public static final String _var_session_ids_entites_gestion_sensibles_du_teleacteur="ids_entites_gestion_sensibles_du_teleacteur";
    public static final String _var_session_SERVEUR_SMTP="SERVEUR_SMTP";
    public static final String _var_session_LECTEUR_PARTAGE="LECTEUR_PARTAGE";
    public static final String _var_session_FicheAppelForm="FicheAppelForm";
    
    public static final String _HORSCIBLE="HORSCIBLE";
    public static final String _AUTRECAMPAGNE="AUTRECAMP";
    public static final String _CLOTURE="CLOTURE";
    public static final String _TRANSFERT_EXTERNE = "TRANSFERE_EX";
    public static final String _TRANSFERT_INTERNE = "TRANSFERE_A";
    public static final String _A_TRAITER = "ATRAITER";
    public static final String _APPEL_SORTANT = "APPELSORTANT";
    
    public static final String _ONGLET_ASSURE_COMPO_FAMILIALE="ONGLET_ASSURE_COMPO_FAMILIALE";
    public static final String _ONGLET_ASSURE="ONGLET_ASSURE";
    public static final String _ONGLET_APPELANT="ONGLET_APPELANT";
    public static final String _ONGLET_ASSURE_MOT_DE_PASSE="ONGLET_ASSURE_MOT_DE_PASSE";
    public static final String _ONGLET_ASSURE_ABONNEMENT="ONGLET_ASSURE_ABONNEMENT";
    public static final String _ONGLET_ASSURE_PRESTATIONS="ONGLET_ASSURE_PRESTATIONS";
    public static final String _ONGLET_ASSURE_HISTORIQUE="ONGLET_ASSURE_HISTORIQUE";
    public static final String _ONGLET_ASSURE_BANQUE_RO="ONGLET_ASSURE_BANQUE_RO";
    public static final String _ONGLET_ASSURE_CONTRATS="ONGLET_ASSURE_CONTRATS";
    public static final String _ONGLET_ASSURE_ENTREPRISE="ONGLET_ASSURE_ENTREPRISE";
    
    public static final String _ONGLET_ENTREPRISE="ONGLET_ENTREPRISE";
    public static final String _ONGLET_ENTREPRISE_SALARIES="ONGLET_ENTREPRISE_SALARIES";
    
    public static final String _Collectif="Collectif";
    public static final String _Individuel="Individuel";
    public static final String _IndivColl="Entreprise à gestion individuelle";
	public static final String _import_export_scenarios = "import_export_scenarios";
	
	public static String NOM_CLASSE_PERSISTENCE = "fr.igestion.crm.config.ObjectPersitenceJson"; // "fr.igestion.crm.config.ObjectPersitenceImpl";
	public static String NOM_REPERTOIRE_STOCKAGE = "";
	
	public static String contextPath;
	
	private static IContacts instance;
    
    private IContacts() {
    	try {
    		NOM_CLASSE_PERSISTENCE = InitialContext.doLookup("java:comp/env/hcontact_classe_persistence");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	try {
    		String nomRepertoireSauve = InitialContext.doLookup("java:comp/env/hcontact_repertoire_stockage");
    		if (nomRepertoireSauve != null) {
    			NOM_REPERTOIRE_STOCKAGE = nomRepertoireSauve.concat(File.separator);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static IContacts getInstance() {
    	
    	if (instance == null) {
    		instance = new IContacts();
    	}
    	return instance;
    }

	/**
	 * @return the Version
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * @return the Hcontacts
	 */
	public String getHcontacts() {
		return _HCONTACTS;
	}

	/**
	 * @return the Accueilform
	 */
	public String getAccueilform() {
		return _AccueilForm;
	}

	/**
	 * @return the Ficheappelform
	 */
	public String getFicheappelform() {
		return _FicheAppelForm;
	}

	/**
	 * @return the Expirationsession
	 */
	public String getExpirationsession() {
		return _expirationSession;
	}

	/**
	 * @return the Ficheslockees
	 */
	public String getFicheslockees() {
		return _fichesLockees;
	}

	/**
	 * @return the Accueiladministration
	 */
	public String getAccueiladministration() {
		return _accueilAdministration;
	}

	/**
	 * @return the Modificationdeclenchementmailresiliation
	 */
	public String getModificationdeclenchementmailresiliation() {
		return _modificationDeclenchementMailResiliation;
	}

	/**
	 * @return the Modificationfluxtransfertclient
	 */
	public String getModificationfluxtransfertclient() {
		return _modificationFluxTransfertClient;
	}

	/**
	 * @return the Pec
	 */
	public String getPec() {
		return _pec;
	}

	/**
	 * @return the ModelePec
	 */
	public String getModelePec() {
		return _modele_pec;
	}

	/**
	 * @return the Message
	 */
	public String getMessage() {
		return _message;
	}

	/**
	 * @return the Entitesgestionsensibles
	 */
	public String getEntitesgestionsensibles() {
		return _entitesGestionSensibles;
	}

	/**
	 * @return the ProcedureMail
	 */
	public String getProcedureMail() {
		return _procedure_mail;
	}

	/**
	 * @return the Sessionsactives
	 */
	public String getSessionsactives() {
		return _sessionsActives;
	}

	/**
	 * @return the Teleacteurs
	 */
	public String getTeleacteurs() {
		return _teleActeurs;
	}

	/**
	 * @return the ModeleProcedureMail
	 */
	public String getModeleProcedureMail() {
		return _modele_procedure_mail;
	}

	/**
	 * @return the Siteweb
	 */
	public String getSiteweb() {
		return _siteWeb;
	}

	/**
	 * @return the Document
	 */
	public String getDocument() {
		return _document;
	}

	/**
	 * @return the Scenarios
	 */
	public String getScenarios() {
		return _scenarios;
	}

	/**
	 * @return the Transferts
	 */
	public String getTransferts() {
		return _transferts;
	}

	/**
	 * @return the Teleacteursentitegestionsensible
	 */
	public String getTeleacteursentitegestionsensible() {
		return _teleActeursEntiteGestionSensible;
	}

	/**
	 * @return the Ajouterentitegestionsensible
	 */
	public String getAjouterentitegestionsensible() {
		return _ajouterEntiteGestionSensible;
	}

	/**
	 * @return the Copierhabilitationsteleacteur
	 */
	public String getCopierhabilitationsteleacteur() {
		return _copierHabilitationsTeleActeur;
	}

	/**
	 * @return the Habilitationteleacteurcampagnes
	 */
	public String getHabilitationteleacteurcampagnes() {
		return _habilitationTeleActeurCampagnes;
	}

	/**
	 * @return the Habilitationteleacteurentitesgestion
	 */
	public String getHabilitationteleacteurentitesgestion() {
		return _habilitationTeleActeurEntitesGestion;
	}

	/**
	 * @return the Adressegestion
	 */
	public String getAdressegestion() {
		return _adresseGestion;
	}

	/**
	 * @return the Ficheappel
	 */
	public String getFicheappel() {
		return _ficheAppel;
	}

	/**
	 * @return the Accueil
	 */
	public String getAccueil() {
		return _accueil;
	}

	/**
	 * @return the Erreurconnexion
	 */
	public String getErreurconnexion() {
		return _erreurConnexion;
	}

	/**
	 * @return the OuvrirFicheFromExterne
	 */
	public String getOuvrirFicheFromExterne() {
		return _ouvrir_fiche_from_externe;
	}

	/**
	 * @return the Fichesatraiter
	 */
	public String getFichesatraiter() {
		return _fichesATraiter;
	}

	/**
	 * @return the StrutsMethod
	 */
	public String getStrutsMethod() {
		return _struts_method;
	}

	/**
	 * @return the Asc
	 */
	public String getAsc() {
		return _ASC;
	}

	/**
	 * @return the Desc
	 */
	public String getDesc() {
		return _DESC;
	}

	/**
	 * @return the Blankselect
	 */
	public String getBlankselect() {
		return _blankSelect;
	}

	/**
	 * @return the Faux
	 */
	public String getFaux() {
		return _faux;
	}

	/**
	 * @return the Vrai
	 */
	public String getVrai() {
		return _vrai;
	}

	/**
	 * @return the Oui
	 */
	public String getOui() {
		return _oui;
	}

	/**
	 * @return the Nom
	 */
	public String getNom() {
		return _nom;
	}

	/**
	 * @return the VarSessionTeleacteur
	 */
	public String getVarSessionTeleacteur() {
		return _var_session_teleActeur;
	}

	/**
	 * @return the VarSessionTeleacteursRecherche
	 */
	public String getVarSessionTeleacteursRecherche() {
		return _var_session_teleacteurs_recherche;
	}

	/**
	 * @return the VarSessionMutuellesHabilitees
	 */
	public String getVarSessionMutuellesHabilitees() {
		return _var_session_mutuelles_habilitees;
	}

	/**
	 * @return the VarSessionSatisfactions
	 */
	public String getVarSessionSatisfactions() {
		return _var_session_satisfactions;
	}

	/**
	 * @return the VarSessionCodesClotures
	 */
	public String getVarSessionCodesClotures() {
		return _var_session_codes_clotures;
	}

	/**
	 * @return the VarSessionCodesCloturesRecherche
	 */
	public String getVarSessionCodesCloturesRecherche() {
		return _var_session_codes_clotures_recherche;
	}

	/**
	 * @return the VarSessionTypesAppelants
	 */
	public String getVarSessionTypesAppelants() {
		return _var_session_types_appelants;
	}

	/**
	 * @return the VarSessionTypesAppelantsAutre
	 */
	public String getVarSessionTypesAppelantsAutre() {
		return _var_session_types_appelants_autre;
	}

	/**
	 * @return the VarSessionTypesAppelantsRecherche
	 */
	public String getVarSessionTypesAppelantsRecherche() {
		return _var_session_types_appelants_recherche;
	}

	/**
	 * @return the VarSessionSousStatuts
	 */
	public String getVarSessionSousStatuts() {
		return _var_session_sous_statuts;
	}

	/**
	 * @return the VarSessionPeriodesRappel
	 */
	public String getVarSessionPeriodesRappel() {
		return _var_session_periodes_rappel;
	}

	/**
	 * @return the VarSessionRegimes
	 */
	public String getVarSessionRegimes() {
		return _var_session_regimes;
	}

	/**
	 * @return the VarSessionOuiNon
	 */
	public String getVarSessionOuiNon() {
		return _var_session_oui_non;
	}

	/**
	 * @return the VarSessionSites
	 */
	public String getVarSessionSites() {
		return _var_session_sites;
	}

	/**
	 * @return the VarSessionIdsEntitesGestionSensiblesDuTeleacteur
	 */
	public String getVarSessionIdsEntitesGestionSensiblesDuTeleacteur() {
		return _var_session_ids_entites_gestion_sensibles_du_teleacteur;
	}

	/**
	 * @return the VarSessionServeurSmtp
	 */
	public String getVarSessionServeurSmtp() {
		return _var_session_SERVEUR_SMTP;
	}

	/**
	 * @return the VarSessionLecteurPartage
	 */
	public String getVarSessionLecteurPartage() {
		return _var_session_LECTEUR_PARTAGE;
	}

	/**
	 * @return the VarSessionFicheappelform
	 */
	public String getVarSessionFicheappelform() {
		return _var_session_FicheAppelForm;
	}

	/**
	 * @return the Horscible
	 */
	public String getHorscible() {
		return _HORSCIBLE;
	}

	/**
	 * @return the Autrecampagne
	 */
	public String getAutrecampagne() {
		return _AUTRECAMPAGNE;
	}

	/**
	 * @return the OngletAssureCompoFamiliale
	 */
	public String getOngletAssureCompoFamiliale() {
		return _ONGLET_ASSURE_COMPO_FAMILIALE;
	}

	/**
	 * @return the OngletAssure
	 */
	public String getOngletAssure() {
		return _ONGLET_ASSURE;
	}

	/**
	 * @return the OngletAppelant
	 */
	public String getOngletAppelant() {
		return _ONGLET_APPELANT;
	}

	/**
	 * @return the OngletAssureMotDePasse
	 */
	public String getOngletAssureMotDePasse() {
		return _ONGLET_ASSURE_MOT_DE_PASSE;
	}

	/**
	 * @return the OngletAssureAbonnement
	 */
	public String getOngletAssureAbonnement() {
		return _ONGLET_ASSURE_ABONNEMENT;
	}

	/**
	 * @return the OngletAssurePrestations
	 */
	public String getOngletAssurePrestations() {
		return _ONGLET_ASSURE_PRESTATIONS;
	}

	/**
	 * @return the OngletAssureHistorique
	 */
	public String getOngletAssureHistorique() {
		return _ONGLET_ASSURE_HISTORIQUE;
	}

	/**
	 * @return the OngletAssureBanqueRo
	 */
	public String getOngletAssureBanqueRo() {
		return _ONGLET_ASSURE_BANQUE_RO;
	}

	/**
	 * @return the OngletAssureContrats
	 */
	public String getOngletAssureContrats() {
		return _ONGLET_ASSURE_CONTRATS;
	}

	/**
	 * @return the OngletAssureEntreprise
	 */
	public String getOngletAssureEntreprise() {
		return _ONGLET_ASSURE_ENTREPRISE;
	}

	/**
	 * @return the OngletEntreprise
	 */
	public String getOngletEntreprise() {
		return _ONGLET_ENTREPRISE;
	}

	/**
	 * @return the OngletEntrepriseSalaries
	 */
	public String getOngletEntrepriseSalaries() {
		return _ONGLET_ENTREPRISE_SALARIES;
	}

	/**
	 * @return the Collectif
	 */
	public String getCollectif() {
		return _Collectif;
	}

	/**
	 * @return the Individuel
	 */
	public String getIndividuel() {
		return _Individuel;
	}

	/**
	 * @return the Indivcoll
	 */
	public String getIndivcoll() {
		return _IndivColl;
	}

	/**
	 * @return the ImportExportScenarios
	 */
	public String getImportExportScenarios() {
		return _import_export_scenarios;
	}

	/**
	 * @return the Cloture
	 */
	public String getCloture() {
		return _CLOTURE;
	}

	/**
	 * @return the TransfertExterne
	 */
	public String getTransfertExterne() {
		return _TRANSFERT_EXTERNE;
	}

	/**
	 * @return the TransfertInterne
	 */
	public String getTransfertInterne() {
		return _TRANSFERT_INTERNE;
	}

	/**
	 * @return the ATraiter
	 */
	public String getATraiter() {
		return _A_TRAITER;
	}

	/**
	 * @return the AppelSortant
	 */
	public String getAppelSortant() {
		return _APPEL_SORTANT;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath the contextPath to set
	 */
	public static void setContextPath(String contextPath) {
		IContacts.contextPath = contextPath;
	}

	/**
	 * @return the _var_context_param_app
	 */
	public String getVarContextParamApp() {
		return _var_context_param_app;
	}

	/**
	 * @return the nOM_CLASSE_PERSISTENCE
	 */
	public String getNOM_CLASSE_PERSISTENCE() {
		return NOM_CLASSE_PERSISTENCE;
	}
	
	/**
	 * @return the nOM_REPERTOIRE_SAUVEGARDE
	 */
	public String getNOM_REPERTOIRE_STOCKAGE() {
		return NOM_REPERTOIRE_STOCKAGE;
	}



}
