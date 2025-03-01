package entity.enemy;

import entity.tower.Tower;
import game.Element;
import game.Point2D;

import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un ennemi de type Boss.
 * Cet ennemi est puissant, vise les tours les plus proches et suit un chemin défini.
 */
public class BossEnemy extends Enemy {

    /**
     * Constructeur pour initialiser un ennemi de type Boss avec des caractéristiques spécifiques.
     *
     * @param spawnPoint Le point de départ (spawn) du Boss sur la carte.
     * @param path       Le chemin que le Boss doit suivre pour atteindre la base.
     */
    public BossEnemy(Point2D spawnPoint, List<Point2D> path) {
        super(spawnPoint, 
              150,             // Points de vie
              Element.FIRE,    // Élément associé (Feu)
              100,             // Puissance d'attaque
              10.0,            // Vitesse d'attaque (en secondes)
              2.0,             // Portée de l'attaque
              0.5,             // Vitesse de déplacement
              100);            // Récompense en pièces d'or
        this.path = path; // Définit le chemin que le Boss doit suivre
    }

    /**
     * Sélectionne la cible à attaquer parmi une liste de tours.
     * La cible est choisie en fonction de sa proximité avec le Boss, à condition qu'elle soit dans sa portée.
     *
     * @param towers La liste des tours disponibles pour cible.
     * @return La tour la plus proche dans la portée du Boss, ou {@code null} s'il n'y a aucune cible valide.
     */
    @Override
    public Tower selectTarget(List<Tower> towers) {
        return towers.stream()
                .filter(this::isInRange) // Filtre les tours dans la portée
                .min(Comparator.comparingDouble(t -> position.distance(t.getPosition()))) // Trouve la plus proche
                .orElse(null); // Retourne null si aucune tour n'est trouvée
    }

    /**
     * Met à jour l'état du Boss à chaque itération de la boucle de jeu.
     * Cette méthode gère le déplacement du Boss sur son chemin et met à jour le timer d'attaque.
     *
     * @param deltaTime Le temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    public void update(double deltaTime) {
        moveAlongPath(deltaTime); // Déplace le Boss le long de son chemin
        attackTimer += deltaTime; // Incrémente le timer pour gérer les attaques
    }
}
