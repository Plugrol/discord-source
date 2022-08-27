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
import de.kissenpvp.discord.api.Bot;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.listener.interaction.ModalSubmitListener;

import java.awt.*;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class ModalListener implements ModalSubmitListener
{
    @Override public void onModalSubmit(ModalSubmitEvent modalSubmitEvent)
    {
        switch (modalSubmitEvent.getModalInteraction().getCustomId())
        {
            case "appeal_ban" -> {
                TextChannel appealChannel = Kissen.getInstance().getImplementation(Bot.class).getAppealChannel();
                if(appealChannel == null)
                {
                    modalSubmitEvent.getModalInteraction().createImmediateResponder().setContent("Our appeal-system is currently offline. Please get into contact with one of our administrators.\nSorry!").respond();
                    return;
                }

                modalSubmitEvent.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbeds(new EmbedBuilder().setColor(Color.RED).setTimestampToNow().setTitle("Ban-Appeal by " + modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_name").orElseThrow() + " sent! See your information below.")
                                .addField("Why we should unban you", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_reason").orElseThrow())
                                .addField("What you learned from it", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_text").orElseThrow())
                                .setAuthor(modalSubmitEvent.getModalInteraction().getUser())).respond().join();

                appealChannel.sendMessage(new EmbedBuilder().setColor(Color.RED).setTimestampToNow().setTitle("Ban-Appeal by " + modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_name").orElseThrow())
                        .addField("Why we should unban them", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_reason").orElseThrow())
                        .addField("What they learned from it", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_text").orElseThrow())
                        .setAuthor(modalSubmitEvent.getModalInteraction().getUser())).join();
            }
            case "appeal_mute" -> {
                TextChannel appealChannel = Kissen.getInstance().getImplementation(Bot.class).getAppealChannel();
                if(appealChannel == null)
                {
                    modalSubmitEvent.getModalInteraction().createImmediateResponder().setContent("Our appeal-system is currently offline. Please get into contact with one of our administrators.\nSorry!").respond();
                    return;
                }

                modalSubmitEvent.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbeds(new EmbedBuilder().setColor(Color.YELLOW).setTimestampToNow().setTitle("Mute-Appeal by " + modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_name").orElseThrow() + " sent! See your information below.")
                        .addField("Why we should unban you", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_reason").orElseThrow())
                        .addField("What you learned from it", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_text").orElseThrow())
                        .setAuthor(modalSubmitEvent.getModalInteraction().getUser())).respond().join();

                appealChannel.sendMessage(new EmbedBuilder().setColor(Color.YELLOW).setTimestampToNow().setTitle("Mute-Appeal by " + modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_name").orElseThrow())
                        .addField("Why we should unban them", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_reason").orElseThrow())
                        .addField("What they learned from it", modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appeal_text").orElseThrow())
                        .setAuthor(modalSubmitEvent.getModalInteraction().getUser())).join();
            }

            default -> Kissen.getInstance().getInternals().system().error("CustomID is unknown. Please report this to a developer. Class: \"de.kissenpvp.discord.listener.ModalListener\"", null, "discord");
        }
    }
}