/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectilemotion;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author sbeli
 */
public class ProjectileMotion extends Application {
    
    private final String[] planetNames = {"Mercury", "Venus", "Earth", "Mars",
        "Jupiter", "Saturn", "Uranus", "Neptune"
    };
            
    private static Label angleUnits;
    private static Label distanceUnits;
    private Label flightTimeResultLabel;
    private Label distanceResultLabel;
    private Label maxHeightResultLabel;
    private Label errorMsg;
    
    // Text Fields
    private TextField velocityTF;
    private TextField angleTF;
    
    Button distanceUnitsButton;
    Button angleUnitsButton;
    
    private static int currentPlanet;
    
    private static boolean metric;
    private static boolean radians;

    /**
     * main method only calls launch()
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        // Sets current planet to earth, metric to true, and radians to true
        initStartingValues();
        
        // Labels
        Label currentSettings = new Label("Current Settings:");
        
        Label velocityLabel = new Label("Initial velocity:");
        Label angleLabel = new Label("Launch angle:");
        Label flightTimeLabel = new Label("Time of flight:");
        Label distanceLabel = new Label("Distance:");
        Label maxHeightLabel = new Label("Maximum height:");
        
        flightTimeResultLabel = new Label();
        distanceResultLabel = new Label();
        maxHeightResultLabel = new Label();
        
        Label currentPlanetLabel = new Label("Current Planet");
        Label planetLabel = new Label(planetNames[currentPlanet]);
        
        errorMsg = new Label();
        
        // Images
        Image[] planetImages = new Image[8];
        loadPlanetImages(planetImages);
        
        ImageView currentPlanetImage = new ImageView();
        currentPlanetImage.setImage(planetImages[currentPlanet]);
        currentPlanetImage.setFitWidth(60);
        currentPlanetImage.setPreserveRatio(true);
        
        // Text Fields
        velocityTF = new TextField();
        angleTF = new TextField();
        
        // Buttons
        distanceUnitsButton = new Button("Metric / Imperial");   // Initially meters/second
        distanceUnitsButton.setOnAction(new distanceUnitHandler());
        
        angleUnitsButton = new Button("Radians / Degrees");            // Initially radians
        angleUnitsButton.setOnAction(new angleUnitHandler());
        
        Button planetButton = new Button("Change Planet");          // Initially earth
        planetButton.setOnAction(event -> {
            ++currentPlanet;
            currentPlanet %= 8;
            currentPlanetImage.setImage(planetImages[currentPlanet]);
            planetLabel.setText(planetNames[currentPlanet]);
        });
        
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(new CalcHandler());
        
        // Layout containers
        BorderPane root = new BorderPane();
        
        HBox topHbox = new HBox(15, distanceUnitsButton, angleUnitsButton, planetButton);
        topHbox.setAlignment(Pos.CENTER);
        topHbox.setPadding(new Insets(10));
        
        VBox leftVbox = new VBox(5, currentSettings, distanceUnits, angleUnits);
        leftVbox.setPadding(new Insets(20));
        
        VBox rightVbox = new VBox(15, currentPlanetLabel, currentPlanetImage, planetLabel);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPadding(new Insets(10));
        
        HBox bottomHbox = new HBox(errorMsg);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(10));
        
        GridPane inputPane = new GridPane();
        inputPane.add(velocityLabel, 0, 0);
        inputPane.add(velocityTF, 1, 0);
        inputPane.add(angleLabel, 0, 1);
        inputPane.add(angleTF, 1, 1);
        inputPane.setHgap(20);
        inputPane.setVgap(5);
        
        HBox calcBox = new HBox(calculateButton);
        calcBox.setAlignment(Pos.CENTER);
        
        GridPane resultPane = new GridPane();
        resultPane.add(flightTimeLabel, 0, 0);
        resultPane.add(flightTimeResultLabel, 1, 0);
        resultPane.add(distanceLabel, 0, 1);
        resultPane.add(distanceResultLabel, 1, 1);
        resultPane.add(maxHeightLabel, 0, 2);
        resultPane.add(maxHeightResultLabel, 1, 2);
        
        resultPane.setHgap(20);
        resultPane.setVgap(5);
        
        VBox centerPane = new VBox(8, inputPane, calcBox, resultPane);
        
        root.setCenter(centerPane);
        root.setLeft(leftVbox);
        root.setRight(rightVbox);
        root.setTop(topHbox);
        root.setBottom(bottomHbox);
        
        // Set Scene
        Scene scene = new Scene(root, 500, 250);
        
        // Set Stage
        stage.setScene(scene);
        stage.setTitle("Projectile Motion Calculator");
        stage.getIcons().add(new Image("file:icon/catapult.png"));
        
        stage.show();
        
    } 
    
    public static void initStartingValues() {
        currentPlanet = 2;
        metric = true;
        radians = true;
        distanceUnits = new Label("Metric");
        angleUnits = new Label("Radians");
    }
    
    /**
     * Currently not using this function
     * @param images an (empty) array of ImageViews
     */
    public static void loadPlanetImages(Image[] images) {
        images[0] = new Image("file:planets/mercury.jpg");
        images[1] = new Image("file:planets/venus.jpg");
        images[2] = new Image("file:planets/earth.jpg");
        images[3] = new Image("file:planets/mars.jpg");
        images[4] = new Image("file:planets/jupiter.jpg");
        images[5] = new Image("file:planets/saturn.jpg");
        images[6] = new Image("file:planets/uranus.png");
        images[7] = new Image("file:planets/neptune.png");
    }
    
    /**
     * this class handles the calculations. It creates a MotionCalculator
     * object, sets the initial values based on user input, and sets result
     * labels. If unexpected input is received, an error message is displayed
     * and the text fields are cleared.
     */
    class CalcHandler implements EventHandler {
        @Override
        public void handle(Event event) {
            errorMsg.setText("");
            try {
                double initialV = Double.parseDouble(velocityTF.getText());
                double angle = Double.parseDouble(angleTF.getText());
                
                MotionCalculator mc = new MotionCalculator();
                mc.setMetric(metric);
                mc.setRadians(radians);
                mc.setInitialVelocity(initialV);
                mc.setLaunchAngle(angle);
                mc.setCurrentPlanet(currentPlanet);
                
                flightTimeResultLabel.setText(String.format("%,.3f", mc.calculateTime()));
                distanceResultLabel.setText(String.format("%,.3f", mc.calculateDisplacement()));
                maxHeightResultLabel.setText(String.format("%,.3f", mc.calculateMaxHeight()));
                
            }
            catch(Exception e) {
                errorMsg.setText("Invalid Input. Try entering some numbers.");
                velocityTF.clear();
                angleTF.clear();
            }
        }
    }
    
    class distanceUnitHandler implements EventHandler {
        @Override
        public void handle(Event e) {
            if(metric) {
                metric = false;
                distanceUnits.setText("Imperial");
            }
            else {
                metric = true;
                distanceUnits.setText("Metric");
            }
        }
    }
    
    class angleUnitHandler implements EventHandler {
    @Override
    public void handle(Event e) {
        if(radians) {
            radians = false;
            angleUnits.setText("Degrees");
        }
        else {
            radians = true;
            angleUnits.setText("Radians");
        }
    }
}
}
