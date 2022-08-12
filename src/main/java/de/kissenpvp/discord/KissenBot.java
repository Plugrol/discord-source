/*
 * KissenEssentials
 * Copyright (C) KissenEssentials team and contributors.
 *
 * This program is free software and is free to redistribute
 * and/or modify under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is intended for the purpose of joy,
 * WITHOUT WARRANTY without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.kissenpvp.discord;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.config.Configuration;
import de.kissenpvp.api.message.language.Languages;
import de.kissenpvp.discord.api.Bot;
import de.kissenpvp.discord.language.BotDisabled;
import de.kissenpvp.discord.language.InvalidToken;
import de.kissenpvp.discord.language.PrepareStart;
import de.kissenpvp.discord.language.StartedSuccessful;
import de.kissenpvp.discord.settings.BotEnabled;
import de.kissenpvp.discord.settings.ServerID;
import de.kissenpvp.discord.settings.Token;
import lombok.Getter;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.listener.GloballyAttachableListener;

import java.util.ArrayList;
import java.util.List;

/**
 * What did you expect?
 *
 * @author Taubsie
 * @since 1.0.0
 */
public class KissenBot implements Bot
{
    private DiscordApi bot;
    @Getter private boolean connected;
    private List<SlashCommandBuilder> COMMANDS;

    @Override public boolean preStart()
    {
        connected = false;
        COMMANDS = new ArrayList<>();
        return Bot.super.preStart();
    }

    @Override public boolean start()
    {
        if(!Kissen.getInstance().getImplementation(Configuration.class).getSetting(BotEnabled.class))
        {
            Kissen.getInstance().getInternals().system().debug(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new BotDisabled()).getText(), null, "discord");
            return true;
        }

        String token = Kissen.getInstance().getImplementation(Configuration.class).getSetting(Token.class);
        if(token.trim().equalsIgnoreCase(new Token().getDefault()) || token.isBlank())
        {
            Kissen.getInstance().getInternals().system().log(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new InvalidToken()).getText());
            return true;
        }

        Kissen.getInstance().getInternals().system().debug(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new PrepareStart()).getText(), null, "discord");
        bot = new DiscordApiBuilder().setToken(token).setAllIntents().setAccountType(AccountType.BOT).setWaitForServersOnStartup(true).setWaitForUsersOnStartup(true).login().join();
        bot.updateActivity(ActivityType.COMPETING, "faster startup times.");
        bot.updateStatus(UserStatus.IDLE);

        Kissen.getInstance().getInternals().system().debug(Kissen.getInstance().getImplementation(Languages.class).getMessage("en_GB", new StartedSuccessful()).getText(), null, "discord");

        bot.updateActivity(ActivityType.PLAYING, "games on KissenPvP.de");
        bot.updateStatus(UserStatus.ONLINE);

        bot.bulkOverwriteGlobalApplicationCommands(COMMANDS);

        connected = true;
        return true;
    }

    @Override public void stop()
    {
        if(!isEnabled())
        {
            return;
        }

        bot.updateStatus(UserStatus.DO_NOT_DISTURB);
        bot.updateActivity(ActivityType.LISTENING, "to the sound of silence.");

        connected = false;

        bot.disconnect();
    }

    @Override public void restart()
    {
        stop();
        start();
    }

    @Deprecated(since = "1.0.0")
    @Override public void reloadBot()
    {

    }

    @Override public void reloadConfig()
    {

    }

    @Override public boolean isEnabled()
    {
        return Kissen.getInstance().getImplementation(Configuration.class).getSetting(BotEnabled.class);
    }

    private long getServerID()
    {
        return Kissen.getInstance().getImplementation(Configuration.class).getSetting(ServerID.class);
    }

    @Override public Server getServer()
    {
        if(!isEnabled() || !isConnected() || getServerID() == (new ServerID().getDefault()))
        {
            return null;
        }

        return bot.getServerById(getServerID()).orElse(null);
    }

    @Override public void registerListener(GloballyAttachableListener globallyAttachableListener)
    {
        bot.addListener(globallyAttachableListener);
    }

    @Override public void addCommand(SlashCommandBuilder slashCommandBuilder)
    {
        COMMANDS.add(slashCommandBuilder);
    }
}