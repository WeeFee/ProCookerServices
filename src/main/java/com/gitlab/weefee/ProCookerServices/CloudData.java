/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;
import express.utils.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CloudData {
    /**
     *
     * @param req
     * @param res
     */
    public static void getUserData(Request req, Response res) {
        if (req.getParam("playerID") == null) {
            res.sendStatus(Status._401);
            return;
        }

        if (Main.database.keyExistsDisk("players", req.getParam("playerID"))) {
            StringBuilder playerData = new StringBuilder();
            for (String entry: Main.database.readFromDatabase("players", req.getParam("playerID"))) {
                playerData.append(entry);
                playerData.append(",");
            }
            res.send(playerData.toString());
        } else {
            res.sendStatus(Status._204);
        }
    }

    /**
     *
     * @param req
     * @param res
     */
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

        if (Main.database.writeToDatabase("players", req.getParam("playerID"), reqBody.split(","))) {
            res.sendStatus(Status._200);
            Main.database.flushDatabase();
        } else {
            res.sendStatus(Status._500);
        }
    }
}
