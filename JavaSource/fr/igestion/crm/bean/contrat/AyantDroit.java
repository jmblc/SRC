package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class AyantDroit {

    private String id = "";
    private String personneId = "";
    private String civilite = "";
    private String nom = "";
    private String prenom = "";
    private String qualite = "";
    private String CSP = "";
    private Date dateNaissance;
    private Date dateAdhesion;
    private Date dateRadiation;
    private String actif = "";
    private String numeross = "";
    private String cless = "";

    public AyantDroit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getCSP() {
        return CSP;
    }

    public void setCSP(String csp) {
        CSP = csp;
    }

    public Date getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(Date dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPersonneId() {
        return personneId;
    }

    public void setPersonneId(String personneId) {
        this.personneId = personneId;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public String getCless() {
        return cless;
    }

    public void setCless(String cless) {
        this.cless = cless;
    }

    public String getNumeross() {
        return numeross;
    }

    public void setNumeross(String numeross) {
        this.numeross = numeross;
    }

}
