package com.example.smn.smn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smn.smn.ajustes.Ajustes;
import com.example.smn.smn.db.DbAdapter;
import com.example.smn.smn.json.JsonConnect;
import com.example.smn.smn.lib.Iconos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Ajustes ajustes;
    private int screen_width;
    private int screen_height;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getDimensionesPantalla();
        ajustes = new Ajustes(this);
        crearMainLayout();

    }

    /**
     * Obtiene las dimensiones en pixeles de la pantalla
     */
    public void getDimensionesPantalla(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
            screen_width = size.x;
            screen_height = size.y;
        } catch (NoSuchMethodError e) {
            screen_width = display.getWidth();
            screen_height = display.getHeight();
        }
    }


    public void crearMainLayout(){

        String uri = "http://200.16.116.28:9000/smn_mobile/obtener_pronostico";
        String json = "{\"co\":\"-65.95642852783203,-39.18587875366211\"";

        //crearLayoutCiudad(null);
        //Recuperar ciudades.
        Set<String> ciudades = ajustes.getCiudades();
        Iterator iCiudades = (Iterator) ciudades.iterator();

        if(iCiudades.hasNext()) {
            json += ",\"loc_info\":[";
            //Recorrer las ciudades para crear las vistas.
            while (iCiudades.hasNext()) {
                //crearLayoutCiudad(iCiudades.next().toString());
                json += "{\"lid\":" + iCiudades.next().toString() + "},";
            }
            json = json.substring(0, json.length()-1);
            json += "]";
        }
        json += "}";

        Toast.makeText(getApplicationContext(), json, Toast.LENGTH_LONG).show();
        AsyncTask jsonTask = new Json().execute(uri, json, "POST");

    }









    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.app_name);
                break;
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
                Intent intent2 = new Intent(this, AlertasActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }





    private class Json extends AsyncTask<String, Void, Object> {

        private Context context = getApplicationContext();
        private String result;

        protected Void doInBackground(String...params) {

            JsonConnect conn = new JsonConnect();
            result = conn.readJSONFeed(params[0], params[1], params[2]);

            return null;
        }

        protected void crearLayoutCiudad(LayoutInflater inflater, LinearLayout linLayout, JSONObject ciudad_ll){

            try {

                Iconos iconos = new Iconos();
                // Array de pronostico de esta y las proximas 11 horas
                JSONArray ph = ciudad_ll.getJSONArray("ph");
                JSONObject datos_ph = null;
                if(ph.length() > 0) {
                    datos_ph = ph.getJSONObject(0);
                }
                // Array de pronostico de este y los siguientes 3 dias
                JSONArray pd = ciudad_ll.getJSONArray("pd");

                View viewCiudadPronostico = inflater.inflate(R.layout.main_ciudad_pronostico, null);

                // Ajustar el ancho del scroll vertical de la ciudad, al tamanio de pantalla
                ScrollView scroll_ciudad = (ScrollView) viewCiudadPronostico.findViewById(R.id.scroll_ciudad);
                ViewGroup.LayoutParams params = scroll_ciudad.getLayoutParams();
                params.width = screen_width;
                scroll_ciudad.setLayoutParams(params);


                // Obtener el nombre de la ciudad desde la BD
                DbAdapter dbHelper = new DbAdapter(context.getApplicationContext(), activity);
                String nombre_ciudad = dbHelper.getNombreCiudadPorId(ciudad_ll.getString("lid"));
                if (nombre_ciudad != null) {
                    TextView tvNombreCiudad = (TextView) viewCiudadPronostico.findViewById(R.id.ciudad_nombre);
                    tvNombreCiudad.setText(nombre_ciudad);
                }

                // icono dia actual
                JSONObject ciudad_pd = pd.getJSONObject(0);
                JSONObject pronosticoManiana = ciudad_pd.getJSONObject("m");
                JSONObject pronosticoTarde = ciudad_pd.getJSONObject("a");

                ImageView ivEstadoActual = (ImageView) viewCiudadPronostico.findViewById(R.id.estado_tiempo);
                ivEstadoActual.setBackgroundResource(iconos.getDrawableById(pronosticoManiana.getString("i")));

                // fondo cambia segun temperatura
                LinearLayout llFondo = (LinearLayout) viewCiudadPronostico.findViewById(R.id.pronostico_ciudad);
                Double temperatura = Double.parseDouble(ciudad_pd.getString("ta"));
                if(temperatura <= 15) {
                    llFondo.setBackgroundResource(R.drawable.fondo_gris);
                }else if(temperatura <= 30) {
                    llFondo.setBackgroundResource(R.drawable.fondo_azul);
                }else{
                    llFondo.setBackgroundResource(R.drawable.fondo_naranja);
                }

                // temperatura actual
                TextView tvTemperaturaActual = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_temperatura);
                tvTemperaturaActual.setText(ciudad_pd.getString("ta") + getString(R.string.main_grados));

                // sensacion termica actual
                String txtSensacionActual = "--";
                if(ph.length() > 0) {
                    txtSensacionActual = datos_ph.getString("s");
                }
                TextView tvSensacionActual = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_sensacion_termica);
                tvSensacionActual.setText(getString(R.string.main_st) + " " + txtSensacionActual + getString(R.string.main_grados));

                // temperaturas maximas y minimas actual
                TextView tvMaxMin = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_max_min);
                tvMaxMin.setText(ciudad_pd.getString("ti") + getString(R.string.main_grados) +
                        getString(R.string.main_pipe) + ciudad_pd.getString("ta") + getString(R.string.main_grados));

                // humedad actual
                String txtHumedadActual = "--";
                if(ph.length() > 0) {
                    txtHumedadActual = datos_ph.getString("m");
                }
                TextView tvHumedad = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_humedad);
                tvHumedad.setText(txtHumedadActual + getString(R.string.main_porcentaje));

                // velocidad viento actual
                String txtVelocidadVientoActual = "--";
                if(ph.length() > 0) {
                    txtVelocidadVientoActual = datos_ph.getString("iv");
                }
                TextView tvVelocidadViento = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_velocidad_viento);
                tvVelocidadViento.setText(txtVelocidadVientoActual + " " + getString(R.string.main_km_h));

                // direccion viento actual
                String txtDireccionVientoActual = "--";
                if(ph.length() > 0) {
                    txtDireccionVientoActual = datos_ph.getString("sv");
                }
                TextView tvDireccionViento = (TextView) viewCiudadPronostico.findViewById(R.id.estado_tiempo_direccion_viento);
                tvDireccionViento.setText(txtDireccionVientoActual);

                // agregar la vista de ciudad al layout
                linLayout.addView(viewCiudadPronostico);

                // pronostico 4 d�as
                LinearLayout scrollerLayout = (LinearLayout) findViewById(R.id.layout_scroller);
                for (int i=0; i < pd.length(); i++) {

                    JSONObject dia_pd = pd.getJSONObject(i);

                    View viewCiudadPronosticoDiario = inflater.inflate(R.layout.main_ciudad_pronostico_diario, null);
                    LinearLayout llDiaActual = (LinearLayout) viewCiudadPronosticoDiario.findViewById(R.id.pronostico_dia_actual);
                    ViewGroup.LayoutParams paramDiaActual = llDiaActual.getLayoutParams();
                    paramDiaActual.width = screen_width;
                    llDiaActual.setLayoutParams(paramDiaActual);

                    // dia pronosticado
                    JSONObject fechaPronosticada = dia_pd.getJSONObject("fechaPronosticada");
                    SimpleDateFormat format = new SimpleDateFormat("EEEE d/MM");
                    TextView tvDiaActualNombre = (TextView) viewCiudadPronosticoDiario.findViewById(R.id.pronostico_dia_actual_fecha);
                    Date fecha = new Date(Integer.parseInt(fechaPronosticada.getString("year")),
                            Integer.parseInt(fechaPronosticada.getString("monthValue"))-1,
                            Integer.parseInt(fechaPronosticada.getString("dayOfMonth")));

                    tvDiaActualNombre.setText(format.format(fecha));

                    scrollerLayout.addView(viewCiudadPronosticoDiario);

                }


                HorizontalScrollView parentScroll= (HorizontalScrollView) findViewById(R.id.scroller_horizontal);
                HorizontalScrollView childScroll= (HorizontalScrollView) findViewById(R.id.scroll_dias_pronostico);
                parentScroll.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        findViewById(R.id.scroll_dias_pronostico).getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });
                childScroll.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });



            }catch (JSONException e){

            }

        }

        protected void onPostExecute(Object o) {
            try {

                //Iconos iconos = new Iconos();
                LayoutInflater inflater = (LayoutInflater) getBaseContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Obtener el layout donde se va a cargar la ciudad
                LinearLayout linLayout = (LinearLayout) findViewById(R.id.linear_ciudades_favoritas);

                JSONObject object = (JSONObject) new JSONTokener(result.toString()).nextValue();

                JSONObject pua = object.getJSONObject("pua");
                //Toast.makeText(getApplicationContext(), pua.getString("pd"), Toast.LENGTH_LONG).show();
                this.crearLayoutCiudad(inflater, linLayout, pua);

                JSONArray ll = object.getJSONArray("ll");
                for (int i=0; i < ll.length(); i++) {

                    JSONObject ciudad_ll = ll.getJSONObject(i);
                    this.crearLayoutCiudad(inflater, linLayout, ciudad_ll);
                }


            } catch (Exception e) {
                Log.d("Error Obtener Ciudad", e.getLocalizedMessage());
            }
        }

    }

}
