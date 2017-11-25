package com.example.android.termin30.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.android.termin30.R;
import com.example.android.termin30.db.ORMLiteHelper;
import com.example.android.termin30.model.Film;
import com.example.android.termin30.model.Glumac;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import static com.example.android.termin30.activities.ListActivity.NOTIF_STATUS;
import static com.example.android.termin30.activities.ListActivity.NOTIF_TOAST;

public class MainActivity extends AppCompatActivity {

    private ORMLiteHelper databaseHelper;
    private SharedPreferences preferences;
    private Glumac g;

    private EditText ime;
    private EditText bio;
    private EditText rodjen;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(ListActivity.GLUMAC_KEY);

        try {
            g = getDatabaseHelper().getGlumacDao().queryForId(key);

            ime = (EditText) findViewById(R.id.glumac_ime);
            bio = (EditText) findViewById(R.id.glumac_biografija);
            rodjen = (EditText) findViewById(R.id.glumac_rodjen);
            rating = (RatingBar) findViewById(R.id.glumac_rating);

            ime.setText(g.getIme());
            bio.setText(g.getBiografija());
            rodjen.setText(g.getRodjen());
            rating.setRating(g.getOcena());
        }catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.glumac_filmovi);

        try{
            List<Film> list = getDatabaseHelper().getFilmDao().queryBuilder()
                    .where()
                    .eq(Film.FIELD_NAME_GLEDALAC, g.getId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Film f = (Film) listView.getItemAtPosition(position);
                    Toast.makeText(MainActivity.this, f.getNaziv()+" "+f.getZanr()+" "+f.getGodina(), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        ListView listview = (ListView) findViewById(R.id.glumac_filmovi);

        if(listview != null){
            ArrayAdapter<Film> adapter = (ArrayAdapter<Film>) listview.getAdapter();

            if(adapter != null){
                try {
                    adapter.clear();
                    List<Film> list = getDatabaseHelper().getFilmDao().queryBuilder()
                            .where()
                            .eq(Film.FIELD_NAME_GLEDALAC, g.getId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Termin 30");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
        boolean toast = preferences.getBoolean(NOTIF_TOAST, false);
        boolean status = preferences.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if(status){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_film:
                //otvara se dialog za unos podataka
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_film);

                Button add = (Button) dialog.findViewById(R.id.add_film);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText naziv = (EditText) dialog.findViewById(R.id.film_naziv);
                        EditText zanr = (EditText) dialog.findViewById(R.id.film_zanr);
                        EditText godina = (EditText) dialog.findViewById(R.id.film_godina);

                        Film f = new Film();
                        f.setNaziv(naziv.getText().toString());
                        f.setZanr(zanr.getText().toString());
                        f.setGodina(godina.getText().toString());
                        f.setGledalac(g);

                        try {
                            getDatabaseHelper().getFilmDao().create(f);
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        //osvezavanje aplikacije
                        refresh();

                        showMessage("Dodat je film za datog glumca");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit:
                //preuzimanje informacija iz polja za unos
                g.setIme(ime.getText().toString());
                g.setRodjen(rodjen.getText().toString());
                g.setBiografija(bio.getText().toString());
                g.setOcena(rating.getRating());

                try {
                    getDatabaseHelper().getGlumacDao().update(g);
                    showMessage("Podaci o glumcu uspesno izmenjeni");
                }catch (SQLException e){
                    e.printStackTrace();
                }
                break;
            case R.id.remove:
                try{
                    getDatabaseHelper().getGlumacDao().delete(g);
                    showMessage("Glumac izbrisan");
                    finish(); //vraca nas na prethodnu aktivnost
                }catch (SQLException e){
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //za komunikaciju sa bazom podataka koristi se ova mestoda
    public ORMLiteHelper getDatabaseHelper(){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(this, ORMLiteHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //oslobadjamo resurse, po zavrsetku rada sa bazom
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
