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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            removeRoles(user);
        }
        addRoles(user);
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
        return getWithRoles(users);
    }

    private User getWithRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> listRole = jdbcTemplate.query("SELECT * FROM  user_roles WHERE user_id=?",
                    (resultSet, rowsNumber) -> Role.valueOf(resultSet.getString("role")), user.id());
            if (listRole.size() > 0) {
                user.setRoles(listRole);
            }
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getWithRoles(users);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> userRoleMap = this.jdbcTemplate.query("SELECT * FROM  user_roles", (ResultSet rs) -> {
            HashMap<Integer, Set<Role>> mapRet = new HashMap<>();
            while (rs.next()) {
                Integer userId = rs.getInt("user_id");
                Role role = Role.valueOf(rs.getString("role"));


                // map - method merge
                /*
                mapRet.merge(userId, new HashSet<>(Collections.singletonList(role)), (oldValue, newValue) -> {
                    oldValue.addAll(newValue);
                    return oldValue;
                });
                 */

                // через computeIfAbsent - так красивее
                mapRet.computeIfAbsent(userId, value -> new HashSet<>())
                        .add(role);
            }
            return mapRet;
        });

        //второй способ, можно сделать мапу так
        /*
        Map<Integer, List<Role>> userRoleMap = jdbcTemplate.query("SELECT * FROM  user_roles", new ResultSetExtractor<Map>() {
            @Override
            public Map extractData(ResultSet rs) throws SQLException, DataAccessException {
                HashMap<Integer, List<Role>> mapRet = new HashMap<>();
                while (rs.next()) {
                    Integer userId = rs.getInt("user_id");
                    Role role = Role.valueOf((String) rs.getString("role"));
                    if (mapRet.containsKey(userId)) {
                        List<Role> name = mapRet.get(userId);
                        name.add(role);
                        mapRet.put(1, name);
                    } else {
                        List<Role> newList = new ArrayList<>();
                        newList.add(role);
                        mapRet.put(userId, newList);
                    }
                }
                return mapRet;
            }
            };
      */

        // тут возможно надо сделать не null, а user, если это будет в дальнейшем роль по умолчанию
        userList.forEach(user -> user.setRoles(userRoleMap.getOrDefault(user.getId(), Collections.emptySet())));

        return userList;
    }

}
