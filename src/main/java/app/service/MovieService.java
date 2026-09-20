package app.service;

import app.DTOs.DiscoverMovieDTO;
import app.DTOs.MovieDTO;
import app.DTOs.SearchResultDTO;
import app.entities.Movie;
import app.DAOs.MovieDAO;
import app.utils.Genres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovieService {
    // API KEY
    private static String apiKey = System.getenv("api_key");

    //API URLS
    private static final String URLFINDBYID = "https://api.themoviedb.org/3/find/$?external_source=imdb_id&api_key=";
    private static final String DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=$&page=";

    //APIREADER INSTANCE
    private static APIReader apiReader = new APIReader();
    private static MovieDAO movieDAO = new MovieDAO();

    public static Movie createMovie(String movieId) {
        String url = URLFINDBYID;

        String specificUrl = url.replace("$", movieId) + apiKey;

        SearchResultDTO result = apiReader.getWithJackson(specificUrl, SearchResultDTO.class);

        MovieDTO movieDTO = result.getMovieResults().get(0);

        Movie movie = DTOtoMovie(movieDTO);

        movieDAO.create(movie);

        return movie;
    }

    public static List<Movie> fetchMovies(int amount) {
        List<Movie> movies = new ArrayList<>();

        int page = 1;

        while (movies.size() < amount) {
            String url = DISCOVER_URL.replace("$", apiKey) +  page;

            DiscoverMovieDTO response = apiReader.getWithJackson(url, DiscoverMovieDTO.class);

            for (MovieDTO dto : response.getResults()) {
                movies.add(DTOtoMovie(dto));

                if (movies.size() >= amount) {
                    break;
                }
            }
            page++;
        }
        return movies;
    }

    public static void importMovies(int amount) {
        List<Movie> movies = fetchMovies(amount);

        movieDAO.createBatch(movies);

        System.out.println(movies.size() + " movies imported to database");
    }

    public static void printMoviesByGenre(String genreName) {
        // Finder genre ud fra variablen
        Integer genreId = Genres.MOVIE_GENRES.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(genreName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (genreId == null) {
            System.out.println("Genre findes ikke: " + genreName);
            return;
        }

        // Henter film fra databasen med sat genre
        Set<Movie> movies = movieDAO.get();

        // finder filmene og printer dem
        movies.stream()
                .filter(movie -> movie.getGenre_ids().contains(genreId))
                .forEach(movie -> System.out.println(movie.getTitle()));
    }


    private static Movie DTOtoMovie(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getId(), movieDTO.getTitle(), movieDTO.getOriginal_language(), movieDTO.getOverview(), movieDTO.getRelease_date(), movieDTO.getMedia_type(), movieDTO.getGenre_ids(), movieDTO.isAdult());
        return movie;
    }
}
