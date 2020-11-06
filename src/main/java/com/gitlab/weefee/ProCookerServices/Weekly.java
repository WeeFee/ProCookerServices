/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

public class Weekly {
    /**
     *
     * @param req
     * @param res
     */
    public static void getCurrentWeekly(Request req, Response res) {
        res.send(Main.database.readFromDatabase("info", "weekly").get(0));
    }
}
