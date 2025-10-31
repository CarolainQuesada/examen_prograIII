package cr.ac.lab.examen_prograiii.model;

public class User {
    private String login;
    private String name;
    private String location;
    private String bio;
    private String blog;
    private String avatar_url;
    private String created_at;
    private int followers;
    private int following;

    public User() {}

    public String getLogin() { return login; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getBio() { return bio; }
    public String getBlog() { return blog; }
    public String getAvatar_url() { return avatar_url; }
    public String getCreated_at() { return created_at; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }

    public void setLogin(String login) { this.login = login; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setBio(String bio) { this.bio = bio; }
    public void setBlog(String blog) { this.blog = blog; }
    public void setAvatar_url(String avatar_url) { this.avatar_url = avatar_url; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public void setFollowers(int followers) { this.followers = followers; }
    public void setFollowing(int following) { this.following = following; }
    
}
