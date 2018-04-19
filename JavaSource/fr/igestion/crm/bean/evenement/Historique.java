package fr.igestion.crm.bean.evenement;

public class Historique {

    private String id = "";
    private String type = "";
    private String qualite = "";
    private String statut = "";
    private String estSortant = "0";
    private String estUnRenvoi = "0";
    private String dateEvenement = "";
    private String dateEvenementTri = "";
    private String objet = "";
    private String detail = "";
    private String satisfaction = "";
    private String commentaire = "";
    private String debutCommentaire = "";
    private String dateCreationDossierParent = "";

    private String aliasRST = "";
    private String courrier_id = "";

    public Historique() {
    }

    public String getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(String dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getDebutCommentaire() {
        return debutCommentaire;
    }

    public void setDebutCommentaire(String debutCommentaire) {
        this.debutCommentaire = debutCommentaire;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEstSortant() {
        return estSortant;
    }

    public void setEstSortant(String estSortant) {
        this.estSortant = estSortant;
    }

    public String getEstUnRenvoi() {
        return estUnRenvoi;
    }

    public void setEstUnRenvoi(String estUnRenvoi) {
        this.estUnRenvoi = estUnRenvoi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public String getDateCreationDossierParent() {
        return dateCreationDossierParent;
    }

    public void setDateCreationDossierParent(String dateCreationDossierParent) {
        this.dateCreationDossierParent = dateCreationDossierParent;
    }

    public String getDateEvenementTri() {
        return dateEvenementTri;
    }

    public void setDateEvenementTri(String dateEvenementTri) {
        this.dateEvenementTri = dateEvenementTri;
    }

    public String getAliasRST() {
        return aliasRST;
    }

    public void setAliasRST(String aliasRST) {
        this.aliasRST = aliasRST;
    }

    public String getCourrier_id() {
        return courrier_id;
    }

    public void setCourrier_id(String courrierId) {
        courrier_id = courrierId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

}
