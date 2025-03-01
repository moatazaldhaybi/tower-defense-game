package game.map;

import entity.enemy.Enemy;
import entity.tower.Tower;
import exception.*;
import game.Point2D;
import game.Store;
import graphic.StdDraw;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Map {
	private Tile[][] tiles;
	private Point2D spawnPoint;
	private Point2D basePoint;
	private List<Point2D> path;
	private List<Enemy> enemies;
	private List<Tower> towers;
	private final String levelFile;
	private final String mapFile;
	
	public Map(String levelFile, String mapFile) {
		this.levelFile = levelFile;
		this.mapFile = mapFile;
		this.enemies = new ArrayList<>();
		this.towers = new ArrayList<>();
	}
	
	public void loadMap() {
		List<String> lines = loadMapFile(mapFile);
		validateMapDimensions(lines);
		
		int height = lines.size();
		int width = lines.get(0).length();
		tiles = new Tile[height][width];
		
		int spawnCount = 0;
		int baseCount = 0;
		
		// Crée les cases et cherche le spawn et la base
		for (int y = 0; y < height; y++) {
			String line = lines.get(y);
			for (int x = 0; x < width; x++) {
				char symbol = line.charAt(x);
				validateTile(symbol, x, y);
				
				tiles[y][x] = new Tile(Tile.Type.fromSymbol(symbol),
						new Point2D(x, y));
				
				if (symbol == 'S') {
					spawnPoint = new Point2D(x + 0.5, y + 0.5);
					spawnCount++;
				}
				else if (symbol == 'B') {
					basePoint = new Point2D(x + 0.5, y + 0.5);
					baseCount++;
				}
			}
		}
		
		validateSpawnAndBase(spawnCount, baseCount);
		calculatePath();
	}
	
	private void validateMapDimensions(List<String> lines) {
		if (lines.isEmpty()) {
			throw new MapException(levelFile, mapFile, "Empty map file");
		}
		
		int width = lines.get(0).length();
		for (int i = 1; i < lines.size(); i++) {
			if (lines.get(i).length() != width) {
				throw new MapException(levelFile, mapFile,
						"Inconsistent map width at line " + (i + 1));
			}
		}
	}
	
	private void validateTile(char symbol, int x, int y) {
		if (!"SBRCX".contains(String.valueOf(symbol))) {
			throw new MapException(levelFile, mapFile,
					"Unknown tile type '" + symbol + "' at position (" + x + "," + y + ")");
		}
	}
	
	private void validateSpawnAndBase(int spawnCount, int baseCount) {
		if (spawnCount == 0) {
			throw new NoEnemySpawnException(levelFile, mapFile);
		}
		if (spawnCount > 1) {
			throw new MultipleEnemySpawnException(levelFile, mapFile);
		}
		if (baseCount == 0) {
			throw new NoPlayerBaseException(levelFile, mapFile);
		}
		if (baseCount > 1) {
			throw new MultiplePlayerBaseException(levelFile, mapFile);
		}
	}
	
	private void calculatePath() {
		path = new ArrayList<>();
		if (spawnPoint == null || basePoint == null) {
			throw new InvalidMapPathException(levelFile, mapFile, "Missing spawn or base point");
		}
		
		List<List<Point2D>> allPaths = findAllPaths();
		
		if (allPaths.isEmpty()) {
			throw new InvalidMapPathException(levelFile, mapFile, "No path found from spawn to base");
		}
		if (allPaths.size() > 1) {
			throw new InvalidMapPathException(levelFile, mapFile, "Multiple paths found from spawn to base");
		}
		
		path = allPaths.get(0);
	}
	
	private List<List<Point2D>> findAllPaths() {
		List<List<Point2D>> allPaths = new ArrayList<>();
		Set<String> visited = new HashSet<>();
		List<Point2D> currentPath = new ArrayList<>();
		
		findPathsDFS(
				(int)spawnPoint.getX(),
				(int)spawnPoint.getY(),
				visited,
				currentPath,
				allPaths
		);
		
		return allPaths;
	}
	
	private void findPathsDFS(int x, int y, Set<String> visited,
							  List<Point2D> currentPath,
							  List<List<Point2D>> allPaths) {
		String pos = x + "," + y;
		if (visited.contains(pos)) {
			// Au lieu de lancer une exception, on retourne simplement
			// car ce n'est pas forcément une boucle, juste un chemin déjà exploré
			return;
		}
		
		visited.add(pos);
		currentPath.add(new Point2D(x + 0.5, y + 0.5));
		
		if (tiles[y][x].getType() == Tile.Type.BASE) {
			allPaths.add(new ArrayList<>(currentPath));
		} else {
			int[][] directions = {{0,-1}, {1,0}, {0,1}, {-1,0}};
			for (int[] dir : directions) {
				int nextX = x + dir[0];
				int nextY = y + dir[1];
				
				if (isValidPosition(nextX, nextY) &&
						(tiles[nextY][nextX].getType() == Tile.Type.ROAD ||
								tiles[nextY][nextX].getType() == Tile.Type.BASE)) {
					findPathsDFS(nextX, nextY, new HashSet<>(visited),
							new ArrayList<>(currentPath), allPaths);
				}
			}
		}
	}
	
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}
	
	public void removeEnemy(Enemy enemy) {
		enemies.remove(enemy);
	}
	
	public void removeTower(Tower tower) {
		towers.remove(tower);
	}
	
	public List<Enemy> getEnemies() {
		return new ArrayList<>(enemies);
	}
	
	public List<Tower> getTowers() {
		return new ArrayList<>(towers);
	}
	
	public Point2D getSpawnPoint() {
		//System.out.println("Spawn point: " + spawnPoint); // Debug
		return spawnPoint;
	}
	
	public List<Point2D> getPath() {
		return new ArrayList<>(path);
	}
	
	private boolean isValidPosition(int x, int y) {
		return x >= 0 && x < tiles[0].length && y >= 0 && y < tiles.length;
	}
	
	private List<String> loadMapFile(String filename) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new FileReader("resources/maps/" + filename + ".mtp"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			throw new GameException("Failed to load map file: " + filename);
		}
		return lines;
	}
	
	public Tile getTileAt(int x, int y) {
		if (isValidPosition(x, y)) {
			return tiles[y][x];
		}
		return null;
	}
	
	public void update(double deltaTime) {
		// Mise à jour des tours et ennemis
		for (Tower tower : towers) {
			tower.update(deltaTime);
		}
		
		// Copie la liste pour éviter les ConcurrentModificationException
		List<Enemy> currentEnemies = new ArrayList<>(enemies);
		for (Enemy enemy : currentEnemies) {
			enemy.update(deltaTime);
		}
	}
	
	public void draw() {
		if (tiles == null) return;
		
		// Calcul de la taille d'une case en fonction des dimensions de la carte
		double mapWidth = tiles[0].length;
		double mapHeight = tiles.length;
		
		// Définir la zone de jeu (zone violette dans le sujet)
		double gameAreaWidth = 700;  // comme spécifié dans le sujet
		double gameAreaHeight = 700;
		double gameAreaX = 350;  // centre de la zone de jeu
		double gameAreaY = 350;
		
		// Calcul de la taille d'une case pour qu'elle s'adapte à la zone de jeu
		double squareSize = Math.min(gameAreaWidth / mapWidth, gameAreaHeight / mapHeight);
		
		// Calcul de l'offset pour centrer la carte dans la zone de jeu
		double offsetX = gameAreaX - (mapWidth * squareSize) / 2;
		double offsetY = gameAreaY - (mapHeight * squareSize) / 2;
		
		// Dessin des cases
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				// Calcul de la position réelle de la case
				double tileX = offsetX + x * squareSize;
				double tileY = offsetY + y * squareSize;
				
				// Mise à jour de la position de la tile
				tiles[y][x].getPosition().setX(tileX);
				tiles[y][x].getPosition().setY(tileY);
				
				// Dessin de la tile
				tiles[y][x].draw(squareSize);
				
				// Dessiner les lignes de la grille
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.line(tileX, tileY, tileX + squareSize, tileY);
				StdDraw.line(tileX, tileY, tileX, tileY + squareSize);
			}
		}
		
		// Dessiner les ennemis et tours
		for (Enemy enemy : enemies) {
			enemy.draw();
		}
		
		for (Tower tower : towers) {
			tower.draw();
		}
	}
	
	public Point2D getScreenPosition(Point2D gridPosition) {
		double mapWidth = tiles[0].length;
		double mapHeight = tiles.length;
		double gameAreaWidth = 700;
		double gameAreaHeight = 700;
		double squareSize = Math.min(gameAreaWidth / mapWidth, gameAreaHeight / mapHeight);
		double offsetX = 350 - (mapWidth * squareSize) / 2;
		double offsetY = 350 - (mapHeight * squareSize) / 2;
		
		return new Point2D(
				offsetX + gridPosition.getX() * squareSize,
				offsetY + gridPosition.getY() * squareSize
		);
	}
	
	public Tile getTileAtScreenPosition(double screenX, double screenY) {
		double mapWidth = tiles[0].length;
		double mapHeight = tiles.length;
		double gameAreaWidth = 700;
		double gameAreaHeight = 700;
		double squareSize = Math.min(gameAreaWidth / mapWidth, gameAreaHeight / mapHeight);
		double offsetX = 350 - (mapWidth * squareSize) / 2;
		double offsetY = 350 - (mapHeight * squareSize) / 2;
		
		// Convertir les coordonnées écran en coordonnées grille
		int gridX = (int)((screenX - offsetX) / squareSize);
		int gridY = (int)((screenY - offsetY) / squareSize);
		
		return getTileAt(gridX, gridY);
	}
	
	public boolean canPlaceTower(Tile tile) {
		if (tile == null) return false;
		
		// Vérifie si la case est constructible et libre
		return tile.getType() == Tile.Type.CONSTRUCTIBLE &&
				towers.stream().noneMatch(t ->
						t.getPosition().getX() == tile.getPosition().getX() &&
								t.getPosition().getY() == tile.getPosition().getY());
	}
	
	public Point2D getGridPosition(double screenX, double screenY) {
		double mapWidth = tiles[0].length;
		double mapHeight = tiles.length;
		double gameAreaWidth = 700;
		double gameAreaHeight = 700;
		double squareSize = Math.min(gameAreaWidth / mapWidth, gameAreaHeight / mapHeight);
		double offsetX = 350 - (mapWidth * squareSize) / 2;
		double offsetY = 350 - (mapHeight * squareSize) / 2;
		
		return new Point2D(
				(screenX - offsetX) / squareSize,
				(screenY - offsetY) / squareSize
		);
	}
	
	public int getWidth() {
		return tiles[0].length;
	}
	
	public int getHeight() {
		return tiles.length;
	}
	
	public void addTower(Tower tower) {
		towers.add(tower);
		// Trouver et mettre à jour la case correspondante
		Tile tile = getTileAtScreenPosition(tower.getPosition().getX(), tower.getPosition().getY());
		if (tile != null) {
			tile.setTower(tower);
		}
	}
	
	public void drawPlacementPreview(Tile hoveredTile, Store.TowerInfo selectedTower) {
		if (hoveredTile != null && selectedTower != null) {
			double tileSize = 700.0 / Math.max(getWidth(), getHeight());
			Point2D pos = hoveredTile.getPosition();
			
			if (hoveredTile.isConstructible() && !hoveredTile.hasTower()) {
				// Afficher un aperçu vert
				StdDraw.setPenColor(new Color(0, 255, 0, 128));
			} else {
				// Afficher en rouge si placement impossible
				StdDraw.setPenColor(new Color(255, 0, 0, 128));
			}
			
			StdDraw.square(
					pos.getX() + tileSize/2,
					pos.getY() + tileSize/2,
					tileSize/2
			);
		}
	}
}