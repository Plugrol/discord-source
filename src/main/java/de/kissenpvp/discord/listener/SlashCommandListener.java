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
package de.kissenpvp.discord.listener;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.base.loader.ClassScanner;
import de.kissenpvp.discord.api.command.SlashCommandDescription;
import de.kissenpvp.discord.command.KissenExecutableDiscordCommand;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class SlashCommandListener implements SlashCommandCreateListener
{
    @Override public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent)
    {
        SlashCommandDescription slashCommandDescription = Kissen.getInstance().getImplementation(ClassScanner.class)
                .getList(SlashCommandDescription.class).stream()
                .filter(commandDescription -> commandDescription.getCommandInfo().command()
                        .equalsIgnoreCase(slashCommandCreateEvent.getSlashCommandInteraction().getCommandName())).findFirst().orElse(null);

        if(slashCommandDescription == null)
        {
            slashCommandCreateEvent.getSlashCommandInteraction().createImmediateResponder().addEmbed(new EmbedBuilder()
                            .setDescription("This slash-command is unknown to the bot. Please report this to the developer(s).").setTimestampToNow())
                    .respond();
            return;
        }

        slashCommandDescription.getExecutable().execute(new KissenExecutableDiscordCommand(slashCommandCreateEvent));
    }
}