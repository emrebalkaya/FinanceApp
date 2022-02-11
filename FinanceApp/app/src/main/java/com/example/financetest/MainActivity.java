package com.example.financetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Console;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btn;
    SharedPreferences preferences;
    int toplamGelir;
    int toplamGider;
    int mevcutPara;
    TextView topGelir,topGider,mevcutBakiye;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn = (FloatingActionButton) findViewById(R.id.anasayfaButon);

        //Veri saklamak için SharedPreferences alanı oluşturuldu.
        preferences = this.getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //İlk giriş için toplam gelir ve toplam gider değişkenleri key olarak 0 atandı.
        if (preferences.getInt("toplamGelir",-99999) == -99999 ||
                preferences.getInt("toplamGider",-99999) == -99999){
            toplamGelir = 0;
            toplamGider = 0;
            editor.putInt("toplamGelir",toplamGelir);
            editor.putInt("toplamGider",toplamGider);
            editor.apply();
            Log.d("TAG", "onCreate: Tanımsız" + preferences.getInt("toplamGelir",500));
        }
        else {//Toplam gelir ve toplam gider değerleri çekildi.
            toplamGider = preferences.getInt("toplamGider",-99999);
            toplamGelir = preferences.getInt("toplamGelir",-99999);
            editor.apply();
            Log.d("TAG", "onCreate: Tanımlı");
        }

        //Toplam Gelir, Toplam Gider ve Mevcut Para değişkenleri viewa bastırıldı.
        mevcutPara = toplamGelir - toplamGider;

        topGelir = (TextView)findViewById(R.id.toplamGelir);
        topGelir.setText(toplamGelir+" TL");

        topGider = (TextView)findViewById(R.id.toplamGider);
        topGider.setText(toplamGider+" TL");

        mevcutBakiye = (TextView)findViewById(R.id.mevcutBakiye);
        mevcutBakiye.setText(mevcutPara+" TL");



        //Buton ile gelir gider activity geçişi
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }

    //Gelir gider Activitysine geçiş
    public void openActivity2() {
        Intent intent = new Intent(this, GelirGider.class);
        startActivity(intent);
    }

    //SharedPreferencesta saklanan veriyi sıfırlama
    public void clearEditor(View v){
        preferences = this.getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}