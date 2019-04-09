package nl.bioinf.jp_kcd_wr.image_library.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * generates a password to be used for testing datasource authentication
 * the testpasswords used now are:
 * Tom:iets123 and piet:password
 */

public class PasswordEncryptor {
    public static void main(String[] args) {
        String password = "password";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println(password + " = " + hashedPassword);
    }
}
