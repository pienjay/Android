package com.esenpi.pienjay.dotdot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SinglePlayer extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    ConstraintLayout relativeLayout;
    static boolean first = true;
    int id[] = new int[24];
    boolean turn=true;
    int s1, s2;
    TextView t1, t2;
    char p1;
    String p2 = "AI", p1Name;
    SoundPool soundPool;
    int click = -1;
    int box = -1;
    int soundOnOff;
    int coins;
    String MY_PREFS_NAME="MyPrefsFile",STORE_PREFS="StorePrefsFile";
    SharedPreferences storePrefs;
    int p1boxes[]=new int[2];
    int p2boxes[]=new int[2];
    int lines[]=new int[4];
    boolean p1box[]=new boolean[2],p2box[]=new boolean[2],playLines[]=new boolean[4];
    //Drawable boxesD[]=new Drawable[4],linesD[]=new Drawable[4];
    int currentLineID,currentP1BoxID,currentP2BoxID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        s1 = 0;
        s2 = 0;
        SharedPreferences prefs=this.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        coins=prefs.getInt("coins",0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(2).build();
        } else
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;
            descriptor = assetManager.openFd("box.ogg");
            box = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("click.ogg");
            click = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            Toast.makeText(this, "Unable to load Sound Effects", Toast.LENGTH_SHORT).show();
        }
        MobileAds.initialize(this, "ca-app-pub-1010385692458600~1252371106");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1010385692458600/1747195125");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Bundle b = getIntent().getExtras();
        try {
            if (b == null)
            {p1='A';soundOnOff=b.getInt("sound");}
            else {
                if(b.getChar("p1")=='\0') {p1='A';}
                else{
                    p1 = b.getChar("p1");
                    p1Name=b.getString("p1Name");}
                soundOnOff=b.getInt("sound");
            }
        }catch (Exception e){p1='A';soundOnOff=b.getInt("sound");}
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.rl);
        t1 = findViewById(R.id.score1);
        t1.setTextColor(Color.RED);
        t2 = findViewById(R.id.score2);
        t2.setTextColor(Color.GREEN);
        id[0] = R.id.a;
        id[1] = R.id.b;
        id[2] = R.id.c;
        id[3] = R.id.d;
        id[4] = R.id.e;
        id[5] = R.id.f;
        id[6] = R.id.g;
        id[7] = R.id.h;
        id[8] = R.id.i;
        id[9] = R.id.j;
        id[10] = R.id.k;
        id[11] = R.id.l;
        id[12] = R.id.m;
        id[13] = R.id.n;
        id[14] = R.id.o;
        id[15] = R.id.p;
        id[16] = R.id.q;
        id[17] = R.id.r;
        id[18] = R.id.s;
        id[19] = R.id.t;
        id[20] = R.id.u;
        id[21] = R.id.v;
        id[22] = R.id.w;
        id[23] = R.id.x;
        String score1 = "" + p1 + ": " + s1, score2 = "" + p2 + ": " + s2;
        t1.setText(score1);
        t2.setText(score2);

        p1boxes[0]=R.drawable.box1;
        p2boxes[0]=R.drawable.box2;
        p1boxes[1]=R.drawable.box_red;
        p2boxes[1]=R.drawable.box_green;
        lines[0]=R.drawable.line;
        lines[1]=R.drawable.line1;
        lines[2]=R.drawable.line2;
        lines[3]=R.drawable.line3;
        /*for(int i=0;i<4;i++)
        {boxesD[i]=getResources().getDrawable(boxes[i]);
            linesD[i]=getResources().getDrawable(lines[i]);}*/
        storePrefs=this.getSharedPreferences(STORE_PREFS,MODE_PRIVATE);
        p1box[0]=storePrefs.getBoolean("p1box0",true);
        p1box[1]=storePrefs.getBoolean("p1box1",false);
        p2box[0]=storePrefs.getBoolean("p2box0",true);
        p2box[1]=storePrefs.getBoolean("p2box1",false);

        playLines[0]=storePrefs.getBoolean("playLines0",true);
        for(int i=1;i<4;i++){
            playLines[i]=storePrefs.getBoolean("playLines"+i,false);
        }
        for(int i=0;i<4;i++)
            if(playLines[i])
                currentLineID=i;
        for(int i=0;i<2;i++)
            if(p1box[i])
                currentP1BoxID=i;
        for(int i=0;i<2;i++)
            if(p2box[i])
                currentP2BoxID=i;
        int q=new Random().nextInt(24);
        int t=new Random().nextInt(2);
            if(t%2==1) {
            findViewById(id[q]).setBackgroundColor(Color.GREEN);
            findViewById(id[q]).setClickable(false);
                Toast.makeText(this, "AI Goes First", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "You Go First, Good luck!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SinglePlayer.this);
        builder.setMessage("Sure to Leave in Between?");
        builder.setCancelable(true);
        builder.setNegativeButton("Nah! :)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes! :(", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SinglePlayer.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onClick(View view) {
        boolean boxFormed=false;
        turn=!turn;
        if (soundOnOff == 0)
            soundPool.play(click, 1, 1, 0, 0, 1);
        TransitionManager.beginDelayedTransition(relativeLayout);
        Button button = findViewById(view.getId());
        Button nButton, pButton, dButton, uButton, mddButton, mduButton, sduButton, sddButton;
        int ID = 0, state = 0;
        button.setClickable(false);
        for (int i = 0; i < 24; i++)
            if (id[i] == view.getId())
                ID = i;
        try {
            nButton = findViewById(id[ID + 1]);
        } catch (Exception e) {
            nButton = null;
        }
        try {
            dButton = findViewById(id[ID + 4]);
        } catch (Exception e) {
            dButton = null;
        }
        try {
            pButton = findViewById(id[ID - 1]);
        } catch (Exception e) {
            pButton = null;
        }
        try {
            uButton = findViewById(id[ID - 4]);
        } catch (Exception e) {
            uButton = null;
        }
        try {
            mddButton = findViewById(id[ID + 5]);
        } catch (Exception e) {
            mddButton = null;
        }
        try {
            mduButton = findViewById(id[ID - 5]);
        } catch (Exception e) {
            mduButton = null;
        }
        try {
            sduButton = findViewById(id[ID - 3]);
        } catch (Exception e) {
            sduButton = null;
        }
        try {
            sddButton = findViewById(id[ID + 3]);
        } catch (Exception e) {
            sddButton = null;
        }
        if (first) {
            if (turn)
                button.setBackgroundColor(Color.GREEN);
            else
                button.setBackgroundColor(Color.RED);
            first = false;
        }
        try {
            if (((ColorDrawable) nButton.getBackground()).getColor() == Color.RED
                    || ((ColorDrawable) nButton.getBackground()).getColor() == Color.GREEN) {
                if (nButton.getId() == id[4] || nButton.getId() == id[8] ||
                        nButton.getId() == id[12] || nButton.getId() == id[16] || nButton.getId() == id[20]) {
                    if (turn)
                        button.setBackgroundColor(Color.GREEN);
                    else
                        button.setBackgroundColor(Color.RED);
                } else {
                    if (turn)
                        button.setBackgroundColor(Color.GREEN);
                    else
                        button.setBackgroundColor(Color.RED);
                    View view1 = new View(SinglePlayer.this);
                    view1.setLayoutParams(new RelativeLayout.LayoutParams((int) (nButton.getX() - button.getX()), 20));
                    view1.setBackgroundResource(lines[currentLineID]);
                    relativeLayout.addView(view1);
                    view1.setX(button.getX() + (button.getWidth() / 2));
                    view1.setY(button.getY() + (button.getHeight() / 2) - 10);


                    if ((((ColorDrawable) uButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) uButton.getBackground()).getColor() == Color.GREEN)
                            && (((ColorDrawable) sduButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) sduButton.getBackground()).getColor() == Color.GREEN)) {
                        boxFormed=true;
                        View box = new View(SinglePlayer.this);
                        box.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        if(!turn)
                            box.setBackgroundResource(p1boxes[currentP1BoxID]);
                        else box.setBackgroundResource(p2boxes[currentP2BoxID]);
                        relativeLayout.addView(box);
                        box.setX(uButton.getX() + (button.getWidth() / 2));
                        box.setY(uButton.getY() + (button.getHeight() / 2));
                        if (soundOnOff == 0)
                            soundPool.play(this.box, 1, 1, 0, 0, 1);
                        TextView text = new TextView(SinglePlayer.this);
                        text.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(50);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        if (((ColorDrawable) button.getBackground()).getColor() == Color.RED) {
                            String s = "" + p1 + ": " + (++s1), p = "" + p1;
                            text.setText(p);
                            t1.setText(s);
                        } else {
                            String s = "" + p2 + ": " + (++s2), p = "" + p2;
                            text.setText(p);
                            t2.setText(s);
                        }
                        relativeLayout.addView(text);
                        text.setX(uButton.getX() + (button.getWidth() / 2));
                        text.setY(uButton.getY() + (button.getHeight() / 2));
                    }


                }
            }
        } catch (Exception e) {
            state++;
        }
        try {
            if (((ColorDrawable) dButton.getBackground()).getColor() == Color.RED
                    || ((ColorDrawable) dButton.getBackground()).getColor() == Color.GREEN) {
                if (turn)
                    button.setBackgroundColor(Color.GREEN);
                else
                    button.setBackgroundColor(Color.RED);
                View view1 = new View(SinglePlayer.this);
                view1.setLayoutParams(new RelativeLayout.LayoutParams(20, (int) (dButton.getY() - button.getY())));
                view1.setBackgroundResource(lines[currentLineID]);
                relativeLayout.addView(view1);
                view1.setX(button.getX() + (button.getWidth() / 2) - 10);
                view1.setY(button.getY() + (button.getHeight() / 2));

                if (nButton.getId() == id[4] || nButton.getId() == id[8] ||
                        nButton.getId() == id[12] || nButton.getId() == id[16] || nButton.getId() == id[20]) {
                } else {
                    if ((((ColorDrawable) nButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) nButton.getBackground()).getColor() == Color.GREEN)
                            && (((ColorDrawable) mddButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) mddButton.getBackground()).getColor() == Color.GREEN)) {
                        boxFormed=true;
                        View box = new View(SinglePlayer.this);
                        box.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        if(!turn)
                            box.setBackgroundResource(p1boxes[currentP1BoxID]);
                        else box.setBackgroundResource(p2boxes[currentP2BoxID]);
                        relativeLayout.addView(box);
                        box.setX(button.getX() + (button.getWidth() / 2));
                        box.setY(button.getY() + (button.getHeight() / 2));
                        if (soundOnOff == 0)
                            soundPool.play(this.box, 1, 1, 0, 0, 1);
                        TextView text = new TextView(SinglePlayer.this);
                        text.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(50);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        if (((ColorDrawable) button.getBackground()).getColor() == Color.RED) {
                            String s = "" + p1 + ": " + (++s1), p = "" + p1;
                            text.setText(p);
                            t1.setText(s);
                        } else {
                            String s = "" + p2 + ": " + (++s2), p = "" + p2;
                            text.setText(p);
                            t2.setText(s);
                        }
                        relativeLayout.addView(text);
                        text.setX(button.getX() + (button.getWidth() / 2));
                        text.setY(button.getY() + (button.getHeight() / 2));
                    }
                }

            }
        } catch (Exception e) {
            state++;
        }
        try {
            if (((ColorDrawable) pButton.getBackground()).getColor() == Color.RED
                    || ((ColorDrawable) pButton.getBackground()).getColor() == Color.GREEN) {
                if (pButton.getId() == id[3] || pButton.getId() == id[7] ||
                        pButton.getId() == id[11] || pButton.getId() == id[15] || pButton.getId() == id[19]) {
                    if (turn)
                        button.setBackgroundColor(Color.GREEN);
                    else
                        button.setBackgroundColor(Color.RED);
                } else {
                    if (turn)
                        button.setBackgroundColor(Color.GREEN);
                    else
                        button.setBackgroundColor(Color.RED);
                    View view1 = new View(SinglePlayer.this);
                    view1.setLayoutParams(new RelativeLayout.LayoutParams((int) (button.getX() - pButton.getX()), 20));
                    view1.setBackgroundResource(lines[currentLineID]);
                    relativeLayout.addView(view1);
                    view1.setX(pButton.getX() + (button.getWidth() / 2));
                    view1.setY(pButton.getY() + (button.getHeight() / 2) - 10);

                    if ((((ColorDrawable) dButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) dButton.getBackground()).getColor() == Color.GREEN)
                            && (((ColorDrawable) sddButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) sddButton.getBackground()).getColor() == Color.GREEN)) {
                        boxFormed=true;
                        View box = new View(SinglePlayer.this);
                        box.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        if(!turn)
                            box.setBackgroundResource(p1boxes[currentP1BoxID]);
                        else box.setBackgroundResource(p2boxes[currentP2BoxID]);
                        relativeLayout.addView(box);
                        box.setX(pButton.getX() + (button.getWidth() / 2));
                        box.setY(pButton.getY() + (button.getHeight() / 2));
                        if (soundOnOff == 0)
                            soundPool.play(this.box, 1, 1, 0, 0, 1);
                        TextView text = new TextView(SinglePlayer.this);
                        text.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(50);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        if (((ColorDrawable) button.getBackground()).getColor() == Color.RED) {
                            String s = "" + p1 + ": " + (++s1), p = "" + p1;
                            text.setText(p);
                            t1.setText(s);
                        } else {
                            String s = "" + p2 + ": " + (++s2), p = "" + p2;
                            text.setText(p);
                            t2.setText(s);
                        }
                        relativeLayout.addView(text);
                        text.setX(pButton.getX() + (button.getWidth() / 2));
                        text.setY(pButton.getY() + (button.getHeight() / 2));
                    }


                }
            }
        } catch (Exception e) {
            state++;
        }
        try {
            if (((ColorDrawable) uButton.getBackground()).getColor() == Color.RED
                    || ((ColorDrawable) uButton.getBackground()).getColor() == Color.GREEN) {
                if (turn)
                    button.setBackgroundColor(Color.GREEN);
                else
                    button.setBackgroundColor(Color.RED);
                View view1 = new View(SinglePlayer.this);
                view1.setLayoutParams(new RelativeLayout.LayoutParams(20, (int) (button.getY() - uButton.getY())));
                view1.setBackgroundResource(lines[currentLineID]);
                relativeLayout.addView(view1);
                view1.setX(uButton.getX() + (button.getWidth() / 2) - 10);
                view1.setY(uButton.getY() + (button.getHeight() / 2));
                if (pButton.getId() == id[3] || pButton.getId() == id[7] ||
                        pButton.getId() == id[11] || pButton.getId() == id[15] || pButton.getId() == id[19]) {
                } else {
                    if ((((ColorDrawable) pButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) pButton.getBackground()).getColor() == Color.GREEN)
                            && (((ColorDrawable) mduButton.getBackground()).getColor() == Color.RED
                            || ((ColorDrawable) mduButton.getBackground()).getColor() == Color.GREEN)) {
                        boxFormed=true;
                        View box = new View(SinglePlayer.this);
                        box.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        if(!turn)
                            box.setBackgroundResource(p1boxes[currentP1BoxID]);
                        else box.setBackgroundResource(p2boxes[currentP2BoxID]);
                        relativeLayout.addView(box);
                        box.setX(mduButton.getX() + (button.getWidth() / 2));
                        box.setY(mduButton.getY() + (button.getHeight() / 2));
                        if (soundOnOff == 0)
                            soundPool.play(this.box, 1, 1, 0, 0, 1);
                        TextView text = new TextView(SinglePlayer.this);
                        text.setLayoutParams(new RelativeLayout.LayoutParams(
                                (int) (findViewById(id[1]).getX() - findViewById(id[0]).getX() - 5),
                                (int) (findViewById(id[4]).getY() - findViewById(id[0]).getY() - 5)));
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(50);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        if (((ColorDrawable) button.getBackground()).getColor() == Color.RED) {
                            String s = "" + p1 + ": " + (++s1), p = "" + p1;
                            text.setText(p);
                            t1.setText(s);
                        } else {
                            String s = "" + p2 + ": " + (++s2), p = "" + p2;
                            text.setText(p);
                            t2.setText(s);
                        }
                        relativeLayout.addView(text);
                        text.setX(mduButton.getX() + (button.getWidth() / 2));
                        text.setY(mduButton.getY() + (button.getHeight() / 2));
                    }
                }

            }
        } catch (Exception e) {
            state++;
            if (state > 3)
                if (turn)
                    button.setBackgroundColor(Color.GREEN);
                else
                    button.setBackgroundColor(Color.RED);
        }
        if (s1 + s2 > 14) {
            if (s1 > s2)
            {Toast.makeText(this, "You win!", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                editor.putInt("coins",coins+5);
                editor.apply();}
            else {Toast.makeText(this, "AI wins!", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                editor.putInt("coins",coins+1);
                editor.apply();}
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Intent i = new Intent(SinglePlayer.this, Home.class);
                        startActivity(i);
                    }
                }
            }, 2000);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Intent i = new Intent(SinglePlayer.this, Home.class);
                    startActivity(i);
                }
            });
        }


        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button nButton,dButton,pButton,uButton,mduButton,mddButton,sduButton,sddButton;
                int ID;
        if(!turn) {
            int clickableButtons[] = new int[24], j = 0;
            for (int i = 0; i < 24; i++) {
                if (findViewById(id[i]).isClickable()) {
                    clickableButtons[j++] = i;
                }
            }int a=0;
            for (int i = 0; i < j; i++) {
                    ID = clickableButtons[i];
                    try {
                        nButton = findViewById(id[ID + 1]);
                    } catch (Exception e) {
                        nButton = null;
                    }
                    try {
                        dButton = findViewById(id[ID + 4]);
                    } catch (Exception e) {
                        dButton = null;
                    }
                    try {
                        pButton = findViewById(id[ID - 1]);
                    } catch (Exception e) {
                        pButton = null;
                    }
                    try {
                        uButton = findViewById(id[ID - 4]);
                    } catch (Exception e) {
                        uButton = null;
                    }
                    try {
                        mddButton = findViewById(id[ID + 5]);
                    } catch (Exception e) {
                        mddButton = null;
                    }
                    try {
                        mduButton = findViewById(id[ID - 5]);
                    } catch (Exception e) {
                        mduButton = null;
                    }
                    try {
                        sduButton = findViewById(id[ID - 3]);
                    } catch (Exception e) {
                        sduButton = null;
                    }
                    try {
                        sddButton = findViewById(id[ID + 3]);
                    } catch (Exception e) {
                        sddButton = null;
                    }
                    try{
                    if (pButton.getId() == id[3] || pButton.getId() == id[7] ||
                            pButton.getId() == id[11] || pButton.getId() == id[15] || pButton.getId() == id[19]) {
                        pButton = null;
                    }}catch (Exception e){pButton=null;}
                    try{
                    if (nButton.getId() == id[4] || nButton.getId() == id[8] ||
                            nButton.getId() == id[12] || nButton.getId() == id[16] || nButton.getId() == id[20]) {
                        nButton = null;
                    }}catch (Exception e){nButton=null;}
                    if(findViewById(id[ID]).isClickable() && pButton!=null && nButton!=null && dButton!=null && uButton!=null)
                    {if (!pButton.isClickable() && !dButton.isClickable() && !sddButton.isClickable()
                            && !nButton.isClickable() && !mddButton.isClickable() && !uButton.isClickable()
                            && !sduButton.isClickable() && !mduButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}
                        if(findViewById(id[ID]).isClickable() && pButton!=null && nButton!=null && uButton!=null)
                    { if (!nButton.isClickable() && !uButton.isClickable() && !sduButton.isClickable()
                            && !pButton.isClickable() && !mduButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && pButton!=null && nButton!=null && dButton!=null)
                    {if (!nButton.isClickable() && !dButton.isClickable() && !mddButton.isClickable()
                            && !pButton.isClickable() && !sddButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && pButton!=null && dButton!=null && uButton!=null)
                    { if (!pButton.isClickable() && !dButton.isClickable() && !sddButton.isClickable()
                            && !uButton.isClickable() && !mduButton.isClickable())
                    { findViewById(id[ID]).performClick();a=1;break;}}
                     if(findViewById(id[ID]).isClickable() && nButton!=null && uButton!=null && dButton!=null)
                    { if (!nButton.isClickable() && !uButton.isClickable() && !mddButton.isClickable()
                            && !dButton.isClickable() && !sduButton.isClickable())
                    { findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && pButton!=null && uButton!=null)
                    {if (!pButton.isClickable() && !uButton.isClickable() && !mduButton.isClickable())
                    { findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && pButton!=null && dButton!=null)
                    { if (!pButton.isClickable() && !dButton.isClickable() && !sddButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && nButton!=null && uButton!=null)
                    {if (!nButton.isClickable() && !uButton.isClickable() && !sduButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}
                          if(findViewById(id[ID]).isClickable() && nButton!=null && dButton!=null)
                    { if (!nButton.isClickable() && !dButton.isClickable() && !mddButton.isClickable())
                    {findViewById(id[ID]).performClick();a=1;break;}}}
            for(int i = 0; i < j; i++) {
                if(a==0){
                    ID = clickableButtons[i];
                    if(findViewById(id[ID]).isClickable())
                    {
                        try {
                            nButton = findViewById(id[ID + 1]);
                        } catch (Exception e) {
                            nButton = null;
                        }
                        try {
                            pButton = findViewById(id[ID - 1]);
                        } catch (Exception e) {
                            pButton = null;
                        }
                        try{
                            if (pButton.getId() == id[3] || pButton.getId() == id[7] ||
                                    pButton.getId() == id[11] || pButton.getId() == id[15] || pButton.getId() == id[19]) {
                                pButton = null;
                            }}catch (Exception e){pButton=null;}
                        try{
                            if (nButton.getId() == id[4] || nButton.getId() == id[8] ||
                                    nButton.getId() == id[12] || nButton.getId() == id[16] || nButton.getId() == id[20]) {
                                nButton = null;
                            }}catch (Exception e){nButton=null;}
                        if(findViewById(id[ID]).isClickable() && pButton!=null)
                        { if (!pButton.isClickable())
                        {findViewById(id[ID]).performClick();a=1;break;}}
                        if(findViewById(id[ID]).isClickable() && nButton!=null)
                        { if (!nButton.isClickable())
                        {findViewById(id[ID]).performClick();a=1;break;}}}}
            }
                         for(int i = 0; i < j; i++) {
                if(a==0){
                ID = clickableButtons[i];
                if(findViewById(id[ID]).isClickable())
                {
                try {
                    dButton = findViewById(id[ID + 4]);
                } catch (Exception e) {
                    dButton = null;
                }
                try {
                    uButton = findViewById(id[ID - 4]);
                } catch (Exception e) {
                    uButton = null;
                }
                         if(findViewById(id[ID]).isClickable() && dButton!=null)
                         { if (!dButton.isClickable())
                         {findViewById(id[ID]).performClick();a=1;break;}}
                         if(findViewById(id[ID]).isClickable() && uButton!=null)
                        { if (!uButton.isClickable())
                        {findViewById(id[ID]).performClick();a=1;break;}}}}
            }
        }
            }
        },200);

        if(boxFormed){
            turn=!turn;
            final Toast toast=Toast.makeText(this, "Make your Move Again!", Toast.LENGTH_SHORT);
            toast.show();
            Handler handler1=new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            },400);
        }
    }
}
