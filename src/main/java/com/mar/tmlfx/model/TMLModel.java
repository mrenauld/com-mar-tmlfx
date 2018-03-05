package com.mar.tmlfx.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.Settings;
import com.mar.tmlfx.model.utils.LibraryUtils;
import com.mar.tmlfx.model.utils.PlaylistUtils;
import com.mar.tmlfx.model.utils.TrackUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TMLModel {

    /** The library. */
    private Library library;

    /** The current playlist. */
    private Playlist currentPlaylist = new Playlist();

    /** Observable list of saved playlists. */
    private ObservableList<Playlist> savedPlaylistList = FXCollections.observableArrayList();

    /** Indicates if the library was modified, and need to be saved. */
    private boolean libraryModified = false;

    /**
     * Constructs a new MusicData object.
     */
    public TMLModel() {
        library = new Library();
    }

    public void addTracksToPlaylist(List<Track> pTrackList) {
        boolean emptyBeforeAdd = currentPlaylist.isEmpty();
        currentPlaylist.addTrackList(pTrackList);

        if (Settings.isPlayRandom()) {
            currentPlaylist.randomizePlayOrder();
        }

        if (emptyBeforeAdd && !currentPlaylist.isEmpty()) {
            currentPlaylist.goToFirst();
        }
    }

    /**
     * Adds the tracks in the specified saved playlist file to the current
     * playlist.
     *
     * @param pPlaylistPath
     */
    public void addTracksToPlaylist(String pPlaylistPath) {
        Playlist playlist = PlaylistUtils.loadPlaylist(pPlaylistPath, library);
        currentPlaylist.addTrackList(playlist.getTrackList());

        if (Settings.isPlayRandom()) {
            currentPlaylist.randomizePlayOrder();
        }
    }

    /**
     * Builds a new library with all the music files found in the specified
     * folder.
     *
     * @param pFolder
     */
    public void buildLibrary() {
        // library.buildLibrary(pFolder);
        currentPlaylist.empty();
    }

    /**
     * Empties the current playlist.
     */
    public void clearPlaylist() {
        currentPlaylist.empty();
    }

    public Playlist getCurrentPLaylist() {
        return currentPlaylist;
    }

    public Library getLibrary() {
        return library;
    }

    public ObservableList<Track> getLibraryTrackList() {
        return library.getTrackList();
    }

    public Track getNextTrackToPlay() {
        if (currentPlaylist != null) {
            currentPlaylist.goToNext();
            return currentPlaylist.getCurrentTrack();
        }
        return null;
    }

    public ObservableList<Track> getPlaylistTrackList() {
        return currentPlaylist.getTrackList();
    }

    public Track getPrevTrackToPlay() {
        if (currentPlaylist != null) {
            currentPlaylist.goToPrev();
            return currentPlaylist.getCurrentTrack();
        }
        return null;
    }

    public ObservableList<Playlist> getSavedPlaylistList() {
        return savedPlaylistList;
    }

    public void initialize() {
        /* Load library. */
        loadLibrary();

        /* Load playlist list. */
        String playlistFolderFilename = Settings.getFilePlaylistFolder();
        ArrayList<Playlist> list = PlaylistUtils.loadSavedPlaylists(playlistFolderFilename, library);
        savedPlaylistList.addAll(list);

        currentPlaylist.empty();
    }

    public void loadLibrary() {
        /* Load library. */
        String libraryFilename = Settings.getFileLibraryFilename();
        if (new File(libraryFilename).exists() == false) {
            LogUtils.logInfo(this.getClass(), "There is no file at [" + libraryFilename + "]");
        } else {
            library = LibraryUtils.loadLibraryFromFile(libraryFilename);
        }

        currentPlaylist.empty();
    }

    public void loadPlaylist(String pPath) {
        Playlist newPlaylist = PlaylistUtils.loadPlaylist(pPath, library);

        currentPlaylist.empty();
        currentPlaylist.addTrackList(newPlaylist.getTrackList());

        if (Settings.isPlayRandom()) {
            currentPlaylist.randomizePlayOrder();
        }

        currentPlaylist.goToFirst();
    }

    public Track playNext() {
        currentPlaylist.goToNext();
        return currentPlaylist.getCurrentTrack();
    }

    public Track playPrev() {
        currentPlaylist.goToPrev();
        return currentPlaylist.getCurrentTrack();
    }

    public Track playSetCurrent(Track pTrack) {
        currentPlaylist.goToTrack(pTrack);
        return currentPlaylist.getCurrentTrack();
    }

    /**
     * Performs the closing operations like saving the library if needed.
     */
    public void quit() {
        if (libraryModified) {
            LibraryUtils.saveLibrary(library, Settings.getFileLibraryFilename());
            LogUtils.logInfo(this,
                    "Library was modified, so it was saved on [" + Settings.getFileLibraryFilename() + "]");
        }
    }

    public void refreshLibrary() {
        LibraryUtils.addTracksFromFolderList(library, Settings.getFileLibraryFolderList());
        libraryModified = true;
    }

    public void removeTracksFromPlaylist(List<Track> pTrackList) {
        currentPlaylist.removeTrackList(pTrackList);
    }

    public void saveCurrentPlaylist(String pFilename) {
        Playlist newPlaylist = new Playlist();
        newPlaylist.setFilename(pFilename);
        newPlaylist.setTitle(pFilename);
        newPlaylist.addTrackList(currentPlaylist.getTrackList());
        PlaylistUtils.savePlaylist(newPlaylist);

        boolean playlistExists = false;
        for (Playlist playlist : savedPlaylistList) {
            if (playlist.getFilename().equals(pFilename)) {
                playlistExists = true;
                break;
            }
        }

        if (!playlistExists) {
            savedPlaylistList.add(newPlaylist);
        }
    }

    public void saveLibrary() {
        LibraryUtils.saveLibrary(library, Settings.getFileLibraryFilename());
        LogUtils.logInfo(this, "Library saved to [" + Settings.getFileLibraryFilename() + "]");
    }

    public void updateTagList(List<Track> pTrackList) {
        for (Track track : pTrackList) {
            TrackUtils.updateTag(track);
        }
        libraryModified = true;
    }
}