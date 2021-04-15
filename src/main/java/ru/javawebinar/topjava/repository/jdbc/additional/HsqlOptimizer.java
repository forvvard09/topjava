package ru.javawebinar.topjava.repository.jdbc.additional;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Configuration
@Profile(Profiles.HSQL_DB)
public class HsqlOptimizer extends AbstrtactFormatOptimizer<Timestamp> {

    @Override
    public Timestamp doDateForDB(LocalDateTime inputDateTime) {
        return Timestamp.valueOf(inputDateTime);
    }
}
