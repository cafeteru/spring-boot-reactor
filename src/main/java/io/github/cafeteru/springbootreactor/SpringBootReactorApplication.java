package io.github.cafeteru.springbootreactor;

import io.github.cafeteru.springbootreactor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var first = createFluxFromJust();
        subscribeExample(first);

        var users = List.of(
                new User("John", "Doe"),
                new User("Jane", "D"));
        var fluxUsers = Flux.fromIterable(users);
        subscribeExample(fluxUsers);
    }

    private static Flux<User> createFluxFromJust() {
        return Flux
                // Crea el observable
                .just("John", "Jane", "Doe", "Foo", "", "Bar")
                // Crea una pipe
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("Empty string");
                    }
                    System.out.println("Nombre: " + e);
                })
                .map(String::toUpperCase)
                .filter(value -> value.length() > 3)
                .map(value -> new User(value, value))
                .map(user -> {
                    user.setName(user.getName().toLowerCase());
                    return user;
                });

    }

    private static void subscribeExample(Flux<?> flux) {
        flux.subscribe(
                value -> log.info(String.valueOf(value)), // onSucess
                error -> log.error(error.getMessage()), // onError
                () -> log.info("Process complete")); // onSuccess, se ejecuta si no hay errores después del onSucess
    }
}
