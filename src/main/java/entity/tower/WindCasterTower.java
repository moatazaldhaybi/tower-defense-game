package entity.tower;

import entity.enemy.Enemy;
import game.Element;
import game.Point2D;

import java.util.Comparator;
import java.util.List;

public class WindCasterTower extends Tower {
	public WindCasterTower(Point2D position) {
		super(position, 30, Element.AIR, 5, 1.5, 6, 50);
	}
	
	@Override
	public Enemy selectTarget(List<Enemy> enemies) {
		// Vise l'ennemi le plus proche à portée
		return enemies.stream()
				.filter(e -> isInRange(e))
				.min(Comparator.comparingDouble(e ->
						position.distance(e.getPosition())))
				.orElse(null);
	}
	
	@Override
	protected void moveAlongPath(double deltaTime) {
	
	}
}