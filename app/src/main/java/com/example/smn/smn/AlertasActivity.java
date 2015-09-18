package com.example.smn.smn;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.example.smn.smn.json.JsonConnect;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AlertasActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);

        //Llenar la lista de ciudades favoritas con las ciudades guardadas en los menu_ajustes
        AsyncTask jsonTask = new Json().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alertas, menu);
        return true;
    }


    private class Json extends AsyncTask<Void, Void, Object> {

        private Context context = getApplicationContext();
        private String result;

        protected Void doInBackground(Void...v) {

            JsonConnect conn = new JsonConnect();
            String uri = "http://200.16.116.28:9000/smn_mobile/obtener_alertas";

            result = conn.readJSONFeed(uri, "", "GET");
            return null;
        }

        protected void onPostExecute(Object o) {
            try {

                //JSONObject object = (JSONObject) new JSONTokener(result.toString()).nextValue();
                //JSONArray alertas = object.getJSONArray("alertas");

                //Toast.makeText(context, alertas.toString(), Toast.LENGTH_SHORT).show();

                //Recuperar alertas.
                Set<String> alertas = new HashSet<String>();
                //alertas.add(items.toString());
                alertas.add("Alerta 1");
                alertas.add("Alerta 2");
                alertas.add("Alerta 3");
                alertas.add("Alerta 4");
                Iterator iAlertas = (Iterator) alertas.iterator();

                // Obtener el layout donde se va a cargar la lista
                LinearLayout linLayout = (LinearLayout) findViewById(R.id.alertas_mensajes);
                View convertView = null;

                //Recorrer las alertas para crear las filas en la vista.
                //for (int i=0; i < alertas.length(); i++) {
                while(iAlertas.hasNext()){

                    // Recuperamos el mensaje de alerta
                    String mensaje = iAlertas.next().toString();
                    //JSONObject mensaje = alertas.getJSONObject(i);


                    LayoutInflater inflater = (LayoutInflater) context.getApplicationContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    convertView = inflater.inflate(R.layout.alertas_mensaje_fila, null);

                    TextView tv = (TextView) convertView.findViewById(R.id.alertas_mensaje);
                    //tv.setText(mensaje.getString("title") + ".\n " + mensaje.getString("description"));
                    tv.setText(mensaje);

                    linLayout.addView(convertView);
                }


            } catch (Exception e) {
                Log.d("ObtenerAlertas", e.getLocalizedMessage());
            }
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, AjustesActivity.class);
                startActivity(intent);
                break;
            case R.id.action_alertas:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
