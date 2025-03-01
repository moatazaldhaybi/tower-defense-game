package game.map;

import exception.GameException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {
	private String name;
	private String mapName;
	private List<Wave> waves;
	
	public Level(String name) {
		this.name = name;
		this.waves = new ArrayList<>();
		loadLevelFile(name);
	}
	
	private void loadLevelFile(String levelName) {
		try (BufferedReader reader = new BufferedReader(
				new FileReader("resources/levels/" + levelName + ".lvl"))) {
			// Première ligne = nom de la map
			this.mapName = reader.readLine();
			
			// Les lignes suivantes sont des waves
			String line;
			while ((line = reader.readLine()) != null) {
				waves.add(new Wave(name, line));
			}
		} catch (IOException e) {
			throw new GameException("Failed to load level file: " + levelName);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public List<Wave> getWaves() {
		return waves;
	}
}