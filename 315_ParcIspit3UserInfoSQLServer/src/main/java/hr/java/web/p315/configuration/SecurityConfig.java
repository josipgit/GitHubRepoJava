package hr.java.web.p315.configuration;

import hr.java.web.p315.domain.Polaznik;
import lombok.AllArgsConstructor; // Automatski generira konstruktor sa svim argumentima.
import hr.java.web.p315.filter.JwtAuthFilter; // Uvozi JWT filter koji provjerava token na svakom zahtjevu.
import hr.java.web.p315.service.UserDetailsServiceImpl; // Uvozi implementaciju UserDetailsService za dohvat korisnika iz baze.
import org.springframework.context.annotation.Bean; // Oznaka za metode koje vraćaju Spring Beanove.
import org.springframework.context.annotation.Configuration; // Oznaka da je klasa konfiguracijska.
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Upravlja procesom autentifikacije.
import org.springframework.security.authentication.AuthenticationProvider; // Sučelje koje provodi autentifikaciju.
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Provider koji koristi UserDetailsService i PasswordEncoder.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Dohvaćanje AuthenticationManagera iz Spring konteksta.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Omogućava @PreAuthorize i slične anotacije.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Konfiguracija HTTP sigurnosti.
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Omogućava web sigurnost.
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Omogućava prilagodbu HttpSecurity.
import org.springframework.security.config.http.SessionCreationPolicy; // Definira način upravljanja sesijama.
import org.springframework.security.core.userdetails.UserDetailsService; // Dohvat korisnika za autentifikaciju.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Encoder za hashiranje lozinki.
import org.springframework.security.crypto.password.PasswordEncoder; // Sučelje za enkodiranje lozinki.
import org.springframework.security.web.SecurityFilterChain; // Definira lanac sigurnosnih filtera.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filter koji obrađuje login zahtjeve.

@Configuration // Oznacava da je klasa konfiguracija.
@EnableWebSecurity // Uključuje web sigurnost.
@EnableMethodSecurity // Omogućuje sigurnost na razini metoda.
@AllArgsConstructor // Generira konstruktor s poljima klase (automatsko ubrizgavanje).
public class SecurityConfig { // Klasa za konfiguraciju Spring Security za JWT u projektu Polaznik.

    private final JwtAuthFilter jwtAuthFilter; // JWT filter za provjeru tokena, automatski ubrizgan konstruktorom.

    @Bean // Registrira UserDetailsService bean u Spring kontekstu.
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // Vraća vlastitu implementaciju UserDetailsService koja dohvaća korisnike iz baze.
    }

