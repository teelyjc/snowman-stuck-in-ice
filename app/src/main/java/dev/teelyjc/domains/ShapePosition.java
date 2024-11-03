package dev.teelyjc.domains;

import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Primitive;

/**
 * @implNote เป็น Class ที่รวบรวม (Grouping primitives)
 *           เข้าด้วยกันเพื่อให้ง่ายต่อการจัดวางวัตถุ
 */
public class ShapePosition {
  private final String name;
  private final Vector3f v;
  private final Primitive p;

  public ShapePosition(String name, Vector3f v, Primitive p) {
    this.name = name;
    this.v = v;
    this.p = p;
  }

  public String getName() {
    return this.name;
  }

  public Vector3f getVector3f() {
    return this.v;
  }

  public Primitive getPrimitive() {
    return this.p;
  }
}
