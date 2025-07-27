package hr.java.web.p315.domain;

import jakarta.persistence.*;       // Hibernate / JPA anotacije za mapiranje na tablice
import lombok.AllArgsConstructor;  // Lombok za automatsko generiranje konstruktora sa svim argumentima
import lombok.Builder;             // Lombok za Builder pattern
import lombok.Data;                // Lombok za getter/setter/toString/equals/hashCode
import lombok.NoArgsConstructor;   // Lombok za no-args konstruktor

import java.time.Instant;          // Klasa za rad s vremenom (timestamp)

@Entity                             // Označava da je klasa JPA entitet (Hibernate ORM)
@Data                               // Lombok generira gettere/settere/equals/hashCode/toString
@Builder                            // Lombok omogućuje gradnju objekta putem Builder patterna
@NoArgsConstructor                  // Lombok generira prazan konstruktor
@AllArgsConstructor                 // Lombok generira konstruktor sa svim poljima
@Table(name = "RefreshToken_Table") // Određuje ime tablice u bazi podataka
public class RefreshToken {         // Početak definicije entiteta

    @Id                                              // Primarni ključ tablice
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto inkrement ID u bazi
    private Long IDRefreshToken;                                 // Polje za ID tokena

    @Column(nullable = false, unique = true)        // Stupac koji ne smije biti null i mora biti jedinstven
    private String token;                           // String koji sadrži refresh token

    @Column(nullable = false)                      // Stupac koji ne smije biti null
    private Instant expiryDate;                    // Datum i vrijeme isteka refresh tokena

    @OneToOne                                       // Veza 1:1 s entitetom UserInfo
    @JoinColumn(name = "UserInfoID", referencedColumnName="IDUserInfo") // FK stupac u tablici koji referencira UserInfo
    private UserInfo userInfo;                     // Referenca na korisnika kojem pripada token

}


/*

Dovoljno je navesti samo name = "IDUserInfo" u @JoinColumn bez korištenja referencedColumnName,
čak i kada kolona nema standardni naziv id. Evo detaljnog objašnjenja:
Zašto je referencedColumnName nepotreban?
Podrazumijevano ponašanje
Hibernate/JPA automatski će povezati IDUserInfo s primarnim ključem (@Id) ciljnog
entiteta (UserInfo), bez obzira na njegov naziv.
Kako funkcionira?
@OneToOne
@JoinColumn(name = "IDUserInfo")  // Dovoljno je samo ovo
private UserInfo userInfo;
Hibernate će pretražiti @Id polje u UserInfo klasi
Automatski će koristiti tu kolonu za vezu.
Kada je referencedColumnName obavezan?
Samo ako želite da strani ključ referencira neku drugu kolonu (koja nije @Id).



RefreshToken je Entitet koji predstavlja refresh token pohranjen u bazi,
kako bi korisnik mogao osvježiti JWT bez ponovne prijave nakon što access token istekne.

✅ Gdje se koristi:
U RefreshTokenService za kreiranje, spremanje, provjeru valjanosti i brisanje refresh tokena kada korisnik traži
novi JWT nakon isteka starog.
U AuthController u /api/v1/refreshToken endpointu.

✅ Polja:
id: primarni ključ, auto inkrement.
token: string koji predstavlja refresh token, jedinstven u bazi.
expiryDate: datum i vrijeme isteka refresh tokena.
userInfo: referenca na Polaznik koji posjeduje taj refresh token (korisnik kojemu pripada).

✅ Prednosti:
Sigurnost: omogućuje da refresh token bude pohranjen u bazi i da se može ručno opozvati (npr. korisnik se odjavi).
Održavanje kontrole: omogućuje da imaš kontrolu kada i kome je izdano produženje pristupa.
Lazy fetch smanjuje nepotrebno dohvaćanje podataka korisnika dok nije potrebno.

✅ Lombok (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor):
automatski generira getter/setter, konstruktore i toString radi urednosti.

✅ Veza s entitetom UserInfo:
UserInfo treba biti JPA entitet koji predstavlja korisnika.
Ako koristiš Polaznik umjesto UserInfo, samo promijeni tip polja i ime stupca:

✅ U praksi:
1️⃣ Prilikom prijave, kreira se RefreshToken i vraća korisniku uz JWT.
2️⃣ Kada JWT istekne, korisnik šalje refreshToken na endpoint /api/v1/refreshToken.
3️⃣ Provjeri se postoji li RefreshToken u bazi i je li istekao.
4️⃣ Ako je ispravan, generira se novi JWT i vraća korisniku.

 */
