package com.sinosun.ctrip.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import com.sinosun.ctrip.client.HttpClient;
import com.sinosun.ctrip.service.TicketService;
import com.sinosun.ctrip.util.NetUtils;
import com.sinosun.ctrip.util.PropertiesUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Properties;

/**
 * Created on 2019/7/31 16:20.
 *
 * @author caogu
 */
public class CtripRest extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(CtripRest.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new CtripRest());
    }

    @Override
    public void start() throws Exception {
        HttpClient.setHttpClient(WebClient.create(vertx));
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/test").handler((RoutingContext routingContext) -> routingContext.response().end("OK!") );
        router.post("/getTicketPrice").handler(new TicketService()::handleGetTicketPrice);

        String filePath = new ClassPathResource("application.properties").getAbsolutePath();
        Properties param = PropertiesUtils.readProperty(filePath);
        String port = param.getProperty("server.port");

        vertx.createHttpServer().requestHandler(router).listen(Integer.valueOf(port), handler -> {
            if (handler.succeeded()) {
                String ip = NetUtils.getLocalAddress();
                logger.info("HTTP server already start domain is : http://" + ip + ":" + port + "/");
            } else {
                logger.error("Failed to listen on port 8080");
            }
        });
    }
}