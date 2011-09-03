package com.cached;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import com.amef.AMEFObject;
import com.amef.AMEFResources;

public class JDBOperations 
{ 	  
	
	public static void appendMove(SocketChannel sock,JdbNewDR reader,String ftable, String ttable, String pid, String nid,AMEFObject object)
	{
		
		AMEFObject query = new AMEFObject();
		query.addPacket("apmov "+ftable+" "+ttable+" "+pid+""+((nid!=null)?" "+nid:""));
		
		try
		{
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, true));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();			
			
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}		
	}
	    
	    
    public static void appendMovewrt(SocketChannel sock,JdbNewDR reader,String ftable, String ttable, String pid, String nid,AMEFObject object)
	{
		
		AMEFObject query = new AMEFObject();
		query.addPacket("apmovwrt "+ftable+" "+ttable+" "+pid+""+" "+nid);
		
		try
		{
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, true));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();			
			
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}		
	}
    
    public static void appendMovewrtpvap(SocketChannel sock,JdbNewDR reader,String ftable, String ttable, String pid, String nid,AMEFObject object,String ind)
	{
		
		AMEFObject query = new AMEFObject();
		query.addPacket("apmovwrtpvap "+ftable+" "+ttable+" "+pid+""+" "+nid+" "+ind);
		
		try
		{
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, true));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();			
			
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}		
	}
    
    public static void updateMovewrtpvap(SocketChannel sock,JdbNewDR reader,String ftable, String ttable, String pid, String nid,AMEFObject object,String ind)
	{
		
		AMEFObject query = new AMEFObject();
		query.addPacket("upmovwrtpvap "+ftable+" "+ttable+" "+pid+""+" "+nid+" "+ind);
		
		try
		{
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, false));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();			
			
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}		
	}
		
	public static void singleInsert(SocketChannel sock,JdbNewDR reader,String table, AMEFObject object,String id)
	{
		
		AMEFObject query = new AMEFObject();
		
		try
		{			
			query.addPacket("insert "+table+" "+id);
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, true));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			//System.out.println(new String(data,"ISO-8859-1"));
			buf.flip();
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}	
	}
	
	public static void singleUpdate(SocketChannel sock,JdbNewDR reader,String table, AMEFObject object,String id)
	{		
		AMEFObject query = new AMEFObject();		
		try
		{			
			query.addPacket("update "+table+" "+id);
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, false));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			//System.out.println(new String(data,"ISO-8859-1"));
			buf.flip();
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}	
	}
	
	public static void updateMove(SocketChannel sock,JdbNewDR reader,String ftable, String ttable, String pid, String nid,AMEFObject object)
	{
		
		AMEFObject query = new AMEFObject();
		query.addPacket("updmov "+ftable+" "+ttable+" "+pid+""+((nid!=null)?" "+nid:""));
		
		try
		{
			query.addPacket(AMEFResources.getEncoder().encodeWL(object, false));
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();			
			
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}		
	}
	
	
	public static AMEFObject select(SocketChannel sock, JdbNewDR reader, String table, String cond, String i,String lim)
	{
		AMEFObject obh = null;
		AMEFObject query = new AMEFObject();
		query.addPacket("select "+table+" "+cond+" "+i+((lim!=null)?" limit "+lim:""));
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			
			sock.write(buf);	
			reader.reset1();			
			outer: while(!reader.isReaderDone())
			{
				data = null;
				while(!reader.isDone())
				{	
					data = reader.readLim1(sock,1);
					if(reader.isReaderDone())
					{
						break outer;	
					}
				}
				reader.reset1();
				if(data!=null)
				obh = AMEFResources.getDecoder().decodeB(data, false, true);
				new WeakReference<byte[]>(data);
				data = null;
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}
		return obh;
	}
	
	public static String find(SocketChannel sock, JdbNewDR reader, String tables, String id)
	{
		AMEFObject obh = null;
		AMEFObject query = new AMEFObject();
		query.addPacket("find "+tables+" "+id);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			
			sock.write(buf);	
			reader.reset1();			
			outer: while(!reader.isReaderDone())
			{
				data = null;
				while(!reader.isDone())
				{	
					data = reader.readLim1(sock,1);
					if(reader.isReaderDone())
					{
						break outer;	
					}
				}
				reader.reset1();
				if(data!=null)
				obh = AMEFResources.getDecoder().decodeB(data, false, true);
				new WeakReference<byte[]>(data);
				data = null;
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}
		if(obh!=null && obh.getPackets().size()>0 && obh.getPackets().get(0)!=null)
			return obh.getPackets().get(0).getValueStr();
		return null;
	}
	
	public static long count(SocketChannel sock, JdbNewDR reader, String table)
	{
		AMEFObject obh = null;
		AMEFObject query = new AMEFObject();
		query.addPacket("count "+table);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			
			sock.write(buf);	
			reader.reset1();			
			outer: while(!reader.isReaderDone())
			{
				data = null;
				while(!reader.isDone())
				{
					data = reader.readLim1(sock,1);
					if(reader.isReaderDone())
					{
						break outer;	
					}
				}
				reader.reset1();
				if(data!=null)
				obh = AMEFResources.getDecoder().decodeB(data, false, true);
				new WeakReference<byte[]>(data);
				data = null;
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}
		if(obh==null || obh.getPackets().size()==0)
			return -1;
		else
			return obh.getPackets().get(0).getNumericValue();
	}
	
	
	public static void singleMove(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid, String nid)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("move "+ftable+" "+ttable+" "+pid+" "+nid);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void singleMovepvap(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid, String nid,String ind)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("movepvap "+ftable+" "+ttable+" "+pid+" "+nid+" "+ind);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void singleMovewrtpvap(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid, String nid,String ind)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("movewrtpvap "+ftable+" "+ttable+" "+pid+" "+nid+" "+ind);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void moveToTemp(SocketChannel sock, JdbNewDR reader, String ftable)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("mvtmp "+ftable);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void clearTmp(SocketChannel sock, JdbNewDR reader)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("clrtmp");
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	
	public static void removeField(SocketChannel sock, JdbNewDR reader, String ftable, String nid, int pos)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("remfld "+ftable+" = "+nid+" "+pos);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	
	public static void movelt(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("movelt "+ftable+" "+ttable+" "+pid);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void movegt(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid, String lim)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("movegt "+ftable+" "+ttable+" "+pid+(lim!=null?" limit "+lim:""));
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	public static void singleMoveWrt(SocketChannel sock, JdbNewDR reader, String ftable, String ttable, String pid, String nid)
	{
		reader.reset1();
		AMEFObject query = new AMEFObject();
		query.addPacket("movewrt "+ftable+" "+ttable+" "+pid+" "+nid);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
			reader.reset1();
		}
	}
	
	
	public static List<AMEFObject> selectMulti(SocketChannel sock, JdbNewDR reader, String table, String cond, String i,String lim)
	{
		List<AMEFObject> objects = new ArrayList<AMEFObject>();
		AMEFObject query = new AMEFObject();
		query.addPacket("select "+table+" "+cond+""+(cond.equals("*")?"":" "+i)+((lim!=null)?" limit "+lim:""));
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			long rdtim = 0,odtim = 0;
			sock.write(buf);			
			try
			{			
				reader.reset1();
				
				outer : while(!reader.isReaderDone())
				{
					data = null;
					while(!reader.isDone())
					{	
						data = reader.readLim1(sock,1);
						LockSupport.parkNanos(1);
						if(reader.isReaderDone())
							break outer;
					}
					reader.reset1();
					if(data!=null)
					{
						if(data[0]=='F')
						{
							break outer;
						}
						AMEFObject obh = AMEFResources.getDecoder().decodeB(data, false, true);
						objects.add(obh);
						new WeakReference<byte[]>(data);
						data = null;
					}
				}
				new WeakReference<ByteBuffer>(buf);
				buf = null;
			}			
			catch(Exception e)
			{}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}
		return objects;
	}
	
	public static List<AMEFObject> selectCount(SocketChannel sock, JdbNewDR reader, String table, String i,String lim)
	{
		List<AMEFObject> objects = new ArrayList<AMEFObject>();
		AMEFObject query = new AMEFObject();
		query.addPacket("selectcnt "+table+" "+i+" "+lim);
		
		try
		{
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);			
			try
			{			
				reader.reset1();
				
				outer : while(!reader.isReaderDone())
				{
					data = null;
					while(!reader.isDone())
					{	
						data = reader.readLim1(sock,1);
						LockSupport.parkNanos(1);
						if(reader.isReaderDone())
							break outer;
					}
					reader.reset1();
					if(data!=null)
					{
						if(data[0]=='F')
						{
							break outer;
						}
						AMEFObject obh = AMEFResources.getDecoder().decodeB(data, false, true);
						objects.add(obh);
						new WeakReference<byte[]>(data);
						data = null;
					}
				}
				new WeakReference<ByteBuffer>(buf);
				buf = null;
			}			
			catch(Exception e)
			{}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}
		return objects;
	}
	
	public static void multiDelete(SocketChannel sock,JdbNewDR reader,String table)
	{
		AMEFObject query = new AMEFObject();
		query.addPacket("delete "+table+" *");
		
		try
		{	
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}	
	}
	
	public static void singleDelete(SocketChannel sock,JdbNewDR reader,String table, String id)
	{
		AMEFObject query = new AMEFObject();
		query.addPacket("delete "+table+" = "+id);
		
		try
		{	
			byte[] data = AMEFResources.getEncoder().encodeB(query, false);
			query = null;
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			sock.write(buf);
			reader.reset1();
			while(!reader.isDone())
			{
				data = reader.readLim4(sock,2);
			}
			new WeakReference<ByteBuffer>(buf);
			buf = null;
			reader.reset1();
			new WeakReference<byte[]>(data);
			data = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			new WeakReference<AMEFObject>(query);
			query = null;
		}	
	}
}
