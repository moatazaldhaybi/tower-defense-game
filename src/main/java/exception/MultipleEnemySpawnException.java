package exception;

/**
 * Exception levée lorsqu'une carte contient plusieurs points de spawn pour les ennemis.
 * Cette exception étend {@link GameException} et fournit des informations détaillées
 * sur les fichiers concernés par l'erreur.
 */
public class MultipleEnemySpawnException extends GameException {
    private final String levelFile; // Le fichier de niveau associé à l'erreur
    private final String mapFile;   // Le fichier de carte contenant plusieurs points de spawn

    /**
     * Constructeur pour créer une exception indiquant plusieurs points de spawn ennemis.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant plusieurs points de spawn.
     */
    public MultipleEnemySpawnException(String levelFile, String mapFile) {
        super(buildErrorMessage(levelFile, mapFile));
        this.levelFile = levelFile;
        this.mapFile = mapFile;
    }

    /**
     * Construit un message d'erreur détaillé avec les informations fournies.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant plusieurs points de spawn.
     * @return Un message formaté décrivant l'erreur.
     */
    private static String buildErrorMessage(String levelFile, String mapFile) {
        return String.format("""
            Map has multiple enemy spawn points:
            Level file: %s
            Map file: %s
            Only one Spawn point (S) is allowed""",
                levelFile, mapFile);
    }
}
