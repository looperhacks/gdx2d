package hevs.gdx2d.components.graphics;

import hevs.gdx2d.components.geometry.Vector2D;
import hevs.gdx2d.lib.utils.Utils;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;

/**
 * A polygon class for rendering stuff.
 * 
 * TODO: implement {@link com.badlogic.gdx.math.Polygon} in order to be able to
 * scale, translate and rotate the polygon.
 * 
 * @author Nils Chatton (chn)
 * @author Christopher Métrailler (mei)
 * @version 2.0
 */
public class Polygon {

	private float[] vertices;
	private float[] triangulatedVertices;
	public Array<Vector2> vectorList;
	private EarClippingTriangulator ect;
	private ShortArray earClippedVertices;
	private float[] gdxpoints;

	/**
	 * Create a polygon.
	 * 
	 * Contains methods to draw it filled or not.
	 * 
	 * @param points polygon points (either clockwise or counterclockwise order)
	 */
	public Polygon(Vector2D[] points) {
		Utils.callCheckExcludeGraphicRender();

		// Convert Vector2D array to a float array
		gdxpoints = new float[points.length * 2];
		vectorList = new Array<Vector2>();

		for (int i = 0, j = 0; i < points.length; i++, j += 2) {
			gdxpoints[j] = new Float(points[i].x);
			gdxpoints[j + 1] = new Float(points[i].y);
			// Save vertices
			vectorList.add(new Vector2(points[i].x, points[i].y));
		}

		// Compute vertices
		vertices = new float[points.length * 2];
		for (int i = 0; i < points.length; i++) {
			vertices[2 * i] = points[i].x;
			vertices[(2 * i) + 1] = points[i].y;
		}

		// Compute triangles in the polygon
		ect = new EarClippingTriangulator();
		earClippedVertices = ect.computeTriangles(gdxpoints);

		triangulatedVertices = new float[earClippedVertices.size * 2];
		for (int i = 0, j = 0; i < earClippedVertices.size; j += 2, i++) {
			// Save coordinates from indexes for each triangles
			final int index = earClippedVertices.get(i) * 2;
			triangulatedVertices[j] = gdxpoints[index];
			triangulatedVertices[j + 1] = gdxpoints[index + 1];
		}
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getEarClippedVertices() {
		// Useful to draw filled polygons
		return triangulatedVertices;
	}

	/**
	 * Check if a point is in the polygon. Use the math Intersector of Gdx.
	 * 
	 * @param p
	 *            point coordinates
	 * @return true if the point is in the polygon
	 */
	public boolean contains(Vector2D p) {
		boolean collides = Intersector.isPointInPolygon(vectorList,
				new Vector2(p.x, p.y));
		return collides;
	}
}