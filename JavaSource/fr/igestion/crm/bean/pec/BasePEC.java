package fr.igestion.crm.bean.pec;


public class BasePEC {
    
    public static final String _TYPE_ETAB_PUBLIC = "Public";
    public static final String _TYPE_ETAB_PRIVE = "Privé";
    
    private String id;
    private String mutuelle;
    private String commentaire;
    private String statut;
    private String envoiCourrier;
    private String envoiFax;
    private String envoiMail;
    private String dureeHospitalisation;
    
    private String fraisSejour;
    private String forfait18;
    private String forfaitJournalier;
    private String chambreParticuliere;
    private String ticketModerateur;
    private String litParent;
    private String honoraire;
    
    
    public String getFraisSejour() {
        return fraisSejour;
    }

    public void setFraisSejour(String fraisSejour) {
        this.fraisSejour = fraisSejour;
    }

    public String getForfait18() {
        return forfait18;
    }

    public void setForfait18(String forfait18) {
        this.forfait18 = forfait18;
    }

    public String getHonoraire() {
        return honoraire;
    }

    public void setHonoraire(String honoraire) {
        this.honoraire = honoraire;
    }

    public String getLitParent() {
        return litParent;
    }

    public void setLitParent(String litParent) {
        this.litParent = litParent;
    }

    public String getTicketModerateur() {
        return ticketModerateur;
    }

    public void setTicketModerateur(String ticketModerateur) {
        this.ticketModerateur = ticketModerateur;
    }

    public String getChambreParticuliere() {
        return chambreParticuliere;
    }

    public void setChambreParticuliere(String chambreParticuliere) {
        this.chambreParticuliere = chambreParticuliere;
    }

    public String getForfaitJournalier() {
        return forfaitJournalier;
    }

    public void setForfaitJournalier(String forfaitJournalier) {
        this.forfaitJournalier = forfaitJournalier;
    }

    public String getDureeHospitalisation() {
        return dureeHospitalisation;
    }

    public void setDureeHospitalisation(String dureeHospitalisation) {
        this.dureeHospitalisation = dureeHospitalisation;
    }

    public String getEnvoiCourrier() {
        return envoiCourrier;
    }

    public void setEnvoiCourrier(String envoiCourrier) {
        this.envoiCourrier = envoiCourrier;
    }

    public String getEnvoiFax() {
        return envoiFax;
    }

    public void setEnvoiFax(String envoiFax) {
        this.envoiFax = envoiFax;
    }

    public String getEnvoiMail() {
        return envoiMail;
    }

    public void setEnvoiMail(String envoiMail) {
        this.envoiMail = envoiMail;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
