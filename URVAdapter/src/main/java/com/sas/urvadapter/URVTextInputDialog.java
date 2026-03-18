package com.sas.urvadapter;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;



/**
 * Утилитный класс для отображения диалога с однострочным полем ввода.
 */
public final class URVTextInputDialog {

    /**
     * Интерфейс для получения введённого текста.
     */
    public interface ICallbackInputText {
        /**
         * Вызывается при нажатии кнопки "OK".
         * @param text введённый текст (никогда не null, может быть пустым)
         */
        void onTextEntered(String text);
    }

    private URVTextInputDialog() {
        // Приватный конструктор запрещает создание экземпляров
    }

    /**
     * Показывает диалог ввода текста.
     *
     * @param context     Контекст (Activity или другой, привязанный к теме)
     * @param title       Заголовок диалога
     * @param hint        Подсказка в поле ввода
     * @param initialText Начальный текст (можно передать null)
     * @param callback    Слушатель, который получит введённый текст
     */
    public static void show(Context context,
                            String title,
                            String hint,
                            String initialText,
                            String txtOK,
                            String txtCancel,
                            ICallbackInputText callback) {
        // Создаём поле ввода
        EditText input = new EditText(context);
        input.setHint(hint);
        input.setText(initialText != null ? initialText : "");
        input.setSingleLine(true); // явно указываем однострочный режим

        if(TextUtils.isEmpty(txtOK)) txtOK = "OK";
        if(TextUtils.isEmpty(txtCancel)) txtCancel = "Cancel";

        // Строим и показываем диалог
        new AlertDialog.Builder(context, R.style.URVDarkDialog)
                .setTitle(title)
                .setView(input)
                .setPositiveButton(txtOK, (dialog, which) -> {
                    String enteredText = input.getText().toString();
                    if (callback != null) {
                        callback.onTextEntered(enteredText);
                    }
                })
                .setNegativeButton(txtCancel, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
