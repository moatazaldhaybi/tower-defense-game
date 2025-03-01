package game;

/**
 * Classe représentant un point dans un espace bidimensionnel.
 * Fournit des méthodes pour manipuler les coordonnées et calculer des distances.
 */
public class Point2D {
    private double x; // Coordonnée X du point
    private double y; // Coordonnée Y du point

    /**
     * Constructeur pour créer un point avec des coordonnées spécifiques.
     *
     * @param x Coordonnée X du point.
     * @param y Coordonnée Y du point.
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur pour créer un point en copiant les coordonnées d'un autre point.
     *
     * @param point2D Le point à copier.
     */
    public Point2D(Point2D point2D) {
        this.x = point2D.x;
        this.y = point2D.y;
    }

    /**
     * Retourne la coordonnée X du point.
     *
     * @return La coordonnée X.
     */
    public double getX() {
        return x;
    }

    /**
     * Retourne la coordonnée Y du point.
     *
     * @return La coordonnée Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Définit une nouvelle valeur pour la coordonnée X.
     *
     * @param x La nouvelle valeur de la coordonnée X.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Définit une nouvelle valeur pour la coordonnée Y.
     *
     * @param y La nouvelle valeur de la coordonnée Y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Calcule la distance entre ce point et un autre point.
     *
     * @param other Le point vers lequel calculer la distance.
     * @return La distance entre les deux points.
     */
    public double distance(Point2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
