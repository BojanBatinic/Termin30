package com.example.android.termin30.activities;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import com.example.android.termin30.R;
import com.example.android.termin30.db.ORMLiteHelper;
import com.example.android.termin30.model.Glumac;
import com.example.android.termin30.preferences.Preferences;
import com.example.android.termin30.dialog.AboutDialog;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by BBLOJB on 25.11.2017..
 */

public class ListActivity extends AppCompatActivity {

    private ORMLiteHelper databaseHelper;
    private SharedPreferences preferenses;

    public static String GLUMAC_KEY = "GLUMAC_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        preferenses = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.glumci_list);

        try{
            List<Glumac> list = getDatabaseHelper().getGlumacDao().queryForAll();
            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Glumac r = (Glumac) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    intent.putExtra(GLUMAC_KEY, r.getId());
                    startActivity(intent);

                }
            });
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }
 private void refresh(){
        ListView listview = (ListView) findViewById(R.id.glumci_list);

        if(listview != null) {
            ArrayAdapter<Glumac> adapter = (ArrayAdapter<Glumac>) listview.getAdapter();

            if(adapter != null){
                try {
                    adapter.clear();
                    List<Glumac> list = getDatabaseHelper().getGlumacDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }

        }
 }

 private void showStatusMessage(String message){
     NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
     NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
     mBuilder.setSmallIcon(R.drawable.ic_launcher);
     mBuilder.setContentTitle("Termin 30");
     mBuilder.setContentText(message);

     Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

     mBuilder.setLargeIcon(bm);
     // ID notifikacije omogucava da se da se dopuni podacima i kasnije
     mNotificationManager.notify(1, mBuilder.build());
 }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     switch (item.getItemId()){
         case R.id.add_new_glumac:
             //dialog za unos podataka o glumcu
             final Dialog dialog = new Dialog(this);
             dialog.setContentView(R.layout.add_glumac_layout);

             Button add = (Button) dialog.findViewById(R.id.add_glumac);
             add.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     EditText ime = (EditText) dialog.findViewById(R.id.glumac_ime);
                     EditText bio = (EditText) dialog.findViewById(R.id.glumac_biografija);
                     RatingBar rating = (RatingBar) dialog.findViewById(R.id.glumac_rating);
                     EditText rodjen = (EditText) dialog.findViewById(R.id.glumac_rodjen);

                     Glumac g = new Glumac();
                     g.setIme(ime.getText().toString());
                     g.setBiografija(bio.getText().toString());
                     g.setRodjen(rodjen.getText().toString());
                     g.setOcena(rating.getRating());

                     try{
                         getDatabaseHelper().getGlumacDao().create(g);

                         //provera podesavanja
                         boolean toast = preferenses.getBoolean(NOTIF_TOAST, false);
                         boolean status = preferenses.getBoolean(NOTIF_STATUS, false);

                         if (toast){
                             Toast.makeText(ListActivity.this, "Dodavanje novog glumca", Toast.LENGTH_SHORT).show();
                         }
                         if (status){
                             showStatusMessage("Dodavanje novog glumca");
                         }
                         //osvezavanje podataka
                         refresh();
                     }catch (SQLException e){
                         e.printStackTrace();
                     }
                     dialog.dismiss();
                 }
             });

             dialog.show();

             break;
         case R.id.about:
             AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
             alertDialog.show();
             break;
         case R.id.preferences:
             startActivity(new Intent(ListActivity.this, Preferences.class));
             break;
     }
        return super.onOptionsItemSelected(item);
    }

    //komunikacija sa bazom ide preko ove metode
    public ORMLiteHelper getDatabaseHelper(){
     if(databaseHelper == null){
         databaseHelper = OpenHelperManager.getHelper(this, ORMLiteHelper.class);
     }
     return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // zatvaramo bazu podataka oslobadjamo resurse
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
