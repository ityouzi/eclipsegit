package cn.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.weixin4j.WeixinException;
import org.weixin4j.spi.HandlerFactory;
import org.weixin4j.spi.IMessageHandler;
import org.weixin4j.util.TokenUtil;

import cn.wx.utils.SignUtil;

/**
 * 请求处理的核心类.
 * @author zrxJuly
 * @createDate 2017-12-9
 * @since TODO: 1.0
 *
 */
public class CoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
     * 请求校验(确认请求来自微信服务器).
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("=======开始请求校验======");

        // 微信加密签名.
        String signature = request.getParameter("signature");
        System.out.println("signature====" + signature);
        // 时间戳.
        String timestamp = request.getParameter("timestamp");
        System.out.println("timestamp====" + timestamp);
        // 随机数.
        String nonce = request.getParameter("nonce");
        System.out.println("nonce====" + nonce);
        // 随机字符串.
        String echostr = request.getParameter("echostr");
        System.out.println("echostr====" + echostr);

        PrintWriter out = response.getWriter();

        // 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败.
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            System.out.println("=======请求校验成功======" + echostr);
            out.print(echostr);
        }

        out.close();
        out = null;
    }
    
    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //消息来源可靠性验证
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");       // 随机数
        //Token为weixin4j.properties中配置的Token
        String token = TokenUtil.get();
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (!TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            //消息不可靠，直接返回
            response.getWriter().write("");
            return;
        }
        //用户每次向公众号发送消息、或者产生自定义菜单点击事件时，响应URL将得到推送
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            //获取POST流
            ServletInputStream in = request.getInputStream();
            //非注解方式，依然采用消息处理工厂模式调用
            IMessageHandler messageHandler = HandlerFactory.getMessageHandler();
            //处理输入消息，返回结果
            String xml = messageHandler.invoke(in);
            System.out.println(xml);
            //返回结果
            response.getWriter().write(xml);
        } catch (IOException | WeixinException ex) {
            response.getWriter().write("");
        }
    }

}