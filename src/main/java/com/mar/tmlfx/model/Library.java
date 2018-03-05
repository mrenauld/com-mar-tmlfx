package com.mar.tmlfx.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mar.framework.core.logging.LogUtils;

public class Library extends TrackList {

    public List<Track> getTracks(List<String> pKeyList) {
        ArrayList<Track> trackList = new ArrayList<Track>();
        for (String key : pKeyList) {
            Track track = map.get(key);
            if (track != null) {
                trackList.add(track);
            } else {
                LogUtils.logError(this, "No track found in library for key [" + key + "]");
            }
        }
        return trackList;
    }

    @Override
    public String toString() {
        String out = "";
        final Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            out += map.get(it.next()).toString();
        }
        return out;
    }
}