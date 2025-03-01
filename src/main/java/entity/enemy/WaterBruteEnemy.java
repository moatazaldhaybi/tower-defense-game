package entity.enemy;

import entity.tower.Tower;
import game.Element;
import game.Point2D;

import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un ennemi de type WaterBrute.
 * Cet ennemi cible la tour avec le moins de points de vie et inflige des dégâts de zone
 * aux tours situées dans un rayon de 1.5 cases.
 */
public class WaterBruteEnemy extends Enemy {

    /**
     * Constructeur pour initialiser un ennemi de type WaterBrute avec des caractéristiques spécifiques.
     *
     * @param spawnPoint Le point de départ (spawn) de l'ennemi sur la carte.
     * @param path       Le chemin que l'ennemi doit suivre pour atteindre la base.
     */
    public WaterBruteEnemy(Point2D spawnPoint, List<Point2D> path) {
        super(spawnPoint, 
              30,              // Points de vie
              Element.WATER,   // Élément associé (Eau)
              5,               // Puissance d'attaque
              1,               // Temps entre deux attaques (en secondes)
              3,               // Portée de l'ennemi
              1,               // Vitesse de déplacement
              3);              // Récompense en pièces d'or
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
     * Effectue une attaque sur les tours à portée, infligeant des dégâts à la cible principale
     * ainsi qu'à toutes les tours situées dans un rayon de 1.5 cases.
     *
     * @param towers La liste des tours disponibles.
     */
    @Override
    public void attack(List<Tower> towers) {
        if (canAttack()) {
            Tower mainTarget = selectTarget(towers); // Trouve la cible principale
            if (mainTarget != null) {
                // Inflige des dégâts à la cible principale et aux tours proches
                for (Tower tower : towers) {
                    if (position.distance(tower.getPosition()) <= 1.5) { // Rayon de 1.5 cases
                        double damage = calculateDamage(tower);
                        tower.takeDamage((int) damage);
                    }
                }
                resetAttackTimer(); // Réinitialise le timer d'attaque
                drawAttackAnimation(mainTarget); // Dessine une animation d'attaque
            }
        }
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
