package app.DAOs;

import app.config.HibernateConfig;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieDAO implements IDAO<Movie> {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    public List<Movie> createBatch(List<Movie> movies) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            int count = 0;

            for (Movie movie : movies) {
                Movie existingMovie =
                        em.find(Movie.class, movie.getId());
                if (existingMovie == null) {
                    em.persist(movie);
                }

                count++;

                if (count % 50 == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.getTransaction().commit();
            return movies;
        }
    }

    @Override
    public Set<Movie> get() {
        try (EntityManager em = emf.createEntityManager()) {

            return new HashSet<>(
                    em.createQuery("SELECT m FROM Movie m", Movie.class)
                            .getResultList()
            );
        }
    }

    @Override
    public Movie getByID(Long id) {
        try (EntityManager em = emf.createEntityManager()) {

            return em.find(Movie.class, id);
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            Movie updatedMovie = em.merge(movie);

            em.getTransaction().commit();

            return updatedMovie;
        }
    }

    @Override
    public Integer delete(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            Movie managedMovie = em.find(Movie.class, movie.getId());

            if (managedMovie != null) {
                em.remove(managedMovie);
            }

            em.getTransaction().commit();

            return movie.getId();
        }
    }
}

