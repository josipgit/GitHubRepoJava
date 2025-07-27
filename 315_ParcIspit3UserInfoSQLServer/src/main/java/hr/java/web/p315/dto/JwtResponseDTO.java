package hr.java.web.p315.dto; // DTO sloj za prijenos podataka

import lombok.Builder; // Omogućuje korištenje Builder patterna
import lombok.Data; // Generira gettere, settere, toString, equals i hashCode

@Data // Automatski generira gettere, settere itd.
@Builder // Omogućuje kreiranje objekta korištenjem builder pristupa (lako slaganje polja prilikom vraćanja odgovora)
public class JwtResponseDTO {

    private String accessToken; // JWT token za pristup API-ju
    private String token; // Refresh token koji se koristi za osvježavanje access tokena
}


/*

📌 Objašnjenje
✅ Što je JwtResponseDTO:
DTO (Data Transfer Object) koji se koristi za vraćanje korisniku odgovora nakon uspješne autentikacije
ili osvježavanja tokena u AuthController.

✅ Zašto je potreban:
Odvojeno i čisto vraćanje accessToken (JWT) i refreshToken prema korisniku nakon prijave ili osvježavanja.
Frontend će koristiti accessToken za pozive prema zaštićenim rutama, a refreshToken za dobivanje novog access
tokena.

✅ Polja:
accessToken: JWT koji se šalje u Authorization headeru za pristup zaštićenim endpointima.
token: Refresh token za dobivanje novog JWT-a bez ponovne prijave.

✅ Lombok anotacije:
@Data: Automatski generira gettere, settere, toString, equals i hashCode.
@Builder: Omogućuje kreiranje objekta u stilu:

JwtResponseDTO response = JwtResponseDTO.builder()
    .accessToken(token)
    .token(refreshToken)
    .build();
što čini kod u AuthController čistim i preglednim.

 */