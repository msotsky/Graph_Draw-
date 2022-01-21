import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.geometry.*;
import javafx.event.*;

public class FirstGUI extends Application {

    private static final String COPYRIGHT_TEXT = " \u00A9 2021, COMP 2631 class";
    private static final Color UNHIGHLIGHTED = Color.BLACK;
    private static final Color HIGHLIGHTED = Color.DARKORANGE;

    private BorderPane root;
    private Scene scene;
    private ImageView iView;
    private Label copyright;

    private MenuBar menuBar;
    private Menu fileMenu;
    private Menu editMenu;
    private Menu toolsMenu;
    private MenuItem open;
    private MenuItem save;
    private MenuItem saveAs;
    private MenuItem exit;

    private VBox vBox;
    private Button button1;
    private Button button2;
    private Button button3;

    private Image[] images = {new Image("images/puppies.bmp"),
                            new Image("images/starwars.bmp"),
                            new Image("images/babyowl.bmp"),
                            new Image("images/candles.bmp")};
    private int currImage = 0;

    //---------------------------------------------------------------

    public void start(Stage stage) throws Exception {
        setUpGUI(stage);
        setUpHandlers(stage);
        stage.setTitle("My First JavaFX Program");
        stage.show();
    }

    //---------------------------------------------------------------

    private void setUpGUI(Stage stage) {
        root = new BorderPane();

        // menu bar in top region
        open = new MenuItem("Open");
        save = new MenuItem("Save");
        saveAs = new MenuItem("Save As...");
        exit = new MenuItem("Exit");
        fileMenu = new Menu("File", null, open, save, saveAs, exit);

        editMenu = new Menu("Edit");
        toolsMenu = new Menu("Tools");

        menuBar = new MenuBar(fileMenu, editMenu, toolsMenu);
        root.setTop(menuBar);

        // image in center region
        iView = new ImageView(images[currImage]);
        root.setCenter(iView);

        // copyright label (centered) in bottom region
        copyright = new Label(COPYRIGHT_TEXT);
        root.setAlignment(copyright, Pos.CENTER);
        root.setBottom(copyright);

        // column of buttons in left region
        button1 = new Button("Click me #1");
        button2 = new Button("Click me #2");
        button3 = new Button("Next Image");
        vBox = new VBox(button1, button2, button3);
        root.setLeft(vBox);

        scene = new Scene(root, Color.PEACHPUFF);

        stage.getIcons().add(new Image("C:/Users/mxm20/Desktop/CSJava/FirstGUI-images/agave-icon-64x64.png"));
        stage.getIcons().add(new Image("C:/Users/mxm20/Desktop/CSJava/FirstGUI-images/agave-icon-48x48.png"));
        stage.getIcons().add(new Image("C:/Users/mxm20/Desktop/CSJava/FirstGUI-images/agave-icon-32x32.png"));
        
        stage.setScene(scene);
    }

    //---------------------------------------------------------------

    private void setUpHandlers(Stage stage) {
        button3.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e) {
                currImage++;
                if (currImage == images.length) {
                    currImage = 0;
                }
                iView.setImage(images[currImage]);
            }
        });

        copyright.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e) {
                copyright.setTextFill(HIGHLIGHTED);
            }
        });

        copyright.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e) {
                copyright.setTextFill(UNHIGHLIGHTED);
            }
        });

    }

    //---------------------------------------------------------------

    public static void main(String[] args) {
        launch();  // same as Application.launch();
    }

    //---------------------------------------------------------------

} // class FirstGUI
