package flashcard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FlashcardApp {
    private String cardsFile;
    private String order;
    private int repetitions;
    private boolean invertCards;
    private Scanner scanner;

    // Achievements
    private boolean achievementCorrect = false;
    private boolean achievementRepeat = false;
    private boolean achievementConfident = false;

    public FlashcardApp(String cardsFile, String order, int repetitions, boolean invertCards) {
        this.cardsFile = cardsFile;
        this.order = order;
        this.repetitions = repetitions;
        this.invertCards = invertCards;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("1. Start studying");
        System.out.println("2. Add new cards");
        System.out.println("3. Delete a card");
        System.out.print("Choose (1/2/3): ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("2")) {
            addCards();
            run();
            return;
        } else if (choice.equals("3")) {
            deleteCard();
            run();
            return;
        }

        List<Card> cards = loadCards(cardsFile);
        if (cards == null || cards.isEmpty()) {
            System.out.println("Error: No cards found in file");
            return;
        }

        Map<Card, Integer> correctCounts = new HashMap<>();
        for (Card card : cards) {
            correctCounts.put(card, 0);
        }

        System.out.println("Flashcard session started! (" + cards.size() + " cards)");
        System.out.println("Order: " + order + " | Repetitions needed: " + repetitions);
        System.out.println("----------------------------------------");

        while (true) {
            List<Card> remaining = new ArrayList<>();
            for (Card card : cards) {
                if (correctCounts.get(card) < repetitions) {
                    remaining.add(card);
                }
            }

            if (remaining.isEmpty()) {
                System.out.println("Congratulations! All cards completed!");
                printAchievements();
                break;
            }

            remaining = organize(remaining);

            boolean allCorrectThisRound = true;
            long roundStart = System.currentTimeMillis();

            for (Card card : remaining) {
                if (correctCounts.get(card) >= repetitions) continue;

                String q = invertCards ? card.getAnswer() : card.getQuestion();
                String a = invertCards ? card.getQuestion() : card.getAnswer();

                System.out.println("\n[" + (correctCounts.get(card) + 1) + "/" + repetitions + "] Q: " + q);
                System.out.print("Your answer: ");
                String userAnswer = scanner.nextLine().trim();

                if (userAnswer.equalsIgnoreCase(a)) {
                    System.out.println("Correct!");
                    card.markCorrect();
                    correctCounts.put(card, correctCounts.get(card) + 1);

                    // CONFIDENT: 3 удаа зөв хариулсан
                    if (card.getCorrectCount() >= 3) {
                        achievementConfident = true;
                    }
                } else {
                    System.out.println("Wrong! Answer: " + a);
                    card.markWrong();
                    allCorrectThisRound = false;
                }

                // REPEAT: нийт 5-аас олон удаа хариулсан
                if (card.getTotalAnswered() > 5) {
                    achievementRepeat = true;
                }
            }

            // CORRECT: тойрогт бүх карт зөв хариулсан
            if (allCorrectThisRound) {
                achievementCorrect = true;
            }

            // Тойрогт 5 секундээс доош хариулсан
            long roundTime = (System.currentTimeMillis() - roundStart) / 1000;
            if (roundTime < 5) {
                System.out.println("\n⚡ SPEED BONUS: Answered in under 5 seconds!");
            }
        }

        scanner.close();
    }

    private void printAchievements() {
        System.out.println("\n========== ACHIEVEMENTS ==========");
        if (achievementCorrect) {
            System.out.println("🏆 CORRECT: All cards correct in last round!");
        }
        if (achievementRepeat) {
            System.out.println("🔁 REPEAT: Answered a card more than 5 times!");
        }
        if (achievementConfident) {
            System.out.println("💪 CONFIDENT: Answered a card correctly 3+ times!");
        }
        if (!achievementCorrect && !achievementRepeat && !achievementConfident) {
            System.out.println("No achievements yet. Keep practicing!");
        }
        System.out.println("==================================");
    }

    private void addCards() {
        System.out.println("Enter cards (type 'done' to finish):");
        try (FileWriter fw = new FileWriter(cardsFile, true)) {
            while (true) {
                System.out.print("Question: ");
                String q = scanner.nextLine().trim();
                if (q.equalsIgnoreCase("done")) break;
                System.out.print("Answer: ");
                String a = scanner.nextLine().trim();
                fw.write(q + ";;" + a + "\n");
                System.out.println("Card added!");
            }
        } catch (IOException e) {
            System.out.println("Error saving card: " + e.getMessage());
        }
    }

    private void deleteCard() {
        List<Card> cards = loadCards(cardsFile);
        if (cards == null || cards.isEmpty()) {
            System.out.println("No cards to delete!");
            return;
        }

        System.out.println("Select card to delete:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i).getQuestion() + " -> " + cards.get(i).getAnswer());
        }
        System.out.print("Enter number (0 to cancel): ");
        try {
            int num = Integer.parseInt(scanner.nextLine().trim());
            if (num == 0) return;
            if (num < 1 || num > cards.size()) {
                System.out.println("Invalid number!");
                return;
            }
            cards.remove(num - 1);
            saveCards(cards);
            System.out.println("Card deleted!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void saveCards(List<Card> cards) {
        try (FileWriter fw = new FileWriter(cardsFile, false)) {
            for (Card card : cards) {
                fw.write(card.getQuestion() + ";;" + card.getAnswer() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving cards: " + e.getMessage());
        }
    }

    private List<Card> organize(List<Card> cards) {
        CardOrganizer organizer;
        switch (order) {
            case "worst-first":
                organizer = new WorstFirstSorter();
                break;
            case "recent-mistakes-first":
                organizer = new RecentMistakesFirstSorter();
                break;
            default:
                organizer = new RandomSorter();
                break;
        }
        return organizer.organize(cards);
    }

    private List<Card> loadCards(String filename) {
        List<Card> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(";;");
                if (parts.length == 2) {
                    cards.add(new Card(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found: " + filename);
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
        return cards;
    }
}