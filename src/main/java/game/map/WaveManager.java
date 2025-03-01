package game.map;

import java.util.List;

import entity.Player;
import entity.enemy.Enemy;
import java.util.List;
import java.util.ArrayList;

public class WaveManager {
	private List<Wave> waves;
	private int currentWaveIndex;
	private List<Enemy> activeEnemies;
	private boolean isWaveComplete;
	
	public WaveManager() {
		this.waves = new ArrayList<>();
		this.currentWaveIndex = 0;
		this.activeEnemies = new ArrayList<>();
		this.isWaveComplete = false;
	}
	
	public void setWaves(List<Wave> waves, Map gameMap) {
		this.waves = waves;
		// Configure la map pour chaque vague
		for (Wave wave : waves) {
			wave.gameMap = gameMap;
		}
	}
	
	public void update(double deltaTime) {
		if (currentWaveIndex >= waves.size()) {
			isWaveComplete = true;
			return;
		}
		
		Wave currentWave = waves.get(currentWaveIndex);
		Enemy newEnemy = currentWave.update(deltaTime);
		
		if (newEnemy != null) {
			newEnemy.setGameMap(currentWave.gameMap);
			System.out.println("New enemy created"); // Debug
			activeEnemies.add(newEnemy);
		}
		
		// Mettre à jour les ennemis actifs et supprimer les morts
		activeEnemies.removeIf(enemy -> enemy.getHealth() <= 0);
		activeEnemies.forEach(enemy -> enemy.update(deltaTime));
		
		// Passer à la vague suivante si la vague actuelle est terminée
		if (currentWave.isComplete() && activeEnemies.isEmpty()) {
			currentWaveIndex++;
		}
		//System.out.println("Active enemies: " + activeEnemies.size());
	}
	
	public List<Enemy> getActiveEnemies() {
		return activeEnemies;
	}
	
	public boolean isComplete() {
		return isWaveComplete && activeEnemies.isEmpty();
	}
	
	public void draw() {
		// Dessiner les ennemis actifs
		for (Enemy enemy : activeEnemies) {
			enemy.draw();
		}
	}
	
	public void removeEnemy(Enemy enemy, Player player) {
		activeEnemies.remove(enemy);
		if (enemy.getHealth() <= 0) {
			player.addMoney(enemy.getReward());
		}
	}
}