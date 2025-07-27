package hr.java.web.p315.domain;

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
@Table(name = "ProgramObrazovanja_Table") // Eksplicitno ime tablice
public class ProgramObrazovanja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDProgramObrazovanja") // Eksplicitno ime kolone
    private Integer id;

    @Column(name = "Naziv", nullable = false, length = 100) // Eksplicitno ime kolone
    private String naziv;

    @Column(name = "CSVET", nullable = false) // Eksplicitno ime kolone
    private Integer csvet;
}
