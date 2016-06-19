package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.phoenixkahlo.networking.ArrayDecoder;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.ArrayListDecoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;

public class ListCoding {

	public static void main(String[] args) throws Exception {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(0, new ArrayEncoder(char.class));
		encoder.registerProtocol(1, new FieldEncoder(String.class, encoder));
		encoder.registerProtocol(2, new ArrayListEncoder(encoder));
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(0, new ArrayDecoder(char.class));
		decoder.registerProtocol(1, new FieldDecoder(String.class, String::new, decoder));
		decoder.registerProtocol(2, new  ArrayListDecoder(decoder));
		
		List<String> list1 = new ArrayList<String>();
		list1.add("hello");
		list1.add("world");
		list1.add("!");
		
		System.out.println(encoder.canEncode(list1));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(list1, out);
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		@SuppressWarnings("unchecked")
		List<String> list2 = (List<String>) decoder.decode(in);
		System.out.println(list2);
	}

}