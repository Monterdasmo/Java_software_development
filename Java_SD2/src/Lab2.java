/**
 * Лабораторна робота №2. Рядки.
 * Номер залікової книжки: 3413
 *
 * Обчислення:
 *  C3  = 3413 mod 3  = 2
 *  C17 = 3413 mod 17 = 13
 *
 * Відповідно до варіанту:
 *
 *  C3 = 2  → тип текстових змінних: StringBuffer
 *  C17 = 13 → дія з текстом:
 *      "В заданому тексті знайти підрядок максимальної довжини, що є паліндромом,
 *       тобто читається однаково зліва направо та справа наліво."
 *
 * Програма:
 *  1) Створює текст у вигляді StringBuffer.
 *  2) Знаходить найдовший паліндромний підрядок (без урахування регістру).
 *  3) Виводить початковий текст та знайдений паліндром.
 */

public class Lab2 {

    public static void main(String[] args) {
        try {
            // Початковий текст (можеш підставити свій варіант)
            StringBuffer text = new StringBuffer(
                    "Madam Anna likes to read civic stories and level her time wisely."
            );

            System.out.println("Original text:");
            System.out.println(text.toString());

            StringBuffer longestPalindrome = findLongestPalindromeSubstring(text);

            if (longestPalindrome.length() > 1) {
                System.out.println("\nLongest palindrome substring:");
                System.out.println(longestPalindrome.toString());
                System.out.println("Length: " + longestPalindrome.length());
            } else if (longestPalindrome.length() == 1) {
                System.out.println("\nOnly single-character palindromes found.");
                System.out.println("Example: " + longestPalindrome.charAt(0));
            } else {
                System.out.println("\nNo palindrome substring was found.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input text: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Знаходить підрядок максимальної довжини, що є паліндромом.
     * Працює без урахування регістру (A == a).
     *
     * Використовує алгоритм "розширення від центру" для кожної позиції:
     *  - паліндром непарної довжини (центр в одній букві)
     *  - паліндром парної довжини (центр між двома буквами)
     *
     * @param text вхідний текст як StringBuffer (не null, не порожній)
     * @return StringBuffer з найдовшим паліндромом (може бути довжини 1)
     */
    public static StringBuffer findLongestPalindromeSubstring(StringBuffer text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        int n = text.length();
        if (n == 0) {
            return new StringBuffer(); // порожній результат
        }

        int bestStart = 0;
        int bestLen = 1; // будь-який окремий символ — вже паліндром

        for (int i = 0; i < n; i++) {
            // Непарна довжина: центр в i
            int[] odd = expandAroundCenter(text, i, i);

            int oddLen = odd[1] - odd[0] + 1;
            if (oddLen > bestLen) {
                bestLen = oddLen;
                bestStart = odd[0];
            }

            // Парна довжина: центр між i та i+1
            int[] even = expandAroundCenter(text, i, i + 1);

            int evenLen = even[1] - even[0] + 1;
            if (evenLen > bestLen) {
                bestLen = evenLen;
                bestStart = even[0];
            }
        }

        // Формуємо результат як StringBuffer (не використовуємо substring() String)
        StringBuffer result = new StringBuffer();
        int end = bestStart + bestLen;
        for (int i = bestStart; i < end; i++) {
            result.append(text.charAt(i));
        }
        return result;
    }

    /**
     * Розширює межі [left, right], поки підрядок є паліндромом (без урахування регістру).
     *
     * @param text  текст
     * @param left  початковий лівий індекс
     * @param right початковий правий індекс
     * @return масив [start, end] — межі знайденого паліндрома
     */
    private static int[] expandAroundCenter(StringBuffer text, int left, int right) {
        int n = text.length();

        while (left >= 0 && right < n) {
            char cLeft = text.charAt(left);
            char cRight = text.charAt(right);

            // Порівнюємо без урахування регістру
            if (Character.toLowerCase(cLeft) != Character.toLowerCase(cRight)) {
                break;
            }

            left--;
            right++;
        }

        // Після виходу з циклу ми вийшли за межі паліндрома, тому зсуваємо назад
        left++;
        right--;

        if (right < left) {
            return new int[]{0, -1};
        }
        return new int[]{left, right};
    }
}
