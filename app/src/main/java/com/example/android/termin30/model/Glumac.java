package com.example.android.termin30.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by BBLOJB on 25.11.2017..
 */
@DatabaseTable(tableName = Glumac.TABLE_NAME_USERS)
public class Glumac {

    public static final String TABLE_NAME_USERS = "glumci";
    public static final String FIELD_NAME_ID = "id";
    public static final String TABLE_GLUMAC_IME = "ime";
    public static final String TABLE_GLUMAC_BIOGRAFIJA = "biografija";
    public static final String TABLE_GLUMAC_OCENA = "ocena";
    public static final String TABLE_GLUMAC_RODJEN = "rodjen";
    public static final String TABLE_GLUMAC_FILMOVI = "filmovi";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = TABLE_GLUMAC_IME)
    private String ime;

    @DatabaseField(columnName = TABLE_GLUMAC_BIOGRAFIJA)
    private String biografija;

    @DatabaseField(columnName = TABLE_GLUMAC_OCENA)
    private Float ocena;

    @DatabaseField(columnName = TABLE_GLUMAC_RODJEN)
    private String rodjen;

    @ForeignCollectionField(columnName = Glumac.TABLE_GLUMAC_FILMOVI, eager = true)
    private ForeignCollection<Film> filmovi;

    public Glumac() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getBiografija() {
        return biografija;
    }

    public void setBiografija(String biografija) {
        this.biografija = biografija;
    }

    public Float getOcena() {
        return ocena;
    }

    public void setOcena(Float ocena) {
        this.ocena = ocena;
    }

    public String getRodjen() {
        return rodjen;
    }

    public void setRodjen(String rodjen) {
        this.rodjen = rodjen;
    }

    public ForeignCollection<Film> getFilmovi() {
        return filmovi;
    }

    public void setFilmovi(ForeignCollection<Film> filmovi) {
        this.filmovi = filmovi;
    }

    @Override
    public String toString() {
        return ime;
    }
}
