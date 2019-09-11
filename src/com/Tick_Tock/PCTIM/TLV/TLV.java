package com.Tick_Tock.PCTIM.Tlv;
import java.util.*;
import java.nio.*;
import com.Tick_Tock.PCTIM.Utils.*;

public class Tlv
{
	public int tag ;

	public byte[] value;

	public static List<Tlv> parseTlv(byte[] data)
	{
		ByteReader reader = new ByteReader(data);
		List<Tlv> tlvs = new ArrayList<Tlv>();
		while (reader.hasMore()){
			Tlv tlv = new Tlv();
			tlv.tag = reader.readShort();
			tlv.value = reader.readBytesByShortLength();
			tlvs.add(tlv);
		}
		reader.destroy();
		return tlvs;
	}
}
