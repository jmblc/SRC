package fr.igestion.crm.bean;

import java.util.Date;

public class ObjetRecherche {

    String id = "";
    String mutuelle = "";
    String mutuelle_id = "";
    String entiteGestion = "";
    String codeAdherentNumeroContrat = "";
    String civilite = "";
    String nom = "";
    String prenom = "";
    String qualite = "";
    String radie = "";
    String outilGestion = "";
    String numeroSecu = "";
    String numeroSiret = "";
    String codeEtablissement = "";
    String codeEntreprise = "";
    String codePostal = "";
    String localite = "";
    String clickable = "";

    String typeAppelant = "";
    String etablissementRS = "";
    String numeroFiness = "";
    String entite_gestion_id="";
    String libelle_entite_gestion="";

    Date dateNaissance = null;

    public ObjetRecherche() {
    }

    public String getId() {
        return id;
    }
    
    public String getEntite_gestion_id() {
        return entite_gestion_id;
    }
    public String getLibelle_entite_gestion() {
        return libelle_entite_gestion;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setEntite_gestion_id(String entite_gestion_id) {
        this.entite_gestion_id = entite_gestion_id;
    }
    
    public void setLibelle_entite_gestion(String libelle_entite_gestion) {
        this.libelle_entite_gestion = libelle_entite_gestion;
    }
    
    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getCodeAdherentNumeroContrat() {
        return codeAdherentNumeroContrat;
    }

    public void setCodeAdherentNumeroContrat(String codeAdherentNumeroContrat) {
        this.codeAdherentNumeroContrat = codeAdherentNumeroContrat;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getRadie() {
        return radie;
    }

    public void setRadie(String radie) {
        this.radie = radie;
    }

    public String getOutilGestion() {
        return outilGestion;
    }

    public void setOutilGestion(String outilGestion) {
        this.outilGestion = outilGestion;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getClickable() {
        return clickable;
    }

    public void setClickable(String clickable) {
        this.clickable = clickable;
    }

    public String getNumeroSecu() {
        return numeroSecu;
    }

    public void setNumeroSecu(String numeroSecu) {
        this.numeroSecu = numeroSecu;
    }

    public String getMutuelleId() {
        return mutuelle_id;
    }

    public void setMutuelleId(String mutuelleId) {
        mutuelle_id = mutuelleId;
    }

    public String getNumeroSiret() {
        return numeroSiret;
    }

    public void setNumeroSiret(String numeroSiret) {
        this.numeroSiret = numeroSiret;
    }

    public String getCodeEtablissement() {
        return codeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }

    public String getCodeEntreprise() {
        return codeEntreprise;
    }

    public void setCodeEntreprise(String codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getEntiteGestion() {
        return entiteGestion;
    }

    public void setEntiteGestion(String entiteGestion) {
        this.entiteGestion = entiteGestion;
    }

    public String getTypeAppelant() {
        return typeAppelant;
    }

    public void setTypeAppelant(String typeAppelant) {
        this.typeAppelant = typeAppelant;
    }

    public String getEtablissementRS() {
        return etablissementRS;
    }

    public void setEtablissementRS(String etablissementRS) {
        this.etablissementRS = etablissementRS;
    }

    public String getNumeroFiness() {
        return numeroFiness;
    }

    public void setNumeroFiness(String numeroFiness) {
        this.numeroFiness = numeroFiness;
    }

}
