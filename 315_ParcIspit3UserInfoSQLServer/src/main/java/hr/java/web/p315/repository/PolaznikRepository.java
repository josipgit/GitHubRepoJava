package hr.java.web.p315.repository;

import hr.java.web.p315.domain.Polaznik; // Tvoj entitet korisnika
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolaznikRepository extends JpaRepository<Polaznik, Integer> {
    Optional<Polaznik> findByUsername(String username);
}


/*

Optional<T> je klasa iz Java 8 koja:
✅ Omogućava rad s vrijednostima koje mogu biti null na siguran način.
✅ Služi kao wrapper oko objekta koji može ili ne mora biti prisutan.
Ako metoda ne pronađe korisnika:
Umjesto da vrati null, vraća Optional.empty().

✅ Da li PolaznikRepository predstavlja UserRepository?
Da, praktički je isto jer:
UserRepository je samo ime repozitorija koji radi s entitetom User.
Ti koristiš Polaznik kao entitet korisnika u svom projektu, pa repozitorij za Polaznik (PolaznikRepository) ima istu ulogu kao UserRepository.

❌ Međutim
Da bi PolaznikRepository bio kompatibilan sa Spring Security JWT klasama, mora imati metodu za dohvat korisnika po username, jer UserDetailsServiceImpl koristi:
polaznikRepository.findByUsername(username)
što trenutno ne postoji u tvojoj klasi.

📌 Što još trebaš imati u entitetu Polaznik?
Da bi ovo radilo, tvoj entitet Polaznik mora imati atribut:
private String username;
s getterom:
public String getUsername() {
    return username;
}

🔹 Ukratko:
✅ PolaznikRepository == UserRepository za tvoj projekt.
✅ Samo je potrebno dodati findByUsername za JWT Security integraciju.
✅ Obavezno provjeri da Polaznik ima polje username (i password).

 */


//package hr.java.web.p315.repository;
//
//import hr.java.web.p315.domain.Polaznik;
//import java.util.List;
//import java.util.Optional;
//
//public interface PolaznikRepository {
//    List<Polaznik> findAll();
//    Optional<Polaznik> findById(Integer id);
//    Polaznik save(Polaznik polaznik);
//    void deleteById(Integer id);
//}