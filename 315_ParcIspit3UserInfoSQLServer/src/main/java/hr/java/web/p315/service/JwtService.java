package hr.java.web.p315.service;

import io.jsonwebtoken.Claims; // Za dohvat podataka iz JWT tokena
import io.jsonwebtoken.Jwts; // Klasa za rad s JWT
import io.jsonwebtoken.SignatureAlgorithm; // Algoritam potpisa
import io.jsonwebtoken.security.Keys; // Generiranje ključa
import org.springframework.security.core.userdetails.UserDetails; // Sučelje koje predstavlja korisnika
import org.springframework.stereotype.Service; // Registrira kao Spring servis

import java.security.Key; // Tip za ključ
import java.util.Date; // Datum za expiry
import java.util.HashMap; // Za dodatne claimove
import java.util.Map; // Mapu claimova
import java.util.function.Function; // Za dohvat podataka iz claimova

@Service // Označava da je ovo Spring servis
public class JwtService {

    // Tajni ključ za potpisivanje i validaciju tokena
    // Preporučuje se generirati duži ključ (barem 256-bit) za produkciju
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Dohvaća username (subject) iz tokena
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Dohvaća datum isteka tokena
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generička metoda za dohvat podataka iz claimova
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Dohvati sve claimove
        return claimsResolver.apply(claims); // Primijeni funkciju za dohvat traženog podatka
    }

    // Metoda koja generira JWT token za zadano korisničko ime
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // Ovdje možeš dodati custom podatke ako želiš
        return createToken(claims, username);
    }

    // Metoda koja provjerava je li token važeći za korisnika
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Dohvati username iz tokena
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Provjerava je li token istekao
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kreira JWT token s claimovima, subjectom i datumom isteka
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationMillis = 1000 * 60 * 60 * 24; // Token vrijedi 24 sata
        return Jwts.builder()
                .setClaims(claims) // Postavi dodatne claimove ako ih ima
                .setSubject(subject) // Postavi subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Vrijeme generiranja
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Vrijeme isteka
                .signWith(secretKey) // Potpiši token
                .compact(); // Generiraj string token
    }

    // Parsira token i vraća sve claimove
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey) // Postavi ključ za validaciju potpisa
                .build()
                .parseClaimsJws(token) // Parsiraj token
                .getBody(); // Dohvati tijelo s claimovima
    }
}


/*

✅ Gdje se koristi JwtService:
Koristi ga JwtAuthFilter i AuthController za:
generiranje JWT tokena prilikom prijave korisnika
dohvat username-a iz tokena
provjeru ispravnosti tokena

✅ Što radi:
1️⃣ generateToken(username) – generira JWT token za korisnika s datumom isteka (npr. 24 sata).
2️⃣ extractUsername(token) – dohvaća korisničko ime iz JWT tokena (subject).
3️⃣ validateToken(token, userDetails) – provjerava je li token ispravan za korisnika i nije li istekao.
4️⃣ Interno koristi io.jsonwebtoken (jjwt) za rad s JWT tokenima.

✅ Ključ (secretKey):
Trenutno se generira automatski za razvoj.
U produkciji ga spremi u application.properties ili application.yml kao jwt.secret kako bi bio postojan
i možeš ga povući preko @Value ili Environment.

✅ Za što ti omogućuje:
Potpun JWT tok za login + autorizaciju bez state sessiona.
Kontroleri mogu koristiti @PreAuthorize("hasRole('ADMIN')") i slične mehanizme kontrole pristupa temeljem JWT.
Eliminira potrebu za ručnim provjerama jer filter automatski postavlja korisnika u SecurityContext.




Upisem u POST metodu:
http://localhost:8083/auth/api/v1/login
U JSON upisem:
{
   "username": "admin",
   "password": "admin"
}

Dobijem:
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1MzEyNDQzNSwiZXhwIjoxNzUzMTMwNDM1fQ.1tFb99dI9Wx3a2P5beZ9w9lQSAMvi7WjCASGH-Jf-xM",
  "token": "5baad674-a2c7-4344-b2e6-68ac05252d2c"
}

Odakle se tokeni ustvari izgeneriraju, gdje je taj kod ili fajla iz kojeg se generiraju?

Evo jasnog, praktičnog pojašnjenja:
1️⃣ Odakle dolaze accessToken i token koje dobiješ?

access Token
Ovo je JWT token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1MzEyNDQzNSwiZXhwIjoxNzUzMTMwNDM1fQ.1tFb99dI9Wx3a2P5beZ9w9lQSAMvi7WjCASGH-Jf-xM
Generira se u tvojoj Spring Boot aplikaciji pomoću JWT biblioteke (npr. io.jsonwebtoken.Jwts ili java-jwt).
Token se obično generira u servisu koji se bavi autentikacijom, primjerice JwtService, JwtUtils,
ili unutar AuthenticationService.

Ovo je UUID token:  5baad674-a2c7-4344-b2e6-68ac05252d2c

Generira se metodom:
UUID.randomUUID().toString();
Koristi se kao dodatni session token ili refresh token, ovisno o tvojoj implementaciji.

2️⃣ Gdje je u tvojoj aplikaciji taj kod?
🔹 Traži klase s nazivima:
JwtService
JwtUtil
JwtTokenProvider
AuthenticationService
ili AuthController

U njima ćeš naći metodu sličnu:
public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
}
ili
String token = UUID.randomUUID().toString();

3️⃣ Koja klasa poziva generiranje?
🔹 Kada zoveš:
POST http://localhost:8083/auth/api/v1/login
Spring Security autentificira korisnika, a zatim:
Tvoj AuthController ili AuthenticationController:
provjeri korisnika,
pozove JwtService.generateToken(user),
vrati ti JSON s accessToken i token.

4️⃣ Kako pronaći točno u svom projektu?
U IntelliJ trazilicu Find in Files (Ctrl+Shift+F) upiši:
generateToken ili Jwts.builder
i odmah ćeš naći gdje se token generira.

✅ Zaključak:
✔️ Tokeni se generiraju u tvojoj aplikaciji automatski prilikom autentikacije,
koristeći JWT i UUID, unutar Auth kontrolera ili Auth servisa.
✔️ JWT se koristi za autorizaciju, UUID opcionalno za refresh/session tracking.

*/