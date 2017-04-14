package userServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.querySQL;

/**
 * Created by zhanglongyu on 2017/2/17.
 */
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String pwd = req.getParameter("password");
        String email = req.getParameter("email");
        System.out.println(username + ":" + pwd);
        PrintWriter out = resp.getWriter();
//注册页面还要查询有没有重复
        querySQL mysql = new querySQL();
        mysql.connSQL();
        String insert = "insert into user(username,password,email) values(?,?,?)";
        String select = "select * from user where username = ?";
        ResultSet rs = mysql.selectSQL(select, username);
        try {
            if (rs.next()) {
                //存在此用户，不能注册
                out.print(-1);
            } else {
                //不存在此用户，可注册
                boolean flag = mysql.insertSQL(insert,username,pwd,email);
                if (flag) {
                    out.print(1);
                } else {
                    out.print(0);
                }
            }
        } catch (SQLException se) {
            System.out.println("显示时数据库出错。");
            se.printStackTrace();
        }
        mysql.deconnSQL();
    }
}
