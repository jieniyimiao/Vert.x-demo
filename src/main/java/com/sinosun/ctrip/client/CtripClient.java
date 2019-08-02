package com.sinosun.ctrip.client;

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
        HttpClient.getHttpClient()
                .postAbs(GET_TICKET_PRICE_URL)
                .timeout(5000)
                .putHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .putHeader("origin", "https://m.ctrip.com")
                .putHeader("referer", "https://m.ctrip.com/webapp/train/?secondwakeup=true&dpclickjump=true&from=https%3A%2F%2Fm.ctrip.com%2Fhtml5%2F")
                .sendJson(req, resp -> {
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
                        routingContext.response().setStatusCode(400).end();
                    }
                });
    }

}
