package uloha1;

import lwjglutils.OGLBuffers;

public class BufferGenerator {

    float[] vertexBufferData = {};
    short[] indexBufferData = {};

    public float[] getVertexBufferData() {
        return vertexBufferData;
    }

    public short[] getIndexBufferData() {
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


    /*for(int i = 0; i < n; i++)
    {
     for(int j = 0; j < m; j++) {

     }
    }*/


    public void createIndexBuffer(int m, int n) {
        // 0,5,6,0,6,1,1,6,7,1,7,2,2,7,8,2,8,3,3,8,9,3,9,4....

        indexBufferData = new short[6*m*m];
        int i = 0;
        short k;
        for(int y = 0; y < n; y++) {
            for(int x = 0; x < m ; x++) {
                k = (short) (x + y*(m+1));
                indexBufferData[i++] = k;
                indexBufferData[i++] = (short) (k + (m+1));
                indexBufferData[i++] = (short) (k + (m+1)+1);
                indexBufferData[i++] = k;
                indexBufferData[i++] = (short) (k + (m+1)+1);
                indexBufferData[i++] = (short) (k + 1);
            }

        }
    }
}
