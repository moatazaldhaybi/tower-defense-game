package entity.enemy;

import entity.tower.Tower;
import game.Element;
import game.Point2D;

import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un ennemi de type EarthBrute.
 * Cet ennemi est caractérisé par une faible vitesse de déplacement mais une bonne résistance.
 */
public class EarthBruteEnemy extends Enemy {

    /**
     * Constructeur pour initialiser un ennemi de type EarthBrute avec des caractéristiques spécifiques.
     *
     * @param spawnPoint Le point de départ (spawn) de l'ennemi sur la carte.
     * @param path       Le chemin que l'ennemi doit suivre pour atteindre la base.
     */
    public EarthBruteEnemy(Point2D spawnPoint, List<Point2D> path) {
        super(spawnPoint, 
              30,              // Points de vie
              Element.EARTH,   // Élément associé (Terre)
              5,               // Puissance d'attaque
              1,               // Vitesse d'attaque (en secondes)
              3,               // Portée de l'attaque
              1,               // Vitesse de déplacement
              3);              // Récompense en pièces d'or
        this.path = path; // Définit le chemin que l'ennemi doit suivre
    }

    /**
     * Sélectionne la cible à attaquer parmi une liste de tours.
     * La cible est choisie en fonction de sa proximité avec l'ennemi, à condition qu'elle soit dans sa portée.
     *
     * @param towers La liste des tours disponibles pour cible.
     * @return La tour la plus proche dans la portée de l'ennemi, ou {@code null} s'il n'y a aucune cible valide.
     */
    @Override
    public Tower selectTarget(List<Tower> towers) {
        return towers.stream()
                .filter(this::isInRange) // Filtre les tours dans la portée
                .min(Comparator.comparingDouble(t -> position.distance(t.getPosition()))) // Trouve la plus proche
                .orElse(null); // Retourne null si aucune tour n'est trouvée
    }

    /**
     * Met à jour l'état de l'ennemi à chaque itération de la boucle de jeu.
     * Cette méthode gère le déplacement de l'ennemi sur son chemin et met à jour le timer d'attaque.
     *
     * @param deltaTime Le temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    public void update(double deltaTime) {
        moveAlongPath(deltaTime); // Déplace l'ennemi le long de son chemin
        attackTimer += deltaTime; // Augmente le timer pour gérer les attaques
    }
}
