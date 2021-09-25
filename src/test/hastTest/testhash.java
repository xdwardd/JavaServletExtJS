package test.hastTest;

import test.utils.BCrypt;

public class testhash {

    public static void main(String[] args) {

        String pass = "Admin@!2";

        String hash = BCrypt.hashpw(pass, BCrypt.gensalt());


        System.out.println(hash);

        boolean isMyPassCorrect = BCrypt.checkpw(pass, hash);
        System.out.println(isMyPassCorrect);
    }
}
