package com.example.android.termin30.db;

import com.example.android.termin30.model.Film;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.termin30.model.Glumac;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by BBLOJB on 25.11.2017..
 */

public class ORMLiteHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME    = "priprema.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Film, Integer> filmDao = null;
    private Dao<Glumac, Integer> glumacDao = null;

    public ORMLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource){
        try {
            TableUtils.createTable(connectionSource, Film.class);
            TableUtils.createTable(connectionSource, Glumac.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Film.class, true);
            TableUtils.dropTable(connectionSource, Glumac.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Film, Integer> getFilmDao() throws SQLException {
        if(filmDao == null){
            filmDao = getDao(Film.class);
        }
        return filmDao;
    }
    public Dao<Glumac, Integer> getGlumacDao() throws SQLException{
        if(glumacDao == null){
            glumacDao = getDao(Glumac.class);
        }
        return glumacDao;
    }

    //kada zavrsiom rad sa bazom podataka oslobadjamo resurse
    @Override
    public void close(){
        filmDao = null;
        glumacDao = null;

        super.close();
    }

}
