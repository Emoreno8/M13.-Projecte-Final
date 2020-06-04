package com.example.futbolproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class CrearEquip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equip);

        final EditText txtNom = findViewById(R.id.editTxtNom);
        final EditText txtPassword = findViewById(R.id.editTxtPassword);
        final EditText txtPoble = findViewById(R.id.editTxtPoble);

        //buton insertar equip
        final Button crearEquip = findViewById(R.id.button2);

        //crida del boto
        crearEquip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //agafem els texts dels label i els guardem a variables
                String nom = txtNom.getText().toString();
                String password = txtPassword.getText().toString();
                String poble = txtPoble.getText().toString();

                //cridem la funcio amb els 3 parametres
                new DescarregaImatge(CrearEquip.this).execute(nom, password, poble );
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
            String registrar_url = "http://192.168.1.17/pfutbol/crear_equips.php";
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

                String nom = params[0];
                String password = params[1];
                String poble = params[2];

                String data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(nom, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("poble", "UTF-8") + "=" + URLEncoder.encode(poble, "UTF-8");


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
