package entity.enemy;

import entity.Entity;
import entity.tower.Tower;
import game.Element;
import game.Point2D;
import game.map.Map;
import graphic.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un ennemi dans le jeu.
 * Cette classe définit le comportement commun à tous les types d'ennemis.
 */
public abstract class Enemy extends Entity {
    protected double speed;                // Vitesse de déplacement
    protected int reward;                  // Récompense donnée au joueur lors de la destruction
    protected double distanceTraveled;     // Distance totale parcourue
    protected List<Point2D> path;          // Chemin à suivre
    protected double attackTimer = 0;      // Timer d'attaque

    /**
     * Constructeur pour initialiser un ennemi avec des caractéristiques spécifiques.
     *
     * @param position     Position initiale de l'ennemi.
     * @param health       Points de vie de l'ennemi.
     * @param element      Élément associé à l'ennemi (Feu, Eau, Terre, Air).
     * @param attackPower  Puissance d'attaque.
     * @param attackSpeed  Temps entre deux attaques (en secondes).
     * @param range        Portée de l'ennemi.
     * @param speed        Vitesse de déplacement.
     * @param reward       Récompense donnée au joueur lorsque l'ennemi est éliminé.
     */
    public Enemy(Point2D position, int health, Element element,
                 double attackPower, double attackSpeed, double range,
                 double speed, int reward) {
        super(position, health, element, attackPower, attackSpeed, range);
        this.speed = speed;
        this.reward = reward;
        this.distanceTraveled = 0;
    }

    /**
     * Retourne la distance totale parcourue par l'ennemi.
     *
     * @return La distance parcourue.
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Déplace l'ennemi le long de son chemin en fonction du temps écoulé.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    protected void moveAlongPath(double deltaTime) {
        if (path == null || path.isEmpty()) return;

        double distanceToMove = speed * deltaTime; // Distance à parcourir
        distanceTraveled += distanceToMove;

        double accumulatedDistance = 0;
        Point2D previousPoint = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            Point2D currentPoint = path.get(i);
            double segmentLength = previousPoint.distance(currentPoint);

            if (accumulatedDistance + segmentLength >= distanceTraveled) {
                double segmentProgress = (distanceTraveled - accumulatedDistance) / segmentLength;
                double newX = previousPoint.getX() + (currentPoint.getX() - previousPoint.getX()) * segmentProgress;
                double newY = previousPoint.getY() + (currentPoint.getY() - previousPoint.getY()) * segmentProgress;
                position = new Point2D(newX, newY);
                return;
            }

            accumulatedDistance += segmentLength;
            previousPoint = currentPoint;
        }

        position = new Point2D(path.get(path.size() - 1)); // Fin du chemin
    }

    /**
     * Dessine l'ennemi à sa position actuelle.
     */
    @Override
    public void draw() {
        Point2D screenPos = gameMap.getScreenPosition(position); // Position à l'écran
        Point2D originalPos = position;
        position = screenPos;

        StdDraw.setPenColor(element.getColor()); // Couleur selon l'élément
        StdDraw.circle(screenPos.getX(), screenPos.getY(), 15); // Dessin du cercle
        drawHealthBar(); // Dessin de la barre de vie
        position = originalPos;
    }

    /**
     * Définit le chemin que l'ennemi doit suivre.
     *
     * @param path Liste des points représentant le chemin.
     */
    public void setPath(List<Point2D> path) {
        this.path = path;
    }

    /**
     * Vérifie si l'ennemi a atteint la base.
     *
     * @return {@code true} si l'ennemi a atteint la base, sinon {@code false}.
     */
    public boolean hasReachedBase() {
        return path != null && !path.isEmpty() &&
                distanceTraveled >= getTotalPathLength();
    }

    /**
     * Calcule la longueur totale du chemin que l'ennemi doit parcourir.
     *
     * @return La longueur totale du chemin.
     */
    private double getTotalPathLength() {
        if (path == null || path.isEmpty()) return 0;

        double totalLength = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalLength += path.get(i).distance(path.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Retourne la récompense donnée au joueur lorsque l'ennemi est éliminé.
     *
     * @return La récompense en pièces d'or.
     */
    public int getReward() {
        return reward;
    }

    /**
     * Inflige des dégâts à l'ennemi.
     *
     * @param damage Quantité de dégâts à infliger.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Retourne les ennemis proches dans un rayon donné.
     *
     * @param radius Rayon dans lequel chercher les ennemis.
     * @return Une liste d'ennemis proches.
     */
    public List<Enemy> getNearbyEnemies(double radius) {
        List<Enemy> nearby = new ArrayList<>();
        if (gameMap == null) return nearby;

        for (Enemy enemy : gameMap.getEnemies()) {
            if (enemy != this && position.distance(enemy.getPosition()) <= radius) {
                nearby.add(enemy);
            }
        }
        return nearby;
    }

    /**
     * Vérifie si une cible est à portée de l'ennemi.
     *
     * @param target La cible à vérifier.
     * @return {@code true} si la cible est à portée, sinon {@code false}.
     */
    public boolean isInRange(Entity target) {
        double distance = position.distance(target.getPosition());
        return distance <= range;
    }

    /**
     * Calcule les dégâts infligés à une tour en fonction des éléments.
     *
     * @param target La tour ciblée.
     * @return Les dégâts infligés.
     */
    public double calculateDamage(Tower target) {
        double multiplier = 1.0;

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

    /**
     * Sélectionne la cible à attaquer parmi une liste de tours.
     *
     * @param towers La liste des tours disponibles pour cible.
     * @return La cible choisie, ou {@code null} si aucune cible valide.
     */
    public abstract Tower selectTarget(List<Tower> towers);

    /**
     * Effectue une attaque sur une cible si possible.
     *
     * @param towers La liste des tours disponibles.
     */
    public void attack(List<Tower> towers) {
        if (canAttack()) {
            Tower target = selectTarget(towers);
            if (target != null) {
                double damage = calculateDamage(target);
                target.takeDamage((int) damage);
                resetAttackTimer();
                drawAttackAnimation(target);
            }
        }
    }

    /**
     * Dessine une animation simple pour représenter une attaque.
     *
     * @param target La cible de l'attaque.
     */
    protected void drawAttackAnimation(Tower target) {
        StdDraw.setPenColor(element.getColor());
        StdDraw.line(position.getX(), position.getY(),
                     target.getPosition().getX(), target.getPosition().getY());
    }

    /**
     * Vérifie si l'ennemi peut attaquer (basé sur le timer d'attaque).
     *
     * @return {@code true} si l'ennemi peut attaquer, sinon {@code false}.
     */
    public boolean canAttack() {
        return attackTimer >= attackSpeed;
    }

    /**
     * Réinitialise le timer d'attaque de l'ennemi.
     */
    public void resetAttackTimer() {
        attackTimer = 0;
    }

    /**
     * Met à jour l'état de l'ennemi à chaque itération de la boucle de jeu.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    public void update(double deltaTime) {
        moveAlongPath(deltaTime);
        attackTimer += deltaTime;
    }
}
