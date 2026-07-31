package com.codejava.commons.fx.validation;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * تحقق نهائى (عند الضغط على حفظ/إرسال) على صيغة القيمة، بعكس {@link InputValidator}
 * الذى يمنع الكتابة الخاطئة أثناء الطباعة.
 */
public final class FieldValidators {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern EGYPT_PHONE_PATTERN =
            Pattern.compile("^01[0125][0-9]{8}$");

    private static final Pattern IBAN_FORMAT_PATTERN =
            Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$");

    private static final Pattern EGYPT_NATIONAL_ID_PATTERN = Pattern.compile("[0-9]{14}");

    private FieldValidators() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /** رقم موبايل مصرى: يبدأ بـ 010/011/012/015 ويتكون من 11 رقم. */
    public static boolean isValidEgyptianPhone(String phone) {
        return phone != null && EGYPT_PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /** تحقق من صيغة IBAN وصحة رقم التحقق (checksum) بخوارزمية MOD-97. */
    public static boolean isValidIBAN(String rawIban) {
        if (rawIban == null) {
            return false;
        }
        String iban = rawIban.replaceAll("\\s+", "").toUpperCase();
        if (!IBAN_FORMAT_PATTERN.matcher(iban).matches()) {
            return false;
        }
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numeric = new StringBuilder(rearranged.length() * 2);
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numeric.append(Character.getNumericValue(c)); // A=10 .. Z=35
            } else {
                numeric.append(c);
            }
        }
        return new BigInteger(numeric.toString()).mod(BigInteger.valueOf(97)).intValue() == 1;
    }

    /**
     * تحقق هيكلى من الرقم القومى المصرى (14 رقم، قرن صحيح، تاريخ ميلاد صحيح).
     * ملحوظة: لا يتحقق من رقم التحقق الأخير (الخانة 14) لعدم وجود خوارزمية رسمية
     * موثقة لحسابها؛ فقط يتأكد من الشكل العام وصحة تاريخ الميلاد المشفر بداخله.
     */
    public static boolean isValidEgyptianNationalId(String rawId) {
        if (rawId == null) {
            return false;
        }
        String id = rawId.trim();
        if (!EGYPT_NATIONAL_ID_PATTERN.matcher(id).matches()) {
            return false;
        }
        char centuryDigit = id.charAt(0);
        if (centuryDigit != '2' && centuryDigit != '3') {
            return false;
        }
        int century = centuryDigit == '2' ? 1900 : 2000;
        int year = century + Integer.parseInt(id.substring(1, 3));
        int month = Integer.parseInt(id.substring(3, 5));
        int day = Integer.parseInt(id.substring(5, 7));
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    /** تحقق أن النص عبارة عن تاريخ صحيح فعلاً حسب الصيغة الممررة (مثل "dd/MM/yyyy"). */
    public static boolean isValidDate(String text, String pattern) {
        if (text == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern)
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(text.trim(), formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** يتأكد أن كل الحقول الممررة غير فارغة (بعد إزالة المسافات). */
    public static boolean notEmpty(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.getText() == null || textField.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /** يتأكد أن كل الـ ComboBox الممررة عليها اختيار (مش null). */
    public static boolean notNullSelection(ComboBox<?>... comboBoxes) {
        for (ComboBox<?> comboBox : comboBoxes) {
            if (comboBox.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    /** يتأكد أن كل الـ DatePicker الممررة عليها تاريخ مختار (مش null). */
    public static boolean notNullDate(DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            if (datePicker.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    /** يظهر/يخفى حدود حمراء حول أى Control (TextField, ComboBox, DatePicker...) لتنبيه المستخدم لخطأ. */
    public static void markError(Control control, boolean hasError) {
        String errorStyle = "-fx-border-color: #e53935; -fx-border-width: 1.5px;";
        control.setStyle(hasError ? errorStyle : "");
    }

    public static void clearError(Control... controls) {
        for (Control control : controls) {
            markError(control, false);
        }
    }
}
