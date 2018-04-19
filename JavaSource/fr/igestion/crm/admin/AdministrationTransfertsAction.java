package fr.igestion.crm.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.ComparateurTransfert;
import fr.igestion.crm.bean.Transfert;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.config.IContacts;

public class AdministrationTransfertsAction extends DispatchAction {

    private static final String _transfert_id="transfert_id";
    private static final String _libelle="libelle";
    private static final String _email="email";
    private static final String _texte_generique="texte_generique";
    private static final String _affectation_campagnes="affectations";
    
    private static final String _var_session_sens_tri_transferts="sens_tri_transferts";
    private static final String _var_session_admin_transferts="admin_transferts";
    
    private static final String _req_message="message";
    
    private static void initTransfertSessionVar(HttpServletRequest request){
    	
        Collection<Transfert> admin_transferts = (Collection<Transfert>) SQLDataService.getTransferts();
        request.getSession().setAttribute(_var_session_admin_transferts, admin_transferts);
        request.setAttribute("info", request.getSession().getAttribute("info"));
        request.getSession().removeAttribute("info");
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);
    }
    
    public void resetTransfertForm(DynaActionForm daf){
        daf.set(_transfert_id, null);
        daf.set(IContacts._struts_method, null);
        daf.set(_libelle,"");
        daf.set(_email,"");
    }

    public ActionForward supprimerTransfert(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        DynaActionForm daf = (DynaActionForm) form;
        String transfert_id = (String) daf.get(_transfert_id);

        boolean res = SQLDataService.supprimerTransfert(transfert_id);
        String message = "";

        if (res) {
            message = "Le transfert a été supprimé avec succès.";
        } else {
            message = "Attention! Le transfert n'a pas été supprimé!";
        }
        request.setAttribute(_req_message, message);

        resetTransfertForm(daf);
        initTransfertSessionVar(request);
        return mapping.findForward(IContacts._transferts);
    }
    
    public ActionForward popModifierTransfert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String transfert_id = (String) request.getParameter(_transfert_id);	
    	Transfert transfert_objet = SQLDataService.getTransfertById(transfert_id);
    	Collection<Campagne> campagnesAffectees = transfert_objet.getCampagnesAffectees();
    	
    	if (transfert_objet != null) {
    		@SuppressWarnings("unchecked")
			Collection<Campagne> campagnes = (Collection<Campagne>)request.getSession().getAttribute("campagnes_creation_et_recherche");
    		ArrayList<HashMap<String, String>> listeCampagnes = new ArrayList<HashMap<String,String>>();
    		
    		boolean isCampagneAffectee = false;
    		HashMap<String, String> detailCampagne;
    		
    		for (Campagne campagne : campagnes) {
    			detailCampagne = new HashMap<String, String>();
    			detailCampagne.put("id", campagne.getId());
    			detailCampagne.put("libelle", campagne.getLibelle());
    			detailCampagne.put("actif", campagne.getActif());
    			if (campagnesAffectees != null && campagnesAffectees.contains(campagne)) {
    				detailCampagne.put("selected", "true");
    				isCampagneAffectee = true;
    			}
    			listeCampagnes.add(detailCampagne);
    		}
    		detailCampagne = new HashMap<String, String>();
			detailCampagne.put("id", "");
			detailCampagne.put("libelle", "- Toutes les campagnes -");
			detailCampagne.put("actif", "1");
			if (!isCampagneAffectee) {
				detailCampagne.put("selected", "true");
			}
			listeCampagnes.add(0, detailCampagne);
			
    		request.setAttribute("transfert_objet", transfert_objet);
    		request.setAttribute("listeCampagnes", listeCampagnes);
    	}

        return mapping.findForward("le_transfert_en_cours");

    }

    public ActionForward modifierTransfert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;

        String transfert_id = (String) daf.get(_transfert_id);
        String libelle = (String) daf.get(_libelle);
        String email = (String) daf.get(_email);
        String[] campagnesAffectees = (String[]) daf.get("affectations");

        boolean res = SQLDataService.modifierTransfert(transfert_id, libelle, email, campagnesAffectees);
        String message = "";

        if (res) {
            message = "Le transfert a été modifié avec succès.";
        } else {
            message = "Attention ! Le transfert n'a pas été modifié !";
        }
        request.getSession().setAttribute("info", message);

        resetTransfertForm(daf);
        return null;

    }

    public ActionForward creerTransfert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DynaActionForm daf = (DynaActionForm) form;
        String libelle = (String) daf.get(_libelle);
        String email = (String) daf.get(_email);
		String[] affectationCampagnes = (String[]) daf.get(_affectation_campagnes);

        boolean res = SQLDataService.creerTransfert(libelle, email, affectationCampagnes);
        String message = "";


        if (res) {
            message = "Le transfert a été créé avec succès.";
        } else {
            message = "Attention ! Le transfert n'a pas été créé !";
        }
        request.getSession().setAttribute("info", message);

        daf.set(IContacts._struts_method, null);

        return null;

    }

    public ActionForward trierTransferts(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_transferts = (String) daf.get(_texte_generique);

        String sens_tri_transferts = (String) request.getSession()
                .getAttribute(_var_session_sens_tri_transferts);
        if (IContacts._ASC.equals(sens_tri_transferts) ) {
            sens_tri_transferts = IContacts._DESC;
        } else {
            sens_tri_transferts = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_transferts,
                sens_tri_transferts);

        Collection<Transfert> admin_transferts = (Collection<Transfert>) request
                .getSession().getAttribute(_var_session_admin_transferts);
        List<Transfert> liste = new ArrayList<Transfert>(admin_transferts);
        ComparateurTransfert comparateur = new ComparateurTransfert(
                sens_tri_transferts, col_de_tri_transferts);
        Collections.sort(liste, comparateur);
        request.getSession().setAttribute(_var_session_admin_transferts, liste);

        return mapping.findForward(IContacts._transferts);
    }

}