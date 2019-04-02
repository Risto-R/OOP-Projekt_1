import java.io.File;
import java.util.*;

public class TestMain {

    public static void lähimadBussipeatused() throws Exception{
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
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
            List<String> peatused = new ArrayList<>();
            for (int i = 1; i < stopsCoordinates.size(); i++) {
                if (parser.getDistance(coord, Double.parseDouble(stopsCoordinates.get(i)[3]), Double.parseDouble(stopsCoordinates.get(i)[4])) < Double.parseDouble(radiusKm)) {
                    //Väljastab kõik bussijaamad
                    // System.out.println(stopsCoordinates.get(i)[2]);
                    peatused.add(stopsCoordinates.get(i)[2]);
                }
            }
            Set<String> unikaalsedPeatused = new HashSet<String>(peatused);
            // Väljastab peatused ilma kordusteta
            for(String s : unikaalsedPeatused){
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saabuvadBussid() throws Exception{
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sisestage peatuse nimi: ");
        String busStop = scanner.nextLine();
        System.out.println("Sisestage bussi number, et näha ainult neid bussiliine.\nENTER kui soovite näha kõiki bussiliine");
        String bussiLiiniNumber = scanner.nextLine();


        ArrayList<BusStop> stops = parser.getStopId(busStop);

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
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();
        System.out.println("===============");
        System.out.println("    Valikud    ");
        System.out.println("===============");

        System.out.println("1. Lähimad bussipeatused");
        System.out.println("2. Saabuvad bussid");
        System.out.println("3. Sulge programm");
        System.out.println("\nSisesta valik: ");
        int valik = scanner.nextInt();

        while(valik != 3){
            switch (valik){
                case 1: lähimadBussipeatused();break;
                case 2: saabuvadBussid();break;
                default:
                    System.out.println("Sellist valikut ei ole.");
            }
            System.out.println("\n===============");
            System.out.println("    Valikud    ");
            System.out.println("===============");
            System.out.println("1. Lähimad bussipeatused");
            System.out.println("2. Saabuvad bussid");
            System.out.println("3. Sulge programm");
            System.out.println("\nSisesta valik: ");
            valik = scanner.nextInt();
        }
        System.out.println("Programm lõpetas töö.");
    }

}
