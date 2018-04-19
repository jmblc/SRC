package fr.igestion.crm.edition;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fr.igestion.crm.IContactsException;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.pec.DemandePec;

public class EditionDemandePEC {
    
    private static final String _UTF8="UTF-8";
    private static final String _utf8="utf-8";
    
    private static final String _Vrai = "1";
    
    private static Font goodies = new Font(FontFamily.ZAPFDINGBATS, 10,
            Font.BOLD, BaseColor.BLACK);
    
    private static Font titre = new Font(FontFamily.HELVETICA, 16,
            Font.BOLD, BaseColor.BLACK);
    
    private static Font titreBlock = new Font(FontFamily.HELVETICA, 14,
            Font.BOLD, BaseColor.BLACK);
    
    private static Font noir10 = new Font(FontFamily.HELVETICA, 10,
            Font.NORMAL, BaseColor.BLACK);
    
    private static Font noir5 = new Font(FontFamily.HELVETICA, 2,
            Font.NORMAL, BaseColor.BLACK);
    
    private static Font noir11 = new Font(FontFamily.HELVETICA, 12,
            Font.NORMAL, BaseColor.BLACK);
    private static Font bleu11 = new Font(FontFamily.HELVETICA, 12,
            Font.NORMAL, new BaseColor(49, 106, 197));
    
    private static final float _padding = 5;
    
    private static Phrase addPhraseUTF8(String str, Font font) {
        try{
            return new Phrase(new String(str.getBytes(_utf8),
                    Charset.forName(_UTF8)), font );
        }
        catch(Exception e){
            return new Phrase(str,font);
        }
    } 
    
    private static PdfPCell addTitreBlock(String str ){
        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        cell.setLeading(10f, 0f);
        Phrase phrase = addPhraseUTF8(str, titreBlock);
        cell.setPhrase(phrase);
        return cell;
    }
    
