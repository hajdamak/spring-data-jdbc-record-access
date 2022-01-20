package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

record Account(
        @Id String id,
        @Version int version,
        String name
) {
    public Account {
        System.out.println("Constructor is accessed: (" + id + ", " + version + ", " + name + ")");
        if (id == null) id = UUID.randomUUID().toString();
    }

    public String name() {
        System.out.println("Field is accessed.");
        return name;
    }
}

@Repository
interface AccountRepo extends CrudRepository<Account, String> {
}

@SpringBootApplication
public class App {

    @Autowired
    private AccountRepo repo;

    public static void main(final String[] args) {
        final var app = new SpringApplicationBuilder(App.class).build();
        app.run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

        System.out.println("Saving record ...");
        final var a1 = new Account(null, 0, "test");
        final var a2 = repo.save(a1);

        System.out.println("Loading record ...");
        final var a3 = repo.findById(a2.id());
        a1.name();
    }

}
