package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    private final String BOT_TOKEN = "8417262241:AAG6Hjh6k871j9YWZSzTA0I0yt1pjmUZZjA";
    private final String BOT_USERNAME = "sfatailoring_bot";

    private final long ADMIN_ID = 6555904556L;
    private final long GROUP_ID = -1002588870596L;

    private boolean waitingForCustomMessage = false;

    @Override
    public String getBotToken() { return BOT_TOKEN; }

    @Override
    public String getBotUsername() { return BOT_USERNAME; }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();
            Long chatId = msg.getChatId();
            String text = msg.getText();


            if (text.equals("/start") && chatId.equals(ADMIN_ID)) {
                sendMenu(chatId);
                return;
            }


            if (waitingForCustomMessage && chatId.equals(ADMIN_ID)) {
                sendToGroup(text);
                waitingForCustomMessage = false;
                sendMessage(ADMIN_ID, "Xabar guruhga yuborildi ✅");
                return;
            }


            if (text.equals("Avto xabar berish") && chatId.equals(ADMIN_ID)) {
                sendToGroup("📄 Bugungi hisobot yozildi. Kirib ko'rishingiz mumkin.");
                sendMessage(ADMIN_ID, "Avto xabar yuborildi 🔔");
                return;
            }

            if (text.equals("Xabar berish") && chatId.equals(ADMIN_ID)) {
                waitingForCustomMessage = true;
                sendMessage(ADMIN_ID, "Guruhga yubormoqchi bo‘lgan xabarni yozing:");
            }
        }
    }


    private void sendMenu(long chatId) {
        SendMessage sm = new SendMessage(String.valueOf(chatId),
                "Quyidagi amaldan birini tanlang:");

        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        kb.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add("Avto xabar berish");
        row.add("Xabar berish");

        kb.setKeyboard(List.of(row));
        sm.setReplyMarkup(kb);

        executeSafe(sm);
    }


    private void sendToGroup(String text) {
        SendMessage sm = new SendMessage(String.valueOf(GROUP_ID), text);
        executeSafe(sm);
    }


    private void sendMessage(long chatId, String text) {
        SendMessage sm = new SendMessage(String.valueOf(chatId), text);
        executeSafe(sm);
    }


    private void executeSafe(SendMessage method) {
        try {
            execute(method);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

