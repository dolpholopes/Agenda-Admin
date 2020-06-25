package com.example.agendaadmin.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.agendaadmin.R;
import com.example.agendaadmin.model.Agendamento;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoverServicoActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> data = new ArrayList<String>();
    private TextView textNome, textTelefone;
    private CheckBox checkWpp, checkCorte, checkBarba, checkPigmentar, checkPlatinar;
    private CardView cardViewRemover;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    private Agendamento agendamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_servico);
        getSupportActionBar().setTitle("Horário agendado");

        data = getIntent().getStringArrayListExtra("data");
        database = FirebaseDatabase.getInstance();


        textNome = findViewById(R.id.textAlterarRemoverNome);
        textTelefone = findViewById(R.id.textAlterarRemoverTelefone);
        checkWpp = findViewById(R.id.checkAlterarRemoverWpp);
        checkBarba = findViewById(R.id.checkBarba);
        checkCorte = findViewById(R.id.checkCorte);
        checkPigmentar = findViewById(R.id.checkPigmentacao);
        checkPlatinar = findViewById(R.id.checkPlatinacao);
        cardViewRemover = findViewById(R.id.cardViewRemover);


        cardViewRemover.setOnClickListener(this);



        obterDadosAgendamento();
    }


    //-------------------------------- AÇÃO DE CLICK DO CARD VIEW ------------------------------
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.cardViewRemover:
                apagarAgendamento();
                break;

        }
    }




    private void obterDadosAgendamento(){
        reference = database.getReference().child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes")
                .child(data.get(1)).child("dia")
                .child(data.get(0)).child(data.get(3));


       if (valueEventListener == null){
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Agendamento agendamentoCliente = dataSnapshot.getValue(Agendamento.class);
                        agendamento = agendamentoCliente;
                        atualizaDados(agendamento);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference.addValueEventListener(valueEventListener);
        }
    }

    private void atualizaDados(Agendamento agendamento){
        textNome.setText(agendamento.getNome());
        textTelefone.setText(agendamento.getContato());
        checkWpp.setChecked(agendamento.isWpp());
        checkBarba.setChecked(agendamento.isBarba());
        checkCorte.setChecked(agendamento.isCorte());
        checkPigmentar.setChecked(agendamento.isPigmentar());
        checkPlatinar.setChecked(agendamento.isPlatinar());

    }



    private void apagarAgendamento(){
        DatabaseReference databaseReference = database.getReference().child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes")
                .child(data.get(1)).child("dia")
                .child(data.get(0));

        databaseReference.child(data.get(3)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                }else{
                    Toast.makeText(getBaseContext(), "Erro ao apagar o agendamento", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
