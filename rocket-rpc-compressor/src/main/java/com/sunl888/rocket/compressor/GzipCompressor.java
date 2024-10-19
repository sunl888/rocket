package com.sunl888.rocket.compressor;

import com.sunl888.rocket.common.annotation.Adaptive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Adaptive
public class GzipCompressor implements Compressor {
    public static final String NAME = "gzip";

    /**
     * 4k 缓冲区
     */
    private static final int BUFFER_SIZE = 4096;

    @Override
    public byte[] compress(byte[] bytes) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip 压缩失败", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip 解压失败", e);
        }
    }
}
