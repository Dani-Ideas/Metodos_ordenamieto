package fes.aragon.controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	
	int tiempoRetardo=40;
	int numeroDatos=40;
	@FXML
	private BarChart<String, Number> bacGrafica;
    @FXML
    private Button btnMescla;
	@FXML
	private Button btnselecion;
	@FXML
	private Button btnBurbuja;
	@FXML
	private Button btnListaNueva;
	@FXML
	private Button btnInsersionD;
    @FXML
    private Button btnInsersionB;

    
    
    @FXML
/*---------------------------------------------------------------------*/
    void metodoMescla(ActionEvent event) {
        this.btnListaNueva.setDisable(true);
        ObservableList<Data<String, Number>> data = bacGrafica.getData().get(0).getData();
        Task<Void> animateSortTask = mezclaTask(data, 0, data.size() - 1);
        exec.submit(animateSortTask);
    }

    
    @FXML
/*---------------------------------------------------------------------*/
    void metodoSelecion(ActionEvent event) {
    	this.btnListaNueva.setDisable(true);
		ObservableList<Data<String, Number>> data = bacGrafica.getData().get(0).getData();
		Task<Void> animateSortTask = selectionTask(data.subList(0, data.size()/2));
		exec.submit(animateSortTask);
		
    }
    @FXML
/*---------------------------------------------------------------------*/
    void metodoInsersionB(ActionEvent event) {
    	this.btnListaNueva.setDisable(true);
        ObservableList<Data<String, Number>> data = bacGrafica.getData().get(0).getData();
        
        Task<Void> animateSortTask = insercionBinariaTask(data.subList(0, data.size() / 2));
        exec.submit(animateSortTask);
       
    }
    
    
	@FXML
/*---------------------------------------------------------------------*/
	void metodoInsersionD(ActionEvent event) {
		this.btnListaNueva.setDisable(true);
	    ObservableList<Data<String, Number>> data = bacGrafica.getData().get(0).getData();
	    Task<Void> animateSortTask = insercionDirectaTask(data.subList(data.size()/2, data.size()));
	    exec.submit(animateSortTask);
	    //this.btnListaNueva.setDisable(false);
	}

