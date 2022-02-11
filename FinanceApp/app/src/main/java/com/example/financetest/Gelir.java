package com.example.financetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class Gelir extends Fragment {
    FloatingActionButton btn;
    TextView textView7;
    String baslik;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public Gelir() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragmentta çalışmak için view çağırıldı.
        View v =inflater.inflate(R.layout.fragment_gelir, container, false);
        btn = (FloatingActionButton) v.findViewById(R.id.gelirDetay);

        //View'e table ekleme
        addTable(v);


        //Gönderilen fragment bilgisi ile birlikte Detay activitysine geçiş
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Detay.class);
                intent.putExtra("method","Gelir");
                startActivity(intent);
                //openActivity2();
            }
        });
        return v;
    }

    public void addTable(View v){
        TableLayout table = (TableLayout) v.findViewById(R.id.tableLayout);

        preferences = this.getActivity().getSharedPreferences("com.example.financetest", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if(preferences.getInt("gelirCount",99999) != 99999) {

            int count = preferences.getInt("gelirCount", 99999);


            //Döngü ile SharedPreferencesta saklanan kayıtları tablo olarak bastırma
            for (int i = 0; i < count; i++) {

                //Gelir verilerinin varlığını kontrol etme
                if (preferences.getString("gelirBaslik" + i, null) != null) {
                    TableRow tbrow1 = new TableRow(this.getActivity());
                    TextView text1 = new TextView(this.getActivity());
                    text1.setText(preferences.getString("gelirBaslik" + i, null).toString());
                    text1.setTextSize(23);
                    text1.setPadding(0, 0, 100, 0);
                    text1.setGravity(Gravity.CENTER);
                    tbrow1.addView(text1);

                    TextView text2 = new TextView(this.getActivity());
                    text2.setText(preferences.getInt("gelirTutar" + i, 99999) + " TL");
                    text2.setGravity(Gravity.CENTER);
                    text2.setTextSize(23);
                    text2.setPadding(0, 0, 100, 0);
                    tbrow1.addView(text2);

                    FloatingActionButton buttonDetay = new FloatingActionButton(this.getActivity());
                    buttonDetay.setSize(FloatingActionButton.SIZE_MINI);
                    buttonDetay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.arrow));
                    buttonDetay.setTag("gelirDetayButon" + i);

                    //Eklenen gelir kaydını güncellemek için detay activitysine kayıt bilgisi ile geçiş
                    buttonDetay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tag = (String) buttonDetay.getTag();
                            int sayi = Integer.parseInt(tag.substring(tag.length()-1));
                            Intent intent = new Intent(getActivity(),Detay.class);
                            intent.putExtra("gelirKayıtDetay",sayi);
                            intent.putExtra("method","Gelir");
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
                    button.setTag("gelirSil" + i);
                    button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.trash));
                    button.setBackgroundTintList(AppCompatResources.getColorStateList(this.getActivity(), android.R.color.holo_red_light));

                    //Kayıt silme butonu listenerı
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tag = (String) button.getTag();
                            int sayi = Integer.parseInt(tag.substring(tag.length() - 1));

                            //Silinen kayıt fiyatı toplam giderden düşürüldü
                            int toplam = preferences.getInt("toplamGelir", 99999);
                            toplam -= preferences.getInt("gelirTutar" + sayi, 99999);
                            editor.putInt("toplamGelir", toplam);
                            editor.remove("gelirBaslik" + sayi);
                            editor.remove("gelirTutar" + sayi);
                            editor.apply();
                            getActivity().finish();
                            GelirGider.fragmentCheck = true;
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

    public void openActivity2() {

    }
}