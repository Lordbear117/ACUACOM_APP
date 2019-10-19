package com.beta.acuacomapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    private ArrayList<AlimentoModel> alimentosModelArrayList;
    private ArrayList<String> nombreAlimento = new ArrayList<String>();
    private ArrayList<String> alimento = new ArrayList<String>();

    private ArrayList<TipoModel> tiposModelArrayList;
    private ArrayList<String> nombreTipo = new ArrayList<String>();
    private ArrayList<String> tipo = new ArrayList<String>();

    String dataZona, dataEstanque, dataAlimento, dataTipo;

    // Arreglo que guarda la conversion de ARRAYLIST a ARRAY
    String[] arrayZonas;

    String[] arrayEstanques;

    String[] arrayAlimentos;

    String[] arrayTipos;


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
    Spinner SpZona, SpEstanque, SpAlimento, SpTipo, SpCiclo;

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
    EditText Cantidad, FechaIni, FechaFin, Granja, Unidad;

    // Creando un botón
    Button InsertButton;

    //-------------------------------------------------

    // Creabdo una Volley RequestQueue
    RequestQueue requestQueue;

    // Variables string que almacenan información para guardar en la BD.
    String AlimentoHolder, TipoHolder, CantidadHolder, FechaHolder, GranjaHolder, CicloHolder, ZonaHolder, EstanqueHolder, UnidadHolder;

    // Creando un Pregress Dialog
    ProgressDialog progressDialog;

    // Guarda la URL del servidor en una variable String.
    String HttpUrl = "http://10.0.1.195/member/insertar_alimento.php";

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
        SpAlimento = (Spinner) findViewById(R.id.SpinAlimento);
        SpTipo = (Spinner) findViewById(R.id.SpinTipo);
        SpCiclo = (Spinner) findViewById(R.id.SpinCiclo);

        /**
         Creación del spinner de alimento
         */
          //  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
           // R.array.alimento, android.R.layout.simple_spinner_item);

         //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

           // SpAlimento.setAdapter(adapter);

        //Adding setOnItemSelectedListener method on spinner.
        SpAlimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
              //  Toast.makeText(CapturaAlimento.this, SpAlimento.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                dataAlimento = arrayAlimentos[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        /**
         Creación del spinner de tipo
         */
           //  ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
            // R.array.tipo, android.R.layout.simple_spinner_item);

             //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

             // SpTipo.setAdapter(adapter1);

        //Adding setOnItemSelectedListener method on spinner.
        SpTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
               // Tipo = SpTipo.getSelectedItem().toString();
                dataTipo = arrayTipos[arg2];

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /**
         // Creación del spinner zona
         */
            // ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
          //   R.array.zona, android.R.layout.simple_spinner_item);

           //  adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //     SpZona.setAdapter(adapter2);

        //Adding setOnItemSelectedListener method on spinner.
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
           // ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
         //   R.array.estanque, android.R.layout.simple_spinner_item);

          //  adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

           //  SpEstanque.setAdapter(adapter3);

        //Adding setOnItemSelectedListener method on spinner.
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

        //Adding setOnItemSelectedListener method on spinner.
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
        Cantidad = (EditText) findViewById(R.id.etCantidad);
        FechaIni = (EditText) findViewById(R.id.etFechaIni);
        FechaFin = (EditText) findViewById(R.id.etFechaFin);
        Granja = (EditText) findViewById(R.id.etGranja);
        Unidad = (EditText) findViewById(R.id.etUnidad);


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
                        Cantidad.setText("");
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
        Unidad.setText("1");

        // Cambiar nombre del toolbar
        NombreGranja = ArrayNombreGranjas[Integer.parseInt(GranjaId)];
        setTitle("Captura de Biometrias: "+NombreGranja);



        // Método para obtner Zonas con volley de la base de datos
        getVolleyZonas();
        getVolleyAlimentos();
        getVolleyTipos();


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




    // Metodo para obtener los valores de los EditText
    public void GetValueFromEditText(){
        FechaHolder = FechaIni.getText().toString().trim();
      //  FechaIniHolder = FechaIni.getText().toString().trim();
      //  FechaFinHolder = FechaFin.getText().toString().trim();
        ZonaHolder = dataZona;
        EstanqueHolder = dataEstanque;
        CicloHolder  = Ciclo;


        AlimentoHolder = dataAlimento;

        TipoHolder = dataTipo;

        CantidadHolder = Cantidad.getText().toString().trim();
        GranjaHolder = Granja.getText().toString().trim();

        UnidadHolder = Unidad.getText().toString().trim();
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
                params.put("tipo", TipoHolder);
                params.put("alimento", AlimentoHolder);
                params.put("unidad", UnidadHolder);
                params.put("cantidad", CantidadHolder);
                params.put("fecha", FechaHolder);
                //params.put("fecha", FechaHolder);   Fecha Incial
                //params.put("fecha", FechaHolder);   Fecha Final

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
        if(KEY_EMPTY.equals(CantidadHolder)){
            Cantidad.setError("Cantidad no puede estar vacio");
            Cantidad.requestFocus();
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

        URLline = "http://10.0.1.195/member/get_zonas.php?granjas="+GranjaId;

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

        URLline = "http://10.0.1.195/member/get_estanque.php?zonas="+dataZona;

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




    private void getVolleyAlimentos(){

        URLline = "http://10.0.1.195/member/get_alimento.php";

        Log.d("getxxx",URLline);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        // Toast.makeText(CapturaAlimento.this,response,Toast.LENGTH_LONG).show();
                        // Aqui le envia el "response" al otro metodo para obtener la informacion del arreglo
                        retrieveJSONAlimentos(response);
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

    private void retrieveJSONAlimentos(String response) {



        //showSimpleProgressDialog(this, "Loading...","Fetching Json",false);
        try{
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                alimentosModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                // Se limpian los ArrayList
                // Sirve para llenar el spinner siempre con la infromacion nueva
                // La informacion relacionada con el spinner padre

                for (int i = 0; i < dataArray.length(); i++) {

                    AlimentoModel dataModel = new AlimentoModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    dataModel.setAlimento(dataobj.getString("alimento_id"));
                    dataModel.setNombreAlimento(dataobj.getString("nombre"));

                    alimentosModelArrayList.add(dataModel);
                }

                for (int i = 0; i < alimentosModelArrayList.size(); i++){
                    nombreAlimento.add(alimentosModelArrayList.get(i).getNombreAlimento().toString());
                    alimento.add(alimentosModelArrayList.get(i).getAlimento().toString());
                }
                arrayAlimentos = alimento.toArray(new String[] {});

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CapturaBiometrias.this, simple_spinner_item, nombreAlimento);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                SpAlimento.setAdapter(adapter);

                //    removeSimpleProgressDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void getVolleyTipos(){

        URLline = "http://10.0.1.195/member/get_tipos.php";

        Log.d("getxxx",URLline);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        // Toast.makeText(CapturaAlimento.this,response,Toast.LENGTH_LONG).show();
                        // Aqui le envia el "response" al otro metodo para obtener la informacion del arreglo
                        retrieveJSONTipos(response);
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

    private void retrieveJSONTipos(String response) {



        //showSimpleProgressDialog(this, "Loading...","Fetching Json",false);
        try{
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                tiposModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                // Se limpian los ArrayList
                // Sirve para llenar el spinner siempre con la infromacion nueva
                // La informacion relacionada con el spinner padre

                for (int i = 0; i < dataArray.length(); i++) {

                    TipoModel dataModel = new TipoModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    dataModel.setTipo(dataobj.getString("tipo_id"));
                    dataModel.setNombreTipo(dataobj.getString("nombre"));

                    tiposModelArrayList.add(dataModel);
                }

                for (int i = 0; i < tiposModelArrayList.size(); i++){
                    nombreTipo.add(tiposModelArrayList.get(i).getNombreTipo().toString());
                    tipo.add(tiposModelArrayList.get(i).getTipo().toString());
                }
                arrayTipos = tipo.toArray(new String[] {});

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CapturaBiometrias.this, simple_spinner_item, nombreTipo);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                SpTipo.setAdapter(adapter1);

                //    removeSimpleProgressDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






}