import java.util.ArrayList;
import java.util.List;

/**
 * Лабораторна робота №4.
 * Тема: Відношення між класами в мові програмування Java.
 *
 * Номер залікової книжки: 3413
 *
 * Зв'язок з ЛР2 (рядки):
 *  C3  = 3413 mod 3  = 2  → тип текстових змінних: StringBuffer
 *  C17 = 3413 mod 17 = 13 → дія з текстом:
 *      "В заданому тексті знайти підрядок максимальної довжини, що є паліндромом,
 *       тобто читається однаково зліва направо та справа наліво."
 *
 * ЛР4 вимагає:
 *  1) Створити окремі класи для:
 *     - літери (Letter),
 *     - слова (Word),
 *     - розділового знака (PunctuationMark),
 *     - речення (Sentence),
 *     - тексту (Text).
 *     Слово складається з масиву літер, речення — з масиву слів та розділових
 *     знаків, текст — з масиву речень.
 *  2) Замінити послідовність табуляцій та пробілів одним пробілом.
 *  3) Виконати дію з ЛР2 (пошук найдовшого паліндромного підрядка), але вже
 *     працюючи з власними класами (через клас Text). :contentReference[oaicite:1]{index=1}
 */
public class Lab4Composition {

    public static void main(String[] args) {
        try {
            // Початковий текст (можеш змінити на свій, головне – не порожній)
            String rawText = "Madam\tAnna   likes   to read   civic stories  and   level  her time wisely.";

            System.out.println("=== Raw text ===");
            System.out.println(rawText);

            // 1) Нормалізуємо пробіли/табуляції та парсимо у власні класи
            Text text = Text.parse(rawText);

            System.out.println("\n=== Normalized text (via Text.toPlainString) ===");
            System.out.println(text.toPlainString());

            // 2) Виконуємо дію з ЛР2: знаходимо найдовший паліндром-підрядок
            String longestPalindrome = text.findLongestPalindromeSubstring();

            System.out.println("\n=== Longest palindrome substring in text ===");
            if (longestPalindrome.isEmpty()) {
                System.out.println("No palindrome substring found.");
            } else {
                System.out.println("Palindrome: \"" + longestPalindrome + "\"");
                System.out.println("Length: " + longestPalindrome.length());
            }

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * Інтерфейс-мітка для елементів речення: слово або розділовий знак.
 */
interface SentencePart {
}

/**
 * Клас, що представляє одну літеру.
 */
class Letter {

    private final char value;

    public Letter(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public Letter toLowerCase() {
        return new Letter(Character.toLowerCase(value));
    }

    public Letter toUpperCase() {
        return new Letter(Character.toUpperCase(value));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Клас, що представляє слово як масив літер.
 */
class Word implements SentencePart {

    private final Letter[] letters;

    public Word(Letter[] letters) {
        this.letters = letters;
    }

    public Letter[] getLetters() {
        return letters;
    }

    public int length() {
        return letters.length;
    }

    /**
     * Повертає слово у вигляді звичайного рядка.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(letters.length);
        for (Letter letter : letters) {
            sb.append(letter.getValue());
        }
        return sb.toString();
    }
}

/**
 * Клас, що представляє розділовий знак (.,!?;: і т.д.).
 */
class PunctuationMark implements SentencePart {

    private final char symbol;

    public PunctuationMark(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}

/**
 * Клас, що представляє речення як масив слів та розділових знаків.
 */
class Sentence {

    private final SentencePart[] parts;

    public Sentence(SentencePart[] parts) {
        this.parts = parts;
    }

    public SentencePart[] getParts() {
        return parts;
    }

    /**
     * Повертає речення у вигляді звичайного рядка.
     * Між словами додається пробіл, розділові знаки прилягають до попереднього слова.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean previousWasWord = false;

        for (SentencePart part : parts) {
            if (part instanceof Word word) {
                if (previousWasWord) {
                    sb.append(' ');
                }
                sb.append(word.toString());
                previousWasWord = true;
            } else if (part instanceof PunctuationMark mark) {
                sb.append(mark.getSymbol());
                previousWasWord = false;
            }
        }
        return sb.toString();
    }
}

/**
 * Клас, що представляє текст як масив речень.
 * Містить логіку нормалізації пробілів та пошуку паліндромного підрядка.
 */
class Text {

    private final Sentence[] sentences;

    public Text(Sentence[] sentences) {
        this.sentences = sentences;
    }

    public Sentence[] getSentences() {
        return sentences;
    }

    /**
     * Нормалізує вхідний рядок:
     *  - замінює послідовності пробілів і табуляцій одним пробілом,
     *  - прибирає початкові/кінцеві пробіли.
     */
    private static String normalizeWhitespace(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean inSpace = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == ' ' || ch == '\t') {
                if (!inSpace) {
                    sb.append(' ');
                    inSpace = true;
                }
            } else {
                sb.append(ch);
                inSpace = false;
            }
        }
        // приберемо пробіли з країв
        return sb.toString().trim();
    }

    /**
     * Парсить сирий текст у структуру Text → Sentence → (Word | PunctuationMark) → Letter.
     */
    public static Text parse(String rawText) {
        String normalized = normalizeWhitespace(rawText);

        if (normalized.isEmpty()) {
            return new Text(new Sentence[0]);
        }

        // Розбиваємо текст на речення за . ! ?
        List<Sentence> sentenceList = new ArrayList<>();
        StringBuilder currentSentence = new StringBuilder();

        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            currentSentence.append(ch);

            if (ch == '.' || ch == '!' || ch == '?') {
                // кінець речення
                sentenceList.add(parseSentence(currentSentence.toString()));
                currentSentence.setLength(0);
            }
        }

        // Якщо після останнього знаку немає крапки/знаку оклику/запитання –
        // теж вважаємо це реченням.
        if (currentSentence.length() > 0) {
            sentenceList.add(parseSentence(currentSentence.toString()));
        }

        Sentence[] sentences = sentenceList.toArray(new Sentence[0]);
        return new Text(sentences);
    }

    /**
     * Парсить один рядок-речення у Sentence:
     * розділяє на слова (набір літер) та розділові знаки.
     */
    private static Sentence parseSentence(String sentenceString) {
        List<SentencePart> parts = new ArrayList<>();

        StringBuilder currentWord = new StringBuilder();

        for (int i = 0; i < sentenceString.length(); i++) {
            char ch = sentenceString.charAt(i);

            if (Character.isLetter(ch)) {
                currentWord.append(ch);
            } else if (ch == ' ') {
                if (currentWord.length() > 0) {
                    parts.add(buildWordFromBuffer(currentWord));
                    currentWord.setLength(0);
                }
            } else {
                // Розділовий знак або інший символ
                if (currentWord.length() > 0) {
                    parts.add(buildWordFromBuffer(currentWord));
                    currentWord.setLength(0);
                }
                if (isPunctuation(ch)) {
                    parts.add(new PunctuationMark(ch));
                }
                // Інші символи (цифри тощо) можна або ігнорувати,
                // або додати як окремий тип; для ЛР достатньо цього.
            }
        }

        if (currentWord.length() > 0) {
            parts.add(buildWordFromBuffer(currentWord));
        }

        SentencePart[] arr = parts.toArray(new SentencePart[0]);
        return new Sentence(arr);
    }

    /**
     * Перетворює вміст StringBuilder у Word (масив літер).
     */
    private static Word buildWordFromBuffer(StringBuilder wordBuffer) {
        Letter[] letters = new Letter[wordBuffer.length()];
        for (int i = 0; i < wordBuffer.length(); i++) {
            letters[i] = new Letter(wordBuffer.charAt(i));
        }
        return new Word(letters);
    }

    /**
     * Перевіряє, чи є символ розділовим знаком.
     */
    private static boolean isPunctuation(char ch) {
        return ",.!?;:–-".indexOf(ch) >= 0;
    }

    /**
     * Повертає весь текст як один String (через структуру класів).
     */
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentences.length; i++) {
            sb.append(sentences[i].toString());
            if (i < sentences.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * Реалізація дії з ЛР2 (C17 = 13):
     * Знаходить підрядок максимальної довжини, що є паліндромом.
     * Порівняння без урахування регістру (A == a).
     */
    public String findLongestPalindromeSubstring() {
        String text = toPlainString();
        if (text.isEmpty()) {
            return "";
        }

        int n = text.length();
        int bestStart = 0;
        int bestLen = 1;

        for (int i = 0; i < n; i++) {
            // непарна довжина (центр у символі i)
            int[] odd = expandAroundCenter(text, i, i);
            int oddLen = odd[1] - odd[0] + 1;
            if (oddLen > bestLen) {
                bestLen = oddLen;
                bestStart = odd[0];
            }

            // парна довжина (центр між i та i+1)
            int[] even = expandAroundCenter(text, i, i + 1);
            int evenLen = even[1] - even[0] + 1;
            if (evenLen > bestLen) {
                bestLen = evenLen;
                bestStart = even[0];
            }
        }

        return text.substring(bestStart, bestStart + bestLen);
    }

    /**
     * Розширює межі [left, right], поки текст залишається паліндромом
     * (ігноруючи регістр символів).
     */
    private int[] expandAroundCenter(String text, int left, int right) {
        int n = text.length();

        while (left >= 0 && right < n) {
            char cLeft = Character.toLowerCase(text.charAt(left));
            char cRight = Character.toLowerCase(text.charAt(right));
            if (cLeft != cRight) {
                break;
            }
            left--;
            right++;
        }

        left++;
        right--;

        if (right < left) {
            return new int[] {0, -1};
        }
        return new int[] {left, right};
    }
}
