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
package de.kissenpvp.discord.loader;

import de.kissenpvp.api.base.Kissen;
import de.kissenpvp.api.base.loader.Loadable;
import de.kissenpvp.api.base.plugin.KissenPlugin;
import de.kissenpvp.api.reflection.ReflectionClass;
import de.kissenpvp.discord.api.Bot;
import org.javacord.api.listener.GloballyAttachableListener;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class ListenerLoader implements Loadable
{
    @Override public boolean isLoadable(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {
        return GloballyAttachableListener.class.isAssignableFrom(reflectionClass.getJavaClass());
    }

    @Override public void load(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {
        Kissen.getInstance().getImplementation(Bot.class).registerListener((GloballyAttachableListener) reflectionClass.getInstance());
    }

    @Override public void enable(ReflectionClass reflectionClass, KissenPlugin kissenPlugin)
    {

    }
}