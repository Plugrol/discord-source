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
package de.kissenpvp.discord;

import de.kissenpvp.api.user.external.ExternalUser;

import java.util.Map;

/**
 * @author Taubsie
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class DiscordUser extends ExternalUser
{
    public DiscordUser(java.util.UUID uuid)
    {
        super(uuid);
    }

    @Override public void setup()
    {

    }

    @Override public void handshake()
    {

    }

    @Override public void login()
    {

    }

    @Override public void logout()
    {

    }

    @Override public void update(String s, Object... objects)
    {

    }

    @Override public void tick()
    {

    }

    @Override public void insertInfo(Map<String, String> map)
    {
        map.put("DiscordID", getUser().getMeta().get("discord_id").toString());
    }

    public Long getDiscordID()
    {
        return getUser().getMeta().containsKey("discord_id") ? getUser().getMeta().get("discord_id", Long.class) : null;
    }

    public boolean isDiscordVerified()
    {
        return getDiscordID() != null;
    }
}