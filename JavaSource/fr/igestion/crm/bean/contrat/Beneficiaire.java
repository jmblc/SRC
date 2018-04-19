package fr.igestion.crm.bean.contrat;

import java.util.Collection;
import java.util.Date;

import fr.igestion.crm.bean.PostItBeneficiaire;

public class Beneficiaire {

    String ID = "";
    String MUTUELLE_ID = "";
    String CODE = "";
    String QUALITE_CODE = "";
    String NUMEROSS = "";
    String CLESS = "";
    String CAISSERO = "";
    String CENTRERO = "";
    String CARTEVITALE = "";
    String REGIME_CODE = "";
    Date DATEPREMIEREADHESION = null;
    String ENTITE_GESTION_ID = "";
    String PERSONNE_ID = "";
    String ETABLISSEMENT_ID = "";
    String PRELEVEMENT_RIB_ID = "";
    String VIREMENT_RIB_ID = "";
    String ACTIF = "";
    Date DATERADIATION = null;
    String POSNOEMIE_CODE = "";
    String BEN_OUS_ID = "";
    String BEN_OFFSHORE = "";
    String BEN_OFA_ID = "";
    Date DATEADHESION = null;
    String qualite = "";
    String outil_gestion = "";
    String entite_gestion_sensible = "";
    String adherent_id = "";

    Personne personne = null;
    PostItBeneficiaire postit = null;
    RIB rib_virement = null, rib_prelevement = null;
    Etablissement etablissement = null;
    InfosRO infos_ro = null;
    Collection<ContratBeneficiaire> contrats_actifs;
    Collection<ContratBeneficiaire> contrats_radies;
    Collection<AyantDroit> ayantsDroit;

    public Beneficiaire() {
    }

    public String getACTIF() {
        return ACTIF;
    }

    public void setACTIF(String actif) {
        ACTIF = actif;
    }

    public String getBEN_OFA_ID() {
        return BEN_OFA_ID;
    }

    public void setBEN_OFA_ID(String ben_ofa_id) {
        BEN_OFA_ID = ben_ofa_id;
    }

    public String getBEN_OFFSHORE() {
        return BEN_OFFSHORE;
    }

    public void setBEN_OFFSHORE(String ben_offshore) {
        BEN_OFFSHORE = ben_offshore;
    }

    public String getBEN_OUS_ID() {
        return BEN_OUS_ID;
    }

    public void setBEN_OUS_ID(String ben_ous_id) {
        BEN_OUS_ID = ben_ous_id;
    }

    public String getCAISSERO() {
        return CAISSERO;
    }

    public void setCAISSERO(String caissero) {
        CAISSERO = caissero;
    }

    public String getCARTEVITALE() {
        return CARTEVITALE;
    }

    public void setCARTEVITALE(String cartevitale) {
        CARTEVITALE = cartevitale;
    }

    public String getCENTRERO() {
        return CENTRERO;
    }

    public void setCENTRERO(String centrero) {
        CENTRERO = centrero;
    }

    public String getCLESS() {
        return CLESS;
    }

    public void setCLESS(String cless) {
        CLESS = cless;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String code) {
        CODE = code;
    }

    public Date getDATEADHESION() {
        return DATEADHESION;
    }

    public void setDATEADHESION(Date dateadhesion) {
        DATEADHESION = dateadhesion;
    }

    public Date getDATEPREMIEREADHESION() {
        return DATEPREMIEREADHESION;
    }

    public void setDATEPREMIEREADHESION(Date datepremiereadhesion) {
        DATEPREMIEREADHESION = datepremiereadhesion;
    }

    public Date getDATERADIATION() {
        return DATERADIATION;
    }

    public void setDATERADIATION(Date dateradiation) {
        DATERADIATION = dateradiation;
    }

    public String getENTITE_GESTION_ID() {
        return ENTITE_GESTION_ID;
    }

    public void setENTITE_GESTION_ID(String entite_gestion_id) {
        ENTITE_GESTION_ID = entite_gestion_id;
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

    public String getMUTUELLE_ID() {
        return MUTUELLE_ID;
    }

    public void setMUTUELLE_ID(String mutuelle_id) {
        MUTUELLE_ID = mutuelle_id;
    }

    public String getNUMEROSS() {
        return NUMEROSS;
    }

    public void setNUMEROSS(String numeross) {
        NUMEROSS = numeross;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public String getPERSONNE_ID() {
        return PERSONNE_ID;
    }

    public void setPERSONNE_ID(String personne_id) {
        PERSONNE_ID = personne_id;
    }

    public String getPOSNOEMIE_CODE() {
        return POSNOEMIE_CODE;
    }

    public void setPOSNOEMIE_CODE(String posnoemie_code) {
        POSNOEMIE_CODE = posnoemie_code;
    }

    public String getPRELEVEMENT_RIB_ID() {
        return PRELEVEMENT_RIB_ID;
    }

    public void setPRELEVEMENT_RIB_ID(String prelevement_rib_id) {
        PRELEVEMENT_RIB_ID = prelevement_rib_id;
    }

    public String getQUALITE_CODE() {
        return QUALITE_CODE;
    }

    public void setQUALITE_CODE(String qualite_code) {
        QUALITE_CODE = qualite_code;
    }

    public String getREGIME_CODE() {
        return REGIME_CODE;
    }

    public void setREGIME_CODE(String regime_code) {
        REGIME_CODE = regime_code;
    }

    public String getVIREMENT_RIB_ID() {
        return VIREMENT_RIB_ID;
    }

    public void setVIREMENT_RIB_ID(String virement_rib_id) {
        VIREMENT_RIB_ID = virement_rib_id;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public String getOutilGestion() {
        return outil_gestion;
    }

    public void setOutilGestion(String outil_gestion) {
        this.outil_gestion = outil_gestion;
    }

    public PostItBeneficiaire getPostItBeneficiaire() {
        return postit;
    }

    public void setPostItBeneficiaire(PostItBeneficiaire postit) {
        this.postit = postit;
    }

    public String getAdherentId() {
        return adherent_id;
    }

    public void setAdherentId(String adherent_id) {
        this.adherent_id = adherent_id;
    }

    public String getEntiteGestionSensible() {
        return entite_gestion_sensible;
    }

    public void setEntiteGestionSensible(String entite_gestion_sensible) {
        this.entite_gestion_sensible = entite_gestion_sensible;
    }

    public Collection<AyantDroit> getAyantsDroit() {
        return ayantsDroit;
    }

    public void setAyantsDroit(Collection<AyantDroit> ayantsDroit) {
        this.ayantsDroit = ayantsDroit;
    }

    public RIB getRibPrelevement() {
        return rib_prelevement;
    }

    public void setRibPrelevement(RIB rib_prelevement) {
        this.rib_prelevement = rib_prelevement;
    }

    public RIB getRibVirement() {
        return rib_virement;
    }

    public void setRibVirement(RIB rib_virement) {
        this.rib_virement = rib_virement;
    }

    public InfosRO getInfosRO() {
        return infos_ro;
    }

    public void setInfosRO(InfosRO infos_ro) {
        this.infos_ro = infos_ro;
    }

    public Collection<ContratBeneficiaire> getContratsActifs() {
        return contrats_actifs;
    }

    public void setContratsActifs(
            Collection<ContratBeneficiaire> contrats_actifs) {
        this.contrats_actifs = contrats_actifs;
    }

    public Collection<ContratBeneficiaire> getContratsRadies() {
        return contrats_radies;
    }

    public void setContratsRadies(
            Collection<ContratBeneficiaire> contrats_radies) {
        this.contrats_radies = contrats_radies;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

}
