package hr.java.web.p315.domain;

import jakarta.persistence.Entity; // Oznaka da je klasa JPA entitet
import jakarta.persistence.GeneratedValue; // Za automatsko generiranje vrijednosti primarnog ključa
import jakarta.persistence.GenerationType; // Tip generiranja ključa (IDENTITY za auto-increment)
import jakarta.persistence.Id; // Označava primarni ključ
import jakarta.persistence.JoinColumn; // Koristi se za specificiranje FK kolone u relacijama
import jakarta.persistence.ManyToOne; // Relacija mnogi prema jednom
import jakarta.persistence.Table; // Omogućuje postavljanje imena tablice u bazi
import jakarta.persistence.Column; // Omogućuje postavljanje imena kolone i njenih svojstava
import lombok.AllArgsConstructor; // Lombok: generira konstruktor sa svim argumentima
import lombok.Data; // Lombok: generira getter/setter/toString/hashCode/equals
import lombok.NoArgsConstructor; // Lombok: generira prazan konstruktor

@Data // Lombok anotacija koja generira gettere, settere, toString, equals i hashCode metode
@AllArgsConstructor // Lombok generira konstruktor sa svim argumentima
@NoArgsConstructor // Lombok generira prazan konstruktor
@Entity // Oznaka da je ova klasa JPA entitet i mapirat će se na tablicu u bazi
@Table(name = "Upis_Table") // Eksplicitno ime tablice u bazi podataka (Upis_Table)
public class Upis {

    @Id // Oznaka da je ovo primarni ključ tablice
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment vrijednost ID-a u bazi
    @Column(name = "IDUpis") // Ime kolone u tablici: IDUpis
    private Integer id; // Primarni ključ za tablicu Upis_Table

    @ManyToOne // Relacija: više zapisa u Upis može se povezivati s jednim Polaznikom
    @JoinColumn(name = "PolaznikID", referencedColumnName = "IDPolaznik", nullable = false) // FK kolona u tablici Upis_Table, obavezna (not null)
    private Polaznik polaznik; // Referenca na entitet Polaznik (Hibernate koristi PK iz Polaznik)

    @ManyToOne // Relacija: više zapisa u Upis može se povezivati s jednim ProgramObrazovanja
    @JoinColumn(name = "ProgramObrazovanjaID", referencedColumnName = "IDProgramObrazovanja", nullable = false) // FK kolona u tablici Upis_Table, obavezna
    private ProgramObrazovanja programObrazovanja; // Referenca na entitet ProgramObrazovanja
}



/*

Hibernate ne gleda ime atributa id u klasi Polaznik, nego gleda referencirani objekt
i mapira ga na vanjski ključ (FK) kroz @JoinColumn(name = "PolaznikID").
Drugim riječima:
Hibernate zna da Upis.polaznik pokazuje na entitet Polaznik.
U @JoinColumn(name = "PolaznikID"), specificiraš kako se FK (Foreign Key) kolona
zove u tablici Upis_Table.
Hibernate automatski koristi primarni ključ iz entiteta Polaznik (koji je IDPolaznik) kao referencu u toj FK koloni.
Dakle:
U tablici Polaznik_Table, primarni ključ je IDPolaznik.
U tablici Upis_Table, FK kolona se zove PolaznikID.
Hibernate će automatski mapirati Upis.polaznik na Polaznik.IDPolaznik koristeći PolaznikID kao FK.


📘 Detaljno objašnjenje funkcionalnosti klase Upis

1️⃣ Svrha klase:
Klasa Upis predstavlja entitet u bazi podataka koji prati evidenciju upisa polaznika na
određeni program obrazovanja.

2️⃣ Mapiranje na bazu:
@Entity označava da će Hibernate mapirati ovu klasu na tablicu.

@Table(name = "Upis_Table") specificira točno ime tablice u SQL Serveru.

3️⃣ Primarni ključ:
Atribut id:

Mapa se na kolonu IDUpis.

Automatski se generira putem IDENTITY strategije (SQL Server IDENTITY kolona).

4️⃣ Relacije:
a) Polaznik:
@ManyToOne: Više zapisa u Upis može biti povezano s istim Polaznikom (npr. isti polaznik upisuje više programa).
@JoinColumn(name = "PolaznikID"): Kolona u Upis_Table zove se PolaznikID, a Hibernate automatski
povezuje tu FK kolonu s PK entiteta Polaznik (koji je IDPolaznik).

b) ProgramObrazovanja:
@ManyToOne: Više zapisa u Upis može biti povezano s istim ProgramObrazovanja (npr. više polaznika upisuje isti program).
@JoinColumn(name = "ProgramObrazovanjaID"): FK kolona u Upis_Table, povezana s PK iz ProgramObrazovanja entiteta.

5️⃣ Lombok anotacije:
Koristiš Lombok za:
@Data ➔ automatsko generiranje getter, setter, toString, equals, hashCode.
@AllArgsConstructor ➔ konstruktor sa svim atributima.
@NoArgsConstructor ➔ prazan konstruktor (obavezno za Hibernate da bi mogao instancirati objekt refleksijom).

Zaključak
Klasa Upis:
✅ Služi za praćenje tko je (Polaznik) upisan na što (ProgramObrazovanja).
✅ Sadrži automatsko mapiranje na tablicu Upis_Table u SQL Serveru.
✅ Relacije su ispravno mapirane koristeći @ManyToOne s preciznim FK imenima.
✅ S @Id i @GeneratedValue Hibernate automatski brine o generiranju ID-a prilikom inserta.
✅ Korištenjem Lombok anotacija značajno se smanjuje količina ručno pisanog koda.

*/
