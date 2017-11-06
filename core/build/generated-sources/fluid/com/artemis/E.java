package com.artemis;

import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.components.AttackDamageComponent;
import com.ziamor.incadium.components.AttackTargetComponent;
import com.ziamor.incadium.components.BlockPlayerInputComponent;
import com.ziamor.incadium.components.BlockingComponent;
import com.ziamor.incadium.components.DeadComponent;
import com.ziamor.incadium.components.FollowTargetComponent;
import com.ziamor.incadium.components.GroundTileComponent;
import com.ziamor.incadium.components.HealthComponent;
import com.ziamor.incadium.components.MonsterComponent;
import com.ziamor.incadium.components.MovementComponent;
import com.ziamor.incadium.components.MovementLerpComponent;
import com.ziamor.incadium.components.PlayerControllerComponent;
import com.ziamor.incadium.components.TerrainTileComponent;
import com.ziamor.incadium.components.TextureComponent;
import com.ziamor.incadium.components.TransformComponent;
import com.ziamor.incadium.components.TurnComponent;
import com.ziamor.incadium.components.TurnTakerComponent;
import java.lang.String;

public final class E {
  protected static SuperMapper _processingMapper;

  protected SuperMapper mappers;

  protected int entityId;

  public E init(SuperMapper mappers, int entityId) {
    this.mappers = mappers;
    this.entityId = entityId;
    return this;
  }

  public static E E(int entityId) {
    if(_processingMapper==null) throw new RuntimeException("SuperMapper system must be registered before any systems using E().");;
    return _processingMapper.getE(entityId);
  }

  public static E E(Entity entity) {
    return E(entity.getId());
  }

  public static E E() {
    return E(_processingMapper.getWorld().create());
  }

  public int id() {
    return entityId;
  }

  public Entity entity() {
    return mappers.getWorld().getEntity(entityId);
  }

  public boolean hasTurnTakerComponent() {
    return mappers.mTurnTakerComponent.has(entityId);
  }

  public boolean hasMonsterComponent() {
    return mappers.mMonsterComponent.has(entityId);
  }

  public boolean hasTransformComponent() {
    return mappers.mTransformComponent.has(entityId);
  }

  public boolean hasBlockingComponent() {
    return mappers.mBlockingComponent.has(entityId);
  }

  public boolean hasTextureComponent() {
    return mappers.mTextureComponent.has(entityId);
  }

  public boolean hasPlayerControllerComponent() {
    return mappers.mPlayerControllerComponent.has(entityId);
  }

  public boolean hasMovementLerpComponent() {
    return mappers.mMovementLerpComponent.has(entityId);
  }

  public boolean hasBlockPlayerInputComponent() {
    return mappers.mBlockPlayerInputComponent.has(entityId);
  }

  public boolean hasMovementComponent() {
    return mappers.mMovementComponent.has(entityId);
  }

  public boolean hasAttackDamageComponent() {
    return mappers.mAttackDamageComponent.has(entityId);
  }

  public boolean hasTerrainTileComponent() {
    return mappers.mTerrainTileComponent.has(entityId);
  }

  public boolean hasDeadComponent() {
    return mappers.mDeadComponent.has(entityId);
  }

  public boolean hasFollowTargetComponent() {
    return mappers.mFollowTargetComponent.has(entityId);
  }

  public boolean hasAttackTargetComponent() {
    return mappers.mAttackTargetComponent.has(entityId);
  }

  public boolean hasTurnComponent() {
    return mappers.mTurnComponent.has(entityId);
  }

  public boolean hasHealthComponent() {
    return mappers.mHealthComponent.has(entityId);
  }

  public boolean hasGroundTileComponent() {
    return mappers.mGroundTileComponent.has(entityId);
  }

  public E turnTakerComponent() {
    mappers.mTurnTakerComponent.create(entityId);
    return this;
  }

  public E monsterComponent() {
    mappers.mMonsterComponent.create(entityId);
    return this;
  }

  public E transformComponent() {
    mappers.mTransformComponent.create(entityId);
    return this;
  }

  public E blockingComponent() {
    mappers.mBlockingComponent.create(entityId);
    return this;
  }

  public E textureComponent() {
    mappers.mTextureComponent.create(entityId);
    return this;
  }

  public E playerControllerComponent() {
    mappers.mPlayerControllerComponent.create(entityId);
    return this;
  }

  public E movementLerpComponent() {
    mappers.mMovementLerpComponent.create(entityId);
    return this;
  }

  public E blockPlayerInputComponent() {
    mappers.mBlockPlayerInputComponent.create(entityId);
    return this;
  }

  public E movementComponent() {
    mappers.mMovementComponent.create(entityId);
    return this;
  }

