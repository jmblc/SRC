package fr.igestion.crm.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.document.Document;
import fr.igestion.crm.config.IContacts;

public class AdministrationDocumentAction extends DispatchAction {
    
    private FormFile file;       
    public FormFile getFile() {         
        return file;     
    }
    public void setFile(FormFile file) {
        this.file = file;     
    } 
    
    private static SimpleDateFormat _ftmtStringToDate = new SimpleDateFormat("dd/MM/yy");
    //private static DateFormat _ftmtDateToString = new DateFormat("dd/MM/yy");
    
    
    private static String _document_id="document_id";
    private static String _libelle="libelle";
    private static String _type="type";
    private static String _description="description";
    private static String _debut="debut";
    private static String _fin="fin";
    private static String _fichier="fichier";
    private static String _nom_fichier="nom_fichier";
    
    private static String _var_session_documents="documents";
    private static String _var_session_documentProcedures="documentProcedures";
        
    private static final String _var_req_message = "message";
    
    private static void resetDocumentForm(DynaActionForm daf){
        daf.set(_document_id,IContacts._blankSelect);
        daf.set(_libelle,"");
        daf.set(_type,"");
        daf.set(_description,"");
        daf.set(_debut,"");
        daf.set(_fin,"");
        daf.set(_fichier,null);
        daf.set(_nom_fichier,null);
        
    }
        
    private static Document initDocumentForm( DynaActionForm daf ) throws Exception{
        Document leDocument = new Document();
        leDocument.setId((String) daf.get(_document_id));
        leDocument.setLibelle((String) daf.get(_libelle));
        leDocument.setType((String) daf.get(_type));
        leDocument.setDescription((String) daf.get(_description));
        FormFile fichier = (FormFile)daf.get(_fichier);
        leDocument.setFichier(fichier);
        if (StringUtils.isNotEmpty(fichier.getFileName())) {
        	leDocument.setNomFichier( leDocument.getFichier().getFileName());
        } else {
        	leDocument.setNomFichier((String)daf.get(_nom_fichier));
        }
        leDocument.setDebut(_ftmtStringToDate.parse((String) daf.get(_debut)));
        if( ((String)daf.get(_fin))!=null && !((String)daf.get(_fin)).equalsIgnoreCase("")){
            leDocument.setFin(_ftmtStringToDate.parse((String) daf.get(_fin)));
        }
        return leDocument;
    }
    
    private static void initFormDocument( DynaActionForm daf, Document leDocument ) throws Exception{
        
        daf.set(_document_id,leDocument.getId());
        daf.set(_libelle,leDocument.getLibelle()); 
        daf.set(_type,leDocument.getType());
        daf.set(_description,leDocument.getDescription());
        daf.set(_debut, _ftmtStringToDate.format(leDocument.getDebut()));
        if( leDocument.getFin() != null  ){
            daf.set(_fin, _ftmtStringToDate.format(leDocument.getFin()));
        }
        else{
            daf.set(_fin, ""); 
        }
        daf.set(_nom_fichier,leDocument.getNomFichier());
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        resetDocumentForm(daf);
        
        // initialisation liste des document
        Collection<Document> documents = SQLDataService
                .getDocuments();
        request.getSession().setAttribute(_var_session_documents, documents);

        request.getSession().setAttribute(_var_session_documentProcedures, null);
        
        request.getSession().setAttribute("AdministrationDocumentForm", daf);
        return mapping.findForward(IContacts._document);

    }
    

    public ActionForward modifierDocument(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        
        boolean res = false;
        
        try{
            Document leDocument = initDocumentForm(daf);
            res=SQLDataService.modifierDocument(leDocument);
        }
        catch(Exception e){
            res = false;
        }
        
        String message = "";
        if (res) {
            message = "Le paramétrage a été réalisé.";
        } else {
            message = "Attention! Le paramétrage n'a pas été modifié!";
        }
        request.setAttribute(_var_req_message, message);
       
        return mapping.findForward(IContacts._document);
    }

    public ActionForward ajouterDocument(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        boolean res = false;
        
        DynaActionForm daf = (DynaActionForm) form;
        try{
            Document leDocument = initDocumentForm(daf);
            res=SQLDataService.ajouterDocument(leDocument);
        }
        catch(Exception e){
            res = false;
        }
        String message = "";
        if (res) {
            message = "Le paramétrage a été réalisé.";
        } else {
            message = "Attention! Le paramétrage n'a pas été modifié!";
        }
        request.setAttribute(_var_req_message, message);
       
        resetDocumentForm(daf);
        Collection<Document> documents = SQLDataService.getDocuments();
        request.getSession().setAttribute(_var_session_documents, documents);
        
        return mapping.findForward(IContacts._document);
    }

    
    public ActionForward supprimerDocument(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
                
        boolean res = false;
        
        try{
            res=SQLDataService.supprimerDocument((String) daf.get(_document_id));
        }
        catch(Exception e){
            res = false;
        }
        
        String message = "";
        if (res) {
            message = "Le document a été supprimé.";
        } else {
            message = "Attention! Le paramétrage n'a pas été modifié!";
        }
        request.setAttribute(_var_req_message, message);
       
        resetDocumentForm(daf);
        Collection<Document> documents = SQLDataService.getDocuments();
        request.getSession().setAttribute(_var_session_documents, documents);
        
        return mapping.findForward(IContacts._document);
    }
    
    public ActionForward selectDocument(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        if (!"-1".equalsIgnoreCase((String) daf.get(_document_id))) {

            Document leDocument = SQLDataService.getDocument((String) daf.get(_document_id));

            if (leDocument!= null) {
                
                try{
                    initFormDocument( daf, leDocument );
                }
                catch(Exception e){
                    request.setAttribute(_var_req_message,
                            "Erreur lecture du document.");
                }
                Collection<ModeleProcedureMail> documentProcedures = SQLDataService
                        .getDocumentProcedures(leDocument);
                request.getSession().setAttribute(_var_session_documentProcedures,
                        documentProcedures);

            } else {
                request.setAttribute(_var_req_message,
                        "Erreur lecture du document.");
            }
        } else {
            resetDocumentForm(daf); 
            request.getSession().setAttribute(_var_session_documentProcedures,
                    null);
        }

        return mapping.findForward(IContacts._document);
    }

    
}