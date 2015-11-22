package hk.ust.cse.hunkim.questionroom.db;


import android.view.View;
import android.widget.ImageView;

import hk.ust.cse.hunkim.questionroom.R;

/**
 * Created by mikacuy on 11/18/15.
 */
public class UserHelper {
    public static final String PREFS_NAME = "LoginPrefs";

    //returns the level given experience
    //to level up we need current_level*20 exp
    public static int getLevel(int experience){
        int level=1;
        while(experience>level*20){
            experience=experience-level*20;
            level++;
        }
        return level;
    }

    //gets the amount of residual experience
    public static int getResidualExperience(int experience){
        int level=getLevel(experience);
        return experience-(level*(level+1))/2;
    }

    public static void setCharacterImage(int level, ImageView view){
        if(level<=5){
            view.setImageResource(R.drawable.class1);
        }
        else if(level<=10){
            view.setImageResource(R.drawable.class2);
        }
        else if(level<=15){
            view.setImageResource(R.drawable.class3);
        }
        else if(level<=20){
            view.setImageResource(R.drawable.class4);
        }
        else{
            view.setImageResource(R.drawable.class5);
        }

    }

    public static String getUserClass(int level){
        if(level<=5){
            return "Farmer";
        }
        else if(level<=10){
            return "Servant";
        }
        else if(level<=15){
            return "Knight";
        }
        else if(level<=20){
            return "Lord";
        }
        else{
            return "Monarch";
        }
    }

}
