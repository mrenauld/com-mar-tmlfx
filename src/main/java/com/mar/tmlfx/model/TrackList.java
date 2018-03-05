package com.mar.tmlfx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mar.framework.core.logging.LogUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Objects representing a list of {@link Track}s. The tracks are stored in an
 * ObservableList, and also in a HashMap for quick access. The key used to index
 * the tracks is the Track key.
 *
 * @author mrenauld
 *
 */
public class TrackList {

    /** HashMap of tracks for quick access. */
    protected HashMap<String, Track> map = new HashMap<String, Track>();

    /** Observable list of tracks. */
    protected ObservableList<Track> trackList = FXCollections.observableArrayList();

    /**
     * Adds the specified {@link Track} to the track list. If a track with the
     * same key is already present in the list, it is not added.
     *
     * @param pTrack
     */
    public void addTrack(Track pTrack) {
        if (map.containsKey(pTrack.getKey())) {
            LogUtils.logDebug(this,
                    "The track with key [" + pTrack.getKey() + "] is already present in the track list");
        } else {
            map.put(pTrack.getKey(), pTrack);
            trackList.add(pTrack);
            LogUtils.logDebug(this, "The track with key [" + pTrack.getKey() + "] is added in the track list");
        }
    }

    /**
     * Adds the specified list of {@link Track}s to the track list. If tracks
     * with the same key are already present in the list, those are skipped.
     *
     * @param pTrackList
     */
    public void addTrackList(List<Track> pTrackList) {
        for (Track track : pTrackList) {
            addTrack(track);
        }
    }

    /**
     * Returns true if the track list contains a {@link Track} with the
     * specified key.
     *
     * @param pKey
     * @return
     */
    public boolean contains(String pKey) {
        return map.containsKey(pKey);
    }

    /**
     * Empties the track list.
     */
    public void empty() {
        map = new HashMap<String, Track>();
        trackList.clear();
    }

    /**
     * Returns the {@link Track} with the specified key from the track list.
     * Returns null if no such key is present in the track list.
     *
     * @param pKey
     * @return
     */
    public Track get(String pKey) {
        return map.get(pKey);
    }

    /**
     * Returns the list of keys for the {@link Track} in the track list.
     *
     * @return
     */
    public List<String> getKeyList() {
        ArrayList<String> keyList = new ArrayList<String>(trackList.size());
        for (Track track : trackList) {
            keyList.add(track.getKey());
        }
        return keyList;
    }

    /**
     * Returns the observable list of {@link Track}s.
     *
     * @return
     */
    public ObservableList<Track> getTrackList() {
        return trackList;
    }

    /**
     * Returns true if the track list is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return trackList.size() == 0;
    }

    /**
     * Removes the {@link Track} with specified key from the track list.
     *
     * @param pKey
     */
    public void removeTrack(String pKey) {
        int indexInList = -1;
        for (int i = 0; i < trackList.size(); ++i) {
            if (trackList.get(i).getKey().equals(pKey)) {
                indexInList = i;
                break;
            }
        }

        if (indexInList != -1) {
            trackList.remove(indexInList);
            map.remove(pKey);
        }
    }

    /**
     * Removes the specified {@link Track} from the track list.
     *
     * @param pTrack
     */
    public void removeTrack(Track pTrack) {
        removeTrack(pTrack.getKey());
    }

    /**
     * Removes the specified list of {@link Track}s from the track list.
     *
     * @param pTrackList
     */
    public void removeTrackList(List<Track> pTrackList) {
        for (Track track : trackList) {
            removeTrack(track);
        }
    }

    /**
     * Returns the number of {@link Track}s in the track list.
     *
     * @return
     */
    public int size() {
        return trackList.size();
    }
}
