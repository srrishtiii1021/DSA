class Solution {
    public int minMoves(int[] nums) {
        int min = nums[0], sum = 0;

        for (int x : nums) {
            min = Math.min(min, x);
            sum += x;
        }

        return sum - min * nums.length;
    }
}