    private static PdfPCell addCellPetitNoir(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Phrase phrase = addPhraseUTF8(str, noir10);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalNoir(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, noir11);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalBleue(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, bleu11);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalNoirGauche(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, noir11);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalNoirGaucheBas(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, noir11);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalBleueDroite(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, bleu11);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCellNormalBleueDroiteBas(String str){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setLeading(15f, 0f);
        Phrase phrase = addPhraseUTF8(str, bleu11);
        cell.setPhrase(phrase);
        return cell;
    }
    
    private static PdfPCell addCheckChecked(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER );
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(110)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCheckCheckedDroite(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT );
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(110)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCheckCheckedDroiteBas(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(110)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    
    private static PdfPCell addCheckUnChecked(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER );
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(111)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCheckUnCheckedDroite(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT );
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(111)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    private static PdfPCell addCheckUnCheckedDroiteBas(){
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setLeading(15f, 0f);
        Phrase phrase = new Phrase( String.valueOf(Character.toChars(111)), goodies);
        cell.setPhrase(phrase);
        return cell;
    }
    
    private static PdfPTable blockEntete( DemandePec laDemandePEC ) throws Exception{
        
        PdfPCell cell;
        Phrase phrase;
        
        PdfPTable tb_entete = new PdfPTable(2);
        tb_entete.setWidths(new int[]{ 1, 10 });
        tb_entete.setWidthPercentage(100);
        tb_entete.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_entete.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_entete.getDefaultCell().setPadding(_padding);
        
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        phrase = new Phrase("DEMANDE DE PRISE ENCHARGE HOSPITALIERE", titre);
        cell.setPhrase(phrase);
        tb_entete.addCell(cell);
                
        tb_entete.addCell( addCellNormalNoir("Date") );
        tb_entete.addCell( addCellNormalBleue( laDemandePEC.getDateCreation() ) );
        
        tb_entete.addCell( addCellNormalNoir("OMC") );
        tb_entete.addCell( addCellNormalBleue( laDemandePEC.getOrganisme() ) );
        
        return tb_entete;
    }
    
    private static PdfPTable blockBeneficiaire( DemandePec laDemandePEC ) throws Exception{
        
        PdfPTable tb_beneficiaire = new PdfPTable(2);
        tb_beneficiaire.setWidths(new int[]{ 2, 4 });
        tb_beneficiaire.setWidthPercentage(100);
        tb_beneficiaire.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_beneficiaire.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_beneficiaire.getDefaultCell().setPadding(_padding);
                
        tb_beneficiaire.addCell( addTitreBlock("BENEFICIARE"));
        
        tb_beneficiaire.addCell( addCellNormalNoirGauche("Nom Prénom") );
        tb_beneficiaire.addCell( addCellNormalBleueDroite( laDemandePEC.getNom_prenom_beneficiaire() ) );
        
        tb_beneficiaire.addCell( addCellNormalNoirGauche("Numéro de sécurité sociale") );
        tb_beneficiaire.addCell( addCellNormalBleueDroite( laDemandePEC.getNumSS_beneficiaire() ) );
        
        tb_beneficiaire.addCell( addCellNormalNoirGaucheBas("Date de naissance") );
        tb_beneficiaire.addCell( addCellNormalBleueDroiteBas( laDemandePEC.getStrDnai_beneficiaire() ) );
        
        return tb_beneficiaire;
    }
    
    private static PdfPTable blockEtablissement( DemandePec laDemandePEC ) throws Exception{
        
        PdfPTable tb_etablissement = new PdfPTable(2);
        tb_etablissement.setWidths(new int[]{ 2, 4 });
        tb_etablissement.setWidthPercentage(100);
        tb_etablissement.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_etablissement.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_etablissement.getDefaultCell().setPadding(_padding);
        
        tb_etablissement.addCell( addTitreBlock("ETABLISSEMENT HOSPITALIER"));
        
        tb_etablissement.addCell( addCellNormalNoirGauche("Raison sociale") );
        tb_etablissement.addCell( addCellNormalBleueDroite( laDemandePEC.getEtablissementRS_appelant() ) );
        
        tb_etablissement.addCell( addCellNormalNoirGauche("Numéro FINESS") );
        tb_etablissement.addCell( addCellNormalBleueDroite( laDemandePEC.getNumFiness_appelant() ) );
        
        tb_etablissement.addCell( addCellNormalNoirGaucheBas("Fax de l'établissement") );
        tb_etablissement.addCell( addCellNormalBleueDroiteBas( laDemandePEC.getFax_appelant() ) );
        
        return tb_etablissement;
    }
    
    private static PdfPTable blockHospitalisation( DemandePec laDemandePEC ) throws Exception{
        
        PdfPTable tb_hospitalisation = new PdfPTable(2);
        tb_hospitalisation.setWidths(new int[]{ 1, 4 });
        tb_hospitalisation.setWidthPercentage(100);
        tb_hospitalisation.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_hospitalisation.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_hospitalisation.getDefaultCell().setPadding(_padding);
        
        tb_hospitalisation.addCell( addTitreBlock("HOSPITALISATION"));
        
        tb_hospitalisation.addCell( addCellNormalNoirGauche("Date d'entrée") );
        tb_hospitalisation.addCell( addCellNormalBleueDroite( laDemandePEC.getStrDateEntree() ) );
        
        tb_hospitalisation.addCell( addCellNormalNoirGauche("Numéro d'entrée") );
        tb_hospitalisation.addCell( addCellNormalBleueDroite( laDemandePEC.getNumEntree() ) );
        
        tb_hospitalisation.addCell( addCellNormalNoirGauche("Discipline") );
        PdfPTable tb_discipline = new PdfPTable(10);
        tb_discipline.setWidths(new int[]{ 3,1,3,1,3,1,3,1,6,1});
        tb_discipline.setWidthPercentage(100);
        tb_discipline.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tb_discipline.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
        tb_discipline.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_discipline.getDefaultCell().setPadding(0);
        tb_discipline.addCell(addCellPetitNoir("Médecine"));
        if( laDemandePEC._medecine.equals(laDemandePEC.getTypeHospitalisation()) ){
            tb_discipline.addCell(addCheckChecked());
        }else{
            tb_discipline.addCell(addCheckUnChecked());
        }
        tb_discipline.addCell(addCellPetitNoir("Chirurgie"));
        if( laDemandePEC._chirurgie.equals(laDemandePEC.getTypeHospitalisation()) ){
            tb_discipline.addCell(addCheckChecked());
        }else{
            tb_discipline.addCell(addCheckUnChecked());
        }
        tb_discipline.addCell(addCellPetitNoir("Psychiatrie"));
        if( laDemandePEC._psychiatrie.equals(laDemandePEC.getTypeHospitalisation()) ){
            tb_discipline.addCell(addCheckChecked());
        }else{
            tb_discipline.addCell(addCheckUnChecked());
        }
        tb_discipline.addCell(addCellPetitNoir("Maternité"));
        if( laDemandePEC._maternite.equals(laDemandePEC.getTypeHospitalisation()) ){
            tb_discipline.addCell(addCheckChecked());
        }else{
            tb_discipline.addCell(addCheckUnChecked());
        }
        tb_discipline.addCell(addCellPetitNoir("Repos/Conv/Rééduc"));
        if( laDemandePEC._maisonreposconvalescence.equals(laDemandePEC.getTypeHospitalisation()) ){
            tb_discipline.addCell(addCheckChecked());
        }else{
            tb_discipline.addCell(addCheckUnChecked());
        }
        PdfPCell dis = new PdfPCell();
        dis.setBorder(Rectangle.RIGHT);
        dis.addElement(tb_discipline);
        tb_hospitalisation.addCell(dis);
        
        tb_hospitalisation.addCell( addCellNormalNoirGauche("Ou Code DMT") );
        tb_hospitalisation.addCell( addCellNormalBleueDroite( laDemandePEC.getCodeDMT() ) );
        
        
        tb_hospitalisation.addCell( addCellNormalNoirGaucheBas("Mode ") );
        PdfPTable tb_mode = new PdfPTable(10);
        tb_mode.setWidths(new int[]{ 3,1,2,1,3,1,2,1,4,1});
        tb_mode.setWidthPercentage(100);
        tb_mode.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tb_mode.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
        tb_mode.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_mode.getDefaultCell().setPadding(0);
        
        tb_mode.addCell(addCellPetitNoir("Compléte"));
        if( laDemandePEC._hospitalisationComplete.equals(laDemandePEC.getModeTraitementHospitalisation() ) ){
            tb_mode.addCell(addCheckChecked());
        }else{
            tb_mode.addCell(addCheckUnChecked());
        }
        tb_mode.addCell(addCellPetitNoir("De jour"));
        if( laDemandePEC._hospitalisationDeJour.equals(laDemandePEC.getModeTraitementHospitalisation()) ){
            tb_mode.addCell(addCheckChecked());
        }else{
            tb_mode.addCell(addCheckUnChecked());
        }
        tb_mode.addCell(addCellPetitNoir("Ambulatoire"));
        if( laDemandePEC._hospitalisationAmbulatoire.equals(laDemandePEC.getModeTraitementHospitalisation()) ){
            tb_mode.addCell(addCheckChecked());
        }else{
            tb_mode.addCell(addCheckUnChecked());
        }
        tb_mode.addCell(addCellPetitNoir("Domicile"));
        if( laDemandePEC._hospitalisationDomicile.equals(laDemandePEC.getModeTraitementHospitalisation()) ){
            tb_mode.addCell(addCheckChecked());
        }else{
            tb_mode.addCell(addCheckUnChecked());
        }
        tb_mode.addCell(addCellPetitNoir(""));
        tb_mode.addCell(addCellPetitNoir(""));
        PdfPCell mod = new PdfPCell();
        mod.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM );
        mod.addElement(tb_mode);
        tb_hospitalisation.addCell(mod);
        
        return tb_hospitalisation;
    }
    
    private static PdfPTable blockFrais( DemandePec laDemandePEC ) throws Exception{
        
        PdfPTable tb_frais = new PdfPTable(2);
        tb_frais.setWidths(new int[]{ 2, 4 });
        tb_frais.setWidthPercentage(100);
        tb_frais.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_frais.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_frais.getDefaultCell().setPadding(_padding);
        
        tb_frais.addCell( addTitreBlock("FRAIS CONCERNES"));
        
        tb_frais.addCell( addCellNormalNoirGauche("Frais de séjour") );
        if( _Vrai.equals( laDemandePEC.getFraisSejour()) ){
            tb_frais.addCell( addCheckCheckedDroite());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroite());
        }
        
        tb_frais.addCell( addCellNormalNoirGauche("Forfait 18 euros") );
        if( _Vrai.equals( laDemandePEC.getForfait18()) ){
            tb_frais.addCell( addCheckCheckedDroite());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroite());
        }    
        
