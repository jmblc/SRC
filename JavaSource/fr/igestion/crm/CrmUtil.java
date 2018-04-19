package fr.igestion.crm;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.Appelant;
import fr.igestion.crm.bean.ObjetRecherche;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.Adresse;
import fr.igestion.crm.bean.contrat.Beneficiaire;
import fr.igestion.crm.bean.contrat.Etablissement;
import fr.igestion.crm.bean.contrat.Personne;
import fr.igestion.crm.config.IContacts;

public class CrmUtil {

    private static final Logger LOGGER = Logger.getLogger(CrmUtil.class);
    
    private static String contextPath = IContacts.getInstance().getContextPath();  

    private static final String _UTF8="UTF-8";
    
    private static DecimalFormat fChiffres_2 = new DecimalFormat("0.00");
    private static DecimalFormat fChiffres_3 = new DecimalFormat("0.000");

    private static Font separateur = new Font(FontFamily.HELVETICA, 14,
            Font.BOLD, BaseColor.BLACK);
    private static Font noir11 = new Font(FontFamily.HELVETICA, 12,
            Font.NORMAL, BaseColor.BLACK);
    private static Font bleu11 = new Font(FontFamily.HELVETICA, 12,
            Font.NORMAL, new BaseColor(49, 106, 197));

    private CrmUtil() {
    }
   
    public static String formatChiffres_2(float chiffre) {
        return fChiffres_2.format(chiffre);
    }

    public static String formatChiffres_3(float chiffre) {
        return fChiffres_3.format(chiffre);
    }
   
