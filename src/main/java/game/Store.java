package game;

import entity.tower.Tower;
import game.map.Map;
import graphic.StdDraw;

import entity.tower.*;

import java.awt.Color;

/**
 * Classe représentant le magasin dans le jeu.
 * Permet au joueur de visualiser et d'acheter des tours.
 */
public class Store {
    private static final double STORE_CENTER_X = 856;   // Centre X du magasin
    private static final double STORE_CENTER_Y = 303;   // Centre Y du magasin
    private static final double STORE_HALF_WIDTH = 144; // Largeur/2 du magasin
    private static final double STORE_HALF_HEIGHT = 303; // Hauteur/2 du magasin

    // Position initiale et espacement pour afficher les tours
    private static final double FIRST_TOWER_Y = 550;
    private static final double TOWER_SPACING = 100;
    private static final double ICON_SIZE = 20;

    // Liste des tours disponibles dans le magasin
    private final TowerInfo[] availableTowers = {
        new TowerInfo("Archer", 30, 5, 1, 2, Element.NONE, 20),
        new TowerInfo("Wind Caster", 30, 5, 1.5, 6, Element.AIR, 50),
        new TowerInfo("Water Caster", 30, 3, 1, 4, Element.WATER, 50),
        new TowerInfo("Earth Caster", 50, 7, 0.5, 2.5, Element.EARTH, 100),
        new TowerInfo("Fire Caster", 30, 10, 0.5, 2.5, Element.FIRE, 100)
    };
    private Map gameMap;

    /**
     * Met à jour la carte liée au magasin.
     *
     * @param newMap La nouvelle carte du jeu.
     */
    public void updateMap(Map newMap) {
        this.gameMap = newMap;
    }

    /**
     * Classe interne pour représenter les informations d'une tour.
     */
    public static class TowerInfo {
        public String name;      // Nom de la tour
        public int pv;           // Points de vie
        public double atk;       // Puissance d'attaque
        public double spd;       // Vitesse d'attaque
        public double range;     // Portée
        public Element element;  // Élément de la tour
        public int cost;         // Coût de la tour

        /**
         * Constructeur pour initialiser une tour.
         *
         * @param name   Nom de la tour.
         * @param pv     Points de vie de la tour.
         * @param atk    Puissance d'attaque.
         * @param spd    Vitesse d'attaque.
         * @param range  Portée de la tour.
         * @param element Élément associé à la tour.
         * @param cost   Coût de la tour.
         */
        public TowerInfo(String name, int pv, double atk, double spd, double range, Element element, int cost) {
            this.name = name;
            this.pv = pv;
            this.atk = atk;
            this.spd = spd;
            this.range = range;
            this.element = element;
            this.cost = cost;
        }

        /**
         * Retourne l'élément associé à la tour.
         *
         * @return L'élément de la tour.
         */
        public Element getElement() {
            return this.element;
        }
    }

    /**
     * Met à jour le magasin (actuellement sans logique).
     *
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    public void update(double deltaTime) {
        // Pas de mise à jour nécessaire pour le moment
    }

    /**
     * Dessine le magasin et les tours disponibles.
     */
    public void draw() {
        // Dessine le cadre du magasin
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(STORE_CENTER_X, STORE_CENTER_Y, STORE_HALF_WIDTH, STORE_HALF_HEIGHT);

        // Dessine chaque tour
        for (int i = 0; i < availableTowers.length; i++) {
            drawTowerOption(STORE_CENTER_X - 100, FIRST_TOWER_Y - (i * TOWER_SPACING), availableTowers[i]);
        }
    }

    /**
     * Dessine une option de tour dans le magasin.
     *
     * @param x    Position X de la tour.
     * @param y    Position Y de la tour.
     * @param tower Les informations de la tour.
     */
    private void drawTowerOption(double x, double y, TowerInfo tower) {
        // Dessine l'icône de la tour
        StdDraw.setPenColor(tower.element.getColor());
        StdDraw.filledSquare(x, y, ICON_SIZE);

        // Affiche les détails de la tour
        StdDraw.setPenColor(Color.BLACK);
        double textX = x + 120;
        double spacing = 40;
        StdDraw.text(textX, y + spacing * 2, tower.name);
        StdDraw.text(textX, y + spacing, String.format("PV: %d | ATK: %.1f", tower.pv, tower.atk));
        StdDraw.text(textX, y, String.format("SPD: %.1f | PO: %.1f", tower.spd, tower.range));
        StdDraw.text(textX, y - spacing, String.format("Cost: %d", tower.cost));
    }

    /**
     * Crée une tour à partir de son nom et d'une position donnée.
     *
     * @param towerName Nom de la tour.
     * @param position  Position où placer la tour.
     * @param gameMap   Carte du jeu où placer la tour.
     * @return La tour créée.
     */
    public Tower createTower(String towerName, Point2D position, Map gameMap) {
        Tower tower = switch (towerName) {
            case "Archer" -> new ArcherTower(position);
            case "Wind Caster" -> new WindCasterTower(position);
            case "Water Caster" -> new WaterCasterTower(position);
            case "Earth Caster" -> new EarthCasterTower(position);
            case "Fire Caster" -> new FireCasterTower(position);
            default -> throw new IllegalArgumentException("Unknown tower type: " + towerName);
        };

        tower.setGameMap(gameMap);

        return tower;
    }

    /**
     * Retourne les informations d'une tour à un index donné.
     *
     * @param index L'index de la tour dans la liste des tours disponibles.
     * @return Les informations de la tour, ou {@code null} si l'index est invalide.
     */
    public TowerInfo getTowerInfo(int index) {
        if (index >= 0 && index < availableTowers.length) {
            return availableTowers[index];
        }
        return null;
    }

    /**
     * Vérifie si une position est dans la zone d'une tour dans le magasin.
     *
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     * @return L'index de la tour sélectionnée, ou -1 si aucune tour n'est sélectionnée.
     */
    public int getTowerIndexAtPosition(double mouseX, double mouseY) {
        if (mouseX < STORE_CENTER_X - STORE_HALF_WIDTH ||
            mouseX > STORE_CENTER_X + STORE_HALF_WIDTH ||
            mouseY < STORE_CENTER_Y - STORE_HALF_HEIGHT ||
            mouseY > STORE_CENTER_Y + STORE_HALF_HEIGHT) {
            return -1;
        }

        for (int i = 0; i < availableTowers.length; i++) {
            double towerY = FIRST_TOWER_Y - (i * TOWER_SPACING);
            if (mouseY >= towerY - TOWER_SPACING / 2 &&
                mouseY <= towerY + TOWER_SPACING / 2) {
                return i;
            }
        }
        return -1;
    }

		private TowerInfo selectedTower;

    /**
     * Retourne la tour actuellement sélectionnée.
     *
     * @return Les informations de la tour sélectionnée.
     */
    public TowerInfo getSelectedTower() {
        return selectedTower;
    }

    /**
     * Définit la tour sélectionnée dans le magasin.
     *
     * @param index L'index de la tour à sélectionner.
     */
    public void setSelectedTower(int index) {
        if (index >= 0 && index < availableTowers.length) {
            selectedTower = availableTowers[index];
        } else {
            clearSelection();
        }
    }

    /**
     * Efface la sélection de tour actuelle.
     */
    public void clearSelection() {
        selectedTower = null;
    }
}