        tb_frais.addCell( addCellNormalNoirGauche("Forfait journalier") );
        if( _Vrai.equals( laDemandePEC.getForfaitJournalier()) ){
            tb_frais.addCell( addCheckCheckedDroite());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroite());
        }
        
        tb_frais.addCell( addCellNormalNoirGauche("Chambre particulière") );
        if( _Vrai.equals( laDemandePEC.getChambreParticuliere()) ){
            tb_frais.addCell( addCheckCheckedDroite());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroite());
        }
        
        tb_frais.addCell( addCellNormalNoirGauche("Lit accompagnant") );
        if( _Vrai.equals( laDemandePEC.getLitParent() ) ){
            tb_frais.addCell( addCheckCheckedDroite());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroite());
        }
        
        tb_frais.addCell( addCellNormalNoirGaucheBas("Honoraires") );
        if( _Vrai.equals( laDemandePEC.getHonoraire() ) ){
            tb_frais.addCell( addCheckCheckedDroiteBas());
        } else{
            tb_frais.addCell( addCheckUnCheckedDroiteBas());
        }
        
        return tb_frais;
    }
    
    private static PdfPTable blockCommentaire( DemandePec laDemandePEC ) throws Exception{
        
        PdfPTable tb_commentaire = new PdfPTable(1);
        tb_commentaire.setWidthPercentage(100);
        tb_commentaire.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tb_commentaire.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tb_commentaire.getDefaultCell().setPadding(_padding);
        
        tb_commentaire.addCell( addTitreBlock("COMMENTAIRE"));
        
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight( 120f );
        cell.addElement( addPhraseUTF8(laDemandePEC.getCommentaire(), bleu11) );
        
        tb_commentaire.addCell( cell );
        
        return tb_commentaire;
    }
    
    public static File editerDemande(DemandePec laDemandePEC) {
    
        File fichier = null;
        
        try{
            
            fichier = File.createTempFile("demandePEC_"+laDemandePEC.getId(),".pdf");
            
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fichier));
            document.open();
            
            document.add(blockEntete(laDemandePEC));
            document.add(new Phrase("\n",noir5));
            document.add(blockBeneficiaire(laDemandePEC));
            document.add(new Phrase("\n",noir5));
            document.add(blockEtablissement(laDemandePEC));
            document.add(new Phrase("\n",noir5));
            document.add(blockHospitalisation(laDemandePEC));
            document.add(new Phrase("\n",noir5));
            document.add(blockFrais(laDemandePEC));
            document.add(new Phrase("\n",noir5));
            document.add(blockCommentaire(laDemandePEC));
            
            document.close();
          
        } catch(Exception e){
            
            throw new IContactsException(e);
        }
            
        return fichier;
    }

    public static void main (String[] args){
    
        editerDemande( testDemande() );
        
    }
    
    private static DemandePec testDemande(){
        DemandePec laDemandePEC = new DemandePec();
        
        laDemandePEC.setDateCreation("24/04/2014");
        laDemandePEC.setOrganisme("MME");
        
        laDemandePEC.setNom_prenom_beneficiaire("DUPOND JEAN");
        laDemandePEC.setNumSS_beneficiaire("1945130000023");
        laDemandePEC.setStrDnai_beneficiaire("19700201");
        
        laDemandePEC.setEtablissementRS_appelant("HOPITAL BERNARD DEBRE");
        laDemandePEC.setNumFiness_appelant("123456789");
        laDemandePEC.setFax_appelant("01 02 03 04 05");
        
        laDemandePEC.setStrDateEntree("20140424");
        laDemandePEC.setNumEntree("15654EE");
        laDemandePEC.setTypeHospitalisation( DemandePec._medecine );
        laDemandePEC.setModeTraitementHospitalisation(DemandePec._hospitalisationAmbulatoire );
        
        laDemandePEC.setFraisSejour("1");
        laDemandePEC.setForfait18("0");
        laDemandePEC.setForfaitJournalier("1");
        laDemandePEC.setChambreParticuliere("0");
        laDemandePEC.setLitParent("0");
        laDemandePEC.setHonoraire("0");
        
        laDemandePEC.setCommentaire("OK");
        
        return laDemandePEC;
    }
    
}
