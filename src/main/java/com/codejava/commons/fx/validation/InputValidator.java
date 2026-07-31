package com.codejava.commons.fx.validation;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * فلاتر جاهزة لحقول JavaFX (TextField) تمنع كتابة محتوى غير مرغوب فيه أثناء الكتابة.
 * يمكن استخدامها فى أى مشروع JavaFX عن طريق إضافة هذه المكتبة كـ dependency.
 */
public final class InputValidator {

    private InputValidator() {
    }

    /**
     * أرقام صحيحة فقط (مثل أرقام التليفونات، الباركود، السعة).
     */
    public static void makeNumericOnly(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[0-9]*"), textFields);
    }

    /**
     * أرقام عشرية موجبة فقط (مثل الأسعار والمبالغ المالية) - نقطة عشرية واحدة كحد أقصى.
     */
    public static void makeDecimalOnly(TextField... textFields) {
        applyControlTextFilter(newText -> newText.matches("([0-9]*)?(\\.[0-9]*)?"), textFields);
    }

    /**
     * أرقام عشرية تسمح بإشارة سالبة فى البداية (مثل الرصيد أو الفروقات المالية).
     */
    public static void makeSignedDecimalOnly(TextField... textFields) {
        applyControlTextFilter(newText -> newText.matches("-?([0-9]*)?(\\.[0-9]*)?"), textFields);
    }

    /**
     * حروف فقط (عربى وإنجليزى) مع السماح بمسافة بين الكلمات (مثل حقل الاسم).
     */
    public static void makeLettersOnly(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[\\u0600-\\u06FFa-zA-Z\\s]*"), textFields);
    }

    /**
     * حروف عربية فقط مع السماح بمسافة بين الكلمات.
     */
    public static void makeArabicLettersOnly(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[\\u0600-\\u06FF\\s]*"), textFields);
    }

    /**
     * حروف إنجليزية فقط مع السماح بمسافة بين الكلمات.
     */
    public static void makeEnglishLettersOnly(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[a-zA-Z\\s]*"), textFields);
    }

    /**
     * حروف (عربى/إنجليزى) وأرقام فقط بدون مسافات أو رموز خاصة (مثل اسم المستخدم أو الكود).
     */
    public static void makeAlphanumericOnly(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[\\u0600-\\u06FFa-zA-Z0-9]*"), textFields);
    }

    /**
     * يمنع كتابة أى مسافات داخل الحقل (مفيد للإيميل، اسم المستخدم، الباركود).
     */
    public static void makeNoSpaces(TextField... textFields) {
        applyFilter(change -> !change.getText().matches(".*\\s.*"), textFields);
    }

    /**
     * يمنع كتابة أى رموز خاصة، ويسمح فقط بحروف (عربى/إنجليزى) وأرقام ومسافات.
     */
    public static void denySpecialCharacters(TextField... textFields) {
        applyFilter(change -> change.getText().matches("[\\u0600-\\u06FFa-zA-Z0-9\\s]*"), textFields);
    }

    /**
     * تحديد حد أقصى لعدد الحروف المسموح بكتابتها فى الحقل.
     */
    public static void makeMaxLength(int maxLength, TextField... textFields) {
        applyControlTextFilter(newText -> newText.length() <= maxLength, textFields);
    }

    /**
     * حقل تليفون: أرقام فقط بحد أقصى لعدد الخانات (مثل 11 رقم فى مصر).
     */
    public static void makePhoneOnly(int maxLength, TextField... textFields) {
        applyControlTextFilter(newText -> newText.matches("[0-9]{0," + maxLength + "}"), textFields);
    }

    /**
     * نسبة مئوية صحيحة فقط من 0 إلى 100 (مثل نسبة الخصم أو نسبة الحضور).
     */
    public static void makePercentageOnly(TextField... textFields) {
        applyControlTextFilter(newText -> {
            if (newText.isEmpty()) {
                return true;
            }
            if (!newText.matches("[0-9]{1,3}")) {
                return false;
            }
            return Integer.parseInt(newText) <= 100;
        }, textFields);
    }

    /**
     * قناع تاريخ بصيغة dd/MM/yyyy أثناء الكتابة (أرقام و "/" فقط، بحد أقصى 10 خانات).
     * التحقق من صحة التاريخ فعلياً (مثل 31/02) بيتم عند الحفظ عن طريق
     * {@link FieldValidators#isValidDate(String, String)}.
     */
    public static void makeDateOnly(TextField... textFields) {
        applyControlTextFilter(newText -> newText.matches("[0-9/]{0,10}"), textFields);
    }

    /**
     * فلتر مخصص عن طريق تمرير Regex خاص بحالة غير موجودة فوق (باب خلفى للمرونة).
     * الفلتر يُطبَّق على النص الكامل للحقل بعد التعديل.
     */
    public static void applyPattern(String regex, TextField... textFields) {
        applyControlTextFilter(newText -> newText.matches(regex), textFields);
    }

    // ================= Helpers =================

    private interface ChangeRule {
        boolean isAllowed(TextFormatter.Change change);
    }

    private interface ControlTextRule {
        boolean isAllowed(String controlNewText);
    }

    private static void applyFilter(ChangeRule rule, TextField... textFields) {
        UnaryOperator<TextFormatter.Change> filter = change -> rule.isAllowed(change) ? change : null;
        for (TextField textField : textFields) {
            textField.setTextFormatter(new TextFormatter<>(filter));
        }
    }

    private static void applyControlTextFilter(ControlTextRule rule, TextField... textFields) {
        UnaryOperator<TextFormatter.Change> filter =
                change -> rule.isAllowed(change.getControlNewText()) ? change : null;
        for (TextField textField : textFields) {
            textField.setTextFormatter(new TextFormatter<>(filter));
        }
    }
}
