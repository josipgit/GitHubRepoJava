package hr.java.web.p315.domain;

// JPA importi anotacije za mapiranje entiteta
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor; // Lombok za generiranje konstruktora sa svim poljima
import lombok.Data; // Lombok za generiranje gettera, settera, toString, equals i hashCode
import lombok.NoArgsConstructor; // Lombok za generiranje praznog konstruktora

@Data // Lombok generira gettere, settere, toString, equals i hashCode metode
@AllArgsConstructor // Lombok generira konstruktor sa svim argumentima
@NoArgsConstructor // Lombok generira prazan konstruktor
@Entity // Označava da je ovo JPA entitet
@Table(name = "Polaznik_Table") // Mapira se na tablicu "Polaznik" u bazi
public class Polaznik {

    @Id // Označava primarni ključ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto inkrementacija ID-a u bazi
    @Column(name = "IDPolaznik") // Mapiranje na kolonu IDPolaznik
    private Integer id; // ID polaznika

    @Column(name = "Ime", nullable = false, length = 100) // Kolona Ime, nije null
    private String ime; // Ime polaznika

    @Column(name = "Prezime", nullable = false, length = 100) // Kolona Prezime, nije null
    private String prezime; // Prezime polaznika

    @Column(name = "Username", nullable = false, unique = true, length = 50)
    // Kolona username, obavezno jedinstveno, potrebno za JWT login
    private String username; // Username polaznika za prijavu

//    @JsonIgnore
//    @Column(name = "Password", nullable = false)
//    // Kolona password, obavezna, potrebno za JWT login
//    private String password; // Hashirana lozinka polaznika

    @Column(name = "Password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

//    ovaj dio ide ako koristis verziju sa Polaznik umjesto UserInfo
//    @ManyToMany(fetch = FetchType.EAGER)
//    // Veza N:M prema rolama, EAGER jer Spring Security treba role odmah prilikom autentikacije
//    @JoinTable(
//            name = "polaznik_roles", // Tablica poveznica
//            joinColumns = @JoinColumn(name = "polaznik_id"), // FK (Foreign Key) prema Polaznik
//            inverseJoinColumns = @JoinColumn(name = "role_id", refrencedColumnName = "IDRole") // FK prema UserRole
//    )
//    private Set<UserRole> roles = new HashSet<>(); // Skup uloga polaznika, npr. ROLE_USER, ROLE_ADMIN
//
//    // ovo tribas da bi ubacija u PolaznikControllerTest bez Set<UserRole> fielda ako koristis verziju sa Polaznik umjesto UserInfo
//    public Polaznik(Integer id, String ime, String prezime, String username, String password) {
//        this.id = id;
//        this.ime = ime;
//        this.prezime = prezime;
//        this.username = username;
//        this.password = password;
//    }

} // end class


/*

📌 Objašnjenje izmjena
✅ Dodano polje username:
JWT Security koristi username za identifikaciju korisnika pri loginu.
Postavljeno kao jedinstveno i obavezno.

✅ Dodano polje password:
Potrebno za prijavu putem Spring Security JWT, s hashiranom lozinkom.

✅ Dodana veza Set<UserRole> roles:
JWT Security koristi role kako bi omogućio ili ograničio pristup.
Postavljeno kao @ManyToMany(fetch = FetchType.EAGER) kako bi role bile odmah dostupne tijekom autentikacije.

✅ Postojeća polja (ime, prezime, id) ostavljena su netaknuta jer su potrebna za tvoj projekt.
📌 Što još trebaš imati
1️⃣ Entitet UserRole:
Mora postojati entitet UserRole s poljem name (npr. ROLE_USER, ROLE_ADMIN) za upravljanje rolama korisnika.
Ako želiš, mogu ti odmah napisati kompatibilnu UserRole klasu za JWT Security.
2️⃣ Lozinke trebaju biti hashirane (BCrypt) pri registraciji.
3️⃣ JWT klase (koje već imaš) sada će ispravno raditi s ovim entitetom.

 */