import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;

/**
 *  A viewer for the polyhedral models defined in Polyhedron.java.
 *  The user can select the model and can control some aspects of the
 *  display.  If a model does not already have colors for its faces,
 *  then random colors are assigned.  The user can drag the polyhedron
 *  to rotate the view.
 */
public class Java3D extends GLJPanel implements GLEventListener, KeyListener  {

    /**
     * A main routine to create and show a window that contains a
     * panel of type IFSPolyhedronViewer.  The program ends when the
     * user closes the window.
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("Java3D -- TRANSFORM WITH KEYS");
        Java3D panel = new Java3D();
        window.setContentPane(panel);
        window.setJMenuBar(panel.createMenuBar());
        window.pack();
        window.setLocation(50,50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        panel.requestFocusInWindow();
    }

    public Java3D() {
        super( new GLCapabilities(null) ); // Makes a panel with default OpenGL "capabilities".
        setPreferredSize( new Dimension(640,480) );
        addGLEventListener(this);
        addKeyListener(this);
    }

    //-------------------- methods to draw the cube ----------------------

    private Camera camera;
    private Polyhedron currentModel;

    private JRadioButtonMenuItem orthographic, drawEdges, drawFaces, drawBoth, coloredFaces;

    //-------------------- GLEventListener Methods -------------------------

    /**
     * Draw the current model, with display options determined by the radio button menu items.
     */
    double scaleX;
    double scaleY;
    double scaleZ;
    double translateX = 0;
    double translateY = 0;
    double translateZ = 0;
    double colorR = 1;
    double colorG = 1;
    double colorB = 1;
    
    public void display(GLAutoDrawable drawable) {    

        GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.

        
        gl2.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        camera.setOrthographic(orthographic.isSelected());
        camera.apply(gl2);
        gl2.glPushMatrix();
        gl2.glTranslated(translateX,translateY,translateZ);
        
        double scale = 1.0/currentModel.maxVertexLength;
        if (scaleX == 0) {
            scaleX = scale;
        }
        if (scaleY == 0) {
            scaleY = scale;
        }
        if (scaleZ == 0) {
            scaleZ = scale;
        }
        gl2.glScaled(scaleX,scaleY,scaleZ);  // scale to fit nicely in window

        int i,j;
        if (drawFaces.isSelected() || drawBoth.isSelected()) {
            if (drawBoth.isSelected()) {
                gl2.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
            }
            gl2.glColor3d(colorR,colorG,colorB); // in case colored is false
            for (i = 0; i < currentModel.faces.length; i++) {
                gl2.glBegin(GL2.GL_TRIANGLE_FAN);
                for (j = 0; j < currentModel.faces[i].length; j++) {
                    int vertexNum = currentModel.faces[i][j];
                    gl2.glVertex3dv( currentModel.vertices[vertexNum], 0 );
                }
                gl2.glEnd();
            }
        }
        gl2.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
        if (drawEdges.isSelected() || drawBoth.isSelected()) {
            if (drawBoth.isSelected()) {
                gl2.glColor3f(0,0,0);
            }
            else {
                gl2.glColor3f(1,1,1);
            }
            for (i = 0; i < currentModel.faces.length; i++) {
                gl2.glBegin(GL2.GL_LINE_LOOP);
                for (j = 0; j < currentModel.faces[i].length; j++) {
                    int vertexNum = currentModel.faces[i][j];
                    gl2.glVertex3dv( currentModel.vertices[vertexNum], 0 );
                }
                gl2.glEnd();
            }
        }
        gl2.glPopMatrix();

    } // end display()

    public void init(GLAutoDrawable drawable) {
        // called when the panel is created
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClearColor( 0, 0, 0, 1 );
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        
        gl2.glLineWidth(2);
        gl2.glPolygonOffset(1,2);
        camera = new Camera();
        camera.lookAt(2,2,6, 0,0,0, 0,1,0);
        camera.setScale(3);
        camera.installTrackball(this);
        currentModel = Polyhedron.octocone;
    }

    public void dispose(GLAutoDrawable drawable) {
        // called when the panel is being disposed
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // called when user resizes the window
    }

