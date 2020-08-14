package com.xiepanpan.catalina.servlets;

import com.xiepanpan.catalina.http.XpRequest;
import com.xiepanpan.catalina.http.XpResponse;
import com.xiepanpan.catalina.http.XpServlet;

/**
 * @author: xiepanpan
 * @Date: 2020/8/14 0014
 * @Description:
 */
public class FirstServlet extends XpServlet {
    @Override
    public void doGet(XpRequest request, XpResponse response) {
        doPost(request,response);
    }

    @Override
    public void doPost(XpRequest request, XpResponse response) {
        String param = "name";
        String str = request.getParameter(param);
        response.write(param+":"+str,200);
    }
}