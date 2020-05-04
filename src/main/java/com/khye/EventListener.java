package com.khye;

import java.awt.Color;
import java.util.List;

import com.khye.config.Configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (!content.startsWith("!"))
            return; // don't do unecessary parsing if it's not a command
        content = content.toLowerCase();
        String[] splitted = content.split(" ");
        switch (splitted[0]) {
            case "!ping":
                channel.sendMessage("Use NOED if smol pp").queue();
                break;

            case "!dbdmeme":
                sendToDiscord(channel, constructMemes(channel, splitted));
                break;

            default:
                break;
        }
    }

    private List<JSONObject> constructMemes(MessageChannel channel, String[] command) {
        List<JSONObject> memesJson = null;
        String source = config.getReddit().getMemeSource();
        // populate memesJson
        if (command.length < 2) {
            // handle single argument
            // defaults to reddit:hot and 1 post
            memesJson = redditIngestor.getContent(source + "hot.json", null, null, 0, 100, config.getApp().getDefaultNMemes());
        } else {
            int nPosts = command.length == 3 ? Integer.parseInt(command[2]) : config.getApp().getDefaultNMemes();
            switch (command[1]) {
                case "top":
                    memesJson = redditIngestor.getContent(source + "top.json", null, null, 0, 100, nPosts);
                    break;
                
                case "new":
                    memesJson = redditIngestor.getContent(source + "new.json", null, null, 0, 100, nPosts);
                    break;

                case "rising":
                    memesJson = redditIngestor.getContent(source + "rising.json", null, null, 0, 100, nPosts);
                    break;
                
                case "hot":
                    memesJson = redditIngestor.getContent(source + "hot.json", null, null, 0, 100, nPosts);
                    break;
            
                default:
                    if (StringUtils.isNumeric(command[1])) {
                        // is a number
                        memesJson = redditIngestor.getContent(source + "hot.json", null, null, 0, 100, Integer.parseInt(command[1]));
                    } else {
                        // unsupported    
                        channel.sendMessage("Unsupported Command").queue();
                    }
                    break;
            }                
        }
        return memesJson;
    }

    private void sendToDiscord(MessageChannel channel, List<JSONObject> memesJson) {
        for (JSONObject meme : memesJson) {
            channel.sendMessage(parseToEmbed(meme)).queue();
            redditIngestor.hidePost(meme.getString("name"));
        }
    }

    private MessageEmbed parseToEmbed(JSONObject entry) {
        EmbedBuilder emBuilder = new EmbedBuilder();
        // String title = String.format("%s - :fire: %s :fire:",
        // entry.getString("title"), entry.getString("score"));
        // emBuilder.setTitle(title);
        emBuilder.setTitle(entry.getString("title"));
        emBuilder.addField("Score", entry.getString("score"), false);
        emBuilder.setDescription(config.getReddit().getBase() + entry.getString("permalink"));
        emBuilder.setFooter("Unfortunately, the bot doesn't work for v.redd.it links");
        emBuilder.setColor(Color.CYAN);
        emBuilder.setAuthor(entry.getString("author"));
        emBuilder.setImage(entry.getString("url"));
        return emBuilder.build();
    }

    @SuppressWarnings("unused")
    private String parseToTextMessage(List<JSONObject> jsonEntries) {
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