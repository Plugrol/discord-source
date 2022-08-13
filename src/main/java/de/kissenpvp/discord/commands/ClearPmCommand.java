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
package de.kissenpvp.discord.commands;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.discord.api.command.DiscordCommand;
import de.kissenpvp.discord.api.command.ExecutableDiscordCommand;
import de.kissenpvp.discord.api.command.SlashCommand;
import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Taubsie
 * @since 1.0.0
 */
@DiscordCommand(command = "clearpm", description = "Clears our private-message-history.")
public class ClearPmCommand implements SlashCommand
{
    @Override public void execute(ExecutableDiscordCommand executableCommand)
    {
        executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().getUser().openPrivateChannel().join();
        if (executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().getUser().getPrivateChannel().isEmpty())
        {
            Kissen.getInstance().getInternals().system().debug("Private-Channel isn't present.", null, "discord");
            return;
        }
        long messageCount = executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().getUser().getPrivateChannel().get().getMessagesAsStream().count();
        InteractionOriginalResponseUpdater message = executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().createImmediateResponder().setContent("Trying to clear the pms. You will still have to clear your own messages! (" + messageCount + " messages)").respond().join();
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            @Override public void run()
            {
                message.delete();
                t.cancel();
            }
        }, 10 * 1000);
        executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().getUser().getPrivateChannel().get().getMessagesAsStream().forEach(Message::delete);
        Kissen.getInstance().getInternals().system().debug("Deleted private-messages(" + messageCount + ") with user " + executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().getUser().getName() + ".", null, "discord");
    }

    @Override public SlashCommandOption[] getSlashCommandOptions()
    {
        return new SlashCommandOption[0];
    }
}