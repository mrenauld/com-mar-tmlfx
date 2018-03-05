package com.mar.tmlfx.model.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mar.framework.core.logging.LogUtils;
import com.mar.framework.core.utils.ObjectUtils;
import com.mar.iotools.directory.DirectoryUtils;
import com.mar.iotools.string.FilepathUtils;
import com.mar.tmlfx.Settings;
import com.mar.tmlfx.model.Library;
import com.mar.tmlfx.model.Playlist;
import com.mar.tmlfx.model.Track;

public class PlaylistUtils {
    public static Playlist loadPlaylist(String pFilename, Library pLibrary) {
        final List<String> keyList = PlaylistUtils.loadTracklist(pFilename);
        Playlist playlist = new Playlist();
        playlist.setFilename(pFilename);
        playlist.setTitle(pFilename);
        List<Track> trackList = pLibrary.getTracks(keyList);
        playlist.addTrackList(trackList);

        return playlist;
    }

    public static ArrayList<Playlist> loadSavedPlaylists(String pFilename, Library pLibrary) {
        ArrayList<String> list = DirectoryUtils.listFilesInFolder(pFilename, false,
                Settings.getFileExtensionPlaylist());
        ArrayList<Playlist> playlistList = new ArrayList<Playlist>();
        for (String fname : list) {
            Playlist playlist = loadPlaylist(fname, pLibrary);
            playlistList.add(playlist);
        }
        return playlistList;
    }

    /**
     * Saves the specified playlist to the specified file.
     *
     * @param pPlaylist
     * @param pFilename
     */
    public static void savePlaylist(Playlist pPlaylist) {
        String filename = pPlaylist.getFilename();

        final String ext = FilepathUtils.getExtension(filename);
        if (ext == null || !ext.equals(Settings.getFileExtensionPlaylist())) {
            filename = FilepathUtils.addExtension(filename, Settings.getFileExtensionPlaylist());
        }

        StringBuilder sb = new StringBuilder();
        List<String> keyList = pPlaylist.getKeyList();
        for (int i = 0; i < keyList.size(); ++i) {
            sb.append(keyList.get(i) + System.getProperty("line.separator"));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            LogUtils.logError(PlaylistUtils.class, "IOException when writing playlist to file [" + filename + "]", e);
        }
    }

    /**
     * Loads the specified playlist file and returns the corresponding keys as
     * an array of strings.
     *
     * @param pFilename
     * @return
     */
    private static List<String> loadTracklist(String pFilename) {
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(pFilename))) {
            list = stream.filter(text -> !ObjectUtils.isObjectEmpty(text)).collect(Collectors.toList());

        } catch (IOException e) {
            LogUtils.logError(PlaylistUtils.class, "IOException when loading playlist from file [" + pFilename + "]",
                    e);
        }

        for (int i = 0; i < list.size(); ++i) {
            list.set(i, list.get(i).split(MusicDataConst.SEP)[0]);
        }

        return list;
    }
}