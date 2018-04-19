package fr.igestion.crm;

import java.io.File;
import java.text.Normalizer;
import javax.servlet.http.HttpServletRequest;
import common.Logger;
import fr.igestion.crm.bean.contrat.Mutuelle;

public class UtilHtml {
 

    private static final Logger LOGGER = Logger.getLogger(UtilHtml.class);
    
    public static final String _html_anchor_motif = "Motif";
    public static final String _html_anchor_sous_motif = "SousMotif";
    public static final String _html_anchor_haut="Haut";
    public static final String _html_anchor_bas="Bas";
    
    private UtilHtml() {
    }
   
    public static String makeProper(String theString) {
        
        StringBuilder properCase = new StringBuilder();
        
        try{
            java.io.StringReader in = new java.io.StringReader(
                    theString.toLowerCase());
            boolean precededBySpace = true;
            while (true) {
                int i = in.read();
                if (i == -1) {
                    break;
                }
                char c = (char) i;
                if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/'
                        || c == '\\' || c == ',') {
                    properCase.append(c);
                    precededBySpace = true;
                } else {
                    if (precededBySpace) {
                        properCase.append(Character.toUpperCase(c));
                    } else {
                        properCase.append(c);
                    }
                    precededBySpace = false;
                }
            }
        }
        catch(Exception e){
            LOGGER.error("makeProper",e);
            throw new IContactsException(e);
        }
        return properCase.toString();
    }

    public static void setAnchor(HttpServletRequest request, String ancre) {
        request.setAttribute("ancre", ancre);
    }

    public static String formatChainePourInfosBulles(String chaine_a_formater) {
        String res = chaine_a_formater;
        res = res.replaceAll("\r", "<br>");
        res = res.replaceAll("\n", "");
        res = res.replaceAll("'", "\\\\'");
        res = res.replaceAll("\"", " ");
        return res;
    }

    public static String getLogoMutuelle(Mutuelle mutuelle,
            HttpServletRequest request) {
        String str_image_logo = "&nbsp";
        String chemin_logo_mutuelle = request.getRealPath(File.separator)
                + "img" + File.separator + "clients" + File.separator
                + mutuelle.getId() + ".gif";

        File fichier_logo = new File(chemin_logo_mutuelle);
        if (fichier_logo.exists()) {
            str_image_logo = "<img src=\"img/clients/" + mutuelle.getId()
                    + ".gif" + "\" border=\"0\"/>";
        } else {
            str_image_logo = "<label class=\"logo\">" + mutuelle.getLibelle()
                    + "</label>";
        }
        return str_image_logo;
    }

    public static String supprimerAccents(String s) {
        String text = Normalizer.normalize(s, Normalizer.Form.NFD);
        return text.replaceAll("[^\\p{ASCII}]", "");
    }

}
