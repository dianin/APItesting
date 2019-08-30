package Models;

public class User {

    public Integer id;
    private String job;
    private String name;

    public User(String name, String job, Long id) {
    }

    public User( String job, String name) {
        this.job = job;
        this.name = name;
    }


    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Models.User{" +
                ", job='" + job + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
