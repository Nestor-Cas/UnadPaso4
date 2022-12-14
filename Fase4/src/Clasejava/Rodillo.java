
package Clasejava;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


public class Rodillo extends Applet{
    private SimpleUniverse universo = null; //se inicializa el universo
    private Canvas3D lienzo = null; //se inicializa el lienzo donde se mostrara el escenario
    
    public Rodillo(){
        setLayout(new BorderLayout()); //definicion de acomodacion de los elementos en la ventana
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        lienzo = new Canvas3D(gc); 
        add("Center",lienzo); //se ubica el elemento en el centro
        universo = new SimpleUniverse(lienzo); //creacion del universo se envia como parametro el lienzo
        BranchGroup escena = escenario(); //se crea la escena y luego se adiciona al universo
        universo.addBranchGraph(escena);
    }
    
    private BranchGroup escenario(){
        BranchGroup objetoRaiz = new BranchGroup();
        //como el boundingbox pero es esfera
        BoundingSphere fronteras = new BoundingSphere(new Point3d(0,0,0),200); //se definen las fronteras del escenario
        TextureLoader archivo = new TextureLoader("src\\Fondojava\\fondorodillo.jpg",this); //se carga la imagen del fondo
        Background backg = new Background(archivo.getImage()); //se coloca el fondo en el escanerio
        backg.setApplicationBounds(fronteras);
        objetoRaiz.addChild(backg);
        objetoRaiz.addChild(rodillo()); //se llama al metodo modelar objeto
        objetoRaiz.addChild(iluminar());
        return objetoRaiz;
    }
    private BranchGroup rodillo(){
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupo = new TransformGroup();
        Transform3D transformacion3d = new Transform3D();
        transformacion3d.setTranslation(new Vector3d(-0.19,0.14,-5.27)); //define posicion del objeto
        transformacion3d.setScale(0.20); //define el tama??o del objeto
        grupo.setTransform(transformacion3d);
        TransformGroup producto = new TransformGroup();
        producto.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        producto.addChild(cargarObjeto("src\\Objeto\\rodilloss.obj")); //se llama el metodo cargarObjeto y se le envia como parametro la ruta del archivo
        
        Alpha alpha = new Alpha(-1, 10000);
        
        Transform3D eje = new Transform3D();
        eje.setRotation(new AxisAngle4f(0.3f,2.0f,-1.0f,-28.0f));
        RotationInterpolator position = new RotationInterpolator(alpha,producto,eje,10.0f,(float) Math.PI*(6.0f));
        BoundingSphere fronteras = new BoundingSphere(new Point3d(0.0,0.0,0.0),6000.0);
        position.setSchedulingBounds(fronteras);
        producto.addChild(position);
        grupo.addChild(producto);
        objetoRaiz.addChild(grupo);
        objetoRaiz.addChild(iluminar());
        objetoRaiz.compile();
        
        return objetoRaiz;
    }
    private BranchGroup cargarObjeto(String filename){
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupo = new TransformGroup();
        ObjectFile cargar = new ObjectFile();
        
        Scene s = null;
        File archivo = new java.io.File(filename); //se captura la ruta del archivo
        
        try {
           s = cargar.load(archivo.toURI().toURL()); 
        } catch(IncorrectFormatException | ParsingErrorException | FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        grupo.addChild(s.getSceneGroup());
        objetoRaiz.addChild(grupo);
        objetoRaiz.compile();
        
        
        return objetoRaiz;
    }
    private Light iluminar(){
        DirectionalLight luz = new DirectionalLight(true,new Color3f(Color.yellow),new Vector3f(-2.0f,-2.0f,-19.0f));
        luz.setInfluencingBounds(new BoundingSphere(new Point3d(),2000.0));
        return luz;
    }
    public static void main (String[] args){
        Rodillo ejecutar = new Rodillo();
        Frame ventana = new MainFrame(ejecutar,1280,720);
    }
    
}
