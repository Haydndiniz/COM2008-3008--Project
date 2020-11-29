package Project_Code;
import Project_Code.DBController;

public abstract class User {

    protected String username;
    private String role;
    private DBController con;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
        con = new DBController("team037","ee143bc0");
    }

    //getMethods
    public DBController getDataAccessController() {
        return con;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    //setMethods
}