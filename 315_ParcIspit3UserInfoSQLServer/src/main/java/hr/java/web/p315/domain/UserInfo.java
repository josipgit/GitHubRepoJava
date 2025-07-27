package hr.java.web.p315.domain; // Paket domena projekta Polaznik

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; // JPA anotacije
import lombok.AllArgsConstructor; // Generira konstruktor sa svim argumentima
import lombok.Data; // Generira gettere, settere, toString, equals i hashCode
import lombok.NoArgsConstructor; // Generira prazan konstruktor
import lombok.ToString;

import java.util.HashSet; // Za inicijalizaciju seta
import java.util.Set; // Kolekcija za role

@Entity // Označava da je ovo JPA entitet
@Data // Lombok generira gettere/settere i ostale metode
@ToString  // Lombok anotacija - generira toString metodu
@NoArgsConstructor // Prazan konstruktor
@AllArgsConstructor // Konstruktor sa svim argumentima
@Table(name = "UserInfo_Table") // Mapira na tablicu UserInfo u bazi
public class UserInfo {

    @Id // Primarni ključ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoinkrement u bazi
    @Column(name = "IDUserInfo")
    private Integer id; // ID korisnika

    @Column(name = "Username", unique = true, nullable = false) // Korisničko ime mora biti unikatno i ne smije biti null
    private String username; // Korisničko ime

    @JsonIgnore  // Ignorira polje prilikom serijalizacije u JSON (zbog sigurnosti)
    @Column(name = "Password", nullable = false) // Lozinka ne smije biti null
    private String password; // Lozinka korisnika

    @ManyToMany(fetch = FetchType.EAGER) // Veza više-na-više, odmah učitava role
    @JoinTable(
            name = "User_Roles", // Naziv tablice povezivanja u bazi
            joinColumns = @JoinColumn(name="UserInfoID", referencedColumnName = "IDUserInfo"), // FK (Foreign Key) na UserInfo id
            inverseJoinColumns = @JoinColumn(name = "UserRoleID", referencedColumnName = "IDUserRole") // FK na UserRole
    )
    private Set<UserRole> roles = new HashSet<>(); // Skup uloga korisnika, inicijaliziran da izbjegne null pointer
}


