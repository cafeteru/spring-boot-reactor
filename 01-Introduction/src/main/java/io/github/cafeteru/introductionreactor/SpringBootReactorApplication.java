package io.github.cafeteru.introductionreactor;

import io.github.cafeteru.introductionreactor.model.Comment;
import io.github.cafeteru.introductionreactor.model.User;
import io.github.cafeteru.introductionreactor.model.UserWithComments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        var first = createFluxFromJust();
//        subscribeExample(first);
//
//        var users = List.of(
//                new User("John", "Doe"),
//                new User("Jane", "D"));
//        var fluxUsers = Flux.fromIterable(users);
//        subscribeExample(fluxUsers);
//
//        var second = createFluxWithFlatMap();
//        subscribeExample(second);
//
//        createFluxWithUserWithComments();
//        createFluxWithZipWith();
//        createFluxWithZipWithAndRange();
//        createFluxWithInterval();
        System.out.println(marsExploration("SOS OOS OSO SOS OSS OSO SOS OSO SOS"));
    }

    public static int marsExploration(String s) {
        // Write your code here
        int contador = 0;
        String subcadena = "SOS";
        int indice = s.indexOf(subcadena);
        while (indice != -1) {
            contador++;
            indice = s.indexOf(subcadena, indice + 1);
        }
        return contador;
    }

    private static Flux<User> createFluxFromJust() {
        log.info("createFluxFromJust");
        return Flux
                // Crea el observable
                .just("John", "Jane", "Doe", "Foo", "", "Bar")
                // Crea una pipe
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("Empty string");
                    }
                    System.out.println("Name: " + e);
                })
                .map(String::toUpperCase)
                .filter(value -> value.length() > 3)
                .map(value -> new User(value, value))
                .map(user -> {
                    user.setName(user.getName().toLowerCase());
                    return user;
                });

    }

    private static Flux<User> createFluxWithFlatMap() {
        log.info("createFluxWithFlatMap");
        var users = List.of(
                new User("John", "Doe"),
                new User("Jane", "D"));
        return Flux.fromIterable(users)
                // flatMap: recibe un objeto y retorna un observable, parecido a un SwitchMap
                .flatMap(user -> user.getName().equalsIgnoreCase("foo") ?
                        Mono.just(user) : Mono.empty())
                .map(user -> {
                    user.setName(user.getName().toLowerCase());
                    return user;
                });
    }

    private static void createFluxWithUserWithComments() {
        log.info("createFluxWithUserWithComments");
        var userMono = Mono.fromCallable(() -> new User("John", "Doe"));
        var commentMono = Mono.fromCallable(() -> {
            var comment = new Comment();
            comment.addComment("Hello");
            comment.addComment("World");
            comment.addComment("From");
            comment.addComment("Reactor");
            return comment;
        });
        var userWithCommentsMono = userMono.flatMap(user -> commentMono.map(comment -> new UserWithComments(user, comment)));
        userWithCommentsMono.subscribe(userWithComments -> log.info(userWithComments.toString()));
    }

    private static void createFluxWithZipWith() {
        log.info("createFluxWithZipWith");
        var userMono = Mono.fromCallable(() -> new User("John", "Doe"));
        var commentMono = Mono.fromCallable(() -> {
            var comment = new Comment();
            comment.addComment("Hello");
            comment.addComment("World");
            comment.addComment("From");
            comment.addComment("Reactor");
            return comment;
        });
        // zipWith: Combina dos flujos
        // var userWithCommentsMono = userMono.zipWith(commentMono).map(tuple -> new UserWithComments(tuple.getT1(), tuple.getT2()));
        var userWithCommentsMono = userMono.zipWith(commentMono, UserWithComments::new);
        userWithCommentsMono.subscribe(userWithComments -> log.info(userWithComments.toString()));
    }

    private void createFluxWithZipWithAndRange() {
        log.info("createFluxWithZipWithAndRange");
        Flux<Integer> range = Flux.range(0, 5);
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * 2)
                .zipWith(range, (justValue, rangeValue) ->
                        String.format("First Flux: %d, Second Flux: %d", justValue, rangeValue))
                .subscribe(log::info);
    }

    private void createFluxWithInterval() {
        log.info("createFluxWithInterval");
        Flux<Integer> range = Flux.range(0, 12);
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        range.zipWith(interval, (rangeValue, intervalValue) ->
                        String.format("First Flux: %d, Second Flux: %d", rangeValue, intervalValue))
                .doOnNext(log::info)
                .blockLast(); // Para forzar que espere a que termine el proceso
    }

    private static void subscribeExample(Flux<?> flux) {
        flux.subscribe(
                value -> log.info(String.valueOf(value)), // onSuccess
                error -> log.error(error.getMessage()), // onError
                () -> log.info("Process complete")); // onSuccess, se ejecuta si no hay errores después del onSuccess
    }
}
