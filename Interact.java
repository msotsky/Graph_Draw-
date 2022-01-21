// Liam Keliher, 2017
// Simple JavaFX application to demonstrate interactivity, and also
// to demonstrate that JavaFX remembers which geometric objects are
// on the screen and in which order.
//
// Clicking on the bottom circle changes its color.  This would be
// more difficult to do with a regular drawing surface.


import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.event.*;

public class Interact extends Application{
	private BorderPane root;
	private boolean flip;
	private Circle c1, c2;
	private static final Color TOP_CIRC_COLOR = Color.rgb(25, 75, 255);
	private static final Color BOT_CIRC_COLOR_1 = Color.RED;
	private static final Color BOT_CIRC_COLOR_2 = Color.PLUM;
	//--------------------------------------------------------------------
	public void start(Stage stage) throws Exception{
		root = new BorderPane();
		c1 = new Circle(100, 200, 50);
		c1.setFill(BOT_CIRC_COLOR_1);
		c2 = new Circle(130, 230, 50);
		c2.setFill(TOP_CIRC_COLOR);
		root.getChildren().addAll(c1, c2);

		flip = true;
		c1.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				if(flip)
					c1.setFill(BOT_CIRC_COLOR_2);
				else
					c1.setFill(BOT_CIRC_COLOR_1);
				flip = !flip;
			}
		});

		Scene scene = new Scene(root, 500, 500, Color.BLANCHEDALMOND);
		stage.setScene(scene);
		stage.show();
	} // start(String[]
	//--------------------------------------------------------------------
	public static void main(String[] args){
		launch(args);
	} // main(String[])
	//--------------------------------------------------------------------
} // class Second
