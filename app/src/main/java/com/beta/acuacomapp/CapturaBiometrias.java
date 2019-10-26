package com.beta.acuacomapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.layout.simple_spinner_item;

public class CapturaBiometrias extends AppCompatActivity implements View.OnClickListener {

    //-------------------------------------------------

    // Variables relacionadas con los Spinner y Volley

    private String URLline = "";

    private ArrayList<ZonasModel> zonasModelArrayList;
    private ArrayList<String> descripcion = new ArrayList<String>();
    private ArrayList<String> zona = new ArrayList<String>();

    private ArrayList<EstanqueModel> estanquesModelArrayList;
    private ArrayList<String> descripcionEstanque = new ArrayList<String>();
    private ArrayList<String> estanque = new ArrayList<String>();



    String dataZona, dataEstanque;

    // Arreglo que guarda la conversion de ARRAYLIST a ARRAY
    String[] arrayZonas;

    String[] arrayEstanques;



    //---------------------------------------------------

    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    ImageButton ibObtenerFechaIni;
    ImageButton ibObtenerFechaFin;

    //-------------------------------------------------

    // Used to compare empty EditText
    private static final String KEY_EMPTY = "";

    //-------------------------------------------------

    //  Spinners nuevos
    Spinner SpZona, SpEstanque, SpCiclo;

    // Contenedores de los datos del spinner
    String  Ciclo;

    // Guarda el nombre de la granja actual usando el Id de Granja y obteniendo el nombre de un arreglo
    String  NombreGranja;


    //-------------------------------------------------

    // String que recibe del INTENT de la activity de MENU GRANJAS
    String GranjaId;

    // Pone la fecha actual en un string
    Date cDate = new Date();
    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    // Creando EditText
    EditText Cantidad, FechaIni, FechaFin, Granja, PesoProm, PorcentajeSobre,PorcentajeRecam, OxigenoMin, OxigenoMax
            , TemperaturaMin, TemperaturaMax, SalinidadProm, PhProm, TurbulenciaProm;

    // Creando un botón
    Button InsertButton;

    //-------------------------------------------------

    // Creabdo una Volley RequestQueue
    RequestQueue requestQueue;

    // Variables string que almacenan información para guardar en la BD.
    String  FechaIniHolder, FechaFinHolder, GranjaHolder, CicloHolder, ZonaHolder, EstanqueHolder, PesoPromHolder, PorcentajeSobreHolder, PorcentajeRecamHolder, OxigenoMinHolder, OxigenoMaxHolder
            , TemperaturaMinHolder, TemperaturaMaxHolder, SalinidadPromHolder, PhPromHolder, TurbulenciaPromHolder;

    // Creando un Pregress Dialog
    ProgressDialog progressDialog;

    // Guarda la URL del servidor en una variable String.
    String HttpUrl = "http://10.0.1.84/member/insertar_biometrias.php";



    String[] ArrayNombreGranjas ={"LUGAR 0","EL TIGRE","COSEMAR","CANACHI","COSPITA","ATARRAYA","ACUACOM","AGUILAS","TELEHUETO"};


  //  String[] alimento_ID = getResources().getStringArray(R.array.alimento_ID);

    //-------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_biometrias);



        // Obtener fecha con boton - - Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFechaIni = (ImageButton) findViewById(R.id.id_obtener_fechaIni);
        ibObtenerFechaIni.setOnClickListener(this);

        ibObtenerFechaFin = (ImageButton) findViewById(R.id.id_obtener_fechaFin);
        ibObtenerFechaFin.setOnClickListener(this);

        //  Esto es necesario para poner el menu en la toolbar
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hacen visible la flecha de regreso en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //region TODO de Sppiners

        //  Asignar ID a los Spinner
        SpZona = (Spinner) findViewById(R.id.SpinZona);
        SpEstanque = (Spinner) findViewById(R.id.SpinEstanque);
        SpCiclo = (Spinner) findViewById(R.id.SpinCiclo);




        /**
         // Creación del spinner zona
         */

        SpZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                dataZona = arrayZonas[arg2];
                getVolleyEstanques();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /**
         Creación del spinner estanque
         */

        SpEstanque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
               // Estanque = SpEstanque.getSelectedItem().toString();
                dataEstanque  = arrayEstanques[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /**
         Creación del spinner ciclo
         */
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.ciclo, android.R.layout.simple_spinner_item);

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpCiclo.setAdapter(adapter4);


        SpCiclo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Ciclo = SpCiclo.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //endregion


        //region TODO de EditText´s y Button

