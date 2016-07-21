package com.phoenixkahlo.eclipse.world.entity;

import java.io.IOException;
import java.io.InputStream;

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
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.utils.ArrayUtils;

/**
 * Uses cornsnake to parse a ship from a text file. Made so that each individual type of ship 
 * need not have it's own class. 
 */
public class ParsedShip extends Ship {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(ParsedShip.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(ParsedShip.class, ParsedShip::new, subDecoder);
	}
	
	private FileResource schematic;
	
	private ParsedShip() {}
	
	public ParsedShip(FileResource schematic) {
		this.schematic = schematic;
		parse();
	}
	
	private void parse() {
		System.out.println("PARSING");
		try {
			ParseTable table = (ParseTable) Parser.parse(schematic.getFile());
			System.out.println(table);
			
			// Image
			ParseTable textureData = table.getTable("texture");
			System.out.println("textureData=" + textureData);
			Image texture = ArrayUtils.conditionSearch(ImageResource.values(),
					resource -> resource.getPath().equals(textureData.get("image"))).image();
			System.out.println("textureData=" + textureData);
			if (textureData.has("width") && textureData.has("height") && textureData.has("angle")) {
				injectTexture(
						texture,
						textureData.getFloat("width"),
						textureData.getFloat("height"),
						textureData.getFloat("angle")
						);
			} else if (textureData.has("width") && textureData.has("angle")) {
				injectTexture(
						texture,
						textureData.getFloat("width"),
						textureData.getFloat("angle")
						);
			} else if (textureData.has("width")) {
				injectTexture(
						texture,
						textureData.getFloat("width")
						);
			} else {
				System.out.println("Warning: texture not injected into " + this);
			}
			
			// Walls
			ParseList walls = table.getList("walls");
			if (walls != null)
				for (Polygon poly : parsePolygons(walls)) {
					addConvexFixture(poly);
				}
			else
				System.out.println("Warning: walls not created for " + this);
			
			// Floor
			ParseList floor = table.getList("floor");
			if (floor != null)
				for (Polygon poly : parsePolygons(floor)) {
					addArea(poly);
				}
			else
				System.out.println("Warning: floor not created for " + this);
			
			// Mass
			getBody().setMass(MassType.NORMAL);
			if (table.has("center"))
				getBody().setMass(new Mass(
						parseVector(table.getList("center")),
						getBody().getMass().getMass(),
						getBody().getMass().getInertia()
						));
			if (table.has("mass"))
				getBody().setMass(new Mass(
						getBody().getMass().getCenter(),
						table.getDouble("mass"),
						getBody().getMass().getInertia()
						));
			if (table.has("inertia"))
				getBody().setMass(new Mass(
						getBody().getMass().getCenter(),
						getBody().getMass().getMass(),
						table.getDouble("inertia")
						));
			
			// Thrust
			if (table.has("forward_thrust"))
				setForwardThrustMultiplier(table.getDouble("forward_thrust"));
			else
				System.out.println("Warning: forward thrust not set for " + this);
			if (table.has("strafe_thrust"))
				setStrafeThrustMultiplier(table.getDouble("strafe_thrust"));
			else
				System.out.println("Warning: strafe thrust not set for " + this);
			if (table.has("backward_thrust"))
				setBackwardThrustMultiplier(table.getDouble("backward_thrust"));
			else
				System.out.println("Warning: backward thrust not set for " + this);
			if (table.has("angular_thrust"))
				setAngularThrustMultiplier(table.getDouble("angularThrust"));
			else
				System.out.println("Warning: angular thrust not set for " + this);
			
			// Helm
			if (table.has("helm"))
				setHelmArea(parsePolygon(table.getList("helm")));
			if (table.has("helm_pos"))
				setHelmPos(parseVector(table.getList("helm_pos")));
			
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		System.out.println("ParsedShip.finishDecoding()");
		super.finishDecoding(in);
		parse();
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
