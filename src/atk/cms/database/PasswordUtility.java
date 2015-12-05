package atk.cms.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MessageDigest class takes clear-text password and creates a hashed version
 * Hashed version created using SHA-256 algorithm
 */
public class PasswordUtility {
     
    public static String hashPassword (String password) throws NoSuchAlgorithmException {        
        
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        
        // Convert password string to an array of bytes
        md.update(password.getBytes());
        
        // Generate array of bytes
        byte[] mdArray = md.digest();
        
        // Regenerate password string from array of bytes
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for (byte b : mdArray) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }        
        return sb.toString();        
    }
}