        // Asigna ID´s a los EditText.
        FechaIni = (EditText) findViewById(R.id.etFechaIni);
        FechaFin = (EditText) findViewById(R.id.etFechaFin);
        Granja = (EditText) findViewById(R.id.etGranja);
        PesoProm = (EditText) findViewById(R.id.etPesoProm);
        PorcentajeSobre = (EditText) findViewById(R.id.etPorcentajeSobre);
        PorcentajeRecam = (EditText) findViewById(R.id.etPorcentajeRecam);
        OxigenoMin = (EditText) findViewById(R.id.etOxigenoMin);
        OxigenoMax = (EditText) findViewById(R.id.etOxigenoMax);
        TemperaturaMin = (EditText) findViewById(R.id.etTemperaturaMin);
        TemperaturaMax = (EditText) findViewById(R.id.etTemperaturaMax);
        SalinidadProm = (EditText) findViewById(R.id.etSalinidadProm);
        PhProm = (EditText) findViewById(R.id.etPhProm);
        TurbulenciaProm = (EditText) findViewById(R.id.etTurbulenciaProm);





        // Asigna ID para el boton
        InsertButton = (Button) findViewById(R.id.btnRegistrar);

        // Crea una  Volley newRequestQueue
        requestQueue = Volley.newRequestQueue(CapturaBiometrias.this);

        progressDialog = new ProgressDialog(CapturaBiometrias.this);

            // Agrega un clickListener al boton
            InsertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Llama al metodo para obtener los valores de los EditText
                    GetValueFromEditText();

