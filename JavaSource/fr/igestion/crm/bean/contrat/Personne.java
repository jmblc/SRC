package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class Personne {

    String ID = "";
    String CODE = "";
    String CIVILITE_CODE = "";
    String SEXE_CODE = "";
    String NOM = "";
    String PRENOM = "";
    String NOMJEUNEFILLE = "";
    Date DATENAISSANCE = null;
    String SITUATIONFAMILIALE_CODE = "";
    String SITUATIONPROFESSIONNELLE_CODE = "";
    String RANGNAISSANCE = "";
    String ADRESSE_ID = "";
    String CSP_CODE = "";
    String civilite = "";
    String csp = "";

    private Adresse adresse = null;

    public Personne() {
    }

    public String getADRESSE_ID() {
        return ADRESSE_ID;
    }

    public void setADRESSE_ID(String adresse_id) {
        ADRESSE_ID = adresse_id;
    }

    public String getCIVILITE_CODE() {
        return CIVILITE_CODE;
    }

    public void setCIVILITE_CODE(String civilite_code) {
        CIVILITE_CODE = civilite_code;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String code) {
        CODE = code;
    }

    public String getCSP_CODE() {
        return CSP_CODE;
    }

    public void setCSP_CODE(String csp_code) {
        CSP_CODE = csp_code;
    }

    public Date getDATENAISSANCE() {
        return DATENAISSANCE;
    }

    public void setDATENAISSANCE(Date datenaissance) {
        DATENAISSANCE = datenaissance;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String nom) {
        NOM = nom;
    }

    public String getNOMJEUNEFILLE() {
        return NOMJEUNEFILLE;
    }

    public void setNOMJEUNEFILLE(String nomjeunefille) {
        NOMJEUNEFILLE = nomjeunefille;
    }

    public String getPRENOM() {
        return PRENOM;
    }

    public void setPRENOM(String prenom) {
        PRENOM = prenom;
    }

    public String getRANGNAISSANCE() {
        return RANGNAISSANCE;
    }

    public void setRANGNAISSANCE(String rangnaissance) {
        RANGNAISSANCE = rangnaissance;
    }

    public String getSEXE_CODE() {
        return SEXE_CODE;
    }

    public void setSEXE_CODE(String sexe_code) {
        SEXE_CODE = sexe_code;
    }

    public String getSITUATIONFAMILIALE_CODE() {
        return SITUATIONFAMILIALE_CODE;
    }

    public void setSITUATIONFAMILIALE_CODE(String situationfamiliale_code) {
        SITUATIONFAMILIALE_CODE = situationfamiliale_code;
    }

    public String getSITUATIONPROFESSIONNELLE_CODE() {
        return SITUATIONPROFESSIONNELLE_CODE;
    }

    public void setSITUATIONPROFESSIONNELLE_CODE(
            String situationprofessionnelle_code) {
        SITUATIONPROFESSIONNELLE_CODE = situationprofessionnelle_code;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getCSP() {
        return csp;
    }

    public void setCSP(String csp) {
        this.csp = csp;
    }

    public String getNomPrenom() {
        return NOM + " " + PRENOM;
    }

    public String getPrenomNom() {
        return PRENOM + " " + NOM;
    }

}
