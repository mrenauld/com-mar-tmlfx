package com.mar.tmlfx.view.controller;

import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.view.TmlFxView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootController {

    private TmlFxView view;

    private TMLModel model;

    @FXML
    private MenuItem fileMenuItemRefreshLibrary;

    @FXML
    private MenuItem fileMenuItemQuit;

    public void setModel(TMLModel pModel) {
        model = pModel;
    }

    public void setView(TmlFxView pView) {
        view = pView;
    }

    @FXML
    private void handleFileMenuMenu(ActionEvent pActionEvent) {
        MenuItem menuItem = (MenuItem) pActionEvent.getTarget();
        if (fileMenuItemRefreshLibrary.equals(menuItem)) {
            model.refreshLibrary();
        } else if (fileMenuItemQuit.equals(menuItem)) {
            view.getPrimaryStage().close();
        }
    }

}
