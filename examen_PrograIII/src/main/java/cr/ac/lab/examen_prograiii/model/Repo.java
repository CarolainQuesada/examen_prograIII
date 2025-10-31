package cr.ac.lab.examen_prograiii.model;

public class Repo {
    private String name;
    private String description;
    private String language;
    private int stargazers_count;
    private int forks_count;
    private String updated_at;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLanguage() { return language; }
    public int getStargazers_count() { return stargazers_count; }
    public int getForks_count() { return forks_count; }
    public String getUpdated_at() { return updated_at; }
}
