package hr.java.web.p315.filter;

import hr.java.web.p315.service.JwtService; // Servis za generiranje i provjeru JWT tokena
import hr.java.web.p315.service.UserDetailsServiceImpl; // Servis za dohvat korisnika iz baze
import jakarta.servlet.FilterChain; // Lanac filtera kroz koji prolazi zahtjev
import jakarta.servlet.ServletException; // Iznimka kod problema s filterom
import jakarta.servlet.http.HttpServletRequest; // HTTP zahtjev
import jakarta.servlet.http.HttpServletResponse; // HTTP odgovor
import lombok.AllArgsConstructor; // Generira konstruktor sa svim argumentima
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token koji drži korisničke podatke nakon autentifikacije
import org.springframework.security.core.context.SecurityContextHolder; // Drži sigurnosni kontekst trenutno autentificiranog korisnika
import org.springframework.security.core.userdetails.UserDetails; // Sučelje koje predstavlja korisnika
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Za dodavanje detalja autentifikacije
import org.springframework.stereotype.Component; // Registrira klasu kao Spring komponentu
import org.springframework.web.filter.OncePerRequestFilter; // Filter koji se izvršava jednom po zahtjevu

import java.io.IOException; // Za rukovanje iznimkama kod IO operacija

@Component // Registrira filter kao Spring komponentu
@AllArgsConstructor // Generira konstruktor sa svim poljima
public class JwtAuthFilter extends OncePerRequestFilter { // Filter koji provjerava JWT token na svakom zahtjevu

    private final JwtService jwtService; // Servis za rad s JWT tokenima
    private final UserDetailsServiceImpl userDetailsService; // Servis za dohvat korisničkih podataka iz baze

    @Override
    protected void doFilterInternal(HttpServletRequest request, // HTTP zahtjev
                                    HttpServletResponse response, // HTTP odgovor
                                    FilterChain filterChain) throws ServletException, IOException { // Lanac filtera
        String authHeader = request.getHeader("Authorization"); // Dohvati Authorization header iz zahtjeva
        String token = null; // Inicijalizira token kao null
        String username = null; // Inicijalizira username kao null

        // Provjeri postoji li header i počinje li s "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Uklanja "Bearer " kako bi ostao samo JWT token
            username = jwtService.extractUsername(token); // Dohvati korisničko ime iz tokena
        }

        // Ako imamo username i korisnik nije već autentificiran
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Dohvati korisničke podatke iz baze

            // Provjeri je li token ispravan za korisnika
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // Korisnički podaci
                        null, // Credentials (ne trebaju kod JWT)
                        userDetails.getAuthorities() // Role korisnika
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Dodaj detalje zahtjeva
                SecurityContextHolder.getContext().setAuthentication(authToken); // Postavi korisnika kao autentificiranog
            }
        }

        filterChain.doFilter(request, response); // Nastavi izvršavanje filtera dalje u lancu
    }
}


/*

✅ Gdje se koristi JwtAuthFilter:
Automatski se poziva na svakom HTTP zahtjevu prema aplikaciji.

Registriran u SecurityConfig pomoću:
.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
kako bi provjeravao JWT prije nego što Spring pokuša autentificirati korisnika putem form login pristupa.

✅ Što radi:
1️⃣ Provjerava postoji li Authorization header i je li oblika Bearer <token>.
2️⃣ Izdvaja token iz headera.
3️⃣ Pomoću JwtService iz tokena dohvaća korisničko ime.
4️⃣ Ako je korisnik već autentificiran (SecurityContextHolder.getContext().getAuthentication() nije null),
preskače daljnju obradu.
5️⃣ Ako nije:

Dohvaća korisničke podatke iz baze koristeći UserDetailsServiceImpl.
Validira token pomoću JwtService.validateToken.
Ako je token ispravan, kreira UsernamePasswordAuthenticationToken i postavlja ga u SecurityContext,
čime omogućuje autorizaciju korisnika na temelju uloga.

✅ Za što ti omogućuje:
Automatsku provjeru JWT tokena na svakom zahtjevu bez dodatne logike u kontrolerima.
Omogućuje pristup zaštićenim resursima samo korisnicima s ispravnim JWT tokenom.
Omogućuje @PreAuthorize i hasRole() zaštitu na kontrolerima temeljem korisničkih uloga iz JWT-a.

 */