package fr.igestion.crm.bean;

import java.util.Collection;

import fr.igestion.crm.bean.contrat.AbonnementService;
import fr.igestion.crm.bean.evenement.Historique;

public class ObjetAppelant {

    private String type = "";
    private String onglet_courant = "";
    private String lisible = "1";
    private Object objet = null;
    private Object detail_objet = null;
    private ObjetPrestations objet_prestations = null;
    private Collection<Historique> historique = null;
    private Collection<AbonnementService> abonnements; 

    private boolean possede_contrats_individuels = false,
            possede_contrats_collectifs = false;
    private boolean possede_contrats_individuels_a_gestion_collective = false,
            possede_contrats_en_contentieux = false,possede_CNSD=false;

    public ObjetAppelant() {
    }

    public Object getObjet() {
        return objet;
    }

    public void setObjet(Object objet) {
        this.objet = objet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<Historique> getHistorique() {
        return historique;
    }

    public void setHistorique(Collection<Historique> historique) {
        this.historique = historique;
    }

    public String getOngletCourant() {
        return onglet_courant;
    }

    public void setOngletCourant(String onglet_courant) {
        this.onglet_courant = onglet_courant;
    }

    public Object getDetailObjet() {
        return detail_objet;
    }

    public void setDetailObjet(Object detail_objet) {
        this.detail_objet = detail_objet;
    }

    public ObjetPrestations getObjetPrestations() {
        return objet_prestations;
    }

    public void setObjetPrestations(ObjetPrestations objet_prestations) {
        this.objet_prestations = objet_prestations;
    }

    public String getLisible() {
        return lisible;
    }

    public void setLisible(String lisible) {
        this.lisible = lisible;
    }

    public boolean hasContratsIndividuels() {
        return possede_contrats_individuels;
    }

    public void setContratsIndividuels(boolean possedeContratsIndividuels) {
        possede_contrats_individuels = possedeContratsIndividuels;
    }

    public boolean hasContratsCollectifs() {
        return possede_contrats_collectifs;
    }
    public boolean hasCNSD() {
        return possede_CNSD;
    }
    public void setCNSD(boolean possedeCNDS) {
    	possede_CNSD = possedeCNDS;
    }

    public void setContratsCollectifs(boolean possedeContratsCollectifs) {
        possede_contrats_collectifs = possedeContratsCollectifs;
    }

    public boolean hasContratsIndividuelsAGestionCollective() {
        return possede_contrats_individuels_a_gestion_collective;
    }

    public void setContratsIndividuelsAGestionCollective(
            boolean possedeContratsIndividuelsAGestionCollective) {
        possede_contrats_individuels_a_gestion_collective = possedeContratsIndividuelsAGestionCollective;
    }

    public boolean hasContratsEnContentieux() {
        return possede_contrats_en_contentieux;
    }

    public void setContratsEnContentieux(boolean possedeContratsEnContentieux) {
        possede_contrats_en_contentieux = possedeContratsEnContentieux;
    }

    public Collection<AbonnementService> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(Collection<AbonnementService> abonnements) {
        this.abonnements = abonnements;
    }
}
