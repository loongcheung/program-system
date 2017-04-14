package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by zhanglongyu on 2017/2/16.
 */
public class querySQL {
    Connection conn = null;
    PreparedStatement statement = null;

    // connect to MySQL
    public void connSQL() {
        String url = "jdbc:mysql://localhost:3306/program";
        String username = "root";
        String password = "123456";

        try {
            Class.forName("com.mysql.jdbc.Driver" );
            System.out.println("连接数据库");
            conn = DriverManager.getConnection( url,username, password );
        }
        //捕获加载驱动程序异常
        catch ( ClassNotFoundException cnfex ) {
            System.err.println(
                    "装载 JDBC/ODBC 驱动程序失败." );
            cnfex.printStackTrace();
        }
        //捕获连接数据库异常
        catch ( SQLException sqlex ) {
            System.err.println( "无法连接数据库" );
            sqlex.printStackTrace();
        }
    }

    // disconnect to MySQL
    public void deconnSQL() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            System.out.println("关闭数据库问题 ：");
            e.printStackTrace();
        }
    }

    // execute selection language
    public ResultSet selectSQL(String sql,String name) {
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1,name);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    // execute insertion language
    public boolean insertSQL(String sql,String username,String password,String eamil) {
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1,username);
            statement.setString(2,password);
            statement.setString(3,eamil);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //execute delete language
    public boolean deleteSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //execute update language
    public boolean updateSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }

    // show data in ju_users
    /*public void layoutStyle2(ResultSet rs) {
        System.out.println("-----------------");
        System.out.println("执行结果如下所示:");
        System.out.println("-----------------");
        System.out.println(" 用户ID" + "/t/t" + "淘宝ID" + "/t/t" + "用户名"+ "/t/t" + "密码");
        System.out.println("-----------------");
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("ju_userID") + "/t/t"
                        + rs.getString("taobaoID") + "/t/t"
                        + rs.getString("ju_userName")
                        + "/t/t"+ rs.getString("ju_userPWD"));
            }
        } catch (SQLException e) {
            System.out.println("显示时数据库出错。");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("显示出错。");
            e.printStackTrace();
        }
    }*/

    /*public static void main(String args[]) {

        helloworld h = new helloworld();
        h.connSQL();
        String s = "select * from ju_users";

        String insert = "insert into ju_users(ju_userID,TaobaoID,ju_userName,ju_userPWD) values("+8329+","+34243+",'mm','789')";
        String update = "update ju_users set ju_userPWD =123 where ju_userName= 'mm'";
        String delete = "delete from ju_users where ju_userName= 'mm'";

        if (h.insertSQL(insert) == true) {
            System.out.println("insert successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }
        if (h.updateSQL(update) == true) {
            System.out.println("update successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }
        if (h.insertSQL(delete) == true) {
            System.out.println("delete successfully");
            ResultSet resultSet = h.selectSQL(s);
            h.layoutStyle2(resultSet);
        }

        h.deconnSQL();
    }*/
}