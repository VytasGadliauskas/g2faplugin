package org.g2faplugin;

import java.io.*;
import java.util.Properties;

public class ConfigFile {
    private static final File configFile = new File("config.properties");

    public static String GetKeyFromConfigFile(String username) {
        try (FileReader reader = new FileReader(configFile)) {
            Properties props = new Properties();
            props.load(reader);
            return props.getProperty(username);
        } catch (FileNotFoundException ex) {
            System.out.println("try with --help");
            System.out.println("no ok");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("try with --help");
            System.out.println("no ok");
            System.exit(1);
        }
        return null;
    }

    public static void PutKeyToConfigFile(String username, String key) {
        Properties props = new Properties();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                props.load(reader);
                props.getProperty(username);
            } catch (FileNotFoundException ex) {
                System.out.println("no ok");
                System.exit(1);
            } catch (IOException ex) {
                System.out.println("no ok");
                System.exit(1);
            }
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            props.setProperty(username, key);
            props.store(writer, "Users with Google 2FA");
        } catch (FileNotFoundException ex) {
            System.out.println("try with --help");
            System.out.println("no ok");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("try with --help");
            System.out.println("no ok");
            System.exit(1);
        }
    }


}
