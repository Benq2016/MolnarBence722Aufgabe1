package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public class Akten {
        Integer id;
        String name;
        String syptome;
        String diagnose;
        String datum;
        String krankenhaus;
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
        System.out.println(akten.size());
    }

}