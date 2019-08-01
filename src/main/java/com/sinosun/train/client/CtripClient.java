package com.sinosun.train.client;

import com.alibaba.fastjson.JSONObject;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;

/**
 * Created on 2019/7/31 15:46.
 *
 * @author caogu
 */
public class CtripClient {
    private static Logger logger = LoggerFactory.getLogger(CtripClient.class);

    private static final String GET_TICKET_PRICE_URL = "https://m.ctrip.com/restapi/soa2/14666/json/GetBookingByStationV3?" +
            "_fxpcqlniredt=09031010110784000474";

    public void getTicketPriceFromCtrip(JSONObject req, RoutingContext routingContext) {
        logger.info("getTicketPrice HTTP request  with data : " + req);
        HttpClient.getHttpClient().postAbs(GET_TICKET_PRICE_URL).sendJson(req, resp -> {
            if (resp.succeeded()) {
                HttpResponse<Buffer> response = resp.result();
                JsonObject ret = response.bodyAsJsonObject();
                logger.info("getTicketPrice HTTP response with body : " + ret);

                JsonObject responseStatus = ret.getJsonObject("ResponseStatus");
                if (!"Success".equals(responseStatus.getString("Ack"))) {
                    logger.error("get ticket price from ctrip failed : " + responseStatus.toString());
                    routingContext.response().setStatusCode(400).end();
                } else {
                    JsonObject responseBody = ret.getJsonObject("ResponseBody");
                    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                            .end(responseBody.toString());
                }
            } else {
                logger.error("HTTP error : " + resp.cause());
            }
        });
    }

}
