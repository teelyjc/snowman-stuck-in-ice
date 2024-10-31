package dev.teelyjc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import dev.teelyjc.constants.Colors;
import dev.teelyjc.domains.ShapePosition;

public class App extends JFrame {
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      new App().setVisible(true);
    });
  }

  private JPanel panel;
  private final SimpleUniverse su;

  public App() {
    this.initialComponents();
    Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

    this.su = new SimpleUniverse(c);
    this.su.getViewingPlatform().setNominalViewingTransform();
    // this.su.getViewer().getView().setMinimumFrameCycleTime();
    this.su.addBranchGraph(this.createSceneGraph());

    this.panel.add(c, BorderLayout.CENTER);
  }

  private void initialComponents() {
    this.panel = new JPanel();
    this.panel.setPreferredSize(new Dimension(500, 500));
    this.panel.setLayout(new BorderLayout());

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(this.panel, BorderLayout.CENTER);

    this.pack();
  }

  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();

    TransformGroup tg = this.getTransformGroup();

    this.applyMouseBehaviors(tg, root);
    this.applyLights(root);

    root.addChild(tg);
    root.addChild(this.getBackgroundColor());
    root.compile();
    return root;
  }

  private Background getBackgroundColor() {
    Background background = new Background();

    background.setColor(Colors.Gray);
    background.setApplicationBounds(new BoundingSphere());

    return background;
  }

  private TransformGroup getTransformGroup() {
    TransformGroup tg = new TransformGroup(new Transform3D());

    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    this.applyTransformGroupForPlatform(tg);
    this.applyTransformForSnowMan(tg);
    this.applyTransformGroupForWalls(tg);
    this.applyTransformGroupForTree(tg);
    this.applyTransformGroupForTree2(tg);
    this.applyTransformForRoad(tg);

    return tg;
  }

  public void applyMouseBehaviors(TransformGroup tg, BranchGroup bg) {
    BoundingSphere bs = new BoundingSphere(
        new Point3d(0, 0, 0), 1000);

    MouseBehavior[] mbs = {
        new MouseTranslate(),
        new MouseZoom(),
        new MouseRotate(),
    };

    for (MouseBehavior mb : mbs) {
      mb.setTransformGroup(tg);
      mb.setSchedulingBounds(bs);

      bg.addChild(mb);
    }
  }

  public void applyLights(BranchGroup bg) {
    BoundingSphere bs = new BoundingSphere(
        new Point3d(0, 0, 0), 1000);

    AmbientLight al = new AmbientLight();
    al.setInfluencingBounds(bs);
    bg.addChild(al);

    DirectionalLight dl = new DirectionalLight();
    dl.setInfluencingBounds(bs);
    bg.addChild(dl);
  }

  public void applyTransformGroupForWalls(TransformGroup tg) {
    ShapePosition[] shapePositions = {
        new ShapePosition(
            "Left",
            new Vector3f(-0.8f, 0, 0),
            new Box(0.05f, 0.8f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
        new ShapePosition(
            "Back",
            new Vector3f(0, 0.8f, 0),
            new Box(0.8f, 0.05f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
        new ShapePosition(
            "Right",
            new Vector3f(0.8f, 0, 0),
            new Box(0.05f, 0.8f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
        new ShapePosition(
            "Front",
            new Vector3f(0, -0.8f, 0),
            new Box(0.8f, 0.05f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
        new ShapePosition(
            "Floor",
            new Vector3f(0, 0, -0.8f),
            new Box(0.8f, 0.8f, 0.05f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
        new ShapePosition(
            "Ceiling",
            new Vector3f(0, 0, 0.8f),
            new Box(0.8f, 0.8f, 0.05f,
                ShapePositionManager.createAppeareance(Colors.SkyBlue, true, true, Colors.SkyBlue, 64))),
    };

    ShapePositionManager spm = new ShapePositionManager("Wall", shapePositions);
    spm.inject(tg);
  }

  public void applyTransformGroupForTree(TransformGroup tg) {
    TransformGroup treeTransformGroup = ShapePositionManager.createTransformGroupTranslation(
        new Vector3f(0, 1.75f, -0.2f));

    ShapePosition[] shapePositions = {
        new ShapePosition("Root",
            new Vector3f(0, 0, 0.2f),
            new Box(0.12f, 0.12f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.Brown, false, false, Colors.Brown, 64))),
        new ShapePosition(
            "Leaves 1",
            new Vector3f(0.3f, 0, 1.0f),
            new Sphere(0.55f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 2",
            new Vector3f(-0.3f, 0, 1.0f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 3",
            new Vector3f(0.15f, 0.25f, 1.2f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 4",
            new Vector3f(0.15f, 0.125f, 1.25f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 5",
            new Vector3f(0.25f, -0.15f, 1.4f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 6",
            new Vector3f(0.35f, -0.3f, 1.25f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 7",
            new Vector3f(0, 0, 0.8f),
            new Sphere(0.45f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
    };

    ShapePositionManager spm = new ShapePositionManager("Tree", shapePositions);

    spm.inject(treeTransformGroup);
    tg.addChild(treeTransformGroup);
  }

  public void applyTransformGroupForTree2(TransformGroup tg) {
    TransformGroup treeTransformGroup = ShapePositionManager.createTransformGroupTranslation(
        new Vector3f(0, -1.75f, -0.2f));

    ShapePosition[] shapePositions = {
        new ShapePosition("Root",
            new Vector3f(0, 0, 0.2f),
            new Box(0.12f, 0.12f, 0.8f,
                ShapePositionManager.createAppeareance(Colors.Brown, false, true, Colors.Brown, 64))),
        new ShapePosition(
            "Leaves 1",
            new Vector3f(-0.3f, 0, 1.0f),
            new Sphere(0.55f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 2",
            new Vector3f(0.3f, 0, 1.0f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 3",
            new Vector3f(-0.15f, 0.25f, 1.2f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 4",
            new Vector3f(-0.15f, 0.125f, 1.25f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 5",
            new Vector3f(-0.25f, -0.15f, 1.4f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 6",
            new Vector3f(-0.35f, -0.3f, 1.25f),
            new Sphere(0.35f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
        new ShapePosition(
            "Leaves 7",
            new Vector3f(0, 0, 0.8f),
            new Sphere(0.45f,
                ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64))),
    };

    ShapePositionManager spm = new ShapePositionManager("Tree 2", shapePositions);

    spm.inject(treeTransformGroup);
    tg.addChild(treeTransformGroup);
  }

  public void applyTransformForSnowMan(TransformGroup tg) {
    ShapePosition[] shapePositions = {
        new ShapePosition(
            "Head",
            new Vector3f(0, 0, 0.25f),
            new Sphere(0.2f, 0, ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Body",
            new Vector3f(0, 0, -0.25f),
            new Sphere(0.4f, 0, ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Left Eye",
            new Vector3f(0.125f, -0.12f, 0.35f),
            new Sphere(0.035f, 0, ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.White, 64))),
        new ShapePosition(
            "Right Eye",
            new Vector3f(0.125f, 0.12f, 0.35f),
            new Sphere(0.035f, 0, ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.White, 64))),
        new ShapePosition(
            "Buttons 0",
            new Vector3f(0.345f, 0, -0.05f),
            new Sphere(0.035f, 0, ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.White, 64))),
        new ShapePosition(
            "Buttons 1",
            new Vector3f(0.4f, 0, -0.25f),
            new Sphere(0.035f, 0, ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.White, 64))),
        new ShapePosition(
            "Buttons 2",
            new Vector3f(0.345f, 0, -0.45f),
            new Sphere(0.035f, 0, ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.White, 64)))
    };

    ShapePositionManager spm = new ShapePositionManager("Human", shapePositions);
    spm.inject(tg);
  }

  public void applyTransformGroupForPlatform(TransformGroup tg) {
    ShapePosition[] shapePositions = {
        new ShapePosition(
            "Platform",
            new Vector3f(0, 0, -0.9f),
            new Box(2, 2, 0.1f, ShapePositionManager.createAppeareance(Colors.Green, false, true, Colors.Green, 64)))
    };

    ShapePositionManager spm = new ShapePositionManager("Platform", shapePositions);
    spm.inject(tg);
  }

  public void applyTransformForRoad(TransformGroup tg) {
    ShapePosition[] shapePositions = {
        new ShapePosition(
            "Road",
            new Vector3f(1.5f, 0, -0.8f),
            new Box(0.4f, 2f, 0.02f,
                ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.Black, 64))),
        new ShapePosition(
            "Road Scratch 1",
            new Vector3f(1.5f, -0.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 2",
            new Vector3f(1.5f, -1.0f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 3",
            new Vector3f(1.5f, -1.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 4",
            new Vector3f(1.5f, 0, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 5",
            new Vector3f(1.5f, 0.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 6",
            new Vector3f(1.5f, 1.0f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Road Scratch 7",
            new Vector3f(1.5f, 1.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road",
            new Vector3f(-1.5f, 0, -0.8f),
            new Box(0.4f, 2f, 0.02f,
                ShapePositionManager.createAppeareance(Colors.Black, false, true, Colors.Black, 64))),
        new ShapePosition(
            "Back Road Scratch 1",
            new Vector3f(-1.5f, -0.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 2",
            new Vector3f(-1.5f, -1.0f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 3",
            new Vector3f(-1.5f, -1.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 4",
            new Vector3f(-1.5f, 0, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 5",
            new Vector3f(-1.5f, 0.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 6",
            new Vector3f(-1.5f, 1.0f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64))),
        new ShapePosition(
            "Back Road Scratch 7",
            new Vector3f(-1.5f, 1.5f, -0.8f),
            new Box(0.02f, 0.08f, 0.03f,
                ShapePositionManager.createAppeareance(Colors.White, false, true, Colors.White, 64)))
    };

    ShapePositionManager spm = new ShapePositionManager("Road", shapePositions);
    spm.inject(tg);
  }
}
