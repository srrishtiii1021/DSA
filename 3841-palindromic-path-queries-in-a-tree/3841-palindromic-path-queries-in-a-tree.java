class Solution {
    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        List<Integer>[] g = new ArrayList[n];

        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<>();

        for (int[] e : edges) {
            g[e[0]].add(e[1]);
            g[e[1]].add(e[0]);
        }

        int log = 1;
        while ((1 << log) <= n)
            log++;

        int[][] up = new int[n][log];
        int[] par = new int[n];
        int[] dep = new int[n];
        int[] tin = new int[n];
        int[] tout = new int[n];
        int[] base = new int[n];
        int[] idx = new int[n];
        int[] st = new int[n];

        Arrays.fill(par, -1);

        int top = 0, timer = 0;
        st[0] = 0;
        par[0] = 0;
        tin[0] = timer++;
        base[0] = 1 << (s.charAt(0) - 'a');

        while (top >= 0) {
            int u = st[top];

            if (idx[u] < g[u].size()) {
                int v = g[u].get(idx[u]++);

                if (v == par[u])
                    continue;

                par[v] = u;
                dep[v] = dep[u] + 1;
                up[v][0] = u;

                for (int j = 1; j < log; j++)
                    up[v][j] = up[up[v][j - 1]][j - 1];

                base[v] = base[u] ^ (1 << (s.charAt(v) - 'a'));
                tin[v] = timer++;
                st[++top] = v;
            } else {
                tout[u] = timer;
                top--;
            }
        }

        Fenwick bit = new Fenwick(n);
        char[] ch = s.toCharArray();
        List<Boolean> ans = new ArrayList<>();

        for (String q : queries) {
            String[] a = q.split(" ");
            int u = Integer.parseInt(a[1]);

            if (a[0].equals("update")) {
                int old = 1 << (ch[u] - 'a');
                int nw = 1 << (a[2].charAt(0) - 'a');
                int delta = old ^ nw;

                bit.range(tin[u], tout[u] - 1, delta, n);
                ch[u] = a[2].charAt(0);
            } else {
                int v = Integer.parseInt(a[2]);
                int w = lca(u, v, up, dep, log);

                int x = (base[u] ^ bit.sum(tin[u]))
                      ^ (base[v] ^ bit.sum(tin[v]))
                      ^ (1 << (ch[w] - 'a'));

                ans.add((x & (x - 1)) == 0);
            }
        }

        return ans;
    }

    int lca(int u, int v, int[][] up, int[] dep, int log) {
        if (dep[u] < dep[v]) {
            int t = u;
            u = v;
            v = t;
        }

        int d = dep[u] - dep[v];

        for (int j = 0; j < log; j++) {
            if ((d & (1 << j)) != 0)
                u = up[u][j];
        }

        if (u == v)
            return u;

        for (int j = log - 1; j >= 0; j--) {
            if (up[u][j] != up[v][j]) {
                u = up[u][j];
                v = up[v][j];
            }
        }

        return up[u][0];
    }

    class Fenwick {
        int n;
        int[] bit;

        Fenwick(int n) {
            this.n = n;
            bit = new int[n + 1];
        }

        void add(int i, int x) {
            for (i++; i <= n; i += i & -i)
                bit[i] ^= x;
        }

        int sum(int i) {
            int res = 0;

            for (i++; i > 0; i -= i & -i)
                res ^= bit[i];

            return res;
        }

        void range(int l, int r, int x, int n) {
            if (l > r)
                return;
                
            add(l, x);
            if (r + 1 < n)
                add(r + 1, x);
        }
    }
}