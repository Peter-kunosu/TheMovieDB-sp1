package app.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    @JsonProperty("adult")
    boolean adult;
    @JsonProperty("backdrop_path")
    String backdrop_path;
    @JsonProperty("id")
    int id;
    @JsonProperty("title")
    String title;
    @JsonProperty("original_language")
    String original_language;
    @JsonProperty("original_title")
    String original_title;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("poster_path")
    String poster_path;
    @JsonProperty("media_type")
    String media_type;
    @JsonProperty("genre_ids")
    List<Integer> genre_ids;
    @JsonProperty("popularity")
    float popularity;
    @JsonProperty("release_date")
    String release_date;
    @JsonProperty("video")
    boolean video;
    @JsonProperty("vote_average")
    float vote_average;
    @JsonProperty("vote_count")
    int vote_count;
}
