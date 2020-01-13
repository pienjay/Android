package com.esenpi.pienjay.dotdot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PopUp extends AppCompatActivity {
    boolean p1box[]=new boolean[2],p2box[]=new boolean[2],playLines[]=new boolean[4],itemsBought[]=new boolean[8];
    String MY_PREFS_NAME="MyPrefsFile",STORE_PREFS="StorePrefsFile";
    SharedPreferences prefs,storePrefs;
    int currentCoins;
    Button button[]=new Button[8];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        DisplayMetrics db = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(db);
        int height=db.heightPixels;
        int width=db.widthPixels;
        getWindow().setBackgroundDrawableResource(R.color.trans);
        getWindow().setLayout((int)(width*.8),(int)(height*.45));

        storePrefs=this.getSharedPreferences(STORE_PREFS,MODE_PRIVATE);
        for(int i=0;i<3;i++)
            itemsBought[i]=storePrefs.getBoolean("bought"+i,true);
        for(int i=3;i<8;i++)
            itemsBought[i]=storePrefs.getBoolean("bought"+i,false);
        p1box[0]=storePrefs.getBoolean("p1box0",true);
        p1box[1]=storePrefs.getBoolean("p1box1",false);
        p2box[0]=storePrefs.getBoolean("p2box0",true);
        p2box[1]=storePrefs.getBoolean("p2box1",false);
        playLines[0]=storePrefs.getBoolean("playLines0",true);
        for(int i=1;i<4;i++){
            playLines[i]=storePrefs.getBoolean("playLines"+i,false);
        }
        prefs=this.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        currentCoins=prefs.getInt("coins",0);
        button[0]=findViewById(R.id.default_line);
        button[1]=findViewById(R.id.default_p1_box);
        button[2]=findViewById(R.id.default_p2_box);
        button[3]=findViewById(R.id.purple_line);
        button[4]=findViewById(R.id.kings_line);
        button[5]=findViewById(R.id.queens_line);
        button[6]=findViewById(R.id.red_box);
        button[7]=findViewById(R.id.green_box);
        if(itemsBought[0] && !playLines[0])
            button[0].setText("Select");
        if(itemsBought[3] && !playLines[1])
            button[3].setText("Select");
        if(itemsBought[4] && !playLines[2])
            button[4].setText("Select");
        if(itemsBought[5] && !playLines[3])
            button[5].setText("Select");
        if(itemsBought[1] && !p1box[0])
            button[1].setText("Select");
        if(itemsBought[6] && !p1box[1])
            button[6].setText("Select");
        if(itemsBought[2] && !p2box[0])
            button[2].setText("Select");
        if(itemsBought[7] && !p2box[1])
            button[7].setText("Select");

        if(itemsBought[0] && playLines[0])
            button[0].setText("Selected");
        if(itemsBought[3] && playLines[1])
            button[3].setText("Selected");
        if(itemsBought[4] && playLines[2])
            button[4].setText("Selected");
        if(itemsBought[5] && playLines[3])
            button[5].setText("Selected");
        if(itemsBought[1] && p1box[0])
            button[1].setText("Selected");
        if(itemsBought[6] && p1box[1])
            button[6].setText("Selected");
        if(itemsBought[2] && p2box[0])
            button[2].setText("Selected");
        if(itemsBought[7] && p2box[1])
            button[7].setText("Selected");

        for(int i=0;i<7;i++)
        {if(button[i].getText()=="Selected")button[i].setClickable(false);
        else button[i].setClickable(true);}

    }
   public void defaultLine(View view)
    {
        button[0].setText("Selected");
        button[0].setClickable(false);
        SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
        editor.putBoolean("playLines0",true);
        for(int i=1;i<4;i++)
        {
        editor.putBoolean("playLines"+i,false);
        editor.apply();}
        for(int i=3;i<6;i++)
        {if(itemsBought[i]) button[i].setText("Select");}
    }
   public void defaultp1box(View view)
    {
        button[1].setText("Selected");
        button[1].setClickable(false);
        SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
        editor.putBoolean("p1box0",true);
        editor.putBoolean("p1box1",false);
        editor.apply();
        if(itemsBought[6]) button[6].setText("Select");
    }
   public void defaultp2box(View view)
    {
        button[2].setText("Selected");
        button[2].setClickable(false);
        SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
        editor.putBoolean("p2box0",true);
        editor.putBoolean("p2box1",false);
        editor.apply();
        if(itemsBought[7]) button[7].setText("Select");
    }
   public void purpleLine(View view)
    {   if(button[3].getText()=="Select")
    {
        button[3].setText("Selected");
        button[3].setClickable(false);
        SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
        editor.putBoolean("playLines0",false);
        editor.putBoolean("playLines1",true);
        for(int i=2;i<4;i++)
        {
            editor.putBoolean("playLines"+i,false);
            editor.apply();}
        button[0].setClickable(true);
        button[0].setText("Select");
        for(int i=4;i<6;i++)
            if(itemsBought[i]) {
            button[i].setClickable(true);
            button[i].setText("Select");}

    }
        else if(currentCoins>=25)
        {
            button[3].setText("Select");
            currentCoins-=25;
            SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putInt("coins",currentCoins);
            editor.apply();
            SharedPreferences.Editor editor1=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor1.putBoolean("bought3",true);
            editor1.apply();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        }
        else{
        Toast.makeText(this, "Not Enough Coins!", Toast.LENGTH_SHORT).show();
    }
    }
   public void kingsLine(View view)
    {
        if(button[4].getText()=="Select")
        {
            button[4].setText("Selected");
            button[4].setClickable(false);
            SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor.putBoolean("playLines0",false);
            editor.putBoolean("playLines2",true);
            editor.putBoolean("playLines1",false);
            editor.putBoolean("playLines3",false);
                editor.apply();
            button[0].setClickable(true);
            button[0].setText("Select");
                if(itemsBought[3]) {
                    button[3].setClickable(true);
                    button[3].setText("Select");}
            if(itemsBought[5]) {
                button[5].setClickable(true);
                button[5].setText("Select");}

        }
        else if(currentCoins>=40)
        {
            button[4].setText("Select");
            currentCoins-=40;
            SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putInt("coins",currentCoins);
            editor.apply();
            SharedPreferences.Editor editor1=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor1.putBoolean("bought4",true);
            editor1.apply();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Not Enough Coins!", Toast.LENGTH_SHORT).show();
        }
    }
   public void queensLine(View view)
    {
        if(button[5].getText()=="Select")
        {
            button[5].setText("Selected");
            button[5].setClickable(false);
            SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor.putBoolean("playLines0",false);
            editor.putBoolean("playLines3",true);
            editor.putBoolean("playLines1",false);
            editor.putBoolean("playLines2",false);
            editor.apply();
            button[0].setClickable(true);
            button[0].setText("Select");
            if(itemsBought[3]) {
                button[3].setClickable(true);
                button[3].setText("Select");}
            if(itemsBought[4]) {
                button[4].setClickable(true);
                button[4].setText("Select");}

        }
        else if(currentCoins>=40)
        {
            button[5].setText("Select");
            currentCoins-=40;
            SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putInt("coins",currentCoins);
            editor.apply();
            SharedPreferences.Editor editor1=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor1.putBoolean("bought5",true);
            editor1.apply();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Not Enough Coins!", Toast.LENGTH_SHORT).show();
        }
    }
   public void redBox(View view)
    {
        if(button[6].getText()=="Select")
        {
            button[6].setText("Selected");
            button[6].setClickable(false);
            SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor.putBoolean("p1box0",false);
            editor.putBoolean("p1box1",true);
            editor.apply();
            button[1].setClickable(true);
            button[1].setText("Select");
        }
        else if(currentCoins>=50)
        {
            button[6].setText("Select");
            currentCoins-=50;
            SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putInt("coins",currentCoins);
            editor.apply();
            SharedPreferences.Editor editor1=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor1.putBoolean("bought6",true);
            editor1.apply();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Not Enough Coins!", Toast.LENGTH_SHORT).show();
        }
    }
   public void greenBox(View view)
    {
        if(button[7].getText()=="Select")
        {
            button[7].setText("Selected");
            button[7].setClickable(false);
            SharedPreferences.Editor editor=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor.putBoolean("p2box0",false);
            editor.putBoolean("p2box1",true);
            editor.apply();
            button[2].setClickable(true);
            button[2].setText("Select");
        }
        else if(currentCoins>=50)
        {
            button[7].setText("Select");
            currentCoins-=50;
            SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putInt("coins",currentCoins);
            editor.apply();
            SharedPreferences.Editor editor1=getSharedPreferences(STORE_PREFS,MODE_PRIVATE).edit();
            editor1.putBoolean("bought7",true);
            editor1.apply();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Not Enough Coins!", Toast.LENGTH_SHORT).show();
        }
    }
}
