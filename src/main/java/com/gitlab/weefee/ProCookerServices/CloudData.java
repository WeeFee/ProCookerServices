package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;
import express.utils.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CloudData {
    public static void getUserData(Request req, Response res) {
        if (req.getParam("playerID") == null) {
            res.sendStatus(Status._401);
            return;
        }

        File userObj = new File("./db/players/" + req.getParam("playerID"));
        if (userObj.exists()) {
            StringBuilder userProfile = new StringBuilder();
            try {
                Scanner userReader = new Scanner(userObj);
                while (userReader.hasNextLine()) {
                    userProfile.append(userReader.nextLine());
                }
                userReader.close();
                res.send(userProfile.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                res.sendStatus(Status._500);
            }
        } else {
            res.sendStatus(Status._204);
        }
    }

    public static void postUserData(Request req, Response res) {
        String reqBody = new BufferedReader(new InputStreamReader(req.getBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        if (!(reqBody.split(",").length == 10)) {
            res.sendStatus(Status._400);
            return;
        } else if (req.getParam("playerID") == null) {
            res.sendStatus(Status._401);
            return;
        }  else if (!reqBody.split(",")[1].equals(req.getParam("playerID"))) {
            res.sendStatus(Status._403);
            return;
        }

        try {
            FileWriter userWriter = new FileWriter("./db/pl ayers/" + req.getParam("playerID"));
            userWriter.write(reqBody);
            userWriter.close();
            res.sendStatus(Status._200);
        } catch (IOException e) {
            e.printStackTrace();
            res.sendStatus(Status._500);
        }
    }
}
