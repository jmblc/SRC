package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class RIB {

    private String ID = "";
    private String TYPERIB = "";
    private String CODEINTERBANCAIRE = "";
    private Date DEBUTEFFET;
    private String CODEGUICHET = "";
    private String NOMETABLISSEMENT = "";
    private String NUMEROCOMPTE = "";
    private String CLERIB = "";

    private String CLE_IBAN = "";
    private String CODE_PAYS = "";
    private String IDENTIFIANT_NATIONAL_COMPTE = "";
    private String CODE_BIC = "";

    public RIB() {
    }
    
    public String getCLE_IBAN() {
        return CLE_IBAN;
    }

    public void setCLE_IBAN(String cLE_IBAN) {
        CLE_IBAN = cLE_IBAN;
    }

    public String getCODE_PAYS() {
        return CODE_PAYS;
    }

    public void setCODE_PAYS(String cODE_PAYS) {
        CODE_PAYS = cODE_PAYS;
    }

    public String getIDENTIFIANT_NATIONAL_COMPTE() {
        return IDENTIFIANT_NATIONAL_COMPTE;
    }

    public void setIDENTIFIANT_NATIONAL_COMPTE(
            String iDENTIFIANT_NATIONAL_COMPTE) {
        IDENTIFIANT_NATIONAL_COMPTE = iDENTIFIANT_NATIONAL_COMPTE;
    }

    public String getCODE_BIC() {
        return CODE_BIC;
    }

    public void setCODE_BIC(String cODE_BIC) {
        CODE_BIC = cODE_BIC;
    }

    public String getCLERIB() {
        return CLERIB;
    }

    public void setCLERIB(String clerib) {
        CLERIB = clerib;
    }

    public String getCODEGUICHET() {
        return CODEGUICHET;
    }

    public void setCODEGUICHET(String codeguichet) {
        CODEGUICHET = codeguichet;
    }

    public String getCODEINTERBANCAIRE() {
        return CODEINTERBANCAIRE;
    }

    public void setCODEINTERBANCAIRE(String codeinterbancaire) {
        CODEINTERBANCAIRE = codeinterbancaire;
    }

    public Date getDEBUTEFFET() {
        return DEBUTEFFET;
    }

    public void setDEBUTEFFET(Date debuteffet) {
        DEBUTEFFET = debuteffet;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getNOMETABLISSEMENT() {
        return NOMETABLISSEMENT;
    }

    public void setNOMETABLISSEMENT(String nometablissement) {
        NOMETABLISSEMENT = nometablissement;
    }

    public String getNUMEROCOMPTE() {
        return NUMEROCOMPTE;
    }

    public void setNUMEROCOMPTE(String numerocompte) {
        NUMEROCOMPTE = numerocompte;
    }

    public String getTYPERIB() {
        return TYPERIB;
    }

    public void setTYPERIB(String typerib) {
        TYPERIB = typerib;
    }

}
