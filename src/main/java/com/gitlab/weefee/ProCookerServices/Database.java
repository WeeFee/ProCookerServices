/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Database class, imported from ProCookerServices.
 */
public class Database {
    private String databaseURL = "";
    /**
     * Database constructor method.
     * @param inputDatabaseURL Path to folder that contains the database.
     */
    public Database(String inputDatabaseURL) {
        databaseURL = inputDatabaseURL;
        this.collectDatabase();
    }

    /**
     * The in-memory database, only used internally.
     */
    private ArrayList<ArrayList<ArrayList<String>>> databaseMemory = null;

    /**
     * The database reference table, only used internally.
     */
    private ArrayList<ArrayList<String>> databaseRef = null;

    /**
     * Loads a value from a file on disk, only used internally.
     * @param collection The collection to read from.
     * @param key The key to read from.
     * @return A string contain the raw contents of the file.
     * @deprecated
     */
    private String readFromDiskDatabase(String collection, String key) {
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
     * Writes a value to a file on disk, only used internally.
     * @param collection The collection to write to.
     * @param key The key to write to.
     * @param content The content to write to file.
     * @return A boolean indicating if the write was successful.
     * @deprecated
     */
    private boolean writeToDiskDatabase(String collection, String key, String content) {
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
     * Read from the in-memory database by specifying a collection and a key to retrieve from.
     * @param collection The collection to read from.
     * @param key The key to read from.
     * @return The values that are contained in the database.
     */
    public ArrayList<String> readFromDatabase(String collection, String key) {
        int[] pos = this.findKey(collection, key);
        return databaseMemory.get(pos[0]).get(pos[1]);
    }

    /**
     * Write to the in-memory database by specifying a collection, a key, and the content you want to add.
     * @param collection The collection to write to.
     * @param key The key to write to.
     * @param content The content to write the database.
     * @return A boolean indicating if the write was successful.
     */
    public boolean writeToDatabase(String collection, String key, String[] content) {
        try {
            int[] pos = this.findKey(collection, key);

            // Assume cloud save
            if (pos[1] == -2) {
                databaseMemory.get(pos[0]).add(new ArrayList<String>(content.length));
                databaseRef.get(pos[0]).add(content[1]);
                pos[1] = databaseMemory.get(pos[0]).size() + -1;
            }
            // Gets the position of the specified key in tje specified collection
            databaseMemory.get(pos[0]).get(pos[1]).clear();
            for (String s : content) {
                // Sets the value of the key in the collection.
                databaseMemory.get(pos[0]).get(pos[1]).add(s);
            }
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check to see if the key in a collection exists on the disk.
     * @param collection The collection to check for.
     * @param key The key to check for.
     * @return A boolean indicating if the key exists.
     * @deprecated
     */
    public boolean keyExistsDisk(String collection, String key) {
        File databaseObj = new File(databaseURL + collection + "/" + key);
        return databaseObj.exists();
    }

    /**
     * Finds the location of a specified key in a specified collection.
     * @param collection The collection to look in.
     * @param key The key to look for.
     * @return X/Y coordinates for the key position in the database.
     */
    public int[] findKey(String collection, String key) {
        int collectionPos = -1;
        for (int i = 0; i < databaseRef.toArray().length; i++) {
            if (databaseRef.get(i).get(0).equals(collection)) {
                // Finds the collection position from databaseRef
                collectionPos = i;
            }
        }
        // Finds the key position from databaseRef
        int keyPos = databaseRef.get(collectionPos).indexOf(key) - 1;
        // Packages it into a nice X/Y int array
        return new int[]{collectionPos, keyPos};
    }

    /**
     * Loads the database into memory.
     * @return A boolean indicating if the load was successful.
     */
    private boolean collectDatabase() {
        // Makes a file array of all folders in the top level directory for collections
        File[] diskCollections = new File(databaseURL).listFiles(File::isDirectory);
        // Ensures that folders are present
        assert diskCollections != null;

        // Creates a new 3D ArrayList to act as the database in memory with the amount of collections as the length.
        databaseMemory = new ArrayList<ArrayList<ArrayList<String>>>((int) Arrays.stream(diskCollections).count());
        // Creates a 2D ArrayList to act as a quick refernce table with the amount of collections as the length.
        databaseRef = new ArrayList<ArrayList<String>>((int) Arrays.stream(diskCollections).count());

        for (int i = 0; i < diskCollections.length; i++) {
            // Gets each file (key) in each folder (collection) and makes a file array.
            File[] diskCollectionKeys = new File(databaseURL + diskCollections[i].getName()).listFiles();
            // Ensures that files are present
            assert diskCollectionKeys != null;

            // Creates a 2D ArrayList for keys with the amount of keys as the length.
            databaseMemory.add(new ArrayList<ArrayList<String>>((int) Arrays.stream(diskCollectionKeys).count()));
            // Creates an ArrayList of Strings that will contain the collection's keys.
            databaseRef.add(new ArrayList<String>((int) Arrays.stream(diskCollectionKeys).count() + 1));
            // Adds the collection's name to the first slot in the ArrayList.
            databaseRef.get(i).add(diskCollections[i].getName());

            for (int x = 0; x < diskCollectionKeys.length; x++) {
                // Splits the values present in the files to deal with arrays.
                String[] keyValues = readFromDiskDatabase(diskCollections[i].getName(), diskCollectionKeys[x].getName()).split(",");
                // Creates an ArrayList of Strings that will contain the values.
                databaseMemory.get(i).add(new ArrayList<String>(keyValues.length));
                for (String value: keyValues) {
                    // Adds the values to the database memory.
                    databaseMemory.get(i).get(x).add(value);
                }
                // Adds the key to the collection refernce table.
                databaseRef.get(i).add(diskCollectionKeys[x].getName());
            }
        }
        return true;
    }

    /**
     * Writes the database to disk.
     * @return A boolean indicating if the write was successful.
     */
    public boolean flushDatabase() {
        for (ArrayList<String> strings : databaseRef) {
            for (int x = 0; x < strings.size(); x++) {
                if (x == 0) {
                    // Skips the collection name in the refernce table.
                    continue;
                }
                // Creates an ArrayList that will stores the value from the database.
                ArrayList<String> contentArray = readFromDatabase(strings.get(0), strings.get(x));

                StringBuilder finalContent = new StringBuilder();

                for (String content: contentArray) {
                    finalContent.append(content);
                    finalContent.append(",");
                }
                if (!writeToDiskDatabase(strings.get(0), strings.get(x), finalContent.toString())) {
                    return false;
                }
            }
        }
        return true;
    }
}
