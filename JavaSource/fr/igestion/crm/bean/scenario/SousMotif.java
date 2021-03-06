package fr.igestion.crm.bean.scenario;

public class SousMotif extends ItemScenario{
    
    private String MOTIF_ID = "";

    private String libelleReferenceExterne = "";
    private String idReferenceExterne = "";
    private String flux_transfert_client = "";

    public SousMotif() {
    }
 
    public String getMOTIF_ID() {
        return MOTIF_ID;
    }

    public void setMOTIF_ID(String motif_id) {
        MOTIF_ID = motif_id;
    }

    public String getIdReferenceExterne() {
        return idReferenceExterne;
    }

    public void setIdReferenceExterne(String idReferenceExterne) {
        this.idReferenceExterne = idReferenceExterne;
    }

    public String getLibelleReferenceExterne() {
        return libelleReferenceExterne;
    }

    public void setLibelleReferenceExterne(String libelleReferenceExterne) {
        this.libelleReferenceExterne = libelleReferenceExterne;
    }

    public String getFluxTransfertClient() {
        return flux_transfert_client;
    }

    public void setFluxTransfertClient(String flux_transfert_client) {
        this.flux_transfert_client = flux_transfert_client;
    }

}
