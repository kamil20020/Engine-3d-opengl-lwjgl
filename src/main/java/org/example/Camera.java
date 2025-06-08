package org.example;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Camera {

    private final Vector3f angle;
    private final Vector3f eye;
    private final Vector3f destination;
    private final Vector3f top;

    public Camera(Vector3f position, EventsHandler eventsHandler){

        this.eye = new Vector3f(position);
        this.angle = new Vector3f(0, 90, 0);
        this.top = new Vector3f(0, 1, 0);
        this.destination = new Vector3f(0, 0, 0);

        eventsHandler.addKeyboardCallback(this::moveCallback);

        updateDestination();
    }

    public void moveCallback(int key, int action){

        if(key == GLFW_KEY_W && action == GLFW_PRESS){

            moveInForward(10);
        }
        else if(key == GLFW_KEY_S && action == GLFW.GLFW_PRESS){

            moveInForward(-10);
        }
        else if(key == GLFW_KEY_A && action == GLFW.GLFW_PRESS){

            angle.y -= 1;
            updateDestination();
        }
        else if(key == GLFW_KEY_D && action == GLFW.GLFW_PRESS){

            angle.y += 1;
            updateDestination();
        }
        else if(key == GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS){

            eye.y += 10;
        }
        else if(key == GLFW_KEY_Z && action == GLFW.GLFW_PRESS){

            eye.y -= 10;
        }

        System.out.println("eye: " + eye);
        System.out.println("dest: " + destination);
    }

    private void moveInForward(int scale){

        Vector3f forward = new Vector3f(destination).sub(eye);

        if(forward.lengthSquared() == 0){

            System.out.println("Vector " + forward + " have length 0");
        }

        forward.normalize();

        eye.x += scale * forward.x;
        eye.y += scale * forward.y;
        eye.z += scale * forward.z;
    }

    private void updateDestination(){

        Vector3f direction = new Vector3f();

        direction.x = (float) (Math.cos(Math.toRadians(angle.x)) * Math.cos(Math.toRadians(angle.y)));
        direction.y = (float) (Math.sin(Math.toRadians(angle.x)));
        direction.z = (float) (Math.cos(Math.toRadians(angle.x)) * Math.sin(Math.toRadians(angle.y)));

        if(direction.lengthSquared() == 0){

            direction.set(0, 0, -1);
        }
        else{
            direction.normalize();
        }

        destination.set(eye).add(new Vector3f(direction).mul(50));
    }

    public void update(){

        Matrix4f view = new Matrix4f().lookAt(
            eye, destination, top
        );

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        view.get(fb);

        glLoadMatrixf(fb);
    }

}
