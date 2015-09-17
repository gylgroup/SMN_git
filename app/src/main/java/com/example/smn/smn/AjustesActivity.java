package com.example.smn.smn;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.smn.smn.ajustes.Ajustes;
import com.example.smn.smn.ajustes.ItemAutoTextAdapter;
import com.example.smn.smn.db.DbAdapter;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


public class AjustesActivity extends ActionBarActivity
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

    // Inicializacion de las vistas/grupos colapsables
    private LinearLayout ajustes_seccion_1;
    private LinearLayout ajustes_seccion_2;

    private TextView ajustes_titulo_1;
    private TextView ajustes_titulo_2;

    // Inicializacion del buscador con autocompletado
    private AutoCompleteTextView buscar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        ajustes = new Ajustes(this);

        initAjustesColapsable();
        cargaAjustes();
        liveSearchCiudades(this);
        cargarCiudadesFavoritas(this);


    }


    private void agregarItemAListaCiudades(final Activity activity, String id, int cantCiudades){

        LayoutInflater inflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Obtener el layout donde se va a cargar la lista
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.ajustes_ciudades);
        View convertView = null;

        // Cargar el layout de cada fila, dependiendo de si es par o impar
        if(cantCiudades % 2 == 0) {
            convertView = inflater.inflate(R.layout.ajustes_ciudad_fila_par, null);
        }else{
            convertView = inflater.inflate(R.layout.ajustes_ciudad_fila_impar, null);
        }
        //convertView.setTag();

        // Obtener el nombre de la ciudad desde la BD
        DbAdapter dbHelper = new DbAdapter(getApplicationContext(), activity);
        String nombre_ciudad = dbHelper.getNombreCiudadPorId(id);
        if( nombre_ciudad != null ) {
            // Cargar los datos en el layout de la fila
            TextView tv = (TextView) convertView.findViewById(R.id.ajustes_ciudad_nombre);
            tv.setText(nombre_ciudad);

            Button borrar = (Button) convertView.findViewById(R.id.btnRemove);
            borrar.setTag(id);
            borrar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Quitar la ciudad de los ajustes
                    ajustes.quitarCiudad(view.getTag().toString());

                    // Refrescar la lista de ciudades
                    LinearLayout lista_ciudades = (LinearLayout) findViewById(R.id.ajustes_ciudades);
                    lista_ciudades.removeAllViews();
                    cargarCiudadesFavoritas(activity);
                }
            });

            linLayout.addView(convertView);
        }
    }

    /**
     * Genera el comportamiento del livesearch de ciudades
     *
     * @param activity
     */
    private void liveSearchCiudades(Activity activity){

        final Activity parentActivity = activity;
        DbAdapter dbHelper = new DbAdapter(getApplicationContext(), activity);

        buscar = (AutoCompleteTextView) findViewById(R.id.buscar);

        ItemAutoTextAdapter adapter = new ItemAutoTextAdapter(activity, dbHelper);
        buscar.setAdapter(adapter);

        buscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                // Agregar la nueva ciudad a los ajustes
                //String nueva_ciudad = Objects.toString(arg3);
                String nueva_ciudad = ""+arg3+"";
                boolean agregada = ajustes.agregarCiudad(nueva_ciudad);

                // Si fue guardada en los ajustes, agregar el item en la lista de favoritas
                if(agregada){
                    int cantCiudades = ajustes.getCiudades().size();
                    agregarItemAListaCiudades(parentActivity, nueva_ciudad, cantCiudades);
                }
                // Limpiar el campo de busqueda
                buscar.setText("");


            }
        });

    }

    /*
    * Llenar la lista de ciudades favoritas con las ciudades guardadas en los ajustes
    *
    * @param activity
    */
    private void cargarCiudadesFavoritas(Activity activity){

        //Recuperar ciudades.
        Set<String> ciudades = ajustes.getCiudades();
        int cantCiudades = 1;

        Iterator iCiudades = (Iterator) ciudades.iterator();

        //Recorrer las ciudades para crear las filas en la vista.
        while(iCiudades.hasNext()){
            // Crear el textview para mostrar la ciudad.
            agregarItemAListaCiudades(activity, iCiudades.next().toString(), cantCiudades++);
        }

    }

    /**
     * Genera el comportamiento para colapsar/expandir los grupos de ajustes
     */
    private void initAjustesColapsable(){

        ajustes_seccion_1 = (LinearLayout) findViewById(R.id.ajustes_seccion_1);
        ajustes_titulo_1 = (TextView) findViewById(R.id.ajustes_titulo_1);
        ajustes_titulo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animacion(getApplicationContext(), ajustes_seccion_1);
                ajustes_seccion_1.setVisibility(ajustes_seccion_1.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });

        ajustes_seccion_2 = (LinearLayout) findViewById(R.id.ajustes_seccion_2);
        ajustes_titulo_2 = (TextView) findViewById(R.id.ajustes_titulo_2);
        ajustes_titulo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animacion(getApplicationContext(), ajustes_seccion_2);
                ajustes_seccion_2.setVisibility(ajustes_seccion_2.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });

    }

    /**
     * Inicializa los elementos del menu de ajustes de acuerdo a las preferencias guardadas     *
     */
    private void cargaAjustes(){

        Boolean unidades = ajustes.getUnidades();
        Boolean notificaciones = ajustes.getNotificaciones();

        ToggleButton tb_unidades = (ToggleButton)findViewById(R.id.toggleUnidades);
        if(unidades) {
            tb_unidades.setChecked(true);
        }
        // Listener para guardar el cambio del ajuste
        tb_unidades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ajustes.setUnidades(isChecked, true);
            }
        });


        ToggleButton tb_notificaciones = (ToggleButton)findViewById(R.id.toggleNotificaciones);
        if(notificaciones) {
            tb_notificaciones.setChecked(true);
        }
        // Listener para guardar el cambio del ajuste
        tb_notificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ajustes.setNotificaciones(isChecked, true);
            }
        });

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {

            case 0:
                ft.replace(R.id.container, AjustesActivityFragment.newInstance(position+1)).commit();
                break;

            case 1:
                ft.replace(R.id.container, AlertasActivityFragment.newInstance(position+1)).commit();
                break;

        }

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
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
    /*public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = inflater.inflate(R.layout.fragment_ajustes, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AjustesActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }*/


}
