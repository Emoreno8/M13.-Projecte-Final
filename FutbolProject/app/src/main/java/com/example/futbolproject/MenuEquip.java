package com.example.futbolproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    public class MenuEquip extends AppCompatActivity {

    Button actJugadors, actCalendari, actConvocatoria, llista;

    TextView Equip;

    ListView listaResultado;


    static ArrayAdapter arrayAdapter;
    //ArrayList de Items
    static ArrayList<String> arrayList;
    //static String arrayJ[];
    //JSON Array
    private JSONArray result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_equip);

        Equip=findViewById(R.id.textView5);
        String nomEquip = LoginEquip.nom;
        Equip.setText(nomEquip);

        actJugadors = findViewById(R.id.button5);
        actCalendari = findViewById(R.id.button6);
        actConvocatoria = findViewById(R.id.button8);

        actJugadors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuEquip.this, CrearJugadors.class);
                startActivity(intent);
            }
        });

        actCalendari.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuEquip.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        actConvocatoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ConvocatoriaActivity.contJ = -1;
                Intent intent = new Intent(MenuEquip.this, ConvocatoriaActivity.class);
                startActivity(intent);
            }
        });

        llista =findViewById(R.id.btnList);

        //listJug=(ListView) findViewById(R.id.listview1);
        listaResultado=(ListView) findViewById(R.id.listview1);

        llista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String consulta = "http://192.168.1.17/pfutbol/pList.php";
                EnviarRecibirDatos(consulta);
                llJ();
                //getData();
            }
        });

    }

        public void llJ(){

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.17/pfutbol/pList.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject j = null;
                            try {
                                // Analizando la cadena Json obtenida al objeto JSON
                                j = new JSONObject(response);
                                // Almacenar la matriz de cadenas JSON en nuestra matriz JSON
                                result = j.getJSONArray(Config.JSON_ARRAY);

                                CargarListView(result);
                                //Método de llamada getJugadors para obtener los noms de la matriz JSON
                                //getJugadors(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //}

                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);

                //-------------
                        arrayList=new ArrayList<String>();
                for(
                        int i = 0;
                        i<10;i++)

                        {
                            arrayList.add(" "+i);
                        }
                        //Array Adapter
                        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
                listaResultado.setAdapter(arrayAdapter);
                //--------------

        }

        private void getJugadors(JSONArray j) {

            // Recorriendo todos los elementos de la matriz json
            for (int i = 0; i < j.length(); i++) {
                try {
                    // Obteniendo objeto json
                    JSONObject json = j.getJSONObject(i);
                    // Agregar el nombre del jugador a la lista de matriz
                    arrayList.add(json.getString(Config.TAG_NOM));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void EnviarRecibirDatos(String URL){

            Toast.makeText(getApplicationContext(), ""+URL, Toast.LENGTH_SHORT).show();

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //response = response.replace("][",",");
                    //if (response.length()>0){
                        try {
                            JSONArray ja = new JSONArray(response);
                            result = ja;
                            Log.i("sizejson",""+ja.length());
                            CargarListView(ja);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    //}

                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);

        }


        public void CargarListView(JSONArray ja){

            ArrayList<String> lista = new ArrayList<>();
            for(int i=0;i<ja.length();i++){

                try {
                    lista.add(ja.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
            listaResultado.setAdapter(adaptador);

        }



}
