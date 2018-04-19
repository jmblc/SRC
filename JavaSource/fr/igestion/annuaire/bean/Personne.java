package fr.igestion.annuaire.bean;

import java.util.Collection;
import java.util.Date;

public class Personne {

    private String PRS_ID = "";
    private String PRS_CODE = "";
    private String PRS_NOM = "";
    private String PRS_PRENOM = "";
    private String PRS_NUMSIRET = "";
    private String PRS_CODNAF = "";
    private Date PRS_DTENAISS = null;
    private String PRS_CVL_ID = "";
    private String PRS_SEXE = "";
    private String PRS_CTG_ID = "";
    private String PRS_ATV_ID = "";
    private String PRS_PCJ_ID = "";
    private String PRS_TYPE = "";
    private String PRS_PRIVE = "";
    private String PRS_ACTIF = "";
    private String PRS_VIP = "";
    private String PRS_PRS_ID_SUPERIEUR = "";
    private String PRS_PRS_ID_MAISONMERE = "";
    private String PRS_PRS_ID_DIRIGEANT = "";
    private String PRS_PRS_ID_CREATEUR = "";
    private String UTL_PRS_ID = "";
    private String PRS_MATRICULE = "";

    private Utilisateur utilisateur = null;

    private Collection coordonnees_postales = null;
    private Collection coordonnees_autres = null;
    private Collection applications = null;
    private Collection groupes = null;
    private Collection services = null;
    private Collection poles = null;
    private Collection services_poles = null;
    private Collection fonctions = null;
    private Personne personne_employeur = null;
    private Personne responsable = null;

    private String entreprise = "";

    public Personne() {

    }

    public String getPRS_ACTIF() {
        return PRS_ACTIF;
    }

    public void setPRS_ACTIF(String prs_actif) {
        PRS_ACTIF = prs_actif;
    }

    public String getPRS_ATV_ID() {
        return PRS_ATV_ID;
    }

    public void setPRS_ATV_ID(String prs_atv_id) {
        PRS_ATV_ID = prs_atv_id;
    }

    public String getPRS_CODNAF() {
        return PRS_CODNAF;
    }

    public void setPRS_CODNAF(String prs_codnaf) {
        PRS_CODNAF = prs_codnaf;
    }

    public String getPRS_CTG_ID() {
        return PRS_CTG_ID;
    }

    public void setPRS_CTG_ID(String prs_ctg_id) {
        PRS_CTG_ID = prs_ctg_id;
    }

    public String getPRS_CVL_ID() {
        return PRS_CVL_ID;
    }

    public void setPRS_CVL_ID(String prs_cvl_id) {
        PRS_CVL_ID = prs_cvl_id;
    }

    public Date getPRS_DTENAISS() {
        return PRS_DTENAISS;
    }

    public void setPRS_DTENAISS(Date prs_dtenaiss) {
        PRS_DTENAISS = prs_dtenaiss;
    }

    public String getPRS_ID() {
        return PRS_ID;
    }

    public void setPRS_ID(String prs_id) {
        PRS_ID = prs_id;
    }

    public String getPRS_NOM() {
        return PRS_NOM;
    }

    public void setPRS_NOM(String prs_nom) {
        PRS_NOM = prs_nom;
    }

    public String getPRS_NUMSIRET() {
        return PRS_NUMSIRET;
    }

    public void setPRS_NUMSIRET(String prs_numsiret) {
        PRS_NUMSIRET = prs_numsiret;
    }

    public String getPRS_PCJ_ID() {
        return PRS_PCJ_ID;
    }

    public void setPRS_PCJ_ID(String prs_pcj_id) {
        PRS_PCJ_ID = prs_pcj_id;
    }

    public String getPRS_PRENOM() {
        return PRS_PRENOM;
    }

    public void setPRS_PRENOM(String prs_prenom) {
        PRS_PRENOM = prs_prenom;
    }

    public String getPRS_PRIVE() {
        return PRS_PRIVE;
    }

    public void setPRS_PRIVE(String prs_prive) {
        PRS_PRIVE = prs_prive;
    }

