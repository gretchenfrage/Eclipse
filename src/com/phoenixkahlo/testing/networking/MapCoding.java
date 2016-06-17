package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.phoenixkahlo.networking.CoderFactory;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.HashMapDecoder;
import com.phoenixkahlo.networking.HashMapEncoder;

public class MapCoding {

	public static void main(String[] args) throws Exception {
		EncodingProtocol encoder = new HashMapEncoder(CoderFactory.makeStringEncoder(), new FieldEncoder(Integer.class));//new PrimitiveEncoder(Integer.class));
		DecodingProtocol decoder = new HashMapDecoder(CoderFactory.makeStringDecoder(), new FieldDecoder(Integer.class, () -> 0));//new PrimitiveDecoder(Integer.class));
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("hello", null);
		map.put("hi", 2);
		map.put(null, 7);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(map, out);
		
		Object obj = decoder.decode(new ByteArrayInputStream(out.toByteArray()));
		System.out.println(obj);
	}

}
