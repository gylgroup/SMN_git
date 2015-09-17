package com.example.smn.smn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;


public class Screen1Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
        HorizontalScrollView parentScroll= (HorizontalScrollView) findViewById(R.id.scroller_horizontal);
        HorizontalScrollView childScroll= (HorizontalScrollView) findViewById(R.id.scroller1);
        HorizontalScrollView anotherChildScroll= (HorizontalScrollView) findViewById(R.id.scroller2);
        int textvId = R.id.ciudad1_dia_1_estado_tiempo_mañana;
        parentScroll.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.scroller1).getParent().requestDisallowInterceptTouchEvent(false);
                findViewById(R.id.scroller2).getParent().requestDisallowInterceptTouchEvent(false);
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
        anotherChildScroll.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AjustesActivity.class);
            startActivity(intent);
            return true;
        }*/

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, AjustesActivity.class);
                startActivity(intent);
                break;
            case R.id.action_alertas:
                Intent intent2 = new Intent(this, AlertasActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_pronostico_dias:
                Intent intent3 = new Intent(this, PronosticoDiasActivity.class);
                startActivity(intent3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}