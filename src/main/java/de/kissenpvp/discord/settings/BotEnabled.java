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
package de.kissenpvp.discord.settings;

import de.kissenpvp.api.config.OptionDefault;

/**
 * Why do you need to check this, what do you think is here?
 *
 * @author Taubsie
 * @since 1.0.0
 */
public class BotEnabled extends OptionDefault<Boolean>
{
    @Override public String getCode()
    {
        return "bot_enabled";
    }

    @Override public String getGroup()
    {
        return "discord";
    }

    @Override public String getDescription()
    {
        return "Ob der bot im gommemode is";
    }

    @Override public Boolean getDefault()
    {
        return false;
    }
}