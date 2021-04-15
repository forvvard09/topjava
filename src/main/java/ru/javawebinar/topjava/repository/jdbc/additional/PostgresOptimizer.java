package ru.javawebinar.topjava.repository.jdbc.additional;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;

@Configuration
@Profile(Profiles.POSTGRES_DB)
public class PostgresOptimizer extends AbstrtactFormatOptimizer<LocalDateTime> {

    @Override
    public LocalDateTime doDateForDB(LocalDateTime inputDateTime) {
        return inputDateTime;
    }
}
