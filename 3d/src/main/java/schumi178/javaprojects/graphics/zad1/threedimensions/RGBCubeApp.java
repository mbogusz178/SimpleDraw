package schumi178.javaprojects.graphics.zad1.threedimensions;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RGBCubeApp {

    private float rotateX = 30;
    private float rotateY = 30;
    private float rotateZ = 30;

    private float rotateXStep = 0.0f;
    private float rotateYStep = 0.0f;
    private float rotateZStep = 0.0f;

    private final int vertexSize = 3;
    private final int colorSize = 3;
    private final int vertices = 36;
    private long window;
    private int vboColorHandle;
    private int vboVertexHandle;

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(300, 300, "Kostka RGB", NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            else if(action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_UP:
                        rotateXStep = 1.0f;
                        break;
                    case GLFW_KEY_DOWN:
                        rotateXStep = -1.0f;
                        break;
                    case GLFW_KEY_LEFT:
                        rotateYStep = 1.0f;
                        break;
                    case GLFW_KEY_RIGHT:
                        rotateYStep = -1.0f;
                        break;
                    default:
                        break;
                }
            } else if(action == GLFW_RELEASE){
                switch (key) {
                    case GLFW_KEY_UP:
                    case GLFW_KEY_DOWN:
                        rotateXStep = 0.0f;
                        break;
                    case GLFW_KEY_LEFT:
                    case GLFW_KEY_RIGHT:
                        rotateYStep = 0.0f;
                        break;
                    default:
                        break;
                }
            }
        });

        try(MemoryStack memoryStack = stackPush()) {
            IntBuffer pWidth = memoryStack.mallocInt(1);
            IntBuffer pHeight = memoryStack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        }

        // Z = -1

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertices * vertexSize);
        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {1f, -1f, -1f});
        vertexData.put(new float[] {1f, 1f, -1f});
        vertexData.put(new float[] {1f, 1f, -1f});
        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {-1f, 1f, -1f});

        FloatBuffer colorData = MemoryUtil.memAllocFloat(vertices * colorSize);
        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {1f, 0f, 0f});
        colorData.put(new float[] {1f, 1f, 0f});
        colorData.put(new float[] {1f, 1f, 0f});
        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {0f, 1f, 0f});

        // Z = +1

        vertexData.put(new float[] {-1f, -1f, 1f});
        vertexData.put(new float[] {1f, -1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {-1f, -1f, 1f});
        vertexData.put(new float[] {-1f, 1f, 1f});

        colorData.put(new float[] {0f, 0f, 1f});
        colorData.put(new float[] {1f, 0f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {0f, 0f, 1f});
        colorData.put(new float[] {0f, 1f, 1f});

        // X = -1

        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {-1f, -1f, 1f});
        vertexData.put(new float[] {-1f, 1f, 1f});
        vertexData.put(new float[] {-1f, 1f, 1f});
        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {-1f, 1f, -1f});

        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {0f, 0f, 1f});
        colorData.put(new float[] {0f, 1f, 1f});
        colorData.put(new float[] {0f, 1f, 1f});
        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {0f, 1f, 0f});

        // X = +1

        vertexData.put(new float[] {1f, -1f, -1f});
        vertexData.put(new float[] {1f, -1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {1f, -1f, -1f});
        vertexData.put(new float[] {1f, 1f, -1f});

        colorData.put(new float[] {1f, 0f, 0f});
        colorData.put(new float[] {1f, 0f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {1f, 0f, 0f});
        colorData.put(new float[] {1f, 1f, 0f});

        // Y = -1

        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {-1f, -1f, 1f});
        vertexData.put(new float[] {1f, -1f, 1f});
        vertexData.put(new float[] {1f, -1f, 1f});
        vertexData.put(new float[] {-1f, -1f, -1f});
        vertexData.put(new float[] {1f, -1f, -1f});

        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {0f, 0f, 1f});
        colorData.put(new float[] {1f, 0f, 1f});
        colorData.put(new float[] {1f, 0f, 1f});
        colorData.put(new float[] {0f, 0f, 0f});
        colorData.put(new float[] {1f, 0f, 0f});

        // Y = +1

        vertexData.put(new float[] {-1f, 1f, -1f});
        vertexData.put(new float[] {-1f, 1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {1f, 1f, 1f});
        vertexData.put(new float[] {-1f, 1f, -1f});
        vertexData.put(new float[] {1f, 1f, -1f});

        colorData.put(new float[] {0f, 1f, 0f});
        colorData.put(new float[] {0f, 1f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {1f, 1f, 1f});
        colorData.put(new float[] {0f, 1f, 0f});
        colorData.put(new float[] {1f, 1f, 0f});

        vertexData.flip();
        colorData.flip();

        vboVertexHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vboColorHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private void loop() {
        glEnable(GL_DEPTH_TEST);
        float rotAngle = 0.1f;

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glPushMatrix();

            glMatrixMode(GL_PROJECTION);
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), 1.0f, 0.01f, 100.0f).get(stack.mallocFloat(16));
                glLoadMatrixf(projection);
            }

            glMatrixMode(GL_MODELVIEW);
            GL11.glTranslatef(0.0f, 0.0f, -5.0f);
            GL11.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(rotateY, 0.0f, 1.0f, 0.0f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

            glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
            glVertexPointer(vertexSize, GL_FLOAT, 0, 0L);

            glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
            glColorPointer(colorSize, GL_FLOAT, 0, 0L);

            glEnableClientState(GL_VERTEX_ARRAY);
            glEnableClientState(GL_COLOR_ARRAY);

            glDrawArrays(GL_TRIANGLES, 0, vertices);

            glDisableClientState(GL_COLOR_ARRAY);
            glDisableClientState(GL_VERTEX_ARRAY);

            glPopMatrix();

            rotateX += rotateXStep;
            rotateY += rotateYStep;

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
