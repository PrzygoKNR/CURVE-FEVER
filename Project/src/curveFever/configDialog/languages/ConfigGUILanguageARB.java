package curveFever.configDialog.languages;

public class ConfigGUILanguageARB implements ConfigGUILanguage {
    @Override
    public String wrongNumber() {
        return "رقم غير صحيح";
    }

    @Override
    public String numberOutOfBound() {
        return "يجب أن يكون الرقم أكبر من 1";
    }

    @Override
    public String player() {
        return "لاعب";
    }

    @Override
    public String ok() {
        return "حسنا";
    }

    @Override
    public String dialogTitle() {
        return "نافذة عادية تماما";
    }

    @Override
    public String clear() {
        return "واضح";
    }

    @Override
    public String exit() {
        return "ىخرج";
    }

    @Override
    public String setMaxNumberOfPlayer() {
        return "تعيين أقصى عدد من اللاعبين";
    }

    @Override
    public String colorUnavailable() {
        return "لون غير متوفر";
    }

    @Override
    public String wrongColor() {
        return "لون خاطئ";
    }

    @Override
    public String keyInUse() {
        return "المفتاح قيد الاستخدام";
    }

    @Override
    public String typeAKey() {
        return "اكتب مفتاحًا";
    }
}
