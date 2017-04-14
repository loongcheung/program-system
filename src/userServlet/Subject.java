package userServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.querySQL;
import util.StringMatch;

/**
 * Created by zhanglongyu on 2017/2/18.
 */
public class Subject extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain;charset=UTG-8");
        resp.setCharacterEncoding("UTF-8");
        querySQL query = new querySQL();
        query.connSQL();
        currentTopic(query, out);
        query.deconnSQL();
    }

    //发送当前题目
    private static void currentTopic(querySQL query, PrintWriter out) {
        String sql = "select * from subject where name = ?";
        String subject = "矩阵转置";
        ResultSet rs = query.selectSQL(sql, subject);
        try {
            while (rs.next()) {
                String result = "{'name':" + "'" + rs.getString("name") + "'" + ",'time':" + "'" + rs.getInt("time") + "'"
                        + ",'space':" + "'" + rs.getInt("space") + "'" + ",'popindex':" + "'" + rs.getString("description") + "'"
                        + ",'iptdes':" + "'" + rs.getString("iptdes") + "'" + ",'optdes':" + "'" + rs.getString("optdes") + "'"
                        + ",'iptexp':" + "'" + rs.getString("iptexp") + "'" + ",'optexp':" + "'" + rs.getString("optexp") + "'" + "}";
                out.print(result);
            }
        } catch (SQLException se) {
            System.out.println("显示时数据库出错。");
            se.printStackTrace();
        }
    }

    //发送题目列表
    private static void listTopic(querySQL query, PrintWriter out) {

    }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String answer = req.getParameter("answer");
        querySQL query = new querySQL();
        query.connSQL();
        PrintWriter out = resp.getWriter();
        //自动阅卷顺序为：编译，语法分析，要点分析，总分统计

        try {//所有的有sql操作的都在这个中执行
            compile(answer,query);
        }catch (SQLException e){
            System.out.println("显示时数据库出错。");
            e.printStackTrace();
        }
        syntax(out);//返回语法得分

        query.deconnSQL();
    }

    //答案编译
    private static int compile(String answer,querySQL query) throws IOException,SQLException{
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./Main.java")));
        writer.write(answer);
        writer.close();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        FileOutputStream err = new FileOutputStream("./err.txt");
        int result = compiler.run(null, null, err, "./Main.java");

        System.out.println((result == 0) ? "compile success" : "compile err");

        Runtime run = Runtime.getRuntime();
        Process p = run.exec("java Main");

        File file = new File("Main.class");
        if (file.exists() && (result != 0)) {
            file.delete();
        }

        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String s = br.readLine();

        System.out.println("args = [" + s + "]");
        //查询数据库的正确结果与编译成功的结果对比
        String  select = "selet answer from regexp where name=?";
        String name = "矩阵转置";
        ResultSet rs = query.selectSQL(select,name);
        String tureAnswer = null;
        while (rs.next()) {
            tureAnswer = rs.getString("answer");
        }
        int answerScore = 0;
        if (s.equals(tureAnswer)) {//结果正确在总分中加入结果分
            System.out.println("结果正确");
            answerScore = 2;//结果正确得分
        }
        return answerScore;
    }

    //语法分析
    private static float syntax(PrintWriter out) throws IOException {
        //获取代码总行数
        BufferedReader in = new BufferedReader(new FileReader("./Main.java"));
        int codeline = 0;//代码总行数
        String lineValue = in.readLine();
        String regex = "\\s*\\{?\\s*|\\s*\\}*\\s*";
        Pattern pattern;
        Matcher matcher;
        while (lineValue != null) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(lineValue);
            if (!matcher.matches()) {
                codeline++;
            }
            lineValue = in.readLine();
        }
        //获取错误代码行数
        BufferedReader errIn = new BufferedReader(new FileReader("./err.txt"));
        int errTotal;//最后一行错误的总数
        int countLine = 0;
        String errlineValue[] = new String[100];
        while (errIn.readLine() != null) {
            //errLineValue存放所有的错误信息
            errlineValue[codeline] = errIn.readLine();
            countLine++;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= codeline - 1; i++) {
            sb.append(errlineValue[i]);
        }

        String errAlert = sb.toString();//返回去用户的错误提示信息
        out.print(errAlert);

        String errLine = errlineValue[codeline - 1].substring(0, 1);
        errTotal = Integer.parseInt(errLine);

        //计算语法得分
        int sytax = 3;
        float sytaxScore = 0;
        sytaxScore = (1 - errTotal / codeline) * sytax;//语法分析总分

        return sytaxScore;
    }

    //要点分析
    private static float essentials(querySQL query) throws SQLException, IOException {
        //获取数据库中正则表的匹配要点，使用StringMatch类匹配要点
        String select = "select * from regexp where name = ?";
        String regName = "矩阵转置";
        String regexp[] = new String[20];
        float essentialScore=0;
        int essentials[] = new int[20];//每个要点的分数
        int essnetialsPart[] = new int[20];
        int i = 0, f = 0;
        ResultSet rs = query.selectSQL(select, regName);
        while (rs.next()) {
            for (; i <= 20; i++) {//获取题目的所有正则
                if (rs.getString("regexp" + i) != null) {
                    f++;
                    regexp[i] = rs.getString("regexp" + i);
                    essentials[i] = rs.getInt("score" + i);
                }
                break;
            }
        }
        BufferedReader in = new BufferedReader(new FileReader("./Main.java"));
        String input = in.readLine();

        //正则匹配
        for (int j = 0; j <= f; j++) {
            StringMatch sm = new StringMatch(regexp[j], input);
            while (input != null) {
                if (sm.initialize()) {
                    if (sm.validateEntireText()) {
                        essnetialsPart [j] = essentials [j];
                    }else {
                        essnetialsPart [j] = 0;
                    }
                }
                input = in.readLine();
            }
        }
        for (int t=0;t<=f;t++){
            essentialScore += essnetialsPart[t];//要点分析总分
        }

        return  essentialScore;
    }
}
