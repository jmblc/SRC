package fr.igestion.crm.bean.evenement;

import java.util.Date;

public class Evenement {

    private String ID = "";
    private Date DATE_CREATION = null;
    private String CREATEUR_ID = "";
    private String MUTUELLE_ID = "";
    private String ENTITE_ID = "";
    private String ETABLT_ID = "";
    private String ADHERENT_ID = "";
    private String BENEFICIAIRE_ID = "";
    private String PERSONNE_ID = "";
    private String ADRESSE_ID = "";
    private String MOTIF_ID = "";
    private String SOUSMOTIF_ID = "";
    private String NUMERORAPPEL = "";
    private Date DATERAPPEL = null;
    private String PERIODERAPPEL = "";
    private String COMMENTAIRE = "";
    private String STATUT_ID = "";
    private Date DATE_MAJ = null;
    private String USERMAJ_ID = "";
    private Date DATEEDITION = null;
    private String TYPE = "";
    private String MEDIA = "";
    private String URGENT = "";
    private String NPAI = "";
    private String COURRIER_ID = "";
    private String MANQUANT = "";
    private String RENVOI = "";
    private String SITE = "";
    private String SOUS_STATUT_ID = "";
    private String JDOCLASS = "";
    private String APPEL_ID = "";
    private String APPELANT_ID = "";
    private String RECLAMATION = "";
    private String CLE_REFEXTERNE = "";
    private String EMAIL = "";

    private Date COL13 = null;
    private Date COL14 = null;

    private String mutuelle = "", motif = "", sousMotif = "", statut = "",
            sousStatut = "";

    public Evenement() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public Date getDATE_CREATION() {
        return DATE_CREATION;
    }

    public void setDATE_CREATION(Date dATECREATION) {
        DATE_CREATION = dATECREATION;
    }

    public String getCREATEUR_ID() {
        return CREATEUR_ID;
    }

    public void setCREATEUR_ID(String cREATEURID) {
        CREATEUR_ID = cREATEURID;
    }

    public String getMUTUELLE_ID() {
        return MUTUELLE_ID;
    }

    public void setMUTUELLE_ID(String mUTUELLEID) {
        MUTUELLE_ID = mUTUELLEID;
    }

    public String getENTITE_ID() {
        return ENTITE_ID;
    }

    public void setENTITE_ID(String eNTITEID) {
        ENTITE_ID = eNTITEID;
    }

    public String getETABLT_ID() {
        return ETABLT_ID;
    }

    public void setETABLT_ID(String eTABLTID) {
        ETABLT_ID = eTABLTID;
    }

    public String getADHERENT_ID() {
        return ADHERENT_ID;
    }

    public void setADHERENT_ID(String aDHERENTID) {
        ADHERENT_ID = aDHERENTID;
    }

    public String getBENEFICIAIRE_ID() {
        return BENEFICIAIRE_ID;
    }

    public void setBENEFICIAIRE_ID(String bENEFICIAIREID) {
        BENEFICIAIRE_ID = bENEFICIAIREID;
    }

    public String getPERSONNE_ID() {
        return PERSONNE_ID;
    }

    public void setPERSONNE_ID(String pERSONNEID) {
        PERSONNE_ID = pERSONNEID;
    }

    public String getADRESSE_ID() {
        return ADRESSE_ID;
    }

    public void setADRESSE_ID(String aDRESSEID) {
        ADRESSE_ID = aDRESSEID;
    }

    public String getMOTIF_ID() {
        return MOTIF_ID;
    }

    public void setMOTIF_ID(String mOTIFID) {
        MOTIF_ID = mOTIFID;
    }

    public String getSOUSMOTIF_ID() {
        return SOUSMOTIF_ID;
    }

    public void setSOUSMOTIF_ID(String sOUSMOTIFID) {
        SOUSMOTIF_ID = sOUSMOTIFID;
    }

    public String getNUMERORAPPEL() {
        return NUMERORAPPEL;
    }

    public void setNUMERORAPPEL(String nUMERORAPPEL) {
        NUMERORAPPEL = nUMERORAPPEL;
    }

    public Date getDATERAPPEL() {
        return DATERAPPEL;
    }

    public void setDATERAPPEL(Date dATERAPPEL) {
        DATERAPPEL = dATERAPPEL;
    }

