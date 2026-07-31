package com.codejava.commons.fx.control;

import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * تنسيق حقول الأرقام/المبالغ المالية بفواصل الآلاف.
 */
public final class CurrencyFieldUtils {

    private CurrencyFieldUtils() {
    }

    /**
     * يربط الحقل بتنسيق آلاف تلقائى (مثل 12345.5 يبقى 12,345.5) لما الحقل يفقد الفوكس،
     * وبيرجع الرقم عادى بدون فواصل لما المستخدم يدوس عليه عشان يعدّل بسهولة.
     * يُفضّل تطبيق {@code InputValidator.makeDecimalOnly} على نفس الحقل الأول عشان يفضل المحتوى رقمى صحيح.
     */
    public static void bindThousandsFormatting(TextField textField) {
        textField.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            String rawText = textField.getText();
            if (rawText == null || rawText.isBlank()) {
                return;
            }
            String plain = rawText.replace(",", "");
            if (isFocused) {
                textField.setText(plain);
                return;
            }
            try {
                double value = Double.parseDouble(plain);
                NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
                format.setMaximumFractionDigits(2);
                textField.setText(format.format(value));
            } catch (NumberFormatException e) {
                // القيمة مش رقم صحيح - سيبها زى ما هى وخلى الـ validation المسؤولة تمسكها
            }
        });
    }
}
