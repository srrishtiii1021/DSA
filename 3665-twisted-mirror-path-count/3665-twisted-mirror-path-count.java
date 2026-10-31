class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int mod = 1000000007;
        int[] dp = new int[n];
        dp[0] = 1;

        for (int i = 0; i < m; i++) {
            int left = 0;

            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int temp = dp[j];
                    dp[j] = left;
                    left = temp;
                } else {
                    left = (left + dp[j]) % mod;
                    dp[j] = left;
                }
            }
        }

        return dp[n - 1];
    }
}