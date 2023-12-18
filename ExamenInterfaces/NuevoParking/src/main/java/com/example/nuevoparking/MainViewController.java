package com.example.nuevoparking;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {


    @FXML
    private TableView<Entrada> tablaEntradas;


    @FXML
    private ImageView imgLogo;


    @FXML
    private TextField txtMatricula;
    @FXML
    private RadioButton radioStandard;
    @FXML
    private RadioButton radioOferta;
    @FXML
    private RadioButton radioLargaDuracion;
    @FXML
    private DatePicker datepickEntrada;
    @FXML
    private DatePicker datePickSalida;
    @FXML
    private Label lblCoste;
    @FXML
    private Button btnAñadirALaTabla;
    @FXML
    private Button btnSalidAplicacion;
    @FXML
    private TableColumn<Entrada, String> columnMatricula;
    @FXML
    private TableColumn<Entrada, String> columnModelo;
    @FXML
    private TableColumn<Entrada, String> columnFechaEntrada;
    @FXML
    private TableColumn<Entrada, String> columnFechaSalida;
    @FXML
    private TableColumn<Entrada, String> columnCliente;
    @FXML
    private TableColumn<Entrada, String> columnTarifa;
    @FXML
    private TableColumn<Entrada, Double> columnCoste;
    @FXML
    private ComboBox<String> comboModelo;
    @FXML
    private ToggleGroup togleGroupRadios;
    private ObservableList<Entrada> observableListEntradas = FXCollections.observableArrayList();
    @FXML
    private ComboBox<Cliente> comboCliente;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        datepickEntrada.setValue(LocalDate.now());

        datePickSalida.setValue(LocalDate.now().plusDays(1));


        datepickEntrada.valueProperty().addListener((observable, newValue, oldValue) -> {
            lblCoste.setText(calcularCoste() + "€");
        });

        datePickSalida.valueProperty().addListener((observable, newValue, oldValue) -> {
            lblCoste.setText(calcularCoste() + "€");
        });

        togleGroupRadios.selectedToggleProperty().addListener((observable, newValue, oldValue) -> {
            lblCoste.setText(calcularCoste() + "€");
        });


        ObservableList<String> nombresClientes = FXCollections.observableArrayList();

        // Agregar los nombres de los clientes a la lista


        // Inicializar valores ficticios para el ComboBox de Modelo
        ObservableList<String> modelosFicticios = FXCollections.observableArrayList(
                "Honda", "Mitshubishi", "Nissan", "Kia"
        );
        comboModelo.setItems(modelosFicticios);

        columnMatricula.setCellValueFactory((fila) -> {
            String matricula = fila.getValue().getMatricula();
            return new SimpleStringProperty(matricula);
        });
        columnModelo.setCellValueFactory((fila) -> {
            String modelo = fila.getValue().getModelo();
            return new SimpleStringProperty(modelo);
        });
        columnFechaEntrada.setCellValueFactory((fila) -> {
            String fechaEntrada = fila.getValue().getFechaEntrada(datepickEntrada.getValue()).toString();
            return new SimpleStringProperty(fechaEntrada);
        });
        columnFechaSalida.setCellValueFactory((fila) -> {
            String fechaSalida = fila.getValue().getFechaSalida().toString();
            return new SimpleStringProperty(fechaSalida);
        });
        columnCliente.setCellValueFactory((fila) -> {
            String cliente = fila.getValue().getCliente().getNombre();
            return new SimpleStringProperty(cliente);
        });
        columnFechaSalida.setCellValueFactory((fila) -> {
            String fechaSalida = fila.getValue().getFechaSalida().toString();
            return new SimpleStringProperty(fechaSalida);
        });
        columnTarifa.setCellValueFactory((fila) -> {
            String tarifa = fila.getValue().getTarifa();
            return new SimpleStringProperty(tarifa);
        });
        columnCoste.setCellValueFactory((fila) -> {
            Double coste = fila.getValue().getCoste();
            return new SimpleObjectProperty<>(coste);
        });


        comboModelo.getSelectionModel().selectFirst();


        observableListEntradas.addAll(Session.getEntradas());


        tablaEntradas.setItems(observableListEntradas);


        comboCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente.getNombre();
            }

            @Override
            public Cliente fromString(String s) {
                return null;
            }
        });


        Cliente cliente1 = new Cliente(1, "pepe Pere", "juan@email.com");
        Cliente cliente2 = new Cliente(2, "Señor Gato", "maria@email.com");
        Cliente cliente3 = new Cliente(3, "Ole maria", "carlos@email.com");
        Cliente cliente4 = new Cliente(4, "Pakito Sierra", "laura@email.com");
        Cliente cliente5 = new Cliente(5, "Jose asnar", "pedro@email.com");


        Entrada entrada1 = new Entrada("reewrhg", "Ford", LocalDate.now(), LocalDate.now(), cliente1, "Standard", 9.0);


        Entrada entrada2 = new Entrada("reewrhg", "Peugeot", LocalDate.now(), LocalDate.now(), cliente3, "Oferta", 7.0);


        tablaEntradas.getItems().setAll(entrada2, entrada1);


        comboCliente.getItems().setAll(cliente1, cliente2, cliente3, cliente4, cliente5);
        comboCliente.getSelectionModel().selectFirst();

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void salirApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void añadirALaTabla(ActionEvent actionEvent) {

        if (comprobarCampos()) {

            Entrada e = new Entrada();

            if (togleGroupRadios.selectedToggleProperty().getValue().equals(radioLargaDuracion)) {
                e.setTarifa("Larga Duracion");
            } else if (togleGroupRadios.selectedToggleProperty().getValue().equals(radioOferta)) {
                e.setTarifa("Oferta");
            } else {
                e.setTarifa("Standad");
            }
            e.setCliente(comboCliente.getValue());
            e.setMatricula(txtMatricula.getText());
            e.setModelo(comboModelo.getValue());
            e.setFechaEntrada(datepickEntrada.getValue());
            e.setFechaSalida(datepickEntrada.getValue());
            e.setCoste(Double.valueOf(lblCoste.getText().substring(0, lblCoste.getText().length() - 1)));

            tablaEntradas.getItems().add(e);

            tablaEntradas.refresh();
        }


    }

    private boolean comprobarCampos() {
        boolean out = true;
        if (txtMatricula.getText() == null || Objects.equals(txtMatricula.getText(), "")) {
            out = false;
            mostrarAlerta("Matricula", "Campo Vacio");

        }

        if (datePickSalida.getValue() == null || datepickEntrada.getValue() == null) {
            out = false;
            mostrarAlerta("Cuidado", "Uno de las fechas esta vacia");

        }

        return out;
    }

    private long calcularCoste() {
        long out = 0;
        long dias = ChronoUnit.DAYS.between(datepickEntrada.getValue(), datePickSalida.getValue());
        out = dias * radioButtonValor();

        return out;
    }

    private long radioButtonValor() {
        long out;
        if (togleGroupRadios.selectedToggleProperty().getValue().equals(radioLargaDuracion)) {
            out = 2;
        } else if (togleGroupRadios.selectedToggleProperty().getValue().equals(radioOferta)) {
            out = 6;
        } else {
            out = 8;
        }
        return out;
    }


}




