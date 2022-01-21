
import java.util.*;
public class Testing {

    public static void main(String[] args) {

        int[] table = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        Random r = new Random();
        for(int i = 0; i <= table.length; i++){
            int x = r.nextInt(100);
            int ans = hash(x);
            table[ans] = x;
        }
        System.out.println(Arrays.toString(table));

        //ArrayList<Integer> list = new ArrayList<>();
        int[] arr = new int[13];
        for(int i = 0; i <= 100; i++){
            System.out.println(i + "    " + hash(i));
            arr[hash(i)]++;
        }
        System.out.println(Arrays.toString(arr));

    }
    public static int hash(int x){
        //System.out.println(x);
        int N = 13; //table size
        int pos = ( (x^2) % N );
        return pos;
    }
}
