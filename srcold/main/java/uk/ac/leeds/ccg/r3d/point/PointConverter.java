/*
 * Copyright 2021 Centre for Computational Geography.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.r3d.point;

import java.awt.Point;

import uk.ac.leeds.ccg.r3d.Display;

public class PointConverter {

    private static double scale = 4;
    private static final double ZoomFactor = 1.2;

    public static void zoomIn() {
        scale *= ZoomFactor;
    }

    public static void zoomOut() {
        scale /= ZoomFactor;
    }

    /**
     * Convert a 3D point into a 2D point.
     * @param point3D The 3D point to convert.
     * @return The 2D screen point.
     */
    public static Point convertPoint(R3D_PointDoubleOffset point3D) {
        double x3d = point3D.getAdjustedY() * scale;
        double y3d = point3D.getAdjustedZ() * scale;
        double depth = point3D.getAdjustedX() * scale;
        double[] newVal = scale(x3d, y3d, depth);
        int x2d = (int) (Display.WIDTH / 2 + newVal[0]);
        int y2d = (int) (Display.HEIGHT / 2 - newVal[1]);
        Point point2D = new Point(x2d, y2d);
        return point2D;
    }

    /**
     * Ads depth perception.
     * @param x3d
     * @param y3d
     * @param depth
     * @return 
     */
    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt(x3d * x3d + y3d * y3d);
        double theta = Math.atan2(y3d, x3d);
        double depth2 = 15 - depth;
        double localScale = Math.abs(1400 / (depth2 + 1400));
        dist *= localScale;
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(theta);
        newVal[1] = dist * Math.sin(theta);
        return newVal;
    }

    public static void rotateAxisX(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        if (p instanceof MorphPoint) {
            MorphPoint morphPoint = (MorphPoint) p;
            rawRotateAxisX(morphPoint.p1, CW, degrees);
            rawRotateAxisX(morphPoint.p2, CW, degrees);
        }
        rawRotateAxisX(p, CW, degrees);
    }

    public static void rotateAxisY(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        if (p instanceof MorphPoint) {
            MorphPoint morphPoint = (MorphPoint) p;
            rawRotateAxisY(morphPoint.p1, CW, degrees);
            rawRotateAxisY(morphPoint.p2, CW, degrees);
        }
        rawRotateAxisY(p, CW, degrees);
    }

    public static void rotateAxisZ(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        if (p instanceof MorphPoint) {
            MorphPoint morphPoint = (MorphPoint) p;
            rawRotateAxisZ(morphPoint.p1, CW, degrees);
            rawRotateAxisZ(morphPoint.p2, CW, degrees);
        }
        rawRotateAxisZ(p, CW, degrees);
    }

    private static void rawRotateAxisX(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y * p.y + p.z * p.z);
        double theta = Math.atan2(p.z, p.y);
        theta -= 2 * Math.toRadians(degrees);
        p.y = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }

    private static void rawRotateAxisY(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.x * p.x + p.z * p.z);
        double theta = Math.atan2(p.x, p.z);
        theta -= 2 * Math.toRadians(degrees);
        p.x = radius * Math.sin(theta);
        p.z = radius * Math.cos(theta);
    }

    private static void rawRotateAxisZ(R3D_PointDoubleOffset p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y * p.y + p.x * p.x);
        double theta = Math.atan2(p.y, p.x);
        theta -= 2 * Math.toRadians(degrees);
        p.y = radius * Math.sin(theta);
        p.x = radius * Math.cos(theta);
    }

}