package mta.course.practical.skills.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GsonModel {

    public static void main(String[] args) {

        Countries countries = extractCountriesFromFile();
        System.out.println("Countries toString(): " + countries);
        System.out.println("Name of first country is: " + countries.getCountries().get(0).getName());
        System.out.println("=============================");
        countries = buildNewCountriesObject();
        String countriesJson = convertToJsonString(countries);
        System.out.println("Countries as JSON [" + countriesJson + "]");

        countriesJson = convertToJsonStringPretty(countries);
        System.out.println("Countries as JSON, but pretty !");
        System.out.println(countriesJson);

    }

    private static String convertToJsonString(Countries countries) {
        return new Gson().toJson(countries);
    }

    private static String convertToJsonStringPretty(Countries countries) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(countries);
    }

    private static Countries buildNewCountriesObject() {
        City london = new City();
        london.setName("london");
        london.setCapital(true);

        City bristol = new City();
        bristol.setName("Bristol");

        Population population = new Population();
        population.setYear(2018);
        population.setTotal(393927489);

        Country england = new Country();
        england.setName("England");
        england.setContinent("Europe");
        england.setPopulation(population);
        england.getCities().add(london);
        england.getCities().add(bristol);

        Countries countries = new Countries();
        countries.getCountries().add(england);
        return countries;
    }

    private static Countries extractCountriesFromFile() {

        Gson gson = new Gson();
        File file = new File("src\\main\\resources\\world.json");

        try {
            FileReader jsonFileContent = new FileReader(file);
            return gson.fromJson(jsonFileContent, Countries.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class Population {
        private int year;
        private int total;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Population{" +
                    "year=" + year +
                    ", total=" + total +
                    '}';
        }
    }

    private static class City {

        private String name;
        private boolean isCapital;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCapital() {
            return isCapital;
        }

        public void setCapital(boolean capital) {
            isCapital = capital;
        }

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", isCapital=" + isCapital +
                    '}';
        }
    }

    private static class Country {
        private String name;
        private String continent;
        private Population population;
        private List<City> cities;

        public Country() {
            cities = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContinent() {
            return continent;
        }

        public void setContinent(String continent) {
            this.continent = continent;
        }

        public Population getPopulation() {
            return population;
        }

        public void setPopulation(Population population) {
            this.population = population;
        }

        public List<City> getCities() {
            return cities;
        }

        public void setCities(List<City> cities) {
            this.cities = cities;
        }

        @Override
        public String toString() {
            return "Country{" +
                    "name='" + name + '\'' +
                    ", continent='" + continent + '\'' +
                    ", population=" + population +
                    ", cities=" + cities +
                    '}';
        }
    }

    private static class Countries {
        private List<Country> countries;

        public Countries() {
            countries = new ArrayList<>();
        }

        public List<Country> getCountries() {
            return countries;
        }

        public void setCountries(List<Country> countries) {
            this.countries = countries;
        }

        @Override
        public String toString() {
            return "Countries{" +
                    "countries=" + countries +
                    '}';
        }
    }

}