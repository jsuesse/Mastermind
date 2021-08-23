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
 /*   }

    public void onMessageReceived(MessageReceivedEvent event) {*/

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


              /*  if(args.length == 1){
                    String mess = args[0];

                    switch (mess){
                        //!i help
                        case "help":
                            channel.sendMessage(event.getMember().getAsMention() + " gib '!i comlist' für eine Liste der verfügbaren Commands ein.").queue();
                            break;
                        //!i hi
                        case "hi":
                            double r = (int)(Math.random() * 4);
                            if(r == 3){
                                channel.sendMessage(event.getMember().getAsMention() + " du stinkst!").queue();
                            }
                            else{
                                channel.sendMessage("Hallo " + event.getMember().getAsMention() + "!").queue();
                            }
                            break;
                        //!i time
                        case "time":
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY, HH:mm:ss");

                            channel.sendMessage("Es ist der " + sdf.format(cal.getTime()) + "Uhr!").queue();
                            break;
                        //!i comlist
                        case "comlist":
                            channel.sendMessage("Präfix: !i , Commands(*Mark 5*):"
                                    + "\n" + "hi -> der Bot scheißt dir manchmal aufs Dach"
                                    + "\n" + "time -> der Bot sagt dir Datum und Zeit"
                                    + "\n" + "randnumb-x-y -> Zufallszahl aus der Menge {x,...,y} mit x,y Element der natürlichen Zahlen und x < y"
                                    + "\n" + "role -> gibt all deine Rollen an"
                                    + "\n" + "role-x -> gibt alle Rollen von x an (x muss der Nickname sein)"
                                    + "\n" + "memc -> gibt die Anzahl an Mitlgiedern auf dem Server an").queue();
                            break;
                        //!i role
                        case "role":
                            Member member = event.getMember();

                            channel.sendMessage( "**" + member.getEffectiveName() + "**" + " hat die Rollen:").queue();

                            List<Role> rollen = member.getRoles();
                            int listlength = rollen.size();

                            if (listlength != 0){
                                for (int i = 0; listlength > i; i++) {
                                    String rolle = String.valueOf(rollen.get(i));
                                    String rollens = rolle.replaceAll("[R||:||(||)||0-9||/|| ]+" , "");
                                    if (listlength > ++i) {
                                        channel.sendMessage(rollens + ",").queue();
                                        i--;
                                    }
                                    else{
                                        channel.sendMessage(rollens + ".").queue();
                                    }
                                }
                            }
                            else{
                                channel.sendMessage("*grillenzirpen*").queue();
                            }
                            break;
                        //!i memc
                        case "memc":
                            int nc = guild.getMemberCount();
                            String servername = guild.getName();
                            channel.sendMessage("Die Anzahl an Mitgliedern des Servers **" + servername + "**  beträgt: **" + nc + "**.").queue();
                            break;
                        //!i resetcredits
                        case "resetcredits":
                            Member memberr = event.getMember();
                            String mems = memberr.getEffectiveName();
                            if (mems.equalsIgnoreCase("galo") || mems.equalsIgnoreCase("Nuker")){
                                try {
                                    VoiceJoinListener.reset_textdoc("HashMapUsers.txt");
                                    channel.sendMessage("Credits wurden zurückgesetzt.").queue();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                channel.sendMessage("Keine Berechtigung.").queue();
                            }
                            break;
                        //!i credits
                        case "credits":
                            Member member2 = event.getMember();

                            long memidlong = member2.getIdLong();

                            HashMap hash_map_users;
                            try {
                                hash_map_users = VoiceJoinListener.get_hash_map_users(memidlong, member2, "HashMapUsers.txt");
                            } catch (IOException e) {
                                hash_map_users = null;
                                e.printStackTrace();
                            }

                            int credits = (int) hash_map_users.get(memidlong);
                            channel.sendMessage("Der User **" + member2.getEffectiveName() + "** hat **" + (credits - 1) + "** Credits.").queue();
                            break;
                        default:
                            channel.sendMessage(event.getMember().getAsMention() +" ... Nö!").queue();
                    }
                }
                else if (args.length == 2){
                    //!i role-arg1
                    if (args[0].equalsIgnoreCase("role")) {
                        String mems = args[1];
                        List<Member> memlist = guild.getMembersByEffectiveName(mems, true);
                        int memsize = memlist.size();

                        for (int q = 0; memsize >= 1 ; q++){
                            memsize--;
                            if (q == 0 && memsize != 0){channel.sendMessage("*Der Nickname **" + mems + "** existiert mehrfach ...*").queue();}
                            String memid = String.valueOf(memlist.get(q));
                            String[] memidsubstrings = memid.split(" / ");
                            String memuserid = memidsubstrings[0];
                            String memuseridfertig = memuserid.replaceAll("[A-Z||a-z||:||(||)||/||.|| ]+" , "");

                            if (memuseridfertig.length() > 18){
                                memuseridfertig = memuseridfertig.substring(memuseridfertig.length() - 18);
                                Member membermz = guild.getMemberById(memuseridfertig);
                                channel.sendMessage("**" + membermz.getEffectiveName() + "**" + " hat die Rollen:").queue();

                                List<Role> rollen = membermz.getRoles();
                                int listlength = rollen.size();

                                if (listlength != 0) {
                                    for (int i = 0; listlength > i; i++) {
                                        String rolle = String.valueOf(rollen.get(i));
                                        String rollens = rolle.replaceAll("[R||:||(||)||0-9]+", "");
                                        if (listlength > ++i) {
                                            channel.sendMessage(rollens + ",").queue();
                                            i--;
                                        } else {
                                            channel.sendMessage(rollens + ".").queue();
                                        }
                                    }
                                } else {
                                    channel.sendMessage("*grillenzirpen*").queue();
                                }
                            }
                            else {
                                Member member = guild.getMemberById(memuseridfertig);

                                channel.sendMessage("**" + member.getEffectiveName() + "**" + " hat die Rollen:").queue();

                                List<Role> rollen = member.getRoles();
                                int listlength = rollen.size();

                                if (listlength != 0) {
                                    for (int i = 0; listlength > i; i++) {
                                        String rolle = String.valueOf(rollen.get(i));
                                        String rollens = rolle.replaceAll("[R||:||(||)||0-9]+", "");
                                        if (listlength > ++i) {
                                            channel.sendMessage(rollens + ",").queue();
                                            i--;
                                        } else {
                                            channel.sendMessage(rollens + ".").queue();
                                        }
                                    }
                                } else {
                                    channel.sendMessage("*grillenzirpen*").queue();
                                }
                            }
                        }
                    }
                    //!i credits-arg1
                    else if (args[0].equalsIgnoreCase("credits")) {
                        String mems = args[1];
                        List<Member> memlist = guild.getMembersByEffectiveName(mems, true);
                        int memsize = memlist.size();

                        for (int q = 0; memsize >= 1 ; q++) {

                            memsize--;
                            if (q == 0 && memsize != 0) {
                                channel.sendMessage("*Der Nickname **" + mems + "** existiert mehrfach ...*").queue();
                            }
                            String memid = String.valueOf(memlist.get(q));
                            String[] memidsubstrings = memid.split(" / ");
                            String memuserid = memidsubstrings[0];
                            String memuseridfertig = memuserid.replaceAll("[A-Z||a-z||:||(||)||/||.||?|| ]+", "");

                            if (memuseridfertig.length() > 18) {
                                memuseridfertig = memuseridfertig.substring(memuseridfertig.length() - 18);
                            }

                            long memidlong = Long.parseLong(memuseridfertig);

                            try {
                                if (VoiceJoinListener.get_hash_map_users(memidlong, memlist.get(q), "HashMapUsers.txt") == null){
                                    channel.sendMessage("Dieser User ist ein Bot. Bots sammeln keine Credits!").queue();
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            HashMap hash_map_users;
                            try {
                                hash_map_users = VoiceJoinListener.get_hash_map_users(memidlong, memlist.get(q), "HashMapUsers.txt");
                            } catch (IOException e) {
                                hash_map_users = null;
                                e.printStackTrace();
                            }

                            int credits = (int) hash_map_users.get(memidlong);
                            channel.sendMessage("Der User **" + mems + "** hat **" + (credits - 1) + "** Credits.").queue();
                        }
                    }
                    else{
                        channel.sendMessage(event.getMember().getAsMention() + "... Nö!").queue();
                    }
                }
                else if (args.length == 3){
                    //!i randnumb-arg1-arg2
                    if (args[0].equalsIgnoreCase("randnumb")){

                        if (args[2].length() <= 9 && args[1].length() <= 9) {
                            String maxs = args[2];
                            String mins = args[1];
                            maxs = maxs.replaceAll("[.||,|| ]+", "");
                            mins = mins.replaceAll("[.||,|| ]+", "");
                            int max = Integer.parseInt(maxs);
                            int min = Integer.parseInt(mins);

                            if (maxs != args[2] || mins != args[1]){
                                channel.sendMessage("(In mindestens einem der Werte wurden Zeichen ersetzt.)").queue();
                            }

                            if (min > 0 && max > min && min <= 2147483647 && max <= 2147483647) {
                                int r = (int) (Math.random() * ((max - min) + 1)) + min;
                                channel.sendMessage("Die Zufallszahl lautet: " + r).queue();
                            } else {
                                channel.sendMessage("Ungültige Eingabe ... bitte achte darauf, dass das Maximum echt größer ist als das Minimum und die Werte Element der natürlichen Zahlen sind! (Das Maximum darf höchstens 9 Ziffern haben)").queue();
                            }
                        }
                        else {
                            channel.sendMessage("Ungültige Eingabe ... bitte achte darauf, dass das Maximum echt größer ist als das Minimum und die Werte Element der natürlichen Zahlen sind! (Das Maximum darf höchstens 9 Ziffern haben)").queue();
                        }
                    }
                    else{
                        channel.sendMessage(event.getMember().getAsMention() + " ... Nö!").queue();
                    }
                }
                else{
                    channel.sendMessage(event.getMember().getAsMention() + " ... Nö! (Ungültige Anzahl von Argumenten.)").queue();
                }*/

