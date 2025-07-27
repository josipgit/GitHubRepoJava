package hr.java.web.p315.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptHashValidator {

    public static void main(String[] args) {
//        String storedHash = "$2a$12$h0HcS2QDb/7zPASbLa2GoOTSRP6CWK0oX7pCK.dPjkM6L5N4pNovi"; // user
        String storedHash = "$2a$12$AGa.KBYA84Osz5sZS.uz8eUVetDZMv8JJI73RbpaVGPwpOeQ0R9x.";  // user
        String userPassword = "user"; // lozinka koju provjeravamo

        boolean matches = BCrypt.checkpw(userPassword, storedHash);
        System.out.println("Password matches: " + matches); // true ako je "user"

//        String hash1 = "$2a$12$h0HcS2QDb/7zPASbLa2GoOTSRP6CWK0oX7pCK.dPjkM6L5N4pNovi";  // user
//        String hash2 = "$2a$12$AGa.KBYA84Osz5sZS.uz8eUVetDZMv8JJI73RbpaVGPwpOeQ0R9x.";  // user

        // generiranje hash lozinke iz tekstualne (ciste, nekodirane, originalne) lozinke napisane na latinici
        String hash1 = BCrypt.hashpw("user", BCrypt.gensalt(12));  // user
        String hash2 = BCrypt.hashpw("user", BCrypt.gensalt(12));  // user

        System.out.println("Password matches: " + BCrypt.checkpw("user", hash1)); // true
        System.out.println("Password matches: " + BCrypt.checkpw("user", hash2)); // true
    }
}
