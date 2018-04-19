package fr.igestion.crm.bean;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.igestion.crm.IContacts;
import fr.igestion.crm.UtilDate;

@SuppressWarnings("unchecked")
public class ComparateurFiche implements Comparator<Appel> {

    private static final Logger LOGGER = Logger
            .getLogger(ComparateurFiche.class);

    private int _ordre;
    private String _colonne;

    private static final String _CRT_DATEAPPEL = "DATEAPPEL";
    private static final String _CRT_AUTEUR = "AUTEUR";
    private static final String _CRT_ID = "ID";
    private static final String _CRT_POLE = "POLE";
    private static final String _CRT_EG = "EG";
    private static final String _CRT_TYPEAPPELANT = "TYPEAPPELANT";
    private static final String _CRT_CODE = "CODE";
    private static final String _CRT_MUTUELLE = "MUTUELLE";
    private static final String _CRT_MOTIF = "MOTIF";
    private static final String _CRT_SOUSMOTIF = "SOUSMOTIF";
    private static final String _CRT_POINT = "POINT";
    private static final String _CRT_SOUSPOINT = "SOUSPOINT";
    private static final String _CRT_CODEADHERENTNUMEROCONTRAT="CODEADHERENTNUMEROCONTRAT";
    private static final String _CRT_STATUT="STATUT";
    
    private static final Map<String, Method> rules = new HashMap<String, Method>();
    static{
        try{
            @SuppressWarnings("rawtypes")
            Class [] argTypes = { Appel.class, Appel.class };
            @SuppressWarnings("rawtypes")
            Class c = Class.forName ("fr.igestion.crm.bean.ComparateurFiche");
            
            rules.put(_CRT_DATEAPPEL, c.getDeclaredMethod( "triParDateAppel" ,argTypes ) );
            rules.put(_CRT_AUTEUR, c.getDeclaredMethod( "triParAuteur" ,argTypes ) );
            rules.put(_CRT_ID, c.getDeclaredMethod( "triParId" ,argTypes ));
            rules.put(_CRT_POLE, c.getDeclaredMethod( "triParPole" ,argTypes ));
            rules.put(_CRT_EG, c.getDeclaredMethod( "triParEG" ,argTypes ));
            rules.put(_CRT_TYPEAPPELANT, c.getDeclaredMethod( "triParTypeAppelant" ,argTypes ));
            rules.put(_CRT_CODE, c.getDeclaredMethod( "triParCode" ,argTypes ));
            rules.put(_CRT_MUTUELLE, c.getDeclaredMethod( "triParMutuelle" ,argTypes ));
            rules.put(_CRT_MOTIF, c.getDeclaredMethod( "triParMotif" ,argTypes ));
            rules.put(_CRT_SOUSMOTIF, c.getDeclaredMethod( "triParSousMotif" ,argTypes ));
            rules.put(_CRT_POINT, c.getDeclaredMethod( "triParPoint" ,argTypes ));
            rules.put(_CRT_SOUSPOINT, c.getDeclaredMethod( "triParSousPoint" ,argTypes ));
            rules.put(_CRT_CODEADHERENTNUMEROCONTRAT, c.getDeclaredMethod( "triParAdherentContrat" ,argTypes ));
            rules.put(_CRT_STATUT, c.getDeclaredMethod( "triParStatut" ,argTypes ));
        }
        catch(Exception e){
            LOGGER.error("ComparateurFiche",e);
        }
    }
    
    @SuppressWarnings("unused")
    public ComparateurFiche(String asc_desc, String colonne) {

        this._colonne = colonne;

        if (IContacts._ASC.equalsIgnoreCase(asc_desc)) {
            this._ordre = 1;
        } else {
            this._ordre = -1;
        }
    }

    @SuppressWarnings("unused")
    private int triParDateAppel( Appel appel1, Appel appel2 ){
        return this._ordre  * UtilDate.fmtUSyyyyMMddHHmmss(appel1.getDATEAPPEL())
                .compareToIgnoreCase(
                        UtilDate.fmtUSyyyyMMddHHmmss(appel2
                                .getDATEAPPEL()));    
    }
    
    @SuppressWarnings("unused")
    private int triParAuteur( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getTeleacteur().compareToIgnoreCase(
                        appel2.getTeleacteur());    
    }
    
    @SuppressWarnings("unused")
    private int triParId( Appel appel1, Appel appel2 ){
        return this._ordre
            * appel1.getID().compareToIgnoreCase(appel2.getID());
    }    
    
    @SuppressWarnings("unused")
    private int triParPole( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getPole().compareToIgnoreCase(appel2.getPole());
    }
    
    @SuppressWarnings("unused")
    private int triParEG( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getEntiteGestion().compareToIgnoreCase(
                        appel2.getEntiteGestion());
    }
    
    @SuppressWarnings("unused")
    private int triParTypeAppelant( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getTypeAppelant().compareToIgnoreCase(
                        appel2.getTypeAppelant());
    }
    
    @SuppressWarnings("unused")
    private int triParCode( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getCodeAdherentNumeroContrat()
                        .compareToIgnoreCase(
                                appel2.getCodeAdherentNumeroContrat());
    }
    
    @SuppressWarnings("unused")
    private int triParMutuelle( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getMutuelle().compareToIgnoreCase(
                        appel2.getMutuelle());    
    }
    
    @SuppressWarnings("unused")
    private int triParMotif( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getMotif().compareToIgnoreCase(appel2.getMotif());    
    }
    
    @SuppressWarnings("unused")
    private int triParSousMotif( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getSousMotif().compareToIgnoreCase(
                        appel2.getSousMotif());
    }
    
    @SuppressWarnings("unused")
    private int triParPoint( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getPoint().compareToIgnoreCase(appel2.getPoint());    
    }
    
    @SuppressWarnings("unused")
    private int triParSousPoint( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getSousPoint().compareToIgnoreCase(
                        appel2.getSousPoint());    
    }
    
    @SuppressWarnings("unused")
    private int triParAdherentContrat( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getCodeAdherentNumeroContrat()
                        .compareToIgnoreCase(
                                appel2.getCodeAdherentNumeroContrat());    
    }
    
    @SuppressWarnings("unused")
    private int triParStatut( Appel appel1, Appel appel2 ){
        return this._ordre
                * appel1.getStatut()
                        .compareToIgnoreCase(appel2.getStatut());    
    }
    
    public int compare(Appel appel1, Appel appel2) {
        int result;
        try{
            Object [] methodData = {appel1, appel2}; 
            Method c = rules.get(_colonne);
            result = (Integer)c.invoke(this, methodData);
        } catch(Exception e){
            LOGGER.warn("Colonne non trouvée " + this._colonne, e);
            return 1;
        }
        return result;
    }
}
