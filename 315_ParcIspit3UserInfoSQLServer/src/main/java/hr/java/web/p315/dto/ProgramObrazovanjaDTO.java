package hr.java.web.p315.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramObrazovanjaDTO {
    private String naziv;
    private Integer csvet;
}