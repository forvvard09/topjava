package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    // тут работаем напрямую через хибер, а не JPA
    /*
    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }
   */

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);

        if (meal.isNew()) {
            em.persist(meal);
        } else {
            /* при использовании этого метода не будет использоваться бин валидация
            Query query = em.createQuery("UPDATE Meal m SET m.description=:description, m.calories=:calories, m.dateTime=:dateTime WHERE m.id=:id AND m.user.id=:userId");
            if (query.setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() == 0) {
                return null;
            }
             */
            Meal mealFind = em.find(Meal.class, meal.getId());
            return mealFind.getUser().getId() == userId ? em.merge(meal) : null;
        }
        return meal;
    }

    @Override
    @Transactional
    //1. Способ (самый лучший, проверяется при загрузке приложения, поэтому хорош

    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter(1, userId)
                .setParameter(2, id)
                .executeUpdate() != 0;
    }
    //2. Способ
    /*
    public boolean delete(int id, int userId) {
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.user.id=:userId AND m.id=:id");
        return query.setParameter("userId", userId)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
     */

    @Override
    public Meal get(int id, int userId) {

        //кидаем NoResultException, вмест ожидаемого NotFoundException, поэтому нам не подходит
        /*
        return (Meal) em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.id=:id")
                .setParameter("userId", userId)
                .setParameter("id", id)
                .getSingleResult();
         */

        Meal meal = em.find(Meal.class, id);
        return meal != null && meal.getUser().getId() == userId ? meal : null;

        //еще один способ
        /*
        List<Meal> meals = em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.id=:id")
                .setParameter("userId", userId)
                .setParameter("id", id)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
         */
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class).
                setParameter("userId", userId).
                getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}