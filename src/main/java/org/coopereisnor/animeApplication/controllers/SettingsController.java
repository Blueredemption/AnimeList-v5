package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.settingsDao.Settings;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

public class SettingsController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;

    // color pickers
    @FXML
    private ColorPicker backgroundColorThreePicker;
    @FXML
    private ColorPicker backgroundColorTwoPicker;
    @FXML
    private ColorPicker backgroundColorOnePicker;
    @FXML
    private ColorPicker borderColorPicker;
    @FXML
    private ColorPicker buttonColorPicker;
    @FXML
    private ColorPicker buttonHoveredColorPicker;
    @FXML
    private ColorPicker buttonPressedColorPicker;
    @FXML
    private ColorPicker rejectionButtonColorPicker;
    @FXML
    private ColorPicker rejectionButtonHoveredColorPicker;
    @FXML
    private ColorPicker rejectionButtonPressedColorPicker;
    @FXML
    private ColorPicker rejectionButtonBorderColorPicker;
    @FXML
    private ColorPicker acceptanceButtonColorPicker;
    @FXML
    private ColorPicker acceptanceButtonPressedColorPicker;
    @FXML
    private ColorPicker acceptanceButtonHoveredColorPicker;
    @FXML
    private ColorPicker acceptanceButtonBorderColorPicker;
    @FXML
    private ColorPicker neutralButtonColorPicker;
    @FXML
    private ColorPicker neutralButtonHoveredColorPicker;
    @FXML
    private ColorPicker neutralButtonPressedColorPicker;
    @FXML
    private ColorPicker neutralButtonBorderColorPicker;
    @FXML
    private ColorPicker textColorPicker;
    @FXML
    private ColorPicker textAreaColorPicker;
    @FXML
    private ColorPicker progressBarEmptyColorPicker;
    @FXML
    private ColorPicker progressBarFullColorPicker;

    // default buttons
    @FXML
    private Button backgroundColorThreeDefault;
    @FXML
    private Button backgroundColorTwoDefault;
    @FXML
    private Button backgroundColorOneDefault;
    @FXML
    private Button borderColorDefault;
    @FXML
    private Button buttonColorDefault;
    @FXML
    private Button buttonHoveredColorDefault;
    @FXML
    private Button buttonPressedColorDefault;
    @FXML
    private Button rejectionButtonColorDefault;
    @FXML
    private Button rejectionButtonHoveredColorDefault;
    @FXML
    private Button rejectionButtonPressedColorDefault;
    @FXML
    private Button rejectionButtonBorderColorDefault;
    @FXML
    private Button acceptanceButtonColorDefault;
    @FXML
    private Button acceptanceButtonHoveredColorDefault;
    @FXML
    private Button acceptanceButtonPressedColorDefault;
    @FXML
    private Button acceptanceButtonBorderColorDefault;
    @FXML
    private Button neutralButtonColorDefault;
    @FXML
    private Button neutralButtonHoveredColorDefault;
    @FXML
    private Button neutralButtonPressedColorDefault;
    @FXML
    private Button neutralButtonBorderColorDefault;
    @FXML
    private Button textColorDefault;
    @FXML
    private Button textAreaColorDefault;
    @FXML
    private Button progressBarEmptyColorDefault;
    @FXML
    private Button progressBarFullColorDefault;
    @FXML
    private Button listStateSave;
    @FXML
    private Button timelineStateSave;


    // other
    @FXML
    private ToggleButton trackingToggleButton;


    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);

        simpleModifications();

        setColorValues();
        setColorActions();

        configureTracking();
        configureListDefaults();
        configureTimelineDefaults();
    }

    private void simpleModifications() {
        scrollPane.setId("background");
    }

    private void setColorValues(){
        backgroundColorThreePicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getBackgroundColorThree()));
        backgroundColorTwoPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getBackgroundColorTwo()));
        backgroundColorOnePicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getBackgroundColorOne()));
        borderColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getBorderColor()));
        buttonColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getButtonColor()));
        buttonHoveredColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getButtonColorHovered()));
        buttonPressedColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getButtonColorPressed()));
        rejectionButtonColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getRejectionColor()));
        rejectionButtonHoveredColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getRejectionColorHovered()));
        rejectionButtonPressedColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getRejectionColorPressed()));
        rejectionButtonBorderColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getRejectionColorBorder()));
        acceptanceButtonColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getAcceptanceColor()));
        acceptanceButtonHoveredColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getAcceptanceColorHovered()));
        acceptanceButtonPressedColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getAcceptanceColorPressed()));
        acceptanceButtonBorderColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getAcceptanceColorBorder()));
        neutralButtonColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getNeutralColor()));
        neutralButtonHoveredColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getNeutralColorHovered()));
        neutralButtonPressedColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getNeutralColorPressed()));
        neutralButtonBorderColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getNeutralColorBorder()));
        textColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getTextColor()));
        textAreaColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getTextAreaColor()));
        progressBarEmptyColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getProgressBarColor()));
        progressBarFullColorPicker.setValue(UtilityMethods.convertColor(settingsDao.getSettings().getProgressBarColorFull()));
    }

    private void setColorActions(){
        // color pickers
        backgroundColorThreePicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorThree(UtilityMethods.convertColor(backgroundColorThreePicker.getValue()));
            saveFunction();
        });
        backgroundColorTwoPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorTwo(UtilityMethods.convertColor(backgroundColorTwoPicker.getValue()));
            saveFunction();
        });
        backgroundColorOnePicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorOne(UtilityMethods.convertColor(backgroundColorOnePicker.getValue()));
            saveFunction();
        });
        borderColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBorderColor(UtilityMethods.convertColor(borderColorPicker.getValue()));
            saveFunction();
        });
        buttonColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColor(UtilityMethods.convertColor(buttonColorPicker.getValue()));
            saveFunction();
        });
        buttonHoveredColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColorHovered(UtilityMethods.convertColor(buttonHoveredColorPicker.getValue()));
            saveFunction();
        });
        buttonPressedColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColorPressed(UtilityMethods.convertColor(buttonPressedColorPicker.getValue()));
            saveFunction();
        });
        rejectionButtonColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColor(UtilityMethods.convertColor(rejectionButtonColorPicker.getValue()));
            saveFunction();
        });
        rejectionButtonHoveredColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorHovered(UtilityMethods.convertColor(rejectionButtonHoveredColorPicker.getValue()));
            saveFunction();
        });
        rejectionButtonPressedColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorPressed(UtilityMethods.convertColor(rejectionButtonPressedColorPicker.getValue()));
            saveFunction();
        });
        rejectionButtonBorderColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorBorder(UtilityMethods.convertColor(rejectionButtonBorderColorPicker.getValue()));
            saveFunction();
        });
        acceptanceButtonColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColor(UtilityMethods.convertColor(acceptanceButtonColorPicker.getValue()));
            saveFunction();
        });
        acceptanceButtonHoveredColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorHovered(UtilityMethods.convertColor(acceptanceButtonHoveredColorPicker.getValue()));
            saveFunction();
        });
        acceptanceButtonPressedColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorPressed(UtilityMethods.convertColor(acceptanceButtonPressedColorPicker.getValue()));
            saveFunction();
        });
        acceptanceButtonBorderColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorBorder(UtilityMethods.convertColor(acceptanceButtonBorderColorPicker.getValue()));
            saveFunction();
        });
        neutralButtonColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColor(UtilityMethods.convertColor(neutralButtonColorPicker.getValue()));
            saveFunction();
        });
        neutralButtonHoveredColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorHovered(UtilityMethods.convertColor(neutralButtonHoveredColorPicker.getValue()));
            saveFunction();
        });
        neutralButtonPressedColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorPressed(UtilityMethods.convertColor(neutralButtonPressedColorPicker.getValue()));
            saveFunction();
        });
        neutralButtonBorderColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorBorder(UtilityMethods.convertColor(neutralButtonBorderColorPicker.getValue()));
            saveFunction();
        });
        textColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setTextColor(UtilityMethods.convertColor(textColorPicker.getValue()));
            saveFunction();
        });
        textAreaColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setTextAreaColor(UtilityMethods.convertColor(textAreaColorPicker.getValue()));
            saveFunction();
        });
        progressBarEmptyColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setProgressBarColor(UtilityMethods.convertColor(progressBarEmptyColorPicker.getValue()));
            saveFunction();
        });
        progressBarFullColorPicker.setOnAction(actionEvent -> {
            settingsDao.getSettings().setProgressBarColorFull(UtilityMethods.convertColor(progressBarFullColorPicker.getValue()));
            saveFunction();
        });

        // default buttons
        backgroundColorThreeDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorThree(Settings.defaultBackgroundColorThree);
            saveFunction();
        });
        backgroundColorTwoDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorTwo(Settings.defaultBackgroundColorTwo);
            saveFunction();
        });
        backgroundColorOneDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBackgroundColorOne(Settings.defaultBackgroundColorOne);
            saveFunction();
        });
        borderColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setBorderColor(Settings.defaultBorderColor);
            saveFunction();
        });
        buttonColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColor(Settings.defaultButtonColor);
            saveFunction();
        });
        buttonHoveredColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColorHovered(Settings.defaultButtonColorHovered);
            saveFunction();
        });
        buttonPressedColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setButtonColorPressed(Settings.defaultButtonColorPressed);
            saveFunction();
        });
        rejectionButtonColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColor(Settings.defaultRejectionColor);
            saveFunction();
        });
        rejectionButtonHoveredColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorHovered(Settings.defaultRejectionColorHovered);
            saveFunction();
        });
        rejectionButtonPressedColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorPressed(Settings.defaultRejectionColorPressed);
            saveFunction();
        });
        rejectionButtonBorderColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setRejectionColorBorder(Settings.defaultRejectionColorBorder);
            saveFunction();
        });
        acceptanceButtonColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColor(Settings.defaultAcceptanceColor);
            saveFunction();
        });
        acceptanceButtonHoveredColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorHovered(Settings.defaultAcceptanceColorHovered);
            saveFunction();
        });
        acceptanceButtonPressedColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorPressed(Settings.defaultAcceptanceColorPressed);
            saveFunction();
        });
        acceptanceButtonBorderColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setAcceptanceColorBorder(Settings.defaultAcceptanceColorBorder);
            saveFunction();
        });
        neutralButtonColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColor(Settings.defaultNeutralColor);
            saveFunction();
        });
        neutralButtonHoveredColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorHovered(Settings.defaultNeutralColorHovered);
            saveFunction();
        });
        neutralButtonPressedColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorPressed(Settings.defaultNeutralColorPressed);
            saveFunction();
        });
        neutralButtonBorderColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setNeutralColorBorder(Settings.defaultNeutralColorBorder);
            saveFunction();
        });
        textColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setTextColor(Settings.defaultTextColor);
            saveFunction();
        });
        textAreaColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setTextAreaColor(Settings.defaultTextAreaColor);
            saveFunction();
        });
        progressBarEmptyColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setProgressBarColor(Settings.defaultProgressBarColor);
            saveFunction();
        });
        progressBarFullColorDefault.setOnAction(actionEvent -> {
            settingsDao.getSettings().setProgressBarColorFull(Settings.defaultProgressBarColorFull);
            saveFunction();
        });
    }

    private void saveFunction(){
        settingsDao.writeCSSFile();
        settingsDao.save();
        application.changeScene("settings.fxml");
    }

    private void configureTracking(){
        trackingToggleButton.setSelected(settingsDao.getSettings().isTracking());
        trackingToggleButton.setText(settingsDao.getSettings().isTracking() ? "On" : "Off");
        trackingToggleButton.setId("toggleButtonColored");
        trackingToggleButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(settingsDao.getSettings().isTracking() != newValue){
                settingsDao.getSettings().setTracking(newValue);
                trackingToggleButton.setText(newValue ? "On" : "Off");
                settingsDao.save();
            }
        }));
    }

    private void configureListDefaults(){
        listStateSave.setOnAction(actionEvent -> {
            settingsDao.getSettings().setDefaultDirectionState(singletonDao.getListContainer().getOrder());
            settingsDao.getSettings().setDefaultDataTypeState(singletonDao.getType());
            settingsDao.getSettings().setDefaultAttributeState(singletonDao.getListContainer().getSortBy());
            settingsDao.getSettings().setDefaultListState(singletonDao.getListFXML());
            settingsDao.save();
        });
    }

    private void configureTimelineDefaults(){
        timelineStateSave.setOnAction(actionEvent -> {
            settingsDao.getSettings().setDefaultAggregateState(singletonDao.getListContainer().getAggregate());
            settingsDao.getSettings().setDefaultTypeState(singletonDao.getListContainer().isType());
            settingsDao.save();
        });
    }
}
