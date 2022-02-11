package com.example.financetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Detay extends AppCompatActivity {
    FloatingActionButton btn;
    Intent i;
    String m;
    int gelirCount;
    int giderKayit,gelirKayit;
    int giderCount;
    EditText editDetayBaslik,editDetayAciklama ,editDetayTutar;
    String detayBaslik,detayAciklama,detayTutarKontrol,gelirBaslikCount,gelirAciklamaCount,gelirTutarCount;
    String giderBaslikCount,giderAciklamaCount,giderTutarCount;
    int detayTutar,detayTutarEski;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detay);

        //Güncelleme veya ekleme yöntemi ve güncellenecek kayıt bilgisi çekildi.
        i = getIntent();
        Bundle b = i.getExtras();
        m = b.getString("method");
        giderKayit= b.getInt("giderKayıtDetay",-99999);
        gelirKayit = b.getInt("gelirKayıtDetay",-99999);
        editDetayBaslik = (EditText)findViewById(R.id.detayBaslik);
        editDetayAciklama = (EditText)findViewById(R.id.detayAciklama);
        editDetayTutar = (EditText)findViewById(R.id.detayTutar);


        preferences = this.getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //Güncelleme için kayıt geldiyse text alanlarına mevcut veriler girilir
        if(giderKayit != -99999){
            editDetayBaslik.setText(preferences.getString("giderBaslik"+giderKayit,null));
            editDetayTutar.setText(preferences.getInt("giderTutar"+giderKayit,-99999)+"");
            if(preferences.getString("giderAciklama"+giderKayit,null) != null){
                editDetayAciklama.setText(preferences.getString("giderAciklama"+giderKayit,null));
            }
            detayTutarEski = Integer.parseInt(editDetayTutar.getText().toString());
        }
        else if(gelirKayit != -99999){
            editDetayBaslik.setText(preferences.getString("gelirBaslik"+gelirKayit,null));
            editDetayTutar.setText(preferences.getInt("gelirTutar"+gelirKayit,-99999)+"");
            if(preferences.getString("gelirAciklama"+gelirKayit,null) != null){
                editDetayAciklama.setText(preferences.getString("gelirAciklama"+gelirKayit,null));
            }
            detayTutarEski = Integer.parseInt(editDetayTutar.getText().toString());
        }


        btn = (FloatingActionButton) findViewById(R.id.gelirGidereDönüş);

        //Gelir ve gider kayıt sayısını tutan Count değişkeni tanımlandı ve SharedPreferencesa atıldı
        if (preferences.getInt("gelirCount",-99999) == -99999 ||
                preferences.getInt("giderCount",-99999) == -99999){
            gelirCount = 0;
            giderCount = 0;
            editor.putInt("gelirCount",gelirCount);
            editor.putInt("giderCount",giderCount);
            editor.apply();
        }
        else {
            giderCount = preferences.getInt("giderCount",-99999);
            gelirCount = preferences.getInt("gelirCount",-99999);
            editor.apply();
        }

        //GelirGider Activitysine dönüş
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }


    //Kaydetme butonu
    public void btnKaydet(View v){
        detayBaslik = editDetayBaslik.getText().toString();
        detayTutarKontrol = editDetayTutar.getText().toString();
        detayAciklama = editDetayAciklama.getText().toString();

        //Başlık veya Tutar boş bırakılmaması için kontrol
        if(!TextUtils.isEmpty(detayBaslik) && !TextUtils.isEmpty(detayTutarKontrol)){
            detayTutar = Integer.parseInt(detayTutarKontrol);

            //Method gelir ise buraya girilir
            if(m.equals("Gelir")) {
                //Yeni kayıt girişi
                if (gelirKayit == -99999){
                    //Başlığın anahtarı tekilliği için kontrol
                    if(!preferences.contains(detayBaslik)){

                        //Girilen veriler alındı ve uyumlu keylerle birlikte SharedPreferencesa atıldı ve GelirCount arttırıldı
                        gelirCount = preferences.getInt("gelirCount", 99999);
                        gelirBaslikCount = "gelirBaslik" + gelirCount;
                        gelirTutarCount = "gelirTutar" + gelirCount;
                        gelirAciklamaCount = "gelirAciklama" + gelirCount;
                        editor.putString(gelirBaslikCount, detayBaslik);
                        editor.putInt(gelirTutarCount, detayTutar);
                        if (!TextUtils.isEmpty(detayAciklama)) {
                            editor.putString(gelirAciklamaCount, detayAciklama);
                        }
                        //Girilen fiyat kadar toplam gelire eklendi
                        int toplamGelir = preferences.getInt("toplamGelir", -99999);
                        toplamGelir += detayTutar;
                        editor.putInt("toplamGelir", toplamGelir);
                        gelirCount++;
                        editor.putInt("gelirCount", gelirCount);
                        editor.apply();
                        GelirGider.fragmentCheck = true;
                    }
                    //Başlık SharedPreferencesta key olarak varsa hata mesajı
                    else{
                        Toast.makeText(getApplicationContext(),"Bu Başlık kullanılamaz...",Toast.LENGTH_SHORT).show();
                    }
                }
                //Kayıt güncelleme
                else{
                    //En son girilen veriler alındı ve kayıt güncellendi
                    gelirBaslikCount = "gelirBaslik" + gelirKayit;
                    gelirTutarCount =  "gelirTutar" + gelirKayit;
                    gelirAciklamaCount = "gelirAciklama" + gelirKayit;
                    editor.putString(gelirBaslikCount,detayBaslik);
                    editor.putInt(gelirTutarCount,detayTutar);
                    if(!TextUtils.isEmpty(detayAciklama)){
                        editor.putString(gelirAciklamaCount,detayAciklama);
                    }
                    int toplamGelir = preferences.getInt("toplamGelir",-99999) ;
                    //En son girilen tutardan eski tutar çıkarılarak aradaki fark mevcut toplam gelire yansıtıldı.
                    toplamGelir += detayTutar - detayTutarEski ;
                    editor.putInt("toplamGelir",toplamGelir);
                    editor.apply();
                    GelirGider.fragmentCheck = true;
                }
            }

            //Method gider ise buraya girilir
            else if(m.equals("Gider")){
                //Yeni kayıt girişi
                if(giderKayit == -99999){
                    //Başlığın anahtarı tekilliği için kontrol
                    if(!preferences.contains(detayBaslik)) {
                        //Girilen veriler alındı ve uyumlu keylerle birlikte SharedPreferencesa atıldı ve GiderCount arttırıldı
                        giderCount = preferences.getInt("giderCount", 99999);
                        giderBaslikCount = "giderBaslik" + giderCount;
                        giderTutarCount = "giderTutar" + giderCount;
                        giderAciklamaCount = "giderAciklama" + giderCount;
                        editor.putString(giderBaslikCount, detayBaslik);
                        editor.putInt(giderTutarCount, detayTutar);
                        if (!TextUtils.isEmpty(detayAciklama)) {
                            editor.putString(giderAciklamaCount, detayAciklama);
                        }
                        //Girilen fiyat kadar toplam gidere eklendi
                        int toplamGider = preferences.getInt("toplamGider", -99999);
                        toplamGider += detayTutar;
                        editor.putInt("toplamGider", toplamGider);
                        giderCount++;
                        editor.putInt("giderCount", giderCount);
                        editor.apply();
                        GelirGider.fragmentCheck = false;
                    }
                    //Başlık SharedPreferencesta key olarak varsa hata mesajı
                    else{
                        Toast.makeText(getApplicationContext(),"Bu Başlık kullanılamaz...",Toast.LENGTH_SHORT).show();
                    }
                }
                //Kayıt güncelleme
                else{
                    //En son girilen veriler alındı ve kayıt güncellendi
                    giderBaslikCount = "giderBaslik" + giderKayit;
                    giderTutarCount =  "giderTutar" + giderKayit;
                    giderAciklamaCount = "giderAciklama" + giderKayit;
                    editor.putString(giderBaslikCount,detayBaslik);
                    editor.putInt(giderTutarCount,detayTutar);
                    if(!TextUtils.isEmpty(detayAciklama)){
                        editor.putString(giderAciklamaCount,detayAciklama);
                    }
                    int toplamGider = preferences.getInt("toplamGider",-99999);
                    //En son girilen tutardan eski tutar çıkarılarak aradaki fark mevcut toplam gelire yansıtıldı.
                    toplamGider += detayTutar - detayTutarEski ;
                    editor.putInt("toplamGider",toplamGider);
                    editor.apply();
                    GelirGider.fragmentCheck = false;
                }
            }
            //Activity Tekrar açıldı
            openActivity2();
        }
        //Başlık SharedPreferencesta key olarak varsa hata mesajı
        else{
            Toast.makeText(getApplicationContext(),"Başlık veya Tutar Boş Bırakılamaz...",Toast.LENGTH_SHORT).show();
        }
    }

    //Activity açma
    public void openActivity2() {
        Intent intent = new Intent(this, GelirGider.class);
        startActivity(intent);
    }
}
