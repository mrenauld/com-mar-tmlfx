package com.mar.tmlfx.model;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class TrackListTest {

    private static Track track1;

    private static Track track2;

    private static Track track3;

    @Before
    public void init() {
        track1 = new Track("filename1");
        track2 = new Track("filename2");
        track3 = new Track("filename3");
    }

    @Test
    public void testAddTracks() {
        TrackList trackList = new TrackList();

        Assertions.assertThat(trackList.size()).isEqualTo(0);

        trackList.addTrack(track1);
        trackList.addTrack(track2);
        trackList.addTrack(track3);

        Assertions.assertThat(trackList.size()).isEqualTo(3);

        /* The track2 is already present so it is not added twice. */
        trackList.addTrack(track2);

        Assertions.assertThat(trackList.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveTracks() {
        TrackList trackList = new TrackList();
        trackList.addTrack(track1);
        trackList.addTrack(track2);
        trackList.addTrack(track3);

        Assertions.assertThat(trackList.size()).isEqualTo(3);

        trackList.removeTrack(track2);

        Assertions.assertThat(trackList.size()).isEqualTo(2);

        trackList.removeTrack(track2);

        Assertions.assertThat(trackList.size()).isEqualTo(2);

        trackList.removeTrack(track1);
        trackList.removeTrack(track3);

        Assertions.assertThat(trackList.isEmpty()).isTrue();
    }

}
