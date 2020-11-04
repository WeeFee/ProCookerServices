/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Database {
    private String databaseURL = "";
    /**
     *
     * @param inputDatabaseURL
     */
    public Database(String inputDatabaseURL) {
        databaseURL = inputDatabaseURL;
        this.collectDatabase();
    }

    /**
     *
     */
    private ArrayList<ArrayList<ArrayList<String>>> databaseMemory = null;

    /**
     *
     */
    private ArrayList<ArrayList<String>> databaseRef = null;
    
    /**
     *
     * @param collection
     * @param key
     * @return
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
     *
     * @param collection
     * @param key
     * @param content
     * @return
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
     *
     * @param collection
     * @param key
     * @return
     */
    public String readFromDatabase(String collection, String key) {
        int[] pos = this.findKey(collection, key);
        return databaseMemory.get(pos[0]).get(pos[1]).get(0);
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
            int[] pos = this.findKey(collection, key);
            databaseMemory.get(pos[0]).get(pos[1]).set(0, content);
            return true;
        } catch (NullPointerException e) {
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

    /**
     *
     * @param collection
     * @param key
     * @return
     */
    public int[] findKey(String collection, String key) {
        int collectionPos = -1;
        for (int i = 0; i < databaseRef.toArray().length; i++) {
            if (databaseRef.get(i).get(0).equals(collection)) {
                collectionPos = i;
            }
        }
        int keyPos = databaseRef.get(collectionPos).indexOf(key) - 1;
        return new int[]{collectionPos, keyPos};
    }

    /**
     *
     * @return
     */
    private boolean collectDatabase() {
        File[] diskCollections = new File(databaseURL).listFiles(File::isDirectory);
        assert diskCollections != null;

        databaseMemory = new ArrayList<ArrayList<ArrayList<String>>>((int) Arrays.stream(diskCollections).count());

        databaseRef = new ArrayList<ArrayList<String>>((int) Arrays.stream(diskCollections).count());

        for (int i = 0; i < diskCollections.length; i++) {
            File[] diskCollectionKeys = new File(databaseURL + diskCollections[i].getName()).listFiles();
            assert diskCollectionKeys != null;

            databaseMemory.add(new ArrayList<ArrayList<String>>((int) Arrays.stream(diskCollectionKeys).count()));

            databaseRef.add(new ArrayList<String>((int) Arrays.stream(diskCollectionKeys).count() + 1));
            databaseRef.get(i).add(diskCollections[i].getName());

            for (int x = 0; x < diskCollectionKeys.length; x++) {
                databaseMemory.get(i).add(new ArrayList<String>(1));
                databaseMemory.get(i).get(x).add(readFromDiskDatabase(diskCollections[i].getName(), diskCollectionKeys[x].getName()));

                databaseRef.get(i).add(diskCollectionKeys[x].getName());
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean flushDatabase() {
        for (ArrayList<String> strings : databaseRef) {
            for (int x = 0; x < strings.size(); x++) {
                if (x == 0) {
                    continue;
                }
                String content = readFromDatabase(strings.get(0), strings.get(x));
                if (writeToDiskDatabase(strings.get(0), strings.get(x), content)) {
                    return true;
                }
            }
        }
        return false;
    }
}
