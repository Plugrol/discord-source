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

import de.kissenpvp.discord.api.command.DiscordCommand;
import de.kissenpvp.discord.api.command.ExecutableDiscordCommand;
import de.kissenpvp.discord.api.command.SlashCommand;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

/**
 * @author Taubsie
 * @since 1.0.0
 */
@DiscordCommand(command = "appeal", description = "Appeal your mute or ban on KissenPvP.de")
public class AppealCommand implements SlashCommand
{
    @Override public void execute(ExecutableDiscordCommand executableCommand)
    {
        executableCommand.getSlashCommandCreateEvent().getSlashCommandInteraction().respondWithModal("appeal", "Appeal",
                ActionRow.of(SelectMenu.create("appealtype", "Type of appeal",
                        List.of(SelectMenuOption.create("label1", "Ban"), SelectMenuOption.create("label2", "Mute")))));
    }

    @Override public SlashCommandOption[] getSlashCommandOptions()
    {
        return new SlashCommandOption[0];
    }
}