    public String getPRS_PRS_ID_DIRIGEANT() {
        return PRS_PRS_ID_DIRIGEANT;
    }

    public void setPRS_PRS_ID_DIRIGEANT(String prs_prs_id_dirigeant) {
        PRS_PRS_ID_DIRIGEANT = prs_prs_id_dirigeant;
    }

    public String getPRS_PRS_ID_MAISONMERE() {
        return PRS_PRS_ID_MAISONMERE;
    }

    public void setPRS_PRS_ID_MAISONMERE(String prs_prs_id_maisonmere) {
        PRS_PRS_ID_MAISONMERE = prs_prs_id_maisonmere;
    }

    public String getPRS_PRS_ID_SUPERIEUR() {
        return PRS_PRS_ID_SUPERIEUR;
    }

    public void setPRS_PRS_ID_SUPERIEUR(String prs_prs_id_superieur) {
        PRS_PRS_ID_SUPERIEUR = prs_prs_id_superieur;
    }

    public String getPRS_PRS_ID_CREATEUR() {
        return PRS_PRS_ID_CREATEUR;
    }

    public void setPRS_PRS_ID_CREATEUR(String prs_prs_id_createur) {
        PRS_PRS_ID_CREATEUR = prs_prs_id_createur;
    }

    public String getPRS_SEXE() {
        return PRS_SEXE;
    }

    public void setPRS_SEXE(String prs_sexe) {
        PRS_SEXE = prs_sexe;
    }

    public String getPRS_TYPE() {
        return PRS_TYPE;
    }

    public void setPRS_TYPE(String prs_type) {
        PRS_TYPE = prs_type;
    }

    public String getPRS_VIP() {
        return PRS_VIP;
    }

    public void setPRS_VIP(String prs_vip) {
        PRS_VIP = prs_vip;
    }

    public String getUTL_PRS_ID() {
        return UTL_PRS_ID;
    }

    public void setUTL_PRS_ID(String utl_prs_id) {
        UTL_PRS_ID = utl_prs_id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Collection getCoordonnees_autres() {
        return coordonnees_autres;
    }

    public void setCoordonnees_autres(Collection coordonnees_autres) {
        this.coordonnees_autres = coordonnees_autres;
    }

    public Collection getCoordonnees_postales() {
        return coordonnees_postales;
    }

    public void setCoordonnees_postales(Collection coordonnees_postales) {
        this.coordonnees_postales = coordonnees_postales;
    }

    public Collection getApplications() {
        return applications;
    }

    public void setApplications(Collection applications) {
        this.applications = applications;
    }

    public Collection getGroupes() {
        return groupes;
    }

    public void setGroupes(Collection groupes) {
        this.groupes = groupes;
    }

    public Collection getFonctions() {
        return fonctions;
    }

    public void setFonctions(Collection fonctions) {
        this.fonctions = fonctions;
    }

    public Collection getPoles() {
        return poles;
    }

    public void setPoles(Collection poles) {
        this.poles = poles;
    }

    public Collection getServices() {
        return services;
    }

    public void setServices(Collection services) {
        this.services = services;
    }

    public Collection getServicesPoles() {
        return services_poles;
    }

    public void setServicesPoles(Collection services_poles) {
        this.services_poles = services_poles;
    }

    public String getNOM_PRENOM() {
        return PRS_NOM + " " + PRS_PRENOM;
    }

    public String getPRENOM_NOM() {
        return PRS_PRENOM + " " + PRS_NOM;
    }

    public Personne getPersonneEmployeur() {
        return personne_employeur;
    }

    public void setPersonneEmployeur(Personne personne_employeur) {
        this.personne_employeur = personne_employeur;
    }

    public Personne getResponsable() {
        return responsable;
    }

    public void setResponsable(Personne responsable) {
        this.responsable = responsable;
    }

    public String getPRS_MATRICULE() {
        return PRS_MATRICULE;
    }

    public void setPRS_MATRICULE(String prs_matricule) {
        PRS_MATRICULE = prs_matricule;
    }

    public String getPRS_CODE() {
        return PRS_CODE;
    }

    public void setPRS_CODE(String prs_code) {
        PRS_CODE = prs_code;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

}
