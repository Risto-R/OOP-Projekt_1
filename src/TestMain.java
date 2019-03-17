import java.util.Scanner;
import java.util.ArrayList;

public class TestMain {

    public static void bussiLiinid(String busStop) throws Exception {
        Parser parser = new Parser();
        ArrayList<BusStop> stops = parser.getStopId(busStop);
        if (stops.size() == 1) { // If only 1 stop was found - print out information
            parser.parseXML(stops.get(0).getStopId()); // This method prints information

        } else if (stops.size() > 1) { // If multiple stops were found - ask for the correct one then print
            System.out.println("On leitud mitu peatust nimega - " + busStop);
            for (int i = 0; i < stops.size(); i++) {
                System.out.println((i + 1) + ". - " + stops.get(i));
            }

            System.out.println("Valige õige peatuse number: ");
            Scanner scanner = new Scanner(System.in);
            int correctStop = scanner.nextInt();

            parser.parseXML(stops.get(correctStop - 1).getStopId());
        } else { // No stop with given name
            System.out.println("Peatust nimega " + busStop + " ei ole!");
        }
    }

    public static void bussiLiinidNumbriga(String busStop, int number) throws Exception {
        Parser parser = new Parser();
        ArrayList<BusStop> stops = parser.getStopId(busStop);
        if (stops.size() == 1) { // If only 1 stop was found - print out information
            parser.parseXML(stops.get(0).getStopId(), number); // This method prints information

        } else if (stops.size() > 1) { // If multiple stops were found - ask for the correct one then print
            System.out.println("On leitud mitu peatust nimega - " + busStop);
            for (int i = 0; i < stops.size(); i++) {
                System.out.println((i + 1) + ". - " + stops.get(i));
            }

            System.out.println("Valige õige peatuse number: ");
            Scanner scanner = new Scanner(System.in);
            int correctStop = scanner.nextInt();

            parser.parseXML(stops.get(correctStop - 1).getStopId(), number);
        } else { // No stop with given name
            System.out.println("Peatust nimega " + busStop + " ei ole!");
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Programmi lõpetamiseks vajutage ENTER");
        System.out.println("Sisestage peatuse nimi: ");
        String busStop = scanner.nextLine();
        System.out.println("Sisestage bussi number, et näha ainult neid bussiliine.\nENTER kui soovite näha kõiki bussiliine");
        String bussiLiiniNumber = scanner.nextLine();

        if (!bussiLiiniNumber.equals(""))
            bussiLiinidNumbriga(busStop,Integer.valueOf(bussiLiiniNumber));
        System.out.println();


        while (!busStop.equals("")) {
            bussiLiinid(busStop);
            System.out.println();
            System.out.println("Programmi lõpetamiseks vajutage ENTER");
            System.out.println("Sisestage peatuse nimi: ");
            busStop = scanner.nextLine();

        }
        System.out.println("Programm lõpetas tegevuse!");

    }
}
