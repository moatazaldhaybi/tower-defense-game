package exception;

/**
 * Exception levée lorsqu'un ennemi inconnu est rencontré dans un fichier de vague.
 * Cette exception étend {@link GameException} et fournit des informations détaillées
 * sur le fichier et la ligne où l'erreur a été détectée.
 */
public class UnknownEnemyException extends GameException {
    private final String levelFile;  // Le fichier de niveau associé à l'erreur
    private final String waveFile;   // Le fichier de vague contenant l'ennemi inconnu
    private final int lineNumber;    // Le numéro de la ligne où l'erreur a été détectée
    private final String lineContent; // Le contenu de la ligne où l'erreur a été détectée

    /**
     * Constructeur pour créer une exception indiquant un ennemi inconnu.
     *
     * @param message     Le message décrivant l'erreur.
     * @param levelFile   Le fichier de niveau associé à l'erreur.
     * @param waveFile    Le fichier de vague où l'erreur a été détectée.
     * @param lineNumber  Le numéro de la ligne contenant l'erreur.
     * @param lineContent Le contenu de la ligne contenant l'erreur.
     */
    public UnknownEnemyException(String message, String levelFile, String waveFile,
                                 int lineNumber, String lineContent) {
        super(buildErrorMessage(message, levelFile, waveFile, lineNumber, lineContent));
        this.levelFile = levelFile;
        this.waveFile = waveFile;
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

    /**
     * Construit un message d'erreur détaillé avec les informations fournies.
     *
     * @param message     Le message décrivant l'erreur.
     * @param levelFile   Le fichier de niveau associé à l'erreur.
     * @param waveFile    Le fichier de vague où l'erreur a été détectée.
     * @param lineNumber  Le numéro de la ligne contenant l'erreur.
     * @param lineContent Le contenu de la ligne contenant l'erreur.
     * @return Un message formaté décrivant l'erreur.
     */
    private static String buildErrorMessage(String message, String levelFile,
                                            String waveFile, int lineNumber,
                                            String lineContent) {
        return String.format("""
            Error: %s
            Level file: %s
            Wave file: %s
            Line %d: %s""",
                message, levelFile, waveFile, lineNumber, lineContent);
    }
}
