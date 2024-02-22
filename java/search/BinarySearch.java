package search;

import java.util.Arrays;

public class BinarySearch {

    public static int iterativeBinarySearch(int x, int[] a) {
        int left = 0;
        int right = a.length - 1;
        //Floyd-Hoare logic (Hoare triples)
        /*
        forall i in [1, a.length - 1] : a[i - 1] >= a[i]
        x - integer && immutable
        {Inv}:
         Let ind(x): a[ind(x)] <= x
         right' > left' && left' <= min(ind(x)) <= right':
             a.length >= 0 && -1 <= min(ind(x)) <= a.length -> true
        */
        while (right >= left) {
            /* {Pred}:
            left = 0; right = a.length - 1
            0 <= left' <= right < a.length
             {Inv} && (right' >= left') -> {Inv}
            */
            int mid = left + (right - left) / 2;
            /*
                0 <= left <= right < a.length
                right' >= left' -> right' - left' >= 0;
                 right' - left' >= 0 && left' >= 0 -> (right' - left') / 2 >= 0;
                 right' - left' >= 0 - length of the currently considered part of the array ->
                 -> right' - left' >= (right' - left') / 2 -> left' <= mid' <= right'
            */
            if (a[mid] > x) {
                left = mid + 1;
                // a[mid' + 1] <= x && a[mid'] > x && {Inv}
                //New range [left = mid + 1; right] potentially contains desired element
            } else {
                right = mid - 1;
                //a[mid'] <= x && a[mid' - 1] <= x && {Inv}
            }
        }
        // B = {ind: a[ind] <= x} -> left == min(B)
        return left;
        //left > right -> !{Inv} - we've checked whole array
        //{Post}: left > right -> return left
    }

    public static int recursiveBinarySearch(int x, int[] a, int left, int right) {
        /*
        forall i in [1, a.length - 1] : a[i - 1] >= a[i]
        x - integer && immutable
        {Inv}: forall i and j in [0; a.length)   0 ≤ i < left : a[i]<x && right < j < len(a) : a[j]>x
        {Pred}: 0 <= left' <= right < a.length
        left' + (right' - left') / 2 == (left' + right) / 2;
        (right' - left') / 2; always: left' <= mid' <= right'
         */
        int mid = left + (right - left) / 2;
        //{Pred}: 0 <= left' <= right < a.length
        if (left > right) {
            //left is answer
            return left;
            //{Post}: left > right -> return left
        }
        if (a[mid] > x) {
            // a[mid' + 1] <= x && a[mid'] > x && {Inv}
            return recursiveBinarySearch(x, a, mid + 1, right);
            //New range [left = mid + 1; right] potentially contains desired element
        } else {
            //a[mid'] <= x && a[mid' - 1] <= x && {Inv}
            return recursiveBinarySearch(x, a, left, mid - 1);
            //New range [left; right = mid - 1] potentially contains desired element
        }
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        int sum = 0;
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        if (sum % 2 == 0) {
            System.out.println(recursiveBinarySearch(x, a, 0, a.length - 1));
        } else {
            System.out.println(iterativeBinarySearch(x, a));
        }
    }
}
