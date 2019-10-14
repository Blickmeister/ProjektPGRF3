package uloha1;


import lvl2advanced.p01gui.p01simple.AbstractRenderer;
import lwjglutils.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import transforms.*;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 *
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer{

	double ox, oy;
	boolean mouseButton1 = false;

	OGLBuffers buffers;

	int shaderProgram, locMat, locHeight;

	int width, height;

	// The window handle
	private long window;


	int  locMath;

	float time = 0;
	Mat4 mvp = new Mat4Identity();


	Camera cam = new Camera();
	Mat4 proj = new Mat4PerspRH(Math.PI / 4, 1, 0.01, 1000.0);

	OGLTexture2D.Viewer textureViewer;

	private GLFWKeyCallback   keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			if (action == GLFW_PRESS || action == GLFW_REPEAT){
				switch (key) {
					case GLFW_KEY_W:
						cam = cam.forward(1);
						break;
					case GLFW_KEY_D:
						cam = cam.right(1);
						break;
					case GLFW_KEY_S:
						cam = cam.backward(1);
						break;
					case GLFW_KEY_A:
						cam = cam.left(1);
						break;
					case GLFW_KEY_LEFT_CONTROL:
						cam = cam.down(1);
						break;
					case GLFW_KEY_LEFT_SHIFT:
						cam = cam.up(1);
						break;
					case GLFW_KEY_SPACE:
						cam = cam.withFirstPerson(!cam.getFirstPerson());
						break;
					case GLFW_KEY_R:
						cam = cam.mulRadius(0.9f);
						break;
					case GLFW_KEY_F:
						cam = cam.mulRadius(1.1f);
						break;
				}
			}
		}
	};

	private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
		@Override
		public void invoke(long window, int w, int h) {
			if (w > 0 && h > 0 &&
					(w != width || h != height)) {
				width = w;
				height = h;
				proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
				if (textRenderer != null)
					textRenderer.resize(width, height);
			}
		}
	};

	private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback () {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

			if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
				mouseButton1 = true;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				ox = xBuffer.get(0);
				oy = yBuffer.get(0);
			}

			if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE){
				mouseButton1 = false;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				double x = xBuffer.get(0);
				double y = yBuffer.get(0);
				cam = cam.addAzimuth((double) Math.PI * (ox - x) / width)
						.addZenith((double) Math.PI * (oy - y) / width);
				ox = x;
				oy = y;
			}
		}
	};

	private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
		@Override
		public void invoke(long window, double x, double y) {
			if (mouseButton1) {
				cam = cam.addAzimuth((double) Math.PI * (ox - x) / width)
						.addZenith((double) Math.PI * (oy - y) / width);
				ox = x;
				oy = y;
			}
		}
	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		@Override
		public void invoke(long window, double dx, double dy) {
			if (dy < 0)
				cam = cam.mulRadius(0.9f);
			else
				cam = cam.mulRadius(1.1f);

		}
	};

	@Override
	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	@Override
	public GLFWWindowSizeCallback getWsCallback() {
		return wsCallback;
	}

	@Override
	public GLFWMouseButtonCallback getMouseCallback() {
		return mbCallback;
	}

	@Override
	public GLFWCursorPosCallback getCursorCallback() {
		return cpCallbacknew;
	}

	@Override
	public GLFWScrollCallback getScrollCallback() {
		return scrollCallback;
	}


	void createBuffers() {

		BufferGenerator buf = new BufferGenerator();

		int m = 10;
		int n = 10;

		buf.createVertexBuffer(m, n);
		buf.createIndexBuffer(m , n);

		float[] vertexBufferData = buf.getVertexBufferData();
		int[] indexBufferData = buf.getIndexBufferData();

        /*for(int j = 0; j < vertexBufferData.length; j+=8) {
            for (int i = j; i < m * 2 + j; i++) {
                System.out.print(vertexBufferData[i] + "  ");
            }
            System.out.println();
            System.out.println();
        }*/

		for(int i = 0; i < indexBufferData.length; i++){
			System.out.print(indexBufferData[i] + "  ");
		}

		// vertex binding description, concise version
		OGLBuffers.Attrib[] attributes = {
				new OGLBuffers.Attrib("inPosition", 2), // 2 floats
				//new OGLBuffers.Attrib("inColor", 3) // 3 floats
		};
		buffers = new OGLBuffers(vertexBufferData, attributes,
				indexBufferData);
	}

	@Override
	public void init() {
		OGLUtils.printOGLparameters();
		OGLUtils.printLWJLparameters();
		OGLUtils.printJAVAparameters();

		// Set the clear color
		glClearColor(0.8f, 0.8f, 0.8f, 1.0f);

		createBuffers();

		shaderProgram = ShaderUtils.loadProgram("/uloha1/start.vert",
				"/lvl1basic/p01start/start.frag",
				null,null,null,null);

		// Shader program set
		glUseProgram(this.shaderProgram);

		// internal OpenGL ID of a shader uniform (constant during one draw call
		// - constant value for all processed vertices or pixels) variable
		//locTime = glGetUniformLocation(shaderProgram, "time");
		locMath = glGetUniformLocation(shaderProgram, "MVP");
		cam = cam.withPosition(new Vec3D(5, 5, 2.5))
				.withAzimuth(Math.PI * 1.25)
				.withZenith(Math.PI * -0.125);
	}

	@Override
	public void display() {
		glViewport(0, 0, width, height);
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		// set the current shader to be used, could have been done only once (in
		// init) in this sample (only one shader used)
		glUseProgram(shaderProgram);
		// to use the default shader of the "fixed pipeline", call
		// glUseProgram(0);
		time += 0.01;
		glUniformMatrix4fv(locMath, false, new Mat4RotX(time).mul(cam.getViewMatrix().mul(proj)).floatArray());
		//glUniform1f(locTime, time); // correct shader must be set before this

		// bind and draw
		//buffers.draw(GL_TRIANGLES, shaderProgram);

		glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
		// bind and draw
		buffers.draw(GL_TRIANGLES, shaderProgram);


	}
}