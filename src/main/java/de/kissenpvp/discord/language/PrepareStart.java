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
package de.kissenpvp.discord.language;

import de.kissenpvp.api.message.language.Sentence;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class PrepareStart implements Sentence
{
    @Override public String getGroup()
    {
        return "discord";
    }

    @Override public String getCode()
    {
        return "prepare_start";
    }

    @Override public MessageType getType()
    {
        return MessageType.NORMAL;
    }

    @Override public String getDefault()
    {
        return "Preparing to start Discordbot.";
    }
}