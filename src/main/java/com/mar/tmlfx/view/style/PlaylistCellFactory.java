package com.mar.tmlfx.view.style;

import com.mar.tmlfx.model.Track;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PlaylistCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private static int current = 0;

    public static void setCurrent(int pIdx) {
        current = pIdx;
    }

    public PlaylistCellFactory() {

    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> pParam) {
        TableCell<S, T> cell = new TableCell<S, T>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                // CSS Styles
                String selected = "selected";
                String cssStyle = "";

                Track track = null;
                if (getTableRow() != null) {
                    track = (Track) getTableRow().getItem();
                }

                // Remove all previously assigned CSS styles from the cell.
                getStyleClass().remove(selected);

                super.updateItem((T) item, empty);

                // Determine how to format the cell based on the status of the
                // container.
                if (track == null) {

                } else if (track.getPlayIdx() == current) {
                    cssStyle = selected;
                } else {

                }

                // Set the CSS style on the cell and set the cell's text.
                getStyleClass().add(cssStyle);
                if (item != null) {
                    setText(item.toString());
                } else {
                    setText("");
                }
            }
        };
        return cell;
    }
}
