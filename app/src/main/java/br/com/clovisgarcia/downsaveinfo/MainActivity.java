package br.com.clovisgarcia.downsaveinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mensagemInserida;

    private String mensagem;

    private Button downloadButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensagemInserida = findViewById(R.id.editTextXML);

        downloadButton = findViewById(R.id.downloadXML);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalisadorXML analisadorXML = new AnalisadorXML(mensagem);
                analisadorXML.process();
                mensagem = analisadorXML.getConteudo();

                mensagemInserida.setText(mensagem);

            }
        });

        MinhaAsync minhaAsync = new MinhaAsync();
        minhaAsync.execute("https://www.w3schools.com/xml/note.xml");

    }

    public void disparoNovaTela(View v){

        mensagem = mensagemInserida.getText().toString();

        Intent myIntent = new Intent(  this, Tela2.class);

        myIntent.putExtra("mensagemEnviada", mensagem);

        startActivity(myIntent);
    }

    private String downloadFile(String theUrl) {
        try {
            URL myUrl = new URL(theUrl);

            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            int response = connection.getResponseCode();
            Log.d("Download", "The response code is "+response);

            InputStream data = connection.getInputStream();

            InputStreamReader characters = new InputStreamReader(data);

            char[] inputBuffer = new char[500];

            StringBuilder tempBuffer = new StringBuilder();

            int charRead;

            while (true){
                charRead = characters.read(inputBuffer);
                if (charRead <= 0) {
                    break;
                }
                tempBuffer.append(String.copyValueOf(inputBuffer, 0,charRead));
            }
            return tempBuffer.toString();
        }
        catch (IOException e) {
            Log.d("Download", "IO Excepction durate a conexão!" + e.getMessage());
        }
        return null;

    }

    private class MinhaAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            mensagem = downloadFile(params[0]);

            if (mensagem == null) {
                Log.d("Download", "Erro download");

            }
            return mensagem;
        }
    }
}