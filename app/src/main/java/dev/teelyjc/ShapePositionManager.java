package dev.teelyjc;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

import dev.teelyjc.domains.ShapePosition;

public class ShapePositionManager {
  public static TransformGroup createTransformGroupTranslation(Vector3f v) {
    Transform3D transform = new Transform3D();
    transform.setTranslation(v);

    return new TransformGroup(transform);
  }

  public static TransformGroup createTransformGroupTranslation(Vector3f v, double x, double y, double z) {
    Transform3D transform = new Transform3D();
    transform.setTranslation(v);

    transform.rotX(x);
    transform.rotY(y);
    transform.rotZ(z);

    return new TransformGroup(transform);
  }

  public static Appearance createAppeareance(Color3f c) {
    Appearance appearance = new Appearance();
    if (c != null) {
      appearance.setColoringAttributes(
          new ColoringAttributes(c, ColoringAttributes.SHADE_GOURAUD));
    }

    return appearance;
  }

  public static Appearance createAppeareance(Color3f c, Boolean isTransparency) {
    Appearance appearance = new Appearance();
    if (c != null) {
      appearance.setColoringAttributes(
          new ColoringAttributes(c, ColoringAttributes.SHADE_GOURAUD));
    }

    if (isTransparency) {
      appearance.setTransparencyAttributes(
          new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.4f));
    }

    return appearance;
  }

  public static Appearance createAppeareance(
      Color3f c,
      Boolean isTransparency,
      Boolean isLightOn,
      Color3f specular,
      int shine) {
    Appearance appearance = new Appearance();
    if (c != null) {
      appearance.setColoringAttributes(
          new ColoringAttributes(c, ColoringAttributes.SHADE_GOURAUD));
    }

    if (isTransparency) {
      appearance.setTransparencyAttributes(
          new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.4f));
    }

    if (isLightOn) {
      Material material = new Material();
      material.setDiffuseColor(c);
      material.setSpecularColor(specular);
      material.setShininess(shine);

      appearance.setMaterial(material);
    }

    return appearance;
  }

  private final String name;
  private final ShapePosition[] shapePositions;

  public ShapePositionManager(String name, ShapePosition[] shapePositions) {
    this.name = name;
    this.shapePositions = shapePositions;
  }

  public String getName() {
    return this.name;
  }

  public void inject(TransformGroup tg) {
    for (ShapePosition sp : this.shapePositions) {
      Vector3f v = sp.getVector3f();
      Primitive p = sp.getPrimitive();

      TransformGroup transform = ShapePositionManager.createTransformGroupTranslation(v);
      transform.addChild(p);

      tg.addChild(transform);
    }
  }
}
