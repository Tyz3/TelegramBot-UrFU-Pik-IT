package ru.DailyProblemBot.enums;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public enum Keyboard {

    START(new String[]{"START"}),
    AUTH(new String[]{"Регистрация", "Войти"}),
    MAIN_USER(new String[]{"Создать заявку", "Голосование"}, new String[]{"Свои заявки", "Все заявки", "Выйти"}),
    MAIN_EXPERT(new String[]{"Голосование", "Все заявки"}, new String[]{"Выйти"}),
    MAIN_MODER(new String[]{"Управление заявками", "Все заявки"}, new String[]{"Редактируемые заявки", "Выйти"}),
    MAIN_ROOT(
            new String[]{"Создать заявку", "Голосование", "Свои заявки"},
            new String[]{"Управление заявками", "Все заявки"},
            new String[]{"Редактируемые заявки", "Выйти"}
    ),

    TASK_CREATE_DESCRIPTION(new String[]{"Удалить заявку"}),
    TASK_CREATE_TITLE(new String[]{"Удалить заявку", "Авто-название"}),
    TASK_CREATE_COST(new String[]{"Удалить заявку", "Пропустить"}),
    TASK_CREATE_FILES(new String[]{"Удалить заявку", "Пропустить/Продолжить"}),
    TASK_CREATE_ADDRESS(new String[]{"Удалить заявку", "Пропустить"}),
    TASK_CREATE_CONFIRM(new String[]{"Нет, удаляем", "Подтвердить"}),

    TASK_VOTING(new String[]{"Пред. заявка", "След. заявка"}, new String[]{"В главное меню"}),
    TASK_SHOWALL(new String[]{"Пред. страница", "След. страница"}, new String[]{"В главное меню"}),
    TASK_SHOWMY(new String[]{"Пред. страница", "След. страница"}, new String[]{"В главное меню"}),
    TASK_EDIT(new String[]{"Пред. заявка", "След. заявка"}, new String[]{"В главное меню"}),
    TASK_EDITING(new String[]{"Пред. заявка", "След. заявка"}, new String[]{"В главное меню"}),

    TASK_EDIT_STATUS(
            new String[]{"Голосование (студенты)", "Модерация"},
            new String[]{"Голосование (эксперты)", "Неактивна"},
            new String[]{"Новая", "Осуществление работ", "Выполнена"},
            new String[]{"Пропустить", "Пропустить всё"}
    ),
    TASK_EDIT_VOTES_LIMIT(
            new String[]{"3", "20", "100", "250", "500"},
            new String[]{"Пропустить", "Пропустить всё"}
    ),
    TASK_EDIT_COST(new String[]{"Пропустить", "Пропустить всё"}),
    TASK_EDIT_ADDRESS(new String[]{"Пропустить", "Пропустить всё"}),
    TASK_EDIT_DESCRIPTION(new String[]{"Пропустить", "Пропустить всё"}),
    TASK_EDIT_TITLE(new String[]{"Пропустить"}),
    TASK_EDIT_CONFIRM(new String[]{"Отменить", "Применить"});

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }

    Keyboard(String[]... rows) {
        this(true, true, true, rows);
    }

    Keyboard(boolean selective, boolean resize, boolean oneTime, String[]... rows) {

        replyKeyboardMarkup.setSelective(selective);
        replyKeyboardMarkup.setResizeKeyboard(resize);
        replyKeyboardMarkup.setOneTimeKeyboard(oneTime);

        // Создаём список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();


        for (String[] row : rows) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (String button : row) {
                keyboardRow.add(button);
            }

            // Добавление в список
            keyboard.add(keyboardRow);
        }

        // Установка клавиатуры
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
