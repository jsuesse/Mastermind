package listener;



import core.BOT;
import games.Mastermind;
import Util.GameUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GameListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        User user = event.getUser();
        if (user.isBot()) {
            return;
        }
        Guild guild = event.getGuild();
        MessageReaction reaction = event.getReaction();
        TextChannel channel = event.getChannel();
        channel.retrieveMessageById(event.getMessageId()).queue(message -> {
            if (message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                Mastermind game;
                if (!GameUtil.hasGame(user.getIdLong())) {
                    game = new Mastermind(user);
                    GameUtil.setGame(user.getIdLong(), game);
                } else game = GameUtil.getGame(user.getIdLong());
                boolean reactionCommand = true;
                String userInput = "";
                switch (event.getReactionEmote().toString()) {
                    //U+1F535 blau U+1F7E2 grün U+1F534 rot U+1F7E1 gelb U+1F7E0 orange U+26AA weiß U+2B55 ring  U+23EA rewind U+1F501 replay U+2B05 arrow left
                    case "RE:U+1f535":
                        userInput = "blau";
                        break;
                    case "RE:U+1f7e2":
                        userInput = "grün";
                        break;
                    case "RE:U+1f534":
                        userInput = "rot";
                        break;
                    case "RE:U+1f7e1":
                        userInput = "gelb";
                        break;
                    case "RE:U+1f7e0":
                        userInput = "orange";
                        break;
                    case "RE:U+26aa":
                        userInput = "weiß";
                        break;
                   // case "RE:U+2B55":
                   //     userInput = "ring";
                  //      break;
                    case "RE:U+23ea":
                        userInput = "rewind";
                        break;
                    case "RE:U+1f501":
                        userInput = "replay";
                        break;
                    case "RE:U+2b05":
                        userInput = "back";
                        break;
                    case "RE:U+1f1fd":
                        userInput = "stop";
                        break;
                    default:
                        reactionCommand = false;
                        break;
                }
                if (reactionCommand) game.run(guild, channel, userInput);
                if (guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
                    reaction.removeReaction(user).queue();
            }
        });
    }
}
