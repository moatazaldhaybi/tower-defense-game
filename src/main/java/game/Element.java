package game;

import java.awt.*;

/**
 * Enumération représentant les éléments dans le jeu.
 * Chaque élément est associé à un nom et une couleur spécifique.
 */
public enum Element {
    NONE("Neutral", new Color(0, 0, 0)),        // Élément neutre (aucun)
    FIRE("Fire", new Color(184, 22, 1)),        // Élément Feu
    EARTH("Earth", new Color(0, 167, 15)),      // Élément Terre
    AIR("Air", new Color(242, 211, 0)),         // Élément Air
    WATER("Water", new Color(6, 0, 160));       // Élément Eau

    private final String name;   // Nom de l'élément
    private final Color color;   // Couleur associée à l'élément

    /**
     * Constructeur pour initialiser un élément avec un nom et une couleur.
     *
     * @param name  Le nom de l'élément.
     * @param color La couleur associée à l'élément.
     */
    Element(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Retourne le nom de l'élément.
     *
     * @return Le nom de l'élément.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne la couleur associée à l'élément.
     *
     * @return La couleur de l'élément.
     */
    public Color getColor() {
        return color;
    }
}
