package test.hastTest;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GenerateSecreteKey {

    public static void main(String[] args){
        //declare class
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        final GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

        //generate secrete key
        String secretKey = googleAuthenticatorKey.getKey();
        System.out.println(secretKey);
    }

}
