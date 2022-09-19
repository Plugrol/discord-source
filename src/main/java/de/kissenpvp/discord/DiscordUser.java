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
package de.kissenpvp.discord;

import de.kissenpvp.api.user.external.ExternalUser;

import java.util.Map;

/**
 * @author Taubsie
 * @since 1.0.0
 */
@SuppressWarnings("unused") public class DiscordUser extends ExternalUser
{
    public DiscordUser(java.util.UUID uuid)
    {
        super(uuid);
    }

    @Override public void insertInfo(Map<String, String> map)
    {
        map.put("Discord Verified", String.valueOf(isDiscordVerified()));
        if (isDiscordVerified())
        {
            map.put("DiscordID", getUser().get("discord_id"));
        }
    }

    public Long getDiscordID()
    {
        return getUser().containsKey("discord_id") ? Long.parseLong(getUser().get("discord_id")) : null;
    }

    public boolean isDiscordVerified()
    {
        return getDiscordID() != null;
    }
}