    public static StringBuilder getTableauFichesAppelsRecherchees(Collection fiches_appels_recherchees) {
    	
    	StringBuilder sb = new StringBuilder("");

        if (fiches_appels_recherchees != null
                && !fiches_appels_recherchees.isEmpty()) {
            sb.append("<table class='m_table' cellspacing='0' width='100%'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"DATEAPPEL\")' nowrap='nowrap' align='center'>Date Appel <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"AUTEUR\")' nowrap='nowrap' align='center'>Auteur <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"ID\")' nowrap='nowrap' align='center'>ID Fiche<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"POLE\")' nowrap='nowrap' align='center'>P&ocirc;le <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"MUTUELLE\")' nowrap='nowrap' align='center'>Mutuelle <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"EG\")' nowrap='nowrap' align='center'>Entit&eacute; de gestion <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"MOTIF\")' nowrap='nowrap' align='center'>Motif <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"SOUSMOTIF\")' nowrap='nowrap' align='center'>Sous-motif<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"POINT\")' nowrap='nowrap' align='center'>Point <img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"SOUSPOINT\")' nowrap='nowrap' align='center'>Sous-point<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"TYPEAPPELANT\")' nowrap='nowrap' align='center'>Type appelant<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"CODEADHERENTNUMEROCONTRAT\")' nowrap='nowrap' align='center'>Code adh&eacute;rent<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierFichesAppelsPar(\"STATUT\")' nowrap='nowrap' align='center'>Statut<img src='"+ contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("</tr>");

            String appel_id = "", couleur_ligne = "m_tr_noir", fonction_javascript = "", commentaires = "";
            for (int i = 0; i < fiches_appels_recherchees.size(); i++) {
                Appel appel = (Appel) fiches_appels_recherchees.toArray()[i];
                appel_id = appel.getID();
                fonction_javascript = "Javascript:ouvrirFicheAppel('"
                        + appel_id + "','E', 'RECHERCHEFICHESAPPELS')";
                commentaires = appel.getCOMMENTAIRE();

                sb.append("<tr class=\""
                        + couleur_ligne
                        + "\" onclick=\""
                        + fonction_javascript
                        + "\" onmouseover=\"this.className='m_tr_selected'\" onmouseout=\"this.className='"
                        + couleur_ligne + "'\">");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL())
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getTeleacteur() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>" + appel_id
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'><img src=\""+ contextPath + "/img/SCANNER.gif\"  class=\"message_box\" id=\"id_ib_resume_fiche_"
                        + i
                        + "\" disposition=\"right-middle\" message=\""
                        + commentaires
                        + "\"  />&nbsp;"
                        + appel.getPole()
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getMutuelle() + "</td>");

                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getEntiteGestion() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getMotif() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getSousMotif() + "</td>");

                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getPoint() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getSousPoint() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getTypeAppelant() + "</td>");

                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getCodeAdherentNumeroContrat() + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + appel.getStatut() + "</td>");
                sb.append("</tr>");
            }

            sb.append("</table>");
        }

        return sb;
    }
    
    public static StringBuilder getTableauAssuresRecherches(Collection assures_recherches) {
    	return getTableauAssuresRecherches(assures_recherches, false);
    }

    public static StringBuilder getTableauAssuresRecherches(Collection assures_recherches, boolean isRechercheAux) {
    	
        StringBuilder sb = new StringBuilder("");       

        if (assures_recherches != null && !assures_recherches.isEmpty()) {

            sb.append("<table class='m_table' cellspacing='0' width='100%'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"MUTUELLE\")' nowrap='nowrap' align='center'>Mutuelle <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"CODEADHERENTNUMEROCONTRAT\")' nowrap='nowrap' align='center'>Num&eacute;ro adh&eacute;rent <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"CIVILITE\")' nowrap='nowrap' align='center'>Civilit&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"NOM\")' nowrap='nowrap' align='center'>Nom <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"PRENOM\")' nowrap='nowrap' align='center'>Pr&eacute;nom <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"QUALITE\")' nowrap='nowrap' align='center'>Qualit&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"DATENAISSANCE\")' nowrap='nowrap' align='center'>Date de naissance <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"NUMEROSECU\")' nowrap='nowrap' align='center'>Num&eacute;ro de s&eacute;curit&eacute; sociale <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"RADIE\")' nowrap='nowrap' align='center'>Radi&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"OUTILGESTION\")' nowrap='nowrap' align='center'>Outil de gestion <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAssuresPar(\"ENTITEGESTION\")' nowrap='nowrap' align='center'>Entité Gestion <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("</tr>");

            String ben_id = "", couleur_ligne = "m_tr_noir", couleur_ligne_selection = "", fonction_javascript = "";
            ObjetRecherche assure = null;
            for (int i = 0; i < assures_recherches.size(); i++) {
                assure = (ObjetRecherche) assures_recherches.toArray()[i];
                ben_id = assure.getId();

                couleur_ligne = ((assure.getRadie() != null && "0"
                        .equals(assure.getRadie()))) ? "m_tr_gris"
                        : "m_tr_noir";
                if (assure.getClickable() != null && "C".equals(assure.getClickable())) {
                    fonction_javascript = "Javascript:ficheAppelSetAssure('" + ben_id + "', " + isRechercheAux + ")";
                    couleur_ligne_selection = "m_tr_selected";
                } else {
                    fonction_javascript = "Javascript:alert('Cet assuré ne peut être sélectionné car il n\\\'appartient pas à la mutuelle en cours.\\n\\nSi vous souhaitez le sélectionner, vous devez changer la mutuelle en cours.')";
                    couleur_ligne_selection = "m_tr_selected_sans_main";
                }
                if (assure.getEntite_gestion_id() =="-1") {
                    fonction_javascript = "Javascript:alert('Cet assuré ne peut être sélectionné car il n\\\'appartient pas à l\\\'entité de gestion (cloisonnement).')";
                    couleur_ligne_selection = "m_tr_selected_sans_main";
                }
                
                sb.append("<tr class=\"" + couleur_ligne + "\" onclick=\""
                        + fonction_javascript
                        + "\" onmouseover=\"this.className='"
                        + couleur_ligne_selection
                        + "'\" onmouseout=\"this.className='" + couleur_ligne
                        + "'\">");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getMutuelle() != null) ? assure
                                .getMutuelle() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getCodeAdherentNumeroContrat() != null) ? assure
                                .getCodeAdherentNumeroContrat() : "&nbsp;")
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getCivilite() != null) ? assure
                                .getCivilite() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getNom() != null) ? assure.getNom()
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getPrenom() != null) ? assure.getPrenom()
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getQualite() != null) ? assure.getQualite()
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + (UtilDate.formatDDMMYYYY(assure.getDateNaissance()))
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getNumeroSecu() != null) ? assure
                                .getNumeroSecu() + "&nbsp;" : "&nbsp;")
                        + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap' align='center'>"
                        + ((assure.getRadie() != null && "0".equals(assure
                                .getRadie())) ? "<img src='" + contextPath + "/img/USER_RADIE.gif' border='0'/>"
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getOutilGestion() != null) ? assure
                                .getOutilGestion() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((assure.getLibelle_entite_gestion() != null) ? assure
                                .getLibelle_entite_gestion() : "&nbsp;") + "</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");

            if (assures_recherches.size() == 1 && assure != null
                    && assure.getClickable() != null
                    && "C".equals(assure.getClickable())) {
                sb.append("<script>");
                sb.append("ficheAppelSetAssure('" + ben_id + "', " + isRechercheAux + ")");
                sb.append("</script>");
            }

        }
        return sb;
    }

    public static StringBuilder getTableauEntreprisesRecherchees(Collection entreprises_recherchees) {
    	
        StringBuilder sb = new StringBuilder("");

        if (entreprises_recherchees != null
                && !entreprises_recherchees.isEmpty()) {
            sb.append("<table class='m_table' cellspacing='0' width='100%'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"MUTUELLE\")' nowrap='nowrap' align='center'>Mutuelle <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"NOM\")' nowrap='nowrap' align='center'>Nom <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"NUMEROSIRET\")' nowrap='nowrap' align='center'>Num&eacute;ro siret <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"CODEETABLISSEMENT\")' nowrap='nowrap' align='center'>Code &eacute;tablissement <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"CODEENTREPRISE\")' nowrap='nowrap' align='center'>Code entreprise <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"ENTITEGESTION\")' nowrap='nowrap' align='center'>Entit&eacute; de gestion <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"CODEPOSTAL\")' nowrap='nowrap' align='center'>Code postal <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"LOCALITE\")' nowrap='nowrap' align='center'>Localit&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"RADIE\")' nowrap='nowrap' align='center'>Radi&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEntreprisesPar(\"OUTILGESTION\")' nowrap='nowrap' align='center'>Outil de gestion <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("</tr>");

            String etab_id = "", couleur_ligne = "m_tr_noir", couleur_ligne_selection = "", fonction_javascript = "";
            ObjetRecherche entreprise = null;
            for (int i = 0; i < entreprises_recherchees.size(); i++) {
                entreprise = (ObjetRecherche) entreprises_recherchees.toArray()[i];
                etab_id = entreprise.getId();

                couleur_ligne = ((entreprise.getRadie() != null && "0"
                        .equals(entreprise.getRadie()))) ? "m_tr_gris"
                        : "m_tr_noir";
                if (entreprise.getClickable() != null
                        && "C".equals(entreprise.getClickable())) {
                    fonction_javascript = "Javascript:ficheAppelSetEntreprise('"
                            + etab_id + "')";
                    couleur_ligne_selection = "m_tr_selected";
                } else {
                    fonction_javascript = "Javascript:alert('Cette entreprise ne peut être sélectionnée car elle n\\\'appartient pas à la mutuelle en cours.\\n\\nSi vous souhaitez la sélectionner, vous devez changer la mutuelle en cours.')";
                    couleur_ligne_selection = "m_tr_selected_sans_main";

                }

                sb.append("<tr class=\"" + couleur_ligne + "\" onclick=\""
                        + fonction_javascript
                        + "\" onmouseover=\"this.className='"
                        + couleur_ligne_selection
                        + "'\" onmouseout=\"this.className='" + couleur_ligne
                        + "'\">");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getMutuelle() != null) ? entreprise
                                .getMutuelle() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getNom() != null) ? entreprise.getNom()
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getNumeroSiret() != null) ? entreprise
                                .getNumeroSiret() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getCodeEtablissement() != null) ? entreprise
                                .getCodeEtablissement() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getCodeEntreprise() != null) ? entreprise
                                .getCodeEntreprise() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getEntiteGestion() != null) ? entreprise
                                .getEntiteGestion() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getCodePostal() != null) ? entreprise
                                .getCodePostal() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getLocalite() != null) ? entreprise
                                .getLocalite() + "&nbsp;" : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap' align='center'>"
                        + ((entreprise.getRadie() != null && "0"
                                .equals(entreprise.getRadie())) ? "<img src='" + contextPath + "/img/USER_RADIE.gif' border='0'/>"
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((entreprise.getOutilGestion() != null) ? entreprise
                                .getOutilGestion() : "&nbsp;") + "</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");

            if (entreprises_recherchees.size() == 1 && entreprise != null
                    && entreprise.getClickable() != null
                    && "C".equals(entreprise.getClickable())) {
                sb.append("<script>");
                sb.append("ficheAppelSetEntreprise('" + etab_id + "')");
                sb.append("</script>");
            }

        }
        return sb;
    }

    public static StringBuilder getTableauAppelantsRecherches(Collection appelants_recherches) {
    	
        StringBuilder sb = new StringBuilder("");

        if (appelants_recherches != null && !appelants_recherches.isEmpty()) {
            sb.append("<table class='m_table' cellspacing='0' width='100%'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"TYPEAPPELANT\")' nowrap='nowrap' align='center'>Type d'appelant <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"CIVILITE\")' nowrap='nowrap' align='center'>Civilit&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"NOM\")' nowrap='nowrap' align='center'>Nom <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"PRENOM\")' nowrap='nowrap' align='center'>Pr&eacute;nom <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"ETABLISSEMENTRS\")' nowrap='nowrap' align='center'>Etablissement Raison sociale <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"NUMEROFINESS\")' nowrap='nowrap' align='center'>Num&eacute;ro FINESS <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"NUMEROSECU\")' nowrap='nowrap' align='center'>Num&eacute;ro s&eacute;curit&eacute; sociale <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierAppelantsPar(\"CODEADHERENTNUMEROCONTRAT\")' nowrap='nowrap' align='center'>Code Adh&eacute;rent <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");

            sb.append("</tr>");

            String appelant_id = "", couleur_ligne = "m_tr_noir", fonction_javascript = "";
            ObjetRecherche appelant = null;
            for (int i = 0; i < appelants_recherches.size(); i++) {
                appelant = (ObjetRecherche) appelants_recherches.toArray()[i];
                appelant_id = appelant.getId();

                fonction_javascript = "Javascript:ficheAppelSetAppelant('"
                        + appelant_id + "')";

                sb.append("<tr class=\""
                        + couleur_ligne
                        + "\" onclick=\""
                        + fonction_javascript
                        + "\" onmouseover=\"this.className='m_tr_selected'\" onmouseout=\"this.className='"
                        + couleur_ligne + "'\">");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getTypeAppelant() != null) ? appelant
                                .getTypeAppelant() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getCivilite() != null) ? appelant
                                .getCivilite() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getNom() != null) ? appelant.getNom()
                                : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getPrenom() != null) ? appelant
                                .getPrenom() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getEtablissementRS() != null) ? appelant
                                .getEtablissementRS() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getNumeroFiness() != null) ? appelant
                                .getNumeroFiness() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getNumeroSecu() != null) ? appelant
                                .getNumeroSecu() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getCodeAdherentNumeroContrat() != null) ? appelant
                                .getCodeAdherentNumeroContrat() : "&nbsp;")
                        + "</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");

            if (appelants_recherches.size() == 1 && appelant != null
                    && appelant.getClickable() != null) {
                sb.append("<script>");
                sb.append("ficheAppelSetAppelant('" + appelant_id + "')");
                sb.append("</script>");
            }

        }
        return sb;
    }

    public static StringBuilder getTableauEtablissementsHospitaliersRecherches(Collection appelants_recherches) {
    	
        StringBuilder sb = new StringBuilder("");

        if (appelants_recherches != null && !appelants_recherches.isEmpty()) {
            sb.append("<table class='m_table' cellspacing='0' width='100%'>");
            sb.append("<tr>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEtablissementsHospitaliersPar(\"ETABLISSEMENTRS\")' nowrap='nowrap' align='center'>Etablissement Raison sociale  <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEtablissementsHospitaliersPar(\"NUMEROFINESS\")' nowrap='nowrap' align='center'>Num&eacute;ro FINESS <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEtablissementsHospitaliersPar(\"CODEPOSTAL\")' nowrap='nowrap' align='center'>Code postal <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");
            sb.append("<td class='m_td_entete' onClick='Javascript:trierEtablissementsHospitaliersPar(\"LOCALITE\")' nowrap='nowrap' align='center'>Localit&eacute; <img src='" + contextPath + "/img/SORT_WHITE.gif' border='0'/></td>");

            sb.append("</tr>");

            String appelant_id = "", couleur_ligne = "m_tr_noir", fonction_javascript = "";
            ObjetRecherche appelant = null;
            for (int i = 0; i < appelants_recherches.size(); i++) {
                appelant = (ObjetRecherche) appelants_recherches.toArray()[i];
                appelant_id = appelant.getId();

                fonction_javascript = "Javascript:pec_hcontacts_set_etablissement_hospitalier('"
                        + appelant_id + "')";

                sb.append("<tr class=\""
                        + couleur_ligne
                        + "\" onclick=\""
                        + fonction_javascript
                        + "\" onmouseover=\"this.className='m_tr_selected'\" onmouseout=\"this.className='"
                        + couleur_ligne + "'\">");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getEtablissementRS() != null) ? appelant
                                .getEtablissementRS() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getNumeroFiness() != null) ? appelant
                                .getNumeroFiness() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getCodePostal() != null) ? appelant
                                .getCodePostal() : "&nbsp;") + "</td>");
                sb.append("<td class='m_td' nowrap='nowrap'>"
                        + ((appelant.getLocalite() != null) ? appelant
                                .getLocalite() : "&nbsp;") + "</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");

            if (appelants_recherches.size() == 1 && appelant != null
                    && appelant.getClickable() != null) {
                sb.append("<script>");
                sb.append("pec_hcontacts_set_etablissement_hospitalier('"
                        + appelant_id + "')");
                sb.append("</script>");
            }

        }
        return sb;
    }

	public static File getFicheAppelPDF(String id_fiche) {

		File fichier = null;

		try {
			Appel appel = SQLDataService.getAppelById(id_fiche);
			// Infos fiche
			String statut = "", alias_statut = "", libelle_sous_statut = "", date_creation = "", date_modification = "", date_cloture = "", auteur = "", modificateur = "", clotureur = "",
					transferts = "";

			String type_appelant = "", alias_type_appelant = "";
			// Infos assures
			String prenom_nom_assure = "", numero_adherent_assure = "", qualite_assure = "", adresse_assure = "", telephones_assure = "", insee_assure = "";
			// Infos entreprise
			String libelle_entreprise = "", entite_gestion_entreprise = "", numero_siret_entreprise = "", correspondant_entreprise = "", adresse_entreprise = "", telephones_entreprise = "";
			// Infos appelant
			String prenom_nom_appelant = "", etablissement_rs_appelant = "", code_adherent_appelant = "", numero_finess_appelant = "", adresse_appelant = "", telephones_appelant = "";
			// Infos scénario
			String campagne = "", mutuelle = "", motif = "", sous_motif = "", point = "", sous_point = "";
			// Infos clôture
			String commentaires = "", satisfaction = "", alias_satisfaction = "";

			if (appel != null) {
				statut = appel.getStatut();
				alias_statut = appel.getAliasStatut();
				campagne = appel.getCampagne();
				mutuelle = appel.getMutuelle();
				libelle_sous_statut = appel.getLibelleSousStatut();
				date_creation = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL());
				date_modification = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEMODIFICATION());
				date_cloture = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATECLOTURE());

				if (!"".equals(appel.getCREATEUR_ID())) {
					TeleActeur teleacteur_auteur = SQLDataService.getTeleActeurById(appel.getCREATEUR_ID());
					if (teleacteur_auteur != null) {
						auteur = " par " + teleacteur_auteur.getPrenomNom();
					}
				}

				if (!"".equals(appel.getMODIFICATEUR_ID())) {
					TeleActeur teleacteur_modificateur = SQLDataService.getTeleActeurById(appel.getMODIFICATEUR_ID());
					if (teleacteur_modificateur != null) {
						modificateur = " par " + teleacteur_modificateur.getPrenomNom();
					}
				}

				if (!"".equals(appel.getCLOTUREUR_ID())) {
					TeleActeur teleacteur_clotureur = SQLDataService.getTeleActeurById(appel.getCLOTUREUR_ID());
					if (teleacteur_clotureur != null) {
						clotureur = " par " + teleacteur_clotureur.getPrenomNom();
					}
				}

				if (!alias_statut.equalsIgnoreCase(IContacts._HORSCIBLE)) {
					type_appelant = appel.getTypeAppelant();
					alias_type_appelant = appel.getAliasTypAppelant();

					// Infos assuré
					if (StringUtils.isNotBlank(appel.getBENEFICIAIRE_ID())) {
						Beneficiaire assure = SQLDataService.getBeneficiaireById(appel.getBENEFICIAIRE_ID());
						if (assure != null) {
							numero_adherent_assure = assure.getCODE();
							qualite_assure = assure.getQualite();
							insee_assure = assure.getNUMEROSS() + " " + assure.getCLESS();
							Personne personne_assure = SQLDataService.getPersonneById(assure.getPERSONNE_ID());
							if (personne_assure != null) {
								prenom_nom_assure = personne_assure.getPrenomNom();
								Adresse adresse = SQLDataService.getAdresseById(personne_assure.getADRESSE_ID());
								if (adresse != null) {
									if (!"".equals(adresse.getLIGNE_1())) {
										adresse_assure = adresse.getLIGNE_1() + "  ";
									}
									if (!"".equals(adresse.getLIGNE_2())) {
										adresse_assure = adresse_assure + adresse.getLIGNE_2() + "  ";
									}
									if (!"".equals(adresse.getLIGNE_3())) {
										adresse_assure = adresse_assure + adresse.getLIGNE_3() + "  ";
									}
									if (!"".equals(adresse.getLIGNE_4())) {
										adresse_assure = adresse_assure + adresse.getLIGNE_4() + "  ";
									}
									if (!"".equals(adresse.getCODEPOSTAL())) {
										adresse_assure = adresse_assure + adresse.getCODEPOSTAL() + "  ";
									}
									if (!"".equals(adresse.getLOCALITE())) {
										adresse_assure = adresse_assure + adresse.getLOCALITE();
									}
									telephones_assure = adresse.getTELEPHONEFIXE() + ((!"".equals(adresse.getTELEPHONEAUTRE())) ? " " + adresse.getTELEPHONEAUTRE() : "");
								}

							}
						}
					}

					// Infos entreprise
					if ("ENTREPRISE".equalsIgnoreCase(alias_type_appelant)) {
						Etablissement etablissement = SQLDataService.getEtablissementById(appel.getETABLISSEMENT_ID());
						if (etablissement != null) {
							libelle_entreprise = etablissement.getLIBELLE();
							entite_gestion_entreprise = etablissement.getEntiteGestion();
							numero_siret_entreprise = etablissement.getSIRET();

							Adresse adresse = SQLDataService.getAdresseById(etablissement.getADRESSE_ID());
							if (adresse != null) {

								if (!"".equals(adresse.getLIGNE_1())) {
									adresse_entreprise = adresse.getLIGNE_1() + "  ";
								}
								if (!"".equals(adresse.getLIGNE_2())) {
									adresse_entreprise = adresse_entreprise + adresse.getLIGNE_2() + "  ";
								}
								if (!"".equals(adresse.getLIGNE_3())) {
									adresse_entreprise = adresse_entreprise + adresse.getLIGNE_3() + "  ";
								}
								if (!"".equals(adresse.getLIGNE_4())) {
									adresse_entreprise = adresse_entreprise + adresse.getLIGNE_4() + "  ";
								}
								if (!"".equals(adresse.getCODEPOSTAL())) {
									adresse_entreprise = adresse_entreprise + adresse.getCODEPOSTAL() + "  ";
								}
								if (!"".equals(adresse.getLOCALITE())) {
									adresse_entreprise = adresse_entreprise + adresse.getLOCALITE();
								}
								telephones_entreprise = adresse.getTELEPHONEFIXE() + ((!"".equals(adresse.getTELEPHONEAUTRE())) ? adresse.getTELEPHONEAUTRE() : "");
							}

						}
					}
					// Infos appelant
					else if (!"ASSURE".equalsIgnoreCase(alias_type_appelant)) {
						Appelant appelant = SQLDataService.getAppelantById(appel.getAPPELANT_ID());
						if (appelant != null) {
							prenom_nom_appelant = ((!"".equals(appelant.getPRENOM())) ? appelant.getPRENOM() + " " : "") + ((!"".equals(appelant.getNOM())) ? appelant.getNOM() + " " : "");
							etablissement_rs_appelant = (!"".equals(appelant.getETABLISSEMENT_RS())) ? appelant.getETABLISSEMENT_RS() + " " : "";
							telephones_appelant = appelant.getADR_TELEPHONEAUTRE() + ((!"".equals(appelant.getADR_TELEPHONEFIXE())) ? appelant.getADR_TELEPHONEFIXE() : "");
							code_adherent_appelant = appelant.getCODEADHERENT();
							numero_finess_appelant = appelant.getNUMFINESS();
						}
					}

					// Scénario
					motif = appel.getMotif();
					sous_motif = appel.getSousMotif();
					point = appel.getPoint();
					sous_point = appel.getSousPoint();

					// Clôture
					commentaires = appel.getCOMMENTAIRE();
					satisfaction = appel.getSatisfaction();
					alias_satisfaction = appel.getAliasSatisfaction();

					transferts = appel.getTRANSFERTS();
				}
			}

			fichier = File.createTempFile(id_fiche + "_", ".pdf");
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fichier));
			document.open();

			int[] tailles = { 20, 80 };
			PdfPTable table_entete, table_fiche, table_appelant, table_scenario, table_cloture;
			PdfPCell cell;
			Phrase phrase;

			// APPELANT + BENEFICIAIRE DEBUT			
			table_appelant = new PdfPTable(2);
			table_appelant.setWidths(tailles);
			table_appelant.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table_appelant.getDefaultCell().setNoWrap(true);
			cell = new PdfPCell();
			cell.setColspan(2);

			phrase = new Phrase("Appelant", separateur);
			cell.setPhrase(phrase);
			table_appelant.addCell(cell);
			cell.setColspan(0);
			
			phrase = new Phrase("Type", bleu11);
			cell.setPhrase(phrase);
			table_appelant.addCell(cell);
			phrase = new Phrase(type_appelant, noir11);
			cell.setPhrase(phrase);
			table_appelant.addCell(cell);

			if ("ENTREPRISE".equals(alias_type_appelant)) {
				
				phrase = new Phrase("Nom", bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(libelle_entreprise, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Entité de gestion".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(entite_gestion_entreprise, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Numéro de siret".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(numero_siret_entreprise, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				
			} else if (!"ASSURE".equals(alias_type_appelant)) {
				
				phrase = new Phrase(new String("Identité".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(prenom_nom_appelant, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Numéro d'adhérent".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(code_adherent_appelant, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase("Etablissement RS", bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(etablissement_rs_appelant, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Numéro FINESS".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(numero_finess_appelant, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Téléphones".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(telephones_appelant, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
			}
			
			if (!"ASSURE".equals(alias_type_appelant)) {

				document.add(table_appelant);
				document.add(new Phrase("\n"));
				
				// BENEFICIAIRE
				if (StringUtils.isNotBlank(appel.getBENEFICIAIRE_ID())) {
					
					table_appelant = new PdfPTable(2);
					table_appelant.setWidths(tailles);
					table_appelant.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table_appelant.getDefaultCell().setNoWrap(true);
					cell = new PdfPCell();
					cell.setColspan(2);

					phrase = new Phrase(new String("Bénéficiaire".getBytes("utf-8"), Charset.forName(_UTF8)), separateur);
					cell.setPhrase(phrase);
					table_appelant.addCell(cell);
					cell.setColspan(0);
				}
			}
			
			if (StringUtils.isNotBlank(appel.getBENEFICIAIRE_ID())) {

				phrase = new Phrase(new String("Identité".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(prenom_nom_assure, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Numéro d'adhérent".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(numero_adherent_assure, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				
				phrase = new Phrase(new String("INSEE".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(insee_assure, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase("Adresse", bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(adresse_assure, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);

				phrase = new Phrase(new String("Téléphones".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				phrase = new Phrase(telephones_assure, noir11);
				cell.setPhrase(phrase);
				table_appelant.addCell(cell);
				
				document.add(table_appelant);
				document.add(new Phrase("\n"));
			}
			// APPELANT + BENEFICIAIRE FIN

			
			// FICHE DEBUT
			table_fiche = new PdfPTable(2);
			table_fiche.setWidths(tailles);

			cell = new PdfPCell();
			cell.setColspan(2);
			table_fiche.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table_fiche.getDefaultCell().setNoWrap(true);

			phrase = new Phrase("Fiche", separateur);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);

			cell.setColspan(0);
			phrase = new Phrase("ID Fiche", bleu11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);
			phrase = new Phrase(appel.getID(), noir11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);

			phrase = new Phrase("Campagne", bleu11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);
			phrase = new Phrase(campagne, noir11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);

			phrase = new Phrase("Mutuelle", bleu11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);
			phrase = new Phrase(mutuelle, noir11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);

			phrase = new Phrase(new String("Créée le".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);
			phrase = new Phrase(date_creation + auteur, noir11);
			cell.setPhrase(phrase);
			table_fiche.addCell(cell);
			document.add(table_fiche);

			document.add(new Phrase("\n"));
			// FICHE FIN

			// SCENARIO DEBUT
			table_scenario = new PdfPTable(2);
			table_scenario.setWidths(tailles);
			cell = new PdfPCell();
			cell.setColspan(2);
			table_scenario.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table_scenario.getDefaultCell().setNoWrap(true);

			phrase = new Phrase(new String("Scénario".getBytes("utf-8"), Charset.forName(_UTF8)), separateur);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);
			cell.setColspan(0);

			phrase = new Phrase("Motif", bleu11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);
			phrase = new Phrase(motif, noir11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);

			phrase = new Phrase("Sous-motif", bleu11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);
			phrase = new Phrase(sous_motif, noir11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);

			phrase = new Phrase("Point", bleu11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);
			phrase = new Phrase(point, noir11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);

			phrase = new Phrase("Sous-point", bleu11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);
			phrase = new Phrase(sous_point, noir11);
			cell.setPhrase(phrase);
			table_scenario.addCell(cell);

			document.add(table_scenario);
			document.add(new Phrase("\n"));
			// SCENARIO FIN

			// CLOTURE DEBUT
			table_cloture = new PdfPTable(2);
			table_cloture.setWidths(tailles);
			cell = new PdfPCell();
			cell.setColspan(2);
			table_cloture.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table_cloture.getDefaultCell().setNoWrap(true);

			phrase = new Phrase(new String("Clôture".getBytes("utf-8"), Charset.forName(_UTF8)), separateur);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);
			cell.setColspan(0);

			phrase = new Phrase(new String("Transférée à".getBytes("utf-8"), Charset.forName(_UTF8)), bleu11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);
			phrase = new Phrase(transferts, noir11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);

			phrase = new Phrase("Satisfaction", bleu11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);
			phrase = new Phrase(satisfaction, noir11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);

			phrase = new Phrase("Commentaires", bleu11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);
			phrase = new Phrase(commentaires, noir11);
			cell.setPhrase(phrase);
			table_cloture.addCell(cell);

			document.add(table_cloture);
			// SCENARIO FIN

			document.close();
		} catch (Exception e) {
			LOGGER.error("getFicheAppelPDF", e);
			throw new IContactsException(e);
		}

		return fichier;
	}

    public static void envoyerFicheDeTransfert(String IDFiche, String destinataire_transfert, File fichier, HttpServletRequest request) {
        try {

            InternetAddress[] email_destinataire_IA = InternetAddress.parse(
                    destinataire_transfert, false);
            String SERVEUR_SMTP = (String) request.getSession().getAttribute(
                    "SERVEUR_SMTP");
            Properties props = System.getProperties();
            props.put("mail.smtp.host", SERVEUR_SMTP);
            String from = "H.Contacts<hcontacts@igestion-gd.fr>";
            String sujet = "iGestion - Fiche de Transfert H.Contacts "
                    + IDFiche;
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, email_destinataire_IA);
            msg.setSubject(MimeUtility.encodeText(sujet, "iso-8859-1", "Q"));

            // NE PAS METTRE DE #
            StringBuilder squeletteHTMLBody = new StringBuilder();
            squeletteHTMLBody.append("<html><head>");
            squeletteHTMLBody.append("<style>");
            squeletteHTMLBody
                    .append(".noir12{font-family:\"Verdana,sans-serif\";font-size:12px;color:\"333333\";}");
            squeletteHTMLBody
                    .append(".noir10{font-family:\"Verdana,sans-serif\";font-size:10px;color:\"333333\";}");
            squeletteHTMLBody.append("</style>");
            squeletteHTMLBody.append("</head>");
            squeletteHTMLBody.append("<body>");

            squeletteHTMLBody.append("<div class=\"noir12\">");
            squeletteHTMLBody
                    .append("Veuillez trouver ci-joint la fiche de transfert H.Contacts <B>"
                            + IDFiche + "</B>.");
            squeletteHTMLBody.append("</div>");

            squeletteHTMLBody
                    .append("<div class=\"noir12\" style=\"padding-top:30px\">");
            squeletteHTMLBody.append("Cordialement,");
            squeletteHTMLBody.append("</div>");

            squeletteHTMLBody.append("<div class=\"noir12\">");
            squeletteHTMLBody.append("Le Service Relation Clients iGestion.");
            squeletteHTMLBody.append("</div>");

            squeletteHTMLBody
                    .append("<div class=\"noir10\" style=\"padding-top:30px\">");
            squeletteHTMLBody
                    .append("Ce mail a &eacute;t&eacute; envoy&eacute; automatiquement, merci de ne pas r&eacute;pondre.");
            squeletteHTMLBody.append("</div>");

            squeletteHTMLBody.append("</body>");
            squeletteHTMLBody.append("</html>");

            Multipart mp = new MimeMultipart();

            MimeBodyPart html = new MimeBodyPart();
            html.setContent(new String(squeletteHTMLBody.toString().getBytes(),
                    _UTF8), "text/html");
            mp.addBodyPart(html);

            MimeBodyPart piece_jointe = new MimeBodyPart();

            DataSource source = new FileDataSource(fichier);
            piece_jointe.setDataHandler(new DataHandler(source));
            piece_jointe.setFileName(fichier.getName());
            mp.addBodyPart(piece_jointe);

            msg.setContent(mp);

            msg.setSentDate(new Date());
            Transport.send(msg);

            fichier.delete();

        } catch (Exception e) {
            LOGGER.error("envoyerFicheDeTransfert", e);
        }
    }

}
