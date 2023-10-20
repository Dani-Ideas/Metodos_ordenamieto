package fes.aragon.controller;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.util.Duration;
public class PrincipalController implements Initializable{
Data<String, Number> primero = null;
Data<String, Number> segundo = null;
int tiempoRetardo=40;
int numeroDatos=40;
	@FXML
	private BarChart<String, Number> bacGrafica;
	@FXML
	private Button btnBurbuja;
	@FXML
	private Button btnListaNueva;
	@FXML
	private Button btnInsersion;

	@FXML
/*---------------------------------------------------------------------*/
	void metodoInsersion(ActionEvent event) {
		
	}

@FXML
/*---------------------------------------------------------------------*/
	void metodoBurbuja(ActionEvent event) {
		this.btnListaNueva.setDisable(true);

		Task<Void> animateSortTask = burbujaTask(bacGrafica.getData().get(0));
		exec.submit(animateSortTask);

	}
@FXML
//*************************************************************************
	void metodoListaNueva(ActionEvent event) {
		bacGrafica.getData().clear();

		Series<String, Number> series = new Series<String, Number>();
		series = generarAleatoriosEnteros(numeroDatos);
		bacGrafica.getData().add(series);
	}

//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
private Task<Void> burbujaTask(Series<String, Number> series) {

	return new Task<Void>() {
		@Override
		protected Void call() throws Exception {
			ObservableList<Data<String, Number>> data = series.getData();
			for (int i = data.size() - 1; i >= 0; i--) {
				for (int j = 0; j < i; j++) {
					primero = data.get(j);
					segundo = data.get(j + 1);
					Platform.runLater(() -> {
						primero.getNode().setStyle("-fx-background-color:red ;");
						segundo.getNode().setStyle("-fx-background-color:blue ;");
					});
					Thread.sleep(tiempoRetardo);
					if (primero.getYValue().doubleValue() >=segundo.getYValue().doubleValue()) {
						CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(() -> {Animation swap =createSwapAnimation(primero, segundo);
								swap.setOnFinished(e -> latch.countDown());
								swap.play();
							});
					latch.await();
					}
				Thread.sleep(tiempoRetardo);
				Platform.runLater(() -> {
					primero.getNode().setStyle("-fx-background-color:blue ;");
					segundo.getNode().setStyle("-fx-background-color:red ;");
				});
			}
		}
			btnListaNueva.setDisable(false);
			return null;
		}
	};
}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		bacGrafica.setAnimated(false);
		Series<String, Number> series =new Series<String, Number>();

		series =generarAleatoriosEnteros(numeroDatos);
		bacGrafica.getData().add(series);
	}
	private Series<String, Number> generarAleatoriosEnteros(int n) {
		Series<String, Number> series = new Series<>();
		Random rnd = new Random();
		for (int i = 1; i <= n; i++) {
			series.getData().add(new Data<>(String.valueOf(i), rnd.nextInt(90) + 10));
		}
		return series;
	}
	private <T> Animation createSwapAnimation(Data<?, T> primero, Data<?, T> segundo) {
		double primeroX =primero.getNode().getParent().localToScene(primero.getNode().getBoundsInParent()).getMinX();
		double segundoX =primero.getNode().getParent().localToScene(segundo.getNode().getBoundsInParent()).getMinX();
		double primeroStartTranslate = primero.getNode().getTranslateX();
		double segundoStartTranslate = segundo.getNode().getTranslateX();
	
		TranslateTransition primeroTranslate = new TranslateTransition(Duration.millis(tiempoRetardo),primero.getNode());
		primeroTranslate.setByX(segundoX - primeroX);
		TranslateTransition sgundoTranslate = new TranslateTransition(Duration.millis(tiempoRetardo),segundo.getNode());

		sgundoTranslate.setByX(primeroX - segundoX);
		ParallelTransition translate = new ParallelTransition(primeroTranslate, sgundoTranslate);
		translate.statusProperty().addListener((obs, oldStatus, newStatus) -> {
			if (oldStatus == Animation.Status.RUNNING) {
				T temp = primero.getYValue();
				primero.setYValue(segundo.getYValue());
				segundo.setYValue(temp);
				primero.getNode().setTranslateX(primeroStartTranslate);
				segundo.getNode().setTranslateX(segundoStartTranslate);
			}
		});
		return translate;
	}
	private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		return t;
	});
}