/*
 * Created on Jul 23, 2007 at 10:53:12 AM.
 * 
 * Copyright (c) 2007 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */
package de.enough.polish.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.enough.polish.util.zip.GZipInputStream;
import de.enough.polish.util.zip.GZipOutputStream;

/**
 * <p></p>
 *
 * <p>Copyright Enough Software 2007 - 2008</p>
 * <pre>
 * history
 *        Jul 23, 2007 - Simon creation
 * </pre>
 * @author Simon Schmitt, simon.schmitt@enough.de
 */
public final class ZipUtil {
	/**
	 *  This function decompresses a given byte array using the DEFLATE algorithm.
	 *  
	 * @param data		the input data to be decompressed
	 * @return			the resulting uncompressed byte array 
	 * @throws IOException
	 */
	public static byte[] decompress( byte[] data ) throws IOException{
		return decompress(data,GZipInputStream.TYPE_DEFLATE);
	}
	/**
	 * This function decompresses a given byte array using the DEFLATE or the GZIP algorithm.
	 * 
	 * @param data		the input data to be decompressed
	 * @param compressionType TYPE_GZIP or TYPE_DEFLATE
	 * @return			the resulting uncompressed byte array 
	 * @throws IOException
	 */
	public static byte[] decompress( byte[] data , int compressionType) throws IOException{
		byte[] tmp=new byte[1024];
		int read;
		
		GZipInputStream zipInputStream = new GZipInputStream(new ByteArrayInputStream( data ) ,1024 ,compressionType,true);
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		
		// take from ZipInputStream and fill into ByteArrayOutputStream
		while ( (read=zipInputStream.read(tmp, 0, 1024))>0 ){
			bout.write(tmp,0,read);
		}
		
		return bout.toByteArray();
	}
	/**
	 * This function compresses a given byte array using the DEFLATE algorithm.
	 * 
	 * @param data		the input data to be compressed
	 * @return			the resulting compressed byte array 
	 * @throws IOException
	 */
	public static byte[] compress( byte[] data ) throws IOException{
		return compress( data, GZipOutputStream.TYPE_DEFLATE );
	}
	/**
	 * This function compresses a given byte array using the DEFLATE algorithm.
	 * 
	 * @param data		the input data to be compressed
	 * @param compressionType	TYPE_GZIP or TYPE_DEFLATE
	 * @return			the resulting compressed byte array 
	 * @throws IOException
	 */
	public static byte[] compress( byte[] data, int compressionType ) throws IOException{
		if (data.length>1<<15){ 
			return compress(data,compressionType,1<<15,1<<15);
			
		} else{
			return compress(data,compressionType,data.length,data.length);
			
		}
	}
	/**
	 * This function compresses a given byte array using the DEFLATE algorithm.
	 * 
	 * @param data		the input data to be compressed
	 * @param compressionType	TYPE_GZIP or TYPE_DEFLATE
	 * @param plainWindowSize	this size is important for the lz77 search. Larger values
	 * 							will result in better compression. Maximum is 32768.
	 * @param huffmanWindowSize	this size is important for the huffmanencoding. A large
	 * 							value will result to a better frequency statistic and therefore to a better compression.
	 * @return					the resulting compressed byte array 
	 * @throws IOException
	 */
	public static byte[] compress( byte[] data , int compressionType,int plainWindowSize, int huffmanWindowSize) throws IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		GZipOutputStream zipOutputStream = new GZipOutputStream(bout ,1024 ,compressionType,plainWindowSize,huffmanWindowSize);
		
		zipOutputStream.write(data);
		zipOutputStream.close();
		
		return bout.toByteArray();
	}
	
}