  public E attackDamageComponent() {
    mappers.mAttackDamageComponent.create(entityId);
    return this;
  }

  public E terrainTileComponent() {
    mappers.mTerrainTileComponent.create(entityId);
    return this;
  }

  public E deadComponent() {
    mappers.mDeadComponent.create(entityId);
    return this;
  }

  public E followTargetComponent() {
    mappers.mFollowTargetComponent.create(entityId);
    return this;
  }

  public E attackTargetComponent() {
    mappers.mAttackTargetComponent.create(entityId);
    return this;
  }

  public E turnComponent() {
    mappers.mTurnComponent.create(entityId);
    return this;
  }

  public E healthComponent() {
    mappers.mHealthComponent.create(entityId);
    return this;
  }

  public E groundTileComponent() {
    mappers.mGroundTileComponent.create(entityId);
    return this;
  }

  public E tag(String tag) {
    mappers.getWorld().getSystem(com.artemis.managers.TagManager.class).register(tag, entityId);
    return this;
  }

  public String tag() {
    return mappers.getWorld().getSystem(com.artemis.managers.TagManager.class).getTag(entityId);
  }

  public E group(String group) {
    World w = mappers.getWorld();
    w.getSystem(com.artemis.managers.GroupManager.class).add(w.getEntity(entityId), group);
    return this;
  }

  public E groups(String... groups) {
    for (int i = 0; groups.length > i; i++) { group(groups[i]); };
    return this;
  }

  public E removeGroup(String group) {
    World w = mappers.getWorld();
    w.getSystem(com.artemis.managers.GroupManager.class).remove(w.getEntity(entityId), group);
    return this;
  }

  public E removeGroups(String... groups) {
    for (int i = 0; groups.length > i; i++) { removeGroup(groups[i]); };
    return this;
  }

  public E removeGroups() {
    World w = mappers.getWorld();
    w.getSystem(com.artemis.managers.GroupManager.class).removeFromAllGroups(w.getEntity(entityId));
    return this;
  }

  public ImmutableBag<String> groups() {
    World w = mappers.getWorld();
    return w.getSystem(com.artemis.managers.GroupManager.class).getGroups(w.getEntity(entityId));
  }

  public boolean isInGroup(String group) {
    World w = mappers.getWorld();
    return w.getSystem(com.artemis.managers.GroupManager.class).isInGroup(w.getEntity(entityId), group);
  }

  public E removeTurnTakerComponent() {
    mappers.mTurnTakerComponent.remove(entityId);
    return this;
  }

  public E removeMonsterComponent() {
    mappers.mMonsterComponent.remove(entityId);
    return this;
  }

  public E removeTransformComponent() {
    mappers.mTransformComponent.remove(entityId);
    return this;
  }

  public E removeBlockingComponent() {
    mappers.mBlockingComponent.remove(entityId);
    return this;
  }

  public E removeTextureComponent() {
    mappers.mTextureComponent.remove(entityId);
    return this;
  }

  public E removePlayerControllerComponent() {
    mappers.mPlayerControllerComponent.remove(entityId);
    return this;
  }

  public E removeMovementLerpComponent() {
    mappers.mMovementLerpComponent.remove(entityId);
    return this;
  }

  public E removeBlockPlayerInputComponent() {
    mappers.mBlockPlayerInputComponent.remove(entityId);
    return this;
  }

  public E removeMovementComponent() {
    mappers.mMovementComponent.remove(entityId);
    return this;
  }

  public E removeAttackDamageComponent() {
    mappers.mAttackDamageComponent.remove(entityId);
    return this;
  }

  public E removeTerrainTileComponent() {
    mappers.mTerrainTileComponent.remove(entityId);
    return this;
  }

  public E removeDeadComponent() {
    mappers.mDeadComponent.remove(entityId);
    return this;
  }

  public E removeFollowTargetComponent() {
    mappers.mFollowTargetComponent.remove(entityId);
    return this;
  }

  public E removeAttackTargetComponent() {
    mappers.mAttackTargetComponent.remove(entityId);
    return this;
  }

  public E removeTurnComponent() {
    mappers.mTurnComponent.remove(entityId);
    return this;
  }

  public E removeHealthComponent() {
    mappers.mHealthComponent.remove(entityId);
    return this;
  }

  public E removeGroundTileComponent() {
    mappers.mGroundTileComponent.remove(entityId);
    return this;
  }

  public TurnTakerComponent getTurnTakerComponent() {
    return mappers.mTurnTakerComponent.get(entityId);
  }

  public MonsterComponent getMonsterComponent() {
    return mappers.mMonsterComponent.get(entityId);
  }

  public TransformComponent getTransformComponent() {
    return mappers.mTransformComponent.get(entityId);
  }

