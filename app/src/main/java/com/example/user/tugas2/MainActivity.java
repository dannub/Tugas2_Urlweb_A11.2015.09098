package com.example.user.tugas2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    TextView HTML;
    Spinner spin;
    EditText text_url;
    ArrayAdapter<CharSequence> list_spiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin = (Spinner) findViewById(R.id.spiner);
        text_url = (EditText) findViewById(R.id.url);
        HTML = (TextView) findViewById(R.id.text);

        list_spiner = ArrayAdapter.createFromResource(this,R.array.protokol,android.R.layout.simple_spinner_item);
        list_spiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(list_spiner);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e("Error"+ Thread.currentThread().getStackTrace()[2],e.getLocalizedMessage());
            }
        });

        if(getSupportLoaderManager().getLoader(0)!=null)
        {
            getSupportLoaderManager().initLoader(0,null,this);
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new HTMLResource(this,args.getString("url_link"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        HTML.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void Get(View view) {
        String link_url, protokol, url;
        protokol = spin.getSelectedItem().toString();
        url = text_url.getText().toString();

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        if(!url.isEmpty()){
            if(url.contains(".")&&!(url.contains(" "))){
                if(checkConnection())
                {
                    HTML.setText("Loading...");

                    link_url = protokol + url;
                    Bundle bundle = new Bundle();
                    bundle.putString("url_link",link_url);
                    getSupportLoaderManager().restartLoader(0,bundle,this);

                }else
                {
                    Toast.makeText(this, "check yor internet connection",Toast.LENGTH_SHORT).show();
                    HTML.setText("No Internet Connection");
                }

            }
            else{
                HTML.setText("invalid URL");
            }

        }
        else
        {
            HTML.setText("URL cant empty");
        }
    }

    private boolean checkConnection() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netfo = conman.getActiveNetworkInfo();

        return netfo != null && netfo.isConnected();
    }

}
