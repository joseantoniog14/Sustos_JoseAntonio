package com.example.sustos_joseantonio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout2;
    EditText editTextNumber;
    Button buttonSustos;
    Button buttonSalir;
    ArrayList<String> listanombres;
    MediaPlayer mediaPlayer;

    public static final String SUSTOS = "textSustos";
    int total;

    boolean hiloActivo = true;
    boolean quitar=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNumber=findViewById(R.id.editTextNumber);
        buttonSustos=findViewById(R.id.buttonSustos);
        buttonSalir=findViewById(R.id.buttonSalir);
        ArrayList<MediaPlayer> sonidos = new ArrayList<>();
        layout2=findViewById(R.id.layout2);

        String [] sound = {getString(R.string.cadenas),getString(R.string.campanas),getString(R.string.cristal),getString(R.string.gato),getString(R.string.gotas),getString(R.string.grito),getString(R.string.pasos),getString(R.string.perro),getString(R.string.respiracion),getString(R.string.risa)};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sound);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                startActivity(intent);
            }
        });


        buttonSustos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux= editTextNumber.getText().toString();
                if (aux.matches("")) {
                    Toast.makeText(MainActivity.this, "Error, falta los números de sustos", Toast.LENGTH_LONG).show();
                }
                else{
                    int S= Integer.parseInt(aux);
                    if (S>=1 && S<=10){
                        buttonSustos.setEnabled(false);
                        editTextNumber.setEnabled(false);
                        total=Integer.valueOf(String.valueOf(editTextNumber.getText()));
                        for (int i=0;i<S;i++){
                            LinearLayout layout=new LinearLayout(MainActivity.this );
                            Spinner spinner1 = new Spinner(MainActivity.this);
                            spinner1.setAdapter(adapter);
                            EditText editext1 =new EditText(MainActivity.this);
                            editext1.setInputType(InputType.TYPE_CLASS_NUMBER);
                            ImageButton imagebutton1=new ImageButton(MainActivity.this);
                            ImageButton imagepause=new ImageButton(MainActivity.this);
                            cuentaatras uno=new cuentaatras();
                            uno.micontador=editext1;
                            uno.layout=layout;
                            uno.imagebutton1=imagebutton1;
                            uno.spinner=spinner1;
                            uno.imagepause1=imagepause;
                            imagebutton1.setBackground(getResources().getDrawable(R.drawable.play));
                            imagepause.setBackground(getResources().getDrawable(R.drawable.pause));
                            imagebutton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (editext1.getText().toString().compareTo("")!=0){
                                        quitar=false;
                                        hiloActivo=true;
                                        uno.execute();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this,"Debe un número mayor a 0",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                            imagepause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (quitar){
                                        layout.removeView(spinner1);
                                        layout.removeView(editext1);
                                        layout.removeView(imagebutton1);
                                        layout.removeView(imagepause);
                                    }
                                    else {
                                        if (hiloActivo){
                                            hiloActivo=false;
                                        }
                                        else {
                                            hiloActivo=true;
                                            cuentaatras uno=new cuentaatras();
                                            uno.micontador=editext1;
                                            uno.layout=layout;
                                            uno.imagebutton1=imagebutton1;
                                            uno.imagepause1=imagepause;
                                            uno.spinner=spinner1;
                                            uno.execute();
                                        }
                                    }
                                }

                            });

                            layout.addView(spinner1);
                            layout.addView(editext1);
                            layout.addView(imagebutton1);
                            layout.addView(imagepause);
                            layout2.addView(layout);

                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error,los sustos tienen que ir de 1 a 10", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    private class cuentaatras extends AsyncTask<String ,String, String> {
        EditText micontador;
        ImageButton imagebutton1;
        ImageButton imagepause1;
        LinearLayout layout;
        Spinner spinner;
        int contador;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setEnabled(false);
            micontador.setEnabled(false);
            imagebutton1.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            contador=Integer.valueOf(String.valueOf(micontador.getText()));
            while (contador!=0&&hiloActivo==true){
                publishProgress(String.valueOf(contador));
                contador--;
                if(contador==0){
                    layout.removeView(spinner);
                    layout.removeView(editTextNumber);
                    layout.removeView(imagebutton1);
                    layout.removeView(imagepause1);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            micontador.setText(String.valueOf(contador));

        }

        @Override
        protected void onPostExecute(String s) {
            total--;
            if(total==0){
                editTextNumber.setText("");
                editTextNumber.setEnabled(true);
                buttonSustos.setEnabled(true);
            }
            if (quitar){
                layout.removeView(spinner);
                layout.removeView(micontador);
                layout.removeView(imagebutton1);
            }
            MediaPlayer mediaPlayer=null;
            switch (spinner.getSelectedItemPosition()){
                case 0:
                    mediaPlayer  = MediaPlayer.create(MainActivity.this,R.raw.cadenas);
                    mediaPlayer.start();
                    break;
                case 1:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.campanas);
                    mediaPlayer.start();
                    break;
                case 2:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.cristal);
                    mediaPlayer.start();
                    break;
                case 3:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.gato);
                    mediaPlayer.start();
                    break;
                case 4:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.gotas);
                    mediaPlayer.start();
                    break;
                case 5:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.grito);
                    mediaPlayer.start();
                    break;
                case 6:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.pasos);
                    mediaPlayer.start();
                    break;
                case 7:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.perro);
                    mediaPlayer.start();
                    break;
                case 8:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.respiracion);
                    mediaPlayer.start();
                    break;
                case 9:
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.risa);
                    mediaPlayer.start();
                    break;
            }
        }
    }
}