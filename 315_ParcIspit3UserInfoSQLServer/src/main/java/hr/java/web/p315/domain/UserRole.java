package hr.java.web.p315.domain;

import jakarta.persistence.*; // JPA anotacije
import lombok.AllArgsConstructor; // Lombok anotacija za konstruktor sa svim argumentima
import lombok.Data; // Lombok anotacija za generiranje gettera, settera, toString itd.
import lombok.NoArgsConstructor; // Lombok anotacija za prazan konstruktor
import lombok.ToString;

@Entity // Označava JPA entitet
@Data // Lombok generira gettere, settere, toString, equals i hashCode
@ToString
@NoArgsConstructor // Prazan konstruktor
@AllArgsConstructor // Konstruktor sa svim argumentima
@Table(name = "UserRole_Table") // Mapira se na tablicu UserRole u bazi
public class UserRole {

    @Id // Primarni ključ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto inkrementacija ID-a u bazi
    @Column(name = "IDUserRole") // Kolona IDRole
    private long id; // ID role

    @Column(name = "UserRoleName", nullable = false, unique = true, length = 50)  // Kolona RoleName, jedinstvena, obavezna
    private String role_name; // Naziv role, npr. ROLE_USER, ROLE_ADMIN

}


/*

📌 Objašnjenje polja
✅ id – Primarni ključ role.
✅ name – Naziv role, jedinstven i obavezan (npr. ROLE_USER, ROLE_ADMIN), bitan za Spring Security za autorizaciju.
✅ polaznici – Obrnuta strana ManyToMany veze iz klase Polaznik, omogućuje da dohvatiš sve polaznike koji imaju
   ovu rolu (nije obavezno koristiti, ali korisno za administraciju).

📌 Što sada imaš spremno:
✅ Potpuno pripremljen entitet UserRole kompatibilan s JWT Security.
✅ Povezivanje s Polaznik entitetom kroz ManyToMany radi jednostavne obrade uloga prilikom autentikacije
   i autorizacije.

 */