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
package de.kissenpvp.discord.language;

import de.kissenpvp.api.message.language.Sentence;

/**
 * @author Taubsie
 * @since 1.0.0
 */
public class InvalidToken implements Sentence
{
    @Override public String getGroup()
    {
        return "discord";
    }

    @Override public String getCode()
    {
        return "token_invalid";
    }

    @Override public MessageType getType()
    {
        return MessageType.ERROR;
    }

    @Override public String getDefault()
    {
        return "Please enter a valid discord token in the configuration.";
    }
}