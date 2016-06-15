package com.phoenixkahlo.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.phoenixkahlo.networkingcore.CoderFactory;
import com.phoenixkahlo.networkingcore.DecodingProtocol;
import com.phoenixkahlo.networkingcore.EncodingProtocol;
import com.phoenixkahlo.networkingcore.FieldDecoder;
import com.phoenixkahlo.networkingcore.FieldEncoder;
import com.phoenixkahlo.networkingcore.HashMapDecoder;
import com.phoenixkahlo.networkingcore.HashMapEncoder;

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
