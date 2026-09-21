package app.service;

import app.DAOs.ActorDAO;
import app.DAOs.DirectorDAO;
import app.DTOs.*;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Movie;
import app.DAOs.MovieDAO;
import app.utils.Genres;

import java.util.*;
import java.util.stream.Collectors;

public class MovieService {
    // API KEY
    private static String apiKey = System.getenv("api_key");

    //API URLS
    private static final String URL_FINDBYID = "https://api.themoviedb.org/3/find/$?external_source=imdb_id&api_key=";
    private static final String DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=$&page=";
    private static final String CREDITS_URL = "https://api.themoviedb.org/3/movie/$/credits?api_key=";

    //APIREADER INSTANCE & DAOs
    private static APIReader apiReader = new APIReader();
    private static MovieDAO movieDAO = new MovieDAO();
    private static ActorDAO actorDAO = new ActorDAO();
    private static DirectorDAO directorDAO = new DirectorDAO();

    public static Movie createMovie(String movieId) {
        String url = URL_FINDBYID;

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
        }

        // Henter film fra databasen med sat genre
        Set<Movie> movies = movieDAO.get();

        // finder filmene og printer dem
        movies.stream()
                .filter(movie -> movie.getGenre_ids().contains(genreId))
                .forEach(movie -> System.out.println(movie.getTitle()));
    }

    public static CreditsDTO getCredits(Movie movie) {
        String url = CREDITS_URL.replace("$", String.valueOf(movie.getId())) + apiKey;

        return apiReader.getWithJackson(url, CreditsDTO.class);
    }

    public static List<CastDTO> getActors(Movie movie) {

        CreditsDTO credits = getCredits(movie);

        return credits.getCast();
    }

    public static List<CrewDTO> getDirectors(Movie movie) {
        CreditsDTO credits = getCredits(movie);

        return credits.getCrew()
                .stream()
                .filter(person -> "Director".equals(person.getJob()))
                .toList();
    }




    private static Movie DTOtoMovie(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getId(), movieDTO.getTitle(), movieDTO.getOriginal_language(), movieDTO.getOverview(), movieDTO.getRelease_date(), movieDTO.getMedia_type(), movieDTO.getGenre_ids(), movieDTO.isAdult());
        return movie;
    }

    private static Actor DTOtoActor (CastDTO castDTO) {
        return new Actor(castDTO.getId(), castDTO.getName());
    }

    private static Director DTOtoDirector (CrewDTO crewDTO) {
        return new Director(crewDTO.getId(), crewDTO.getName());
    }

    public static void addCreditsToMovie(Movie movie) {
        CreditsDTO credits = getCredits(movie);

        Set<Actor> actors = new HashSet<>();

        for (CastDTO castDTO : credits.getCast()) {
            Actor actor = actorDAO.getByID(castDTO.getId());

            if (actor == null) {
                actor = DTOtoActor(castDTO);
                actorDAO.create(actor);
            }

            actors.add(actor);
        }

        Set<Director> directors = new HashSet<>();

        for (CrewDTO crewDTO : credits.getCrew()) {

            if ("Director".equals(crewDTO.getJob())) {
                Director director = directorDAO.getByID(crewDTO.getId());

                if (director == null) {
                    director = DTOtoDirector(crewDTO);
                    directorDAO.create(director);
                }

                directors.add(director);
            }
        }

        movie.setActors(actors);
        movie.setDirectors(directors);

        movieDAO.update(movie);
    }

    public static void addCreditsToAllMovies() {
        Set<Movie> movies = movieDAO.get();

        for (Movie movie : movies) {
            addCreditsToMovie(movie);
        }

        System.out.println("Credits added to all movies");
    }
}
