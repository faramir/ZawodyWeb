
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public class ZawodyWebPassword {

    public static String hashPass(String login, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update((login + "+" + password).getBytes());
            byte[] digest = md.digest();
            org.apache.commons.codec.binary.Hex hex = new Hex();
            return new String(hex.encode(digest));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String login;
        String password;
        while (scanner.hasNext()) {
            login = scanner.next();
            password = scanner.next();
            System.out.println(login + "\t" + password + "\t->\t" + hashPass(login, password));
        }
    }
}
