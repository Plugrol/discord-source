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

import de.kissenpvp.api.ban.BanType;
import de.kissenpvp.api.ban.event.BanAssignEvent;
import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.database.connection.SQL;
import de.kissenpvp.api.event.EventListener;
import de.kissenpvp.discord.DiscordUser;
import de.kissenpvp.discord.api.Bot;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class BanAssignListener implements EventListener<BanAssignEvent>
{
    @Override public void call(BanAssignEvent banAssignEvent)
    {
        try
        {
            PreparedStatement preparedStatement =
                    Kissen.getInstance().getImplementation(SQL.class).getPreparedStatement("SELECT uuid FROM " + Kissen.getInstance().getPublicMeta().getTable() + " " + "WHERE identifier = ? AND " + "value = ?;");
            preparedStatement.setString(1, "total_id"); preparedStatement.setString(2, banAssignEvent.getBanNode().totalID().toString()); ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                DiscordUser discordUser =
                        Kissen.getInstance().getImplementation(de.kissenpvp.api.user.User.class).getUser(UUID.fromString(resultSet.getString("uuid").substring("user".length()))).getExternalUser(DiscordUser.class);
                Bot bot = Kissen.getInstance().getImplementation(Bot.class); if (discordUser.isDiscordVerified() && bot.getServer() != null && bot.getMember(discordUser.getDiscordID()) != null)
            {
                punish(banAssignEvent.getBanNode().banIDNode().banType(), banAssignEvent.getBanNode().reason(), banAssignEvent.getBanNode().banner(),
                        banAssignEvent.getBanNode().validationNode().timeNode().length(), bot.getMember(discordUser.getDiscordID()), bot);
            }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void punish(BanType banType, String reason, String banner, long length, User user, Bot bot)
    {
        switch (banType)
        {
            case MUTE ->
            {
                user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got muted on KissenPvP.de").addField("Duration", (length == -1) ? "permanent" :
                        Duration.ofMillis(length).toString()).addField("Reason", (reason == null) ? "none" : reason).addField("Banner", (banner == null) ? "CONSOLE" : banner).setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED).setFooter("Appeal this mute using \"/appeal\"."));
                user.timeout(bot.getServer(), (length == -1) ? Duration.ofDays(365L) : Duration.ofMillis(length), reason);
            } case KICK ->
                user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got kicked from KissenPvP.de").addField("Reason", (reason == null) ? "none" :
                        reason).addField("Banner", (banner == null) ? "CONSOLE" : banner).setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED));
            case BAN ->
            {
                user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got banned from KissenPvP.de").addField("Duration", (length == -1) ? "permanent" :
                        Duration.ofMillis(length).toString()).addField("Reason", (reason == null) ? "none" : reason).addField("Banner", (banner == null) ? "CONSOLE" : banner).setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED).setFooter("Appeal this ban using \"/appeal\"."));
                if (length == -1)
                {
                    Objects.requireNonNull(bot.getServer()).banUser(user, 0, reason).join();
                }
                else
                {
                    user.timeout(bot.getServer(), Duration.ofMillis(length), reason);
                }
            } default -> Kissen.getInstance().getInternals().system().log("BanType is unknown. Please report this to a developer. Class: \"de.kissenpvp.discord.listener.BanAssignListener\"");
        }
    }
}