package hr.java.web.p315.dto; // DTO sloj za prijenos podataka

import lombok.Data; // Lombok generira gettere, settere, toString, equals i hashCode

@Data // Automatski generira gettere, settere, toString, equals i hashCode metode
public class RefreshTokenRequestDTO {

    private String token; // Refresh token koji klijent šalje kada želi osvježiti JWT access token
}


/*

📌 Objašnjenje na hrvatskom
✅ Što je RefreshTokenRequestDTO:
DTO klasa koja prima refreshToken od klijenta kada želi osvježiti JWT accessToken pozivom na /auth/api/v1/refreshToken.

✅ Zašto je potreban:
Da bi Spring REST kontroler (AuthController) mogao mapirati JSON zahtjev s poljem token na Java objekt prilikom slanja zahtjeva za osvježavanje tokena:
{
    "token": "tvoj_refresh_token"
}
Održava čistoću koda i omogućuje lakše validacije u budućnosti.

✅ Polja:
token: String koji sadrži refreshToken poslan od strane klijenta.

✅ Lombok @Data:
Automatski generira:
Gettere i settere za polje token
toString() za debug
equals() i hashCode() za usporedbe objekata

📌 Jesu li @AllArgsConstructor i @NoArgsConstructor potrebni za RefreshTokenRequestDTO?
✅ Nisu obavezni, ali @NoArgsConstructor je preporučljiv u kontekstu Spring MVC-a i Jackson deserializacije
JSON-a u objekt.
Detaljno:
1️⃣ Spring + Jackson prilikom @RequestBody:
Prilikom mapiranja JSON zahtjeva u objekt (RefreshTokenRequestDTO), Jackson:
koristi prazan (default) konstruktor
postavlja polja pomoću reflectiona (setter metode ili direktno).
Ako nisi deklarirao nijedan konstruktor ručno, Lombok-ov @Data ne generira konstruktore pa će defaultni biti
prisutan automatski.
2️⃣ Ako kasnije dodaš ručno bilo koji konstruktor, automatski više nemaš defaultni prazan konstruktor,
tada će @NoArgsConstructor biti nužan kako bi Jackson mogao deserializirati JSON.
3️⃣ @AllArgsConstructor nije potreban za funkcionalnost DTO-a u ovom slučaju, osim ako ne planiraš ručno kreirati
instancu s vrijednostima (npr. new RefreshTokenRequestDTO(token)), što kod DTO-a za @RequestBody nije potrebno.

✅ Preporučeni minimalni oblik:

package hr.java.web.p315.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor // osigurava prazan konstruktor za Jackson deserializaciju
public class RefreshTokenRequestDTO {
    private String token;
}

Zaključak:
✅ Koristi @NoArgsConstructor (ili imaj defaultni prazan konstruktor) za DTO koji prima JSON @RequestBody.
✅ @AllArgsConstructor nije potreban za DTO koji služi samo kao primatelj JSON-a u kontroleru.

 */