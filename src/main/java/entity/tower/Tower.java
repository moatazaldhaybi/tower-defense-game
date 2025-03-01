package entity.tower;

import entity.Entity;
import entity.enemy.Enemy;
import game.Element;
import game.Point2D;
import game.map.Map;
import graphic.StdDraw;

import java.awt.*;
import java.util.List;

public abstract class Tower extends Entity {
	protected int cost;
	protected double lastAttackTime;
	
	public double attackTimer = 0;
	
	public Tower(Point2D position, int health, Element element,
				 double attackPower, double attackSpeed, double range, int cost) {
		super(position, health, element, attackPower, attackSpeed, range);
		this.cost = cost;
		this.lastAttackTime = 0;
	}
	
	public abstract Enemy selectTarget(List<Enemy> enemies);
	
	public double calculateDamage(Enemy target) {
		// Calcul des dégâts selon les vulnérabilités élémentaires
		double multiplier = 1.0;
		
		// Cycle élémentaire : FEU < EAU < AIR < TERRE < FEU
		if (this.element == Element.FIRE && target.getElement() == Element.WATER ||
				this.element == Element.WATER && target.getElement() == Element.AIR ||
				this.element == Element.AIR && target.getElement() == Element.EARTH ||
				this.element == Element.EARTH && target.getElement() == Element.FIRE) {
			multiplier = 0.5; // Résistance
		} else if (this.element == Element.FIRE && target.getElement() == Element.EARTH ||
				this.element == Element.EARTH && target.getElement() == Element.AIR ||
				this.element == Element.AIR && target.getElement() == Element.WATER ||
				this.element == Element.WATER && target.getElement() == Element.FIRE) {
			multiplier = 1.5; // Vulnérabilité
		}
		
		return attackPower * multiplier;
	}
	
	public boolean canAttack() {
		return attackTimer >= attackSpeed;
	}
	
	public void resetAttackTimer() {
		attackTimer = 0;
	}
	
	public int getCost() {
		return cost;
	}
	
	protected double getTowerSize() {
		// La taille de la tour sera 40% d'une case
		return (700.0 / Math.max(10, 10)) * 0.4; // Pour une grille 10x10
	}
	
	@Override
	public void draw() {
		// Utilise la couleur de l'élément
		StdDraw.setPenColor(element.getColor());
		
		// Dessine la tour comme un carré
		double size = getTowerSize();
		StdDraw.filledSquare(position.getX(), position.getY(), size);
		
		// Dessine une bordure noire
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.square(position.getX(), position.getY(), size);
		
		// Dessine la barre de vie
		drawHealthBar();
	}
	
	public void attack(List<Enemy> enemies) {
		if (canAttack()) {
			System.out.println("Tour peut attaquer");
			Enemy target = selectTarget(enemies);
			if (target != null) {
				System.out.println("Cible trouvée");
				if (isInRange(target)) {
					System.out.println("Cible à portée");
					double damage = calculateDamage(target);
					System.out.println("Dégâts calculés: " + damage);
					target.takeDamage((int)damage);
					resetAttackTimer();
					drawAttackAnimation(target);
				} else {
					System.out.println("Cible hors portée");
				}
			} else {
				System.out.println("Pas de cible trouvée");
			}
		}
	}
	
	protected void drawAttackAnimation(Enemy target) {
		if (gameMap != null) {
			StdDraw.setPenColor(element.getColor());
			Point2D sourceScreenPos = gameMap.getScreenPosition(position);
			Point2D targetScreenPos = gameMap.getScreenPosition(target.getPosition());
			StdDraw.line(sourceScreenPos.getX(), sourceScreenPos.getY(),
					targetScreenPos.getX(), targetScreenPos.getY());
		}
	}
	
	@Override
	public void update(double deltaTime) {
		attackTimer += deltaTime;
		//System.out.println("AttackTimer: " + attackTimer + ", AttackSpeed: " + attackSpeed + ", peut attaquer: " + (attackTimer >= attackSpeed));
		
	}
	
	@Override
	protected boolean isInRange(Entity target) {
		// On convertit la position de la tour en coordonnées grille
		Map gameMap = ((Enemy)target).getGameMap();
		Point2D gridPos = gameMap.getGridPosition(position.getX(), position.getY());
		
		// Maintenant on peut calculer la distance en coordonnées grille
		double distance = gridPos.distance(target.getPosition());
		return distance <= range;
	}
}