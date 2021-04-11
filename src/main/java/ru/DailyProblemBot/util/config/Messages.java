package ru.DailyProblemBot.util.config;

import ru.DailyProblemBot.enums.InlineKeyboard;
import ru.DailyProblemBot.enums.Keyboard;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.DailyProblemBot.util.JSONConfig;
import ru.DailyProblemBot.util.Sender;

import java.util.ArrayList;
import java.util.List;

public enum Messages {

	afkUnLogin,
	unlogin,
	start,
	auth,
	signin_login, signin_password, signin_errorSignInData,
	signup_name, signup_studentId,
	signup_email, signup_password(false),
	signup_errorPattern, signup_success,
	logout, banned,
	main,
	fatalError,
	notEnoughPerms,

	tasks_create_common,
	tasks_create_description,
	tasks_create_descriptionPattern,
	tasks_create_title,
	tasks_create_titlePattern,
	tasks_create_cost,
	tasks_create_files,
	tasks_create_fileAdded,
	tasks_create_fileTypeOrSizeNotAllowed,
	tasks_create_address,
	tasks_create_summary,
	tasks_create_confirm,
	tasks_create_cancel,
	tasks_create_created,

	tasks_vote_taskFormat,
	tasks_vote_intro,
	tasks_vote_nothingToShow,
	tasks_vote_alreadyVoted,

	tasks_showAll_intro,
	tasks_showAll_taskFormat,
	tasks_showAll_nothingToShow,

	tasks_showMy_intro,
	tasks_showMy_taskFormat,
	tasks_showMy_nothingToShow,
	tasks_showMy_successfullyDeleted,

	tasks_edit_intro,
	tasks_edit_taskFormat,
	tasks_edit_nothingToShow,
	tasks_edit_status,
	tasks_edit_votesLimit,
	tasks_edit_votesPattern,
	tasks_edit_cost,
	tasks_edit_address,
	tasks_edit_description,
	tasks_edit_title,
	tasks_edit_confirm,
	tasks_edit_changesApply,
	tasks_edit_changesCancel,

	tasks_editing_intro,
	tasks_editing_taskFormat,
	tasks_editing_nothingToShow,

	tasks_inlineAnswer_downloadFiles,
	tasks_inlineAnswer_vote,
	tasks_inlineAnswer_deleteTask,
	tasks_inlineAnswer_deleteTaskPerm,
	tasks_inlineAnswer_editTask,
	tasks_inlineAnswer_editTaskPerm,
	tasks_inlineAnswer_seeVotesYes,
	tasks_inlineAnswer_seeVotesNo;

	Messages() {}

	Messages(boolean enableMarkdown) {
		this.enableMarkdown = enableMarkdown;
	}

	
	private String message;
	private boolean enableMarkdown = true;
	
	public static void load(JSONConfig c) {
		for (Messages msg : values()) {
			try {

				Object obj = c.get(msg.name().replace("_", "."));

				if (obj instanceof String) {
					msg.message = obj.toString();
				} else if (obj instanceof List<?>) {
					msg.message = String.join("\n", (ArrayList<String>) obj);
				} else {
					msg.message = "";
				}

			} catch (NullPointerException e) {
				System.err.println(Messages.class.getCanonicalName() + " | NullPointerException. Path: '" + msg.name().replace("_", ".") + "'.");
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.err.println(Messages.class.getCanonicalName() + " | ClassCastException. Path: '" + msg.name().replace("_", ".") + "'.");
				e.printStackTrace();
			}
		}
	}
	
	public String get() {
		return Messages.this.message;
	}

	public void send(String chatId) {
		new Sender(this.message, enableMarkdown).send(chatId);
	}

	public void send(String chatId, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
		new Sender(this.message, enableMarkdown).send(chatId, keyboard);
	}

	public void send(String chatId, Keyboard keyboard) {
		new Sender(this.message, enableMarkdown).send(chatId, keyboard);
	}

	public void sendPhoto(String chatId, String fileId) {
		new Sender(this.message, enableMarkdown).sendPhoto(chatId, fileId);
	}

	public void sendPhoto(String chatId, String fileId, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
		new Sender(this.message, enableMarkdown).sendPhoto(chatId, fileId, keyboard);
	}

	public void sendPhoto(String chatId, String fileId, Keyboard keyboard) {
		new Sender(this.message, enableMarkdown).sendPhoto(chatId, fileId, keyboard);
	}

	public void sendDocument(String chatid, String fileId) {
		new Sender(this.message, enableMarkdown).sendDocument(chatid, fileId);
	}

	public void sendDocument(String chatid, String fileId, InlineKeyboard.ReplaceInlineKeyboard keyboard) {
		new Sender(this.message, enableMarkdown).sendDocument(chatid, fileId, keyboard);
	}

	public void sendDocument(String chatid, String fileId, Keyboard keyboard) {
		new Sender(this.message, enableMarkdown).sendDocument(chatid, fileId, keyboard);
	}

	public void sendAnswer(CallbackQuery callbackQuery) {
		new Sender(this.message, enableMarkdown).sendAnswer(callbackQuery);
	}

	public void sendAnswer(CallbackQuery callbackQuery, boolean showAlert) {
		new Sender(this.message, enableMarkdown).sendAnswer(callbackQuery, showAlert);
	}

	// Первая замена выполняется здесь, последующие в классе MessageSender
	public Sender replace(String from, String to) {
		return new Sender(this.message, enableMarkdown).replace(from, to);
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


}
