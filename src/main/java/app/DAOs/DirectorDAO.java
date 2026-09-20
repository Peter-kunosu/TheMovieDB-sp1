package app.DAOs;

import app.config.HibernateConfig;
import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

public class DirectorDAO implements IDAO <Director> {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Director create(Director director) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();
            return director;
        }
    }

    @Override
    public Set<Director> get() {
        return Set.of();
    }

    @Override
    public Director getByID(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Director.class, id);
        }
    }

    @Override
    public Director update(Director director) {
        return null;
    }

    @Override
    public Integer delete(Director director) {
        return 0;
    }
}
