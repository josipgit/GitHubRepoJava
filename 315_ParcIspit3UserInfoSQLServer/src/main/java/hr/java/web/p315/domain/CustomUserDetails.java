package hr.java.web.p315.domain;

import org.springframework.security.core.GrantedAuthority; // Predstavlja pravo/autorizaciju korisnika
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implementacija GrantedAuthority s jednostavnim imenom role
import org.springframework.security.core.userdetails.UserDetails; // Spring Security sučelje za detalje korisnika

import java.util.ArrayList; // Za kreiranje liste
import java.util.Collection; // Kolekcija za vraćanje prava
import java.util.List; // Lista za prava

/**
 * CustomUserDetails je adapter klasa koja omogućuje Spring Security da radi s našim UserInfo entitetom.
 */
public class CustomUserDetails extends UserInfo implements UserDetails {

    private String username; // Korisničko ime korisnika
    private String password; // Lozinka korisnika
    private Collection<? extends GrantedAuthority> authorities; // Role/prava korisnika

    /**
     * Konstruktor koji prima UserInfo objekt i popunjava potrebna polja.
     * @param userInfo dohvaćeni korisnik iz baze
     */
    public CustomUserDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername(); // Postavlja korisničko ime
        this.password = userInfo.getPassword(); // Postavlja lozinku

        List<GrantedAuthority> auths = new ArrayList<>(); // Lista za role/prava

        // Za svaki UserRole korisnika, dodaje SimpleGrantedAuthority s nazivom role
        for (UserRole role : userInfo.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getRole_name().toUpperCase())); // Role pretvara u velika slova radi konzistentnosti
        }

        this.authorities = auths; // Postavlja liste prava korisnika
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Vraća role/prava korisnika
    }

    @Override
    public String getPassword() {
        return password; // Vraća lozinku korisnika
    }

    @Override
    public String getUsername() {
        return username; // Vraća korisničko ime korisnika
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Račun nije istekao
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Račun nije zaključan
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Lozinka nije istekla
    }

    @Override
    public boolean isEnabled() {
        return true; // Račun je omogućen
    }
}


/*

Detaljno objašnjenje klase na hrvatskom
Što je CustomUserDetails?
CustomUserDetails je adapter klasa koja omogućava Spring Security da koristi tvoj vlastiti UserInfo entitet
prilikom autentifikacije i autorizacije korisnika unutar JWT security stacka projekta Polaznik.

Zašto je ovo potrebno?
Spring Security prilikom autentifikacije koristi UserDetailsService koji mora vratiti objekt koji implementira
UserDetails.
Tvoj UserInfo entitet ne implementira UserDetails, stoga pomoću CustomUserDetails:

✅ Pakiraš UserInfo podatke u oblik koji Spring Security razumije,
✅ Prenosiš korisničko ime, lozinku i role u Spring Security,
✅ Omogućavaš provjeru pristupa po rolama pomoću @PreAuthorize ili konfiguracije.

📌 Što ova klasa radi?
🔹 Konstruktor CustomUserDetails(UserInfo userInfo):
Prima UserInfo iz baze.
Izdvaja korisničko ime i lozinku.
Izdvaja role korisnika i konvertira ih u GrantedAuthority objekte koje Spring koristi za autorizaciju.

🔹 Metode getUsername, getPassword, getAuthorities:
Vraćaju podatke koje Spring Security koristi za autentifikaciju i autorizaciju.

🔹 Metode isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled:
Vraćaju true što znači:
Račun nije istekao,
Nije zaključan,

Lozinka nije istekla,
Račun je omogućen.

Ako u budućnosti dodaš polja za enabled, locked, expiryDate, možeš ove metode prilagoditi za finiju kontrolu.

📌 Gdje ćeš koristiti ovu klasu?
Koristit ćeš je unutar:
✅ UserDetailsServiceImpl (npr. loadUserByUsername vraća new CustomUserDetails(userInfo)),
✅ Spring Security filtera (JwtAuthFilter) za autorizaciju zahtjeva,
✅ U kombinaciji s JWT tokenima za zaštitu ruta u Polaznik projektu.

 */