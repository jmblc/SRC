package fr.igestion.crm.bean.contrat;

import fr.igestion.crm.bean.IdLibelleCode;

public class EntiteGestion extends IdLibelleCode {
    
    private String mutuelle_id = "";
    private String mutuelle = "";
    private String sensible = "";
    private String nbrTeleacteursActifsHabilites = "";
    private SiteWeb siteWeb = null;

    public EntiteGestion() {
    }

    public SiteWeb getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(SiteWeb siteWeb) {
        this.siteWeb = siteWeb;
    }
    
    public String getMutuelle_id() {
        return mutuelle_id;
    }

    public void setMutuelle_id(String mutuelle_id) {
        this.mutuelle_id = mutuelle_id;
    }

    public String getSensible() {
        return sensible;
    }

    public void setSensible(String sensible) {
        this.sensible = sensible;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }


    public String getNbrTeleacteursActifsHabilites() {
        return nbrTeleacteursActifsHabilites;
    }


    public void setNbrTeleacteursActifsHabilites(
            String nbrTeleacteursActifsHabilites) {
        this.nbrTeleacteursActifsHabilites = nbrTeleacteursActifsHabilites;
    }

    

}
