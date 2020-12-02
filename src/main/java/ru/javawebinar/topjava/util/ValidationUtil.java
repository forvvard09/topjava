package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id, String nameService) {
        checkNotFoundWithId(object != null, id, nameService);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id, String nameService) {
        checkNotFound(found, "id=" + id, nameService);
    }

    public static <T> T checkNotFound(T object, String msg, String nameService) {
        checkNotFound(object != null, msg, nameService);
        return object;
    }

    public static void checkNotFound(boolean found, String msg, String nameService) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg + " service: " + nameService);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }
}