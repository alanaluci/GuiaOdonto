package maddo.foo.telasplash;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {
    private static String TAG = "LOG";
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listview = (ListView) findViewById(R.id.ListView);
        txtEstados = (TextView) findViewById(R.id.txtCategory);

        estadosList = new ArrayList<Estados>();

        // spinner item select listener
        listview.setOnItemSelectedListener(this);

        new GetEstados().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Guia Odonto");
        mToolbar.setSubtitle("just a subtitle");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);

        mToolbarBottom = (Toolbar) findViewById(R.id.inc_tb_bottom);
        mToolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;
                return true;
            }
        });
        mToolbarBottom.inflateMenu(R.menu.menu_bottom);

        mToolbarBottom.findViewById(R.id.iv_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Settings pressed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
**
**
* DAQUI PRA CIMA NÃO MEXER - DAQUI PRA BAIXO É TENTATIVA DE COLOCAR O BANCO DE DADOS NA LISTVIEW
**
**
 */
    private TextView txtCategory;
    private Spinner spinnerFood;
    // array list for spinner adapter
    private ArrayList<Estados> estadosList;
    ProgressDialog pDialog;
    // API urls
    // Url to create new category
    //private String URL_NEW_CATEGORY = "http://10.0.2.2/food_api/new_category.php";
    // Url to get all categories
    private String URL_ESTADOS = "http://servdonto.no-ip.org/prestadores/android/guia/index1.php";


    /**
     * Adding spinner data
     * */
    private void populateTextView() {
        List<String> lables = new ArrayList<String>();

        txtEstados.setText("");

        for (int i = 0; i < estadosList.size(); i++) {
            lables.add(estadosList.get(i).getName());
        }

        // Creating adapter for listview
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_ListView_item, lables);



        // attaching data adapter to spinner
        listView.setAdapter(listViewAdapter);
    }
    /**
     * Async task to get all food categories
     * */
    private class GetEstados extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Carregando os Estados..");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_ESTADOS, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray estados = jsonObj
                                .getJSONArray("uf");

                        for (int i = 0; i < estados.length(); i++) {
                            JSONObject catObj = (JSONObject) estados.get(i);
                            Estados uf = new Estados(catObj.getInt("id_uf"),
                                    catObj.getString("uf"));
                            estadosList.add(uf);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateTextView();
        }

    }


    }


