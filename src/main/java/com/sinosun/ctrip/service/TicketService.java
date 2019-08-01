package com.sinosun.ctrip.service;

import com.alibaba.fastjson.JSONObject;
import com.sinosun.ctrip.client.CtripClient;
import com.sinosun.ctrip.dto.GetTicketPriceReq;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.joda.time.LocalDateTime;

/**
 * Created on 2019/8/1 14:35.
 *
 * @author caogu
 */
public class TicketService {
    private static Logger logger = LoggerFactory.getLogger(TicketService.class);

    public void handleGetTicketPrice(RoutingContext routingContext) {
        JsonObject reqDate = routingContext.getBodyAsJson();
        HttpServerResponse response = routingContext.response();
        if (reqDate == null) {
            sendError(400, response);
        } else {
            GetTicketPriceReq data = buildGetTicketPriceReq(reqDate);
            data.validate();
            new CtripClient().getTicketPriceFromCtrip(JSONObject.parseObject(JSONObject.toJSONString(data)), routingContext);
        }
    }

    private GetTicketPriceReq buildGetTicketPriceReq(JsonObject reqDate) {
        GetTicketPriceReq getTicketPriceReq = new GetTicketPriceReq();
        getTicketPriceReq.setDepartStation(reqDate.getString("DepartStation"));
        getTicketPriceReq.setArriveStation(reqDate.getString("ArriveStation"));
        getTicketPriceReq.setDepartDate(LocalDateTime.parse(reqDate.getString("DepartDate")).toDate());
        getTicketPriceReq.setStudent(reqDate.getBoolean("Student", false));
        return getTicketPriceReq;
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
