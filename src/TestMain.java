import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();
        System.out.println("Sisestage oma aadress: ");
        String address = scanner.nextLine();
        System.out.println("Sisestage otsingu raadius (km): ");
        String radiusKm = scanner.nextLine();
        System.out.println("Lähimad peatused: ");

        try {
            double[] coord = parser.getCoordinates(address.replace(" ", "%20"));

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

            for (int i = 1; i < stopsCoordinates.size(); i++) {
                if (parser.getDistance(coord, Double.parseDouble(stopsCoordinates.get(i)[3]), Double.parseDouble(stopsCoordinates.get(i)[4])) < Double.parseDouble(radiusKm)) {
                    System.out.println(stopsCoordinates.get(i)[2]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Programmi lõpetamiseks vajutage ENTER");
        System.out.println("Sisestage peatuse nimi: ");
        String busStop = scanner.nextLine();
        System.out.println("Sisestage bussi number, et näha ainult neid bussiliine.\nENTER kui soovite näha kõiki bussiliine");
        String bussiLiiniNumber = scanner.nextLine();


        ArrayList<BusStop> stops = parser.getStopId(busStop);

        while (!busStop.equals("")) {

            if (stops.size() == 1) { // If only 1 stop was found - print out information
                //System.out.println("I have only 1 BusStop object!");
                parser.parseXML(stops.get(0).getStopId(), bussiLiiniNumber); // This method prints information
            } else if (stops.size() > 1) { // If multiple stops were found - ask for the correct one then print
                System.out.println("On leitud mitu peatust nimega - " + busStop);
                for (int i = 0; i < stops.size(); i++) {
                    System.out.println((i + 1) + ". - " + stops.get(i));
                }

                System.out.println("Valige õige peatuse number: ");
                int correctStop = scanner.nextInt();

                parser.parseXML(stops.get(correctStop-1).getStopId(), bussiLiiniNumber);
            } else { // No stop with given name
                System.out.println("Peatust nimega " + busStop + " ei ole!");
            }

            System.out.println();
            System.out.println("Programmi lõpetamiseks vajutage ENTER");
            System.out.println("Sisestage peatuse nimi: ");
            busStop = scanner.nextLine();

        }
        System.out.println("Programm lõpetas tegevuse!");

    }
}
