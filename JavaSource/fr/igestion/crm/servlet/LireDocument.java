package fr.igestion.crm.servlet;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.document.Document;

public class LireDocument extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    
	
		try{
			String idDocument = (String) request.getParameter("idDocument");
			
			byte[] contenuByte = null;
			String nomFichier = "";
			
			Document leDocument = SQLDataService.getDocument(idDocument);
			if( leDocument != null ){
					contenuByte = leDocument.getContenu();
					nomFichier = leDocument.getNomFichier();
			}
			
			ByteArrayInputStream bais = null;	
			ServletOutputStream sout = null;
			byte[] buffer;
			int bytes_read = 0;
			int bufferSize = 0;	

			String contentType = "";	
			
			if(contenuByte != null){
				bais  = new ByteArrayInputStream(contenuByte);							
				response.reset();								
				response.setContentType(contentType); 						
				response.setHeader("Content-Disposition", "attachment;filename=" + nomFichier + ";" );				
							
				if( sout == null ){	
					sout = response.getOutputStream(); 	
				}
				bufferSize = contenuByte.length;				
				buffer = new byte[bufferSize];
				while( (bytes_read = bais.read(buffer)) != -1){
					sout.write(buffer, 0, bytes_read);
				}			
				sout.flush();
				sout.close(); 		
				sout = null;
			}
		}
		catch(Exception e){}
		
	}
		
	
	
	
	
	
}