    public String getPERIODERAPPEL() {
        return PERIODERAPPEL;
    }

    public void setPERIODERAPPEL(String pERIODERAPPEL) {
        PERIODERAPPEL = pERIODERAPPEL;
    }

    public String getCOMMENTAIRE() {
        return COMMENTAIRE;
    }

    public void setCOMMENTAIRE(String cOMMENTAIRE) {
        COMMENTAIRE = cOMMENTAIRE;
    }

    public String getSTATUT_ID() {
        return STATUT_ID;
    }

    public void setSTATUT_ID(String sTATUTID) {
        STATUT_ID = sTATUTID;
    }

    public Date getDATE_MAJ() {
        return DATE_MAJ;
    }

    public void setDATE_MAJ(Date dATEMAJ) {
        DATE_MAJ = dATEMAJ;
    }

    public String getUSERMAJ_ID() {
        return USERMAJ_ID;
    }

    public void setUSERMAJ_ID(String uSERMAJID) {
        USERMAJ_ID = uSERMAJID;
    }

    public Date getDATEEDITION() {
        return DATEEDITION;
    }

    public void setDATEEDITION(Date dATEEDITION) {
        DATEEDITION = dATEEDITION;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String tYPE) {
        TYPE = tYPE;
    }

    public String getMEDIA() {
        return MEDIA;
    }

    public void setMEDIA(String mEDIA) {
        MEDIA = mEDIA;
    }

    public String getURGENT() {
        return URGENT;
    }

    public void setURGENT(String uRGENT) {
        URGENT = uRGENT;
    }

    public String getNPAI() {
        return NPAI;
    }

    public void setNPAI(String nPAI) {
        NPAI = nPAI;
    }

    public String getCOURRIER_ID() {
        return COURRIER_ID;
    }

    public void setCOURRIER_ID(String cOURRIERID) {
        COURRIER_ID = cOURRIERID;
    }

    public String getMANQUANT() {
        return MANQUANT;
    }

    public void setMANQUANT(String mANQUANT) {
        MANQUANT = mANQUANT;
    }

    public String getRENVOI() {
        return RENVOI;
    }

    public void setRENVOI(String rENVOI) {
        RENVOI = rENVOI;
    }

    public String getSITE() {
        return SITE;
    }

    public void setSITE(String sITE) {
        SITE = sITE;
    }

    public String getSOUS_STATUT_ID() {
        return SOUS_STATUT_ID;
    }

    public void setSOUS_STATUT_ID(String sOUSSTATUTID) {
        SOUS_STATUT_ID = sOUSSTATUTID;
    }

    public String getJDOCLASS() {
        return JDOCLASS;
    }

    public void setJDOCLASS(String jDOCLASS) {
        JDOCLASS = jDOCLASS;
    }

    public String getAPPEL_ID() {
        return APPEL_ID;
    }

    public void setAPPEL_ID(String aPPELID) {
        APPEL_ID = aPPELID;
    }

    public String getAPPELANT_ID() {
        return APPELANT_ID;
    }

    public void setAPPELANT_ID(String aPPELANTID) {
        APPELANT_ID = aPPELANTID;
    }

    public String getRECLAMATION() {
        return RECLAMATION;
    }

    public void setRECLAMATION(String rECLAMATION) {
        RECLAMATION = rECLAMATION;
    }

    public String getCLE_REFEXTERNE() {
        return CLE_REFEXTERNE;
    }

    public void setCLE_REFEXTERNE(String cLEREFEXTERNE) {
        CLE_REFEXTERNE = cLEREFEXTERNE;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getSousMotif() {
        return sousMotif;
    }

    public void setSousMotif(String sousMotif) {
        this.sousMotif = sousMotif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSousStatut() {
        return sousStatut;
    }

    public void setSousStatut(String sousStatut) {
        this.sousStatut = sousStatut;
    }

    public Date getCOL13() {
        return COL13;
    }

    public void setCOL13(Date cOL13) {
        COL13 = cOL13;
    }

    public Date getCOL14() {
        return COL14;
    }

    public void setCOL14(Date cOL14) {
        COL14 = cOL14;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String eMAIL) {
        EMAIL = eMAIL;
    }
}
