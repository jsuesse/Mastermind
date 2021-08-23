package games;

import core.*;
import Util.GameUtil;
import net.dv8tion.jda.api.entities.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;


public class Mastermind {

    long gameMessageID;
    long channelID;
    public boolean gameActive = false;
    User user;
    public long lastAction;


    Guess code;
    Guess[] guesslist = new Guess[7];
    String[] allcolors={"blau","grün","rot","gelb","orange","weiß"};
    int[] hitlist=new int[7];
    int[] critlist=new int[7];
    int hits=0;
    int crits=0;
    Guess currentguess;//redundant
    int guesscounter=0;
    int colorcounter=0;
    String desc;

    String knownfirstfield, unknownfirstfield, firstfield,knownsecondfield, unknownsecondfield, secondfield="";
    public Mastermind(User user) {
        this.user = user;
    }

    public void setGameMessage(Message gameMessage) {
        // To avoid an Unknown Message error, we will store the IDs and retrieve the Channel object when needed.
        gameMessageID = gameMessage.getIdLong();
        channelID = gameMessage.getChannel().getIdLong();
    }
    public int getGuesscounter(){
        return guesscounter;
    }

    public void generateinitialfields() {
    //TODO
        knownfirstfield=knownsecondfield=" ";
        unknownfirstfield=":o: :o: :o: :o: \n\n";
        for (int i=0;i<6;i++) unknownfirstfield += ":o: :o: :o: :o: \n\n";
        unknownsecondfield=unknownfirstfield;
        mergefields();
        updatedesc();
    }
    public void  generateguessatindex(int index){
        guesslist[index]=new Guess();
    }

    public void newGame(MessageChannel channel) {
        if (!gameActive) {
            gameActive = true;
            lastAction = System.currentTimeMillis();
            generateinitialfields();
            guesslist = new Guess[7];
            hitlist=new int[7];
            critlist=new int[7];
            generateguessatindex(0);
            randomizeCode();
            hits=0;
            crits=0;
            guesscounter=0;
            colorcounter=0;
            GameUtil.sendGameEmbed(channel,desc,user,firstfield, secondfield);
        }
    }

    public void randomizeCode() {
        code=new Guess();
        int counter = 0;
        int[] pickednumbers = {-1, -1, -1, -1};
        int random;
        while (counter != 4) {
            random = ThreadLocalRandom.current().nextInt(0, 5 + 1);
            if (!((pickednumbers[0] == random) || (pickednumbers[1] == random) || (pickednumbers[2] == random) || (pickednumbers[3] == random))) {
                pickednumbers[counter] = random;
                counter++;
            }
        }

        for (int i = 0; i < 4; i++) {
            code.addcolor(allcolors[pickednumbers[i]]);
        }
    }

    public boolean hasWon() {
        return crits == 4;
    }

    public void addcolortocurrentguess(String color) {
        guesslist[guesscounter].addcolor(color);
        modifyfirstfields();
        colorcounter++;
        if (colorcounter == 4) {
            calculatehits(guesslist[guesscounter]);
            modifysecondfields();
            guesscounter++;
            if (guesscounter < 7) {
                generateguessatindex(guesscounter);
                colorcounter = 0;
            }
        }
        updatedesc();
    }
    public void updatedesc(){
         desc="Wähle mit den Reaktionen nacheinander deine Farbe. \n"+"Wähle jetzt: "+ (colorcounter+1) +". Farbe des "+(guesscounter+1)+".ten Guesses."+
                "\n :arrow_left: löscht die letzten Farbe, \n :rewind: setzt den Guess komplett zurück \n" +
                " :repeat: startet das Spiel neu \n :regional_indicator_x: beendet das Spiel";
    }
    public String determinediscordcolorfromcolor(String color) {
        switch (color) {
            // :blue_circle: :yellow_circle: :green_circle: :white_circle: \n\n " +
            // :yellow_circle: :green_circle: :white_circle: :red_circle:
            case "blau":
                return ":blue_circle: ";

            case "grün":
                return ":green_circle: ";

            case "rot":
                return ":red_circle: ";

            case "gelb":
                return ":yellow_circle: ";

            case "orange":
                return ":orange_circle: ";

            case "weiß":
                return ":white_circle: ";

            default:
                return ":x: ";
            case "":
                return ":o:";
        }
    }


