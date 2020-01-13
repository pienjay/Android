package com.esenpi.pienjay.dotdot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.IOException;

public class Home  extends AppCompatActivity implements RewardedVideoAdListener{
    EditText e1,e2;
    Button sound;
    int soundOnOff=0;
    SoundPool soundPool;
    int click=-1;
    TextView coins;
    int currentCoins;
    String MY_PREFS_NAME="MyPrefsFile",STORE_PREFS="StorePrefsFile";
    String coinsText;
    private RewardedVideoAd mRewardedVideoAd;
    SharedPreferences prefs,storePrefs;
    boolean p1box[]=new boolean[2],p2box[]=new boolean[2],playLines[]=new boolean[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.text);
        MobileAds.initialize(this, "ca-app-pub-1010385692458600~1252371106");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            soundPool=new SoundPool.Builder().setMaxStreams(2).build();
        }else
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        try{
            AssetManager assetManager=getAssets();
            AssetFileDescriptor descriptor;
            descriptor=assetManager.openFd("click.ogg");
            click=soundPool.load(descriptor,0);
        }catch (IOException e){
            Toast.makeText(this,"Unable to load Sound Effects",Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.text);
        sound=findViewById(R.id.sound);
        //All caps code for editText
        e1=findViewById(R.id.player1);
        e2=findViewById(R.id.player2);
        e1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        e2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        coins=findViewById(R.id.coins);
        prefs=this.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        currentCoins=prefs.getInt("coins",0);
        coinsText="Coins: "+currentCoins;
        coins.setText(coinsText);

        storePrefs=this.getSharedPreferences(STORE_PREFS,MODE_PRIVATE);
        p1box[0]=storePrefs.getBoolean("p1box0",true);
        p1box[1]=storePrefs.getBoolean("p1box1",false);
        p2box[0]=storePrefs.getBoolean("p2box0",true);
        p2box[1]=storePrefs.getBoolean("p2box1",false);

        playLines[0]=storePrefs.getBoolean("playLines0",true);
        for(int i=1;i<4;i++){
            playLines[i]=storePrefs.getBoolean("playLines"+i,false);
        }




    }
    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "Rewarded! Currency: " + reward.getType() + "  Amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
        SharedPreferences.Editor editor=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        currentCoins+=5;
        editor.putInt("coins",currentCoins);
        editor.apply();
        coinsText="Coins: "+currentCoins;
        coins.setText(coinsText);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "Your Free Coins are Available!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }
    @Override
    public void onRewardedVideoAdClosed() {
        // Load the next rewarded video ad.
        loadRewardedVideoAd();
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-1010385692458600/4611674370",
                new AdRequest.Builder().build());
    }
    public void click(View view)
    {
        if(soundOnOff==0)
    {sound.setBackgroundResource(R.drawable.ic_soundoff);soundOnOff=1;}
    else {sound.setBackgroundResource(R.drawable.ic_soundon);soundOnOff=0;
    soundPool.play(click,1,1,0,0,1);}
    }

    public void onClick(View v)
    {
        if(soundOnOff==0)
        soundPool.play(click,1,1,0,0,1);
        Intent i = new Intent(this, MainActivity.class);
        try {
            EditText player1 = (EditText) findViewById(R.id.player1);
            EditText player2 = (EditText) findViewById(R.id.player2);
            char p1 = player1.getText().toString().charAt(0);
            char p2 = player2.getText().toString().charAt(0);
            i.putExtra("p1", p1);
            i.putExtra("p1Name",player1.getText().toString());
            i.putExtra("p2", p2);
            i.putExtra("p2Name",player2.getText().toString());
            i.putExtra("sound",soundOnOff);
            Toast.makeText(this, player1.getText().toString()+"'S TURN", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"Alright, we'll go with A & B, Make your Move!",Toast.LENGTH_SHORT).show();}
        i.putExtra("sound",soundOnOff);
        startActivity(i);
    }

    public void singlePlayer(View v)
    {
        if(soundOnOff==0)
            soundPool.play(click,1,1,0,0,1);
        Intent i = new Intent(this, SinglePlayer.class);
        try {
            EditText player1=(EditText)findViewById(R.id.player1);
            char p1=player1.getText().toString().charAt(0);
            i.putExtra("p1",p1);
            i.putExtra("p1Name",player1.getText().toString());
            i.putExtra("sound",soundOnOff);
            Toast.makeText(this, player1.getText().toString()+"'S TURN", Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
        i.putExtra("sound",soundOnOff);
        startActivity(i);
        }

    @Override
    public void onBackPressed(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Leaving Already?");
        builder.setCancelable(true);
        builder.setNegativeButton("Nah! :)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes, Unfortunately :(", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void store(View v) {
    Intent i=new Intent(this,PopUp.class);
    startActivity(i);
    }
    public void support(View view){
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
        else Toast.makeText(this, "Free Coins Unavailable!", Toast.LENGTH_SHORT).show();
    }
}