     // ----------------  Methods from the KeyListener interface --------------

    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        if ( key == KeyEvent.VK_LEFT )
            scaleY -= .1;
        else if ( key == KeyEvent.VK_RIGHT )
            scaleY += .1;
        else if ( key == KeyEvent.VK_DOWN)
            scaleX += .1;
        else if ( key == KeyEvent.VK_UP )
            scaleX -= .1;
        else if ( key == KeyEvent.VK_PAGE_UP )
            scaleZ += .1;
        else if ( key == KeyEvent.VK_PAGE_DOWN )
            scaleZ -= .1;
        else if ( key == KeyEvent.VK_HOME )
            scaleX = scaleY = scaleZ = 1;
        
        else if ( key == KeyEvent.VK_NUMPAD1 )
            colorR -= 0.1;
        else if ( key == KeyEvent.VK_NUMPAD2 )
            colorG -= 0.1;
        else if ( key == KeyEvent.VK_NUMPAD3 )
            colorB -= 0.1;
        else if ( key == KeyEvent.VK_NUMPAD4 )
            colorR = 0.5;
        else if ( key == KeyEvent.VK_NUMPAD5 )
            colorG = 0.5;
        else if ( key == KeyEvent.VK_NUMPAD6 )
            colorB = 0.5;
        else if ( key == KeyEvent.VK_NUMPAD7 )
            colorR += 0.1;
        else if ( key == KeyEvent.VK_NUMPAD8 )
            colorG += 0.1;
        else if ( key == KeyEvent.VK_NUMPAD9 )
            colorB += 0.1;
        
        else if ( key == KeyEvent.VK_Q )
            translateX += 0.1;
        else if ( key == KeyEvent.VK_W )
            translateY += 0.1;
        else if ( key == KeyEvent.VK_E )
            translateZ += 0.1;
        else if ( key == KeyEvent.VK_A )
            translateX -= 0.1;
        else if ( key == KeyEvent.VK_S )
            translateY -= 0.1;
        else if ( key == KeyEvent.VK_D )
            translateZ -= 0.1;
        else if ( key == KeyEvent.VK_R )
            translateX = translateY = translateZ = 0;

        repaint();
    }

    public void keyReleased(KeyEvent evt) {
    }
    
    public void keyTyped(KeyEvent evt) {
    }

    
    // ---------------------- Menu bar ------------------------------------------------

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu model = new JMenu("Shapes");
        JMenu render = new JMenu("Render Options");
        menuBar.add(model);
        menuBar.add(render);
        
        ActionListener repainter = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        };

        JRadioButtonMenuItem[] items;
        items = createRadioMenuGroup(new String[] {"White Faces", "Customize Faces"}, render, repainter);
        coloredFaces = items[0];
        coloredFaces.setSelected(true);
        render.addSeparator();
        items = createRadioMenuGroup(new String[] {"Draw Faces Only", "Draw Edges Only", "Draw Both"}, 
                                                            render, repainter);
        drawFaces = items[0];
        drawEdges = items[1];
        drawBoth = items[2];
        drawBoth.setSelected(true);
        render.addSeparator();
        items = createRadioMenuGroup(new String[] {"Perspective Projection", "Orthographics Projection"}, render, repainter);
        orthographic = items[1];
        items[0].setSelected(true);

        items = createRadioMenuGroup(new String[] {
                "Octocone",
                "Dome",
                "Square Donut",
                "Hourglass",
                "Letter X",
                "Letter V"
        }, 
        model,
        new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                switch (evt.getActionCommand()) {
                case "Octocone": currentModel = Polyhedron.octocone; break;
                case "Dome": currentModel = Polyhedron.dome; break;
                case "Square Donut": currentModel = Polyhedron.squaredonut; break;
                case "Hourglass": currentModel = Polyhedron.hourglass; break;
                case "Letter X": currentModel = Polyhedron.letterX; break;
                case "Letter V": currentModel = Polyhedron.letterV; break;
                }
                camera.lookAt(2,2,6, 0,0,0, 0,1,0);
                repaint();
            }
        });
        items[0].setSelected(true);

        return menuBar;
    }

    private JRadioButtonMenuItem[] createRadioMenuGroup(String[] itemNames, JMenu menu, ActionListener listener) {
        JRadioButtonMenuItem[] items = new JRadioButtonMenuItem[itemNames.length];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemNames.length; i++) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(itemNames[i]);
            group.add(item);
            items[i] = item;
            menu.add(item);
            if (listener != null) {
                item.addActionListener(listener);
            }
        }
        return items;
    }

}