    public void modifyfirstfields() {
// check initializing of fields TODO

        String discordcolor=determinediscordcolorfromcolor(guesslist[guesscounter].getcolorlistatindex(colorcounter));
        knownfirstfield+=discordcolor;
        unknownfirstfield=unknownfirstfield.replaceFirst(":o:",""); //DID THAT HAPPEN? TODO
        mergefields();
    }
    public void modifysecondfields() {
        int i;
        int j;
        for (j = 0; j < crits; j++) {
            knownsecondfield += ":red_circle: ";
        }
        for (i = 0; i < hits; i++) {
            knownsecondfield += ":white_circle: ";
        }
        for (int k = j + i; k < 4; k++) {
            knownsecondfield += ":black_circle: ";
        }
        knownsecondfield += "\n\n";
        knownfirstfield += "\n\n";
        //unknownsecondfield = unknownsecondfield.substring(unknownsecondfield.indexOf("\n\n") + 2);
        unknownfirstfield=unknownfirstfield.replaceFirst("\n\n","");//HERE
        unknownsecondfield=unknownfirstfield;

        mergefields();
    }
    public void backactions() {

/*
            guesslist[guesscounter].addcolor(color);
            modifyfirstfields();
            colorcounter++;
            if (colorcounter == 4) {
                calculatehits(guesslist[guesscounter]);
                modifysecondfields();
                guesscounter++;
                if (guesscounter < 7) {
                    generateguessatindex(guesscounter);
                    colorcounter = 0;
                }
            }
            updatedesc();
        }*/
    if (colorcounter>0) {
        String replaceinknown = determinediscordcolorfromcolor(guesslist[guesscounter].getcolorlistatindex(colorcounter-1));
        knownfirstfield = knownfirstfield.substring(0, knownfirstfield.lastIndexOf(replaceinknown));
        unknownfirstfield = ":o: " + unknownfirstfield;
        guesslist[guesscounter].removecolor();
        colorcounter--;
        updatedesc();
        mergefields();
        TextChannel textChannel = BOT.getShardManager().getTextChannelById(channelID);
        if (textChannel != null) {
            textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> GameUtil.updateGameEmbed(gameMessage, desc, user, firstfield, secondfield));
            }

        }
    }

    public void  rewindactions(){
        while (colorcounter>0){
            backactions();
        }
    }

    public void run(Guild guild, TextChannel channel, String userInput) {
        if (userInput.equals("stop") && gameActive) {
            stop();
           // channel.sendMessage("Thanks for playing, " + user.getAsMention() + "!")
             //       .queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
        }
        if ((userInput.equals("play") || userInput.equals("")) && !gameActive) {
            newGame(channel);
        } else if (gameActive) {
            if (!hasWon() && guesscounter < 7) {
                switch (userInput) { //CONTINUE HERE
                    case "blau":
                        addcolortocurrentguess("blau");
                        break;
                    case "grün":
                        addcolortocurrentguess("grün");
                        break;
                    case "rot":
                        addcolortocurrentguess("rot");
                        break;
                    case "gelb":
                        addcolortocurrentguess("gelb");
                        break;
                    case "orange":
                        addcolortocurrentguess("orange");
                        break;
                    case "weiß":
                        addcolortocurrentguess("weiß");
                        break;
                    //---- special actions ---
                    case "back":
                        backactions();
                        break;
                    case "rewind":
                        rewindactions();
                        break;
                    case "replay":
                        stop();
                        run(guild, channel, "play");
                        break;

                    default:
                        //stop();
                        channel.sendMessage(">"+userInput+"< ist eine ungültige Eingabe!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                       // throw new IllegalStateException("Unexpected value: " + userInput);
                        break;
                }
              //  TextChannel textChannel = BOT.getShardManager().getTextChannelById(channelID);
                TextChannel textChannel = BOT.getShardManager().getTextChannelById(channelID);
                if (textChannel != null) {
                    final String codestring;
                    String tmp="";
                    for (int i=0;i<4;i++){
                        tmp += determinediscordcolorfromcolor(code.getcolorlistatindex(i))+" ";
                    }
                    codestring=tmp;
                    if (hasWon()) {
                        textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> GameUtil.sendWinEmbed(gameMessage, desc, user, firstfield, secondfield, "WIN",codestring));
                        stop();
                    } else {
                        if (guesscounter < 7) {
                            textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> GameUtil.updateGameEmbed(gameMessage, desc, user, firstfield, secondfield));
                            //Continue here
                        } else {String code;
                            textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> GameUtil.sendWinEmbed(gameMessage, desc, user, firstfield, secondfield, "LOSE",codestring));
                            stop();
                        }
                    }

                }
                // ERROR Textchannel == NUll
            }
        }
    }

        //TODO
        //außerdem muss der DESC Text geändert werden, sowie der Guess angezeigt werden
        //optional: Check-mark nach 4 farben als zusätzliche Bestätigung

    public void mergefields (){
            firstfield=knownfirstfield+unknownfirstfield;
            secondfield=knownsecondfield+unknownsecondfield;
    }


    public void stop() {
        gameActive = false;
        TextChannel textChannel = BOT.getShardManager().getTextChannelById(channelID);;
        if (textChannel != null) {
            textChannel.sendMessage("Thanks for playing, " + user.getAsMention() + "!")
                    .queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
         //   textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> gameMessage.delete().queue());
            textChannel.retrieveMessageById(gameMessageID).queue(gameMessage -> {
                gameMessage.clearReactions().queue();
                if (!hasWon() && guesscounter<7) {
                    gameMessage.delete().queueAfter(1, TimeUnit.MICROSECONDS);
                }
            }
            );
            GameUtil.removeGame(user.getIdLong());
        }
    }

    public void calculatehits(Guess guess){ //Prüfen, ob damit auch double-select  spielbar ist
    hitlist[guesscounter]=hits;
    critlist[guesscounter]=crits;
    hits=crits=0;
    currentguess=guesslist[guesscounter];
    Guess copyofcode=new Guess(code.getcolorlistatindex(0),code.getcolorlistatindex(1),code.getcolorlistatindex(2),code.getcolorlistatindex(3));
    Guess copyofguess=new Guess(currentguess.getcolorlistatindex(0),currentguess.getcolorlistatindex(1),currentguess.getcolorlistatindex(2),currentguess.getcolorlistatindex(3));
    for (int i=0;i<4;i++){//calc crits
        if(currentguess.getcolorlistatindex(i)==code.getcolorlistatindex(i)){
            copyofcode.removecoloratlocation(i);
            copyofguess.removecoloratlocation(i);
            crits++;
        }
    }
    for (int j=0;j<4;j++){
        for (int k=0;k<4;k++){
            if (copyofguess.getcolorlistatindex(j)==copyofcode.getcolorlistatindex(k) && copyofguess.getcolorlistatindex(j) !=""){
                hits++;
                copyofguess.removecoloratlocation(j);
                copyofcode.removecoloratlocation(k);
            }
        }

    }
    }

}
