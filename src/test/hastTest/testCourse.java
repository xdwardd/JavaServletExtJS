package test.hastTest;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

public class testCourse {
   /*
    public static void main(String[] args){
        if (Math.random() < 0.5){
            System.out.println("Heads");
        }else{
            System.out.println("Tails");
        }
    }*/


    /* public static void main(String[] args){
         int n = Integer.parseInt(args[0]);
         int i = 0;
         int v = 1;

         while (args.length <= n)

         {
             System.out.println(v);
             i = i = 1;
             v = 2 * v;
         }
     }*/




            public static void main(String[] args) {
                    File f = new File("fgffd");
                    System.out.println("Mime Type of " + f.getName() + " is " +
                            new MimetypesFileTypeMap().getContentType(f));
                    // expected output :
                    // "Mime Type of gumby.gif is image/gif"
                }




}
