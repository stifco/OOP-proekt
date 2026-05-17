/**
 * Входна точка на приложението CFG Manager.
 * Просто създава CommandSystem и стартира цикъла му.
 */
public class Main {

    /**
     * Стартира приложението.
     *
     * @param args аргументи от командния ред (не се ползват)
     */
    public static void main(String[] args) {
        CommandSystem cs = new CommandSystem();
        cs.run();
    }
}h