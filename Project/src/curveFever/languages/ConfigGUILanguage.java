package curveFever.languages;

public class ConfigGUILanguage extends GUILanguage {

    protected ConfigGUILanguage() {
        super();
    }

    public ConfigGUILanguage(String fileName, InterfaceLanguages languageCode) {
        super(fileName, languageCode);
    }

    protected final void setupHashMap() {
        dictionary.put("wrong number", "default-generated-text");
        dictionary.put("number must be greater than 1", "default-generated-text");
        dictionary.put("player", "default-generated-text");
        dictionary.put("ok", "default-generated-text");
        dictionary.put("dialog title", "default-generated-text");
        dictionary.put("clear", "default-generated-text");
        dictionary.put("exit", "default-generated-text");
        dictionary.put("set max number of players", "default-generated-text");
        dictionary.put("color unavailable", "default-generated-text");
        dictionary.put("wrong color", "default-generated-text");
        dictionary.put("key in use", "default-generated-text");
        dictionary.put("type a key", "default-generated-text");
    }

    public final String wrongNumber() {
        return dictionary.get("wrong number");
    }

    public final String numberOutOfBound() {
        return dictionary.get("number must be greater than 1");
    }

    public final String player() {
        return dictionary.get("player");
    }

    public final String ok() {
        return dictionary.get("ok");
    }

    public final String dialogTitle() {
        return dictionary.get("dialog title");
    }

    public final String clear() {
        return dictionary.get("clear");
    }

    public final String exit() {
        return dictionary.get("exit");
    }

    public final String setMaxNumberOfPlayer() {
        return dictionary.get("set max number of players");
    }

    public final String colorUnavailable() {
        return dictionary.get("color unavailable");
    }

    public final String wrongColor(){
        return dictionary.get("wrong color");
    }

    public final String keyInUse() {
        return dictionary.get("key in use");
    }

    public final String typeAKey() {
        return dictionary.get("type a key");
    }
}
