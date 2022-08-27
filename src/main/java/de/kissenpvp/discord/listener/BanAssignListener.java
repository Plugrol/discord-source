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

import de.kissenpvp.api.ban.BanIDNode;
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
            PreparedStatement preparedStatement = Kissen.getInstance().getImplementation(SQL.class).getPreparedStatement("SELECT uuid FROM table WHERE identifier = ? AND value = ?;");
            preparedStatement.setString(1, "total_id");
            preparedStatement.setString(2, banAssignEvent.getBanNode().totalID().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                DiscordUser discordUser = Kissen.getInstance().getImplementation(de.kissenpvp.api.user.User.class).getExternalUser(DiscordUser.class, UUID.fromString(resultSet.getString("uuid").substring("user".length())));
                Bot bot = Kissen.getInstance().getImplementation(Bot.class);
                if(discordUser.isDiscordVerified() && bot.getServer() != null && bot.getMember(discordUser.getDiscordID()) != null)
                {
                    punish(banAssignEvent.getBanNode().banIDNode().banType(), banAssignEvent.getBanNode().reason(), banAssignEvent.getBanNode().banner(), banAssignEvent.getBanNode().timeNode().length(), bot.getMember(discordUser.getDiscordID()), bot);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void punish(BanIDNode.BanType banType, String reason, String banner, long length, User user, Bot bot)
    {
        switch (banType)
        {
            case MUTE -> {
                user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got muted on KissenPvP.de")
                        .addField("Duration", (length == -1) ? "permanent" : Duration.ofMillis(length).toString())
                        .addField("Reason", (reason == null) ? "none" : reason).addField("Banner", (banner == null) ? "CONSOLE" : banner)
                        .setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED).setFooter("Appeal this mute using \"/appeal\"."));
                user.timeout(bot.getServer(), (length == -1) ? Duration.ofDays(365L) : Duration.ofMillis(length), reason);
            }
            case KICK -> user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got kicked from KissenPvP.de")
                    .addField("Reason", (reason == null) ? "none" : reason).addField("Banner", (banner == null) ? "CONSOLE" : banner)
                    .setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED));
            case BAN -> {
                user.openPrivateChannel().join().sendMessage(new EmbedBuilder().setTimestampToNow().setTitle("You got banned from KissenPvP.de")
                        .addField("Duration", (length == -1) ? "permanent" : Duration.ofMillis(length).toString())
                        .addField("Reason", (reason == null) ? "none" : reason).addField("Banner", (banner == null) ? "CONSOLE" : banner)
                        .setAuthor(bot.getMember(bot.getBotId())).setColor(Color.RED).setFooter("Appeal this ban using \"/appeal\"."));
                if(length == -1)
                {
                    Objects.requireNonNull(bot.getServer()).banUser(user, 0, reason).join();
                }
                else {
                    user.timeout(bot.getServer(), Duration.ofMillis(length), reason);
                }
            }
            default -> Kissen.getInstance().getInternals().system().error("BanType is unknown. Please report this to a developer. Class: \"de.kissenpvp.discord.listener.BanAssignListener\"", null, "discord");
        }
    }
}