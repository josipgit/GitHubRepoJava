package hr.java.web.p315.dto; // DTO sloj

import lombok.Data; // Generira gettere, settere, toString, equals i hashCode
import lombok.NoArgsConstructor; // Generira prazan konstruktor
import lombok.AllArgsConstructor; // Generira konstruktor sa svim argumentima

@Data // Automatski generira gettere, settere itd.
@NoArgsConstructor // Prazan konstruktor potreban za deserializaciju JSON-a
@AllArgsConstructor // Konstruktor sa svim argumentima
public class AuthRequestDTO {

    private String username; // Korisničko ime za prijavu
    private String password; // Lozinka za prijavu
}


/*

📌 Objašnjenje
✅ Što je AuthRequestDTO:
DTO (Data Transfer Object) koji služi za slanje podataka prilikom POST /api/v1/login u AuthController, gdje korisnik šalje username i password u tijelu zahtjeva.

✅ Zašto je potreban:
Olakšava primanje podataka s frontenda ili Postman-a prilikom autentikacije.
Održava sloj DTO-a odvojenim od entiteta (Polaznik).

✅ Lombok anotacije:
@Data: Automatski generira sve gettere, settere, toString, equals i hashCode.
@NoArgsConstructor: Potreban za Jackson deserializaciju JSON-a.
@AllArgsConstructor: Koristan za testiranje ili inicijalizaciju.

 */
