package org.coopereisnor.animeApplication.customJavaFXObjects;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

public class ProgressIndicatorBar extends StackPane {
    private final double progress;

    private final ProgressBar bar  = new ProgressBar();
    private final  Label label = new Label();

    public ProgressIndicatorBar(double progress) {
        this.progress = progress;

        setValues();
        bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.
        bar.setPrefHeight(35); // this will cause problems if you set the height > value, but I'll cross that bridge if I ever find myself there.
        getChildren().setAll(bar, label);
    }

    // synchronizes the progress indicated with the work done.
    private void setValues() {
        label.setText(((int)(progress*100)) +"%");
        label.setId("progressLabel");
        bar.setProgress(progress);
    }
}
