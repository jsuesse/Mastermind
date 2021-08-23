package core;

import listener.*;/*
import listener.VoiceMoveListener;*/

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
//import net.dv8tion.jda.core.*;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BOT {

    private static JDA shardManager;
    private JDABuilder builder;

    public static void main(String[] args) throws LoginException {
        String token = null;
        try {
            File tokenFile = Paths.get("token.txt").toFile();
            if (!tokenFile.exists()) {
                System.out.println("[ERROR] Could not find token.txt file");
                System.out.print("Please paste in your bot token: ");
                Scanner s = new Scanner(System.in);
                token = s.nextLine();
                System.out.println();
                System.out.println("[INFO] Creating token.txt - please wait");
                if (!tokenFile.createNewFile()) {
                    System.out.println(
                            "[ERROR] Could not create token.txt - please create this file and paste in your token"
                                    + ".");
                    s.close();
                    return;
                }
                Files.write(tokenFile.toPath(), token.getBytes());
                s.close();
            }
            token = new String(Files.readAllBytes(tokenFile.toPath()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (token == null) return;

        builder=JDABuilder.createDefault(token);
        builder.setActivity(Activity.watching("beep boop"));
        builder.setStatus(OnlineStatus.ONLINE);

        builder.addEventListeners(new CommandListener(), new GameListener());
        /*
        builder.addEventListeners(new VoiceJoinListener());
        builder.addEventListeners(new VoiceLeaveListener());
        builder.addEventListeners(new VoiceMoveListener());*/

        shardManager = builder.build();
        System.out.println("Bot online!");

        Thread consoleThread = new Thread(() -> {
            Scanner s = new Scanner(System.in);
            while (s.hasNextLine()) {
                processCommand(s.nextLine());
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.setName("Console Thread");
        consoleThread.start();

    }

    public static JDA getShardManager() {
        return shardManager;
    }

    public void shutdown() {
        new Thread(() -> {

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null){

                    if(line.equalsIgnoreCase("exit")){
                        if(shardManager != null){
                            builder.setStatus(OnlineStatus.OFFLINE);
                            shardManager.shutdown();
                            System.out.println("Bot offline *knürtsch*");
                        }
                        reader.close();
                    }
                    else {
                        System.out.println("Bot offline *knürtsch*");
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }).start();
    }

}
