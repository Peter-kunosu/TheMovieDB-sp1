package app;

import app.entities.Movie;
import app.service.MovieService;

public class Main {
    public static void main(String[] args) {

        /*Movie movie = MovieService.createMovie("tt17663992");
        System.out.println("filmen blev gemt");
        System.out.println(movie);*/

        MovieService.importMovies(6);

        MovieService.addCreditsToAllMovies();

        MovieService.printMoviesByGenre("Horror");


    }
}
