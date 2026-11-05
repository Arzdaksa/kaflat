package mta.course.practical.skills.model;

public class Route {
    private final String from;
    private final String to;
    private final boolean useTaw;

    public Route(String from, String to, boolean useTaw) {
        this.from = from;
        this.to = to;
        this.useTaw = useTaw;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isUseTaw() {
        return useTaw;
    }
}
