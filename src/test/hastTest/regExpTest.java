package test.hastTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regExpTest {

    public static void main (String[] args){

        Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})");
        Matcher matcher = pattern.matcher("Admin12!@");
        System.out.println(matcher.matches());
    }


}
