package flashcard;

import java.util.ArrayList;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> wrong = new ArrayList<>();
        List<Card> correct = new ArrayList<>();

        for (Card card : cards) {
            if (card.wasWrongLastRound()) {
                wrong.add(card);
            } else {
                correct.add(card);
            }
        }

        wrong.addAll(correct);
        return wrong;
    }
}