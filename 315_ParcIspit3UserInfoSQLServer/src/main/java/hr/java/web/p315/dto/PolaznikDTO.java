package hr.java.web.p315.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolaznikDTO {
    private String ime;
    private String prezime;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // omogućuje samo unos, ne vraća u GET-u
    private String password;
}
