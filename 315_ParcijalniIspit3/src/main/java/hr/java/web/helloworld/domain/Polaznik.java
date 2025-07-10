package hr.java.web.helloworld.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Polaznik") // Eksplicitno navodimo ime tablice
public class Polaznik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPolaznik") // Eksplicitno ime kolone točno onako kako je u bazi
    private Integer id;

    @Column(name = "Ime", nullable = false, length = 100) // Eksplicitno ime kolone
    private String ime;

    @Column(name = "Prezime", nullable = false, length = 100) // Eksplicitno ime kolone
    private String prezime;
}
