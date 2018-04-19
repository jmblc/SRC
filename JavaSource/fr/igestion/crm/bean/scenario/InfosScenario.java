package fr.igestion.crm.bean.scenario;

public class InfosScenario {

    private String id;
    private String campagneLib;
    private String mutuelleLib;
    private String libelle;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampagneLib() {
        return campagneLib;
    }

    public void setCampagneLib(String campagneLib) {
        this.campagneLib = campagneLib;
    }

    public String getMutuelleLib() {
        return mutuelleLib;
    }

    public void setMutuelleLib(String mutuelleLib) {
        this.mutuelleLib = mutuelleLib;
    }

}
