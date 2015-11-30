package com.android.ground.ground.custom;

import android.graphics.PointF;


/**
 * Created by Tacademy on 2015-11-30.
 */
public class CustomIntersection {

        private PointF roots;

        public static CustomIntersection newInstance() {
            return new CustomIntersection();
        }

        private CustomIntersection() {
            roots = new PointF();
        }

        public void circlesIntersection(CustomCircle c1, CustomCircle c2, PointF i1, PointF i2) {
            circlesIntersection(c1.getX(), c1.getY(), c1.getR(), c2.getX(), c2.getY(), c2.getR(), i1, i2);
        }

        public void circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2, PointF i1, PointF i2) {
            float x = cx2 - cx1;
            float y = cy2 - cy1;
            float q = (r1 * r1 - r2 * r2 + x * x + y * y) / 2;

            if (x != 0) {
                intersection(x, y, r1, q, true, i1, i2);
            } else if (y != 0) {
                intersection(y, x, r1, q, false, i1, i2);
            }

            i1.offset(cx1, cy1);
            i2.offset(cx1, cy1);
        }

        private void intersection(float x, float y, float r, float q, boolean order, PointF i1, PointF i2) {
            float a = x * x + y * y;
            float b = -2 * q * y;
            float c = q * q - r * r * x * x;

            quadraticRoots(a, b, c);

            float v1 = (q - roots.x * y) / x;
            float v2 = (q - roots.y * y) / x;

            if (order) {
                i1.set(v1, roots.x);
                i2.set(v2, roots.y);
            } else {
                i1.set(roots.x, v1);
                i2.set(roots.y, v2);
            }
        }

        private void quadraticRoots(float a, float b, float c) {
            float d = b * b - 4.0f * a * c;
            float aa = a + a;

            if (d < 0.0) {
                roots.set(-b / aa, -b / aa);
            } else if (b < 0.0) {
                float re = (float) ((-b + Math.sqrt(d)) / aa);
                if (re < c / (a * re))
                    roots.set(re, c / (a * re));
                else
                    roots.set(c / (a * re), re);
            } else {
                float re = (float) ((-b - Math.sqrt(d)) / aa);
                if (c / (a * re) < re)
                    roots.set(c / (a * re), re);
                else
                    roots.set(re, c / (a * re));
            }
        }

    }
