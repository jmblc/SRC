package fr.igestion.crm.bean;

import java.util.Date;

public class Salarie {

    private String numeroAdherent = "";
    private String civilite = "";
    private String nom = "";
    private String prenom = "";
    private String qualite = "";
    private Date dateNaissance = null;
    private Date dateRadiation = null;
    private String numerosecu = "";
    private String clesecu = "";
    private String nomMutuelle = "";
    private String codeMutuelle = "";
    private String idBeneficiaire = "";
    private String actif = "";
    private String code_groupe_assures = "";
    private String libelle_groupe_assures = "";

    public Salarie() {
    }
    
    public String getCodeMutuelle() {
        return codeMutuelle;
    }

    public void setCodeMutuelle(String codeMutuelle) {
        this.codeMutuelle = codeMutuelle;
    }

    public String getIdBeneficiaire() {
        return idBeneficiaire;
    }

    public void setIdBeneficiaire(String idBeneficiaire) {
        this.idBeneficiaire = idBeneficiaire;
    }

    public String getNomMutuelle() {
        return nomMutuelle;
    }

    public void setNomMutuelle(String nomMutuelle) {
        this.nomMutuelle = nomMutuelle;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    public void setNumeroAdherent(String numeroAdherent) {
        this.numeroAdherent = numeroAdherent;
    }

    public String getNumeroSecu() {
        return numerosecu;
    }

    public void setNumeroSecu(String numerosecu) {
        this.numerosecu = numerosecu;
    }

    public String getCleSecu() {
        return clesecu;
    }

    public void setCleSecu(String clesecu) {
        this.clesecu = clesecu;
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

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getNumerosecu() {
        return numerosecu;
    }

    public void setNumerosecu(String numerosecu) {
        this.numerosecu = numerosecu;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public String getCodeGroupeAssures() {
        return code_groupe_assures;
    }

    public void setCodeGroupeAssures(String codeGroupeAssures) {
        code_groupe_assures = codeGroupeAssures;
    }

    public String getLibelleGroupeAssures() {
        return libelle_groupe_assures;
    }

    public void setLibelleGroupeAssures(String libelleGroupeAssures) {
        libelle_groupe_assures = libelleGroupeAssures;
    }

}
