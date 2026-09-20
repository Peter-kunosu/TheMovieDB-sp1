package app.DAOs;

import app.config.HibernateConfig;
import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

import java.util.Set;

public class ActorDAO implements IDAO<Actor> {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Actor create(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
            return actor;
        }
    }

    @Override
    public Set<Actor> get() {
        return Set.of();
    }

    @Override
    public Actor getByID(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Actor.class, id);
        }
    }

    @Override
    public Actor update(Actor actor) {
        return null;
    }

    @Override
    public Integer delete(Actor actor) {
        return 0;
    }
}
