package com.example.smn.smn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PronosticoDiasActivity extends ActionBarActivity {

    private GraphView mGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronostico_dias);

        mGraph = (GraphView) findViewById(R.id.example_graph);

        // Our datasets
        float[][] data1 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, {10.0f, 8.0f, 12.0f, 12.0f, 6.0f, 5.0f}};
        float[][] data2 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f}, {8.0f, 4.0f, 11.0f, 10.0f, 5.0f, 2.0f}};
        float[][] data3 = {{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f}, {3.0f, 1.0f, 3.0f, Float.NaN, 2.0f, 7.0f}};

        // The first dataset must be inputted into the graph using setData to replace the placeholder data already there
        mGraph.setData(new float[][][]{data1}, 0, 5, 0, 15);

        // We want to add the second data set, but only adjust the max x value as all the other stay the same, so we input NaNs in their place
        mGraph.addData(data2, Float.NaN, 7, Float.NaN, Float.NaN);

        // Add the third dataset, which includes NaNs to signify a gap in the data
        mGraph.addData(data3, Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        mGraph.setOverlay1Text("Ejemplo de un grafo", 0.1f, 0.1f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pronostico_dias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AjustesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
