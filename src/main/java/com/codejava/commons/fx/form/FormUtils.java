package com.codejava.commons.fx.form;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * مساعدات عامة لأى فورم/شاشة JavaFX (تنقل بالفوكس، توسيط النافذة، تحميل CSS).
 */
public final class FormUtils {

    private FormUtils() {
    }

    /**
     * بيخلى زرار Enter فى أى حقل ينقل الفوكس للحقل اللى بعده مباشرة حسب الترتيب الممرر
     * (مفيد لملء فورم طويل بالكيبورد بدل الماوس/Tab).
     */
    public static void focusNextOnEnter(TextField... fieldsInOrder) {
        for (int i = 0; i < fieldsInOrder.length; i++) {
            TextField current = fieldsInOrder[i];
            TextField next = (i + 1 < fieldsInOrder.length) ? fieldsInOrder[i + 1] : null;
            current.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER && next != null) {
                    next.requestFocus();
                }
            });
        }
    }

    /** يوسّط الـ Stage فى منتصف الشاشة الحالية. */
    public static void centerOnScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /** يحمّل ملف CSS من الـ classpath ويضيفه على الـ Scene (مثال: "/styles/app.css"). */
    public static void loadStylesheet(Scene scene, String cssClasspathResource) {
        String cssUrl = FormUtils.class.getResource(cssClasspathResource).toExternalForm();
        scene.getStylesheets().add(cssUrl);
    }
}
