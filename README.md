# fx-commons

مكتبة جافا خفيفة قابلة لإعادة الاستخدام فى أى مشروع JavaFX، بتجمع الأدوات اللى بتتكرر
فى أكتر من مشروع بدل ما تتكتب من الأول كل مرة.

## المحتوى

| الكلاس | الباكدج | الوظيفة |
|---|---|---|
| `InputValidator` | `validation` | فلاتر تمنع كتابة محتوى غلط داخل `TextField` أثناء الطباعة (real-time) |
| `FieldValidators` | `validation` | تحقق نهائى وقت الحفظ (إيميل، تليفون، IBAN، رقم قومى، تاريخ...) + تلوين الحقول الخاطئة |
| `AlertUtils` | `dialog` | رسائل جاهزة (نجاح/خطأ/تحذير/تأكيد) بدل كتابة `Alert` كامل كل مرة |
| `FormUtils` | `form` | الانتقال للحقل التالى بـ Enter، توسيط النافذة، تحميل CSS |
| `TableUtils` | `control` | رسالة مخصصة للجدول الفاضى، توزيع الأعمدة بالتساوى |
| `CurrencyFieldUtils` | `control` | تنسيق حقل رقمى بفواصل الآلاف تلقائيًا |

## التثبيت

### 1. بناء المكتبة وتثبيتها فى الـ local Maven repository

```bash
cd fx-commons
mvn clean install
```

### 2. إضافتها كـ dependency فى أى مشروع

```xml
<dependency>
    <groupId>com.codejava.commons</groupId>
    <artifactId>fx-commons</artifactId>
    <version>1.0.0</version>
</dependency>
```

> ملحوظة: المكتبة معلنة أن `javafx-controls` هى `provided` (وقت الترجمة فقط)، فالمشروع المستهلك لازم يكون عنده JavaFX أصلاً كـ dependency.

## الاستخدام

### فلاتر أثناء الكتابة — `com.codejava.commons.fx.validation.InputValidator`

```java
InputValidator.makeNumericOnly(phoneField, barcodeField);
InputValidator.makeDecimalOnly(priceField);
InputValidator.makeSignedDecimalOnly(balanceField);
InputValidator.makeLettersOnly(nameField);          // عربى + إنجليزى
InputValidator.makeArabicLettersOnly(arabicNameField);
InputValidator.makeEnglishLettersOnly(englishNameField);
InputValidator.makeAlphanumericOnly(usernameField);
InputValidator.makeNoSpaces(emailField);
InputValidator.denySpecialCharacters(commentField);
InputValidator.makeMaxLength(20, nationalIdField);
InputValidator.makePhoneOnly(11, mobileField);
InputValidator.makePercentageOnly(discountField);   // 0 - 100
InputValidator.makeDateOnly(birthDateField);         // قناع dd/MM/yyyy أثناء الكتابة

// أى حالة خاصة مش موجودة فوق
InputValidator.applyPattern("[A-Z]{0,3}[0-9]{0,6}", codeField);
```

### تحقق نهائى قبل الحفظ — `com.codejava.commons.fx.validation.FieldValidators`

```java
if (!FieldValidators.notEmpty(nameField, phoneField)
        || !FieldValidators.notNullSelection(branchCombo)
        || !FieldValidators.notNullDate(birthDatePicker)) {
    FieldValidators.markError(nameField, nameField.getText().isBlank());
    return;
}

if (!FieldValidators.isValidEmail(emailField.getText())) {
    FieldValidators.markError(emailField, true);
    return;
}

if (!FieldValidators.isValidEgyptianPhone(phoneField.getText())) {
    FieldValidators.markError(phoneField, true);
    return;
}

if (!FieldValidators.isValidIBAN(ibanField.getText())) {
    FieldValidators.markError(ibanField, true);
    return;
}

if (!FieldValidators.isValidEgyptianNationalId(nationalIdField.getText())) {
    FieldValidators.markError(nationalIdField, true);
    return;
}

if (!FieldValidators.isValidDate(birthDateField.getText(), "dd/MM/yyyy")) {
    FieldValidators.markError(birthDateField, true);
    return;
}

FieldValidators.clearError(nameField, phoneField, emailField, ibanField, nationalIdField, birthDateField);
```

### رسائل جاهزة — `com.codejava.commons.fx.dialog.AlertUtils`

```java
AlertUtils.showSuccess("تم الحفظ", "تم حفظ بيانات الطالب بنجاح");
AlertUtils.showError("خطأ", "الرقم القومى غير صحيح");
AlertUtils.showWarning("تنبيه", "الكمية أقل من الحد الأدنى المسموح به");

if (AlertUtils.showConfirm("تأكيد الحذف", "هل أنت متأكد من حذف هذا العنصر؟")) {
    // نفّذ الحذف
}
```

### مساعدات الفورم — `com.codejava.commons.fx.form.FormUtils`

```java
FormUtils.focusNextOnEnter(nameField, phoneField, emailField, addressField);
FormUtils.centerOnScreen(primaryStage);
FormUtils.loadStylesheet(scene, "/styles/app.css");
```

### مساعدات الجداول والحقول الرقمية — `com.codejava.commons.fx.control`

```java
TableUtils.setEmptyPlaceholder(studentsTable, "لا يوجد طلاب مسجلين حاليًا");
TableUtils.distributeColumnsEvenly(studentsTable);

CurrencyFieldUtils.bindThousandsFormatting(totalPriceField);
```

## إضافة دوال جديدة

كل الدوال فى `InputValidator` مبنية فوق دالتين مساعدتين (`applyFilter` و `applyControlTextFilter`)،
فأى قاعدة جديدة (Regex أو شرط مخصص) ممكن تتضاف كدالة `static void` جديدة فى نفس الكلاس بسطرين بس.
نفس الفكرة تنطبق على باقى الكلاسات: أى أداة عامة جديدة محتاجها فى أكتر من مشروع تتضاف كـ `static` method
فى الباكدج المناسب (`validation` / `dialog` / `form` / `control`)، أو باكدج جديد لو الفكرة نوع مختلف تمامًا.

## الترخيص

المشروع مرخّص تحت [MIT License](LICENSE).
