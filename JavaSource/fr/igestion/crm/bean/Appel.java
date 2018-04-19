package fr.igestion.crm.bean;

import java.util.Date;

public class Appel {

    String ID = "";
    String CREATEUR_ID = "";
    String MODIFICATEUR_ID = "";
    String CAMPAGNE_ID = "";
    String MUTUELLE_ID = "";
    Date DATEAPPEL = null;
    Date DATECLOTURE = null;

    String MOTIF_ID = "";
    String S_MOTIF_ID = "";
    String POINT_ID = "";
    String S_POINT_ID = "";
    String REGIME_CODE = "";

    String TRAITEMENTURGENT = "";
    String RECLAMATION = "";

    String COMMENTAIRE = "";
    String SATISFACTION_CODE = "";
    String CLOTURE_CODE = "";

    String NUMERORAPPEL = "";
    Date DATERAPPEL = null;
    String PERIODERAPPEL_CODE = "";

    String CODEAPPELANT_SELECTIONNE = "";
    String BENEFICIAIRE_ID = "";
    String BENEFICIAIRE_QUALITE = "";
    String ADHERENT_ID = "";
    String ETABLISSEMENT_ID = "";
    String APPELANT_ID = "";
    String ENTITEGESTION_ID = "";

    String EDITIONENCOURS = "";
    String EDITEUR_ID = "";
    Date DATEEDITION = null;
    String editeur = "";

    String CLOTUREUR_ID = "";
    Date DATEMODIFICATION = null;

    String PRIORITE = "";
    String SOUS_STATUT = "";

    String TRANSFERTS = "";

    String NOMDOCUMENTGENERE = "";
    String MODELE_EDITION_ID = "";

    String satisfaction = "", alias_satisfaction = "", statut = "",
            alias_statut = "", type_appelant = "", alias_type_appelant = "",
            periode_rappel = "";
    String campagne = "", mutuelle = "", entite_gestion = "", teleacteur = "",
            debut_commentaires = "";
    String motif = "", sous_motif = "", point = "", sous_point = "", pole = "",
            code_adherent_numero_contrat = "", libelle_sous_statut = "";

    String resolu = "";

    public Appel() {
    }
    
    public String getResolu() {
        return resolu;
    }

    public void setResolu(String resolu) {
        this.resolu = resolu;
    }
  
    public String getADHERENT_ID() {
        return ADHERENT_ID;
    }

    public void setADHERENT_ID(String adherent_id) {
        ADHERENT_ID = adherent_id;
    }

    public String getAPPELANT_ID() {
        return APPELANT_ID;
    }

    public void setAPPELANT_ID(String appelant_id) {
        APPELANT_ID = appelant_id;
    }

    public String getBENEFICIAIRE_ID() {
        return BENEFICIAIRE_ID;
    }

    public void setBENEFICIAIRE_ID(String beneficiaire_id) {
        BENEFICIAIRE_ID = beneficiaire_id;
    }

    public String getBENEFICIAIRE_QUALITE() {
        return BENEFICIAIRE_QUALITE;
    }

    public void setBENEFICIAIRE_QUALITE(String beneficiaire_qualite) {
        BENEFICIAIRE_QUALITE = beneficiaire_qualite;
    }

    public String getCAMPAGNE_ID() {
        return CAMPAGNE_ID;
    }

    public void setCAMPAGNE_ID(String campagne_id) {
        CAMPAGNE_ID = campagne_id;
    }

    public String getCLOTURE_CODE() {
        return CLOTURE_CODE;
    }

    public void setCLOTURE_CODE(String cloture_code) {
        CLOTURE_CODE = cloture_code;
    }

    public String getCLOTUREUR_ID() {
        return CLOTUREUR_ID;
    }

    public void setCLOTUREUR_ID(String clotureur_id) {
        CLOTUREUR_ID = clotureur_id;
    }

    public String getCODEAPPELANT_SELECTIONNE() {
        return CODEAPPELANT_SELECTIONNE;
    }

