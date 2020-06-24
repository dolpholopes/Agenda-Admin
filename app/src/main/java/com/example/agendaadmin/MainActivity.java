package com.example.agendaadmin;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.agendaadmin.fragment.FragmentCalendario;
import com.example.agendaadmin.fragment.FragmentHome;
import com.example.agendaadmin.util.Permissao;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    private FragmentHome fragmentHome;
    private FragmentCalendario fragmentCalendario;

    private Fragment fragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        navigationBottom();

        fragmentHome = new FragmentHome();
        fragmentCalendario = new FragmentCalendario();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout_Fragment,fragmentHome).commit();

        permissao();
    }

    private void navigationBottom(){

        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            private MenuItem item;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                switch (menuitem.getItemId()){
                    case R.id.item_navegacao_home:
                        fragment  = fragmentHome;
                        break;

                    case R.id.item_navegacao_calendario:
                        fragment = fragmentCalendario;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment,fragment).commit();

                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }

    private void permissao(){
        String permissoes[] = new String[]{
                Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS
        };

        Permissao.validate(this, 333, permissoes);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
