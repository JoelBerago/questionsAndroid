package hk.ust.cse.hunkim.questionroom.question;

import android.widget.Toast;

import hk.ust.cse.hunkim.questionroom.MainActivity;

/**
 * Created by JT on 11/10/2015.
 */
public class User {

    private String ITSC; //used as unique identifier
    private String username;
    private String RPGclass;
    private int level;
    private int XP; private float percent;
    private boolean jail;

    public static final int LEVEL_INCREMENT = 20;

    //constructor should ask for ITSC, username, XP, and jail from backend
    public User(String ITSC, String username, int totalXP, boolean jail)
    {
        this.ITSC = ITSC;
        this.username = username;
        this.jail = jail;

        //backend XP is total, frontend is XP above current level. Need to convert
        XP = totalXP;
        level = 1;
        while(XP > (level*LEVEL_INCREMENT))
        {
            XP -= level*LEVEL_INCREMENT;
            level++;
        }
        percent = (float) XP/(level*LEVEL_INCREMENT);
        RPGclass = "Knight"; //to be determined
    }

    public void setRPGClass(String newclass)
    {
        RPGclass=newclass;
    }

    public String getRPGClass()
    {
        return RPGclass;
    }

    public int getLevel()
    {
        return level;
    }

    public int getXP()
    {
        return XP;
    }

    public void addXP(int amount)
    {
        XP += amount;
        if(XP>=(level*LEVEL_INCREMENT)) //level up!
        {
            XP -= level*LEVEL_INCREMENT; //keep balance
            level++;
            //Toast.makeText(MainActivity.this, "Level up! Congratulations!", Toast.LENGTH_SHORT).show();
            //currently doesn't work, MainActivity.this is not an accessible instance
        }
        //assume it is impossible to gain enough XP to level up twice at once.

        percent = (float) XP/(level*LEVEL_INCREMENT);
    }


}
