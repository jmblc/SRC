package fr.igestion.crm.bean;

public class TeleActeur {
    private String password = "";
    private String login = "";
    private String extension = "";
    private String codeCivilite = "";
    private String libelleCivilite = "";
    private String nom = "";
    private String prenom = "";
    private String email = "";
    private String role = "";
    private String poste = "";
    private String acdName = "";
    private String actif = "";
    private String id = "";
    private String annuaireId = "";
    private String site = "";
    private String libelleTransfert = "";
    private String utl_Id = "";
    private String idHermes = "";
    private String ongletsFiches = "";
    private String societe = "";
    private String service = "";
    private String pole = "";
    private String HCH_ADMINISTRATION = "";
    private String HCH_STATISTIQUES = "";
    private String EXCLU_MESSAGE_CONFIDENTIALITE = "";

    public TeleActeur() {

    }

    public String getUtl_Id() {
        return utl_Id;
    }

    public void setUtl_Id(String utl_Id) {
        this.utl_Id = utl_Id;
    }

    public String getAnnuaireId() {
        return annuaireId;
    }

    public void setAnnuaireId(String annuaireId) {
        this.annuaireId = annuaireId;
    }

    public String getCodeCivilite() {
        return codeCivilite;
    }

    public void setCodeCivilite(String codeCivilite) {
        this.codeCivilite = codeCivilite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAcdName() {
        return acdName;
    }

    public void setAcdName(String acdName) {
        this.acdName = acdName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getLibelleCivilite() {
        return libelleCivilite;
    }

    public void setLibelleCivilite(String libelleCivilite) {
        this.libelleCivilite = libelleCivilite;
    }

    public String getLibelleTransfert() {
        return libelleTransfert;
    }

    public void setLibelleTransfert(String libelleTransfert) {
        this.libelleTransfert = libelleTransfert;
    }

    public String getNomPrenom() {
        return nom + " " + prenom;
    }

    public String getPrenomNom() {
        return prenom + " " + nom;
    }

    public String getIdHermes() {
        return idHermes;
    }

    public void setIdHermes(String idHermes) {
        this.idHermes = idHermes;
    }

    public String getOngletsFiches() {
        return ongletsFiches;
    }

    public void setOngletsFiches(String ongletsFiches) {
        this.ongletsFiches = ongletsFiches;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getHCH_ADMINISTRATION() {
        return HCH_ADMINISTRATION;
    }

    public void setHCH_ADMINISTRATION(String hch_administration) {
        HCH_ADMINISTRATION = hch_administration;
    }

    public String getHCH_STATISTIQUES() {
        return HCH_STATISTIQUES;
    }

    public void setHCH_STATISTIQUES(String hCHSTATISTIQUES) {
        HCH_STATISTIQUES = hCHSTATISTIQUES;
    }

    public String getEXCLU_MESSAGE_CONFIDENTIALITE() {
        return EXCLU_MESSAGE_CONFIDENTIALITE;
    }

    public void setEXCLU_MESSAGE_CONFIDENTIALITE(
            String eXCLUMESSAGECONFIDENTIALITE) {
        EXCLU_MESSAGE_CONFIDENTIALITE = eXCLUMESSAGECONFIDENTIALITE;
    }

}
