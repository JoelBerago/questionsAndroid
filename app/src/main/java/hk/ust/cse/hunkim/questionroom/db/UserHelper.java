package hk.ust.cse.hunkim.questionroom.db;


/**
 * Created by mikacuy on 11/18/15.
 */
public class UserHelper {
    public static final String PREFS_NAME = "LoginPrefs";

    //returns the level given experience
    public static int getLevel(int experience){
        return experience/20;
    }

    //gets the amount of residual experience
    public static int getResidualExperience(int experience){
        return experience-(experience/20);
    }

}
