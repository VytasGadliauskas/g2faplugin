package org.example;

import com.google.zxing.WriterException;
import de.taimos.totp.TOTP;
import io.github.shashankn.qrterminal.QRCode;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException, WriterException {
        String secretKey = null;
        if (args.length > 0) {
            secretKey = ConfigFile.GetKeyFromConfigFile();
            if (args[0].equals("--key")&&(args.length == 3)) {
                ConfigFile.PutKeyToConfigFile(generateSecretKey());
                secretKey = ConfigFile.GetKeyFromConfigFile();
                System.out.println(QRCode.from(getGoogleAuthenticatorBarCode(secretKey,args[1],args[2])).generate());
                System.out.println("");
                System.out.println("Google 2FA CODE: "+secretKey);
                System.exit(0);
            } else if (args[0].equals("--help")) {
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("--help             : display help");
                System.out.println("--key email name   : generate new QR-code");
                System.out.println("  for OpenVpn: ");
                System.out.println("java -jar g2faplugin.jar username='any' password='2fa code' ");
                System.out.println("");
                System.out.println("1. generate google 2fa code:  java -jar g2faplugin.jar --key email name ");
                System.out.println("2. install on android Google authentificator  ");
                System.out.println("https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en_US");
                System.out.println("3. usage in OpenVpn: java -jar g2faplugin.jar username='any' password='2fa code' ");
                System.out.println("");
                System.out.println("2023.06 author: vytasgadliauskas@gmail.com");
                System.out.println("---------------------------------------------------------------------------------------------");
            } else {
                String code = getTOTPCode(secretKey);
                if (code != null) {
                    HashMap<String, String> params = new HashMap<>();
                    for (int i = 0; i < args.length; i++) {
                        if (args[i].contains("=")) {
                            String[] arrStr = args[i].split("=");
                            if (arrStr.length ==2 ) {
                                params.put(arrStr[0], arrStr[1]);
                            }
                        }
                    }
                    if (!params.isEmpty()) {
                        if (params.get("password") != null) {
                            if (params.get("password").equals(code)) {
                                System.out.println("1");
                                System.exit(0);
                            } else {
                                System.out.println("0");
                                System.exit(0);
                            }
                        }
                    }
                }
                System.out.println("try with --help");
                System.out.println("0");
                System.exit(0);
            }
            System.out.println("0");
            System.exit(0);
        }
        System.out.println("try with --help");
        System.out.println("0");
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