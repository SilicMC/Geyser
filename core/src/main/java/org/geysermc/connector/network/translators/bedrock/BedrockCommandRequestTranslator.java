/*
 * Copyright (c) 2019-2021 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.connector.network.translators.bedrock;

import org.geysermc.common.PlatformType;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.command.CommandManager;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.PacketTranslator;
import org.geysermc.connector.network.translators.Translator;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import org.geysermc.connector.network.translators.chat.MessageTranslator;

@Translator(packet = CommandRequestPacket.class)
public class BedrockCommandRequestTranslator extends PacketTranslator<CommandRequestPacket> {

    @Override
    public void translate(GeyserSession session, CommandRequestPacket packet) {
        String command = packet.getCommand().replace("/", "");
        CommandManager commandManager = GeyserConnector.getInstance().getCommandManager();
        if (session.getConnector().getPlatformType() == PlatformType.STANDALONE && command.trim().startsWith("geyser ") && commandManager.getCommands().containsKey(command.split(" ")[1])) {
            commandManager.runCommand(session, command);
        } else {
            String message = packet.getCommand().trim();

            if (MessageTranslator.isTooLong(message, session)) {
                return;
            }

            ServerboundChatPacket chatPacket = new ServerboundChatPacket(message);
            session.sendDownstreamPacket(chatPacket);
        }
    }
}