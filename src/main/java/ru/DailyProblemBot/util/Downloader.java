package ru.DailyProblemBot.util;

import ru.DailyProblemBot.main.Main;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Downloader {

    public static final String DOWNLOAD_DIR = "/";

    private static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException {

        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File download(String fileEntry) {
        /*
        Формат fileEntry
        img:jpg:fileId
        doc:docx:fileId
         */
        String[] args = fileEntry.split(":");

        String fileName = "";
        String fileId = "";

        if (args[0].equals("img")) {
            fileName = args[2].concat(".").concat(args[1]);
            fileId = args[2];
        } else if (args[0].equals("doc")) {
            fileName = args[2];
            fileId = args[3];
        }

        try {
            JSONObject json = readJsonFromUrl("https://api.telegram.org/bot".concat(Main.getTelegramBot().getBotToken()).concat("/getFile?file_id=").concat(fileId));
            String namePath = json.getJSONObject("result").getString("file_path");
            String newURL = "https://api.telegram.org/file/bot".concat(Main.getTelegramBot().getBotToken()).concat("/").concat(namePath);

            File file = new File(DOWNLOAD_DIR.concat(fileName));

            FileUtils.copyURLToFile(new URL(newURL), file);

            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteFileFromDisk(File file) {
        return file.delete();
    }

    public static String makeDownloadableEntryFromPhoto(List<PhotoSize> photoSizes) {

        // Выбираем фото с максимальным сжатием
        PhotoSize photo = photoSizes.get(0);

        // img:jpg:fileId
        return "img:".concat("jpg").concat(":").concat(photo.getFileId());
    }

    public static String makeDownloadableEntryFromDocument(Document document) {

        // doc:fileType:fileName.docx:fileId
        if (fileTypeAllowed(document.getMimeType()) && document.getFileSize() <= Main.getSettings().LOADED_FILE_MAX_SIZE) {
            return "doc:".concat(document.getMimeType()).concat(":").concat(document.getFileName()).concat(":").concat(document.getFileId());
        } else {
            return "";
        }
    }

    private static boolean fileTypeAllowed(String type) {
        return Main.getSettings().LOADED_FILES_WHITELIST.contains(type);
    }

}
