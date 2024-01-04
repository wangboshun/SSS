package org.example.model;

public class Event {
    public String user;
    public String type;
    public String url;

    public Long cnt;

    @Override
    public String toString() {
        return "Event{" +
                "user='" + user + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", cnt=" + cnt +
                '}';
    }

    public Event() {

    }

    public Event(String user, String url) {
        this.url = url;
        this.user = user;
    }

    public Event(String user, String url, String type) {
        this.url = url;
        this.user = user;
        this.type = type;
    }

    public Event(String user, String url, String type,long cnt) {
        this.url = url;
        this.user = user;
        this.type = type;
        this.cnt=cnt;
    }
}
