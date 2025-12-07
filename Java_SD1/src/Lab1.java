/**
 * Лабораторна робота №1. Масиви.
 * Номер залікової книжки: 3413
 *
 * Обчислення:
 *  C5 = 3413 mod 5  = 3
 *  C7 = 3413 mod 7  = 4
 *  C11 = 3413 mod 11 = 3
 *
 * Відповідно до варіантів:
 *
 *  C5 = 3  →  Операція з матрицями: C = A ⊕ B
 *             (побітове виключне "АБО" (XOR) для кожного елемента)
 *
 *  C7 = 4  →  Тип елементів матриць: long
 *             (матриці A, B, C мають тип long[][])
 *
 *  C11 = 3 →  Дія з матрицею C:
 *             Обчислити суму найбільших елементів кожного рядка матриці C.
 *
 * Програма:
 *  1) Створює дві матриці A і B типу long.
 *  2) Обчислює C = A XOR B.
 *  3) Виводить матрицю C.
 *  4) Обчислює та виводить суму максимумів по рядках матриці C.
 */

public class Lab1 {

    public static void main(String[] args) {
        try {
            // Розміри матриць (для прикладу)
            int rows = 3;
            int cols = 4;

            // Матриця A (тип long, як вимагає C7 = 4)
            long[][] A = {
                    {5, 12, 7, 3},
                    {9, 0, 4, 11},
                    {6, 8, 15, 2}
            };

            // Матриця B (тип long)
            long[][] B = {
                    {1, 4, 9, 3},
                    {7, 6, 2, 10},
                    {5, 12, 8, 1}
            };

            // C5 = 3 → C = A ⊕ B (побітове XOR поелементно)
            long[][] C = xorMatrices(A, B);

            System.out.println("Matrix C = A XOR B:");
            printMatrix(C);

            // C11 = 3 → сума найбільших елементів кожного рядка матриці C
            long sumMax = sumOfMaxInRows(C);

            System.out.println("\nSum of maximum elements in each row of C = " + sumMax);

        } catch (Exception e) {
            // Обробка будь-яких виключних ситуацій
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Обчислює C = A XOR B для двох матриць однакового розміру.
     * Побітове виключне "АБО" (^) застосовується до кожної пари елементів.
     */
    public static long[][] xorMatrices(long[][] A, long[][] B) {
        if (A == null || B == null) {
            throw new IllegalArgumentException("Matrices must not be null");
        }
        if (A.length == 0 || B.length == 0) {
            throw new IllegalArgumentException("Matrices must not be empty");
        }
        if (A.length != B.length || A[0].length != B[0].length) {
            throw new IllegalArgumentException("Matrices must have the same dimensions");
        }

        int rows = A.length;
        int cols = A[0].length;

        long[][] C = new long[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (A[i].length != cols || B[i].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
            for (int j = 0; j < cols; j++) {
                C[i][j] = A[i][j] ^ B[i][j]; // XOR для long
            }
        }
        return C;
    }

    /**
     * Обчислює суму найбільших елементів кожного рядка матриці C.
     * (Дія згідно C11 = 3)
     */
    public static long sumOfMaxInRows(long[][] C) {
        if (C == null || C.length == 0) {
            throw new IllegalArgumentException("Matrix C must not be null or empty");
        }

        long sum = 0;
        for (long[] row : C) {
            if (row == null || row.length == 0) {
                throw new IllegalArgumentException("Row in matrix C must not be null or empty");
            }

            long max = row[0];
            for (long value : row) {
                if (value > max) {
                    max = value;
                }
            }
            sum += max;
        }
        return sum;
    }

    /**
     * Виводить матрицю на екран у табличному вигляді.
     */
    public static void printMatrix(long[][] M) {
        if (M == null) {
            System.out.println("Matrix is null");
            return;
        }
        for (long[] row : M) {
            if (row == null) {
                System.out.println("null row");
                continue;
            }
            for (long val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }
}
