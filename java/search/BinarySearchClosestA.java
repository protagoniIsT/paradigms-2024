package search;
//Floyd-Hoare logic (Hoare triples)
public class BinarySearchClosestA {
    //{Inv}: x in [a[left'], a[right']]
    //{Pred}: left = 0; right = a.length - 1;
    //forall i in [1, a.length - 1] : a[i - 1] <= a[i]
    //x - integer && immutable
    public static int iterativeBinarySearch(int x, int[] a) {
        /*
        {Inv}:
         Let A = {abs(x - e) forall e in a} -> ind(x): abs(a[ind(x)] - x) == min(A)
         right' > left' && left' <= min(ind(x)) <= right':
             a.length >= 0 && -1 <= min(ind(x)) <= a.length -> true
        */
        int left = 0;
        int right = a.length - 1;
        while (left <= right) {
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
                 right' - left' >= 0 -> right' - left' >= (right' - left') / 2 -> left' <= mid' <= right'
            */
            if (a[mid] < x) {
                //a[mid'] < x && {Inv} -> left' = mid' + 1
                left = mid + 1;
                //New range [left = mid + 1; right] potentially contains desired element (element > x)
            } else if (a[mid] > x) {
                //a[mid'] > x && {Inv} -> right' = mid' - 1
                right = mid - 1;
                //New range [left; right = mid - 1] potentially contains desired element (element < x)
            } else {
                return a[mid]; // abs(a[mid] - x) == 0
            }
        }
        //{Post}: left > right -> continue
        if (left >= a.length) {
            // x not in a (x > max(a)) -> abs(a[a.length - 1] - x) == min(A)
            return a[a.length - 1];
        } else if (left == 0) {
            // x not in a (x < min(a)) -> abs(a[0] - x) == min(A)
            return a[0];
        } else {
            //min(a) < x < max(a)
            int closestLess = a[left - 1];
            int closestSuperior = a[left];
            /*
            forall i in [0, a.length - 1] : a[i] == x; min(a) < x < max(a) -> i = left - 1 -- max index such that a[i] < x,
            а j = left -- min index such that a[j] > x.
             */
            return (x - closestLess <= closestSuperior - x) ? closestLess : closestSuperior;
            // closest to x
        }
    }

    private static int recursiveBinarySearch(int[] a, int x, int left, int right) {
        /*
        {Inv}:
         Let A = {abs(x - e) forall e in a} -> ind(x): abs(a[ind(x)] - x) == min(A)
         right' > left' && left' <= min(ind(x)) <= right':
             a.length >= 0 && -1 <= min(ind(x)) <= a.length -> true
        */
        int mid = left + (right - left) / 2;
        if (left > right) {
            //desired element not in a
            if (right < 0) {
                // search to the left from the beginning -> closest to x -- a[left'] (0 <= left <= right < a.length)
                return left;
            } else if (left >= a.length || x - a[right] <= a[left] - x) {
                // search was made right from the end -> closest to x -- a[right]
                return right;
            } else {
                //else -- a[left]
                return left;
            }
        }
        if (a[mid] < x) {
            // a[mid' + 1] >= x && a[mid'] < x && {Inv}
            return recursiveBinarySearch(a, x, mid + 1, right);
            //New range [left = mid + 1; right] potentially contains desired element
        } else if (a[mid] > x) {
            //a[mid'] > x && a[mid' - 1] <= x && {Inv}
            return recursiveBinarySearch(a, x, left, mid - 1);
            //New range [left; right = mid - 1] potentially contains desired element
        } else {
            //a[mid'] == x (closest to x)
            return mid;
        }
        /*
        in this implementation, we look for an index,
          and then in the main function we print an array element with a recursiveBinarySearch index (a, x, 0, a.length - 1)
         */
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        int sum = 0;
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
            sum += a[i - 1];
        }
        if (sum % 2 == 0) {
            System.out.println(a[recursiveBinarySearch(a, x, 0, a.length - 1)]);
        } else {
            System.out.println(iterativeBinarySearch(x, a));
        }
    }
}
