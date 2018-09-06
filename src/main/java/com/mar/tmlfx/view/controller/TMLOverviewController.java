package com.mar.tmlfx.view.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.mar.framework.core.logging.LogUtils;
import com.mar.iotools.string.FilepathUtils;
import com.mar.tmlfx.Settings;
import com.mar.tmlfx.model.Playlist;
import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.model.Track;
import com.mar.tmlfx.view.TmlFxView;
import com.mar.tmlfx.view.ViewUtils;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class TMLOverviewController {

    @FXML
    private PlayerControllerVLCJ playerController;

    @FXML
    private TextField libraryFilterField;

    @FXML
    private TableView<Playlist> playlistListTable;

    @FXML
    private TableColumn<Playlist, String> playlistListColumnTitle;

    @FXML
    private TableView<Track> libraryTable;

    @FXML
    private TableColumn<Track, String> libraryColumnArtist;

    @FXML
    private TableColumn<Track, String> libraryColumnTitle;

    @FXML
    private TableColumn<Track, String> libraryColumnAlbum;

    @FXML
    private ContextMenu libraryContextMenu;

    @FXML
    private MenuItem libraryContextMenuItemAdd;

    @FXML
    private MenuItem libraryContextMenuItemEditTag;

    @FXML
    private MenuItem libraryContextMenuItemOpenTrackLocation;

    @FXML
    private TableView<Track> playlistTable;

    @FXML
    private TableColumn<Track, String> playlistColumnArtist;

    @FXML
    private TableColumn<Track, String> playlistColumnTitle;

    @FXML
    private TableColumn<Track, Number> playlistColumnPlayIdx;

    @FXML
    private ContextMenu playlistContextMenu;

    @FXML
    private MenuItem playlistContextMenuItemSave;

    @FXML
    private MenuItem playlistContextMenuItemClear;

    @FXML
    private MenuItem playlistContextMenuItemRemove;

    private TmlFxView view;

    private TMLModel model;

    public void init() {
        /*
         * Wrap the library ObservableList in a FilteredList (initially displays
         * all data).
         */
        FilteredList<Track> filteredData = new FilteredList<>(model.getLibraryTrackList(), track -> true);

        /* Set the filter Predicate whenever the filter changes. */
        libraryFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(track -> {

                /* If filter text is empty, display all tracks. */
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                /*
                 * Compare artist, title, etc with the filter text (in lower
                 * case).
                 */
                return ViewUtils.isTrackMatching(track, newValue);
            });
        });

        /* Wrap the FilteredList in a SortedList. */
        SortedList<Track> sortedData = new SortedList<>(filteredData);

        /* Bind the SortedList comparator to the TableView comparator. */
        sortedData.comparatorProperty().bind(libraryTable.comparatorProperty());

        /* Add sorted (and filtered) data to the table. */
        libraryTable.setItems(sortedData);

        /* Add playlist data to the table. */
        playlistTable.setItems(model.getPlaylistTrackList());

        /* Add playlist list to the table. */
        playlistListTable.setItems(model.getSavedPlaylistList());

        /* Sort library columns. */
        libraryTable.getSortOrder().add(libraryColumnArtist);
        libraryTable.getSortOrder().add(libraryColumnAlbum);
        libraryTable.getSortOrder().add(libraryColumnTitle);

        /* Enable multi-selection. */
        libraryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void quit() {
        playerController.quit();
    }

    public void setModel(TMLModel pModel) {
        model = pModel;
        playerController.setModel(pModel);
    }

    public void setView(TmlFxView pView) {
        view = pView;
    }

    @FXML
    private void handleLibraryContextMenu(ActionEvent pActionEvent) {
        MenuItem menuItem = (MenuItem) pActionEvent.getTarget();
        ObservableList<Track> trackList = libraryTable.getSelectionModel().getSelectedItems();
        if (libraryContextMenuItemAdd.equals(menuItem) && trackList.size() > 0) {
            model.addTracksToPlaylist(trackList);
        } else if (libraryContextMenuItemEditTag.equals(menuItem) && trackList.size() > 0) {
            boolean isSaveClicked = view.showTagEditDialog(trackList);
            if (isSaveClicked) {
                model.updateTagList(trackList);
            }
        } else if (libraryContextMenuItemOpenTrackLocation.equals(menuItem) && trackList.size() > 0) {
            /* Open first selected track in explorer. */
            try {
                Track track = trackList.get(0);
                Runtime.getRuntime()
                        .exec("explorer.exe /select," + FilepathUtils.replaceWithFileSeparator(track.getFilename()));
            } catch (IOException e) {
                LogUtils.logError(this, "IOException while showing track in file explorer", e);
            }
        }
    }

    @FXML
    private void handleLibraryDoubleClick(MouseEvent pMouseEvent) {
        if (pMouseEvent.getClickCount() == 2) {
            Track item = libraryTable.getSelectionModel().getSelectedItem();
            if (item != null) {
                ArrayList<Track> list = new ArrayList<Track>();
                list.add(item);
                model.addTracksToPlaylist(list);
                playlistTable.refresh();
            }
        }
    }

    @FXML
    private void handlePlaylistContextMenu(ActionEvent pActionEvent) {
        MenuItem menuItem = (MenuItem) pActionEvent.getTarget();
        ObservableList<Track> trackList = playlistTable.getSelectionModel().getSelectedItems();
        if (playlistContextMenuItemSave.equals(menuItem) && model.getCurrentPLaylist().size() > 0) {
            FileChooser fileChooser = new FileChooser();

            /* Set extension filter. */
            String ext = Settings.getFileExtensionPlaylist();
            String description = ext.toUpperCase() + " files (*." + ext.toLowerCase() + ")";
            String filter = "*." + ext.toLowerCase();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, filter);
            fileChooser.getExtensionFilters().add(extFilter);

            /* Set initial folder. */
            fileChooser.setInitialDirectory(new File(Settings.getFilePlaylistFolder()));

            /* Show save file dialog. */
            File file = fileChooser.showSaveDialog(view.getPrimaryStage());

            if (file != null) {
                model.saveCurrentPlaylist(file.getAbsolutePath());
                playlistTable.refresh();
            }
        } else if (playlistContextMenuItemRemove.equals(menuItem)) {
            model.getCurrentPLaylist().removeTrackList(trackList);
        } else if (playlistContextMenuItemClear.equals(menuItem)) {
            model.getCurrentPLaylist().empty();
        }
    }

    @FXML
    private void handlePlaylistDoubleClick(MouseEvent pMouseEvent) {
        if (pMouseEvent.getClickCount() == 2) {
            Track item = playlistTable.getSelectionModel().getSelectedItem();
            if (item != null) {
                model.playSetCurrent(item);
                playerController.play(item);
            }
        }
    }

    @FXML
    private void handlePlaylistListDoubleClick(MouseEvent pMouseEvent) {
        if (pMouseEvent.getClickCount() == 2) {
            Playlist item = playlistListTable.getSelectionModel().getSelectedItem();
            if (item != null) {
                model.loadPlaylist(item.getFilename());
            }
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        playlistListColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        libraryColumnArtist.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        libraryColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        libraryColumnAlbum.setCellValueFactory(cellData -> cellData.getValue().albumProperty());

        playlistColumnArtist.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        playlistColumnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        playlistColumnPlayIdx.setCellValueFactory(cellData -> cellData.getValue().playIdxProperty());
    }

}
