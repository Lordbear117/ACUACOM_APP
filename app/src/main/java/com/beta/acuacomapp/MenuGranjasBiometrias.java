package com.beta.acuacomapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class MenuGranjasBiometrias extends AppCompatActivity implements View.OnClickListener {

    private MaterialCardView EltigreCard, CosemarCard, CanachiCard, CospitaCard, AtarrayaCard, AcuacomCard, AguilasCard, TelehuetoCard;

    String NombreMenu="";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_granjas);

        //Aqui van las referencias de las CardView
        EltigreCard = (MaterialCardView) findViewById(R.id.eltigrecardId);
        CosemarCard = (MaterialCardView) findViewById(R.id.cosemarcardId);
        CanachiCard = (MaterialCardView) findViewById(R.id.canachicardId);
        CospitaCard = (MaterialCardView) findViewById(R.id.cospitacardId);
        AtarrayaCard = (MaterialCardView) findViewById(R.id.atarrayacardId);
        AcuacomCard = (MaterialCardView) findViewById(R.id.acuacomcardId);
        AguilasCard = (MaterialCardView) findViewById(R.id.aguilascardId);
        TelehuetoCard = (MaterialCardView) findViewById(R.id.telehuetocardId);

        //Click listeners para las cards
        EltigreCard.setOnClickListener(this);
        CosemarCard.setOnClickListener(this);
        CanachiCard.setOnClickListener(this);
        CospitaCard.setOnClickListener(this);
        AtarrayaCard.setOnClickListener(this);
        AcuacomCard.setOnClickListener(this);
        AguilasCard.setOnClickListener(this);
        TelehuetoCard.setOnClickListener(this);

        //Esto es necesario para poner el menu en la toolbar
        MaterialToolbar toolbar = (MaterialToolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hacen visible la flecha de regreso en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Obtner el valor o ID de la granja correspondiente a traves de INTENT
        Intent i = getIntent();
        if (i!=null){
            NombreMenu = i.getStringExtra("NombreMenu");
        }

        // Cambiar nombre del toolbar

        setTitle("Menú de Granjas: "+NombreMenu);


        // <---------AQUI TERMINA ONCREATE----------> //
    }


    // Metodo para regresar a la activity anterior al hacer click en la flecha

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }




    // MÉTODO SWITCH PARA INCIAR NUEVA VENTANA Y ENVIAR UN ID A LA NUEVA ACTIVITY
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.eltigrecardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "1");
                startActivity(i);
                break;

            case R.id.cosemarcardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "2");
                startActivity(i);
                break;

            case R.id.canachicardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "3");
                startActivity(i);
                break;

            case R.id.cospitacardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "4");
                startActivity(i);
                break;

            case R.id.atarrayacardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "5");
                startActivity(i);
                break;

            case R.id.acuacomcardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "6");
                startActivity(i);
                break;

            case R.id.aguilascardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "7");
                startActivity(i);
                break;

            case R.id.telehuetocardId:
                i = new Intent(this, CapturaBiometrias.class);
                i.putExtra("GranjaId", "8");
                startActivity(i);
                break;

                default: break;
            }
          }
        }
