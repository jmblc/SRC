package fr.igestion.crm.bean.scenario;

public class Point extends ItemScenario{

    private String sous_motif_id = "";

    public Point() {
    }
 
    public String getSousMotifId() {
        return sous_motif_id;
    }

    public void setSousMotifId(String sousMotifId) {
        sous_motif_id = sousMotifId;
    }
 
}
