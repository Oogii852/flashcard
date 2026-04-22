package flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {

    public RecentMistakesFirstSorter() {}

    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> wrongCards = new ArrayList<>();
        List<Card> otherCards = new ArrayList<>();

        for (Card card : cards) {
            if (card.wasWrongLastRound()) {
                wrongCards.add(card);
            } else {
                otherCards.add(card);
            }
        }

        Collections.reverse(wrongCards);

        List<Card> result = new ArrayList<>();
        result.addAll(wrongCards);
        result.addAll(otherCards);
        return result;
    }
}