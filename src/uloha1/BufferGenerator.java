package uloha1;

public class BufferGenerator {

    float[] vertexBufferData = {};
    int[] indexBufferData = {};

    public float[] getVertexBufferData() {
        return vertexBufferData;
    }

    public int[] getIndexBufferData() {
        return indexBufferData;
    }

    public BufferGenerator() {
    }

    public void createVertexBuffer(int m, int n) {

        vertexBufferData = new float[m*n*2];
        //vertexBufferData[0] = 0;

        float interval = (float) 1/(m-1);

        float x = 0;
        float y = 0;

        for(int i = 0; i < m*n*2; i+=m*2) {
            for(int j = i; j < n*2+i-1; j+=2) {
                vertexBufferData[j] = x;
                vertexBufferData[j+1] = y;
                x+=interval;
            }
            y+=interval;
            x = 0;
        }
    }

    public void createIndexBuffer(int m, int n) {
        // 0,5,6,0,6,1,1,6,7,1,7,2,2,7,8,2,8,3,3,8,9,3,9,4....

        /*indexBufferData = new int[6*m*m];
        int i = 0;
        int k;
        for(int y = 0; y < n; y++) {
            for(int x = 0; x < m ; x++) {
                k = x + y*(m+1);
                indexBufferData[i++] = k;
                indexBufferData[i++] = k + (m+1);
                indexBufferData[i++] = k + (m+1)+1;
                indexBufferData[i++] = k;
                indexBufferData[i++] = k + (m+1)+1;
                indexBufferData[i++] = k + 1;
            }

        }*/
        indexBufferData = new int[6*(m-1)*(n-1)];
        int i = 0;
        int k;
        for(int y = 0; y < n - 1; y++) {
            for(int x = 0; x < m - 1; x++) {
                k = x + y*m;
                indexBufferData[i++] = k;
                indexBufferData[i++] = k + 1;
                indexBufferData[i++] = k + (m+1);
                indexBufferData[i++] = k;
                indexBufferData[i++] = k + (m+1);
                indexBufferData[i++] = k + m;
            }
        }
    }
}
