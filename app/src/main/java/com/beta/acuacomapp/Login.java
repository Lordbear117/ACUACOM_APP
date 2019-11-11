package com.beta.acuacomapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private String login_url = "http://10.0.1.102/member/login.php";
    private SessionHandler session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        // Revisa si hay una sesión activa para decidir que ACTIVITY abrir al inicio de la app
        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_login);

        //  Se declaran las variables y se le da acción al botón de inicio
        etUsername =  findViewById(R.id.etNombre);
        etPassword =  findViewById(R.id.etContraseña);

        Button login = findViewById(R.id.btnLogin);
        Button register = findViewById(R.id.btnRegister);


        //  Lanza la ventana de Registro cuando el boton de Registro es presionado
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        // CliclListener de Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Recupera los datos introducidos en los EditText
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
              }
            }
          );

        /**
                               <---------AQUI TERMINA ONCREATE---------->
         */
    }

    /**
       Lanza el Menu Principal en un Login exitoso
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(i);
        finish();
    }

    /**
       Muestra la barra de progreso durante el Logging In
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Logging In.. Porfavor espera...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     METODO PARA LOGEAR AL USUARIO
     */

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //  Llena los parametros solicitados por el servidor
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //  Revisa si el usuario se logeo exitosamente

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(username,response.getString(KEY_FULL_NAME));
                                loadDashboard();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //  Muestra un mensaje de error siempre que uno suceda
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Valida los campos y muestra errores si existen
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(username)){
            etUsername.setError("Nombre de Usuario no puede estar vacío");
            etUsername.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Contraseña no puede estar vacía");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

}
