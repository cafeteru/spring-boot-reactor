package io.github.cafeteru.springbootreactor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootReactorApplication.class, args);
  }

  @Override
  public void run(String... args) {
    Flux<String> names = Flux
      // Crea el observable
      .just("John", "Jane", "Doe", "Foo", "Bar")
      // Crea una pipe
      .doOnNext(System.out::println);
    names.subscribe();
  }
}
