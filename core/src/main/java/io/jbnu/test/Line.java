package io.jbnu.test;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Line {
    public Vector2 left;
    public Vector2 right;
    float a;
    float b;

    public int idx;
    public Line neighborL = null;
    public Line neighborR = null;

    public Line(Vector2 _left, Vector2 _right, int _idx) {
        this.left = _left;
        this.right = _right;
        this.idx = _idx++;

        if(right.x == left.x) a = 1f;
        else a = (right.y - left.y) / (right.x - left.x);
        b = right.y - a * right.x;
    }

    public void DebugRender(ShapeRenderer _renderer) {
        _renderer.line(left, right);
    }

    public void TrySetNeighbor(Line _line) {
        if(left.epsilonEquals(_line.right)) {
            if(_line.neighborR != null || this.neighborL != null)throw new IllegalArgumentException("Line Reallocated " + _line.right);

            _line.neighborR = this;
            this.neighborL = _line;
        }
        else if(right.epsilonEquals(_line.left)) {
            if(_line.neighborL != null || this.neighborR != null)
                throw new IllegalArgumentException("Line Reallocated " + _line.left);

            _line.neighborL = this;
            this.neighborR = _line;
        }
    }

    public boolean WithinLine(float _fX) {
        return (left.x <= _fX && _fX <= right.x );
    }
    public boolean IsOnLeft(float _fX) {
        return _fX <= left.x;
    }

    public float GetHeight(float _fX) {
        return a * _fX + b;
    }
}
