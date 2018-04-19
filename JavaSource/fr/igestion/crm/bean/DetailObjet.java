package fr.igestion.crm.bean;

public class DetailObjet {

    private String vip = "";
    private String npai = "";
    private String radie = "";
    private String code_vip = "";
    private String libelle_vip = "";
    private String id_objet = "";

    public DetailObjet() {
    }

    public String getCodeVip() {
        return code_vip;
    }

    public void setCodeVip(String code_vip) {
        this.code_vip = code_vip;
    }

    public String getIdObjet() {
        return id_objet;
    }

    public void setIdObjet(String id_objet) {
        this.id_objet = id_objet;
    }

    public String getLibelleVip() {
        return libelle_vip;
    }

    public void setLibelleVip(String libelle_vip) {
        this.libelle_vip = libelle_vip;
    }

    public String isNpai() {
        return npai;
    }

    public void setNpai(String npai) {
        this.npai = npai;
    }

    public String isRadie() {
        return radie;
    }

    public void setRadie(String radie) {
        this.radie = radie;
    }

    public String isVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

}
