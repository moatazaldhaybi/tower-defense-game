package entity.enemy;

import entity.tower.Tower;
import game.Element;
import game.Point2D;

import java.util.List;

/**
 * Classe représentant un ennemi de type Minion.
 * Cet ennemi est basique, se déplace uniquement sur son chemin et ne peut pas attaquer.
 */
public class MinionEnemy extends Enemy {

    /**
     * Constructeur pour initialiser un ennemi de type Minion avec des caractéristiques spécifiques.
     *
     * @param startPosition La position de départ de l'ennemi sur la carte.
     * @param path          Le chemin que l'ennemi doit suivre pour atteindre la base.
     */
    public MinionEnemy(Point2D startPosition, List<Point2D> path) {
        // Caractéristiques issues du PDF :
        // Name: Minion, PV: 10, ATK: 3, ATKSpeed: 0, Range: 0, Element: NONE, Speed: 1, Reward: 1
        super(startPosition, 10, Element.NONE, 3, 0, 0, 1, 1);
        this.path = path;
    }

    /**
     * Sélectionne une cible parmi une liste de tours.
     * Pour le Minion, cette méthode retourne toujours {@code null} car il ne peut pas attaquer.
     *
     * @param towers La liste des tours disponibles pour cible.
     * @return Toujours {@code null} car le Minion ne peut pas attaquer.
     */
    @Override
    public Tower selectTarget(List<Tower> towers) {
        return null;
    }

    /**
     * Met à jour l'état de l'ennemi à chaque itération de la boucle de jeu.
     * Le Minion se déplace uniquement sur son chemin et ne peut pas attaquer.
     *
     * @param deltaTime Temps écoulé (en secondes) depuis la dernière mise à jour.
     */
    @Override
    public void update(double deltaTime) {
        moveAlongPath(deltaTime); // Déplace l'ennemi le long de son chemin
    }
}
