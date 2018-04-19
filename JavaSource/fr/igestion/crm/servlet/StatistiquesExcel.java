package fr.igestion.crm.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import common.Logger;

import fr.igestion.crm.IContactsException;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilDate;
import fr.igestion.crm.bean.LigneExcel;

public class StatistiquesExcel extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StatistiquesExcel.class);
    
    private static final long serialVersionUID = 1L;

    private static String _idRapport = null;
    private static String _nomFichier = null;

    private String[] _tab_colonnes = null;
    private int _nbr_tab_colonnes = 0;
    private static int limite_lignes = 65500;

    private static String _DATEDEBUT = null;
    private static String _DATEFIN = null;

    private static String _RESOLU = null;
    
    private static String _TELEACTEUR_ID = null;

    private static String _MUTUELLE_ID = null;
    private static String _MUTUELLE_TEXT = null;

    private static String _SITE_ID = null;
    private static String _SITE_TEXT = null;

    private static String _CREATEUR_ID = null;
    private static String _CREATEUR_TEXT = null;

    private static String _REFERENCE_ID = null;
    private static String _REFERENCE_TEXT = null;

    private static String _STATUT_ID = null;
    private static String _STATUT_TEXT = null;
        

    private WritableFont _font_arial_8_bold_noir = null;
    private WritableCellFormat _wcf_font_arial_8_bold_noir = null;

    private WritableFont _font_arial_8_noir = null;
    private WritableCellFormat _wcf_font_arial_8_noir = null;

    private WritableFont _font_arial_8_noir_bold_fond_bleu = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_bleu = null;

    private WritableFont _font_arial_8_noir_fond_bleu_left = null;
    private WritableCellFormat _wcf_font_arial_8_noir_fond_bleu_left = null;

    private WritableFont _font_arial_8_noir_fond_bleu_right = null;
    private WritableCellFormat _wcf_font_arial_8_noir_fond_bleu_right = null;

    private WritableFont _font_arial_8_noir_bold_fond_violet = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_violet = null;

    private WritableFont _font_bordure = null;
    private WritableCellFormat _wcf_font_bordure = null;
    private WritableCellFormat _wcf_font_sans_bordure = null;

    private WritableFont _wf_datas = null;
    private WritableCellFormat _wcf_datas = null;
    private WritableCellFormat _wcf_datas_left = null;

    private WritableFont _wf_datas_bold = null;
    private WritableCellFormat _wcf_datas_bold = null;

    private WritableFont _wf_datas_pct = null;
    private WritableCellFormat _wcf_datas_pct = null;

    private WritableFont _wf_datas_flotant = null;
    private WritableCellFormat _wcf_datas_flotant = null;

    private WritableFont _wf_datas_flotant_bold = null;
    private WritableCellFormat _wcf_datas_flotant_bold = null;

    private WritableFont _font_arial_8_noir_bold_fond_gris = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_gris = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        _idRapport = (String) request.getParameter("idRapport");
        String sr = "";

        _MUTUELLE_ID = (String) request.getParameter("mutuelle_id");
        _MUTUELLE_TEXT = (String) request.getParameter("mutuelle_text");

        _TELEACTEUR_ID = (String) request.getParameter("teleacteur_id");

        _SITE_ID = (String) request.getParameter("site_id");
        _SITE_TEXT = (String) request.getParameter("site_text");

        _REFERENCE_ID = (String) request.getParameter("reference_id");
        _REFERENCE_TEXT = (String) request.getParameter("reference_text");

        _CREATEUR_ID = (String) request.getParameter("createur_id");
        _CREATEUR_TEXT = (String) request.getParameter("createur_text");

        _STATUT_ID = (String) request.getParameter("statut_id");
        _STATUT_TEXT = (String) request.getParameter("statut_text");

        _DATEDEBUT = (String) request.getParameter("date_debut");
        _DATEFIN = (String) request.getParameter("date_fin");

        _RESOLU = (String) request.getParameter("resolu");
        
        Workbook modele_wb = null;
        WritableWorkbook wb = null;

        _nomFichier = "Statistiques_HContacts"
                + "_"
                + _DATEDEBUT.replaceAll("/", "")
                + ((!"".equals(_DATEFIN)) ? "_" + _DATEFIN.replaceAll("/", "")
                        : "");
        sr = "Résumé de la demande";
        request.getSession().setAttribute(_idRapport, sr);

        try {

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + _nomFichier + ".xls");

            initVariablesStatic();

            wb = Workbook.createWorkbook(response.getOutputStream());

            this.Traitement(wb, request);

            wb.write();
            wb.close();

            request.getSession().setAttribute(_idRapport, "FINI");

        } catch (Exception e) {

            LOGGER.warn("doGet",e);
            if (e.getClass().getName().indexOf("ClientAbortException") != -1) {
                request.getSession().setAttribute(_idRapport,
                        "ANNULATION_CLIENT");
            } else {
                request.getSession().setAttribute(_idRapport, "ERREUR");
            }
            throw new ServletException("", e);
        } finally {

            if (modele_wb != null) {
                modele_wb.close();
            }
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                LOGGER.warn("doGet",e);
            }

        }

    }

    public void Traitement(WritableWorkbook wb, HttpServletRequest request)
            throws Exception {

        Map<String, String> criteres = new HashMap<String, String>();

        criteres.put("TELEACTEUR_ID", _TELEACTEUR_ID);
        criteres.put("MUTUELLE_ID", _MUTUELLE_ID);
        criteres.put("SITE_ID", _SITE_ID);
        criteres.put("CREATEUR_ID", _CREATEUR_ID);
        criteres.put("REFERENCE_ID", _REFERENCE_ID);
        criteres.put("STATUT_ID", _STATUT_ID);
        criteres.put("DATEDEBUT", _DATEDEBUT);
        criteres.put("DATEFIN", _DATEFIN);
        criteres.put("RESOLU", _RESOLU);

        request.getSession()
                .setAttribute(_idRapport, "Exécution de la requête");

        Collection<LigneExcel> donnees = SQLDataService
                .getStatistiquesFichesAppels(criteres);

        request.getSession().setAttribute(_idRapport, "Ecriture des données");

        int nbr_onglets = 0;
        if (donnees != null && !donnees.isEmpty()) {
            nbr_onglets = (int) Math.ceil((float) donnees.size()
                    / (float) limite_lignes);
        }

        for (int i = 0; i < nbr_onglets; i++) {
            
            WritableSheet sheet = wb.createSheet("Feuil" + i, i);
            // Résumé de la feuille
            resumeFeuille(sheet);

            ECRIRE_COLONNES(sheet);
            ECRIRE_DONNEES(sheet, donnees, (int) i * limite_lignes,
                    (int) (i + 1) * limite_lignes);

            // Taille des colonnes
            sheet.setColumnView(0, 12);
            sheet.setColumnView(1, 18);
            sheet.setColumnView(2, 25);
            sheet.setColumnView(3, 16);
            sheet.setColumnView(4, 35);
            sheet.setColumnView(5, 16);

            sheet.setColumnView(6, 16);
            sheet.setColumnView(7, 16);

            sheet.setColumnView(8, 25);
            sheet.setColumnView(9, 25);

            sheet.setColumnView(10, 12);
            sheet.setColumnView(11, 12);

            sheet.setColumnView(12, 7);
            sheet.setColumnView(13, 40);
            sheet.setColumnView(14, 40);
            sheet.setColumnView(15, 40);
            sheet.setColumnView(16, 40);
        }

        if (nbr_onglets == 0) {
            WritableSheet sheet = wb.createSheet("Feuil" + 0, 0);
            // Résumé de la feuille
            resumeFeuille(sheet);
            ECRIRE_COLONNES(sheet);
        }

    }

    public void resumeFeuille(WritableSheet sheet) throws Exception {
        
        sheet.addCell(new Label(0, 0, "Date début", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 0, _DATEDEBUT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 1, "Date fin", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 1, _DATEFIN, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 2, "Site", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 2, _SITE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 3, "Client", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 3, _MUTUELLE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 4, "Auteur", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 4, _CREATEUR_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 5, "Type de fiche",
                _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 5, _REFERENCE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 6, "Résolu", _wcf_font_arial_8_bold_noir));
        if( "1".equalsIgnoreCase(_RESOLU)){
            sheet.addCell(new Label(1, 6, "Oui", _wcf_font_arial_8_noir));
        }
        else if("0".equalsIgnoreCase(_RESOLU) ){
            sheet.addCell(new Label(1, 6, "Non", _wcf_font_arial_8_noir));
        }
        else{
            sheet.addCell(new Label(1, 6, "- Tous -", _wcf_font_arial_8_noir));
        }
        
        sheet.addCell(new Label(0, 7, "Statut", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 7, _STATUT_TEXT, _wcf_font_arial_8_noir));

    }

    public void ECRIRE_DONNEES(WritableSheet sheet,
            Collection<LigneExcel> donnees, int borne_inf, int borne_sup) {

        Label l = null;
        int ligne_i = 0;

        Object[] tab_donnees = null;

        try{
            if (donnees != null && !donnees.isEmpty()) {
                LigneExcel ligne_donnees = null;
                tab_donnees = donnees.toArray();
    
                for (int i = borne_inf; i < borne_sup; i++) {
    
                    if (i < donnees.size()) {
                        ligne_donnees = (LigneExcel) tab_donnees[i];
    
                        // SITE
                        l = new Label(0, 10 + ligne_i,
                                (String) ligne_donnees.getSITE(), _wcf_datas_left);
                        sheet.addCell(l);
    
                        // CLIENT
                        l = new Label(1, 10 + ligne_i,
                                (String) ligne_donnees.getCLIENT(), _wcf_datas_left);
                        sheet.addCell(l);
    
                        // TYPE DE FICHE
                        l = new Label(2, 10 + ligne_i,
                                (String) ligne_donnees.getTYPEFICHE(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // TYPE APPELANT
                        l = new Label(3, 10 + ligne_i,
                                (String) ligne_donnees.getTYPEAPPELANT(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // NOM APPELANT
                        l = new Label(4, 10 + ligne_i,
                                (String) ligne_donnees.getNOMAPPELANT(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // CODE APPELANT
                        l = new Label(5, 10 + ligne_i,
                                (String) ligne_donnees.getCODE_APPELANT(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // DATE APPEL
                        l = new Label(6, 10 + ligne_i,
                                (String) UtilDate.fmtDDMMYYYYHHMMSS(ligne_donnees
                                        .getDATE_APPEL()), _wcf_datas_left);
                        sheet.addCell(l);
    
                        // DATE CLOTURE
                        l = new Label(7, 10 + ligne_i,
                                (String) UtilDate.fmtDDMMYYYYHHMMSS(ligne_donnees
                                        .getDATE_CLOTURE()), _wcf_datas_left);
                        sheet.addCell(l);
    
                        // CREATEUR
                        l = new Label(8, 10 + ligne_i,
                                (String) ligne_donnees.getCREATEUR(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // CLOTUREUR
                        l = new Label(9, 10 + ligne_i,
                                (String) ligne_donnees.getCLOTUREUR(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // STATUT
                        l = new Label(10, 10 + ligne_i,
                                (String) ligne_donnees.getSTATUT(), _wcf_datas_left);
                        sheet.addCell(l);
    
                        // IDFICHE
                        l = new Label(11, 10 + ligne_i,
                                (String) ligne_donnees.getIDFICHE(),
                                _wcf_datas_left);
                        sheet.addCell(l);
    
                        // RESOLU
                        l = new Label(12, 10 + ligne_i,
                                (String) ligne_donnees.getRESOLU(),
                                _wcf_datas_left);
                        sheet.addCell(l);
                        
                        // COMMENTAIRE
                        l = new Label(13, 10 + ligne_i,
                                (String) ligne_donnees.getCOMMENTAIRE(),
                                _wcf_datas_left);
                        sheet.addCell(l); 
                     // MOTIF
                        l = new Label(14, 10 + ligne_i,
                                (String) ligne_donnees.getMotif(),
                                _wcf_datas_left);
                        sheet.addCell(l); 
                     // SOUS MOTIF
                        l = new Label(15, 10 + ligne_i,
                                (String) ligne_donnees.getSous_motif(),
                                _wcf_datas_left);
                        sheet.addCell(l);
                     // ENTITE GESTION
                        l = new Label(16, 10 + ligne_i,
                                (String) ligne_donnees.getLib_entite_gestion(),
                                _wcf_datas_left);
                        sheet.addCell(l);   
                    }
    
                    ligne_i++;
                }
            }
        } catch(Exception e){
            LOGGER.warn("ECRIRE_DONNEES",e);
            throw new IContactsException(e);
        }
    }

    public void ECRIRE_COLONNES(WritableSheet sheet) {
        try {
            for (int i = 0; i < _nbr_tab_colonnes; i++) {
                sheet.addCell(new Label(i, 9, _tab_colonnes[i],
                        _wcf_font_arial_8_noir_bold_fond_gris));
            }
        } catch (Exception e) {
            LOGGER.warn("ECRIRE_COLONNES",e);
        }
    }

    public void initVariablesStatic() {

        try {
            _font_arial_8_bold_noir = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _font_arial_8_bold_noir.setColour(Colour.BLACK);
            _wcf_font_arial_8_bold_noir = new WritableCellFormat(
                    _font_arial_8_bold_noir);

            _font_arial_8_noir = new WritableFont(WritableFont.ARIAL, 8);
            _font_arial_8_noir.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir = new WritableCellFormat(_font_arial_8_noir);

            _font_arial_8_noir_bold_fond_bleu = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _font_arial_8_noir_bold_fond_bleu.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_bold_fond_bleu = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_bleu);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_bleu.setWrap(true);
            _wcf_font_arial_8_noir_bold_fond_bleu.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_fond_bleu_left = new WritableFont(
                    WritableFont.ARIAL, 8);
            _font_arial_8_noir_fond_bleu_left.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_fond_bleu_left = new WritableCellFormat(
                    _font_arial_8_noir_fond_bleu_left);
            _wcf_font_arial_8_noir_fond_bleu_left
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_fond_bleu_left.setAlignment(Alignment.LEFT);
            _wcf_font_arial_8_noir_fond_bleu_left
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_fond_bleu_left.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_fond_bleu_right = new WritableFont(
                    WritableFont.ARIAL, 8);
            _font_arial_8_noir_fond_bleu_right.setItalic(true);
            _font_arial_8_noir_fond_bleu_right.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_fond_bleu_right = new WritableCellFormat(
                    _font_arial_8_noir_fond_bleu_right);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setAlignment(Alignment.RIGHT);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_fond_bleu_right.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_bold_fond_violet = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _font_arial_8_noir_bold_fond_violet.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_bold_fond_violet = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_violet);
            _wcf_font_arial_8_noir_bold_fond_violet.setBackground(Colour
                    .getInternalColour(31));
            _wcf_font_arial_8_noir_bold_fond_violet
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_violet
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_violet.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_bordure = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _font_bordure.setColour(Colour.BLUE);
            _wcf_font_bordure = new WritableCellFormat(_font_bordure);
            _wcf_font_bordure.setAlignment(Alignment.CENTRE);
            _wcf_font_bordure.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_bordure.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas.setColour(Colour.BLACK);

            // Centrée
            _wcf_datas = new WritableCellFormat(_wf_datas,
                    NumberFormats.DEFAULT);
            _wcf_datas.setAlignment(Alignment.CENTRE);
            _wcf_datas.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas
                    .setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            // A gauche
            _wcf_datas_left = new WritableCellFormat(_wf_datas,
                    NumberFormats.DEFAULT);
            _wcf_datas_left.setAlignment(Alignment.LEFT);
            _wcf_datas_left.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_left.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wcf_font_sans_bordure = new WritableCellFormat(_wf_datas);
            _wcf_font_sans_bordure.setAlignment(Alignment.CENTRE);
            _wcf_font_sans_bordure
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_sans_bordure.setBorder(Border.NONE, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_bold = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _wf_datas_bold.setColour(Colour.BLACK);
            _wcf_datas_bold = new WritableCellFormat(_wf_datas_bold,
                    NumberFormats.DEFAULT);
            _wcf_datas_bold.setAlignment(Alignment.CENTRE);
            _wcf_datas_bold.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_bold.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_pct = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas_pct.setColour(Colour.BLUE);
            _wcf_datas_pct = new WritableCellFormat(_wf_datas_pct);
            _wcf_datas_pct.setAlignment(Alignment.CENTRE);
            _wcf_datas_pct.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_pct.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_flotant = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas_flotant.setColour(Colour.BLACK);
            _wcf_datas_flotant = new WritableCellFormat(_wf_datas_flotant,
                    NumberFormats.FLOAT);
            _wcf_datas_flotant.setAlignment(Alignment.CENTRE);
            _wcf_datas_flotant.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_flotant.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_flotant_bold = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _wf_datas_flotant_bold.setColour(Colour.BLACK);
            _wcf_datas_flotant_bold = new WritableCellFormat(
                    _wf_datas_flotant_bold, NumberFormats.FLOAT);
            _wcf_datas_flotant_bold.setAlignment(Alignment.CENTRE);
            _wcf_datas_flotant_bold
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_flotant_bold.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _font_arial_8_noir_bold_fond_gris = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _wcf_font_arial_8_noir_bold_fond_gris = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_gris);
            _wcf_font_arial_8_noir_bold_fond_gris.setBackground(Colour.GRAY_25);
            _wcf_font_arial_8_noir_bold_fond_gris
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_gris
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_gris.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _tab_colonnes = new String[] { "SITE", "CLIENT", "TYPE DE FICHE",
                    "TYPE D'APPELANT", "NOM APPELANT", "CODE APPELANT",
                    "DATE APPEL", "DATE CLOTURE", "CREATEUR", "CLOTUREUR",
                    "STATUT", "ID FICHE", "RESOLU", "COMMENTAIRE","MOTIF","SOUS MOTIF","ENTITE GESTION"};
            _nbr_tab_colonnes = _tab_colonnes.length;

        } catch (Exception e) {
            LOGGER.warn("initVariablesStatic",e);
        }

    }

}
