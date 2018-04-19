package curveFever.configDialog.languages;

public class ConfigGUILanguageENG implements ConfigGUILanguage {
    @Override
    public final String wrongNumber() {
        return "Wrong number";
    }

    @Override
    public final String numberOutOfBound() {
        return "Number must be greater than 1";
    }

    @Override
    public final String player() {
        return "Player";
    }

    @Override
    public String ok() {
        return "OK";
    }

    @Override
    public String dialogTitle() {
        return "Normalnie nazwane OKIENKO";
    }

    @Override
    public String clear() {
        return "Clear";
    }

    @Override
    public String exit() {
        return "Exit";
    }

    @Override
    public String setMaxNumberOfPlayer() {
        return "Set max number of players";
    }

    @Override
    public String colorUnavailable() {
        return "Color Unavailable";
    }

    @Override
    public String wrongColor() {
        return "Wrong Color";
    }

    @Override
    public String keyInUse() {
        return "Key in use";
    }

    @Override
    public String typeAKey() {
        return "Type a key";
    }
}
