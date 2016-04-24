/**
 * Created by William Bennett on 4/23/2016.
 */
public class Vector2 {
    public float X;
    public float Y;

    public Vector2() {
        X = 0;
        Y = 0;
    }

    public Vector2(float val) {
        X = Y = val;
    }

    public Vector2(float x, float y) {
        X = x;
        Y = y;
    }

    public float dotProduct(Vector2 other) {
        return X * other.X + Y * other.Y;
    }

    public float length() {
        return (float)Math.sqrt(X * X + Y * Y);
    }

    public float lengthSquared() {
        return X * X + Y * Y;
    }

    public Vector2 Add(Vector2 other) {
        return new Vector2(X + other.X, Y + other.Y);
    }

    public Vector2 Subtract(Vector2 other) {
        return new Vector2(X - other.X, Y - other.Y);
    }

    public Vector2 Multiply(Vector2 other) {
        return new Vector2(X * other.X, Y * other.Y);
    }

    public Vector2 Divide(Vector2 other) {
        return new Vector2(X / other.X, Y / other.Y);
    }

    public Vector2 Negate() {
        return new Vector2(-X, -Y);
    }

    public Vector2 Normalize() {
        float val = 1.0f / length();
        return new Vector2(X * val, Y  * val);
    }

    public String toString() {
        return Float.toString(X) + ", " + Float.toString(Y);
    }
}
