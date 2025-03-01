package entity;

import graphic.StdDraw;
import java.awt.Color;

/**
 * Classe représentant le joueur dans le jeu.
 * Le joueur dispose d'une quantité de vie et d'argent, qui sont affichés dans une interface graphique.
 */
public class Player {
    private int health = 100; // Points de vie initiaux du joueur
    private int money = 50;  // Argent initial du joueur

    // Position et dimensions de l'interface joueur
    private static final double INFO_CENTER_X = 856;
    private static final double INFO_CENTER_Y = 641;
    private static final double INFO_HALF_WIDTH = 144;
    private static final double INFO_HALF_HEIGHT = 25;

    /**
     * Met à jour l'état du joueur. Actuellement, aucune mise à jour n'est nécessaire.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    public void update(double deltaTime) {
        // Pas de logique à mettre à jour pour l'instant
    }

    /**
     * Dessine l'interface du joueur, affichant sa vie et son argent.
     */
    public void draw() {
        // Dessine le cadre de l'interface
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(INFO_CENTER_X, INFO_CENTER_Y, INFO_HALF_WIDTH, INFO_HALF_HEIGHT);

        // Dessine l'argent (pièce d'or)
        drawCoin(INFO_CENTER_X - 100, INFO_CENTER_Y, 15);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(INFO_CENTER_X - 60, INFO_CENTER_Y, String.valueOf(money));

        // Dessine la vie (cœur)
        drawHeart(INFO_CENTER_X + 60, INFO_CENTER_Y, 15);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(INFO_CENTER_X + 100, INFO_CENTER_Y, String.valueOf(health));
    }

    /**
     * Dessine une pièce d'or sur l'interface.
     *
     * @param x      Coordonnée X de la pièce.
     * @param y      Coordonnée Y de la pièce.
     * @param radius Rayon de la pièce.
     */
    private void drawCoin(double x, double y, double radius) {
        StdDraw.setPenColor(new Color(212, 175, 55)); // Couleur dorée
        StdDraw.filledCircle(x, y, radius);
        StdDraw.setPenColor(new Color(192, 192, 192)); // Couleur argentée pour le centre
        StdDraw.filledCircle(x, y, radius * 0.7);
    }

    /**
     * Dessine un cœur sur l'interface pour représenter la vie.
     *
     * @param x    Coordonnée X du cœur.
     * @param y    Coordonnée Y du cœur.
     * @param size Taille du cœur.
     */
    private void drawHeart(double x, double y, double size) {
        StdDraw.setPenColor(new Color(223, 75, 95)); // Couleur rouge pour le cœur
        double[] xCoords = {
                x, x - size, x - size, x - 0.66 * size,
                x - 0.33 * size, x, x + 0.33 * size,
                x + 0.66 * size, x + size, x + size
        };
        double[] yCoords = {
                y - size, y, y + 0.5 * size, y + size,
                y + size, y + 0.5 * size, y + size,
                y + size, y + 0.5 * size, y
        };
        StdDraw.filledPolygon(xCoords, yCoords);
    }

    /**
     * Inflige des dégâts au joueur en réduisant ses points de vie.
     *
     * @param damage Quantité de dégâts à infliger.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0; // Les points de vie ne peuvent pas être négatifs
    }

    /**
     * Vérifie si le joueur peut se permettre une dépense donnée.
     *
     * @param cost Le montant à vérifier.
     * @return {@code true} si le joueur a suffisamment d'argent, sinon {@code false}.
     */
    public boolean canAfford(int cost) {
        return money >= cost;
    }

    /**
     * Déduit un montant d'argent du joueur, si possible.
     *
     * @param amount Montant à déduire.
     */
    public void spendMoney(int amount) {
        if (canAfford(amount)) {
            money -= amount;
        }
    }

    /**
     * Ajoute une récompense en argent au joueur.
     *
     * @param reward Montant à ajouter.
     */
    public void addMoney(int reward) {
        money += reward;
    }

    /**
     * Retourne les points de vie actuels du joueur.
     *
     * @return Points de vie actuels.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Retourne l'argent actuel du joueur.
     *
     * @return Montant d'argent actuel.
     */
    public int getMoney() {
        return this.money;
    }
}
