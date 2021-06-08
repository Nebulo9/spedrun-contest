package fr.nebulo9.speedruncontest.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class CompressedFileActions {
	public static void download(String url,File file) throws IOException {
		ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
		FileOutputStream outStream = new FileOutputStream(file);
		FileChannel fileChannel = outStream.getChannel();
		
		fileChannel.transferFrom(fileChannel, 0, Long.MAX_VALUE);
	}
	
	public static void decompressGzip(File source,File target) throws IOException {
		Path sourcePath = source.toPath();
		Path targetPath = target.toPath();
		
		if(Files.notExists(sourcePath)) {
			throw new IOException();
		}
		
		try (InputStream inStream = Files.newInputStream(sourcePath);
			BufferedInputStream bufferIS = new BufferedInputStream(inStream);
			GzipCompressorInputStream compressorIS = new GzipCompressorInputStream(bufferIS);
			TarArchiveInputStream archiveIS = new TarArchiveInputStream(compressorIS)) {
			
			ArchiveEntry entry;
			while((entry = archiveIS.getNextEntry()) != null) {
				Path resolvedTargetDir = targetPath.resolve(entry.getName());
				Path normalizedPath = resolvedTargetDir.normalize();
				if(!normalizedPath.startsWith(targetPath)) {
					throw new IOException();
				}
				
				if(entry.isDirectory()) {
					Files.createDirectories(normalizedPath);
				} else {
					Path parent = normalizedPath.getParent();
					if(parent != null) {
						if(Files.notExists(parent)) {
							Files.createDirectories(parent);
						}
					}
					
					Files.copy(archiveIS, normalizedPath,StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}
}
