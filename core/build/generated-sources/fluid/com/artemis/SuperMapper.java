package com.artemis;

import com.artemis.utils.Bag;
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

public final class SuperMapper extends BaseSystem {
  protected Bag es = new Bag(128);

  public ComponentMapper<TurnTakerComponent> mTurnTakerComponent;

  public ComponentMapper<MonsterComponent> mMonsterComponent;

  public ComponentMapper<TransformComponent> mTransformComponent;

  public ComponentMapper<BlockingComponent> mBlockingComponent;

  public ComponentMapper<TextureComponent> mTextureComponent;

  public ComponentMapper<PlayerControllerComponent> mPlayerControllerComponent;

  public ComponentMapper<MovementLerpComponent> mMovementLerpComponent;

  public ComponentMapper<BlockPlayerInputComponent> mBlockPlayerInputComponent;

  public ComponentMapper<MovementComponent> mMovementComponent;

  public ComponentMapper<AttackDamageComponent> mAttackDamageComponent;

  public ComponentMapper<TerrainTileComponent> mTerrainTileComponent;

  public ComponentMapper<DeadComponent> mDeadComponent;

  public ComponentMapper<FollowTargetComponent> mFollowTargetComponent;

  public ComponentMapper<AttackTargetComponent> mAttackTargetComponent;

  public ComponentMapper<TurnComponent> mTurnComponent;

  public ComponentMapper<HealthComponent> mHealthComponent;

  public ComponentMapper<GroundTileComponent> mGroundTileComponent;

  protected void initialize() {
    E._processingMapper=this;
  }

  public void processSystem() {
    E._processingMapper=this;
  }

  E getE(int entityId) {
    E e = (E) es.safeGet(entityId);
    if ( e == null ) { e = new E().init(this,entityId); es.set(entityId, e); };
    return e;
  }
}
