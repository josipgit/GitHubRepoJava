package hr.java.web.p315.controller;

import hr.java.web.p315.domain.RefreshToken; // Import entiteta za RefreshToken
import hr.java.web.p315.dto.AuthRequestDTO; // Import DTO-a za zahtjev za autentifikaciju
import hr.java.web.p315.dto.JwtResponseDTO; // Import DTO-a za odgovor s JWT-om
import hr.java.web.p315.dto.RefreshTokenRequestDTO; // Import DTO-a za zahtjev za osvježavanje tokena
import hr.java.web.p315.service.JwtService; // Import servisa za generiranje i validaciju JWT
import hr.java.web.p315.service.RefreshTokenService; // Import servisa za upravljanje RefreshToken entitetima
import lombok.AllArgsConstructor; // Generira konstruktor sa svim poljima
import org.springframework.security.authentication.AuthenticationManager; // Upravljac za autentifikaciju
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Klasa za Username/Password token za autentifikaciju
import org.springframework.security.core.Authentication; // Reprezentacija autentifikacijskog objekta
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exception za slučaj da korisnik nije pronađen
import org.springframework.web.bind.annotation.PostMapping; // Anotacija za POST rutu
import org.springframework.web.bind.annotation.RequestBody; // Anotacija za vezanje tijela zahtjeva
import org.springframework.web.bind.annotation.RequestMapping; // Anotacija za bazni path kontrolera
import org.springframework.web.bind.annotation.RestController; // Oznacava REST kontroler

@RestController // Oznacava da je klasa REST kontroler
@RequestMapping("auth") // Bazni path za sve rute u ovom kontroleru je /auth
@AllArgsConstructor // Generira konstruktor sa svim argumentima pomoću Lomboka
public class AuthController { // Kontroler za autentifikaciju i osvježavanje tokena

    private AuthenticationManager authenticationManager; // Za autentifikaciju korisnika

    private JwtService jwtService; // Za generiranje i validaciju JWT tokena

    private RefreshTokenService refreshTokenService; // Za kreiranje i validaciju refresh tokena

    @PostMapping("/api/v1/login") // Endpoint za login
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) { // Metoda prima DTO s korisničkim imenom i lozinkom i vraća JWT + refresh token
        Authentication authentication = authenticationManager.authenticate( // Pokušava autentificirati korisnika
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(), // Dohvaća korisničko ime iz DTO-a
                        authRequestDTO.getPassword()  // Dohvaća lozinku iz DTO-a
                )
        );

        if (authentication.isAuthenticated()) { // Provjerava je li autentifikacija uspješna
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername()); // Kreira refresh token za korisnika
            return JwtResponseDTO.builder() // Graditelj za JWT odgovor
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername())) // Generira access token (JWT)
                    .token(refreshToken.getToken()) // Postavlja osvježavajući token
                    .build(); // Vraća DTO
        } else {
            throw new UsernameNotFoundException("Korisnik nije pronađen ili lozinka nije ispravna."); // Izbacuje iznimku ako autentifikacija nije prošla
        }
    }

    @PostMapping("/api/v1/refreshToken") // Endpoint za osvježavanje tokena
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) { // Metoda prima DTO s refresh tokenom i vraća novi JWT
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken()) // Traži refresh token u bazi
                .map(refreshTokenService::verifyExpiration) // Provjerava je li refresh token istekao
                .map(RefreshToken::getUserInfo) // Dohvaća korisničke podatke povezane s tokenom
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername()); // Generira novi JWT na temelju korisničkog imena
                    return JwtResponseDTO.builder() // Graditelj DTO-a
                            .accessToken(accessToken) // Novi access token
                            .token(refreshTokenRequestDTO.getToken()) // Vraća isti refresh token
                            .build(); // Vraća DTO
                })
                .orElseThrow(() -> new RuntimeException("Refresh token ne postoji u bazi podataka.")); // Ako token nije pronađen, baca iznimku
    }
}



/*
Detaljno objašnjenje klase na hrvatskom
Što je AuthController?
AuthController je REST kontroler koji upravlja autentifikacijom korisnika pomoću JWT tokena i
refresh token mehanizmom u tvojoj Spring Boot Polaznik aplikaciji.

Koje rute pruža?
1️/auth/api/v1/login

Prima:
AuthRequestDTO s korisničkim imenom i lozinkom.

Postupak:
Autentificira korisnika pomoću AuthenticationManager.
Ako je autentifikacija uspješna:
Generira JWT token (access token).
Kreira i vraća refresh token.

Vraća:
JwtResponseDTO s poljima:
accessToken (JWT),
token (refresh token).

Ako korisnik ili lozinka nisu ispravni, baca UsernameNotFoundException.
/auth/api/v1/refreshToken

Prima:
RefreshTokenRequestDTO s postojećim token poljem.

Postupak:
Provjerava postoji li refresh token u bazi.
Provjerava je li refresh token istekao.

Ako je sve ispravno, generira novi access token (JWT).
Vraća:
JwtResponseDTO s poljima:
accessToken (novi JWT),
token (isti refresh token).

Ako refresh token ne postoji, baca RuntimeException.
Zašto je važan ovaj kontroler?
Omogućava sigurnu prijavu korisnika koristeći JWT bez potrebe za održavanjem sesije.
Refresh token mehanizam omogućuje produljenje sesije bez ponovnog logina, čime se korisničko iskustvo
poboljšava uz sigurnost (kratak životni vijek JWT-a).
Priprema backend Polaznik aplikacije za frontend SPA klijente (React, Angular, mobile) koji koriste JWT
za autorizaciju na zaštićenim rutama.

----------------------------
Da, ako gradite aplikaciju koja zahtijeva autentikaciju i autorizaciju, logičan je slijed:
Prvo se korisnik autenticira (npr. login) preko AuthController-a.
Nakon uspješne autentikacije, korisnik dobiva token (npr. JWT) ili se stvara sesija.
Taj token se zatim šalje u svakom zahtjevu (npr. u Authorization headeru).
Zatim pristupa zaštićenim rutama (npr. PolaznikController)
Svaki zahtjev na /polaznici ili slično treba provjeriti valjanost tokena/sesije.
Ako je sve u redu, korisnik može dohvaćati, filtrirati ili mijenjati podatke.

Primjer tijeka:
Login zahtjev → POST /api/auth/login (vraća JWT)
Dohvat polaznika → GET /api/polaznici (s JWT u headeru)

Zašto prvo autentikacija?
Bez autorizacije, PolaznikController bi trebao biti dostupan svima (što je rijetko slučaj u stvarnim aplikacijama).
Ako koristite middleware (npr. Spring Security, Laravel Auth, Express JWT), on automatski odbija neovlaštene zahtjeve.

Implementacijski savjeti:
Backend:

Koristite @PreAuthorize (Spring) ili middleware (Node.js) za zaštitu ruta.
Provjerite rolove ako postoje (npr. "admin" može sve, "user" samo čitati).

Frontend:
Spremite token u localStorage ili HttpOnly cookie.
Dodajte ga u Authorization: Bearer <token> header.
Ako radite testiranje (npr. Postman), prvo pošaljite login zahtjev, kopirajte token,
pa ga zalijepite u zahtjeve za PolaznikController.

 */