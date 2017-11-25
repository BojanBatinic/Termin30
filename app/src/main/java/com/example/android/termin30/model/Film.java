package com.example.android.termin30.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by BBLOJB on 25.11.2017..
 */
@DatabaseTable(tableName = Film.TABLE_NAME_USERS)
public class Film {

    public static final String TABLE_NAME_USERS = "filmovi";
    public static final String FIELD_NAME_ID = "id";
    public static final String TABLE_FILM_NAZIV = "naziv";
    public static final String TABLE_FILM_ZANR = "zanr";
    public static final String FILED_NAME_GODINA = "godina";
    public static final String FIELD_NAME_GLEDALAC = "gledalac";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = TABLE_FILM_NAZIV)
    private String naziv;

    @DatabaseField(columnName = TABLE_FILM_ZANR)
    private String zanr;

    @DatabaseField(columnName = FILED_NAME_GODINA)
    private String godina;

    @DatabaseField(columnName = FIELD_NAME_GLEDALAC, foreign = true, foreignAutoRefresh = true)
    private Glumac gledalac;

    public Film() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getGodina() {
        return godina;
    }

    public void setGodina(String godina) {
        this.godina = godina;
    }

    public Glumac getGledalac() {
        return gledalac;
    }

    public void setGledalac(Glumac gledalac) {
        this.gledalac = gledalac;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
