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
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.listener.interaction.ButtonClickListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Taubsie
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ButtonListener implements ButtonClickListener
{
    @Override public void onButtonClick(ButtonClickEvent buttonClickEvent)
    {
        if (buttonClickEvent.getButtonInteraction().getChannel().isEmpty())
        {
            Kissen.getInstance().getInternals().system().debug("Channel is null.", null, "discord");
            return;
        }

        if(buttonClickEvent.getButtonInteraction().getCustomId().equalsIgnoreCase("click"))
        {
            buttonClickEvent.getButtonInteraction().createImmediateResponder().setContent("Hello! :) I'm your friendly discord-bot!").respond();
        }
        else if (buttonClickEvent.getButtonInteraction().getCustomId().equalsIgnoreCase("emoji"))
        {
            buttonClickEvent.getButtonInteraction().getMessage().edit("You should click the button that says \"Click Me!\".");
            buttonClickEvent.getButtonInteraction().getChannel().get().type().join();
            buttonClickEvent.getButtonInteraction().acknowledge();
            Timer t = new Timer();
            int secondsUntilEdit = 10;
            t.schedule(new TimerTask()
            {
                @Override public void run()
                {
                    buttonClickEvent.getButtonInteraction().getMessage().edit("Click the button below.");
                    t.cancel();
                }
            }, secondsUntilEdit * 1000);
        }
    }
}