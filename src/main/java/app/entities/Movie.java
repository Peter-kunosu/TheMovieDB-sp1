package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
public class Movie {
    @Id
    private int id;

    private String title;
    private String original_language;
    private String overview;
    private String release_date;
    private String media_type;
    private List<Integer> genre_ids;
    private boolean adult;
}

