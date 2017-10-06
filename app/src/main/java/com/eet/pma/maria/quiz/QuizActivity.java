package com.eet.pma.maria.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int id_respostes[]={
            R.id.resposta1, R.id.resposta2, R.id.resposta3, R.id.resposta4
    };
    private int resposta_correcta;
    private int pregunta_actual;
    private int[] resp_usuari;
    private String[] totes_preg;
    private TextView texto_pregunta;
    private boolean[] n_res_correct;
    private RadioGroup rd;
    private Button button_check, button_ant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        texto_pregunta = (TextView) findViewById(R.id.texto_pregunta); //variable TextView
        rd = (RadioGroup) findViewById(R.id.respostes_btn);
        button_check = (Button) findViewById(R.id.btn_comprovar);
        button_ant = (Button) findViewById(R.id.btn_abans);

        totes_preg = getResources().getStringArray(R.array.totes_les_preguntes); //cridem a la matriu de totes les preguntes (string[])
        n_res_correct = new boolean[totes_preg.length]; //array on diu si cada pregunta es correcte o no
        resp_usuari = new int[totes_preg.length];
        for(int i = 0; i < resp_usuari.length; i++){
            resp_usuari[i] = -1; //inicializamos el array de las respuestas del usuario a -1 porque si ponemos 0, serà respuesta 0
        }
        pregunta_actual = 0;
        mostrarPregunta();

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprovarResposta();

                if(pregunta_actual < totes_preg.length - 1){
                    pregunta_actual++;
                    mostrarPregunta();
                } else{
                    int correctes = 0, incorrectes = 0;
                    for(boolean b : n_res_correct){ //recorre el array (es como un bucle normal)
                        if(b)correctes++;
                        else incorrectes++;
                    }
                    String resultat = String.format("Correctes: %d  ---  Incorrectes: %d",correctes,incorrectes) ;
                    Toast.makeText(QuizActivity.this,resultat, Toast.LENGTH_LONG).show();
                    //finish(); //cierra la app immediatamente (pero sale el texto)
                }
            }
        });

        button_ant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprovarResposta();
                if(pregunta_actual > 0){
                    pregunta_actual--;
                    mostrarPregunta();
                }
            }
        });
    }

    private void comprovarResposta() {
        int id = rd.getCheckedRadioButtonId();//pedirle al RadioGroup cual de los botones estan activados (-1 = cuando no hay ninguno apretado)
        int solucio = -1;

        for(int j = 0; j < id_respostes.length; j++){
            if(id_respostes[j] == id) solucio = j;
        }

        n_res_correct[pregunta_actual] = (solucio == resposta_correcta); //posem a 1 si la resposta apretada es correcte
        //if(solucio != -1) n_res_correct[solucio]=true; PETAAA!
        resp_usuari[pregunta_actual] = solucio;
    }

    private void mostrarPregunta() {
        String p = totes_preg[pregunta_actual];
        String[] parts = p.split(";"); //divideix i guarda les diferents parts de la pregunta (marcats per ;)

        rd.clearCheck(); //para que no quede marcada la opción de la pregunta anterior

        texto_pregunta.setText(parts[0]); //poner como texto la pregunta que ponemos en el strings.xml

        for(int i = 0; i < id_respostes.length; i++){
            RadioButton rb = (RadioButton) findViewById(id_respostes[i]);
            String resposta = parts[i+1]; //per posar quina resposta és correcta
            if(resposta.charAt(0) == '*'){  //buscar la diferència entre charArt i contains
                resposta_correcta = i;
                resposta = resposta.substring(1); //guarda el string desde el caracter 1, porque en 0 hay el asterisco
            }
            rb.setText(resposta); //surt el text de resposta que està a l'array (separat per comes però que ja ho hem separat anteriorment)
            if(resp_usuari[pregunta_actual] == i){
                rb.setChecked(true); //perque es quedi marcada la pregunta que haviem contestat previament
            }
        }

        if(pregunta_actual == 0){
            button_ant.setVisibility(View.GONE);
        } else{
            button_ant.setVisibility(View.VISIBLE);
        }

        //CANVIAR EL BOTÓ DE "COMPROVAR" PER EL BOTÓ "ACABA"
        if (pregunta_actual == totes_preg.length - 1){
            button_check.setText(R.string.ACABA);
        }else{
            button_check.setText(R.string.SEGÜENT);
        }
    }
}
