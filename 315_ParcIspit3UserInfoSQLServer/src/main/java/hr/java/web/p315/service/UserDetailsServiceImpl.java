package hr.java.web.p315.service;

import hr.java.web.p315.domain.CustomUserDetails; // Uvozi klasu koja implementira UserDetails (koristi se za Security)
import hr.java.web.p315.domain.Polaznik; // Entitet koji predstavlja korisnika u sustavu
import hr.java.web.p315.domain.UserInfo;
import hr.java.web.p315.repository.PolaznikRepository; // Repository za dohvat korisnika iz baze
import hr.java.web.p315.repository.UserRepository;
import lombok.AllArgsConstructor; // Generira konstruktor sa svim argumentima
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // Spring Security UserDetails sučelje
import org.springframework.security.core.userdetails.UserDetailsService; // Sučelje za dohvat korisničkih podataka iz baze
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Iznimka ako korisnik nije pronađen
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service; // Oznaka da je klasa servis

//@Service // Registrira klasu kao Spring servis
//@AllArgsConstructor // Generira konstruktor sa svim poljima za dependency injection
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService { // Implementira UserDetailsService za Spring Security

    @Autowired  // treba u ovoj verziji, ISTRAZI
    private UserRepository userRepository; // Repository za dohvat korisnika iz baze

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Metoda za dohvat korisnika po username
        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username) // Dohvati korisnika iz baze prema korisničkom imenu
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik s korisničkim imenom " + username + " nije pronađen.")); // Ako korisnik nije pronađen, baci iznimku
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user); // Vraća CustomUserDetails koji koristi Spring Security za autentifikaciju
    }
}


/*
KOMENTARI:

✅ Gdje se koristi:
Koristi Spring Security za dohvat korisnika iz baze prilikom autentifikacije (login).
Vezana je na SecurityConfig gdje se injektira kao UserDetailsService za DaoAuthenticationProvider.

✅ Što radi:
Implementira metodu loadUserByUsername koju Spring Security automatski poziva kada korisnik pokuša pristupiti zaštićenim resursima s JWT tokenom.
Dohvaća korisnika (Polaznik) prema korisničkom imenu iz baze koristeći PolaznikRepository.
Ako korisnik ne postoji, baca UsernameNotFoundException koju Spring Security koristi za odbijanje pristupa.
Ako korisnik postoji, vraća CustomUserDetails, koji pretvara entitet Polaznik u Spring Security kompatibilni objekt koji sadrži username, password i role korisnika.

✅ Za što ti omogućuje:
Radi login u /auth/api/v1/login.
Omogućuje dohvat korisničkih podataka za generiranje JWT tokena.
Omogućuje autorizaciju pristupa zaštićenim resursima koristeći JWT token.
Potpuno integrira Polaznik kao user entitet za JWT security.

 */