package entity.enemy;

import entity.tower.Tower;
import game.Element;
import game.Point2D;

import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un ennemi de type WindGrognard.
 * Cet ennemi cible la tour à portée avec le moins de points de vie.
 */
public class WindGrognardEnemy extends Enemy {

    /**
     * Constructeur pour initialiser un ennemi de type WindGrognard avec des caractéristiques spécifiques.
     *
     * @param startPosition La position de départ de l'ennemi sur la carte.
     * @param path          Le chemin que l'ennemi doit suivre pour atteindre la base.
     */
    public WindGrognardEnemy(Point2D startPosition, List<Point2D> path) {
        super(startPosition, 
              1,             // Points de vie
              Element.AIR,   // Élément associé (Air)
              7,             // Puissance d'attaque
              2,             // Temps entre deux attaques (en secondes)
              5,             // Portée de l'ennemi
              2,             // Vitesse de déplacement
              1);            // Récompense en pièces d'or
        this.path = path;
    }

    /**
     * Sélectionne la cible principale à attaquer parmi une liste de tours.
     * La cible est choisie en fonction de ses points de vie, en visant la tour avec le moins de PV.
     *
     * @param towers La liste des tours disponibles pour cible.
     * @return La tour avec le moins de points de vie dans la portée de l'ennemi, ou {@code null} si aucune cible valide.
     */
    @Override
    public Tower selectTarget(List<Tower> towers) {
        return towers.stream()
                .filter(this::isInRange) // Filtre les tours dans la portée
                .min(Comparator.comparingInt(Tower::getHealth)) // Trouve celle avec le moins de PV
                .orElse(null); // Retourne null si aucune tour n'est trouvée
    }

    /**
     * Met à jour l'état de l'ennemi à chaque itération de la boucle de jeu.
     * Cette méthode gère le déplacement de l'ennemi sur son chemin et met à jour le timer d'attaque.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    public void update(double deltaTime) {
        moveAlongPath(deltaTime); // Déplace l'ennemi le long de son chemin
        attackTimer += deltaTime; // Incrémente le timer pour gérer les attaques
    }
}
