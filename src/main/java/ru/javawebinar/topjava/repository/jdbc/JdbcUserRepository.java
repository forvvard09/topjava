package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            addRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            removeRoles(user);
            addRoles(user);
        }
        return user;
    }

    private void addRoles(User user) {
        List<Role> listRole = new ArrayList<Role>(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, listRole.get(i).name());
                    }

                    @Override
                    public int getBatchSize() {
                        return listRole.size();
                    }
                });
    }

    private void removeRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (users.size() > 0) {
            user.setRoles(addRoles(user.id()));
        }
        return user;
    }

    private Collection<Role> addRoles(int id) {
        List<Role> listRole = jdbcTemplate.query("SELECT * FROM  user_roles WHERE user_id=?",
                (rs, rowNum) -> Role.valueOf(rs.getString("role")), id);
        return listRole;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        //to do
        User user = DataAccessUtils.singleResult(users);
        user.setRoles(addRoles(user.getId()));
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM  user_roles");
        for (User user : userList) {
            List<Role> listRole = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                Role role = Role.valueOf((String) row.get("role"));
                int userId = (int) row.get("user_id");
                if (user.id() == userId) {
                    listRole.add(role);
                }
            }
            user.setRoles(listRole);
        }
        return userList;
    }
}
