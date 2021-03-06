package cn.edu.sicau.web.servlet;

import cn.edu.sicau.Service.UserService;
import cn.edu.sicau.domain.User;
import cn.edu.sicau.exception.UserException;
import cn.edu.sicau.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdatePwdServlet",urlPatterns = "/UpdatePwdServlet")
public class UpdatePwdServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");//设置Post接受数据的编码为utf-8
        response.setContentType("text/html;charset=utf-8");//设置Post响应数据的编码为utf-8

        //依赖UserService
        UserService userService=new UserService();

        /**
         * "requestCode"、"requestParam"是和客户端约定好的请求体字段名称
         * "requestParam"代表参数，并且是json封装的数据
         */
        BufferedReader reader=request.getReader();
        String JsonParameters=reader.readLine();
        JSONObject userParam= JSON.parseObject(JsonParameters).getJSONObject("requestParam");

        User user=new User();
        user.setAccount((String) userParam.get("account"));
        user.setPassword((String) userParam.get("pwd"));

        Map<String,Object> resp=new HashMap<String,Object>();
        try {

            userService.updatePassword(user);

            //密码修改成功，返回给客户端信息resCode：103、resMsg：密码修改成功
            resp.put("resCode","100");
            resp.put("resMsg","密码修改成功");
            //将返回数据封装成json字符串
            String result= JsonUtil.toJson(resp);
            //返回给客户端

            PrintWriter out = response.getWriter();
            out.print(result);
            out.flush();

        } catch (UserException e) {


            //密码修改失败，返回给客户端信息resCode：202、resMsg：账号不存在
            resp.put("resCode","202");
            resp.put("resMsg",e.getMessage());
            //将返回数据封装成json字符串
            String result=JsonUtil.toJson(resp);
            //返回给客户端
            //response.getWriter().print(result);

            PrintWriter out = response.getWriter();
            out.print(result);
            out.flush();
        }

    }
}
