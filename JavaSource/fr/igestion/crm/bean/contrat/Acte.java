package fr.igestion.crm.bean.contrat;

import fr.igestion.crm.bean.LibelleCode;

public class Acte extends LibelleCode {

    private String id = "";
    private String familleacte_id = "";

    public Acte() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilleacte_id() {
        return familleacte_id;
    }

    public void setFamilleacte_id(String familleacte_id) {
        this.familleacte_id = familleacte_id;
    }

}
