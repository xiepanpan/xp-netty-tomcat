package com.xiepanpan.catalina.http;

import com.sun.xml.internal.ws.api.pipe.ContentType;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author: xiepanpan
 * @Date: 2020/8/14 0014
 * @Description:
 */
public class XpResponse {

    private ChannelHandlerContext context;
    private HttpRequest request;
    private static Map<Integer, HttpResponseStatus> statusMap = new HashMap<Integer, HttpResponseStatus>();

    static {
        statusMap.put(200,HttpResponseStatus.OK);
        statusMap.put(404,HttpResponseStatus.NOT_FOUND);
    }

    public XpResponse(ChannelHandlerContext context, HttpRequest request) {
        this.context = context;
        this.request = request;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void write(String outString,Integer status) {
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    statusMap.get(status),
                    Unpooled.wrappedBuffer(outString.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE,"text/json");
            response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
            response.headers().set(EXPIRES,0);
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            context.write(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            context.flush();
        }
    }
}