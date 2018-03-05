package com.mar.tmlfx;

import java.util.ArrayList;

import com.mar.framework.core.settings.AbstractSettings;

public class Settings extends AbstractSettings {

    public static final String TML_FILE_EXTENSION_LIBRARY = "tml.file.extension.library";

    public static final String TML_FILE_EXTENSION_PLAYLIST = "tml.file.extension.playlist";

    public static final String TML_FILE_LIBRARY_FILENAME = "tml.file.library.filename";

    public static final String TML_FILE_LIBRARY_FOLDER_LIST = "tml.file.library.folder.list";

    public static final String TML_FILE_PLAYLIST_FOLDER = "tml.file.playlist.folder";

    public static final String TML_PATH_VLC = "tml.path.vlc";

    public static final String TML_PLAY_RANDOM = "tml.play.random";

    public static String getFileExtensionLibrary() {
        return getValue(TML_FILE_EXTENSION_LIBRARY);
    }

    public static String getFileExtensionPlaylist() {
        return getValue(TML_FILE_EXTENSION_PLAYLIST);
    }

    public static String getFileLibraryFilename() {
        return getValue(TML_FILE_LIBRARY_FILENAME);
    }

    public static ArrayList<String> getFileLibraryFolderList() {
        return getValueList(TML_FILE_LIBRARY_FOLDER_LIST);
    }

    public static String getFilePlaylistFolder() {
        return getValue(TML_FILE_PLAYLIST_FOLDER);
    }

    public static String getPathVlc() {
        return getValue(TML_PATH_VLC);
    }

    public static boolean isPlayRandom() {
        return getValueBoolean(TML_PLAY_RANDOM);
    }

    public static void setPlayRandom(boolean pValue) {
        setValue(TML_PLAY_RANDOM, pValue);
    }
}
