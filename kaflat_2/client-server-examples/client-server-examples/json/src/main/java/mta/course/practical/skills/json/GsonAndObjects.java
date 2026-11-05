package mta.course.practical.skills.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GsonAndObjects {

    public static void main(String[] args) {

        // creates some customers and put them in a list
        Customer mosh = new Customer("Mosh", 34);
        Customer tikva = new Customer("Tikva", 69);
        List<Customer> myCustomers = new ArrayList<>();
        myCustomers.add(mosh);
        myCustomers.add(tikva);

        Gson gson = new Gson();
        String objectAsJsonString = gson.toJson(myCustomers);
        System.out.println(objectAsJsonString);
        /*
            [{
                "age": 34,
                "name": "Mosh"
             }, {
                "age": 69,
                "name": "Tikva"
             }
            ]
         */

        // deserialize simple JSON from JSON String to generic object
        String singleCustomerAsJsonString = "{\"age\":34,\"name\":\"Mosh\"}";
        JsonObject jsonObject = JsonParser.parseString(singleCustomerAsJsonString).getAsJsonObject();
        System.out.println("Name: " + jsonObject.get("name").getAsString());
        System.out.println("Age: " + jsonObject.get("age").getAsInt());

        // deserialize json array from JSON String to generic array
        String myCustomersAsJsonString = "[" +
                "{\"age\":34,\"name\":\"Mosh\"}," +
                "{\"age\":69,\"name\":\"Tikva\"}" +
                "]";
        JsonArray jsonArray = JsonParser.parseString(myCustomersAsJsonString).getAsJsonArray();
        jsonObject = jsonArray.get(0).getAsJsonObject();
        System.out.println("First Customer Name: " + jsonObject.get("name").getAsString());
        System.out.println("First Customer Age: " + jsonObject.get("age").getAsInt());

        jsonObject = jsonArray.get(1).getAsJsonObject();
        System.out.println("Second Customer Name: " + jsonObject.get("name").getAsString());
        System.out.println("Second Customer Age: " + jsonObject.get("age").getAsInt());

    }
    
    private static class Customer {
        private int age;
        private String name;

        public Customer(String name, int age) {
            this.age = age;
            this.name = name;
        }

    }
}
