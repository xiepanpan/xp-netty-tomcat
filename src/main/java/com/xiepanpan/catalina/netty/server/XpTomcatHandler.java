package com.xiepanpan.catalina.netty.server;

import com.xiepanpan.catalina.http.XpRequest;
import com.xiepanpan.catalina.http.XpResponse;
import com.xiepanpan.catalina.http.XpServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @author: xiepanpan
 * @Date: 2020/8/14 0014
 * @Description:
 */
public class XpTomcatHandler extends ChannelInboundHandlerAdapter {

    private Logger LOG = Logger.getLogger(XpTomcatHandler.class);

    private static final Map<Pattern,Class<?>> servletMapping = new HashMap<Pattern, Class<?>>();

    static {
        Cous
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            XpRequest request = new XpRequest(ctx,httpRequest);
            XpResponse response = new XpResponse(ctx,httpRequest);
            String uri = request.getUri();
            String method = request.getMethod();
            LOG.info(String.format("URI :%s method %s",uri,method));

            boolean hasPattern = false;

            for (Map.Entry<Pattern,Class<?>> entry: servletMapping.entrySet()) {
                if (entry.getKey().matcher(uri).matches()) {
                    XpServlet xpServlet = (XpServlet) entry.getValue().newInstance();
                    if ("get".equalsIgnoreCase(method)){
                        xpServlet.doGet(request,response);
                    }else {
                        xpServlet.doPost(request,response);
                    }
                    hasPattern = true;
                }
            }
            if (!hasPattern) {
                //不匹配 方法找不到
                String out = String.format("404 NotFound URL%s for method %s",uri,method);
                response.write(out,404);
                return;
            }

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.flush();
    }
}