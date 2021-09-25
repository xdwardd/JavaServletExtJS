package test.hastTest;

public class PasswordTest {

    public static void main(String[] args) {

        String password = "Admin2!";

        System.out.println("is password valid = "+checkPass(password));
    }

    public static boolean checkPass(String password){
        boolean hasNum = false; boolean hasCap = false; boolean hasLow = false; char c;
        for (int i = 0; i < password.length(); i++){
            c = password.charAt(i);
            if(Character.isDigit(c)){
                hasNum = true;
            }else if (Character.isUpperCase(c)){
                hasCap = true;
            }else if(Character.isLowerCase(c)){
                hasLow = true;
            }else if(hasNum && hasCap && hasLow){
                return true;
            }
        }
        return false;
    }
}
