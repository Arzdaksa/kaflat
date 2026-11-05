package mta.course.practical.skills.Controller;

import mta.course.practical.skills.model.Route;
import mta.course.practical.skills.model.Person;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplexWorld {

    @GetMapping("/route")
    public Route calculateRoute() {
        // my complex logic goes here, and it ends with...
        Route route = new Route("CHEDERA", "GEDERA", true);
        return route;
    }







    @PostMapping(value = "/person", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updatePerson(@RequestBody Person person) {
        // do some logic with person...
        return "Successfully got Person: " + person.getFirstName() + " " + person.getLastName();
    }
}
