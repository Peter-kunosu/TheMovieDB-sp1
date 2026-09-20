package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Director {
    @Id
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "directors")
    private Set<Movie> movies = new HashSet<Movie>();

    public Director(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
