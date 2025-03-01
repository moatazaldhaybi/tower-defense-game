package exception;

/**
 * Classe de base pour les exceptions spécifiques au jeu.
 * Hérite de {@link RuntimeException}, permettant de gérer des erreurs non contrôlées
 * liées aux mécanismes du jeu.
 */
public class GameException extends RuntimeException {

    /**
     * Constructeur pour créer une exception avec un message d'erreur spécifique.
     *
     * @param message Le message décrivant l'erreur.
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructeur pour créer une exception avec un message d'erreur spécifique et une cause sous-jacente.
     *
     * @param message Le message décrivant l'erreur.
     * @param cause   L'exception d'origine ayant causé cette erreur.
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