@FXML
/*---------------------------------------------------------------------*/
	void metodoBurbuja(ActionEvent event) {
		this.btnListaNueva.setDisable(true);
		ObservableList<Data<String, Number>> data = bacGrafica.getData().get(0).getData();
		Task<Void> animateSortTask = burbujaTask(data.subList(0, data.size()/2));
		exec.submit(animateSortTask);
		
		//animateSortTask = burbujaTask(data.subList(data.size()/2,data.size()));
		//exec.submit(animateSortTask);
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
	private Task<Void> burbujaTask(List<Data<String, Number>> data) {

		return new Task<Void>() {
			Data<String, Number> primero = null;
			Data<String, Number> segundo = null;
			@Override
			protected Void call() throws Exception {
				//ObservableList<Data<String, Number>> data = series.getData();
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
	
	
	//tiene varios problemas al mometo de graficarlo,  Creo yo que son las animaciones , estube investigando y se otra otra lib, per no se porque bubuja funciona perfecto
	private Task<Void> insercionDirectaTask(List<Data<String, Number>> data) {
	    return new Task<Void>() {
	        Data<String, Number> actual = null;
	        Data<String, Number> temp= null;
	        int anterior;
	        @Override
	        protected Void call() throws Exception {
	            for (int i = 1; i < data.size()-1; i++) {
	                actual = data.get(i);
	                anterior = i - 1;
	                Platform.runLater(() -> {
	                    actual.getNode().setStyle("-fx-background-color:red ;");
	                });
	                Thread.sleep(tiempoRetardo);
	                while (anterior >= 0 && data.get(anterior).getYValue().doubleValue() > actual.getYValue().doubleValue()) {
	                    temp = data.get(anterior);
	                    
	                    // Mueve los elementos mayores para hacer espacio para actual
	                    data.set(anterior+1, data.get(anterior));
	                    
	                    Platform.runLater(() -> {Animation swap = createSwapAnimation(temp, data.get(anterior + 1));
	                    	swap.setOnFinished(e -> actual.getNode().setStyle("-fx-background-color:blue ;"));
	                    	swap.play();
	                    });
	                    anterior--;
	                }
	                // Coloca actual en la posición correcta
	                data.set(anterior + 1, actual);

	                Platform.runLater(() -> {
	                    actual.getNode().setStyle("-fx-background-color:blue ;");
	                });
	                Thread.sleep(tiempoRetardo);
	            }
	            return null;
	        }
	    };
	}
	
	//tiene varios problemas al mometo de graficarlo,  Creo yo que son las animaciones , estube investigando y se otra otra lib, per no se porque bubuja funciona perfecto
	private Task<Void> insercionBinariaTask(List<Data<String, Number>> data) {
	    return new Task<Void>() {
	        Data<String, Number> actual = null;
	        int j,k;
	        @Override
	        protected Void call() throws Exception {
	            for (int i = 1; i < data.size(); i++) {
	                actual = data.get(i);
	                j = i - 1;
	                Platform.runLater(() -> {
	                    actual.getNode().setStyle("-fx-background-color:red ;");
	                });
	                Thread.sleep(tiempoRetardo);

	                // Realiza una búsqueda binaria para encontrar la posición correcta
	                int low = 0;
	                int high = j;
	                while (low <= high) {
	                    int mid = (low + high) / 2;
	                    if (actual.getYValue().doubleValue() < data.get(mid).getYValue().doubleValue()) {
	                        high = mid - 1;
	                    } else {
	                        low = mid + 1;
	                    }
	                }

	                // Mueve los elementos mayores para hacer espacio
	                for (k = i - 1; k >= low; k--) {
	                    data.set(k + 1, data.get(k));
	                    Platform.runLater(() -> {Animation transition = createSwapAnimation(data.get(k), data.get(k + 1));
	                        transition.play();
	                    });
	                    Thread.sleep(tiempoRetardo);
	                }

	                // Coloca actual en la posición correcta
	                data.set(low, actual);

	                Platform.runLater(() -> {
	                    actual.getNode().setStyle("-fx-background-color:blue ;");
	                });

	                Thread.sleep(tiempoRetardo);
	            }
	            return null;
	        }
	    };
	}
	/*
	 * Nota esta funcion es propia, creo que en una clase se hizo con el mike pero llege tarde
	 * hay un extracto del metodo del mike reuperado de una captura de pantalla que realice en clase
	 * */
	//tiene varios problemas al mometo de graficarlo,  Creo yo que son las animaciones , estube investigando y se otra otra lib, per no se porque bubuja funciona perfecto
	
	
	private Task<Void> selectionTask(List<Data<String, Number>> data) {
	    return new Task<Void>() {
	        Data<String, Number> primero = null;
	        Data<String, Number> segundo = null;
	        @Override
	        protected Void call() throws Exception {
	            int min = 0;
	            for (int i = 0; i < data.size(); i++) {
	                min = i;
	                for (int j = i; j < data.size(); j++) {
	                    primero = data.get(j);
	                    segundo = data.get(min);
	                    Platform.runLater(() -> {
	                        primero.getNode().setStyle("-fx-background-color:blue ;");
	                        segundo.getNode().setStyle("-fx-background-color:red ;");
	                    });
	                    Thread.sleep(tiempoRetardo);
	                    if (primero.getYValue().doubleValue() < segundo.getYValue().doubleValue()) {
	                        min = j;
	                    }
	                }
	                if (i != min) {
	                    Data<String, Number> temp = data.get(i);
	                    data.set(i, data.get(min));
	                    data.set(min, temp);
	                    primero = data.get(i);
	                    segundo = data.get(min);
	                    Platform.runLater(() -> {
	                        Animation transition = createSwapAnimation(primero, segundo);
	                        transition.setOnFinished(e -> {
	                            primero.getNode().setStyle("-fx-background-color:blue ;");
	                            segundo.getNode().setStyle("-fx-background-color:red ;");
	                        });
	                        transition.play();
	                    });
	                }
	                Thread.sleep(tiempoRetardo);
	            }
	            return null;
	        }
	    };
	}
	
	/*private Task<Void> selectionTask(List<Data<String, Number>> data){
		return new Task <Void>() {
			Data<String, Number> primero = null;
			Data<String, Number> segundo = null;
			@Override
			protected Void call() throws Exception{
				//ObservableList<Data<String,Number>> data= series.getData();
				int min=0;
				for (int i=0;i<data.size();i++) {
					min=i;
					for(int j=i; j<data.size();j++) {
						primero= data.get(j);
						segundo= data.get(min);
						Platform.runLater(() -> {
							primero.getNode().setStyle("-fx-background-color:blue ;");
							segundo.getNode().setStyle("-fx-background-color:red ;");
						});
						Thread.sleep(tiempoRetardo);
						if (primero.getYValue().doubleValue()<segundo.getYValue().doubleValue()) {
							min=j;
						}
					}
					if (i!=min) {
						primero= data.get(i);
						segundo= data.get(min);
						
						CountDownLatch latch = new CountDownLatch(1);
					}
				}
				return null;
			}
		};
		
	}*/
	/*
	 * 1 realizar una división 
	 * 2 Luego, aplicar el algoritmo de mezcla a ambas mitades 
	 * 3 finalmente combinar las dos mitades ordenadas en una lista ordenada completa para eso esta merge
	 * merge es el metod justo debajo de mezclaTask
	 * */
	private Task<Void> mezclaTask(List<Data<String, Number>> data, int inicio, int fin) {
	    return new Task<Void>() {
	        @Override
	        protected Void call() throws Exception {
	            if (inicio < fin) {
	                int medio = (inicio + fin) / 2;
	                // Divide la lista en dos mitades
	                Task<Void> tareaIzquierda = mezclaTask(data, inicio, medio);
	                Task<Void> tareaDerecha = mezclaTask(data, medio + 1, fin);

	                // Espera a que las dos mitades estén ordenadas
	                exec.submit(tareaIzquierda);
	                exec.submit(tareaDerecha);
	                tareaIzquierda.get();
	                tareaDerecha.get();

	                // Realiza la mezcla de las dos mitades
	                merge(data, inicio, medio, fin);
	                
	                // Actualiza la vista de la gráfica en la interfaz de usuario
	                Platform.runLater(() -> {
	                    bacGrafica.getData().get(0).setData((ObservableList<Data<String, Number>>) data);
	                });

	                Thread.sleep(tiempoRetardo);
	            }
	            return null;
	        }
	    };
	}

	private void merge(List<Data<String, Number>> data, int inicio, int medio, int fin) {
	    List<Data<String, Number>> aux = new ArrayList<>(data.subList(inicio, fin + 1));
	    int i = 0, j = medio - inicio + 1, k = inicio;

	    while (i <= medio - inicio && j <= fin - inicio) {
	        Data<String, Number> primera = aux.get(i);
	        Data<String, Number> segunda = aux.get(j);
	        if (primera.getYValue().doubleValue() <= segunda.getYValue().doubleValue()) {
	            data.set(k++, primera);
	            i++;
	        } else {
	            data.set(k++, segunda);
	            j++;
	        }
	    }

	    while (i <= medio - inicio) {
	        data.set(k++, aux.get(i++));
	    }

	    while (j <= fin - inicio) {
	        data.set(k++, aux.get(j++));
	    }
	}
	
	/*
	 * public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
	 * 
	 * */

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