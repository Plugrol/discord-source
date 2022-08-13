/*
 *  Copyright 14.07.2022 KissenPvP
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.kissenpvp.discord;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.config.Configuration;
import de.kissenpvp.api.message.language.Languages;
import de.kissenpvp.discord.api.Bot;
import de.kissenpvp.discord.api.command.SlashCommandDescription;
import de.kissenpvp.discord.language.BotDisabled;
import de.kissenpvp.discord.language.InvalidToken;
import de.kissenpvp.discord.language.PrepareStart;
import de.kissenpvp.discord.language.StartedSuccessful;
import de.kissenpvp.discord.settings.BotEnabled;
import de.kissenpvp.discord.settings.ServerID;
import de.kissenpvp.discord.settings.Token;
import lombok.Getter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.listener.GloballyAttachableListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * What did you expect?
 *
 * @author Taubsie
 * @since 1.0.0
 */
public class KissenBot implements Bot {
    private DiscordApi bot;
    @Getter
    private boolean connected;
    private List<SlashCommandBuilder> commands;
    @Getter
    private List<SlashCommandDescription> slashCommandDescriptions;

    @Override
    public boolean preStart() {
        connected = false;
        if (!Kissen.getInstance()
                .getImplementation(Configuration.class)
                .getSetting(BotEnabled.class)) {
            Kissen.getInstance()
                    .getInternals()
                    .system()
                    .debug(Kissen.getInstance()
                            .getImplementation(Languages.class)
                            .getMessage("en_GB", new BotDisabled())
                            .getText(), null, "discord");
            return true;
        }

        String token = Kissen.getInstance().getImplementation(Configuration.class).getSetting(Token.class);
        if (token.trim().equalsIgnoreCase(new Token().getDefault()) || token.isBlank()) {
            Kissen.getInstance().getInternals().system().log(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new InvalidToken()).getText());
            return true;
        }

        Kissen.getInstance().getInternals().system().debug(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new PrepareStart()).getText(), null, "discord");

        bot = new DiscordApiBuilder().setToken(token).setAllIntents().setWaitForServersOnStartup(true).setWaitForUsersOnStartup(true).login().join();

        bot.updateActivity(ActivityType.COMPETING, "faster startup times.");
        bot.updateStatus(UserStatus.IDLE);

        Kissen.getInstance().getInternals().system().debug(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new StartedSuccessful()).getText(), null, "discord");

        bot.updateActivity(ActivityType.PLAYING, "▁▂▃▄▅▆ KissenPvP.de ▆▅▄▃▂▁  >Dein Netzwerk für Minispiele. Joine jetzt!<");
        bot.updateStatus(UserStatus.ONLINE);

        bot.bulkOverwriteGlobalApplicationCommands(commands);

        connected = true;
        return true;
    }

    @Override
    public void stop() {

        if (bot == null) {
            return;
        }

        bot.updateStatus(UserStatus.DO_NOT_DISTURB);
        bot.updateActivity(ActivityType.LISTENING, "to the sound of silence.");

        connected = false;

        bot.disconnect();
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Deprecated(since = "1.0.0")
    @Override
    public void reloadBot() {

    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public boolean isEnabled() {
        return Kissen.getInstance().getImplementation(Configuration.class).getSetting(BotEnabled.class);
    }

    private long getServerID()
    {
        return Kissen.getInstance().getImplementation(Configuration.class).getSetting(ServerID.class);
    }

    @Override
    public Server getServer() {
        if (!isEnabled() || !isConnected() || getServerID() == (new ServerID().getDefault())) {
            return null;
        }

        return bot.getServerById(getServerID()).orElse(null);
    }

    @Override
    public void registerListener(GloballyAttachableListener globallyAttachableListener) {
        bot.addListener(globallyAttachableListener);
    }


    @Override
    public void addCommand(SlashCommandDescription slashCommandDescription) {

        if (slashCommandDescriptions == null) {
            slashCommandDescriptions = new ArrayList<>();
        }

        slashCommandDescriptions.add(slashCommandDescription);

        SlashCommandBuilder slashCommandBuilder = org.javacord.api.interaction.SlashCommand.with(slashCommandDescription.getCommandInfo().command(), slashCommandDescription.getCommandInfo().description());
        Arrays.stream(slashCommandDescription.getExecutable().getSlashCommandOptions()).forEach(slashCommandBuilder::addOption);

        if (commands == null) {
            commands = new ArrayList<>();
        }
        commands.add(slashCommandBuilder);
    }
}