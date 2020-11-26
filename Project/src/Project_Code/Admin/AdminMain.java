package Project_Code.Admin;

import Project_Code.*;

/**
 * AdminMain inherits user
 * Implements all actions carried out by Admin role
 */
public class AdminMain extends User {


    private DBController dac = super.getDataAccessController();

    public AdminMain(String username) {
        super(username, "Administrator");
    }



}
