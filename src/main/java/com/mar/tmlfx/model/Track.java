package com.mar.tmlfx.model;

import java.io.Serializable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Track implements Serializable {

    public static enum INFOTYPES {
        FILENAME, ARTIST, TITLE, ARTIST_TITLE, ALBUM, NUMBER, GENRE, YEAR
    }

    private static final long serialVersionUID = -6540703748123219207L;;

    private StringProperty title = new SimpleStringProperty();

    private StringProperty artist = new SimpleStringProperty();

    private StringProperty filename = new SimpleStringProperty();

    private StringProperty album = new SimpleStringProperty();

    private IntegerProperty tracknumber = new SimpleIntegerProperty();

    private StringProperty genre = new SimpleStringProperty();

    private IntegerProperty year = new SimpleIntegerProperty();

    private IntegerProperty playIdx = new SimpleIntegerProperty();

    /**
     * Empty constructor.
     */
    public Track() {

    }

    /**
     * Constructs a track with the specified filename.
     *
     * @param pFilename
     */
    public Track(String pFilename) {
        filename = new SimpleStringProperty(pFilename);
    }

    /**
     * Returns the album property.
     *
     * @return
     */
    public StringProperty albumProperty() {
        return album;
    }

    /**
     * Returns the artist property.
     *
     * @return
     */
    public StringProperty artistProperty() {
        return artist;
    }

    @Override
    public boolean equals(Object pObject) {
        if (!(pObject instanceof Track)) {
            return false;
        }

        Track track = (Track) pObject;

        if (!artist.get().equals(track.getArtist())) {
            return false;
        }
        if (!title.get().equals(track.getTitle())) {
            return false;
        }
        if (!album.get().equals(track.getAlbum())) {
            return false;
        }
        if (!genre.get().equals(track.getGenre())) {
            return false;
        }
        if (!filename.get().equals(track.getFilename())) {
            return false;
        }
        if (tracknumber.get() != track.getNumber()) {
            return false;
        }
        if (year.get() != track.getYear()) {
            return false;
        }
        return true;
    }

    /**
     * Returns the filename property.
     * 
     * @return
     */
    public StringProperty filenameProperty() {
        return filename;
    }

    /**
     * Returns the genre property.
     * 
     * @return
     */
    public StringProperty genreProperty() {
        return genre;
    }

    /**
     * Returns the album.
     * 
     * @return
     */
    public String getAlbum() {
        return album.get();
    }

    /**
     * Returns the artist.
     * 
     * @return
     */
    public String getArtist() {
        return artist.get();
    }

    /**
     * Returns the filename.
     * 
     * @return
     */
    public String getFilename() {
        return filename.get();
    }

    /**
     * Returns the genre.
     * 
     * @return
     */
    public String getGenre() {
        return genre.get();
    }

    /**
     * Returns the key (filename).
     * 
     * @return
     */
    public String getKey() {
        return filename.get();
    }

    /**
     * Returns the track number.
     * 
     * @return
     */
    public int getNumber() {
        return tracknumber.get();
    }

    /**
     * Returns the track play index (used when it is stored in a
     * {@link TrackList}).
     * 
     * @return
     */
    public int getPlayIdx() {
        return playIdx.get();
    }

    /**
     * Returns the title.
     * 
     * @return
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Returns the year.
     * 
     * @return
     */
    public int getYear() {
        return year.get();
    }

    /**
     * Returns the play index property.
     * 
     * @return
     */
    public IntegerProperty playIdxProperty() {
        return playIdx;
    }

    /**
     * Sets the album.
     * 
     * @param pAlbum
     */
    public void setAlbum(String pAlbum) {
        album.set(pAlbum);
    }

    /**
     * Sets the artist.
     * 
     * @param pArtist
     */
    public void setArtist(String pArtist) {
        artist.set(pArtist);
    }

    /**
     * Sets the filename.
     * 
     * @param pFilename
     */
    public void setFilename(String pFilename) {
        filename.set(pFilename);
    }

    /**
     * Sets the genre.
     * 
     * @param pGenre
     */
    public void setGenre(String pGenre) {
        genre.set(pGenre);
    }

    /**
     * Sets the number.
     * 
     * @param pTrackNumber
     */
    public void setNumber(int pTrackNumber) {
        tracknumber.set(pTrackNumber);
    }

    /**
     * Sets the play index.
     * 
     * @param pPlayIdx
     */
    public void setPlayIdx(int pPlayIdx) {
        playIdx.set(pPlayIdx);
    }

    /**
     * Sets the title.
     * 
     * @param pTitle
     */
    public void setTitle(String pTitle) {
        title.set(pTitle);
    }

    /**
     * Sets the year.
     * 
     * @param pYear
     */
    public void setYear(int pYear) {
        year.set(pYear);
    }

    /**
     * Returns the title property.
     * 
     * @return
     */
    public StringProperty titleProperty() {
        return title;
    }

    @Override
    public String toString() {
        String out = "";
        out += "Artist : " + artist + "\r\n";
        out += "Title : " + title + "\r\n";
        if (filename == null || filename.equals("")) {
            out += "Filename : not defined.\r\n";
        } else {
            out += "Filename : " + filename + "\r\n";
        }
        if (album != null && !album.equals("")) {
            out += "Album : " + album + "\r\n";
        }
        if (tracknumber.get() != 0) {
            out += "Track number : " + Integer.toString(tracknumber.get()) + "\r\n";
        }
        if (genre != null && !genre.equals("")) {
            out += "Genre : " + genre + "\r\n";
        }
        if (year.get() != 0) {
            out += "Year : " + Integer.toString(year.get()) + "\r\n";
        }
        out += "EoT\r\n";
        return out;
    }

    /**
     * Returns the track number property.
     * 
     * @return
     */
    public IntegerProperty tracknumberProperty() {
        return tracknumber;
    }

    /**
     * Returns the year property.
     * 
     * @return
     */
    public IntegerProperty yearProperty() {
        return year;
    }
}
