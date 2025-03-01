package game.map;

import entity.enemy.*;
import exception.GameException;
import exception.UnknownEnemyException;
import game.Point2D;
import game.SpawnInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Wave {
	private List<SpawnInfo> spawnSchedule;
	private double elapsedTime;
	private int currentEnemyIndex;
	private final String levelFile;
	private final String waveFile;
	public Map gameMap;  // Référence à la carte pour obtenir le point de spawn
	
	public Wave(String levelFile, String waveFile) {
		this.levelFile = levelFile;
		this.waveFile = waveFile;
		this.spawnSchedule = new ArrayList<>();
		this.elapsedTime = 0;
		this.currentEnemyIndex = 0;
		loadWaveFromFile(waveFile);
	}
	
	private void loadWaveFromFile(String filename) {
		try (BufferedReader reader = new BufferedReader(
				new FileReader("resources/waves/" + filename + ".wve"))) {
			String line;
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				String[] parts = line.split("\\|");
				if (parts.length == 2) {
					try {
						double spawnTime = Double.parseDouble(parts[0].trim());
						EnemyType enemyType = EnemyType.fromString(
								parts[1].trim(),
								levelFile,
								waveFile,
								lineNumber,
								line
						);
						spawnSchedule.add(new SpawnInfo(
								spawnTime,
								enemyType,
								lineNumber,
								line
						));
					} catch (NumberFormatException e) {
						throw new GameException("Invalid spawn time format: " + parts[0]);
					}
				}
			}
		} catch (IOException e) {
			throw new GameException("Failed to load wave file: " + filename);
		}
	}
	
	public Enemy update(double deltaTime) {
		elapsedTime += deltaTime;
		
		if (currentEnemyIndex < spawnSchedule.size()) {
			SpawnInfo nextSpawn = spawnSchedule.get(currentEnemyIndex);
			
			if (elapsedTime >= nextSpawn.getTime()) {
				currentEnemyIndex++;
				return createEnemy(
						nextSpawn.getType(),
						nextSpawn.getLineNumber(),
						nextSpawn.getLineContent()
				);
			}
		}
		
		return null;
	}
	
	private Enemy createEnemy(EnemyType type, int lineNumber, String lineContent) {
		Point2D spawnPoint = gameMap.getSpawnPoint();
		List<Point2D> path = gameMap.getPath();
		
		try {
			Enemy enemy = switch (type) {
				case MINION -> new MinionEnemy(spawnPoint, path);
				case WIND_GROGNARD -> new WindGrognardEnemy(spawnPoint, path);
				case FIRE_GROGNARD -> new FireGrognardEnemy(spawnPoint, path);
				case WATER_BRUTE -> new WaterBruteEnemy(spawnPoint, path);
				case EARTH_BRUTE -> new EarthBruteEnemy(spawnPoint, path);
				case BOSS -> new BossEnemy(spawnPoint, path);
				default -> null;
			};
			if(enemy == null) {
				throw new UnknownEnemyException(
						"Unknown enemy type: " + type,
						levelFile,
						waveFile,
						lineNumber,
						lineContent
				);
			}
			
			enemy.setGameMap(gameMap);
			System.out.println("Created enemy at " + spawnPoint + " with path size: " + path.size());
			return enemy;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create enemy of type: " + type, e);
		}
	}
	
	public boolean isComplete() {
		return currentEnemyIndex >= spawnSchedule.size();
	}
}