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
package de.kissenpvp.discord.loader;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.base.loader.Loadable;
import de.kissenpvp.api.base.plugin.KissenPlugin;
import de.kissenpvp.api.reflection.ReflectionClass;
import de.kissenpvp.discord.api.Bot;
import de.kissenpvp.discord.api.command.DiscordCommand;
import de.kissenpvp.discord.api.command.SlashCommand;
import de.kissenpvp.discord.api.command.SlashCommandDescription;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class DiscordCommandLoader implements Loadable
{
    @Override
    public boolean isLoadable(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {
        return Kissen.getInstance().getImplementation(Bot.class).isEnabled() && reflectionClass.getJavaClass().getAnnotation(DiscordCommand.class) != null && !reflectionClass.isAbstract();
    }

    @Override
    public void load(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {
        Kissen.getInstance().getImplementation(Bot.class).addCommand(new SlashCommandDescription()
        {
            @Override
            public DiscordCommand getCommandInfo()
            {
                return reflectionClass.getJavaClass().getAnnotation(DiscordCommand.class);
            }

            @Override
            public SlashCommand getExecutable()
            {
                return (SlashCommand) reflectionClass.newInstance();
            }
        });
    }

    @Override
    public void enable(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {
        /* Ignored */
    }
}