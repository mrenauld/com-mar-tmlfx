package com.mar.tmlfx.view;

import java.io.IOException;
import java.util.List;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.model.Track;
import com.mar.tmlfx.view.controller.RootController;
import com.mar.tmlfx.view.controller.TMLOverviewController;
import com.mar.tmlfx.view.controller.TagEditDialogController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TmlFxView extends Application {

    private static TMLModel model;

    public static void setModel(TMLModel pModel) {
        model = pModel;
    }

    private Stage primaryStage;

    private BorderPane rootLayout;

    private RootController rootController;

    private TMLOverviewController overviewController;

    public TMLModel getModel() {
        return model;
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public boolean showTagEditDialog(List<Track> pTrackList) {
        String fxml = "controller/TagEditDialog.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TmlFxView.class.getResource(fxml));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit tag");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            TagEditDialogController controller = loader.getController();
            controller.setTrackList(pTrackList);
            controller.setStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isUpdateClicked();
        } catch (IOException e) {
            LogUtils.logError(this, "IOException while opening [" + fxml + "]", e);
            return false;
        }
    }

    @Override
    public void start(Stage pPrimaryStage) throws Exception {
        primaryStage = pPrimaryStage;
        primaryStage.setTitle("TML");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                overviewController.quit();
                model.quit();
                Platform.exit();
                System.exit(0);
            }
        });

        initRootLayout();
        showOverview();
    }

    private void initRootLayout() {
        String fxml = "controller/RootLayout.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TmlFxView.class.getResource(fxml));
            rootLayout = (BorderPane) loader.load();

            rootController = loader.getController();
            rootController.setView(this);
            rootController.setModel(model);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            LogUtils.logError(this, "IOException while opening [" + fxml + "]", e);
        }
    }

    private void showOverview() {
        String fxml = "controller/TMLOverview.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TmlFxView.class.getResource(fxml));
            AnchorPane overview = (AnchorPane) loader.load();

            rootLayout.setCenter(overview);

            // Give the controller access to the main app.
            overviewController = loader.getController();
            overviewController.setView(this);
            overviewController.setModel(model);
            overviewController.init();
        } catch (IOException e) {
            LogUtils.logError(this, "IOException while opening [" + fxml + "]", e);
        }
    }

}
