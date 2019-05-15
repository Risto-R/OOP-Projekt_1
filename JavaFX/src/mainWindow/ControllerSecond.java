package mainWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;



public class ControllerSecond implements Initializable {

    @FXML private ListView<String> listView;
    @FXML private ListView<String> listView2;

@Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void ListViewClickPeatused(){
        System.out.println(listView.getSelectionModel().getSelectedItem());
    }

    public void LoeAjalugu() throws Exception{
        ObservableList peatused =
                FXCollections.observableArrayList();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("OtsitudPeatused.txt"), "UTF-8"))) {
            String rida = bf.readLine();
            while (rida != null) {
                peatused.add(rida);
                rida = bf.readLine();
            }
            listView.setItems(peatused);
        }

        ObservableList aadress =
                FXCollections.observableArrayList();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("OtsitudAadressid.txt"), "UTF-8"))) {
            String rida = bf.readLine();
            while (rida != null) {
                aadress.add(rida);
                rida = bf.readLine();
            }
            listView2.setItems(aadress);
        }
    }

    public void KustutaAjalugu()throws Exception{
        try (BufferedWriter kirjutaja = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OtsitudPeatused.txt"), "UTF-8"))) {
            kirjutaja.write("");
        }
        try (BufferedWriter kirjutaja = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OtsitudAadressid.txt"), "UTF-8"))) {
            kirjutaja.write("");
        }
        LoeAjalugu();
    }
}
