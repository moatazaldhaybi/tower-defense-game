package entity.tower;

import entity.enemy.Enemy;
import game.Element;
import game.Point2D;
import graphic.StdDraw;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;

public class WaterCasterTower extends Tower {
	public WaterCasterTower(Point2D position) {
		super(position, 30, Element.WATER, 3, 1, 4, 50);
	}
	
	@Override
	public Enemy selectTarget(List<Enemy> enemies) {
		// Vise l'ennemi le plus avancé à portée (comme l'Archer)
		return enemies.stream()
				.filter(this::isInRange)
				.max(Comparator.comparingDouble(Enemy::getDistanceTraveled))
				.orElse(null);
	}
	
	@Override
	protected void moveAlongPath(double deltaTime) {
		// Les tours ne bougent pas
	}
}