//    @Bean // Registrira SecurityFilterChain bean u Spring kontekstu.
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Konfigurira HTTP sigurnost.
//        http.csrf(AbstractHttpConfigurer::disable) // Isključuje CSRF zaštitu jer JWT ne koristi sesije.
//                .cors(AbstractHttpConfigurer::disable) // Isključuje CORS (može se omogućiti prema potrebi).
//                .authorizeHttpRequests(authorize -> authorize // Konfigurira dopuštene zahtjeve.
//                        .requestMatchers("/auth/api/v1/login", "/auth/api/v1/refreshToken").permitAll() // Putanje za login i refresh token su dostupne svima.
//                        .requestMatchers("/api/v1/**", "/polaznici/**").authenticated() // Sve ostale navedene putanje zahtijevaju autentifikaciju.
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Koristi stateless pristup (bez sesija).
//                .authenticationProvider(authenticationProvider()) // Postavlja AuthenticationProvider.
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Dodaje JWT filter prije standardnog filtera za autentifikaciju.
//        return http.build(); // Vraća konfigurirani SecurityFilterChain.
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // dopusti H2 konzolu bez CSRF
                        .disable() // onemogući CSRF općenito
                )
                // CORS
                .cors(AbstractHttpConfigurer::disable)

                // HTTP Headers (potrebno za H2 konzolu da radi u iframe-u)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()
                        )
                )

                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints
                        .requestMatchers("/auth/api/v1/login", "/auth/api/v1/refreshToken").permitAll()

                        // H2 console access
                        .requestMatchers("/h2-console/**").permitAll()

                        // PUT requests require ADMIN authority
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/polaznici/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/polaznici/**").hasAuthority("ROLE_ADMIN")

                        // General authentication rules
                        .requestMatchers("/api/v1/**", "/polaznici/**").authenticated()

                        // Svi ostali zahtjevi moraju biti autentificirani
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean // Registrira PasswordEncoder bean u Spring kontekstu.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Koristi BCrypt algoritam za hashiranje lozinki.
    }

    @Bean // Registrira AuthenticationProvider bean.
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // Koristi DAO AuthenticationProvider.
        authenticationProvider.setUserDetailsService(userDetailsService()); // Postavlja UserDetailsService.
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Postavlja PasswordEncoder.
        return authenticationProvider; // Vraća konfigurirani AuthenticationProvider.
    }

    @Bean // Registrira AuthenticationManager bean.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Dohvaća AuthenticationManager iz konfiguracije.
    }
}


/*

Što je @Bean?
@Bean je Spring anotacija kojom definiraš jedan bean ručno u svojoj @Configuration klasi (ili klasi označenoj
s @SpringBootApplication).
Označava metodu čiji povratni objekt Spring kontejner registrira u svoj ApplicationContext kao bean.

Gdje se koristi?
Koristi se u:
@Configuration klasama
@SpringBootApplication klasama
kako bi ručno registrirao objekte koje želiš da Spring upravlja njima
(umjesto da koristiš @Component, @Service, @Repository ili automatsko skeniranje paketa).

Zašto se koristi?
Kada trebaš konfigurirati ili inicijalizirati bean na specifičan način.
Kada koristiš third-party klase koje nemaš označene s @Component (npr. JDBC DataSource, ObjectMapper, RestTemplate).
Kada želiš preciznu kontrolu nad kreiranjem beana.

Kako radi?
Spring prilikom pokretanja:
1️⃣ Skenira @Configuration klase.
2️⃣ Poziva metode označene s @Bean (bean metode).
3️⃣ Povratni objekt od bean metode registrira kao bean s nazivom metode.
4️⃣ Omogućuje injekciju tog beana u druge dijelove aplikacije pomoću @Autowired ili konstruktorom.

Što bean zapravo jest:
Bean u Springu je običan Java objekt kojim Spring upravlja unutar svog kontejnera.
Spring bean = objekt u memoriji kojim Spring upravlja:
1) Stvara ga (instancira).
2) Konfigurira ga (ovisnosti, parametri).
3) Upravlja njegovim životnim ciklusom (stvaranje, korištenje, uništavanje).

Za što služi bean?
Bean može biti bilo što:
Servis (UserService) koji dohvaća i sprema podatke.
RestTemplate za HTTP pozive.
DataSource za bazu.
ObjectMapper za JSON parsiranje.
Helper klasa za validaciju.

Dakle bean je bilo koji objekt koji ti želiš da Spring stvori i kojim upravlja.

Bean nije isto što i podatak
Bean je alat/objekt koji koristiš za dohvat i spremanje podataka, ali sam bean nije podatak.




Što je kontejner u Springu?
IoC = Spring kontrolira instanciranje i povezivanje objekata umjesto tebe.
Spring kontejner (IoC Container) je dio Spring Frameworka koji:
✅ Instancira (stvara) beanove.
✅ Upravlja njihovim životnim ciklusom (stvaranje, konfiguracija, uništavanje).
✅ Povezuje ih (Dependency Injection) prema tvojim potrebama.

Gdje se „nalazi” taj kontejner?
Kontejner nastaje prilikom pokretanja tvoje Spring Boot aplikacije.
To je memorijska struktura u tvojoj aplikaciji koja pamti sve beanove (npr. UserService, DataSource, RestTemplate).
Kad pokreneš Spring Boot aplikaciju onda iz maina SpringApplication.run pokreće Spring kontejner.
Od tog trenutka:
Sve metode označene s @Bean u @Configuration klasama bit će pozvane.
Ono što te metode vrate registrira se kao bean u kontejneru.
Ostatak aplikacije može koristiti te beanove pomoću @Autowired, @Inject ili konstruktorom.




🔹 Klasa SecurityConfig postavlja Spring Security konfiguraciju za projekt Polaznik kako bi JWT autorizacija radila
pravilno, uz stateless pristup (bez sesija) koristeći JwtAuthFilter za provjeru tokena na svakom zahtjevu.

🔹 Najvažnije točke:
✅ CSRF i CORS su isključeni jer JWT ne koristi sesije i frontend sam šalje token.
✅ Definirane dozvoljene putanje (login i refresh dostupni svima, ostale zaštićene).
✅ Korišten BCryptPasswordEncoder za hashiranje lozinki radi sigurnosti.
✅ Korišten DaoAuthenticationProvider s vlastitim UserDetailsServiceImpl.
✅ Dodaje se JwtAuthFilter prije UsernamePasswordAuthenticationFilter, kako bi svi zahtjevi prvo provjerili JWT token.
✅ Koristi stateless policy uz JWT, ne kreirajući HTTP sesije.




2. Što znači ** i koja je razlika između ** i *?

** (dvostruki wildcard)
Rekurzivno podudaranje - poklapa se sa svim podputanjama
Pokriva:
Sve dodatne segmente putanje (/)
Sve dodatne parametre (?, query string)
Primjer: /api/v1/** pokriva:
/api/v1/users
/api/v1/users/5
/api/v1/users/5/orders
/api/v1/admin/settings

* (jednostruki wildcard)
Poklapa samo jedan segment putanje
Ne pokriva dodatne / znakove
Primjer: /api/v1/* pokriva:
/api/v1/users
/api/v1/products
Ali NE pokriva:
/api/v1/users/5
/api/v1/products/category

*/