package ru.javawebinar.topjava.config;


import ru.javawebinar.topjava.storage.MealsMemoryStorage;
import ru.javawebinar.topjava.storage.Storage;

import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

public class Config {

    private static final Config INSTANCE = new Config();

    private final Storage storage;

    private Config() {
        storage = new MealsMemoryStorage();
        MEALS.forEach(storage::save);
    }

    public static Config get() {
        return INSTANCE;
    }

    public Storage getStorage() {
        return storage;
    }
}
