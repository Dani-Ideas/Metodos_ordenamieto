package fes.aragon.inicio;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
public class Main extends Application {
@Override
public void start(Stage primaryStage) {
try {
Pane root =

(Pane)FXMLLoader.load(getClass().getResource("/fes/aragon/fxml/Principal.fxml"));

Scene scene = new Scene(root);

scene.getStylesheets().add(getClass().getResource("/fes/aragon/css/application.css").toExternalForm());

primaryStage.setScene(scene);
primaryStage.show();
} catch(Exception e) {
e.printStackTrace();
}
}
public static void main(String[] args) {
launch(args);
}
}

/*package fes.aragon.inicio;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("fes/aragon/fxml/Principal.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("fes/aragon/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}*/