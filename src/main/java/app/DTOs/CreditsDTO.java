package app.DTOs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class CreditsDTO {
    @JsonProperty("id")
    private int id;
    @JsonProperty("cast")
    private List<CastDTO> cast;
    @JsonProperty("crew")
    private List<CrewDTO> crew;
}
