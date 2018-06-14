package org.sz.action.monitor.listener.dto;

/**
 * This DTO is used when client DB table row is transported.
 */
public class Client {

    private Long id;
    private String name;
    private String city;
    private int age;

    /**
     * Constructor.
     */
    public Client() {
    }

    /**
     * Constructor.
     *
     * @param id   id of the client
     * @param name name of the client
     * @param city city of the client
     * @param age  age of the client
     */
    public Client(Long id, String name, String city, int age) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", age=" + age +
                '}';
    }
}
