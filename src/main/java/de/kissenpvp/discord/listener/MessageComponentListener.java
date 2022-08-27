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
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class MessageComponentListener implements MessageComponentCreateListener
{
    @Override public void onComponentCreate(MessageComponentCreateEvent messageComponentCreateEvent)
    {
        switch (messageComponentCreateEvent.getMessageComponentInteraction().getCustomId())
        {
            case "appeal_type" -> {
                SelectMenuInteraction selectMenuInteraction = messageComponentCreateEvent.getMessageComponentInteraction().asSelectMenuInteraction().orElseThrow();

                switch (selectMenuInteraction.getChosenOptions().get(0).getValue())
                {
                    case "appeal_ban" -> {
                        selectMenuInteraction.respondWithModal("appeal_ban", "Appeal a ban on KissenPvP.de",
                                ActionRow.of(TextInput.create(TextInputStyle.SHORT, "appeal_name", "Which account is this appeal for?")),
                                ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH, "appeal_reason", "Tell us why we should unban you.")),
                                ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH, "appeal_text", "What will you do differently?"))).join();
                    }
                    case "appeal_mute" -> {
                        selectMenuInteraction.respondWithModal("appeal_mute", "Appeal a mute on KissenPvP.de",
                                ActionRow.of(TextInput.create(TextInputStyle.SHORT, "appeal_name", "Which account is this appeal for?")),
                                ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH, "appeal_reason", "Tell us why we should unban you.")),
                                ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH, "appeal_text", "What will you do differently?"))).join();
                    }

                    default -> Kissen.getInstance().getInternals().system().error("Value of SelectMenuInteraction is unknown. Please report this to a developer. Class: \"de.kissenpvp.discord.listener.MessageComponentListener\"", null, "discord");
                }
            }

            default -> Kissen.getInstance().getInternals().system().error("CustomID is unknown. Please report this to a developer. Class: \"de.kissenpvp.discord.listener.MessageComponentListener\"", null, "discord");
        }
    }
}