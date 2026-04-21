# Flashcard CLI System

CSA311 Homework 1 - Simple flashcard learning system built with Java and Maven.

## Requirements

- Java 17+
- Maven 3.6+

## Build

```bash
mvn package
```

## Run

```bash
java -jar target/flashcard-1.0.jar <cards-file> [options]
```

## Options

```
--help                    Show this help
--order <order>           random | worst-first | recent-mistakes-first
--repetitions <num>       How many correct answers required per card
--invertCards             Swap question and answer
```

## Examples

```bash
# Basic run
java -jar target/flashcard-1.0.jar cards.txt

# Worst first order, 2 repetitions
java -jar target/flashcard-1.0.jar cards.txt --order worst-first --repetitions 2

# Invert cards
java -jar target/flashcard-1.0.jar cards.txt --invertCards

# Show help
java -jar target/flashcard-1.0.jar --help
```

## Cards file format

```
# Comment line
Question one;;Answer one
Question two;;Answer two
```

## Achievements

- **CORRECT**: All cards correct in last round
- **REPEAT**: Answered a card more than 5 times
- **CONFIDENT**: Answered a card correctly 3+ times
- **SPEED BONUS**: Answered all cards in under 5 seconds

## Project Structure

```
flashcard/
├── src/
│   └── main/
│       └── java/
│           └── flashcard/
│               ├── Main.java
│               ├── Card.java
│               ├── CardOrganizer.java
│               ├── RandomSorter.java
│               ├── WorstFirstSorter.java
│               ├── RecentMistakesFirstSorter.java
│               └── FlashcardApp.java
├── cards.txt
└── pom.xml
```