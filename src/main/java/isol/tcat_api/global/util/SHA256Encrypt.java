package isol.tcat_api.global.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256Encrypt {

    public String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];

        secureRandom.nextBytes(salt);

        StringBuffer stringBuffer = new StringBuffer();

        for (byte b : salt) {
            stringBuffer.append(String.format("%02x", b));
        }

        return stringBuffer.toString();
    }

    public String getEncrypt(String pw, String salt) {
        String result = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update((pw + salt).getBytes());

            byte[] pwSalt = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();

            for (byte b : pwSalt) {
                stringBuffer.append(String.format("%02x", b));
            }

            result = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
