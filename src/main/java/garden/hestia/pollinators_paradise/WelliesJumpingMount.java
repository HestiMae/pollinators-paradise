package garden.hestia.pollinators_paradise;

import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.player.PlayerEntity;

public class WelliesJumpingMount implements JumpingMount {

	private final PlayerEntity player;
	public float jumpStrength = 0.0F;

	public WelliesJumpingMount(PlayerEntity player)
	{
		this.player = player;
	}
	@Override
	public void setJumpStrength(int strength) {
		if (strength >= 90) {
			this.jumpStrength = 2.4F;
		} else {
			this.jumpStrength = 0.4F + 2.0F * (float)strength / 90.0F;
		}

	}

	@Override
	public boolean canJump() {
		return true;
	}

	// server
	@Override
	public void startJumping(int height) {
		setJumpStrength(height);
	}

	@Override
	public void stopJumping() {

	}
}
