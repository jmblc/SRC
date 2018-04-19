package fr.igestion.crm.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class GarantieRecherche {

    private String PLA_ID = "";
    private String PLA_MUTUELLE_ID = "";
    private String PLA_PRODUIT_ID = "";
    private String PLA_RISQUE_ID = "";
    private String PLA_RISQUE_OPTION_ID = "";
    private String PLA_REGIME_ID = "";
    private String PLA_CREATEUR_ID = "";
    private String LIBELLE_MUTUELLE = "";
    private String LIBELLE_PRODUIT = "";
    private String LIBELLE_RISQUE = "";
    private String LIBELLE_RISQUE_OPTION = "";
    private String LIBELLE = "";
    private String CODERT_PRODUIT = "";
    private String CODE_RISQUE = "";
    private String CODE_RISQUE_OPTION = "";
    private String LIBELLE_REGIME = "";
    private String PLA_BLOCAGE = "";
    private String PLA_UTL_BLOCAGE = "";
    private Date PLA_DATE_BLOCAGE;
    private Date PLA_DATE_CREATION;
    private String NIVEAU = "";

    private int nombre_versions = 0;
    private int nombre_versions_actives = 0;

    private String NOM_PRENOM_CREATEUR = "";

    private Collection<VersionGarantie> versions = new ArrayList<VersionGarantie>();

    public GarantieRecherche() {
    }

    public String getCODERT_PRODUIT() {
        return CODERT_PRODUIT;
    }

    public void setCODERT_PRODUIT(String codert_produit) {
        CODERT_PRODUIT = codert_produit;
    }

    public String getLIBELLE_MUTUELLE() {
        return LIBELLE_MUTUELLE;
    }

    public void setLIBELLE_MUTUELLE(String libelle_mutuelle) {
        LIBELLE_MUTUELLE = libelle_mutuelle;
    }

    public String getLIBELLE_PRODUIT() {
        return LIBELLE_PRODUIT;
    }

    public void setLIBELLE_PRODUIT(String libelle_produit) {
        LIBELLE_PRODUIT = libelle_produit;
    }

    public String getLIBELLE_REGIME() {
        return LIBELLE_REGIME;
    }

    public void setLIBELLE_REGIME(String libelle_regime) {
        LIBELLE_REGIME = libelle_regime;
    }

    public String getPLA_ID() {
        return PLA_ID;
    }

    public void setPLA_ID(String pla_id) {
        PLA_ID = pla_id;
    }

    public String getPLA_MUTUELLE_ID() {
        return PLA_MUTUELLE_ID;
    }

    public void setPLA_MUTUELLE_ID(String pla_mutuelle_id) {
        PLA_MUTUELLE_ID = pla_mutuelle_id;
    }

    public String getPLA_PRODUIT_ID() {
        return PLA_PRODUIT_ID;
    }

    public void setPLA_PRODUIT_ID(String pla_produit_id) {
        PLA_PRODUIT_ID = pla_produit_id;
    }

    public String getPLA_RISQUE_ID() {
        return PLA_RISQUE_ID;
    }

    public void setPLA_RISQUE_ID(String pla_risque_id) {
        PLA_RISQUE_ID = pla_risque_id;
    }

    public String getPLA_REGIME_ID() {
        return PLA_REGIME_ID;
    }

    public void setPLA_REGIME_ID(String pla_regime_id) {
        PLA_REGIME_ID = pla_regime_id;
    }

    public String getPLA_BLOCAGE() {
        return PLA_BLOCAGE;
    }

    public void setPLA_BLOCAGE(String pla_blocage) {
        PLA_BLOCAGE = pla_blocage;
    }

    public Date getPLA_DATE_BLOCAGE() {
        return PLA_DATE_BLOCAGE;
    }

    public void setPLA_DATE_BLOCAGE(Date pla_date_blocage) {
        PLA_DATE_BLOCAGE = pla_date_blocage;
    }

    public String getPLA_UTL_BLOCAGE() {
        return PLA_UTL_BLOCAGE;
    }

    public void setPLA_UTL_BLOCAGE(String pla_utl_blocage) {
        PLA_UTL_BLOCAGE = pla_utl_blocage;
    }

    public int getNombreVersions() {
        return nombre_versions;
    }

    public void setNombreVersions(int nombre_versions) {
        this.nombre_versions = nombre_versions;
    }

    public int getNombreVersionsActives() {
        return nombre_versions_actives;
    }

    public void setNombreVersionsActives(int nombre_versions_actives) {
        this.nombre_versions_actives = nombre_versions_actives;
    }

    public Collection<VersionGarantie> getVersions() {
        return versions;
    }

    public void setVersions(Collection<VersionGarantie> versions) {
        this.versions = versions;
    }

    public String getPLA_CREATEUR_ID() {
        return PLA_CREATEUR_ID;
    }

    public void setPLA_CREATEUR_ID(String pla_createur_id) {
        PLA_CREATEUR_ID = pla_createur_id;
    }

    public Date getPLA_DATE_CREATION() {
        return PLA_DATE_CREATION;
    }

    public void setPLA_DATE_CREATION(Date pla_date_creation) {
        PLA_DATE_CREATION = pla_date_creation;
    }

    public String getNOM_PRENOM_CREATEUR() {
        return NOM_PRENOM_CREATEUR;
    }

    public void setNOM_PRENOM_CREATEUR(String nom_prenom_createur) {
        NOM_PRENOM_CREATEUR = nom_prenom_createur;
    }

    public String getCODE_RISQUE() {
        return CODE_RISQUE;
    }

    public void setCODE_RISQUE(String code_risque) {
        CODE_RISQUE = code_risque;
    }

    public String getLIBELLE_RISQUE() {
        return LIBELLE_RISQUE;
    }

    public void setLIBELLE_RISQUE(String libelle_risque) {
        LIBELLE_RISQUE = libelle_risque;
    }

    public String getCODE_RISQUE_OPTION() {
        return CODE_RISQUE_OPTION;
    }

    public void setCODE_RISQUE_OPTION(String code_risque_option) {
        CODE_RISQUE_OPTION = code_risque_option;
    }

    public String getLIBELLE_RISQUE_OPTION() {
        return LIBELLE_RISQUE_OPTION;
    }

    public void setLIBELLE_RISQUE_OPTION(String libelle_risque_option) {
        LIBELLE_RISQUE_OPTION = libelle_risque_option;
    }

    public String getPLA_RISQUE_OPTION_ID() {
        return PLA_RISQUE_OPTION_ID;
    }

    public void setPLA_RISQUE_OPTION_ID(String pla_risque_option_id) {
        PLA_RISQUE_OPTION_ID = pla_risque_option_id;
    }

    public String getLIBELLE() {
        return LIBELLE;
    }

    public void setLIBELLE(String libelle) {
        LIBELLE = libelle;
    }

    public String getNIVEAU() {
        return NIVEAU;
    }

    public void setNIVEAU(String niveau) {
        NIVEAU = niveau;
    }

}
