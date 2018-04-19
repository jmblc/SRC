package fr.igestion.crm.bean.contrat;

import java.util.ArrayList;
import java.util.Collection;

public class Decompte {

    private String id = "";
    private String date_decompte = null;
    private String numero_decompte = "";

    private float total_depenses;
    private float total_remboursements_secu;
    private float total_remboursements_mutuelle;

    private Collection<Prestation> prestations = new ArrayList<Prestation>();

    public Decompte() {

    }

    public String getDateDecompte() {
        return date_decompte;
    }

    public void setDateDecompte(String date_decompte) {
        this.date_decompte = date_decompte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroDecompte() {
        return numero_decompte;
    }

    public void setNumeroDecompte(String numero_decompte) {
        this.numero_decompte = numero_decompte;
    }

    public Collection<Prestation> getPrestations() {
        return prestations;
    }

    public void setPrestations(Collection<Prestation> prestations) {
        this.prestations = prestations;
    }

    public float getTotalDepenses() {
        return total_depenses;
    }

    public void setTotalDepenses(float total_depenses) {
        this.total_depenses = total_depenses;
    }

    public float getTotalRemboursementsMutuelle() {
        return total_remboursements_mutuelle;
    }

    public void setTotalRemboursementsMutuelle(
            float total_remboursements_mutuelle) {
        this.total_remboursements_mutuelle = total_remboursements_mutuelle;
    }

    public float getTotalRemboursementsSecu() {
        return total_remboursements_secu;
    }

    public void setTotalRemboursementsSecu(float total_remboursements_secu) {
        this.total_remboursements_secu = total_remboursements_secu;
    }

}
