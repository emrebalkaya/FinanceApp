package com.example.financetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class GelirGider extends AppCompatActivity {
    float x1,x2,y1,y2;
    FloatingActionButton btn;
    public static boolean fragmentCheck = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gelir_gider);


        //Gelir ve gider fragmentları arası geçiş için TabLayout oluşturuldu.
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.beginFakeDrag();

        //Sayfalar arası geçiş için viewPager ve PagerAdapter kütüphaneleri kullanıldı.
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        //Ekleme ve silme işleminden sonra en son kalınan sayfayı açmak için kontroller.
        if(fragmentCheck){
            viewPager.setCurrentItem(0);
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tabLayout.selectTab(tab);
        }
        else {
            viewPager.setCurrentItem(1);
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tabLayout.selectTab(tab);
        }

        btn = (FloatingActionButton) findViewById(R.id.homeButonu);//Anasayfaya dönüş butonu


        //Buton ile anasayfaya dönmek için onClickListener tanımlandı.
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });


        //TabLayoutta tıklama ile geçiş için onTabSelectedListener tanımlandı.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Anasayfaya dönüş
    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //Uygulama kapatıldığında fragmentCheck sıfırlama
    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentCheck = true;
    }

}