                    //  Valida que el TextView contenga informacion
                    if(validateInputs()) {
                        // Muestra el dialogo de progreso mientras se registra la informacion
                        progressDialog.setMessage("Porfavor espera, estamos registrando tu información");
                        progressDialog.show();
                        RegisterData();
                        // Vaciar los campos de texto cuando se registre la informacion
                        PesoProm.setText("");
                    }
                }
            });

            //endregion


        //region INTENT Y LLENAR EDITEXT AL INICIAR ACTIVITY

        // Obtner el valor o ID de la granja correspondiente a traves de INTENT
        Intent i = getIntent();
        if (i!=null){
            GranjaId = i.getStringExtra("GranjaId");
        }

        // Ingresa el ID de granja en el EditText
        Granja.setText(GranjaId);

        // Ingresa la fecha  en el EditText
        FechaIni.setText(fDate);
        FechaFin.setText(fDate);

        // Cambiar nombre del toolbar
        NombreGranja = ArrayNombreGranjas[Integer.parseInt(GranjaId)];
        setTitle("Captura de Biometrias: "+NombreGranja);

        /**
         FILTROS
         */
        // Filtro para solo poner 4 decimales
        PesoProm.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,4)});
        PorcentajeSobre.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,2)});
        PorcentajeRecam.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,4)});

        OxigenoMin.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        OxigenoMax.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        TemperaturaMin.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        TemperaturaMax.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        SalinidadProm.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        PhProm.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,1)});
        TurbulenciaProm.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,1)});




        // Método para obtner Zonas con volley de la base de datos
        getVolleyZonas();



        //endregion


        // <---------AQUI TERMINA ONCREATE----------> //
    }

    //region ---MÉTODOS---

    // Metodo para regresar a la activity anterior al hacer click en la flecha

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }




    // Filtro que permite poner una cantidad determinada de digitos antes y despues del punto ( . )
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }


    // Metodo para obtener los valores de los EditText
    public void GetValueFromEditText(){
        ZonaHolder = dataZona;
        EstanqueHolder = dataEstanque;

        FechaIniHolder = FechaIni.getText().toString().trim();
        FechaFinHolder = FechaFin.getText().toString().trim();

        CicloHolder  = Ciclo;

        GranjaHolder = Granja.getText().toString().trim();

        PesoPromHolder = PesoProm.getText().toString().trim();
        PorcentajeSobreHolder = PorcentajeSobre.getText().toString().trim();
        PorcentajeRecamHolder = PorcentajeRecam.getText().toString().trim();

        OxigenoMinHolder = OxigenoMin.getText().toString().trim();
        OxigenoMaxHolder = OxigenoMax.getText().toString().trim();
        TemperaturaMinHolder = TemperaturaMin.getText().toString().trim();
        TemperaturaMaxHolder = TemperaturaMax.getText().toString().trim();
        SalinidadPromHolder = SalinidadProm.getText().toString().trim();
        PhPromHolder = PhProm.getText().toString().trim();
        TurbulenciaPromHolder = TurbulenciaProm.getText().toString().trim();
    }

    // Metodo para registrar la informacion en la base de datos
    private void RegisterData(){
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing response message coming from server.
                        Toast.makeText(CapturaBiometrias.this, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(CapturaBiometrias.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("granja", GranjaHolder);
                params.put("zona", ZonaHolder);
                params.put("estanque", EstanqueHolder);
                params.put("ciclo", CicloHolder);
                params.put("fechaIni", FechaIniHolder);
                params.put("fechaFin", FechaFinHolder);

                params.put("peso_prom", PesoPromHolder);
                params.put("porcentaje_sobre", PorcentajeSobreHolder);
                params.put("porcentaje_recam", PorcentajeRecamHolder);
                params.put("oxigeno_min", OxigenoMinHolder);
                params.put("oxigeno_max", OxigenoMaxHolder);
                params.put("temperatura_min", TemperaturaMinHolder);
                params.put("temperatura_max", TemperaturaMaxHolder);
                params.put("salinidad_prom", SalinidadPromHolder);
                params.put("ph_prom", PhPromHolder);
                params.put("turbulencia_prom", TurbulenciaPromHolder);

                return params;
            }

        };

        // Creating RequestQueue.
       // RequestQueue requestQueue = Volley.newRequestQueue(CapturaBiometrias.this);

        // Adding the StringRequest object into requestQueue.
        //requestQueue.add(stringRequest);

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    /**
     * Valida ingresos y muestra los errores si existe alguno
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(PesoPromHolder)){
            PesoProm.setError("Cantidad no puede estar vacio");
            PesoProm.requestFocus();
            return false;

        }else if(KEY_EMPTY.equals(PorcentajeSobreHolder)){
            PorcentajeSobre.setError("Cantidad no puede estar vacio");
            PorcentajeSobre.requestFocus();
            return false;
        }else if(KEY_EMPTY.equals(PorcentajeRecamHolder)){
            PorcentajeRecam.setError("Cantidad no puede estar vacio");
            PorcentajeRecam.requestFocus();
            return false;
        }
        return true;
    }

    // Mofificar  para ambas fechas -------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_obtener_fechaIni:
                obtenerFechaIni();
                break;

            case R.id.id_obtener_fechaFin:
                obtenerFechaFin();
                break;
        }
    }





    // Mofificar  para ambas fechas -------------------------------------------------------------------------------------------------------
        private void obtenerFechaIni() {
            DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                    final int mesActual = month + 1;
                    //Formateo el día obtenido: antepone el 0 si son menores de 10
                    String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    //Formateo el mes obtenido: antepone el 0 si son menores de 10
                    String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    //Muestro la fecha con el formato deseado
                    FechaIni.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);


                }
                //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                /*
                 *También puede cargar los valores que usted desee
                 */
            }, anio, mes, dia);
            //Muestro el widget
            recogerFecha.show();
        }


    private void obtenerFechaFin() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                FechaFin.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /*
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

        //endregion


    private void getVolleyZonas(){

        URLline = "http://10.0.1.84/member/get_zonas.php?granjas="+GranjaId;

        Log.d("getxxx",URLline);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        //Toast.makeText(CapturaAlimento.this,response,Toast.LENGTH_LONG).show();
                        // Aqui le envia el "response" al otro metodo para obtener la informacion del arreglo
                        retrieveJSONZonas(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
       // RequestQueue requestQueue = Volley.newRequestQueue(this);

       // requestQueue.add(stringRequest);

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void retrieveJSONZonas(String response) {

        //showSimpleProgressDialog(this, "Loading...","Fetching Json",false);
        try{
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                zonasModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");



                for (int i = 0; i < dataArray.length(); i++) {

                    ZonasModel dataModel = new ZonasModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    dataModel.setZona(dataobj.getString("zona_id"));
                    dataModel.setDescripcion(dataobj.getString("descripcion"));

                    zonasModelArrayList.add(dataModel);
                }

                for (int i = 0; i < zonasModelArrayList.size(); i++){
                    descripcion.add(zonasModelArrayList.get(i).getDescripcion().toString());
                    zona.add(zonasModelArrayList.get(i).getZona().toString());
                }
                arrayZonas = zona.toArray(new String[] {});


                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CapturaBiometrias.this, simple_spinner_item, descripcion);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                SpZona.setAdapter(adapter2);

                //    removeSimpleProgressDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    private void getVolleyEstanques(){

        URLline = "http://10.0.1.84/member/get_estanque.php?zonas="+dataZona;

        Log.d("getxxx",URLline);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                       // Toast.makeText(CapturaAlimento.this,response,Toast.LENGTH_LONG).show();
                        // Aqui le envia el "response" al otro metodo para obtener la informacion del arreglo
                        retrieveJSONEstanques(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
       // RequestQueue requestQueue = Volley.newRequestQueue(this);

        //requestQueue.add(stringRequest);

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void retrieveJSONEstanques(String response) {



        //showSimpleProgressDialog(this, "Loading...","Fetching Json",false);
        try{
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                estanquesModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                // Se limpian los ArrayList
                // Sirve para llenar el spinner siempre con la infromacion nueva
                // La informacion relacionada con el spinner padre

                estanquesModelArrayList.clear();
                descripcionEstanque.clear();
                estanque.clear();

                for (int i = 0; i < dataArray.length(); i++) {

                    EstanqueModel dataModel = new EstanqueModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    dataModel.setEstanque(dataobj.getString("estanque_id"));
                    dataModel.setDescripcionEstanque(dataobj.getString("descripcion"));

                    estanquesModelArrayList.add(dataModel);
                }

                for (int i = 0; i < estanquesModelArrayList.size(); i++){
                    descripcionEstanque.add(estanquesModelArrayList.get(i).getDescripcionEstanque().toString());
                    estanque.add(estanquesModelArrayList.get(i).getEstanque().toString());
                }
                arrayEstanques = estanque.toArray(new String[] {});

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CapturaBiometrias.this, simple_spinner_item, descripcionEstanque);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                SpEstanque.setAdapter(adapter3);

                //    removeSimpleProgressDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }









}