package com.gitlab.weefee.ProCookerServices;

import express.utils.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Database {
    public static String readFromDatabase(String collection, String key) {
        File databaseObj = new File("./db/" + collection + "/" + key);

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

    public static boolean writeToDatabase(String collection, String key, String content) {
        try {
            FileWriter userWriter = new FileWriter("./db/" + collection + "/" + key);
            userWriter.write(content);
            userWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean keyExists(String collection, String key) {
        File databaseObj = new File("./db/" + collection + "/" + key);
        return databaseObj.exists();
    }
}
