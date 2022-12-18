package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeApplication.singleton.StatisticsContainer;
import org.coopereisnor.manipulation.ConfigurePlots;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.YearSet;
import org.coopereisnor.utility.UtilityMethods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimelineController implements Controller{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final Application application = singletonDao.getApplication();
    private final StatisticsContainer statisticsContainer = singletonDao.getStatisticsContainer();

    private StackedBarChart<String, Number> stackedBarChart;
    int activeYearSetIndex;

    @FXML
    private GridPane gridPane;
    @FXML
    private VBox chartHolderVBox;
    @FXML
    private Button leftYearButton;
    @FXML
    private Button rightYearButton;
    @FXML
    private Label yearLabel;
    @FXML
    private ComboBox<String> scaleComboBox;
    @FXML
    private ToggleButton typeToggleButton;
    @FXML
    private Label hoverLabel;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());

        ArrayList<YearSet> yearSets = statisticsContainer.getConfigurePlots().getYearSets();
        activeYearSetIndex = yearSets.isEmpty() ? -1 : yearSets.size() - 1;

        loadFXMLActions();
        updateYearControls();
        loadData();
    }

    private void loadFXMLActions(){
        leftYearButton.setOnAction(actionEvent -> {
            activeYearSetIndex -= 1;
            updateYearControls();
            loadData();
        });


        rightYearButton.setOnAction(actionEvent -> {
            activeYearSetIndex += 1;
            updateYearControls();
            loadData();
        });

        scaleComboBox.setItems(FXCollections.observableArrayList(List.of("Month", "Week", "Day")));
        scaleComboBox.getSelectionModel().select(singletonDao.getListContainer().getAggregate());
        scaleComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            singletonDao.getListContainer().setAggregate(newValue);
            loadData();
        });

        typeToggleButton.setText(singletonDao.getListContainer().isType() ? "Anime" : "Occurrence");
        typeToggleButton.setSelected(singletonDao.getListContainer().isType());
        typeToggleButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                typeToggleButton.setText("Anime");
            } else{
                typeToggleButton.setText("Occurrence");
            }
            singletonDao.getListContainer().setType(newValue);
            loadData();
        }));
    }

    private void updateYearControls(){
        ArrayList<YearSet> yearSets = statisticsContainer.getConfigurePlots().getYearSets();
        yearLabel.setText(yearSets.isEmpty() ? LocalDate.now().getYear() +"" : yearSets.get(activeYearSetIndex).getYear() +"");
        leftYearButton.setText(yearSets.isEmpty() ? "" : (yearSets.get(activeYearSetIndex).getYear() - 1) +"");
        leftYearButton.setDisable(yearSets.isEmpty() || activeYearSetIndex == 0);
        rightYearButton.setText(yearSets.isEmpty() ? "" : (yearSets.get(activeYearSetIndex).getYear() + 1) +"");
        rightYearButton.setDisable(yearSets.isEmpty() || activeYearSetIndex == yearSets.size() - 1);
    }

    // this acts like a (re)load function.
    private void loadData(){
        // I have to remake the chart every time or there is an unfixable repaint issue with the X-axis label
        // sometimes I actually miss swing, and not having revalidate() or repaint() calls to toy with is problematic here :(
        setupChart();

        // remove old data if it exists
        stackedBarChart.getData().clear();

        // retrieve new data
        ArrayList<YearSet> yearSets = statisticsContainer.getConfigurePlots().getYearSets();

        if(!yearSets.isEmpty()){
            List<String> list = List.of();
            ArrayList<XYChart.Series<String, Number>> aggregate = new ArrayList<>();

            if(scaleComboBox.getSelectionModel().getSelectedItem().equals("Month")){
                aggregate = yearSets.get(activeYearSetIndex).getAggregateOfSeriesByMonth();
                list = Arrays.asList(ConfigurePlots.MONTH_CATEGORIES);
            }else if(scaleComboBox.getSelectionModel().getSelectedItem().equals("Week")){
                aggregate = yearSets.get(activeYearSetIndex).getAggregateOfSeriesByWeek();
                list = Arrays.asList(ConfigurePlots.WEEK_CATEGORIES);
            }else if(scaleComboBox.getSelectionModel().getSelectedItem().equals("Day")){
                aggregate = yearSets.get(activeYearSetIndex).getAggregateOfSeriesByDay();
                list = Arrays.asList(ConfigurePlots.DAY_CATEGORIES);
            }

            // apply the selected category
            ((CategoryAxis)stackedBarChart.getXAxis()).setCategories(FXCollections.observableArrayList(list));

            for(XYChart.Series<String, Number> series : aggregate){
                // add data
                stackedBarChart.getData().add(series);

                // apply colors and hover effects
                for(XYChart.Data<String, Number> data : series.getData()){
                    String styleString = "-fx-background-color: "
                            +(typeToggleButton.isSelected() ?
                            UtilityMethods.getStringOfColor(((Pair)data.getExtraValue()).anime().getFocusedOccurrence().getImageAverageColor()) :
                            UtilityMethods.getStringOfColor(((Pair)data.getExtraValue()).occurrence().getImageAverageColor()))
                            +";";
                    data.getNode().setStyle(styleString);
                    data.getNode().setOnMouseEntered(event -> {
                        data.getNode().getStyleClass().add("onHover");
                        hoverLabel.setText(typeToggleButton.isSelected() ?
                                ((Pair)data.getExtraValue()).anime().getName() :
                                ((Pair)data.getExtraValue()).occurrence().getName());
                    });
                    data.getNode().setOnMouseExited(event -> {
                        data.getNode().getStyleClass().remove("onHover");
                        hoverLabel.setText("");
                    });
                    data.getNode().setOnMouseClicked(mouseEvent -> {
                        if(typeToggleButton.isSelected()){
                            singletonDao.setCurrentAnime(((Pair)data.getExtraValue()).anime(), null);
                        }else{
                            singletonDao.setCurrentAnime(((Pair)data.getExtraValue()).anime(), ((Pair)data.getExtraValue()).occurrence());
                        }
                        application.changeScene("anime.fxml");
                    });
                }
            }
        }
    }

    private void setupChart(){
        chartHolderVBox.getChildren().remove(stackedBarChart);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Episodes Watched");

        stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.setVerticalGridLinesVisible(false);
        stackedBarChart.setHorizontalGridLinesVisible(false);
        stackedBarChart.setAnimated(false);
        stackedBarChart.setLegendVisible(false);
        stackedBarChart.setCategoryGap(1);

        VBox.setVgrow(stackedBarChart, Priority.ALWAYS);
        chartHolderVBox.getChildren().add(stackedBarChart);
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
