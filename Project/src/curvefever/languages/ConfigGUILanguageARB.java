package curvefever.languages;

public final class ConfigGUILanguageARB extends ConfigGUILanguage {

    public ConfigGUILanguageARB() {
        languageCode = InterfaceLanguages.ARABIC;
        dictionary.put("wrong number", "رقم غير صحيح");
        dictionary.put("number must be greater than 1", "يجب أن يكون الرقم أكبر من 1");
        dictionary.put("player", "لاعب");
        dictionary.put("ok", "حسنا");
        dictionary.put("dialog title", "نافذة عادية تماما");
        dictionary.put("clear", "واضح");
        dictionary.put("exit", "ىخرج");
        dictionary.put("set max number of players", "تعيين أقصى عدد من اللاعبين");
        dictionary.put("color unavailable", "لون غير متوفر");
        dictionary.put("wrong color", "لون خاطئ");
        dictionary.put("key in use", "المفتاح قيد الاستخدام");
        dictionary.put("type a key", "اكتب مفتاحًا");
    }
}
