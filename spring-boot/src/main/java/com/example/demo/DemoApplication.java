package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Controller
class GraphqlUserController {

    private final UserRepository userRepository;

    GraphqlUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryMapping
    User usersByName(@Argument String name) {
        return this.userRepository.byUsername(name);
    }

    @QueryMapping
    Collection<User> users() {
        return this.userRepository.all();
    }
}


@Controller
@ResponseBody
class RestUserController {

    private final UserRepository userRepository;

    RestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    User add(@RequestParam String username) {
        this.userRepository.createUser(username);
        return this.userRepository.byUsername(username);
    }

    @DeleteMapping("/users/{name}")
    void deleteByName(@PathVariable String name) {
        this.userRepository.deleteByName(name);
    }

    @GetMapping("/users/{name}")
    User getByName(@PathVariable String name) {
        return this.userRepository.byUsername(name);
    }

    @GetMapping("/users")
    Collection<User> users() {
        return this.userRepository.all();
    }
}

@Repository
class UserRepository {

    private final JdbcTemplate db;

    UserRepository(JdbcTemplate db) {
        this.db = db;
    }

    Collection<User> all() {
        return this.db.query("select * from notes_user ", (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"),
                new ArrayList<>()));
    }

    User byUsername(String username) {
        var all = this.db.query(
                "select * from notes_user where username = ? ",
                (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"),
                        new ArrayList<>()), username);
        return all.iterator().next();
    }

    void createUser(String userame) {
        var updated = this.db.update(
                "insert into notes_user ( username ) values(?)", userame);
    }

    public void deleteByName(String name) {
        this.db.update("delete from notes_user where username = ?", name);
    }
}

record User(Integer id, String username, List<Note> notes) {
}

record Note(Integer id, String title, String content) {
}