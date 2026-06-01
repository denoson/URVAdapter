package com.sas.urvadapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class URVColorPickerFS extends DialogFragment {

    public interface IOnColorSelectedListener {
        void onColorSelected(int color);
    }

    private static final String ARG_INITIAL_COLOR = "initial_color";
    private static final String ARG_ENABLE_ALPHA = "enable_alpha";
    private static final String ARG_OK_TEXT = "ok_text";
    private static final String ARG_CANCEL_TEXT = "cancel_text";
    private static final String ARG_SELECT_TEXT = "select_text";

    // Константы палитры
    private static final int PALETTE_ROWS = 8;
    private static final int PALETTE_COLS = 10;

    // Константы верхней панели HUE
    private static final int NORMAL_HUE_COUNT = 30;
    private static final int TOTAL_HUE_ITEMS = NORMAL_HUE_COUNT + 1; // включая специальный элемент

    // Статическая история (глобальная для всех экземпляров диалога)
    private static final int MAX_HISTORY_SIZE = 20;
    private static final List<Integer> sHistory = new ArrayList<>();

    // Ключи для сохранения состояния
    private static final String STATE_CURRENT_COLOR = "current_color";
    private static final String STATE_CURRENT_ALPHA = "current_alpha";
    private static final String STATE_PRESET_MODE = "preset_mode";
    private static final String STATE_SELECTED_HUE_POS = "selected_hue_pos";
    private static final String STATE_SELECTED_PALETTE_POS = "selected_palette_pos";
    private static final String STATE_CURRENT_HUE = "current_hue";

    public static List<Integer> getHistory() {
        return new ArrayList<>(sHistory);
    }

    public static void setHistory(List<Integer> history) {
        sHistory.clear();
        if (history != null) {
            sHistory.addAll(history);
            while (sHistory.size() > MAX_HISTORY_SIZE) {
                sHistory.remove(0);
            }
        }
    }

    public static void clearHistory() {
        sHistory.clear();
    }

    private static void addToHistory(int color) {
        sHistory.remove(Integer.valueOf(color));
        sHistory.add(0, color);
        while (sHistory.size() > MAX_HISTORY_SIZE) {
            sHistory.remove(sHistory.size() - 1);
        }
    }

    private IOnColorSelectedListener listener;
    private int initialColor;
    private int currentColor;
    private int currentAlpha = 255;
    private boolean enableAlpha;
    private boolean enableNoColorButton = true;
    private boolean horizontal = true;

    private String okText;
    private String cancelText;
    private String selectText;
    private String hexHint;
    private String alphaLabel;

    private LinearLayout hueContainer;
    private GridView paletteGrid;
    private View newColorPreview;
    private View oldColorPreview;
    private TextView hexText;
    private Button cancelButton;
    private Button selectButton;
    private Button noColorButton;
    private View selectedHueView = null;
    private int[] normalHueColors;

    private View alphaPanel;
    private SeekBar alphaSeekBar;
    private TextView alphaValueText;

    private PaletteAdapter normalPaletteAdapter;
    private PresetPaletteAdapter presetPaletteAdapter;
    private BaseAdapter currentPaletteAdapter;

    private boolean presetMode = true; // по умолчанию первый элемент
    private int selectedHuePosition = 0;
    private int selectedPalettePosition = -1;
    private float currentHue = 0f; // для обычного режима

    private boolean updatingHex = false;

    public static URVColorPickerFS newInstance(int initialColor, boolean enableAlpha) {
        return newInstance(initialColor, enableAlpha, null, null, null);
    }

    public static URVColorPickerFS newInstance(int initialColor,
                                               boolean enableAlpha,
                                               String okText,
                                               String cancelText,
                                               String selectText) {
        URVColorPickerFS dialog = new URVColorPickerFS();
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putBoolean(ARG_ENABLE_ALPHA, enableAlpha);
        args.putString(ARG_OK_TEXT, okText);
        args.putString(ARG_CANCEL_TEXT, cancelText);
        args.putString(ARG_SELECT_TEXT, selectText);
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnColorSelectedListener(IOnColorSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogDark);
        hexHint = enableAlpha ? "#AARRGGBB" : "#RRGGBB";

        Bundle args = getArguments();
        if (args != null) {
            initialColor = args.getInt(ARG_INITIAL_COLOR, 0xFF000000);
            enableAlpha = args.getBoolean(ARG_ENABLE_ALPHA, false);
            okText = args.getString(ARG_OK_TEXT, "OK");
            cancelText = args.getString(ARG_CANCEL_TEXT, "Cancel");
            selectText = args.getString(ARG_SELECT_TEXT, "Select");
        }

        // Восстановление состояния
        if (savedInstanceState != null) {
            currentColor = savedInstanceState.getInt(STATE_CURRENT_COLOR, initialColor);
            currentAlpha = savedInstanceState.getInt(STATE_CURRENT_ALPHA, 255);
            presetMode = savedInstanceState.getBoolean(STATE_PRESET_MODE, true);
            selectedHuePosition = savedInstanceState.getInt(STATE_SELECTED_HUE_POS, 0);
            selectedPalettePosition = savedInstanceState.getInt(STATE_SELECTED_PALETTE_POS, -1);
            currentHue = savedInstanceState.getFloat(STATE_CURRENT_HUE, 0f);
        } else {
            currentColor = initialColor;
            if (enableAlpha) {
                currentAlpha = Color.alpha(initialColor);
            } else {
                currentAlpha = 255;
            }
            presetMode = true;
            selectedHuePosition = 0;
            selectedPalettePosition = -1;
            float[] hsv = new float[3];
            Color.colorToHSV(currentColor, hsv);
            currentHue = hsv[0];
        }

        // Цвета для обычных оттенков (30 штук)
        normalHueColors = new int[NORMAL_HUE_COUNT];
        for (int i = 0; i < NORMAL_HUE_COUNT; i++) {
            float hue = (i * 360f) / NORMAL_HUE_COUNT;
            normalHueColors[i] = Color.HSVToColor(new float[]{hue, 1f, 1f});
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_COLOR, currentColor);
        outState.putInt(STATE_CURRENT_ALPHA, currentAlpha);
        outState.putBoolean(STATE_PRESET_MODE, presetMode);
        outState.putInt(STATE_SELECTED_HUE_POS, selectedHuePosition);
        outState.putInt(STATE_SELECTED_PALETTE_POS, selectedPalettePosition);
        outState.putFloat(STATE_CURRENT_HUE, currentHue);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fs_color_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hueContainer = view.findViewById(R.id.hue_container);
        paletteGrid = view.findViewById(R.id.palette_grid);
        newColorPreview = view.findViewById(R.id.new_color_preview);
        oldColorPreview = view.findViewById(R.id.old_color_preview);
        hexText = view.findViewById(R.id.hex_text);
        cancelButton = view.findViewById(R.id.button_cancel);
        selectButton = view.findViewById(R.id.button_select);
        noColorButton = view.findViewById(R.id.button_no_color);

        alphaPanel = view.findViewById(R.id.alpha_panel);
        alphaSeekBar = view.findViewById(R.id.alpha_seekbar);
        alphaValueText = view.findViewById(R.id.alpha_value);

        cancelButton.setText(cancelText);
        selectButton.setText(selectText);

        View hScroll = view.findViewById(R.id.scrollHUE);
        horizontal = hScroll == null;

        // 1. Получаем корневой макет диалога
        final View rootView = view;
        // 2. Устанавливаем слушатель для обработки системных вставок
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            // Получаем вставки для системных панелей (статус бар и навигационная панель)
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Получаем вставки для клавиатуры (IME)
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            // Вычисляем нижний отступ: максимальное из высоты навигационной панели и клавиатуры
            // Когда клавиатура открыта, приоритет у её высоты, чтобы не перекрывать поле ввода HEX
            int bottomPadding = Math.max(systemBarsInsets.bottom, imeInsets.bottom);

            // Устанавливаем отступы для корневого макета
            v.setPadding(
                    systemBarsInsets.left,   // левый отступ (обычно 0, но может пригодиться для вырезов)
                    systemBarsInsets.top,    // верхний отступ для строки состояния
                    systemBarsInsets.right,  // правый отступ (обычно 0)
                    bottomPadding            // нижний отступ для навигационной панели или клавиатуры
            );

            // Сообщаем, что мы обработали вставки
            return insets;
        });

        // 3. Включаем полноэкранный режим, чтобы содержимое могло рисоваться под системными панелями
        //    (но с нашими отступами оно останется видимым)
        Window window = getDialog() != null ? getDialog().getWindow() : null;
        if (window != null) {
            // Отключаем автоматическую подгонку размеров системой, берём управление на себя
            WindowCompat.setDecorFitsSystemWindows(window, false);

            // Обработка выреза экрана (например, камеры)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(params);
            }
        }



        fillHueStrip();

        normalPaletteAdapter = new PaletteAdapter(requireContext(), currentHue);
        presetPaletteAdapter = new PresetPaletteAdapter(requireContext());

        // Восстанавливаем выделение в верхней панели
        setHueSelection(selectedHuePosition);
        if (presetMode) {
            switchToPresetPalette();
        } else {
            switchToNormalPalette();
        }

        // Восстанавливаем выделение в палитре
        if (presetMode) {
            presetPaletteAdapter.setSelectedPosition(selectedPalettePosition);
        } else {
            normalPaletteAdapter.setSelectedPosition(selectedPalettePosition);
        }

        paletteGrid.setNumColumns(PALETTE_COLS);

        // Расчёт высоты строки палитры (безопасный)
        paletteGrid.post(() -> {
            int gridHeight = paletteGrid.getHeight();
            if (gridHeight > 0) {
                float density = getResources().getDisplayMetrics().density;
                int verticalSpacing = (int) (4 * density);
                int paddingTop = paletteGrid.getPaddingTop();
                int paddingBottom = paletteGrid.getPaddingBottom();
                int availableHeight = gridHeight - paddingTop - paddingBottom - (PALETTE_ROWS - 1) * verticalSpacing;
                if (availableHeight > 0) {
                    int rowHeight = availableHeight / PALETTE_ROWS;
                    normalPaletteAdapter.setRowHeight(rowHeight);
                    presetPaletteAdapter.setRowHeight(rowHeight);
                }
            }
        });

        paletteGrid.setOnItemClickListener((parent, v, position, id) -> {
            int color;
            if (presetMode) {
                color = presetPaletteAdapter.getItem(position);
            } else {
                color = normalPaletteAdapter.getItem(position);
            }

            if (enableAlpha) {
                int alpha = Color.alpha(color);
                currentColor = (color & 0x00FFFFFF) | (alpha << 24);
                if(alphaSeekBar != null) alphaSeekBar.setProgress(alpha);
                //currentColor = (color & 0x00FFFFFF) | (currentAlpha << 24); // альтернативный вариант заменять альфа как к alphaSeekBar
            } else {
                currentColor = color | 0xFF000000;
            }

            selectedPalettePosition = position;
            if (presetMode) {
                presetPaletteAdapter.setSelectedPosition(position);
            } else {
                normalPaletteAdapter.setSelectedPosition(position);
            }
            updateBottomPreview();
        });

        // Альфа-канал
        if (enableAlpha) {
            alphaPanel.setVisibility(View.VISIBLE);
            alphaSeekBar.setMax(255);
            alphaSeekBar.setProgress(currentAlpha);
            alphaValueText.setText(String.valueOf(currentAlpha));
            alphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        currentAlpha = progress;
                        alphaValueText.setText(String.valueOf(progress));
                        currentColor = (currentColor & 0x00FFFFFF) | (currentAlpha << 24);
                        updateBottomPreview();
                    }
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        } else {
            alphaPanel.setVisibility(View.GONE);
        }

        // Клик по HEX
        hexText.setOnClickListener(v -> showHexInputDialog());

        cancelButton.setOnClickListener(v -> dismiss());

        selectButton.setOnClickListener(v -> {
            if (listener != null) listener.onColorSelected(currentColor);
            addToHistory(currentColor);
            if (presetPaletteAdapter != null) presetPaletteAdapter.refreshHistory();
            dismiss();
        });

        if(noColorButton != null) {
            if(!enableNoColorButton) noColorButton.setVisibility(View.GONE);
            noColorButton.setOnClickListener(v -> {
                if (listener != null) listener.onColorSelected(0);
                dismiss();
            });
        }

        updateBottomPreview();
    }

    private void fillHueStrip() {
        hueContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        // Специальный первый элемент (серый)
        View specialItem = inflater.inflate(R.layout.item_hue_color, hueContainer, false);
        //setRoundedBackground(specialItem.findViewById(R.id.color_square), 0xFFAAAAAA); // rounded
        specialItem.setBackgroundResource(R.drawable.bck_palette_start);
        specialItem.setOnClickListener(v -> {
            selectedHuePosition = 0;
            setHueSelection(0);
            presetMode = true;
            switchToPresetPalette();
        });
        hueContainer.addView(specialItem);

        // Обычные 30 элементов
        for (int i = 0; i < NORMAL_HUE_COUNT; i++) {
            View item = inflater.inflate(R.layout.item_hue_color, hueContainer, false);
            setRoundedBackground(item.findViewById(R.id.color_square), normalHueColors[i]); // rounded

            final int index = i + 1;
            item.setOnClickListener(v -> {
                selectedHuePosition = index;
                setHueSelection(index);
                presetMode = false;
                currentHue = ((index - 1) * 360f) / NORMAL_HUE_COUNT;
                normalPaletteAdapter.setHue(currentHue);
                normalPaletteAdapter.setSelectedPosition(-1);
                switchToNormalPalette();
            });
            hueContainer.addView(item);
        }

        int sizeDp = 50;
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeDp,
              getResources().getDisplayMetrics());

        hueContainer.setWeightSum(TOTAL_HUE_ITEMS);
        for (int i = 0; i < hueContainer.getChildCount(); i++) {
            View child = hueContainer.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
            if(horizontal) {
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = px;
                params.bottomMargin = px / 10;
            }
             else {
                params.width = px;
            }
            child.setLayoutParams(params);
        }
    }

    private void setHueSelection(int position) {
        if (selectedHueView != null) {
            selectedHueView.findViewById(R.id.selected_border).setVisibility(View.GONE);
        }
        View newSelected = hueContainer.getChildAt(position);
        if (newSelected != null) {
            newSelected.findViewById(R.id.selected_border).setVisibility(View.VISIBLE);
            selectedHueView = newSelected;
        }
    }

    private void switchToNormalPalette() {
        if (currentPaletteAdapter != normalPaletteAdapter) {
            paletteGrid.setAdapter(normalPaletteAdapter);
            currentPaletteAdapter = normalPaletteAdapter;
            normalPaletteAdapter.setSelectedPosition(-1);
        }
    }

    private void switchToPresetPalette() {
        if (currentPaletteAdapter != presetPaletteAdapter) {
            paletteGrid.setAdapter(presetPaletteAdapter);
            currentPaletteAdapter = presetPaletteAdapter;
            presetPaletteAdapter.setSelectedPosition(-1);
        }
    }

    private void updateBottomPreview() {
        newColorPreview.setBackgroundColor(currentColor);
        oldColorPreview.setBackgroundColor(initialColor);

        setRoundedBackground(newColorPreview, currentColor);
        setRoundedBackground(oldColorPreview, initialColor);

        if (enableAlpha) {
            hexText.setText(String.format("#%08X", currentColor));
        } else {
            hexText.setText(String.format("#%06X", (0xFFFFFF & currentColor)));
        }
    }

    private int parseHexColor(String hex, boolean alpha) throws NumberFormatException {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (alpha && hex.length() == 8) {
            return (int) Long.parseLong(hex, 16);
        } else if (!alpha && hex.length() == 6) {
            return 0xFF000000 | Integer.parseInt(hex, 16);
        } else {
            throw new NumberFormatException("Invalid hex length");
        }
    }

    private void setRoundedBackground(View view, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(getResources().getDimension(R.dimen.color_square_radius));
        view.setBackground(drawable);
    }

    private void showHexInputDialog() {
        if (!isAdded()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter HEX color");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (enableAlpha) {
            input.setText(String.format("%08X", currentColor));
            input.setHint(hexHint);
        } else {
            input.setText(String.format("%06X", (0xFFFFFF & currentColor)));
            input.setHint(hexHint);
        }
        input.setSelection(input.getText().length());
        builder.setView(input);

        builder.setPositiveButton(okText, (dialog, which) -> {
            String hex = input.getText().toString().trim();
            try {
                int color = parseHexColor(hex, enableAlpha);
                currentColor = color;
                if (enableAlpha) {
                    currentAlpha = Color.alpha(color);
                    alphaSeekBar.setProgress(currentAlpha);
                    alphaValueText.setText(String.valueOf(currentAlpha));
                }
                selectedPalettePosition = -1;
                if (presetMode) {
                    presetPaletteAdapter.setSelectedPosition(-1);
                } else {
                    normalPaletteAdapter.setSelectedPosition(-1);
                }
                updateBottomPreview();
            } catch (NumberFormatException e) {
                // ignore
            }
        });
        builder.setNegativeButton(cancelText, null);
        builder.show();
    }

    public boolean isEnableNoColorButton() {
        return enableNoColorButton;
    }

    public void setEnableNoColorButton(boolean enableNoColorButton) {
        this.enableNoColorButton = enableNoColorButton;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hueContainer = null;
        paletteGrid = null;
        newColorPreview = null;
        oldColorPreview = null;
        hexText = null;
        cancelButton = null;
        selectButton = null;
        selectedHueView = null;
        alphaPanel = null;
        alphaSeekBar = null;
        alphaValueText = null;
    }

    // ===================== Обычный адаптер =====================
    private class PaletteAdapter extends BaseAdapter {
        private Context context;
        private float hue;
        private int[] colors;
        private int rowHeight = 0;
        private int selectedPosition = -1;

        PaletteAdapter(Context context, float hue) {
            this.context = context;
            this.hue = hue;
            generateColors();
        }

        private void generateColors() {
            int size = PALETTE_ROWS * PALETTE_COLS;
            colors = new int[size];
            for (int row = 0; row < PALETTE_ROWS; row++) {
                float value = 1f - (row / (float) (PALETTE_ROWS - 1));
                for (int col = 0; col < PALETTE_COLS; col++) {
                    float saturation = col / (float) (PALETTE_COLS - 1);
                    colors[row * PALETTE_COLS + col] = Color.HSVToColor(new float[]{hue, saturation, value});
                }
            }
        }

        public void setHue(float hue) {
            this.hue = hue;
            generateColors();
            notifyDataSetChanged();
        }

        public void setRowHeight(int rowHeight) {
            this.rowHeight = rowHeight;
            notifyDataSetChanged();
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return colors.length; }

        @Override
        public Integer getItem(int position) { return colors[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_palette_color, parent, false);
            }
            view.findViewById(R.id.color_square).setBackgroundColor(colors[position]);

            View border = view.findViewById(R.id.selected_border);
            border.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

            View colorSquare = view.findViewById(R.id.color_square);
            setRoundedBackground(colorSquare, colors[position]);

            if (rowHeight > 0) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = rowHeight;
                view.setLayoutParams(params);
            }
            return view;
        }
    }

    // ===================== Адаптер для первого элемента (пресет) =====================
    private class PresetPaletteAdapter extends BaseAdapter {
        private Context context;
        private int[] colors;
        private int rowHeight = 0;
        private int selectedPosition = -1;

        // 40 цветов Material Design (ровно 40)
        private final int[] MATERIAL_COLORS = {
                0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7,
                0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4,
                0xFF009688, 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
                0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722,
                0xFF795548, 0xFF9E9E9E, 0xFF607D8B, 0xFFF06292,
                0xFFBA68C8, 0xFF64B5F6, 0xFF4DB6AC, 0xFF81C784,
                0xFFFFB74D, 0xFFA1887F, 0xFFE57373, 0xFFF8BBD0,
                0xFFB39DDB, 0xFF90CAF9, 0xFF80CBC4, 0xFFA5D6A7,
                0xFFFFCC80, 0xFFBCAAA4, 0xFFEF9A9A, 0xFFC5E1A5,
                0xFFFFF59D, 0xFFCE93D8, 0xFFB0BEC5, 0xFFFFFFFF
        };
        private final int MATERIAL_COUNT = MATERIAL_COLORS.length;

        PresetPaletteAdapter(Context context) {
            this.context = context;
            generateColors();
        }

        private void generateColors() {
            int size = PALETTE_ROWS * PALETTE_COLS;
            colors = new int[size];

            // Строка 0: градация серого (белый -> чёрный)
            for (int col = 0; col < PALETTE_COLS; col++) {
                int gray = 255 - (col * 255 / (PALETTE_COLS - 1));
                colors[0 * PALETTE_COLS + col] = Color.rgb(gray, gray, gray);
            }

            // Строки 1-4: фиксированные цвета из массива MATERIAL_COLORS (первые 40)
            for (int row = 1; row <= 4; row++) {
                int startIndex = (row - 1) * PALETTE_COLS;
                for (int col = 0; col < PALETTE_COLS; col++) {
                    int materialIndex = startIndex + col;
                    if (materialIndex < MATERIAL_COUNT) {
                        colors[row * PALETTE_COLS + col] = MATERIAL_COLORS[materialIndex];
                    } else {
                        colors[row * PALETTE_COLS + col] = 0xFF000000; // fallback
                    }
                }
            }

            // Строка 5: чёрная строка-разделитель
            for (int col = 0; col < PALETTE_COLS; col++) {
                colors[5 * PALETTE_COLS + col] = 0xFF000000;
            }

            // Строки 6-7: история (по 10 цветов в каждой)
            for (int i = 0; i < 2 * PALETTE_COLS; i++) {
                int row = 6 + (i / PALETTE_COLS);
                int col = i % PALETTE_COLS;
                if (i < sHistory.size()) {
                    colors[row * PALETTE_COLS + col] = sHistory.get(i);
                } else {
                    colors[row * PALETTE_COLS + col] = 0xFF000000;
                }
            }
        }

        public void refreshHistory() {
            // Обновляем только строки истории (6 и 7)
            for (int i = 0; i < 2 * PALETTE_COLS; i++) {
                int row = 6 + (i / PALETTE_COLS);
                int col = i % PALETTE_COLS;
                if (i < sHistory.size()) {
                    colors[row * PALETTE_COLS + col] = sHistory.get(i);
                } else {
                    colors[row * PALETTE_COLS + col] = 0xFF000000;
                }
            }
            notifyDataSetChanged();
        }

        public void setRowHeight(int rowHeight) {
            this.rowHeight = rowHeight;
            notifyDataSetChanged();
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return colors.length; }

        @Override
        public Integer getItem(int position) { return colors[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_palette_color, parent, false);
            }
            view.findViewById(R.id.color_square).setBackgroundColor(colors[position]);

            View border = view.findViewById(R.id.selected_border);
            border.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

            View colorSquare = view.findViewById(R.id.color_square);
            setRoundedBackground(colorSquare, colors[position]);

            if (rowHeight > 0) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = rowHeight;
                view.setLayoutParams(params);
            }
            return view;
        }
    }

    // Удобный метод для показа
    public static void show(FragmentManager fm, int initialColor, boolean enableAlpha, IOnColorSelectedListener listener) {
        URVColorPickerFS dialog = newInstance(initialColor, enableAlpha);
        dialog.setOnColorSelectedListener(listener);
        dialog.show(fm, "color_picker");
    }

    // Перегруженный метод для кастомизации текстов
    public static void show(FragmentManager fm,
                            int initialColor,
                            boolean enableAlpha,
                            String okText,
                            String cancelText,
                            String selectText,
                            IOnColorSelectedListener listener) {
        URVColorPickerFS dialog = newInstance(initialColor, enableAlpha,
                okText, cancelText, selectText);
        dialog.setOnColorSelectedListener(listener);
        dialog.show(fm, "color_picker");
    }
}