package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.Background;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetBackgroundEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetBackgroundEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetBackgroundEvent.class, SetBackgroundEvent::new, subDecoder);
	}
	
	private Background background;
	
	private SetBackgroundEvent() {}
	
	public SetBackgroundEvent(Background background) {
		this.background = background;
	}
	
	@Override
	public void accept(WorldState state) {
		state.setBackground(background);
	}

}