    public void setCODEAPPELANT_SELECTIONNE(String codeappelant_selectionne) {
        CODEAPPELANT_SELECTIONNE = codeappelant_selectionne;
    }

    public String getCOMMENTAIRE() {
        return COMMENTAIRE;
    }

    public void setCOMMENTAIRE(String commentaire) {
        COMMENTAIRE = commentaire;
    }

    public String getCREATEUR_ID() {
        return CREATEUR_ID;
    }

    public void setCREATEUR_ID(String createur_id) {
        CREATEUR_ID = createur_id;
    }

    public Date getDATEAPPEL() {
        return DATEAPPEL;
    }

    public void setDATEAPPEL(Date dateappel) {
        DATEAPPEL = dateappel;
    }

    public Date getDATECLOTURE() {
        return DATECLOTURE;
    }

    public void setDATECLOTURE(Date datecloture) {
        DATECLOTURE = datecloture;
    }

    public Date getDATEEDITION() {
        return DATEEDITION;
    }

    public void setDATEEDITION(Date dateedition) {
        DATEEDITION = dateedition;
    }

    public Date getDATEMODIFICATION() {
        return DATEMODIFICATION;
    }

    public void setDATEMODIFICATION(Date datemodification) {
        DATEMODIFICATION = datemodification;
    }

    public Date getDATERAPPEL() {
        return DATERAPPEL;
    }

    public void setDATERAPPEL(Date daterappel) {
        DATERAPPEL = daterappel;
    }

    public String getEDITEUR_ID() {
        return EDITEUR_ID;
    }

    public void setEDITEUR_ID(String editeur_id) {
        EDITEUR_ID = editeur_id;
    }

    public String getEDITIONENCOURS() {
        return EDITIONENCOURS;
    }

    public void setEDITIONENCOURS(String editionencours) {
        EDITIONENCOURS = editionencours;
    }

    public String getENTITEGESTION_ID() {
        return ENTITEGESTION_ID;
    }

    public void setENTITEGESTION_ID(String entitegestion_id) {
        ENTITEGESTION_ID = entitegestion_id;
    }

    public String getETABLISSEMENT_ID() {
        return ETABLISSEMENT_ID;
    }

