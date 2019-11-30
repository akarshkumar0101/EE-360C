
public class Program3 {
    public int maxCoinValue (int[] cities) {
        // Implement your dynamic programming algorithm here
        // You may use helper functions as needed



        return 0;
    }
}
class Solution{
    private final int cities[];
    private final int maxCoins[][];
    private final int length;

    public Solution(int[] cities){
        this.cities = cities;
        length = cities.length;
        maxCoins = new int[length][length];
        for(int l=0;l<length;l++){
            for(int r=0;r<length;r++){
                maxCoins[l][r] = -1;
            }
        }
    }
    public void solve(){

    }
    public int getSolution(){

        return 0;
    }

}


