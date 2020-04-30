package com.khye;

import java.util.List;

import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * EventListener
 */
public class EventListener extends ListenerAdapter {
    private static Logger log = LoggerFactory.getLogger(EventListener.class);
    private Configuration config;
    private RedditIngestor redditIngestor;

    public EventListener(Configuration config) {
        this.config = config;
        redditIngestor = new RedditIngestor(config);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return; // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g.
        // console view (strip discord formatting)
        MessageChannel channel = event.getChannel();
        content = content.toLowerCase();
        switch (content) {
            case "!ping":
                channel.sendMessage("Use NOED if smol pp").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
                break;

            case "!dbdmeme":
                String source = config.getReddit().getMemeSource();
                List<JSONObject> memesJson = redditIngestor.getHot(source, null, null, 0, 100, config.getApp().getDefaultNMemes());
                String msg = parseToMessage(memesJson);
                channel.sendMessage(msg).queue();;
                break;

            default:
                break;
        }
    }

    private String parseToMessage(List<JSONObject> jsonEntries) {
        StringBuilder strBuilder = new StringBuilder();
        for (JSONObject entry : jsonEntries) {
            String title = entry.getString("title");
            String url = entry.getString("url");
            strBuilder.append(String.format("%s : %s\n", title, url));
            
            // hide post
            String fullName = entry.getString("name");
            redditIngestor.hidePost(fullName);
        }
        log.debug(strBuilder.toString());
        return strBuilder.toString();
    }
}