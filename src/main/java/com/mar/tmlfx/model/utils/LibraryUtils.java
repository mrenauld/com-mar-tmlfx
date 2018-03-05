package com.mar.tmlfx.model.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.Library;
import com.mar.tmlfx.model.Track;

public class LibraryUtils {

    /**
     * Adds the content of the specified folder and subfolders to the specified
     * {@link Library}.
     *
     * @param pLibrary
     * @param pFolder
     */
    public static void addTracksFromFolder(Library pLibrary, String pFolder) {
        String normalizedPath = pFolder;

        if (normalizedPath.charAt(normalizedPath.length() - 1) != '/') {
            normalizedPath = normalizedPath + "/";
        }

        final File directory = new File(normalizedPath);
        final String[] children = directory.list();
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                final File subdir = new File(normalizedPath + children[i]);
                if (subdir.isDirectory()) {
                    addTracksFromFolder(pLibrary, normalizedPath + children[i]);
                } else {
                    final Track track = TrackUtils.loadTag(normalizedPath + children[i]);
                    if (track != null) {
                        pLibrary.addTrack(track);
                    }
                }
            }
        }
    }

    /**
     * Adds the content of the specified folder list and their subfolders to the
     * specified {@link Library}.
     *
     * @param pLibrary
     * @param pFolderList
     */
    public static void addTracksFromFolderList(Library pLibrary, ArrayList<String> pFolderList) {
        for (String folder : pFolderList) {
            addTracksFromFolder(pLibrary, folder);
        }
    }

    /**
     * Returns a {@link Library} loaded from the specified filename.
     *
     * @param pFilename
     * @return
     */
    public static Library loadLibraryFromFile(String pFilename) {
        Library library = new Library();

        try {
            InputStream input = new FileInputStream(pFilename);
            // BufferedReader br = new BufferedReader(new
            // InputStreamReader(input, "ISO-8859-1"));
            BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;

            while ((line = br.readLine()) != null) {
                Track track = TrackUtils.importFromString(line);

                if (track != null) {
                    library.addTrack(track);
                }
            }

            br.close();

        } catch (final IOException e) {
            LogUtils.logError(LibraryUtils.class, "IOException during library loading", e);
        }

        LogUtils.logInfo(LibraryUtils.class, "Library loaded with [" + library.size() + "] tracks");

        return library;
    }

    /**
     * Saves a {@link Library} to the specified filename.
     *
     * @param pLibrary
     * @param pFilename
     */
    public static void saveLibrary(Library pLibrary, String pFilename) {
        try {
            // PrintWriter pw = new PrintWriter(path, "ISO-8859-1");
            PrintWriter pw = new PrintWriter(pFilename, "UTF-8");
            for (Track track : pLibrary.getTrackList()) {
                pw.write(TrackUtils.exportToString(track) + "\r\n");
            }
            pw.close();
        } catch (final FileNotFoundException e) {
            LogUtils.logError(LibraryUtils.class, "FileNotFoundException during library loading", e);
        } catch (final IOException e) {
            LogUtils.logError(LibraryUtils.class, "IOException during library loading", e);
        }
    }

}
