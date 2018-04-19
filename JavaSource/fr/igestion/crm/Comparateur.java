package fr.igestion.crm;

import java.util.Comparator;

public class Comparateur implements Comparator {

    private int ordre;
    private String cle;

    public Comparateur(int ordre, String cle) {
        this.ordre = ordre;
        this.cle = cle;
    }

    public int compare(Object o1, Object o2) {
        if (!(o1.getClass().equals(o2.getClass()))) {
            throw new ClassCastException();
        }
        Triable c1 = (Triable) o1;
        Triable c2 = (Triable) o2;

        if ("S".equals(cle)) {
            return this.ordre
                    * (c1.getCleDeTriStr().compareTo(c2.getCleDeTriStr()));
        } else {
            return this.ordre
                    * ((int) c1.getCleDeTri() - (int) c2.getCleDeTri());
        }

    }

}
