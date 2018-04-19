package fr.igestion.crm.bean;

import java.util.Collection;
import java.util.Date;

import fr.igestion.crm.bean.contrat.AbonnementService;

public class Appelant {

    String ID = "";
    String NOM = "";
    String PRENOM = "";
    String CIVILITE_CODE = "";
    String TYPE_CODE = "";
    String ADR_LIGNE_1 = "";
    String ADR_LIGNE_2 = "";
    String ADR_LIGNE_3 = "";
    String ADR_LIGNE_4 = "";
    String ADR_CODEPOSTAL = "";
    String ADR_LOCALITE = "";
    String ADR_PAYS = "";
    String ADR_TELEPHONEFIXE = "";
    String ADR_TELEPHONEAUTRE = "";
    String ADR_TELECOPIE = "";
    String ADR_COURRIEL = "";
    String VIP = "";
    String NPAI = "";
    String CODEADHERENT = "";
    String NUMEROSS = "";
    String CLESS = "";
    Date DATENAISSANCE = null;
    String ETABLISSEMENT_RS = "";
    String JDOCLASS = "";
    String NUMFINESS = "";

    String civilite = "";
    String type = "";
    
    public Appelant() {
    }

    public String getADR_CODEPOSTAL() {
        return ADR_CODEPOSTAL;
    }

    public void setADR_CODEPOSTAL(String adr_codepostal) {
        ADR_CODEPOSTAL = adr_codepostal;
    }

    public String getADR_COURRIEL() {
        return ADR_COURRIEL;
    }

    public void setADR_COURRIEL(String adr_courriel) {
        ADR_COURRIEL = adr_courriel;
    }

    public String getADR_LIGNE_1() {
        return ADR_LIGNE_1;
    }

    public void setADR_LIGNE_1(String adr_ligne_1) {
        ADR_LIGNE_1 = adr_ligne_1;
    }

    public String getADR_LIGNE_2() {
        return ADR_LIGNE_2;
    }

    public void setADR_LIGNE_2(String adr_ligne_2) {
        ADR_LIGNE_2 = adr_ligne_2;
    }

    public String getADR_LIGNE_3() {
        return ADR_LIGNE_3;
    }

    public void setADR_LIGNE_3(String adr_ligne_3) {
        ADR_LIGNE_3 = adr_ligne_3;
    }

    public String getADR_LIGNE_4() {
        return ADR_LIGNE_4;
    }

    public void setADR_LIGNE_4(String adr_ligne_4) {
        ADR_LIGNE_4 = adr_ligne_4;
    }

    public String getADR_LOCALITE() {
        return ADR_LOCALITE;
    }

    public void setADR_LOCALITE(String adr_localite) {
        ADR_LOCALITE = adr_localite;
    }

    public String getADR_PAYS() {
        return ADR_PAYS;
    }

    public void setADR_PAYS(String adr_pays) {
        ADR_PAYS = adr_pays;
    }

    public String getADR_TELECOPIE() {
        return ADR_TELECOPIE;
    }

    public void setADR_TELECOPIE(String adr_telecopie) {
        ADR_TELECOPIE = adr_telecopie;
    }

    public String getADR_TELEPHONEAUTRE() {
        return ADR_TELEPHONEAUTRE;
    }

    public void setADR_TELEPHONEAUTRE(String adr_telephoneautre) {
        ADR_TELEPHONEAUTRE = adr_telephoneautre;
    }

    public String getADR_TELEPHONEFIXE() {
        return ADR_TELEPHONEFIXE;
    }

    public void setADR_TELEPHONEFIXE(String adr_telephonefixe) {
        ADR_TELEPHONEFIXE = adr_telephonefixe;
    }

    public String getCIVILITE_CODE() {
        return CIVILITE_CODE;
    }

    public void setCIVILITE_CODE(String civilite_code) {
        CIVILITE_CODE = civilite_code;
    }

    public String getCLESS() {
        return CLESS;
    }

    public void setCLESS(String cless) {
        CLESS = cless;
    }

    public String getCODEADHERENT() {
        return CODEADHERENT;
    }

    public void setCODEADHERENT(String codeadherent) {
        CODEADHERENT = codeadherent;
    }

    public Date getDATENAISSANCE() {
        return DATENAISSANCE;
    }

    public void setDATENAISSANCE(Date datenaissance) {
        DATENAISSANCE = datenaissance;
    }

    public String getETABLISSEMENT_RS() {
        return ETABLISSEMENT_RS;
    }

    public void setETABLISSEMENT_RS(String etablissement_rs) {
        ETABLISSEMENT_RS = etablissement_rs;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getJDOCLASS() {
        return JDOCLASS;
    }

    public void setJDOCLASS(String jdoclass) {
        JDOCLASS = jdoclass;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String nom) {
        NOM = nom;
    }

    public String getNPAI() {
        return NPAI;
    }

    public void setNPAI(String npai) {
        NPAI = npai;
    }

    public String getNUMEROSS() {
        return NUMEROSS;
    }

    public void setNUMEROSS(String numeross) {
        NUMEROSS = numeross;
    }

    public String getNUMFINESS() {
        return NUMFINESS;
    }

    public void setNUMFINESS(String numfiness) {
        NUMFINESS = numfiness;
    }

    public String getPRENOM() {
        return PRENOM;
    }

    public void setPRENOM(String prenom) {
        PRENOM = prenom;
    }

    public String getTYPE_CODE() {
        return TYPE_CODE;
    }

    public void setTYPE_CODE(String type_code) {
        TYPE_CODE = type_code;
    }

    public String getVIP() {
        return VIP;
    }

    public void setVIP(String vip) {
        VIP = vip;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
