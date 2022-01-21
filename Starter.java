// JavaFX starter class
// Liam Keliher, 2018
//
// Example of a few GUI components and preliminary interactivity.


import javafx.application.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.event.*;

public class Starter extends Application{
	private static final int SIZE = 300;

	private BorderPane bPane;
	private Button b1, b2, b3, b4;
	private Button swap, flip, blur, rotate, deft;
	private VBox vBox;
	private ToolBar toolbar;
	private WritableImage wImage;
	private PixelWriter writer;
	private boolean isBlack = true;
	//--------------------------------------------------------------------
	public void start(Stage stage) {

		createAndArrangeComponents();		
		addHandlers();

		// Scene scene = new Scene(root, 600, 300, Color.DARKGOLDENROD);
		Scene scene = new Scene(bPane);
		stage.setScene(scene);
		stage.setTitle("My First JavaFX Application");
		stage.show();
	} // start(Stage)
	//--------------------------------------------------------------------
	private void createAndArrangeComponents() {
		bPane = new BorderPane();

		vBox = new VBox();
		b1 = new Button("Button #1");
		b2 = new Button("Button #2");
		b3 = new Button("Button #3");
		b4 = new Button("Button #4");
		vBox.getChildren().addAll(b1, b2, b3, b4);

		swap = new Button("Swap");
		flip = new Button("Flip");
		blur = new Button("Blur");
		rotate = new Button("Rotate");
		deft = new Button("Default");
		toolbar = new ToolBar(swap, flip, blur, rotate, deft);

		wImage = new WritableImage(SIZE, SIZE);
		writer = wImage.getPixelWriter();
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				writer.setColor(x,  y,  (isBlack ? Color.BLACK : Color.RED));
			} // for y
		} // for x
		ImageView iView = new ImageView(wImage);

		bPane.setLeft(vBox);
		bPane.setTop(toolbar);
		bPane.setCenter(iView);
	} // createAndArrangeComponents()
	//--------------------------------------------------------------------
	private void addHandlers() {

		swap.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				for (int x = 0; x < SIZE; x++) {
					for (int y = 0; y < SIZE; y++) {
						if (isBlack) {						
							writer.setColor(x,  y,  Color.RED);
						} // if
						else {
							writer.setColor(x,  y,  Color.BLACK);
						} // else
					} // for y
				} // for x
				isBlack = !isBlack;
			} // handle(ActionEvent)
		});

	} // addHandlers()
	//--------------------------------------------------------------------
	public static void main(String[] args) {
		launch(args);
	} // main(String[])
	//--------------------------------------------------------------------	
} // class Starter
