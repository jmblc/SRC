package fr.igestion.crm.bean;

public class ComptageSalaries {

    private String code = "";
    private int nbr_adherent = 0;
    private int nbr_autres = 0;
    private int total = 0;

    public ComptageSalaries() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNbrAdherent() {
        return nbr_adherent;
    }

    public void setNbrAdherent(int nbrAdherent) {
        nbr_adherent = nbrAdherent;
    }

    public int getNbrAutres() {
        return nbr_autres;
    }

    public void setNbrAutres(int nbrAutres) {
        nbr_autres = nbrAutres;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
