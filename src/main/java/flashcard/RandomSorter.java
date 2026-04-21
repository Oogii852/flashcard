package flashcard;

import java.util.Collections;
import java.util.List;

public class RandomSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        Collections.shuffle(cards);
        return cards;
    }
}