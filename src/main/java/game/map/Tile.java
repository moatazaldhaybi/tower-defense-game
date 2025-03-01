package game.map;

import entity.tower.Tower;
import game.Point2D;
import graphic.StdDraw;

import java.awt.Color;

public class Tile {
	public enum Type {
		SPAWN('S', Color.RED),
		BASE('B', Color.ORANGE),
		ROAD('R', new Color(194, 178, 128)),
		CONSTRUCTIBLE('C', Color.LIGHT_GRAY),
		DECORATION('X', new Color(11, 102, 35));
		
		private final char symbol;
		private final Color color;
		
		Type(char symbol, Color color) {
			this.symbol = symbol;
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
		
		public static Type fromSymbol(char symbol) {
			for (Type type : values()) {
				if (type.symbol == symbol) return type;
			}
			throw new IllegalArgumentException("Unknown tile type: " + symbol);
		}
	}
	
	private Type type;
	private Point2D position;
	private Tower tower;
	
	public Tile(Type type, Point2D position) {
		this.type = type;
		this.position = position;
		this.tower = null;
	}
	
	// Getters
	public Type getType() {
		return type;
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public Tower getTower() {
		return tower;
	}
	
	// Setters
	public void setTower(Tower tower) {
		this.tower = tower;
	}
	
	public boolean isConstructible() {
		return type == Type.CONSTRUCTIBLE;
	}
	
	public boolean hasTower() {
		return tower != null;
	}
	
	public void draw(double squareSize) {
		// Dessin du carré de la case
		StdDraw.setPenColor(type.getColor());
		StdDraw.filledSquare(
				position.getX() + squareSize/2,
				position.getY() + squareSize/2,
				squareSize/2
		);
		
		// Si une tour est présente sur la case, la dessiner
		if (tower != null) {
			// On utilise déjà la position stockée dans la tour car elle a été correctement
			// initialisée au centre de la case lors de la création
			tower.draw();
		}
	}
}