package mta.course.practical.skills.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
//        throw new RuntimeException("An error occurred");
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam String name, @RequestParam String last) {
        return "Hello " + name + " " + last.toUpperCase();
    }

}
