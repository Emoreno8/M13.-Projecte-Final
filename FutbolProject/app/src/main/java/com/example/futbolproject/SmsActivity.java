package com.example.futbolproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SmsActivity extends AppCompatActivity {
    Button enviar;

    String dataPartit;

    private TextView missatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        missatge = findViewById(R.id.txtSms);
        //data del partit
        dataPartit = ConvocatoriaActivity.mTv.getText().toString();
        missatge.setText("Estas convocat el partit "+dataPartit+"");

        enviar = findViewById(R.id.button9);

        if(ActivityCompat.checkSelfPermission(SmsActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(SmsActivity.this,Manifest
                .permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){ActivityCompat.requestPermissions(SmsActivity.this,new String[]
                {Manifest.permission.SEND_SMS,}, 1000);

        }else{

        };
            enviar.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    EnviarMensaje("600786923", "eh rebut el missatge");
                    Intent intent = new Intent(SmsActivity.this, MenuEquip.class);
                    startActivity(intent);
                }
        });
    }

    private void EnviarMensaje (String numero, String mensaje){
        try{
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(numero, null, mensaje, null, null);
                Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
