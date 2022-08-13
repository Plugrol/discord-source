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
package de.kissenpvp.discord.listener;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.discord.KissenBot;
import de.kissenpvp.discord.api.Bot;
import de.kissenpvp.discord.api.command.SlashCommandDescription;
import de.kissenpvp.discord.command.KissenExecutableDiscordCommand;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class SlashCommandListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {

        SlashCommandDescription slashCommandDescription = ((KissenBot) Kissen.getInstance().getImplementation(Bot.class)).getSlashCommandDescriptions().stream().filter(commandDescription -> commandDescription.getCommandInfo().command().equalsIgnoreCase(slashCommandCreateEvent.getSlashCommandInteraction().getCommandName())).findFirst().orElse(null);

        if (slashCommandDescription == null) {
            slashCommandCreateEvent.getSlashCommandInteraction().createImmediateResponder().addEmbed(new EmbedBuilder().setDescription("This slash-command is unknown to the bot. Please report this to the developer(s).").addField("Command-Name", slashCommandCreateEvent.getSlashCommandInteraction().getCommandName()).setTimestampToNow()).respond();
            return;
        }

        slashCommandDescription.getExecutable().execute(new KissenExecutableDiscordCommand(slashCommandCreateEvent));
    }
}