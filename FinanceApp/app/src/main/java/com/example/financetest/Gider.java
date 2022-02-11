package com.example.financetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Gider extends Fragment {
    TextView textView7;
    String baslik;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Fragment frg;

    public Gider() {
        // Required empty public constructor
    }

    FloatingActionButton btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragmentta çalışmak için view çağırıldı.
        View v =inflater.inflate(R.layout.fragment_gider, container, false);
        btn = (FloatingActionButton) v.findViewById(R.id.giderDetay);
        preferences = this.getActivity().getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);

        //View'e table ekleme
        addTable(v);

        //Gönderilen fragment bilgisi ile birlikte Detay activitysine geçiş
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Detay.class);
                intent.putExtra("method","Gider");
                startActivity(intent);
                //openActivity2();
            }
        });
        return v;
    }

    public void addTable(View v){

        TableLayout table = (TableLayout) v.findViewById(R.id.tableLayoutGider);


        preferences = this.getActivity().getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if(preferences.getInt("giderCount",99999) != 99999) {

            int count = preferences.getInt("giderCount", 99999);

            //Döngü ile SharedPreferencesta saklanan kayıtları tablo olarak bastırma
            for (int i = 0; i < count; i++) {

                //Gider verilerinin varlığını kontrol etme
                if (preferences.getString("giderBaslik" + i, null) != null) {
                    TableRow tbrow1 = new TableRow(this.getActivity());
                    TextView text1 = new TextView(this.getActivity());
                    text1.setText(preferences.getString("giderBaslik" + i, null).toString());
                    text1.setTextSize(23);
                    text1.setPadding(0, 0, 100, 0);
                    text1.setGravity(Gravity.CENTER);
                    tbrow1.addView(text1);

                    TextView text2 = new TextView(this.getActivity());
                    text2.setText(preferences.getInt("giderTutar" + i, 99999) + " TL");
                    text2.setGravity(Gravity.CENTER);
                    text2.setTextSize(23);
                    text2.setPadding(0, 0, 100, 0);
                    tbrow1.addView(text2);

                    FloatingActionButton buttonDetay = new FloatingActionButton(this.getActivity());
                    buttonDetay.setSize(FloatingActionButton.SIZE_MINI);
                    buttonDetay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.arrow));
                    buttonDetay.setTag("giderDetayButon" + i);

                    //Eklenen gider kaydını güncellemek için detay activitysine kayıt bilgisi ile geçiş
                    buttonDetay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tag = (String) buttonDetay.getTag();
                            int sayi = Integer.parseInt(tag.substring(tag.length()-1));
                            Intent intent = new Intent(getActivity(),Detay.class);
                            intent.putExtra("giderKayıtDetay",sayi);
                            intent.putExtra("method","Gider");
                            startActivity(intent);
                        }
                    });
                    tbrow1.addView(buttonDetay);

                    TextView text3 = new TextView(this.getActivity());
                    text3.setText("as");
                    text3.setGravity(Gravity.CENTER);
                    text3.setTextSize(23);
                    text3.setVisibility(View.INVISIBLE);
                    tbrow1.addView(text3);

                    FloatingActionButton button = new FloatingActionButton(this.getActivity());
                    button.setSize(FloatingActionButton.SIZE_MINI);
                    button.setTag("giderSil" + i);
                    button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.trash));
                    button.setBackgroundTintList(AppCompatResources.getColorStateList(this.getActivity(), android.R.color.holo_red_light));

                    //Kayıt silme butonu listenerı
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tag = (String) button.getTag();
                            int sayi = Integer.parseInt(tag.substring(tag.length() - 1));

                            //Silinen kayıt fiyatı toplam gelirden düşürüldü
                            int toplam = preferences.getInt("toplamGider", 99999);
                            toplam -= preferences.getInt("giderTutar" + sayi, 99999);
                            editor.putInt("toplamGider", toplam);
                            editor.remove("giderBaslik" + sayi);
                            editor.remove("giderTutar" + sayi);
                            editor.apply();
                            getActivity().finish();
                            GelirGider.fragmentCheck = false;
                            Intent intent = new Intent(getActivity(),GelirGider.class);
                            startActivity(intent);

                        }
                    });
                    tbrow1.addView(button);

                    table.addView(tbrow1);
                }
            }
        }
    }

}