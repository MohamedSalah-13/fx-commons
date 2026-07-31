package com.codejava.commons.fx.control;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * مساعدات عامة لجداول JavaFX (TableView).
 */
public final class TableUtils {

    private TableUtils() {
    }

    /** يظهر رسالة مخصصة فى الجدول لما يكون فاضى بدل رسالة "No content in table" الافتراضية. */
    public static void setEmptyPlaceholder(TableView<?> tableView, String message) {
        tableView.setPlaceholder(new Label(message));
    }

    /** يوزّع عرض الأعمدة بالتساوى على عرض الجدول الحالى. */
    public static void distributeColumnsEvenly(TableView<?> tableView) {
        int columnCount = tableView.getColumns().size();
        tableView.getColumns().forEach(column ->
                column.prefWidthProperty().bind(tableView.widthProperty().divide(columnCount)));
    }
}
