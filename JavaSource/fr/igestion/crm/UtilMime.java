package fr.igestion.crm;

import java.util.HashMap;
import java.util.Map;

public class UtilMime {
        
    private static final Map<String, String> mimeExt=new HashMap<String, String>();
    static
    {   mimeExt.put(null,"");
        mimeExt.put("pdf","application/pdf");
        mimeExt.put("audio_basic","audio/basic");
        mimeExt.put("audio_wav", "audio/wav");
        mimeExt.put("mp3", "audio/mp3");
        mimeExt.put("image_gif", "image/gif");
        mimeExt.put("image_jpeg", "image/jpeg");
        mimeExt.put("image_bmp", "image/bmp");
        mimeExt.put("image_x-png", "image/x-png");
        mimeExt.put("msdownload","application/x-msdownload");
        mimeExt.put("video_avi", "video/avi");
        mimeExt.put("video_mpeg", "video/mpeg");
        mimeExt.put("html", "text/html");
        mimeExt.put("xml", "text/xml");
        mimeExt.put("xls", "application/vnd.ms-excel");
        mimeExt.put("xlt", "application/vnd.ms-excel");
        mimeExt.put("doc", "application/msword");
        mimeExt.put("gif", "image/gif");
        mimeExt.put("htm", "text/html");
        mimeExt.put("jpe", "image/jpeg");
        mimeExt.put("jpeg", "image/jpeg");
        mimeExt.put("jpg", "image/jpeg");
        mimeExt.put("rtf", "application/rtf");
        mimeExt.put("tif", "image/tiff");
        mimeExt.put("tiff", "image/tiff");
    }
    
    private UtilMime() {
    }
   
    public static String getMimeType(String format) {
        
        String miMeType = mimeExt.get( format.toLowerCase() );
 
        return (miMeType==null)? "" : miMeType ;
    }

    public static String getExtension(String fichier) {
        String res = "";
        String nom_fichier = fichier.toLowerCase();
        res = nom_fichier.substring(nom_fichier.lastIndexOf('.') + 1,
                nom_fichier.length());
        return res;
    }

}
