package entity.tower;

import entity.enemy.Enemy;
import game.Element;
import game.Point2D;
import graphic.StdDraw;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;

public class ArcherTower extends Tower {
	public ArcherTower(Point2D position) {
		super(position, 30, Element.NONE, 5, 1, 2, 20);
	}
	
	@Override
	public Enemy selectTarget(List<Enemy> enemies) {
		System.out.println("Nombre d'ennemis: " + enemies.size());
		
		// Vise l'ennemi le plus avancé à portée
		Enemy target = enemies.stream()
				.peek(e -> {
					boolean inRange = isInRange(e);
				})
				.filter(this::isInRange)
				.max(Comparator.comparingDouble(Enemy::getDistanceTraveled))
				.orElse(null);
		
		return target;
	}
	
	@Override
	protected void moveAlongPath(double deltaTime) {
		// Les tours ne bougent pas
	}
}