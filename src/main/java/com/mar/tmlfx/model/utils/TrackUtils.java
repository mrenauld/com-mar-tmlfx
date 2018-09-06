package com.mar.tmlfx.model.utils;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import com.mar.framework.core.logging.LogUtils;
import com.mar.framework.core.utils.ObjectUtils;
import com.mar.iotools.string.FilepathUtils;
import com.mar.tmlfx.model.Track;

public class TrackUtils {

    public static final String NO_INFO = "-";

    public static final String[] DEFINED_GENRE = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk",
            "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Autre", "Pop", "R'n'B", "Rap", "Reggae", "Rock",
            "Techno", "Industrial", "Alternatif", "Ska", "Death Metal", "Pranks", "Musique de film", "Euro-Techno",
            "Ambient", "Trip-Hop", "Musique vocale", "Jazz-Funk", "Fusion", "Trance", "Musique classique",
            "Instrumental", "Acid", "House", "Musique de jeu video", "Extrait sonore", "Gospel", "Noise",
            "Rock alternatif", "Bass", "Soul", "Punk", "Space", "Meditative", "Pop instrumental", "Rock instrumental",
            "Musique ethnique", "Gothique", "Darkwave", "Techno-Industrial", "Musique électronique", "Pop-Folk",
            "Eurodance", "Dream", "Southern Rock", "Comédie", "Cult", "Gangsta", "Hit-parade", "Rap chrétien",
            "Pop/Funk", "Jungle", "Musique amérindienne", "Cabaret", "New Wave", "Psychédélique", "Rave",
            "Comédie musicale", "Bande-annonce", "Lo-fi", "Musique tribale", "Acid Punk", "Acid Jazz", "Polka",
            "Rétro", "Théatre", "Rock & Roll", "Hard Rock" };

    public static String exportToString(Track pTrack) {
        StringBuilder sb = new StringBuilder();

        sb.append(stringToLibString(pTrack.getFilename()) + MusicDataConst.SEP);
        sb.append(stringToLibString(pTrack.getArtist()) + MusicDataConst.SEP);
        sb.append(stringToLibString(pTrack.getTitle()) + MusicDataConst.SEP);
        sb.append(stringToLibString(pTrack.getAlbum()) + MusicDataConst.SEP);
        sb.append(intToLibString(pTrack.getNumber()) + MusicDataConst.SEP);
        sb.append(intToLibString(pTrack.getYear()) + MusicDataConst.SEP);
        sb.append(stringToLibString(pTrack.getGenre()));

        return sb.toString();
    }

    public static Track importFromString(final String trackString) {
        Track track = null;
        String[] split = trackString.split(MusicDataConst.SEP);

        if (split.length == 7) {
            track = new Track();
            track.setFilename(libStringToString(split[0]));
            track.setArtist(libStringToString(split[1]));
            track.setTitle(libStringToString(split[2]));
            track.setAlbum(libStringToString(split[3]));
            track.setNumber(libStringToInt(split[4]));
            track.setYear(libStringToInt(split[5]));
            track.setGenre(libStringToString(split[6]));
        } else {
            LogUtils.logError(TrackUtils.class, "The track string [" + trackString + "] cannot be parsed as a track. "
                    + "Expected number of values is 7, actual number is [" + split.length + "]");
        }

        return track;
    }

    /**
     * Returns a new track built from the tag from the specified file.
     *
     * @param path
     *            the path to the file.
     * @return the new track.
     */
    public static Track loadTag(String path) {

        path = normalizePath(path);

        final String extension = FilepathUtils.getExtension(path).toLowerCase();
        // TODO more file ext.
        if (!extension.equals("mp3")) {
            return null;
        }

        Track track = null;

        try {
            MP3File mp3file = new MP3File(new File(path), MP3File.LOAD_ALL);

            if (mp3file.hasID3v2Tag()) {
                ID3v24Tag v24tag = mp3file.getID3v2TagAsv24();
                String artist = v24tag.getFirst(FieldKey.ARTIST);
                String title = v24tag.getFirst(FieldKey.TITLE);

                track = new Track(path);

                // Artist.
                track.setArtist(artist);
                // Title.
                track.setTitle(title);
                // Album.
                String album = v24tag.getFirst(FieldKey.ALBUM);
                track.setAlbum(album);
                // Track number.
                try {
                    int tracknumber = Integer.parseInt(v24tag.getFirst(FieldKey.TRACK));
                    track.setNumber(tracknumber);
                } catch (final NumberFormatException e) {
                    LogUtils.logDebug(TrackUtils.class, "The track number is not a valid number.");
                }
                // Genre.
                String genre = v24tag.getFirst(FieldKey.GENRE);
                if (genre.length() > 3 && genre.charAt(0) == '(' && genre.charAt(genre.length() - 1) == ')') {
                    try {
                        String numgenre = "";
                        for (int i = 1; i < genre.length() - 1; ++i) {
                            numgenre += genre.charAt(i);
                        }
                        int idx = Integer.parseInt(numgenre);
                        if (idx < DEFINED_GENRE.length) {
                            track.setGenre(DEFINED_GENRE[idx]);
                        }
                    } catch (NumberFormatException e) {
                        LogUtils.logDebug(TrackUtils.class, "The genre number is not a valid number.");
                    }
                } else {
                    track.setGenre(genre);
                }
                // Year.
                try {
                    int year = Integer.parseInt(v24tag.getFirst(FieldKey.YEAR));
                    track.setYear(year);
                } catch (NumberFormatException e) {
                    LogUtils.logDebug(TrackUtils.class, "The year is not a valid number.");
                }

            } else {
                LogUtils.logDebug(TrackUtils.class, "No id3v2 tag for the track at " + path + ".");
            }
            if (!mp3file.hasID3v2Tag() && mp3file.hasID3v1Tag()) {
                AbstractID3v1Tag tag = mp3file.getID3v1Tag();
                ID3v1Tag v1tag = (ID3v1Tag) tag;
                String artist = v1tag.getFirst(FieldKey.ARTIST);
                String title = v1tag.getFirst(FieldKey.TITLE);

                track = new Track(path);

                // Artist.
                track.setArtist(artist);
                // Title.
                track.setTitle(title);

            } else if (!mp3file.hasID3v1Tag()) {
                LogUtils.logDebug(TrackUtils.class, "No id3v1 tag for the track at " + path + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        return track;
    }

    public static String normalizePath(final String path) {
        final String[] split = path.split("[\\\\]");
        String out = "";
        for (int i = 0; i < split.length; ++i) {
            out += split[i];
            if (i < split.length - 1) {
                out += "/";
            }
        }
        return out;
    }

    public static void updateTag(Track pTrack) {
        LogUtils.logInfo(TrackUtils.class, "Updating tag for track [" + pTrack.getFilename() + "]");

        // Check if the extension is valid.
        final String filename = pTrack.getFilename();
        if (!FilepathUtils.hasExtension(filename, "mp3")) {
            LogUtils.logError(TrackUtils.class,
                    "Impossible to save tag, unexpected extension for filename [" + filename + "]");
            return;
        }

        try {
            final MP3File mp3file = new MP3File(new File(filename));

            final Tag tag = mp3file.getTag();

            // Artist.
            tag.setField(FieldKey.ARTIST, pTrack.getArtist());
            // Track name.
            tag.setField(FieldKey.TITLE, pTrack.getTitle());
            // Album.
            if (!ObjectUtils.isObjectEmpty(pTrack.getAlbum())) {
                tag.setField(FieldKey.ALBUM, pTrack.getAlbum());
            } else {
                tag.deleteField(FieldKey.ALBUM);
            }
            // Track number.
            if (pTrack.getNumber() > 0) {
                tag.setField(FieldKey.TRACK, Integer.toString(pTrack.getNumber()));
            } else {
                tag.deleteField(FieldKey.TRACK);
            }
            // Genre.
            if (!ObjectUtils.isObjectEmpty(pTrack.getGenre())) {
                tag.setField(FieldKey.GENRE, pTrack.getGenre());
            } else {
                tag.deleteField(FieldKey.GENRE);
            }
            // Year.
            if (pTrack.getYear() > 0) {
                tag.setField(FieldKey.YEAR, Integer.toString(pTrack.getYear()));
            } else {
                tag.deleteField(FieldKey.TRACK);
            }

            // ...

            mp3file.commit();

        } catch (IOException e) {
            LogUtils.logError(TrackUtils.class, "IOException while saving tag", e);
        } catch (TagException e) {
            LogUtils.logError(TrackUtils.class, "TagException while saving tag", e);
        } catch (ReadOnlyFileException e) {
            LogUtils.logError(TrackUtils.class, "ReadOnlyFileException while saving tag", e);
        } catch (InvalidAudioFrameException e) {
            LogUtils.logError(TrackUtils.class, "InvalidAudioFrameException while saving tag", e);
        } catch (CannotWriteException e) {
            LogUtils.logError(TrackUtils.class, "CannotWriteException while saving tag", e);
        }
    }

    private static String intToLibString(int pInt) {
        String libString = "";
        if (pInt != 0) {
            libString += pInt;
        } else {
            libString += NO_INFO;
        }
        return libString;
    }

    private static int libStringToInt(String pString) {
        int i = 0;
        if (!NO_INFO.equals(pString)) {
            i = Integer.parseInt(pString);
        }
        return i;
    }

    private static String libStringToString(String pLibString) {
        String s = "";
        if (!NO_INFO.equals(pLibString)) {
            s = pLibString;
        }
        return s;
    }

    private static String stringToLibString(String pString) {
        String libString = "";
        if (!ObjectUtils.isObjectEmpty(pString)) {
            libString = pString;
        } else {
            libString = NO_INFO;
        }
        return libString;
    }
}