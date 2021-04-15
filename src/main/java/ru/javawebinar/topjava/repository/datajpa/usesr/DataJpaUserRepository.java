package ru.javawebinar.topjava.repository.datajpa.usesr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Repository
@Transactional(readOnly = true)
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudUserRepository = crudRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudUserRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudUserRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudUserRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    @Autowired
    private MealService mealService;


    @Transactional
    public User getWithMeals(Integer id) {
        User user = checkNotFoundWithId(get(id), id);
        List<Meal> meals = mealService.getAll(id);
        //List<Meal> mealsCheck = meals.isEmpty() ? null : meals;
        user.setMeals(meals);
        return user;
    }
}
