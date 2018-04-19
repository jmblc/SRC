package fr.igestion.crm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UtilDate {
 
    private static DateFormat fmtddMMyyyyHHmmss = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss");
    private static DateFormat fmtUSyyyyMMddHHmmss = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");
    private static DateFormat fmtddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat fmtUSyyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
    private static DateFormat fmtyyyy = new SimpleDateFormat("yyyy");
    private static DateFormat fmtMM = new SimpleDateFormat("MM");
    private static DateFormat fmtDD = new SimpleDateFormat("dd");
    private static DateFormat fmtDDMM = new SimpleDateFormat("ddMM");
    private static DateFormat fmtHHmmss = new SimpleDateFormat("HH:mm:ss");
    private static DateFormat fmtHHmm = new SimpleDateFormat("HH:mm");
    private static DateFormat fmtHH = new SimpleDateFormat("HH");
    private static DateFormat fmtmm = new SimpleDateFormat("mm");
    private static DateFormat fmtmmss = new SimpleDateFormat("mm:ss");
    private static DateFormat fmtmmssSS = new SimpleDateFormat("mm:ss:SS");
   
    
    private UtilDate() {
    }
    
    public static String fmtDDMMYYYYHHMMSS(Date uneDate) {

        if (uneDate != null) {
            return fmtddMMyyyyHHmmss.format(uneDate);
        } else {
            return "";
        }
    }

    public static String fmtUSyyyyMMddHHmmss(Date uneDate) {
        return fmtUSyyyyMMddHHmmss.format(uneDate);
    }

    public static String formatDDMMYYYY(Date uneDate) {
        if (uneDate != null) {
            return fmtddMMyyyy.format(uneDate);
        } else {
            return "";
        }    
    }

    public static String formatUSYYYYMMDD(Date uneDate) {
        return fmtUSyyyyMMdd.format(uneDate);
    }

    public static String formatYYYY(Date uneDate) {
        return fmtyyyy.format(uneDate);
    }

    public static String formatMM(Date uneDate) {
        return fmtMM.format(uneDate);
    }

    public static String formatDD(Date uneDate) {
        return fmtDD.format(uneDate);
    }

    public static String formatDDMM(Date uneDate) {
        return fmtDDMM.format(uneDate);
    }

    public static String formatHHMMSS(Date uneDate) {
        fmtHHmmss.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmtHHmmss.format(uneDate);
    }

    public static String formatHHMM(Date uneDate) {
        return fmtHHmm.format(uneDate);
    }

    public static String formatHH(Date uneDate) {
        return fmtHH.format(uneDate);
    }

    public static String formatmm(Date uneDate) {
        return fmtmm.format(uneDate);
    }

    public static String formatmmss(Date uneDate) {
        fmtHHmmss.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmtmmss.format(uneDate);
    }

    public static String fmtmmssSS(Date uneDate) {
        fmtHHmmss.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmtmmssSS.format(uneDate);
    }

}
