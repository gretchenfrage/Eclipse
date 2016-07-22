package com.phoenixkahlo.eclipse.world.entity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Image;

import com.phoenixkahlo.cornsnake.ParseException;
import com.phoenixkahlo.cornsnake.ParseList;
import com.phoenixkahlo.cornsnake.ParseTable;
import com.phoenixkahlo.cornsnake.Parser;
import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.utils.ArrayUtils;

/**
 * Each ParsedShipTemplate is derived from a cornsnake file and can be imposed on a ship which 
 * will bring the ship to the schematic's specifications. The cornsnake file should contain a 
 * table, which may contain various elements. The master table may contains a table "texture", 
 * which must contain a string "image" representing the short path of an ImageResource, and 
 * a number "width". The "texture" table may contains numbers "height" and "angle". The master 
 * table may contain "walls" and "floor", both lists of polygons (expressed as a list of 
 * vector2s (expressed as a list of two numbers)). The master table may contain numbers "mass" 
 * and "inertia", and a vector2 "center" (of mass). The master table may contain numbers 
 * "foward_thrust", "backward_thrust", "strafe_thrust", and "angular_thrust". The master table 
 * may contain a polygon "helm" and a vector2 "helm_pos". 
 */
public enum ParsedShipTemplate {

	BASIC_SHIP_1("basic_ship_1");
	
	private List<Consumer<Ship>> consumers = new ArrayList<Consumer<Ship>>();
	
	ParsedShipTemplate(String path) {
		try {
			File file = new File(ParsedShipTemplate.class.getClassLoader().getResource(
					"resources/schematics/" + path + ".cos").toURI());
			ParseTable table = (ParseTable) Parser.parse(file);
			
			// Image
			ParseTable textureData = table.getTable("texture");
			Image texture = ArrayUtils.conditionSearch(ImageResource.values(),
					resource -> resource.getPath().equals(textureData.get("image"))).image();
			if (textureData.has("width") && textureData.has("height") && textureData.has("angle")) {
				consumers.add(ship -> ship.injectTexture(
						texture,
						textureData.getFloat("width"),
						textureData.getFloat("height"),
						textureData.getFloat("angle")
						));
			} else if (textureData.has("width") && textureData.has("angle")) {
				consumers.add(ship -> ship.injectTexture(
						texture,
						textureData.getFloat("width"),
						textureData.getFloat("angle")
						));
			} else if (textureData.has("width")) {
				consumers.add(ship -> ship.injectTexture(
						texture,
						textureData.getFloat("width")
						));
			} else {
				System.out.println("Warning: texture not injected into " + this);
			}
			
			// Walls
			ParseList walls = table.getList("walls");
			if (walls != null)
				for (Polygon poly : parsePolygons(walls)) {
					consumers.add(ship -> ship.addConvexFixture(poly));
				}
			else
				System.out.println("Warning: walls not created for " + this);
			
			// Floor
			ParseList floor = table.getList("floor");
			if (floor != null)
				for (Polygon poly : parsePolygons(floor)) {
					consumers.add(ship -> ship.addArea(poly));
				}
			else
				System.out.println("Warning: floor not created for " + this);
			
			// Mass
			consumers.add(ship -> ship.getBody().setMass(MassType.NORMAL));
			if (table.has("center"))
				consumers.add(ship -> ship.getBody().setMass(new Mass(
						parseVector(table.getList("center")),
						ship.getBody().getMass().getMass(),
						ship.getBody().getMass().getInertia()
						)));
			if (table.has("mass"))
				consumers.add(ship -> ship.getBody().setMass(new Mass(
						ship.getBody().getMass().getCenter(),
						table.getDouble("mass"),
						ship.getBody().getMass().getInertia()
						)));
			if (table.has("inertia"))
				consumers.add(ship -> ship.getBody().setMass(new Mass(
						ship.getBody().getMass().getCenter(),
						ship.getBody().getMass().getMass(),
						table.getDouble("inertia")
						)));
			
			// Thrust
			if (table.has("forward_thrust"))
				consumers.add(ship -> ship.setForwardThrustMultiplier(table.getDouble("forward_thrust")));
			else
				System.out.println("Warning: forward thrust not set for " + this);
			if (table.has("strafe_thrust"))
				consumers.add(ship -> ship.setStrafeThrustMultiplier(table.getDouble("strafe_thrust")));
			else
				System.out.println("Warning: strafe thrust not set for " + this);
			if (table.has("backward_thrust"))
				consumers.add(ship -> ship.setBackwardThrustMultiplier(table.getDouble("backward_thrust")));
			else
				System.out.println("Warning: backward thrust not set for " + this);
			if (table.has("angular_thrust"))
				consumers.add(ship -> ship.setAngularThrustMultiplier(table.getDouble("angular_thrust")));
			else
				System.out.println("Warning: angular thrust not set for " + this);
			
			// Helm
			if (table.has("helm"))
				consumers.add(ship -> ship.setHelmArea(parsePolygon(table.getList("helm"))));
			if (table.has("helm_pos"))
				consumers.add(ship -> ship.setHelmPos(parseVector(table.getList("helm_pos"))));
		} catch (URISyntaxException | ParseException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void apply(Ship ship) {
		consumers.forEach(consumer -> consumer.accept(ship));
	}
	
	private static Polygon[] parsePolygons(ParseList data) {
		Polygon[] polygons = new Polygon[data.size()];
		for (int i = 0; i < data.size(); i++) {
			polygons[i] = parsePolygon(data.getList(i));
		}
		return polygons;
	}
	
	private static Polygon parsePolygon(ParseList data) {
		Vector2[] vertices = new Vector2[data.size()];
		for (int i = 0; i < data.size(); i++) {
			vertices[i] = parseVector(data.getList(i));
		}
		return new Polygon(vertices);
	}
	
	private static Vector2 parseVector(ParseList data) {
		return new Vector2(data.getDouble(0), data.getDouble(1));
	}
	
}
