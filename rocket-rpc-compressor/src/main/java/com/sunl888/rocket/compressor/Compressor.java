package com.sunl888.rocket.compressor;


import com.sunl888.rocket.common.annotation.SPI;

@SPI(GzipCompressor.NAME)
public interface Compressor {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
