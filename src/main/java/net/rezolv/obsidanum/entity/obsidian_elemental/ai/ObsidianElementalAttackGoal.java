package net.rezolv.obsidanum.entity.obsidian_elemental.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

public class ObsidianElementalAttackGoal extends MeleeAttackGoal {
    private final ObsidianElemental entity;
    private int attackDelay = 23; // Длительность анимации атаки
    private int ticksUntilNextAttack = 40;
    private boolean shouldCountTillNextAttack = false;

    public ObsidianElementalAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((ObsidianElemental) pMob);
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 23;
        ticksUntilNextAttack = 40;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
                entity.attackAnimationState.start(entity.tickCount); // Начинаем анимацию атаки
            }

            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= attackDelay / 2; // Выполняем атаку в середине анимации
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack == attackDelay;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
        this.resetAttackCooldown();
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}