package de.neoventus.init;

/**
 * Persissions of the Roles
 *
 * @autor: Markus Knauer
 * @version: 0.0.1
 *
 */
public enum Permission {

    ADMIN(0), CEO(1), SERVICE(2), CHEF(3), BAR(4);

    final int nr;

    private Permission(int nr){
        this.nr = nr;
    }

}
