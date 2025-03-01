package exception;

/**
 * Exception levée lorsqu'une carte contient plusieurs bases de joueur.
 * Cette exception étend {@link GameException} et fournit des informations détaillées
 * sur les fichiers concernés par l'erreur.
 */
public class MultiplePlayerBaseException extends GameException {
    private final String levelFile; // Le fichier de niveau associé à l'erreur
    private final String mapFile;   // Le fichier de carte contenant plusieurs bases

    /**
     * Constructeur pour créer une exception indiquant plusieurs bases de joueur.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant plusieurs bases.
     */
    public MultiplePlayerBaseException(String levelFile, String mapFile) {
        super(buildErrorMessage(levelFile, mapFile));
        this.levelFile = levelFile;
        this.mapFile = mapFile;
    }

    /**
     * Construit un message d'erreur détaillé avec les informations fournies.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant plusieurs bases.
     * @return Un message formaté décrivant l'erreur.
     */
    private static String buildErrorMessage(String levelFile, String mapFile) {
        return String.format("""
            Map has multiple player bases:
            Level file: %s
            Map file: %s
            Only one Base point (B) is allowed""",
                levelFile, mapFile);
    }
}