  public BlockingComponent getBlockingComponent() {
    return mappers.mBlockingComponent.get(entityId);
  }

  public TextureComponent getTextureComponent() {
    return mappers.mTextureComponent.get(entityId);
  }

  public PlayerControllerComponent getPlayerControllerComponent() {
    return mappers.mPlayerControllerComponent.get(entityId);
  }

  public MovementLerpComponent getMovementLerpComponent() {
    return mappers.mMovementLerpComponent.get(entityId);
  }

  public BlockPlayerInputComponent getBlockPlayerInputComponent() {
    return mappers.mBlockPlayerInputComponent.get(entityId);
  }

  public MovementComponent getMovementComponent() {
    return mappers.mMovementComponent.get(entityId);
  }

  public AttackDamageComponent getAttackDamageComponent() {
    return mappers.mAttackDamageComponent.get(entityId);
  }

  public TerrainTileComponent getTerrainTileComponent() {
    return mappers.mTerrainTileComponent.get(entityId);
  }

  public DeadComponent getDeadComponent() {
    return mappers.mDeadComponent.get(entityId);
  }

  public FollowTargetComponent getFollowTargetComponent() {
    return mappers.mFollowTargetComponent.get(entityId);
  }

  public AttackTargetComponent getAttackTargetComponent() {
    return mappers.mAttackTargetComponent.get(entityId);
  }

  public TurnComponent getTurnComponent() {
    return mappers.mTurnComponent.get(entityId);
  }

  public HealthComponent getHealthComponent() {
    return mappers.mHealthComponent.get(entityId);
  }

  public GroundTileComponent getGroundTileComponent() {
    return mappers.mGroundTileComponent.get(entityId);
  }

  public E transformComponentY(float y) {
    mappers.mTransformComponent.create(this.entityId).y=y;
    return this;
  }

  public float transformComponentY() {
    return mappers.mTransformComponent.create(entityId).y;
  }

  public E transformComponentX(float x) {
    mappers.mTransformComponent.create(this.entityId).x=x;
    return this;
  }

  public float transformComponentX() {
    return mappers.mTransformComponent.create(entityId).x;
  }

  public E transformComponentZ(float z) {
    mappers.mTransformComponent.create(this.entityId).z=z;
    return this;
  }

  public float transformComponentZ() {
    return mappers.mTransformComponent.create(entityId).z;
  }

  public E transformComponent(float p0, float p1, float p2) {
    mappers.mTransformComponent.create(entityId).set(p0, p1, p2);
    return this;
  }

  public E textureComponentTexture(Texture texture) {
    mappers.mTextureComponent.create(this.entityId).texture=texture;
    return this;
  }

  public Texture textureComponentTexture() {
    return mappers.mTextureComponent.create(entityId).texture;
  }

  public E textureComponent(String p0) {
    mappers.mTextureComponent.create(entityId).set(p0);
    return this;
  }

  public E movementLerpComponentInterpolator(Interpolation interpolator) {
    mappers.mMovementLerpComponent.create(this.entityId).interpolator=interpolator;
    return this;
  }

  public Interpolation movementLerpComponentInterpolator() {
    return mappers.mMovementLerpComponent.create(entityId).interpolator;
  }

  public E movementLerpComponent(float p0, float p1, float p2, float p3, float p4) {
    mappers.mMovementLerpComponent.create(entityId).set(p0, p1, p2, p3, p4);
    return this;
  }

  public Vector2 movementLerpComponentCurrentPos() {
    return mappers.mMovementLerpComponent.create(entityId).getCurrentPos();
  }

  public boolean movementLerpComponentIsFinished() {
    return mappers.mMovementLerpComponent.create(entityId).isFinished();
  }

  public E blockPlayerInputComponentStartTime(long startTime) {
    mappers.mBlockPlayerInputComponent.create(this.entityId).startTime=startTime;
    return this;
  }

  public long blockPlayerInputComponentStartTime() {
    return mappers.mBlockPlayerInputComponent.create(entityId).startTime;
  }

  public E blockPlayerInputComponentDuration(float duration) {
    mappers.mBlockPlayerInputComponent.create(this.entityId).duration=duration;
    return this;
  }

  public float blockPlayerInputComponentDuration() {
    return mappers.mBlockPlayerInputComponent.create(entityId).duration;
  }

  public E blockPlayerInputComponent(float p0) {
    mappers.mBlockPlayerInputComponent.create(entityId).set(p0);
    return this;
  }

  public E movementComponentDirection(MovementComponent.Direction direction) {
    mappers.mMovementComponent.create(this.entityId).direction=direction;
    return this;
  }

  public MovementComponent.Direction movementComponentDirection() {
    return mappers.mMovementComponent.create(entityId).direction;
  }

