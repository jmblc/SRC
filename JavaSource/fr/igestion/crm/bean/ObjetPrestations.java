package fr.igestion.crm.bean;

import java.util.Collection;

import fr.igestion.crm.bean.contrat.Acte;
import fr.igestion.crm.bean.contrat.Decompte;

public class ObjetPrestations {

    private Collection<String> dates_decomptes = null;
    private Collection<Acte> codes_actes = null;
    private Collection<Decompte> decomptes = null;

    public ObjetPrestations() {
    }

    public Collection<Acte> getCodesActes() {
        return codes_actes;
    }

    public void setCodesActes(Collection<Acte> codes_actes) {
        this.codes_actes = codes_actes;
    }

    public Collection<String> getDatesDecomptes() {
        return dates_decomptes;
    }

    public void setDatesDecomptes(Collection<String> dates_decomptes) {
        this.dates_decomptes = dates_decomptes;
    }

    public Collection<Decompte> getDecomptes() {
        return decomptes;
    }

    public void setDecomptes(Collection<Decompte> decomptes) {
        this.decomptes = decomptes;
    }

}
