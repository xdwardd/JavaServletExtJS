package test.hastTest;


import org.apache.commons.text.StringEscapeUtils;


public class escapeSpecialChar
{



    public static void main(String[] args) {

        String input = "This String contains HTML Special characters requires encoding ñ < and >";
        System.out.println("Input: " + input);
        System.out.println("Conversion using Apache commons StringEscapeUtils: " + StringEscapeUtils.escapeHtml4(input));



    }


}
