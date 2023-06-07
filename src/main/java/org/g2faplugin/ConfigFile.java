package org.g2faplugin;

import java.io.*;
import java.util.Properties;

public class ConfigFile {
    private static final File configFile = new File("config.properties");

    public static String GetKeyFromConfigFile(){
        try ( FileReader reader = new FileReader(configFile)){
            Properties props = new Properties();
            props.load(reader);
            return props.getProperty("key");
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
        return null;
    }
    public static void  PutKeyToConfigFile(String key){
        try (FileWriter writer = new FileWriter(configFile)){
            Properties props = new Properties();
            props.setProperty("key", key);
            props.store(writer, "application settings");
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
    }





}
