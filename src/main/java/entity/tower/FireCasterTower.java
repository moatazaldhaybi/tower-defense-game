package entity.tower;

import entity.enemy.Enemy;
import game.Element;
import game.Point2D;
import graphic.StdDraw;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;

public class FireCasterTower extends Tower {
	public FireCasterTower(Point2D position) {
		super(position, 30, Element.FIRE, 10, 0.5, 2.5, 100);
	}
	
	@Override
	public Enemy selectTarget(List<Enemy> enemies) {
		// Vise l'ennemi le plus proche à portée
		return enemies.stream()
				.filter(this::isInRange)
				.min(Comparator.comparingDouble(e ->
						position.distance(e.getPosition())))
				.orElse(null);
	}
	
	
	@Override
	protected void moveAlongPath(double deltaTime) {
		// Les tours ne bougent pas
	}
}