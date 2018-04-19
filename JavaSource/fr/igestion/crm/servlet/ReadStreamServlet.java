package fr.igestion.crm.servlet;

import java.io.IOException;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilMime;
import fr.igestion.crm.bean.StreamFile;

public class ReadStreamServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ReadStreamServlet.class);

    private static final String _var_request_numeroBlob="numeroBlob";
    private static final String _var_request_courrierID="courrierID";
    private static final String _var_request_imageID="imageID";
    private static final String _var_request_base="base";

    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String numeroBlob = request.getParameter(_var_request_numeroBlob);
        String courrierID = request.getParameter(_var_request_courrierID);
        String imageID = request.getParameter(_var_request_imageID);
        String base = request.getParameter(_var_request_base);

        Map<String, String> criteres = new HashMap<String, String>();
        ServletOutputStream out = response.getOutputStream();
        byte[] data = null;
        Blob blob = null;
        String extension = "";
        String nomFichier = "";
        String contentType = "";

        criteres.put(_var_request_base, base);
        criteres.put(_var_request_numeroBlob, numeroBlob);

        if ("2".equals(numeroBlob)) {
            criteres.put(_var_request_courrierID, courrierID);
        } else if ("3".equals(numeroBlob)) {
            criteres.put(_var_request_imageID, imageID);
        } else if ("4".equals(numeroBlob)) {
            criteres.put(_var_request_imageID, imageID);
        }

        try {

            StreamFile sf = SQLDataService.getFileStream(criteres);
            nomFichier = sf.getNomFichier();
            if ("".equals(nomFichier)) {
                nomFichier = "Nom spécifié";
            }
            extension = sf.getExtension();
            contentType = UtilMime.getMimeType(extension);
            blob = sf.getBlob();
            data = blob.getBytes(1, (int) blob.length());
            
        } catch (Exception e) {
            LOGGER.warn("doGet",e);
            response.setContentType("text/html");
            out.println(getMessageErreur(e));
            out.flush();
            out.close();
        }

        response.setContentType(contentType);
        response.setContentLength(data.length);
        response.setHeader("Content-Disposition", "inline; filename="
                + nomFichier + "." + extension);

        out.write(data);
        out.flush();
        out.close();

    }

    public String getMessageErreur(Exception e) throws java.io.IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"./layout/hcontacts_styles.css\">");
        sb.append("</head><body>");
        sb.append("<center><label class=\"noir11B\">Erreur : </label> <label class=\"orange11B\"> "
                + e.toString() + "<label/></center> ");
        sb.append("</body></html>");
        return sb.toString();
    }

}
