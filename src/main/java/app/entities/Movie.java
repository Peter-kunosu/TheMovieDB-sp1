package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String release_date;
    private String media_type;
    private List<Integer> genre_ids;
    private boolean adult;

    @ManyToMany
    @JoinTable(name = "movie_actor", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<Actor>();

    @ManyToMany
    @JoinTable(name = "movie_director", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "director_id"))
    private Set<Director> directors = new HashSet<>();

    public Movie(int id, String title, String originalLanguage, String overview, String releaseDate, String mediaType, List<Integer> genreIds, boolean adult) {
        this.id = id;
        this.title = title;
        this.original_language = originalLanguage;
        this.overview = overview;
        this.release_date = releaseDate;
        this.media_type = mediaType;
        this.genre_ids = genreIds;
        this.adult = adult;

    }
}

