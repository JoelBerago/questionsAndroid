package hk.ust.cse.hunkim.questionroom.question;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hk.ust.cse.hunkim.questionroom.MainActivity;
import hk.ust.cse.hunkim.questionroom.R;

/**
 * Created by JT on 11/10/2015.
 */
public class User {

    private int userid; //used as unique identifier
    private String username;
    private Hierarchy RPGclass;
    private int level;
    private int XP; private int percent;
    private boolean jail;

    public static final int LEVEL_INCREMENT = 20;
    public enum Hierarchy{
        //http://crunchify.com/why-and-for-what-should-i-use-enum-java-enum-examples/

        Farmer(1), Servant(2), Knight(3), Lord(4), Monarch(5);

        private int value;
        Hierarchy(int value)
        {this.value = value;}

        void classUP()
        {value++;}
    }

    //constructor should ask for ITSC, username, XP, and jail from backend
    public User(int id, String username, int totalXP, boolean jail, View view)
    {
        this.userid = id;
        this.username = username;
        this.jail = jail;

        //backend XP is total, frontend is XP above current level. Need to convert
        XP = totalXP;
        level = 1;
        RPGclass = Hierarchy.Farmer;
        while(XP > (level*LEVEL_INCREMENT))
        {
            XP -= level*LEVEL_INCREMENT;
            level++;
            if(level%10==0 && level<45) {
                RPGclass.classUP();
                TextView textView = (TextView) view.findViewById(R.id.profileCharacterClass);
                textView.setText(getRPGClass()+" Lv. "+getLevel());
            }
        }
        percent = (100*XP)/(level*LEVEL_INCREMENT);
    }

    public String getRPGClass()
    {
        return RPGclass.toString();
    }

    public int getLevel()
    {
        return level;
    }

    public int getXP()
    {
        return XP;
    }

    public void addXP(int amount, View view)
    {
        XP += amount;
        if(XP>=(level*LEVEL_INCREMENT)) //level up!
        {
            XP -= level * LEVEL_INCREMENT; //keep balance
            level++;
            //Toast.makeText(MainActivity.this, "Level up! Congratulations!", Toast.LENGTH_SHORT).show();
            //currently doesn't work, MainActivity.this is not an accessible instance
            if (level % 10 == 0 && level < 45) RPGclass.classUP();

            TextView textView = (TextView) view.findViewById(R.id.profileCharacterClass);
            textView.setText(getRPGClass() + " Lv " + getLevel());
        }
        //assume it is impossible to gain enough XP to level up twice at once.

        percent = (100*XP)/(level*LEVEL_INCREMENT);
        ProgressBar expBar = (ProgressBar) view.findViewById(R.id.experienceBar);
        expBar.setProgress(percent);
    }


}
