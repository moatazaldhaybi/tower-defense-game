package exception;

/**
 * Exception levée lorsqu'un chemin de carte invalide est détecté.
 * Cette exception étend {@link GameException} et fournit des détails spécifiques
 * sur les fichiers et la raison de l'erreur.
 */
public class InvalidMapPathException extends GameException {
    private final String levelFile; // Le fichier de niveau associé à l'erreur
    private final String mapFile;   // Le fichier de carte où l'erreur a été trouvée
    private final String reason;    // La raison spécifique de l'erreur

    /**
     * Constructeur pour créer une exception avec des détails sur l'erreur.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant l'erreur.
     * @param reason    La raison décrivant pourquoi le chemin de la carte est invalide.
     */
    public InvalidMapPathException(String levelFile, String mapFile, String reason) {
        super(buildErrorMessage(levelFile, mapFile, reason));
        this.levelFile = levelFile;
        this.mapFile = mapFile;
        this.reason = reason;
    }

    /**
     * Construit un message d'erreur détaillé avec les informations fournies.
     *
     * @param levelFile Le fichier de niveau associé à l'erreur.
     * @param mapFile   Le fichier de carte contenant l'erreur.
     * @param reason    La raison de l'erreur.
     * @return Un message formaté décrivant l'erreur.
     */
    private static String buildErrorMessage(String levelFile, String mapFile, String reason) {
        return String.format("""
            Invalid map path in:
            Level file: %s
            Map file: %s
            Reason: %s""",
                levelFile, mapFile, reason);
    }
}
