package app.service;

import app.DTOs.MovieDTO;
import app.DTOs.SearchResultDTO;
import app.entities.Movie;
import app.DAOs.MovieDAO;

public class MovieService {
    // API KEY
    private static String apiKey = System.getenv("api_key");

    //API URLS
    private static final String URLFINDBYID = "https://api.themoviedb.org/3/find/$";

    //APIREADER INSTANCE
    private static APIReader apiReader = new APIReader();
    private static MovieDAO movieDAO = new MovieDAO();

    public static Movie createMovie(String movieId) {
        String url = URLFINDBYID;

        String specificUrl = url.replace("$", movieId);

        SearchResultDTO result = apiReader.getWithJackson(specificUrl, SearchResultDTO.class);

        MovieDTO movieDTO = result.getMovieResults().get(0);

        Movie movie = DTOtoMovie(movieDTO);

        movieDAO.create(movie);

        return movie;
    }


    private static Movie DTOtoMovie(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getId(), movieDTO.getTitle(), movieDTO.getOriginal_language(), movieDTO.getOverview(), movieDTO.getRelease_date(), movieDTO.getMedia_type(), movieDTO.getGenre_ids(), movieDTO.isAdult());
        return movie;
    }
}
