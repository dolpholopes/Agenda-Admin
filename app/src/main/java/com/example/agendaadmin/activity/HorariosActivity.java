package com.example.agendaadmin.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaadmin.R;
import com.example.agendaadmin.adapter.AdapterRecyclerView;
import com.example.agendaadmin.util.Util;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HorariosActivity extends AppCompatActivity implements AdapterRecyclerView.ClickItemRecyclerView{

    private AdapterRecyclerView adapterRecyclerView;
    private RecyclerView recyclerView;

    private List<String> horarios = new ArrayList<String>();
    private List<String> horarios_temp = new ArrayList<String>();
    private ArrayList<String> data = new ArrayList<String>();


    private FirebaseDatabase database;
    private DatabaseReference referenceBuscarHorario;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        getSupportActionBar().setTitle("Escolha o horário");

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance();
        data = getIntent().getStringArrayListExtra("data");


        configurarRecyclerView();
        carregarHorarioFuncionamento();

    }

    //------------------------------------ CONFIGURAR LISTVIEW ------------------------------------------
    private void configurarRecyclerView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRecyclerView =  new AdapterRecyclerView(this, horarios, this);
        recyclerView.setAdapter(adapterRecyclerView);

    }


    //------------------------------------ CARREGAR HORARIOS ------------------------------------------
    private void carregarHorarioFuncionamento(){
        DatabaseReference reference = database.getReference().child("BD").child("Calendario").child("HorariosFuncionamento");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String horario = snapshot.getValue(String.class);
                        horarios.add(horario);
                        horarios_temp.add(horario);

                    }
                    adapterRecyclerView.notifyDataSetChanged();
                  // adapterListView.notifyDataSetChanged();
                    buscarHorarioReservado();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    //------------------------------------ BUSCAR HORARIOS AGENDADOS ------------------------------------------

    private void buscarHorarioReservado(){
        referenceBuscarHorario = database.getReference().child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0));

        if (childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String chave = dataSnapshot.getKey();
                    int index = horarios.indexOf(chave);
                    String horario = chave + " - Reservado";
                    horarios.set(index,horario);
                    adapterRecyclerView.notifyDataSetChanged();
                  //  adapterListView.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String chave = dataSnapshot.getKey();
                    String horario = chave + " - Reservado";
                    int index = horarios.indexOf(horario);
                    horarios.set(index,chave);
                    adapterRecyclerView.notifyDataSetChanged();
                  //  adapterListView.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            referenceBuscarHorario.addChildEventListener(childEventListener);
        }
    }






    //------------------------------------ CLICK ITEM DA LISTA ------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void clickItem(String horario, int position) {

        if (Util.statusInternet_MoWi(getBaseContext())) {
            consultarHorarioSelecionadoBanco(horario, position);
        }else{
            Toast.makeText(getBaseContext(), "Erro - Sem conexao com a internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void consultarHorarioSelecionadoBanco(final String horario, final int position){
        DatabaseReference reference = database.getReference().child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0)).child(horarios_temp.get(position));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getBaseContext(), "Horario Reservado - Exibir  dados do  cliente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), RemoverServicoActivity.class);

                    String horarioSelecionado = horarios_temp.get(position);

                    data.add(3,horarioSelecionado);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(), "Horario disponivel", Toast.LENGTH_SHORT).show();
                    //CHAMANDO A PROXIMA ACTIVITY
                     Intent intent = new Intent(getBaseContext(), AgendamentoServicoActivity.class);
                    data.add(3,horario);
                    intent.putExtra("data", data);
                     startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



/*
    private void chamarProximaActivity(String horario, int position){
        if (horario.contains("Reservado")){
            Toast.makeText(getBaseContext(), "Exibir dados do cliente", Toast.LENGTH_SHORT).show();
        }else{

        }
    }


 */



    //------------------------------------ CICLO DE VIDA DA ACTIVITY ------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (childEventListener == null){
            referenceBuscarHorario.removeEventListener(childEventListener);
            //childEventListener = null;
        }

    }
}