/*

Automatsko povezivanje
Hibernate će automatski povezati IDUserRole iz spojne tablice s primarnim ključem
UserRole klase (koji je označen s @Id), čak i ako se zove IDUserRole umjesto standardnog id.
Kako Hibernate radi:
Traži @Id polje u UserRole klasi
Koristi njegovu @Column(name = "IDUserRole") anotaciju za vezu
Ne treba mu referencedColumnName jer već zna da gleda primarni ključ



Bez obzira kako se UserRole tablica zove (UserRole_Table ili bilo što drugo), dok god je primarni ključ
u klasi UserRole označen s @Id i naziva se samo id, Hibernate/JPA će automatski prepoznati vezu u kodu
u klasi UserInfo u liniji
inverseJoinColumns = @JoinColumn(name = "role_id") // FK na UserRole

Zašto to funkcionira?
Podrazumijevana konvencija
Ako ne navedete referencedColumnName, Hibernate pretpostavlja da role_id u veznici (User_Roles)
referencira polje id u klasi UserRole (jer je označeno s @Id).

Naziv tablice nije bitan
@Table(name = "UserRole_Table") utječe samo na ime tablice u bazi, ne na veze.

Provjera ispravnosti
U vašem slučaju:
UserRole ima polje id (s @Id) → Hibernate će tražiti id u UserRole_Table.
role_id u User_Roles će se povezati na UserRole_Table.id.

Kada morate koristiti referencedColumnName?
Samo ako primarni ključ u UserRole nije nazvan id (npr. role_id ili IDRole).
Tada biste morali eksplicitno navesti:
inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "IDRole")


Hibernate/JPA će automatski povezati authority_id iz spojne tablice users_authority
s id kolonom iz tablice authority (klasa UserRole).
Konvencija nad konfiguracijom:
inverseJoinColumns automatski referencira primarni ključ (id) ciljnog entiteta (UserRole)
Ako kolona u UserRole ima drugačije ime (npr. da se kolona umjesto id zove role_id),
morali biste to eksplicitno navesti:
inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "role_id")

Ovo nije stvaranje nove tablice u kodu - ovo je samo mapiranje veze na već postojeću tablicu u bazi podataka.
Evo detalja:
Što @JoinTable radi?
Oznaka samo opisuje kako veza više-na-više između UserInfo i UserRole treba biti implementirana u bazi
Ne stvara tablicu direktno - to radi baza podataka (kroz DDL) ili Hibernate
(ako koristite hibernate.hbm2ddl.auto=update)

Što je "User_Roles" tablica?
To je poveznica (join table) - klasična relacijska bazična struktura za many-to-many veze
Sadrži samo dva strana ključa (user_id i role_id)
Nema vlastite poslovne logike - samo povezuje druge tablice

@JoinTable u JPA/Hibernateu označava veznu tablicu (engl. join table) koja služi isključivo za
povezivanje dviju tablica u many-to-many odnosu. To nije klasična tablica s vlastitim podacima,
već tehnička struktura za upravljanje vezama.

Kratko objašnjenje:
Što je veznica (join table)?
Tablica u bazi koja sadrži samo strani ključeve druge dvije tablice
Nema vlastiti ID niti poslovne atribute
Primjer: User_Roles (povezuje UserInfo i UserRole)

Što @JoinTable zapravo radi?
Ne stvara tablicu – samo mapira vezu na postojeću strukturu u bazi
Ako tablica ne postoji, Hibernate je može kreirati (ako je omogućeno u postavkama)

Analogija iz stvarnog svijeta
Knjige (Book)
Autore (Author)
Zamislite da imate tablicu Knjige i Autori
Veznica Knjige_Autori samo bilježi tko je napisao koju knjigu
Tablicu Book_Author koja samo bilježi koja knjiga pripada kojem autoru
Zašto ovo nije "nova tablica s podacima" u smislu entiteta?
Nema odgovarajuće JPA klase za User_Roles
Nema vlastitog ID-a ili poslovnih polja
Nema repositoryja/servicea - koristi se isključivo kroz vezu iz UserInfo.roles

Kako baza vidi ovu vezu?
-- Baza vidi ovu strukturu:
CREATE TABLE User_Roles (
    user_id INT REFERENCES UserInfo_Table(id),
    role_id BIGINT REFERENCES UserRole_Table(IDRole),
    PRIMARY KEY (user_id, role_id)  -- Kompozitni ključ
);

Ključna razlika: Mapiranje != Stvaranje
Ako tablica već postoji u bazi: Hibernate je samo koristi

Ako ne postoji: Hibernate je može kreirati (ako je omogućeno u postavkama)

Kako možete promijeniti perspektivu?
Umjesto "nova tablica", gledajte na ovo kao:
"Povezivanje dvije tablice pomoću stranih ključeva u trećoj tablici"




✅ Predstavlja korisnika u JWT security sustavu, uključujući username, password i skup roles koje korisnik ima.
✅ Koristi se:
U CustomUserDetails, gdje se iz UserInfo pretvaraju u GrantedAuthority role za Spring Security.
Kod registracije korisnika, gdje se kreira novi zapis u tablici UserInfo.
Kod provjere autentikacije u UserDetailsServiceImpl za dohvat korisnika iz baze.
✅ Veza s UserRole je @ManyToMany jer korisnik može imati više uloga, a više korisnika može imati istu ulogu.
✅ Tablica povezivanja (user_roles) koristi se kako bi se Spring Securityu omogućilo čitanje uloga korisnika
   prilikom autentikacije i autorizacije.
✅ Spreman je za potpunu integraciju sa svim prethodnim JWT klasama (RefreshToken, JwtService, JwtAuthFilter,
   AuthController).

Ako želiš, mogu ti odmah pripremiti:
✅ UserRole s default ulogama (ROLE_USER, ROLE_ADMIN),
✅ DataLoader (CommandLineRunner) koji puni bazu s test korisnikom i adminom,
za potpunu funkcionalnost JWT security sustava na projektu Polaznik.


✅ Da li Polaznik zapravo zamjenjuje UserInfo?
Da, ako želiš da entitet Polaznik bude i korisnik sustava koji se prijavljuje putem JWT-a, tada UserInfo
postaje nepotreban i Polaznik ga u potpunosti zamjenjuje.

U tom slučaju:
Polaznik preuzima uloge korisnika sustava, s username, password, i roles.
Ne trebaš UserInfo jer bi bio duplicirana tablica.
PolaznikRepository preuzima ulogu UserRepository.
UserDetailsServiceImpl dohvaća Polaznik po username.
CustomUserDetails prima Polaznik umjesto UserInfo.

✅ Kada je ovo ispravan pristup?
Koristi se kada:
✅ Svi korisnici sustava moraju biti fizički polaznici (nema administratora koji nisu polaznici).
✅ Sustav nema različite tipove korisnika osim polaznika.
✅ Želiš pojednostaviti model i održavanje baze.

❌ Kada nije preporučeno:
Ako želiš:
Imati administratore, profesore ili druge tipove korisnika koji nisu polaznici.
Razdvojiti domenski entitet (Polaznik = poslovni entitet) od sustava korisnika (UserInfo = entitet za autentikaciju).
Održavati fleksibilnost za proširenje sustava.
Tada je bolje zadržati UserInfo i koristiti ga isključivo za login, a Polaznik za evidenciju polaznika.

✅ Zaključak za tvoj projekt Polaznik:
Budući da radiš edukacijski projekt i želiš JWT login direktno za polaznike, tvoj trenutni pristup je potpuno ispravan:

✅ Polaznik zamjenjuje UserInfo.
✅ Sve JWT klase rade s Polaznik.
✅ PolaznikRepository zamjenjuje UserRepository.
✅ UserDetailsServiceImpl koristi PolaznikRepository za dohvat po username.
✅ CustomUserDetails prima Polaznik kao argument.
✅ RefreshToken i RefreshTokenService rade s Polaznik.


 */