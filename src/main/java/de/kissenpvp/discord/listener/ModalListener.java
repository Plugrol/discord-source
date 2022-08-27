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
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.listener.interaction.ModalSubmitListener;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class ModalListener implements ModalSubmitListener
{
    @Override public void onModalSubmit(ModalSubmitEvent modalSubmitEvent)
    {
        if(!modalSubmitEvent.getModalInteraction().getCustomId().equalsIgnoreCase("appeal"))
        {
            return;
        }

        Kissen.getInstance().getInternals().system().log("appealtype:" + modalSubmitEvent.getModalInteraction().getTextInputValueByCustomId("appealtype").orElse("null lol"));
    }
}