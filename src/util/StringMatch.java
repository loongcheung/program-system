package util;

import java.util.regex.*;

/**
 * Created by zhanglongyu on 2017/2/27.
 */
public class StringMatch {

    private String regex = "",input = "",regexError = "";
    private Pattern pattern;
    private Matcher matcher;

    public StringMatch () {

    }

    public StringMatch(String regex,String input) {
        setRegex(regex);
        setInput(input);
    }

    //初始化，如果语法错误则抛出异常
    public boolean initialize () {
        try {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(input);
            return true;
        }
        catch (PatternSyntaxException e) {
            regexError = "\n正则表达式语法错误！错误的相关信息如下："+
                    "\n当前的正则表达式是:"+e.getPattern()+
                    "\n错误信息："+e.getMessage();
            return false;
        }
    }

    //根据正则表达式来匹配输入的整个文本
    public boolean validateEntireText () {
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    //根据正则表达式来匹配输入的部分文本
    public boolean validatePartText () {
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public String getRegexError () {
        return this.regexError;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setInput (String input) {
        this.input = input;
    }
}
