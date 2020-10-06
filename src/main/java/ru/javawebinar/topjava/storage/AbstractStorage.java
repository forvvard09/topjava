package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;


public abstract class AbstractStorage<SK> implements Storage {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractStorage.class);

    protected abstract void doSave(SK key, Meal newMeal);

    protected abstract Meal getFromStorage(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Meal newMeal);

    protected abstract void doDelete(SK searchKey);

    protected abstract SK getPosition(SK uuid);

    protected abstract List<Meal>doCopyAll();

    @Override
    public void save(Meal meal) {
        LOG.debug("Save: " + meal);
        doSave((SK) meal.getUuid(), meal);
    }

    @Override
    public Meal get(String key) {
        LOG.debug("get meal by key: " + key);
        return getFromStorage((SK) key);
    }

    @Override
    public void delete(String key) {
        LOG.debug("Delete meal by key: " + key);
        doDelete((SK) key);
    }

    @Override
    public void update(Meal newMeal) {
        LOG.debug("Update meal: " + newMeal);
        doUpdate((SK) newMeal.getUuid(), newMeal);

    }

    @Override
    public List<Meal> getAll() {
        LOG.info("getAll");
        return doCopyAll();
    }
}
