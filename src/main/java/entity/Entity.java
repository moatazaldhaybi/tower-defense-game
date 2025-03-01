package entity;

import game.Element;
import game.Point2D;
import game.map.Map;
import graphic.StdDraw;

import java.awt.*;

/**
 * Classe abstraite représentant une entité dans le jeu.
 * Les entités peuvent avoir des points de vie, une position, une portée, et peuvent attaquer ou se déplacer.
 */
public abstract class Entity {
    protected Point2D position;          // Position actuelle de l'entité
    protected int maxHealth;             // Points de vie maximum
    protected int health;                // Points de vie actuels
    protected Element element;           // Élément associé à l'entité
    protected double attackPower;        // Puissance d'attaque
    public double attackSpeed;           // Temps entre deux attaques
    protected double range;              // Portée d'attaque
    public Map gameMap;                  // Référence à la carte sur laquelle se trouve l'entité

    /**
     * Constructeur pour initialiser une entité avec ses caractéristiques spécifiques.
     *
     * @param position    Position initiale de l'entité.
     * @param health      Points de vie de l'entité.
     * @param element     Élément associé à l'entité (Feu, Eau, Terre, Air, etc.).
     * @param attackPower Puissance d'attaque.
     * @param attackSpeed Temps entre deux attaques (en secondes).
     * @param range       Portée de l'entité.
     */
    public Entity(Point2D position, int health, Element element,
                  double attackPower, double attackSpeed, double range) {
        this.position = position;
        this.maxHealth = health;
        this.health = health;
        this.element = element;
        this.attackPower = attackPower;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    /**
     * Met à jour l'état de l'entité à chaque itération de la boucle de jeu.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    public abstract void update(double deltaTime);

    /**
     * Dessine l'entité à sa position actuelle.
     */
    public abstract void draw();

    /**
     * Dessine une barre de vie au-dessus de l'entité, avec une couleur verte pour les PV restants
     * et rouge pour les PV perdus.
     */
    protected void drawHealthBar() {
        double barWidth = 35;   // Largeur fixe pour la barre de vie
        double barHeight = 5;   // Hauteur fixe pour la barre de vie

        // Position au-dessus de l'entité
        double barX = position.getX();
        double barY = position.getY() + 20;  // Décalage fixe au-dessus de l'entité

        // Fond rouge de la barre
        StdDraw.setPenColor(Color.RED);
        StdDraw.filledRectangle(barX, barY, barWidth / 2, barHeight / 2);

        // Partie verte représentant la vie restante
        StdDraw.setPenColor(Color.GREEN);
        double healthWidth = (barWidth * health) / maxHealth;
        StdDraw.filledRectangle(barX - (barWidth - healthWidth) / 2, barY, healthWidth / 2, barHeight / 2);

        // Contour noir
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(barX, barY, barWidth / 2, barHeight / 2);
    }

    /**
     * Vérifie si une cible est à portée de l'entité.
     *
     * @param target L'entité cible.
     * @return {@code true} si la cible est à portée, sinon {@code false}.
     */
    protected boolean isInRange(Entity target) {
        double distance = position.distance(target.getPosition());
        return distance <= range;
    }

    /**
     * Retourne la position actuelle de l'entité.
     *
     * @return La position de l'entité.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Déplace l'entité le long d'un chemin en fonction du temps écoulé.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    protected abstract void moveAlongPath(double deltaTime);

    /**
     * Retourne les points de vie actuels de l'entité.
     *
     * @return Les points de vie de l'entité.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Retourne la puissance d'attaque de l'entité.
     *
     * @return La puissance d'attaque.
     */
    public int getAttackPower() {
        return (int) attackPower;
    }

    /**
     * Retourne l'élément associé à l'entité.
     *
     * @return L'élément de l'entité.
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * Inflige des dégâts à l'entité.
     *
     * @param damage La quantité de dégâts à infliger.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Définit la carte sur laquelle l'entité évolue.
     *
     * @param gameMap La carte de l'entité.
     */
    public void setGameMap(Map gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * Retourne la carte sur laquelle l'entité évolue.
     *
     * @return La carte de l'entité.
     */
    public Map getGameMap() {
        return gameMap;
    }
}
