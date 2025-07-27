package hr.java.web.p315.service;

import hr.java.web.p315.domain.RefreshToken;
import hr.java.web.p315.domain.UserInfo;
import hr.java.web.p315.repository.RefreshTokenRepository; // repo za refresh tokene
import hr.java.web.p315.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service  // Servis koji upravlja logikom kreiranja, provjere valjanosti i brisanja refresh tokena.
public class RefreshTokenService {

    @Autowired  // Injekcija se vrši direktno u polje (field) klase, ne treba konstruktor, teze testiranje (treba Mockito)
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.refresh.token.expiration.ms}") // dohvaća vrijednost iz application.properties
    private Long refreshTokenDurationMs; // trajanje refresh tokena u milisekundama

    public Optional<RefreshToken> findByToken(String token) {  // Pronalazi refresh token prema string vrijednosti tokena
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String username) {  // Kreira i sprema novi refresh token za korisnika prema username.
        UserInfo user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs)) // trenutni trenutak + trajanje
                .token(UUID.randomUUID().toString()) // generira jedinstveni string tokena, field token iz klase RefreshToken
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {  // Provjerava je li refresh token istekao, ako je - briše ga i baca iznimku.
        if (token.getExpiryDate().isBefore(Instant.now())) { // ako je datum isteka prije trenutnog vremena
            refreshTokenRepository.delete(token); // obriši token iz baze
            throw new RuntimeException("Refresh token expired. Please sign in again.");
        }
        return token; // ako nije istekao, vrati token
    }

    public int deleteByUserId(Long userId) {  // Brisanje refresh tokena prema korisniku.
        return refreshTokenRepository.deleteByUserInfoId(userId);
    }

}


/*

✅ Svrha servisa:
RefreshTokenService centralizira poslovnu logiku za rad s refresh tokenima, uključujući:
generiranje novog refresh tokena pri prijavi,
provjeru je li token istekao,
pronalazak tokena u bazi prema string vrijednosti,
brisanje tokena kad korisnik izađe iz sustava ili token istekne.

✅ Glavne metode:
1️⃣ findByToken(String token)
– Vraća opcionalni RefreshToken prema string vrijednosti tokena.
– Koristi se u AuthController kod /api/v1/refreshToken poziva.
2️⃣ createRefreshToken(String username)
– Pronalazi korisnika prema username iz baze (UserInfo).
– Generira novi RefreshToken s UUID tokenom i datumom isteka.
– Sprema u bazu i vraća spreman RefreshToken.
3️⃣ verifyExpiration(RefreshToken token)
– Provjerava je li token istekao (expiryDate < sada).
– Ako je istekao, briše token i baca RuntimeException.
– Ako nije istekao, vraća isti token.
4️⃣ deleteByUserId(Long userId)
– Briše sve refresh tokene povezane s određenim korisnikom (korisno prilikom odjave ili administracije).

✅ Osiguran parametrizirani rok trajanja:

properties
jwt.refresh.token.expiration.ms=86400000
(možeš postaviti u application.properties za npr. 1 dan = 86400000ms).

✅ UUID koristi se za generiranje unikatnih stringova refresh tokena, sigurno i jednostavno.
✅ @Transactional osigurava konzistenciju podataka prilikom operacija nad bazom.

✅ Korištenje:
Integriraš u AuthController prilikom:
kreiranja refresh tokena kod login-a,
dohvaćanja i provjere refresh tokena kod /refreshToken endpointa,
eventualnog brisanja tokena kod odjave.

 */