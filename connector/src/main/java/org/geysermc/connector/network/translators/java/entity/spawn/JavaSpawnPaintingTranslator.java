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

package org.geysermc.connector.network.translators.java.entity.spawn;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.nukkitx.math.vector.Vector3f;
import org.geysermc.connector.entity.EntityDefinition;
import org.geysermc.connector.entity.type.Entity;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.PacketTranslator;
import org.geysermc.connector.network.translators.Translator;
import org.geysermc.connector.registry.Registries;

@Translator(packet = ServerSpawnPaintingPacket.class)
public class JavaSpawnPaintingTranslator extends PacketTranslator<ServerSpawnPaintingPacket> {

    @Override
    public void translate(GeyserSession session, ServerSpawnPaintingPacket packet) {
        Vector3f position = Vector3f.from(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());

        EntityDefinition<?> definition = Registries.ENTITY_DEFINITIONS.get(EntityType.PAINTING);
        // noinspection ConstantConditions
        Entity entity = definition.newEntity(
                session,
                packet.getEntityId(),
                session.getEntityCache().getNextEntityId().incrementAndGet(),
                position,
                Vector3f.ZERO,
                Vector3f.ZERO
        );

        entity.postInitialize(packet);
        session.getEntityCache().spawnEntity(entity);
    }
}
