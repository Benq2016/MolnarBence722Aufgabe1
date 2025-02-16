package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.toUpperCase;

public class Main {
    public class Akten {
        Integer id;
        String name;
        String syptome;
        String diagnose;
        String datum;
        String krankenhaus;

        @Override
        public String toString() {
            return name;
        }
    }

    public List<Akten> readData(String filename) throws Exception {
        List<Akten> akten = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }

            String jsonString = jsonContent.toString().trim();
            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);
            }

            String[] jsonObjects = jsonString.split("\\s*,\\s*(?=\\{)");

            for (String jsonObject : jsonObjects) {
                Akten akt = new Akten();
                jsonObject = jsonObject.replaceAll("[{}\"]", "");
                String[] fields = jsonObject.split("\\s*,\\s*");
                for (String field : fields) {
                    String[] keyValue = field.split("\\s*:\\s*");
                    switch (keyValue[0]) {
                        case "Id": {
                            akt.id = Integer.parseInt(keyValue[1]);
                            break;
                        }
                        case "Patient": {
                            akt.name = keyValue[1];
                            break;
                        }
                        case "Symptom": {
                            akt.syptome = keyValue[1];
                            break;
                        }
                        case "Diagnose": {
                            akt.diagnose = keyValue[1];
                            break;
                        }
                        case "Datum": {
                            akt.datum = keyValue[1];
                            break;
                        }
                        case "Krankenhaus": {
                            akt.krankenhaus = keyValue[1];
                        }
                    }
                }
                akten.add(akt);
            }
        }
        return akten;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        List<Main.Akten> akten = main.readData("src/fallakten.json");

        System.out.println("Enter a character: ");
        Scanner scanner = new Scanner(System.in);
        char input = scanner.nextLine().charAt(0);

        if(isLowerCase(input))
            input = toUpperCase(input);

        char finalInput = input;
        akten.stream().filter(a -> a.name.startsWith(String.valueOf(finalInput))).forEach(System.out::println);

        akten.stream()
                .filter(akt->akt.syptome.equals("Fieber"))
                .sorted(Comparator.comparing(akt-> {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").parse(akt.datum);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .forEach(akt->System.out.printf("%s: %s - Diagnose: %s%n", akt.datum, akt.name, akt.diagnose));
    }

}