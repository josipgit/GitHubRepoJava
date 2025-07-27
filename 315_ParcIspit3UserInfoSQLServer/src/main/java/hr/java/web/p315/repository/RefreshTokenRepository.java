package hr.java.web.p315.repository; // Paket repozitorija u projektu Polaznik

import hr.java.web.p315.domain.RefreshToken; // Import entiteta RefreshToken
import org.springframework.data.jpa.repository.JpaRepository; // Osnovno Spring Data JPA sučelje
import org.springframework.stereotype.Repository; // Označava Spring komponentu tipa Repository

import java.util.Optional; // Koristi se za vraćanje Optional tipa

@Repository // Označava Springu da je ovo bean repozitorij koji može koristiti dependency injection
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    // Nasljeđuje JpaRepository s RefreshToken entitetom i Integer kao tipom ID-a

    Optional<RefreshToken> findByToken(String token);
    // Dohvaća RefreshToken iz baze prema token stringu

    int deleteByUserInfoId(Long id);
    // Briše sve refresh tokene povezane s danim korisnikom (Polaznikom)
    // i vraća broj obrisanih redaka (korisno kod logout funkcionalnosti)
}


/*

Što radi ova klasa:
✅ Predstavlja repozitorij sloj za RefreshToken, povezan s JWT refresh token mehanizmom.
✅ Omogućava:
Pretraživanje refresh tokena prema token stringu (findByToken).
Brisanje svih tokena povezanih s korisnikom (deleteByUserInfo), što se koristi kod logout funkcionalnosti
ili rotacije tokena.
✅ Automatski iz Spring Data JPA generira SQL upite, pa ne trebaš pisati ručno @Query.
✅ Integrira se direktno s RefreshTokenService, čime omogućuje održavanje sigurnog token sustava za
projekt Polaznik.

 */