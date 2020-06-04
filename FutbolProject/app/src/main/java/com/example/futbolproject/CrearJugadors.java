package com.example.futbolproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CrearJugadors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_jugadors);

        final EditText txtDorsal = findViewById(R.id.editDorsal);
        final EditText txtNomJugador = findViewById(R.id.editNomJugador);
        final EditText txtMail = findViewById(R.id.editMail);
        final EditText txtMovil = findViewById(R.id.editMovil);
        final String nomClub = LoginEquip.nom;

        final Button btnJugador = findViewById(R.id.button4);

        btnJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //String nomEquip;
                String dorsal = txtDorsal.getText().toString();
                String nomJugador = txtNomJugador.getText().toString();
                String mail = txtMail.getText().toString();
                String movil = txtMovil.getText().toString();

                new CrearJugadors.DescarregaImatge(CrearJugadors.this).execute(nomJugador, mail, nomClub, dorsal, movil);

                Intent intent = new Intent(CrearJugadors.this, MenuEquip.class);
                startActivity(intent);
            }
        });


    }


    public static class DescarregaImatge extends AsyncTask<String, Void, String> {

        private WeakReference<Context> context;

        public DescarregaImatge(Context context)
        {
            this.context = new WeakReference<>(context);
        }



        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String registrar_url = "http://192.168.1.17/pfutbol/crear_jugador.php";
            String resultat;

            try
            {
                URL url = new URL(registrar_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String nomJugador = params[0];
                String mail = params[1];
                String nomClub = params[2];
                String dorsal = params[3];
                String movil = params[4];

                String data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(nomJugador, "UTF-8") + "&"
                    + URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8") + "&"
                    + URLEncoder.encode("club", "UTF-8") + "=" + URLEncoder.encode(nomClub, "UTF-8") + "&"
                    + URLEncoder.encode("dorsal", "UTF-8") + "=" + URLEncoder.encode(dorsal, "UTF-8") + "&"
                        + URLEncoder.encode("movil", "UTF-8") + "=" + URLEncoder.encode(movil, "UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    //resultat += line;
                }
                resultat = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return  resultat;

            } catch (MalformedURLException e) {
                Log.d("APP", "S'ha utilitzat una URL amb format incorrecte");
                resultat = "S'ha produit un error";

            } catch (IOException e) {

                System.out.println(e);

                Log.d("APP", "Error inesperat!!! Possibles problemes de connexió de xarxa");
                resultat = "S'ha produit un error. Comprova la teva connexió";
            }
            return  resultat;
        }

        @Override
        protected void onPostExecute(String resultat) {
            Toast.makeText(context.get(), resultat, Toast.LENGTH_LONG).show();
        }
    }


}
