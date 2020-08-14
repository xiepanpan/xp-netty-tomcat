package com.xiepanpan.catalina.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author: xiepanpan
 * @Date: 2020/8/14 0014
 * @Description:
 */
public class XpRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest request;

    public XpRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        return queryStringDecoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> params = getParameters();
        List<String> param = params.get(name);
        if (param!= null) {
            return param.get(0);
        } else {
            return null;
        }
    }

    public String getUri() {
        return request.getUri();
    }

    public String getMethod() {
        return request.getMethod().name();
    }
}
