package hk.ust.cse.hunkim.questionroom.question;

import android.widget.Toast;

import hk.ust.cse.hunkim.questionroom.MainActivity;

/**
 * Created by JT on 11/10/2015.
 */
public class User {

    private String ITSC;
    private String RPGclass;
    private int level;
    private int XP;

    public static final int LEVEL_INCREMENT = 20;

    public User(String logininfo)
    {
        ITSC = logininfo;
        level = 1;
        XP = 0;
        RPGclass = "Soldier"; //idk
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
    }


}
