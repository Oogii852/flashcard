package flashcard;

import java.util.List;

public class WorstFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        cards.sort((a, b) -> b.getWrongCount() - a.getWrongCount());
        return cards;
    }
}