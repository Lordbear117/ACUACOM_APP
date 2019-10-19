package com.beta.acuacomapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener{

    private MaterialCardView AlimentoCard, BiometriasCard;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        /**
         Muestra el texto y el botón de LOGOUT
         */
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Bienvenido "+user.getFullName()+", tu sesión expira en "+user.getSessionExpiryDate());

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(MenuPrincipal.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        /**
         Aqui van las referencias de las CardView
         */
        AlimentoCard = (MaterialCardView) findViewById(R.id.alimentocardId);
        BiometriasCard = (MaterialCardView) findViewById(R.id.biometriascardId);

        /**
         * Click listeners para las cards
         */
        AlimentoCard.setOnClickListener(this);
        BiometriasCard.setOnClickListener(this);

        /**
         Esto es necesario para poner el menu en la toolbar
         */
        MaterialToolbar toolbar = (MaterialToolbar)
        findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
                                 <---------AQUI TERMINA ONCREATE---------->
         */
    }

    // Agrega los 3 puntos de la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); // Ref: Carpeta menu
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){

            case R.id.alimentocardId : i = new Intent(this, MenuGranjasAlimento.class);
                i.putExtra("NombreMenu", "Alimento");
            startActivity(i); break;


            case R.id.biometriascardId : i = new Intent(this, MenuGranjasBiometrias.class);
                i.putExtra("NombreMenu", "Biometrias");
            startActivity(i); break;

        default:break;
        }
    }


}
