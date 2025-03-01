package entity.tower;

import entity.enemy.Enemy;
import game.Element;
import game.Point2D;
import graphic.StdDraw;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;

public class EarthCasterTower extends Tower {
	public EarthCasterTower(Point2D position) {
		super(position, 50, Element.EARTH, 7, 0.5, 2.5, 100);
	}
	
	@Override
	public Enemy selectTarget(List<Enemy> enemies) {
		// Vise l'ennemi avec le plus de PV à portée
		return enemies.stream()
				.filter(this::isInRange)
				.max(Comparator.comparingInt(Enemy::getHealth))
				.orElse(null);
	}
	
	@Override
	protected void moveAlongPath(double deltaTime) {
		// Les tours ne bougent pas
	}
	
	@Override
	public double calculateDamage(Enemy target) {
		double damage = super.calculateDamage(target);
		// 1.0 case = environ 70 pixels (selon l'affichage)
		List<Enemy> nearbyEnemies = target.getNearbyEnemies(70.0);
		for (Enemy nearby : nearbyEnemies) {
			nearby.takeDamage((int)damage);
		}
		return damage;
	}
}