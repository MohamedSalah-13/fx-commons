package com.codejava.commons.fx.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * رسائل جاهزة (Alert) بدل ما تتكتب من الصفر فى كل شاشة.
 */
public final class AlertUtils {

    private AlertUtils() {
    }

    public static void showInfo(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    public static void showSuccess(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(AlertType.ERROR, title, message);
    }

    /** رسالة تأكيد بزرارين (نعم/لا) وبترجع true لو المستخدم ضغط "نعم". */
    public static boolean showConfirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private static void show(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
