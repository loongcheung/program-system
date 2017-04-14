
import javax.tools.JavaCompiler;

import javax.tools.ToolProvider;
import java.io.*;

/**
 * Created by zhanglongyu on 2017/2/24.
 */
public class Test {
    public static void main(String[] args) {
        try {
            String content = "public class aa\n" +
                    "{\n" +
                    "public static void main(String[] args)\n" +
                    "{\n" +
                    "int a = 1;int b = 1; int sum = 0;\n" +
                    "sum = a+b\n" +
                    "System.out.println(\"1+1=\"+sum);\n" +
                    "}\n" +
                    "}";
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("./aa.java")));
            writer.write(content);
            writer.close();

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            FileOutputStream err = new FileOutputStream("./err.txt");
            int result = compiler.run(null, null, err, "./aa.java");

            System.out.println((result == 0) ? "compile success" : "compile err");

            Runtime run = Runtime.getRuntime();
            Process p = run.exec("java aa");

            File file = new File("aa.class");
            if (file.exists()&&(result!=0)) {
                file.delete();
            }

            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String s = br.readLine();

            System.out.println("args = [" + s + "]");
          /*  while (s.equals("2")) {
                System.out.println("满分");
            }*/
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
