package ru.DailyProblemBot.util.config;

import ru.DailyProblemBot.util.JSONConfig;

public enum Config {
	
	botUsername, botToken,
	patterns_name, patterns_email, patterns_password, patterns_studentId,

	simpleDateFormat
	;
	
	private String setting;
	
	public static void load(JSONConfig c) {
		for (Config param : values()) {
			try {

				param.setting = c.getString(param.name().replace("_", "."), "nothing");

			} catch (NullPointerException e) {
				System.err.println(Config.class.getCanonicalName() + " | NullPointerException. Path: '" + param.name().replace("_", ".") + "'.");
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.err.println(Config.class.getCanonicalName() + " | ClassCastException. Path: '" + param.name().replace("_", ".") + "'.");
				e.printStackTrace();
			}
		}
	}
	
	public String get() {
		return Config.this.setting;
	}

}
