package ru.DailyProblemBot.telegram;

import ru.DailyProblemBot.main.Main;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageControl {

    private static final Map<String, List<Integer>> messageHistory = new HashMap<>();

    public static void addMessageToHistory(String chatId, Integer messageId) {
        List<Integer> msgs = messageHistory.getOrDefault(chatId, new ArrayList<>());

        msgs.add(messageId);

        if (!messageHistory.containsKey(chatId)) {
            messageHistory.put(chatId, msgs);
        }
    }

    public static void deleteMessageById(String chatId, Integer messageId) {
        if (messageHistory.containsKey(chatId)) {
            List<Integer> msgs = messageHistory.get(chatId);

            if (msgs.remove(messageId)) {
                deleteMessage(chatId, messageId);
            }
        }
    }

    public static void deleteLastMessage(String chatId) {
        if (messageHistory.containsKey(chatId)) {
            List<Integer> msgs = messageHistory.get(chatId);

            if (msgs.size() != 0) {
                int last = msgs.size() - 1;
                deleteMessage(chatId, msgs.get(last));
                msgs.remove(last);
            }
        }
    }

    public static void deleteAllMessages(String chatId) {
        if (messageHistory.containsKey(chatId)) {
            List<Integer> msgs = messageHistory.get(chatId);
            msgs.parallelStream().forEach(id -> deleteMessage(chatId, id));
            msgs.clear();
        }
    }

    private static void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage del = new DeleteMessage();
        del.setChatId(chatId);
        del.setMessageId(messageId);
        try {
            Main.getTelegramBot().execute(del);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String replaceSpecialEntities(String text) {
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`")
                .replace("~", "\\~");
    }

}