  public E movementComponent(MovementComponent.Direction p0) {
    mappers.mMovementComponent.create(entityId).set(p0);
    return this;
  }

  public E attackDamageComponentDamage(float damage) {
    mappers.mAttackDamageComponent.create(this.entityId).damage=damage;
    return this;
  }

  public float attackDamageComponentDamage() {
    return mappers.mAttackDamageComponent.create(entityId).damage;
  }

  public E attackDamageComponentDmg(float p0) {
    mappers.mAttackDamageComponent.create(entityId).setDmg(p0);
    return this;
  }

  public E terrainTileComponentRegion(TextureRegion region) {
    mappers.mTerrainTileComponent.create(this.entityId).region=region;
    return this;
  }

  public TextureRegion terrainTileComponentRegion() {
    return mappers.mTerrainTileComponent.create(entityId).region;
  }

  public E terrainTileComponentRegion(String p0, int p1, int p2) {
    mappers.mTerrainTileComponent.create(entityId).setRegion(p0, p1, p2);
    return this;
  }

  public E terrainTileComponentRegion(Texture p0, int p1, int p2) {
    mappers.mTerrainTileComponent.create(entityId).setRegion(p0, p1, p2);
    return this;
  }

  public E followTargetComponentTarget(int target) {
    mappers.mFollowTargetComponent.create(this.entityId).target=target;
    return this;
  }

  public int followTargetComponentTarget() {
    return mappers.mFollowTargetComponent.create(entityId).target;
  }

  public E followTargetComponent(int p0) {
    mappers.mFollowTargetComponent.create(entityId).set(p0);
    return this;
  }

  public E attackTargetComponentTarget(int target) {
    mappers.mAttackTargetComponent.create(this.entityId).target=target;
    return this;
  }

  public int attackTargetComponentTarget() {
    return mappers.mAttackTargetComponent.create(entityId).target;
  }

  public E attackTargetComponent(int p0) {
    mappers.mAttackTargetComponent.create(entityId).set(p0);
    return this;
  }

  public E turnComponentFinishedTurn(boolean finishedTurn) {
    mappers.mTurnComponent.create(this.entityId).finishedTurn=finishedTurn;
    return this;
  }

  public boolean turnComponentFinishedTurn() {
    return mappers.mTurnComponent.create(entityId).finishedTurn;
  }

  public E healthComponentMaxHealth(float maxHealth) {
    mappers.mHealthComponent.create(this.entityId).maxHealth=maxHealth;
    return this;
  }

  public float healthComponentMaxHealth() {
    return mappers.mHealthComponent.create(entityId).maxHealth;
  }

  public E healthComponentCurrentHealth(float currentHealth) {
    mappers.mHealthComponent.create(this.entityId).currentHealth=currentHealth;
    return this;
  }

  public float healthComponentCurrentHealth() {
    return mappers.mHealthComponent.create(entityId).currentHealth;
  }

  public E healthComponentHealthStat(float p0, float p1) {
    mappers.mHealthComponent.create(entityId).setHealthStat(p0, p1);
    return this;
  }

  public E healthComponentMaxHp(float p0) {
    mappers.mHealthComponent.create(entityId).setMaxHp(p0);
    return this;
  }

  public E groundTileComponentGroundTexture(Texture groundTexture) {
    mappers.mGroundTileComponent.create(this.entityId).groundTexture=groundTexture;
    return this;
  }

  public Texture groundTileComponentGroundTexture() {
    return mappers.mGroundTileComponent.create(entityId).groundTexture;
  }

  public void deleteFromWorld() {
    mappers.getWorld().delete(entityId);
  }

  public boolean isTurnTakerComponent() {
    return mappers.mTurnTakerComponent.has(entityId);
  }

  public E turnTakerComponent(boolean value) {
    mappers.mTurnTakerComponent.set(entityId, value);
    return this;
  }

  public boolean isMonsterComponent() {
    return mappers.mMonsterComponent.has(entityId);
  }

  public E monsterComponent(boolean value) {
    mappers.mMonsterComponent.set(entityId, value);
    return this;
  }

  public boolean isBlockingComponent() {
    return mappers.mBlockingComponent.has(entityId);
  }

  public E blockingComponent(boolean value) {
    mappers.mBlockingComponent.set(entityId, value);
    return this;
  }

  public boolean isPlayerControllerComponent() {
    return mappers.mPlayerControllerComponent.has(entityId);
  }

  public E playerControllerComponent(boolean value) {
    mappers.mPlayerControllerComponent.set(entityId, value);
    return this;
  }

  public boolean isDeadComponent() {
    return mappers.mDeadComponent.has(entityId);
  }

  public E deadComponent(boolean value) {
    mappers.mDeadComponent.set(entityId, value);
    return this;
  }
}
