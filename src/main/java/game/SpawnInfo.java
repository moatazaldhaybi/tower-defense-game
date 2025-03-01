package game;

import entity.enemy.EnemyType;

/**
 * Classe représentant les informations liées au spawn d'un ennemi.
 * Inclut le type d'ennemi, le moment où il doit apparaître, ainsi que des détails
 * sur l'origine de l'information (ligne et contenu du fichier source).
 */
public class SpawnInfo {
    private final double time;         // Temps d'apparition de l'ennemi (en secondes)
    private final EnemyType type;      // Type de l'ennemi à spawner
    private final int lineNumber;      // Numéro de la ligne dans le fichier source
    private final String lineContent;  // Contenu de la ligne dans le fichier source

    /**
     * Constructeur pour initialiser les informations de spawn d'un ennemi.
     *
     * @param time        Le moment où l'ennemi doit apparaître (en secondes).
     * @param type        Le type de l'ennemi.
     * @param lineNumber  Le numéro de la ligne dans le fichier source.
     * @param lineContent Le contenu de la ligne dans le fichier source.
     */
    public SpawnInfo(double time, EnemyType type, int lineNumber, String lineContent) {
        this.time = time;
        this.type = type;
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

    /**
     * Retourne le moment où l'ennemi doit apparaître.
     *
     * @return Le temps de spawn (en secondes).
     */
    public double getTime() {
        return time;
    }

    /**
     * Retourne le type d'ennemi à spawner.
     *
     * @return Le type d'ennemi.
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Retourne le numéro de la ligne où l'information de spawn a été trouvée.
     *
     * @return Le numéro de la ligne.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Retourne le contenu de la ligne où l'information de spawn a été trouvée.
     *
     * @return Le contenu de la ligne.
     */
    public String getLineContent() {
        return lineContent;
    }
}
