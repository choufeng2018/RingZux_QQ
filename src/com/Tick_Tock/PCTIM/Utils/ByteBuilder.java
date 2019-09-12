package com.Tick_Tock.PCTIM.Utils;
import io.netty.buffer.*;

public class ByteBuilder
{
	private ByteBuf byteBuffer;

    public ByteBuilder(){
		this.byteBuffer =Unpooled.directBuffer();
	}
	public void clean()
	{
		this.byteBuffer.release();
		this.byteBuffer=Unpooled.directBuffer();
		this.byteBuffer.writerIndex(0);
	}

	public void destroy(){
		this.byteBuffer.release();
	}
	
	public int totalcount(){
		return this.byteBuffer.readableBytes();
		
	}

	public int length()
	{
		return this.byteBuffer.writerIndex();
	}

	public byte[] getDataAndDestroy()
	{
		byte[] data = new byte[this.length()];
		this.byteBuffer.getBytes(0,data);
		this.destroy();
		return data;
	}

	public byte[] getDataAndDontDestroyAndClean()
	{
		byte[] data = new byte[this.length()];
		this.byteBuffer.getBytes(0,data);
		this.clean();
		return data;
	}

	public byte[] getDataAndDontDestroy()
	{
		byte[] data = new byte[this.length()];
		this.byteBuffer.getBytes(0,data);
		return data;
	}

	public ByteBuf getBuf(){
		return this.byteBuffer;
	}

	private byte[] selfGetData()
	{
		byte[] data = new byte[this.length()];
		this.byteBuffer.getBytes(0,data);
		return data;
	}

	public ByteBuilder writeBytes(byte[] to_write)
	{
		this.byteBuffer.writeBytes(to_write);
		return this;
	}


	public ByteBuilder writeBytes(String to_write)
	{
		this.writeBytes(to_write.getBytes());
		return this;
	}

	public ByteBuilder writeBytes(ByteBuf to_write)
	{
		this.byteBuffer.writeBytes(to_write);
		return this;
	}

	public ByteBuilder writeInt(long to_write)
	{
		this.byteBuffer.writeInt((int)to_write);
		return this;
	}
	public ByteBuilder writeShort(int to_write)
	{
		this.byteBuffer.writeShort((short)to_write);
		return this;
	}

	public ByteBuilder writeByte(byte to_write)
	{
		this.byteBuffer.writeByte(to_write);
		return this;
	}

	public ByteBuilder rewriteShort(int to_write)
	{
		byte[] data = this.selfGetData();
		this.byteBuffer.writerIndex(0);
		this.byteBuffer.writeShort(to_write);
		this.byteBuffer.writeBytes(data);
		return this;
	}

	public ByteBuilder rewriteByte(byte to_write)
	{
		byte[] data = this.selfGetData();
		this.byteBuffer.writerIndex(0);
		this.byteBuffer.writeByte(to_write);
		this.byteBuffer.writeBytes(data);
		return this;
	}

	public ByteBuilder rewriteInt(int to_write)
	{
		byte[] data = this.selfGetData();
		this.byteBuffer.clear();
		this.byteBuffer.writeInt(to_write);
		this.byteBuffer.writeBytes(data);
		return this;
	}

	public ByteBuilder rewriteBytes(byte[] to_write)
	{
		byte[] data = this.selfGetData();
		this.byteBuffer.writerIndex(0);
		this.byteBuffer.writeBytes(to_write);
		this.byteBuffer.writeBytes(data);
		return this;
	}

	public ByteBuilder writeBytesByShortLength(byte[] to_write)
	{
		this.writeShort(to_write.length);
		this.writeBytes(to_write);
		return this;
	}

	public ByteBuilder rewriteSelfShortLength(int i)
	{
		rewriteShort((short)(this.length() + i));
		return this;
	}

	public ByteBuilder rewriteSelfIntLength(int i)
	{
		rewriteInt((this.length() + i));
		return this;
	}

	public ByteBuilder j(int paramInt1, int paramInt2)
	{
		b(paramInt1, paramInt2);
		return this;
	}

	public ByteBuilder j(int paramInt, long paramLong)
	{
		b(paramInt, paramLong);
		return this;
	}

	public ByteBuilder j(int paramInt, String paramString)
	{
		a(paramInt, paramString.getBytes());
		return this;
	}

	public ByteBuilder j(int paramInt, byte[] paramArrayOfByte)
	{
		a(paramInt, paramArrayOfByte);
		return this;
	}

	private void a(int paramInt, byte... paramVarArgs)
	{
		c(paramInt << 3 | 0x2);
		c(paramVarArgs.length);
		this.byteBuffer.writeBytes(paramVarArgs);
	}

	private void b(int paramInt, long paramLong)
	{
		c(paramInt << 3 | 0x0);
		c(paramLong);
	}

	private void c(long j)
	{
        long abs = Math.abs(j);
        do {
            this.byteBuffer.writeByte((byte) ((int) ((127 & abs) | 128)));
            abs >>= 7;
        } while (abs != 0);
		this.byteBuffer.readerIndex(this.byteBuffer.readableBytes()-1);
		byte y =this.byteBuffer.readByte();
		this.byteBuffer.readerIndex(0);
		this.byteBuffer.writerIndex(this.byteBuffer.readableBytes()-1);
		this.byteBuffer.writeByte(y&127);
    }
}

