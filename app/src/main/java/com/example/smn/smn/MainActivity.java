package com.example.smn.smn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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
import java.util.Set;


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
    private Iconos iconos;
    private Integer posicion;
    private GraphView mGraph;

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
        GPSTracker gps = new GPSTracker(this);
        Location location = gps.getLocation();
        if(location != null) {
            crearMainLayout(location);
        }

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


    public void crearMainLayout(Location location){


        String uri = "http://200.16.116.28:9000/smn_mobile/obtener_pronostico";
        //String json = "{\"co\":\"-65.95642852783203,-39.18587875366211\"";
        String json = "{\"co\":\""+location.getLongitude()+","+location.getLatitude()+"\"";

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

        //Toast.makeText(getApplicationContext(), json, Toast.LENGTH_LONG).show();
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
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

        protected void crearLayoutCiudad(LayoutInflater inflater, LinearLayout linLayout, JSONObject ciudad_ll, Integer posicion_vista){

            try {

                iconos = new Iconos();
                posicion = posicion_vista;

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

                // pronostico 4 d�as
                LinearLayout scrollerLayout = (LinearLayout) viewCiudadPronostico.findViewById(R.id.layout_scroller);
                for (int i=0; i < pd.length(); i++) {

                    JSONObject dia_pd = pd.getJSONObject(i);

                    // layout dia pronosticado
                    LinearLayout linearDias = new LinearLayout(this.context);
                    linearDias.setOrientation(LinearLayout.VERTICAL);
                    linearDias.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearDias.setPadding(0,20, 0, 10);
                    linearDias.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    ViewGroup.LayoutParams paramDias = linearDias.getLayoutParams();
                    paramDias.width = screen_width;
                    linearDias.setLayoutParams(paramDias);

                    // nombre del dia pronosticado
                    TextView tvDiaActualNombre = new TextView(this.context);
                    tvDiaActualNombre.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvDiaActualNombre.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvDiaActualNombre.setTextAppearance(this.context, R.style.ajustesNombreDiaPronosticado);

                    JSONObject fechaPronosticada = dia_pd.getJSONObject("fechaPronosticada");
                    SimpleDateFormat format = new SimpleDateFormat("EEEE d/MM");
                    Date fecha = new Date(Integer.parseInt(fechaPronosticada.getString("year")),
                            Integer.parseInt(fechaPronosticada.getString("monthValue"))-1,
                            Integer.parseInt(fechaPronosticada.getString("dayOfMonth")));
                    tvDiaActualNombre.setText(format.format(fecha));
                    linearDias.addView(tvDiaActualNombre);

                    // layout pronostico maniana tarde
                    LinearLayout linearDiaManianaTarde = new LinearLayout(this.context);
                    linearDiaManianaTarde.setOrientation(LinearLayout.HORIZONTAL);
                    linearDiaManianaTarde.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));


                    // LAYOUT PRONOSOTICO MANANA
                    LinearLayout linearDiaManiana = new LinearLayout(this.context);
                    linearDiaManiana.setOrientation(LinearLayout.VERTICAL);
                    linearDiaManiana.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearDiaManiana.setPadding(0, 20, 0, 0);
                    linearDiaManiana.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.50f));

                    // texto Mañana
                    TextView tvTextoManiana = new TextView(this.context);
                    tvTextoManiana.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvTextoManiana.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvTextoManiana.setText(R.string.maniana);
                    tvTextoManiana.setTextAppearance(this.context, R.style.ajustesTextoManianaTarde);
                    linearDiaManiana.addView(tvTextoManiana);

                    // icono pronostico maniana
                    JSONObject objetoManiana = dia_pd.getJSONObject("m");
                    ImageView ivEstadoManiana = new ImageView(this.context);
                    ivEstadoManiana.setPadding(0,20,0,0);
                    ivEstadoManiana.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivEstadoManiana.setBackgroundResource(iconos.getDrawableById(objetoManiana.getString("i")));
                    ViewGroup.LayoutParams ivParamsManiana = ivEstadoManiana.getLayoutParams();
                    ivParamsManiana.width = 180;
                    ivParamsManiana.height = 180;
                    linearDiaManiana.addView(ivEstadoManiana);

                    // layout pronostico temperatura min/max
                    LinearLayout linearDiaManianaMaxMin = new LinearLayout(this.context);
                    linearDiaManianaMaxMin.setOrientation(LinearLayout.HORIZONTAL);
                    linearDiaManianaMaxMin.setPadding(10, 0, 10, 10);
                    linearDiaManianaMaxMin.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

                    // icono min
                    ImageView ivManianaMin = new ImageView(this.context);
                    ivManianaMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivManianaMin.setBackgroundResource(R.drawable.min);
                    ivManianaMin.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsManianaMin = ivManianaMin.getLayoutParams();
                    ivParamsManianaMin.width = 10;
                    ivParamsManianaMin.height = 10;
                    linearDiaManianaMaxMin.addView(ivManianaMin);

                    // texto rango temperatura
                    TextView tvTextoManianaMaxMin = new TextView(this.context);
                    tvTextoManianaMaxMin.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvTextoManianaMaxMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    String objetoTemperaturaMinMax = dia_pd.getString("ti")+getString(R.string.main_grados)+
                            getString(R.string.main_pipe)+dia_pd.getString("ta")+getString(R.string.main_grados);
                    tvTextoManianaMaxMin.setText(objetoTemperaturaMinMax);
                    tvTextoManianaMaxMin.setTextAppearance(this.context, R.style.ajustesTextoMaxMin);
                    linearDiaManianaMaxMin.addView(tvTextoManianaMaxMin);

                    // icono min
                    ImageView ivManianaMax = new ImageView(this.context);
                    ivManianaMax.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivManianaMax.setBackgroundResource(R.drawable.max);
                    ivManianaMax.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsManianaMax = ivManianaMax.getLayoutParams();
                    ivParamsManianaMax.width = 10;
                    ivParamsManianaMax.height = 10;
                    linearDiaManianaMaxMin.addView(ivManianaMax);

                    linearDiaManiana.addView(linearDiaManianaMaxMin);
                    linearDiaManianaTarde.addView(linearDiaManiana);




                    // LAYOUT PRONOSTICO TARDE-NOCHE
                    LinearLayout linearDiaTardeNoche = new LinearLayout(this.context);
                    linearDiaTardeNoche.setOrientation(LinearLayout.VERTICAL);
                    linearDiaTardeNoche.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearDiaTardeNoche.setPadding(0, 20, 0, 0);
                    linearDiaTardeNoche.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.50f));

                    // texto Tarde/Noche
                    TextView tvTextoTarde = new TextView(this.context);
                    tvTextoTarde.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvTextoTarde.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvTextoTarde.setText(R.string.tarde_noche);
                    tvTextoTarde.setTextAppearance(this.context, R.style.ajustesTextoManianaTarde);
                    linearDiaTardeNoche.addView(tvTextoTarde);

                    // icono pronostico tarde
                    JSONObject objetoTarde = dia_pd.getJSONObject("a");
                    ImageView ivEstadoTarde = new ImageView(this.context);
                    ivEstadoTarde.setPadding(0,20,0,0);
                    ivEstadoTarde.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivEstadoTarde.setBackgroundResource(iconos.getDrawableById(objetoTarde.getString("i")));
                    ViewGroup.LayoutParams ivParamsTarde = ivEstadoTarde.getLayoutParams();
                    ivParamsTarde.width = 180;
                    ivParamsTarde.height = 180;
                    linearDiaTardeNoche.addView(ivEstadoTarde);

                    // layout pronostico temperatura min/max
                    LinearLayout linearDiaTardeMaxMin = new LinearLayout(this.context);
                    linearDiaTardeMaxMin.setOrientation(LinearLayout.HORIZONTAL);
                    linearDiaTardeMaxMin.setPadding(10, 0, 10, 10);
                    linearDiaTardeMaxMin.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

                    // icono min
                    ImageView ivTardeMin = new ImageView(this.context);
                    ivTardeMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivTardeMin.setBackgroundResource(R.drawable.min);
                    ivTardeMin.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsTardeMin = ivTardeMin.getLayoutParams();
                    ivParamsTardeMin.width = 10;
                    ivParamsTardeMin.height = 10;
                    linearDiaTardeMaxMin.addView(ivTardeMin);

                    // texto rango temperatura
                    TextView tvTextoTardeMaxMin = new TextView(this.context);
                    tvTextoTardeMaxMin.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvTextoTardeMaxMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    String objetoTemperaturaMinMaxTarde = dia_pd.getString("ti")+getString(R.string.main_grados)+
                            getString(R.string.main_pipe)+dia_pd.getString("ta")+getString(R.string.main_grados);
                    tvTextoTardeMaxMin.setText(objetoTemperaturaMinMaxTarde);
                    tvTextoTardeMaxMin.setTextAppearance(this.context, R.style.ajustesTextoMaxMin);
                    linearDiaTardeMaxMin.addView(tvTextoTardeMaxMin);

                    // icono max
                    ImageView ivTardeMax = new ImageView(this.context);
                    ivTardeMax.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivTardeMax.setBackgroundResource(R.drawable.max);
                    ivTardeMax.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsTardeMax = ivTardeMax.getLayoutParams();
                    ivParamsTardeMax.width = 10;
                    ivParamsTardeMax.height = 10;
                    linearDiaTardeMaxMin.addView(ivTardeMax);

                    linearDiaTardeNoche.addView(linearDiaTardeMaxMin);
                    linearDiaManianaTarde.addView(linearDiaTardeNoche);

                    linearDias.addView(linearDiaManianaTarde);


                    scrollerLayout.addView(linearDias);

                }


                // PRONOSTICO EXTENDIDO
                LinearLayout layoutPronosticoExtendido = (LinearLayout) viewCiudadPronostico.findViewById(R.id.pronostico_extendido);

                // division
                View division = new View(context);
                division.setBackgroundColor(Color.parseColor("#ffafafaf"));
                division.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2));
                layoutPronosticoExtendido.addView(division);

                for (int i=0; i < pd.length(); i++) {

                    JSONObject dia_pd = pd.getJSONObject(i);

                    LinearLayout linearExtendido = new LinearLayout(this.context);
                    linearExtendido.setOrientation(LinearLayout.HORIZONTAL);
                    linearExtendido.setPadding(10,10,10,10);
                    if(i % 2 == 0) {
                        linearExtendido.setBackgroundColor(Color.parseColor("#ff606060"));
                    }else{
                        linearExtendido.setBackgroundColor(Color.parseColor("#ff2b2b2b"));
                    }
                    linearExtendido.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                    // nombre del dia pronosticado
                    LinearLayout viewUnTercio = new LinearLayout(context);
                    viewUnTercio.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

                    TextView tvDiaNombre = new TextView(this.context);
                    tvDiaNombre.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvDiaNombre.setTextAppearance(this.context, R.style.ajustesTextoPronosticoExtendido);

                    JSONObject fechaPronosticada = dia_pd.getJSONObject("fechaPronosticada");
                    SimpleDateFormat format = new SimpleDateFormat("EEEE d");
                    Date fecha = new Date(Integer.parseInt(fechaPronosticada.getString("year")),
                            Integer.parseInt(fechaPronosticada.getString("monthValue"))-1,
                            Integer.parseInt(fechaPronosticada.getString("dayOfMonth")));
                    tvDiaNombre.setText(format.format(fecha));
                    viewUnTercio.addView(tvDiaNombre);
                    linearExtendido.addView(viewUnTercio);


                    // icono pronostico maniana
                    LinearLayout viewDosTercios = new LinearLayout(context);
                    viewDosTercios.setGravity(Gravity.RIGHT);
                    viewDosTercios.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));

                    JSONObject objetoManiana = dia_pd.getJSONObject("m");
                    ImageView ivEstadoManiana = new ImageView(this.context);
                    ivEstadoManiana.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.RIGHT));
                    ivEstadoManiana.setBackgroundResource(iconos.getDrawableById(objetoManiana.getString("i")));
                    ViewGroup.LayoutParams ivParamsManiana = ivEstadoManiana.getLayoutParams();
                    ivParamsManiana.width = 50;
                    ivParamsManiana.height = 50;
                    viewDosTercios.addView(ivEstadoManiana);
                    linearExtendido.addView(viewDosTercios);

                    // linear temperaturas min/max
                    LinearLayout linearExtendidoMinMax = new LinearLayout(this.context);
                    linearExtendidoMinMax.setOrientation(LinearLayout.HORIZONTAL);
                    linearExtendidoMinMax.setGravity(Gravity.RIGHT);
                    linearExtendidoMinMax.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));

                    // icono min
                    ImageView ivMin = new ImageView(this.context);
                    ivMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.LEFT));
                    ivMin.setBackgroundResource(R.drawable.min);
                    ivMin.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsMin = ivMin.getLayoutParams();
                    ivParamsMin.width = 10;
                    ivParamsMin.height = 10;
                    linearExtendidoMinMax.addView(ivMin);

                    // texto temperatura min
                    TextView tvTextoMin = new TextView(this.context);
                    tvTextoMin.setGravity(Gravity.CENTER_HORIZONTAL);
                    tvTextoMin.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvTextoMin.setText(dia_pd.getString("ti")+getString(R.string.main_grados));
                    tvTextoMin.setTextAppearance(this.context, R.style.ajustesTextoPronosticoExtendido);
                    linearExtendidoMinMax.addView(tvTextoMin);


                    // icono max
                    ImageView ivMax = new ImageView(this.context);
                    ivMax.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER_HORIZONTAL));
                    ivMax.setBackgroundResource(R.drawable.max);
                    ivMax.setPadding(3, 10, 3, 10);
                    ViewGroup.LayoutParams ivParamsMax = ivMax.getLayoutParams();
                    ivParamsMax.width = 10;
                    ivParamsMax.height = 10;
                    linearExtendidoMinMax.addView(ivMax);

                    // texto temperatura max
                    TextView tvTextoMax = new TextView(this.context);
                    tvTextoMax.setGravity(Gravity.RIGHT);
                    tvTextoMax.setLayoutParams(new android.app.ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    tvTextoMax.setText(dia_pd.getString("ta")+getString(R.string.main_grados));
                    tvTextoMax.setTextAppearance(this.context, R.style.ajustesTextoPronosticoExtendido);
                    linearExtendidoMinMax.addView(tvTextoMax);


                    linearExtendido.addView(linearExtendidoMinMax);
                    layoutPronosticoExtendido.addView(linearExtendido);

                    // division
                    View division2 = new View(context);
                    division2.setBackgroundColor(Color.parseColor("#ffafafaf"));
                    division2.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2));
                    layoutPronosticoExtendido.addView(division2);

                }


                // GRAFICO TEMPERATURAS MAX/MIN
                mGraph = (GraphView) viewCiudadPronostico.findViewById(R.id.grafico_temperaturas);


                for (int i=0; i < pd.length(); i++) {
                    JSONObject dia_pd = pd.getJSONObject(i);
                    //Temperaturas minimas
                    //dia_pd.getString("ti")

                    //Temperaturas maximas
                    //dia_pd.getString("ta")

                    //Nombre del dia
                    /*JSONObject fechaPronosticada = dia_pd.getJSONObject("fechaPronosticada");
                    SimpleDateFormat format = new SimpleDateFormat("EEEE d");
                    Date fecha = new Date(Integer.parseInt(fechaPronosticada.getString("year")),
                            Integer.parseInt(fechaPronosticada.getString("monthValue"))-1,
                            Integer.parseInt(fechaPronosticada.getString("dayOfMonth")));
                    format.format(fecha);
                    */

                }

                // Our datasets
                float[][] data1 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, {0.0f, 10.0f, 8.0f, 12.0f, 12.0f, 0.0f}};
                float[][] data2 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, {0.0f, 8.0f, 4.0f, 11.0f, 10.0f, 0.0f}};

                // The first dataset must be inputted into the graph using setData to replace the placeholder data already there
                mGraph.setData(new float[][][]{data1}, 0, 5, 0, 15);
                // We want to add the second data set, but only adjust the max x value as all the other stay the same, so we input NaNs in their place
                mGraph.addData(data2, Float.NaN, 5, Float.NaN, Float.NaN);

                mGraph.setXLabels(new String[]{"L","M","M","J","V"});






                HorizontalScrollView parentScroll= (HorizontalScrollView) findViewById(R.id.scroller_horizontal);
                HorizontalScrollView childScroll= (HorizontalScrollView) viewCiudadPronostico.findViewById(R.id.scroll_dias_pronostico);
                childScroll.setId(iconos.getNuevoScrollerIdPorPosicion(posicion));
                parentScroll.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        findViewById(iconos.getNuevoScrollerIdPorPosicion(posicion)).getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });
                childScroll.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        /*float x1 = 0, x2 = 0, y1 = 0, y2 = 0, dx = 0, dy = 0;
                        switch(event.getAction()) {
                            case (MotionEvent.ACTION_DOWN):
                                x1 = event.getX();
                                y1 = event.getY();
                                break;
                            case (MotionEvent.ACTION_MOVE):
                                x2 = event.getX();
                                y2 = event.getY();
                                dx = x2-x1;
                                dy = y2-y1;

                                if(Math.abs(dx) > Math.abs(dy)) {
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                }
                                break;
                        }*/

                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });

                // agregar la vista de ciudad al layout
                linLayout.addView(viewCiudadPronostico);




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
                this.crearLayoutCiudad(inflater, linLayout, pua, 0);

                JSONArray ll = object.getJSONArray("ll");
                for (int i=1; i <= ll.length(); i++) {

                    JSONObject ciudad_ll = ll.getJSONObject(i-1);
                    this.crearLayoutCiudad(inflater, linLayout, ciudad_ll, i);
                }


            } catch (Exception e) {
                Log.d("Error Obtener Ciudad", e.getLocalizedMessage());
            }
        }

    }

}
