package flashcard;

public class Main {
    public static void main(String[] args) {
        String cardsFile = null;
        String order = "random";
        int repetitions = 1;
        boolean invertCards = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help":
                    printHelp();
                    return;
                case "--order":
                    if (i + 1 < args.length) {
                        order = args[++i];
                    } else {
                        System.out.println("Error: --order requires a value");
                        return;
                    }
                    break;
                case "--repetitions":
                    if (i + 1 < args.length) {
                        try {
                            repetitions = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.out.println("Error: --repetitions must be a number");
                            return;
                        }
                    }
                    break;
                case "--invertCards":
                    invertCards = true;
                    break;
                default:
                    cardsFile = args[i];
            }
        }

        if (cardsFile == null) {
            System.out.println("Error: cards file not specified");
            printHelp();
            return;
        }

        new FlashcardApp(cardsFile, order, repetitions, invertCards).run();
    }

    private static void printHelp() {
        System.out.println("Usage: flashcard <cards-file> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --help                    Show this help");
        System.out.println("  --order <order>           random | worst-first | recent-mistakes-first");
        System.out.println("  --repetitions <num>       How many correct answers required per card");
        System.out.println("  --invertCards             Swap question and answer");
    }
}