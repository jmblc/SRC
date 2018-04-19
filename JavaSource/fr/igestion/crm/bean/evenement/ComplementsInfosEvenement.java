package fr.igestion.crm.bean.evenement;

import java.util.Collection;
import java.util.Date;

import fr.igestion.crm.bean.LigneDVS;

public class ComplementsInfosEvenement {

    private String LOT = "";
    private String TCO_ID = "";
    private String FLAG_HISTO = "";

    private Date DATE_INSERTION = null;
    private Date DATE_IDENTIFICATION = null;
    private Date DATE_TRAITEMENT = null;
    private Date DATE_RECEPTION_POSTE = null;

    private String ORIGINAL = "";
    private String USERSCAN = "";
    private String USERIDENTIF = "";
    private String USERTRAIT = "";

    private String EXPEDITEUR = "";
    private String SUJET = "";
    private String CC = "";
    private String DESTINATAIRE = "";

    private String courrier_origine = "";

    private Collection<LigneDVS> infosCourriers;

    public ComplementsInfosEvenement() {
    }

    public String getLOT() {
        return LOT;
    }

    public void setLOT(String lOT) {
        LOT = lOT;
    }

    public Date getDATE_INSERTION() {
        return DATE_INSERTION;
    }

    public void setDATE_INSERTION(Date dATEINSERTION) {
        DATE_INSERTION = dATEINSERTION;
    }

    public Date getDATE_IDENTIFICATION() {
        return DATE_IDENTIFICATION;
    }

    public void setDATE_IDENTIFICATION(Date dATEIDENTIFICATION) {
        DATE_IDENTIFICATION = dATEIDENTIFICATION;
    }

    public Date getDATE_TRAITEMENT() {
        return DATE_TRAITEMENT;
    }

    public void setDATE_TRAITEMENT(Date dATETRAITEMENT) {
        DATE_TRAITEMENT = dATETRAITEMENT;
    }

    public Date getDATE_RECEPTION_POSTE() {
        return DATE_RECEPTION_POSTE;
    }

    public void setDATE_RECEPTION_POSTE(Date dATERECEPTIONPOSTE) {
        DATE_RECEPTION_POSTE = dATERECEPTIONPOSTE;
    }

    public String getORIGINAL() {
        return ORIGINAL;
    }

    public void setORIGINAL(String oRIGINAL) {
        ORIGINAL = oRIGINAL;
    }

    public String getUSERSCAN() {
        return USERSCAN;
    }

    public void setUSERSCAN(String uSERSCAN) {
        USERSCAN = uSERSCAN;
    }

    public String getUSERIDENTIF() {
        return USERIDENTIF;
    }

    public void setUSERIDENTIF(String uSERIDENTIF) {
        USERIDENTIF = uSERIDENTIF;
    }

    public String getUSERTRAIT() {
        return USERTRAIT;
    }

    public void setUSERTRAIT(String uSERTRAIT) {
        USERTRAIT = uSERTRAIT;
    }

    public String getEXPEDITEUR() {
        return EXPEDITEUR;
    }

    public void setEXPEDITEUR(String eXPEDITEUR) {
        EXPEDITEUR = eXPEDITEUR;
    }

    public String getSUJET() {
        return SUJET;
    }

    public void setSUJET(String sUJET) {
        SUJET = sUJET;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String cC) {
        CC = cC;
    }

    public String getDESTINATAIRE() {
        return DESTINATAIRE;
    }

    public void setDESTINATAIRE(String dESTINATAIRE) {
        this.DESTINATAIRE = dESTINATAIRE;
    }

    public String getTCO_ID() {
        return TCO_ID;
    }

    public void setTCO_ID(String tCOID) {
        this.TCO_ID = tCOID;
    }

    public String getCourrierOrigine() {
        return courrier_origine;
    }

    public void setCourrierOrigine(String courrierOrigine) {
        this.courrier_origine = courrierOrigine;
    }

    public String getFLAG_HISTO() {
        return FLAG_HISTO;
    }

    public void setFLAG_HISTO(String fLAGHISTO) {
        this.FLAG_HISTO = fLAGHISTO;
    }

    public Collection<LigneDVS> getInfosCourriers() {
        return infosCourriers;
    }

    public void setInfosCourriers(Collection<LigneDVS> infosCourriers) {
        this.infosCourriers = infosCourriers;
    }

}
