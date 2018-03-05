package com.mar.tmlfx;

import com.mar.framework.core.logging.LogUtils;
import com.mar.tmlfx.model.TMLModel;
import com.mar.tmlfx.view.TmlFxView;
import com.sun.jna.NativeLibrary;

import javafx.application.Application;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class TmlFx {

    public static void main(String[] args) {

        LogUtils.setLevel(LogUtils.LEVEL_INFO);

        /* Important. */
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), Settings.getPathVlc());

        TMLModel model = new TMLModel();
        model.initialize();

        TmlFxView.setModel(model);
        Application.launch(TmlFxView.class);
    }

}
