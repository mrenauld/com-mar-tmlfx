package com.mar.tmlfx.view;

import java.util.concurrent.TimeUnit;

import com.mar.tmlfx.model.Track;

import javafx.util.Duration;

public class ViewUtils {

    public static String formatTimeFX(Duration pElapsed, Duration pDuration) {
        int intElapsed = (int) Math.floor(pElapsed.toSeconds());

        int elapsedHours = intElapsed / (60 * 60);
        intElapsed -= elapsedHours * 60 * 60;

        int elapsedMinutes = intElapsed / 60;
        intElapsed -= elapsedMinutes * 60;

        int elapsedSeconds = intElapsed;

        if (pDuration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(pDuration.toSeconds());

            int durationHours = intDuration / (60 * 60);
            intDuration -= durationHours * 60 * 60;

            int durationMinutes = intDuration / 60;
            intDuration -= durationMinutes * 60;

            int durationSeconds = intDuration;

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }

    public static String formatTimeVLCJ(long pElapsed, long pDuration) {
        long elapsedHours = TimeUnit.MILLISECONDS.toHours(pElapsed);
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(pElapsed)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(pElapsed));
        long elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(pElapsed)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pElapsed));

        if (pDuration > 0) {
            long durationHours = TimeUnit.MILLISECONDS.toHours(pDuration);
            long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(pDuration)
                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(pDuration));
            long durationSeconds = TimeUnit.MILLISECONDS.toSeconds(pDuration)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pDuration));

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }

    public static boolean isTrackMatching(Track pTrack, String pFilter) {
        String text = pFilter.toLowerCase();

        if (text.startsWith("artist:")) {
            text = text.replace("artist:", "").trim();
            return pTrack.getArtist().toLowerCase().contains(text);
        } else if (text.startsWith("title:")) {
            text = text.replace("title:", "").trim();
            return pTrack.getTitle().toLowerCase().contains(text);
        } else if (text.startsWith("album:")) {
            text = text.replace("album:", "").trim();
            return pTrack.getAlbum().toLowerCase().contains(text);
        } else {
            if (pTrack.getArtist().toLowerCase().contains(text)) {
                return true;
            } else if (pTrack.getTitle().toLowerCase().contains(text)) {
                return true;
            } else if (pTrack.getAlbum().toLowerCase().contains(text)) {
                return true;
            }
        }

        return false;
    }
}
