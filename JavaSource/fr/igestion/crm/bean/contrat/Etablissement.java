package fr.igestion.crm.bean.contrat;

import java.util.Collection;
import java.util.Date;

import fr.igestion.crm.bean.LibelleCode;
import fr.igestion.crm.bean.PostItEtablissement;
import fr.igestion.crm.bean.Salarie;

public class Etablissement {

    private String ID = "";
    private String CODE = "";
    private String LIBELLE = "";
    private String SIRET = "";
    private String APE = "";
    private String MUTUELLE_ID = "";
    private String ENTREPRISE_ID = "";
    private String ADRESSE_ID = "";
    private String ENTITE_GESTION_ID = "";
    private String GRANDCOMPTE_ID = "";
    private String ACTIF = "";
    private Date DATERADIATION = null;

    private String ETB_OFFSHORE = "";
    private String ETB_OFA_ID = "";
    private String ETB_OUS_ID = "";

    String outil_gestion = "";
    String entite_gestion = "";
    String entite_gestion_sensible = "";
    String correspondant_adresse_id = "";
    String correspondant_personne_id = "";

    private Collection<Salarie> salaries = null;
    private Collection<ContratEtablissement> contrats_actifs;
    private Collection<ContratEtablissement> contrats_radies;
    private Collection<LibelleCode> all_contrats;

    PostItEtablissement postit = null;
    private Adresse adresse = null;
    private Correspondant correspondant = null;

    public Etablissement() {
    }

    public String getACTIF() {
        return ACTIF;
    }

    public void setACTIF(String actif) {
        ACTIF = actif;
    }

    public String getADRESSE_ID() {
        return ADRESSE_ID;
    }

    public void setADRESSE_ID(String adresse_id) {
        ADRESSE_ID = adresse_id;
    }

    public String getAPE() {
        return APE;
    }

    public void setAPE(String ape) {
        APE = ape;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String code) {
        CODE = code;
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

    public String getENTREPRISE_ID() {
        return ENTREPRISE_ID;
    }

    public void setENTREPRISE_ID(String entreprise_id) {
        ENTREPRISE_ID = entreprise_id;
    }

    public String getETB_OFA_ID() {
        return ETB_OFA_ID;
    }

    public void setETB_OFA_ID(String etb_ofa_id) {
        ETB_OFA_ID = etb_ofa_id;
    }

    public String getETB_OUS_ID() {
        return ETB_OUS_ID;
    }

    public void setETB_OUS_ID(String etb_ous_id) {
        ETB_OUS_ID = etb_ous_id;
    }

    public String getETB_OFFSHORE() {
        return ETB_OFFSHORE;
    }

    public void setETB_OFFSHORE(String etb_offshore) {
        ETB_OFFSHORE = etb_offshore;
    }

    public String getEntiteGestionSensible() {
        return entite_gestion_sensible;
    }

    public void setEntiteGestionSensible(String entite_gestion_sensible) {
        this.entite_gestion_sensible = entite_gestion_sensible;
    }

    public String getGRANDCOMPTE_ID() {
        return GRANDCOMPTE_ID;
    }

    public void setGRANDCOMPTE_ID(String grandcompte_id) {
        GRANDCOMPTE_ID = grandcompte_id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getLIBELLE() {
        return LIBELLE;
    }

    public void setLIBELLE(String libelle) {
        LIBELLE = libelle;
    }

    public String getOutilGestion() {
        return outil_gestion;
    }

    public void setOutilGestion(String outil_gestion) {
        this.outil_gestion = outil_gestion;
    }

    public String getMUTUELLE_ID() {
        return MUTUELLE_ID;
    }

    public void setMUTUELLE_ID(String mutuelle_id) {
        MUTUELLE_ID = mutuelle_id;
    }

    public String getSIRET() {
        return SIRET;
    }

    public void setSIRET(String siret) {
        SIRET = siret;
    }

    public Collection<ContratEtablissement> getContratsActifs() {
        return contrats_actifs;
    }

    public void setContratsActifs(
            Collection<ContratEtablissement> contrats_actifs) {
        this.contrats_actifs = contrats_actifs;
    }

    public Collection<ContratEtablissement> getContratsRadies() {
        return contrats_radies;
    }

    public void setContratsRadies(
            Collection<ContratEtablissement> contrats_radies) {
        this.contrats_radies = contrats_radies;
    }

    public Collection<LibelleCode> getAllContrats() {
        return all_contrats;
    }

    public void setAllContrats(Collection<LibelleCode> allContrats) {
        all_contrats = allContrats;
    }

    public PostItEtablissement getPostItEtablissement() {
        return postit;
    }

    public void setPostItEtablissement(PostItEtablissement postit) {
        this.postit = postit;
    }

    public Collection<Salarie> getSalaries() {
        return salaries;
    }

    public void setSalaries(Collection<Salarie> salaries) {
        this.salaries = salaries;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getEntiteGestion() {
        return entite_gestion;
    }

    public void setEntiteGestion(String entite_gestion) {
        this.entite_gestion = entite_gestion;
    }

    public String getCorrespondantAdresseId() {
        return correspondant_adresse_id;
    }

    public void setCorrespondantAdresseId(String correspondant_adresse_id) {
        this.correspondant_adresse_id = correspondant_adresse_id;
    }

    public String getCorrespondantPersonneId() {
        return correspondant_personne_id;
    }

    public void setCorrespondantPersonneId(String correspondant_personne_id) {
        this.correspondant_personne_id = correspondant_personne_id;
    }

    public Correspondant getCorrespondant() {
        return correspondant;
    }

    public void setCorrespondant(Correspondant correspondant) {
        this.correspondant = correspondant;
    }

}
