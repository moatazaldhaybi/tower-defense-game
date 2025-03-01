package entity.enemy;

import exception.UnknownEnemyException;

/**
 * Enumération représentant les différents types d'ennemis dans le jeu.
 * Chaque type est associé à un nom et une classe spécifique d'ennemi.
 */
public enum EnemyType {
    MINION("Minion", MinionEnemy.class),
    WIND_GROGNARD("Wind Grognard", WindGrognardEnemy.class),
    FIRE_GROGNARD("Fire Grognard", FireGrognardEnemy.class),
    WATER_BRUTE("Water Brute", WaterBruteEnemy.class),
    EARTH_BRUTE("Earth Brute", EarthBruteEnemy.class),
    BOSS("Boss", BossEnemy.class);

    private final String name; // Nom du type d'ennemi
    private final Class<? extends Enemy> enemyClass; // Classe associée à l'ennemi

    /**
     * Constructeur pour initialiser un type d'ennemi.
     *
     * @param name       Nom du type d'ennemi.
     * @param enemyClass Classe spécifique de l'ennemi correspondant.
     */
    EnemyType(String name, Class<? extends Enemy> enemyClass) {
        this.name = name;
        this.enemyClass = enemyClass;
    }

    /**
     * Méthode statique pour obtenir un type d'ennemi à partir de son nom.
     *
     * @param name        Le nom du type d'ennemi (par exemple, "Minion").
     * @param levelFile   Le fichier du niveau actuel (pour gérer les erreurs).
     * @param waveFile    Le fichier de la vague actuelle (pour gérer les erreurs).
     * @param lineNumber  Le numéro de ligne dans le fichier de vague.
     * @param lineContent Le contenu de la ligne dans le fichier de vague.
     * @return L'instance de l'énumération correspondant au nom fourni.
     * @throws UnknownEnemyException Si le type d'ennemi n'est pas reconnu.
     */
    public static EnemyType fromString(String name, String levelFile, String waveFile, int lineNumber, String lineContent) {
        for (EnemyType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new UnknownEnemyException(
                "Unknown enemy type: " + name,
                levelFile,
                waveFile,
                lineNumber,
                lineContent
        );
    }
}
