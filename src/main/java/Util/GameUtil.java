package Util;
import games.Mastermind;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GameUtil {
    private static final HashMap<Long, Mastermind> games = new HashMap<>();

    //    public static void setGame(long userId, Game game) {
    //        games.put(userId, game);
    //    }
    //
    //    public static boolean hasGame(long userId) {
    //        return games.containsKey(userId);
    //    }
    //
    //    public static Game getGame(long userId) {
    //        return games.get(userId);
    //    }
    //
    //    public static void removeGame(long userId) {
    //        games.remove(userId);
    //    }
    //sendGameEmbed
    //String desc= Wähle mit den Reaktionen deine x.te Farbe \n <- löscht den letzten Knubbel, << setzt den Guess komplett zurück
    // <> startet das Spiel neu
    //X beendet das Spiel
    //firstfield/secondfield =:blue_circle: :yellow_circle: :green_circle: :white_circle: \n \n " +
    //                            ":yellow_circle: :green_circle: :white_circle: :red_circle: \n\n" +
    //                            ":o: :o: :o: :o:"
    //etc
    //onMessageReactionAdd(); schreiben?
    public static void setGame(long userId, Mastermind game) {
        games.put(userId, game);
    }

    public static boolean hasGame(long userId) {
        return games.containsKey(userId);
    }

    public static Mastermind getGame(long userId) {
        return games.get(userId);
    }

    public static void removeGame(long userId) {
        games.remove(userId);
    }

    public static void sendGameEmbed(MessageChannel channel, String desc, User user, String firstfield, String secondfield) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Mastermind: " + user.getName());
        embed.setDescription(desc);
        embed.addField("Guesses",firstfield,true);
        embed.addField("Hits",secondfield, true);
        embed.addField("Player", user.getAsMention(), false);
        channel.sendMessage(embed.build()).queue();
    }
    public static void updateGameEmbed(Message message, String desc, User user, String firstfield, String secondfield) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Mastermind: " + user.getName());
        embed.setDescription(desc);
        embed.addField("Guesses",firstfield,true);
        embed.addField("Hits",secondfield, true);
        embed.addField("Player", user.getAsMention(), false);
        message.editMessage(embed.build()).queue();
    }
    public static void sendWinEmbed(Message message, String desc, User user, String firstfield, String secondfield, String result, String code) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Mastermind: " + user.getName());
        embed.setDescription("YOU "+result.toUpperCase());
        embed.addField("Guesses",firstfield,true);
        embed.addField("Hits",secondfield, true);
        embed.addField("Der Code war", code , false);

        embed.addField("Player", user.getAsMention(), false);
        message.editMessage(embed.build()).queue();
    }



}
