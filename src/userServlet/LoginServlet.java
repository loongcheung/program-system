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
 * Created by zhanglongyu on 2017/2/16.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String pwd = req.getParameter("password");
        PrintWriter out = resp.getWriter();
        querySQL query = new querySQL();
        query.connSQL();
        String select = "select * from user where username = ?";
        ResultSet rs = query.selectSQL(select,username);
        try {
            if (rs.next()) {
                System.out.println(rs.getString("password"));
                if (rs.getString("password").equals(pwd)) {
                    //登陆成功
                    out.print(1);
                } else {
                    out.print(-1);
                }
            } else {
                //不存在此用户
                out.print(0);
            }
        } catch (SQLException se) {
            System.out.println("显示时数据库出错。");
            se.printStackTrace();
        }
    }
}
