package mainWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    // Method for XML parsing and output
    public ObservableList<Bus> parseXML(String stopId, String busNumber) throws ParserConfigurationException, SAXException, IOException {
        ObservableList<Bus> busTimetable = FXCollections.observableArrayList();

        // XML document parsing from URL
        String url = "http://lv.raad.tartu.ee:8877/SmartBusSVC.asmx/GetBoardInfo?stop_id=" + stopId;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(url);

        doc.getDocumentElement().normalize();

        NodeList informationTag = doc.getElementsByTagName("information");
        NodeList infoTag = informationTag.item(0).getChildNodes(); // infoTag contains info for each bus

        //  Code below outputs info
        for (int i = 0; i < infoTag.getLength(); i++) {
            Element e = (Element) infoTag.item(i);

            String time = getValueFromElement(e, "text_begin").substring(0, getValueFromElement(e, "text_begin").length()-1); // Bus time + bus number
            String title = getValueFromElement(e, "text_middle"); // Bus direction

            int number = Character.getNumericValue(time.charAt(8));
            if (busNumber.equals("")) {
                busTimetable.add(new Bus(time.substring(0, 5), time.substring(time.lastIndexOf(" ")), title));
            } else {
                if (number == Integer.valueOf(busNumber)) {
                    busTimetable.add(new Bus(time.substring(0, time.indexOf(" ")), time.substring(time.lastIndexOf(" ")), title));
                }
            }

        }
        return busTimetable;
    }

    private String getValueFromElement(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Element childElement = (Element) nodeList.item(0);
        Node childNode = childElement.getChildNodes().item(0);
        return childNode.getNodeValue();
    }

    // This method looks for a bus ID from the file stops.txt
    public ArrayList<BusStop> getStopId(String stopName) throws Exception {
        ArrayList<String[]> stops = new ArrayList<>();
        ArrayList<BusStop> busStops = new ArrayList<>();
        File file = new File("stops.txt");

        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNextLine()) {
                String row = sc.nextLine();
                String[] pieces = row.split(",");

                stops.add(pieces);
            }

            for (String[] stop : stops) {
                if (stop[2].toLowerCase().equals(stopName.toLowerCase()) && stop[7].contains("Tartu linn")) {
                    busStops.add(new BusStop(stop[2], stop[1], stop[8]));
                }
            }
        }

        return busStops; // Returns List with BusStop objects
    }

    // Gets latitude and longitude from address
    public double[] getCoordinates(String address) throws Exception {
        String url = "https://nominatim.openstreetmap.org/search/" + address + "%20tartu?format=json&limit=1";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (response.length() > 2) {
            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString().substring(1, response.length()));
            double[] coordinates = {Double.parseDouble(myResponse.getString("lat")), Double.parseDouble(myResponse.getString("lon"))};
            return coordinates;

        } else {
            double[] coordinates = {0, 0};
            System.out.println("Vale aadress!");
            return coordinates;
        }
    }

    // Calculate the distance between two points
    public double getDistance(double[] coordinates, double lat1, double lon1) {
        double R = 6371;
        double dLat = Math.toRadians(lat1- coordinates[0]);
        double dLon = Math.toRadians(lon1 - coordinates[1]);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(coordinates[0])) * Math.cos(Math.toRadians(coordinates[1])) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;

        return d;
    }
}