import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Parser {

    // Method for parse and output
    public void parseXML(String stopId) throws ParserConfigurationException, SAXException, IOException {
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

            String time = getValueFromElement(e, "text_begin"); // Bus time + bus number
            String title = getValueFromElement(e, "text_middle"); // Bus direction
            System.out.println(time + " " + title);

        }
    }

    // Kui sisestatakse ka bussi number
    public void parseXML(String stopId,String num) throws ParserConfigurationException, SAXException, IOException {
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

            String time = getValueFromElement(e, "text_begin"); // Bus time + bus number
            String title = getValueFromElement(e, "text_middle"); // Bus direction
            if(time.substring(7).trim().equals(num))
                System.out.println(time + " " + title);

        }
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
                if (stop[2].equals(stopName) && stop[7].contains("Tartu linn")) {
                    busStops.add(new BusStop(stop[2], stop[1], stop[8]));
                }
            }
        }

        return busStops; // Returns List with BusStop objects
    }
}