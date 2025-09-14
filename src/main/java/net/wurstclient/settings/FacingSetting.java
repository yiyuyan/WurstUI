/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.settings;

import java.util.function.Consumer;

import net.minecraft.util.math.Vec3d;
import net.wurstclient.WurstClient;
import net.wurstclient.util.RotationUtils;

public class FacingSetting extends EnumSetting<FacingSetting.Facing>
{
	public static final WurstClient WURST = WurstClient.INSTANCE;
	
	public FacingSetting(String name, String description, Facing[] values,
		Facing selected)
	{
		super(name, description, values, selected);
	}
	
	public static FacingSetting withoutPacketSpam(String description)
	{
		return withoutPacketSpam("Facing", description, Facing.SERVER);
	}
	
	public static FacingSetting withoutPacketSpam(String name,
		String description, Facing selected)
	{
		Facing[] values = {Facing.OFF, Facing.SERVER, Facing.CLIENT};
		return new FacingSetting(name, description, values, selected);
	}
	
	public static FacingSetting withPacketSpam(String name, String description,
		Facing selected)
	{
		return new FacingSetting(name, description, Facing.values(), selected);
	}
	
	public enum Facing
	{
		OFF("Off", v -> {}),
		
		SERVER("Server-side",
			v -> WURST.getRotationFaker().faceVectorPacket(v)),
		
		CLIENT("Client-side",
			v -> WURST.getRotationFaker().faceVectorClient(v)),
		
		SPAM("Packet spam",
			v -> RotationUtils.getNeededRotations(v).sendPlayerLookPacket());
		
		public String name;
		public Consumer<Vec3d> face;
		
		Facing(String name, Consumer<Vec3d> face)
		{
			this.name = name;
			this.face = face;
		}
		
		public void face(Vec3d v)
		{
			face.accept(v);
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