    public void setETABLISSEMENT_ID(String etablissement_id) {
        ETABLISSEMENT_ID = etablissement_id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getMODIFICATEUR_ID() {
        return MODIFICATEUR_ID;
    }

    public void setMODIFICATEUR_ID(String modificateur_id) {
        MODIFICATEUR_ID = modificateur_id;
    }

    public String getMOTIF_ID() {
        return MOTIF_ID;
    }

    public void setMOTIF_ID(String motif_id) {
        MOTIF_ID = motif_id;
    }

    public String getMUTUELLE_ID() {
        return MUTUELLE_ID;
    }

    public void setMUTUELLE_ID(String mutuelle_id) {
        MUTUELLE_ID = mutuelle_id;
    }

    public String getNUMERORAPPEL() {
        return NUMERORAPPEL;
    }

    public void setNUMERORAPPEL(String numerorappel) {
        NUMERORAPPEL = numerorappel;
    }

    public String getPeriodeRappel() {
        return periode_rappel;
    }

    public void setPeriodeRappel(String periode_rappel) {
        this.periode_rappel = periode_rappel;
    }

    public String getPERIODERAPPEL_CODE() {
        return PERIODERAPPEL_CODE;
    }

    public void setPERIODERAPPEL_CODE(String perioderappel_code) {
        PERIODERAPPEL_CODE = perioderappel_code;
    }

    public String getPOINT_ID() {
        return POINT_ID;
    }

    public void setPOINT_ID(String point_id) {
        POINT_ID = point_id;
    }

    public String getPRIORITE() {
        return PRIORITE;
    }

    public void setPRIORITE(String priorite) {
        PRIORITE = priorite;
    }

    public String getRECLAMATION() {
        return RECLAMATION;
    }

    public void setRECLAMATION(String reclamation) {
        RECLAMATION = reclamation;
    }

    public String getREGIME_CODE() {
        return REGIME_CODE;
    }

    public void setREGIME_CODE(String regime_code) {
        REGIME_CODE = regime_code;
    }

    public String getS_MOTIF_ID() {
        return S_MOTIF_ID;
    }

    public void setS_MOTIF_ID(String s_motif_id) {
        this.S_MOTIF_ID = s_motif_id;
    }

    public String getS_POINT_ID() {
        return S_POINT_ID;
    }

    public void setS_POINT_ID(String s_point_id) {
        S_POINT_ID = s_point_id;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getSATISFACTION_CODE() {
        return SATISFACTION_CODE;
    }

    public void setSATISFACTION_CODE(String satisfaction_code) {
        SATISFACTION_CODE = satisfaction_code;
    }

    public String getSOUS_STATUT() {
        return SOUS_STATUT;
    }

    public void setSOUS_STATUT(String sous_statut) {
        this.SOUS_STATUT = sous_statut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTRAITEMENTURGENT() {
        return TRAITEMENTURGENT;
    }

    public void setTRAITEMENTURGENT(String traitementurgent) {
        this.TRAITEMENTURGENT = traitementurgent;
    }

    public String getTypeAppelant() {
        return type_appelant;
    }

    public void setTypeAppelant(String type_appelant) {
        this.type_appelant = type_appelant;
    }

    public String getCampagne() {
        return campagne;
    }

    public void setCampagne(String campagne) {
        this.campagne = campagne;
    }

    public String getEntiteGestion() {
        return entite_gestion;
    }

    public void setEntiteGestion(String entite_gestion) {
        this.entite_gestion = entite_gestion;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSousMotif() {
        return sous_motif;
    }

    public void setSousMotif(String sous_motif) {
        this.sous_motif = sous_motif;
    }

    public String getSousPoint() {
        return sous_point;
    }

    public void setSousPoint(String sous_point) {
        this.sous_point = sous_point;
    }

    public String getTeleacteur() {
        return teleacteur;
    }

    public void setTeleacteur(String teleacteur) {
        this.teleacteur = teleacteur;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getCodeAdherentNumeroContrat() {
        return code_adherent_numero_contrat;
    }

    public void setCodeAdherentNumeroContrat(String code_adherent_numero_contrat) {
        this.code_adherent_numero_contrat = code_adherent_numero_contrat;
    }

    public String getDebutCommentaires() {
        return debut_commentaires;
    }

    public void setDebutCommentaires(String debut_commentaires) {
        this.debut_commentaires = debut_commentaires;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getAliasTypAppelant() {
        return alias_type_appelant;
    }

    public void setAliasTypeAppelant(String aliasTypeAppelant) {
        this.alias_type_appelant = aliasTypeAppelant;
    }

    public String getAliasStatut() {
        return alias_statut;
    }

    public void setAliasStatut(String aliasStatut) {
        this.alias_statut = aliasStatut;
    }

    public String getAliasSatisfaction() {
        return alias_satisfaction;
    }

    public void setAliasSatisfaction(String alias) {
        this.alias_satisfaction = alias;
    }

    public String getLibelleSousStatut() {
        return libelle_sous_statut;
    }

    public void setLibelleSousStatut(String libelleSousStatut) {
        this.libelle_sous_statut = libelleSousStatut;
    }

    public String getNOMDOCUMENTGENERE() {
        return NOMDOCUMENTGENERE;
    }

    public void setNOMDOCUMENTGENERE(String nOMDOCUMENTGENERE) {
        NOMDOCUMENTGENERE = nOMDOCUMENTGENERE;
    }

    public String getMODELE_EDITION_ID() {
        return MODELE_EDITION_ID;
    }

    public void setMODELE_EDITION_ID(String mODELEEDITIONID) {
        MODELE_EDITION_ID = mODELEEDITIONID;
    }

    public String getTRANSFERTS() {
        return TRANSFERTS;
    }

    public void setTRANSFERTS(String tRANSFERTS) {
        TRANSFERTS = tRANSFERTS;
    }

}
