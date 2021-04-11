package ru.DailyProblemBot.util;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
import ru.DailyProblemBot.main.Main;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.DailyProblemBot.telegram.MessageControl;

import java.io.File;
import java.util.Objects;

public class Sender {

    private static final int PLACEHOLDERS_LIMIT = 20;

    private final String message;
    private final boolean enableMarkdown;

    public Sender() {
        this("");
    }

    public Sender(String message) {
        this(message, false);
    }

    public Sender(String message, boolean enableMarkdown) {
        this.message = message;
        this.enableMarkdown = enableMarkdown;
    }

    private final String[] cache = new String[PLACEHOLDERS_LIMIT * 2];
    private int placePosition = 0;

    public Sender replace(String from, String to) {
        cache[placePosition] = from;
        cache[placePosition + 1] = to;
        placePosition += 2;
        return this;
    }

    public Sender replace(String from, int to) {
        return replace(from, String.valueOf(to));
    }

    public Sender replace(String from, long to) {
        return replace(from, String.valueOf(to));
    }

    public Sender replace(String from, double to) {
        return replace(from, String.valueOf(to));
    }

    public void send(String chatId) {
        send(chatId, (ReplyKeyboard) null);
    }

    public void send(String chatId, Keyboard keyboard) {
        send(chatId, keyboard.getReplyKeyboardMarkup());
    }

    public void send(String chatId, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
        send(chatId, keyboard.getInlineKeyboard());
    }

    public void send(String chatId, ReplyKeyboard keyboard) {
        SendMessage msg = new SendMessage();
        msg.setReplyMarkup(keyboard);
        msg.enableMarkdown(enableMarkdown);
        msg.setChatId(chatId);
        msg.setText(this.get());
        try {
            Message out = Main.getTelegramBot().execute(msg);
            // Отдельное сохранение id-сообщений бота для дальнейшего удаления
            MessageControl.addMessageToHistory(chatId, out.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(String chatId, String fileEntry) {
        if (fileEntry == null) {
            send(chatId, (ReplyKeyboard) null);
        } else {
            sendPhoto(chatId, fileEntry, (ReplyKeyboard) null);
        }
    }

    public void sendPhoto(String chatId, String fileEntry, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
        if (fileEntry == null) {
            send(chatId, keyboard);
        } else {
            sendPhoto(chatId, fileEntry, keyboard.getInlineKeyboard());
        }
    }

    public void sendPhoto(String chatId, String fileEntry, Keyboard keyboard) {
        if (fileEntry == null) {
            send(chatId, keyboard);
        } else {
            sendPhoto(chatId, fileEntry, keyboard.getReplyKeyboardMarkup());
        }
    }

    private void sendPhoto(String chatId, String fileEntry, ReplyKeyboard keyboard) {
        SendPhoto msg = new SendPhoto();
        msg.setReplyMarkup(keyboard);
        msg.setCaption(this.get());
        msg.setChatId(chatId);

        if (enableMarkdown) msg.setParseMode("markdown");

        File file = null;

        if (fileEntry != null) {
            file = Objects.requireNonNull(Downloader.download(fileEntry));
            msg.setPhoto(new InputFile(file));
        }

        try {
            Message out = Main.getTelegramBot().execute(msg);
            // Отдельное сохранение id-сообщений бота для дальнейшего удаления
            MessageControl.addMessageToHistory(chatId, out.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } finally {
            // Удаляем скачанный документ с диска
            if (file != null) {
                Downloader.deleteFileFromDisk(file);
            }
        }
    }

    public void sendDocument(String chatId, String fileEntry) {
        if (fileEntry == null) {
            send(chatId, (ReplyKeyboard) null);
        } else {
            sendDocument(chatId, fileEntry, (ReplyKeyboard) null);
        }
    }

    public void sendDocument(String chatId, String fileEntry, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
        if (fileEntry == null) {
            send(chatId, keyboard);
        } else {
            sendDocument(chatId, fileEntry, keyboard.getInlineKeyboard());
        }
    }

    public void sendDocument(String chatId, String fileEntry, Keyboard keyboard) {
        if (fileEntry == null) {
            send(chatId, keyboard);
        } else {
            sendDocument(chatId, fileEntry, keyboard.getReplyKeyboardMarkup());
        }
    }

    public void sendDocument(String chatId, String fileEntry, ReplyKeyboard keyboard) {
        SendDocument msg = new SendDocument();
        msg.setReplyMarkup(keyboard);
        msg.setChatId(chatId);
        msg.setCaption(this.get());

        if (enableMarkdown) msg.setParseMode("markdown");

        File file = null;

        if (fileEntry != null) {
            file = Objects.requireNonNull(Downloader.download(fileEntry));
            msg.setDocument(new InputFile(file));
        }

        try {
            Message out = Main.getTelegramBot().execute(msg);
            // Отдельное сохранение id-сообщений бота для дальнейшего удаления
            MessageControl.addMessageToHistory(chatId, out.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } finally {
            // Удаляем скачанный документ с диска
            if (file != null) {
                Downloader.deleteFileFromDisk(file);
            }
        }
    }

    public void sendAnswer(CallbackQuery callbackQuery) {
        sendAnswer(callbackQuery, false);
    }

    public void sendAnswer(CallbackQuery callbackQuery, boolean showAlert) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackQuery.getId());
        answer.setText(this.get());
        answer.setShowAlert(showAlert);

        answer.setCacheTime(10);
        try {
            Main.getTelegramBot().execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

    public String get() {
        return placeholders(message);
    }
}
