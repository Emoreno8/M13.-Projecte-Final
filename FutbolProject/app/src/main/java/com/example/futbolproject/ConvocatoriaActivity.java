package com.example.futbolproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.futbolproject.mMySQL.Downloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ConvocatoriaActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    //Calendarii
    static TextView mTv;
    Button mBtn;
    Calendar c;
    DatePickerDialog dpd;

    //Spinner objectes declarats
    static Spinner spinner;
    static ListView llistaJugadors;
    static ArrayAdapter jugadorsList;
    static ArrayList<String> listaNombres;
    static String arrayJ[] = new String[18];
    static int arrayMovils[] = new int[18];
    static int contJ;

    //ArrayList de l' Spinner Items
    static ArrayList<String> jugadors;

    //JSON Array
    private JSONArray result;

    //TextViews declarats
    private TextView textViewMovil;
    private TextView textViewDorsal;
    private TextView textViewNom;

    static String nomClub = LoginEquip.nom;

    // Convocar
    Button convocar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocatoria);

        mTv = (TextView) findViewById(R.id.textView6);
        mBtn = (Button) findViewById(R.id.buton);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int any = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(ConvocatoriaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        mTv.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, dia, mes, any);
                dpd.show();
            }
        });

        //Llista listView
        for(int i=0; i<18; i++){
            arrayJ[i]=""+(i+1);
        }

        llistaJugadors=(ListView) findViewById(R.id.listview);

        //Initialitzar ArrayList
        jugadors = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        // Agregar un elemento de escucha seleccionado a nuestro Spinner
        // Como hemos implementado la clase Spinner.OnItemSelectedListener a esta clase, estamos pasando esto a setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);


        //Initializing TextViews
        textViewMovil = (TextView) findViewById(R.id.textViewMovil);
        textViewDorsal = (TextView) findViewById(R.id.textViewDorsal);
        textViewNom = (TextView) findViewById(R.id.textViewNom);

        // Este método obtendrá los datos de la URL
        getData();

        //Avtiviti convocar
        convocar = findViewById(R.id.btnConvocar);

        convocar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConvocatoriaActivity.this, SmsActivity.class);
                startActivity(intent);
            }
        });


    }


    //----------------------------------------------------
    //SPINNER
    private void getData(){

        // Crear una solicitud de cadena
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            // Analizando la cadena Json obtenida al objeto JSON
                            j = new JSONObject(response);

                            // Almacenar la matriz de cadenas JSON en nuestra matriz JSON
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Método de llamada getJugadors para obtener los noms de la matriz JSON
                            getJugadors(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getJugadors(JSONArray j){

        // Recorriendo todos los elementos de la matriz json
        for(int i=0;i<j.length();i++){
            try {

                // Obteniendo objeto json
                JSONObject json = j.getJSONObject(i);

                // Agregar el nombre del alumno a la lista de matriz
                jugadors.add(json.getString(Config.TAG_NOM));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Configuración del adaptador para mostrar los elementos en la ruleta
        spinner.setAdapter(new ArrayAdapter<String>(ConvocatoriaActivity.this, android.R.layout.simple_spinner_dropdown_item, jugadors));

    }

    private String getNom(int position){
        String nom="";
        try {
            JSONObject json = result.getJSONObject(position);
            nom = json.getString(Config.TAG_NOM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nom;
    }

    // Método para obtener el nombre del estudiante de un puesto en particular
    private String getMail(int position){
        String movil="";
        try {
            //Obteniendo objeto de índice dado
            JSONObject json = result.getJSONObject(position);

            //Obteniendo el nombre de ese objeto
            movil = json.getString(Config.TAG_MOVIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Retornem el nom
        return movil;
    }

    // Haciendo lo mismo con este método como lo hicimos con getName ()
    private String getDorsal(int position){
        String dorsal="";
        try {
            JSONObject json = result.getJSONObject(position);
            dorsal = json.getString(Config.TAG_DORSAL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dorsal;
    }

    // Este método se ejecutará cuando hagamos un clic a un elemento del spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Establecer los valores en vistas de texto para un elemento seleccionado
        textViewNom.setText(getNom(position));
        textViewMovil.setText(getMail(position));
        textViewDorsal.setText(getDorsal(position));

        guardarMovils();
        insertJugador();

    }

    // guardar jugadors a una array i mostrarlos a la llista
    public void insertJugador(){
        if(contJ >= 0){
            String jugador=textViewNom.getText().toString();
            listaNombres= new ArrayList<String>();
            //Jugadors array
            arrayJ[contJ]= jugador;
            contJ++;
            //Agregar a la ViewList
            for(int i=0; i<18; i++){
                listaNombres.add(arrayJ[i]);
            }
            //Array Adapter
            jugadorsList=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaNombres);
            llistaJugadors.setAdapter(jugadorsList);
        }else{
            listaNombres= new ArrayList<String>();
            contJ++;
            for(int i=0; i<18; i++){
                listaNombres.add(arrayJ[i]);
            }
            //Array Adapter
            jugadorsList=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaNombres);
            llistaJugadors.setAdapter(jugadorsList);
        }

    }

    // guardar numeros de jugadors per enviar misatge
    public void guardarMovils(){
        if(contJ >= 0){
            arrayMovils[contJ]= Integer.parseInt(textViewMovil.getText().toString());
        }
    }

    // Cuando no se selecciona ningún elemento, este método se ejecutará
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewNom.setText("");
        textViewMovil.setText("");
        textViewDorsal.setText("");
    }


}
