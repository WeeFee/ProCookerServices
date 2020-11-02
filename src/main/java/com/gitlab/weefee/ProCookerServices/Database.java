package com.gitlab.weefee.ProCookerServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Database {
    private String databaseURL = "";

    /**
     *
     * @param inputURL
     */
    public Database(String inputURL) {
        databaseURL = inputURL;
    }
    
    /**
     *
     * @param collection
     * @param key
     * @return
     */
    public String readFromDatabase(String collection, String key) {
        File databaseObj = new File(databaseURL + collection + "/" + key);

        StringBuilder dataLoaded = new StringBuilder();

        try {
            Scanner databaseReader = new Scanner(databaseObj);
            while (databaseReader.hasNextLine()) {
                dataLoaded.append(databaseReader.nextLine());
            }
            databaseReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return dataLoaded.toString();
    }

    /**
     *
     * @param collection
     * @param key
     * @param content
     * @return
     */
    public boolean writeToDatabase(String collection, String key, String content) {
        try {
            FileWriter userWriter = new FileWriter(databaseURL + collection + "/" + key);
            userWriter.write(content);
            userWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param collection
     * @param key
     * @return
     */
    public boolean keyExists(String collection, String key) {
        File databaseObj = new File(databaseURL + collection + "/" + key);
        return databaseObj.exists();
    }
}
