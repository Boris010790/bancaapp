package utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe contenente dei metodi di utilità.
 * @author Boris
 */

public abstract class Utility {

  /**
   * Metodo che codifica un testo utilizzando una funzione hash non invertibile.
   * @param testo il messaggio in chiaro che si vuole codificare.
   * @return il testo codificato in md5.
   */
    
    public static String codificaInMD5(String testo) {
        String s = testo;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        m.update(s.getBytes());
        return new BigInteger(1, m.digest()).toString(16);
    }

    /**
     * Restituisce una funzione javascript che permette una particolare visualizzazione lato client.
     * @param message il messaggio che si vuole mostrare all'utente.
     * @param status indica se è un messaggio di successo o di errore.
     * @return unauna funzione javascript che permette una particolare visualizzazione lato client.
     */
    public static String getMessage(String message, String status) {
        return "<script> $.simplyToast('" + message.toUpperCase() + "', '" + status + "');</script>";
    }

    /**
     * Stabilisce se una email è sintatticamente corretta.
     * @param email l'email che si vuole verificare
     * @return true se la email è valida, false altrimenti
     */
    public static boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Stabilisce se una stringa può essere parsificata in Double senza generare errori.
     * @param s la stringa che si vuole parsificare.
     * @return true la stringa è parsificabile in un double, false altrimenti.
     */
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Trasforma in maiuscolo le lettere di ogni parola di una stringa e setta in minuscolo le restanti
     * @param s il testo che si vuole trasformare.
     * @return una stringa con le lettere di ogni parola in maiuscolo e in minuscolo le restanti.
     */
    
    public static String capitalize(String s) {
    
        if (s == null || s.length() < 2) {
            return s;
        }

        String[] lista = s.split("\\s+");
        for (int i = 0; i < lista.length; i++) {
            String temp = lista[i].toLowerCase();
            lista[i] = temp.substring(0, 1).toUpperCase() + "" + temp.substring(1, temp.length());
        }

        return String.join(" ", lista);

    }

    /**
     * Restituisce la data e l'ora in quel preciso istante
     * @return la data e l'ora in quel preciso istante
     */
    public static String getActualDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return day + "-" + month + "-" + year+" "+cal.getTime().getHours()+":"+cal.getTime().getMinutes()+":"+cal.getTime().getSeconds();
    }

    /**
     * Dato un numero restituisce il mese dell'anno in formato stringa.
     * @param mese il mese nel formato numerico
     * @return il mese nel formato stringa o null nel caso il mese non sia compreso tra 1 e 12
     */
    public static String getMese(int mese) {
        switch (mese) {
            case 1:
                return "Gennaio";
            case 2:
                return "Febbraio";
            case 3:
                return "Marzo";
            case 4:
                return "Aprile";
            case 5:
                return "Maggio";
            case 6:
                return "Giugno";
            case 7:
                return "Luglio";
            case 8:
                return "Agosto";
            case 9:
                return "Settembre";
            case 10:
                return "Ottobre";
            case 11:
                return "Novembre";
            case 12:
                return "Dicembre";
        }
        return null;
    }
    
    /**
     * Restituisce la data e l'ora nel formato gg mese aaaa hh:mm
     * @param datetime la stringa contenente la data
     * @return la data e l'ora nel formato gg mese aaaa hh:mm
    */
    
    public static String getFormatDateView(String datetime){
       String data = datetime.split("\\s+")[0];
       String time = datetime.split("\\s+")[1];
      
       String ora = time.split(":")[0];
       String minuti = time.split(":")[1];
       if(Integer.parseInt(minuti) < 10 ) minuti="0"+minuti;
       
        String [] lista = data.split("-");
        int mese = Integer.parseInt(lista[1]);
        return lista[0] + " "+getMese(mese).substring(0,3).toUpperCase()+" "+lista[2]+" "+ora+":"+minuti;
    }
    
    /**
     * Dato un double, ne restituisce la rappresentazione nel formato stringa 0.00
     * @param saldo l'importo nel formato double
     * @return una stringa nel formato 0.00
     */
    public static String getFormatImportoView(Double saldo){
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(saldo);
    }
}


