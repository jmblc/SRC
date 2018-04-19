package fr.igestion.crm.servlet;

import java.io.ByteArrayInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.Logger;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilMime;
import fr.igestion.crm.bean.VersionGarantie;

public class AfficherPlaquetteGarantie extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(AfficherPlaquetteGarantie.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {

            String idPlaquette = (String) request.getParameter("idPlaquette");
            VersionGarantie version = SQLDataService
                    .getGarantieVersion(idPlaquette);
            byte[] contenu_bytes = null;
            String extension = "";

            if (version != null) {
                contenu_bytes = version.getPLV_BLOB();
                extension = UtilMime.getExtension(version.getPLV_NOM_FICHIER());
            }

            ByteArrayInputStream bais = null;
            ServletOutputStream sout = null;
            byte[] buffer;
            int bytes_read = 0, bufferSize = 0;
            String contentType = "";

            if (contenu_bytes != null) {
                bais = new ByteArrayInputStream(contenu_bytes);
                contentType = UtilMime.getMimeType(extension);
                response.reset();
                response.setContentType(contentType);

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + version.getPLV_NOM_FICHIER()
                                + ";");

                if (sout == null) {
                    sout = response.getOutputStream();
                }
                bufferSize = contenu_bytes.length;

                buffer = new byte[bufferSize];
                while ((bytes_read = bais.read(buffer)) != -1) {
                    sout.write(buffer, 0, bytes_read);
                }
                sout.flush();
                sout.close();
                sout = null;
            }
        } catch (Exception e) {
            LOGGER.error("Anomalie", e);
        }
    }

}
