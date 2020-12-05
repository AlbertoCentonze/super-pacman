package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;

abstract public class Ghost extends MovableAreaEntity {
  final static int GHOST_SCORE = 500;
  final static int SIGHT_RADIUS = 5;
  final static int GHOST_SPEED = 16;

  private Animation afraidAnimation;
  private Animation ghostAnimation;

  private DiscreteCoordinates respawnPoint;
  private boolean playerMemorized = false;

  public Ghost(Area area, DiscreteCoordinates position, String ghostType) {
    super(area, Orientation.UP, position);
    this.respawnPoint = position;

    Sprite[] ghostSpritesheet = RPGSprite.extractSprites("superpacman/ghost." + ghostType, 2, 1.f, 1.f, this, 16, 16);
    ghostAnimation = new Animation(3, ghostSpritesheet);

    Sprite[] afraidSpritesheet = RPGSprite.extractSprites("superpacman/ghost.afraid", 2, 1.f, 1.f, this, 16, 16);
    afraidAnimation = new Animation(3, afraidSpritesheet);
  }

  protected abstract Orientation getNextOrientation();

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);

    move(GHOST_SPEED);

    if (getIsAfraid())
      afraidAnimation.update(deltaTime);
    else
      ghostAnimation.update(deltaTime);
  }

  @Override
  public void draw(Canvas canvas) {
    if (getIsAfraid())
      afraidAnimation.draw(canvas);
    else
      ghostAnimation.draw(canvas);
  }

  public boolean getIsAfraid() {
    return ((SuperPacmanArea) getOwnerArea()).isEveryGhostScared();
  }

  public void respawn() {
    playerMemorized = false;
    setCurrentPosition(respawnPoint.toVector());
  }

  @Override
  final public boolean isViewInteractable() {
    return false;
  }

  @Override
  final public boolean isCellInteractable() {
    return true;
  }

  @Override
  public final boolean takeCellSpace() {
    return false;
  }

  @Override
  public void acceptInteraction(AreaInteractionVisitor v) {
    ((SuperPacmanInteractionVisitor) v).interactWith(this);
  }

  @Override
  public List<DiscreteCoordinates> getCurrentCells() {
    return Collections.singletonList(getCurrentMainCellCoordinates());
  }
}
