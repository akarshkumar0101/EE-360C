
public class Program3 {
    public int maxCoinValue (int[] cities) {
        // Implement your dynamic programming algorithm here
        // You may use helper functions as needed
        Solution solution = new Solution(cities);
        return solution.solve();
    }
}
class Solution {
    //using ints is justified because the domains are as follows:
    //max number of cities = 2500
    //max value per city = 30000
    //this results in a max amount of coins acquired: 75,000,000
    //this bound is well within the limits of integer values (Integer.MAX_VALUE = 2147483647)

    //take subsets with left (inc) and right(exc)
    private final int cities[];
    private final int citySums[][];


    private final int maxCoins[][];
//    private final int minCoins[][];
    private final int length;


    private final int[][] MAX;
    //private final int[][] MIN;

    private static final int MAX_WORST = Integer.MIN_VALUE;
    private static final int MIN_WORST = Integer.MAX_VALUE;

    public Solution(int[] cities) {
        this.cities = cities;
        length = cities.length;
        maxCoins = new int[length+1][length+1];
//        minCoins = new int[length+1][length+1];
        for(int left=0;left<length+1;left++){
            for(int right=0;right<length+1;right++){
                maxCoins[left][right] = MAX_WORST;
//                minCoins[left][right] = MIN_WORST;
            }
        }
        citySums = new int[length+1][length+1];

        MAX = new int[length+1][length+1];
    }
    public int solve(){
        findSums();

        int initCollectGold = citySums[0][length];
//        return maximize(0,length) - initCollectGold;
        return alphaBetaMax(0,0,length, MIN_WORST) - initCollectGold;
//        return solveIterative() - initCollectGold;
    }

    private int maximize(int left, int right){
        if(right<left){
            throw new IllegalArgumentException("Invalid left and right values for the given city.");
        }

        int collectedGold = citySums[left][right];

        //no cities left
        if(right-left==0){
            return 0;
        }
        //one city left
        if(right-left==1){
            return collectedGold;
        }

        //if not computed yet
        if(maxCoins[left][right]==MAX_WORST){
            int maxVal = MAX_WORST;

            //wall variable is the index directly left of wall
            //bounds: wall can be left, wall can be right-2 (because right-1 is the rightmost index)
            for(int wall=left;wall<right-1;wall++){
                //put wall at the wall index
                int calcVal = minimize(left, right, wall);
                maxVal = Math.max(maxVal, calcVal);
            }
            maxCoins[left][right] = maxVal+collectedGold;
        }

        return maxCoins[left][right];
    }
    private int minimize(int left, int right, int wall){
        //either attack from the west or the east
        int minCoins = MIN_WORST;

        //attack from the west
        {
            int coins = maximize(wall+1,right);
            minCoins = Math.min(minCoins, coins);
        }
        //attack from the east
        {
            int coins = maximize(left,wall+1);
            minCoins = Math.min(minCoins, coins);
        }
        return minCoins;
    }


    private void findSums(){
        int overallSum = 0;
        for(int i=0;i <length;i++){
            overallSum += cities[i];
        }

        int subarrayOverallSum = overallSum;
        for(int left=0;left<length;left++){
            //subarrayOverallSum is the sum from left to length;
            int subarraysum = subarrayOverallSum;
            for(int right=length;left<=right;right--){
                //subarraysum is the sum from left to right
                citySums[left][right] = subarraysum;
                if(right-1>=0) {
                    subarraysum -= cities[right - 1];
                }
            }
            subarrayOverallSum-=cities[left];
        }
    }


    //alpha is the minimum score king is guaranteed of
    //beta is the maximum score the enemy is guaranteed of
    private int alphaBetaMax(int depth, int left, int right, int minValSoFar){
        if(right<left){
            throw new IllegalArgumentException("Invalid left and right values for the given city.");
        }

        int collectedGold = citySums[left][right];

        //no cities left
        if(right-left==0){
            return 0;
        }
        //one city left
        if(right-left==1){
            return collectedGold;
        }

        //parent call is minimizing. The beta value passed in is the "best value" the parent has seen so far.
        //if our values ever becomes greater than the beta it has seen (we are going bigger and bigger from here),
        //then our values will not be considered by the parent, not worth it for him.
        ///we are maximizing the child nodes.

        //if not computed yet
        if(maxCoins[left][right]==MAX_WORST){
            int maxValSoFar = MAX_WORST;

            //wall variable is the index directly left of wall
            //bounds: wall can be left, wall can be right-2 (because right-1 is the rightmost index)

//            for(int wall=left;wall<right-1;wall++){
            for(int wall=right-2;wall>=left;wall--){
                //put wall at the wall index
                int childVal = alphaBetaMin(depth+1, left, right, wall, maxValSoFar);
                maxValSoFar = Math.max(maxValSoFar, childVal);

                if(maxValSoFar>=minValSoFar){
                    //then our nodes don't matter.

                    return collectedGold+maxValSoFar;
                }
            }
            maxCoins[left][right] = collectedGold+maxValSoFar;
        }

        return maxCoins[left][right];
    }
    private int alphaBetaMin(int depth, int left, int right, int wall, int maxValSoFar){
        //either attack from the west or the east
        //this nodes value
        int minValSoFar = MIN_WORST;

        //parent call is maximizing. The alpha value passed in is the "best value" the parent has seen so far.
        //if our values ever becomes less than the alpha it has seen (we are going lesser and lesser from here),
        //then our values will not be considered by the parent, not worth it for him.
        ///we are minimizing the child nodes.


        //attack from the east
        {
            int childVal = alphaBetaMax(depth+1, left,wall+1, minValSoFar);
            minValSoFar = Math.min(minValSoFar, childVal);

            if(maxValSoFar>=minValSoFar){
                return minValSoFar;
            }
        }

        //attack from the west
        {
            int childVal = alphaBetaMax(depth+1, wall+1,right, minValSoFar);
            minValSoFar = Math.min(minValSoFar, childVal);

            if(maxValSoFar>=minValSoFar){
                return minValSoFar;
            }
        }

        return minValSoFar;
    }

    private int solveIterative(){
        for(int right=0;right<=length;right++){
            for(int left = right;left>=0;left--){
                if(left>right){
                    throw new RuntimeException("nope nope nope");
                }
                //no cities left
                if(right-left==0){
                    MAX[left][right] = 0;
                }
                //one city left
                else if(right-left==1){
                    MAX[left][right] = citySums[left][right];
                }
                else {
                    MAX[left][right] = getVal(left, right);
                }
            }
        }
        return MAX[0][length];
    }
    private int getVal(int left, int right){
        int maxVal = MAX_WORST;
        for(int wall=left;wall<right-1;wall++){

//            int childVal = alphaBetaMax(depth+1, left,wall+1, true, minValSoFar);
//            int childVal = alphaBetaMax(depth+1, wall+1,right, true, minValSoFar);

            int westattack = 0, eastattack = 0;
            eastattack = MAX[left][wall+1];
            westattack = MAX[wall+1][right];

            int minVal = Math.min(westattack, eastattack);
            maxVal = Math.max(maxVal, minVal);
        }
        return maxVal+citySums[left][right];
    }




}


