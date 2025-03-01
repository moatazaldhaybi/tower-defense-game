package exception;

/**
 * Exception levée lorsqu'une carte ne contient aucun point de spawn pour les ennemis.
 * Cette exception étend {@link GameException} et fournit des informations détaillées
 * sur les fichiers concernés par l'erreur.
 */
public class NoEnemySpawnException extends GameException {
    private final String levelFile; // Le fichier de niveau associé à l'erreur
    private final String mapFile;   // Le fichier de carte où l'erreur a été détectée

    /**
     * Constructeur pour créer une exception indiquant l'absence de point de spawn pour les ennemis.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte concerné.
     */
    public NoEnemySpawnException(String levelFile, String mapFile) {
        super(buildErrorMessage(levelFile, mapFile));
        this.levelFile = levelFile;
        this.mapFile = mapFile;
    }

    /**
     * Construit un message d'erreur détaillé avec les informations fournies.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte concerné.
     * @return Un message formaté décrivant l'erreur.
     */
    private static String buildErrorMessage(String levelFile, String mapFile) {
        return String.format("""
            Map has no enemy spawn point:
            Level file: %s
            Map file: %s
            Missing the Spawn point (S)""",
                levelFile, mapFile);
    }
}
