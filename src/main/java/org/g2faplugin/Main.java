package org.g2faplugin;

import com.google.zxing.WriterException;
import de.taimos.totp.TOTP;
import io.github.shashankn.qrterminal.QRCode;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws WriterException, NoSuchAlgorithmException, InvalidKeySpecException {

        String secretKey = null;
        String username = System.getenv("username");
        String password = System.getenv("password");
        logger.debug("username : "+username+" , password :" +password);
        if (args.length > 0) {
            if (args[0].equals("--key") && (args.length == 3)) {
                ConfigFile.PutKeyToConfigFile(args[1], generateSecretKey());
                secretKey = ConfigFile.GetKeyFromConfigFile(args[1]);
                System.out.println(QRCode.from(getGoogleAuthenticatorBarCode(secretKey, args[1], args[2])).generateHalfBlock());
                System.out.println("");
                System.out.println("Google 2FA CODE: " + secretKey);
                System.exit(1);
            } else if (args[0].equals("--help")) {
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("--help             : display help");
                System.out.println("--key username servicename (pvz.: MineVPN)  : generate new QR-code");
                System.out.println("  for OpenVpn: ");
                System.out.println("java -jar g2faplugin.jar username='username' password='2fa code' ");
                System.out.println("");
                System.out.println("1. generate google 2fa code:  java -jar g2faplugin.jar --key email name ");
                System.out.println("2. install on android Google authentificator  ");
                System.out.println("https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en_US");
                System.out.println("3. usage in OpenVpn: java -jar g2faplugin.jar username='any' password='2fa code' ");
                System.out.println("   or use executable from APPLICATION folder");
                System.out.println("");
                System.out.println("2023.06 author: vytasgadliauskas@gmail.com");
                System.out.println("---------------------------------------------------------------------------------------------");
                System.exit(1);
            }
        }
        if (username != null && password != null) {
            secretKey = ConfigFile.GetKeyFromConfigFile(username);
            String code = getTOTPCode(secretKey);
            if (code != null) {
                logger.debug("code from TOTP: "+ code);
                if (password.equals(code)) {
                    System.out.println("ok");
                    logger.debug(" ok ");
                    System.exit(0);
                } else {
                    System.out.println("no ok");
                    logger.debug(" no ok ");
                    System.exit(1);
                }
            }
            System.out.println("try with --help");
            System.out.println("no ok");
            logger.debug(" no ok ");
            System.exit(1);
        }
        System.out.println("try with --help");
        System.out.println("no ok");
        logger.debug(" no ok ");
        System.exit(1);
    }

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        if (secretKey != null) {
            byte[] bytes = base32.decode(secretKey);
            String hexKey = Hex.encodeHexString(bytes);
            return TOTP.getOTP(hexKey);
        }
        return null;
    }

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}