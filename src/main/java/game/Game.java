package game;

import entity.Player;
import entity.enemy.Enemy;
import entity.tower.ArcherTower;
import entity.tower.Tower;
import exception.GameException;
import game.map.Level;
import game.map.Map;
import game.map.Tile;
import game.map.WaveManager;
import graphic.StdDraw;
import game.Store.TowerInfo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale du jeu qui gère la boucle de jeu, les niveaux, la carte, les ennemis et les tours.
 */
public class Game {

    private Map map; // La carte du jeu
    private Player player; // Instance du joueur
    private Store store; // Boutique pour acheter des tours
    private WaveManager waveManager; // Gestionnaire des vagues d'ennemis
    private boolean isRunning; // Indique si le jeu est en cours d'exécution
    private List<Level> levels; // Liste des niveaux du jeu
    public int currentLevel = 0; // Indice du niveau actuel
    private Tile selectedTile; // Case sélectionnée par le joueur
    private TowerInfo selectedTowerType; // Type de tour sélectionné

    /**
     * Constructeur pour initialiser les composants du jeu.
     */
    public Game() {
        this.levels = new ArrayList<>();
        this.waveManager = new WaveManager();
    }

    /**
     * Charge le fichier de configuration des niveaux du jeu.
     * Si une erreur survient, une {@link GameException} est levée.
     */
    private void loadGameFile() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("resources/games/game.g"))) {
            String levelName;
            while ((levelName = reader.readLine()) != null) {
                levels.add(new Level(levelName));
            }
        } catch (IOException e) {
            throw new GameException("Failed to load game file");
        }
    }

    /**
     * Retourne la liste des niveaux du jeu.
     *
     * @return Liste des niveaux.
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Lance le jeu en initialisant les composants et en démarrant la boucle principale.
     */
    public void launch() {
        init();
        gameLoop();
    }

    /**
     * Initialise les composants principaux du jeu, y compris la carte, le joueur, et la boutique.
     */
    private void init() {
        StdDraw.setCanvasSize(1024, 720);
        StdDraw.setXscale(-12, 1012);
        StdDraw.setYscale(-10, 710);
        StdDraw.enableDoubleBuffering();

        isRunning = true;

        loadGameFile();
        
        map = new Map(levels.get(currentLevel).getName(), levels.get(currentLevel).getMapName());
        map.loadMap();

        waveManager.setWaves(levels.get(currentLevel).getWaves(), map);

        player = new Player();
        store = new Store();
    }

    /**
     * Boucle principale du jeu. Met à jour l'état et affiche les composants à chaque itération.
     */
    private void gameLoop() {
        long previousTime = System.currentTimeMillis();

        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - previousTime) / 1000.0;
            previousTime = currentTime;

            update(deltaTime);
            StdDraw.clear();
            draw();
            StdDraw.show();
        }
    }

    /**
     * Met à jour l'état du jeu, y compris les ennemis, le joueur, et les tours.
     *
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    private void update(double deltaTime) {
        waveManager.update(deltaTime);
        map.update(deltaTime);
        player.update(deltaTime);
        store.update(deltaTime);

        if (player.getHealth() <= 0) {
            isRunning = false;
        }

        for (Enemy enemy : new ArrayList<>(waveManager.getActiveEnemies())) {
            if (enemy.hasReachedBase()) {
                player.takeDamage(enemy.getAttackPower());
                waveManager.removeEnemy(enemy, player);
            } else if (enemy.getHealth() <= 0) {
                waveManager.removeEnemy(enemy, player);
            }
        }

        if (waveManager.isComplete()) {
            currentLevel++;
            if (currentLevel < levels.size()) {
                loadNextLevel();
            } else {
                isRunning = false;
            }
        }

        handleTowerAttacks(deltaTime);
        handleMouseInput();
    }

    /**
     * Gère les attaques des tours sur les ennemis.
     *
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    private void handleTowerAttacks(double deltaTime) {
        List<Enemy> activeEnemies = waveManager.getActiveEnemies();
        List<Tower> towers = map.getTowers();

        if (activeEnemies.isEmpty() || towers.isEmpty()) return;

        for (Tower tower : towers) {
            tower.update(deltaTime);
            tower.attack(activeEnemies);
        }

        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : activeEnemies) {
            if (enemy.getHealth() <= 0) {
                enemiesToRemove.add(enemy);
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            waveManager.removeEnemy(enemy, player);
        }
    }

    /**
     * Gère les clics dans la boutique pour sélectionner une tour.
     *
     * @param x Coordonnée X du clic de la souris.
     * @param y Coordonnée Y du clic de la souris.
     */
    private void handleStoreClick(double x, double y) {
        int towerIndex = store.getTowerIndexAtPosition(x, y);
        if (towerIndex >= 0) {
            TowerInfo selectedTowerInfo = store.getTowerInfo(towerIndex);

            if (player.canAfford(selectedTowerInfo.cost)) {
                selectedTowerType = selectedTowerInfo;
            } else {
                System.out.println("Pas assez d'argent pour sélectionner cette tour !");
            }
        }
    }

    /**
     * Gère les clics sur la carte pour placer une tour.
     *
     * @param clickedTile La case sur laquelle le clic a été effectué.
     */
    private void handleMapClick(Tile clickedTile) {
        if (clickedTile == null) return;

        if (selectedTowerType != null && clickedTile.isConstructible() && !clickedTile.hasTower()) {
            if (player.canAfford(selectedTowerType.cost)) {
                double tileSize = 700.0 / Math.max(map.getWidth(), map.getHeight());

                Point2D towerPos = new Point2D(
                        clickedTile.getPosition().getX() + tileSize / 2,
                        clickedTile.getPosition().getY() + tileSize / 2
                );

                Tower newTower = store.createTower(selectedTowerType.name, towerPos, map);
                clickedTile.setTower(newTower);
                map.addTower(newTower);

                player.spendMoney(selectedTowerType.cost);
                selectedTowerType = null;
                store.clearSelection();
            }
        }
    }

    /**
     * Gère les clics de la souris pour les interactions avec la carte ou la boutique.
     */
    private void handleMouseInput() {
        if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();

            Tile clickedTile = map.getTileAtScreenPosition(mouseX, mouseY);
            if (clickedTile != null) {
                handleMapClick(clickedTile);
            }

            if (isInStoreArea(mouseX, mouseY)) {
                handleStoreClick(mouseX, mouseY);
            }
        }
    }

    /**
     * Vérifie si une position se trouve dans la zone de la boutique.
     *
     * @param x Coordonnée X de la position.
     * @param y Coordonnée Y de la position.
     * @return {@code true} si la position est dans la boutique, sinon {@code false}.
     */
    private boolean isInStoreArea(double x, double y) {
        return x >= 712 && x <= 1000 && y >= 0 && y <= 606;
    }

    /**
     * Charge le niveau suivant et réinitialise les composants nécessaires.
     */
    private void loadNextLevel() {
        map = new Map(levels.get(currentLevel).getName(), levels.get(currentLevel).getMapName());
        map.loadMap();

        waveManager = new WaveManager();
        waveManager.setWaves(levels.get(currentLevel).getWaves(), map);

        selectedTile = null;
        selectedTowerType = null;

        store.updateMap(map);
    }

    /**
     * Dessine les composants du jeu, y compris la carte, le joueur, et la boutique.
     */
    private void draw() {
        map.draw();
        store.draw();
        player.draw();
        waveManager.draw();

        if (selectedTowerType != null) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();
            Tile hoveredTile = map.getTileAtScreenPosition(mouseX, mouseY);

            if (hoveredTile != null) {
                double tileSize = 700.0 / Math.max(map.getWidth(), map.getHeight());
                Point2D tileCenter = hoveredTile.getPosition();

                tileCenter = new Point2D(
                        tileCenter.getX() + tileSize / 2,
                        tileCenter.getY() + tileSize / 2
                );

                if (hoveredTile.isConstructible() && !hoveredTile.hasTower()) {
                    StdDraw.setPenColor(new Color(0, 255, 0, 128));
                } else {
					StdDraw.setPenColor(new Color(255, 0, 0, 128));
				}
				StdDraw.square(tileCenter.getX(), tileCenter.getY(), tileSize/2);
				
				// Afficher l'aperçu de la tour
				StdDraw.setPenColor(selectedTowerType.getElement().getColor());
				StdDraw.filledSquare(tileCenter.getX(), tileCenter.getY(), tileSize/4);
			}
		}
	}

}