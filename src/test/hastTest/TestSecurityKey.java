package test.hastTest;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.util.Scanner;

public class TestSecurityKey {
    public static void main (String [] args){
        Scanner scanner = new Scanner(System.in);
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        System.out.println("Enter Your Security Key:");
        int securityKey = scanner.nextInt();

        if(googleAuthenticator.authorize("FON36ZA5OXKZCU3J",securityKey)){
            System.out.println("Security is valid");

        }else {
            System.out.println("Security is not valid");
        }

    }
}


//EPW4VX5KSNJLKSXD