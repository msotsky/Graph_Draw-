/*
* Maxime Sotsky
* Dr. Keliher
* COMP 2631 2021 Winter FINAL
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.event.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.input.*;

import java.util.*;
import java.io.*;

public class GraphDraw extends Application {

    public static ArrayList<ArrayList<Integer>> cliques = new ArrayList<>();

    public final int RADIUS = 200;
    public final int SIZE_X = 640;
    public final int SIZE_Y = 550;
    public final int NODEMID_X = 15;
    public final int NODEMID_Y = 10;

    private int numNodes;
    private boolean[][] adjMatrix;
    private double[] nodeCenterX;
    private double[] nodeCenterY;
    private boolean[] active;
    private double[][] botLeftTopRight; //numNodes row / 2 col (rect coords)
    
    //======================    GUI VARS

    //imp & misc
    private BorderPane root;
    private Scene scene;
    private FileChooser fc;
    private VBox vb;
    private Button bClear;
    private Button bClique;

    //toolbar
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem open;
    private MenuItem exit;
    private Canvas canvas;
    private GraphicsContext gc;

    //start setup
    public void start(Stage stage) throws Exception{
		setup();		
		setUpHandlers(stage);
		Scene scene = new Scene(root, SIZE_X, SIZE_Y);
		stage.setScene(scene);
		stage.setTitle("GraphDraw");
		stage.show();
	}

    //setup
    private void setup(){

        //root & vbox
        root = new BorderPane();
        vb = new VBox();
        vb.setPrefHeight(50);
        vb.setPrefWidth(100);
        
        //toolbar
        open = new MenuItem("Open");
        exit = new MenuItem("Exit");
        fileMenu = new Menu("File", null, open , exit);
        menuBar = new MenuBar(fileMenu);
        root.setTop(menuBar);

        //buttons
        bClear = new Button("Clear");
		bClique = new Button("Clique me!");
        bClear.setMinWidth(vb.getPrefWidth());
        bClear.setMinHeight(vb.getPrefHeight());
        bClique.setMinWidth(vb.getPrefWidth());
        bClique.setMinHeight(vb.getPrefHeight());

        //canvas
        canvas = new Canvas(500,500);
        root.setCenter(canvas);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BEIGE);
        gc.fillRect(0,0,500,500);
        makeCircle(gc);

        vb.getChildren().addAll(bClear, bClique);
        root.setLeft(vb);
    }
    //draws the graph on the canvas
    private void drawGraph(GraphicsContext gc){

        gc.setFill(Color.BEIGE);
        gc.fillRect(0,0,500,500); //CLEAR CANVAS
        makeCircle(gc);

        gc.beginPath();
        gc.setStroke(Color.BLACK);

        for(int i = 0; i < numNodes; i++){
            for(int j = 0; j < numNodes; j++){
                if(adjMatrix[i][j] == true && active[i] == true && active[j] == true){
                    gc.moveTo(nodeCenterX[i],nodeCenterY[i]);
                    gc.lineTo(nodeCenterX[j],nodeCenterY[j]);
                    gc.stroke();
                }
            }
        }

        botLeftTopRight = new double[numNodes][4];
        Color c = Color.web("#FF00FF",0.4); //semi-transparent magenta (not active node color)

        for(int i = 0; i < nodeCenterX.length; i++){
            if(active[i] == true){
                gc.setFill(Color.MAGENTA);
                gc.fillRoundRect(nodeCenterX[i]-NODEMID_X, nodeCenterY[i]-NODEMID_Y, 30, 20, 5, 5); //30/2 , 20/2 (middle)
                gc.setFill(Color.BLACK);
                gc.fillText(String.valueOf(i),nodeCenterX[i], nodeCenterY[i]); 
            }else{
                gc.setFill(c);
                gc.fillRoundRect(nodeCenterX[i]-NODEMID_X, nodeCenterY[i]-NODEMID_Y, 30, 20, 5, 5);
                gc.setFill(Color.BLACK);
                gc.fillText(String.valueOf(i),nodeCenterX[i], nodeCenterY[i]); 
            }
        }
        //Storing bot left and top right rectangle coords (used for active / !active nodes)
        //activation point must be somewhere in there
        for(int i = 0; i < numNodes; i++){
            botLeftTopRight[i][0] = (nodeCenterX[i] - NODEMID_X); //Bottom Left x
            botLeftTopRight[i][1] = (nodeCenterY[i] + NODEMID_Y); //Bottom Left y
            botLeftTopRight[i][2] = (nodeCenterX[i] + NODEMID_X); //Top Right x
            botLeftTopRight[i][3] = (nodeCenterY[i] - NODEMID_Y); //Top Right y
        }
        
    }

    //Invisible circle
    private void makeCircle(GraphicsContext gc){
        gc.setStroke(Color.BEIGE); //Not visible  --CHANGE TO BLACK FOR TESTING
        gc.strokeOval(500/2 - RADIUS, 500/2 - RADIUS, RADIUS * 2, RADIUS * 2); //500 canvas h and w
    }
    
    //Handlers
    private void setUpHandlers(Stage stage){
        //checks if user clicked on the area of the node rectangle
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e){
                double x = e.getX();
                double y = e.getY();
                //top left = 0.0
                for(int i = 0; i < botLeftTopRight.length; i++){
                    if((x > botLeftTopRight[i][0]) && (x < botLeftTopRight[i][2]) && (y < botLeftTopRight[i][1]) && (y > botLeftTopRight[i][3])){
                        if(active[i] == true)
                            active[i] = false;
                        else
                            active[i] = true;
                    }
                }
                drawGraph(gc);
            }
        });
        //Clear canvas and reset instance vars
        bClear.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                gc.setFill(Color.BEIGE);
                gc.fillRect(0,0,500,500);
                makeCircle(gc);
                adjMatrix = null;
                numNodes = 0;
                nodeCenterX = null;
                nodeCenterY = null;
                active = null;
            }
        });
        //finds the largest clique on the graph
        bClique.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                if(adjMatrix == null)
                    return;
                cliques = new ArrayList<ArrayList<Integer>>();
                ArrayList<Integer> P = new ArrayList<>(); 
                ArrayList<Integer> X = new ArrayList<>(); //{} empty
                ArrayList<Integer> R = new ArrayList<>(); //{} empty
                for(int i = 0; i < numNodes; i++){
                    P.add(i);   //adding all nodes (int)
                }
                ArrayList<ArrayList<Integer>> cliqueList = bronKerbosch(R, P, X);
                ArrayList<Integer> largestClique = cliqueList.get(getLargestClique(cliqueList));  
                for(int i = 0; i < numNodes; i++){
                    if(!largestClique.contains(i)){
                        active[i] = false;
                    }
                }
                drawGraph(gc);
            }
        });
        //user exit prog option
        exit.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                Platform.exit();
            }
        });
        //user open file option
        open.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                fc = new FileChooser();
                File file = fc.showOpenDialog(stage);
                readInGraph(file);
                computeNodeCoords();
            }
            
        });
    }

    //Helper methods
    //Reads txt file and makes a graph, also tests whether the graph is valid or not
    private boolean readInGraph(File f){

        try{
            Scanner sc = new Scanner(f);
            int temp_numNodes = sc.nextInt();
            int zeroOrOne = 0;

            if(temp_numNodes < 0)
                return false;
                    
            boolean[][] temp_adjMatrix = new boolean[temp_numNodes][temp_numNodes];
            for (int i = 0; i < temp_numNodes; i++){
                for (int j = 0; j < temp_numNodes; j++){
                    zeroOrOne = sc.nextInt();
                    if(zeroOrOne != 0 && zeroOrOne != 1)
                        return false;
                    else{
                        boolean bool = (zeroOrOne == 1); // 1 = true / 0 = false
                        temp_adjMatrix[i][j] = bool;
                    }
                } 
            }
            boolean check = CheckAdjMatrix(temp_numNodes, temp_adjMatrix);
            if (!check)
                return false;
            else{
                numNodes = temp_numNodes;
                adjMatrix = temp_adjMatrix;
                active = new boolean[numNodes];
                for(int i = 0; i < active.length; i++)
                    active[i] = true;
                
                return true;
            }         
        }catch(Exception e){
            return false;
        }    
    }

    //verify that n is positive, adj has size n x n, is symmetric
    //and adj has no true values on the diagonal (no self loops allowed)
    private static boolean CheckAdjMatrix(int n, boolean[][] adj){

        if(n <= 0)
            return false;

        for (int i = 0; i < n; i++){
            //diag
            if (adj[i][i] == true)
                return false;
                
            //sym
            for (int j = 0; j < n; j++){
                if (adj[i][j] != adj[j][i])
                    return false;  
            }
        }
        return true;
    }

    //this method computes the x and y coordinates of the centers of the nodes that will drawn on the canvas.
    //These centers should be evenly spaced around an invisible circle radius 200 whose center is the center of the canvas.
    private void computeNodeCoords(){

        nodeCenterX = new double[numNodes];
        nodeCenterY = new double[numNodes];

        for(int i = numNodes; i >= 1; i--){
            double[] dbArr = computeNodeCoordsHelper(i, numNodes);
            nodeCenterX[i-1] = dbArr[0];
            nodeCenterY[i-1] = dbArr[1];
        }
        drawGraph(gc);
    }

    //Finds the indexes of the nodes (evenly spread around a circle of radius 200 at mid of canvas 500 x 500)
    private double[] computeNodeCoordsHelper(int i, int n){
        // top left 0.0
        double y = RADIUS * Math.cos( ((Math.PI*2) / n) * i) + 250; //half way point on canvas size 500 x 500
        double x = RADIUS * Math.sin( ((Math.PI*2) / n) * i) + 250;
        double[] coor = {x,y};
        return coor;
    }

    //Helper for cliques 
    private ArrayList<ArrayList<Integer>> matrixToList(boolean [][] adjM){
        ArrayList<ArrayList<Integer>> adjL = new ArrayList<>(adjM[0].length);
        for(int i = 0; i < adjM[0].length; i++){
            adjL.add(new ArrayList<Integer>());
        }
        for(int i = 0; i < adjM.length; i++){
            for(int j = 0; j < adjM.length; j++){
                if(adjM[i][j] == true){
                    adjL.get(i).add(j);
                }
            }
        }
        return adjL;
    }

    //Pseudo code: https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm#Without_pivoting
    /*
        algorithm BronKerbosch1(R, P, X) is
        if P and X are both empty then
        report R as a maximal clique
        for each vertex v in P do
        BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
        P := P \ {v}
        X := X ⋃ {v}
    */
    //making duplicate vars R P X, getting ConcurrentModificationExcption
    //R cliques , P nodes to do , X done nodes
    private ArrayList<ArrayList<Integer>> bronKerbosch(ArrayList<Integer> R, ArrayList<Integer> P, ArrayList<Integer> X){

        ArrayList<ArrayList<Integer>> adjList = matrixToList(adjMatrix); //making my adjMatrix to adjList for bronKerbosch
        
        if(P.isEmpty() && X.isEmpty()) 
            return cliques; 
        
        Iterator<Integer> iterator = P.iterator();
        ArrayList<Integer> temp_R = new ArrayList<>();
        ArrayList<Integer> temp_P = new ArrayList<>();
        ArrayList<Integer> temp_X = new ArrayList<>();

        while(iterator.hasNext()){

            int node = iterator.next();
            temp_R = new ArrayList<>();
            temp_P = new ArrayList<>();
            temp_X = new ArrayList<>();
            temp_R.addAll(R);
            temp_R.add(node);

            for(int n : P){
                if(adjList.get(node).contains(n))
                    temp_P.add(n);
            }
            for(int n : X){
                if(adjList.get(node).contains(n))
                    temp_X.add(n);
            }
            cliques.add(temp_R);
            bronKerbosch(temp_R, temp_P, temp_X);
            iterator.remove();
            X.add(node);
        }
        return cliques;
    }

    //gets index of first largest clique found by bronKerbosch algorithm
    private int getLargestClique(ArrayList<ArrayList<Integer>> cliq){
        int largestSize = 0;
        int largestClique = 0;
        for(int i = 0; i < cliq.size(); i++){
            if(cliq.get(i).size() > largestSize){
                largestClique = i;
                largestSize = cliq.get(largestClique).size();
            }
        }
        return largestClique;
    }
    //main
    public static void main(String[] args){
		launch(args);
	}

}
