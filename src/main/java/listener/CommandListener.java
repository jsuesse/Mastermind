package listener;


import Util.GameUtil;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import games.coinflip;
import games.Mastermind;

import java.util.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        if (user.getId().equals(event.getJDA().getSelfUser().getId())) {
            List<MessageEmbed> embeds = message.getEmbeds();
            if (embeds.size() > 0) {
                MessageEmbed embed = embeds.get(0);
                if (embed.getTitle() != null && embed.getTitle().length() > 0) {
                    if (embed.getTitle().startsWith("Mastermind")) {
                        message.addReaction("U+1F535").queue();
                        message.addReaction("U+1F535").queue();
                        message.addReaction("U+1F7E2").queue();
                        message.addReaction("U+1F534").queue();
                        message.addReaction("U+1F7E1").queue();
                        message.addReaction("U+1F7E0").queue();
                        message.addReaction("U+26AA").queue();
                        message.addReaction("U+2B55").queue();
                        message.addReaction("U+2B05").queue();
                        message.addReaction("U+23EA").queue();
                        message.addReaction("U+1F501").queue();
                        message.addReaction("U+1F1FD").queue();
                        List<MessageEmbed.Field> fields = embed.getFields();
                        for (MessageEmbed.Field field : fields) {
                            if (field.getName() != null && field.getName().equals("Player")) {
                                if (field.getValue() != null) {
                                    long playerId = Long
                                            .parseLong(field.getValue().substring(2, field.getValue().length() - 1));
                                    if (GameUtil.hasGame(playerId)) {
                                        Mastermind game = GameUtil.getGame(playerId);
                                        game.setGameMessage(message);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return; //Ende der IF-Kaskade
        }
    //sonst onMessageReceived
 /*   } public void onMessageReceived(MessageReceivedEvent event) {*/

    String wholemessage = event.getMessage().getContentDisplay();  //getcontentraw
            //!i arg0-arg1- ... || !I arg0-arg1- ...
            if(wholemessage.startsWith("!t ") || wholemessage.startsWith("!T ")){
               // String[] args = message.substring(3).split("-");
                //event.getMember().getAsMention() @User
                String text=wholemessage.substring(3);
                //channel.sendMessage(message).queue();

                if (text.equals("flip")){ //primitiver Coinflip
                    coinflip coinflipobject=new coinflip();
                    channel.sendMessage(coinflipobject.coinflippen()).queue();
                }else if(text.startsWith("mastermind")) {
                    Mastermind game;
                    String userInput=text.replace("mastermind ","");
                    if (!GameUtil.hasGame(user.getIdLong())) {
                        game = new Mastermind(user);
                        GameUtil.setGame(user.getIdLong(), game);
                    } else game = GameUtil.getGame(user.getIdLong());
                    //
                   // String userInput = event.getName().toLowerCase();
                    if (userInput.equals("play")||userInput.equals("")) {
                        if (game.gameActive) {
                            event.getChannel().sendMessage(user.getAsMention() + ", you already have an active game.\nUse `" + "!t mastermind "
                                    + "stop` to stop your current game first.").queue();
                        }
                    }

                    game.run(event.getGuild(), channel, userInput); //guesscounter 7
     //               if (userInput.equals("stop")) GameUtil.removeGame(user.getIdLong());
                    if ((game.gameActive ||game.hasWon() || game.getGuesscounter()==7) && guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
                        event.getMessage().delete().queue();
                }
                else {
                    channel.sendMessage("Das darfst du nicht!").queue();
                }
            }
    }
}