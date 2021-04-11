package ru.DailyProblemBot.enums;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public enum InlineKeyboard {

    TASK_VOTE(
            new String[]{"Голосую За ({share+}%)|{taskId}:vote_yes", "Голосую Против ({share-}%)|{taskId}:vote_no"},
            new String[]{"Скачать файлы|{taskId}:download_files"}
    ),
    TASK_SHOWALL(
            new String[]{"ЗА {share+}%|{taskId}:count_votes_yes", "ПРОТИВ {share-}%|{taskId}:count_votes_no"},
            new String[]{"Скачать файлы|{taskId}:download_files"}
    ),
    TASK_SHOWMY_1(
            new String[]{"ЗА {share+}%|{taskId}:count_votes_yes", "ПРОТИВ {share-}%|{taskId}:count_votes_no"},
            new String[]{"Удалить|{taskId}:delete_task", "Скачать файлы|{taskId}:download_files"}
    ),
    TASK_SHOWMY_2(
            new String[]{"ЗА {share+}%|{taskId}:count_votes_yes", "ПРОТИВ {share-}%|{taskId}:count_votes_no"},
            new String[]{"Скачать файлы|{taskId}:download_files"}
    ),
    TASK_EDIT(
            new String[]{"Редактировать|{taskId}:edit_task", "Удалить|{taskId}:delete_task"},
            new String[]{"Скачать файлы|{taskId}:download_files"}
    ),
    TASK_EDITING(
            new String[]{"Редактировать|{taskId}:edit_task", "Удалить|{taskId}:delete_task"},
            new String[]{"Скачать файлы|{taskId}:download_files"}
    );

    private final String[][] rows;

    InlineKeyboard(String[]... rows) {

        this.rows = rows;
    }

    public ReplaceInlineKeyboard getInlineKeyboard() {
        return new ReplaceInlineKeyboard();
    }

    public ReplaceInlineKeyboard replace(String from, String to) {
        return new ReplaceInlineKeyboard().replace(from, to);
    }

    public ReplaceInlineKeyboard replace(String from, int to) {
        return replace(from, String.valueOf(to));
    }

    public ReplaceInlineKeyboard replace(String from, long to) {
        return replace(from, String.valueOf(to));
    }

    public ReplaceInlineKeyboard replace(String from, double to) {
        return replace(from, String.valueOf(to));
    }

    public class ReplaceInlineKeyboard {

        private static final int PLACEHOLDERS_LIMIT = 6;

        private final String[] cache = new String[PLACEHOLDERS_LIMIT * 2];
        private int placePosition = 0;

        public ReplaceInlineKeyboard replace(String from, String to) {
            cache[placePosition] = from;
            cache[placePosition + 1] = to;
            placePosition += 2;
            return this;
        }

        public ReplaceInlineKeyboard replace(String from, int to) {
            return replace(from, String.valueOf(to));
        }

        public ReplaceInlineKeyboard replace(String from, long to) {
            return replace(from, String.valueOf(to));
        }

        public ReplaceInlineKeyboard replace(String from, double to) {
            return replace(from, String.valueOf(to));
        }


        public InlineKeyboardMarkup getInlineKeyboard() {
            // TODO не самая удачная реализация подстановки вместе со сборкой

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();

            for (String[] row : InlineKeyboard.this.rows) {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();

                for (String text : row) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    String[] args = text.split("\\|");

                    button.setText(placeholders(args[0]));
                    if (args.length == 2) {
                        button.setCallbackData(placeholders(args[1]));
                    } else {
                        button.setCallbackData("");
                    }

                    buttonRow.add(button);
                }

                // Добавление в список
                buttonRows.add(buttonRow);
            }

            inlineKeyboardMarkup.setKeyboard(buttonRows);

            return inlineKeyboardMarkup;
        }

        private String placeholders(String input) {

            if (placePosition != 0 && cache.length % 2 == 0 && cache.length > 1) {
                for (int i = 0; i < cache.length; i += 2) {
                    if (cache[i] == null) break;
                    input = input.replace(cache[i], cache[i + 1]);
                }
            }

            return input;
        }
    }
}
