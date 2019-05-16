package mainWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML private ListView<String> listView;
    @FXML private TableView<Bus> table;
    @FXML private TableColumn<Bus, String> time;
    @FXML private TableColumn<Bus, String> name;
    @FXML private TableColumn<Bus, String> direction;

    public TextField busStopInput;
    public TextField aadressInput;
    public TextField radiusInput;

    Parser parser = new Parser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initializing cells in the table
        time.setCellValueFactory(new PropertyValueFactory<Bus, String>("busTime"));
        name.setCellValueFactory(new PropertyValueFactory<Bus, String>("busName"));
        direction.setCellValueFactory(new PropertyValueFactory<Bus, String>("busDirection"));
    }
    public void SavePeatused(String peatus) throws Exception {
        try (BufferedWriter kirjutaja = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OtsitudPeatused.txt", true), "UTF-8"))) {
            kirjutaja.write(peatus + "\n");
        }
    }

    public void SaveAadress(String aadress,String raadius) throws Exception{try (BufferedWriter kirjutaja = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OtsitudAadressid.txt", true), "UTF-8"))) {
        kirjutaja.write(aadress +"      R: "+raadius+ "\n");
    }}
    // Handling stopSearchButton click event
    public void handleStopSearchButton() throws Exception {
        SavePeatused(busStopInput.getText());
        ArrayList<BusStop> stops = parser.getStopId(busStopInput.getText());
        if (stops.size() == 1) {
            //busStopInput.setStyle("-fx-text-inner-color: black;");
            table.setItems(parser.parseXML(stops.get(0).getStopId(), ""));
        } else if (stops.size() > 1) { // If array has more than 1 stops, show dialog window with stops options
            // Initializing dialog windows with stops information
            ChoiceDialog<BusStop> dialog = new ChoiceDialog<>(stops.get(0), stops);
            dialog.setTitle("Peatuse valik");
            dialog.setHeaderText("Valige õige suund!");
            dialog.setContentText("Suund:");

            Optional<BusStop> result = dialog.showAndWait(); // Showing dialog window and waiting for an action

            result.ifPresent(busStop -> {
                try {
                    table.setItems(parser.parseXML(busStop.getStopId(), "")); // Providing chosen stop to the parser and showing putting data into the table
                } catch (ParserConfigurationException e) {
                    System.out.println("Parser error!");
                } catch (SAXException e) {
                    System.out.println("Parser error!");
                } catch (IOException e) {
                    System.out.println("Parser error!");
                }
            });
        }
        else { // If no stop with such name, show dialog window with the error
            // Initializing dialog window
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Peatuse otsing");

            alert.setHeaderText(null);
            alert.setContentText("Peatuse nimega " + busStopInput.getText() + " ei ole!");
            alert.showAndWait();
        }

    }

    // Handling nearest stops ListView click
    public void handleListViewClick() throws Exception {
        busStopInput.setText(listView.getSelectionModel().getSelectedItem()); // Putting chosen stop to busStopInput TextField
    }


    // Handling nearestStopsButton click event
    public void handleNearestStopsButton() throws Exception {
        SaveAadress(aadressInput.getText(),radiusInput.getText());
        if (!aadressInput.getText().isEmpty() || !radiusInput.getText().isEmpty()) { // If either TextFields are not empty, proceed with parsing
            listView.setItems(lähimadBussipeatused(parser, aadressInput.getText(), radiusInput.getText()));
        } else {
            System.out.println("Ei saa olla tühi!");
        }
    }

    public ObservableList<String> lähimadBussipeatused(Parser parser, String address, String radiusKm) throws Exception {
        ObservableList<String> peatusedL = FXCollections.observableArrayList();

        double[] coord = parser.getCoordinates(address.replace(" ", "%20"));

        if (coord[0] == 0.0 && coord[1] == 0.0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Vale aadress");

            alert.setHeaderText(null);
            alert.setContentText("Vale aadress!");
            alert.showAndWait();
        } else {
            ArrayList<String[]> stopsCoordinates = new ArrayList<>();
            File file = new File("stops.txt");

            try (Scanner sc = new Scanner(file, "UTF-8")) {
                while (sc.hasNextLine()) {
                    String row = sc.nextLine();
                    String[] pieces = row.split(",");

                    if (pieces[7].contains("Tartu linn")) {
                        stopsCoordinates.add(pieces);
                    }
                }
            }
            List<String> peatused = new ArrayList<>();
            try {
                for (int i = 1; i < stopsCoordinates.size(); i++) {
                    if (parser.getDistance(coord, Double.parseDouble(stopsCoordinates.get(i)[3]), Double.parseDouble(stopsCoordinates.get(i)[4])) < Double.parseDouble(radiusKm)) {
                        //Väljastab kõik bussijaamad
                        // System.out.println(stopsCoordinates.get(i)[2]);
                        peatusedL.add(stopsCoordinates.get(i)[2]);
                    }
                }
            } catch (NumberFormatException e) {
                //System.out.println("parseDouble() viga!");
                // Initializing dialog window
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Vale otsingu raadius");

                alert.setHeaderText(null);
                alert.setContentText("Palun sisestage ainult numbreid!");
                alert.showAndWait();
            }
//            Set<String> unikaalsedPeatused = new HashSet<String>(peatused);
//            // Väljastab peatused ilma kordusteta
//            for(String s : unikaalsedPeatused){
//                System.out.println(s);
//            }

        }
        return peatusedL;
    }
}
