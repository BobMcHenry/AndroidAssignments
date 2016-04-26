package com.bobmchenry.ad340.week2assignment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * Main Activity, uses the roller fragment and stat-block fragment to create
 * an interactive dice roller that tracks the stats of an unmodified d20 roll,
 * which will show it's linear disbursement as the roll count increases.
 *
 * @Author Bob McHenry
 *
 */
public class RollerWithStats extends AppCompatActivity {

    // Local fields for showing stats.
    int numRolls, nat20, nat1, rollSum;
    double avgRoll;
    int[] dieResults;
    SharedPreferences sharedPref;

    @Override
    /**
     * onCreate is overridden to add our two fragments, grab our collected stats
     * from shared prefs and populate the initial view.
    **/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Adding fragments using managers and transactions.
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        RollerFragment rf = new RollerFragment();
        StatsFragment sf = new StatsFragment();

        ft.add(R.id.rollFragmentView, rf);
        ft.add(R.id.statsFragmentView, sf);

        // Initialize shared prefs
        sharedPref = getPreferences(MODE_PRIVATE);

        // Get values, if the exist, or populate shared prefs with 0 values
        SharedPreferences.Editor spEdit = sharedPref.edit();
        spEdit.putInt("numRolls", sharedPref.getInt("numRolls", 0));
        spEdit.putInt("nat20", sharedPref.getInt("nat20", 0));
        spEdit.putInt("nat1", sharedPref.getInt("nat1", 0));
        spEdit.putInt("rollSum", sharedPref.getInt("rollSum", 0));

        dieResults = new int[20];
        for (int i = 0; i < dieResults.length; i++){
            String key = "hitsOn"+String.valueOf(i+1);
            spEdit.putInt(key, sharedPref.getInt(key, 0));
            dieResults[i] = sharedPref.getInt(key,0);
        }

        // Commit changes to shared prefs
        spEdit.commit();

        //Assign values to fields from shared prefs for operations and display
        numRolls = sharedPref.getInt("numRolls", 0);
        nat20 = sharedPref.getInt("nat20", 0);
        nat1 = sharedPref.getInt("nat1", 0);
        rollSum = sharedPref.getInt("rollSum",0);
        avgRoll = (double)rollSum/numRolls;


        //Print stats from fields
        updateText();

    }

    /**
     * Roll a die and update the roller fragment and statblock with the results of that roll.
     * Additionally, add values to shared prefs.
     * @param view
     */
    public void getD20Roll(View view){
        TextView tv = (TextView)view.findViewById(R.id.rollFragment);

        int roll = roll20();
        rollSum += roll;
        ++dieResults[roll-1];

        tv.setText("" + roll);

        SharedPreferences.Editor spEdit = sharedPref.edit();
        spEdit.putInt(("hitsOn"+roll), dieResults[roll-1]);
        spEdit.putInt("numRolls", ++numRolls);
        spEdit.putInt("nat20", roll == 20 ? ++nat20 : nat20);
        spEdit.putInt("nat1", roll == 1 ? ++nat1 : nat1);
        spEdit.putInt("rollSum", rollSum);
        spEdit.apply();
        avgRoll = (double)rollSum/numRolls;

        updateText();
    }

    // Update stat block text with local values.
    private void updateText() {

        // Total rolls text
        TextView numRollsTV = (TextView)findViewById(R.id.numRollsText);
        numRollsTV.setText(getResources().getString(R.string.numRollsString)
                +" \t"+numRolls);

        // Critical Success stats text
        NumberFormat formatter = new DecimalFormat("#0.00");
        TextView nat20Text = (TextView)findViewById(R.id.critSuccessText);
        nat20Text.setText(getResources().getString(R.string.critSuccessString)
                +" \t"+nat20
                + " (" + formatter.format((double)nat20/numRolls*100) + "%)"
        );

        // Critical Fails stats text
        TextView nat1Text = (TextView)findViewById(R.id.critFailText);
        nat1Text.setText(getResources().getString(R.string.critFailString)
                +" \t"+nat1
                + " (" + formatter.format((double)nat1/numRolls*100) + "%)"
        );

        // Average Roll Stat Text
        // TODO: 4/17/16 Bug found when program is stopped and restarted, avgRoll is not properly displayed. Track down bug
        TextView avgRollText = (TextView)findViewById(R.id.avgRollText);
        avgRollText.setText(getResources().getString(R.string.avgRollString)+" \t"+formatter.format(avgRoll));

        // Shows the tally of each die face as rolls are tabbed up.
        int[] rolls = {
                R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven,
                R.id.eight, R.id.nine, R.id.ten, R.id.eleven, R.id.twelve, R.id.thirteen,
                R.id.fourteen, R.id.fifteen, R.id.sixteen, R.id.seventeen, R.id.eighteen,
                R.id.nineteen, R.id.twenty };
        
        for (int i=0; i < rolls.length; i++){
            TextView tv = (TextView)findViewById(rolls[i]);
            tv.setText(" "+(i+1)+": "+dieResults[i]+" ");
        }
    }

    // Simple d20 roller using java.util.Random object
    // Generates an int value from 1 to 20 exclusive.
    private int roll20() {
        Random r = new Random();

        return r.nextInt(20)+1;
    }

    // Reset button. Nukes shared prefs and resets local fields to 0
    public void resetPrefs(View view) {
        SharedPreferences.Editor spEdit = sharedPref.edit();
        spEdit.clear();
        spEdit.apply();
        numRolls = nat1 = nat20 = rollSum = 0;
        avgRoll= 0;
        dieResults = new int[20];
        updateText();

    }

}
