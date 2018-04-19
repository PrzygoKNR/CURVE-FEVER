package curveFever.languages;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class GUILanguage {

    protected final Map<String, String> dictionary = new HashMap<>();

    protected InterfaceLanguages languageCode;

    protected GUILanguage() {
        setupHashMap();
    }

    protected GUILanguage(String fileName, InterfaceLanguages languageCode) {
        this.languageCode = languageCode;
        setupHashMap();
        translationsLoader(fileName);
    }

    protected abstract void setupHashMap();

    protected void translationsLoader(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/curveFever/languages/" +
                                                                        "resources/" + fileName);

        try(Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)))) {
            while(scanner.hasNextLine()) {
                dictionary.put(scanner.nextLine().trim().toLowerCase(), scanner.nextLine().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InterfaceLanguages getLanguageCode() {
        return languageCode;
    }
}
